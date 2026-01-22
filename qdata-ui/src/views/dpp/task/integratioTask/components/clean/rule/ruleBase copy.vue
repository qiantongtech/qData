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
  <!-- 清洗规则基础页面   -->
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
      style="height: 600px; padding-right: 10px"
      v-show="dialogStatus == 1 || dialogStatus == 2"
      :disabled="dialogStatus == 2"
    >
      <el-form ref="formRef" :model="form" label-width="130px">
        <div class="h2-title">基础信息</div>
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="清洗名称"
              prop="name"
              :rules="
                !falg
                  ? [
                      {
                        required: true,
                        message: '请输入清洗名称',
                        trigger: 'blur',
                      },
                    ]
                  : []
              "
            >
              <el-input
                v-if="!falg"
                v-model="form.name"
                placeholder="请输入清洗名称"
              />
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="清洗规则编号" prop="ruleCode">
              <el-input
                v-if="!falg"
                v-model="form.ruleCode"
                placeholder="请输入清洗规则编号"
                disabled
              />
              <div v-else class="form-readonly">{{ form.ruleCode || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="清洗规则名称" prop="ruleName">
              <el-input
                v-if="!falg"
                v-model="form.ruleName"
                placeholder="请输入清洗规则名称"
                disabled
              />
              <div v-else class="form-readonly">{{ form.ruleName || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status" :disabled="falg">
                <el-radio :value="'1'">上线</el-radio>
                <el-radio :value="'0'">下线</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="规则描述" prop="ruleDesc">
              <el-input
                v-if="!falg"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                v-model="form.ruleDesc"
                placeholder="请输入规则描述"
              />
              <div v-else class="form-readonly textarea">
                {{ form.ruleDesc ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Where 条件" prop="whereClause">
              <el-input
                v-if="!falg"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                v-model="form.whereClause"
                placeholder="请输入 Where 条件"
              />
              <div v-else class="form-readonly textarea">
                {{ form?.whereClause ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 规则配置 -->
        <div class="h2-title">规则配置</div>
        <el-row v-if="type != 3">
          <el-col :span="24">
            <el-form-item
              label="清洗字段"
              prop="columns"
              :rules="
                !falg
                  ? [
                      {
                        required: true,
                        message: '请选择清洗字段',
                        trigger: 'blur',
                      },
                    ]
                  : []
              "
            >
              <template v-if="!falg">
                <el-select
                  v-if="isMultipleSelect"
                  v-model="form.columns"
                  placeholder="请选择清洗字段"
                  multiple
                  clearable
                >
                  <el-option
                    v-for="dict in processedFields"
                    :key="dict.columnName"
                    :label="dict.label"
                    :value="dict.columnName"
                  />
                </el-select>
                <el-select
                  v-else
                  v-model="form.columns"
                  placeholder="请选择清洗字段"
                  clearable
                >
                  <el-option
                    v-for="dict in processedFields"
                    :key="dict.columnName"
                    :label="dict.label"
                    :value="dict.columnName"
                    :disabled="shouldDisableField(dict)"
                  />
                </el-select>
              </template>
              <div v-else class="form-readonly">{{ columnsDisplayText }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <component
          :is="currentRuleComponent"
          ref="ruleComponentRef"
          :form="form.ruleConfig"
          :inputFields="processedFields"
          :falg="falg"
          :columnList="columnList"
        />
      </el-form>
    </div>
    <template #footer>
      <template v-if="dialogStatus == 1"
        ><el-button type="primary" @click="handleSave" v-if="!falg"
          >确定</el-button
        >
        <el-button @click="handleBack" v-if="!mode">返回</el-button>
        <!-- <el-button type="warning" @click="handleSpotCheck">预览</el-button> -->
      </template>
      <el-button @click="closeDialog" v-else>关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import SideMenu from "./ruleSelectorMenu.vue";
import { getRuleConfig, getRuleComponent } from "./registry.js";

import moment from "moment";
let falg = ref(false);
const { proxy } = getCurrentInstance();
const { quality_warning_status } = proxy.useDict("quality_warning_status");
const emit = defineEmits(["confirm"]);
// 父组件传入评测对象列表
const props = defineProps({
  inputFields: {
    type: Array,
    default: () => [],
  },
  type: {
    type: String,
    default: "",
  },
});

const { inputFields } = toRefs(props);
const processedFields = computed(() => {
  return inputFields.value.map((item) => ({
    ...item,
    label: item.columnComment
      ? `${item.columnName} / ${item.columnComment}`
      : item.columnName,
  }));
});
const columnsDisplayText = computed(() => {
  if (isMultipleSelect.value) {
    const values = Array.isArray(form.columns) ? form.columns : [];
    const labels = values.map((v) => {
      const f = processedFields.value.find((d) => d.columnName === v);
      return f ? f.label : v;
    });
    return labels.length ? labels.join(", ") : "-";
  } else {
    const v = form.columns;
    if (!v) return "-";
    const f = processedFields.value.find((d) => d.columnName === v);
    return f ? f.label : v;
  }
});

const dialogVisible = ref(false);
const dialogStatus = ref(1);
const dialogTitle = ref("");
const formRef = ref();

let form = reactive({
  name: "",
  ruleName: "", //清洗规则名称：
  ruleCode: "", //清洗规则编号：
  status: "1",
  // warningLevel: "2",
  whereClause: "",
  columns: "",
  tableName: "",
  ruleDesc: "",
  type: "",
  ruleConfig: {
    //数值边界调整
    max: "100",
    min: "0",
    handleType: "1",
    // 去除字符串空格
    handleType: "1", //"1-去除前后空格，2-去除所有空格"
    // 正则表达式替换
    pattern: "", //表达式
    replacement: "", //replacement
    ruleValue: [],
    deduplicationStrategy: "1",
    dataRangeValue: moment().format("YYYY-MM-DD"),
    // 数据添加值
    stringValue: "", //添加值
    // 超长字段截断
    maxLength: "100",
    direction: "1",
    // 日期格式
    targetFormat: "yyyy-MM-dd",
    inputFormats: [
      "yyyyMMdd",
      "yyyy-MM-dd",
      "yyyy/MM/dd",
      "yyyy.MM.dd",
      "yyyy-MM-dd HH:mm:ss",
      "timestamp",
    ],
  },
});
const isMultipleSelect = computed(() => {
  return form.ruleCode == "019" || form.ruleCode == "029";
});
// 新增的计算属性，用于判断字段是否应该被禁用
const shouldDisableField = computed(() => {
  return (dict) => {
    // 对于规则 025（按组合字段去重），只允许 pkFlag 为 1 的字段
    // if (form.ruleCode == "025") {
    //   return dict.pkFlag != 1;
    // }

    // 对于规则 039（清理过期记录），只允许日期类型字段
    if (
      form.ruleCode == "039" ||
      form.ruleCode == "007" ||
      form.ruleCode == "017"
    ) {
      const isDateType =
        dict.columnType?.toUpperCase().includes("DATE") ||
        dict.columnType?.toUpperCase().startsWith("TIMESTAMP") ||
        dict.columnType?.toUpperCase() === "TIME" ||
        dict.columnType?.toUpperCase() === "YEAR";
      return !isDateType;
    }

    // 对于规则 007 日期格式
    // if (form.ruleCode == "007") {
    //   const isStringType =
    //     dict.columnType?.toUpperCase().includes("CHAR") ||
    //     dict.columnType?.toUpperCase().includes("TEXT") ||
    //     dict.columnType?.toUpperCase().includes("VARCHAR") ||
    //     dict.columnType?.toUpperCase().includes("STRING");
    //   return !isStringType;
    // }

    return false;
  };
});
let title = ref();

// 计算属性：当前规则配置
const currentRuleConfig = computed(() => {
  return getRuleConfig(form.ruleCode);
});

// 计算属性：当前规则组件
const currentRuleComponent = computed(() => {
  return getRuleComponent(form.ruleCode) || getRuleComponent("EMPTY");
});

let loading = ref(false);
let columnList = ref([]);

let ruleComponentRef = ref();
async function handleSave() {
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning("请完善必填项");
    return;
  }
  let res = { valid: true, data: {} };
  res = await ruleComponentRef.value?.validate();
  if (!res.valid) return;
  if (!isMultipleSelect.value) {
    form.columns = [form.columns];
  }
  if (form.ruleCode == "035") {
  }
  const formCopy = JSON.parse(
    JSON.stringify({
      ...form,
      ruleConfig: JSON.stringify({
        columns: form.columns,
        ...res.data,
        parentName: form.parentName,
      }),
    })
  );

  emit("confirm", formCopy, mode.value);
}
let sampleCheckMsg = ref();

function handleCardClick(data) {
  resetForm();
  form.ruleName = data?.name;
  form.ruleCode = data?.code;
  form.ruleType = data?.strategyKey;
  form.type = data?.type;
  form.parentName = data?.parentName;
  form.dimensionType = data?.qualityDim;
  dialogTitle.value = `新增清洗规则${data?.name ? "-" + data.name : ""}`;
  dialogStatus.value = 1;
}
let mode = ref();
async function openDialog(record, index, fg) {
  falg.value = fg;
  mode.value = index;
  resetForm();
  dialogTitle.value = `${mode.value ? "修改" : "新增"}清洗规则${
    record?.ruleName ? `-${record.ruleName}` : ""
  }`;
  if (falg?.value) {
    dialogTitle.value = `清洗规则${
      record?.ruleName ? `-${record.ruleName}` : ""
    }`;
  }
  dialogStatus.value = mode.value ? 1 : 0;
  dialogVisible.value = true;

  if (index) {
    dialogStatus.value = 1;
    const { ruleType, ruleConfig, columns, ...rest } = record;
    Object.assign(form, rest);
    form.ruleType = ruleType;

    try {
      form.ruleConfig =
        typeof ruleConfig == "string" ? JSON.parse(ruleConfig) : ruleConfig;
    } catch (e) {
      form.ruleConfig = {};
    }
    if (isMultipleSelect.value) {
      form.columns = Array.isArray(columns) ? columns : [];
    } else {
      form.columns =
        Array.isArray(columns) && columns.length > 0 ? columns[0] : "";
    }
  } else {
    resetForm();
  }
}

const initialForm = () => ({
  id: "",
  name: "",
  type: "",
  ruleName: "", //清洗规则名称：
  ruleCode: "", //清洗规则编号：
  status: "1",
  whereClause: "",
  columns: isMultipleSelect.value ? [] : "",
  tableName: "",
  ruleDesc: "",
  ruleConfig: {
    //数值边界调整
    max: "100",
    min: "0",
    handleType: "1",
    // 去除字符串空格
    handleType: "1", //"1-去除前后空格，2-去除所有空格"
    // 正则表达式替换
    pattern: "", //表达式
    replacement: "", //replacement

    ruleValue: [],
    deduplicationStrategy: "1",
    // 枚举值映射标准化
    stringValue: [],
    dataRange: "1", // 0：固定时间范围，1：具体日期
    dataRangeType: "1", // 0：天前
    dataRangeValue: moment().format("YYYY-MM-DD"),
    handleType: "1", // 0：过期处理方式，1：删除记录
    handleColumns: "", // // 标记字段     选中过期处理方式才会有
    handleValue: "", // 标记值       选中过期处理方式才会有
    // 超长字段截断
    maxLength: "0",
    direction: "1",
    // 日期格式
    targetFormat: "",
    inputFormats: [
      "yyyyMMdd",
      "yyyy-MM-dd",
      "yyyy/MM/dd",
      "yyyy.MM.dd",
      "yyyy-MM-dd HH:mm:ss",
      "timestamp",
    ],
    // 字段值替换
    mode: "1", // 1-白名单，2-黑名单
    allowed: [], //清洗值
    defaultValue: "", //默认值
    ignoreCase: "1", // 1-大小写敏感，2-大小写不敏感
    caseSensitive: "1", // 1-去除空格，2-不去除空格
    ignoreNullValue: "1", // 1-忽略null，2-不忽略null
    // 日期空值填充
    fillType: "3", //1=当前日期, 2=昨天, 3=固定值
    defaultValue: "", // 固定值 fillType=3 时使用
    format: "", // 日期格式
  },
});

function resetForm() {
  Object.assign(form, initialForm());
  columnList.value = [];
  title.value = "";
  sampleCheckMsg.value = "";
}

function closeDialog() {
  dialogVisible.value = false;
  resetForm();
}

function handleBack() {
  dialogStatus.value = 0;
  dialogTitle.value = `新增清洗规则`;
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
