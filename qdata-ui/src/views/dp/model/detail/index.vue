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
      :data="dpModelDetail"
      :header="{
        className: 'clearfixs',
        nameKey: 'modelComment',
        statusKey: 'status',
        statusOptions: dp_model_status,
      }"
      :items="detailItems"
    />
    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <qt-tab-pane
          :label="td('dp.modelForm.attributeFields')"
          name="1"
        >
          <modelColumn :is-detail="isMaterializedDetail" />
        </qt-tab-pane>
        <el-tab-pane :label="td('dp.document.releaseLog')" name="2" v-if="isMaterializedDetail">
          <modelMaterialized
            :modelId="route.query.id"
            :row="dpModelDetail"
          ></modelMaterialized>
        </el-tab-pane>
        <el-tab-pane :label="td('dp.document.detailInfo')" name="3">
          <info :daDiscoveryTaskDetail="dpModelDetail"></info>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="DpModel">
import useDefaultLang from "@/composables/useDefaultLang";
import { getDpModel } from "@/api/dp/model/model";
import { useRoute, useRouter } from "vue-router";
import { deptUserTree } from "@/api/system/system/user.js";
import modelColumn from "@/views/dp/model/detail/modelColumn.vue";
import modelMaterialized from "@/views/dp/model/detail/materializationLog.vue";
import info from "@/views/dp/model/detail/info.vue";
import {
  formatHierarchyDisplayName,
  formatModelName,
} from "../../../../utils/dm/utils.js";
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dp_model_status, dp_model_create_type, table_type } = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type",
  "table_type"
);
const router = useRouter();
const activeName = ref("1");
const getNickNameById = (userId) => {
  if (!userList.value || !Array.isArray(userList.value)) {
    return null;
  }

  if (!userId) return null;

  const user = userList.value.find((user) => user.userId == userId);
  return user ? user.nickName : null;
};

const handleClick = (tab, event) => {
  console.log(tab, event);
};

const showSearch = ref(true);
const route = useRoute();
const isMaterializedDetail = computed(
  () => route.path === "/dm/model/materializedModel/detail"
);
let id = route.query.id || 1;
// Monitor id changes
watch(
  () => route.query.id,
  (newId) => {
    id = newId || -1;
    activeName.value = "1";
    getDpModelDetailById();
  },
  { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
const data = reactive({
  dpModelDetail: {},
  form: {},
});

const { dpModelDetail, rules } = toRefs(data);
const detailItems = computed(() => [
  {
    key: "modelName",
    label: td('dp.model.detail.englishName'),
  },
  { label: td('dp.model.tableType'), key: "tableType", dictOptions: table_type.value },
  {
    label: td('dp.model.hierarchy'),
    formatter: (data) => formatHierarchyDisplayName(data, data.tableType),
  },
  {
    label: td('dp.model.namingConvention'),
    formatter: (data) =>
      formatModelName({ ...data, modelName: "", modelNameSuffix: "" }),
  },
]);
const userList = ref();
/** Form query at the top of the complex details page */
function getDpModelDetailById() {
  const _ID = id;
  if (_ID == -1) {
    return;
  }
  getDpModel(_ID).then((response) => {
    dpModelDetail.value = response.data;
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
  });
}

getDpModelDetailById();
</script>

<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;

  .pagecont-bottom {
    min-height: calc(100vh - 345px) !important;
  }
}
</style>
