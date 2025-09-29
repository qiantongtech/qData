/**
 * 存放一些节点操作的公共方法
 */
import { DataUri, Shape } from "@antv/x6";
import { History } from "@antv/x6-plugin-history";
import { Export } from "@antv/x6-plugin-export";
import { Selection } from "@antv/x6-plugin-selection";
import "@/assets/system/styles/global.scss";
import { cuPort } from "@/utils/graph";
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import { getNodeUniqueKey } from "@/api/dpp/task/etlTask";
import { ElMessage } from "element-plus";
import { DagreLayout } from '@antv/layout';
import { register } from '@antv/x6-vue-shape';
import NodeView from "@/views/dpp/components/nodeView";
/**
 * 插件使用
 */
export const usePlugins = (graph) => {
  graph
    .use(
      new History({
        enabled: true,
      })
    )
    .use(
      new Selection({
        enabled: true,
        rubberband: true,
        showNodeSelectionBox: true,
      })
    )
    .use(new Export());
};
/**
 * 画布缩放比例
 * @param {*} graph
 * @returns
 */
export const getCanvasScale = (graph) => {
  const scaleValue = graph.zoom();
  let result = parseFloat(scaleValue * 100).toFixed(0);
  return result;
};
/**
 * 自定义html节点
 */
export const useHtmlNode = (node) => {
  Shape.HTML.register({
    shape: "cu-data-node",
    width: 180,
    height: 60,
    html(cell) {
      const { name: nodeName, createPerson, icon, length, releaseState, taskParams } = cell.getData();
      const htmlContainer = document.createElement("div");
      htmlContainer.setAttribute("class", "cu_html_container");
      const htmlTop = document.createElement("img");
      htmlTop.setAttribute("class", "cu_html_top");
      // 确定 icon 来源
      let iconSrc = taskParams.icon || icon;
      // 检查 icon 是否是 base64，如果是则直接使用
      if (iconSrc && iconSrc.startsWith("data:image")) {
        htmlTop.setAttribute("src", iconSrc);
      } else if (iconSrc) {
        DataUri.imageToDataUri(iconSrc, function (nu, url) {
          htmlTop.src = url;
          // **将 base64 存回 taskParams.icon**
          const newData = {
            ...cell.getData(),
            taskParams: { ...taskParams, icon: url }, // 更新 taskParams.icon
          };
          cell.setData(newData);
        });
      }
      // 右侧的文本区域
      const htmlText = document.createElement("div");
      htmlText.setAttribute("class", "cu_html_text");
      // 标题
      const htmlTitle = document.createElement("div");
      htmlTitle.setAttribute("class", "cu_html_title");
      htmlTitle.innerText = nodeName;

      // 组合文字内容
      htmlText.appendChild(htmlTitle);
      // 组合整个节点
      htmlContainer.appendChild(htmlTop);
      htmlContainer.appendChild(htmlText);

      return htmlContainer;
    },
  });
};
// 自定义vue节点
export const useVueNode = (graph) => {
  register({
    shape: "vue-node", // 自定义节点类型
    component: NodeView,
    width: 36,
    height: 40,
    props: {
      // styletype: 2,
    },
  })
}
/**
 * 显示节点上的连接桩
 * @param {*} ports
 * @param {*} show
 */
export const showPorts = (ports, show) => {
  for (let i = 0, len = ports.length; i < len; i = i + 1) {
    ports[i].style.visibility = show ? "visible" : "hidden";
  }
};
export const renderGraphs = async (graph, savedData, styletype = 1) => {
  console.log("🚀 ~ renderGraphs ~ styletype:", styletype)
  if (!graph) {
    console.warn("renderGraph: graph 不存在");
    return;
  }

  if (!savedData) {
    console.warn("renderGraph: savedData 不存在");
    graph.clearCells();
    return;
  }

  // 不清空 graph，避免重复渲染
  // graph.clearCells();

  const taskList = Array.isArray(savedData.taskDefinitionList) ? savedData.taskDefinitionList : [];
  const relations = Array.isArray(savedData.taskRelationJson) ? savedData.taskRelationJson : [];

  // 准备节点和边
  const layoutNodes = taskList.map((task) => ({
    id: String(task.code), // 强制转成字符串
    width: 36,
    height: 40,
    data: task,
  }));

  const layoutEdges = relations
    .filter(
      (rel) =>
        rel &&
        rel.preNodeCode != null &&
        rel.postNodeCode != null &&
        String(rel.preNodeCode) !== '0'
    )
    .map((rel) => ({
      source: String(rel.preNodeCode),
      target: String(rel.postNodeCode),
    }));

  // Dagre 布局
  const dagreLayout = new DagreLayout({
    type: 'dagre',
    rankdir: 'LR',
    nodesep: 50,
    ranksep: 50,
  });

  dagreLayout.layout({
    nodes: layoutNodes,
    edges: layoutEdges,
  });

  // 添加节点（去重）
  layoutNodes.forEach((n) => {
    if (!graph.getCellById(n.id)) {
      graph.addNode({
        id: n.id,
        shape: 'vue-node',
        component: NodeView,
        x: n.x || 0,
        y: n.y || 0,
        width: n.width,
        height: n.height,
        data: { ...n.data, styletype },
        attrs: {
          body: { stroke: '#D3D8EA', strokeWidth: 1 },
        },
        ports: {
          ...cuPort,
          items: [
            { group: 'left', id: 'port-left' },
            { group: 'right', id: 'port-right' },
          ],
        },
      });
    }
  });

  // 添加边（去重）
  layoutEdges.forEach((e) => {
    const edgeId = `${e.source}-${e.target}`; // 用 source-target 做唯一 id
    if (!graph.getCellById(edgeId)) {
      const sourceNode = graph.getCellById(e.source);
      const targetNode = graph.getCellById(e.target);
      if (!sourceNode || !targetNode) return;

      graph.addEdge({
        id: edgeId,
        source: { cell: e.source, port: 'port-right' },
        target: { cell: e.target, port: 'port-left' },
        attrs: {
          line: {
            stroke: '#D3D8EA',
            strokeWidth: 1,
            targetMarker: { name: 'block', width: 12, height: 8 },
          },
        },
      });
    }
  });
};

// 更新节点状态
export const updateGraphNodes = (graph, nodeInstanceList) => {
  if (!graph || !Array.isArray(nodeInstanceList)) return;
  const codeNodeMap = {};
  graph.getNodes().forEach((node) => {
    const code = node.getData()?.code;
    if (code) codeNodeMap[String(code)] = node;
  });
  nodeInstanceList.forEach((inst) => {
    const node = codeNodeMap[String(inst.nodeCode)];
    if (node) {
      const oldData = node.getData() || {};
      node.setData({ ...oldData, status: inst.status });
      console.log("🚀 ~ updateGraphNodes ~ inst.status:", inst.status)
    }
  });
};
/**
 * 画布清空
 */
export const handleRmNodes = (graph) => {
  graph.clearCells();
};

//输出组件字段处理
export const handleType2TaskParams = (fromColumns, toColumns) => {
  // 目标列和源列数组
  const target_columns = [];
  const columns = [];

  // 获取最短的数组长度，避免索引越界
  const minLength = Math.min(fromColumns.length, toColumns.length);

  // 遍历 fromColumns 和 toColumns
  for (let i = 0; i < minLength; i++) {
    const fromCol = fromColumns[i];
    const toCol = toColumns[i];

    // 仅当两者都被选中时，加入对应列
    if (fromCol.isChecked && toCol.isChecked) {
      target_columns.push(toCol.columnName); // 加入目标列
      columns.push(fromCol.columnName); // 加入源列
    }
  }

  // 返回结果对象
  return {
    target_columns,
    columns,
  };
};

// 主函数：根据不同的type来处理任务参数
export const transNodeData = async (graph) => {
  const allNodes = JSON.parse(JSON.stringify(graph.getNodes()));
  const allEdges = JSON.parse(JSON.stringify(graph.getEdges()));
  // 处理节点数据
  const tailNodes = allEdges.reduce((acc, edge) => {
    acc[edge.target.cell] = true;
    return acc;
  }, {});
  const isHeadNode = (code) => !tailNodes[code];

  const locations = [];
  const tasksMap = {};
  const taskDefinitionList = [];
  const sortedNodes = allNodes
    .filter((node) => node.shape === "cu-data-node")
    .sort((a, b) => {
      if (a.data?.taskParams?.type == 1 && b.data?.taskParams?.type != 1) return -1;
      if (a.data?.taskParams?.type != 1 && b.data?.taskParams?.type == 1) return 1;
      if (a.data?.taskParams?.type == 2) return 1;
      if (b.data?.taskParams?.type == 2) return -1;
      return 0;
    });
  // 处理节点
  // 构建 tasksMap
  for (const item of sortedNodes) {
    if (item.shape === "cu-data-node") {
      const code = item.id;
      tasksMap[code] = item.data;
    }
  }
  // 处理节点
  for (const item of sortedNodes) {
    if (item.shape === "cu-data-node") {
      const code = item.id;
      locations.push({
        taskCode: item.data.code,
        x: item.position.x,
        y: item.position.y,
      });
      taskDefinitionList.push({ ...item.data });
    }
  }

  // 处理任务关系
  const taskRelationJson = [];

  // 处理所有节点
  allNodes.forEach((node) => {
    if (isHeadNode(node.id)) {
      // 从 tasksMap 获取节点对应的任务
      const task = tasksMap[node.id];
      taskRelationJson.push({
        name: "",
        preTaskCode: 0,
        preTaskVersion: 0,
        postTaskCode: task?.code || 0,
        postTaskVersion: task?.version || 0,
        conditionType: "NONE",
        conditionParams: {},
      });
    }
  });
  // 处理所有边
  allEdges.forEach((item) => {
    if (item.shape === "edge") {
      const sourceId = item.source.cell;
      const prevTask = tasksMap[sourceId];
      const targetId = item.target.cell;
      const task = tasksMap[targetId] || "";
      taskRelationJson.push({
        name: "",
        preTaskCode: prevTask?.code || 0,
        preTaskVersion: prevTask?.version || 0,
        postTaskCode: task?.code || 0,
        postTaskVersion: task?.version || 0,
        conditionType: "NONE",
        conditionParams: {},
      });
    }
  });
  return {
    locations,
    taskRelationJson,
    taskDefinitionList,
  };
};
function getAllConnectedEdges(graph, node) {
  const visited = new Set();
  const queue = [];
  const edges = [];
  let currentNode;
  // 将起始节点加入队列并标记为已访问
  queue.push(node);
  visited.add(node);

  while (queue.length > 0) {
    currentNode = queue.shift();
    // 获取当前节点的所有直接相连的边并加入结果数组中
    const connectedEdges = graph.getConnectedEdges(currentNode);
    connectedEdges.forEach((edge) => {
      if (!edges.includes(edge)) {
        // 避免重复添加相同的边
        edges.push(edge);
      }
    });
    // 将当前节点的所有相邻节点加入队列（如果它们未被访问过）
    const adjacentNodes = graph.getNeighbors(currentNode);
    adjacentNodes.forEach((adjacentNode) => {
      if (!visited.has(adjacentNode)) {
        visited.add(adjacentNode);
        queue.push(adjacentNode);
      }
    });
  }
  return edges;
}
export const validateGraph = (graph, flag) => {
  const nodes = graph.getNodes(); // 获取所有节点
  const edges = graph.getEdges(); // 获取所有边
  let valid = true;
  let errorMessages = [];

  if (nodes.length === 0) {
    const msg = "当前任务缺少输入、转换、输出组件，请设置相关组件";
    if (!flag) ElMessage.warning(msg);
    return { isValid: false, errorMessages: [msg] };
  }

  let inputNodeExists = false;
  let outputNodeExists = false;

  const addErrorMessage = (message) => {
    errorMessages.push(message);
  };

  // 表输出组件校验（type == 2）
  const validateType2TaskParams = (taskParams, node) => {
    if (!taskParams.tableFields || taskParams.tableFields.length === 0) {
      valid = false;
      addErrorMessage(`${node.data.name} 表输出组件未进行字段映射，请设置字段映射`);
    } else {
      let { target_columns = [], columns = [] } = handleType2TaskParams(taskParams.tableFields, taskParams.toColumnsList);
      if (target_columns.length === 0 || columns.length === 0) {
        valid = false;
        addErrorMessage(`${node.data.name} 表输出组件未进行字段映射，请设置字段映射`);
      }
    }
  };



  // 所有节点循环校验
  nodes.forEach((node) => {
    const { data } = node;
    const taskParams = data?.taskParams;
    const componentType = data?.componentType;

    if (!taskParams) return;
    if (componentType == 41) {
      if (!taskParams.sequenceFieldName) {
        valid = false;
        addErrorMessage(`${data.name} 节点信息不完善，请完善`);
      }
      return;
    }
    if (componentType == 44 || componentType == 45) {
      if (!taskParams.plaintextField) {
        valid = false;
        addErrorMessage(`${data.name} 节点信息不完善，请完善`);
      }
      return;
    }
    if (!Array.isArray(taskParams.tableFields) || taskParams.tableFields.length == 0) {
      valid = false;
      addErrorMessage(`${data.name} 节点信息不完善，请完善`);
      return;
    }
    // 特定类型组件额外校验
    if (taskParams.type == "2") {
      validateType2TaskParams(taskParams, node);
    }
    // 标记输入/输出组件
    if (taskParams.type == "1") inputNodeExists = data;
    if (taskParams.type == "2") outputNodeExists = data;
  });
  if (!inputNodeExists && !outputNodeExists) {
    valid = false;
    addErrorMessage("当前任务缺少输入、输出组件，请设置输入、输出节点");
  } else if (!inputNodeExists) {
    valid = false;
    addErrorMessage("当前任务缺少输入组件，请设置输入节点");
  } else if (!outputNodeExists) {
    valid = false;
    addErrorMessage("当前任务缺少输出组件，请设置输出节点");
  }


  if (errorMessages.length > 0 && !flag) {
    ElMessage.warning(errorMessages[0]);
  }

  return { isValid: errorMessages.length === 0, errorMessages };
};

/**
 * 使用 graph.fromJSON 还原transNodeData处理过的数据流程图画布
 */
export const renderGraph = (graph, savedData, width) => {
  if (!graph) {
    console.warn("renderGraph: graph 不存在");
    return;
  }

  if (!savedData) {
    console.warn("renderGraph: savedData 不存在");
    graph.clearCells();
    return;
  }

  graph.clearCells();

  const locations = Array.isArray(savedData.locations) ? savedData.locations : [];
  const taskList = Array.isArray(savedData.taskDefinitionList) ? savedData.taskDefinitionList : [];
  const relations = Array.isArray(savedData.taskRelationJson) ? savedData.taskRelationJson : [];

  // 添加节点
  locations.forEach((location) => {
    const nodeData = taskList.find((item) => item.code == location.taskCode);
    if (nodeData) {
      graph.addNode({
        id: String(location.taskCode), // 确保 ID 为字符串
        shape: "cu-data-node",
        x: location.x,
        y: location.y,
        width: width || 170,
        height: 50,
        data: nodeData,
        ports: {
          ...cuPort,
          items: [
            { group: "top", id: "port-top" },
            { group: "bottom", id: "port-bottom" },
          ],
        },
      });
    }
  });

  // 添加边，添加前先检查节点是否存在
  relations.forEach((relation) => {
    const preId = String(relation?.preNodeCode);
    const postId = String(relation?.postNodeCode);

    if (!preId || !postId || preId === "0") return;

    const sourceNode = graph.getCellById(preId);
    const targetNode = graph.getCellById(postId);

    if (!sourceNode || !targetNode) {
      console.warn(`跳过无效边: source=${preId}, target=${postId}（节点不存在）`);
      return;
    }

    graph.addEdge({
      source: {
        cell: preId,
        port: "port-bottom",
      },
      target: {
        cell: postId,
        port: "port-top",
      },
      data: {
        sourceId: preId,
        targetId: postId,
      },
      attrs: {
        line: {
          stroke: "#2666FB",
          strokeWidth: 1,
          targetMarker: { name: "block", width: 12, height: 8 },
        },
      },
    });
  });
};
// 获取code
export const fetchNodeUniqueKey = async () => {
  try {
    const response = await getNodeUniqueKey({
      projectCode: userStore.projectCode || "133545087166112",
      projectId: userStore.projectId,
    });
    if (response.code == "200") {
      return response.data;
    }
    return null; // 如果没有数据，返回 null
  } catch (error) {
    return null; // 发生错误时返回 null
  }
};
// 获取上级节点 封装成下拉框
export const createNodeSelect = (graph, currentNodeId) => {
  return graph
    .getNodes()
    .filter((node) => node.id !== currentNodeId && node?.data?.taskParams?.type !== 2) // 过滤掉当前节点和 taskParams.type 为 2 的节点
    .map((node) => ({
      label: node.data.name || "未知节点",
      value: node.id,
    }));
};
// 当前节点的所有下级节点
export const getAllChildNodes = (node, graph) => {
  const outgoingEdges = graph.getOutgoingEdges(node);
  const childNodes = [];
  if (outgoingEdges) {
    outgoingEdges.forEach((edge) => {
      const childNode = edge.getTargetCell();
      if (childNode) {
        childNodes.push(childNode);
        const grandChildren = getAllChildNodes(childNode, graph);
        childNodes.push(...grandChildren);
      }
    });
  }
  return childNodes;
};
// 找到当前节点的父节点
export function getParentNode(currentNode, graph) {
  if (!currentNode || !graph) return null;

  const incomingEdges = graph.getIncomingEdges(currentNode) || [];
  if (incomingEdges.length === 0) return null;

  const parentEdge = incomingEdges[0]; // 默认只取第一个入边
  const parentNode = parentEdge?.getSourceCell?.();

  return parentNode?.isNode?.() ? parentNode : null;
}
export const createDataNode = (graph, data) => {
  return graph.createNode({
    shape: "cu-data-node",
    width: 180,
    height: 50,
    label: data?.label || data.name,
    data: {
      id: "",
      code: "", // 组件的 code
      taskType: data.taskType,
      name: data?.label || data.name, // 名字
      version: "0", // 版本号
      componentType: data?.componentType || "",
      outputFields: [],
      inputFields: "",
      taskParams: {
        ...(data.componentType == 7 && {
          name: "API输入组件",
          typeName: "API输入组件",
          apiUrl: window.location.origin + import.meta.env.VITE_APP_BASE_API,
          apiMethod: "GET",
          returnFormat: 1,
          returnDataLine: ["$.data.rows"],
          pageFlag: 0,
          page: {
            pageNoKey: "pageNo", //分页参数key，需在参数中用${pageNo}进行占位，pageNo为当前参数的值
            maxPage: 10, //最大页数
          },
          interval: 0, //间隔时间 单位毫秒(默认0)
          description: "",
          apiHeaders: [{
            "name": "Accept",
            "value": "*/*"
          }], //header
          inParams: {
            urlParams: [],
            type: 1,
            bodyParams: "",
          },
          outParams: [],
          outputFields: [],
        }),
        ...(data.componentType == 50 && {}),
        ...(data.componentType == 21 && {}),
        ...(data.componentType == 22 && {}),
        ...(data.componentType == 23 && {}),
        ...(data.componentType == 41 && {
          idGenerateType: '1',
        }),
        ...(data.componentType == 42 && {
          keyField: null,
          groupFields: [],
        }),
        ...(data.componentType == 43 && {
          keyField: null,
          groupTableFields: [],
        }),
        ...(data.componentType == 44 && {
          algorithm: "AES",
          secretKey: "",
          plaintextField: "",
          encryptedField: "",
        }),
        ...(data.componentType == 45 && {
          algorithm: "AES",
          secretKey: "",
          plaintextField: "",
          encryptedField: "",
        }),

        ...(data.componentType == 48 && {
          columnName: null,
          columnType: "string",
          format: null,
          length: null,
          precision: null,
          currencySymbol: null,
          decimalSymbol: null,
          groupingSymbol: null,
          defaultValue: null,
          nullIf: null,
        }),
        ...(data.componentType == 49 && {
          input: "", //输入字段
          output: "", //输出字段
          unKnown: "", //缺省值
        }),
        ...(data.componentType == 39 && {
          fieldDerivationType: "FIELD_DERIVE_CONCAT", //操作类型
          fieldDerivationName: "", //新的字段名
          fieldDerivationPrefix: "", //前缀
          fieldDerivationSuffix: "", //前缀
        }),
        ...(data.componentType == 47 && {
          selectedSourceField: "", //字段名称
          targetFieldName: "", //目标字段
          defaultValueWhenUnmatched: "", //不匹配时的默认值
        }),
        ...(data.componentType == 35 && {
          splitField: "",
          address: "", // 需要拆分的字段
          splitType: "delimiter", // "delimiter"->分隔符 或 "regex"->正则表达式
          delimiter: "", // splitType为"delimiter"时必填
          regex: "", // splitType为"regex"时必填
          enclosure: "", // 可选，
        }),
        ...(data.type == 1 && {
          querySql: "",
          csvFile: "",
          topic: "", //主題
          clmt: "0", //连接状态
          logicOperator: "and", //表輸入逻辑连接符
          datasource_id: "", // 源表数据源id 输出
          asset_id: "", // 源表资产id 输入
          table_name: "", // 源表名 输入
          columns: "", // 源表同步字段列表 输入
          readerDatasource: {
            datasourceId: "",
            datasourceType: "",
            dbname: "",
          },
          readModeType: "1", // 读取方式：1:全量 2:id增量 3:时间范围增量 默认全量
          idIncrementConfig: {
            //id增量
            incrementColumn: "", // 增量字段
            incrementStart: "", // 开始值
          },
          dateIncrementConfig: {
            //时间范围增量
            logic: "and", // 逻辑运算符：1: and 2: or 默认and
            dateFormat: "yyyy-MM-dd", // 时间格式：yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss（手动输入）
            column: [],
          },
          ...(data.componentType == 34 && {
            sortFields: [], //排序字段
          }),

          ...(data.componentType == 6 && {
            path: "", //文件路径
            fileType: "csv", // 文件类型 "csv"、"text"
            fieldDelimiter: ",", // 分隔符 默认
            encoding: "UTF-8", //编码
            compression: "", //压缩方式,当fileType（文件类型）为csv下的文件压缩方式，目前仅支持 gzip、bzip2、lzo、snappy
            hadoopConfig: "", //配置
            haveKerberos: false, //kerberos认证
            kerberosKeytabFilePath: "", //是否有Kerberos认证，默认false,true，则配置项kerberosKeytabFilePath，kerberosPrincipal为必填
            kerberosPrincipal: "", //"Kerberos认证Principal名，如xxxx/hadoopclient@xxx.xxx",
          }),
        }),
        parentId: "", //上級节点的id
        config: "", //配置参数
        typeName: data?.label || data.name, //组件类型
        icon: data.icon,
        taskType: data.taskType,
        type: data.type, // 组件类型 1:输入组件 2:输出组件
        batchSize: "1024", // 一次性写入量
        tableFields: [], // 表输出 源表字段
        where: "", // where
        datasourceId: "",
        ...(data.type == 2 && {
          target_datasource_id: "", // 目标数据源id 输出
          target_asset_id: "", // 目标资产id 输出
          target_table_name: "", // 目标表名 输出
          target_columns: "", // 目标表同步字段列表 输出
          writerDatasource: {
            datasourceId: "",
            datasourceType: "",
            dbname: "",
          },
          toColumnsList: [], // 表输入 表字段
          postSql: "", // 后置 SQL
          selectedColumns: [], // 更新主键
          selectedColumn: "",
          writeModeType: 2, //写入模式
          preSql: "", //前置 SQL
          ...(data.componentType == 93 && {
            path: "", //文件路径
            fileName: "", //文件名称
            fileType: "csv", // 文件类型 "csv"、"text"
            fieldDelimiter: ",", // 分隔符 默认
            encoding: "UTF-8", //编码
            compression: "", //压缩方式,当fileType（文件类型）为csv下的文件压缩方式，目前仅支持 gzip、bzip2、lzo、snappy
            hadoopConfig: "", //配置
            haveKerberos: false, //kerberos认证
            kerberosKeytabFilePath: "", //是否有Kerberos认证，默认false,true，则配置项kerberosKeytabFilePath，kerberosPrincipal为必填
            kerberosPrincipal: "", //"Kerberos认证Principal名，如xxxx/hadoopclient@xxx.xxx",
            writerDatasource: {
              datasourceId: "",
              datasourceType: "",
              dbname: "",
            },
          }),
        }),
        ...(data.type == 3 && {
          mainArgs: {},
        }),
      },
    },
    ports: {
      ...cuPort, // 其他连接桩配置
      items: [
        { group: "top", id: "port-top" },
        { group: "bottom", id: "port-bottom" },
      ],
    },
    options: {
      maxConnections: Infinity, // 最大连接数
    },
  });
};
// 根据 componentType 返回默认 taskParams
export const getDefaultTaskParams = (data) => {
  console.log("🚀 ~ getDefaultTaskParams ~ data:", data.componentType);
  console.log("🚀 ~ getDefaultTaskParams ~ data.taskParams?.type:", data.taskParams?.type);

  const base = {
    inputFields: [],
    tableFields: [],
    outputFields: [],
  };

  if (data.componentType == 7) {
    return {
      ...base,
      name: "API输入组件",
      typeName: "API输入组件",
      apiUrl: window.location.origin + import.meta.env.VITE_APP_BASE_API,
      apiMethod: "GET",
      returnFormat: 1,
      returnDataLine: ["$.data.rows"],
      pageFlag: 0,
      page: {
        pageNoKey: "pageNo", //分页参数key，需在参数中用${pageNo}进行占位，pageNo为当前参数的值
        maxPage: 10, //最大页数
      },
      interval: 0, //间隔时间 单位毫秒(默认0)
      description: "",
      apiHeaders: [], //header
      inParams: {
        urlParams: [],
        type: 1,
        bodyParams: "",
      },
      outParams: [],
      outputFields: [],
    };
  }
  if (data.componentType == 50) {
    return {
      ...base,
    };
  }
  if (data.componentType == 21) {
    return {
      ...base,
    };
  }
  if (data.componentType == 22) {
    return {
      ...base,
    };
  }
  if (data.componentType == 23) {
    return {
      ...base,
    };
  }
  if (data.componentType == 42) {
    return {
      ...base,
      keyField: null,
      groupFields: [],
    };
  }
  if (data.componentType == 42) {
    return {
      ...base,
      keyField: null,
      groupTableFields: [],
    };
  }
  if (data.componentType == 44 || data.componentType == 45) {
    return {
      ...base,
      algorithm: "AES",
      secretKey: "",
      plaintextField: "",
      encryptedField: "",
    };
  }
  if (data.componentType == 48) {
    return {
      ...base,
      columnName: null,
      columnType: "string",
      format: null,
      length: null,
      precision: null,
      currencySymbol: null,
      decimalSymbol: null,
      groupingSymbol: null,
      defaultValue: null,
      nullIf: null,
    };
  }

  if (data.componentType == 49) {
    return {
      ...base,
      input: "", //输入字段
      output: "", //输出字段
      unKnown: "", //缺省值
    };
  }
  if (data.componentType == 47) {
    return {
      ...base,
      selectedSourceField: "", //字段名称
      targetFieldName: "", //目标字段
      defaultValueWhenUnmatched: "", //不匹配时的默认值
    };
  }

  if (data.componentType == 35) {
    return {
      ...base,
      splitField: "",
      address: "",
      splitType: "delimiter",
      delimiter: "",
      regex: "",
      enclosure: "",
    };
  }
  if (data.componentType == 35) {
    return {
      ...base,
      selectedSourceField: "", //字段名称
      targetFieldName: "", //目标字段
      defaultValueWhenUnmatched: "", //不匹配时的默认值
    };
  }
  if (data.componentType == 34) {
    return {
      ...base,
      datasortFields: [],
    };
  }

  if (data.taskParams?.type == 1) {
    const commonReaderDatasource = {
      datasourceId: "",
      datasourceType: "",
      dbname: "",
    };

    const baseParams = {
      ...base,
      querySql: "",
      csvFile: "",
      inputFields: "", // 会被 base.inputFields 覆盖为 []
      topic: "",
      clmt: "0",
      logicOperator: "and",
      datasource_id: "",
      asset_id: "",
      table_name: "",
      columns: "",
      readerDatasource: { ...commonReaderDatasource },
      readModeType: "1",
      idIncrementConfig: {
        incrementColumn: "",
        incrementStart: "",
      },
      dateIncrementConfig: {
        logic: "and",
        dateFormat: "yyyy-MM-dd",
        column: [],
      },
    };

    if (data.componentType == 6) {
      Object.assign(baseParams, {
        path: "",
        fileType: "csv",
        fieldDelimiter: ",",
        encoding: "UTF-8",
        compression: "",
        hadoopConfig: "",
        haveKerberos: false,
        kerberosKeytabFilePath: "",
        kerberosPrincipal: "",
      });
    }

    return baseParams;
  }

  // Writer 部分如需启用，取消注释即可
  // if (data.taskParams?.type === 2) {
  //   const commonWriterDatasource = {
  //     datasourceId: "",
  //     datasourceType: "",
  //     dbname: "",
  //   };

  //   const baseParams = {
  //     ...base,
  //     target_datasource_id: "",
  //     target_asset_id: "",
  //     target_table_name: "",
  //     target_columns: "",
  //     writerDatasource: { ...commonWriterDatasource },
  //     toColumnsList: [],
  //     postSql: "",
  //     selectedColumns: [],
  //     selectedColumn: "",
  //     writeModeType: 2,
  //     preSql: "",
  //   };

  //   if (data.componentType === 93) {
  //     Object.assign(baseParams, {
  //       path: "",
  //       fileName: "",
  //       fileType: "csv",
  //       fieldDelimiter: ",",
  //       encoding: "UTF-8",
  //       compression: "",
  //       hadoopConfig: "",
  //       haveKerberos: false,
  //       kerberosKeytabFilePath: '',
  //       kerberosPrincipal: '',
  //     });
  //   }

  //   return baseParams;
  // }

  if (data.componentType == 31) {
    return {
      ...base,
      mainArgs: {},
    };
  }

  // 默认
  return base;
};

let divMenuContainer = null;

export function createMenuDom({
  x,
  y,
  menuItems = [],
  container, // 直接传 DOM 节点，不是 id
  onHide,
}) {
  if (!container) {
    console.warn("必须传入容器 DOM 元素 container");
    return;
  }

  // 清理已有菜单
  if (divMenuContainer) {
    if (container.contains(divMenuContainer)) {
      container.removeChild(divMenuContainer);
    }
    divMenuContainer = null;
    document.body.removeEventListener("click", onBodyClick);
  }

  // 创建菜单容器
  divMenuContainer = document.createElement("div");
  divMenuContainer.className = "div-menu-container";
  Object.assign(divMenuContainer.style, {
    position: "absolute",
    left: `${x}px`,
    top: `${y}px`,
    zIndex: 1000,
    background: "#fff",
    border: "1px solid #ccc",
    borderRadius: "4px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
    minWidth: "140px",
    userSelect: "none",
  });

  // 添加菜单项
  menuItems.forEach(({ label, action }) => {
    const item = document.createElement("div");
    item.className = "div-menu-item";
    item.innerText = label;
    Object.assign(item.style, {
      padding: "8px 12px",
      cursor: "pointer",
      borderBottom: "1px solid #eee",
    });
    item.addEventListener("click", () => {
      action();
      hideMenu();
    });
    divMenuContainer.appendChild(item);
  });

  if (divMenuContainer.lastChild) {
    divMenuContainer.lastChild.style.borderBottom = "none";
  }

  container.appendChild(divMenuContainer);

  // 调整位置，避免菜单超出容器边界
  const menuRect = divMenuContainer.getBoundingClientRect();
  const contRect = container.getBoundingClientRect();
  const THRESHOLD = 500;

  let newLeft = x;
  let newTop = y;

  const distRight = contRect.right - x;
  if (distRight <= THRESHOLD) {
    newLeft = x - menuRect.width;
  }
  const distBottom = contRect.bottom - y;
  if (distBottom <= THRESHOLD) {
    newTop = y - menuRect.height;
  }
  divMenuContainer.style.left = `${newLeft}px`;
  divMenuContainer.style.top = `${newTop}px`;

  divMenuContainer.addEventListener("click", (e) => e.stopPropagation());

  function hideMenu() {
    if (divMenuContainer) {
      divMenuContainer.style.display = "none";
      document.body.removeEventListener("click", onBodyClick);
      if (container.contains(divMenuContainer)) {
        container.removeChild(divMenuContainer);
      }
      divMenuContainer = null;
      if (typeof onHide === "function") onHide();
    }
  }

  function onBodyClick() {
    hideMenu();
  }

  document.body.addEventListener("click", onBodyClick);

  return {
    hide: hideMenu,
  };
}
// 判断数组是否一样
export function areFieldNamesEqual(fieldsA = [], fieldsB = []) {
  const namesB = new Set(fieldsB.map((f) => f.columnName));
  return fieldsA.every((f) => namesB.has(f.columnName));
}
/**
 * 校验节点名称
 */
export function shouldAbortByName(graph, nodeData) {
  const newName = nodeData?.name?.trim();
  const currentcode = nodeData?.code;
  if (!newName || !currentcode) return false;
  const allNodes = graph?.getCells?.() || [];
  return allNodes.some((cell) => {
    if (!cell?.getProp) return false;
    const data = cell.getProp("data") || {};
    console.log("🚀currentcode2222", currentcode);
    return data.name == newName && cell.data.code != currentcode;
  });
}

export const exportGraphAsPNG = (
  graph,
  {
    fileName = "流程图",
    width = 1920,
    height = 1080,
    padding = 40,
    quality = 1,
    stylesheet
  } = {}
) => {
  if (!graph) {
    console.warn("exportGraphAsPNG: graph 实例不存在");
    return;
  }

  const defaultStylesheet = `
.cu_html_container {
  display: flex; 
  flex-direction: row; 
  align-items: center; 
  justify-content: flex-start;
  background: white;
  border: 1px solid #ddd; 
  border-radius: 2px; 
  padding: 8px;
  height: 33px !important; 
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  overflow: hidden;
}

.cu_html_top {
  width: 30px; 
  height: 30px;
  margin-right: 10px; 
}

.cu_html_text {
  display: flex;
  flex-direction: row;
  align-items: center;
  flex: 1; 
  white-space: nowrap; 
  overflow: hidden;
  text-overflow: ellipsis;
}

.cu_html_title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cu_html_tag {
  position: absolute;
  top: 0px;
  right: 5px;
  width: 50px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  font-size: 12px;
  font-weight: bold;
  color: white;
  transform: skewX(-20deg);
  border-radius: 4px;
}
`;

  graph.exportPNG(fileName, {
    width,
    height,
    padding,
    quality,
    copyStyles: true,
    stylesheet: stylesheet || defaultStylesheet
  });
};
// 表输入的规则
// 表输入的规则
export function renameRuleToRuleConfig(data) {
  return data
    .filter(col => Array.isArray(col.cleanRuleList) && col.cleanRuleList.length > 0)
    .map(col => {
      return col.cleanRuleList.map(rule => {
        let parsedRule = {};
        try {
          parsedRule = JSON.parse(rule.rule); // 原来的 rule 解析成对象
        } catch (e) {
          console.warn(`rule JSON 解析失败: ${rule.rule}`, e);
        }
        const ruleConfig = {
          ...parsedRule,
          columns: [col.columnName]
        };
        const { rule: _, ...rest } = rule;
        return {
          ...rest,
          columns: [col.columnName],
          ruleConfig
        };
      });
    })
    .flat();
}
