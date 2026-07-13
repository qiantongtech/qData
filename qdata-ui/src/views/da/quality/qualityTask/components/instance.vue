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
  <!-- List of execution records -->
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="dialog"
    :title="title"
    style="width: 1200px"
    destroy-on-close
  >
    <el-table
      stripe
      height="380px"
      v-loading="loading"
      :data="jobLogList"
      :default-sort="defaultSort"
      @sort-change="handleSortChange"
    >
      <el-table-column
        :label="td('common.texts.number')"
        align="center"
        prop="id"
        width="80"
      />
      <el-table-column
        :label="td('da.qualityTaskLog.columnLabels.taskName')"
        align="center"
        prop="name"
      >
        <template #default="scope">
          {{ scope.row.name || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('da.qualityTaskLog.columnLabels.qualityScore')"
        align="center"
        prop="score"
        width="80"
      >
        <template #default="scope">
          {{ scope.row.score || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('da.qualityTaskLog.columnLabels.executionStatus')"
        align="center"
        prop="successFlag"
      >
        <template #default="scope">
          <dict-tag
            :options="quality_log_success_flag"
            :value="scope.row.successFlag"
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="td('da.qualityTaskLog.columnLabels.problemData')"
        align="center"
        prop="problemData"
      >
        <template #default="scope">
          {{ scope.row.problemData || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('da.qualityTaskLog.columnLabels.startTime')"
        align="center"
        prop="startTime"
        width="160"
        sortable="custom"
        column-key="start_time"
        :sort-orders="['descending', 'ascending']"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.startTime, "{y}-{m}-{d} {h}:{i}")
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="td('da.qualityTaskLog.columnLabels.endTime')"
        align="center"
        prop="endTime"
        width="160"
        sortable="custom"
        column-key="end_time"
        :sort-orders="['descending', 'ascending']"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, "{y}-{m}-{d} {h}:{i}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="td('common.texts.operation')"
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        width="200"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="View"
            @click="logDetailCatList(scope.row)"
            v-hasPermi="['monitor:job:query']"
            >{{ td("da.qualityTask.instanceComponent.view") }}</el-button
          >
          <!-- <el-button link type="warning" @click="handleExport(scope.row)" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-download-line mr5"></i>Download
          </el-button> -->
          <el-button
            link
            type="primary"
            icon="view"
            @click="
              routeTo('/da/quality/qualityTaskLog/detail', {
                ...scope.row,
                info: true,
              })
            "
            >{{ td("common.button.details") }}</el-button
          >
        </template>
      </el-table-column>

      <template #empty>
        <div class="emptyBg">
          <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
          <p>{{ td("common.noData") }}</p>
        </div>
      </template>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- <template #footer>
            <div style="text-align: right">
        <el-button @click="closeDialog">Close</el-button>
        <el-button type="primary" @click="saveData">Save</el-button>
        </div>
</template> -->
  </el-dialog>
  <!-- Scheduling log details -->
  <el-dialog
    :title="td('da.qualityTask.instanceComponent.viewLogTitle')"
    v-model="open"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
  >
    <div v-html="formattedText"></div>
    <!-- <template #footer>
            <div class="dialog-footer">
                <el-button @click="open = false">Close</el-button>
            </div>
        </template> -->
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const defaultSort = ref({ columnKey: "start_time", order: "desc" });
const { sys_common_status, sys_job_group, quality_log_success_flag } =
  proxy.useDict(
    "sys_common_status",
    "sys_job_group",
    "quality_log_success_flag"
  );
import {
  listDppQualityLog,
  getDppQualityLog,
  delDppQualityLog,
  addDppQualityLog,
  updateDppQualityLog,
} from "@/api/da/quality/qualityTaskLog";
import { qualityLogLogDetailCat } from "@/api/da/quality/qualityTask";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "" },
  data: { type: Object, default: () => ({}) },
});
const open = ref(false);
let form = ref();
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  nodeId: undefined,
  taskId: undefined,
});
const formattedText = computed(() => {
  return form.value.logContent.replace(/\n/g, "<br>");
});
const router = useRouter();

/** Sorting trigger events */
function handleSortChange({ column, prop, order }) {
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

function routeTo(link, row) {
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
          id: row?.id,
          info: row?.info,
          score: row.score,
        },
      });
    }
  }
}

async function logDetailCatList(row) {
  try {
    if (!row.path) {
      proxy.$message.warning(td("da.qualityTask.instanceComponent.noLogMsg"));
      return;
    }
    form.value = {};
    const response = await qualityLogLogDetailCat({ handleMsg: row.path });
    if (response && response.content) {
      form.value = response.content;
      open.value = true;
    }
  } catch (error) {}
}

const total = ref(0);
const dateRange = ref([]);
let jobLogList = ref([]);
let loading = ref(false);
/** Query scheduling log list */
function getList() {
  loading.value = true;
  queryParams.value.qualityId = props.data.id;
  listDppQualityLog({
    ...queryParams.value,
  }).then((response) => {
    jobLogList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}
const emit = defineEmits(["update:visible", "confirm"]);

watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      getList();
    } else {
      jobLogList.value = [];
    }
  }
);

// Computed property handling v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// How to close a dialog box
const closeDialog = () => {
  emit("update:visible", false);
};

// How to save data
const saveData = () => {
  emit("confirm", localNode.value); // Submit local data to parent component
  emit("update:visible", false);
};
</script>
