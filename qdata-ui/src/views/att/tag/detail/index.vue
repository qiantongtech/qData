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
      :data="AttTagDetail"
      :header="{
        className: 'clearfixs',
        nameKey: 'name',
        statusKey: 'status',
        statusOptions: dp_model_status,
      }"
      :items="detailItems"
      :mode="'free'"
    />

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="资产信息" name="1">
          <asset :ids="AttTagDetail"></asset>
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="2">
          <info :AttTagDetail="AttTagDetail"></info>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="Tag">
import { useI18n } from 'vue-i18n'
import { getAttTag } from "@/api/att/tag/tag.js";
import { useRoute } from "vue-router";
import asset from "@/views/att/tag/detail/asset.vue";
import info from "@/views/att/tag/detail/info.vue";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");
const handleClick = (tab, event) => {
  console.log(AttTagDetail.value);
};

const showSearch = ref(true);
const route = useRoute();
const activeName = ref("0");
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1; // 如果 id 为空，使用默认值 1
    activeName.value = "0";
    getAttTagDetailById();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  AttTagDetail: {},
  form: {},
});

const { AttTagDetail, rules } = toRefs(data);
const detailItems = computed(() => [
  { label: "标签管理类目", key: "catName" },
  { label: "近义词", key: "nearSynonyms" },
  { label: "同义词", key: "synonyms" },
  {
    label: t('common.texts.description'),
    key: "description",
    span: 24,
    ellipsisClass: "ellipsis-2",
    className: "mt2 mb2",
  },
]);

/** 复杂详情页面上方表单查询 */
function getAttTagDetailById() {
  const _id = id;
  getAttTag(_id).then((response) => {
    activeName.value = "1";
    AttTagDetail.value = response.data;
  });
}

// getAttTagDetailById();
</script>
