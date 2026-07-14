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
        {{ nodeData.name !== null ? nodeData.name : td('dpp.integratioTask.integrationTask', '集成任务') }}
      </div>

      <div class="head-btns">
        <el-button
          type="primary"
          size="small"
          :disabled="loading"
          @click="handleExportData(false)"
          v-if="!route.query.info"
        >
          <img src="@/assets/images/dpp/etl/icon-title-active-one.svg" alt="" />{{ td('dpp.developTaskDetail.taskSave', '任务保存') }}
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
          />{{ td('dpp.developTaskDetail.taskCancel', '任务取消') }}
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
          />{{ td('dpp.developTaskDetail.taskConfig', '任务配置') }}
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
          />{{ td('dpp.developTaskDetail.taskDetail', '任务详情') }}
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
          />{{ td('dpp.integratioTask.taskCheck', '任务检查') }}
        </el-button>
        <!-- <el-button type="primary" size="small" @click="selectTab('log')">执行一下</el-button> -->
      </div>
    </div>
    <div class="flex-container">
      <!-- 左侧树 -->
      <div class="left-pane" v-if="!route.query.info">
        <div class="left-tree">
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
      <!-- 右侧主内容 -->
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
      :title="!route.query.info ? td('dpp.developTaskDetail.editTaskConfig', '修改任务配置') : td('dpp.developTaskDetail.taskDetail', '任务详情')"
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
// 输入组件
import InputForm from "@/views/dpp/task/integratioTask/components/input/tableForm.vue";
import excelInputForm from "@/views/dpp/task/integratioTask/components/input/excelForm.vue";
import csvForm from "@/views/dpp/task/integratioTask/components/input/csvForm.vue";

// 转换组件
import DedupFilter from "@/views/dpp/task/integratioTask/components/transform/dedupFilter.vue";
import AddConstants from "@/views/dpp/task/integratioTask/components/transform/addConstants.vue";
import FieldSelectAndmodificat from "@/views/dpp/task/integratioTask/components/transform/fieldSelectAndmodificat.vue";
import ValueMapping from "@/views/dpp/task/integratioTask/components/transform/valueMapping.vue";

// 清洗组件
import TransformForm from "@/views/dpp/task/integratioTask/components/clean/cleanForm.vue";
// 排序组件
import OrderConfig from "@/views/dpp/task/integratioTask/components/transform/orderConfig.vue";
// 字段派生期
import FieldBuilder from "@/views/dpp/task/integratioTask/components/transform/fieldBuilder.vue";
// 输出表组件
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
// "edit"：编辑，"input"：只看输入字段，"output"：只看输出字段
// tooltip 显示内容
const taskType = ref("");

//获取执行引擎
const getTaskType = (json) => {
  if (!json) {
    return "SPARK";
  }
  let type = json && JSON.parse(json).taskType;
  return type;
};

// 图标
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
    default:
      return null;
  }
};
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
//左侧组件数据
const treeData = ref([]);
// 撤销按钮
const undoDisabled = ref(null);
// 导出的数据
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
    treeData.value = [...getTreeData(getTaskType(nodeData.value.draftJson))];
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
        name: td('dpp.integratioTask.dataIntegrationCategory', '数据集成类目'),
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
// 保存 没有code
const closeDialog = () => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // 根据组件 ID 删除组件
  }
  drawer.value = false;
};

watch(
  () => userStore.projectCode,
  () => {
    getDeptTree();
  }
);
// 弹窗保存
const handleFormSubmit = async (nodeData = {}) => {
  if (!currentNode?.value?.id) return;
  const node = graph.getCellById(currentNode.value.id);
  if (!node) return;

  const oldData = node.getProp("data") || {};
  const parent = getParentNode(currentNode.value, graph);
  const childNodes = getAllChildNodes(node, graph) || [];

  // 判断节点名称是否重复
  if (shouldAbortByName(graph, nodeData)) {
    drawer.value = true;
    proxy.$message.warning(
      td('dpp.integratioTask.nodeNameExists', '节点名称') + `”${currentNode.value.data.name}”` + td('dpp.integratioTask.nodeNameExistsSuffix', '已存在，请修改后再保存')
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
    // type 2：有输入和独立输出
    if (parent?.data?.taskParams?.outputFields) {
      inputFields = parent.data.taskParams.outputFields || [];
      outputFields = (taskParams.outputFields || []).map((field) => ({
        ...field,
        source: nodeData.name || "",
      }));
    }
  } else {
    // 其他类型，通用处理
    if (parent?.data?.taskParams?.outputFields) {
      inputFields = parent.data.taskParams.outputFields || [];
      outputFields = taskParams.outputFields || [];
    }
  }

  // 如果节点名称修改了，替换 inputFields/outputFields 中的 source
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

  // 合并更新当前节点数据
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
        td('dpp.integratioTask.clearChildNodesWarning', '修改字段将会同时清空所有子节点的字段配置，是否确认继续？'),
        { type: "warning", distinguishCancelAndClose: true }
      );
    } catch (e) {
      return (drawer.value = true);
    }
  }

  node.setProp("data", newData);

  const newInputFields = outputFields;

  if (outputsChanged) {
    // 只有输出字段实际变化，才清空子节点
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
    // 仅名称变化，更新 inputFields 的 source，但不清空子节点 tableFields
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

// 运行实例保存
const handletaskConfig = (form) => {
  delete form.draftJson;
  console.log("🚀 ~ handletaskConfig ~ form:", form);
  nodeData.value.taskConfig = {
    ...form,
    draftJson: JSON.stringify(form),
  };
};
// 大保存
const handleExportData = async (localSave) => {
  loading.value = true;
  try {
    // 检查
    if (!localSave) {
      const { isValid } = validateGraph(graph);
      if (!isValid) {
        return;
      }
    }
    // 检查是否有任务配置
    if (!hasTaskConfig(nodeData.value)) {
      taskConfigDialogVisible.value = true;
      return;
    }
    // 转换节点数据
    exportData2.value = await transNodeData(graph);
    // 将任务相关的配置整合到 exportData2 中
    exportData2.value = {
      ...exportData2.value,
      taskRelationJson: JSON.stringify(exportData2.value.taskRelationJson),
      taskDefinitionList: JSON.stringify(exportData2.value.taskDefinitionList),
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
      ...nodeData.value?.taskConfig,
    };
    // 判断是更新还是创建
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
    // 成功后处理
    if (res.code == "200") {
      handleSuccess();
    } else {
      proxy.$modal.msgWarning(td('dpp.integratioTask.operationFailed', '操作失败，请联系管理员'));
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
  const message = td('common.message.msgOpSuccess', '操作成功');
  router.push("/dpp/task/integratioTask");
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
    if (route.query?.info)
      return proxy.$modal.msgWarning(td('dpp.integratioTask.nodeNotEditable', '不可编辑，当前页面只能查看'));
    if (!data.componentType)
      return proxy.$modal.msgWarning(td('dpp.integratioTask.nodeDeveloping', '正在开发中，敬请期待'));
    const node = createDataNode(graph, data);
    dnd.start(node, e);
  }
};
const fieldPreviewDialog = ref();
const openDialog = (node, data, title) => {
  fieldPreviewDialog.value.show(node, data, title);
};
/**
 * 组件右键删除
 * @param {*}
 */
let selectedEdge = ref();
// 定义方法来清空当前选中边的颜色
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
  graph.use(new Export());

  // 插件
  bindShortcuts(graph);
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
  if (!route.query.info) {
    graph.on("node:contextmenu", handleNodeContextMenu);
  }

  // 连接边事件
  graph.on("edge:connected", handleEdgeConnected);

  graph.on("edge:contextmenu", handleEdgeContextMenu);

  graph.on("node:dblclick", handleNodeDblClick);
  if (route.query.info) {
    graph.getPlugin("keyboard")?.disable();
  }
  if (!route.query.info) {
    // 移除 panning 插件或解绑拖动事件
    graph.off("blank:mousedown"); // 解绑空白区域拖动事件
    graph.off("blank:mousemove");
    graph.off("blank:mouseup");
  }
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

// 处理已有节点的情况
function handleExistingNode(node) {
  if (node.data.taskParams.type == 2) {
    proxy.$message.warning(td('dpp.integratioTask.onlyOneOutputComponent', '只能有一个输出组件！'));
  } else if (node.data.taskParams.type == "1") {
    proxy.$message.warning(td('dpp.integratioTask.onlyOneInputComponent', '只能有一个输入组件！'));
  }
  graph.removeNode(node.id);
}
// 处理非输入节点
function handleNonInputNode(node) {
  const edges = graph.getEdges();
  edges.forEach((edge) => {
    if (edge.getTargetNode() == node) {
      sourceNode.value = edge.getSourceNode(); // 获取上级组件的数据
      // drawer.value = true; // 控制抽屉显示
    }
  });
  // drawer.value = true; // 控制抽屉显示
}

// 切换端口的显示状态
function togglePortsVisibility(visible) {
  const container = document.getElementById("graphContainer");
  const ports = container.querySelectorAll(".x6-port-body");
  showPorts(ports, visible);
}
/**
 * 统一处理删除节点或连接线的逻辑
 */
function handleDeleteCells(graph, cells, menuController) {
  if (!cells || cells.length === 0) {
    ElMessageBox.warning(td('dpp.integratioTask.deleteNothingSelected', '操作失败，没有选中要删除的节点或连线'));
    return;
  }

  const target = cells[0];
  const isEdge = target.isEdge?.();
  const isNode = target.isNode?.();

  let message = td('dpp.integratioTask.deleteLineWarning', '删除该连线将同时清空其所有子节点的字段配置，是否确认继续？');

  let sourceNode = null;

  if (isNode) {
    sourceNode = target;

    const childNodes = getAllChildNodes(sourceNode, graph);
    const hasChildNodes = childNodes.length > 0;

    message = hasChildNodes
      ? td('dpp.integratioTask.deleteNodeWarning', '删除该节点将同时清空其所有子节点的字段配置，是否确认继续？')
      : td('dpp.integratioTask.confirmDeleteNode', '是否确认删除该节点？');
  }

  if (isEdge) {
    sourceNode = target.getSourceCell?.();
  }

  ElMessageBox.confirm(message, td('dpp.integratioTask.confirmDelete', '确认删除'), {
    confirmButtonText: td('dpp.integratioTask.confirm', '确认'),
    cancelButtonText: td('common.button.cancel', '取消'),
    type: "warning",
  })
    .then(() => {
      // 重置子节点配置（无论节点还是边）
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

      // 执行删除操作
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

// 处理节点右键菜单事件
function handleNodeContextMenu({ e, node, edge, type = 0 }) {
  e.preventDefault();

  const pos = graph.clientToGraph(e.clientX, e.clientY);
  const container = document.getElementById("graphContainer");

  let menuController = null;

  const menuItems = [
    {
      label: td('dpp.integratioTask.deleteNode', '删除节点'),
      action: () => {
        // 这里传入单个节点或边的数组，menuController 传进去供关闭菜单用
        handleDeleteCells(graph, [type === 0 ? node : edge], menuController);
      },
    },
    {
      label: td('dpp.integratioTask.editNode', '编辑节点'),
      action: () => {
        handleNodeDblClick({ node }, "edit");
        menuController?.hide();
      },
    },
  ];

  if (node.data.taskParams.type != 1) {
    menuItems.push({
      label: td('dpp.integratioTask.showInputFields', '显示输入字段'),
      action: () => {
        const input = node.data.taskParams.inputFields;
        if (!Array.isArray(input) || input.length == 0) {
          ElMessage.warning(td('dpp.integratioTask.cannotFindInputFields', '无法找到输入字段'));
        } else {
          openDialog(input, node, td('dpp.integratioTask.inputFields', '输入字段'));
        }
        menuController?.hide();
      },
    });
  }

  menuItems.push({
    label: td('dpp.integratioTask.showOutputFields', '显示输出字段'),
    action: () => {
      const output = node.data.taskParams.outputFields;
      if (!Array.isArray(output) || output.length === 0) {
        ElMessage.warning(td('dpp.integratioTask.cannotFindOutputFields', '无法找到输出字段'));
      } else {
        openDialog(output, node, td('dpp.integratioTask.outputFields', '输出字段'));
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

// 处理连接边事件
function handleEdgeConnected({ edge }) {
  if (!loading.value) {
    hasUnsavedChanges.value = true;
    const source = edge.getSourceCell();
    const target = edge.getTargetCell();
    // 检查源节点和目标节点是否相同
    if (source === target) {
      graph.removeEdge(edge); // 移除边
      proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorSelf', '连接错误，节点不能连接到自己'));
      return;
    }

    // 获取源节点和目标节点的 taskParams.type
    const sourceType = source.data?.taskParams?.type;
    const targetType = target.data?.taskParams?.type;

    // 类型 1 不能作为目标节点
    if (targetType == 1) {
      graph.removeEdge(edge); // 移除边
      proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorInput', '连接错误，输入组件不能被连接'));
      return;
    }

    // 类型 2 不能作为输入节点（源节点）
    if (sourceType == 2) {
      graph.removeEdge(edge); // 移除边
      proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorOutput', '连接错误，输出组件不能连接到其他组件'));
      return;
    }

    // 类型 2 节点只能作为输出节点连接一次
    if (targetType == 2) {
      const targetEdges = graph
        .getEdges()
        .filter((e) => e.getTargetCell() === target);
      if (targetEdges.length > 1) {
        graph.removeEdge(edge); // 移除边
        proxy.$modal.msgWarning(td('dpp.integratioTask.connectionErrorOutputOnce', '连接错误，目标节点只能作为输出连接一次'));
        return;
      }
    }

    updateTargetNodeData(source, target, edge);
  }
}
// 更新目标节点的数据
function updateTargetNodeData(source, target, edge) {
  const childNodes = getAllChildNodes(source, graph);

  // 更新子节点的数据
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
      td('dpp.integratioTask.addCleanRulePrompt', '是否要给转换组件添加输入组件绑定的清洗规则？'),
      td('common.message.prompt', '提示'),
      {
        confirmButtonText: td('dpp.integratioTask.yes', '是'),
        cancelButtonText: td('dpp.integratioTask.no', '否'),
        type: "warning",
      }
    )
      .then(() => {
        // 调用方法生成规则配置
        const result = renameRuleToRuleConfig(
          target.data.taskParams.inputFields
        );
        console.log("🚀 ~ updateTargetNodeData ~ result:", result);
        proxy.$message.success(td('dpp.integratioTask.cleanRuleAdded', '添加清洗规则') + ` ${result?.length || 0} ` + td('dpp.integratioTask.cleanRuleAddedSuffix', '条'));
        // 给目标节点赋值
        if (target.data?.taskParams) {
          target.data.taskParams.tableFields = result;
          target.data = { ...target.data };
        }
      })
      .catch(() => {});
  }
}

// 处理边右键菜单事件
function handleEdgeContextMenu(event) {
  const edge = event.edge;
  const { x, y } = event;
  let menuController = null;
  const menuItems = [
    {
      label: td('dpp.integratioTask.deleteLine', '删除连接线'),
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
// 重置操作逻辑
const handleCancel = () => {
  proxy.$modal
    .confirm(td('dpp.integratioTask.resetWarning', '点击重置将清除所有未保存的更改，您确定要继续吗？'))
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
      exportGraphAsPNG(graph);
      break;
    }
    case "reset": {
      handleCancel();
      break;
    }
  }
};
// 初始化函数
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
// 离开页面时提示
onBeforeRouteLeave((to, from, next) => {
  // 检查是否有未保存的更改
  if (hasUnsavedChanges.value) {
    ElMessageBox.confirm(
      td('dpp.integratioTask.discardWarning', '您已经编辑部分任务内容，是否放弃已编辑内容？'), // 提示信息
      td('common.message.prompt', '提示'), // 标题
      {
        confirmButtonText: td('common.button.save', '保存'), // 确认按钮文本
        cancelButtonText: td('dpp.integratioTask.discard', '放弃'), // 取消按钮文本
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
  { name: "checkMessage", label: td('dpp.integratioTask.checkMessage', '检查消息'), content: td('dpp.integratioTask.checkMessageContent', '检查消息内容') },
  // { name: "log", label: "日志", content: "日志内容" },
]);
// 用于设置标签区域的样式
const tabAreaStyle = ref({
  position: "absolute",
  height: "300px",
  width: "100%",
  transition: "bottom 0.3s",
  bottom: "-9999px",
  "background-color": "rgb(255, 255, 255)",
  "margin-left": "15px",
});
// 任务检查
const minimizeAction = () => {
  tabAreaStyle.value.bottom = "-9999px";
}; // 切换到任务检查标签
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
    message += td('dpp.integratioTask.checkFailed', '检查未通过:') + "<br>" + errorMessages.join("<br>");
  } else {
    message += td('dpp.integratioTask.checkPassed', '检查通过');
  }
  if (
    !nodeData.value?.taskConfig ||
    Object.keys(nodeData.value.taskConfig).length === 0
  ) {
    message += "<br>" + td('dpp.integratioTask.taskConfigNotFilled', '任务配置未填写');
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

    :deep .el-tree-node[aria-disabled="true"] {
      display: none;
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
