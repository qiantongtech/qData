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
  <div class="basicInfo">
    <el-descriptions title="" :column="2" border>
      <el-descriptions-item v-for="(item, index) in fileDesc" :key="index" label-class-name="base-label"
        :span="item.span" class-name="base-content">
        <template #label>
          <div class="cell-item">{{ item.label }}</div>
        </template>
        <div v-if="item.key == 'status'">
          <el-tag :type="item.status == -1 ? 'warning' : 'success'">{{ item.status == -1 ? td('dpp.info.draft', '草稿') : td('dpp.info.completed', '完成')
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

const props = defineProps({
  dppEtlTaskDetail: {
    type: Object,
    default: () => ({}),
  },
});

// 公共字段
const baseTable = [
  { key: "status", label: td('dpp.info.configStatus', '配置状态'), value: "" },
  { key: "crontab", label: td('dpp.info.scheduleCycle', '调度周期'), value: "" },
  { key: "executionType", label: td('dpp.info.executionStrategy', '执行策略'), value: "" },
  { key: "lastExecuteTime", label: td('dpp.info.recentRunTime', '最近运行时间'), value: "" },
  { key: "lastExecuteStatus", label: td('dpp.info.recentExecutionResult', '最近执行结果'), value: "" },
  { key: "taskPriority", label: td('dpp.info.taskPriority', '任务优先级'), value: "" },
  { key: "workerGroup", label: td('dpp.info.workerGroup', 'Worker分组'), value: "" },
  { key: "yarnQueue", label: td('dpp.info.yarnQueue', 'Yarn队列'), value: "" },
  { key: "failRetryTimes", label: td('dpp.info.failRetryTimes', '失败重试次数'), value: "" },
  { key: "failRetryInterval", label: td('dpp.info.failRetryInterval', '失败重试间隔'), value: "" },
  { key: "delayTime", label: td('dpp.info.delayExecutionTime', '延迟执行时间'), value: "", type: "time" },
  { key: "taskType", label: td('dpp.info.executionEngine', '执行引擎'), value: "" },
];

// Spark 字段
const sparkFields = [
  { key: "driverCores", label: td('dpp.info.driverCores', 'Driver核心数'), value: "" },
  { key: "driverMemory", label: td('dpp.info.driverMemory', 'Driver内存数'), value: "" },
  { key: "numExecutors", label: td('dpp.info.executorCount', 'Executor数量'), value: "" },
  { key: "executorMemory", label: td('dpp.info.executorMemory', 'Executor内存数'), value: "" },
  { key: "executorCores", label: td('dpp.info.executorCores', 'Executor核心数'), value: "" },
];

// Flink 字段
const flinkFields = [
  { key: "jobManagerMemory", label: td('dpp.info.jobManagerMemory', 'JobManager内存数'), value: "" },
  { key: "taskManagerMemory", label: td('dpp.info.taskManagerMemory', 'TaskManager内存数'), value: "" },
  { key: "slot", label: td('dpp.info.slotCount', 'Slot数量'), value: "" },
  { key: "taskManager", label: td('dpp.info.taskManagerCount', 'TaskManager数量'), value: "" },
  { key: "parallelism", label: td('dpp.info.parallelism', '并行度'), value: "" },
];

// 动态生成 fileDesc
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

// 获取字段值
const getDescValue = (row) => {
  const detail = props.dppEtlTaskDetail || {};
  if (row.type === "time") {
    row.value = detail[row.key] ? moment(detail[row.key]).format("YYYY-MM-DD HH:mm") : "-";
  } else {
    row.value = detail[row.key];
  }
  return row.value !== null && row.value !== undefined && row.value !== "" ? row.value : "-";
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
