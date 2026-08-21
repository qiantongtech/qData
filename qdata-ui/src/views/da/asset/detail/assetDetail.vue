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
import ComponentOne from "./table/column.vue";
import DataQualityControl from "./table/quality.vue";
import lineage from "./table/lineage.vue";
import ComponentTwo from "./table/preview.vue";
import ComponentThree from "./api/simulation.vue";
import authParams from "./api/authParams";
import RequestParamsForm from "./api/requestParamsForm";
import ResponseFormatConfig from "./api/responseFormatConfig";
import info from "./info.vue";
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
  // Logic can be customized as needed
  console.log("Tab clicked:", tab);
}

const detailItems = computed(() => {
  const data = daAssetDetail.value;
  const type = String(data.type);

  if (type === "1") {
    // database table
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
    // File
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

  // Default display
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

// Computed property generates tab pane array
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
          //       "View and edit all field information of the asset, including Chinese name, English name, type, length, etc.",
          // },
        },
        {
          label: td('dpp.asset.detail.index.tabPreview'),
          name: "2",
          component: ComponentTwo,
          // tip: { content: "View real-time data samples of this asset to help understand the data content" },
        },
        {
          label: td('dpp.asset.detail.index.tabQuality'),
          name: "3",
          component: DataQualityControl,
          // tip: {
          //   content: "View the data quality indicators of this asset, such as completeness, accuracy, consistency, etc.",
          // },
        },
        // {
        //   label: "Asset Lineage",
        //   name: "4",
        //   component: lineage,
        //   // tip: { content: "View the data source and destination of the asset, and understand its upstream and downstream dependencies" },
        // },
        {
          label: td('dpp.asset.detail.index.tabOverview'),
          name: "5",
          component: info,
          // tip: {
          //   content: "View the overall information of the asset, such as creation time, responsible person, number of visits, etc.",
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
// Monitor id changes
watch(
    () => route.query.id,
    (newId) => {
      id = newId || null; // If id is empty, the default value 1 is used
      getDaAssetDetailById();
    },
    { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
const data = reactive({
  daAssetDetail: {},
  form: {},
});

const { daAssetDetail } = toRefs(data);

/** Form query at the top of the complex details page */
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
  // Clear parameters or reset status
  data.daAssetDetail = {};
  data.form = {};
  activeName.value = "0"; // Reset tab page
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
