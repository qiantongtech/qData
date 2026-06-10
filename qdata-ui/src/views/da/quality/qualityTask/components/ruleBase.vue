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
  <!-- 新增评测规则的 每个规则的配置 -->
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
      style="max-height: 650px; overflow-y: auto; padding-right: 10px"
      v-show="dialogStatus == 1 || dialogStatus == 2"
      :disabled="dialogStatus == 2"
    >
      <el-form ref="formRef" :model="form" label-width="130px">
        <div class="h2-title">基础信息</div>
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="评测名称"
              prop="name"
              v-if="type != 3"
              :rules="[
                { required: true, message: '请输入评测名称', trigger: 'blur' },
              ]"
            >
              <template v-if="!falg">
                <el-input v-model="form.name" placeholder="请输入评测名称" />
              </template>
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
            <el-form-item
              label="稽查名称"
              prop="name"
              v-else
              :rules="[
                { required: true, message: '请输入稽查名称', trigger: 'blur' },
              ]"
            >
              <template v-if="!falg">
                <el-input v-model="form.name" placeholder="请输入稽查名称" />
              </template>
              <div v-else class="form-readonly">{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="稽查规则编号" prop="ruleCode">
              <div class="form-readonly">{{ form.ruleCode || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="稽查规则名称" prop="ruleName">
              <div class="form-readonly">{{ form.ruleName || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="告警等级" prop="warningLevel">
              <template v-if="!falg">
                <el-select
                  v-model="form.warningLevel"
                  placeholder="请选择质量维度"
                  style="width: 290px"
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
            <el-form-item :label="t('common.texts.status')" prop="status">
              <el-radio-group v-model="form.status" :disabled="falg">
                <el-radio :value="'1'">上线</el-radio>
                <el-radio :value="'0'">下线</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="规则描述" prop="ruleDescription">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  maxlength="500个字符"
                  show-word-limit
                  v-model="form.ruleDescription"
                  placeholder="请输入规则描述"
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
            <el-form-item label="错误示例" prop="errDescription">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  maxlength="500个字符"
                  show-word-limit
                  v-model="form.errDescription"
                  placeholder="请输入错误示例"
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
            <el-form-item label="修复建议" prop="suggestion">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  maxlength="500个字符"
                  show-word-limit
                  v-model="form.suggestion"
                  placeholder="请输入修复建议"
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
            <el-form-item label="Where 条件" prop="whereClause">
              <template v-if="!falg">
                <el-input
                  type="textarea"
                  maxlength="500个字符"
                  show-word-limit
                  v-model="form.whereClause"
                  placeholder="请输入 Where 条件"
                />
              </template>
              <div v-else class="form-readonly textarea">
                {{ form.whereClause || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 规则配置 -->
        <div class="h2-title">规则配置</div>
        <el-row>
          <el-col :span="12" class="hasMsg" v-if="type != 3">
            <el-form-item
              label="评测对象"
              prop="tableName"
              :rules="[
                {
                  required: true,
                  message: '请选择评测对象',
                  trigger: 'change',
                },
              ]"
            >
              <template v-if="!falg">
                <el-select
                  v-model="form.tableName"
                  placeholder="请选择评测对象"
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
            v-if="form.ruleType != 'TIME_ORDER_VALIDATION' && type != 3"
          >
            <el-form-item
              label="检查字段"
              prop="evaColumn"
              :rules="[
                {
                  required: true,
                  message: '请选择检查字段',
                  trigger: 'change',
                },
              ]"
            >
              <template v-if="!falg">
                <el-select
                  v-if="isMultipleRuleType"
                  v-model="form.evaColumn"
                  multiple
                  placeholder="请选择检查字段"
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
                  placeholder="请选择检查字段"
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
          :dppQualityTaskObjSaveReqVO="dppQualityTaskObjSaveReqVO"
          :falg="falg"
          :columnList="columnList"
        />

        <div class="h2-title" v-if="form.ruleType == 'CHARACTER_VALIDATION'">
          样例监测
        </div>
        <el-row v-if="form.ruleType == 'CHARACTER_VALIDATION'">
          <el-col :span="12">
            <el-form-item label="样例数据" prop="sampleData">
              <el-input v-model="title" placeholder="请输入样例数据" />
              <!-- <span class="msg">样例必须符合规则，如不符合不能包含特殊字符</span> -->
              <div style="margin-top: 6px; display: inline-block">
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
              <i class="iconfont-mini icon-a-zu22377 mr5"></i>监测
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      <SpotCheckDialog ref="spotCheckRef" />
    </div>
    <template #footer>
      <template v-if="dialogStatus == 1">
        <el-button type="warning" v-if="type != 3" @click="handleSpotCheck"
          >抽查</el-button
        >
        <el-button @click="handleBack" v-if="!mode">{{ t('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="handleSave" v-if="!falg"
          >{{ t('common.button.confirm') }}</el-button
        >
      </template>
      <el-button @click="closeDialog" v-else>{{ t('common.button.cancel') }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, reactive, watch, toRefs } from "vue";
import SideMenu from "./ruleSelectorMenu.vue";
import SpotCheckDialog from "./spotCheckResult.vue";
import { getColumnByAssetId } from "@/api/dpp/task/index.js";
// 通过注册中心按需加载规则子组件，减少静态 import 带来的首屏体积
import { getRuleConfig, getRuleComponent } from "./rule/registry.js";
import { verifyInterfaceValue } from "@/api/da/quality/qualityTask";

const { t } = useI18n();
let falg = ref(false);
const { proxy } = getCurrentInstance();
const { quality_warning_status } = proxy.useDict("quality_warning_status");
const emit = defineEmits(["confirm"]);
// 父组件传入评测对象列表
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
  ruleName: "", //稽查规则名称：
  ruleCode: "", //稽查规则编号：
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
    // 字符串类型校验
    allowedChars: ["1"], // 允许字符类型
    useRegexFlag: 0, // 使用正则
    regex: "", // 正则表达式
    // 忽略空值：，保留一个
    ignoreNullValue: "0", //忽略空值：
    // 字段长度范围校验
    minLength: null, //最小长度
    maxLength: null, //最大长度
    // 字段精度
    scale: "2", // 小数位数
    skipInteger: "1", // 忽略整数值
    // 字段组完整性校验
    fillStrategy: "1",

    // 数值字段范围校验
    minValue: null,
    maxValue: null,
    includeBoundary: "1",
    //  枚举值校验
    useCodeTable: "0",
    ruleCodeTableId: "",
    ignoreCase: "0",
    codeList: [],
    validValues: [],
    calculationGroups: [],
    // 时间选择
    conditions: [],
  },
});
const isMultipleRuleType = computed(
  () =>
    form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
    form.ruleType == "GROUP_FIELD_COMPLETENESS"
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

// 计算属性：当前规则配置
const currentRuleConfig = computed(() => {
  return getRuleConfig(form.ruleType);
});

// 计算属性：当前规则组件
const currentRuleComponent = computed(() => {
  return getRuleComponent(form.ruleType) || null;
});

let loading = ref(false);
let columnList = ref([]);
// 评测字段列表缓存：key = datasourceId|tableName
const columnsCache = new Map();

const spotCheckRef = ref();

//监测
async function handleSpotCheck() {
  console.log(
    "🚀 ~ handleSpotCheck ~  selectedRef.value:",
    selectedRef.value.datasourceId
  );
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning("校验未通过，请完善必填项");
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;
    // 如果子组件返回了evaColumn字段（如timeOrderRule组件），直接赋值给form.evaColumn
    if (res.data?.evaColumn) {
      form.evaColumn = res.data.evaColumn;
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
  console.log(
    "🚀 ~ handleSpotCheck ~  formCopy.evaColumn:",
    formCopy.evaColumn
  );

  let obj = {
    ...formCopy,
    datasourceId: selectedRef.value?.datasourceId,
    title: title.value,
  };
  // let resw = await validationErrorDataSql(obj)
  spotCheckRef.value.openDialog(obj);
}
function handleTargetObjectChange(tableName) {
  const selected = dppQualityTaskObjSaveReqVO.value.find(
    (item) => item.tableName == tableName
  );
  console.log("🚀 ~ handleTargetObjectChange ~ selected:", selected);
  if (selected) {
    form.datasourceId = selected.datasourceId;
    if (
      form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
      form.ruleType == "GROUP_FIELD_COMPLETENESS"
    ) {
      console.log("2222");

      form.evaColumn = [];
    } else {
      form.evaColumn = "";
    }
    // 清空当前表的缓存，确保每次切换都重新请求
    const cacheKey = `${selected.datasourceId}|${tableName}`;
    columnsCache.delete(cacheKey);
    fetchColumns();
  } else {
    form.datasourceId = null;
    form.tableName = "";
    if (
      form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
      form.ruleType == "GROUP_FIELD_COMPLETENESS"
    ) {
      form.evaColumn = [];
    } else {
      form.evaColumn = "";
    }

    columnList.value = [];
  }
}
async function fetchColumns() {
  console.log("🚀 ~ fetchColumns ~ selectedRef:", selectedRef.value);

  if (!selectedRef.value.datasourceId || !form?.tableName) {
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
    proxy.$message.warning("校验未通过，请完善必填项");
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;

    // 如果子组件返回了evaColumn字段（如timeOrderRule组件），直接赋值给form.evaColumn
    if (res.data?.evaColumn) {
      form.evaColumn = res.data.evaColumn;
    }
  }
  const selectedLabels = columnList.value.map((col) => ({
    name: col.columnName,
    label: col.label,
  }));
  // 先把 evaColumn 数组转为逗号分隔字符串
  if (Array.isArray(form.evaColumn)) {
    form.evaColumn = form.evaColumn.join(",");
  }
  // 构建最终的 rule 字段
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
    return proxy.$message.warning("校验未通过，请添加样例数据");
  }
  await nextTick();
  try {
    await formRef?.value?.validate();
  } catch (err) {
    proxy.$message.warning("校验未通过，请完善必填项");
    return;
  }
  let res = { valid: true, data: {} };
  if (form.ruleType !== "COMPOSITE_UNIQUENESS_VALIDATION") {
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;
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
    sampleCheckMsg.value = resw.msg || "检测失败";
  }
}
function handleCardClick(data) {
  resetForm();
  if (props.type == 2) {
    form.tableName = props.tableName;
  }
  if (props.type == 3) {
    form.tableName = props.tableName;
  }
  form.ruleName = data?.name;
  form.ruleCode = data?.code;
  form.ruleType = data?.strategyKey;
  form.dimensionType = data?.qualityDim;
  const prefix = props?.type == 3 ? "新增稽查规则" : "新增评测规则";
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
  const prefix = props?.type == 3 ? "稽查规则" : "评测规则";
  if (falg.value) {
    dialogTitle.value = `${prefix}详情${
      record?.ruleName ? `-${record.ruleName}` : ""
    }`;
  } else {
    dialogTitle.value = `${mode.value ? t('common.button.update') : t('common.button.add')}${prefix}${
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
    console.log("🚀 ~ openDialog ~ form.ruleType:", form.ruleType);
    if (props.type == 2) {
      form.tableName = props.tableName;
    }
    if (
      form.ruleType == "COMPOSITE_UNIQUENESS_VALIDATION" ||
      form.ruleType == "GROUP_FIELD_COMPLETENESS"
    ) {
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
  ruleName: "", //稽查规则名称：
  ruleCode: "", //稽查规则编号：
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
    // 字符串类型校验
    allowedChars: ["1"], // 允许字符类型
    useRegexFlag: 0, // 使用正则
    regex: "", // 正则表达式
    ignoreNullValue: "1", // 忽略空值：

    // 字段长度范围校验
    minLength: null, // 最小长度
    maxLength: null, // 最大长度

    // 字段精度
    scale: "2", // 小数位数
    skipInteger: "1", // 忽略整数值

    // 字段组完整性校验
    fillStrategy: "1",

    // 数值字段范围校验
    minValue: null,
    maxValue: null,
    includeBoundary: "1",

    // 枚举值校验
    useCodeTable: "0",
    ruleCodeTableId: "",
    ignoreCase: "0",
    codeList: [],
    validValues: [],
    calculationGroups: [],

    // 时间字段
    allowPartialEmpty: "1",

    // 多条件字段
    conditions: [],
  },
});

function resetForm() {
  Object.assign(form, initialForm());
  columnList.value = [];
  columnsCache.clear(); // 清空字段列表缓存
  title.value = "";
  sampleCheckMsg.value = "";
}

function closeDialog() {
  dialogVisible.value = false;
  resetForm();
}

function handleBack() {
  dialogStatus.value = 0;
  dialogTitle.value = `新增评测规则`;
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
