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
  <!-- Time field sequence verification -->
  <el-form ref="formRef" :model="form" :disabled="falg">
    <el-form-item label="">
      <div
        class="field-line"
        style="display: flex; align-items: center; flex-wrap: wrap; gap: 8px"
      >
        <el-select
          v-if="form.conditions.length > 0"
          v-model="form.conditions[0].leftField"
          :placeholder="td('da.qualityTaskRules.ruleCommon.fieldPlaceholder')"
          style="width: 120px"
          @change="onLeftFieldChange($event, 0)"
        >
          <el-option
            v-for="col in timeColumns"
            :key="col.columnName"
            :label="col.columnName"
            :value="col.columnName"
          />
        </el-select>
        <div v-else style="width: 120px"></div>
        <template v-for="(cond, index) in form.conditions" :key="index">
          <el-select
            v-model="cond.operator"
            :placeholder="td('da.qualityTaskRules.ruleCommon.operatorPlaceholder')"
            style="width: 50px"
          >
            <el-option label="<" value="<" />
            <el-option label="≤" value="<=" />
          </el-select>

          <el-select
            v-model="cond.rightField"
            :placeholder="td('da.qualityTaskRules.ruleCommon.fieldPlaceholder')"
            style="width: 120px"
          >
            <el-option
              v-for="col in timeColumns"
              :key="col.columnName"
              :label="col.columnName"
              :value="col.columnName"
            />
          </el-select>
          <el-button
            v-if="!falg && index === form.conditions.length - 1"
            icon="Delete"
            type="danger"
            circle
            @click="removeLastGroup"
            :disabled="form.conditions.length === 0"
          />
        </template>

        <!-- Add button -->
        <el-button
          v-if="!falg"
          icon="Plus"
          type="primary"
          circle
          @click="addGroup"
        />
      </div>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { reactive, ref, computed, watch, onMounted } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();

const props = defineProps({
  form: Object,
  falg: Boolean,
  columnList: Array,
});
const emit = defineEmits(["update:form"]);

const formRef = ref(null);

const form = reactive({
  conditions: props.form.conditions?.length
    ? JSON.parse(JSON.stringify(props.form.conditions))
    : [{ leftField: "", operator: "<", rightField: "" }],
});

const columnList = ref([]);

const timeColumns = computed(() =>
  columnList.value.filter((col) => {
    console.log("🚀 ~  columnList.value:", columnList.value);
    if (!col.columnType) return false;
    const type = col.columnType.toUpperCase();
    return (
      type == "DATE" ||
      type.startsWith("TIMESTAMP") ||
      type == "TIME" ||
      type == "YEAR" ||
      type == "DATETIME"
    );
  })
);

// To splice the selected time field, refer to the implementation of evaColumn
const timeOrderFields = computed(() => {
  if (!form.conditions || form.conditions.length === 0) return "";
  // Get all unique field names
  const fieldNames = new Set();
  form.conditions.forEach((cond) => {
    if (cond.leftField) fieldNames.add(cond.leftField);
    if (cond.rightField) fieldNames.add(cond.rightField);
  });
  // Convert to array and sort
  const fieldsArray = Array.from(fieldNames);
  // Use label in columnList to display, if not, display columnName
  const map = new Map(
    (columnList.value || []).map((c) => [c.columnName, c.label || c.columnName])
  );
  return fieldsArray.map((field) => map.get(field) || field).join(", ");
});

watch(
  () => props.columnList,
  (newVal) => {
    columnList.value = newVal || [];
  },
  { immediate: true }
);

function addGroup() {
  form.conditions.push({ leftField: "", operator: "<", rightField: "" });
}

function removeLastGroup() {
  if (form.conditions.length > 1) {
    form.conditions.pop();
  }
}

function onLeftFieldChange(value, index) {
  form.conditions[0].leftField = value;
  for (let i = 1; i < form.conditions.length; i++) {
    form.conditions[i].leftField = form.conditions[i - 1].rightField;
  }
}

watch(
  () => form.conditions.map((c) => c.rightField),
  () => {
    for (let i = 1; i < form.conditions.length; i++) {
      form.conditions[i].leftField = form.conditions[i - 1].rightField;
    }
  }
);

function validateCalculationGroups() {
  if (form.conditions.length === 0) {
    ElMessage.warning(td('da.qualityTaskRules.ruleCommon.conditionGroupRequired'));
    return false;
  }
  for (let i = 0; i < form.conditions.length; i++) {
    const group = form.conditions[i];
    if (!group.leftField) {
      ElMessage.warning(td('da.qualityTaskRules.ruleCommon.leftFieldRequired', { i: i + 1 }));
      return false;
    }
    if (!group.operator || !["<", "<="].includes(group.operator)) {
      ElMessage.warning(td('da.qualityTaskRules.ruleCommon.operatorInvalid', { i: i + 1 }));
      return false;
    }
    if (!group.rightField) {
      ElMessage.warning(td('da.qualityTaskRules.ruleCommon.rightFieldRequired', { i: i + 1 }));
      return false;
    }
  }
  return true;
}

function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (!valid) {
        ElMessage.warning(td('da.qualityTaskRules.ruleCommon.formIncomplete'));
        resolve({ valid: false });
        return;
      }
      if (!validateCalculationGroups()) {
        resolve({ valid: false });
        return;
      }

      // Get all unique field names
      const fieldNames = new Set();
      form.conditions.forEach((cond) => {
        if (cond.leftField) fieldNames.add(cond.leftField);
        if (cond.rightField) fieldNames.add(cond.rightField);
      });

      // Convert to array
      const fieldsArray = Array.from(fieldNames);

      resolve({
        valid: true,
        data: {
          conditions: JSON.parse(JSON.stringify(form.conditions)),
          evaColumn: fieldsArray, // Directly returns the field array for assignment to the evaColumn of the parent component.
        },
      });
    });
  });
}

defineExpose({ validate });
</script>

<style scoped>
.field-line {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}
</style>
