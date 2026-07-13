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
  <!-- Numerical accuracy check -->
  <el-form ref="formRef" :model="form" :disabled="false">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.decimalPlaces')" prop="scale">
          <el-input
            v-if="!falg"
            v-model="form.scale"
            :placeholder="td('da.qualityTaskRules.ruleCommon.decimalPlaceholder')"
            type="number"
            min="0"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.scale ?? "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.ignoreInteger')" prop="skipInteger">
          <el-radio-group v-if="!falg" v-model="form.skipInteger">
            <el-radio :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.yes') }}</el-radio>
            <el-radio :value="'0'">{{ td('da.qualityTaskRules.ruleCommon.no') }}</el-radio>
          </el-radio-group>
          <div v-else class="form-readonly">
            {{
              form.skipInteger === "1"
                ? td('da.qualityTaskRules.ruleCommon.yes')
                : form.skipInteger === "0"
                ? td('da.qualityTaskRules.ruleCommon.no')
                : "-"
            }}
          </div>
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
import { getColumnByAssetId } from "@/api/dpp/task/index.js";
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

const exposedFields = ["scale", "skipInteger", "ignoreNullValue"];

function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
        const result = Object.fromEntries(
          exposedFields.map((key) => [key, form[key]])
        );
        resolve({ valid: true, data: result });
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
