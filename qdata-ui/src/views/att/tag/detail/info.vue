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
      :data="AttTagDetail"
      :items="items"
      :border="true"
    />
  </div>
</template>
<script setup name="BasicInfo">
import { useI18n } from 'vue-i18n'

const { t } = useI18n();
const { AttTagDetail } = defineProps({
  AttTagDetail: {
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
