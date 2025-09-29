<template>
  <div class="app-containers" ref="app-container">
    <div class="flex-container">

      <!-- 右侧主内容 -->
      <div class="right-pane" v-loading="loading">
        <!-- <el-empty description=" 暂无任务流程" v-if="!nodeData?.locations || nodeData.locations.length === 0">
        </el-empty> -->
        <div id="graphContainer" class="graph-container" ref="graphContainer"></div>
        <TeleportContainer />
        <!-- 工具栏 -->
        <div class="toolbar" v-if="nodeData?.locations">
          <template v-for="item in toolbar" :key="item.id">
            <el-tooltip class="box-item" effect="light" :content="item.tip" placement="bottom"
              v-if="item.tip !== '重置' && item.tip !== '导出'">
              <div class="toolbar-item" @click="toolbarClick(item)">
                <img :src="getAssetsFile(item.icon)" alt="" />
              </div>
            </el-tooltip>
          </template>
        </div>
      </div>
    </div>
    <!-- 动态表单 -->
    <component :is="currentFormComponent" :visible="drawer" :key="currentNode?.id || Date.now()" :title="title"
      @update="closeDialog" :currentNode="currentNode" :info="route.query.info" :graph="graph" />
    <!-- 字段预览弹窗 -->
    <FieldPreviewDialog ref="fieldPreviewDialog" />
  </div>
</template>
<script setup>
import { Graph } from "@antv/x6";
import { Dnd } from "@antv/x6-plugin-dnd";
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
/// 输入组件
import InputForm from "@/views/dpp/task/integratioTask/components/input/tableForm.vue";
import excelInputForm from "@/views/dpp/task/integratioTask/components/input/excelForm.vue";
import csvForm from "@/views/dpp/task/integratioTask/components/input/csvForm.vue";
// 转换组件
// 清洗组件
import TransformForm from "@/views/dpp/task/integratioTask/components/clean/cleanForm.vue";
// 排序组件
import OrderConfig from "@/views/dpp/task/integratioTask/components/transform/orderConfig.vue";
// 字段派生期
import FieldBuilder from "@/views/dpp/task/integratioTask/components/transform/fieldBuilder.vue";
// 输出表组件
import OutputForm from "@/views/dpp/task/integratioTask/components/output/tableForm.vue";
import { getLogByTaskInstanceId, getTaskInfo } from "@/api/dpp/task/etlTask";
// import taskConfigDialog from "@/views/dpp/etl/components//task.vue";
import useUserStore from "@/store/system/user";
import { Export } from '@antv/x6-plugin-export'
const userStore = useUserStore();
import { getTeleport } from "@antv/x6-vue-shape";
const TeleportContainer = defineComponent(getTeleport());
import { Selection } from "@antv/x6-plugin-selection";
import {
  usePlugins,
  fetchNodeUniqueKey,
  exportGraphAsPNG,
  useVueNode,
  renderGraphs,
} from "@/views/dpp/utils/opBase";
const { proxy } = getCurrentInstance();
const route = useRoute();
let hasUnsavedChanges = ref(false);
let nodeData = ref();
let graph = null;
let dnd = null;
const drawer = ref(false);
const taskConfigDialogVisible = ref(false);
const currentNode = ref({});
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
    case "5":
      return hiveForm;
    case "6":
      return hdfsForm;
    case "7":
      return ApiForm;
    case "31":
      return TransformForm;
    case "33":
      return TransformForm;
    case "34":
      return OrderConfig;
    case "35":
      return fieldSplit;
    case "21":
      return StringReplace;
    case "50":
      return StringOperation;
    case "47":
      return ValueMapping;
    case "48":
      return AddConstants;
    case "49":
      return NumericRange;
    case "22":
      return FieldSelectAndmodificat;
    case "23":
      return SetFieldValues;
    case "39":
      return FieldBuilder;
    case "40":
      return DedupFilter;
    case "41":
      return addField;
    case "42":
      return RowToColumn;
    case "43":
      return ColumnToRow;
    case "44":
      return CryptoBox;
    case "45":
      return DecryptForm;
    case "46":
      return CalcWidget;
    case "91":
      return OutputForm;
    case "92":
      return HiveoutForm;
    case "93":
      return hdfsOutputForm;
    default:
      return null;
  }
});
const props = defineProps({
  dppEtlTaskDetail: {
    type: Object,
    default: () => ({})
  }
});
const undoDisabled = ref(null);
let loading = ref(false);
async function updateFlow(data) {
  initializeGraph();
  bindGraphEvents();
  if (data) {
    nodeData.value = data;
    renderGraphs(graph, nodeData.value);
  } else {
    const res = await getTaskInfo(route.query.id);
    initializeGraph();
    bindGraphEvents();
    nodeData.value = res.data;
    renderGraphs(graph, nodeData.value);
  }

}
useVueNode(graph)
const fieldPreviewDialog = ref();
// const updateGraphNode = (list) => {
//   updateGraphNodes(graph, list);
// };
const updateGraphNode = (nodeInstanceList) => {
  if (!graph || !Array.isArray(nodeInstanceList)) return;
  const codeNodeMap = {};
  graph.getNodes().forEach((node) => {
    console.log("🚀 ~ updateGraphNode ~ node.getData():", node.getData())

    const code = node.getData()?.code;
    console.log("🚀 ~ updateGraphNode ~ code:", code)
    if (code) codeNodeMap[String(code)] = node;
  });
  nodeInstanceList.forEach((inst) => {
    const node = codeNodeMap[String(inst.nodeCode)];
    if (node) {
      const oldData = node.getData() || {};
      node.setData({ ...oldData, status: inst.status });
      console.log("🚀 ~ updateGraphNode ~ nodeInstanceList:", nodeInstanceList)

    }
  });
};
/**
 * 组件右键删除
 * @param {*}
 */
let selectedEdge = ref();

const title = ref("");

function initializeGraph() {
  graph = new Graph({
    container: proxy.$refs.graphContainer,
    width: "100%",
    height: "100%",
    grid: false,
    background: { color: "#ff0000" },
    autoResize: true,
    panning: false,
    interactive: {
      nodeMovable: () => false,
      edgeMovable: () => false,
      arrowMovable: () => false,
    }, interacting: false, mousewheel: {
      enabled: true,
      zoomAtMousePosition: true,
      minScale: 0.5,
      maxScale: 3,
    },
  });
  // 禁用 Dnd
  dnd = new Dnd({
    target: graph,
    scaled: false,
    validateNode: () => false,
  });
  // Selection 插件
  graph.use(
    new Selection({
      enabled: true,
      multiple: true,
      rubberband: true,
      movable: false,
      showNodeSelectionBox: true,
    })
  );
  graph.use(new Export());
  usePlugins(graph);
}
// 保存 没有code
const closeDialog = () => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // 根据组件 ID 删除组件
  }
  drawer.value = false;
};
// 绑定事件
function bindGraphEvents() {

  graph.on("node:added", handleNodeAdded);


  graph.on("node:dblclick", handleNodeDblClick);
  if (route.query.info) {
    graph.getPlugin('keyboard')?.disable();
  }
}


// / 处理节点添加事件
async function handleNodeAdded({ node }) {
  if (!node.data.code) {
    node.data.code = await fetchNodeUniqueKey();
  }
  if (!loading.value) {
    hasUnsavedChanges.value = true;
    currentNode.value = {};
    sourceNode.value = {};
    const nodeData = graph.getNodes();
    const nodeType = node.data.taskParams.type;
    if (nodeType == "1" || nodeType == "2") {
      const existingNode = nodeData.find(
        (item) => item.data.taskParams.type === nodeType && item.id !== node.id
      );
      if (existingNode) {
        handleExistingNode(node);
        return;
      }
    }
    if (nodeType !== "1") {
      handleNonInputNode(node);
    }
  }
}

// 处理已有节点的情况
function handleExistingNode(node) {
  if (node.data.taskParams.type == 2) {
    proxy.$message.warning(`只能有一个输出组件！`);
  } else if (node.data.taskParams.type == "1") {
    proxy.$message.warning(`只能有一个输入组件！`);
  }
  graph.removeNode(node.id);
}
// 处理非输入节点
function handleNonInputNode(node) {
  const edges = graph.getEdges();
  edges.forEach((edge) => {
    if (edge.getTargetNode() == node) {
      sourceNode.value = edge.getSourceNode();
    }
  });
  // drawer.value = true; // 控制抽屉显示
}
function handleNodeDblClick({ node }, type = 'edit') {
  graph.cleanSelection();
  hasUnsavedChanges.value = true;
  currentNode.value = node;
  drawer.value = true;
}
// 重置操作逻辑
const handleCancel = () => {
  proxy.$modal
    .confirm(`点击重置将清除所有未保存的更改，您确定要继续吗？`)
    .then(() => {
      // 刷新当前页签
      proxy.$tab.refreshPage(route);
    });
};
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
      exportGraphAsPNG(graph,); break;
    }
    case "reset": {
      handleCancel();
      break;
    }
  }
};

onMounted(() => {
  initializeGraph();
  bindGraphEvents();
});

const getAssetsFile = (url) => {
  return new URL(`/src/assets/dpp/etl/${url}`, import.meta.url).href;
};
defineExpose({ updateFlow, updateGraphNode })
</script>

<style scoped lang="less">
.app-containers {
  height: 100%;
  overflow: hidden;
}

.flex-container {
  display: flex;
  min-height: calc(100vh - 455px) !important;
  overflow: hidden;
}

.right-pane {
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;

  .graph-container {
    flex: 1;
    box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  }

  .toolbar {
    position: absolute;
    top: 0px;
    left: 0px;
    display: flex;
    align-items: center;
    z-index: 100;

    .toolbar-item {
      width: 34px;
      height: 32px;
      cursor: pointer;
      margin-right: 5px;
      display: flex;
      align-items: center;
      justify-content: center;

      img {
        width: 100%;
        height: 100%;
      }

      &:hover {
        background-color: rgba(255, 255, 255, 0.2);
      }
    }
  }
}

/* X6 图画布样式 */
:deep(.x6-widget-selection-box) {
  fill: rgba(0, 123, 255, 0.3);
  stroke: #007bff;
  opacity: 1;
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
