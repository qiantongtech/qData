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
  <!-- 数值字段范围校验 -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item label="最小值" prop="minValue">
          <el-input
            v-if="!falg"
            v-model="form.minValue"
            placeholder="不填写表示不限制最小值"
            type="number"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.minValue ?? "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最大值" prop="maxValue">
          <el-input
            v-if="!falg"
            v-model="form.maxValue"
            placeholder="不填写表示不限制最大值"
            type="number"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.maxValue ?? "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="是否包含边界值" prop="includeBoundary">
          <el-radio-group v-if="!falg" v-model="form.includeBoundary">
            <el-radio :value="'1'">包含</el-radio>
            <el-radio :value="'0'">不包含</el-radio>
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
    ? "包含"
    : form.includeBoundary === "0"
    ? "不包含"
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
