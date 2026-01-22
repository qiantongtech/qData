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
          label="正则表达式"
          prop="regex"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: '请输入正则表达式',
                    trigger: 'blur',
                  },
                ]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.regex"
            placeholder="请输入正则表达式"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.regex || "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          label="替换为"
          prop="replacement"
          :rules="
            !falg
              ? [{ required: true, message: '请输入替换内容', trigger: 'blur' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.replacement"
            placeholder="请输入替换内容"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.replacement || "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import { getColumnByAssetId } from "@/api/dpp/task/index.js";

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
          callback(new Error("请输入正则表达式"));
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
      message: "请选择允许的字符类型",
      trigger: "change",
    },
  ],
  ignoreNullValue: [
    { required: true, message: "请选择忽略空值", trigger: "change" },
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
