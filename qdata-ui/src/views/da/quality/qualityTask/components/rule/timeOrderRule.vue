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
  <!-- 时间字段先后顺序校验 -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
    <el-form-item label="">
      <div
        class="field-line"
        style="display: flex; align-items: center; flex-wrap: wrap; gap: 8px"
      >
        <el-select
          v-if="form.conditions.length > 0"
          v-model="form.conditions[0].leftField"
          placeholder="字段"
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
            placeholder="符号"
            style="width: 50px"
          >
            <el-option label="<" value="<" />
            <el-option label="≤" value="<=" />
          </el-select>

          <el-select
            v-model="cond.rightField"
            placeholder="字段"
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

        <!-- 添加按钮 -->
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

// 拼接选择的时间字段，参考evaColumn的实现
const timeOrderFields = computed(() => {
  if (!form.conditions || form.conditions.length === 0) return "";
  // 获取所有唯一的字段名
  const fieldNames = new Set();
  form.conditions.forEach((cond) => {
    if (cond.leftField) fieldNames.add(cond.leftField);
    if (cond.rightField) fieldNames.add(cond.rightField);
  });
  // 转换为数组并排序
  const fieldsArray = Array.from(fieldNames);
  // 使用columnList中的label显示，如果没有则显示columnName
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
    ElMessage.warning("校验未通过，请至少添加一组计算条件");
    return false;
  }
  for (let i = 0; i < form.conditions.length; i++) {
    const group = form.conditions[i];
    if (!group.leftField) {
      ElMessage.warning(`校验未通过，请填写第 ${i + 1} 个计算组的左字段`);
      return false;
    }
    if (!group.operator || !["<", "<="].includes(group.operator)) {
      ElMessage.warning(`校验未通过，第 ${i + 1} 个计算组的符号无效`);
      return false;
    }
    if (!group.rightField) {
      ElMessage.warning(`校验未通过，请填写第 ${i + 1} 个计算组的右字段`);
      return false;
    }
  }
  return true;
}

function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (!valid) {
        ElMessage.warning("校验未通过，请完善表单必填项");
        resolve({ valid: false });
        return;
      }
      if (!validateCalculationGroups()) {
        resolve({ valid: false });
        return;
      }

      // 获取所有唯一的字段名
      const fieldNames = new Set();
      form.conditions.forEach((cond) => {
        if (cond.leftField) fieldNames.add(cond.leftField);
        if (cond.rightField) fieldNames.add(cond.rightField);
      });

      // 转换为数组
      const fieldsArray = Array.from(fieldNames);

      resolve({
        valid: true,
        data: {
          conditions: JSON.parse(JSON.stringify(form.conditions)),
          evaColumn: fieldsArray, // 直接返回字段数组，用于赋值给父组件的evaColumn
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
