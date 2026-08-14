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
    :draggable="true"
    class="medium-dialog"
    :title="form.taskParams.typeName"
    showCancelButton
    :show-close="false"
    destroy-on-close
  >
    <el-form
      ref="dpModelRefs"
      :model="form"
      label-width="140px"
      @submit.prevent
      v-loading="loading"
      :disabled="info"
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.nodeName', 'Node Name')"
            prop="name"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.nodeNameRequired',
                  'Please enter node name'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.name"
              :placeholder="
                td('dpp.integration.nodeNamePlaceholder', 'Please enter node name')
              "
            />
            <div v-else class="form-readonly">{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.type', 'Type')"
            prop="taskParams.typeName"
           :label-position="labelPosition">
            <template v-if="!info">
              <el-select
                v-model="form.taskParams.typeName"
                :placeholder="
                  td('dpp.integration.typePlaceholder', 'Please enter type')
                "
                filterable
                disabled
              >
                <el-option
                  v-for="dict in typeList"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
            <div v-else class="form-readonly">
              {{ form.taskParams.typeName }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">{{
          td("dpp.integration.constantFields", "Constant Fields")
        }}</span>
      </el-divider>
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAddField">
              <i class="iconfont-mini icon-xinzeng mr5"></i
              >{{ td("common.button.add") }}
            </el-button>
          </el-col>
        </el-row>
      </div>
      <el-table
        stripe
        height="500px"
        :data="tableFields"
        v-loading="loadingList"
      >
        <el-table-column
          :label="td('common.display.index', 'Index')"
          type="index"
          width="80"
          align="left"
        />

        <el-table-column
          :label="td('dpp.integration.fieldName', 'Field Name')"
          align="left"
          prop="columnName"
        >
          <template #default="scope">
            <el-input
              v-model="scope.row.columnName"
              :placeholder="td('common.form.namePlaceholder', 'Please enter name')"
              :class="{ 'field-input-error': getFieldError(scope.$index, 'columnName') }"
              :title="getFieldError(scope.$index, 'columnName')"
              style="width: 100%"
            />
          </template>
        </el-table-column>

        <el-table-column
          :label="td('dpp.integration.fieldType', 'Field Type')"
          align="left"
          prop="type"
          width="150"
        >
          <template #default="scope">
            <el-select
              v-model="scope.row.type"
              :placeholder="td('common.form.statusPlaceholder', 'Please select status')"
              style="width: 100%"
              @change="validateConstantFields"
            >
              <el-option
                v-for="dict in columntype"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column
          :label="td('dpp.integration.defaultValueLabel', 'Default Value')"
          align="left"
          prop="defaultValue"
        >
          <template #default="scope">
            <el-input
              v-model="scope.row.defaultValue"
              :placeholder="td('common.form.namePlaceholder', 'Please enter name')"
              :class="{ 'field-input-error': getFieldError(scope.$index, 'defaultValue') }"
              :title="getFieldError(scope.$index, 'defaultValue')"
              style="width: 100%"
              @input="validateConstantFields"
            />
          </template>
        </el-table-column>

        <el-table-column
          :label="td('dpp.integration.setEmptyString', 'Set to Empty String')"
          align="left"
          prop="emptyString"
          width="150"
        >
          <template #header>
            <div class="justify-center">
              <span>{{
                td("dpp.integration.setEmptyString", "Set to Empty String")
              }}</span>
              <el-tooltip
                effect="dark"
                :content="td('dpp.integration.setEmptyStringTooltip')"
                placement="top"
              >
                <el-icon>
                  <InfoFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <template #default="scope">
            <el-select
              v-model="scope.row.emptyString"
              :placeholder="td('common.form.statusPlaceholder', 'Please select status')"
              style="width: 100%"
              @change="handleEmptyStringChange(scope.row)"
            >
              <el-option
                :label="td('dpp.integration.yes', 'Yes')"
                :value="true"
              />
              <el-option
                :label="td('dpp.integration.no', 'No')"
                :value="false"
              />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column
          :label="td('common.texts.operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="120"
          v-if="!info"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              >{{ td("common.button.delete") }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{
          td("common.button.close")
        }}</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">{{
          td("common.button.save")
        }}</el-button>
      </div>
    </template>
  </el-dialog>

  <FieldConflictDialog
    v-model="showConflictDialog"
    :existingFields="tableFields"
    :newFields="inputFields"
    @resolve="onResolveFields"
  />
  <CreateEditModal
    :visibleDialogs="opens"
    @update:visibleDialogs="opens = $event"
    @confirm="submitForm"
    :row="row"
    :tableFields="tableFields"
    :inputFields="inputFields"
  />
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import CreateEditModal from "../fieldMergeModal.vue";
import FieldConflictDialog from "../fieldDetection.vue";
import {
  defineProps,
  defineEmits,
  ref,
  computed,
  watch,
  getCurrentInstance,
} from "vue";

import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
import { typeList } from "@/utils/graph.js";
import useUserStore from "@/store/system/user.js";
import { createNodeSelect } from "@/views/dpp/utils/opBase.js";
import { hasDuplicateObjects } from "@/utils/index.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const columntype = [
  { value: "BigNumber", label: "BigNumber" },
  { value: "Binary", label: "Binary" },
  { value: "Boolean", label: "Boolean" },
  { value: "Date", label: "Date" },
  { value: "Integer", label: "Integer" },
  { value: "Internet Address", label: "Internet Address" },
  { value: "Number", label: "Number" },
  { value: "String", label: "String" },
  { value: "Timestamp", label: "Timestamp" },
];
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: { type: Object, default: () => ({}) },
  taskType: { type: String, default: "" },
});

function handleAddField() {
  // If any existing field columnName is empty, prevent new additions
  const lastEmpty = tableFields.value.find((item) => !item.columnName);
  if (lastEmpty) {
    proxy.$message.warning(
      td(
        "dpp.integration.addFieldFailedFillCurrent",
        "Add failed, please fill current field name first"
      )
    );
    return;
  }
  // last line name
  let isRepeat = hasDuplicateObjects(tableFields.value, "columnName");
  if (isRepeat) {
    proxy.$message.warning(
      td("dpp.integration.noRepeatFieldNames", "Please do not use duplicate field names")
    );
    return;
  }

  tableFields.value.push({
    columnName: null,
    type: "String",
    defaultValue: null,
    emptyString: false,
    source: form.value.name,
  });
  validateConstantFields();
}

const showConflictDialog = ref(false);

function onResolveFields(payload) {
  if (!payload) return;
  switch (payload.action) {
    case "addNewOnly":
      console.log("Parent component: add new fields only");
      break;
    case "addAll":
      console.log("Parent component: add all fields");
      break;
    case "clearAndAddAll":
      tableFields.value = deepCopy(originalTableFieldsBackup.value);
      console.log(
        "🚀 ~ onResolveFields ~  tableFields.value:",
        tableFields.value
      );
      console.log("Parent component: clear and add all fields");
      break;
    case "cancel":
      console.log("Parent component: cancel operation");
      break;
  }
}

const emit = defineEmits(["update", "confirm"]);

const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update", newValue);
  },
});

let tableFields = ref([]);
let originalTableFieldsBackup = ref([]);
let inputFields = ref([]);
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let dpModelRefs = ref();
let form = ref({});
let fieldErrors = ref([]);
let showFieldErrors = ref(false);

function isValidDateValue(value, includeTime = false) {
  const pattern = includeTime
    ? /^(\d{4})-(\d{2})-(\d{2})[ T](\d{2}):(\d{2})(?::(\d{2})(?:\.\d{1,3})?)?$/
    : /^\d{4}-\d{2}-\d{2}$/;
  const match = value.match(pattern);
  if (!match) return false;
  const dateParts = value.slice(0, 10).split("-").map(Number);
  const [year, month, day] = dateParts;
  const date = new Date(Date.UTC(year, month - 1, day));
  const validDate = date.getUTCFullYear() === year &&
    date.getUTCMonth() === month - 1 && date.getUTCDate() === day;
  if (!validDate || !includeTime) return validDate;
  const [, , , , hour, minute, second = "0"] = match;
  return Number(hour) <= 23 && Number(minute) <= 59 && Number(second) <= 59;
}

function isValidInternetAddress(value) {
  const ipv4 = /^(?:(?:25[0-5]|2[0-4]\d|1?\d?\d)\.){3}(?:25[0-5]|2[0-4]\d|1?\d?\d)$/;
  const ipv6 = /^(?:[\da-f]{1,4}:){2,7}[\da-f]{0,4}$/i;
  return ipv4.test(value) || ipv6.test(value);
}

function isDefaultValueCompatible(item) {
  const value = String(item.defaultValue ?? "").trim();
  if (item.emptyString) return item.type === "String";
  if (item.type === "String") return true;
  if (!value) return true;
  switch (item.type) {
    case "Integer":
      return /^[+-]?\d+$/.test(value);
    case "BigNumber":
      return /^[+-]?(?:\d+(?:\.\d+)?|\.\d+)$/.test(value);
    case "Number":
      return Number.isFinite(Number(value));
    case "Boolean":
      return /^(true|false|0|1)$/i.test(value);
    case "Date":
      return isValidDateValue(value);
    case "Timestamp":
      return isValidDateValue(value, true);
    case "InternetAddress":
      return isValidInternetAddress(value);
    case "Binary":
      return /^(?:[A-Za-z\d+/]{4})*(?:[A-Za-z\d+/]{2}==|[A-Za-z\d+/]{3}=)?$/.test(value);
    default:
      return false;
  }
}

function validateConstantFields() {
  const upstreamNames = new Set(
    inputFields.value
      .map((item) => String(item.columnName ?? "").trim().toLowerCase())
      .filter(Boolean)
  );
  fieldErrors.value = tableFields.value.map((item) => {
    const errors = {};
    const fieldName = String(item.columnName ?? "").trim();
    if (fieldName && upstreamNames.has(fieldName.toLowerCase())) {
      errors.columnName = td(
        "dpp.integration.constantFieldNameConflictsUpstream",
        "The constant field name duplicates an upstream field name."
      );
    }
    if (item.type && !isDefaultValueCompatible(item)) {
      const message = td(
        "dpp.integration.constantDefaultValueTypeMismatch",
        "Default value does not match the field type. Please modify it."
      );
      errors.defaultValue = message;
    }
    return errors;
  });
  return fieldErrors.value.every((errors) => Object.keys(errors).length === 0);
}

const getFieldError = (index, field) =>
  showFieldErrors.value ? fieldErrors.value[index]?.[field] || "" : "";

function getConstantValidationMessage() {
  for (const errors of fieldErrors.value) {
    if (errors.columnName) return errors.columnName;
  }
  for (const errors of fieldErrors.value) {
    if (errors.defaultValue) return errors.defaultValue;
  }
  return "";
}

function handleEmptyStringChange() {
  validateConstantFields();
}

function handleDelete(row) {
  // Delete the corresponding field from tableFields
  const idxTable = tableFields.value.findIndex(
    (item) => item.columnName === row.columnName
  );
  if (idxTable !== -1) {
    tableFields.value.splice(idxTable, 1);
  } else {
    proxy.$message.warning(
      td("dpp.integration.deleteFailedFieldNotFound", "Delete failed, field not found")
    );
  }

  // Restore the original state of deleted fields in inputFields (if any)
  const originalField = originalTableFieldsBackup.value.find(
    (item) => item.columnName === row.columnName
  );
  if (originalField) {
    const idxField = inputFields.value.findIndex(
      (item) => item.columnName === row.columnName
    );
    if (idxField !== -1) {
      inputFields.value[idxField] = deepCopy(originalField);
    } else {
      inputFields.value.push(deepCopy(originalField));
    }
  }
  validateConstantFields();
}

// Submit pop-up rule data
const submitForm = (value) => {
  if (!value || !Array.isArray(value)) return;

  value.forEach((ruleItem) => {
    if (!ruleItem?.ruleConfig) return;

    let parsedConfig;
    try {
      parsedConfig = JSON.parse(ruleItem.ruleConfig);
    } catch (e) {
      console.warn("Unable to parse ruleConfig:", e, ruleItem.ruleConfig);
      return;
    }
    const sourceField = parsedConfig?.fieldMerge?.sourceField;
    if (!sourceField) return;

    const tableIndex = tableFields.value.findIndex(
      (item) => item.columnName == sourceField
    );
    if (tableIndex !== -1) {
      const updatedItem = {
        ...tableFields.value[tableIndex],
        cleanRuleList: [ruleItem],
        elementId: [ruleItem.ruleId],
      };
      tableFields.value[tableIndex] = updatedItem;

      const fieldIndex = inputFields.value.findIndex(
        (item) => item.columnName == sourceField
      );
      if (fieldIndex !== -1) {
        inputFields.value[fieldIndex] = updatedItem;
      } else {
        inputFields.value.push(updatedItem);
      }
    }
  });
  opens.value = false;
};

const off = () => {
  proxy.resetForm("dpModelRefs");
  tableFields.value = [];
  originalTableFieldsBackup.value = [];
  form.value = {};
  row.value = {};
  fieldErrors.value = [];
  showFieldErrors.value = false;
};

const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;

    // Verify tableFields is not empty
    if (!Array.isArray(tableFields.value) || tableFields.value.length === 0) {
      proxy.$message.warning(
        td("dpp.integration.atLeastOneFieldValue", "At least one field value is required")
      );
      return;
    }

    if (tableFields.value.length > 0) {
      const hasEmptyName = tableFields.value.some(
        (item) => !item.columnName?.trim()
      );
      if (hasEmptyName) {
        proxy.$message.warning(
          td(
            "dpp.integration.fieldNameCannotBeEmpty",
            "Validation failed, field name is required"
          )
        );
        return;
      }
    }

    // last line name
    let isRepeat = hasDuplicateObjects(tableFields.value, "columnName");
    if (isRepeat) {
      proxy.$message.warning(
        td("dpp.integration.noRepeatFieldNames", "Please do not use duplicate field names")
      );
      return;
    }

    showFieldErrors.value = true;
    if (!validateConstantFields()) {
      proxy.$message.warning(
        getConstantValidationMessage()
      );
      return;
    }

    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      });
      loading.value = false;
      form.value.code = response.data;
    }

    const taskParams = form.value.taskParams || {};
    const splitField = form.value.taskParams.splitField;
    const inputWithoutSplit = inputFields.value.filter(
      (item) => item.columnName !== splitField
    );
    taskParams.outputFields = [
      ...inputWithoutSplit,
      ...tableFields.value.map((item) => ({ ...item, columnType: item.type })),
    ];
    taskParams.tableFields = tableFields.value.map((field) => ({
      ...field,
      name: field.columnName,
    }));
    taskParams.mainArgs = taskParams.mainArgs || { cleanRuleList: [] };
    form.value.taskParams = taskParams;
    console.log("Save data - outputFields:", taskParams.outputFields);
    emit("confirm", form.value);
    // closeDialog();
  } catch (error) {
    console.error("Failed to save data:", error);
    loading.value = false;
  }
};

const closeDialog = () => {
  off();
  emit("update", false);
};

function deepCopy(data) {
  if (data === undefined || data === null) {
    return {};
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    console.log(e, "deepCopy error");
    return {};
  }
}

let nodeOptions = ref([]);
watch(
  // Dialog state is initialized only when it opens or switches to another node.
  // Watching currentNode.data here would overwrite rows added by the user when
  // X6 refreshes the node data object during editing.
  () => [props.visible, props.currentNode?.id],
  ([visible]) => {
    if (!visible) {
      off();
      return;
    }
    form.value = deepCopy(props.currentNode?.data || {});
    nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
    const taskParams = deepCopy(props.currentNode?.data?.taskParams || {});
    originalTableFieldsBackup.value = deepCopy(
      props.currentNode?.data?.taskParams?.tableFields || []
    );
    inputFields.value = taskParams?.inputFields || [];
    tableFields.value = taskParams?.tableFields || [];
    validateConstantFields();
  },
  { immediate: true }
);
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

:deep(.field-input-error .el-input__wrapper),
:deep(.field-input-error .el-select__wrapper) {
  box-shadow: 0 0 0 1px var(--el-color-danger) inset;
}
</style>
