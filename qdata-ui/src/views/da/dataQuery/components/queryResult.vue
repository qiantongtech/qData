<template>
  <el-dialog v-model="visibleDialog" draggable class="medium-dialog" :title="title" destroy-on-close @close="clearData">
    <div>
      <!-- 导出按钮 -->
      <el-button :disabled="!callData.dataTotal > 0" type="warning" plain icon="Download"
        @click="downloadMethodNotification" :loading="downloadLoading">导出</el-button>

      <el-table :data="callData.dataList" stripe border height="540" v-loading="loading"
        style="width: 100%; margin: 15px 0">
        <el-table-column label="序号" width="80" align="center" v-if="callData.dataTotal > 0">
          <template #default="scope">
            <span>{{
              (callData.pageNum - 1) * callData.pageSize + scope.$index + 1
            }}</span>
          </template>
        </el-table-column>

        <!-- 动态生成列 -->
        <template v-for="column in callData.columnList" :key="column">
          <el-table-column :prop="column" :label="column" align="center" :min-width="180" :show-overflow-tooltip="{effect: 'light'}" />
        </template>

        <!-- 如果没有数据时，显示暂无记录 -->
        <template #empty>
          <div class="emptyBg">
            <!-- <img src="@/assets/system/images/no_data/noData.png" alt="" /> -->
            <p>暂无记录</p>
          </div>
        </template>
      </el-table>
      <pagination v-show="callData.dataTotal > 0" :total="callData.dataTotal" v-model:page="callData.pageNum"
        v-model:limit="callData.pageSize" @pagination="handleQuery" />
    </div>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, getCurrentInstance } from "vue";
import { encrypt } from "@/utils/aesEncrypt";
import { executeSqlQuery } from "@/api/da/dataSource/dataSource";

const { proxy } = getCurrentInstance();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  queryParams: { type: Object, default: () => ({}) },
  spl: { type: String, default: "" },
});

let loading = ref(false);
let downloadLoading = ref(false);
const callData = ref({
  dataList: [],
  columnList: [],
  pageNum: 1,
  pageSize: 20, // 查询每页默认20条
  dataTotal: 0,
});

const emit = defineEmits(["update:visible", "confirm"]);

const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      const sqlText = encrypt(props.spl);
      callData.value = {
        ...props.queryParams,
        sqlText,
      };
      handleQuery();
    }
  }
);

async function handleQuery() {
  loading.value = true;
  try {
    const response = await executeSqlQuery(callData.value);
    const { data } = response;
    const dataList = Array.isArray(data.data) ? [...data.data] : [];
    const columnList = dataList.length > 0 ? Object.keys(dataList[0]) : [];
    callData.value.dataList = dataList;
    callData.value.columnList = columnList;
    callData.value.dataTotal = data.total || 0;
  } catch (error) {
    throw error; // 👈 抛出错误给调用方处理
  } finally {
    loading.value = false;
  }
}

const closeDialog = () => {
  callData.value = {
    dataList: [],
    columnList: [],
    dataTotal: 0,
    pageNum: 1,
    pageSize: 20,
  };
  emit("update:visible", false);
};

const clearData = () => {
  callData.value.dataList = [];
  callData.value.columnList = [];
  callData.value.dataTotal = 0;
};

// 计算总文件数（导出用）
const totalFiles = computed(() => Math.ceil(callData.value.dataTotal / 5000));

// 导出逻辑（不影响当前分页）
const downloadMethod = () => {
  const pageSize = 5000;
  const total = callData.value.dataTotal;
  let pageNum = 1;

  downloadLoading.value = true;

  if (total === 0) {
    ElNotification({
      title: "提示",
      message: "该表没有数据",
      type: "info",
      duration: 2000,
    });
    downloadLoading.value = false;
    return;
  }

  const exportParams = {
    ...callData.value,
    pageSize,
    exportType: 0,
  };

  while ((pageNum - 1) * pageSize < total) {
    exportParams.pageNum = pageNum;
    proxy.download2(
      "/da/daDatasource/exportSqlQueryResult/export",
      exportParams,
      `${new Date().getTime()}_${pageNum}.xlsx`
    );
    pageNum++;
  }

  downloadLoading.value = false;
};

// 导出确认提示
const downloadMethodNotification = () => {
  const totalFilesCount = totalFiles.value;

  ElMessageBox.confirm(
    `是否导出总数为：<span style="color: rgb(0, 160, 233);">${callData.value.dataTotal}</span>，以每5000数据一份文件进行导出，总共导出 ${totalFilesCount} 份？`,
    "提示",
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    downloadMethod();
  });
};
</script>

<style lang="scss" scoped>
.column-header {
  display: flex;
  flex-direction: column;
}

.column-item {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
