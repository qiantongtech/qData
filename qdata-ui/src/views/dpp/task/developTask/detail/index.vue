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
  <div class="app-container" ref="app-container" v-loading="loading" style="overflow: hidden !important">
    <div class="head-container">
      <div class="head-title">
        <el-tooltip :content="nodeData.typaCode" placement="top">
          <img :src="getDatasourceIcon(nodeData.typaCode)" alt=""
            style="width: 20px; margin-right: 5px; cursor: pointer;" />
        </el-tooltip>

        {{ nodeData.name != "" ? nodeData.name : td('dpp.developTaskDetail.dataDevCategory', '数据开发任务') }}
      </div>
      <div class="head-btns">
        <el-button type="primary" size="small" @click="handleExportData" v-if="!route.query.info">
          <img src="@/assets/images/dpp/etl/icon-title-active-one.svg" alt="">{{ td('dpp.developTaskDetail.taskSave', '任务保存') }}
        </el-button>
        <el-button type="primary" plain size="small" @click="routeTo('/dpp/task/developTask', '')">
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-three.svg" alt="">
          <img class="act" src="@/assets/images/dpp/etl/icon-title-active-three.svg" alt="">{{ td('dpp.developTaskDetail.taskCancel', '任务取消') }}
        </el-button>
        <el-button type="primary" plain size="small" @click="openTaskConfigDialog" v-if="!route.query.info">
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-four.svg" alt="">
          <img class="act" src="@/assets/images/dpp/etl/icon-title-active-four.svg" alt="">{{ td('dpp.developTaskDetail.taskConfig', '任务配置') }}
        </el-button>
        <el-button type="primary" plain size="small" @click="openTaskConfigDialog" v-else>
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-four.svg" alt="">
          <img class="act" src="@/assets/images/dpp/etl/icon-title-active-four.svg" alt="">{{ td('dpp.developTaskDetail.taskDetail', '任务详情') }}
        </el-button>
        <el-button type="primary" plain v-if="formStatus == 1" size="small" @click="handleRun">
          <img class="currImg" src="@/assets/images/dpp/etl/icon-title-two.svg" alt="">
          <img class="act" src="@/assets/images/dpp/etl/icon-title-active-two.svg" alt="">{{ td('dpp.developTaskDetail.taskRun', '任务运行') }}
        </el-button>
      </div>
    </div>
    <!-- <sql-editor ref="editorRef" :value="form.taskParams.sql" class="sql-editor" :height="'calc(100vh - 180px)'"
        :readonly="route.query?.info" @changeTextarea="changeTextarea($event)" /> -->
    <div class="sql-row">
      <div class="sideConfig" :style="{ visibility: route.query.info ? 'hidden' : 'visible' }">
        <div class="icon" :class="{ act: activeValue.name == item.name }" v-for="item in iconList" :key="item"
          @click="handleIcon(item)" :title="item.name">
          <el-icon>
            <component :is="item.icon" />
          </el-icon>
        </div>
      </div>
      <div class="editor-warp">
        <div class="editor-con" :style="`width: calc(100%  - ${configWidth}px);`">
          <div class="editor-main" ref="editorMain" :style="`height: calc(100% - ${consoleHeight}px);`">
            <Editor ref="editorRef" :model-value="form.sql" @update:model-value="changeTextarea"
              :style="{ borderBottom: activeValue.type ? 'none' : '' }" :readOnly="route.query.info" />
            <div v-if="false" class="full-screen" :title="isFullscreen ? td('dpp.developTaskDetail.exitFullscreen', '退出全屏') : td('dpp.developTaskDetail.fullscreen', '全屏')" @click="fullScreenCallBack">
              <i :class="isFullscreen ? 'iconfont icon-fullscreen-exit-line' : 'iconfont icon-a-quanpingxianxing'"
                style="font-size: 20px"></i>
            </div>
          </div>
          <Console ref="consoleRef" :currValue="activeValue" @close="closeConsoleDialog" v-if="activeValue.type" />
        </div>
        <ConfigView ref="configViewRef" :style="`height: calc(100% - ${consoleHeight}px);`" :currValue="activeValueR"
          @close="closeConsoleDialogR" v-if="activeValueR.type" :readOnly="route.query.info" />
      </div>
      <div class="sideConfig sideConfig-r">
        <div class="icon" :class="{ act: activeValueR.name == item.name }" v-for="item in iconListR" :key="item"
          @click="handleIconR(item)" :title="item.name">
          <el-icon>
            <component :is="item.icon" />
          </el-icon>
        </div>
      </div>
    </div>
    <div class="tabs-container" v-bind:style="tabAreaStyle">
      <!-- The icon is placed on the far right -->
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
    <add :visible="taskConfigDialogVisible" :title="!route.query.info ? td('dpp.developTaskDetail.editTaskConfig', '修改任务配置') : td('dpp.developTaskDetail.taskDetail', '任务详情')"
      @update:visible="taskConfigDialogVisible = $event" @save="handletaskConfig" :data="nodeData" :userList="userList"
      :deptOptions="deptOptions" :info="true" />
  </div>
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import add from "../add/add.vue";
import useUserStore from "@/store/system/user";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataDevCat } from "@/api/att/cat/dataDevCat/dataDevCat";
// sqlEditor
import { useFullscreen } from "@vueuse/core";
import Console from "@/components/SqlEditor/console/index.vue";
import Editor from "@/components/SqlEditor/editor/index.vue";
import ConfigView from "@/components/SqlEditor/configView/index.vue";

const userStore = useUserStore();
import { createProcessDefinition, etlTask, updateProcessDefinition, startDppEtlTask } from "@/api/dpp/task/index.js";

const { td } = useDefaultLang();
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
// Monitor id changes
watch(
    [() => route.query.id, () => userStore.projectCode],
    ([newId, newCode], [oldId, oldCode]) => {
      if (newId) {
        console.log("route.query", route.query);
        id = newId || 1;
        if (id) {
          getList();
        }
      }

      if (newCode) {
        getDeptTree();
      }
    }
);
// icon
const getDatasourceIcon = (type) => {
  switch (type) {
    case "DM":
      return new URL("@/assets/images/common/dpp/ds-dm.png", import.meta.url).href;
    case "Oracle":
      return new URL("@/assets/images/common/dpp/img-oracle-one.png", import.meta.url).href;
    case "MYSQL":
      return new URL("@/assets/images/common/dpp/ds-mysql.png", import.meta.url).href;
    case "Kingbase":
      return new URL("@/assets/images/common/dpp/ds-kingbase.png", import.meta.url).href;
    case "Sqlerver":
      return new URL("@/assets/images/common/dpp/ds-sqlserver.png", import.meta.url).href;
    case "PostgreSql":
      return new URL("@/assets/images/common/dpp/ds-kafka.png", import.meta.url).href;
    case "Hive":
      return new URL("@/assets/images/common/dpp/ds-hive.png", import.meta.url).href;
    case "SparkSql":
      return new URL("@/assets/images/common/dpp/icon-spark.svg", import.meta.url).href;
    case "FlinkBatch":
      return new URL("@/assets/images/common/dpp/icon-flink.svg", import.meta.url).href;
    case "FlinkStream":
      return new URL("@/assets/images/common/dpp/icon-flink.svg", import.meta.url).href;
    default:
      return null;
  }
};
let hasUnsavedChanges = ref(false);
let nodeData = ref({ name: "" });
// Task configuration
const taskConfigDialogVisible = ref(false);
let editorRef = ref("");
function changeTextarea(val) {
  form.value.sql = val;
  hasUnsavedChanges.value = true;
}
const formStatus = ref(null);
function getList() {
  loading.value = true;
  etlTask(route.query.id)
    .then((response) => {
      // Task configuration
      nodeData.value = {
        ...response.data.taskConfig,
        draftJson: response.data.draftJson,
        status: response.data.status,
        typaCode: response.data.draftJson ? JSON.parse(response.data.draftJson).typaCode : "",
      };
      // Edit or add tags
      formStatus.value = response.data.status;
      iconListR.value[0].data = JSON.parse(JSON.stringify(response.data));
      iconList.value[0].data = JSON.parse(JSON.stringify(response.data));
      // Selected by default - Console
      consoleVisible.value = true;
      activeValue.value = iconList.value[0];
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
      // Department
    })
    .finally(() => {
      loading.value = false;
    });
}

let userList = ref([]);
let deptOptions = ref([]);
function getDeptTree() {
  Promise.all([
    listAttDataDevCat({
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
      validFlag: true,
    }).catch((err) => {
      console.error("Failed to fetch category data", err);
      return { data: [] };
    }),
    deptUserTree().catch((err) => {
      console.error("Failed to fetch user data", err);
      return { data: [] };
    }),
  ])
    .then(([taskCatRes, userRes]) => {
      deptOptions.value = []
      // Process department category data
      deptOptions.value = [
        {
          id: 0,
          name: td('dpp.developTask.dataDevCategory', '数据开发类目'),
          value: "",
          children: proxy.handleTree(taskCatRes.data, "id", "parentId"),
        },
      ];

      // Processing of user data
      userList.value = userRes.data;
    })
    .finally(() => {
      // After all requests are completed, hide the loading
    });
}

if (route.query.id) {
  getList();
}
const handleRun = async () => {
  let id = route.query.id;
  if (!id) {
    proxy.$modal.msgWarning(td('dpp.developTaskDetail.noTaskId', '无效的任务id，请刷新后重试'));
    return;
  }
  loading.value = true;
  try {
    const res = await startDppEtlTask(id);
    if (res.code == 200) {
      proxy.$modal.msgSuccess(td('common.message.msgOpSuccess', '操作成功'));
      // Open the console
      activeValue.value = iconList.value[0];
    } else {
      proxy.$modal.msgWarning(res?.msg || td('dpp.developTask.executeFailed', '执行失败，请联系管理员'));
    }
  } finally {
    loading.value = false;
  }
};
// Run instance save
const handletaskConfig = (obj) => {
  nodeData.value = {
    ...obj,
  };
};
const dataJson = () => {
  const taskParams = configViewRef.value.form;
  const taskDefinitionList = {
    taskParams: taskParams,
    // Task configuration
    id: form.value.id,
    name: nodeData.value.name,
    code: form.value.code,
    version: form.value.version,
    description: nodeData.value.description,
    // environmentCode: taskParams.environmentCode,
    // flag: taskParams.flag,
    // isCache: taskParams.isCache,
    // taskExecuteType: taskParams.taskExecuteType,
    // Basic configuration
    taskPriority: taskParams.taskPriority,
    workerGroup: taskParams.workerGroup,
    failRetryTimes: taskParams.failRetryTimes,
    failRetryInterval: taskParams.failRetryInterval,
    delayTime: taskParams.delayTime,
    // Other configurations
    taskType: taskParams.taskType,
    componentType: taskParams.componentType,
  };
  taskDefinitionList.taskParams.sql = form.value.sql;
  // Task configuration
  nodeData.value.componentType = taskParams.componentType;
  nodeData.value.taskType = taskParams.taskType;
  nodeData.value.executionType = "PARALLEL";
  // Prepare the data to be returned
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
  // Return these three values
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
// Great save
const handleExportData = async () => {
  loading.value = true;
  try {
    if (!form.value.sql) {
      return proxy.$modal.msgWarning(td('dpp.developTaskDetail.saveFailedInputSql', '保存失败，请输入sql语句'));
    }
    if (!nodeData.value?.name) {
      taskConfigDialogVisible.value = true;
      return;
    }
    let valid = await configViewRef.value.configRef.validate();
    if (!valid) {
      return proxy.$modal.msgWarning(td('dpp.developTaskDetail.saveFailedCheckAttr', '保存失败，请检查属性配置必填项'));
    }
    exportData2.value = dataJson();
    console.log("🚀 ~ handleExportData ~ exportData2.value:", exportData2.value);
    // Determine whether to update or create based on nodeData.id
    const res =
      formStatus.value != "-1"
        ? await updateProcessDefinition({
          ...exportData2.value,
          id: form.value?.id,
        })
        : await createProcessDefinition(exportData2.value);

    // Processing after success
    if (res.code == "200") {
      handleSuccess();
    } else {
      proxy.$modal.msgWarning(td('dpp.integratioTask.operationFailed', '操作失败，请联系管理员'));
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
  const message = form.value?.id ? td('common.message.msgOpSuccess', '操作成功') : td('common.message.msgOpSuccess', '操作成功');
  router.push("/dpp/task/developTask");
  proxy.$modal.msgSuccess(message);
};

const handleError = (error) => {
  console.error("Operation failed:", error);
  proxy.$modal.msgWarning(error.message || td('dpp.integratioTask.operationFailed', '操作失败，请联系管理员'));
};

const openTaskConfigDialog = () => {
  taskConfigDialogVisible.value = true;
};

const minimizeAction = () => {
  tabAreaStyle.value.bottom = "-9999px";
};

// initialization function
onMounted(() => {
  if(userStore.projectId){
    getDeptTree();
  }
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
// Used to control the currently selected tag
const activeTab = ref("checkMessage");
const tabs = ref([
  { name: "checkMessage", label: td('dpp.developTaskDetail.checkMessage', '检查消息'), content: td('dpp.developTaskDetail.checkMessageContent', '检查消息内容') },
  // { name: "log", label: "log", content: "log content" },
]);
// Used to style the label area
const tabAreaStyle = ref({
  position: "absolute",
  height: "300px",
  width: "98%",
  transition: "bottom 0.3s",
  bottom: "-9999px",
  backgroundColor: "#fff",
});
// Switch to task inspection tab
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
    errors.push(td('dpp.developTaskDetail.inputSqlPrompt', '请输入sql语句'));
  }
  if (!form.value?.taskConfig?.name) {
    errors.push(td('dpp.developTaskDetail.taskConfigNotFilled', '任务配置未填写'));
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
    message += td('dpp.developTaskDetail.checkFailed', '检查未通过:') + "<br>" + errorMessages.join("<br>"); // Replace \n with <br> for HTML line breaks
  } else {
    message += td('dpp.developTaskDetail.checkPassed', '检查通过');
  }
  tabs.value[0].content = message;
  console.log("🚀 ~ isValidClick ~ message:", message);
};
// Logic to jump to and save data (if needed)
const saveData = async () => {
  // Let's say this is a function that saves data
  hasUnsavedChanges.value = false; // After saving, mark it as no unsaved content
  // Perform save operation
};
// Prompt when leaving the page
onBeforeRouteLeave((to, from, next) => {
  if (hasUnsavedChanges.value) {
    ElMessageBox.confirm(
      td('dpp.developTaskDetail.discardContent', '您已经编辑部分任务内容，是否放弃已编辑内容？'),
      td('common.message.prompt', '提示'),
      {
        confirmButtonText: td('common.button.save', '保存'),
        cancelButtonText: td('dpp.developTaskDetail.discard', '放弃'),
        type: "warning",
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // Click save
            handleExportData();
            saveData();
            done();       // Close pop-up window
            next(false);  // Prevent routes from leaving
          } else if (action === 'cancel') {
            // Click to give up
            done();       // Close pop-up window
            next();       // Allow route to leave
          } else if (action === 'close') {
            // Click the X number to only close the pop-up window and not block routing
            done();       // Close pop-up window
            next(false);  // Prevent routes from leaving
            // Without calling next(), routing will not be affected
          }
        }
      }
    );
  } else {
    next();
  }
});

// #region sql editor
// left icon
const iconList = ref([
  {
    name: td('dpp.developTaskDetail.logConsole', '日志控制台'),
    type: "console",
    icon: "Tickets",
    data: {},
  },
  {
    name: td('dpp.developTaskDetail.queryResult', '查询结果'),
    type: "result",
    icon: "Odometer",
    data: [],
  },
  {
    name: td('dpp.developTaskDetail.executionHistory', '执行历史记录'),
    type: "history",
    icon: "Timer",
    data: [],
  },
]);
const activeValue = ref({ name: "" });
const handleIcon = (item) => {
  if (activeValue.value.name == item.name) {
    closeConsoleDialog();
  } else {
    activeValue.value = item;
    consoleVisible.value = true; // show console
  }
};
const closeConsoleDialog = () => {
  activeValue.value = { name: "" }; // Reset selected
  consoleVisible.value = false;     // Altitude return to zero
};
// right icon
const iconListR = ref([
  {
    name: td('dpp.developTaskDetail.attrConfig', '属性配置'),
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
const consoleVisible = ref(false);
// Custom width and height of console and configuration pages
const consoleRef = ref(null);
const consoleHeight = computed(() => {
  if (!consoleVisible.value) return 0;
  return consoleRef.value?.currHeight || 0;
});
const configViewRef = ref(null);
const configWidth = computed(() => configViewRef.value && configViewRef.value.currWidth);

// full screen
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
      color: #000000;
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
  /* Set the fill color of the check box */
  stroke: #007bff;
  /* Set the border color of the selected box */
  opacity: 1;
  /* Set the transparency of the check box */
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
      background-color: #e9effe;

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
