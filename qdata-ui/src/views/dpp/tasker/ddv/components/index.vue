<template>
  <div class="app-container" ref="app-container" v-loading="loading" style="overflow: hidden !important">
    <div class="head-container">
      <div class="head-title">
        <img :src="getDatasourceIcon(nodeData.typaCode)" alt="" :style="getDatasourceIcon(nodeData.typaCode)?'width: 20px;margin-right: 5px;':''">
        {{ nodeData.name != "" ? nodeData.name : "数据开发任务" }}
      </div>
      <div class="head-btns">
        <el-button type="primary" icon="CircleCheck" @click="handleExportData" v-if="!route.query.info">保存</el-button>
        <el-button icon="Back" @click="routeTo('/dpp/tasker/dpptaskerddv', '')">取消</el-button>
        <el-button icon="Setting" @click="openTaskConfigDialog">任务配置</el-button>
        <el-button icon="Coin" v-if="!route.query.info" @click="selectTab('checkMessage')">任务检查</el-button>
        <el-button icon="VideoPlay" v-if="formStatus == 1" @click="handleRun">运行</el-button>
      </div>
    </div>
    <!-- <sql-editor ref="editorRef" :value="form.taskParams.sql" class="sql-editor" :height="'calc(100vh - 180px)'"
        :readonly="route.query?.info" @changeTextarea="changeTextarea($event)" /> -->
    <div class="sql-row">
      <div class="sideConfig">
        <div class="icon" :class="{ act: activeValue.name == item.name }" v-for="item in iconList" :key="item" @click="handleIcon(item)" :title="item.name">
          <el-icon><component :is="item.icon" /></el-icon>
        </div>
      </div>
      <div class="editor-warp">
        <div class="editor-con" :style="`width: calc(100%  - ${configWidth}px);`">
          <div class="editor-main" ref="editorMain" :style="`height: calc(100% - ${consoleHeight}px);`">
            <Editor ref="editorRef" :model-value="form.sql" @update:model-value="changeTextarea" :style="{ borderBottom: activeValue.type ? 'none' : '' }" />
            <div v-if="false" class="full-screen" :title="isFullscreen ? '退出全屏' : '全屏'" @click="fullScreenCallBack">
              <i :class="isFullscreen ? 'iconfont icon-fullscreen-exit-line' : 'iconfont icon-a-quanpingxianxing'" style="font-size: 20px"></i>
            </div>
          </div>
          <Console ref="consoleRef" :currValue="activeValue" @close="closeConsoleDialog" v-if="activeValue.type" />
        </div>
        <ConfigView ref="configViewRef" :currValue="activeValueR" @close="closeConsoleDialogR" v-if="activeValueR.type" />
      </div>
      <div class="sideConfig sideConfig-r">
        <div class="icon" :class="{ act: activeValueR.name == item.name }" v-for="item in iconListR" :key="item" @click="handleIconR(item)" :title="item.name">
          <el-icon><component :is="item.icon" /></el-icon>
        </div>
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
    <taskConfigDialog
      :visible="taskConfigDialogVisible"
      title="修改配置"
      @update:visible="taskConfigDialogVisible = $event"
      @save="handletaskConfig"
      :data="nodeData"
      :userList="userList"
      :deptOptions="deptOptions"
      :info="true"
    />
  </div>
</template>
<script setup>
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
// import SqlEditor from "@/components/SqlEditor/index1.vue";
// import SqlEditor from "@/components/SqlEditor/index3.vue";
import taskConfigDialog from "./taskConfigDialog.vue";
import useUserStore from "@/store/system/user";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataDevCat } from "@/api/att/cat/attDataDevCat/attDataDevCat";
// sqlEditor
import { useFullscreen } from "@vueuse/core";
import Console from "@/components/SqlEditor/console/index.vue";
import Editor from "@/components/SqlEditor/editor/index.vue";
import ConfigView from "@/components/SqlEditor/configView/index.vue";

const userStore = useUserStore();
import { createProcessDefinition, dppEtlTask, updateProcessDefinition, startDppEtlTask } from "@/api/dpp/etl/dppEtlTask";
// import { treeData } from "../components/data";
// import { renderGraph } from "@/utils/opBase";
// import { right } from "@antv/x6/lib/registry/port-layout/line";

const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();
let loading = ref(false);
let form = ref({
  sql: "",
});

let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    console.log("route.query", route.query);

    id = newId || 1;
    if (id) {
      getList();
    }
  }
);
// 图标
const getDatasourceIcon = (type) => {
  switch (type) {
    case "DM":
      return new URL("@/assets/system/images/dpp/DM.png", import.meta.url).href;
    case "Oracle":
      return new URL("@/assets/system/images/dpp/oracle.png", import.meta.url).href;
    case "MYSQL":
      return new URL("@/assets/system/images/dpp/mysql.png", import.meta.url).href;
    case "Kingbase":
      return new URL("@/assets/system/images/dpp/kingBase.png", import.meta.url).href;
    case "Sqlerver":
      return new URL("@/assets/system/images/dpp/sqlServer.png", import.meta.url).href;
    case "PostgreSql":
      return new URL("@/assets/system/images/dpp/kafka.png", import.meta.url).href;
    default:
      return null;
  }
};
let hasUnsavedChanges = ref(false);
let nodeData = ref({ name: "" });
// 任务配置
const taskConfigDialogVisible = ref(false);
let editorRef = ref("");
function changeTextarea(val) {
  form.value.sql = val;
  hasUnsavedChanges.value = true;
}
const formStatus = ref(null);
function getList() {
  loading.value = true;
  dppEtlTask(route.query.id)
    .then((response) => {
      // 任务配置
      nodeData.value = {
        ...response.data.taskConfig,
        draftJson: response.data.draftJson,
        status: response.data.status,
        typaCode: response.data.draftJson ? JSON.parse(response.data.draftJson).typaCode : "",
      };
      // 修改或新增标记
      formStatus.value = response.data.status;
      iconListR.value[0].data = JSON.parse(JSON.stringify(response.data));
      iconList.value[0].data = JSON.parse(JSON.stringify(response.data));
      // 默认选中-控制台
      // activeValue.value = iconList.value[0];
      activeValueR.value = iconListR.value[0];
      form.value = {
        ...response.data,
      };
      if (response.data.taskDefinitionList.length == 0) {
        let sql = JSON.parse(response.data.draftJson);
        form.value.sql = sql.sqlData.content;
      } else {
        form.value.sql = response.data.taskDefinitionList[0].taskParams.sql;
      }
      // 部门
    })
    .finally(() => {
      loading.value = false;
    });
}

let userList = ref([]);
let deptOptions = ref([]);
function getDeptTree() {
  Promise.all([
    listAttDataDevCat().catch((err) => {
      console.error("获取类别数据失败", err);
      return { data: [] };
    }),
    deptUserTree().catch((err) => {
      console.error("获取用户数据失败", err);
      return { data: [] };
    }),
  ])
    .then(([taskCatRes, userRes]) => {
      // 处理部门类别数据
      deptOptions.value = [
        {
          id: 0,
          name: "数据开发类目",
          value: "",
          children: proxy.handleTree(taskCatRes.data, "id", "parentId"),
        },
      ];

      // 处理用户数据
      userList.value = userRes.data;
    })
    .finally(() => {
      // 所有请求完成后，隐藏 loading
    });
}

if (route.query.id) {
  getList();
}
const handleRun = async () => {
  let id = route.query.id;
  if (!id) {
    proxy.$modal.msgError("无效的任务 ID");
    return;
  }
  loading.value = true;
  try {
    const res = await startDppEtlTask(id);
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      // 打开控制台
      activeValue.value = iconList.value[0];
    } else {
      proxy.$modal.msgError(res?.msg || "执行失败");
    }
  } finally {
    loading.value = false;
  }
};
// 运行实例保存
const handletaskConfig = (obj) => {
  nodeData.value = {
    ...obj,
  };
};
const dataJson = () => {
  const taskParams = configViewRef.value.form;
  const taskDefinitionList = {
    taskParams: taskParams,
    // 任务配置
    id: form.value.id,
    name: nodeData.value.name,
    code: form.value.code,
    version: form.value.version,
    description: nodeData.value.description,
    // environmentCode: taskParams.environmentCode,
    // flag: taskParams.flag,
    // isCache: taskParams.isCache,
    // taskExecuteType: taskParams.taskExecuteType,
    // 基本配置
    taskPriority: taskParams.taskPriority,
    workerGroup: taskParams.workerGroup,
    failRetryTimes: taskParams.failRetryTimes,
    failRetryInterval: taskParams.failRetryInterval,
    delayTime: taskParams.delayTime,
    // 其他配置
    taskType: taskParams.taskType,
    componentType: taskParams.componentType,
  };
  taskDefinitionList.taskParams.sql = form.value.sql;
  // 任务配置
  nodeData.value.componentType = taskParams.componentType;
  nodeData.value.taskType = taskParams.taskType;
  nodeData.value.executionType = "PARALLEL";
  // 准备需要返回的数据
  const taskRelationData = {
    name: "",
    preTaskCode: 0,
    preTaskVersion: 0,
    postTaskCode: form.value?.code || 0,
    postTaskVersion: form.value?.version || 0,
    conditionType: "NONE",
    conditionParams: {},
  };
  const locations = [{ taskCode: form.value?.code, x: 0, y: 0 }];
  // 返回这三个值
  return {
    ...form.value,
    taskRelationJson: JSON.stringify([taskRelationData]),
    locations,
    taskDefinitionList: JSON.stringify([taskDefinitionList]),
    projectCode: userStore.projectCode || "133545087166112",
    projectId: userStore.projectId,
    type: "3",
    ...nodeData.value,
  };
};
let exportData2 = ref();
// 大保存
const handleExportData = async () => {
  loading.value = true;
  try {
    if (!form.value.sql) {
      return proxy.$modal.msgError("请输入sql语句");
    }
    if (!nodeData.value?.name) {
      taskConfigDialogVisible.value = true;
      return;
    }
    let valid = await configViewRef.value.configRef.validate();
    if (!valid) {
      return proxy.$modal.msgError("请检查属性配置必填项");
    }
    exportData2.value = dataJson();
    console.log("🚀 ~ handleExportData ~ exportData2.value:", exportData2.value);
    // 根据 nodeData.id 判断是更新还是创建
    const res =
      formStatus.value != "-1"
        ? await updateProcessDefinition({
            ...exportData2.value,
            id: form.value?.id,
          })
        : await createProcessDefinition(exportData2.value);

    // 成功后处理
    if (res.code == "200") {
      handleSuccess();
    } else {
      proxy.$modal.msgError("操作失败");
    }
  } catch (error) {
    handleError(error);
  } finally {
    loading.value = false;
  }
};

const handleSuccess = () => {
  taskConfigDialogVisible.value = false;
  hasUnsavedChanges.value = false;
  const message = form.value?.id ? "操作成功" : "操作成功";
  router.push("/dpp/tasker/dpptaskerddv");
  proxy.$modal.msgSuccess(message);
};

const handleError = (error) => {
  console.error("操作失败:", error);
  proxy.$modal.msgError(error.message || "操作失败，请检查必填项");
};

const openTaskConfigDialog = () => {
  taskConfigDialogVisible.value = true;
};

const minimizeAction = () => {
  tabAreaStyle.value.bottom = "-9999px";
};

// 初始化函数
onMounted(() => {
  getDeptTree();
});
// eslint-disable-next-line no-unused-vars
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
  width: "98%",
  transition: "bottom 0.3s",
  bottom: "-9999px",
  backgroundColor: "#fff",
});
// 切换到任务检查标签
const selectTab = (tabName) => {
  console.log("🚀 ~ selectTab ~ tabName:", tabName);
  activeTab.value = tabName;

  if (activeTab.value == "checkMessage") {
    isValidClick();
  }
  tabAreaStyle.value.bottom = "0px";
};
const validateGraph = () => {
  let errors = [];

  if (!form.value.sql) {
    errors.push("请输入sql语句");
  }
  if (!form.value?.taskConfig?.name) {
    errors.push("任务配置未填写");
  }

  if (errors.length > 0) {
    return {
      isValid: false,
      errorMessages: errors,
    };
  } else {
    return {
      isValid: true,
      errorMessages: [],
    };
  }
};
const isValidClick = () => {
  const { isValid, errorMessages } = validateGraph();
  console.log("🚀 ~ isValidClick ~ errorMessages:", errorMessages);
  let message = "";
  // Check graph validation
  if (!isValid && errorMessages.length != 0) {
    message += "检查未通过:<br>" + errorMessages.join("<br>"); // Replace \n with <br> for HTML line breaks
  } else {
    message += "检查通过";
  }
  tabs.value[0].content = message;
  console.log("🚀 ~ isValidClick ~ message:", message);
};
// 跳转并保存数据的逻辑（如果需要）
const saveData = async () => {
  // 假设这是保存数据的函数
  hasUnsavedChanges.value = false; // 保存后标记为无未保存内容
  // 执行保存操作
};
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
    // 如果没有未保存的更改，直接跳转
    next();
  }
});

// #region sql编辑器
// 左侧图标
const iconList = ref([
  {
    name: "日志控制台",
    type: "console",
    icon: "Tickets",
    data: {},
  },
  {
    name: "查询结果",
    type: "result",
    icon: "Odometer",
    data: [],
  },
  {
    name: "执行历史记录",
    type: "history",
    icon: "Timer",
    data: [],
  },
]);
const activeValue = ref({ name: "" });
const handleIcon = (item) => {
  if (activeValue.value.name == item.name) {
    activeValue.value = { name: "" };
  } else {
    activeValue.value = item;
  }
};
const closeConsoleDialog = () => {
  activeValue.value = { name: "" };
};
// 右侧图标
const iconListR = ref([
  {
    name: "属性配置",
    type: "attrConfig",
    icon: "Operation",
    data: {
      taskDefinitionList: [],
    },
  },
]);
const activeValueR = ref({ name: "" });
const handleIconR = (item) => {
  if (activeValueR.value.name == item.name) {
    activeValueR.value = { name: "" };
  } else {
    activeValueR.value = item;
  }
};
const closeConsoleDialogR = () => {
  activeValueR.value = { name: "" };
};
// 控制台和配置页的自定义宽高
const consoleRef = ref(null);
const consoleHeight = computed(() => consoleRef.value && consoleRef.value.currHeight);
const configViewRef = ref(null);
const configWidth = computed(() => configViewRef.value && configViewRef.value.currWidth);

// 全屏
const editorMain = ref(null);
const { isFullscreen, toggle } = useFullscreen(editorMain);
const fullScreenCallBack = () => {
  toggle();
};
// #endregion
</script>

<style lang="less" scoped>
.app-container {
  position: relative;
  height: calc(87vh - 7px);
  overflow: hidden !important;

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
        display: none;
      }
      .head-icon {
        width: 30px;
        margin-left: 5px;
        margin-right: 10px;
      }
    }

    .head-btns {
      .el-button {
        height: 32px;
        padding: 8px 18px;
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

.zjiconimg {
  font-size: 12px;
}

.colorxz {
  color: #358cf3;
}

.colorwxz {
  color: #afd1fa;
}

.iconimg {
  font-size: 15px;
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
<style lang="scss" scoped>
.sql-row {
  width: 100%;
  height: calc(100vh - 190px);
  display: flex;
  background: #fff;
  .sideConfig {
    padding: 10px;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    // border-top: 1px solid rgba(0, 0, 0, 0.06);
    .icon {
      cursor: pointer;
      width: 30px;
      height: 30px;
      border-radius: 2px;
      border: 1px solid var(--el-color-primary);
      display: flex;
      justify-content: center;
      align-items: center;
      margin-top: 16px;
      &:hover {
        background-color: var(--el-color-primary);
        .el-icon {
          color: #fff;
        }
      }
      &.act {
        background-color: var(--el-color-primary);
        .el-icon {
          color: #fff;
        }
      }
      .el-icon {
        color: var(--el-color-primary);
      }
    }
    &.sideConfig-r {
      justify-content: flex-start;
      .icon {
        margin-top: 0;
        margin-bottom: 15px;
      }
    }
  }
  .editor-warp {
    width: calc(100% - 100px);
    height: 100%;
    position: relative;
    background: #fff;
    display: flex;
    .editor-con {
      width: 100%;
      height: 100%;
      border-left: 1px solid rgba(0, 0, 0, 0.06);
      border-right: 1px solid rgba(0, 0, 0, 0.06);
      .editor-main {
        position: relative;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        // padding: 15px 0;
        .json-editor {
          padding: 15px 0;
        }
      }
    }
  }
}
.full-screen {
  cursor: pointer;
  position: absolute;
  top: 30px;
  right: 10%;
  z-index: 10;
  box-shadow: rgb(204, 204, 204) 0px 0px 10px;
  padding: 5px 5px;
  border-radius: 4px;
}
</style>
