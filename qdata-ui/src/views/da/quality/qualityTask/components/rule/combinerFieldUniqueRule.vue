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
  <!-- Field group integrity check -->
  <el-form ref="formRef" :model="form" :disabled="false">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.fieldCompleteness')" prop="fillStrategy">
          <el-radio-group
            v-if="!falg"
            v-model="form.fillStrategy"
            class="rule-half"
          >
            <el-radio :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.mustFillAll') }}</el-radio>
            <el-radio :value="'2'"
              >{{ td('da.qualityTaskRules.ruleCommon.fillAllOrNone') }}</el-radio
            >
          </el-radio-group>
          <div v-else class="form-readonly">{{ fillStrategyText }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { reactive, ref, computed } from "vue";
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
const fillStrategyText = computed(() =>
  form.fillStrategy === "1"
    ? td('da.qualityTaskRules.ruleCommon.mustFillAll')
    : form.fillStrategy === "2"
    ? td('da.qualityTaskRules.ruleCommon.fillAllOrNone')
    : "-"
);
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
        const exposedFields = ["fillStrategy"];
        const data = Object.fromEntries(
          exposedFields.map((key) => [key, form[key]])
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
