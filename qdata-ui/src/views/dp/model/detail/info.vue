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
    <DescriptionsInfo
      :column="2"
      :data="daDiscoveryTaskDetail"
      :items="items"
      :border="true"
    >
      <template #tableType="{ data }">
        <dict-tag :options="table_type" :value="data.tableType" />
      </template>
    </DescriptionsInfo>
  </div>
</template>
<script setup name="BasicInfo">
import { useI18n } from 'vue-i18n'
import {
  formatModelName,
  formatHierarchyDisplayName,
} from "../../../../utils/dm/utils";
const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { dp_model_create_type, table_name_case, dp_document_type, table_type } =
  proxy.useDict(
    "dp_model_create_type",
    "table_name_case",
    "dp_document_type",
    "table_type"
  );
const props = defineProps({
  daDiscoveryTaskDetail: {
    type: Object,
    default: () => {},
  },
});
const items = computed(() => [
  // {
  //   label: "表类型",
  //   key: "tableType",
  //   slot: "tableType",
  // },
  {
    label: "命名大小写",
    key: "tableCase",
    dictOptions: table_name_case.value,
  },
  // {
  //   label: "归属层级",
  //   formatter: (val, data) => formatHierarchyDisplayName(data, data.tableType),
  // },
  {
    label: "表命名规范",
    formatter: (val, data) =>
      formatModelName({ ...data, modelName: "", modelNameSuffix: "" }),
  },
  {
    label: "标准类型",
    key: "documentType",
    dictOptions: dp_document_type.value,
  },
  { label: "标准登记", key: "documentName" },
  { label: t('common.texts.createdBy'), key: "createBy" },
  { label: "联系方式", key: "createUserPhoneNumber" },
  { label: t('common.texts.updatedBy'), key: "updateBy" },
  { label: "联系方式", key: "updateUserPhoneNumber" },
]);
</script>
