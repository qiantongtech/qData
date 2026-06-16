<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="medium-dialog"
    :title="title"
    destroy-on-close
  >
    <el-table stripe height="580px" v-loading="loading" :data="jobLogList">
      <el-table-column
        width="150"
        :label="td('dpp.instance.integratioTask.id', '编号')"
        align="left"
        prop="id"
      />
      <el-table-column
        :show-overflow-tooltip="{ effect: 'light' }"
        :label="td('dpp.instance.integratioTask.taskInstanceName', '任务名称')"
        align="left"
        width="300"
        prop="taskInstanceName"
      >
        <template #default="scope">
          {{ scope.row.name || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        width="100"
        :label="td('dpp.instance.integratioTask.executionType', '执行类型')"
        align="left"
        prop="commandType"
      >
        <template #default="scope">
          <dict-tag
            :options="dpp_etl_task_instance_command_type"
            :value="scope.row.commandType.trim()"
          />
        </template>
      </el-table-column>
      <el-table-column
        width="100"
        :label="td('dpp.instance.integratioTask.executionStatus', '执行状态')"
        align="left"
        prop="status"
      >
        <template #default="scope">
          <dict-tag
            :options="dpp_etl_node_instance"
            :value="scope.row.status.trim()"
          />
        </template>
      </el-table-column>
      <el-table-column
        width="160"
        :label="td('dpp.instance.integratioTask.startTime', '开始时间')"
        align="left"
        prop="startTime"
        :show-overflow-tooltip="{ effect: 'light' }"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.startTime, "{y}-{m}-{d} {h}:{i}") || "-"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="160"
        :label="td('dpp.instance.integratioTask.endTime', '结束时间')"
        align="left"
        prop="endTime"
        :show-overflow-tooltip="{ effect: 'light' }"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.endTime, "{y}-{m}-{d} {h}:{i}") || "-"
          }}</span>
        </template>
      </el-table-column>

      <el-table-column
        :label="td('dpp.instance.integratioTask.extractionCount', '抽取量')"
        align="left"
        prop="extractionCount"
        width="80"
      >
        <template #default="scope">
          {{ "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('dpp.instance.integratioTask.writeCount', '写入量')"
        align="left"
        prop="writeCount"
        width="80"
      >
        <template #default="scope">
          {{ "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('dpp.instance.integratioTask.responsiblePerson', '责任人')"
        align="left"
        prop="personChargeName"
        width="80"
      >
        <template #default="scope">
          {{ scope.row.personChargeName || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('common.texts.operation', '操作')"
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
            >{{
              td("dpp.instance.integratioTask.detail", "查看日志")
            }}</el-button
          >
          <el-button
            link
            type="warning"
            @click="handleExport(scope.row)"
            icon="Download"
            @mousedown="(e) => e.preventDefault()"
          >
            {{ td("dpp.instance.integratioTask.downloadLog", "下载日志") }}
          </el-button>
        </template>
      </el-table-column>
      <template #empty>
        <div class="emptyBg">
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
    <template #footer>
      <div style="text-align: right">
        <el-button @click="visibleDialog = false">{{
          td("common.button.close", "关闭")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 调度日志详细 -->
  <el-dialog
    :title="td('dpp.instance.integratioTask.detail', '查看日志')"
    v-model="open"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
  >
    <div v-html="formattedText"></div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="open = false">{{
          td("common.button.close", "关闭")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
  <TaskLogDialog ref="logDialogRef" />
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import { listDppEtlTaskInstance } from "@/api/dpp/instance/job";
import TaskLogDialog from "@/views/dpp/components/taskLog.vue";
import {
  getRunTaskInstance,
  getLogByTaskInstanceId,
} from "@/api/dpp/task/index.js";
import request from "@/utils/request";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dpp_etl_node_instance } = proxy.useDict("dpp_etl_node_instance");
const { dpp_etl_node_type, dpp_etl_task_instance_command_type } = proxy.useDict(
  "dpp_etl_node_type",
  "dpp_etl_task_instance_command_type"
);
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  taskType: { type: Number, default: 1 },
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
  return msg.value ? msg.value.replace(/\n/g, "<br>") : ""; // 将换行符替换为 <br> 标签
});
/** 导出按钮操作 */
async function handleExport(row) {
  proxy.download(
    "/dpp/etlTaskInstance/downloadLog",
    {
      taskInstanceId: row.id,
    },
    `${row.name}.log`
  );
}
let msg = ref();
async function logDetailCatList(row) {
  msg.value = {};
  const response = await getLogByTaskInstanceId({ taskInstanceId: row.id });
  msg.value = response.data.log;
  open.value = true;
}
const logDialogRef = ref(null);

// 打开日志弹窗
// const logDetailCatList = (row) => {
//     logDialogRef.value.open(row.id);
// };
const total = ref(0);
const dateRange = ref([]);
let jobLogList = ref([]);
let loading = ref(false);
/** 查询调度日志列表 */
function getList() {
  loading.value = true;
  queryParams.value.taskId = props.data.id;
  queryParams.value.taskType = props.taskType;
  listDppEtlTaskInstance({
    ...queryParams.value,
  }).then((response) => {
    jobLogList.value = response.data.rows;
    console.log(
      "🚀 ~ listDppEtlTaskInstance ~ jobLogList.value:",
      jobLogList.value
    );
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

// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// 关闭对话框的方法
const closeDialog = () => {
  emit("update:visible", false);
};

// 保存数据的方法
const saveData = () => {
  emit("confirm", localNode.value);
  emit("update:visible", false);
};
</script>
