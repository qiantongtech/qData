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
            :label="td('dpp.integration.fieldName', 'Field Name')"
            prop="columnName"
            :rules="[
              { required: true, message: td('dpp.integration.fieldNameRequired', 'Please enter field name'), trigger: 'blur' },
            ]"
           :label-position="labelPosition">
            <el-input v-model="form.columnName" :placeholder="td('dpp.integration.fieldNamePlaceholder', 'Please enter field name')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.fieldType', 'Field Type')"
            prop="columnType"
            :rules="[
              { required: true, message: td('dpp.integration.fieldTypeRequired', 'Please select field type'), trigger: 'change' },
            ]"
           :label-position="labelPosition">
            <el-select v-model="form.columnType" :placeholder="td('dpp.integration.fieldTypePlaceholder', 'Please select field type')">
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
            :label="td('dpp.integration.jsonParseValue', 'JSON Parse Value')"
            prop="key"
            :rules="[
              {
                required: true,
                message: td('dpp.integration.jsonParseValueRequired', 'Please enter JSON parse value'),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-model="form.key"
              type="textarea"
              maxlength="256字符"
              show-word-limit
              :placeholder="td('dpp.integration.jsonParseValuePlaceholder', 'e.g.: info.aga')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <!-- close button -->
        <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
        <!-- save button -->
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

const dialogTitle = computed(() => props.title || td("common.form.namePlaceholder", "Please enter name"));

const emit = defineEmits(["update:visible", "confirm"]);
// Define field type array
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
  catCode: "", // Can be initialized to empty or set to default value
  executionType: "PARALLEL", // Initialized to empty or default value
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

// Computed property handling v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// How to close a dialog box
const closeDialog = () => {
  emit("update:visible", false);
};
let daDiscoveryTaskRef = ref();
// How to save data
const saveData = () => {
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      emit("confirm", form.value);
      emit("update:visible", false);
    } else {
      console.log("Form validation failed");
    }
  });
};
</script>
