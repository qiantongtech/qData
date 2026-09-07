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
  <div class="app-container" ref="app-container" v-if="dsApiDetail">
    <DetailInfo
      :show="showSearch"
      :data="dsApiDetail"
      :header="{
        nameKey: 'name',
        statusKey: 'status',
        statusOptions: sys_disable,
      }"
      :items="detailItems"
      mode="free"
    />

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane :label="td('ds.api.apiDetail.parameterInfo')" name="1">
          <component-one :form2="form2" v-if="activeName === '1'"></component-one>
        </el-tab-pane>
        <el-tab-pane :label="td('ds.api.apiDetail.testInfo')" name="2">
          <component-two :form1="form1" v-if="activeName === '2'"></component-two>
        </el-tab-pane>
        <!--        <el-tab-pane label="Authorization information" name="2">-->
        <!--          <component-two ></component-two>-->
        <!--        </el-tab-pane>-->
      </el-tabs>
    </div>

  </div>
</template>

<script setup name="DsApi">
import { getDsApi } from "@/api/ds/api/api.js";
import { onBeforeRouteLeave, useRoute } from 'vue-router';
import { ref, reactive, watch, toRefs, computed, getCurrentInstance } from 'vue';
import ComponentOne from "@/views/ds/api/detail/parameter.vue";
import ComponentTwo from "@/views/ds/api/detail/simulation.vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { ds_api_log_status, ds_api_bas_info_api_service_type,
    ds_api_bas_info_api_method_type, ds_api_bas_info_res_data_type,sys_disable }
    = proxy.useDict('ds_api_log_status', 'ds_api_bas_info_api_service_type',
    'ds_api_bas_info_api_method_type', 'ds_api_bas_info_res_data_type','sys_disable');

const activeName = ref('1')

const handleClick = (tab, event) => {
  console.log(tab, event)
}

const showSearch = ref(true);

const detailItems = computed(() => [
  { label: td('ds.api.apiDetail.belongingCategory'), key: 'catName' },
  { label: td('ds.api.apiDetail.apiVersion'), key: 'apiVersion' },
   {
    label: td('ds.api.apiDetail.apiRequestAddress'),
    formatter: (val, data) => `/services/${data.apiVersion}${data.apiUrl}`
  },
  { label: td('common.texts.description'), key: 'description', span: 24, ellipsisClass: 'ellipsis-2' , className: "mt2 mb2",},
  { label: td('ds.api.apiDetail.requestMethod'), key: 'reqMethod', dictOptions: ds_api_bas_info_api_method_type.value },
  { label: td('ds.api.apiDetail.returnResultType'), key: 'resDataType', dictOptions: ds_api_bas_info_res_data_type.value },
  { label: td('common.texts.createdBy'), key: 'createBy' },
  { label: td('common.texts.createdTime'), key: 'createTime', type: 'time' ,className: "mt2 mb2",},
 
]);

const route = useRoute();
let id = route.query.id || 1;
// Monitor id changes
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;  // If id is empty, the default value 1 is used
    getDsApiDetailById();

  },
  { immediate: true }  // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
const data = reactive({
  dsApiDetail: {
  },
  form: {},
  form1: {

  },
  form2: {},
});

const { dsApiDetail, rules, form1, form2 } = toRefs(data);
function safeParse(str, defaultVal) {
  if (!str) return defaultVal;
  try {
    return JSON.parse(str);
  } catch (e) {
    console.warn("Failed to parse JSON; returning the original value:", str, e);
    return str;  // Return original string instead of defaultVal
  }
}
/** Form query at the top of the complex details page */
function getDsApiDetailById() {
  const _ID = id;
  getDsApi(_ID).then(response => {
    if (response.data.apiServiceType == 3) {
      dsApiDetail.value = response.data;
      form2.value = response.data;
      form2.value.resParams = safeParse(response.data.resParams, []);
      form2.value.reqParams = safeParse(response.data.reqParams, []);
      form2.value.headerJson = safeParse(response.data.headerJson, []);
      form1.value = response.data;
      form1.value.apiId = response.data?.apiId;
      form1.value.transmitType = response.data?.transmitType;
      form1.value.executeConfig = safeParse(response.data.configJson, {});
      form1.value.reqParams = safeParse(response.data.reqParams, []);
      form1.value.resParams = safeParse(response.data.resParams, []);
      form1.value.headerJson = safeParse(response.data.headerJson, []);
    } else {
      dsApiDetail.value = response.data;
      form2.value.resParams = JSON.parse(response.data.resParams)
      form2.value.reqParams = JSON.parse(response.data.reqParams)
      console.log('123123', dsApiDetail.value)
      form1.value = response.data;
      form1.value.rateLimit = { enable: "1", times: 5, seconds: 60 }
      form1.value.executeConfig = JSON.parse(response.data.configJson);
      form1.value.reqParams = JSON.parse(response.data.reqParams);
      form1.value.resParams = JSON.parse(response.data.resParams);
    }

  });

}

getDsApiDetailById();

onBeforeRouteLeave((to, from) => {
  // Monitor route changes and destroy the current page if the route changes.
  if (to.path !== from.path) {
    console.log("Route changed; destroying the current page")
    form2.value = {}
    form1.value = {}
  }
});

</script>