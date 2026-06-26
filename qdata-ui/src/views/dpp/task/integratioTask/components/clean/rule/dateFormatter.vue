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

<!-- 日期格式统一 -->
<template>
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.dateFormat', '日期格式')"
          prop="selectedOption"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: td('dpp.cleanRule.selectDateFormat', '请选择日期格式'),
                    trigger: 'change',
                  },
                ]
              : []
          "
        >
          <template v-if="!falg">
            <el-select
              v-model="form.selectedOption"
              :placeholder="td('dpp.cleanRule.selectDateFormat', '请选择日期格式')"
              class="rule-half"
            >
              <el-option label="yyyy" value="yyyy" />
              <el-option label="yyyy-MM" value="yyyy-MM" />
              <el-option label="yyyy-MM-dd" value="yyyy-MM-dd" />
              <el-option label="yy-MM-dd" value="yy-MM-dd" />
              <el-option
                label="yyyy-MM-dd HH:mm:ss"
                value="yyyy-MM-dd HH:mm:ss"
              />
            </el-select>
          </template>
          <div v-else class="form-readonly">{{ selectedOptionText }}</div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12" v-if="form.selectedOption == '1'">
        <el-form-item
          :label="td('dpp.cleanRule.dateFormat', '日期格式')"
          prop="targetFormat"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: td('dpp.cleanRule.inputDateFormat', '请输入日期格式，例如：YY-MM-DD'),
                    trigger: 'blur',
                  },
                ]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.targetFormat"
            :placeholder="td('dpp.cleanRule.inputDateFormat', '请输入日期格式，例如：YY-MM-DD')"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.targetFormat || "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { reactive, ref, watch } from "vue";
const { td } = useDefaultLang();

const props = defineProps({
  form: Object,
  inputFields: Array,
  falg: Boolean,
});

const emit = defineEmits(["update:form"]);

const formRef = ref(null);

// 初始化 selectedOption
// const getInitialSelectedOption = (targetFormat) => {
//   if (!targetFormat) return "yyyy-MM-dd";
//   // 判断是否为预设值
//   if (targetFormat === "yyyy-MM-dd" || targetFormat === "YYYY-MM-DD HH:mm:ss") {
//     return targetFormat;
//   }
//   // 否则为自定义值，选择配置模板
//   return "1";
// };

const form = reactive({
  ...props.form,
});
const selectedOptionText = computed(() => {
  if (form.selectedOption === "1") return td('dpp.cleanRule.custom', '自定义');
  return form.selectedOption || "-";
});

watch(
  () => form.selectedOption,
  (newVal, oldVal) => {
    if (newVal === "1" && oldVal !== "1") {
      // 从预设值切换到配置模板时，清空 targetFormat，让用户输入自定义格式
      form.targetFormat = "";
    } else if (newVal !== "1") {
      // 选择预设值时，targetFormat 设置为选中的值
      form.targetFormat = newVal;
    }
  }
);
//
function validate() {
  console.log("🚀 ~ validate ~ props.form?:", props.form?.inputFormats);
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
        resolve({
          valid: true,
          data: {
            targetFormat: form.targetFormat,
            selectedOption: form.selectedOption,
            inputFormats: props.form?.inputFormats,
          },
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
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
.rule-half {
  width: 100%;
}
</style>
