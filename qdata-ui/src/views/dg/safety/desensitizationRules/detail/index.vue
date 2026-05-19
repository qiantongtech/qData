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
      :data="ruleDetail"
      :items="detailItems"
      :header="{
        className: 'clearfixs',
        nameKey: 'name',
        statusKey: 'validFlag',
        statusOptions: [
          { label: '启用', value: 'true', color: '#13ce66' },
          { label: '禁用', value: 'false', color: '#ff4949' },
        ],
      }"
    />

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName">
        <el-tab-pane label="敏感清单" name="1">
          <asset :ruleDetail="ruleDetail"></asset>
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="2">
          <info :ruleDetail="ruleDetail"></info>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="DesensitizationRuleDetail">
import { getDesensitizeRules } from "@/api/dg/safety/desensitizeRules";
import { useRoute } from "vue-router";
import {
  getCurrentInstance,
  ref,
  reactive,
  toRefs,
  watch,
  computed,
  unref,
} from "vue";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";
import asset from "@/views/dg/safety/desensitizationRules/detail/asset.vue";
import info from "@/views/dg/safety/desensitizationRules/detail/info.vue";

const { proxy } = getCurrentInstance();
const { dp_model_status, dg_application_scene, dg_mask_type } = proxy.useDict(
  "dp_model_status",
  "dg_application_scene",
  "dg_mask_type"
);

const route = useRoute();

const showSearch = ref(true);
const activeName = ref("0");

const headerConfig = computed(() => ({
  className: "clearfixs",
  nameKey: "name",
  statusKey: "validFlag",
  statusOptions: unref(dp_model_status),
}));

let id = route.query.id || 1;
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;
    activeName.value = "0";
    getDetail();
  },
  { immediate: true }
);

const data = reactive({ ruleDetail: {} });
const { ruleDetail } = toRefs(data);

const dataCategoryList = ref([]);

async function getDataCategoryList() {
  try {
    const res = await selectTreeDataCategory();
    const rawData = res?.data || [];
    const processTree = (nodes) => {
      return nodes.map((node) => {
        const newNode = { ...node };
        if (node.children && node.children.length > 0) {
          newNode.children = processTree(node.children);
        }
        return newNode;
      });
    };
    dataCategoryList.value = processTree(rawData);
  } catch {
    dataCategoryList.value = [];
  }
}

getDataCategoryList();

function formatDataCategory(v) {
  if (v == null || v === "") return "-";
  const vv = String(v);
  const findInTree = (nodes) => {
    for (const node of nodes) {
      if (String(node.id) === vv) return node.name;
      if (node.children && node.children.length > 0) {
        const found = findInTree(node.children);
        if (found) return found;
      }
    }
    return null;
  };
  return findInTree(dataCategoryList.value) ?? vv;
}

const detailItems = computed(() => [
  {
    label: "数据分类",
    key: "dataCategoryName",
  },
  {
    label: "应用场景",
    key: "applicationScene",
    dictOptions: unref(dg_application_scene),
  },
  { label: "脱敏方式", key: "maskType", dictOptions: unref(dg_mask_type) },
  {
    label: "描述",
    key: "description",
    span: 24,
    ellipsisClass: "ellipsis-2",
    className: "mt2 mb2",
  },
]);

function getDetail() {
  getDesensitizeRules(id).then((res) => {
    activeName.value = "1";
    ruleDetail.value = res.data || {};
  });
}
</script>

