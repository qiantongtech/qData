/**
 * 存放一些节点操作的公共方法
 */
import { DataUri, Shape } from '@antv/x6';
import { History } from '@antv/x6-plugin-history';
import { Export } from '@antv/x6-plugin-export';
import { Selection } from '@antv/x6-plugin-selection';
import '@/assets/system/styles/global.scss'
import { cuPort } from '@/utils/graph';
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import {
  getNodeUniqueKey,

} from "@/api/dpp/etl/dppEtlTask";
import { ElMessage, } from 'element-plus'
/**
 * 插件使用
 */
export const usePlugins = graph => {
  graph
    .use(
      new History({
        enabled: true
      })
    )
    .use(
      new Selection({
        enabled: true,
        rubberband: true,
        showNodeSelectionBox: true
      })
    )
    .use(new Export());
};
/**
 * 画布缩放比例
 * @param {*} graph
 * @returns
 */
export const getCanvasScale = graph => {
  const scaleValue = graph.zoom();
  let result = parseFloat(scaleValue * 100).toFixed(0);
  return result;
};
/**
 * 自定义html节点
 */
export const useHtmlNode = (node) => {
  Shape.HTML.register({
    shape: 'cu-data-node',
    width: 180,
    height: 60,
    html(cell) {
      const { name: nodeName, createPerson, icon, length, releaseState, taskParams } = cell.getData();
      const htmlContainer = document.createElement('div');
      htmlContainer.setAttribute('class', 'cu_html_container');
      const htmlTop = document.createElement('img');
      htmlTop.setAttribute('class', 'cu_html_top');
      let iconSrc = taskParams.icon || icon;
      if (iconSrc && iconSrc.startsWith('data:image')) {
        htmlTop.setAttribute('src', iconSrc);
      } else if (iconSrc) {
        DataUri.imageToDataUri(iconSrc, function (nu, url) {
          htmlTop.src = url;
          const newData = {
            ...cell.getData(),
            taskParams: { ...taskParams, icon: url }
          };
          cell.setData(newData);
        });
      }
      const htmlText = document.createElement('div');
      htmlText.setAttribute('class', 'cu_html_text');
      const htmlTitle = document.createElement('div');
      htmlTitle.setAttribute('class', 'cu_html_title');
      htmlTitle.innerText = nodeName;
      htmlText.appendChild(htmlTitle);
      htmlContainer.appendChild(htmlTop);
      htmlContainer.appendChild(htmlText);

      return htmlContainer;
    }
  });
};
/**
 * 显示节点上的连接桩
 * @param {*} ports
 * @param {*} show
 */
export const showPorts = (ports, show) => {
  for (let i = 0, len = ports.length; i < len; i = i + 1) {
    ports[i].style.visibility = show ? 'visible' : 'hidden';
  }
};

/**
 * 画布清空
 */
export const handleRmNodes = graph => {
  graph.clearCells();
};
// 装换组件规则的封装
export const transformColumnsData = (columnsList) => {
  return columnsList.map(item => {
    const columns = item.columnName;
    const cleanRules = item && item.cleanRuleList ? item.cleanRuleList.map(rule => ({
      ruleId: rule.ruleId,
      data: rule.ruleConfig,
      ruleName: rule.ruleName,
    })) : [];
    return { columns, cleanRules };
  });
}
//输出组件字段处理
export const handleType2TaskParams = (fromColumns, toColumns) => {
  const target_columns = [];
  const columns = [];
  const minLength = Math.min(fromColumns.length, toColumns.length);
  for (let i = 0; i < minLength; i++) {
    const fromCol = fromColumns[i];
    const toCol = toColumns[i];
    if (fromCol.isChecked && toCol.isChecked) {
      target_columns.push(toCol.columnName);
      columns.push(fromCol.columnName);
    }
  }
  return {
    target_columns,
    columns
  };
};

// 主函数：根据不同的type来处理任务参数
export const transNodeData = async (graph) => {
  const allNodes = JSON.parse(JSON.stringify(graph.getNodes()));
  const allEdges = JSON.parse(JSON.stringify(graph.getEdges()));
  const tailNodes = allEdges.reduce((acc, edge) => {
    acc[edge.target.cell] = true;
    return acc;
  }, {});
  const isHeadNode = code => !tailNodes[code];
  const locations = [];
  const tasksMap = {};
  const taskDefinitionList = [];
  const sortedNodes = allNodes
    .filter(node => node.shape === 'cu-data-node')
    .sort((a, b) => {
      if (a.data?.taskParams?.type == 1 && b.data?.taskParams?.type != 1) return -1;
      if (a.data?.taskParams?.type != 1 && b.data?.taskParams?.type == 1) return 1;
      if (a.data?.taskParams?.type == 2) return 1;
      if (b.data?.taskParams?.type == 2) return -1;
      return 0;
    });
  for (const item of sortedNodes) {
    if (item.shape === 'cu-data-node') {
      const code = item.id;
      tasksMap[code] = item.data;
    }
  }
  for (const item of sortedNodes) {
    if (item.shape === 'cu-data-node') {
      const code = item.id;
      locations.push({
        taskCode: item.data.code,
        x: item.position.x,
        y: item.position.y,
      });
      taskDefinitionList.push({ ...item.data });
    }
  }

  const taskRelationJson = [];

  allNodes.forEach(node => {
    if (isHeadNode(node.id)) {
      const task = tasksMap[node.id];
      taskRelationJson.push({
        name: '',
        preTaskCode: 0,
        preTaskVersion: 0,
        postTaskCode: task?.code || 0,
        postTaskVersion: task?.version || 0,
        conditionType: 'NONE',
        conditionParams: {},
      });
    }
  });
  // 处理所有边
  allEdges.forEach(item => {
    if (item.shape === 'edge') {
      const sourceId = item.source.cell;
      const prevTask = tasksMap[sourceId];
      const targetId = item.target.cell;
      const task = tasksMap[targetId] || '';
      taskRelationJson.push({
        name: '',
        preTaskCode: prevTask?.code || 0,
        preTaskVersion: prevTask?.version || 0,
        postTaskCode: task?.code || 0,
        postTaskVersion: task?.version || 0,
        conditionType: 'NONE',
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
  queue.push(node);
  visited.add(node);

  while (queue.length > 0) {
    currentNode = queue.shift();
    const connectedEdges = graph.getConnectedEdges(currentNode);
    connectedEdges.forEach(edge => {
      if (!edges.includes(edge)) {
        edges.push(edge);
      }
    });
    const adjacentNodes = graph.getNeighbors(currentNode);
    adjacentNodes.forEach(adjacentNode => {
      if (!visited.has(adjacentNode)) {
        visited.add(adjacentNode);
        queue.push(adjacentNode);
      }
    });
  }
  return edges;
}
export const getEdgesForType2Nodes = (graph) => {
  const nodes = graph.getNodes();
  const edges = graph.getEdges();
  const type2Nodes = nodes.filter(node => node.data.taskParams && node.data.taskParams.type == '2');
  let relatedEdges = [];
  let type1NodeData = [];
  type2Nodes.forEach(node => {
    let incomingEdges = getAllConnectedEdges(graph, node);
    incomingEdges.forEach(edge => {
      const sourceNodeId = edge.source.cell;
      const sourceNode = nodes.find(n => n.id == sourceNodeId);
      if (sourceNode && sourceNode.data.taskParams && sourceNode.data.taskParams.type == '1' && sourceNode.data.componentType == 1) {
        type1NodeData.push(sourceNode.data);
      }
    });
    relatedEdges = [...relatedEdges, ...incomingEdges];
  });

  return { relatedEdges, type1NodeData };
};

export const validateGraph = (graph, flag) => {
  const nodes = graph.getNodes();
  const edges = graph.getEdges();
  let valid = true;
  let errorMessages = [];
  if (nodes.length === 0) {
    if (!flag) {
      ElMessage.error('当前任务缺少输入、转换、输出组件，请设置相关组件');
    }
    return { isValid: false, errorMessages: ['当前任务缺少输入、转换、输出组件，请设置相关组件'] };
  }

  let inputNodeExists = false;
  let outputNodeExists = false;

  const addErrorMessage = (message) => {
    errorMessages.push(message);
  };

  // (表输出)
  const validateType2TaskParams = (taskParams, node) => {
    if (!taskParams.tableFields || taskParams.tableFields.length === 0) {
      valid = false,
        addErrorMessage(`${node.data.name} 表输出组件未进行字段映射，请设置字段映射`);
    } else {
      let { target_columns = [], columns = [] } = handleType2TaskParams(taskParams.tableFields, taskParams.toColumnsList);
      if (target_columns.length === 0 || columns.length === 0) {
        valid = false,
          addErrorMessage(`${node.data.name} 表输出组件未进行字段映射，请设置字段映射`);
      }
    }
  };
  // (装换组件)
  const validateType3TaskParams = (taskParams, node) => {
    if (!taskParams.tableFields || taskParams.tableFields.length === 0) {
      valid = false,
        addErrorMessage(`${node.data.name} 未设置转换规则，请设置转换规则`);
    } else {
      let hasValidElementName = taskParams.tableFields.some(item =>
        item.cleanRuleList !== null && Array.isArray(item.cleanRuleList) && item.cleanRuleList.length > 0
      );
      if (!hasValidElementName) {
        valid = false,
          addErrorMessage(`${node.data.name}缺少转换规则`);
      }

    }
  };
  nodes.forEach(node => {
    const taskParams = node.data && node.data.taskParams;

    if (!taskParams) return;

    if (taskParams.type == '2') {
      validateType2TaskParams(taskParams, node);
    }

    if (taskParams.type == '3') {
      validateType3TaskParams(taskParams, node);
    }

    if (taskParams.type == '1') inputNodeExists = node.data;
    if (taskParams.type == '2') outputNodeExists = node.data;
  });
  if (!inputNodeExists && !outputNodeExists) {
    valid = false,
      addErrorMessage('当前任务缺少输入、输出组件，请设置输入、输出节点');
  } else if (!inputNodeExists) {
    valid = false,
      addErrorMessage('当前任务缺少输入组件，请设置输入节点');
  } else if (!outputNodeExists) {
    valid = false,
      addErrorMessage('当前任务缺少输出组件，请设置输出节点');
  }
  const { relatedEdges, type1NodeData } = getEdgesForType2Nodes(graph);
  const sourceNodeData = type1NodeData[0];
  if (sourceNodeData) {
    if (sourceNodeData.taskParams.table_name == outputNodeExists.taskParams.target_table_name) {
      if (sourceNodeData.taskParams.readerDatasource.datasourceId == outputNodeExists.taskParams.writerDatasource.datasourceId) {
        addErrorMessage(`${sourceNodeData.name} 与 ${outputNodeExists.name} 的输入输出不能为同一个数据源的同一个表`);
      }
    }
  }

  if (errorMessages.length > 0 && !flag) {
    ElMessage.error(errorMessages[0]);
  }
  return { isValid: errorMessages.length == 0, errorMessages };
};
export const renderGraph = (graph, savedData, width) => {
  graph.clearCells();
  savedData.locations.forEach((location) => {
    const nodeData = savedData.taskDefinitionList.find(item => item.code == location.taskCode);
    if (nodeData) {
      const node = graph.addNode({
        id: location.taskCode,
        shape: 'cu-data-node',
        x: location.x,
        y: location.y,
        width: width || 170,
        height: 50,
        data: nodeData,
        ports: {
          ...cuPort,
          items: [
            { group: 'top', id: 'port-top' },
            { group: 'bottom', id: 'port-bottom' },
          ]
        }
      });
    }
  });

  // 还原边
  savedData.taskRelationJson.forEach((relation) => {
    if (relation.postNodeCode) {
      if (relation.preNodeCode != 0) {
        graph.addEdge({
          source: {
            cell: relation.preNodeCode,
            port: 'port-bottom',
          },
          target: {
            cell: relation.postNodeCode,
            port: 'port-top',
          },
          data: {
            sourceId: relation.preNodeCode,
            targetId: relation.postNodeCode
          },
          attrs: {
            line: {
              stroke: '#2666FB',
              strokeWidth: 1,
              targetMarker: { name: 'block', width: 12, height: 8 },
            }
          },
        });
      }
    }
  });
};
// 获取code
export const fetchNodeUniqueKey = async () => {
  try {
    const response = await getNodeUniqueKey({
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
    });
    if (response.code == '200') {
      return response.data;
    }
    return null; l
  } catch (error) {
    return null;
  }
};
// 获取上级节点 封装成下拉框
export const createNodeSelect = (graph, currentNodeId) => {
  return graph.getNodes()
    .filter(node => node.id !== currentNodeId && node?.data?.taskParams?.type !== 2)
    .map(node => ({
      label: node.data.name || '未知节点',
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