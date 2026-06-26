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
          {{ dpDocumentDetail.name }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.number') }}</div>
              <div class="infotop-row-value">{{ dpDocumentDetail.id }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.document.standardCode') }}</div>
              <div class="infotop-row-value">
                {{ dpDocumentDetail.code || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.document.standardCategory') }}</div>
              <div class="infotop-row-value">
                {{ dpDocumentDetail.catName || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.description') }}</div>
              <div class="infotop-row-value">
                {{ dpDocumentDetail.description || "-" }}
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.document.issuingAgency') }}</div>
              <div class="infotop-row-value">
                {{ dpDocumentDetail.issuingAgency || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.document.version') }}</div>
              <div class="infotop-row-value">
                {{ dpDocumentDetail.version || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('dp.document.standardStatus') }}</div>
              <dict-tag :options="dp_document_status" :value="dpDocumentDetail.status" />
            </div>
          </el-col>
          <!-- <el-col :span="24">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.remark') }}</div>
              <div class="infotop-row-value">
                {{ dpDocumentDetail.remark || "-" }}
              </div>
            </div>
          </el-col> -->
          <!-- <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">标准名称</div>
              <div class="infotop-row-value">
                {{ dpDocumentDetail.name || '-' }}
              </div>
            </div>
          </el-col> -->
        </el-row>

      </div>
    </div>
    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane v-for="pane in tabPanes" :key="pane.name" :label="pane.label" :name="pane.name">
          <component v-if="activeName === pane.name" :is="pane.component" :form1="dpDocumentDetail"
            :activeName="activeName" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="DpDocument">
import useDefaultLang from "@/composables/useDefaultLang"
import { getDpDocument } from "@/api/dp/document/document";
import { useRoute } from 'vue-router';
import BasicInfo from "./info.vue";
import model from "./model.vue";
import dataElem from "./dataElem.vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_document_status } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_document_status"
);
const activeName = ref('0')
let tabPanes = ref([
  { label: td('dp.document.logicalModel'), name: "0", component: model },
  {
    label: td('dp.document.dataElement'), name: "1", component: dataElem
  },
  { label: td('dp.document.codeTable'), name: '2', component: dataElem },
  { label: td('dp.document.detailInfo'), name: "4", component: BasicInfo },

])
const handleClick = (tab, event) => {
  console.log(tab, event)
}

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;
    getDpDocumentDetailById();

  },
  { immediate: true }
);
const data = reactive({
  dpDocumentDetail: {
  },
  form: {},
});

const { dpDocumentDetail, rules } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDpDocumentDetailById() {
  const _ID = id;
  getDpDocument(_ID).then(response => {
    dpDocumentDetail.value = response.data;
  });
}

getDpDocumentDetailById();

</script>
<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;

  .pagecont-bottom {
    min-height: calc(100vh - 338px) !important;
  }
}
</style>
