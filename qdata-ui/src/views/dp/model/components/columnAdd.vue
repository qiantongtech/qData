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
-->

<template>
  <el-dialog v-model="localVisible" :title="title" draggable destroy-on-close>
    <el-form
      ref="dpModelRefs"
      :model="form"
      :rules="rules"
      label-width="100px"
      @submit.prevent
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.relatedStandard')" prop="dataElemId">
            <el-select
              v-model="form.dataElemId"
              :placeholder="td('dp.modelForm.relatedStandardPlaceholder')"
              @change="handleDatasourceChange"
              filterable
              clearable
            >
              <el-option
                v-for="dict in DpData"
                :key="dict.id"
                :label="dict.name"
                :value="dict.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.model.chineseName')" prop="cnName">
            <el-input v-model="form.cnName" :placeholder="td('dp.model.chineseNamePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.model.englishName')" prop="engName">
            <el-input
              v-model="form.engName"
              :placeholder="td('dp.model.englishNamePlaceholder')"
              @input="convertToUpperCase('engName', form.engName)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.dataType')" prop="columnType">
            <el-select v-model="form.columnType" :placeholder="td('dp.modelForm.dataTypePlaceholder')">
              <el-option
                v-for="dict in column_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            :label="td('dp.modelForm.attributeLength')"
            prop="columnLength"
            :rules="
              form.columnType === 'DATE'
                ? []
                : [
                    {
                      required: true,
                      message: td('dp.modelForm.attributeLengthRequired'),
                      trigger: 'change',
                    },
                  ]
            "
          >
            <el-input-number
              :step="1"
              step-strictly
              v-model="form.columnLength"
              style="width: 100%"
              controls-position="right"
              :min="1"
              :max="9999999999"
              :placeholder="td('dp.modelForm.attributeLengthPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <!-- DECIMAL  NUMBER  NUMERIC -->
          <el-form-item :label="td('dp.modelForm.decimalPlaces')" prop="columnScale">
            <el-input-number
              :step="1"
              :disabled="
                form.columnType !== 'DECIMAL' &&
                form.columnType !== 'NUMBER' &&
                form.columnType !== 'NUMERIC' &&
                form.columnType !== 'FLOAT' &&
                form.columnType !== 'DOUBLE'
              "
              step-strictly
              v-model="form.columnScale"
              style="width: 100%"
              controls-position="right"
              :min="0"
              :max="9999999999"
              :placeholder="td('dp.modelForm.decimalPlacesPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.defaultValue')" prop="defaultValue">
            <el-input v-model="form.defaultValue" :placeholder="td('dp.modelForm.defaultValuePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="modelComment">
            <el-input
              v-model="form.description"
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.isPrimaryKey')" prop="pkFlag">
            <el-radio-group v-model="form.pkFlag" @change="handlePkFlagChange">
              <el-radio
                v-for="dict in dp_model_column_pk_flag"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.isRequired')" prop="nullableFlag">
            <el-radio-group
              v-model="form.nullableFlag"
              :disabled="form.pkFlag == 1"
            >
              <el-radio
                v-for="dict in dp_model_column_nullable_flag"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="confirmDialog"> {{ td('common.button.confirm') }} </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { defineProps, defineEmits, ref, computed, watch } from "vue";

import { getDpDataElemList } from "@/api/dp/dataElem/dataElem";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type, dp_model_column_pk_flag, dp_model_column_nullable_flag } =
  proxy.useDict(
    "column_type",
    "dp_model_column_pk_flag",
    "dp_model_column_nullable_flag"
  );

// 接收父组件传递的属性
const props = defineProps({
  visible: { type: Boolean, default: true },
  deptOptions: { type: Array, default: () => [] },
  column_type: { type: Array, default: () => [] },
  userList: { type: Array, default: () => [] },
  deptList: { type: Array, default: () => [] },
  row: { type: Object, default: () => ({}) },
  data: { type: Object, default: () => {} },
});
let title = ref();
watch(
  () => props.visible,
  (newVal) => {
    console.log("Object.keys(props.row).length === 0", props.row.index);
    if (newVal) {
      getDpDataElem();
      if (props.row && props.row.index !== undefined) {
        // 编辑状态
        title.value = td('dp.modelForm.editColumnTitle');
        Object.assign(form.value, props.row);
        form.value.authorityDept = Number(form.value.authorityDept);
      } else {
        // 新增状态
        title.value = td('dp.modelForm.addColumnTitle');
        // 重置表单
        form.value = {
          id: "",
          dataElemId: "",
          cnName: "", // 使用可选链操作符
          engName: "",
          columnType: "",
          columnLength: "",
          pkFlag: "0", // 设置默认值
          authorityDept: null,
          modelComment: "",
          nullableFlag: "0", // 设置默认值
          defaultValue: "",
          columnScale: "",
          modelId: props.data?.id, // 保存模型ID
        };
      }
    }
  },
  { immediate: true } // 新增immediate属性，确保组件挂载时就执行一次
);
let DpData = ref([]);
const handlePkFlagChange = (value) => {
  if (value == 1) {
    form.value.nullableFlag = "1";
  }
};
const getDpDataElem = async () => {
  try {
    const response = await getDpDataElemList();
    DpData.value = response.data;
    console.log("DpData", DpData.value);
  } catch (error) {
    console.error("请求失败:", error);
  }
};
const handleDatasourceChange = (value) => {
  const selectedDatasource = DpData.value.find((item) => item.id === value);
  if (selectedDatasource) {
    form.value.dataElemName = selectedDatasource.name;
    form.value.cnName = selectedDatasource.name;
    form.value.engName = selectedDatasource.engName;
    form.value.columnType = selectedDatasource.columnType;
  }
};
// 定义向父组件发送事件的接口
const emit = defineEmits(["update:dialogFormVisible", "confirm"]);

// 处理弹窗显示状态
const localVisible = computed({
  get() {
    return props.visible;
  },
  set(value) {
    emit("update:dialogFormVisible", value);
  },
});

// 表单数据和验证规则
const form = ref({
  id: "",
  dataElemId: "",
  cnName: "",
  engName: "",
  columnType: "",
  columnLength: "1",
  pkFlag: "",
  authorityDept: null,
  modelComment: "",
  nullableFlag: "",
  defaultValue: "",
  columnScale: "",
});

const rules = ref({
  cnName: [{ required: true, message: td('dp.dataElem.nameZhRequired'), trigger: "blur" }],
  engName: [
    { required: true, message: td('dp.dataElem.nameEnRequired'), trigger: "blur" },
    {
      pattern: /^[A-Za-z][A-Za-z0-9_]*$/,
      message: td('dp.modelForm.tableNamePattern'),
      trigger: "blur",
    },
  ],
  columnType: [
    { required: true, message: td('dp.modelForm.dataTypeRequired'), trigger: "blur" },
  ],
  defaultValue: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback();
          return;
        }

        // 去除首尾的单引号后再计算长度
        let actualValue = value;
        if (value.startsWith("'") && value.endsWith("'")) {
          actualValue = value.slice(1, -1);
        }

        // 对于数值类型，检查数值长度
        if (
          ["NUMBER", "DECIMAL", "NUMERIC", "FLOAT", "DOUBLE"].includes(
            form.value.columnType
          )
        ) {
          const numStr = actualValue.toString().replace(".", ""); // 移除小数点再计算长度
          if (numStr.length > form.value.columnLength) {
            callback(
              new Error(td('dp.modelForm.defaultLengthError').replace('<length>', form.value.columnLength))
            );
            return;
          }
        }
        // 对于字符类型，直接检查字符串长度
        else if (actualValue.length > form.value.columnLength) {
          callback(
            new Error(td('dp.modelForm.defaultLengthError').replace('<length>', form.value.columnLength))
          );
          return;
        }
        callback();
      },
      trigger: ["blur", "change"],
    },
  ],
});

// 添加对属性长度改变的监听
watch(
  () => form.value.columnLength,
  (newVal) => {
    // 当属性长度改变时，触发默认值的校验
    if (form.value.defaultValue) {
      proxy.$refs["dpModelRefs"]?.validateField("defaultValue");
    }
  }
);

// 关闭对话框
const closeDialog = () => {
  proxy.resetForm("dpModelRefs");
  localVisible.value = false;
  form.value = {
    id: "",
    dataElemId: "",
    cnName: "",
    engName: "",
    columnType: "",
    columnLength: "1",
    pkFlag: "",
    authorityDept: null,
    modelComment: "",
    nullableFlag: "",
    defaultValue: "",
    columnScale: "",
  };
};
// 转换输入值为大写
const convertToUpperCase = (key, value) => {
  const uppercasedValue = value.replace(/[a-z]/g, (char) => char.toUpperCase());

  form.value[key] = uppercasedValue;

  console.log("🚀 ~ convertToUpperCase ~ form.value[key]:", form.value[key]);
};

// 确认操作
const confirmDialog = () => {
  proxy.$refs["dpModelRefs"].validate((valid) => {
    if (valid) {
      emit("confirm", form.value);
      closeDialog();
    } else {
      console.log("表单验证失败");
    }
  });
};
</script>

