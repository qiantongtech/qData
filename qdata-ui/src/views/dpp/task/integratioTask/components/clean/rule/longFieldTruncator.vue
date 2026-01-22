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
  <!--  超长字段截断  -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item
          label="字符数量"
          prop="maxLength"
          :rules="
            !falg
              ? [{ required: true, message: '请输入字符数量', trigger: 'blur' }]
              : []
          "
        >
          <el-input-number v-if="!falg" v-model="form.maxLength" :min="0" />
          <div v-else class="form-readonly">{{ form.maxLength ?? "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12" class="hasMsg">
        <el-form-item
          label="处理方式"
          prop="direction"
          :rules="
            !falg
              ? [{ required: true, message: '请选择处理方式', trigger: 'blur' }]
              : []
          "
        >
          <el-radio-group v-model="form.direction" :disabled="falg">
            <el-radio :value="'1'">正向</el-radio>
            <el-radio :value="'2'">反向</el-radio>
          </el-radio-group>
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

const form = reactive({ ...props.form });
console.log("🚀 ~ form:", form);
const exposedFields = ["maxLength", "direction"];
const directionText = computed(() =>
  form.direction === "1" ? "正向" : form.direction === "2" ? "反向" : "-"
);
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
