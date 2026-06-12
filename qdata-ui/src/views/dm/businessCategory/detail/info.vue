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
  { key: "createBy", label: td('common.texts.createdBy', '创建人') },
  { key: "createTime", label: td('common.texts.createdTime', '创建时间'), type: "time" },
  { key: "updateBy", label: td('common.texts.updatedBy', '更新人') },
  { key: "updateTime", label: td('common.texts.updatedTime', '更新时间'), type: "time" },
  { key: "remark", label: td('common.texts.remark', '备注'), span: 24 },
]);
</script>
