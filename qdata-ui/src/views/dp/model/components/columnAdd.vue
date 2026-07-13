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
  <el-dialog v-model="localVisible" :title="title" draggable destroy-on-close>
    <el-form
      ref="dpModelRefs"
      :model="form"
      :rules="rules"
      label-width="100px"
      @submit.prevent
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.relatedStandard')" prop="dataElemId" :label-position="labelPosition">
            <el-select
              v-model="form.dataElemId"
              :placeholder="td('dp.modelForm.relatedStandardPlaceholder')"
              @change="handleDatasourceChange"
              filterable
              clearable
            >
              <el-option
                v-for="dict in DpData"
                :key="dict.id"
                :label="dict.name"
                :value="dict.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.model.chineseName')" prop="cnName" :label-position="labelPosition">
            <el-input v-model="form.cnName" :placeholder="td('dp.model.chineseNamePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.model.englishName')" prop="engName" :label-position="labelPosition">
            <el-input
              v-model="form.engName"
              :placeholder="td('dp.model.englishNamePlaceholder')"
              @input="convertToUpperCase('engName', form.engName)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.dataType')" prop="columnType" :label-position="labelPosition">
            <el-select v-model="form.columnType" :placeholder="td('dp.modelForm.dataTypePlaceholder')">
              <el-option
                v-for="dict in column_type"
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
            :label="td('dp.modelForm.attributeLength')"
            prop="columnLength"
            :rules="
              form.columnType === 'DATE'
                ? []
                : [
                    {
                      required: true,
                      message: td('dp.modelForm.attributeLengthRequired'),
                      trigger: 'change',
                    },
                  ]
            "
           :label-position="labelPosition">
            <el-input-number
              :step="1"
              step-strictly
              v-model="form.columnLength"
              style="width: 100%"
              controls-position="right"
              :min="1"
              :max="9999999999"
              :placeholder="td('dp.modelForm.attributeLengthPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <!-- DECIMAL  NUMBER  NUMERIC -->
          <el-form-item :label="td('dp.modelForm.decimalPlaces')" prop="columnScale" :label-position="labelPosition">
            <el-input-number
              :step="1"
              :disabled="
                form.columnType !== 'DECIMAL' &&
                form.columnType !== 'NUMBER' &&
                form.columnType !== 'NUMERIC' &&
                form.columnType !== 'FLOAT' &&
                form.columnType !== 'DOUBLE'
              "
              step-strictly
              v-model="form.columnScale"
              style="width: 100%"
              controls-position="right"
              :min="0"
              :max="9999999999"
              :placeholder="td('dp.modelForm.decimalPlacesPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.defaultValue')" prop="defaultValue" :label-position="labelPosition">
            <el-input v-model="form.defaultValue" :placeholder="td('dp.modelForm.defaultValuePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="modelComment" :label-position="labelPosition">
            <el-input
              v-model="form.description"
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.isPrimaryKey')" prop="pkFlag" :label-position="labelPosition">
            <el-radio-group v-model="form.pkFlag" @change="handlePkFlagChange">
              <el-radio
                v-for="dict in dp_model_column_pk_flag"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.isRequired')" prop="nullableFlag" :label-position="labelPosition">
            <el-radio-group
              v-model="form.nullableFlag"
              :disabled="form.pkFlag == 1"
            >
              <el-radio
                v-for="dict in dp_model_column_nullable_flag"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="confirmDialog"> {{ td('common.button.confirm') }} </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { defineProps, defineEmits, ref, computed, watch } from "vue";

import { getDpDataElemList } from "@/api/dp/dataElem/dataElem";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type, dp_model_column_pk_flag, dp_model_column_nullable_flag } =
  proxy.useDict(
    "column_type",
    "dp_model_column_pk_flag",
    "dp_model_column_nullable_flag"
  );

// Receive properties passed by parent component
const props = defineProps({
  visible: { type: Boolean, default: true },
  deptOptions: { type: Array, default: () => [] },
  column_type: { type: Array, default: () => [] },
  userList: { type: Array, default: () => [] },
  deptList: { type: Array, default: () => [] },
  row: { type: Object, default: () => ({}) },
  data: { type: Object, default: () => {} },
});
let title = ref();
watch(
  () => props.visible,
  (newVal) => {
    console.log("Object.keys(props.row).length === 0", props.row.index);
    if (newVal) {
      getDpDataElem();
      if (props.row && props.row.index !== undefined) {
        // Edit status
        title.value = td('dp.modelForm.editColumnTitle');
        Object.assign(form.value, props.row);
        form.value.authorityDept = Number(form.value.authorityDept);
      } else {
        // Add status
        title.value = td('dp.modelForm.addColumnTitle');
        // Reset form
        form.value = {
          id: "",
          dataElemId: "",
          cnName: "", // Use optional chaining operator
          engName: "",
          columnType: "",
          columnLength: "",
          pkFlag: "0", // Set default value
          authorityDept: null,
          modelComment: "",
          nullableFlag: "0", // Set default value
          defaultValue: "",
          columnScale: "",
          modelId: props.data?.id, // Save model ID
        };
      }
    }
  },
  { immediate: true } // Add an immediate attribute to ensure that it is executed once when the component is mounted.
);
let DpData = ref([]);
const handlePkFlagChange = (value) => {
  if (value == 1) {
    form.value.nullableFlag = "1";
  }
};
const getDpDataElem = async () => {
  try {
    const response = await getDpDataElemList();
    DpData.value = response.data;
    console.log("DpData", DpData.value);
  } catch (error) {
    console.error("Request failed:", error);
  }
};
const handleDatasourceChange = (value) => {
  const selectedDatasource = DpData.value.find((item) => item.id === value);
  if (selectedDatasource) {
    form.value.dataElemName = selectedDatasource.name;
    form.value.cnName = selectedDatasource.name;
    form.value.engName = selectedDatasource.engName;
    form.value.columnType = selectedDatasource.columnType;
  }
};
// Define the interface for sending events to the parent component
const emit = defineEmits(["update:dialogFormVisible", "confirm"]);

// Handle pop-up window display status
const localVisible = computed({
  get() {
    return props.visible;
  },
  set(value) {
    emit("update:dialogFormVisible", value);
  },
});

// Form data and validation rules
const form = ref({
  id: "",
  dataElemId: "",
  cnName: "",
  engName: "",
  columnType: "",
  columnLength: "1",
  pkFlag: "",
  authorityDept: null,
  modelComment: "",
  nullableFlag: "",
  defaultValue: "",
  columnScale: "",
});

const rules = ref({
  cnName: [{ required: true, message: td('dp.dataElem.nameZhRequired'), trigger: "blur" }],
  engName: [
    { required: true, message: td('dp.dataElem.nameEnRequired'), trigger: "blur" },
    {
      pattern: /^[A-Za-z][A-Za-z0-9_]*$/,
      message: td('dp.modelForm.tableNamePattern'),
      trigger: "blur",
    },
  ],
  columnType: [
    { required: true, message: td('dp.modelForm.dataTypeRequired'), trigger: "blur" },
  ],
  defaultValue: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback();
          return;
        }

        // Remove the leading and trailing single quotes and then calculate the length.
        let actualValue = value;
        if (value.startsWith("'") && value.endsWith("'")) {
          actualValue = value.slice(1, -1);
        }

        // For numeric types, check the numeric length
        if (
          ["NUMBER", "DECIMAL", "NUMERIC", "FLOAT", "DOUBLE"].includes(
            form.value.columnType
          )
        ) {
          const numStr = actualValue.toString().replace(".", ""); // Remove the decimal point and calculate the length
          if (numStr.length > form.value.columnLength) {
            callback(
              new Error(td('dp.modelForm.defaultLengthError').replace('<length>', form.value.columnLength))
            );
            return;
          }
        }
        // For character types, check the string length directly
        else if (actualValue.length > form.value.columnLength) {
          callback(
            new Error(td('dp.modelForm.defaultLengthError').replace('<length>', form.value.columnLength))
          );
          return;
        }
        callback();
      },
      trigger: ["blur", "change"],
    },
  ],
});

// Add a listener for attribute length changes
watch(
  () => form.value.columnLength,
  (newVal) => {
    // When the attribute length changes, trigger the verification of the default value
    if (form.value.defaultValue) {
      proxy.$refs["dpModelRefs"]?.validateField("defaultValue");
    }
  }
);

// Close dialog
const closeDialog = () => {
  proxy.resetForm("dpModelRefs");
  localVisible.value = false;
  form.value = {
    id: "",
    dataElemId: "",
    cnName: "",
    engName: "",
    columnType: "",
    columnLength: "1",
    pkFlag: "",
    authorityDept: null,
    modelComment: "",
    nullableFlag: "",
    defaultValue: "",
    columnScale: "",
  };
};
// Convert input value to uppercase
const convertToUpperCase = (key, value) => {
  const uppercasedValue = value.replace(/[a-z]/g, (char) => char.toUpperCase());

  form.value[key] = uppercasedValue;

  console.log("🚀 ~ convertToUpperCase ~ form.value[key]:", form.value[key]);
};

// Confirm action
const confirmDialog = () => {
  proxy.$refs["dpModelRefs"].validate((valid) => {
    if (valid) {
      emit("confirm", form.value);
      closeDialog();
    } else {
      console.log("Form validation failed");
    }
  });
};
</script>

