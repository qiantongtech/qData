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
      <template v-if="!info">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.cleanRule.nodeName', 'Node Name')"
              prop="name"
              :rules="[
                {
                  required: true,
                  message: td('dpp.cleanRule.inputNodeName', 'Please enter node name'),
                  trigger: 'change',
                },
              ]"
             :label-position="labelPosition">
              <el-input v-model="form.name" :placeholder="td('dpp.cleanRule.inputNodeName', 'Please enter node name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.cleanRule.type', 'Type')" prop="typeName" :label-position="labelPosition">
              <el-select
                v-model="form.taskParams.typeName"
                :placeholder="td('dpp.cleanRule.inputType', 'Please enter type')"
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
            <el-form-item :label="td('common.texts.description', 'Description')" prop="description" :label-position="labelPosition">
              <el-input
                v-model="form.description"
                type="textarea"
                maxlength="256字符"
                show-word-limit
                :placeholder="td('common.form.descriptionPlaceholder', 'Please enter description')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.cleanRule.whereCondition', 'Where Condition')" prop="where" :label-position="labelPosition">
              <el-input
                v-model="form.taskParams.where"
                type="textarea"
                maxlength="256字符"
                show-word-limit
                :placeholder="td('dpp.cleanRule.inputWhereCondition', 'Please enter where condition')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <template v-else>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.cleanRule.nodeName', 'Node Name') + ':'" prop="id" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.cleanRule.type', 'Type')" prop="typeName" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.taskParams.typeName }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description', 'Description')" prop="description" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.cleanRule.whereCondition', 'Where Condition')" prop="where" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.where ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <div class="h2-title">{{ td('dpp.cleanRule.ruleSetting', 'Rule Setting') }}</div>

      <div class="justify-between mb15" style="margin-top: 10px" v-if="!info">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="Plus"
              @click="openRuleSelector(undefined)"
              >{{ td('dpp.cleanRule.addRule', 'Add Rule') }}</el-button
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
        <el-table-column :label="td('dpp.cleanRule.index', 'No.')" width="80" align="left">
          <template #header>
            <div class="justify-center">
              <span>{{ td('dpp.cleanRule.index', 'No.') }}</span>
              <el-tooltip
                effect="light"
                :content="td('dpp.cleanRule.cleanRuleOrderTip', 'Clean rules are executed in the order configured below')"
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
          :label="td('dpp.cleanRule.cleanName', 'Clean Name')"
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
          :label="td('dpp.cleanRule.cleanField', 'Clean Field')"
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
          :label="td('dpp.cleanRule.cleanRule', 'Clean Rule')"
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
          :label="td('dpp.cleanRule.ruleDescription', 'Rule Description')"
          align="left"
          prop="ruleDescription"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.ruleDescription || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.cleanRule.dimension', 'Dimension')"
          align="left"
          prop="parentName"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="150"
        >
          <template #default="scope">
            {{ scope.row.parentName || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.status', 'Status')" align="left" prop="status">
          <template #default="scope">
            {{ scope.row.status == "1" ? td('dpp.cleanRule.online', 'Online') : td('dpp.cleanRule.offline', 'Offline') }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.operation', 'Operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="180"
          v-if="!info"
        >
          <template #default="scope">
            <!-- <el-button link type="primary" icon="view"
              @click="openRuleDialog(scope.row, scope.$index + 1, true)">View</el-button> -->
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="openRuleDialog(scope.row, scope.$index + 1)"
              >{{ td('common.button.update', 'Edit') }}</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleRuleDelete(scope.$index + 1)"
              >{{ td('common.button.delete', 'Delete') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{ td('common.button.close', 'Close') }}</el-button>
        <el-button type="primary" @click="saveData" v-if="!info"
          >{{ td('common.button.save', 'Save') }}</el-button
        >
        <el-tooltip
          :content="td('dpp.cleanRule.getCleanRuleTip', 'Clean rules associated with asset data elements will be auto-retrieved')"
          placement="top"
          v-if="!info"
        >
          <el-button type="warning" @click="renameRuleToRule">
            <el-icon style="margin-right: 4px">
              <Refresh />
            </el-icon>
            {{ td('dpp.cleanRule.getCleanRule', 'Get Clean Rules') }}
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
import { typeList } from "@/utils/graph.js";
const { proxy } = getCurrentInstance();
import Sortable from "sortablejs";
import useUserStore from "@/store/system/user.js";
import {
  createNodeSelect,
  getParentNode,
  renameRuleToRuleConfig,
} from "@/views/dpp/utils/opBase.js";
import RuleSelectorDialog from "./rule/ruleBase.vue";
import { validateWhereCondition } from "../../utils/foolproof.js";

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
  taskType: { type: String, default: '' },
});
let dragTable = ref(null);
let sortableInstance = null;
function setSort() {
  nextTick(() => {
    const tbody = dragTable.value?.$el.querySelector(
      ".el-table__body-wrapper tbody"
    );
    if (!tbody) {
      console.warn("tbody not found; drag initialization failed");
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
          "Order after drag:",
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
    // Find whether there is old data with the same ruleName and the same columns
    const existingIndex = tableFields.value.findIndex(
      (oldItem) =>
        norm(oldItem.ruleName) === norm(newItem.ruleName) &&
        sameCols(oldItem.columns, newItem.columns)
    );

    if (existingIndex > -1) {
      // Cover
      tableFields.value[existingIndex] = newItem;
      coverCount++;
    } else {
      // Append
      tableFields.value.push(newItem);
      addCount++;
    }
  });

  proxy.$message.success(td('dpp.cleanRule.coverAndAdd', 'Overwrite {coverCount}, append {addCount}', { coverCount, addCount }));
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
    proxy.$message.warning(td('dpp.cleanRule.cleanNameDuplicate', 'Clean name must be unique!'));
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
// input field
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
// variable definition
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
  // Clear table field data
  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// save data
const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;

    if (!Array.isArray(tableFields.value) || tableFields.value.length === 0) {
      return proxy.$message.warning('当前转换组件未配置任何清洗规则，请添加规则。');
    }
    const whereResult = validateWhereCondition(form.value?.taskParams?.where);
    if (!whereResult.valid) {
      return proxy.$message.warning(whereResult.message);
    }

    // If there is no code, call the interface to get the unique code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false; // end loading state
      form.value.code = response.data; // Set unique code
    }
    const taskParams = form.value?.taskParams;
    taskParams.tableFields = deepCopy(tableFields.value);
    taskParams.outputFields = deepCopy(inputFields.value);
    emit("confirm", form.value);
  } catch (error) {
    console.error("Failed to save data:", error);
    loading.value = false;
  }
};
const closeDialog = () => {
  off();
  // Close dialog
  emit("update", false);
};

// Listen for property changes
function deepCopy(data) {
  if (data === undefined || data === null) {
    return {}; // Or return a default value
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    return {}; // Or return a default value
  }
}
let nodeOptions = ref([]);

// Listen for property changes
watchEffect(() => {
  if (!props.visible) {
    off();
    return;
  }
  form.value = deepCopy(props.currentNode?.data || {});
  nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
  inputFields.value = deepCopy(props.currentNode?.data?.taskParams?.inputFields || []);
  tableFields.value = deepCopy(props.currentNode?.data?.taskParams?.tableFields || []);
  setSort();
});
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
