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
      :data="AttTagDetail"
      :items="items"
      :border="true"
    />
  </div>
</template>
<script setup name="BasicInfo">
import useDefaultLang from "@/composables/useDefaultLang.js";

const { td } = useDefaultLang();
const { AttTagDetail } = defineProps({
  AttTagDetail: {
    type: Object,
    default: () => ({}),
  },
});

const items = computed(() => [
  { key: "createBy", label: td('common.texts.createdBy') },
  { key: "createTime", label: td('common.texts.createdTime'), type: "time" },
  { key: "updateBy", label: td('common.texts.updatedBy') },
  { key: "updateTime", label: td('common.texts.updatedTime'), type: "time" },
]);
</script>
