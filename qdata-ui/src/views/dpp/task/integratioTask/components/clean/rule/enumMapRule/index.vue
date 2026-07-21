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
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <div v-loading="loadingList">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <template v-if="!falg">
            <el-col :span="1.5">
              <el-button
                type="primary"
                icon="Plus"
                @click="opencodeDialog(undefined)"
                >{{ td('dpp.cleanRule.addRule', 'Add Rule') }}</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="primary"
                icon="Plus"
                @click="showDialog(undefined)"
                >{{ td('dpp.cleanRule.importRule', 'Import Rule') }}</el-button
              >
            </el-col>
          </template>
        </el-row>
      </div>
      <el-table stripe :data="form.stringValue" v-loading="loading">
        <el-table-column :label="td('dpp.cleanRule.originalValue', 'Original Value')" align="left" prop="value">
          <template #default="scope">
            <template v-if="!falg">
              <el-input
                v-model="scope.row.value"
                style="width: 100%"
                :placeholder="td('dpp.cleanRule.inputOriginalValue', 'Please enter original value')"
              />
            </template>
            <div v-else class="form-readonly">{{ scope.row.value || "-" }}</div>
          </template>
        </el-table-column>

        <el-table-column :label="td('dpp.cleanRule.standardValue', 'Standard Value')" align="left" prop="name">
          <template #default="scope">
            <template v-if="!falg">
              <el-input
                v-model="scope.row.name"
                style="width: 100%"
                :placeholder="td('dpp.cleanRule.inputStandardValue', 'Please enter standard value')"
              />
            </template>
            <div v-else class="form-readonly">{{ scope.row.name || "-" }}</div>
          </template>
        </el-table-column>

        <el-table-column
          v-if="!falg"
          :label="td('common.texts.operation', 'Operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="150"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.$index + 1)"
              >{{ td('common.button.delete', 'Delete') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-row> </el-row>
    <singleSelectTableDialog ref="dialogRef" @confirm="handleConfirm" />
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { reactive, ref, watch } from "vue";
import {
  listDpDataElem,
  listDpDataElemCode,
} from "@/api/dp/dataElem/dataElem.js";
import singleSelectTableDialog from "./dataElem.vue";

const { td } = useDefaultLang();
const props = defineProps({
  form: Object,
  inputFields: Array,
  falg: Boolean,
});
let loadingList = ref(false);
const emit = defineEmits(["update:form"]);
let loading = ref(false);
const formRef = ref(null);
const { proxy } = getCurrentInstance();
const form = reactive({ ...props.form });
let dpDataElemstringValue = ref([]);
let dpDataElemList = ref([]);

const dialogRef = ref();

function showDialog() {
  dialogRef.value.openDialog(td('dpp.cleanRule.selectDataElem', 'Select Data Element'));
}

function handleConfirm(row, list) {
  console.log("Selected row:", row);
  dpDataElemstringValue.value = [];

  loadCodeItemsByTableId(row.id);
}
function loadCodeItemsByTableId(id) {
  if (!id || id == -1) return;
  loading.value = true;
  listDpDataElemCode({
    pageNum: 1,
    pageSize: 999,
    dataElemId: id,
    ruleType: 2,
  }).then((res) => {
    // dpDataElemstringValue.value = res.data.rows;
    form.stringValue = (res?.data?.rows || []).map(
      ({ codeValue, codeName, ...rest }) => ({
        ...rest,
        value: codeValue ?? "",
        name: codeName ?? "",
      })
    );
    loading.value = false;
  });
}
function handleDelete(index) {
  form.stringValue.splice(Number(index) - 1, 1);
}
function opencodeDialog() {
  const hasIncomplete = (form.stringValue || []).some(
    (item) => !item.value || !item.name
  );

  if (hasIncomplete) {
    ElMessage.warning(td('dpp.cleanRule.fillAllItems', 'Please fill all items first'));
    return;
  }

  if (!Array.isArray(form.stringValue)) {
    form.stringValue = [];
  }

  // Add a new row of empty data
  form.stringValue.push({
    value: "",
    name: "",
  });
}

function loadCodeTableList() {
  listDpDataElem({
    pageNum: 1,
    pageSize: 999,
    type: "2",
  })
    .then((res) => {
      dpDataElemList.value = res.data.rows;
      loading.value = false;
    })
    .catch(() => {
      loading.value = false;
    });
}
function handleUseCodeTableChange(val) {
  if (val == "1") {
    loadCodeTableList();
  } else {
    form.codeTableId = "";
    form.stringValue = [];
    dpDataElemList.value = [];
  }
}

onMounted(() => {
  if (form.useCodeTable === "1" && form.codeTableId) {
    handleUseCodeTableChange("1", true);
  }
});
function checkValueAndName(list) {
  if (!list || list.length === 0) {
    return { formIsValid: false, message: td('dpp.cleanRule.atLeastOneRule', 'At least one rule data is required!') };
  }
  const values = [];
  const names = [];
  for (const item of list) {
    const v = item.value?.trim();
    const n = item.name?.trim();
    if (!v || !n) {
      return { formIsValid: false, message: td('dpp.cleanRule.originalAndStandardRequired', 'Original value and standard value are required!') };
    }
    values.push(v);
    names.push(n);
  }

  const hasDuplicate = (arr) =>
    arr.some((val, idx) => arr.indexOf(val) !== idx);

  if (hasDuplicate(values)) {
    return { formIsValid: false, message: td('dpp.cleanRule.originalValueDuplicate', 'Original value must be unique!') };
  }
  if (hasDuplicate(names)) {
    return { formIsValid: false, message: td('dpp.cleanRule.standardValueDuplicate', 'Standard value must be unique!') };
  }

  return { formIsValid: true, message: "" };
}

function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (!valid) {
        resolve({ valid: false });
        return;
      }
      const { formIsValid, message } = checkValueAndName(form.stringValue);
      if (!formIsValid) {
        proxy.$message.warning(message);
        resolve({ valid: false });
        return;
      }
      const result = {
        stringValue: form.stringValue,
      };

      resolve({ valid: true, data: result });
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
</style>
