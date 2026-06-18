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
  <!-- 字段长度范围校验 -->
  <el-form ref="formRef" :model="form" :disabled="false">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.minLength')" prop="minLength">
          <el-input
            v-if="!falg"
            v-model="form.minLength"
            :placeholder="td('da.qualityTaskRules.ruleCommon.minLengthPlaceholder')"
            type="number"
            min="0"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.minLength ?? "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.maxLength')" prop="maxLength">
          <el-input
            v-if="!falg"
            v-model="form.maxLength"
            :placeholder="td('da.qualityTaskRules.ruleCommon.maxLengthPlaceholder')"
            type="number"
            min="0"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.maxLength ?? "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.ignoreNullValue')" prop="ignoreNullValue">
          <el-radio-group v-if="!falg" v-model="form.ignoreNullValue">
            <el-radio :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.yes') }}</el-radio>
            <el-radio :value="'0'">{{ td('da.qualityTaskRules.ruleCommon.no') }}</el-radio>
          </el-radio-group>
          <div v-else class="form-readonly">
            {{
              form.ignoreNullValue === "1"
                ? td('da.qualityTaskRules.ruleCommon.yes')
                : form.ignoreNullValue === "0"
                ? td('da.qualityTaskRules.ruleCommon.no')
                : "-"
            }}
          </div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const props = defineProps({
  form: Object,
  dppQualityTaskObjSaveReqVO: Array,
  falg: Boolean,
});

const emit = defineEmits(["update:form"]);
const formRef = ref(null);
const form = reactive({ ...props.form });
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
        const data = Object.fromEntries(
          ["minLength", "maxLength", "ignoreNullValue"].map((key) => [
            key,
            form[key],
          ])
        );
        resolve({
          valid: true,
          data,
        });
      } else {
        resolve({ valid: false });
      }
    });
  });
}

defineExpose({ validate });
</script>
<style scoped>
.rule-half {
  width: 100%;
}
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
</style>
