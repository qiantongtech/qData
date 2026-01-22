<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <!-- 清理过期数据 -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item label="时间范围">
          <el-radio-group
            v-model="form.dataRange"
            @change="handleDataRangeChange"
            :disabled="falg"
          >
            <el-radio label="0">最近时间范围</el-radio>
            <el-radio label="1">具体日期</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          v-if="form.dataRange == '0'"
          label="维持时间范围"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: '请输入维持时间范围',
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
              <el-option label="天前" value="1" />
              <el-option label="月前" value="2" />
              <el-option label="年前" value="3" />
            </el-select>
          </template>
          <div v-else class="form-readonly">{{ rangeValueText }}</div>
        </el-form-item>

        <el-form-item
          v-if="form.dataRange == '1'"
          label="具体日期"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: '请选择具体日期',
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
              placeholder="选择日期"
            />
            <el-select v-model="form.dataRangeType" class="rule-half">
              <el-option label="之前" value="1" />
              <el-option label="之后" value="2" />
            </el-select>
          </template>
          <div v-else class="form-readonly">{{ dateValueText }}</div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="处理方式">
          <el-radio-group v-model="form.handleType" :disabled="falg">
            <el-radio label="0">标记为过期</el-radio>
            <el-radio label="1">删除记录</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          label="标记字段"
          v-if="form.handleType == '0'"
          :rules="
            !falg
              ? [
                  {
                    required: true,
                    message: '请选择标记字段',
                    trigger: 'change',
                  },
                ]
              : []
          "
        >
          <template v-if="!falg">
            <el-select
              v-model="form.handleColumns"
              placeholder="请选择清洗字段"
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
          label="标记值"
          v-if="form.handleType == '0'"
          :rules="
            !falg
              ? [{ required: true, message: '请输入标记值', trigger: 'change' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.handleValue"
            placeholder="请输入标记值"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.handleValue || "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { reactive, ref } from "vue";
import moment from "moment";

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
    ? "最近时间范围"
    : form.dataRange === "1"
    ? "具体日期"
    : "-"
);
const rangeValueText = computed(() => {
  const typeText =
    form.dataRangeType === "1"
      ? "天前"
      : form.dataRangeType === "2"
      ? "月前"
      : form.dataRangeType === "3"
      ? "年前"
      : "";
  return form.dataRangeValue ? `${form.dataRangeValue} ${typeText}` : "-";
});
const dateValueText = computed(() => {
  const typeText =
    form.dataRangeType === "1"
      ? "之前"
      : form.dataRangeType === "2"
      ? "之后"
      : "";
  return form.dataRangeValue ? `${form.dataRangeValue} ${typeText}` : "-";
});
const handleTypeText = computed(() =>
  form.handleType === "0"
    ? "标记为过期"
    : form.handleType === "1"
    ? "删除记录"
    : "-"
);
const handleColumnsText = computed(() => {
  const f = (inputFields || []).find(
    (d) => d.columnName === form.handleColumns
  );
  return f ? f.label : form.handleColumns || "-";
});

// 用change事件监听dataRange变化，切换时如果dataRangeValue为空则赋默认值
function handleDataRangeChange(newVal) {
  if (newVal === "0") {
    form.dataRangeValue = 30;
  } else if (newVal === "1") {
    form.dataRangeValue = moment().format("YYYY-MM-DD");
  }
}

// 验证并返回清洗后的有效数据
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (!valid) return resolve({ valid: false });

      // 只保存当前页面实际有的字段
      const cleanedData = {
        dataRange: form.dataRange,
        dataRangeValue: form.dataRangeValue,
        handleType: form.handleType,
        dataRangeType: form.dataRangeType,
      };

      if (form.dataRange === "0") {
        // 最近时间范围，dataRangeValue 是数字
        cleanedData.dataRangeValue = form.dataRangeValue;
      } else if (form.dataRange === "1") {
        // 具体日期，dataRangeValue 是日期字符串
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
