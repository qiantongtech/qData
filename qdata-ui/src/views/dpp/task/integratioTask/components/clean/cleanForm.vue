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
    >
      <template v-if="!info">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.cleanRule.nodeName', '节点名称')"
              prop="name"
              :rules="[
                {
                  required: true,
                  message: td('dpp.cleanRule.inputNodeName', '请输入节点名称'),
                  trigger: 'change',
                },
              ]"
            >
              <el-input v-model="form.name" :placeholder="td('dpp.cleanRule.inputNodeName', '请输入节点名称')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.cleanRule.type', '类型')" prop="typeName">
              <el-select
                v-model="form.taskParams.typeName"
                :placeholder="td('dpp.cleanRule.inputType', '请输入类型')"
                filterable
                disabled
              >
                <el-option
                  v-for="dict in typeList"
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
            <el-form-item :label="td('common.texts.description', '描述')" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                :placeholder="td('common.form.descriptionPlaceholder', '请输入描述')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.cleanRule.whereCondition', 'where条件')" prop="where">
              <el-input
                v-model="form.taskParams.where"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                :placeholder="td('dpp.cleanRule.inputWhereCondition', '请输入where条件')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <template v-else>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.cleanRule.nodeName', '节点名称') + ':'" prop="id">
              <div class="form-readonly">
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.cleanRule.type', '类型')" prop="typeName">
              <div class="form-readonly">
                {{ form.taskParams.typeName }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description', '描述')" prop="description">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.cleanRule.whereCondition', 'where条件')" prop="where">
              <div class="form-readonly textarea">
                {{ form.where ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <div class="h2-title">{{ td('dpp.cleanRule.ruleSetting', '规则设置') }}</div>

      <div class="justify-between mb15" style="margin-top: 10px" v-if="!info">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="Plus"
              @click="openRuleSelector(undefined)"
              >{{ td('dpp.cleanRule.addRule', '新增规则') }}</el-button
            >
          </el-col>
        </el-row>
      </div>
      <el-table
        stripe
        height="350px"
        :data="tableFields"
        v-loading="loadingList"
        ref="dragTable"
        row-key="name"
      >
        <el-table-column :label="td('dpp.cleanRule.index', '序号')" width="80" align="left">
          <template #header>
            <div class="justify-center">
              <span>{{ td('dpp.cleanRule.index', '序号') }}</span>
              <el-tooltip
                effect="light"
                :content="td('dpp.cleanRule.cleanRuleOrderTip', '清洗规则按照下面配置的列表顺序，依次执行')"
                placement="top"
              >
                <el-icon class="tip-icon">
                  <InfoFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
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
                <Operation />
              </el-icon>
              <span style="margin-left: 4px">{{ $index + 1 }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.cleanRule.cleanName', '清洗名称')"
          align="left"
          prop="name"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="300"
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.cleanRule.cleanField', '清洗字段')"
          align="left"
          prop="columns"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="300"
        >
          <template #default="scope">
            {{
              scope.row.columns && scope.row.columns.length
                ? scope.row.columns.join(", ")
                : "-"
            }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.cleanRule.cleanRule', '清洗规则')"
          align="left"
          prop="ruleName"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="300"
        >
          <template #default="scope">
            {{ scope.row.ruleName || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.cleanRule.ruleDescription', '规则描述')"
          align="left"
          prop="ruleDescription"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.ruleDescription || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.cleanRule.dimension', '维度')"
          align="left"
          prop="parentName"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="150"
        >
          <template #default="scope">
            {{ scope.row.parentName || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.status', '状态')" align="left" prop="status">
          <template #default="scope">
            {{ scope.row.status == "1" ? td('dpp.cleanRule.online', '上线') : td('dpp.cleanRule.offline', '下线') }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.operation', '操作')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="180"
          v-if="!info"
        >
          <template #default="scope">
            <!-- <el-button link type="primary" icon="view"
              @click="openRuleDialog(scope.row, scope.$index + 1, true)">查看</el-button> -->
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="openRuleDialog(scope.row, scope.$index + 1)"
              >{{ td('common.button.update', '修改') }}</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleRuleDelete(scope.$index + 1)"
              >{{ td('common.button.delete', '删除') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{ td('common.button.close', '关闭') }}</el-button>
        <el-button type="primary" @click="saveData" v-if="!info"
          >{{ td('common.button.save', '保存') }}</el-button
        >
        <el-tooltip
          :content="td('dpp.cleanRule.getCleanRuleTip', '会自动获取资产关联的数据元中的清洗规则')"
          placement="top"
          v-if="!info"
        >
          <el-button type="warning" @click="renameRuleToRule">
            <el-icon style="margin-right: 4px">
              <Refresh />
            </el-icon>
            {{ td('dpp.cleanRule.getCleanRule', '获取清洗规则') }}
          </el-button>
        </el-tooltip>
      </div>
    </template>
  </el-dialog>
  <RuleSelectorDialog
    ref="ruleSelectorDialog"
    @confirm="RuleSelectorconfirm"
    :inputFields="inputFields"
  />
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { defineProps, defineEmits, ref, computed, watch } from "vue";

import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
const { proxy } = getCurrentInstance();
import Sortable from "sortablejs";
import useUserStore from "@/store/system/user.js";
import {
  createNodeSelect,
  getParentNode,
  renameRuleToRuleConfig,
} from "@/views/dpp/utils/opBase.js";
import RuleSelectorDialog from "./rule/ruleBase.vue";

const { td } = useDefaultLang();
const userStore = useUserStore();
const {
  att_rule_clean_type,
  da_discovery_task_status,
  dpp_etl_task_execution_type,
} = proxy.useDict(
  "att_rule_clean_type",
  "da_discovery_task_status",
  "dpp_etl_task_execution_type"
);
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '' },
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
          tableFields.value.map((f) => f.name)
        );
      },
    });
  });
}
let ruleSelectorDialog = ref();
const openRuleSelector = (row) => {
  ruleSelectorDialog.value.openDialog(row);
};
const openRuleDialog = (row, index, falg) => {
  ruleSelectorDialog.value.openDialog(row, index, falg);
};
const renameRuleToRule = () => {
  const result = renameRuleToRuleConfig(inputFields.value);
  let coverCount = 0;
  let addCount = 0;

  const norm = (v) =>
    String(v ?? "")
      .trim()
      .toUpperCase();
  const sameCols = (a, b) => {
    if (!Array.isArray(a) || !Array.isArray(b) || a.length !== b.length)
      return false;
    return (
      [...a].map(norm).sort().join("|") === [...b].map(norm).sort().join("|")
    );
  };

  result.forEach((newItem) => {
    // 找到是否存在相同 ruleName 且 columns 一样的旧数据
    const existingIndex = tableFields.value.findIndex(
      (oldItem) =>
        norm(oldItem.ruleName) === norm(newItem.ruleName) &&
        sameCols(oldItem.columns, newItem.columns)
    );

    if (existingIndex > -1) {
      // 覆盖
      tableFields.value[existingIndex] = newItem;
      coverCount++;
    } else {
      // 追加
      tableFields.value.push(newItem);
      addCount++;
    }
  });

  proxy.$message.success(td('dpp.cleanRule.coverAndAdd', `覆盖 ${coverCount} 条，追加 ${addCount} 条`, { coverCount, addCount }));
};

function RuleSelectorconfirm(obj, mode) {
  console.log("🚀 ~ RuleSelectorconfirm ~ obj:", obj);
  const index = Number(mode) - 1;
  const list = tableFields.value;
  const isDuplicate = list.some((item, i) => {
    if (index >= 0) {
      return i !== index && item.name == obj.name;
    } else {
      return item.name === obj.name;
    }
  });

  if (isDuplicate) {
    proxy.$message.warning(td('dpp.cleanRule.cleanNameDuplicate', '清洗名称不能重复！'));
    return;
  }

  if (!isNaN(index) && index >= 0 && index < list.length) {
    list.splice(index, 1, obj);
  } else {
    list.push(obj);
  }

  tableFields.value = list;
  ruleSelectorDialog.value.closeDialog();
  setSort();
}
function handleRuleDelete(index) {
  tableFields.value.splice(Number(index) - 1, 1);
  setSort();
}
// 输入字段
let inputFields = ref([]);
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
// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref([]);
let dpModelRefs = ref();
let form = ref({});

function handleRule(data) {
  row.value = {};
  row.value = data;
  opens.value = true;
}
const submitForm = (value) => {
  if (row.value?.index) {
    tableFields.value[row.value.index - 1] = {
      ...tableFields.value[row.value.index - 1],
      cleanRuleList: value,
      elementId: value.map((item) => item.ruleId),
    };

    opens.value = false;
  }
};

const off = () => {
  proxy.resetForm("dpModelRefs");
  // 清空表格字段数据
  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// 保存数据
const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;

    // 如果没有 code，就调用接口获取唯一的 code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false; // 结束加载状态
      form.value.code = response.data; // 设置唯一的 code
    }
    const taskParams = form.value?.taskParams;
    taskParams.tableFields = tableFields.value;
    taskParams.outputFields = inputFields.value;
    emit("confirm", form.value);
  } catch (error) {
    console.error("保存数据失败:", error);
    loading.value = false;
  }
};
const closeDialog = () => {
  off();
  // 关闭对话框
  emit("update", false);
};

// 监听属性变化
function deepCopy(data) {
  if (data === undefined || data === null) {
    return {}; // 或者返回一个默认值
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    return {}; // 或者返回一个默认值
  }
}
let nodeOptions = ref([]);

// 监听属性变化
watchEffect(() => {
  if (!props.visible) {
    off();
    return;
  }
  form.value = deepCopy(props.currentNode?.data || {});
  nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
  inputFields.value = props.currentNode?.data?.taskParams?.inputFields;
  tableFields.value = props.currentNode?.data?.taskParams?.tableFields;
  setSort();
});
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
