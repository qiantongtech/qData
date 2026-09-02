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
  <!-- Cleaning rules basic page   -->
  <el-dialog
    v-model="dialogVisible"
    draggable
    class="medium-dialog"
    :title="dialogTitle"
    destroy-on-close
    :append-to="$refs['app-container']"
  >
    <div class="content" v-if="dialogStatus == 0">
      <SideMenu
        :dialogStatus="dialogStatus"
        @card-click="handleCardClick"
        ref="SideMenus"
        :type="type"
      />
    </div>
    <div
      class="content form-content"
      v-show="dialogStatus == 1 || dialogStatus == 2"
      :disabled="dialogStatus == 2"
    >
      <el-form ref="formRef" :model="form" label-width="130px" :label-position="labelPosition">
        <div class="h2-title">{{ td('dpp.cleanRule.basicInfo', 'Basic Info') }}</div>
        <el-row>
          <el-col :span="8">
            <el-form-item
              :label="td('dpp.cleanRule.cleanName', 'Clean Name')"
              prop="name"
              :rules="
                !falg
                  ? [
                      {
                        required: true,
                        message: td('dpp.cleanRule.inputCleanName', 'Please enter clean name'),
                        trigger: 'blur',
                      },
                      { pattern: /^(?!\s*$).+/, message: td('dpp.cleanRule.noEmptySpace', 'Clean name cannot be empty spaces'), trigger: 'blur' }
                    ]
                  : []
              "
              :label-position="labelPosition"
            >
              <el-input
                v-if="!falg"
                v-model="form.name"
                :placeholder="td('dpp.cleanRule.inputCleanName', 'Please enter clean name')"
              />
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="td('dpp.cleanRule.cleanRuleCode', 'Clean Rule Code')" prop="ruleCode" :label-position="labelPosition">
              <el-input
                v-if="!falg"
                v-model="form.ruleCode"
                :placeholder="td('dpp.cleanRule.inputCleanRuleCode', 'Please enter clean rule code')"
                disabled
              />
              <div v-else class="form-readonly">{{ form.ruleCode || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="td('dpp.cleanRule.cleanRuleName', 'Clean Rule Name')" prop="ruleName" :label-position="labelPosition">
              <el-input
                v-if="!falg"
                v-model="form.ruleName"
                :placeholder="td('dpp.cleanRule.inputCleanRuleName', 'Please enter clean rule name')"
                disabled
              />
              <div v-else class="form-readonly">{{ form.ruleName || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
              <el-radio-group v-model="form.status" :disabled="falg">
                <el-radio :value="'1'">{{ td('dpp.cleanRule.online', 'Online') }}</el-radio>
                <el-radio :value="'0'">{{ td('dpp.cleanRule.offline', 'Offline') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('dpp.cleanRule.ruleDesc', 'Rule Description')" prop="ruleDesc" :label-position="labelPosition">
              <el-input
                v-if="!falg"
                type="textarea"
                maxlength="256字符"
                show-word-limit
                v-model="form.ruleDesc"
                :placeholder="td('dpp.cleanRule.inputRuleDesc', 'Please enter rule description')"
              />
              <div v-else class="form-readonly textarea">
                {{ form.ruleDesc ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('dpp.cleanRule.whereConditionLabel', 'Where Condition')" prop="whereClause" :label-position="labelPosition">
              <el-input
                v-if="!falg"
                type="textarea"
                maxlength="256字符"
                show-word-limit
                v-model="form.whereClause"
                :placeholder="td('dpp.cleanRule.inputWhereCondition', 'Please enter where condition')"
              />
              <div v-else class="form-readonly textarea">
                {{ form?.whereClause ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- Rule configuration -->
        <div class="h2-title">{{ td('dpp.cleanRule.ruleConfig', 'Rule Config') }}</div>
        <el-row v-if="type != 3">
          <el-col :span="24">
            <el-form-item
              :label="td('dpp.cleanRule.cleanField', 'Clean Field')"
              prop="columns"
              :rules="
                !falg
                  ? [
                      {
                        required: true,
                        message: td('dpp.cleanRule.selectCleanField', 'Please select clean field'),
                        trigger: 'blur',
                      },
                    ]
                  : []
              "
              :label-position="labelPosition"
            >
              <template v-if="!falg">
                <el-select
                  v-if="isMultipleSelect"
                  v-model="form.columns"
                  :placeholder="td('dpp.cleanRule.selectCleanField', 'Please select clean field')"
                  multiple
                  clearable
                >
                  <el-option
                    v-for="dict in cleanFieldOptions"
                    :key="dict.columnName"
                    :label="dict.label"
                    :value="dict.columnName"
                    :disabled="shouldDisableField(dict)"
                  />
                </el-select>
                <el-select
                  v-else
                  v-model="form.columns"
                  :placeholder="td('dpp.cleanRule.selectCleanField', 'Please select clean field')"
                  clearable
                >
                  <el-option
                    v-for="dict in cleanFieldOptions"
                    :key="dict.columnName"
                    :label="dict.label"
                    :value="dict.columnName"
                    :disabled="shouldDisableField(dict)"
                  />
                </el-select>
              </template>
              <div v-else class="form-readonly">{{ columnsDisplayText }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <component
          :is="currentRuleComponent"
          ref="ruleComponentRef"
          :form="form.ruleConfig"
          :inputFields="processedFields"
          :falg="falg"
          :columnList="columnList"
        />
      </el-form>
    </div>
    <template #footer>
      <template v-if="dialogStatus == 1"
        ><el-button type="primary" @click="handleSave" v-if="!falg"
          >{{ td('common.button.confirm') }}</el-button
        >
        <el-button @click="handleBack" v-if="!mode">{{ td('common.button.return') }}</el-button>
        <!-- <el-button type="warning" @click="handleSpotCheck">Preview</el-button> -->
      </template>
      <el-button @click="closeDialog" v-else>{{ td('common.button.close') }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import SideMenu from "./ruleSelectorMenu.vue";
import { getRuleConfig, getRuleComponent } from "./registry.js";
import { isNumericColumnType, isTextColumnType, validateWhereCondition } from "../../../utils/foolproof.js";

import moment from "moment";

const { td } = useDefaultLang();
let falg = ref(false);
const { proxy } = getCurrentInstance();
const { quality_warning_status } = proxy.useDict("quality_warning_status");
const emit = defineEmits(["confirm"]);
// The parent component passes in the evaluation object list
const props = defineProps({
  inputFields: {
    type: Array,
    default: () => [],
  },
  type: {
    type: String,
    default: "",
  },
});

const { inputFields } = toRefs(props);
const processedFields = computed(() => {
  return inputFields.value.map((item) => ({
    ...item,
    label: item.columnComment
      ? `${item.columnName} / ${item.columnComment}`
      : item.columnName,
  }));
});

const textFieldRuleCodes = ["021", "022"];
const isTextField = (dict) => {
  const type = dict.columnType?.toUpperCase() || "";
  return (
    type.includes("CHAR") ||
    type.includes("TEXT") ||
    type.includes("VARCHAR") ||
    type.includes("STRING")
  );
};
const cleanFieldOptions = computed(() => {
  if (textFieldRuleCodes.includes(form.ruleCode)) {
    return processedFields.value.filter(isTextField);
  }
  return processedFields.value;
});

const columnsDisplayText = computed(() => {
  if (isMultipleSelect.value) {
    const values = Array.isArray(form.columns) ? form.columns : [];
    const labels = values.map((v) => {
      const f = processedFields.value.find((d) => d.columnName === v);
      return f ? f.label : v;
    });
    return labels.length ? labels.join(", ") : "-";
  } else {
    const v = form.columns;
    if (!v) return "-";
    const f = processedFields.value.find((d) => d.columnName === v);
    return f ? f.label : v;
  }
});

const dialogVisible = ref(false);
const dialogStatus = ref(1);
const dialogTitle = ref("");
const formRef = ref();

let form = reactive({
  name: "",
  ruleName: "", // Cleaning rule name
  ruleCode: "", // Cleaning rule code
  status: "1",
  // warningLevel: "2",
  whereClause: "",
  columns: "",
  tableName: "",
  ruleDesc: "",
  type: "",
  ruleConfig: {
    // Numerical boundary adjustment
    max: "100",
    min: "0",
    handleType: "1",
    // Remove spaces from string
    handleType: "1", // "1-Remove leading and trailing spaces, 2-Remove all spaces"
    // Regular expression replacement
    pattern: "", // Expression
    replacement: "", // Replacement
    ruleValue: [],
    deduplicationStrategy: "1",
    dataRangeValue: moment().format("YYYY-MM-DD"),
    // Data added value
    stringValue: "", //Add value
    // Very long field truncation
    maxLength: "100",
    direction: "1",
    // date format
    targetFormat: "yyyy-MM-dd",
    inputFormats: [
      "yyyyMMdd",
      "yyyy-MM-dd",
      "yyyy/MM/dd",
      "yyyy.MM.dd",
      "yyyy-MM-dd HH:mm:ss",
      "timestamp",
    ],
  },
});
const isMultipleSelect = computed(() => {
  return form.ruleCode == "019" || form.ruleCode == "029";
});
// A new computed property to determine if a field should be disabled
const shouldDisableField = computed(() => {
  return (dict) => {
    // For rule 039 (Clean expired records), only date type fields are allowed
    if (
      form.ruleCode == "039" ||
      form.ruleCode == "007" ||
      form.ruleCode == "017" ||
      form.ruleCode == "038"
    ) {
      const isDateType =
        dict.columnType?.toUpperCase().includes("DATE") ||
        dict.columnType?.toUpperCase().startsWith("TIMESTAMP") ||
        dict.columnType?.toUpperCase() === "TIME" ||
        dict.columnType?.toUpperCase() === "YEAR";
      return !isDateType;
    }

    // For rule 014 (Numeric null value filling), only numeric type fields are allowed
    if (form.ruleCode == "014") {
      return !isNumericColumnType(dict.columnType);
    }

    // For rule 015 (String null value filling), only string type fields are allowed
    if (form.ruleCode == "015" || form.ruleCode == "034") {
      return !isTextColumnType(dict.columnType);
    }

    if (form.ruleCode == "001" || form.ruleCode == "008") {
      return !isNumericColumnType(dict.columnType);
    }

    if (
      ["009", "010", "011", "012"].includes(form.ruleCode) ||
      textFieldRuleCodes.includes(form.ruleCode)
    ) {
      return !isTextColumnType(dict.columnType);
    }

    return false;
  };
});

watch(
  [() => form.ruleCode, () => form.ruleName, processedFields],
  () => {
    const selectableNames = new Set(
      processedFields.value
        .filter((item) => !shouldDisableField.value(item))
        .map((item) => item.columnName)
    );
    if (isMultipleSelect.value) {
      const values = Array.isArray(form.columns) ? form.columns : [];
      form.columns = values.filter((item) => selectableNames.has(item));
      return;
    }
    if (form.columns && !selectableNames.has(form.columns)) {
      form.columns = "";
    }
  }
);
let title = ref();

// Computed property: current rule configuration
const currentRuleConfig = computed(() => {
  return getRuleConfig(form.ruleCode);
});

// Computed property: current rule component
const currentRuleComponent = computed(() => {
  return getRuleComponent(form.ruleCode) || getRuleComponent("EMPTY");
});

let loading = ref(false);
let columnList = ref([]);

let ruleComponentRef = ref();
async function handleSave() {
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning(td("dpp.cleanRule.completeRequired", "Please complete required fields"));
    return;
  }
  const whereResult = validateWhereCondition(form.whereClause);
  if (!whereResult.valid) {
    proxy.$message.warning(whereResult.message);
    return;
  }
  let res = { valid: true, data: {} };
  res = await ruleComponentRef.value?.validate();
  if (!res.valid) return;
  if (!isMultipleSelect.value) {
    form.columns = [form.columns];
  }
  if (form.ruleCode == "035") {
  }
  const formCopy = JSON.parse(
    JSON.stringify({
      ...form,
      ruleConfig: JSON.stringify({
        columns: form.columns,
        ...res.data,
        parentName: form.parentName,
      }),
    })
  );

  emit("confirm", formCopy, mode.value);
}
let sampleCheckMsg = ref();

function handleCardClick(data) {
  resetForm();
  form.ruleName = data?.name;
  form.ruleCode = data?.code;
  form.ruleType = data?.strategyKey;
  form.type = data?.type;
  form.parentName = data?.parentName;
  form.dimensionType = data?.qualityDim;
  dialogTitle.value = `${td('dpp.cleanRule.addCleanRule', 'Add Clean Rule')}${data?.name ? "-" + data.name : ""}`;
  dialogStatus.value = 1;
}
let mode = ref();
async function openDialog(record, index, fg) {
  falg.value = fg;
  mode.value = index;
  resetForm();
  dialogTitle.value = `${mode.value ? td('common.button.update') : td('common.button.add')}${td('dpp.cleanRule.cleanRulePrefix', 'Clean Rule')}${
    record?.ruleName ? `-${record.ruleName}` : ""
  }`;
  if (falg?.value) {
    dialogTitle.value = `${td('dpp.cleanRule.cleanRulePrefix', 'Clean Rule')}${
      record?.ruleName ? `-${record.ruleName}` : ""
    }`;
  }
  dialogStatus.value = mode.value ? 1 : 0;
  dialogVisible.value = true;

  if (index) {
    dialogStatus.value = 1;
    const { ruleType, ruleConfig, columns, ...rest } = record;
    Object.assign(form, rest);
    form.ruleType = ruleType;

    try {
      form.ruleConfig =
        typeof ruleConfig == "string" ? JSON.parse(ruleConfig) : ruleConfig;
    } catch (e) {
      form.ruleConfig = {};
    }
    if (isMultipleSelect.value) {
      form.columns = Array.isArray(columns) ? columns : [];
    } else {
      form.columns =
        Array.isArray(columns) && columns.length > 0 ? columns[0] : "";
    }
  } else {
    resetForm();
  }
}

const initialForm = () => ({
  id: "",
  name: "",
  type: "",
  ruleName: "", // Cleaning rule name
  ruleCode: "", // Cleaning rule code
  status: "1",
  whereClause: "",
  columns: isMultipleSelect.value ? [] : "",
  tableName: "",
  ruleDesc: "",
  ruleConfig: {
    // Numerical boundary adjustment
    max: "100",
    min: "0",
    handleType: "1",
    // Remove spaces from string
    handleType: "1", // "1-Remove leading and trailing spaces, 2-Remove all spaces"
    // Regular expression replacement
    pattern: "", // Expression
    replacement: "", // Replacement

    ruleValue: [],
    deduplicationStrategy: "1",
    // Enumeration value mapping normalization
    stringValue: [],
    dataRange: "1", // 0: Fixed time range, 1: Specific date
    dataRangeType: "1", // 0: Days ago
    dataRangeValue: moment().format("YYYY-MM-DD"),
    handleType: "1", // 0: Expiration handling method, 1: Delete records
    handleColumns: "", // Only if expiration handling method is selected, marking field exists
    handleValue: "", // Only if expiration handling method is selected, marking value exists
    // Very long field truncation
    maxLength: "0",
    direction: "1",
    // Date format
    targetFormat: "",
    inputFormats: [
      "yyyyMMdd",
      "yyyy-MM-dd",
      "yyyy/MM/dd",
      "yyyy.MM.dd",
      "yyyy-MM-dd HH:mm:ss",
      "timestamp",
    ],
    // Field value replacement
    mode: "1", // 1-whitelist, 2-blacklist
    allowed: [], // Cleaning values
    defaultValue: "", // Default value
    ignoreCase: "1", // 1-Case sensitive, 2-Case insensitive
    caseSensitive: "1", // 1-Remove spaces, 2-Do not remove spaces
    ignoreNullValue: "1", // 1-Ignore null, 2-do not ignore null
    // Date null filling
    fillType: "3", // 1=Current date, 2=Yesterday, 3=Fixed value
    defaultValue: "", // Used when fixed value fillType=3
    format: "", // Date format
    // Keyword desensitization
    keywords: [""],
    maskString: "",
  },
});

function resetForm() {
  Object.assign(form, initialForm());
  columnList.value = [];
  title.value = "";
  sampleCheckMsg.value = "";
}

function closeDialog() {
  dialogVisible.value = false;
  resetForm();
}

function handleBack() {
  dialogStatus.value = 0;
  dialogTitle.value = td('dpp.cleanRule.addCleanRule', 'Add Clean Rule');
  resetForm();
}
defineExpose({ openDialog, closeDialog });
</script>

<style lang="scss" scoped>
.form-content {
  max-height: 650px;
  overflow-y: auto;
  padding-right: 20px;
}
.blue-text {
  color: var(--el-color-primary);
}

.medium-dialog {
  width: 800px;
}
</style>
<style>

</style>
