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
  <el-dialog
    v-model="visibleDialog"
    :draggable="true"
    class="medium-dialog"
    :title="form.taskParams.typeName"
    showCancelButton
    :show-close="false"
    destroy-on-close
  >
    <el-form
      ref="dpModelRefs"
      :model="form"
      label-width="110px"
      @submit.prevent
      v-loading="loading"
      :disabled="info"
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.nodeName', '节点名称')"
            prop="name"
            :rules="[
              { required: true, message: td('dpp.integration.nodeNameRequired', '请输入节点名称'), trigger: 'change' },
            ]"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.name"
              :placeholder="td('dpp.integration.nodeNamePlaceholder', '请输入节点名称')"
            />
            <div v-else class="form-readonly">{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.type', '类型')" prop="typeName" :label-position="labelPosition">
            <template v-if="!info">
              <el-select
                v-model="form.taskParams.typeName"
                :placeholder="td('dpp.integration.typePlaceholder', '请输入类型')"
                filterable
                disabled
              >
                <el-option
                  v-for="dict in typeList"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
            <div v-else class="form-readonly">
              {{ form.taskParams.typeName }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.description"
              type="textarea"
              :maxlength="500"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
            <div v-else class="form-readonly textarea">
              {{ form.description || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="center">
        <span class="blue-text">{{ td('dpp.integration.selectAndModifyFields', '需要选择与修改的字段') }}</span>
      </el-divider>
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAddField">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              @click="handleFetchFields"
              :disabled="info"
              v-if="!info"
              >{{ td('dpp.integration.fetchFields') }}</el-button
            >
          </el-col>
        </el-row>
      </div>
      <el-table
        stripe
        height="310px"
        :data="tableFields"
        v-loading="loadingList"
        ref="dragTable"
        row-key="columnName"
      >
        <el-table-column :label="td('common.display.index', '序号')" width="80" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.fieldName', '字段名称')" align="left" prop="columnName">
          <template #default="scope">
            <el-select
              v-model="scope.row.columnName"
              :placeholder="td('dpp.integration.selectFieldPlaceholder', '请选择字段')"
              style="flex: 1"
            >
              <el-option
                v-for="item in inputFields"
                :key="item.value"
                :label="item.label"
                :value="item.columnName"
                :disabled="isOptionDisabled(item.columnName, scope.row)"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.integration.fieldAlias', '字段别名')"
          align="left"
          prop="outputField"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            <el-input
              v-model="scope.row.outputField"
              :placeholder="td('dpp.integration.fieldAliasPlaceholder', '请输入新的字段名称')"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.fieldType', '字段类型')" align="left" prop="type" width="150">
          <template #default="scope">
            <el-select
              v-model="scope.row.type"
              :placeholder="td('dpp.integration.fieldTypePlaceholder', '请选择字段类型')"
              style="width: 100%"
              clearable
            >
              <el-option
                v-for="dict in columntype"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.column.fieldLength', '字段长度')"
          align="left"
          prop="length"
          width="150"
        >
          <template #default="scope">
            <el-input-number
              :placeholder="td('dpp.integration.fieldLengthPlaceholder', '请输入字段长度')"
              v-model="scope.row.length"
              :min="0"
              controls-position="right"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.integration.fieldPrecision', '字段精度')"
          align="left"
          prop="precision"
          width="150"
        >
          <template #default="scope">
            <el-input-number
              :placeholder="td('dpp.integration.fieldPrecisionPlaceholder', '请输入字段精度')"
              v-model="scope.row.precision"
              :min="0"
              controls-position="right"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="150"
          v-if="!info"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
            >
              {{ td('common.button.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-divider content-position="center">
        <span class="blue-text">{{ td('dpp.integration.fieldsToRemove', '需要移除的字段') }}</span>
      </el-divider>
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAddField2">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
        </el-row>
      </div>
      <el-table
        stripe
        height="310px"
        :data="removeFields"
        v-loading="loadingList"
        ref="dragTable"
        row-key="columnName"
      >
        <el-table-column :label="td('common.display.index', '序号')" width="80" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.fieldName', '字段名称')" align="left" prop="columnName">
          <template #default="scope">
            <el-select
              v-model="scope.row.columnName"
              :placeholder="td('dpp.integration.selectFieldPlaceholder', '请选择字段')"
              style="flex: 1"
            >
              <el-option
                v-for="item in inputFields"
                :key="item.value"
                :label="item.label"
                :value="item.columnName"
                :disabled="isOptionDisabled(item.columnName, scope.row)"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="150"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete2(scope.row)"
            >
              {{ td('common.button.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
        <el-button type="primary" @click="saveData" v-if="!info"
          >{{ td('common.button.save') }}</el-button
        >
      </div>
    </template>
  </el-dialog>

  <FieldConflictDialog
    v-model="showConflictDialog"
    :existingFields="tableFields"
    :newFields="inputFields"
    @resolve="onResolveFields"
  />
  <CreateEditModal
    :visibleDialogs="opens"
    @update:visibleDialogs="opens = $event"
    @confirm="submitForm"
    :row="row"
    :tableFields="tableFields"
    :inputFields="inputFields"
  />
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import CreateEditModal from "../fieldMergeModal.vue";
import FieldConflictDialog from "../fieldDetection.vue";
import {
  defineProps,
  defineEmits,
  ref,
  computed,
  watchEffect,
  getCurrentInstance,
} from "vue";

import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
import useUserStore from "@/store/system/user.js";
import { createNodeSelect } from "@/views/dpp/utils/opBase.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const userStore = useUserStore();

const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '' },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: { type: Object, default: () => ({}) },
});
const columntype = [
  { value: "BigNumber", label: "BigNumber" },
  { value: "Binary", label: "Binary" },
  { value: "Boolean", label: "Boolean" },
  { value: "Date", label: "Date" },
  { value: "Integer", label: "Integer" },
  { value: "InternetAddress", label: "InternetAddress" },
  { value: "Number", label: "Number" },
  { value: "String", label: "String" },
  { value: "Timestamp", label: "Timestamp" },
];

let dragTable = ref(null);

function hasDuplicateObjects(arr, key) {
  if (arr.length <= 1) return false;
  const seen = new Set(); // 记录已出现的键值
  for (const item of arr) {
    const value = item[key];
    // 若当前键值已存在于Set中，说明有重复
    if (seen.has(value)) {
      return true;
    }
    value !== "" && seen.add(value);
  }
  return false;
}
function handleAddField() {
  if (!Array.isArray(inputFields.value) || inputFields.value.length === 0) {
    proxy.$message.warning(td("dpp.integration.inputFieldEmptyCannotAdd", "输入字段为空，无法添加字段"));
    return;
  }
  // 已添加的字段名
  const tableUsedNames = tableFields.value.map((item) => item.columnName);
  const removeUsedNames = removeFields.value.map((item) => item.columnName);
  const usedNames = [...tableUsedNames, ...removeUsedNames];
  // 找到未使用的字段
  const nextField = inputFields.value.find(
    (item) => !usedNames.includes(item.columnName)
  );

  if (!nextField) {
    proxy.$message.warning(td("dpp.integration.noMoreFieldsToAdd", "新增失败，已无可添加的字段"));
    return;
  }

  let isRepeat = hasDuplicateObjects(tableFields.value, "outputField");
  if (isRepeat) {
    proxy.$message.warning(td("dpp.integration.noRepeatOutputField", "请不要填写重复输出字段"));
    return;
  }
  let names = inputFields.value.map((item) => item.columnName);
  let isOut = names.find((item) =>
    tableFields.value.some((row) => row.outputField === item)
  );
  if (isOut) {
    proxy.$message.warning(td("dpp.integration.outputFieldCannotDuplicate", "输出字段不能与已有字段名称重复"));
    return;
  }

  tableFields.value.push({
    columnName: nextField.columnName,
    columnType: nextField.columnType,
    outputField: "",
    type: "",
    length: null,
    precision: null,
    ignoreCase: 1,
    source: form.value.name,
  });
}
function handleAddField2() {
  if (!Array.isArray(inputFields.value) || inputFields.value.length === 0) {
    proxy.$message.warning(td("dpp.integration.inputFieldEmptyCannotAdd", "输入字段为空，无法添加字段"));
    return;
  }
  // 已添加的字段名
  const tableUsedNames = tableFields.value.map((item) => item.columnName);
  const removeUsedNames = removeFields.value.map((item) => item.columnName);
  const usedNames = [...tableUsedNames, ...removeUsedNames];
  // 找到未使用的字段
  const nextField = inputFields.value.find(
    (item) => !usedNames.includes(item.columnName)
  );

  if (!nextField) {
    proxy.$message.warning(td("dpp.integration.noMoreFieldsAvailable", "已无可添加的字段"));
    return;
  }

  removeFields.value.push({
    columnName: nextField.columnName,
    columnType: nextField.columnType,
    ignoreCase: 1,
    source: form.value.name,
  });
}
const showConflictDialog = ref(false);

const handleFetchFields = () => {
  showConflictDialog.value = true;
};
function onResolveFields(payload) {
  if (!payload || !payload.action) return;
  const tableNames = tableFields.value.map((f) => f.columnName).sort();
  const inputNames = inputFields.value.map((f) => f.columnName).sort();
  const isEqual =
    tableNames.length === inputNames.length &&
    tableNames.every((name, idx) => name === inputNames[idx]);
  switch (payload.action) {
    case "addNewOnly": {
      console.log("父组件：只增加新字段");

      // 计算已有字段名称
      const tableUsedNames = tableFields.value.map((item) => item.columnName);
      const removeUsedNames = removeFields.value.map((item) => item.columnName);
      const existingNames = [...tableUsedNames, ...removeUsedNames];
      // 找到新字段中不在已有字段中的字段
      const newUniqueFields = inputFields.value.filter(
        (f) => !existingNames.includes(f.columnName)
      );
      // 加入到 tableFields 中
      tableFields.value = tableFields.value.concat(deepCopy(newUniqueFields));
      break;
    }

    case "addAll": {
      console.log(
        "🚀 ~ onResolveFields ~  tableFields.value =:",
        tableFields.value
      );
      if (isEqual) {
        proxy.$message.warning(td("dpp.integration.alreadyLatestFields", "新增失败，当前已是最新字段"));
      }
      console.log("父组件：增加所有字段");
      tableFields.value = [];
      removeFields.value = [];
      // 这里先清空，再加全部字段，避免重复
      tableFields.value = deepCopy(inputFields.value);

      break;
    }

    case "clearAndAddAll": {
      console.log("父组件：清空并增加所有字段");
      removeFields.value = [];
      // 恢复原始备份字段
      tableFields.value = deepCopy(inputFields.value);

      break;
    }

    case "cancel": {
      console.log("父组件：取消操作");
      break;
    }
  }
}

const isOptionDisabled = (optionValue) => {
  // 已添加的字段名
  const tableUsedNames = tableFields.value.map((item) => item.columnName);
  const removeUsedNames = removeFields.value.map((item) => item.columnName);
  const usedNames = [...tableUsedNames, ...removeUsedNames];
  return usedNames.some((row) => row === optionValue);
};

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
let removeFields = ref([]);
let originalTableFieldsBackup = ref([]);
let inputFields = ref([]);
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let dpModelRefs = ref();
let form = ref({});

function handleDelete(row) {
  // 1. 从 tableFields 中删除对应项
  const idxTable = tableFields.value.findIndex(
    (item) => item.columnName === row.columnName
  );
  if (idxTable !== -1) {
    tableFields.value.splice(idxTable, 1);
  }
  const originalField = originalTableFieldsBackup.value.find(
    (item) => item.columnName === row.columnName
  );
  if (originalField) {
    const idxField = inputFields.value.findIndex(
      (item) => item.columnName === row.columnName
    );
    if (idxField !== -1) {
      inputFields.value[idxField] = JSON.parse(JSON.stringify(originalField));
    } else {
      inputFields.value.push(JSON.parse(JSON.stringify(originalField)));
    }
  }
}
function handleDelete2(row) {
  // 1. 从 tableFields 中删除对应项
  const idxTable = removeFields.value.findIndex(
    (item) => item.columnName === row.columnName
  );
  if (idxTable !== -1) {
    removeFields.value.splice(idxTable, 1);
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

    const tableIndex = tableFields.value.findIndex(
      (item) => item.columnName == sourceField
    );
    if (tableIndex !== -1) {
      const updatedItem = {
        ...tableFields.value[tableIndex],
        cleanRuleList: [ruleItem],
        elementId: [ruleItem.ruleId],
      };
      tableFields.value[tableIndex] = updatedItem;

      const fieldIndex = inputFields.value.findIndex(
        (item) => item.columnName == sourceField
      );
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
  inputFields.value = [];
  originalTableFieldsBackup.value = [];
};

const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;
    // 判断表格是否为空
    if (!tableFields.value || tableFields.value.length === 0) {
      proxy.$message.warning(td("dpp.integration.validateFailedAddAtLeastOne", "校验未通过，请至少添加一个字段"));
      return;
    }
    let isRepeat = hasDuplicateObjects(tableFields.value, "outputField");
    if (isRepeat) {
      proxy.$message.warning(td("dpp.integration.noRepeatOutputField", "请不要填写重复输出字段"));
      return;
    }

    let names = inputFields.value.map((item) => item.columnName);
    let isOut = names.find((item) =>
      tableFields.value.some((row) => row.outputField === item)
    );
    if (isOut) {
      proxy.$message.warning(td("dpp.integration.outputFieldCannotDuplicate", "输出字段不能与已有字段名称重复"));
      return;
    }

    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      });
      loading.value = false;
      form.value.code = response.data;
    }
    const taskParams = form.value?.taskParams || {};
    taskParams.tableFields = tableFields.value.map((item) => ({
      ...item,
      inputField: item.columnName,
    }));
    taskParams.removeFields = removeFields.value;
    taskParams.mainArgs = taskParams.mainArgs || {};

    // 构造 outputFields = inputFields + tableFields 的增强值
    let outputFields = inputFields.value.map((input) => {
      const matched = tableFields.value.find(
        (item) => item.columnName === input.columnName
      );
      return matched
        ? {
            ...input,
            ...matched,
            columnName: matched.outputField || matched.columnName,
          }
        : { ...input };
    });
    // 过滤掉 removeFields 中的值
    taskParams.outputFields = outputFields.filter(
      (item) =>
        removeFields.value.findIndex(
          (f) => f.columnName === item.columnName
        ) === -1
    );

    console.log("保存数据 - outputFields:", taskParams.outputFields);
    emit("confirm", form.value);
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
    console.error("deepCopy error:", e);
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
  originalTableFieldsBackup.value = deepCopy(
    props.currentNode?.data?.taskParams?.inputFields || []
  );
  inputFields.value = taskParams?.inputFields || [];
  tableFields.value = taskParams?.tableFields || [];
  removeFields.value = taskParams?.removeFields || [];
});
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
