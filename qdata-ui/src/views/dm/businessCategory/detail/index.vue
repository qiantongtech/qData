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
      :data="businessLayerDetail"
      :header="{
        className: 'clearfixs',
        nameKey: 'name',
        statusKey: 'validFlag',
        statusOptions: [
          { label: td('dm.businessCategory.enableText', 'Enable'), value: 'true', color: '#13ce66' },
          { label: td('dm.businessCategory.disableText', 'Disable'), value: 'false', color: '#ff4949' },
        ],
      }"
      :items="detailItems"
    />

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName">
        <el-tab-pane :label="td('dm.businessCategory.relatedDataDomain', 'Related Data Domain')" name="1">
          <asset :businessLayerDetail="businessLayerDetail"></asset>
        </el-tab-pane>
        <el-tab-pane :label="td('dm.businessCategory.detailInfo', 'Detail Information')" name="2">
          <info :businessLayerDetail="businessLayerDetail"></info>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="BusinessLayerDetail">
import useDefaultLang from "@/composables/useDefaultLang"
import { getBusinessCategory } from "@/api/dm/businessCategory/businessCategory";
import { useRoute } from "vue-router";
import asset from "./asset.vue";
import info from "./info.vue";
import {
  ref,
  reactive,
  toRefs,
  watch,
  computed,
  getCurrentInstance,
} from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const route = useRoute();

const showSearch = ref(true);
const activeName = ref("1");

const data = reactive({
  businessLayerDetail: {},
});
const { businessLayerDetail } = toRefs(data);

const detailItems = computed(() => [
  { label: td('dm.businessCategory.upperCategory', 'Parent Business Category'), key: "parentName" },
  { label: td('dm.businessCategory.englishAbbr', 'English Abbreviation'), key: "engName" },
  { label: td('dm.businessCategory.ownerId', 'Responsible Person'), key: "ownerName" },
  {
    label: td('common.texts.description', 'Description'),
    key: "description",
    span: 24,
    ellipsisClass: "ellipsis-2",
    className: "mt2 mb2",
  },
  { label: td('common.texts.createdBy', 'Created By'), key: "createBy" },
  { label: td('common.texts.createdTime', 'Created Time'), key: "createTime" },
  { label: td('common.texts.updatedTime', 'Updated Time'), key: "updateTime" },
  { label: td('common.texts.remark', 'Remark'), key: "remark", span: 24 },
]);

function getDetail(id) {
  getBusinessCategory(id).then((res) => {
    businessLayerDetail.value = res.data || {};
  });
}

watch(
  () => route.query.id,
  (newId) => {
    if (newId) {
      getDetail(newId);
    }
  },
  { immediate: true }
);
</script>
