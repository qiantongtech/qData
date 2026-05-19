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
import { computed } from "vue";
const { ruleDetail } = defineProps({
  ruleDetail: {
    type: Object,
    default: () => ({}),
  },
});

const items = computed(() => [
  { key: "createBy", label: "创建人" },
  { key: "createTime", label: "创建时间", type: "time" },
  { key: "updateBy", label: "更新人" },
  { key: "updateTime", label: "更新时间", type: "time" },
  { key: "remark", label: "备注", span: 24 },
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

