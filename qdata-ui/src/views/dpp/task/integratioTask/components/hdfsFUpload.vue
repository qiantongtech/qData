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
  <el-dialog v-model="visibleDialog" draggable class="dialog" :title="dialogTitle" destroy-on-close>
    <el-form ref="daDiscoveryTaskRef" :model="form" label-width="120px" @submit.prevent :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.fieldName', 'Field Name')" prop="columnName" :rules="[
            { required: true, message: td('dpp.integration.fieldNameRequired', 'Please enter field name'), trigger: 'blur' },
          ]" :label-position="labelPosition">
            <el-input v-model="form.columnName" :placeholder="td('dpp.integration.fieldNamePlaceholder', 'Please enter field name')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.fieldType', 'Field Type')" prop="columnType" :rules="[
            { required: true, message: td('dpp.integration.fieldTypeRequired', 'Please select field type'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-select v-model="form.columnType" :placeholder="td('dpp.integration.fieldTypePlaceholder', 'Please select field type')">
              <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label"
                :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="type != 1">
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.index', 'Index')" prop="index" :rules="[
            { required: true, message: td('dpp.integration.indexRequired', 'Please enter index'), trigger: 'blur' },
            {
              validator: (_rule, value, callback) => {
                if (value < 0) {
                  callback(new Error(td('dpp.integration.indexNoNegative', 'Index cannot be negative')))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ]">
            <el-input v-model.number="form.index" type="number" :placeholder="td('dpp.integration.indexPlaceholder', 'Please enter index')" :min="0" />
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
  type: { type: String, default: '0' },
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
  catCode: "",
  executionType: "PARALLEL",
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
