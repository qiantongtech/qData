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
import useDefaultLang from "@/composables/useDefaultLang"
import {
  formatModelName,
  formatHierarchyDisplayName,
} from "../../../../utils/dm/utils";
const { td } = useDefaultLang();
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
  {
    label: td('dp.model.detail.caseConvention'),
    key: "tableCase",
    dictOptions: table_name_case.value,
  },
  {
    label: td('dp.model.namingConvention'),
    formatter: (val, data) =>
      formatModelName({ ...data, modelName: "", modelNameSuffix: "" }),
  },
  {
    label: td('dp.modelForm.documentType'),
    key: "documentType",
    dictOptions: dp_document_type.value,
  },
  { label: td('dp.modelForm.documentId'), key: "documentName" },
  { label: td('common.texts.createdBy'), key: "createBy" },
  { label: td('dp.model.detail.contactInfo'), key: "createUserPhoneNumber" },
  { label: td('common.texts.updatedBy'), key: "updateBy" },
  { label: td('dp.model.detail.contactInfo'), key: "updateUserPhoneNumber" },
]);
</script>
