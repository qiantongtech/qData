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
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">

        <div class="infotop-title mb15">
          <!-- <div class="h2-titles" style="font-weight: 600;">[&nbsp;{{ dpModelDetail.id || '-' }}&nbsp;]&nbsp;&nbsp;{{
            dpModelDetail.modelComment ||
            '' }}</div> -->
          <div class="task-item">
            <!-- square number -->
            <div class="task-id">
              {{ dppEtlTaskDetail.id || '-' }}
            </div>

            <!-- Name -->
            <div class="task-name">
              {{ dppEtlTaskDetail.name || '' }}
            </div>
          </div>
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dpp.integratioTask.responsiblePerson', '责任人') }}</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail?.personChargeName || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dpp.integratioTask.contactNumber', '联系电话') }}</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.contactNumber || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dpp.integratioTask.taskStatus', '任务状态') }}</div>
              <div class="infotop-row-value">
                <el-tag :type="dppEtlTaskDetail.status == '1' ? 'success' : 'danger'">
                  {{ dppEtlTaskDetail.status == '1' ? td('dpp.integratioTask.on', '开启') : td('dpp.integratioTask.off', '关闭') }}
                </el-tag>
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dpp.integratioTask.dataIntegrationCategory', '数据集成类目') }}</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.catName || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dpp.developTask.scheduleStatus', '调度状态') }}</div>
              <div class="infotop-row-value">
                <el-tag :type="dppEtlTaskDetail.schedulerState == '0' ? 'success' : 'danger'">
                  {{ dppEtlTaskDetail.schedulerState == '0' ? td('dpp.integratioTask.on', '开启') : td('dpp.integratioTask.off', '关闭') }}
                </el-tag>
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.createdTime', '创建时间') }}</div>
              <div class="infotop-row-value">
                {{ parseTime(dppEtlTaskDetail.createTime, '{y}-{m}-{d} {h}:{i}') }}
              </div>
            </div>
          </el-col>

          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.description', '描述') }}</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.description || '-' }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom" v-loading="loading">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane :label="td('dpp.integratioTask.taskFlow', '任务流程')" name="1" key="1">
          <process ref="compRef" :dppEtlTaskDetail="dppEtlTaskDetail" />
        </el-tab-pane>
        <el-tab-pane :label="td('dpp.integratioTask.detailInfo', '详细信息')" name="2" key="2">
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
import { onActivated, reactive, ref, toRefs, watch, getCurrentInstance } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
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
