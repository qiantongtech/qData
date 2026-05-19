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
          label="属性字段"
          name="1"
        >
          <modelColumn :is-detail="isMaterializedDetail" />
        </qt-tab-pane>
        <el-tab-pane label="发布日志" name="2" v-if="isMaterializedDetail">
          <modelMaterialized
            :modelId="route.query.id"
            :row="dpModelDetail"
          ></modelMaterialized>
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="3">
          <info :daDiscoveryTaskDetail="dpModelDetail"></info>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="DpModel">
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
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || -1;
    activeName.value = "1";
    getDpModelDetailById();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  dpModelDetail: {},
  form: {},
});

const { dpModelDetail, rules } = toRefs(data);
const detailItems = computed(() => [
  {
    key: "modelName",
    label: "英文名称",
  },
  { label: "表类型", key: "tableType", dictOptions: table_type.value },
  {
    label: "归属层级",
    formatter: (data) => formatHierarchyDisplayName(data, data.tableType),
  },
  {
    label: "表命名规范",
    formatter: (data) =>
      formatModelName({ ...data, modelName: "", modelNameSuffix: "" }),
  },
]);
const userList = ref();
/** 复杂详情页面上方表单查询 */
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
