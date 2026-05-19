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
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
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
    label: "描述",
    key: "description",
    span: 24,
    ellipsisClass: "ellipsis-2",
    className: "mt2 mb2",
  },
  { label: "创建人", key: "createBy" },
  { label: "创建时间", key: "createTime" },
  { label: "更新时间", key: "updateTime" },
  { label: "备注", key: "remark", span: 24 },
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
