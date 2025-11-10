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
  For brand customization, please contact the official team for authorization.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
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
            <!-- 正方形编号 -->
            <div class="task-id">
              {{ dppEtlTaskDetail.id || '-' }}
            </div>

            <!-- 名称 -->
            <div class="task-name">
              {{ dppEtlTaskDetail.name || '' }}
            </div>
          </div>
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">责任人</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail?.personChargeName || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">联系电话</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.contactNumber || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务状态</div>
              <div class="infotop-row-value">
                <el-tag :type="dppEtlTaskDetail.status == '1' ? 'success' : 'danger'">
                  {{ dppEtlTaskDetail.status == '1' ? "开启" : "关闭" }}
                </el-tag>
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">数据集成类目</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.catName || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">调度状态</div>
              <div class="infotop-row-value">
                <el-tag :type="dppEtlTaskDetail.schedulerState == '0' ? 'success' : 'danger'">
                  {{ dppEtlTaskDetail.schedulerState == '0' ? "开启" : "关闭" }}
                </el-tag>
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">
                {{ parseTime(dppEtlTaskDetail.createTime, '{y}-{m}-{d} {h}:{i}') }}
              </div>
            </div>
          </el-col>

          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
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
        <el-tab-pane label="任务流程" name="1" key="1">
          <process ref="compRef" :dppEtlTaskDetail="dppEtlTaskDetail" />
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="2" key="2">
          <info :dppEtlTaskDetail="dppEtlTaskDetail" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { etlTask } from "@/api/dpp/task/index.js";
import { useRoute } from "vue-router";
import process from "@/views/dpp/task/integratioTask/detail/process.vue";
import info from "@/views/dpp/task/integratioTask/detail/info.vue";
import { onActivated, reactive, ref, toRefs, watch, getCurrentInstance } from "vue";

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
