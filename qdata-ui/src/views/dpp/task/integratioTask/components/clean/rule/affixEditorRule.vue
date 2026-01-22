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
  <!--  字段前缀/后缀统一  -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item
          label="标记值"
          prop="stringValue"
          :rules="
            !falg
              ? [{ required: true, message: '请输入标记值', trigger: 'blur' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.stringValue"
            placeholder="请输入添加值"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.stringValue || "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12" class="hasMsg">
        <el-form-item
          label="处理方式"
          prop="handleType"
          :rules="
            !falg
              ? [{ required: true, message: '请选择处理方式', trigger: 'blur' }]
              : []
          "
        >
          <el-radio-group v-model="form.handleType" :disabled="falg">
            <el-radio :value="'1'">加前綴</el-radio>
            <el-radio :value="'2'">加后綴</el-radio>
            <el-radio :value="'3'">去除前缀</el-radio>
            <el-radio :value="'4'">去除后缀</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row> </el-row>
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

const form = reactive({ ...props.form });
const boundaryExamples = computed(() => {
  switch (form.handleType) {
    case "3":
      return ["示例: 如果年龄 > 150，则设置为 150。"];
    case "2":
      return ["示例: 如果收入 < 1000，则设置为 1000。"];
    case "1":
      return [
        "示例1: 如果年龄 > 150，则设置为 150。如果收入 < 1000，则设置为 1000。",
      ];
    default:
      return [];
  }
});
const handleTypeText = computed(() => {
  if (form.handleType === "1") return "加前綴";
  if (form.handleType === "2") return "加后綴";
  if (form.handleType === "3") return "去除前缀";
  if (form.handleType === "4") return "去除后缀";
  return "-";
});
const loading = ref(false);
const exposedFields = ["stringValue", "handleType"];
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
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
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
.rule-half {
  width: 100%;
}
</style>
