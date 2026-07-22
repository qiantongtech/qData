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
  <div class="app-container" ref="app-container">
    <div class="head-container">
      <div class="head-title">
        <el-tooltip :content="taskType" placement="top">
          <img
            :src="getDatasourceIcon(nodeData.draftJson)"
            alt=""
            style="width: 20px; margin-right: 5px; cursor: pointer"
          />
        </el-tooltip>
        {{ nodeData.name !== null ? nodeData.name : td('dpp.integratioTask.integrationTask', 'Integration Task') }}
      </div>

      <div class="head-btns">
        <el-button
          type="primary"
          size="small"
          :disabled="loading"
          @click="handleExportData(false)"
          v-if="!route.query.info"
        >
          <img src="@/assets/images/dpp/etl/icon-title-active-one.svg" alt="" />{{ td('dpp.developTaskDetail.taskSave', 'Task Save') }}
        </el-button>
        <el-button
          type="primary"
          plain
          size="small"
          :disabled="loading"
          @click="routeTo('/dpp/task/integratioTask', '')"
        >
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-three.svg" alt="" />
          <img
            class="act"
            src="@/assets/images/dpp/etl/icon-title-active-three.svg"
            alt=""
          />{{ td('dpp.developTaskDetail.taskCancel', 'Task Cancel') }}
        </el-button>
        <el-button
          type="primary"
          plain
          size="small"
          :disabled="loading"
          @click="openTaskConfigDialog"
          v-if="!route.query.info"
        >
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-four.svg" alt="" />
          <img
            class="act"
            src="@/assets/images/dpp/etl/icon-title-active-four.svg"
            alt=""
          />{{ td('dpp.developTaskDetail.taskConfig', 'Task Config') }}
        </el-button>
        <el-button
          type="primary"
          plain
          size="small"
          :disabled="loading"
          @click="openTaskConfigDialog"
          v-else
        >
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-four.svg" alt="" />
          <img
            class="act"
            src="@/assets/images/dpp/etl/icon-title-active-four.svg"
            alt=""
          />{{ td('dpp.developTaskDetail.taskDetail', 'Task Details') }}
        </el-button>
        <el-button
          type="primary"
          plain
          :disabled="loading"
          v-if="!route.query.info"
          size="small"
          @click="selectTab('checkMessage')"
        >
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-two.svg" alt="" />
          <img
            class="act"
            src="@/assets/images/dpp/etl/icon-title-active-two.svg"
            alt=""
          />{{ td('dpp.integratioTask.taskCheck', 'Task Check') }}
        </el-button>
        <!-- <el-button type="primary" size="small" @click="selectTab('log')">Execute it</el-button> -->
      </div>
    </div>
    <div class="flex-container">
      <!-- tree on left -->
      <div class="left-pane" v-if="!route.query.info">
        <div
          class="left-tree"
          :class="{ 'show-disabled-nodes': taskType === 'DATAX' }"
        >
          <el-tree
            :data="treeData"
            :empty-text="''"
            :props="{
              label: 'label',
              children: 'children',
              disabled: 'disabled',
            }"
            ref="deptTreeRef"
            default-expand-all
          >
            <template #default="{ node, data }">
              <div
                class="custom-tree-node"
                :class="{ 'is-disabled': data.disabled }"
                @mousedown="startDrag($event, node, data)"
              >
                <img
                  v-if="node.level === 1 && data.type == '1'"
                  src="@/assets/images/common/dpp/icon-srz.svg"
                  alt="icon"
                  class="icon-img"
                />
                <img
                  v-if="node.level === 1 && data.type == '3'"
                  src="@/assets/images/common/dpp/icon-zh1.svg"
                  alt="icon"
                  class="icon-img"
                />
                <img
                  v-if="node.level === 1 && data.type == '2'"
                  src="@/assets/images/common/dpp/icon-sc.svg"
                  alt="icon"
                  class="icon-img"
                />
                <img
                  v-if="data.icon"
                  :src="data.icon"
                  alt="icon"
                  class="icon-img"
                />
                <span class="treelable"> {{ data.label }}</span>
              </div>
            </template>
          </el-tree>
        </div>
      </div>
      <!-- Main content on the right -->
      <div class="right-pane" v-loading="loading">
        <div
          id="graphContainer"
          class="graph-container"
          ref="graphContainer"
        ></div>
        <div class="toolbar">
          <template v-for="(item, index) in toolbar" :key="item.id">
            <el-tooltip
              class="box-item"
              effect="light"
              :content="item.tip"
              placement="bottom"
              v-if="!(route.query.info && item.tip == td('dpp.integration.reset'))"
            >
              <div class="toolbar-item" @click="toolbarClick(item)">
                <img :src="getAssetsFile(item.icon)" alt="" />
              </div>
            </el-tooltip>
          </template>
        </div>
        <div class="tabs-container" :style="tabAreaStyle">
          <el-icon class="icon-right" @click="minimizeAction">
            <Minus />
          </el-icon>
          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane
              v-for="(tab, index) in tabs"
              :key="index"
              :name="tab.name"
            >
              <template #label>
                <span>{{ tab.label }}</span>
              </template>
              <div class="tab-content" v-html="tab.content"></div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
    <component
      :is="currentFormComponent"
      :visible="drawer"
      :key="currentNode?.id || Date.now()"
      :title="title"
      @update="closeDialog"
      @confirm="handleFormSubmit"
      :currentNode="currentNode"
      :info="route.query.info"
      :graph="graph"
    />
    <add
      :visible="taskConfigDialogVisible"
      :title="!route.query.info ? td('dpp.developTaskDetail.editTaskConfig', 'Edit Task Config') : td('dpp.developTaskDetail.taskDetail', 'Task Details')"
      @update:visible="taskConfigDialogVisible = $event"
      @save="handletaskConfig"
      :data="nodeData"
      :userList="userList"
      :deptOptions="deptOptions"
      :info="true"
    />
    <FieldPreviewDialog ref="fieldPreviewDialog" />
  </div>
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { Graph } from "@antv/x6";
import { Dnd } from "@antv/x6-plugin-dnd";
import { baseConfig, cuPort, typeList, toolbar } from "@/utils/graph";
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import FieldPreviewDialog from "@/views/dpp/task/integratioTask/components/fieldPreview.vue";
// input component
import InputForm from "@/views/dpp/task/integratioTask/components/input/tableForm.vue";
import excelInputForm from "@/views/dpp/task/integratioTask/components/input/excelForm.vue";
import csvForm from "@/views/dpp/task/integratioTask/components/input/csvForm.vue";

// Transform component
import DedupFilter from "@/views/dpp/task/integratioTask/components/transform/dedupFilter.vue";
import AddConstants from "@/views/dpp/task/integratioTask/components/transform/addConstants.vue";
import FieldSelectAndmodificat from "@/views/dpp/task/integratioTask/components/transform/fieldSelectAndmodificat.vue";
import ValueMapping from "@/views/dpp/task/integratioTask/components/transform/valueMapping.vue";

// Clean components
import TransformForm from "@/views/dpp/task/integratioTask/components/clean/cleanForm.vue";
// sorting component
import OrderConfig from "@/views/dpp/task/integratioTask/components/transform/orderConfig.vue";
// Field derivation period
import FieldBuilder from "@/views/dpp/task/integratioTask/components/transform/fieldBuilder.vue";
// Output table component
import OutputForm from "@/views/dpp/task/integratioTask/components/output/tableForm.vue";

import add from "../add//add.vue";
import useUserStore from "@/store/system/user";
import { deptUserTree } from "@/api/system/system/user.js";
import { Export } from "@antv/x6-plugin-export";
import { listAttTaskCat } from "@/api/att/cat/taskCat/taskCat";
const userStore = useUserStore();
// import {
//     createEtlTaskFrontPostposition,
//     dppEtlTask,
//     updateProcessDefinitions,
// } from "@/api/dpp/task/etlTask";
import {
  createEtlTaskFrontPostposition,
  etlTask,
  updateProcessDefinitions,
} from "@/api/dpp/task/index.js";
import { getTreeData } from "@/views/dpp/task/integratioTask/data.js";
import { Selection } from "@antv/x6-plugin-selection";
import { Keyboard } from "@antv/x6-plugin-keyboard";
import {
  useHtmlNode,
  showPorts,
  usePlugins,
  transNodeData,
  renderGraph,
  validateGraph,
  getAllChildNodes,
  createDataNode,
  fetchNodeUniqueKey,
  getParentNode,
  getDefaultTaskParams,
  createMenuDom,
  areFieldNamesEqual,
  shouldAbortByName,
  exportGraphAsPNG,
  renameRuleToRuleConfig,
} from "@/views/dpp/utils/opBase";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();
let id = route.query.id || 1;
// "edit": edit, "input": only look at input fields, "output": only look at output fields
// tooltip display content
const taskType = ref("");

//Get execution engine
const getTaskType = (json) => {
  if (!json) {
    return "SPARK";
  }
  let type = json && JSON.parse(json).taskType;
  return type;
};

// icon
const getDatasourceIcon = (json) => {
  let type = json && JSON.parse(json).taskType;
  taskType.value = type;
  switch (type) {
    case "FLINK":
      return new URL("@/assets/images/common/dpp/icon-flink.svg", import.meta.url)
        .href;
    case "SPARK":
      return new URL("@/assets/images/common/dpp/icon-spark.svg", import.meta.url)
        .href;
    case "DATAX":
      return new URL("@/assets/images/common/img-datax.png", import.meta.url)
          .href;
    default:
      return null;
  }
};
// Monitor id changes
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;
    if (id) {
      getList();
    }
  }
);
// Jump judgment
let hasUnsavedChanges = ref(false);
let nodeData = ref({ taskConfig: {}, name: null });
// Global Graph
let graph = null;
// global dnd
let dnd = null;
// drawer
const drawer = ref(false);
// Task configuration
const taskConfigDialogVisible = ref(false);
// Store information about the current component
const currentNode = ref({});
// Information about the parent component of the current component
const sourceNode = ref({});
const currentFormComponent = computed(() => {
  if (!drawer.value || !currentNode.value) return null;
  const componentType = currentNode.value?.data?.componentType || "";
  switch (componentType) {
    case "1":
      return InputForm;
    case "2":
      return excelInputForm;

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
//Left component data
const treeData = ref([]);
// Undo button
const undoDisabled = ref(null);
// Exported data
const exportData2 = ref("");
let loading = ref(false);
function getList() {
  loading.value = true;
  etlTask(route.query.id).then((response) => {
    nodeData.value = response.data;
    nodeData.value.taskConfig = {
      ...nodeData.value.taskConfig,
      draftJson: nodeData.value.draftJson,
    };
    renderGraph(graph, nodeData.value);
    const currentTaskType = getTaskType(nodeData.value.draftJson);
    taskType.value = currentTaskType;
    treeData.value = [...getTreeData(currentTaskType)];
    loading.value = false;
  });
}
let userList = ref([]);
let deptOptions = ref([]);
function getDeptTree() {
  listAttTaskCat({
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    validFlag: true,
  }).then((response) => {
    deptOptions.value = [];
    var children = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('dpp.integratioTask.dataIntegrationCategory', 'Data Integration Category'),
        value: "",
        id: 0,
        children: children,
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
// Save without code
const closeDialog = () => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // Remove component based on component ID
  }
  drawer.value = false;
};

watch(
  () => userStore.projectCode,
  () => {
    getDeptTree();
  }
);
// Pop-up window to save
const handleFormSubmit = async (nodeData = {}) => {
  if (!currentNode?.value?.id) return;
  const node = graph.getCellById(currentNode.value.id);
  if (!node) return;

  const oldData = node.getProp("data") || {};
  const parent = getParentNode(currentNode.value, graph);
  const childNodes = getAllChildNodes(node, graph) || [];

  // Determine whether the node name is repeated
  if (shouldAbortByName(graph, nodeData)) {
    drawer.value = true;
    proxy.$message.warning(
      td('dpp.integratioTask.nodeNameExists', 'Node name') + `”${currentNode.value.data.name}”` + td('dpp.integratioTask.nodeNameExistsSuffix', 'already exists, please modify and save')
    );
    return;
  }

  const oldName = oldData.name || "";
  const newName = nodeData.name || "";
  const nameChanged = oldName !== newName;

  const taskParams = nodeData.taskParams || {};
  const type = taskParams.type;
  const tableFields = taskParams.tableFields || [];

  let inputFields = [];
  let outputFields = [];

  if (type == 1) {
    inputFields = tableFields.map((field) => ({
      ...field,
      source: nodeData.name || "",
    }));
    outputFields = inputFields;
  } else if (type == 2) {
    // type 2: with input and independent output
    if (parent?.data?.taskParams?.outputFields) {
      inputFields = parent.data.taskParams.outputFields || [];
      outputFields = (taskParams.outputFields || []).map((field) => ({
        ...field,
        source: nodeData.name || "",
      }));
    }
  } else {
    // Other types, general processing
    if (parent?.data?.taskParams?.outputFields) {
      inputFields = parent.data.taskParams.outputFields || [];
      outputFields = taskParams.outputFields || [];
    }
  }

  // If the node name is modified, replace source in inputFields/outputFields
  if (nameChanged) {
    const replaceSourceName = (fields) =>
      fields.map((f) => ({
        ...f,
        source: f.source === oldName ? newName : f.source,
      }));

    inputFields = replaceSourceName(inputFields);
    outputFields = replaceSourceName(outputFields);
  }

  const oldOutputs = oldData.taskParams?.outputFields || [];
  const outputsChanged = !areFieldNamesEqual(outputFields, oldOutputs);

  // Merge and update current node data
  const newData = {
    ...oldData,
    ...nodeData,
    taskParams: {
      ...oldData.taskParams,
      ...taskParams,
      inputFields,
      outputFields,
    },
  };

  const needConfirm =
    outputsChanged && oldOutputs.length > 0 && childNodes.length > 0;
  if (needConfirm && type == 1) {
    try {
      await ElMessageBox.confirm(
        td('dpp.integratioTask.clearChildNodesWarning', 'Modifying fields will clear all child node field configs. Continue?'),
        { type: "warning", distinguishCancelAndClose: true }
      );
    } catch (e) {
      return (drawer.value = true);
    }
  }

  node.setProp("data", newData);

  const newInputFields = outputFields;

  if (outputsChanged) {
    // Only when the output field actually changes will the child nodes be cleared.
    childNodes.forEach((n) => {
      if (!n?.data?.taskParams || typeof n.setProp !== "function") return;
      const defaultParams = getDefaultTaskParams(n.data);
      n.setProp("data", {
        ...n.data,
        taskParams: {
          ...n.data.taskParams,
          ...defaultParams,
          inputFields: newInputFields,
        },
      });
    });
  } else if (nameChanged) {
    // Only the name changes, the source of inputFields is updated, but the child node tableFields is not cleared.
    childNodes.forEach((n) => {
      if (!n?.getProp || !n.getProp) return;
      const childData = n.getProp("data") || {};
      if (!childData?.taskParams) return;

      const updatedTaskParams = {
        ...childData.taskParams,
        inputFields: childData.taskParams.inputFields.map((f) => ({
          ...f,
          source: f.source === oldName ? newName : f.source,
        })),
      };

      n.setProp("data", {
        ...childData,
        taskParams: updatedTaskParams,
      });
    });
  }

  drawer.value = false;
};

// Run instance save
const handletaskConfig = (form) => {
  delete form.draftJson;
  console.log("🚀 ~ handletaskConfig ~ form:", form);
  nodeData.value.taskConfig = {
    ...form,
    draftJson: JSON.stringify(form),
  };
};
// Great save
const handleExportData = async (localSave) => {
  loading.value = true;
  try {
    // Check
    if (!localSave) {
      const { isValid } = validateGraph(graph);
      if (!isValid) {
        return;
      }
    }
    // Check if there is a task configuration
    if (!hasTaskConfig(nodeData.value)) {
      taskConfigDialogVisible.value = true;
      return;
    }
    // Convert node data
    exportData2.value = await transNodeData(graph);
    // Integrate task-related configuration into exportData2
    exportData2.value = {
      ...exportData2.value,
      taskRelationJson: JSON.stringify(exportData2.value.taskRelationJson),
      taskDefinitionList: JSON.stringify(exportData2.value.taskDefinitionList),
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
      ...nodeData.value?.taskConfig,
    };
    // Determine whether to update or create
    const res =
      localSave || (nodeData.value?.dsId > 0 || nodeData.value?.quartzId > 0)
        ? await updateProcessDefinitions(
            {
              ...exportData2.value,
              id: nodeData.value.id,
            },
            { localSave }
          )
        : await createEtlTaskFrontPostposition({
            ...exportData2.value,
            id: nodeData.value.id,
          });
    // Processing after success
    if (res.code == "200") {
      handleSuccess();
    } else {
      proxy.$modal.msgWarning(td('dpp.integratioTask.operationFailed', 'Operation failed, please contact administrator'));
    }
  } finally {
    loading.value = false;
  }
};
// Determine whether there is a task configuration
const hasTaskConfig = (nodeData) => {
  return nodeData?.taskConfig && Object.keys(nodeData.taskConfig).length > 0;
};
// Page jump
const handleSuccess = () => {
  taskConfigDialogVisible.value = false;
  hasUnsavedChanges.value = false;
  const message = td('common.message.msgOpSuccess', 'Operation successful');
  router.push("/dpp/task/integratioTask");
  proxy.$modal.msgSuccess(message);
};

// Task configuration pop-up window
const openTaskConfigDialog = () => {
  taskConfigDialogVisible.value = true;
};
useHtmlNode();
const startDrag = (e, treeNode, data) => {
  // Get all nodes in the graph
  const nodes = graph.getCells().filter((cell) => {
    return cell.isNode();
  });

  if (treeNode.level === 2) {
    if (route.query?.info)
      return proxy.$modal.msgWarning(td('dpp.integratioTask.nodeNotEditable', 'Not editable, current page is view only'));
    if (data.disabled) return;
    if (!data.componentType)
      return proxy.$modal.msgWarning(td('dpp.integratioTask.nodeDeveloping', 'Under development, stay tuned'));
    const node = createDataNode(graph, data);
    dnd.start(node, e);
  }
};
const fieldPreviewDialog = ref();
const openDialog = (node, data, title) => {
  fieldPreviewDialog.value.show(node, data, title);
};
/**
 * Right click on component to delete
 * @param {*}
 */
let selectedEdge = ref();
// Define a method to clear the color of the currently selected edge
function clearSelectedEdge() {
  if (selectedEdge.value) {
    selectedEdge.value.prop("attrs/line/stroke", "#2666FB");
    selectedEdge.value = null;
  }
}
const title = ref("");
function bindShortcuts(graph) {
  graph.use(new Keyboard());
  graph.bindKey(["delete", "backspace"], () => {
    const cells = graph.getSelectedCells();
    if (cells.length > 0) {
      handleDeleteCells(graph, cells);
      console.log("444");
    }
    return false;
  });
}
function initializeGraph() {
  graph = new Graph({
    container: proxy.$refs.graphContainer,
    width: "100%",
    height: "60%",
    grid: false, //grid
    background: { color: "#ff0000" },
    autoResize: true, //Canvas adaptive
    panning: true, //Canvas drag
    interactive: true,
    ...baseConfig,
    //Canvas zoom
    mousewheel: {
      enabled: true,
      zoomAtMousePosition: true,
      minScale: 0.5,
      maxScale: 3,
    },
    selecting: {
      enabled: true,
      className: "my-selecting", // Customize selected style class name
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
  graph.use(new Export());

  // plug-in
  bindShortcuts(graph);
  usePlugins(graph);

  undoDisabled.value = graph.canUndo();
}

// Binding events
function bindGraphEvents() {
  // Listen for side click events
  graph.on("edge:click", handleEdgeClick);

  graph.on("blank:click", clearSelectedEdge);

  graph.on("node:added", handleNodeAdded);

  graph.on("node:mouseenter", () => togglePortsVisibility(true));

  // Node mouse leave event
  graph.on("node:mouseleave", () => togglePortsVisibility(false));
  if (!route.query.info) {
    graph.on("node:contextmenu", handleNodeContextMenu);
  }

  // Connect edge events
  graph.on("edge:connected", handleEdgeConnected);

  graph.on("edge:contextmenu", handleEdgeContextMenu);

  graph.on("node:dblclick", handleNodeDblClick);
  if (route.query.info) {
    graph.getPlugin("keyboard")?.disable();
  }
  if (!route.query.info) {
    // Remove the panning plug-in or unbind the drag event
    graph.off("blank:mousedown"); // Unbind the drag event of the blank area
    graph.off("blank:mousemove");
    graph.off("blank:mouseup");
  }
}
// Handling edge click events
function handleEdgeClick({ cell }) {
  const currentColor = cell.prop("attrs/line/stroke");
  clearSelectedEdge();

  const newColor = currentColor === "#f14f10" ? "#2666FB" : "#f14f10";
  cell.prop("attrs/line/stroke", newColor);

  selectedEdge.value = cell;
}

// / Handle node addition event
async function handleNodeAdded({ node }) {
  if (!node.data.code) {
    node.data.code = await fetchNodeUniqueKey({ scheduler: nodeData.value.taskConfig.scheduler});
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
    proxy.$message.warning(td('dpp.integratioTask.onlyOneOutputComponent', 'Only one output component allowed!'));
  } else if (node.data.taskParams.type == "1") {
    proxy.$message.warning(td('dpp.integratioTask.onlyOneInputComponent', 'Only one input component allowed!'));
  }
  graph.removeNode(node.id);
}
// Handle non-input nodes
function handleNonInputNode(node) {
  const edges = graph.getEdges();
  edges.forEach((edge) => {
    if (edge.getTargetNode() == node) {
      sourceNode.value = edge.getSourceNode(); // Get data from superior components
      // drawer.value = true; //Control drawer display
    }
  });
  // drawer.value = true; //Control drawer display
}

// Switch the display status of a port
function togglePortsVisibility(visible) {
  const container = document.getElementById("graphContainer");
  const ports = container.querySelectorAll(".x6-port-body");
  showPorts(ports, visible);
}
/**
 * Unify the logic of deleting nodes or connecting lines
 */
function handleDeleteCells(graph, cells, menuController) {
  if (!cells || cells.length === 0) {
    ElMessageBox.warning(td('dpp.integratioTask.deleteNothingSelected', 'Operation failed, no node or line selected for deletion'));
    return;
  }

  const target = cells[0];
  const isEdge = target.isEdge?.();
  const isNode = target.isNode?.();

  let message = td('dpp.integratioTask.deleteLineWarning', 'Deleting this line will clear all child node field configs. Continue?');

  let sourceNode = null;

  if (isNode) {
    sourceNode = target;

    const childNodes = getAllChildNodes(sourceNode, graph);
    const hasChildNodes = childNodes.length > 0;

    message = hasChildNodes
      ? td('dpp.integratioTask.deleteNodeWarning', 'Deleting this node will clear all child node field configs. Continue?')
      : td('dpp.integratioTask.confirmDeleteNode', 'Are you sure to delete this node?');
  }

  if (isEdge) {
    sourceNode = target.getSourceCell?.();
  }

  ElMessageBox.confirm(message, td('dpp.integratioTask.confirmDelete', 'Are you sure to delete data integration task with ID "{ids}"?'), {
    confirmButtonText: td('dpp.integratioTask.confirm', 'Confirm'),
    cancelButtonText: td('common.button.cancel', 'Cancel'),
    type: "warning",
  })
    .then(() => {
      // Reset child node configuration (no matter node or edge)
      if (sourceNode) {
        const childNodes = getAllChildNodes(sourceNode, graph);
        childNodes.forEach((n) => {
          if (n.data?.taskParams) {
            const defaultParams = getDefaultTaskParams(n.data);
            n.data.taskParams = {
              ...n.data.taskParams,
              ...defaultParams,
            };
          }
        });
      }

      // Perform delete operation
      cells.forEach((cell) => {
        if (cell.isNode?.()) {
          graph.removeNode(cell);
        } else if (cell.isEdge?.()) {
          graph.removeEdge(cell);
        }
      });

      menuController?.hide?.();
    })
    .catch(() => {
      menuController?.hide?.();
    });
}

// Handling node right-click menu events
function handleNodeContextMenu({ e, node, edge, type = 0 }) {
  e.preventDefault();

  const pos = graph.clientToGraph(e.clientX, e.clientY);
  const container = document.getElementById("graphContainer");

  let menuController = null;

  const menuItems = [
    {
      label: td('dpp.integratioTask.deleteNode', 'Delete Node'),
      action: () => {
        // Here, an array of a single node or edge is passed in, and the menuController is passed in for closing the menu.
        handleDeleteCells(graph, [type === 0 ? node : edge], menuController);
      },
    },
    {
      label: td('dpp.integratioTask.editNode', 'Edit Node'),
      action: () => {
        handleNodeDblClick({ node }, "edit");
        menuController?.hide();
      },
    },
  ];

  if (node.data.taskParams.type != 1) {
    menuItems.push({
      label: td('dpp.integratioTask.showInputFields', 'Show Input Fields'),
      action: () => {
        const input = node.data.taskParams.inputFields;
        if (!Array.isArray(input) || input.length == 0) {
          ElMessage.warning(td('dpp.integratioTask.cannotFindInputFields', 'Cannot find input fields'));
        } else {
          openDialog(input, node, td('dpp.integratioTask.inputFields', 'Input Fields'));
        }
        menuController?.hide();
      },
    });
  }

  menuItems.push({
    label: td('dpp.integratioTask.showOutputFields', 'Show Output Fields'),
    action: () => {
      const output = node.data.taskParams.outputFields;
      if (!Array.isArray(output) || output.length === 0) {
        ElMessage.warning(td('dpp.integratioTask.cannotFindOutputFields', 'Cannot find output fields'));
      } else {
        openDialog(output, node, td('dpp.integratioTask.outputFields', 'Output Fields'));
      }
      menuController?.hide();
    },
  });

  menuController = createMenuDom({
    x: pos.x,
    y: pos.y,
    menuItems,
    container,
  });
}

// Handle connection edge events
function handleEdgeConnected({ edge }) {
  if (!loading.value) {
    hasUnsavedChanges.value = true;
    const source = edge.getSourceCell();
    const target = edge.getTargetCell();
    // Check if the source node and target node are the same
    if (source === target) {
      graph.removeEdge(edge); // remove edge
      proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorSelf', 'Connection error, node cannot connect to itself'));
      return;
    }

    // Get the taskParams.type of the source node and target node
    const sourceType = source.data?.taskParams?.type;
    const targetType = target.data?.taskParams?.type;

    // Type 1 cannot be used as a target node
    if (targetType == 1) {
      graph.removeEdge(edge); // remove edge
      proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorInput', 'Connection error, input component cannot be connected'));
      return;
    }

    // Type 2 cannot be used as an input node (source node)
    if (sourceType == 2) {
      graph.removeEdge(edge); // remove edge
      proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorOutput', 'Connection error, output component cannot connect to other components'));
      return;
    }

    // Type 2 nodes can only be connected once as output nodes
    if (targetType == 2) {
      const targetEdges = graph
        .getEdges()
        .filter((e) => e.getTargetCell() === target);
      if (targetEdges.length > 1) {
        graph.removeEdge(edge); // remove edge
        proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorOutputOnce', 'Connection error, target node can only be output connected once'));
        return;
      }
    }

    updateTargetNodeData(source, target, edge);
  }
}
// Update the data of the target node
function updateTargetNodeData(source, target, edge) {
  const childNodes = getAllChildNodes(source, graph);

  // Update the data of child nodes
  childNodes.forEach((childNode) => {
    if (childNode.data?.taskParams) {
      childNode.data.taskParams.inputFields =
        source.data.taskParams.outputFields;
      childNode.data.taskParams.tableFields = [];
      childNode.data.taskParams.outputFields =
        source.data.taskParams.inputFields;
      childNode.data = { ...childNode.data };
    }
  });

  const needBindCleanRule =
    source.data.componentType == 1 &&
    source.data.taskParams?.clmt != 2 &&
    target.data.componentType == 31 &&
    edge;

  if (needBindCleanRule) {
    ElMessageBox.confirm(
      td('dpp.integratioTask.addCleanRulePrompt', 'Add clean rules bound to input component for transform component?'),
      td('common.message.prompt', 'Prompt'),
      {
        confirmButtonText: td('dpp.integratioTask.yes', 'Yes'),
        cancelButtonText: td('dpp.integratioTask.no', 'No'),
        type: "warning",
      }
    )
      .then(() => {
        // Call method to generate rule configuration
        const result = renameRuleToRuleConfig(
          target.data.taskParams.inputFields
        );
        console.log("🚀 ~ updateTargetNodeData ~ result:", result);
        proxy.$message.success(td('dpp.integratioTask.cleanRuleAdded', 'Added {count} clean rules') + ` ${result?.length || 0} ` + td('dpp.integratioTask.cleanRuleAddedSuffix', 'items'));
        // Assign a value to the target node
        if (target.data?.taskParams) {
          target.data.taskParams.tableFields = result;
          target.data = { ...target.data };
        }
      })
      .catch(() => {});
  }
}

// Handling side right-click menu events
function handleEdgeContextMenu(event) {
  const edge = event.edge;
  const { x, y } = event;
  let menuController = null;
  const menuItems = [
    {
      label: td('dpp.integratioTask.deleteLine', 'Delete Connection Line'),
      action: () => {
        handleDeleteCells(graph, [edge], menuController);
      },
    },
  ];
  const container = document.getElementById("graphContainer");

  createMenuDom({
    x,
    y,
    menuItems,
    container,
  });
}
function handleNodeDblClick({ node }, type = "edit") {
  graph.cleanSelection();
  clearSelectedEdge();
  hasUnsavedChanges.value = true;
  currentNode.value = node;
  drawer.value = true;
}
// Reset operation logic
const handleCancel = () => {
  proxy.$modal
    .confirm(td('dpp.integratioTask.resetWarning', 'Clicking reset will clear all unsaved changes. Continue?'))
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
// initialization function
onMounted(async () => {
  if (userStore.projectId) {
    getDeptTree();
  }
  await initializeGraph();
  bindGraphEvents();
  if (route.query.id) {
    getList();
  }
});
// Prompt when leaving the page
onBeforeRouteLeave((to, from, next) => {
  // Check for unsaved changes
  if (hasUnsavedChanges.value) {
    ElMessageBox.confirm(
      td('dpp.integratioTask.discardWarning', 'You have edited some task content. Discard edited content?'), // Prompt message
      td('common.message.prompt', 'Prompt'), // Title
      {
        confirmButtonText: td('common.button.save', 'Save'), // Confirm button text
        cancelButtonText: td('dpp.integratioTask.discard', 'Discard'), // Cancel button text
        type: "warning", // Popup type
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
// Used to control the currently selected tag
const activeTab = ref("checkMessage");
const tabs = ref([
  { name: "checkMessage", label: td('dpp.integratioTask.checkMessage', 'Check Message'), content: td('dpp.integratioTask.checkMessageContent', 'Check message content') },
  // { name: "log", label: "log", content: "log content" },
]);
// Used to style the label area
const tabAreaStyle = ref({
  position: "absolute",
  height: "300px",
  width: "100%",
  transition: "bottom 0.3s",
  bottom: "-9999px",
  "background-color": "rgb(255, 255, 255)",
  "margin-left": "15px",
});
// task check
const minimizeAction = () => {
  tabAreaStyle.value.bottom = "-9999px";
}; // Switch to task inspection tab
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
    message += td('dpp.integratioTask.checkFailed', 'Check failed:') + "<br>" + errorMessages.join("<br>");
  } else {
    message += td('dpp.integratioTask.checkPassed', 'Check passed');
  }
  if (
    !nodeData.value?.taskConfig ||
    Object.keys(nodeData.value.taskConfig).length === 0
  ) {
    message += "<br>" + td('dpp.integratioTask.taskConfigNotFilled', 'Task config not filled');
  }

  tabs.value[0].content = message;
};
const getAssetsFile = (url) => {
  return new URL(`/src/assets/images/dpp/etl/${url}`, import.meta.url).href;
};
</script>

<style scoped lang="less">
:deep(.tip-icon) {
  color: #888;
  font-size: 14px;
  margin-left: 5px;
}

.app-container {
  height: calc(87vh - 7px);
  overflow: hidden;

  .tabs-container {
    position: relative;
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
      color: #000000;
      display: flex;
      align-items: center;
    }

    .head-btns {
      img {
        margin-right: 6px;
      }

      .currImg {
        display: inline-block;
      }

      .act {
        display: none;
      }

      .el-button {
        height: 28px;

        &:hover {
          .act {
            display: inline-block;
          }

          .currImg {
            display: none;
          }
        }
      }
    }
  }
}

.flex-container {
  display: flex;
  height: calc(87vh - 60px);
  overflow: hidden;
}

.left-pane {
  width: 274px;
  background-color: #fff;
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  margin-right: 15px;

  .left-tree {
    padding: 15px;
    flex: 1;
    overflow-y: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;

    &:not(.show-disabled-nodes) {
      :deep(.el-tree-node[aria-disabled="true"]) {
        display: none;
      }
    }
  }

  .icon-img {
    width: 15px;
    height: 15px;
  }
}

.right-pane {
  //min-height: 864px;
  width: 20vw;
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;

  .toolbar {
    position: absolute;
    top: 16px;
    right: unset;
    bottom: unset;
    left: 20px;
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
    flex: 1;
    min-height: 70vh;
    box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  }

  .tabs-container {
    position: relative;

    .icon-right {
      position: absolute;
      top: 10px;
      right: 30px;
      font-size: 20px;
      color: #666;
      z-index: 10000;
      cursor: pointer;
    }
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

  &.is-disabled {
    cursor: not-allowed;
    opacity: 0.45;
  }
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

.graph-container {
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1) !important;
  min-height: 70vh !important;
}

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
