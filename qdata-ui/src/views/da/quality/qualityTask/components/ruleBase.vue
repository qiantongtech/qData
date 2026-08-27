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
    width="1200px"
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
      <el-form ref="formRef" :model="form" label-width="130px">
        <div class="h2-title">{{ td('da.qualityTask.ruleSelectorMenu.basicInfo', 'Basic Info') }}</div>
        <el-row>
          <el-col :span="8">
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.evaluationName', 'Evaluation Name')"
              prop="name"
              v-if="type != 3"
              :rules="[
                { required: true, message: td('da.qualityTask.ruleSelectorMenu.evaluationNameRequired', 'Evaluation Name Required'), trigger: 'blur' },
                { pattern: /^(?!\s+$).*$/, message: td('common.texts.noSpaces', 'Cannot be all spaces'), trigger: 'blur' }
              ]"
            >
              <template v-if="!falg">
                <el-input v-model="form.name" :placeholder="td('da.qualityTask.ruleSelectorMenu.evaluationNamePlaceholder', 'Evaluation Name Placeholder')" maxlength="50" />
              </template>
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.inspectionName', 'Inspection Name')"
              prop="name"
              v-else
              :rules="[
                { required: true, message: td('da.qualityTask.ruleSelectorMenu.inspectionNameRequired', 'Inspection Name Required'), trigger: 'blur' },
                { pattern: /^(?!\s+$).*$/, message: td('common.texts.noSpaces', 'Cannot be all spaces'), trigger: 'blur' }
              ]"
            >
              <template v-if="!falg">
                <el-input v-model="form.name" :placeholder="td('da.qualityTask.ruleSelectorMenu.inspectionNamePlaceholder', 'Inspection Name Placeholder')" maxlength="50" />
              </template>
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.inspectionRuleId', 'Inspection Rule ID')" prop="ruleCode">
              <div class="form-readonly">{{ form.ruleCode || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.inspectionRuleName', 'Inspection Rule Name')" prop="ruleName">
              <div class="form-readonly">{{ form.ruleName || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.alarmLevel', 'Alarm Level')" prop="warningLevel">
              <template v-if="!falg">
                <el-select
                  v-model="form.warningLevel"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.qualityDimensionPlaceholder', 'Quality Dimension Placeholder')"
                  class="warning-level-select"
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
            <el-form-item :label="td('common.texts.status', 'Status')" prop="status">
              <el-radio-group v-model="form.status" :disabled="falg">
                <el-radio :value="'1'">{{ td('da.qualityTask.online', 'Online') }}</el-radio>
                <el-radio :value="'0'">{{ td('da.qualityTask.offline', 'Offline') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.ruleDescription', 'Rule Description')" prop="ruleDescription">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="256"
                  show-word-limit
                  v-model="form.ruleDescription"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.ruleDescriptionPlaceholder', 'Rule Description Placeholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.ruleDescription || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.errorExample', 'Error Example')" prop="errDescription">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="256"
                  show-word-limit
                  v-model="form.errDescription"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.errorExamplePlaceholder', 'Error Example Placeholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.errDescription || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.fixSuggestion', 'Fix Suggestion')" prop="suggestion">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="256"
                  show-word-limit
                  v-model="form.suggestion"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.fixSuggestionPlaceholder', 'Fix Suggestion Placeholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.suggestion || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.whereCondition', 'Where Condition')" prop="whereClause">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  :maxlength="256"
                  show-word-limit
                  v-model="form.whereClause"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.whereConditionPlaceholder', 'Where Condition Placeholder')"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.whereClause || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- Rule configuration -->
        <div class="h2-title">{{ td('da.qualityTask.ruleSelectorMenu.ruleConfig', 'Rule Config') }}</div>
        <el-row>
          <el-col
            :span="12"
            class="hasMsg"
            v-if="
              type != 3 &&
              form.ruleType != 'STATUS_CONSIST_VAL' &&
              form.ruleType != 'MASTER_DATA_REF_VALIDATION' &&
              form.ruleType != 'CROSS_TABLE_CONSIST_VAL' &&
              form.ruleType != 'FOREIGN_KEY_VALIDITY_VAL' &&
              form.ruleType != 'NUMERIC_LOGIC_VALUE_VALIDITY_VAL'
            "
          >
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.evaluationTarget', 'Evaluation Target')"
              prop="tableName"
              :rules="[
                {
                  required: true,
                  message: td('da.qualityTask.ruleSelectorMenu.evaluationTargetRequired', 'Evaluation Target Required'),
                  trigger: 'change',
                },
              ]"
            >
              <template v-if="!falg">
                <el-select
                  v-model="form.tableName"
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.evaluationTargetPlaceholder', 'Evaluation Target Placeholder')"
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
            v-if="
              form.ruleType != 'TIME_ORDER_VALIDATION' &&
              form.ruleType != 'TIME_ORDER_VAL' &&
              form.ruleType != 'NUMERIC_LOGIC_RELATION_VALIDATION' &&
              form.ruleType != 'NUM_LOGIC_REL_VAL' &&
              form.ruleType != 'STATUS_DEPENDENCY_VALIDATION' &&
              form.ruleType != 'STATUS_DEPEND_VAL' &&
              form.ruleType != 'STATUS_CONSIST_VAL' &&
              form.ruleType != 'MUTUALLY_EXCLUSIVE_FIELD_CONFLICT' &&
              form.ruleType != 'MASTER_DATA_REF_VALIDATION' &&
              form.ruleType != 'NUM_OUTLIER_VAL' &&
              form.ruleType != 'CROSS_TABLE_CONSIST_VAL' &&
              form.ruleType != 'FOREIGN_KEY_VALIDITY_VAL' &&
              form.ruleType != 'NUMERIC_LOGIC_VALUE_VALIDITY_VAL' &&
              type != 3
            "
          >
            <el-form-item
              :label="td('da.qualityTask.ruleSelectorMenu.checkField', 'Check Field')"
              prop="evaColumn"
              :rules="[
                {
                  required: true,
                  message: td('da.qualityTask.ruleSelectorMenu.checkFieldRequired', 'Check Field Required'),
                  trigger: 'change',
                },
              ]"
            >
              <template v-if="!falg">
                <el-select
                  v-if="isMultipleRuleType"
                  v-model="form.evaColumn"
                  multiple
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.checkFieldPlaceholder', 'Check Field Placeholder')"
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
                  :placeholder="td('da.qualityTask.ruleSelectorMenu.checkFieldPlaceholder', 'Check Field Placeholder')"
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
          :tableName="form.tableName"
          @update:tableName="
            (val) => {
              form.tableName = val;
              handleTargetObjectChange(val);
            }
          "
          :dppQualityTaskObjSaveReqVO="dppQualityTaskObjSaveReqVO"
          :falg="falg"
          :columnList="columnList"
          :loading="loading"
        />

        <div class="h2-title" v-if="form.ruleType == 'CHARACTER_VALIDATION'">
          {{ td('da.qualityTask.ruleSelectorMenu.sampleCheck', 'Sample Check') }}
        </div>
        <el-row v-if="form.ruleType == 'CHARACTER_VALIDATION'">
          <el-col :span="12">
            <el-form-item :label="td('da.qualityTask.ruleSelectorMenu.sampleData', 'Sample Data')" prop="sampleData">
              <el-input v-model="title" :placeholder="td('da.qualityTask.ruleSelectorMenu.sampleDataPlaceholder', 'Sample Data Placeholder')" />
              <div class="sample-tag-wrapper">
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
              <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('da.qualityTask.ruleSelectorMenu.monitor', 'Monitor') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      <SpotCheckDialog ref="spotCheckRef" />
    </div>
    <template #footer>
      <template v-if="dialogStatus == 1">
        <el-button type="warning" v-if="type != 3" @click="handleSpotCheck"
          >{{ td('da.qualityTask.ruleSelectorMenu.spotCheck', 'Spot Check') }}</el-button
        >
        <el-button @click="handleBack" v-if="!mode">{{ td('common.button.cancel', 'Cancel') }}</el-button>
        <el-button type="primary" @click="handleSave" v-if="!falg"
          >{{ td('common.button.confirm', 'Confirm') }}</el-button
        >
      </template>
      <el-button @click="closeDialog" v-else>{{ td('common.button.cancel', 'Cancel') }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, reactive, watch, toRefs, nextTick, getCurrentInstance, onMounted, computed } from "vue";
import SideMenu from "./ruleSelectorMenu.vue";
import SpotCheckDialog from "./spotCheckResult.vue";
import { getColumnByAssetId } from "@/api/dpp/task/index.js";
import { getRuleConfig, getRuleComponent } from "./rule/registry.js";
import { verifyInterfaceValue } from "@/api/da/quality/qualityTask";

const { td } = useDefaultLang();
let falg = ref(false);
const { proxy } = getCurrentInstance();
const { quality_warning_status } = proxy.useDict("quality_warning_status");
const emit = defineEmits(["confirm"]);

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
  ruleName: "", 
  ruleCode: "", 
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
    allowedChars: ["1"], 
    useRegexFlag: 0, 
    regex: "", 
    ignoreNullValue: "0", 
    minLength: null, 
    maxLength: null, 
    scale: "2", 
    skipInteger: "1", 
    fillStrategy: "1",
    minValue: null,
    maxValue: null,
    includeBoundary: "1",
    useCodeTable: "0",
    ruleCodeTableId: "",
    ignoreCase: "0",
    codeList: [],
    validValues: [],
    calculationGroups: [],
    conditions: [],
  },
});

const isMultipleRuleType = computed(
  () =>
    form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
    form.ruleType == "COMP_UNIQUE_VAL" ||
    form.ruleType == "GROUP_FIELD_COMPLETENESS" ||
    form.ruleType == "GROUP_COMPLETE_VAL"
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

const currentRuleConfig = computed(() => {
  return getRuleConfig(form.ruleType);
});

const currentRuleComponent = computed(() => {
  return getRuleComponent(form.ruleType) || null;
});

let loading = ref(false);
let columnList = ref([]);
const columnsCache = new Map();
const spotCheckRef = ref();

async function handleSpotCheck() {
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.validationIncomplete', 'Validation Incomplete'));
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;
    if (res.data?.evaColumn) {
      form.evaColumn = res.data.evaColumn;
    }
    if (res.data?.tableName) {
      form.tableName = res.data.tableName;
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

  let obj = {
    ...formCopy,
    datasourceId: selectedRef.value?.datasourceId,
    title: title.value,
  };
  spotCheckRef.value.openDialog(obj);
}

function handleTargetObjectChange(tableName) {
  const selected = dppQualityTaskObjSaveReqVO.value.find(
    (item) => item.tableName == tableName
  );
  if (selected) {
    form.datasourceId = selected.datasourceId;
    if (isMultipleRuleType.value) {
      form.evaColumn = [];
    } else {
      form.evaColumn = "";
    }
    const cacheKey = `${selected.datasourceId}|${tableName}`;
    columnsCache.delete(cacheKey);
    fetchColumns();
  } else {
    form.datasourceId = null;
    form.tableName = "";
    if (isMultipleRuleType.value) {
      form.evaColumn = [];
    } else {
      form.evaColumn = "";
    }
    columnList.value = [];
  }
}

async function fetchColumns() {
  if (
    form.ruleType == 'MASTER_DATA_REF_VALIDATION' ||
    form.ruleType == 'CROSS_TABLE_CONSIST_VAL' ||
    form.ruleType == 'FOREIGN_KEY_VALIDITY_VAL' ||
    form.ruleType == 'STATUS_CONSIST_VAL' ||
    form.ruleType == 'NUMERIC_LOGIC_VALUE_VALIDITY_VAL'
  ) {
    return;
  }

  if (!selectedRef.value?.datasourceId || !form?.tableName) {
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
    proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.validationIncomplete', 'Validation Incomplete'));
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;

    if (res.data?.evaColumn) {
      form.evaColumn = res.data.evaColumn;
    }
    if (res.data?.tableName) {
      form.tableName = res.data.tableName;
    }
  }
  const currentColumnList = res.data?.columnList || columnList.value;
  const selectedLabels = currentColumnList.map((col) => ({
    name: col.columnName,
    label: col.label,
  }));
  if (Array.isArray(form.evaColumn)) {
    form.evaColumn = form.evaColumn.join(",");
  }
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
    return proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.sampleDataRequired', 'Sample Data Required'));
  }
  if (title.value.trim() === "") {
    return proxy.$message.warning(td('common.texts.noSpaces', 'Cannot be all spaces'));
  }
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning(td('da.qualityTask.ruleSelectorMenu.validationIncomplete', 'Validation Incomplete'));
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;
    
    if (res.data?.evaColumn) {
      form.evaColumn = res.data.evaColumn;
    }
    if (res.data?.tableName) {
      form.tableName = res.data.tableName;
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
  let resw = await verifyInterfaceValue({ ...formCopy, title: title.value });

  if (resw.code === 200) {
    sampleCheckMsg.value = resw.data;
  } else {
    sampleCheckMsg.value = resw.msg || td('da.qualityTask.ruleSelectorMenu.monitorFailed', 'Monitor Failed');
  }
}

function handleCardClick(data) {
  resetForm();
  if (props.type == 2 || props.type == 3) {
    form.tableName = props.tableName;
  }
  form.ruleName = data?.name;
  form.ruleCode = data?.code;
  form.ruleType = data?.strategyKey;
  form.dimensionType = data?.qualityDim;
  const prefix = props?.type == 3 ? td('da.qualityTask.ruleSelectorMenu.addInspectionRule', 'Add Inspection Rule') : td('da.qualityTask.ruleSelectorMenu.addEvaluationRule', 'Add Evaluation Rule');
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
  const prefix = props?.type == 3 ? td('da.qualityTask.ruleSelectorMenu.inspectionRule', 'Inspection Rule') : td('da.qualityTask.ruleSelectorMenu.evaluationRule', 'Evaluation Rule');
  if (falg.value) {
    dialogTitle.value = `${props?.type == 3 ? td('da.qualityTask.ruleSelectorMenu.inspectionRuleDetail', 'Inspection Rule Detail') : td('da.qualityTask.ruleSelectorMenu.evaluationRuleDetail', 'Evaluation Rule Detail')}${
      record?.ruleName ? `-${record.ruleName}` : ""
    }`;
  } else {
    dialogTitle.value = `${mode.value ? td('common.button.update', 'Update') : td('common.button.add', 'Add')}${prefix}${
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
    if (props.type == 2) {
      form.tableName = props.tableName;
    }
    if (isMultipleRuleType.value) {
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
  ruleName: "", 
  ruleCode: "", 
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
    allowedChars: ["1"], 
    useRegexFlag: 0, 
    regex: "", 
    ignoreNullValue: "1", 
    minLength: null, 
    maxLength: null, 
    scale: "2", 
    skipInteger: "1", 
    fillStrategy: "1",
    minValue: null,
    maxValue: null,
    includeBoundary: "1",
    useCodeTable: "0",
    ruleCodeTableId: "",
    ignoreCase: "0",
    codeList: [],
    validValues: [],
    calculationGroups: [],
    allowPartialEmpty: "1",
    conditions: [],
  },
});

function resetForm() {
  Object.assign(form, initialForm());
  columnList.value = [];
  columnsCache.clear(); 
  title.value = "";
  sampleCheckMsg.value = "";
}

function closeDialog() {
  dialogVisible.value = false;
  resetForm();
}

function handleBack() {
  dialogStatus.value = 0;
  dialogTitle.value = td('da.qualityTask.ruleSelectorMenu.addEvaluationRule', 'Add Evaluation Rule');
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
.warning-level-select {
  width: 290px;
}
.sample-tag-wrapper {
  margin-top: 6px;
  display: inline-block;
}
</style>
