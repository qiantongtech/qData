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
              <div class="infotop-row-lable">Chinese name</div>
              <div class="infotop-row-value">
                {{ form.name || "-" }}
              </div>
            </div>
          </el-col> -->
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
    <!-- Tab section -->
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

/** Detail button operation */
function getDetail() {
  const id = dataElemId.value;
  if (!id) return;
  getDpDataElem(id).then((response) => {
    form.value = response.data;
  });
}

// Get data when page loads
onMounted(() => {
  getDetail();
});

// Return to list page
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
