<template>
  <el-dialog
    v-model="dialogVisible"
    draggable
    class="medium-dialog"
    :title="title"
      destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="form.ruleConfig.fieldMerge"
      :rules="formRules"
      label-width="150px"
    >
      <el-row :gutter="20">
        <!-- 选择字段（单选） -->
        <el-col :span="12">
          <el-form-item
            label="选择字段"
            prop="sourceField"
          >
            <el-select
              v-model="form.ruleConfig.fieldMerge.sourceField"
              placeholder="请选择字段名称"
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

        <!-- 合并字段（多选） -->
        <el-col :span="12">
          <el-form-item
            label="合并字段"
            prop="sourceFields"
          >
            <el-select
              v-model="form.ruleConfig.fieldMerge.sourceFields"
              placeholder="请选择字段名称"
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

        <!-- 分隔符 -->
        <el-col :span="12">
          <el-form-item
            label="分隔符"
            prop="separator"
          >
            <el-input
              v-model="form.ruleConfig.fieldMerge.separator"
              placeholder="请输入分隔符（不能包含中文）"
            />
          </el-form-item>
        </el-col>

        <!-- 空值处理 -->
        <el-col :span="12">
          <el-form-item
            label="空值处理"
            prop="handleNull"
          >
            <el-select
              v-model="form.ruleConfig.fieldMerge.handleNull"
              placeholder="请选择空值处理方式"
              filterable
            >
              <el-option label="保留为空" value="keep" />
              <el-option label="替换为默认值" value="default" />
              <el-option label="删除该条记录" value="remove" />
            </el-select>
          </el-form-item>
        </el-col>

        <!-- 默认值（仅在选择 default 时显示） -->
        <el-col :span="12" v-if="form.ruleConfig.fieldMerge.handleNull === 'default'">
          <el-form-item
            label="默认值"
            prop="defaultValue"
          >
            <el-input
              v-model="form.ruleConfig.fieldMerge.defaultValue"
              placeholder="请输入默认值"
            />
          </el-form-item>
        </el-col>

        <!-- 是否去除空格 -->
        <el-col :span="12">
          <el-form-item
            label="是否去除首尾空格"
            prop="trimSpace"
          >
            <el-radio-group v-model="form.ruleConfig.fieldMerge.trimSpace">
              <el-radio :label="true">是</el-radio>
              <el-radio :label="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed } from "vue";

// props
const props = defineProps({
  visibleDialogs: { type: Boolean, default: true },
  title: { type: String, default: "字段合并规则配置" },
  row: { type: Object, default: () => ({}) },
  tableFields: { type: Array, default: () => [] },
  fieldFields: { type: Array, default: () => [] },
  id: { type: String, default: "" },
});
const usedFields = computed(() => {
  return props.fieldFields
    ?.map(f => f?.columnName)
    .filter(Boolean); 
});



// emits
const emit = defineEmits(["update:visibleDialogs", "confirm"]);

// dialog 显示状态响应式绑定
const dialogVisible = computed({
  get: () => props.visibleDialogs,
  set: (val) => emit("update:visibleDialogs", val),
});

// 表单数据
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

// 表单引用
const formRef = ref(null);

// 表单校验规则
const formRules = {
  sourceField: [{ required: true, message: "请选择字段", trigger: "change" }],
  sourceFields: [{ required: true, message: "请选择字段", trigger: "change" }],
  separator: [
    { required: true, message: "请输入分隔符", trigger: "blur" },
    {
      pattern: /^[^\u4e00-\u9fa5]+$/,
      message: "分隔符不能包含中文",
      trigger: "blur",
    },
  ],
  handleNull: [{ required: true, message: "请选择空值处理方式", trigger: "change" }],
  defaultValue: [
    {
      validator: (rule, value, callback) => {
        if (form.value.ruleConfig.fieldMerge.handleNull === "default" && !value) {
          callback(new Error("请输入默认值"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  trimSpace: [{ required: true, message: "请选择是否去除空格", trigger: "change" }],
};

// 监听弹窗显示，弹出时初始化数据
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
            console.error("ruleConfig JSON 解析失败:", err);
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

// 重置表单并清除校验状态
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

// 关闭弹窗事件，重置表单
function closeDialog() {
  dialogVisible.value = false; // 触发 update:visibleDialogs 通知父组件关闭弹窗
  reset();
}

// 提交表单
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
