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
    <DescriptionsInfo :column="2" :data="businessLayerDetail" :items="items" :border="true" />
  </div>
</template>

<script setup name="BusinessLayerInfo">
import useDefaultLang from "@/composables/useDefaultLang"
import { computed } from "vue";

const { td } = useDefaultLang();
const props = defineProps({
  businessLayerDetail: {
    type: Object,
    default: () => ({}),
  },
});

const items = computed(() => [
  { key: "createBy", label: td('common.texts.createdBy', 'Created By') },
  { key: "createTime", label: td('common.texts.createdTime', 'Created Time'), type: "time" },
  { key: "updateBy", label: td('common.texts.updatedBy', 'Updated By') },
  { key: "updateTime", label: td('common.texts.updatedTime', 'Updated Time'), type: "time" },
  { key: "remark", label: td('common.texts.remark', 'Remark'), span: 24 },
]);
</script>
