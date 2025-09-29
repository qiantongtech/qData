<!-- eslint-disable vue/no-parsing-error -->
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

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="输入字段" prop="taskParams.inputField"
            :rules="[{ required: true, message: '请选择输入字段', trigger: 'change' }]">
            <el-select v-model="form.taskParams.inputField" placeholder="请选择输入字段" style="width: 100%">
              <el-option v-for="item in inputFields" :key="item.value" :label="item.label" :value="item.columnName" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="输出字段" prop="taskParams.outputField"
            :rules="[{ required: true, message: '请输入输出字段', trigger: 'change' }]">
            <el-input v-model="form.taskParams.outputField" placeholder="请输入输出字段" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺省值" prop="taskParams.defaultValue">
            <el-input v-model="form.taskParams.defaultValue" placeholder="请输入缺省值" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">范围(最小 <= 值 < 最大)</span>
      </el-divider>
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAddField"> <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
        </el-row>
      </div>
      <el-table stripe height="310px" :data="tableFields" v-loading="loadingList">
        <el-table-column label="序号" type="index" width="80" align="left" />

        <el-table-column label="下限" align="left" prop="min">
          <template #default="scope">
            <el-input-number :disabled="scope.$index === 0" placeholder="请输入" v-model="scope.row.min" :min="0"
              :precision="1" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>

        <el-table-column label="上界" align="left" prop="max">
          <template #default="scope">
            <el-input-number :disabled="scope.$index === tableFields.length - 1" placeholder="请输入"
              v-model="scope.row.max" :min="0" :precision="1" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>

        <el-table-column label="值" align="left" prop="label">
          <template #header>
            <div class="justify-center">
              <span>值</span>
              <el-tooltip effect="dark" content="命中该区间时输出的文字值" placement="top">
                <el-icon>
                  <InfoFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <template #default="scope">
            <el-input v-model="scope.row.label" placeholder="请输入输出值" style="width: 100%" />
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="120">
          <template #default="scope">
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
              :disabled="scope.$index === 0 || scope.$index === tableFields.length - 1">删除</el-button>
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

  <CreateEditModal :visibleDialogs="opens" @update:visibleDialogs="opens = $event" @confirm="submitForm" :row="row"
    :tableFields="tableFields" :inputFields="inputFields" />
</template>

<script setup>
import CreateEditModal from "../fieldMergeModal.vue";
import { defineProps, defineEmits, ref, computed, watchEffect, getCurrentInstance } from "vue";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
import useUserStore from "@/store/system/user.js";
import { createNodeSelect } from "@/views/dpp/utils/opBase.js";
import { hasDuplicateObjects } from "@/utils/index.js";

const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: { type: Object, default: () => ({}) },
});

function validateArray(arr, lastArr = false) {
  // 边界处理：空数组或非数组直接返回 false（根据业务需求调整）
  if (!Array.isArray(arr)) return false;
  if (arr.length === 0) return true; // 空数组默认满足（可根据需求修改）

  // 遍历数组检查规则
  for (let i = 0; i < arr.length; i++) {
    const current = arr[i];
    // 检查当前对象是否包含 a 和 b 属性
    if (current.min === undefined || current.max === undefined) {
      console.error(`第${i}个对象缺少a或b属性`);
      return false;
    }

    // 规则 1：当前对象的 a 必须小于 b
    if (i != arr.length - 1) {
      if (current.min >= current.max) {
        console.error(`第${i}个对象：a(${current.min}) 不小于 b(${current.max})`);
        return false;
      }
    }

    // 规则 2：从第二个对象开始，当前 a 必须等于前一个对象的 b
    if (i > 0 && (lastArr || i != arr.length - 1)) {
      const prev = arr[i - 1];
      if (current.min !== prev.max) {
        console.error(`第${i}个对象a(${current.min}) 不等于第${i - 1}个对象b(${prev.max})`);
        return false;
      }
    }
  }

  // 所有规则都满足
  return true;
}

function validateRange(lastArr) {
  // 如果有任意一个已有字段 label 为空，阻止新增
  const lastEmpty = tableFields.value.find((item, index) => {
    if (index == 0) {
      return !item.label || !item.max;
    } else if (index == tableFields.value.length - 1) {
      return !item.label || !item.min;
    } else {
      return !item.label || !item.min || !item.max;
    }
  });
  if (lastEmpty) {
    proxy.$message.warning("下限/上界/值不能为空");
    return false;
  }

  // 上限下限逻辑
  let isMin = validateArray(tableFields.value, lastArr);
  if (!isMin) {
    proxy.$message.warning("请按照下限上界逻辑填写");
    return false;
  }
  // 值
  let isLabel = hasDuplicateObjects(tableFields.value, "label");
  if (isLabel) {
    proxy.$message.warning("请不要填写重复的值");
    return false;
  }

  return true;
}

function handleAddField() {
  let valid = validateRange();
  if (!valid) return;
  tableFields.value.splice(tableFields.value.length - 1, 0, {
    min: null,
    max: null,
    label: null,
    source: form.value.name,
  });
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

let originalTableFieldsBackup = ref([]);
let inputFields = ref([]);
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let dpModelRefs = ref();
let form = ref({});
let tableFields = ref([]);
let tableModel = ref([
  {
    min: null,
    max: 5,
    label: "Less than 5",
    source: form.value.name,
  },
  {
    min: 5,
    max: 10,
    label: "5-10",
    source: form.value.name,
  },
  {
    min: 10,
    max: null,
    label: "More than 10",
    source: form.value.name,
  },
]);

function handleDelete(row) {
  // 从 tableFields 中删除对应字段
  const idxTable = tableFields.value.findIndex((item) => item.label === row.label);
  if (idxTable !== -1) {
    tableFields.value.splice(idxTable, 1);
  } else {
    proxy.$message.warning("字段未找到，删除失败");
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
  tableFields.value = deepCopy(tableModel.value);
  originalTableFieldsBackup.value = [];
  form.value = {};
  row.value = {};
};

const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;
    // 逻辑校验
    let validRange = validateRange(true);
    if (!validRange) return;

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
    taskParams.tableFields = tableFields.value;
    taskParams.mainArgs = taskParams.mainArgs || { cleanRuleList: [] };
    taskParams.outputFields = [
      ...inputFields.value,
      {
        columnName: form.value.taskParams.outputField,
        source: form.value.name,
      },
    ];
    form.value.taskParams = taskParams;
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
  tableFields.value = taskParams?.tableFields.length > 0 ? taskParams?.tableFields : deepCopy(tableModel.value);
});
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
