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
  <!-- 正则表达式替换   -->
  <el-form
    ref="formRef"
    :model="form"
    :rules="rules"
    label-width="130px"
    :disabled="false"
  >
    <el-row>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.regex', '正则表达式')"
          prop="regex"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: td('dpp.cleanRule.inputRegex', '请输入正则表达式'),
                    trigger: 'blur',
                  },
                ]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.regex"
            :placeholder="td('dpp.cleanRule.inputRegex', '请输入正则表达式')"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.regex || "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.replaceWith', '替换为')"
          prop="replacement"
          :rules="
            !falg
              ? [{ required: true, message: td('dpp.cleanRule.inputReplacement', '请输入替换内容'), trigger: 'blur' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.replacement"
            :placeholder="td('dpp.cleanRule.inputReplacement', '请输入替换内容')"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.replacement || "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { reactive, ref, watch } from "vue";
import { getColumnByAssetId } from "@/api/dpp/task/index.js";
const { td } = useDefaultLang();

const props = defineProps({
  form: Object,
  inputFields: Array,
  falg: Boolean,
});
const emit = defineEmits(["update:form"]);

const formRef = ref(null);
const form = reactive({ ...props.form });
// 表单校验规则
const rules = {
  regex: [
    {
      validator: (rule, value, callback) => {
        if (form.pattern && !value) {
          callback(new Error(td('dpp.cleanRule.inputRegex', '请输入正则表达式')));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  allowedChars: [
    {
      type: "array",
      required: true,
      min: 1,
      message: td('dpp.cleanRule.selectAllowedChars', '请选择允许的字符类型'),
      trigger: "change",
    },
  ],
  ignoreNullValue: [
    { required: true, message: td('dpp.cleanRule.selectIgnoreNull', '请选择忽略空值'), trigger: "change" },
  ],
};
const exposedFields = ["regex", "replacement"];
function validate() {
  return new Promise((resolve, reject) => {
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
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
.rule-half {
  width: 100%;
}
</style>
