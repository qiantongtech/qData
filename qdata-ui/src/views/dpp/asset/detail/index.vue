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
import useDefaultLang from "@/composables/useDefaultLang"
import { getDaAsset } from "@/api/da/asset/asset";

const { td } = useDefaultLang();
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
      { label: td('dpp.asset.detail.index.tableName'), key: "tableName", ellipsisClass: "ellipsis" },
      {
        label: td('dpp.asset.detail.index.assetType'),
        key: "type",
        dictOptions: da_asset_type.value,
        ellipsisClass: "ellipsis",
      },
      {
        label: td('dpp.asset.detail.index.hierarchy'),
        formatter: (val, row) => formatHierarchyDisplayName(row, row.tableType),
        ellipsisClass: "ellipsis",
      },
    ];
  } else if (type === "2") {
    // API
    return [
      { label: td('dpp.asset.detail.index.appName'), key: "appName", ellipsisClass: "ellipsis" },
      { label: td('dpp.asset.detail.index.dataDomain'), key: "dataDomainName", ellipsisClass: "ellipsis" },
      {
        label: td('dpp.asset.detail.index.themeDomain'),
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
      { label: td('dpp.asset.detail.index.fileName'), key: "name", ellipsisClass: "ellipsis" },
      { label: td('dpp.asset.detail.index.dataDomain'), key: "dataDomainName", ellipsisClass: "ellipsis" },
      {
        label: td('dpp.asset.detail.index.themeDomain'),
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
    { label: td('dpp.asset.detail.index.category'), key: "catName", ellipsisClass: "ellipsis" },
    {
      label: td('dpp.asset.detail.index.assetType'),
      key: "type",
      dictOptions: da_asset_type.value,
      ellipsisClass: "ellipsis",
    },
    {
      label: td('dpp.asset.detail.index.themeDomain'),
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
          label: td('dpp.asset.detail.index.tabColumns'),
          name: "0",
          component: ComponentOne,
          // tip: {
          //   content:
          //       "查看和编辑该资产的所有字段信息，包括中文名、英文名、类型、长度等",
          // },
        },
        {
          label: td('dpp.asset.detail.index.tabPreview'),
          name: "2",
          component: ComponentTwo,
          // tip: { content: "查看该资产的实时数据样例，帮助理解数据内容" },
        },
        {
          label: td('dpp.asset.detail.index.tabQuality'),
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
          label: td('dpp.asset.detail.index.tabOverview'),
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
          label: td('dpp.asset.detail.index.tabAuthParams'),
          name: "0",
          component: authParams,
          tip: {
            content:
                td('dpp.asset.detail.index.tabAuthParamsTip'),
          },
        },
        {
          label: td('dpp.asset.detail.index.tabRequestParams'),
          name: "1",
          component: RequestParamsForm,
          tip: {
            content: td('dpp.asset.detail.index.tabRequestParamsTip'),
          },
        },
        {
          label: td('dpp.asset.detail.index.tabResponseFormat'),
          name: "2",
          component: ResponseFormatConfig,
          tip: {
            content:
                td('dpp.asset.detail.index.tabResponseFormatTip'),
          },
        },
        {
          label: td('dpp.asset.detail.index.tabPreviewData'),
          name: "3",
          component: ComponentThree,
          tip: { content: td('dpp.asset.detail.index.tabPreviewDataTip') },
        },
        {
          label: td('dpp.asset.detail.index.tabOverview'),
          name: "4",
          component: info,
          tip: {
            content: td('dpp.asset.detail.index.tabOverviewTip'),
          },
        },
      ];
    case "3":
      return [{ label: td('dpp.asset.detail.index.tabOverview'), name: "0", component: info }];
    case "4":
      return [
        { label: td('dpp.asset.detail.index.tabOverview'), name: "0", component: info },
        { label: td('dpp.asset.detail.index.tabPreview'), name: "1", component: ComponentTwo },
      ];
    case "5":
      return [{ label: td('dpp.asset.detail.index.tabOverview'), name: "0", component: info }];
    case "6":
      return [
        { label: td('dpp.asset.detail.index.tabOverview'), name: "0", component: info },
        { label: td('dpp.asset.detail.index.tabColumns'), name: "1", component: ComponentOne },
        { label: td('dpp.asset.detail.index.tabPreview'), name: "2", component: ComponentTwo },
      ];
    case "7":
      return [{ label: td('dpp.asset.detail.index.tabOverview'), name: "0", component: info }];
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
