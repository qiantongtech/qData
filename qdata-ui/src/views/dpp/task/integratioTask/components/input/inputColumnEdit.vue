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
  <el-dialog v-model="visibleDialog" draggable class="dialog" :title="title" destroy-on-close>
    <el-form ref="daDiscoveryTaskRef" :model="form" label-width="120px" @submit.prevent>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="基准类型" prop="type" :rules="[
            { required: true, message: '请选择基准类型', trigger: 'change' },
          ]">
            <el-select v-model="form.type" placeholder="请选择基准类型">
              <el-option v-for="benchmark in benchmarkTypes" :key="benchmark.value" :label="benchmark.label"
                         :value="benchmark.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="字段名称" prop="incrementColumn" :rules="[
            { required: true, message: '请输入字段名称', trigger: 'blur' },
          ]">
            <el-select v-model="form.incrementColumn" placeholder="请输入字段名称">
              <el-option v-for="item in ColumnByAssettab" :key="item.columnName" :label="item.columnName"
                         :value="item.columnName" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.type !== '2'">
          <el-form-item label="运算符" prop="operator" :rules="[
            { required: true, message: '请选择运算符', trigger: 'change' },
          ]">
            <el-select v-model="form.operator" placeholder="请选择运算符">
              <el-option v-for="operator in operators" :key="operator.value" :label="operator.label"
                         :value="operator.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="基准值" prop="data" :rules="dataRules">
            <template v-if="form.type === '1'">
              <el-date-picker clearable
                              v-model="form.data"
                              :type="pickerType"
                              :format="dateIncrementConfig_dateFormat2"
                              :value-format="dateIncrementConfig_dateFormat2"
                              placeholder="请选择固定时间">
              </el-date-picker>
            </template>
            <template v-else-if="form.type === '3'">
              <sql-editor placeholder="请输入sql" ref="editorRef" :value="form.data" class="sql-editor" :height="'300px'"
                          @changeTextarea="changeTextarea($event)" />
            </template>
            <template v-else>
              <el-input v-model="form.data" placeholder="自动获取当前时间，无需填写" disabled />
            </template>
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="form.type === '2'">
          <el-form-item label="游标时间" prop="cursorTime" :rules="[{ required: true, message: '请选择游标时间', trigger: 'change' }]">
            <el-date-picker clearable
                            v-model="form.cursorTime"
                            :type="pickerType"
                            :format="dateIncrementConfig_dateFormat2"
                            :value-format="dateIncrementConfig_dateFormat2"
                            placeholder="请选择游标时间">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch, getCurrentInstance } from "vue";
import SqlEditor from "@/components/SqlEditor/index1.vue";

const { proxy } = getCurrentInstance();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  ColumnByAssettab: { type: Array, default: () => [] },
  dateIncrementConfig_dateFormat: { type: String, default: 'YYYY-MM-DD' },
});

// day.js与java的日期格式不兼容，需要处理
const dateIncrementConfig_dateFormat2 = computed(() => {
  return props.dateIncrementConfig_dateFormat
      .replace(/yyyy/g, 'YYYY')
      .replace(/dd/g, 'DD')
});
const pickerType = computed(() => {
  const format = props.dateIncrementConfig_dateFormat;
  return format.includes('HH') ? 'datetime' : 'date';
});

const emit = defineEmits(["update:visible", "confirm"]);

const form = ref({
  incrementColumn: "",
  operator: "",
  type: "",
  data: "",
});

let daDiscoveryTaskRef = ref();
let editorRef = ref("");

// 运算符
const operators = ref([
  { label: ">", value: ">" },
  { label: ">=", value: ">=" },
  { label: "<", value: "<" },
  { label: "<=", value: "<=" },
]);

// 基准类型
const benchmarkTypes = ref([
  { label: "固定值", value: "1" },
  { label: "时间范围", value: "2" },
  { label: "SQL表达式", value: "3" },
]);

// 动态基准值规则
const dataRules = computed(() => {
  if (form.value.type === "1" || form.value.type === "3") {
    return [{ required: true, message: "请输入基准值", trigger: "change" }];
  }
  return [];
});

// 监听 visible 弹窗打开时初始化表单
watch(
    () => props.visible,
    (newVal) => {
      if (newVal) {
        form.value = JSON.parse(JSON.stringify(props.data || {}));
      } else {
        proxy.resetForm("daDiscoveryTaskRef");
      }
    }
);

// 默认选择字段名称
watch(
    () => props.ColumnByAssettab,
    (newVal) => {
      if (newVal?.length > 0 && !form.value.incrementColumn) {
        form.value.incrementColumn = newVal[0].columnName;
      }
    },
    { immediate: true, deep: true }
);

// 监听类型变化重置 data
watch(() => form.value.type, (newType) => {
  form.value.data = "";
});

// SQL 编辑器 change 回调
function changeTextarea(val) {
  form.value.data = val;
}

// 显示/隐藏 dialog
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// 关闭弹窗
const closeDialog = () => {
  emit("update:visible", false);
};

// 保存逻辑
const saveData = () => {
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      emit("confirm", form.value);
      emit("update:visible", false);
    } else {
      console.log("表单校验未通过");
    }
  });
};
</script>
