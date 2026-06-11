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
import { useI18n } from 'vue-i18n'
import { computed } from "vue";

const { t } = useI18n();
const props = defineProps({
  businessLayerDetail: {
    type: Object,
    default: () => ({}),
  },
});

const items = computed(() => [
  { key: "createBy", label: t('common.texts.createdBy') },
  { key: "createTime", label: t('common.texts.createdTime'), type: "time" },
  { key: "updateBy", label: t('common.texts.updatedBy') },
  { key: "updateTime", label: t('common.texts.updatedTime'), type: "time" },
  { key: "remark", label: t('common.texts.remark'), span: 24 },
]);
</script>
