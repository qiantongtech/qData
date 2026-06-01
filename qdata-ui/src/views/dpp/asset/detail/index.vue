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
        :data="daAssetDetail"
        :header="{
        className: 'clearfixs',
        idKey: 'id',
        nameKey: 'name',
        statusKey: 'status',
        statusOptions: da_assets_status,
      }"
        :items="detailItems"
    >
    </DetailInfo>
    <div class="pagecont-bottom">
      <el-tabs
          v-model="activeName"
          class="demo-tabs"
          @tab-click="handleClick"
          v-if="
          !daAssetDetail.daAssetFiles ||
          ['.xlsx', '.xls', '.csv'].includes(daAssetDetail.daAssetFiles.type)
        "
      >
        <qt-tab-pane
            v-for="pane in tabPanes"
            :key="pane.name"
            :label="pane.label"
            :name="pane.name"
            :tip="pane.tip"
        >
          <component
              v-if="activeName === pane.name"
              :is="pane.component"
              :form1="daAssetDetail"
          />
        </qt-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script setup name="DaAsset">
import { getDaAsset } from "@/api/da/asset/asset";
import { useRoute } from "vue-router";
import DetailInfo from "@/components/DetailInfo/index.vue";
import ComponentOne from "@/views/dpp/asset/detail/table/column.vue";
import DataQualityControl from "@/views/dpp/asset/detail/table/quality.vue";
import lineage from "@/views/dpp/asset/detail/table/lineage.vue";
import ComponentTwo from "@/views/dpp/asset/detail/table/preview.vue";
import ComponentThree from "@/views/dpp/asset/detail/api/simulation.vue";
import authParams from "@/views/dpp/asset/detail/api/authParams";
import RequestParamsForm from "@/views/dpp/asset/detail/api/requestParamsForm";
import ResponseFormatConfig from "@/views/dpp/asset/detail/api/responseFormatConfig";
import info from "@/views/dpp/asset/detail/info.vue";
import { formatHierarchyDisplayName } from "@/utils/dm/utils";

const { proxy } = getCurrentInstance();
const {
  da_assets_status,
  da_asset_gis_type,
  da_asset_api_method,
  da_asset_type,
  table_type,
} = proxy.useDict(
    "da_assets_status",
    "da_asset_gis_type",
    "da_asset_api_method",
    "da_asset_type",
    "table_type"
);
const activeName = ref("0");
function handleClick(tab) {
  // 可根据需要自定义逻辑
  console.log("Tab clicked:", tab);
}

const detailItems = computed(() => {
  const data = daAssetDetail.value;
  const type = String(data.type);

  if (type === "1") {
    // 数据库表
    return [
      { label: "表名称", key: "tableName", ellipsisClass: "ellipsis" },
      {
        label: "资产类型",
        key: "type",
        dictOptions: da_asset_type.value,
        ellipsisClass: "ellipsis",
      },
      {
        label: "归属层级",
        formatter: (val, row) => formatHierarchyDisplayName(row, row.tableType),
        ellipsisClass: "ellipsis",
      },
    ];
  } else if (type === "2") {
    // API
    return [
      { label: "应用名称", key: "appName", ellipsisClass: "ellipsis" },
      { label: "数据分域", key: "dataDomainName", ellipsisClass: "ellipsis" },
      {
        label: "所属主题",
        formatter: (_, data) =>
            Array.isArray(data?.daAssetThemeRelList) &&
            data.daAssetThemeRelList.length
                ? data.daAssetThemeRelList.map((ele) => ele.themeName).join(", ")
                : "-",
        ellipsisClass: "ellipsis",
      },
    ];
  } else if (type === "7" || type === "4") {
    // 文件
    return [
      { label: "文件名称", key: "name", ellipsisClass: "ellipsis" },
      { label: "数据分域", key: "dataDomainName", ellipsisClass: "ellipsis" },
      {
        label: "所属主题",
        formatter: (_, data) =>
            Array.isArray(data?.daAssetThemeRelList) &&
            data.daAssetThemeRelList.length
                ? data.daAssetThemeRelList.map((ele) => ele.themeName).join(", ")
                : "-",
        ellipsisClass: "ellipsis",
      },
    ];
  }

  // 默认展示
  return [
    { label: "所属类目", key: "catName", ellipsisClass: "ellipsis" },
    {
      label: "资产类型",
      key: "type",
      dictOptions: da_asset_type.value,
      ellipsisClass: "ellipsis",
    },
    {
      label: "所属主题",
      formatter: (_, data) =>
          Array.isArray(data?.daAssetThemeRelList) &&
          data.daAssetThemeRelList.length
              ? data.daAssetThemeRelList.map((ele) => ele.themeName).join(", ")
              : "-",
      ellipsisClass: "ellipsis",
    },
  ];
});

// 计算属性生成 tab pane 数组
const tabPanes = computed(() => {
  console.log(
      "🚀 ~ tabPanes ~ daAssetDetail.value.type:",
      daAssetDetail.value.type
  );
  switch (daAssetDetail.value.type) {
    case "1":
      return [
        {
          label: "资产字段",
          name: "0",
          component: ComponentOne,
          // tip: {
          //   content:
          //       "查看和编辑该资产的所有字段信息，包括中文名、英文名、类型、长度等",
          // },
        },
        {
          label: "资产预览",
          name: "2",
          component: ComponentTwo,
          // tip: { content: "查看该资产的实时数据样例，帮助理解数据内容" },
        },
        {
          label: "资产质量",
          name: "3",
          component: DataQualityControl,
          // tip: {
          //   content: "查看该资产的数据质量指标，如完整性、准确性、一致性等",
          // },
        },
        // {
        //   label: "资产血缘",
        //   name: "4",
        //   component: lineage,
        //   // tip: { content: "查看该资产的数据来源与去向，了解其上下游依赖关系" },
        // },
        {
          label: "资产概览",
          name: "5",
          component: info,
          // tip: {
          //   content: "查看该资产的整体信息，如创建时间 、责任人、访问次数等",
          // },
        },
      ];
    case "2":
      return [
        {
          label: "鉴权参数",
          name: "0",
          component: authParams,
          tip: {
            content:
                "查看调用该接口所需的认证信息，如 Token、AppKey、签名算法等",
          },
        },
        {
          label: "请求参数",
          name: "1",
          component: RequestParamsForm,
          tip: {
            content: "查看调用该接口时需要传入的参数，如 key、location 等",
          },
        },
        {
          label: "返回格式",
          name: "2",
          component: ResponseFormatConfig,
          tip: {
            content:
                "查看接口返回的数据结构，如 JSON、XML 格式，包含字段名、类型、示例等",
          },
        },
        {
          label: "预览数据",
          name: "3",
          component: ComponentThree,
          tip: { content: "查看该接口的实际返回数据样例，帮助理解数据内容" },
        },
        {
          label: "资产概览",
          name: "4",
          component: info,
          tip: {
            content: "查看该资产的整体信息，如创建时间、责任人、访问次数等",
          },
        },
      ];
    case "3":
      return [{ label: "资产概览", name: "0", component: info }];
    case "4":
      return [
        { label: "资产概览", name: "0", component: info },
        { label: "资产预览", name: "1", component: ComponentTwo },
      ];
    case "5":
      return [{ label: "资产概览", name: "0", component: info }];
    case "6":
      return [
        { label: "资产概览", name: "0", component: info },
        { label: "资产字段", name: "1", component: ComponentOne },
        { label: "资产预览", name: "2", component: ComponentTwo },
      ];
    case "7":
      return [{ label: "资产概览", name: "0", component: info }];
    default:
  }
});
const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || null;
// 监听 id 变化
watch(
    () => route.query.id,
    (newId) => {
      id = newId || null; // 如果 id 为空，使用默认值 1
      getDaAssetDetailById();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  daAssetDetail: {},
  form: {},
});

const { daAssetDetail } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDaAssetDetailById() {
  if (!id) {
    return;
  }
  const _id = id;
  getDaAsset(_id).then((response) => {
    daAssetDetail.value = response.data;
    if (response.data.type == "5") {
      daAssetDetail.value.daAssetVideo.config = JSON.parse(
          response.data.daAssetVideo.config
      );
    }
  });
}

onActivated(() => {
  activeName.value = "0";
  getDaAssetDetailById();
  // listDaAssetColumn();
});
onBeforeUnmount(() => {
  // 清空参数或重置状态
  data.daAssetDetail = {};
  data.form = {};
  activeName.value = "0"; // 重置tab页
});
// listDaAssetColumn();
</script>
<style lang="scss" scoped>
.li-type {
  display: flex;
  align-items: center;

  img {
    width: 18px;
    margin: 0 5px;
  }
}
.pagecont-bottom {
  min-height: calc(100vh - 340px) !important;
}
</style>
