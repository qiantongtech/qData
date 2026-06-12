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
      :data="ruleDetail"
      :items="items"
      :border="true"
    />

    <!-- <div class="mt20" v-if="ruleDetail.intervalList && ruleDetail.intervalList.length">
      <div class="section-title mb15">脱敏区间</div>
      <el-table :data="ruleDetail.intervalList" border stripe>
        <el-table-column label="区间号" prop="intervalNo" width="100" />
        <el-table-column label="起始位置" prop="startNum" />
        <el-table-column label="截止位置" prop="endNum" />
      </el-table>
    </div> -->
  </div>
</template>

<script setup name="DesensitizationRuleInfo">
import useDefaultLang from "@/composables/useDefaultLang"
import { computed } from "vue";

const { td } = useDefaultLang();
const { ruleDetail } = defineProps({
  ruleDetail: {
    type: Object,
    default: () => ({}),
  },
});

const items = computed(() => [
  { key: "createBy", label: td('common.texts.createdBy') },
  { key: "createTime", label: td('common.texts.createdTime'), type: "time" },
  { key: "updateBy", label: td('common.texts.updatedBy') },
  { key: "updateTime", label: td('common.texts.updatedTime'), type: "time" },
  { key: "remark", label: td('common.texts.remark'), span: 24 },
]);
</script>

<style scoped lang="scss">
.section-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  position: relative;
  padding-left: 10px;
  &::before {
    content: "";
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 3px;
    height: 14px;
    background: #409eff;
  }
}
</style>

