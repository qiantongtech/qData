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
  <div class="app-container">
    <div class="pagecont-top" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ form.name || "-" }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.number') }}</div>
              <div class="infotop-row-value">
                {{ form.id || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.dataElem.nameEn') }}</div>
              <div class="infotop-row-value">
                {{ form.engName || "-" }}
              </div>
            </div>
          </el-col>
          <!-- <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">中文名称</div>
              <div class="infotop-row-value">
                {{ form.name || "-" }}
              </div>
            </div>
          </el-col> -->
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.dataElem.type') }}</div>
              <div class="infotop-row-value">
                <dict-tag :options="dp_data_elem_code_type" :value="form.type" />
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.description') }}</div>
              <div class="infotop-row-value">
                {{ form.description || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.dataElem.catCode') }}</div>
              <div class="infotop-row-value">
                {{ form.catName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.dataElem.columnType') }}</div>
              <div class="infotop-row-value">
                <dict-tag :options="column_type" :value="form.columnType" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.status') }}</div>
              <div class="infotop-row-value">
                <dict-tag :options="sys_disable" :value="form.status" />
              </div>
            </div>
          </el-col>

        </el-row>
      </div>
    </div>
    <!-- 标签页部分 -->
    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane :label="td('dp.dataElem.detail.cleanRule')" name="1" lazy>
          <cleanRule :dataElemId="dataElemId" dataType="2" />
        </el-tab-pane>
        <el-tab-pane :label="td('dp.dataElem.detail.auditRule')" name="2" lazy>
          <auditRule :dataElemId="dataElemId" dataType="1" />
        </el-tab-pane>
        <el-tab-pane :label="td('dp.dataElem.detail.relationInfo')" name="3" lazy>
          <asset />
        </el-tab-pane>
        <el-tab-pane :label="td('dp.dataElem.detail.detailInfo')" name="5" lazy>
          <info :daDiscoveryTaskDetail="form" />

        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="dataElemDetailDialog">
import useDefaultLang from "@/composables/useDefaultLang"
import { onMounted } from "vue";

const { proxy } = getCurrentInstance();

import { getDpDataElem } from "@/api/dp/dataElem/dataElem";

import cleanRule from "@/views/dp/dataElem/detail/column/cleanRule";
import auditRule from "@/views/dp/dataElem/detail/column/auditRule";
import asset from "@/views/dp/dataElem/detail/components/asset.vue";
import info from "@/views/dp/dataElem/detail/column/info.vue";
import { useRoute } from "vue-router";

const { td } = useDefaultLang();
const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_data_elem_code_type"
);

const dpDataElemRuleRelList = ref([]);

const data = reactive({
  form: {},
  activeName: "1",
});
const { form, activeName } = toRefs(data);
const dataElemId = ref("");
const route = useRoute();
dataElemId.value = route.query.id;

/** 详情按钮操作 */
function getDetail() {
  const id = dataElemId.value;
  if (!id) return;
  getDpDataElem(id).then((response) => {
    form.value = response.data;
  });
}

// 页面加载时获取数据
onMounted(() => {
  getDetail();
});

// 返回列表页
function goBack() {
  router.go(-1);
}
</script>

<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;

  .pagecont-bottom {
    min-height: calc(100vh - 345px) !important;
  }
}
</style>
