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
  For brand customization, please contact the official team for authorization.
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
  如需定制品牌，请通过官方渠道申请品牌授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="app-container">
    <el-input
      v-model="input1"
      style="max-width: 300px; margin-right: 20px"
      placeholder="单选"
      class="input-with-select"
    >
      <template #append>
        <el-button :icon="Search" @click="radioShow" />
      </template>
    </el-input>

    <el-input
      v-model="input2"
      style="max-width: 300px"
      placeholder="多选"
      class="input-with-select"
    >
      <template #append>
        <el-button :icon="Search" @click="checkShow" />
      </template>
    </el-input>

    <!-- 单选  字典管理 -->
    <Current ref="dictRef1" @confirm="radioSubmit" />
    <!-- 多选  字典管理 -->
    <Selection ref="dictRef2" @confirm="checkSubmit" />
  </div>
</template>

<script setup name="ToolChoose">
import Current from "./temp-current.vue";
import Selection from "./userTypeMultiple.vue";
import { Search } from "@element-plus/icons-vue";
const dictRef1 = ref();
const dictRef2 = ref();
const input1 = ref("");
const input2 = ref("");
const radioVal = ref(null);
const checkVal = ref([]);

// 单选
function radioShow() {
  dictRef1.value.open(radioVal.value);
}

function radioSubmit(val) {
  radioVal.value = val;
  input1.value = val.dictName;
}

// 多选
function checkShow() {
  dictRef2.value.open(checkVal.value);
}
function checkSubmit(val) {
  checkVal.value = [...val];
  input2.value = val.map((item) => item.id);
}
</script>
