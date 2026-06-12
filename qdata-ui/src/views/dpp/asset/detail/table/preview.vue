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
  <!-- 资产预览tab -->
  <div style="padding: 5px">
    <div class="justify-between mb15">
      <el-row :gutter="15" class="btn-style">
        <el-col :span="1.5" v-if="form1.type != '6'">
          <el-button
            type="primary"
            plain
            @click="handleAdd"
            v-hasPermi="['da:assetColumn:assetcolumn:add']"
            :loading="loading"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add', '新增') }}
          </el-button>
        </el-col>
        <el-button
          style="margin-left: 7px"
          plain
          type="primary"
          :loading="loading"
          @click="handleQuery"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query', '查询') }}
        </el-button>
        <el-button @click="handleReset" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset', '重置') }}
        </el-button>
      </el-row>
    </div>
    <el-row :gutter="24" v-if="!formVisible && form1.type != '6'">
      <el-col :span="1">
        <el-button
          style=""
          @click="toggleForm(true)"
          type="primary"
          size="small"
          >+</el-button
        >
      </el-col>
      <el-col :span="7">
        <el-alert
          style="height: 24px"
          :title=”td('dpp.asset.addFilterTip', '点击”+”以添加筛选准则')”
          type="info"
          :closable="false"
        />
      </el-col>
    </el-row>
    <div class="custom-form">
      <el-form
        v-show="formVisible"
        :model="formData"
        ref="formRef"
        label-width="auto"
      >
        <div
          v-for="(item, index) in formData.rows"
          :key="index"
          class="form-row"
        >
          <el-form-item :prop="'rows.' + index + '.checked'">
            <el-checkbox v-model="item.checked"> </el-checkbox>
          </el-form-item>
          <el-form-item :prop="'rows.' + index + '.field'">
            <div>
              <el-select
                :disabled="!item.checked"
                :class="item.checked ? 'select' : ''"
                style="margin: 0; width: 100px; border: none; color: red"
                v-model="item.field"
                :placeholder="td('dpp.asset.selectField', '选择字段')"
              >
                <el-option
                  v-for="field in tableColumns"
                  :key="field.en"
                  :label="field.en"
                  :value="field.en"
                ></el-option>
              </el-select>
            </div>
          </el-form-item>
          <el-form-item :prop="'rows.' + index + '.operator'">
            <el-select
              :disabled="!item.checked"
              style="margin: 0; width: 20px"
              v-model="item.operator"
            >
              <el-option
                style="text-align: center"
                v-for="operator in operators"
                :key="operator"
                :label="operator"
                :value="operator"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :prop="'rows.' + index + '.value'">
            <div :class="item.checked ? 'inner' : ''">
              <el-input
                v-if="item.operator === '='"
                :disabled="!item.checked"
                v-model="item.value"
                :placeholder="td('dpp.asset.enterValue', '请输入值')"
              ></el-input>

              <el-input
                v-else-if="item.operator === '>'"
                :disabled="!item.checked"
                v-model="item.value"
                type="number"
                :placeholder="td('dpp.asset.enterValue', '请输入值')"
              ></el-input>
            </div>
          </el-form-item>
          <el-form-item
            :prop="'rows.' + index + '.logic'"
            style="display: block"
          >
            <el-select
              :disabled="!item.checked"
              v-if="index < formData.rows.length - 1"
              style="margin: 0; width: 80px; display: block"
              v-model="item.logic"
              :placeholder="td('dpp.asset.selectLogic', '选择逻辑')"
            >
              <el-option value="AND" style="text-align: center">AND</el-option>
              <el-option value="OR" style="text-align: center">OR</el-option>
            </el-select>
            <div v-else style="width: 80px"></div>
          </el-form-item>
          <el-form-item>
            <el-button
              @click="removeRow(index)"
              type="danger"
              size="small"
              style="margin-left: 10px"
              >-</el-button
            >
            <el-button
              v-if="index == formData.rows.length - 1"
              @click="addRow(index)"
              type="primary"
              size="small"
              >+</el-button
            >
          </el-form-item>
        </div>
      </el-form>
    </div>
  </div>
  <qt-table v-bind="tableStore" ref="tableRef">
    <template #cellText="{ row, column_data }">
      {{ row?.[column_data?.prop] ?? "-" }}
    </template>
    <template #handle="{ row }" v-if="tableColumns && form1.type != '6'">
      <el-button
        link
        type="primary"
        icon="Edit"
        @click="handleUpdate(row)"
        v-hasPermi="['da:asset:edit']"
        >{{ td('common.button.update', '修改') }}</el-button
      >
      <el-button
        link
        type="primary"
        icon="view"
        @click="openHistory(row)"
        v-hasPermi="['da:asset:edit']"
        >{{ td('dpp.asset.updateRecord', '修改记录') }}</el-button
      >
    </template>
  </qt-table>
  <updateDataDialog
    ref="updateDialogRef"
    :columns="tableColumns"
    @ok="handleQuery"
  />
  <UpdateHistory
    ref="updateHistoryRef"
    :columns="tableColumns"
    @success="handleQuery"
  />
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, reactive, watch } from "vue";
import { useRoute } from "vue-router";
import { preview } from "@/api/da/asset/assetColumn.js";
import updateDataDialog from "../components/previewEdit.vue";
import UpdateHistory from "../components/previewEditLog.vue";

const { td } = useDefaultLang();
const props = defineProps({
  form1: {
    type: Object,
    default: {},
  },
});
let tableRef = ref(null);
const route = useRoute();
let assetId = route.query.id || 1;
const { proxy } = getCurrentInstance();
const tableColumns = ref([]);
const loading = ref(false);
const query = ref();
const operators = ref(["=", ">"]);
const formData = ref({
  rows: [],
});
const formVisible = ref(false);
const updateDialogRef = ref(null); // 组件的 ref 引用
watch(
  () => route.query.id,
  (newId) => {
    if (route.path == "/da/asset/detail" || route.path == "/dpp/asset/detail") {
      assetId = newId || 1;
      tableRef.value?.getList();
    }
  },
  { immediate: true }
);
const toggleForm = (falg) => {
  formVisible.value = !formVisible.value;
  if (falg) {
    addRow(-1);
  }
};
const generateSqlQuery = () => {
  const rows = formData.value.rows;
  let sql = "";
  let firstAdded = false;
  for (let i = 0; i < rows.length; i++) {
    const row = rows[i];
    if (!row.checked) continue;
    const cond = `${row.field} ${row.operator} '${row.value}'`;
    if (!firstAdded) {
      sql += cond;
      firstAdded = true;
    } else {
      const logic = rows[i - 1]?.logic || "AND";
      sql += ` ${logic} ${cond}`;
    }
  }
  return sql;
};

// 查询按钮点击事件
const handleQuery = () => {
  let falg = validateFields();
  if (!falg) return false;
  query.value = generateSqlQuery();
  tableRef.value?.getList();
};
const validateFields = () => {
  for (let row of formData.value.rows) {
    if (!row.field || !row.operator) {
      ElMessage.warning(td('dpp.asset.validateIncomplete', '校验未通过，查询条件请输入完整'));
      return false;
    }
  }
  return true;
};
const removeRow = (index) => {
  if (formData.value.rows.length > 1) {
    formData.value.rows.splice(index, 1);
  } else {
    formData.value.rows = [];
    toggleForm();
  }
};
const addRow = (index) => {
  let flag = validateFields();
  if (!flag) return false;
  if (index !== undefined && formData.value.rows[index]) {
    formData.value.rows[index].logic = "AND";
  }
  const newRow = {
    checked: true,
    field:
      tableColumns.value[index + 1]?.en ||
      tableColumns.value[tableColumns.value.length - 1]?.en,
    operator: "=",
    value: "",
    logic: "",
  };
  if (index !== undefined) {
    formData.value.rows.splice(index + 1, 0, newRow);
  } else {
    formData.value.rows.push(newRow);
  }
};

function handleUpdate(row) {
  updateDialogRef.value?.addRow(row, props.form1);
}
function handleAdd() {
  updateDialogRef.value?.addRow(undefined, props.form1);
}

const updateHistoryRef = ref(null);
function openHistory(row) {
  if (updateHistoryRef.value) {
    updateHistoryRef.value.show(row, props.form1);
  }
}
function handleDelete() {
  proxy.$message.warning(td('dpp.asset.funcDeveloping', '功能开发中....'));
}

const handleReset = () => {
  formData.value.rows = [];
  formVisible.value = false;
  query.value = "";
  tableRef.value?.resetQuery();
};

const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      fit: false,
      defaultSort: { prop: "", order: "" },
    },
  },
  columns: [],
  func: (params) => {
    loading.value = true;
    const payload = {
      id: assetId,
      filter: query.value || generateSqlQuery(),
      orderBy:
        params.orderByColumn && params.isAsc
          ? [{ orderByColumn: params.orderByColumn, isAsc: params.isAsc }]
          : [],
      pageNum: params.pageNum,
      pageSize: params.pageSize,
    };
    return preview(payload)
      .then((response) => {
        tableColumns.value = response.data.columns || [];
        const dynamicCols = (response.data.columns || []).map((c) => ({
          label: `${c.en || "-"}（${c.cn || "-"})`,
          prop: c.field,
          sortable: true,
          sortableKey: c.field,
          slot: "cellText",
          showOverflowTooltip: { effect: "light" },
          width: 230,
        }));
        const handleCol = {
          label: td('common.texts.operation', '操作'),
          fixed: "right",
          slot: "handle",
          width: 200,
        };
        tableStore.columns =
          props.form1 && props.form1.type != "6"
            ? [...dynamicCols, handleCol]
            : dynamicCols;
        loading.value = false;
        return {
          data: {
            rows: response.data.tableData || [],
            total: response.data.total || 0,
          },
        };
      })
      .catch(() => {
        loading.value = false;
        return { data: { rows: [], total: 0 } };
      });
  },
  params: {
    pageNum: 1,
    pageSize: 10,
  },
  events: {
    formatParams(params) {
      if (params.isAsc === "descending") params.isAsc = "desc";
      if (params.isAsc === "ascending") params.isAsc = "asc";
      return params;
    },
  },
});
</script>

<style scoped lang="scss">
.column-header {
  display: flex;
  flex-direction: column;
}

.column-item {
  white-space: nowrap;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: -17px;
}

.form-row .el-form-item {
  margin-right: 10px;
}

.custom-form {
  margin-bottom: 10px;
  max-height: 100px;
  overflow: auto;

  ::v-deep .el-input__wrapper,
  ::v-deep .el-select .el-input__wrapper {
    border: none !important;
    box-shadow: none !important;
  }

  ::v-deep .el-select__wrapper {
    box-shadow: none;
    padding: 0;
  }

  ::v-deep .is-hovering {
    box-shadow: none !important;
  }

  ::v-deep .el-icon {
    display: none;
  }

  .inner {
    ::v-deep .el-input__inner {
      color: #2666fb;
    }
  }

  .inner-text {
    ::v-deep .el-input__inner {
      color: #999093 !important;
    }
  }

  .select {
    ::v-deep .el-select__placeholder {
      color: #2666fb;
    }
  }

  :deep .el-select__wrapper.is-disabled {
    background-color: #fff;
  }

  .select-text {
    ::v-deep .el-select__placeholder {
      color: #999093 !important;
    }
  }

  :deep .el-input.is-disabled .el-input__wrapper {
    background-color: #fff;
  }
}
::v-deep(.el-table .el-table__header-wrapper th) {
  word-break: normal;
}
::v-deep(.el-table .el-table__header-wrapper th .cell) {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  white-space: nowrap;
}
::v-deep(.el-table .el-table__header-wrapper th .caret-wrapper) {
  margin-left: 0;
  margin-top: 2px;
}
::v-deep(.el-table__body-wrapper) {
  overflow-x: auto;
}
</style>
