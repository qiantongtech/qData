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
  <!-- 字符串类型校验 -->
  <el-form
    ref="formRef"
    :model="form"
    :rules="falg ? {} : rules"
    :disabled="false"
  >
    <el-row :gutter="20">
      <el-col :span="12">
        <!-- 评测对象下拉 -->
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.useRegex')" prop="useRegexFlag">
          <el-checkbox
            v-if="!falg"
            v-model="form.useRegexFlag"
            :true-value="1"
            :false-value="0"
            >{{ td('da.qualityTaskRules.ruleCommon.useRegex') }}</el-checkbox
          >
          <div v-else class="form-readonly">
            {{ form.useRegexFlag == 1 ? td('da.qualityTaskRules.ruleCommon.useRegex') : td('da.qualityTaskRules.ruleCommon.noRegex') }}
          </div>
        </el-form-item>
      </el-col>
      <el-col :span="12" v-if="!form.useRegexFlag" class="hasMsg">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.allowedCharType')" prop="allowedChars">
          <template v-if="!falg">
            <el-checkbox-group v-model="form.allowedChars" name="chars">
              <el-checkbox :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.number') }}</el-checkbox>
              <el-checkbox :value="'2'">{{ td('da.qualityTaskRules.ruleCommon.letter') }}</el-checkbox>
              <el-checkbox :value="'3'">{{ td('da.qualityTaskRules.ruleCommon.space') }}</el-checkbox>
              <el-checkbox :value="'4'">{{ td('da.qualityTaskRules.ruleCommon.specialChar') }}</el-checkbox>
            </el-checkbox-group>
          </template>
          <div v-else class="form-readonly">{{ allowedCharsText }}</div>
          <span class=”msg”
            ><el-icon>
              <InfoFilled /> </el-icon
            >{{ td('da.qualityTaskRules.ruleCommon.regexTip') }}</span
          >
        </el-form-item>
      </el-col>
      <el-col :span="12" v-if="!form.useRegexFlag">
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
      <el-col :span="12" v-if="form.useRegexFlag">
        <el-form-item
          :label="td('da.qualityTaskRules.ruleCommon.regexLabel')"
          prop="regex"
          :rules="[
            {
              required: form.useRegexFlag,
              message: td('da.qualityTaskRules.ruleCommon.regexRequired'),
              trigger: 'blur',
              validator: (rule, value, callback) => {
                if (form.useRegexFlag && !value) {
                  callback(new Error(td('da.qualityTaskRules.ruleCommon.regexRequired')));
                } else {
                  callback();
                }
              },
            },
          ]"
        >
          <el-input
            v-if="!falg"
            v-model="form.regex"
            :placeholder="td('da.qualityTaskRules.ruleCommon.regexPlaceholder')"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.regex || "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { reactive, ref, watch, computed } from "vue";
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
const columnList = ref([]);
const allowedCharsText = computed(() => {
  const map = { 1: td('da.qualityTaskRules.ruleCommon.number'), 2: td('da.qualityTaskRules.ruleCommon.letter'), 3: td('da.qualityTaskRules.ruleCommon.space'), 4: td('da.qualityTaskRules.ruleCommon.specialChar') };
  return Array.isArray(form.allowedChars) && form.allowedChars.length
    ? form.allowedChars.map((v) => map[v] || v).join(", ")
    : "-";
});
watch(
  () => form.useRegexFlag,
  (val) => {
    if (val) {
      form.allowedChars = ["1"];
      form.ignoreNullValue = "1";
    }
  }
);
// 表单校验规则
const rules = {
  regex: [
    {
      validator: (rule, value, callback) => {
        if (form.useRegexFlag && !value) {
          callback(new Error(td('da.qualityTaskRules.ruleCommon.regexRequired')));
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
      message: td('da.qualityTaskRules.ruleCommon.charTypeRequired'),
      trigger: "change",
    },
  ],
  ignoreNullValue: [
    { required: true, message: td('da.qualityTaskRules.ruleCommon.ignoreNullRequired'), trigger: "change" },
  ],
};

const exposedFields = [
  "useRegexFlag",
  "allowedChars",
  "ignoreNullValue",
  "regex",
];

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
.rule-half {
  width: 100%;
}
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
</style>
