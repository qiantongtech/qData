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
        <el-tab-pane :label="td('att.common.assetInfo')" name="1" lazy>
          <asset v-if="activeName === '1'" :ids="AttTagDetail"></asset>
        </el-tab-pane>
        <el-tab-pane :label="td('att.common.detailInfo')" name="2" lazy>
          <info v-if="activeName === '2'" :AttTagDetail="AttTagDetail"></info>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="Tag">
import useDefaultLang from "@/composables/useDefaultLang";
import { getAttTag } from "@/api/att/tag/tag.js";
import { useRoute } from "vue-router";
import asset from "@/views/att/tag/detail/asset.vue";
import info from "@/views/att/tag/detail/info.vue";

const { td } = useDefaultLang();
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
  { label: td('att.tag.detail.catName'), key: "catName" },
  { label: td('att.tag.detail.nearSynonyms'), key: "nearSynonyms" },
  { label: td('att.tag.detail.synonyms'), key: "synonyms" },
  {
    label: td('common.texts.description'),
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
