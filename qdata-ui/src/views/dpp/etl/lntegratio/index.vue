<template>
  <div class="app-container" ref="app-container">
    <div class="head-container">
      <div class="head-title">
        {{ nodeData.name !== null ? nodeData.name : "集成任务" }}
      </div>
      <div class="head-btns">
        <el-button type="primary" size="small" @click="handleExportData" v-if="!route.query.info">保存</el-button>
        <el-button type="primary" size="small" @click="routeTo('/dpp/tasker/dppEtlTask', '')">取消</el-button>
        <el-button type="primary" size="small" @click="openTaskConfigDialog">任务配置</el-button>
        <el-button v-if="!route.query.info" type="primary" size="small"
          @click="selectTab('checkMessage')">任务检查</el-button>
        <!--        <el-button type="primary" size="small" @click="selectTab('log')"
          >执行一下</el-button
        >-->
      </div>
    </div>

    <el-row>
      <el-col :span="4">
        <div class="left-pane">
          <div class="left-tree">
            <div class="">
              <el-tree :data="treeData" :props="{ label: 'label', children: 'children' }" ref="deptTreeRef"
                default-expand-all>
                <template #default="{ node, data }">
                  <div class="custom-tree-node" @mousedown="startDrag($event, node, data)">
                    <img v-if="node.level === 1 && data.type == '1'" src="@/assets/system/images/dpp/tsr.png" alt="icon"
                      class="icon-img" />
                    <img v-if="node.level === 1 && data.type == '3'" src="@/assets/system/images/dpp/tzh.png" alt="icon"
                      class="icon-img" />
                    <img v-if="node.level === 1 && data.type == '2'" src="@/assets/system/images/dpp/tsc.png" alt="icon"
                      class="icon-img" />
                    <img v-if="data.icon" :src="data.icon" alt="icon" class="icon-img" />
                    <span class="treelable"> {{ data.label }}</span>
                  </div>
                </template>
              </el-tree>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="20" v-loading="loading">
        <!-- 右侧部分 -->
        <div class="right-pane">
          <div id="graphContainer" class="graph-container" ref="graphContainer"></div>

          <div class="toolbar">
            <template v-for="item in toolbar" :key="item.id">
              <el-tooltip class="box-item" effect="light" :content="item.tip" placement="bottom">
                <div class="toolbar-item" @click="toolbarClick(item)">
                  <img :src="getAssetsFile(item.icon)" alt="" />
                </div>
              </el-tooltip>
            </template>
          </div>
        </div>
        <div class="tabs-container" v-bind:style="tabAreaStyle">
          <!-- 图标放置在最右侧 -->
          <el-icon class="icon-right" @click="minimizeAction">
            <Minus />
          </el-icon>
          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane v-for="(tab, index) in tabs" :key="index" :name="tab.name">
              <template #label>
                <span>{{ tab.label }}</span>
              </template>
              <div class="tab-content" v-html="tab.content"></div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
    <component :is="currentFormComponent" :visible="drawer" :key="currentNode?.id || Date.now()" :title="title"
      @update="closeDialog" @confirm="handleFormSubmit" :currentNode="currentNode" :info="route.query.info"
      :graph="graph" />
    <taskConfigDialog :visible="taskConfigDialogVisible" title="任务配置" @update:visible="taskConfigDialogVisible = $event"
      @confirm="handletaskConfig" :data="nodeData" :userList="userList" :deptOptions="deptOptions"
      :info="route.query.info" />
  </div>
</template>
<script setup>
import { Graph } from "@antv/x6";
import { Dnd } from "@antv/x6-plugin-dnd";
import { baseConfig, cuPort, typeList, toolbar } from "@/utils/graph";
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import InputForm from "../components/formComponents/InputForm.vue";
import OutputForm from "../components/formComponents/OutputForm.vue";
import csvForm from "../components/formComponents/csvForm.vue";
import TransformForm from "../components/formComponents/TransformForm.vue";
import ExcelInputForm from "../components/formComponents/ExcelInputForm.vue";
import KafkaForm from "../components/formComponents/KafkaForm.vue";
import taskConfigDialog from "../components//taskConfigDialog.vue";
import useUserStore from "@/store/system/user";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttTaskCat } from "@/api/att/cat/attTaskCat/attTaskCat";
const userStore = useUserStore();
import {
  dppEtlTask,
  updateProcessDefinitions,
  createProcessDefinitions
} from "@/api/dpp/etl/dppEtlTask";
import { treeData } from "../components/data";
import { Selection } from "@antv/x6-plugin-selection";
import {
  useHtmlNode,
  showPorts,
  usePlugins,
  transNodeData,
  renderGraph,
  validateGraph,
  getAllChildNodes
} from "../components/opBase";
const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;
    if (id) {
      getList();
    }
  }
);
// 跳转判断
let hasUnsavedChanges = ref(false);
let nodeData = ref({ taskConfig: {}, name: null });
// 全局的Graph
let graph = null;
// 全局的dnd
let dnd = null;
// 抽屉
const drawer = ref(false);
// 任务配置
const taskConfigDialogVisible = ref(false);
// 存储当前组件的信息
const currentNode = ref({});
// 当前组件的上级组件的信息
const sourceNode = ref({});
const currentFormComponent = computed(() => {
  if (!drawer.value || !currentNode.value) return null;
  const componentType = currentNode.value?.data?.componentType || "";
  switch (componentType) {
    case "1":
      return InputForm;
    case "2":
      return ExcelInputForm;
    case "3":
      return KafkaForm;
    case "4":
      return csvForm;
    case "31":
      return TransformForm;
    case "91":
      return OutputForm;
    default:
      return OutputForm;
  }
});

// 撤销按钮
const undoDisabled = ref(null);
// 导出的数据
const exportData2 = ref("");
let loading = ref(false);
function getList() {
  loading.value = true;
  dppEtlTask(route.query.id).then((response) => {
    nodeData.value = response.data;
    renderGraph(graph, nodeData.value);
    loading.value = false;
  });
}
let userList = ref([]);
let deptOptions = ref([]);
function getDeptTree() {
  listAttTaskCat().then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "数据集成类目",
        value: "",
        children: deptOptions.value,
      },
    ];
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
  });
}
if (route.query.id) {
  getList();
}
// 保存 没有code
const closeDialog = (node) => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // 根据组件 ID 删除组件
  }
  drawer.value = false;
};
// 检查 节点是否一致
const areArraysEqual = (array1, array2) => {
  // 检查长度是否相等
  if (array1.length !== array2.length) {
    return false;
  }

  // 遍历数组，逐个比较元素
  for (let i = 0; i < array1.length; i++) {
    const item1 = array1[i];
    const item2 = array2[i];

    // 如果元素是对象，比较对象的属性
    if (typeof item1 === "object" && typeof item2 === "object") {
      // 假设对象有 'columnName' 和 'isChecked' 属性
      if (
        item1.columnName !== item2.columnName ||
        item1.isChecked !== item2.isChecked
      ) {
        return false;
      }
    } else {
      // 如果元素是原始类型，直接比较值
      if (item1 !== item2) {
        return false;
      }
    }
  }

  // 如果通过了所有检查，数组内容相同
  return true;
};
// 连线 创建
const createEdge = (sourceId, targetId) => {
  const sourcePort = "port-bottom"; // 源节点的下桩
  const targetPort = "port-top"; // 目标节点的上桩

  graph.addEdge({
    source: {
      cell: sourceId,
      port: sourcePort, // 指定连接端口为下桩
    },
    target: {
      cell: targetId,
      port: targetPort, // 指定连接端口为上桩
    },

    attrs: {
      line: {
        stroke: "#2666FB", // 边的颜色
        strokeWidth: 1,
        targetMarker: { name: "block", width: 12, height: 8 },
      },
    },
  });
};

// 弹窗保存
const handleFormSubmit = (nodeData) => {
  const node = graph.getCellById(currentNode.value.id);
  if (node) {
    const result = areArraysEqual(
      nodeData.taskParams.tableFields,
      node.data.taskParams.tableFields
    );

    if (nodeData.taskParams.parentId != node.data.taskParams.parentId) {
      createEdge(nodeData.taskParams.parentId, currentNode.value.id);
    }
    if (!result) {
      const childNodes = getAllChildNodes(node, graph);
      for (let i = 0; i < childNodes.length; i++) {
        const childNode = childNodes[i];
        if (childNode.data?.taskParams) {
          childNode.data.taskParams.csvFile = nodeData.taskParams.csvFile;
          childNode.data.taskParams.tableFields =
            nodeData.taskParams.tableFields;
          childNode.data = { ...childNode.data };
        }
      }
    }
    node.setProp(
      "data",
      JSON.parse(JSON.stringify({ ...node.getProp("data"), ...nodeData }))
    );
  }
  // 关闭抽屉（表单）
  drawer.value = false;
};
// 运行实例保存
const handletaskConfig = (form) => {
  nodeData.value.taskConfig = { ...form };
};
// 大保存
const handleExportData = async () => {
  loading.value = true;
  try {
    // 检查
    const { isValid } = validateGraph(graph);
    if (!isValid) {
      return;
    }
    // 检查是否有任务配置
    if (!hasTaskConfig(nodeData.value)) {
      taskConfigDialogVisible.value = true;
      return;
    }
    // 转换节点数据
    exportData2.value = await transNodeData(graph);
    console.log("🚀 ~ handleExportData ~ exportData2.value:", exportData2.value)
    // 将任务相关的配置整合到 exportData2 中
    exportData2.value = {
      ...exportData2.value,
      taskRelationJson: JSON.stringify(exportData2.value.taskRelationJson),
      taskDefinitionList: JSON.stringify(exportData2.value.taskDefinitionList),
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
      ...nodeData.value?.taskConfig,
    };
    // 根据 nodeData.id 判断是更新还是创建
    const res = nodeData.value?.id
      ? await updateProcessDefinitions({
        ...exportData2.value,
        id: nodeData.value.id,
      })
      : await createProcessDefinitions(exportData2.value);
    // 成功后处理
    if (res.code == "200") {
      handleSuccess();
    } else {
      proxy.$modal.msgError("操作失败");
    }
  } finally {
    loading.value = false;
  }
};
// 判断是否有任务配置
const hasTaskConfig = (nodeData) => {
  return nodeData?.taskConfig && Object.keys(nodeData.taskConfig).length > 0;
};
// 页面跳转
const handleSuccess = () => {
  taskConfigDialogVisible.value = false;
  hasUnsavedChanges.value = false;
  const message = nodeData.value.id ? "修改成功" : "新增成功";
  router.push("/dpp/tasker/dppEtlTask");
  proxy.$modal.msgSuccess(message);
};


// 任务配置弹窗
const openTaskConfigDialog = () => {
  taskConfigDialogVisible.value = true;
};
useHtmlNode();
const startDrag = (e, treeNode, data) => {
  // 获取图形中的所有节点
  const nodes = graph.getCells().filter((cell) => {
    return cell.isNode();
  });

  if (treeNode.level === 2) {
    if (route.query?.info) return proxy.$modal.msgWarning("详情页面只能查看");
    if (!data.componentType) return proxy.$modal.msgWarning("正在开发中");
    const node = graph.createNode({
      shape: "cu-data-node",
      width: 180,
      height: 50,
      label: data?.label,
      data: {
        id: "",
        code: "", // 组件的 code
        taskType: data.taskType,
        name: data?.label, // 名字
        version: "0", // 版本号
        componentType: data?.componentType || "",
        taskParams: {
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
            idIncrementConfig: {//id增量
              incrementColumn: "", // 增量字段
              incrementStart: "", // 开始值
            },
            dateIncrementConfig: {//时间范围增量
              logic: "and", // 逻辑运算符：1: and 2: or 默认and
              dateFormat: "yyyy-MM-dd", // 时间格式：yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss（手动输入）
              column: [

              ],
            },
          }),
          parentId: "", //上級节点的id
          config: "", //配置参数
          typeName: data?.label, //组件类型
          icon: data.icon,
          taskType: data.taskType,
          type: data.type, // 组件类型 1:输入组件 2:输出组件
          batchSize: "1024", // 一次性写入量
          tableFields: "", // 源表同步字段列表 输入
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
            writeModeType: 2,//写入模式
            preSql: "",//前置 SQL
          }),
          ...(data.type == 3 && {
            mainArgs: {},
          }),
          tableFields: [], // 表输出 源表字段
          where: "", // where
          datasourceId: "",
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
    dnd.start(node, e); // 启动拖拽操作
  }
};

let divMenuContainer = null;
/**
 * 组件右键删除
 * @param {*}
 */
const createMenuDom = ({ x, y, node, edge, type }) => {
  if (route?.query?.info) return;
  hasUnsavedChanges.value = true;
  // 清理现有的菜单容器
  if (divMenuContainer) {
    document.getElementById("graphContainer").removeChild(divMenuContainer);
  }
  // 创建新的菜单容器
  divMenuContainer = document.createElement("div");
  divMenuContainer.setAttribute("class", "div-menu-container");
  divMenuContainer.style.left = `${x + 30}px`;
  divMenuContainer.style.top = `${y}px`;
  // 创建菜单项
  const divMenuItem = document.createElement("div");
  divMenuItem.setAttribute("class", "div-menu-item");
  divMenuItem.innerText = type === 0 ? "删除组件" : "删除连接线";

  divMenuItem.addEventListener("click", () => {
    if (type === 0) {
      // 获取当前节点的所有子节点（包括下级的下级节点）
      const childNodes = getAllChildNodes(node, graph);
      childNodes.forEach((childNode) => {
        if (childNode.data?.taskParams) {
          childNode.data.taskParams.tableFields = [];
          childNode.data = { ...childNode.data };
        }
      });
      // 删除当前组件
      graph.removeNode(node);
    } else {
      // 获取边的源节点和目标节点的所有下级节点
      const sourceNode = edge.getSourceCell();
      const childNodes = getAllChildNodes(sourceNode, graph);
      childNodes.forEach((childNode) => {
        if (childNode.data?.taskParams) {
          childNode.data.taskParams.tableFields = [];
          childNode.data = { ...childNode.data };
        }
      });
      // 删除连接线
      graph.removeEdge(edge);
    }
    // 隐藏菜单
    divMenuContainer.style.display = "none";
  });

  divMenuContainer.appendChild(divMenuItem);
  document.getElementById("graphContainer").appendChild(divMenuContainer);

  // 点击其他地方隐藏菜单
  const hideMenu = () => {
    if (divMenuContainer) {
      divMenuContainer.style.display = "none";
    }
  };
  document.body.addEventListener("click", hideMenu);

  // 移除菜单监听器时要清理
  divMenuItem.addEventListener("click", () => {
    document.body.removeEventListener("click", hideMenu);
  });
};

let selectedEdge = ref(); // 外部变量，用于记录当前选中的边
// 定义方法来清空当前选中边的颜色
function clearSelectedEdge() {
  if (selectedEdge.value) {
    selectedEdge.value.prop("attrs/line/stroke", "#2666FB"); // 恢复默认颜色
    selectedEdge.value = null; // 清空选中的边
  }
}
const title = ref("");
// 初始化图表
function initializeGraph() {
  graph = new Graph({
    container: proxy.$refs.graphContainer,
    width: "100%",
    height: "60%",
    grid: false, //网格
    background: { color: "#ff0000" },
    autoResize: true, //画布自适应
    panning: true, //画布拖动
    interactive: true,
    ...baseConfig,
    //画布缩放
    mousewheel: {
      enabled: true,
      zoomAtMousePosition: true,
      minScale: 0.5,
      maxScale: 3,
    },
    selecting: {
      enabled: true,
      className: "my-selecting", // 自定义选中样式类名
    },
  });

  dnd = new Dnd({
    target: graph,
    scaled: false,
  });
  graph.use(
    new Selection({
      enabled: true,
      multiple: true,
      rubberband: true,
      movable: true,
      showNodeSelectionBox: true,
      pointerEvents: "none",
    })
  );
  // 插件
  baseConfig.bindShortcuts(graph);
  usePlugins(graph);
  undoDisabled.value = graph.canUndo();
}

// 绑定事件
function bindGraphEvents() {
  // 监听边的点击事件
  graph.on("edge:click", handleEdgeClick);

  graph.on("blank:click", clearSelectedEdge);

  graph.on("node:added", handleNodeAdded);

  graph.on("node:mouseenter", () => togglePortsVisibility(true));

  // 节点鼠标离开事件
  graph.on("node:mouseleave", () => togglePortsVisibility(false));

  graph.on("node:contextmenu", handleNodeContextMenu);

  // 连接边事件
  graph.on("edge:connected", handleEdgeConnected);

  graph.on("edge:contextmenu", handleEdgeContextMenu);

  graph.on("node:dblclick", handleNodeDblClick);
}
// 处理边的点击事件
function handleEdgeClick({ cell }) {
  const currentColor = cell.prop("attrs/line/stroke");
  clearSelectedEdge();

  const newColor = currentColor === "#f14f10" ? "#2666FB" : "#f14f10";
  cell.prop("attrs/line/stroke", newColor);

  selectedEdge.value = cell;
}

// / 处理节点添加事件
function handleNodeAdded({ node }) {
  if (!loading.value) {
    hasUnsavedChanges.value = true;
    currentNode.value = {};
    sourceNode.value = {};
    const nodeData = graph.getNodes();
    const nodeType = node.data.taskParams.type;
    const existingNode =
      nodeType != "3" &&
      nodeData.find(
        (item) => item.data.taskParams.type === nodeType && item.id !== node.id
      );
    if (existingNode) {
      handleExistingNode(node);
      return;
    }
    if (nodeType != "1") {
      handleNonInputNode(node);
    }
    currentNode.value = node;
    drawer.value = true; // 控制抽屉显示
  }
}
// 处理已有节点的情况
function handleExistingNode(node) {
  if (node.data.taskParams.type == 2) {
    proxy.$message.error(`只能有一个输出组件！`);
  } else if (node.data.taskParams.type == "1") {
    proxy.$message.error(`只能有一个输入组件！`);
  }
  graph.removeNode(node.id);
}
// 处理非输入节点
function handleNonInputNode(node) {
  const edges = graph.getEdges();
  edges.forEach((edge) => {
    if (edge.getTargetNode() == node) {
      sourceNode.value = edge.getSourceNode(); // 获取上级组件的数据
      drawer.value = true; // 控制抽屉显示
    }
  });
  drawer.value = true; // 控制抽屉显示
}

// 切换端口的显示状态
function togglePortsVisibility(visible) {
  const container = document.getElementById("graphContainer");
  const ports = container.querySelectorAll(".x6-port-body");
  showPorts(ports, visible);
}
// 处理节点右键菜单事件
function handleNodeContextMenu({ e, node }) {
  const pos = graph.clientToGraph(e.clientX, e.clientY);
  createMenuDom({ x: pos.x, y: pos.y, node, type: 0 });
}
// 处理连接边事件
function handleEdgeConnected({ edge }) {
  if (!loading.value) {
    hasUnsavedChanges.value = true;
    const source = edge.getSourceCell();
    const target = edge.getTargetCell();
    // 检查源节点和目标节点是否相同
    if (source === target) {
      graph.removeEdge(edge); // 移除边
      proxy.$modal.msgWarning("节点不能连接到自己");
      return;
    }

    // 获取源节点和目标节点的 taskParams.type
    const sourceType = source.data?.taskParams?.type;
    const targetType = target.data?.taskParams?.type;

    // 类型 1 不能作为目标节点
    if (targetType === 1) {
      graph.removeEdge(edge); // 移除边
      proxy.$modal.msgWarning("输入组件不能被连接");
      return;
    }

    // 类型 2 不能作为输入节点（源节点）
    if (sourceType === 2) {
      graph.removeEdge(edge); // 移除边
      proxy.$modal.msgWarning("输出组件不能连接到其他组件");
      return;
    }

    // 类型 2 节点只能作为输出节点连接一次
    if (targetType === 2) {
      const targetEdges = graph
        .getEdges()
        .filter((e) => e.getTargetCell() === target);
      if (targetEdges.length > 1) {
        graph.removeEdge(edge); // 移除边
        proxy.$modal.msgWarning("目标节点只能作为输出连接一次");
        return;
      }
    }

    updateTargetNodeData(source, target, edge);
  }
}
// 更新目标节点的数据
function updateTargetNodeData(source, target, edge) {
  const childNodes = getAllChildNodes(source, graph);
  childNodes.forEach((childNode) => {
    if (childNode.data?.taskParams) {
      childNode.data.taskParams.tableFields =
        source.data.taskParams.tableFields;
      childNode.data = { ...childNode.data };
    }
  });
}
// 处理边右键菜单事件
function handleEdgeContextMenu(event) {
  const edge = event.edge;
  const { x, y } = event;
  createMenuDom({ x: x, y: y, edge, type: 1 });
}
// 处理节点双击事件
function handleNodeDblClick({ node }) {
  graph.cleanSelection(); // 清除所有选中的元素
  clearSelectedEdge();
  hasUnsavedChanges.value = true;
  currentNode.value = {};
  currentNode.value = node;
  drawer.value = true; // 控制抽屉显示
}
const toolbarClick = (item) => {
  switch (item.id) {
    // case "full-screen": {
    //   toggle();
    //   isfull.value = !isfull.value;
    //   break;
    // }
    case "zoom-in":
      graph.zoom(0.2);
      break;
    case "zoom-out":
      graph.zoom(-0.2);
      break;
    case "redo":
      if (graph.canRedo()) {
        graph.redo();
      }
      break;
    case "undo":
      if (graph.canUndo()) {
        graph.undo();
      }
      break;
    case "auto-fit":
      graph.centerContent();
      graph.zoomTo(1);
      break;
    case "export": {
      graph.exportPNG("流程图", {
        width: 1920,
        height: 1080,
        padding: 20,
        quality: 0.9,
      });
      break;
    }
    case "reset": {
      handleCancel();
      break;
    }
  }
};
// 初始化函数
onMounted(() => {
  getDeptTree();
  initializeGraph();
  bindGraphEvents();
});
// 离开页面时提示
onBeforeRouteLeave((to, from, next) => {
  // 检查是否有未保存的更改
  if (hasUnsavedChanges.value) {
    ElMessageBox.confirm(
      "您已经编辑部分任务内容，是否放弃已编辑内容？", // 提示信息
      "提示", // 标题
      {
        confirmButtonText: "保存", // 确认按钮文本
        cancelButtonText: "放弃", // 取消按钮文本
        type: "warning", // 弹窗类型
      }
    )
      .then(() => {
        handleExportData();
        next(false);
        saveData();
      })
      .catch(() => {
        next();
        saveData();
      });
  } else {
    next();
  }
});
const saveData = async () => {
  hasUnsavedChanges.value = false;
};
function routeTo(link, row) {
  hasUnsavedChanges.value = false;
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          // id: row.id,
        },
      });
    }
  }
}
// 用于控制当前选中的标签
const activeTab = ref("checkMessage");
const tabs = ref([
  { name: "checkMessage", label: "检查消息", content: "检查消息内容" },
  // { name: "log", label: "日志", content: "日志内容" },
]);
// 用于设置标签区域的样式
const tabAreaStyle = ref({
  position: "absolute",
  height: "300px",
  width: "83%",
  transition: "bottom 0.3s",
  bottom: "-9999px",
  "background-color": "rgb(255, 255, 255)",
  "margin-left": "15px",
});
// 任务检查
const minimizeAction = () => {
  tabAreaStyle.value.bottom = "-9999px";
};// 切换到任务检查标签
const selectTab = (tabName) => {
  activeTab.value = tabName;
  if (activeTab.value == "checkMessage") {
    isValidClick();
  }
  tabAreaStyle.value.bottom = "0px";
};
const isValidClick = (tab) => {
  const { isValid, errorMessages } = validateGraph(graph, true);
  let message = "";
  if (!isValid && errorMessages.length != 0) {
    message += "检查未通过:<br>" + errorMessages.join("<br>");
  } else {
    message += "检查通过";
  }
  if (
    !nodeData.value?.taskConfig ||
    Object.keys(nodeData.value.taskConfig).length === 0
  ) {
    message += "<br>任务配置未填写";
  }

  tabs.value[0].content = message;
};
const getAssetsFile = (url) => {
  return new URL(`/src/assets/dpp/etl/${url}`, import.meta.url).href;
};

</script>

<style scoped lang="less">
.app-container {
  height: calc(87vh - 7px);
  overflow: hidden;

  .tabs-container {
    position: relative;
  }

  .right-pane {
    min-height: 864px;
  }

  .icon-right {
    position: absolute;
    top: 10px;
    right: 30px;
    cursor: pointer;
    font-size: 20px;
    color: #666;
    z-index: 1000000;
  }

  .head-container {
    height: 50px;
    background: #fff;
    box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
    padding: 0px 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    .head-title {
      font-family: PingFang SC;
      font-size: 16px;
      color: var(--el-color-primary);
      display: flex;
      align-items: center;

      &::before {
        content: "";
        display: inline-block;
        background: var(--el-color-primary);
        width: 6px;
        height: 16px;
        border-radius: 2px;
        margin-right: 10px;
      }
    }

    .head-btns {
      .el-button {
        height: 28px;
      }
    }
  }
}

.left-pane {
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1) !important;
  background-color: #fff;
  height: 80vh;
  overflow: hidden;

  .left-tree {
    padding: 15px 15px 15px 15px;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }

  .icon-img {
    width: 15px;
    height: 15px;
  }
}

.el-aside {
  padding: 2px 0;
  margin-bottom: 0;
  background-color: #f0f2f5;
}

.custom-tree-node {
  display: flex;
  align-items: center;
  width: 200px;
  user-select: none;
}

.treelable {
  margin-left: 5px;
}

.highlight {
  stroke: red;
  stroke-width: 2px;
}

// .icon-img {
//   width: 20px;
//   height: 20px;
//   display: inline-block;
//   vertical-align: middle;
// }
.custom-tabs {
  font-size: 14px;
  padding-left: 20px;
}

#graphContainer {
  margin-left: 15px;
}

.toolbar {
  position: absolute;
  top: 16px;
  right: unset;
  bottom: unset;
  left: 18.9%;
  display: flex;
  align-items: center;
  justify-content: center;
  // opacity: 0.65;
  z-index: 100;

  .toolbar-item {
    display: inline-block;
    width: 34px;
    height: 32px;
    cursor: pointer;
    box-sizing: content-box;
    margin-right: 5px;

    &:hover {
      background-color: rgba(255, 255, 255, 0.2);
    }

    img {
      width: 100%;
      height: 100%;
    }
  }

  .search {
    :deep(.el-input__wrapper) {
      background: transparent;

      .el-input__inner {
        color: #fff;
      }
    }
  }
}

.graph-container {
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1) !important;
  min-height: 70vh !important;
}

:deep(.x6-widget-selection-box) {
  fill: rgba(0, 123, 255, 0.3);
  /* 设置选中框的填充颜色 */
  stroke: #007bff;
  /* 设置选中框的边框颜色 */
  opacity: 1;
  /* 设置选中框的透明度 */
  pointer-events: none !important;
}

:deep(.x6-graph-background) {
  background-color: white !important;
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1) !important;
}

:deep(.x6-graph-grid) {
  display: none;
}
</style>
