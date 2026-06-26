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
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="dialog"
    :title="dialogTitle"
    destroy-on-close
  >
    <el-form
      ref="daDiscoveryTaskRef"
      :model="form"
      label-width="120px"
      @submit.prevent
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.fieldName', '字段名称')"
            prop="columnName"
            :rules="[
              { required: true, message: td('dpp.integration.fieldNameRequired', '请输入字段名称'), trigger: 'blur' },
            ]"
           :label-position="labelPosition">
            <el-input v-model="form.columnName" :placeholder="td('dpp.integration.fieldNamePlaceholder', '请输入字段名称')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.fieldType', '字段类型')"
            prop="columnType"
            :rules="[
              { required: true, message: td('dpp.integration.fieldTypeRequired', '请选择字段类型'), trigger: 'change' },
            ]"
           :label-position="labelPosition">
            <el-select v-model="form.columnType" :placeholder="td('dpp.integration.fieldTypePlaceholder', '请选择字段类型')">
              <el-option
                v-for="dict in columntype"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            :label="td('dpp.integration.jsonParseValue', 'JSON解析值')"
            prop="key"
            :rules="[
              {
                required: true,
                message: td('dpp.integration.jsonParseValueRequired', '请输入JSON解析值'),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-model="form.key"
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('dpp.integration.jsonParseValuePlaceholder', '例如:info.aga')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <!-- 关闭按钮 -->
        <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
        <!-- 保存按钮 -->
        <el-button type="primary" @click="saveData">{{ td('common.button.save') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { defineProps, defineEmits, ref, computed, watch } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type } = proxy.useDict("column_type");

const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '' },
  data: { type: Object, default: () => ({}) },
});

const dialogTitle = computed(() => props.title || td("common.form.namePlaceholder", "表单标题"));

const emit = defineEmits(["update:visible", "confirm"]);
// 定义字段类型数组
const columntype = [
  { value: "STRING", label: "STRING" },
  { value: "BOOL", label: "BOOL" },
  { value: "BYTES", label: "BYTES" },
  { value: "DATE", label: "DATE" },
  { value: "DOUBLE", label: "DOUBLE" },
  { value: "LONG", label: "LONG" },
];
const form = ref({
  name: "",
  catCode: "", // 可以初始化为空，也可以设为默认值
  executionType: "PARALLEL", // 初始化为空或默认值
  crontab: "",
  releaseState: 0,
  description: "",
  contactNumber: "",
  catCode: "",
  personCharge: "",
});

watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      form.value = JSON.parse(JSON.stringify(props.data || {}));
      console.log("🚀 ~ form.value:", props.data);
    } else {
      proxy.resetForm("daDiscoveryTaskRef");
    }
    console.log("🚀 ~ props.data:", props);
  }
);

// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// 关闭对话框的方法
const closeDialog = () => {
  emit("update:visible", false);
};
let daDiscoveryTaskRef = ref();
// 保存数据的方法
const saveData = () => {
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      emit("confirm", form.value);
      emit("update:visible", false);
    } else {
      console.log("表单校验未通过");
    }
  });
};
</script>
