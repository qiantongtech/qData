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
  <!-- 数值字段范围校验 -->
  <el-form ref="formRef" :model="form" :disabled="false">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.minValue')" prop="minValue">
          <el-input
            v-if="!falg"
            v-model="form.minValue"
            :placeholder="td('da.qualityTaskRules.ruleCommon.minValuePlaceholder')"
            type="number"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.minValue ?? "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.maxValue')" prop="maxValue">
          <el-input
            v-if="!falg"
            v-model="form.maxValue"
            :placeholder="td('da.qualityTaskRules.ruleCommon.maxValuePlaceholder')"
            type="number"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.maxValue ?? "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.includeBoundary')" prop="includeBoundary">
          <el-radio-group v-if="!falg" v-model="form.includeBoundary">
            <el-radio :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.include') }}</el-radio>
            <el-radio :value="'0'">{{ td('da.qualityTaskRules.ruleCommon.exclude') }}</el-radio>
          </el-radio-group>
          <div v-else class="form-readonly">{{ includeBoundaryText }}</div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import { getColumnByAssetId } from "@/api/dpp/task/index.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();

const props = defineProps({
  form: Object,
  dppQualityTaskObjSaveReqVO: Array,
  falg: Boolean,
});

const emit = defineEmits(["update:form"]);

const formRef = ref(null);

const form = reactive({ ...props.form });
const includeBoundaryText = computed(() =>
  form.includeBoundary === "1"
    ? td('da.qualityTaskRules.ruleCommon.include')
    : form.includeBoundary === "0"
    ? td('da.qualityTaskRules.ruleCommon.exclude')
    : "-"
);
const columnList = ref([]);
// 评测对象改变时，更新相关字段和字段列表
async function handleTargetObjectChange(selectedName, falg) {
  const selected = props.auditTargets.find(
    (item) => item.datasourceName == selectedName
  );
  if (selected) {
    form.datasourceId = selected.datasourceId;
    form.assetid = selected.assetid || "";
    form.checkField = "";
    await fetchColumns();
  } else {
    form.datasourceId = null;
    form.assetid = "";
    form.checkField = "";
    columnList.value = [];
  }
}

const loading = ref(false);

async function fetchColumns() {
  if (!form.datasourceId || !form.assetid) {
    columnList.value = [];
    return;
  }
  loading.value = true;
  try {
    const res = await getColumnByAssetId({
      id: form.datasourceId,
      tableName: form.assetid,
    });
    if (res.code == "200") {
      columnList.value = res.data;
    } else {
      columnList.value = [];
    }
  } catch (error) {
    columnList.value = [];
  } finally {
    loading.value = false;
  }
}

const exposedFields = [
  "targetObject",
  "datasourceId",
  "assetid",
  "checkField",
  "minValue",
  "maxValue",
  "includeBoundary",
];

function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
        const data = Object.fromEntries(
          exposedFields.map((key) => [key, form[key]])
        );
        resolve({
          valid: true,
          data,
        });
      } else {
        resolve({ valid: false });
      }
    });
  });
}

onMounted(() => {
  if (form.targetObject) {
    fetchColumns();
  }
  console.log("子组件 mounted hook");
});

defineExpose({ validate });
</script>
<style scoped>
.rule-half {
  width: 100%;
}
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
</style>
