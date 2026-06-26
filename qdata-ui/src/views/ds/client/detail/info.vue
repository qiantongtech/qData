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
  <div class="basicInfo">
    <el-descriptions title="" :column="2" border>
      <el-descriptions-item v-for="(item, index) in fileDesc" :key="index" label-class-name="base-label"
        :span="item.span" class-name="base-content">
        <template #label>
          <div class="cell-item">{{ item.label }}</div>
        </template>
        <div v-if="item.key == 'logo'">
          <image-preview :src="item.value" :width="50" :height="50" />
        </div>
        <div v-else-if="item.key == 'type'">
          <dict-tag :options="auth_app_type" :value="clientDetail.type" />
        </div>
        <div v-else-if="item.key == 'publicFlag'">
          <dict-tag :options="auth_public" :value="clientDetail.publicFlag" />
        </div>
        <div v-else>{{ getDescValue(item) }}</div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script setup name="BasicInfo">
import useDefaultLang from "@/composables/useDefaultLang";
import moment from "moment";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { auth_public, auth_app_type } = proxy.useDict("auth_public", "auth_app_type");
const props = defineProps({
  clientDetail: {
    type: Object,
    default: () => { },
  },
});
const fileDesc = computed(() => {
  return table.value;
});
const table = ref([
  // {
  //   key: "name",
  //   label: "应用名称",
  //   value: "",
  // },
  {
    key: "type",
    label: td('ds.client.details.appType'),
    value: "",
  },
  {
    key: "publicFlag",
    label: td('ds.client.details.isPublic'),
    value: "",
  },

  // {
  //   key: "allowUrl",
  //   label: "授权路径",
  //   value: "",
  // },
  {
    key: "createBy",
    label: td('common.texts.createdBy'),
    value: "",
  },
  {
    key: "createTime",
    label: td('common.texts.createdTime'),
    value: "",
    type: "time",
  },
  {
    key: "updateBy",
    label: td('common.texts.updatedBy'),
    value: "",
  },
  {
    key: "updateTime",
    label: td('common.texts.updatedTime'),
    value: "",
    type: "time",
  },
  {
    key: "remark",
    label: td('common.texts.remark'),
    value: "",
    span: 24,
  },
]);
const getDescValue = (row) => {
  let detail = { ...props.clientDetail };
  if (props.clientDetail) {
    if (row.type == "time") {
      row.value = moment(detail[row.key]).format("YYYY-MM-DD");
    } else {
      row.value = detail[row.key];
    }
  }
  return row.value !== null && row.value !== undefined && row.value !== "" ? row.value : "-";
};
</script>
<style lang="scss" scoped>
:deep(.base-label) {
  width: 200px;

  .cell-item {
    font-weight: 500;
  }
}
</style>