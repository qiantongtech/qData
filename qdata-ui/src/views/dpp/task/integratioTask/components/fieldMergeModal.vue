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
    v-model="dialogVisible"
    draggable
    class="medium-dialog"
    :title="dialogTitle"
      destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="form.ruleConfig.fieldMerge"
      :rules="formRules"
      label-width="150px"
     :label-position="labelPosition">
      <el-row :gutter="20">
        <!-- Select field (single choice) -->
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.selectField', '选择字段')"
            prop="sourceField"
           :label-position="labelPosition">
            <el-select
              v-model="form.ruleConfig.fieldMerge.sourceField"
              :placeholder="td('dpp.integration.selectFieldPlaceholder', '请选择字段名称')"
              filterable
              :disabled="row.columnName"
            >
              <el-option
                v-for="dict in tableFields"
                :key="dict.columnName"
                :label="dict.columnName"
                :value="dict.columnName"
          :disabled="usedFields.includes(dict.columnName)"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <!-- Merge fields (multiple selection) -->
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.mergeFields', '合并字段')"
            prop="sourceFields"
           :label-position="labelPosition">
            <el-select
              v-model="form.ruleConfig.fieldMerge.sourceFields"
              :placeholder="td('dpp.integration.selectFieldPlaceholder', '请选择字段名称')"
              filterable
             collapse-tags multiple
            >
              <el-option
                v-for="dict in tableFields"
                :key="dict.columnName"
                :label="dict.columnName"
                :value="dict.columnName"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <!-- delimiter -->
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.separator', '分隔符')"
            prop="separator"
           :label-position="labelPosition">
            <el-input
              v-model="form.ruleConfig.fieldMerge.separator"
              :placeholder="td('dpp.integration.separatorPlaceholder', '请输入分隔符（不能包含中文）')"
            />
          </el-form-item>
        </el-col>

        <!-- Null value handling -->
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.nullHandling', '空值处理')"
            prop="handleNull"
           :label-position="labelPosition">
            <el-select
              v-model="form.ruleConfig.fieldMerge.handleNull"
              :placeholder="td('dpp.integration.nullHandlingPlaceholder', '请选择空值处理方式')"
              filterable
            >
              <el-option :label="td('dpp.integration.keepAsNull', '保留为空')" value="keep" />
              <el-option :label="td('dpp.integration.replaceWithDefault', '替换为默认值')" value="default" />
              <el-option :label="td('dpp.integration.deleteRecord', '删除该条记录')" value="remove" />
            </el-select>
          </el-form-item>
        </el-col>

        <!-- Default value (only shown when default is selected) -->
        <el-col :span="12" v-if="form.ruleConfig.fieldMerge.handleNull === 'default'">
          <el-form-item
            :label="td('dpp.integration.defaultValueLabel', '默认值')"
            prop="defaultValue"
           :label-position="labelPosition">
            <el-input
              v-model="form.ruleConfig.fieldMerge.defaultValue"
              :placeholder="td('dpp.integration.defaultValuePlaceholder', '请输入默认值')"
            />
          </el-form-item>
        </el-col>

        <!-- Whether to remove spaces -->
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.trimLeadingTrailingSpace', '是否去除首尾空格')"
            prop="trimSpace"
           :label-position="labelPosition">
            <el-radio-group v-model="form.ruleConfig.fieldMerge.trimSpace">
              <el-radio :label="true">{{ td('dpp.integration.yes', '是') }}</el-radio>
              <el-radio :label="false">{{ td('dpp.integration.no', '否') }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ td('common.button.save') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, watch, computed } from "vue";

const { td } = useDefaultLang();// props
const props = defineProps({
  visibleDialogs: { type: Boolean, default: true },
  title: { type: String, default: '' },
  row: { type: Object, default: () => ({}) },
  tableFields: { type: Array, default: () => [] },
  fieldFields: { type: Array, default: () => [] },
  id: { type: String, default: "" },
});
const dialogTitle = computed(() => props.title || td("dpp.integration.fieldMergeConfig", "字段合并规则配置"));
const usedFields = computed(() => {
  return props.fieldFields
    ?.map(f => f?.columnName)
    .filter(Boolean);
});


// emits
const emit = defineEmits(["update:visibleDialogs", "confirm"]);

// dialog display status responsive binding
const dialogVisible = computed({
  get: () => props.visibleDialogs,
  set: (val) => emit("update:visibleDialogs", val),
});

// form data
const form = ref({
  id: props.id,
  ruleConfig: {
    fieldMerge: {
      sourceField: "",
      sourceFields: [],
      separator: "-",
      handleNull: "skip",
      defaultValue: "",
      trimSpace: true,
    },
  },
});

// form reference
const formRef = ref(null);

// Form validation rules
const formRules = {
  sourceField: [{ required: true, message: td("dpp.integration.selectFieldRequired", "请选择字段"), trigger: "change" }],
  sourceFields: [{ required: true, message: td("dpp.integration.selectFieldRequired", "请选择字段"), trigger: "change" }],
  separator: [
    { required: true, message: td("dpp.integration.separatorRequired", "请输入分隔符"), trigger: "blur" },
    {
      pattern: /^[^\u4e00-\u9fa5]+$/,
      message: td("dpp.integration.separatorNoChinese", "分隔符不能包含中文"),
      trigger: "blur",
    },
  ],
  handleNull: [{ required: true, message: td("dpp.integration.nullHandlingRequired", "请选择空值处理方式"), trigger: "change" }],
  defaultValue: [
    {
      validator: (rule, value, callback) => {
        if (form.value.ruleConfig.fieldMerge.handleNull === "default" && !value) {
          callback(new Error(td("dpp.integration.defaultValueRequired", "请输入默认值")));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  trimSpace: [{ required: true, message: td("dpp.integration.trimSpaceRequired", "请选择是否去除空格"), trigger: "change" }],
};

// Monitor the pop-up window display and initialize the data when it pops up
watch(
  () => props.visibleDialogs,
  (val) => {
    if (val && props.row) {
      const list = props.row.cleanRuleList;
      if (Array.isArray(list) && list.length > 0) {
        const lastRule = list[list.length - 1];
        if (lastRule?.ruleConfig) {
          try {
            const parsed = JSON.parse(lastRule.ruleConfig);
            form.value = {
              id: props.row.elementId?.[0] || "",
              ruleConfig: parsed,
            };
          } catch (err) {
            console.error("Failed to parse ruleConfig JSON:", err);
          }
        }
      } else {
        form.value = {
          id: props.row.elementId?.[0] || "",
          ruleConfig: {
            fieldMerge: {
              sourceField: "",
              sourceFields: [],
              separator: "-",
              handleNull: "skip",
              defaultValue: "",
              trimSpace: true,
            },
          },
        };
      }
    }
  }
);

// Reset the form and clear validation status
function reset() {
  form.value = {
    id: props.id,
    ruleConfig: {
      fieldMerge: {
        sourceField: "",
        sourceFields: [],
        separator: "-",
        handleNull: "skip",
        defaultValue: "",
        trimSpace: true,
      },
    },
  };
  if (formRef.value) formRef.value.clearValidate();
}

// Close the pop-up event and reset the form
function closeDialog() {
  dialogVisible.value = false; // Trigger update:visibleDialogs to notify the parent component to close the pop-up window
  reset();
}

// Submit form
function submitForm() {
  formRef.value.validate((valid) => {
    if (!valid) return;
    if (form.value.ruleConfig.fieldMerge.handleNull !== "default") {
      form.value.ruleConfig.fieldMerge.defaultValue = "";
    }
    const saveData = [
      {
        id: form.value.id || null,
        dataElemId: props.id,
        ruleType: 2,
        ruleId: form.value.id,
        ruleConfig: JSON.stringify(form.value.ruleConfig),
      },
    ];
    reset();
    emit("confirm", saveData);
    console.log("🚀 ~ formRef.value.validate ~ saveData:", saveData);
  });
}
</script>

<style scoped lang="scss">
.medium-dialog {
  .el-form-item {
    margin-bottom: 16px;
  }
}
</style>
