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
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ daAssetDetail?.name }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ td('common.texts.number') }}</div>
              <div class="infotop-row-value">
                {{ daAssetDetail.id || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="desc.span || 8" v-for="desc in descList" :key="desc.label" style="margin-bottom: 2px">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ desc.label }}</div>
              <div class="infotop-row-value">
                <span v-if="desc.key == 'daAssetThemeRelList'">{{desc.value.length > 0 ? desc.value.map((ele) =>
                  ele.themeName).join(", ") : "-"}}</span>
                <span v-else-if="desc.key == 'status'"><dict-tag :options="da_assets_status"
                    :value="desc.value" /></span>
                <span class="li-type" v-else-if="desc.key == 'type'"
                  :style="{ color: desc.value == 2 ? '#c0d043' : desc.value == 1 ? '#21a3dd' : desc.value == 7 ? '#edce2e' : '' }">
                  <img v-if="desc.value == 2" src="@/assets/images/da/asset/icon-ds-api-one.svg" alt="" />
                  <img v-if="desc.value == 1" src="@/assets/images/da/asset/icon-ds-api-three.svg" alt="" />
                  <img v-if="desc.value == 7" src="@/assets/images/da/asset/icon-ds-api-five.svg" alt="" />
                  {{ desc.value == 2 ? "api" : desc.value == 1 ? td('da.assetDetail.detail.dbTable') : desc.value == 7 ? td('da.assetDetail.detail.file') : "-" }}
                </span>
                <span v-else>{{ desc.value || "-" }}</span>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-col :span="24">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">{{ td('common.texts.description') }}</div>
            <div class="infotop-row-value">
              <span class="ellipsis-2">
                {{ daAssetDetail.description || "-" }}
              </span>
            </div>
          </div>
        </el-col>
        <el-row :gutter="2" v-if="daAssetDetail.type == 1">
          <template v-if="daAssetDetail.type == 1">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.datasource') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail.datasourceName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top" style="margin: 2px 0;">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.tableDesc') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail.tableComment || "-" }}
                </div>
              </div>
            </el-col>
          </template>
          <el-col :span="8">
            <div class="infotop-row border-top" style="margin: 2px 0;">
              <div class="infotop-row-lable">{{ td('da.assetDetail.detail.englishName') }}</div>
              <div class="infotop-row-value">{{ daAssetDetail.tableName }}</div>
            </div>
          </el-col>
          <template v-if="daAssetDetail.type == 2">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.appName') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetApi?.appName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.developer') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetApi?.developerName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.serviceUrl') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetApi?.url || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.requestType') }}</div>
                <div class="infotop-row-value">
                  <dict-tag :options="da_asset_api_method" :value="daAssetDetail.daAssetApi.httpMethod" />
                </div>
              </div>
            </el-col>
          </template>
          <template v-if="daAssetDetail.type == 3">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.serviceType') }}</div>
                <div class="infotop-row-value">
                  <dict-tag :options="da_asset_gis_type" :value="daAssetDetail?.daAssetGis?.type" />
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.serviceUrl') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetGis?.url || "-" }}
                </div>
              </div>
            </el-col>
          </template>
          <template v-if="daAssetDetail.type == 4">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.fileType') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetGeo?.fileType || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.uploadFile') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail.daAssetGeo?.fileUrl || "-" }}
                </div>
              </div>
            </el-col>
          </template>
          <template v-if="daAssetDetail.type == 5">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.platform') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.platform || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.platformIp') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.ip || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.platformPort') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.port || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.cameraCode') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.cameraCode || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.cameraName') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.cameraName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.publicKey') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.appkey || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">{{ td('da.assetDetail.detail.privateKey') }}</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.appSecret || "-" }}
                </div>
              </div>
            </el-col>
          </template>
        </el-row>
      </div>
    </div>
    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick"
        v-if="!daAssetDetail.daAssetFiles || ['.xlsx', '.xls', '.csv'].includes(daAssetDetail.daAssetFiles.type)">
        <el-tab-pane v-for="pane in tabPanes" :key="pane.name" :label="pane.label" :name="pane.name">
          <component v-if="activeName === pane.name" :is="pane.component" :form1="daAssetDetail" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script setup name="DaAsset">
import useDefaultLang from "@/composables/useDefaultLang"
import { getDaAsset } from "@/api/da/asset/asset";
import { useRoute } from "vue-router";
import ComponentOne from "./table/column.vue";
import DataQualityControl from "./table/quality.vue";
import ComponentTwo from "./table/preview.vue";
import ComponentThree from "./api/simulation.vue";
import authParams from "./api/authParams";
import RequestParamsForm from "./api/requestParamsForm";
import ResponseFormatConfig from "./api/responseFormatConfig";
import lineage from "./table/lineage.vue";
import info from "./info.vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { da_assets_status, da_asset_gis_type, da_asset_api_method } = proxy.useDict("da_assets_status", "da_asset_gis_type", "da_asset_api_method");
const activeName = ref("0");
function handleClick(tab) {
  // Custom logic can be applied as needed
  console.log("Tab clicked:", tab);
}

const descList = ref([
  {
    key: "catName",
    label: td('da.assetDetail.detail.belongCat'),
    value: "",
  },
  {
    key: "type",
    label: td('da.assetDetail.detail.type'),
    value: "",
  },
  {
    key: "daAssetThemeRelList",
    label: td('da.assetDetail.detail.belongTopic'),
    value: "",
  },
  {
    key: "tag",
    label: td('da.assetDetail.detail.dataTag'),
    value: "",
  },
  {
    key: "status",
    label: td('common.texts.status'),
    value: "",
  },

]);

// Computed property to generate tab pane array
const tabPanes = computed(() => {
  console.log("🚀 ~ tabPanes ~ daAssetDetail.value.type:", daAssetDetail.value.type);
  switch (daAssetDetail.value.type) {
    case "1":
      return [
        { label: td('da.assetDetail.detail.assetFields'), name: "0", component: ComponentOne },
        { label: td('da.assetDetail.detail.assetPreview'), name: "2", component: ComponentTwo },
        { label: td('da.assetDetail.detail.assetQuality'), name: '3', component: DataQualityControl },
        // { label: 'Asset Lineage', name: '4', component: lineage },
        { label: td('da.assetDetail.detail.assetOverview'), name: "5", component: info },
      ];
    case "2":
      return [
        { label: td('da.assetDetail.detail.authParams'), name: "0", component: authParams },
        { label: td('da.assetDetail.detail.requestParams'), name: "1", component: RequestParamsForm },
        { label: td('da.assetDetail.detail.responseFormat'), name: "2", component: ResponseFormatConfig },
        { label: td('da.assetDetail.detail.previewData'), name: "3", component: ComponentThree },
        { label: td('da.assetDetail.detail.assetOverview'), name: "4", component: info },

      ];
    case "3":
      return [{ label: td('da.assetDetail.detail.assetOverview'), name: "0", component: info }];
    case "4":
      return [
        { label: td('da.assetDetail.detail.assetOverview'), name: "0", component: info },
        { label: td('da.assetDetail.detail.assetPreview'), name: "1", component: ComponentTwo },
      ];
    case "5":
      return [{ label: td('da.assetDetail.detail.assetOverview'), name: "0", component: info }];
    case "6":
      return [
        { label: td('da.assetDetail.detail.assetOverview'), name: "0", component: info },
        { label: td('da.assetDetail.detail.assetFields'), name: "1", component: ComponentOne },
        { label: td('da.assetDetail.detail.assetPreview'), name: "2", component: ComponentTwo },
      ];
    case "7":
      return [{ label: td('da.assetDetail.detail.assetOverview'), name: "0", component: info }];
    default:
  }
});
const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || null;
// Watch id changes
watch(
  () => route.query.id,
  (newId) => {
    if (route.path == '/da/asset/detail') {
      id = newId || null; // If ID is empty, use default value 1
      getDaAssetDetailById();
    }
  },
  { immediate: true } // `immediate` for true means that when the page is loaded, watch
);
const data = reactive({
  daAssetDetail: {},
  form: {},
});

const { daAssetDetail } = toRefs(data);

/** Complex detail page top form query */
function getDaAssetDetailById() {
  if (!id) {
    return;
  }
  const _id = id;
  getDaAsset(_id).then((response) => {
    daAssetDetail.value = response.data;
    descList.value.forEach((item) => {
      item.value = response.data[item.key];
    });
    if (response.data.type == "5") {
      daAssetDetail.value.daAssetVideo.config = JSON.parse(response.data.daAssetVideo.config);
    }
  });
}

onActivated(() => {
  activeName.value = "0";
  getDaAssetDetailById();
  // listDaAssetColumn();
});
onBeforeUnmount(() => {
  // Clear parameters or reset state
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
</style>
