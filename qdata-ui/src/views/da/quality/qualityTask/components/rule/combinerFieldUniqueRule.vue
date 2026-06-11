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
  <!-- 字段组完整性校验 -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="24">
        <el-form-item label="字段完整性" prop="fillStrategy">
          <el-radio-group
            v-if="!falg"
            v-model="form.fillStrategy"
            class="rule-half"
          >
            <el-radio :value="'1'">必须全部填写（部分为空为异常）</el-radio>
            <el-radio :value="'2'"
              >要么全部为空，要么全部填写（部分填写为异常）</el-radio
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
    ? "必须全部填写（部分为空为异常）"
    : form.fillStrategy === "2"
    ? "要么全部为空，要么全部填写（部分填写为异常）"
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
