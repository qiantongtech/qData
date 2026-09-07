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
        className: 'clearfixs',
        nameKey: 'name',
        statusKey: 'status',
        statusOptions: taskStatusOptions,
      }"
      :items="detailItems"
      mode="free"
    >
      <template #schedulerState="{ data }">
        <el-tag :type="data.schedulerState == '0' ? 'success' : 'danger'">
          {{ data.schedulerState == '0' ? td('dpp.integratioTask.on', 'On') : td('dpp.integratioTask.off', 'Off') }}
        </el-tag>
      </template>
      <template #configStatus="{ data }">
        <el-tag :type="data.status == -1 ? 'warning' : 'success'">
          {{ data.status == -1 ? td('dpp.info.draft', 'Draft') : td('dpp.info.completed', 'Completed') }}
        </el-tag>
      </template>
    </DetailInfo>

    <div class="pagecont-bottom" v-loading="loading">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane :label="td('dpp.integratioTask.taskFlow', 'Task Flow')" name="1" key="1">
          <process ref="compRef" :dppEtlTaskDetail="dppEtlTaskDetail" />
        </el-tab-pane>
        <el-tab-pane :label="td('dpp.integratioTask.detailInfo', 'Detail Info')" name="2" key="2">
          <info :dppEtlTaskDetail="dppEtlTaskDetail" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { etlTask } from "@/api/dpp/task/index.js";
import { useRoute } from "vue-router";
import process from "@/views/dpp/task/integratioTask/detail/process.vue";
import info from "@/views/dpp/task/integratioTask/detail/info.vue";
import DetailInfo from "@/components/DetailInfo/index.vue";
import { onActivated, onDeactivated, reactive, ref, toRefs, watch, getCurrentInstance, computed } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dpp_etl_task_status } = proxy.useDict("dpp_etl_task_status");

const taskStatusOptions = computed(() => {
  const options = dpp_etl_task_status.value || [];
  const hasMinusOne = options.some(opt => String(opt.value) === '-1');
  const hasZero = options.some(opt => String(opt.value) === '0');
  
  const extraOptions = [];
  if (!hasMinusOne) {
    extraOptions.push({ label: td('dpp.integratioTask.off', 'Off'), value: '-1', elTagType: 'danger' });
  }
  if (!hasZero) {
    extraOptions.push({ label: td('dpp.integratioTask.off', 'Off'), value: '0', elTagType: 'danger' });
  }
  
  return [...options, ...extraOptions];
});

const activeName = ref("1");
const showSearch = ref(true);
const route = useRoute();
let loading = ref(false);
const data = reactive({
  dppEtlTaskDetail: {},
  form: {}
});
let compRef = ref(null);
const { dppEtlTaskDetail } = toRefs(data);

const detailItems = computed(() => [
  {
    label: td("dpp.integratioTask.dataIntegrationCategory", "Data Integration Category"),
    key: "catName",
  },
  {
    label: td("dpp.developTask.scheduleStatus", "Schedule Status"),
    slot: "schedulerState",
  },
  {
    label: td("dpp.integratioTask.responsiblePerson", "Responsible Person"),
    key: "personChargeName",
  },
  {
    label: td("common.texts.description", "Description"),
    key: "description",
    span: 24,
    className: "mt2 mb2",
  },
  {
    label: td("dpp.info.configStatus", "Config Status"),
    slot: "configStatus",
  },
  {
    label: td("dpp.integratioTask.contactNumber", "Contact Phone"),
    key: "contactNumber",
  },
  {
    label: td("common.texts.createdTime", "Created Time"),
    key: "createTime",
    type: "time",
  },
  
]);
function getDppEtlTaskDetailById(id) {
  if (!id) return;
  loading.value = true;
  etlTask(id).then(response => {
    dppEtlTaskDetail.value = {
      ...response.data,
      ...JSON.parse(response.data.draftJson || "{}"),
      catName: response.data.catName
    };
    compRef.value?.updateFlow(dppEtlTaskDetail.value);
    loading.value = false;

  });
}
watch(
  () => route.query.id,
  (newId) => {
    getDppEtlTaskDetailById(newId);
  },
  { immediate: true }
);
onDeactivated(() => {
  activeName.value = '1'
  dppEtlTaskDetail.value = { taskConfig: {}, name: null };

});
const handleClick = (tab, event) => {
  console.log(tab, event);
};
</script>

<style lang="less" scoped>
.pagecont-bottom {
  min-height: calc(100vh - 340px) !important;
}
</style>
