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
import moment from "moment";
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
    label: "应用类型",
    value: "",
  },
  {
    key: "publicFlag",
    label: "是否公开",
    value: "",
  },

  // {
  //   key: "allowUrl",
  //   label: "授权路径",
  //   value: "",
  // },
  {
    key: "createBy",
    label: "创建人",
    value: "",
  },
  {
    key: "createTime",
    label: "创建时间",
    value: "",
    type: "time",
  },
  {
    key: "updateBy",
    label: "更新人",
    value: "",
  },
  {
    key: "updateTime",
    label: "更新时间",
    value: "",
    type: "time",
  },
  {
    key: "remark",
    label: "备注",
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
