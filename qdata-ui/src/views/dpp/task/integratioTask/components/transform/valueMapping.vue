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
      label-width="180px"
      @submit.prevent
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="节点名称"
            prop="name"
            :rules="[
              { required: true, message: '请输入节点名称', trigger: 'change' },
            ]"
          >
            <el-input
              v-if="!info"
              v-model="form.name"
              placeholder="请输入节点名称"
            />
            <div v-else class="form-readonly">{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="typeName">
            <template v-if="!info">
              <el-select
                v-model="form.taskParams.typeName"
                placeholder="请输入类型"
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
        <el-col :span="12">
          <el-form-item
            label="使用字段"
            prop="taskParams.inputField"
            :rules="[
              { required: true, message: '请选择使用字段', trigger: 'blur' },
            ]"
          >
            <template v-if="!info">
              <el-select
                v-model="form.taskParams.inputField"
                placeholder="请选择字段名称"
                filterable
              >
                <el-option
                  v-for="dict in inputFields"
                  :key="dict.columnName"
                  :label="dict.columnName"
                  :value="dict.columnName"
                />
              </el-select>
            </template>
            <div v-else class="form-readonly">
              {{ form.taskParams.inputField }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="目标字段"
            prop="taskParams.outputField"
            :rules="[
              { required: true, message: '请输入目标字段', trigger: 'change' },
            ]"
          >
            <el-input
              v-if="!info"
              v-model="form.taskParams.outputField"
              placeholder="请输入目标字段"
            />
            <div v-else class="form-readonly">
              {{ form.taskParams.outputField }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="不匹配时的默认值"
            prop="taskParams.defaultValue"
            :rules="[
              {
                required: false,
                message: '请输入不匹配时的默认值',
                trigger: 'change',
              },
            ]"
          >
            <template #label>
              <div class="justify-center">
                <span>不匹配时的默认值</span>
                <el-tooltip
                  effect="light"
                  content="若不填写时，则使用原值"
                  placement="top"
                >
                  <el-icon class="tip-icon">
                    <InfoFilled />
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
            <el-input
              v-if="!info"
              v-model="form.taskParams.defaultValue"
              placeholder="请选择不匹配时的默认值"
            />
            <div v-else class="form-readonly">
              {{ form.taskParams.defaultValue || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="center">
        <span class="blue-text">字段值</span>
      </el-divider>
      <div class="justify-between mb15" v-if="!info">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAddField">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
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
        <el-table-column label="序号" width="80" align="left">
          <template #default="{ $index }">
            <div
              class="allowDrag"
              style="
                cursor: move;
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <el-icon>
                <SortDescending />
              </el-icon>
              <span style="margin-left: 4px">{{ $index + 1 }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="原值" align="left" prop="source">
          <template #default="scope">
            <el-input v-model="scope.row.source" placeholder="请输入原值" />
          </template>
        </el-table-column>
        <el-table-column label="目标值" align="left" prop="target">
          <template #default="scope">
            <el-input v-model="scope.row.target" placeholder="请输入目标值" />
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="250"
          v-if="!info"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData" v-if="!info"
          >保存</el-button
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
import { hasDuplicateObjects } from "@/utils/index.js";
import Sortable from "sortablejs";
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: { type: Object, default: () => ({}) },
});

let dragTable = ref(null);
let sortableInstance = null;
function setSort() {
  nextTick(() => {
    const tbody = dragTable.value?.$el.querySelector(
      ".el-table__body-wrapper tbody"
    );
    if (!tbody) {
      console.warn("tbody 找不到，拖拽初始化失败");
      return;
    }

    if (sortableInstance) {
      sortableInstance.destroy();
    }

    sortableInstance = Sortable.create(tbody, {
      handle: ".allowDrag",
      animation: 150,
      onEnd: (evt) => {
        const movedItem = tableFields.value.splice(evt.oldIndex, 1)[0];
        tableFields.value.splice(evt.newIndex, 0, movedItem);
        console.log(
          "拖拽后顺序:",
          tableFields.value.map((f) => f.columnName)
        );
      },
    });
  });
}

function handleAddField() {
  // 1. 校验已有行的目标值是否都有填写
  const incompleteRow = tableFields.value.find(
    (row) => !row.source || !row.target
  );
  if (incompleteRow) {
    proxy.$message.warning("新增失败，请先填写字段值");
    return;
  }

  // 最后一行名称
  let isRepeat = hasDuplicateObjects(tableFields.value, "source");
  if (isRepeat) {
    proxy.$message.warning("新增失败，请不要填写重复的原值");
    return;
  }

  // 4. 新增字段对象（可以根据需要扩展属性）
  tableFields.value.push({
    columnName: "",
    source: "", // 也可以初始化为 nextField.columnName 或其他默认值
    target: "", // 目标值默认空，需用户填写
    order: "asc",
    caseSensitive: false,
    locale: true,
    collatorStrength: 0,
    presorted: false,
  });

  // 5. 重新初始化拖拽排序
  setSort();
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
      console.log(
        "🚀 ~ onResolveFields ~  tableFields.value:",
        tableFields.value
      );
      console.log("父组件：清空并增加所有字段");
      setSort();
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
  if (!row || !row.columnName) return;

  const idx = tableFields.value.findIndex(
    (item) => item.columnName === row.columnName
  );
  if (idx !== -1) {
    tableFields.value.splice(idx, 1);
  }
}

const off = () => {
  proxy.resetForm("dpModelRefs");
  dpModelRefs.value?.clearValidate?.();
  tableFields.value = [];
  inputFields.value = [];
  originalTableFieldsBackup.value = [];
};

const saveData = async () => {
  try {
    // 表单校验
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;
    // 校验 tableFields 不为空
    if (!Array.isArray(tableFields.value) || tableFields.value.length === 0) {
      proxy.$message.warning("校验未通过，请至少一个字段值");
      return;
    }

    // 1. 校验已有行的目标值是否都有填写
    const incompleteRow = tableFields.value.find(
      (row) => !row.source || !row.target
    );
    if (incompleteRow) {
      proxy.$message.warning("校验未通过，请先填写字段值");
      return;
    }

    // 最后一行名称
    let isRepeat = hasDuplicateObjects(tableFields.value, "source");
    if (isRepeat) {
      proxy.$message.warning("校验未通过，请不要填写重复的原值");
      return;
    }

    // 没有 code 时生成唯一 code
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
    taskParams.tableFields = tableFields.value;
    taskParams.mainArgs = taskParams.mainArgs || {};

    // 输出字段拼接目标字段
    taskParams.outputFields = [
      ...inputFields.value,
      {
        columnName: form.value.taskParams.outputField,
        source: form.value.name,
      },
    ];

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
  // 备份初始表字段，避免被篡改
  originalTableFieldsBackup.value = deepCopy(
    props.currentNode?.data?.taskParams?.inputFields || []
  );
  let taskParams = deepCopy(props.currentNode?.data?.taskParams || {});
  inputFields.value = taskParams?.inputFields || [];
  tableFields.value = taskParams?.tableFields || [];
});
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
