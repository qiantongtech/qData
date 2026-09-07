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
  <div class="app-container" ref="app-container">
    <DetailInfo
      :show="showSearch"
      :data="dppEtlTaskDetail"
      :header="{
        nameKey: 'names',
        idKey: 'id',
        statusKey: 'status',
        statusOptions: dpp_etl_node_instance,
      }"
      :items="detailItems"
      mode="free"
    />

    <div class="pagecont-bottom" v-loading="loading">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane :label="td('dpp.instance.taskFlow', 'Task Flow')" name="1" key="1">
          <processNode ref="compRef" />
        </el-tab-pane>
        <el-tab-pane :label="td('dpp.instance.taskLog', 'Task Log')" name="2" key="2">
          <instanceLog ref="compReftwo" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { useRoute } from "vue-router";
import processNode from "./processNode.vue";
import instanceLog from "./instanceLog.vue";
import { reactive, ref, toRefs, watch, getCurrentInstance, computed, onUnmounted, onDeactivated } from "vue";
import { getLogByTaskInstanceId, getTaskInfo } from "@/api/dpp/task/etlTask";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dpp_etl_node_type, dpp_etl_task_instance_command_type, dpp_etl_node_instance } = proxy.useDict(
  "dpp_etl_node_type",
  "dpp_etl_task_instance_command_type",
  "dpp_etl_node_instance"
);
const activeName = ref("1");
const showSearch = ref(true);
const route = useRoute();
let loading = ref(false);
let compReftwo = ref(null);
const data = reactive({
  dppEtlTaskDetail: {},
  form: {}
});
const { dppEtlTaskDetail } = toRefs(data);
const detailItems = computed(() => [
  {
    label: td('common.texts.createdBy', 'Created By'),
    key: 'createBy',
     className: "mt2 mb2",
  },
  
  {
    label: td('common.texts.createdTime', 'Created Time'),
    key: 'createTime',
    formatter: (val) => proxy.parseTime(val, '{y}-{m}-{d} {h}:{i}'),
     className: "mt2 mb2",
  },
  
  {
    label: td('dpp.instance.responsiblePerson', 'Responsible Person'),
    key: 'personChargeName',
     className: "mt2 mb2",
    
  },
  {
    label: td('dpp.instance.executionType', 'Execution Type'),
    key: 'commandType',
    dictOptions: dpp_etl_task_instance_command_type.value
  },
  {
    label: td('dpp.instance.startTime', 'Start Time'),
    key: 'startTime',
    formatter: (val) => proxy.parseTime(val, '{y}-{m}-{d} {h}:{i}')
  },
  {
    label: td('dpp.instance.endTime', 'End Time'),
    key: 'endTime',
    formatter: (val) => proxy.parseTime(val, '{y}-{m}-{d} {h}:{i}')
  }
]);
let compRef = ref(null);
let logContent = ref("");
let polling = ref(false);
const getTask = async (taskId) => {
  if (!taskId) return
  const res = await getTaskInfo(taskId);
  dppEtlTaskDetail.value = { ...res.data.taskInstance, names: res.data.name };
  compRef.value?.updateFlow(res.data);

  return res.data;
};

let timer = null;

const fetchLog = async (taskId) => {
  if (!taskId || !polling.value) return;

  const res = await getLogByTaskInstanceId({ taskInstanceId: taskId });
  const { status, log, nodeInstanceList } = res.data;
  logContent.value = log;
  compRef.value?.updateGraphNode(nodeInstanceList);
  compReftwo.value?.updateLog(log);
  const s = Number(status);
  if ([5, 6, 7].includes(s)) {
    polling.value = false;
    return;
  }
  if (polling.value) {
    timer = setTimeout(() => {
      if (!polling.value) return;
      fetchLog(taskId);
    }, 3000);
  }
};
watch(
  () => route.query.id,
  async (newId) => {
    const taskData = await getTask(newId);
    polling.value = true;
    fetchLog(newId);
  },
  { immediate: true }
);

// Cleanup function
const clearPolling = () => {
  polling.value = false;
  if (timer) {
    clearTimeout(timer);
    timer = null;
  }
};
onUnmounted(clearPolling);
onDeactivated(() => {
  clearPolling();
  dppEtlTaskDetail.value = { taskConfig: {}, name: null };
  logContent.value = "";
  activeName.value = "1";
});
const handleClick = (tab, event) => {
  console.log(tab, event);
};
</script>

<style lang="less" scoped>
.pagecont-bottom {
  min-height: calc(100vh - 350px) !important;
}
</style>
