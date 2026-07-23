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
        <el-col :span="24">
          <el-form-item :label="td('dpp.integration.benchmarkType', 'Benchmark Type')" prop="type" :rules="[
            { required: true, message: td('dpp.integration.benchmarkTypeRequired', 'Please select benchmark type'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-select v-model="form.type" :placeholder="td('dpp.integration.benchmarkTypePlaceholder', 'Please select benchmark type')">
              <el-option v-for="benchmark in benchmarkTypes" :key="benchmark.value" :label="benchmark.label"
                         :value="benchmark.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dpp.integration.fieldName', 'Field Name')" prop="incrementColumn" :rules="[
            { required: true, message: td('dpp.integration.fieldNameRequired', 'Please enter field name'), trigger: 'blur' },
          ]" :label-position="labelPosition">
            <el-select v-model="form.incrementColumn" :placeholder="td('dpp.integration.fieldNamePlaceholder', 'Please enter field name')">
              <el-option v-for="item in ColumnByAssettab" :key="item.columnName" :label="item.columnName"
                         :value="item.columnName" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.type !== '2'">
          <el-form-item :label="td('dpp.integration.operator', 'Operator')" prop="operator" :rules="[
            { required: true, message: td('dpp.integration.operatorRequired', 'Please select operator'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-select v-model="form.operator" :placeholder="td('dpp.integration.operatorPlaceholder', 'Please select operator')">
              <el-option v-for="operator in operators" :key="operator.value" :label="operator.label"
                         :value="operator.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dpp.integration.benchmarkValue', 'Benchmark Value')" prop="data" :rules="dataRules" :label-position="labelPosition">
            <template v-if="form.type === '1'">
              <el-date-picker clearable
                              v-model="form.data"
                              :type="pickerType"
                              :format="dateIncrementConfig_dateFormat2"
                              :value-format="dateIncrementConfig_dateFormat2"
                              :placeholder="td('dpp.integration.selectFixedTime', 'Please select fixed time')">
              </el-date-picker>
            </template>
            <template v-else-if="form.type === '3'">
              <sql-editor :placeholder="td('dpp.integration.sqlStatement', 'SQL Statement')" ref="editorRef" :value="form.data" class="sql-editor" :height="'300px'"
                          @changeTextarea="changeTextarea($event)" />
            </template>
            <template v-else>
              <el-input v-model="form.data" :placeholder="td('dpp.integration.autoCurrentTime', 'Auto-get current time, no need to fill')" disabled />
            </template>
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="form.type === '2'">
          <el-form-item :label="td('dpp.integration.cursorTime', 'Cursor Time')" prop="cursorTime" :rules="[{ required: true, message: '请选择游标时间', trigger: 'change' }]" :label-position="labelPosition">
            <el-date-picker clearable
                            v-model="form.cursorTime"
                            :type="pickerType"
                            :format="dateIncrementConfig_dateFormat2"
                            :value-format="dateIncrementConfig_dateFormat2"
                            :placeholder="td('dpp.integration.cursorTimePlaceholder', 'Please select cursor time')">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
        <el-button type="primary" @click="saveData">{{ td('common.button.save') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { defineProps, defineEmits, ref, computed, watch, getCurrentInstance } from "vue";
import SqlEditor from "@/components/SqlEditor/index1.vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '' },
  data: { type: Object, default: () => ({}) },
  ColumnByAssettab: { type: Array, default: () => [] },
  dateIncrementConfig_dateFormat: { type: String, default: 'YYYY-MM-DD' },
});

const dialogTitle = computed(() => props.title || td("common.form.namePlaceholder", "Please enter name"));

// day.js is not compatible with the date format of java and needs to be processed
const dateIncrementConfig_dateFormat2 = computed(() => {
  return props.dateIncrementConfig_dateFormat
      .replace(/yyyy/g, 'YYYY')
      .replace(/dd/g, 'DD')
});
const pickerType = computed(() => {
  const format = props.dateIncrementConfig_dateFormat;
  return format.includes('HH') ? 'datetime' : 'date';
});

const emit = defineEmits(["update:visible", "confirm"]);

const form = ref({
  incrementColumn: "",
  operator: "",
  type: "",
  data: "",
});

let daDiscoveryTaskRef = ref();
let editorRef = ref("");

// operator
const operators = ref([
  { label: ">", value: ">" },
  { label: ">=", value: ">=" },
  { label: "<", value: "<" },
  { label: "<=", value: "<=" },
]);

// Base type
const benchmarkTypes = ref([
  { label: td("dpp.integration.fixedValue", "Fixed Value"), value: "1" },
  { label: td("dpp.integration.timeRange", "Time Range"), value: "2" },
  { label: td("dpp.integration.sqlExpression", "SQL Expression"), value: "3" },
]);

// Dynamic base value rules
const dataRules = computed(() => {
  if (form.value.type === "1" || form.value.type === "3") {
    return [{ required: true, message: td("dpp.integration.benchmarkValueRequired", "Please enter benchmark value"), trigger: "change" }];
  }
  return [];
});

// Initialize the form when listening to the visible pop-up window.
watch(
    () => props.visible,
    (newVal) => {
      if (newVal) {
        form.value = JSON.parse(JSON.stringify(props.data || {}));
      } else {
        proxy.resetForm("daDiscoveryTaskRef");
      }
    }
);

// Field name selected by default
watch(
    () => props.ColumnByAssettab,
    (newVal) => {
      if (newVal?.length > 0 && !form.value.incrementColumn) {
        form.value.incrementColumn = newVal[0].columnName;
      }
    },
    { immediate: true, deep: true }
);

// Monitoring type changes reset data
watch(() => form.value.type, (newType) => {
  form.value.data = "";
});

// SQL editor change callback
function changeTextarea(val) {
  form.value.data = val;
}

// Show/hide dialog
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// Close pop-up window
const closeDialog = () => {
  emit("update:visible", false);
};

// save logic
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
