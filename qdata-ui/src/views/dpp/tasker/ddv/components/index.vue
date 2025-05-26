<template>
  <div class="app-container" ref="app-container" v-loading="loading" style="overflow: hidden !important">
    <div class="head-container">
      <div class="head-title">
        {{ nodeData.name != "" ? nodeData.name : "数据开发任务" }}
      </div>
      <div class="head-btns">
        <el-button type="primary" size="small" @click="handleExportData" v-if="!route.query.info">保存</el-button>
        <el-button type="primary" size="small" @click="routeTo('/dpp/tasker/dpptaskerddv', '')">取消</el-button>
        <el-button type="primary" size="small" @click="openTaskConfigDialog">任务配置</el-button>
        <el-button type="primary" size="small" v-if="!route.query.info"
          @click="selectTab('checkMessage')">任务检查</el-button>
      </div>
    </div>
    <el-row>
      <sql-editor ref="editorRef" :value="form.taskParams.sql" class="sql-editor" :height="'calc(100vh - 180px)'"
        :readonly="route.query?.info" @changeTextarea="changeTextarea($event)" />
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
    </el-row>
    <taskConfigDialog :visible="taskConfigDialogVisible" title="任务配置" @update:visible="taskConfigDialogVisible = $event"
      @confirm="handletaskConfig" :data="form" :userList="userList" :deptOptions="deptOptions"
      :info="route.query.info" />
  </div>
</template>
<script setup>
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import SqlEditor from "@/components/SqlEditor/index1.vue";
import taskConfigDialog from "./taskConfigDialog.vue";
import useUserStore from "@/store/system/user";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataDevCat } from "@/api/att/cat/attDataDevCat/attDataDevCat";

const userStore = useUserStore();
import {
  createProcessDefinition,
  dppEtlTask,
  updateProcessDefinition,
} from "@/api/dpp/etl/dppEtlTask";
import { treeData } from "../components/data";

import { right } from "@antv/x6/lib/registry/port-layout/line";

const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();
let loading = ref(false);
let form = ref({
  id: "",
  code: "", // 组件的 code
  taskType: "",
  name: "name", // 名字
  version: "", // 版本号
  componentType: "",
  taskConfig: {
    name: "",
    catCode: "",
    personCharge: "",
    contactNumber: "",
    releaseState: "0",
    description: "",
  },
  taskParams: {
    sqlType: "0",
    type: "",
    sql: "",
    typaCode: "DM", // 默认值
    localParams: [],
    datasources: {
      datasourceId: "", // 默认值
      datasourceType: "",
      dbname: "",
    },
  },
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
let hasUnsavedChanges = ref(false);
let nodeData = ref({ name: "", taskConfig: {} });
// 任务配置
const taskConfigDialogVisible = ref(false);
let editorRef = ref("");
function changeTextarea(val) {
  form.value.taskParams.sql = val;
  hasUnsavedChanges.value = true;
}
let deptTreeRef = ref();
const handleNodeClick = (checkedNodes, checkedKeys, data, event) => {
  if (checkedKeys.disabled) {
    event.preventDefault();
    return;
  }
  form.value.taskParams.typaCode = checkedKeys.data.label;
  form.value.taskParams.datasources.datasourceId = "";
  taskConfigDialogVisible.value = true;
};

function getList() {
  loading.value = true;
  dppEtlTask(route.query.id).then((response) => {
    nodeData.value = response.data.taskConfig;
    form.value = {
      ...response.data.taskDefinitionList[0],
      taskConfig: response.data.taskConfig,
      id: response.data.id,
    };
    loading.value = false;
    // 部门
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
          id: "",
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
// 运行实例保存
const handletaskConfig = (obj) => {
  form.value = { ...obj };
  form.value.name = obj.taskConfig.name;
};
const dataJson = () => {
  const { taskConfig, ...taskDefinition } = form.value;
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
  console.log("🚀 ~ dataJson ~ taskRelationData:", taskRelationData);

  const locations = [{ taskCode: form.value?.code, x: 0, y: 0 }];
  // 返回这三个值
  return {
    taskRelationJson: JSON.stringify([taskRelationData]),
    locations,
    taskDefinitionList: JSON.stringify([taskDefinition]),
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    ...taskConfig,
    type: "3",
  };
};
let exportData2 = ref();
// 大保存
const handleExportData = async () => {
  loading.value = true;
  try {
    if (!form.value.taskParams.sql) {
      return proxy.$modal.msgError("请输入sql语句");
    }
    if (!form.value?.taskConfig?.name) {
      taskConfigDialogVisible.value = true;
      return;
    }
    exportData2.value = dataJson();
    // 根据 nodeData.id 判断是更新还是创建
    const res = form.value?.id
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
  const message = form.value?.id ? "修改成功" : "新增成功";
  router.push("/dpp/tasker/dpptaskerddv");
  proxy.$modal.msgSuccess(message);
};

const handleError = (error) => {
  console.error("操作失败:", error);
  proxy.$modal.msgError(error.message || "发生错误");
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
    let res = isValidClick();
  }
  tabAreaStyle.value.bottom = "0px";
};
const validateGraph = () => {
  let errors = [];

  if (!form.value.taskParams.sql) {
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
const isValidClick = (tab) => {
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
</script>

<style scoped lang="less">
.app-container {
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
