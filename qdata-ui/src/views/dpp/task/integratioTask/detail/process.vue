<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="app-containers" ref="app-container">
    <div class="flex-container">
      <!-- Main content on the right -->
      <div class="right-pane" v-loading="loading">
        <el-empty
          :description="td('common.noTaskProcess')"
          v-if="!nodeData?.locations || nodeData.locations.length === 0"
        >
        </el-empty>
        <div
          id="graphContainers"
          class="graph-container"
          ref="graphContainers"
        ></div>
        <TeleportContainer />
        <!-- Toolbar -->
        <div class="toolbar" v-if="nodeData?.locations">
          <template v-for="item in toolbar" :key="item.id">
            <el-tooltip
              class="box-item"
              effect="light"
              :content="item.tip"
              placement="bottom"
              v-if="item.tip !== td('dpp.integration.reset') && item.tip !== td('dpp.integration.export')"
            >
              <div class="toolbar-item" @click="toolbarClick(item)">
                <img :src="getAssetsFile(item.icon)" alt="" />
              </div>
            </el-tooltip>
          </template>
        </div>
      </div>
    </div>
    <!-- dynamic form -->
    <component
      :is="currentFormComponent"
      :visible="drawer"
      :key="currentNode?.id || Date.now()"
      :title="title"
      @update="closeDialog"
      :currentNode="currentNode"
      :info="route.query.info"
      :graph="graph"
    />
    <!-- Field preview pop-up window -->
    <FieldPreviewDialog ref="fieldPreviewDialog" />
  </div>
</template>
<script setup name="process">
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import { Graph } from "@antv/x6";
import { Dnd } from "@antv/x6-plugin-dnd";
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
// input component
import InputForm from "@/views/dpp/task/integratioTask/components/input/tableForm.vue";
import excelInputForm from "@/views/dpp/task/integratioTask/components/input/excelForm.vue";
import csvForm from "@/views/dpp/task/integratioTask/components/input/csvForm.vue";
// src/views/dpp/task/integratioTask/components/transform/dedupFilter.vue
import DedupFilter from "@/views/dpp/task/integratioTask/components/transform/dedupFilter.vue";
import AddConstants from "@/views/dpp/task/integratioTask/components/transform/addConstants.vue";
import FieldSelectAndmodificat from "@/views/dpp/task/integratioTask/components/transform/fieldSelectAndmodificat.vue";
import ValueMapping from "@/views/dpp/task/integratioTask/components/transform/valueMapping.vue";
// Transform component
// Clean components
import TransformForm from "@/views/dpp/task/integratioTask/components/clean/cleanForm.vue";
// sorting component
import OrderConfig from "@/views/dpp/task/integratioTask/components/transform/orderConfig.vue";
// Field derivation period
import FieldBuilder from "@/views/dpp/task/integratioTask/components/transform/fieldBuilder.vue";
// Output table component
import OutputForm from "@/views/dpp/task/integratioTask/components/output/tableForm.vue";
import useUserStore from "@/store/system/user";
import { Export } from "@antv/x6-plugin-export";
import { etlTask } from "@/api/dpp/task/index.js";
import { register, getTeleport } from "@antv/x6-vue-shape";
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
let nodeData = ref({ taskConfig: {}, name: null });
let graph = null;
let dnd = null;
const drawer = ref(false);
const addVisible = ref(false);
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
    default: () => ({}),
  },
});
const undoDisabled = ref(null);
let loading = ref(false);
function getList() {
  // If the detail passed by the parent component already contains draftJson, use it directly.
  if (props.dppEtlTaskDetail?.draftJson) {
    nodeData.value = props.dppEtlTaskDetail;
    renderGraphs(graph, nodeData.value, 2);
    loading.value = false;
    return;
  }
  if (!route.query.id) return;
  // Otherwise, go to the interface to pull it.
  etlTask(route.query.id).then((response) => {
    nodeData.value = response.data;
    nodeData.value.taskConfig = {
      ...nodeData.value.taskConfig,
      draftJson: nodeData.value.draftJson,
    };
    renderGraphs(graph, nodeData.value, 2);
  });
}

useVueNode(graph);
const fieldPreviewDialog = ref();
const openDialog = (node, data, title) => {
  fieldPreviewDialog.value.show(node, data, title);
};
/**
 * Right click on component to delete
 * @param {*}
 */
let selectedEdge = ref();

const title = ref("");

function initializeGraph() {
  graph = new Graph({
    container: proxy.$refs.graphContainers,
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
    },
    interacting: false,
    mousewheel: {
      enabled: true,
      zoomAtMousePosition: true,
      minScale: 0.5,
      maxScale: 3,
    },
  });
  // Disable Dnd
  dnd = new Dnd({
    target: graph,
    scaled: false,
    validateNode: () => false,
  });
  // Selection plugin
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
// Save without code
const closeDialog = () => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // Remove component based on component ID
  }
  drawer.value = false;
};
// Binding events
function bindGraphEvents() {
  graph.on("node:added", handleNodeAdded);

  graph.on("node:dblclick", handleNodeDblClick);
  if (route.query.info) {
    graph.getPlugin("keyboard")?.disable();
  }
}

// / Handle node addition event
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

    // currentNode.value = node;
    // drawer.value = true;
  }
}

// Handle the situation of existing nodes
function handleExistingNode(node) {
  if (node.data.taskParams.type == 2) {
    proxy.$message.warning(td('dpp.utils.onlyOneOutputComponent'));
  } else if (node.data.taskParams.type == 1) {
    proxy.$message.warning(td('dpp.utils.onlyOneInputComponent'));
  }
  graph.removeNode(node.id);
}
// Handle non-input nodes
function handleNonInputNode(node) {
  const edges = graph.getEdges();
  edges.forEach((edge) => {
    if (edge.getTargetNode() == node) {
      sourceNode.value = edge.getSourceNode();
    }
  });
  // drawer.value = true; //Control drawer display
}
function handleNodeDblClick({ node }, type = "edit") {
  graph.cleanSelection();
  hasUnsavedChanges.value = true;
  currentNode.value = node;
  drawer.value = true;
}
// Reset operation logic
const handleCancel = () => {
  proxy.$modal
    .confirm(td('dpp.utils.resetConfirm'))
    .then(() => {
      // Refresh the current tab
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
      exportGraphAsPNG(graph);
      break;
    }
    case "reset": {
      handleCancel();
      break;
    }
  }
};
async function updateFlow(data) {
  initializeGraph();
  bindGraphEvents();
  nodeData.value = { ...data, ...nodeData.value };
  renderGraphs(graph, nodeData.value, 2);
}
onActivated(() => {});
watch(
  () => props.dppEtlTaskDetail,
  (newVal) => {
    if (newVal) {
      getList();
    }
  },
  { immediate: true, deep: true } // immediate guarantees that it will be triggered for the first time
);
const getAssetsFile = (url) => {
  return new URL(`/src/assets/images/dpp/etl/${url}`, import.meta.url).href;
};
defineExpose({ updateFlow });
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

/* X6 chart canvas style */
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
