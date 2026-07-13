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
  <!-- Clean up expired data -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item :label="td('dpp.cleanRule.timeRange', '时间范围')">
          <el-radio-group
            v-model="form.dataRange"
            @change="handleDataRangeChange"
            :disabled="falg"
          >
            <el-radio label="0">{{ td('dpp.cleanRule.recentTimeRange', '最近时间范围') }}</el-radio>
            <el-radio label="1">{{ td('dpp.cleanRule.specificDate', '具体日期') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          v-if="form.dataRange == '0'"
          :label="td('dpp.cleanRule.maintainTimeRange', '维持时间范围')"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: td('dpp.cleanRule.inputMaintainTimeRange', '请输入维持时间范围'),
                    trigger: 'change',
                  },
                ]
              : []
          "
          prop="dataRangeValue"
        >
          <template v-if="!falg">
            <el-input-number
              v-model="form.dataRangeValue"
              :min="1"
              class="rule-half"
            />
            <el-select v-model="form.dataRangeType" class="rule-half">
              <el-option :label="td('dpp.cleanRule.daysAgo', '天前')" value="1" />
              <el-option :label="td('dpp.cleanRule.monthsAgo', '月前')" value="2" />
              <el-option :label="td('dpp.cleanRule.yearsAgo', '年前')" value="3" />
            </el-select>
          </template>
          <div v-else class="form-readonly">{{ rangeValueText }}</div>
        </el-form-item>

        <el-form-item
          v-if="form.dataRange == '1'"
          :label="td('dpp.cleanRule.specificDate', '具体日期')"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: td('dpp.cleanRule.specificDate', '请选择具体日期'),
                    trigger: 'change',
                  },
                ]
              : []
          "
          prop="dataRangeValue"
        >
          <template v-if="!falg">
            <el-date-picker
              v-model="form.dataRangeValue"
              type="date"
              format="YYYY/MM/DD"
              class="rule-half"
              value-format="YYYY-MM-DD"
              :placeholder="td('dpp.cleanRule.selectDate', '选择日期')"
            />
            <el-select v-model="form.dataRangeType" class="rule-half">
              <el-option :label="td('dpp.cleanRule.before', '之前')" value="1" />
              <el-option :label="td('dpp.cleanRule.after', '之后')" value="2" />
            </el-select>
          </template>
          <div v-else class="form-readonly">{{ dateValueText }}</div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item :label="td('dpp.cleanRule.handleMethod', '处理方式')">
          <el-radio-group v-model="form.handleType" :disabled="falg">
            <el-radio label="0">{{ td('dpp.cleanRule.markAsExpired', '标记为过期') }}</el-radio>
            <el-radio label="1">{{ td('dpp.cleanRule.deleteRecord', '删除记录') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.markField', '标记字段')"
          v-if="form.handleType == '0'"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: td('dpp.cleanRule.selectMarkField', '请选择标记字段'),
                    trigger: 'change',
                  },
                ]
              : []
          "
        >
          <template v-if="!falg">
            <el-select
              v-model="form.handleColumns"
              :placeholder="td('dpp.cleanRule.selectCleanField', '请选择清洗字段')"
              clearable
              class="rule-half"
            >
              <el-option
                v-for="dict in inputFields"
                :key="dict.columnName"
                :label="dict.label"
                :value="dict.columnName"
              />
            </el-select>
          </template>
          <div v-else class="form-readonly">{{ handleColumnsText }}</div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.markValue', '标记值')"
          v-if="form.handleType == '0'"
          :rules="
            !falg
              ? [{ required: true, message: td('dpp.cleanRule.inputMarkValue', '请输入标记值'), trigger: 'change' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.handleValue"
            :placeholder="td('dpp.cleanRule.inputMarkValue', '请输入标记值')"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.handleValue || "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { reactive, ref } from "vue";
import moment from "moment";
const { td } = useDefaultLang();

const props = defineProps({
  form: Object,
  inputFields: Array,
  falg: Boolean,
});

const emit = defineEmits(["update:form"]);
const formRef = ref(null);

const dataRange = props.form?.dataRange ?? "0";

const form = reactive({ ...props.form });
const dataRangeText = computed(() =>
  form.dataRange === "0"
    ? td('dpp.cleanRule.recentTimeRange', '最近时间范围')
    : form.dataRange === "1"
    ? td('dpp.cleanRule.specificDate', '具体日期')
    : "-"
);
const rangeValueText = computed(() => {
  const typeText =
    form.dataRangeType === "1"
      ? td('dpp.cleanRule.daysAgo', '天前')
      : form.dataRangeType === "2"
      ? td('dpp.cleanRule.monthsAgo', '月前')
      : form.dataRangeType === "3"
      ? td('dpp.cleanRule.yearsAgo', '年前')
      : "";
  return form.dataRangeValue ? `${form.dataRangeValue} ${typeText}` : "-";
});
const dateValueText = computed(() => {
  const typeText =
    form.dataRangeType === "1"
      ? td('dpp.cleanRule.before', '之前')
      : form.dataRangeType === "2"
      ? td('dpp.cleanRule.after', '之后')
      : "";
  return form.dataRangeValue ? `${form.dataRangeValue} ${typeText}` : "-";
});
const handleTypeText = computed(() =>
  form.handleType === "0"
    ? td('dpp.cleanRule.markAsExpired', '标记为过期')
    : form.handleType === "1"
    ? td('dpp.cleanRule.deleteRecord', '删除记录')
    : "-"
);
const handleColumnsText = computed(() => {
  const f = (inputFields || []).find(
    (d) => d.columnName === form.handleColumns
  );
  return f ? f.label : form.handleColumns || "-";
});

// Use the change event to monitor dataRange changes. If dataRangeValue is empty during switching, a default value will be assigned.
function handleDataRangeChange(newVal) {
  if (newVal === "0") {
    form.dataRangeValue = 30;
  } else if (newVal === "1") {
    form.dataRangeValue = moment().format("YYYY-MM-DD");
  }
}

// Verify and return valid data after cleaning
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (!valid) return resolve({ valid: false });

      // Only save the fields that are actually present on the current page
      const cleanedData = {
        dataRange: form.dataRange,
        dataRangeValue: form.dataRangeValue,
        handleType: form.handleType,
        dataRangeType: form.dataRangeType,
      };

      if (form.dataRange === "0") {
        // Recent time range, dataRangeValue is a number
        cleanedData.dataRangeValue = form.dataRangeValue;
      } else if (form.dataRange === "1") {
        // Specific date, dataRangeValue is a date string
        cleanedData.dataRangeValue = form.dataRangeValue;
      }

      if (form.handleType === "0") {
        cleanedData.handleColumns = form.handleColumns;
        cleanedData.handleValue = form.handleValue;
      }

      resolve({
        valid: true,
        data: cleanedData,
      });
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
.rule-half {
  width: 100%;
}
</style>
