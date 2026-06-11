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
  <div class="app-container" ref="app-container">
    <DetailInfo
      :show="showSearch"
      :data="businessLayerDetail"
      :header="{
        className: 'clearfixs',
        nameKey: 'name',
        statusKey: 'validFlag',
        statusOptions: [
          { label: '启用', value: 'true', color: '#13ce66' },
          { label: '禁用', value: 'false', color: '#ff4949' },
        ],
      }"
      :items="detailItems"
    />

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName">
        <el-tab-pane label="关联数据域" name="1">
          <asset :businessLayerDetail="businessLayerDetail"></asset>
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="2">
          <info :businessLayerDetail="businessLayerDetail"></info>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="BusinessLayerDetail">
import { useI18n } from 'vue-i18n'
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

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const route = useRoute();

const showSearch = ref(true);
const activeName = ref("1");

const data = reactive({
  businessLayerDetail: {},
});
const { businessLayerDetail } = toRefs(data);

const detailItems = computed(() => [
  { label: "上级业务分类", key: "parentName" },
  { label: "英文简写", key: "engName" },
  { label: "负责人", key: "ownerName" },
  {
    label: t('common.texts.description'),
    key: "description",
    span: 24,
    ellipsisClass: "ellipsis-2",
    className: "mt2 mb2",
  },
  { label: t('common.texts.createdBy'), key: "createBy" },
  { label: t('common.texts.createdTime'), key: "createTime" },
  { label: t('common.texts.updatedTime'), key: "updateTime" },
  { label: t('common.texts.remark'), key: "remark", span: 24 },
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
