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

<!-- 日期格式统一 -->
<template>
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item
          label="日期格式"
          prop="selectedOption"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: '请选择日期格式',
                    trigger: 'change',
                  },
                ]
              : []
          "
        >
          <template v-if="!falg">
            <el-select
              v-model="form.selectedOption"
              placeholder="请选择日期格式"
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
          label="日期格式"
          prop="targetFormat"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: '请输入日期格式，例如：YY-MM-DD',
                    trigger: 'blur',
                  },
                ]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.targetFormat"
            placeholder="请输入日期格式，例如：YY-MM-DD"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.targetFormat || "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";

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
  if (form.selectedOption === "1") return "自定义";
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
