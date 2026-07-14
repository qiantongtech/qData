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
  <!-- Add evaluation rule config for each rule -->
  <el-dialog
    v-model="dialogVisible"
    draggable
    class="medium-dialog"
    :class="{ 'max-dialogs-status0': dialogStatus === 0 }"
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
      class="content"
      style="max-height: 650px; overflow-y: auto; padding-right: 10px"
      v-show="dialogStatus == 1 || dialogStatus == 2"
      :disabled="dialogStatus == 2"
    >
      <el-form ref="formRef" :model="form" :label-position="labelPosition">
        <div class="h2-title">{{ td('da.qualityTask.ruleSelectorMenu.basicInfo') }}</div>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.evaluationName')"
              prop="name"
              v-if="type != 3"
              :rules="[
                { required: true, message: td('da.qualityTask.ruleSelectorMenu.evaluationNameRequired'), trigger: 'blur' },
              ]"
             :label-position="labelPosition">
              <template v-if="!falg">
                <el-input v-model="form.name" :placeholder="td('da.qualityTask.ruleSelectorMenu.evaluationNamePlaceholder')" />
              </template>
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.inspectionName')"
              prop="name"
              v-else
              :rules="[
                { required: true, message: td('da.qualityTask.ruleSelectorMenu.inspectionNameRequired'), trigger: 'blur' },
              ]"
             :label-position="labelPosition">
              <template v-if="!falg">
                <el-input v-model="form.name" :placeholder="td('da.qualityTask.ruleSelectorMenu.inspectionNamePlaceholder')" />
              </template>
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.inspectionRuleId')" prop="ruleCode" :label-position="labelPosition">
              <div class="form-readonly">{{ form.ruleCode || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.inspectionRuleName')" prop="ruleName" :label-position="labelPosition">
              <div class="form-readonly">{{ form.ruleName || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.alarmLevel')" prop="warningLevel" :label-position="labelPosition">
              <template v-if="!falg">
                <el-select
                  v-model="form.warningLevel"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.qualityDimensionPlaceholder')"
                  style="width: 290px"
                >
                  <el-option
                    v-for="dict in quality_warning_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </template>
              <div v-else class="form-readonly">
                {{
                  quality_warning_status.find(
                    (i) => i.value === form.warningLevel
                  )?.label || "-"
                }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
              <el-radio-group v-model="form.status" :disabled="falg">
                <el-radio :value="'1'">{{ td('da.qualityTask.online') }}</el-radio>
                <el-radio :value="'0'">{{ td('da.qualityTask.offline') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.ruleDescription')" prop="ruleDescription" :label-position="labelPosition">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="500"
                  show-word-limit
                  v-model="form.ruleDescription"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.ruleDescriptionPlaceholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.ruleDescription || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.errorExample')" prop="errDescription" :label-position="labelPosition">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="500"
                  show-word-limit
                  v-model="form.errDescription"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.errorExamplePlaceholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.errDescription || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.fixSuggestion')" prop="suggestion" :label-position="labelPosition">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="500"
                  show-word-limit
                  v-model="form.suggestion"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.fixSuggestionPlaceholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.suggestion || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.whereCondition')" prop="whereClause" :label-position="labelPosition">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="500"
                  show-word-limit
                  v-model="form.whereClause"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.whereConditionPlaceholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.whereClause || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- Rule configuration -->
        <div class="h2-title">{{ td('da.qualityTask.ruleSelectorMenu.ruleConfig') }}</div>
        <el-row :gutter="20">
          <el-col :span="12" class="hasMsg" v-if="type != 3">
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.evaluationTarget')"
              prop="tableName"
              :rules="[
                {
                  required: true,
                  message: td('da.qualityTask.ruleSelectorMenu.evaluationTargetRequired'),
                  trigger: 'change',
                },
              ]"
             :label-position="labelPosition">
              <template v-if="!falg">
                <el-select
                  v-model="form.tableName"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.evaluationTargetPlaceholder')"
                  filterable
                  clearable
                  :disabled="type == 2"
                  @change="handleTargetObjectChange"
                >
                  <el-option
                    v-for="item in dppQualityTaskObjSaveReqVO"
                    :key="item.tableName"
                    :label="item.name"
                    :value="item.tableName"
                  />
                </el-select>
              </template>
              <div v-else class="form-readonly">
                {{ selectedRef?.name || "-" }}
              </div>
              <span class="msg" v-if="selectedRef">
                <el-icon>
                  <InfoFilled />
                </el-icon>
                {{ selectedRef?.datasourceType || "" }} /
                {{ selectedRef?.tableName || "" }}
              </span>
            </el-form-item>
          </el-col>
          <el-col
            :span="12"
            v-if="form.ruleType != 'TIME_ORDER_VALIDATION' && type != 3"
          >
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.checkField')"
              prop="evaColumn"
              :rules="[
                {
                  required: true,
                  message: td('da.qualityTask.ruleSelectorMenu.checkFieldRequired'),
                  trigger: 'change',
                },
              ]"
             :label-position="labelPosition">
              <template v-if="!falg">
                <el-select
                  v-if="isMultipleRuleType"
                  v-model="form.evaColumn"
                  multiple
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.checkFieldPlaceholder')"
                  filterable
                  clearable
                  :loading="loading"
                  collapse-tags
                >
                  <el-option
                    v-for="col in columnList"
                    :key="col.columnName"
                    :label="col.label"
                    :value="col.columnName"
                  />
                </el-select>
                <el-select
                  v-else
                  v-model="form.evaColumn"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.checkFieldPlaceholder')"
                  filterable
                  clearable
                  :loading="loading"
                >
                  <el-option
                    v-for="col in columnList"
                    :key="col.columnName"
                    :label="col.label"
                    :value="col.columnName"
                  />
                </el-select>
              </template>
              <div v-else class="form-readonly">
                {{ evaColumnLabel || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <component
          :is="currentRuleComponent"
          ref="ruleComponentRef"
          :form="form.rule"
          :dppQualityTaskObjSaveReqVO="dppQualityTaskObjSaveReqVO"
          :falg="falg"
          :columnList="columnList"
        />

        <div class="h2-title" v-if="form.ruleType == 'CHARACTER_VALIDATION'">
          {{ td('da.qualityTask.ruleSelectorMenu.sampleCheck') }}
        </div>
        <el-row :gutter="20" v-if="form.ruleType == 'CHARACTER_VALIDATION'">
          <el-col :span="12">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.sampleData')" prop="sampleData" :label-position="labelPosition">
              <el-input v-model="title" :placeholder="td('da.qualityTask.ruleSelectorMenu.sampleDataPlaceholder')" />
              <!-- <span class="msg">Sample must comply with rule; cannot contain special characters if non-compliant</span> -->
              <div style="margin-top: 6px; display: inline-block">
                <el-tag
                  v-if="sampleCheckMsg"
                  closable
                  type="warning"
                  @close="sampleCheckMsg = ''"
                >
                  {{ sampleCheckMsg }}
                </el-tag>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="11" :offset="1">
            <el-button plain type="primary" @click="handleSampleCheck">
              <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('da.qualityTask.ruleSelectorMenu.monitor') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      <SpotCheckDialog ref="spotCheckRef" />
    </div>
    <template #footer>
      <template v-if="dialogStatus == 1">
        <el-button type="warning" v-if="type != 3" @click="handleSpotCheck"
          >{{ td('da.qualityTask.ruleSelectorMenu.spotCheck') }}</el-button
        >
        <el-button @click="handleBack" v-if="!mode">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="handleSave" v-if="!falg"
          >{{ td('common.button.confirm') }}</el-button
        >
      </template>
      <el-button @click="closeDialog" v-else>{{ td('common.button.cancel') }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, reactive, watch, toRefs } from "vue";
import SideMenu from "./ruleSelectorMenu.vue";
import SpotCheckDialog from "./spotCheckResult.vue";
import { getColumnByAssetId } from "@/api/dpp/task/index.js";
// Load rule subcomponents on demand via registry to reduce first-screen bundle size
import { getRuleConfig, getRuleComponent } from "./rule/registry.js";
import { verifyInterfaceValue } from "@/api/da/quality/qualityTask";

const { td } = useDefaultLang();
let falg = ref(false);
const { proxy } = getCurrentInstance();
const { quality_warning_status } = proxy.useDict("quality_warning_status");
const emit = defineEmits(["confirm"]);
// Evaluation object list from parent component
const props = defineProps({
  dppQualityTaskObjSaveReqVO: {
    type: Array,
    default: () => [],
  },
  type: {
    type: String,
    default: "",
  },
  tableName: {
    type: String,
    default: "",
  },
});
const { dppQualityTaskObjSaveReqVO } = toRefs(props);
const dialogVisible = ref(false);
const dialogStatus = ref(1);
const dialogTitle = ref("");
const formRef = ref();

let form = reactive({
  name: "",
  ruleName: "", // Inspection rule name:
  ruleCode: "", // Inspection rule code:
  status: "1",
  warningLevel: "2",
  ruleDescription: "",
  errDescription: "",
  suggestion: "",
  whereClause: "",
  ruleType: "",
  dimensionType: "",
  evaColumn: [],
  tableName: "",

  rule: {
    // String type validation
    allowedChars: ["1"], // Allowed character types
    useRegexFlag: 0, // Use regex
    regex: "", // Regex
    // Ignore null value, keep one
    ignoreNullValue: "0", // Ignore null value
    // Field length range validation
    minLength: null, // Min length
    maxLength: null, // Max length
    // Field precision
    scale: "2", // Decimal places
    skipInteger: "1", // Skip integer value
    // Field group integrity validation
    fillStrategy: "1",

    // Numeric field range validation
    minValue: null,
    maxValue: null,
    includeBoundary: "1",
    // Enum value validation
    useCodeTable: "0",
    ruleCodeTableId: "",
    ignoreCase: "0",
    codeList: [],
    validValues: [],
    calculationGroups: [],
    // Time selection
    conditions: [],
  },
});
const isMultipleRuleType = computed(
  () =>
    form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
    form.ruleType == "GROUP_FIELD_COMPLETENESS"
);
const selectedRef = computed(() => {
  return (
    dppQualityTaskObjSaveReqVO.value.find(
      (item) => item.tableName == form.tableName
    ) || null
  );
});
const evaColumnLabel = computed(() => {
  const map = new Map(
    (columnList.value || []).map((c) => [c.columnName, c.label])
  );
  const val = form.evaColumn;
  if (!val) return "";
  if (Array.isArray(val)) {
    return val.map((v) => map.get(v) || v).join(", ");
  }
  return map.get(val) || val;
});
let title = ref();

// Computed: current rule configuration
const currentRuleConfig = computed(() => {
  return getRuleConfig(form.ruleType);
});

// Computed: current rule component
const currentRuleComponent = computed(() => {
  return getRuleComponent(form.ruleType) || null;
});

let loading = ref(false);
let columnList = ref([]);
// Evaluation field list cache: key = datasourceId|tableName
const columnsCache = new Map();

const spotCheckRef = ref();

// Watch
async function handleSpotCheck() {
  console.log(
    "🚀 ~ handleSpotCheck ~  selectedRef.value:",
    selectedRef.value.datasourceId
  );
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.validationIncomplete'));
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;
    // If sub-component returns evaColumn field (e.g. timeOrderRule), assign directly to form.evaColumn
    if (res.data?.evaColumn) {
      form.evaColumn = res.data.evaColumn;
    }
  }

  const ruleData = res.data;
  const formCopy = JSON.parse(
    JSON.stringify({
      ...form,
      rule: JSON.stringify({ ...ruleData }),
    })
  );
  if (Array.isArray(formCopy.evaColumn)) {
    formCopy.evaColumn =
      formCopy.evaColumn.length > 0 ? formCopy.evaColumn.join(",") : null;
  }
  console.log(
    "🚀 ~ handleSpotCheck ~  formCopy.evaColumn:",
    formCopy.evaColumn
  );

  let obj = {
    ...formCopy,
    datasourceId: selectedRef.value?.datasourceId,
    title: title.value,
  };
  // let resw = await validationErrorDataSql(obj)
  spotCheckRef.value.openDialog(obj);
}
function handleTargetObjectChange(tableName) {
  const selected = dppQualityTaskObjSaveReqVO.value.find(
    (item) => item.tableName == tableName
  );
  console.log("🚀 ~ handleTargetObjectChange ~ selected:", selected);
  if (selected) {
    form.datasourceId = selected.datasourceId;
    if (
      form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
      form.ruleType == "GROUP_FIELD_COMPLETENESS"
    ) {
      console.log("2222");

      form.evaColumn = [];
    } else {
      form.evaColumn = "";
    }
    // Clear current table cache to ensure fresh request on each switch
    const cacheKey = `${selected.datasourceId}|${tableName}`;
    columnsCache.delete(cacheKey);
    fetchColumns();
  } else {
    form.datasourceId = null;
    form.tableName = "";
    if (
      form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
      form.ruleType == "GROUP_FIELD_COMPLETENESS"
    ) {
      form.evaColumn = [];
    } else {
      form.evaColumn = "";
    }

    columnList.value = [];
  }
}
async function fetchColumns() {
  console.log("🚀 ~ fetchColumns ~ selectedRef:", selectedRef.value);

  if (!selectedRef.value.datasourceId || !form?.tableName) {
    columnList.value = [];
    return;
  }
  loading.value = true;
  try {
    const cacheKey = `${form?.datasourceId || selectedRef.value.datasourceId}|${
      form?.tableName
    }`;
    if (columnsCache.has(cacheKey)) {
      columnList.value = columnsCache.get(cacheKey);
    } else {
      const res = await getColumnByAssetId({
        id: form?.datasourceId || selectedRef.value.datasourceId,
        tableName: form?.tableName,
      });
      if (res.code == "200") {
        const list = res.data.map((col) => ({
          ...col,
          label:
            col.columnName + (col.columnComment ? "/" + col.columnComment : ""),
        }));
        columnList.value = list;
        columnsCache.set(cacheKey, list);
      } else {
        columnList.value = [];
        columnsCache.set(cacheKey, []);
      }
    }
  } catch (error) {
    columnList.value = [];
  } finally {
    loading.value = false;
  }
}
let ruleComponentRef = ref();
async function handleSave() {
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.validationIncomplete'));
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;

    // If sub-component returns evaColumn field (e.g. timeOrderRule), assign directly to form.evaColumn
    if (res.data?.evaColumn) {
      form.evaColumn = res.data.evaColumn;
    }
  }
  const selectedLabels = columnList.value.map((col) => ({
    name: col.columnName,
    label: col.label,
  }));
  // Convert evaColumn array to comma-separated string first
  if (Array.isArray(form.evaColumn)) {
    form.evaColumn = form.evaColumn.join(",");
  }
  // Build the final rule field
  form.rule = JSON.stringify({
    ...res.data,
    evaColumns: selectedLabels,
  });

  const formCopy = JSON.parse(JSON.stringify(form));
  emit("confirm", formCopy, mode.value);
}

let sampleCheckMsg = ref();
async function handleSampleCheck() {
  if (!title.value) {
    return proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.sampleDataRequired'));
  }
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.validationIncomplete'));
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;
  }
  const ruleData = res.data;
  const formCopy = JSON.parse(
    JSON.stringify({
      ...form,

      rule: JSON.stringify({ ...ruleData }),
    })
  );
  if (Array.isArray(formCopy.evaColumn)) {
    formCopy.evaColumn =
      formCopy.evaColumn.length > 0 ? formCopy.evaColumn.join(",") : null;
  }
  let resw = await verifyInterfaceValue({ ...formCopy, title: title.value });

  if (resw.code === 200) {
    sampleCheckMsg.value = resw.data;
  } else {
    sampleCheckMsg.value = resw.msg || td('da.qualityTask.ruleSelectorMenu.monitorFailed');
  }
}
function handleCardClick(data) {
  resetForm();
  if (props.type == 2) {
    form.tableName = props.tableName;
  }
  if (props.type == 3) {
    form.tableName = props.tableName;
  }
  form.ruleName = data?.name;
  form.ruleCode = data?.code;
  form.ruleType = data?.strategyKey;
  form.dimensionType = data?.qualityDim;
  const prefix = props?.type == 3 ? td('da.qualityTask.ruleSelectorMenu.addInspectionRule') : td('da.qualityTask.ruleSelectorMenu.addEvaluationRule');
  dialogTitle.value = `${prefix}${data?.name ? "-" + data.name : ""}`;
  if (form.tableName) {
    handleTargetObjectChange(form.tableName);
  }
  dialogStatus.value = 1;
}
let mode = ref();
async function openDialog(record, index, fg) {
  falg.value = fg;
  mode.value = index;
  resetForm();
  const prefix = props?.type == 3 ? td('da.qualityTask.ruleSelectorMenu.inspectionRule') : td('da.qualityTask.ruleSelectorMenu.evaluationRule');
  if (falg.value) {
    dialogTitle.value = `${props?.type == 3 ? td('da.qualityTask.ruleSelectorMenu.inspectionRuleDetail') : td('da.qualityTask.ruleSelectorMenu.evaluationRuleDetail')}${
      record?.ruleName ? `-${record.ruleName}` : ""
    }`;
  } else {
    dialogTitle.value = `${mode.value ? td('common.button.update') : td('common.button.add')}${prefix}${
      record?.ruleName ? `-${record.ruleName}` : ""
    }`;
  }

  dialogStatus.value = mode.value ? 1 : 0;
  dialogVisible.value = true;

  if (index) {
    dialogStatus.value = 1;
    const { evaColumn, ruleType, rule, ...rest } = record;
    Object.assign(form, rest);
    form.ruleType = record.ruleType;
    console.log("🚀 ~ openDialog ~ form.ruleType:", form.ruleType);
    if (props.type == 2) {
      form.tableName = props.tableName;
    }
    if (
      form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
      form.ruleType == "GROUP_FIELD_COMPLETENESS"
    ) {
      form.evaColumn = evaColumn ? evaColumn.split(",") : [];
    } else {
      form.evaColumn = evaColumn || "";
    }

    try {
      form.rule = typeof rule === "string" ? JSON.parse(rule) : rule;
    } catch (e) {
      form.rule = {};
    }
    if (form.tableName) {
      await fetchColumns();
    }
  } else {
    resetForm();
  }
}

const initialForm = () => ({
  name: "",
  ruleName: "", // Inspection rule name:
  ruleCode: "", // Inspection rule code:
  status: "1",
  warningLevel: "2",
  ruleDescription: "",
  errDescription: "",
  suggestion: "",
  whereClause: "",
  ruleType: "",
  dimensionType: "",
  evaColumn: undefined,
  tableName: "",
  rule: {
    // String type validation
    allowedChars: ["1"], // Allowed character types
    useRegexFlag: 0, // Use regex
    regex: "", // Regex
    ignoreNullValue: "1", // Ignore null value

    // Field length range validation
    minLength: null, // Min length
    maxLength: null, // Max length

    // Field precision
    scale: "2", // Decimal places
    skipInteger: "1", // Skip integer value

    // Field group integrity validation
    fillStrategy: "1",

    // Numeric field range validation
    minValue: null,
    maxValue: null,
    includeBoundary: "1",

    // Enum value validation
    useCodeTable: "0",
    ruleCodeTableId: "",
    ignoreCase: "0",
    codeList: [],
    validValues: [],
    calculationGroups: [],

    // Time field
    allowPartialEmpty: "1",

    // Multi-condition field
    conditions: [],
  },
});

function resetForm() {
  Object.assign(form, initialForm());
  columnList.value = [];
  columnsCache.clear(); // Clear field list cache
  title.value = "";
  sampleCheckMsg.value = "";
}

function closeDialog() {
  dialogVisible.value = false;
  resetForm();
}

function handleBack() {
  dialogStatus.value = 0;
  dialogTitle.value = td('da.qualityTask.ruleSelectorMenu.addEvaluationRule');
  resetForm();
}
defineExpose({ openDialog, closeDialog });
</script>

<style scoped>
.blue-text {
  color: var(--el-color-primary);
}

.medium-dialog {
  width: 800px;
}
</style>
<style>
.el-dialog.max-dialogs-status0 .el-dialog__body {
  padding: 0 !important;
  padding-left: 10px !important;
}
</style>
