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
