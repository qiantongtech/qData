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
  <div class="app-container" ref="app-container">
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStore.params"
          @query="handleQueryClick"
          @reset="handleResetQueryClick"
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['att:tag:add']"
        >
          {{ td('common.button.add') }}
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
          v-hasPermi="['att:tag:remove']"
        >
          {{ td('common.button.delete') }}
        </el-button>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef">
        <template #whitelistNameDesc="{ row }">
          <div class="name-label task-title">
            <div class="task-title-row">
              <div class="task-name-ellipsis">
                <span
                  class="task-name-ellipsis__inner"
                  :title="row.whitelistName || '-'"
                >
                  {{ row.whitelistName || "-" }}
                </span>
              </div>
            </div>
            <div class="desc-text" :title="row.description">
              {{ row.description }}
            </div>
          </div>
        </template>

        <template #effectiveTimeRange="{ row }">
          <span>{{ formatEffectiveTimeRange(row.effectiveTimeRange) }}</span>
        </template>

        <template #effectiveAccount="{ row }">
          <span>{{ formatEffectiveAccount(row.effectiveAccount) }}</span>
        </template>

        <template #createByInfo="{ row }">
          <div class="creator-info">
            <div class="creator-info__name">{{ row.createBy || "-" }}</div>
            {{ row.createAccount || "-" }}
          </div>
        </template>

        <template #status="scope">
          <el-switch
            v-model="scope.row.status"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="(e) => handleStatusChange(scope.row.id, scope.row, e)"
          />
        </template>

        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(row)"
            v-hasPermi="['att:tag:query']"
          >
            {{ td('common.button.details') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            :disabled="row.status == 1"
            v-hasPermi="['att:tag:edit']"
          >
            {{ td('common.button.update') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            :disabled="row.status == 1"
            @click="handleDelete(row)"
            v-hasPermi="['att:tag:remove']"
          >
            {{ td('common.button.delete') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="whitelistRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
      >
        <el-form-item :label="td('dg.desensWhitelist.whitelistName')" prop="whitelistName">
          <el-input
            v-model="form.whitelistName"
            :placeholder="td('dg.desensWhitelist.whitelistNamePlaceholder')"
          />
        </el-form-item>
        <el-form-item :label="td('dg.desensWhitelist.dataCategory')" prop="dataCategoryCode">
          <el-tree-select
            v-model="form.dataCategoryCode"
            filterable
            :data="dataCategoryOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }"
            value-key="code"
            :placeholder="td('dg.desensWhitelist.dataCategoryPlaceholder')"
            check-strictly
            class="form-control--compact"
          />
        </el-form-item>
        <el-form-item :label="td('dg.desensWhitelist.effectiveAccount')" prop="effectiveAccount">
          <el-select
            v-model="form.effectiveAccount"
            multiple
            filterable
            collapse-tags
            collapse-tags-tooltip
            :placeholder="td('dg.desensWhitelist.effectiveAccountPlaceholder')"
          >
            <el-option
              v-for="opt in effectiveAccountOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="td('dg.desensWhitelist.effectiveTimeRange')" prop="effectiveTimeRange">
          <el-date-picker
            v-model="form.effectiveTimeRange"
            type="daterange"
            range-separator="~"
            ::start-placeholder="td('common.form.startDatePlaceholder')"
            ::end-placeholder="td('common.form.endDatePlaceholder')"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <qt-form-item :label="td('common.texts.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dp_model_status"
              :key="dict.value"
              :label="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </qt-form-item>
        <el-form-item :label="td('common.texts.description')" prop="description" class="row-full">
          <el-input
            v-model="form.description"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder')"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full">
          <el-input
            v-model="form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder')"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      :title="title"
      v-model="openDetail"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="whitelistDetailRef"
        :model="form"
        label-width="110px"
        class="column-form"
      >
        <el-form-item :label="td('common.texts.number') + ':'" prop="id">
          <div class="form-readonly">{{ form.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.desensWhitelist.whitelistName')" prop="whitelistName">
          <div class="form-readonly">{{ form.whitelistName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.desensWhitelist.dataCategory')" prop="dataCategoryName">
          <div class="form-readonly">{{ form.dataCategoryName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.desensWhitelist.effectiveAccount')" prop="effectiveAccount" class="row-full">
          <div
            class="form-readonly effective-account-readonly"
            :title="formatEffectiveAccount(form.effectiveAccount)"
          >
            {{ formatEffectiveAccount(form.effectiveAccount) }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dg.desensWhitelist.effectiveTimeRange')" prop="effectiveTimeRange">
          <div class="form-readonly">
            {{ formatEffectiveTimeRange(form.effectiveTimeRange) }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.status')" prop="status">
          <div class="form-readonly">
            {{ getStatusLabel(form.status) }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.description')" prop="description" class="row-full">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdBy')" prop="createBy">
          <div class="form-readonly">{{ form.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdTime')" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy">
          <div class="form-readonly">
            {{ form.updateBy ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DesensWhitelist">
import {
  addMockDesensWhitelist,
  deleteMockDesensWhitelist,
  getMockDesensWhitelistById,
  listMockDataCategory,
  listMockEffectiveAccounts,
  queryMockDesensWhitelistList,
  updateMockDesensWhitelist,
} from "./mockData";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";
import { getCurrentInstance, onMounted, reactive, ref, toRefs } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");

const store = reactive({
  rows: [],
});

const dataCategoryOptions = ref([]);
const allDataCategoryOptions = ref([]);

const effectiveAccountOptions = listMockEffectiveAccounts().map((v) => ({
  label: v,
  value: v,
}));
function formatEffectiveTimeRange(v) {
  if (!Array.isArray(v) || v.length !== 2) return "-";
  const [s, e] = v;
  if (!s || !e) return "-";
  return `${s}~${e}`;
}

function formatEffectiveAccount(v) {
  if (Array.isArray(v)) return v.length ? v.join("、") : "-";
  return v || "-";
}

function getStatusLabel(v) {
  const vv = v === 0 || v ? String(v) : "";
  const found = (dp_model_status.value || []).find(
    (d) => String(d.value) === vv
  );
  return found?.label || (vv ? vv : "-");
}

async function initDataCategoryOptions() {
  try {
    const res = await selectTreeDataCategory();
    const rawData = res?.data || [];
    const allTree = (nodes) => {
      return nodes.map((node) => {
        const newNode = { ...node };
        if (node.children && node.children.length > 0) {
          newNode.children = allTree(node.children);
        }
        return newNode;
      });
    };
    allDataCategoryOptions.value = [
      {
        name: td('dg.desensWhitelist.dataCategory'),
        code: "",
        id: 0,
        children: allTree(rawData),
      },
    ];

    const processTree = (nodes) => {
      return nodes
        .filter((node) => String(node.desensitizationRulesFlag) !== "1")
        .map((node) => {
          const newNode = { ...node };
          newNode.disabled = String(node.type) === "1";
          if (node.children && node.children.length > 0) {
            newNode.children = processTree(node.children);
          }
          return newNode;
        });
    };
    const tree = processTree(rawData);
    dataCategoryOptions.value = [
      {
        name: td('dg.desensWhitelist.dataCategory'),
        code: "",
        id: 0,
        children: tree,
      },
    ];
  } catch (error) {
    console.error("Failed to load data category:", error);
  }
}

onMounted(() => {
  initDataCategoryOptions();
});

const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetail,
    },
  },
  columns: [
    { type: "selection", width: 55, align: "left" },
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: td('dg.desensWhitelist.nameDesc'),
      prop: "whitelistName",
      align: "left",
      width: 260,
      slot: "whitelistNameDesc",
    },
    {
      label: td('dg.desensWhitelist.dataCategory'),
      prop: "dataCategoryName",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dg.desensWhitelist.effectiveAccount'),
      prop: "effectiveAccount",
      slot: "effectiveAccount",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dg.desensWhitelist.effectiveTimeRange'),
      prop: "effectiveTimeRange",
      slot: "effectiveTimeRange",
      width: 190,
      align: "left",
    },
    {
      label: td('common.texts.status'),
      prop: "status",
      slot: "status",
      width: 120,
    },
    {
      label: td('common.texts.createdBy'),
      prop: "createBy",
      slot: "createByInfo",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    {
      label: td('common.texts.operation'),
      width: 220,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: (params) => {
    const res = queryMockDesensWhitelistList(params);
    return Promise.resolve({ data: { rows: res.rows, total: res.total } });
  },
  params: {
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('dg.desensWhitelist.whitelistName'),
      prop: "whitelistName",
      align: "left",
      component: { is: "input", placeholder: td('dg.desensWhitelist.whitelistNamePlaceholder') },
    },
    {
      label: td('dg.desensWhitelist.dataCategory'),
      prop: "dataCategoryCode",
      component: {
        is: "tree-select",
        data: allDataCategoryOptions,
        props: { value: "code", label: "name", children: "children" },
        valueKey: "code",
        placeholder: td('dg.desensWhitelist.dataCategoryPlaceholder'),
        checkStrictly: true,
      },
    },
    {
      label: td('common.texts.status'),
      prop: "status",
      component: {
        is: "select",
        placeholder: td('common.form.statusPlaceholder'),
        options: dp_model_status,
      },
    },
  ],
});

function handleQueryClick() {
  tableRef.value.getList();
}
function handleResetQueryClick() {
  tableRef.value.resetQuery();
}

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? td('dg.desensWhitelist.enabled') : td('dg.desensWhitelist.disabled');
  proxy.$modal
    .confirm(td('dg.desensWhitelist.confirmStatus').replace('{text}', text).replace('{name}', row.whitelistName || "-"))
    .then(function () {
      updateMockDesensWhitelist({ id, status: row.status });
      proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
      tableRef.value.getList();
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const data = reactive({
  form: {},
  rules: {
    whitelistName: [
      { required: true, message: td('dg.desensWhitelist.whitelistNameRequired'), trigger: "blur" },
    ],
    dataCategoryCode: [
      { required: true, message: td('dg.desensWhitelist.dataCategoryRequired'), trigger: "change" },
    ],
    effectiveAccount: [
      { required: true, message: td('dg.desensWhitelist.effectiveAccountRequired'), trigger: "blur" },
    ],
    effectiveTimeRange: [
      { required: true, message: td('dg.desensWhitelist.effectiveTimeRangeRequired'), trigger: "change" },
    ],
  },
});

const { form, rules } = toRefs(data);

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    whitelistName: null,
    description: null,
    dataCategoryCode: null,
    dataCategoryName: null,
    effectiveAccount: [],
    effectiveTimeRange: null,
    status: "0",
    remark: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  };
  proxy.resetForm("whitelistRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  initDataCategoryOptions();
  open.value = true;
  title.value = td('dg.desensWhitelist.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  initDataCategoryOptions();
  const _id = row?.id;
  form.value = getMockDesensWhitelistById(_id) || row || form.value;
  open.value = true;
  title.value = td('dg.desensWhitelist.editTitle');
}
/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  initDataCategoryOptions();
  const _id = row?.id;
  form.value = getMockDesensWhitelistById(_id) || row || form.value;
  openDetail.value = true;
  title.value = td('dg.desensWhitelist.detailTitle');
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["whitelistRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateMockDesensWhitelist(form.value);
        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
        open.value = false;
        tableRef.value.getList();
      } else {
        addMockDesensWhitelist(form.value);
        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
        open.value = false;
        tableRef.value.getList();
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    _ids = store.rows.map((item) => item.id).join(",");
  }
  if (!_ids) return;

  proxy.$modal
    .confirm(td('dg.desensWhitelist.confirmDeleteId').replace('{id}', _ids))
    .then(() => {
      deleteMockDesensWhitelist(_ids);
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {
      // 用户取消删除操作
    });
}
</script>

<style scoped lang="scss">
.creator-info__account {
  color: var(--el-text-color-secondary);
}
.effective-account-readonly {
  width: 100%;
  display: block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
