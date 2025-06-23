<template>
  <div class="app-container" ref="app-container" style="overflow: hidden !important">
    <!-- 头部区域 -->
    <div class="head-container">
      <div class="head-title">
        {{ nodeData.name != "" ? nodeData.name : "数据查询" }}
      </div>
      <div class="head-btns">
        <el-form ref="daDiscoveryTaskRef" :model="queryParams" label-width="0px" @submit.prevent>
          <el-form-item label="" prop="datasourceId">
            <el-select class="head-select" v-model="queryParams.id" placeholder="请选择数据源" filterable size="default"
              style="width: 210px" :loading="loading">
              <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-button plain type="primary" size="small" @click="handleQuery"><i
            class="iconfont-mini icon-a-zu22377 mr5"></i>查询</el-button>
        <el-button type="primary" size="small" @click="handleClear">清除</el-button>
      </div>
    </div>
    <!-- 编辑区域 -->
    <el-row>
      <sql-editor ref="editorRef" :value="queryParams.sqlText" class="sql-editor" :height="'calc(100vh - 180px)'"
        @changeTextarea="handleChangeTextarea" placeholder="select * FROM log_table" />
    </el-row>
    <TableInfoDialog :visible="dialogVisible" title="查询结果" @update:visible="dialogVisible = $event"
      :queryParams="queryParams" />
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import SqlEditor from "@/components/SqlEditor/index1.vue";
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/datasource/daDatasource";
import TableInfoDialog from "./components/TableInfoDialog";
import {
  getDaDatasourceList,
} from "@/api/dp/model/dpModel";
import { executeSqlQuery } from "@/api/da/datasource/daDatasource";
import { encrypt } from "@/utils/aesEncrypt";
// 定义查询参数、下拉数据以及加载状态
let queryParams = ref({
  pageNum: 1,
  pageSize: 20,
  sqlText: "",
  id: "",
});
let createTypeList = ref([]);
const loading = ref(false);

// 节点数据及其他相关变量
let nodeData = ref({ name: "", taskConfig: {} });
const dialogVisible = ref(false);
const queryResult = ref("");
const editorRef = ref(null);

// 监听 sql-editor 文本变化
function handleChangeTextarea(val) {
  queryParams.value.sqlText = val;
}

// 查询按钮点击处理函数
async function handleQuery() {
  const errors = [];

  if (!queryParams.value.sqlText) {
    errors.push("请输入 SQL 语句！");
  }
  if (!queryParams.value.id) {
    errors.push("请选择数据源！");
  }

  if (errors.length) {
    ElMessage.error(errors.join("，"));
    return;
  }
  queryResult.value = "正在查询，请稍后...";
  const response = await executeSqlQuery({
    ...queryParams.value,
    sqlText: encrypt(queryParams.value.sqlText),
  });

  if (response.code === 200) {
    queryResult.value = response.data;
    dialogVisible.value = true;
  } else {
    ElMessage.error(response.msg || "查询失败");
    queryResult.value = "";
  }
}


// 清除 SQL 编辑器内容
function handleClear() {
  editorRef.value?.clear();
}

// 获取数据源函数
function getDaDatasource() {
  loading.value = true;
  getDaDatasourceList({
    // projectCode: userStore.projectCode,
    // projectId: userStore.projectId,
  })
    .then((response) => {
      createTypeList.value = response.data;
      loading.value = false;
    })
    .catch(() => {
      loading.value = false;
    });
}
// watch(
//   () => [userStore.projectCode, userStore.projectId],
//   ([newCode, newId]) => {
//     if (newCode && newId) {
//       getDaDatasource();
//     }
//   },
//   { immediate: true }
// );
getDaDatasource();
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
    padding: 0 15px;
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
      display: flex;
      align-items: center;

      .head-select {
        height: 18px;
        margin-right: 10px;
      }

      .el-button {
        height: 28px;
        margin-left: 5px;
      }
    }
  }
}

.sql-editor {
  width: 100%;
}

.dialog-content {
  padding: 20px;
  font-size: 14px;
}
</style>