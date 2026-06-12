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
          { label: td('dm.businessCategory.enableText', '启用'), value: 'true', color: '#13ce66' },
          { label: td('dm.businessCategory.disableText', '禁用'), value: 'false', color: '#ff4949' },
        ],
      }"
      :items="detailItems"
    />

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName">
        <el-tab-pane :label="td('dm.businessCategory.relatedDataDomain', '关联数据域')" name="1">
          <asset :businessLayerDetail="businessLayerDetail"></asset>
        </el-tab-pane>
        <el-tab-pane :label="td('dm.businessCategory.detailInfo', '详细信息')" name="2">
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
  { label: td('dm.businessCategory.upperCategory', '上级业务分类'), key: "parentName" },
  { label: td('dm.businessCategory.englishAbbr', '英文简写'), key: "engName" },
  { label: td('dm.businessCategory.ownerId', '负责人'), key: "ownerName" },
  {
    label: td('common.texts.description', '描述'),
    key: "description",
    span: 24,
    ellipsisClass: "ellipsis-2",
    className: "mt2 mb2",
  },
  { label: td('common.texts.createdBy', '创建人'), key: "createBy" },
  { label: td('common.texts.createdTime', '创建时间'), key: "createTime" },
  { label: td('common.texts.updatedTime', '更新时间'), key: "updateTime" },
  { label: td('common.texts.remark', '备注'), key: "remark", span: 24 },
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
