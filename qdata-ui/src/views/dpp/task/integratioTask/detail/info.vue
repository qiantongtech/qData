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
  <div class="basicInfo">
    <el-descriptions title="" :column="2" border>
      <el-descriptions-item v-for="(item, index) in fileDesc" :key="index" label-class-name="base-label"
        :span="item.span" class-name="base-content">
        <template #label>
          <div class="cell-item">{{ item.label }}</div>
        </template>
        <div v-if="item.key == 'status'">
          <el-tag :type="item.status == -1 ? 'warning' : 'success'">{{ item.status == -1 ? td('dpp.info.draft', 'Draft') : td('dpp.info.completed', 'Completed')
          }}</el-tag>
        </div>
        <div v-else-if="item.key == 'type'">
          <dict-tag :options="auth_app_type" :value="dppEtlTaskDetail.type" />
        </div>
        <div v-else-if="item.key == 'publicFlag'">
          <dict-tag :options="auth_public" :value="dppEtlTaskDetail.publicFlag" />
        </div>
        <div v-else-if="item.key == 'crontab'">
          {{ cronToZh(dppEtlTaskDetail.crontab) || "-" }}
        </div>
        <div v-else-if="item.key == 'scheduler'">
          {{ getSchedulerLabel(dppEtlTaskDetail.scheduler) }}
        </div>
        <div v-else>{{ getDescValue(item) }}</div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script setup name="BasicInfo">
import moment from "moment";
import { cronToZh } from "@/utils/cronUtils";
import useDefaultLang from "@/composables/useDefaultLang"

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { auth_public, auth_app_type } = proxy.useDict("auth_public", "auth_app_type");

const schedulerOptions = [
  { label: "Quartz", value: "QUARTZ" },
  { label: "DolphinScheduler", value: "DOLPHINSCHEDULER" },
];

const props = defineProps({
  dppEtlTaskDetail: {
    type: Object,
    default: () => ({}),
  },
});

// public fields
const baseTable = [
  { key: "status", label: td('dpp.info.configStatus', 'Config Status'), value: "" },
  { key: "crontab", label: td('dpp.info.scheduleCycle', 'Schedule Cycle'), value: "" },
  { key: "executionType", label: td('dpp.info.executionStrategy', 'Execution Strategy'), value: "" },
  { key: "scheduler", label: td('dpp.info.scheduler', 'Scheduler'), value: "" },
  { key: "lastExecuteTime", label: td('dpp.info.recentRunTime', 'Recent Run Time'), value: "" },
  { key: "lastExecuteStatus", label: td('dpp.info.recentExecutionResult', 'Recent Execution Result'), value: "" },
  { key: "taskPriority", label: td('dpp.info.taskPriority', 'Task Priority'), value: "" },
  { key: "workerGroup", label: td('dpp.info.workerGroup', 'Worker Group'), value: "" },
  { key: "yarnQueue", label: td('dpp.info.yarnQueue', 'Yarn Queue'), value: "" },
  { key: "failRetryTimes", label: td('dpp.info.failRetryTimes', 'Retry Count on Failure'), value: "" },
  { key: "failRetryInterval", label: td('dpp.info.failRetryInterval', 'Retry Interval'), value: "" },
  { key: "delayTime", label: td('dpp.info.delayExecutionTime', 'Delay Execution Time'), value: "", type: "time" },
  { key: "taskType", label: td('dpp.info.executionEngine', 'Execution Engine'), value: "" },
];

// Spark field
const sparkFields = [
  { key: "driverCores", label: td('dpp.info.driverCores', 'Driver Cores'), value: "" },
  { key: "driverMemory", label: td('dpp.info.driverMemory', 'Driver Memory'), value: "" },
  { key: "numExecutors", label: td('dpp.info.executorCount', 'Executor Count'), value: "" },
  { key: "executorMemory", label: td('dpp.info.executorMemory', 'Executor Memory'), value: "" },
  { key: "executorCores", label: td('dpp.info.executorCores', 'Executor Cores'), value: "" },
];

// Flink fields
const flinkFields = [
  { key: "jobManagerMemory", label: td('dpp.info.jobManagerMemory', 'JobManager Memory'), value: "" },
  { key: "taskManagerMemory", label: td('dpp.info.taskManagerMemory', 'TaskManager Memory'), value: "" },
  { key: "slot", label: td('dpp.info.slotCount', 'Slot Count'), value: "" },
  { key: "taskManager", label: td('dpp.info.taskManagerCount', 'TaskManager Count'), value: "" },
  { key: "parallelism", label: td('dpp.info.parallelism', 'Parallelism'), value: "" },
];

// Dynamically generate fileDesc
const fileDesc = computed(() => {
  const type = props.dppEtlTaskDetail?.taskType;
  let table = [...baseTable];

  if (type === "SPARK") {
    table = table.concat(sparkFields);
  } else if (type === "FLINK") {
    table = table.concat(flinkFields);
  }
  return table;
});

// Get field value
const getDescValue = (row) => {
  const detail = props.dppEtlTaskDetail || {};
  if (row.type === "time") {
    row.value = detail[row.key] ? moment(detail[row.key]).format("YYYY-MM-DD HH:mm") : "-";
  } else {
    row.value = detail[row.key];
  }
  return row.value !== null && row.value !== undefined && row.value !== "" ? row.value : "-";
};
const getSchedulerLabel = (value) => {
  return schedulerOptions.find((item) => item.value == value)?.label || value || "-";
};
</script>
<style lang="scss" scoped>
:deep(.base-label) {
  width: 200px;

  .cell-item {
    font-weight: 500;
  }
}
</style>
