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
