<template>
  <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
    showCancelButton :show-close="false" destroy-on-close>
    <el-form ref="dpModelRefs" :model="form" label-width="140px" @submit.prevent v-loading="loading" :disabled="info">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="节点名称" prop="name" :rules="[{ required: true, message: '请输入节点名称', trigger: 'change' }]">
            <el-input v-model="form.name" placeholder="请输入节点名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="taskParams.typeName">
            <el-select v-model="form.taskParams.typeName" placeholder="请输入类型" filterable disabled>
              <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">常量字段</span>
      </el-divider>
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAddField"> <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
        </el-row>
      </div>
      <el-table stripe height="500px" :data="tableFields" v-loading="loadingList">
        <el-table-column label="序号" type="index" width="80" align="left" />

        <el-table-column label="字段名称" align="left" prop="columnName">
          <template #default="scope">
            <el-input v-model="scope.row.columnName" placeholder="请输入" style="width: 100%" />
          </template>
        </el-table-column>

        <el-table-column label="字段类型" align="left" prop="type" width="150">
          <template #default="scope">
            <el-select v-model="scope.row.type" placeholder="请选择" style="width: 100%">
              <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column label="默认值" align="left" prop="defaultValue">
          <template #default="scope">
            <el-input v-model="scope.row.defaultValue" placeholder="请输入" style="width: 100%" />
          </template>
        </el-table-column>

        <el-table-column label="设为空串" align="left" prop="emptyString" width="150">
          <template #header>
            <div class="justify-center">
              <span>设为空串</span>
              <el-tooltip effect="dark" content="勾选后，即使“默认值”字段填写了内容，也会被覆盖为空字符串" placement="top">
                <el-icon>
                  <InfoFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <template #default="scope">
            <el-select v-model="scope.row.emptyString" placeholder="请选择" style="width: 100%">
              <el-option label="是" :value="true" />
              <el-option label="否" :value="false" />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="120">
          <template #default="scope">
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <FieldConflictDialog v-model="showConflictDialog" :existingFields="tableFields" :newFields="inputFields"
    @resolve="onResolveFields" />
  <CreateEditModal :visibleDialogs="opens" @update:visibleDialogs="opens = $event" @confirm="submitForm" :row="row"
    :tableFields="tableFields" :inputFields="inputFields" />
</template>

<script setup>
import CreateEditModal from "../fieldMergeModal.vue";
import FieldConflictDialog from "../fieldDetection.vue";
import { defineProps, defineEmits, ref, computed, watchEffect, getCurrentInstance } from "vue";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
import useUserStore from "@/store/system/user.js";
import { createNodeSelect } from "@/views/dpp/utils/opBase.js";
import { hasDuplicateObjects } from "@/utils/index.js";

const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const columntype = [
  { value: "BigNumber", label: "BigNumber" },
  { value: "Binary", label: "Binary" },
  { value: "Boolean", label: "Boolean" },
  { value: "Date", label: "Date" },
  { value: "Integer", label: "Integer" },
  { value: "Internet Address", label: "Internet Address" },
  { value: "Number", label: "Number" },
  { value: "String", label: "String" },
  { value: "Timestamp", label: "Timestamp" },
];
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: { type: Object, default: () => ({}) },
});

function handleAddField() {
  // 如果有任意一个已有字段 columnName 为空，阻止新增
  const lastEmpty = tableFields.value.find((item) => !item.columnName);
  if (lastEmpty) {
    proxy.$message.warning("添加失败，请先填写当前字段名称");
    return;
  }
  // 最后一行名称
  let isRepeat = hasDuplicateObjects(tableFields.value, "columnName");
  if (isRepeat) {
    proxy.$message.warning("请不要填写重复的字段名称");
    return;
  }

  tableFields.value.push({
    columnName: null,
    type: "String",
    defaultValue: null,
    emptyString: false,
    source: form.value.name,
  });
}

const showConflictDialog = ref(false);

function onResolveFields(payload) {
  if (!payload) return;
  switch (payload.action) {
    case "addNewOnly":
      console.log("父组件：只增加新字段");
      break;
    case "addAll":
      console.log("父组件：增加所有字段");
      break;
    case "clearAndAddAll":
      tableFields.value = deepCopy(originalTableFieldsBackup.value);
      console.log("🚀 ~ onResolveFields ~  tableFields.value:", tableFields.value);
      console.log("父组件：清空并增加所有字段");
      break;
    case "cancel":
      console.log("父组件：取消操作");
      break;
  }
}

const emit = defineEmits(["update", "confirm"]);

const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update", newValue);
  },
});

let tableFields = ref([]);
let originalTableFieldsBackup = ref([]);
let inputFields = ref([]);
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let dpModelRefs = ref();
let form = ref({});

function handleDelete(row) {
  // 从 tableFields 中删除对应字段
  const idxTable = tableFields.value.findIndex((item) => item.columnName === row.columnName);
  if (idxTable !== -1) {
    tableFields.value.splice(idxTable, 1);
  } else {
    proxy.$message.warning("删除失败，字段未找到");
  }

  // 恢复 inputFields 中被删除字段的原始状态（如果有）
  const originalField = originalTableFieldsBackup.value.find((item) => item.columnName === row.columnName);
  if (originalField) {
    const idxField = inputFields.value.findIndex((item) => item.columnName === row.columnName);
    if (idxField !== -1) {
      inputFields.value[idxField] = deepCopy(originalField);
    } else {
      inputFields.value.push(deepCopy(originalField));
    }
  }
}

// 提交弹窗规则数据
const submitForm = (value) => {
  if (!value || !Array.isArray(value)) return;

  value.forEach((ruleItem) => {
    if (!ruleItem?.ruleConfig) return;

    let parsedConfig;
    try {
      parsedConfig = JSON.parse(ruleItem.ruleConfig);
    } catch (e) {
      console.warn("无法解析 ruleConfig:", e, ruleItem.ruleConfig);
      return;
    }
    const sourceField = parsedConfig?.fieldMerge?.sourceField;
    if (!sourceField) return;

    const tableIndex = tableFields.value.findIndex((item) => item.columnName == sourceField);
    if (tableIndex !== -1) {
      const updatedItem = {
        ...tableFields.value[tableIndex],
        cleanRuleList: [ruleItem],
        elementId: [ruleItem.ruleId],
      };
      tableFields.value[tableIndex] = updatedItem;

      const fieldIndex = inputFields.value.findIndex((item) => item.columnName == sourceField);
      if (fieldIndex !== -1) {
        inputFields.value[fieldIndex] = updatedItem;
      } else {
        inputFields.value.push(updatedItem);
      }
    }
  });
  opens.value = false;
};

const off = () => {
  proxy.resetForm("dpModelRefs");
  tableFields.value = [];
  originalTableFieldsBackup.value = [];
  form.value = {};
  row.value = {};
};

const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;

    // 校验 tableFields 不为空
    if (!Array.isArray(tableFields.value) || tableFields.value.length === 0) {
      proxy.$message.warning("请至少一个字段值");
      return;
    }

    if (tableFields.value.length > 0) {
      const hasEmptyName = tableFields.value.some((item) => !item.columnName?.trim());
      if (hasEmptyName) {
        proxy.$message.warning("校验未通过，字段名称不能为空");
        return;
      }
    }

    // 最后一行名称
    let isRepeat = hasDuplicateObjects(tableFields.value, "columnName");
    if (isRepeat) {
      proxy.$message.warning("请不要填写重复的字段名称");
      return;
    }

    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false;
      form.value.code = response.data;
    }

    const taskParams = form.value.taskParams || {};
    const splitField = form.value.taskParams.splitField;
    const inputWithoutSplit = inputFields.value.filter((item) => item.columnName !== splitField);
    taskParams.outputFields = [...inputWithoutSplit, ...tableFields.value.map((item) => ({ ...item, columnType: item.type }))];
    taskParams.tableFields = tableFields.value.map((field) => ({ ...field, name: field.columnName }));
    taskParams.mainArgs = taskParams.mainArgs || { cleanRuleList: [] };
    form.value.taskParams = taskParams;
    console.log("保存数据 - outputFields:", taskParams.outputFields);
    emit("confirm", form.value);
    // closeDialog();
  } catch (error) {
    console.error("保存数据失败:", error);
    loading.value = false;
  }
};

const closeDialog = () => {
  off();
  emit("update", false);
};

function deepCopy(data) {
  if (data === undefined || data === null) {
    return {};
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    console.log(e, "deepCopy error");
    return {};
  }
}

let nodeOptions = ref([]);
watchEffect(() => {
  if (!props.visible) {
    off();
    return;
  }
  form.value = deepCopy(props.currentNode?.data || {});
  nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
  let taskParams = deepCopy(props.currentNode?.data?.taskParams || {});
  originalTableFieldsBackup.value = deepCopy(props.currentNode?.data?.taskParams?.tableFields || []);
  inputFields.value = taskParams?.inputFields || [];
  tableFields.value = taskParams?.tableFields || [];
});
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
