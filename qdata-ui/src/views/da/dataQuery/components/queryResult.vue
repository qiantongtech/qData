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
  <el-dialog v-model="visibleDialog" draggable class="medium-dialog" :title="effectiveTitle" destroy-on-close @close="clearData">
    <div>
      <!-- export button -->
      <el-button :disabled="!callData.dataTotal > 0" type="warning" plain icon="Download"
        @click="downloadMethodNotification" :loading="downloadLoading">{{ td('da.qualityTask.dataQuery.queryResult.export') }}</el-button>

      <el-table :data="callData.dataList" stripe border height="540" v-loading="loading"
        style="width: 100%; margin: 15px 0">
        <el-table-column :label="td('da.qualityTask.dataQuery.queryResult.serialNumber')" width="80" align="center" v-if="callData.dataTotal > 0">
          <template #default="scope">
            <span>{{
              (callData.pageNum - 1) * callData.pageSize + scope.$index + 1
            }}</span>
          </template>
        </el-table-column>

        <!-- Dynamically generate columns -->
        <template v-for="column in callData.columnList" :key="column">
          <el-table-column :prop="column" :label="column" align="center" :min-width="180" :show-overflow-tooltip="{effect: 'light'}" />
        </template>

        <!-- If there is no data, it will display that there is no record yet. -->
        <template #empty>
          <div class="emptyBg">
            <!-- <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" /> -->
            <p>{{td('common.noData')}}</p>
          </div>
        </template>
      </el-table>
      <pagination v-show="callData.dataTotal > 0" :total="callData.dataTotal" v-model:page="callData.pageNum"
        v-model:limit="callData.pageSize" @pagination="handleQuery" />
    </div>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{ td('da.qualityTask.dataQuery.queryResult.close') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, getCurrentInstance } from "vue";
import { encrypt } from "@/utils/aesEncrypt";
import { executeSqlQuery } from "@/api/da/dataSource/dataSource";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "查询结果" },
  queryParams: { type: Object, default: () => ({}) },
  spl: { type: String, default: "" },
});
const effectiveTitle = computed(() => props.title || td('da.qualityTask.dataQuery.queryResult.formTitle'));

let loading = ref(false);
let downloadLoading = ref(false);
const callData = ref({
  dataList: [],
  columnList: [],
  pageNum: 1,
  pageSize: 20, // Query default 20 items per page
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
    throw error; // 👈 Throw an error to the caller for processing
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

// Calculate the total number of files (for export)
const totalFiles = computed(() => Math.ceil(callData.value.dataTotal / 5000));

// Export logic (does not affect current paging)
const downloadMethod = () => {
  const pageSize = 5000;
  const total = callData.value.dataTotal;
  let pageNum = 1;

  downloadLoading.value = true;

  if (total === 0) {
    ElNotification({
      title: td('da.qualityTask.dataQuery.queryResult.notification.title'),
      message: td('da.qualityTask.dataQuery.queryResult.notification.noData'),
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
      "/da/dataSource/exportSqlQueryResult/export",
      exportParams,
      `${new Date().getTime()}_${pageNum}.xlsx`
    );
    pageNum++;
  }

  downloadLoading.value = false;
};

// Export confirmation prompt
const downloadMethodNotification = () => {
  const totalFilesCount = totalFiles.value;

  ElMessageBox.confirm(
    td('da.qualityTask.dataQuery.queryResult.notification.exportConfirm', '', { total: callData.value.dataTotal, files: totalFilesCount }),
    td('da.qualityTask.dataQuery.queryResult.notification.title'),
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: td('da.qualityTask.dataQuery.queryResult.notification.confirm'),
      cancelButtonText: td('da.qualityTask.dataQuery.queryResult.notification.cancel'),
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
