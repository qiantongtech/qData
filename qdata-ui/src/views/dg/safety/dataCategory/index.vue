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
    <el-container>
      <DeptTree
        ref="DeptTreeRef"
        :leftWidth="leftWidth"
        title="数据分类类目"
        type="dataCategory"
        :placeholder="'请输入数据分类类目'"
        editable
        :api="tagCatTreeApi"
        :extraParams="tagCatExtraParams"
        @node-click="handleNodeClick"
        @tree-change="handleTreeChange"
      />
      <el-main class="main-content">
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
              v-hasPermi="['dg:dataCategory:add']"
            >
              {{ t('common.button.add') }}
            </el-button>

            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDelete"
              v-hasPermi="['dg:dataCategory:remove']"
            >
              {{ t('common.button.delete') }}
            </el-button>

            <el-button
              type="primary"
              plain
              icon="Operation"
              :disabled="!store.rows.length"
              @click="handleBatchLevel"
            >
              指定分级
            </el-button>
          </template>
          <qt-table v-bind="tableStore" ref="tableRef">
            <template #nameDesc="{ row }">
              <div class="name-label task-title">
                <div class="task-title-row">
                  <div class="task-name-ellipsis">
                    <span
                      class="task-name-ellipsis__inner"
                      :title="row.name || '-'"
                    >
                      {{ row.name || "-" }}
                    </span>
                  </div>
                </div>
                <div class="desc-text" :title="row.description || '-'">
                  {{ row.description || "-" }}
                </div>
              </div>
            </template>
            <template #dataLevel="{ row }">
              <div
                style="display: flex; align-items: center"
                v-if="row.dataLevelShortName"
              >
                <LevelBadge :levelData="row.dataLevelShortName" />
              </div>
              <span v-else>-</span>
            </template>
            <template #priority="{ row }">
              <dict-tag :options="dg_data_priority" :value="row.priority" />
            </template>
            <template #desensitizationRulesFlag="{ row }">
              <el-tag
                :type="row.desensitizationRulesFlag == 1 ? 'primary' : 'danger'"
                :style="
                  row.desensitizationRulesFlag == 1 ? 'cursor: pointer' : ''
                "
                @click="handleViewRule(row)"
              >
                {{ row.desensitizationRulesFlag == 1 ? "已配置" : "未配置" }}
              </el-tag>
            </template>
            <template #status="scope">
              <el-switch
                v-model="scope.row.validFlag"
                active-color="#13ce66"
                inactive-color="#ff4949"
                :active-value="true"
                :inactive-value="false"
                @change="(e) => handleStatusChange(scope.row.id, scope.row, e)"
              />
            </template>
            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="View"
                @click="handleDetail(row)"
                v-hasPermi="['dg:dataCategory:query']"
              >
                {{ t('common.button.details') }}
              </el-button>
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                :disabled="row.status == 1"
                v-hasPermi="['dg:dataCategory:edit']"
              >
                {{ t('common.button.update') }}
              </el-button>

              <el-popover placement="bottom" :width="150" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">
                    {{ t('common.button.more') }}
                  </el-button>
                </template>
                <div style="width: 100px" class="butgdlist">
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    @click="handleDelete(row)"
                    v-hasPermi="['dg:dataCategory:remove']"
                  >
                    {{ t('common.button.delete') }}
                  </el-button>
                  <el-button
                    link
                    type="primary"
                    icon="Setting"
                    @click="handleMapping(row)"
                    style="padding-left: 14px"
                  >
                    设置脱敏
                  </el-button>
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- 新增或详情数据分类对话框 -->
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
        ref="DataCategoryRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="所属类目" prop="catCode">
              <el-tree-select
                filterable
                v-model="form.catCode"
                :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择所属类目"
                check-strictly
                @change="handleCatChange"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入分类名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="数据分级" prop="dataLevelId">
              <el-select
                v-model="form.dataLevelId"
                placeholder="请选择数据分级"
              >
                <el-option
                  v-for="opt in dataLevelOptions"
                  :key="opt.value"
                  :label="opt.label"
                  :value="opt.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <qt-form-item
              label="优先级"
              prop="priority"
              :tip="{ content: '优先级越高，越优先匹配' }"
            >
              <el-select v-model="form.priority" placeholder="请选择优先级">
                <el-option
                  v-for="dict in dg_data_priority"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </qt-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="分类缩写">
              <el-input v-model="form.shortName" placeholder="请输入分类缩写" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <qt-form-item
              :label="t('common.texts.status')"
              prop="validFlag"
              :tip="{
                content:
                  '启用状态表示该数据分类可用于打标数据资产；禁用后无法再被使用，但已有数据分类仍保留。',
              }"
            >
              <el-radio-group v-model="form.validFlag">
                <el-radio :label="false">禁用</el-radio>
                <el-radio :label="true">启用</el-radio>
              </el-radio-group>
            </qt-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="t('common.texts.description')" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :placeholder="t('common.form.descriptionPlaceholder')"
                maxlength="500个字符"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="t('common.texts.remark')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                :placeholder="t('common.form.remarkPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ t('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" @click="submitForm">
            {{ t('common.button.confirm') }}
          </el-button>
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
        ref="DataCategoryDetailRef"
        :model="form"
        label-width="110px"
        class="column-form"
      >
        <el-form-item label="编号:" prop="id">
          <div class="form-readonly">{{ form.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <div class="form-readonly">{{ form.name ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="所属类目" prop="catName">
          <div class="form-readonly">
            {{ form.catName ?? form.catCode ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item label="数据分级" prop="dataLevelId">
          <div v-if="form.dataLevelId">
            <LevelBadge :levelData="form.dataLevelShortName" />
          </div>
          <div class="form-readonly" v-else>-</div>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <dict-tag :options="dg_data_priority" :value="form.priority" />
        </el-form-item>
        <el-form-item label="脱敏配置" prop="desensitizationRulesFlag">
          <el-tag
            v-if="form.desensitizationRulesFlag == 1"
            type="primary"
            style="cursor: pointer"
            @click="handleViewRule(form)"
          >
            已配置
          </el-tag>
          <el-tag v-else type="danger">未配置</el-tag>
        </el-form-item>
        <el-form-item :label="t('common.texts.status')" prop="validFlag">
          <el-tag v-if="form.validFlag === true" type="primary">启用</el-tag>
          <el-tag v-else-if="form.validFlag === false" type="danger"
            >禁用</el-tag
          >
        </el-form-item>

        <el-form-item :label="t('common.texts.description')" prop="description" class="row-full">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="t('common.texts.remark')" prop="remark" class="row-full">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="t('common.texts.createdBy')" prop="createBy">
          <div class="form-readonly">{{ form.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="t('common.texts.createdTime')" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="t('common.texts.updatedBy')" prop="updateBy">
          <div class="form-readonly">{{ form.updateBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="t('common.texts.updatedTime')" prop="updateTime">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ t('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="指定分级"
      class="tag-view"
      v-model="batchLevelOpen"
      width="600px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-form-item label="数据分级">
        <el-select
          v-model="batchDataLevel"
          placeholder="请选择数据分级"
          style="width: calc(100% - 117px)"
        >
          <el-option
            v-for="opt in dataLevelOptions"
            :key="String(opt.value)"
            v-bind="opt"
          />
        </el-select>
        <el-button
          type="primary"
          style="margin-left: 14px"
          plain
          @click="openDataLevelDialog"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增分级
        </el-button>
      </el-form-item>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="batchLevelOpen = false">{{ t('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitBatchLevel">{{ t('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <RuleFormDialog
      ref="ruleDialogRef"
      :append-to="$refs['app-container']"
      :maskRuleOptions="dg_replace_rule"
      :get="getDesensitizeRules"
      :add="addDesensitizeRules"
      :update="updateDesensitizeRules"
      @success="handleDialogSuccess"
    />
    <DataLevelDialog
      v-model:open="dataLevelDialogOpen"
      :title="dataLevelDialogTitle"
      @success="loadDataLevelOptions"
    />
  </div>
</template>

<script setup name="DataCategory">
import { useI18n } from 'vue-i18n'
import {
  listDataCategory,
  getDataCategory,
  delDataCategory,
  addDataCategory,
  updateDataCategory,
  batchDataLevel as batchDataLevelApi,
} from "@/api/dg/safety/dataCategory/dataCategory.js";
import DeptTree from "@/components/DeptTree/index.vue";
import {
  listDataCategoryCat,
  getDataCategoryCat,
  addDataCategoryCat,
  updateDataCategoryCat,
  delDataCategoryCat,
} from "@/api/dg/safety/dataCategory/dataCategoryCat.js";
import LevelBadge from "@/views/dg/safety/dataLevel/components/LevelBadge.vue";
import DataLevelDialog from "@/views/dg/safety/dataLevel/components/DataLevelDialog.vue";
import { listAllDataLevel } from "@/api/dg/dataLevel/dataLevel";
import {
  listDesensitizeRules,
  getDesensitizeRules,
  addDesensitizeRules,
  updateDesensitizeRules,
  delDesensitizeRules,
} from "@/api/dg/safety/desensitizeRules";
import RuleFormDialog from "@/views/dg/safety/desensitizationRules/components/ruleFormDialog.vue";
import { getCurrentInstance, ref, reactive, toRefs, onMounted } from "vue";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { dg_data_priority, dg_replace_rule } = proxy.useDict(
  "dg_data_priority",
  "dg_replace_rule"
);

const deptOptions = ref([]);
const leftWidth = ref(300); // 初始左侧宽度
const store = reactive({
  rows: [],
});
const dataLevelOptions = ref([]);
const dataLevelLoading = ref(false);

const tagCatExtraParams = reactive({ validFlag: true });
const tagCatTreeApi = {
  list: listDataCategoryCat,
  get: getDataCategoryCat,
  add: addDataCategoryCat,
  update: updateDataCategoryCat,
  del: delDataCategoryCat,
};

function handleTreeChange(treeData) {
  if (!Array.isArray(treeData)) {
    deptOptions.value = [];
    return;
  }
  deptOptions.value = treeData.map((node) =>
    String(node?.id) === "0" ? { ...node, code: node?.code ?? "" } : node
  );
}
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
    },
  },
  columns: [
    { type: "selection", width: 45 },
    { label: t('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: "分类名称/描述",
      prop: "name",
      slot: "nameDesc",
      align: "left",
      minWidth: 220,
    },

    {
      label: "分类缩写",
      prop: "shortName",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "所属类目",
      prop: "catName",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "数据分级",
      prop: "dataLevelId",
      slot: "dataLevel",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "优先级",
      prop: "priority",
      slot: "priority",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "脱敏配置",
      prop: "desensitizationRulesFlag",
      slot: "desensitizationRulesFlag",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.status'),
      prop: "validFlag",
      slot: "status",
      minWidth: 120,
    },
    {
      label: t('common.texts.createdBy'),
      prop: "createBy",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    {
      label: t('common.texts.operation'),
      width: 220,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listDataCategory,
  params: {
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const searchStore = reactive({
  items: [
    {
      label: "分类名称",
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: "请输入分类名称" },
    },
    {
      label: "数据分级",
      prop: "dataLevelId",
      component: {
        is: "select",
        placeholder: "请选择数据分级",
        options: dataLevelOptions,
      },
    },
    // {
    //   label: "优先级",
    //   prop: "priority",
    //   component: {
    //     is: "select",
    //     placeholder: "请选择优先级",
    //     options: dg_data_priority,
    //   },
    // },
    {
      label: t('common.texts.status'),
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: t('common.form.statusPlaceholder'),
        options: [
          { label: "启用", value: true },
          { label: "禁用", value: false },
        ],
      },
    },
  ],
});

// 新的 handleNodeClick 函数
function handleNodeClick(data) {
  if (!data || String(data.id) === "0") {
    tableStore.params.catCode = null;
  } else {
    const code = data?.code ?? data?.value ?? null;
    tableStore.params.catCode = code ? code : null;
  }
  tableRef.value.getList();
}

function handleQueryClick() {
  tableRef.value.getList();
}
let DeptTreeRef = ref(null);
function handleResetQueryClick() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = null;
  tableRef.value.resetQuery();
}

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e ? "启用" : "禁用";
  let dataForm = {
    id: id,
    validFlag: row.validFlag,
  };
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据分类吗？')
    .then(function () {
      updateDataCategory(dataForm).then(() => {
        proxy.$modal.msgSuccess(t('common.message.msgOpSuccess'));
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const batchLevelOpen = ref(false);
const batchDataLevel = ref(null);
const ruleDialogRef = ref(null);
const dataLevelDialogOpen = ref(false);
const dataLevelDialogTitle = ref("新增分级");
const data = reactive({
  form: { icon: null },
  rules: {
    name: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
    catCode: [
      { required: true, message: "所属类目不能为空", trigger: "change" },
    ],
    dataLevelId: [
      { required: true, message: "数据分级不能为空", trigger: "change" },
    ],
    priority: [
      { required: true, message: "优先级不能为空", trigger: "change" },
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
    catId: null,
    catCode: null,
    name: null,
    dataLevelId: null,
    priority: null,
    validFlag: false,
    description: null,
    remark: null,
  };
  proxy.resetForm("DataCategoryRef");
}

function handleCatChange(code) {
  const findNode = (nodes, code) => {
    for (const node of nodes) {
      if (node.code === code) return node;
      if (node.children) {
        const found = findNode(node.children, code);
        if (found) return found;
      }
    }
    return null;
  };
  const selectedNode = findNode(deptOptions.value, code);
  if (selectedNode) {
    form.value.catId = selectedNode.id;
  } else {
    form.value.catId = null;
  }
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  // 如果左侧树选中了节点，预填所属类目
  if (tableStore.params.catCode) {
    form.value.catCode = tableStore.params.catCode;
    // 需要找到对应的 catId，这里可能需要从 deptOptions 中查找
    const findNode = (nodes, code) => {
      for (const node of nodes) {
        if (node.code === code) return node;
        if (node.children) {
          const found = findNode(node.children, code);
          if (found) return found;
        }
      }
      return null;
    };
    const selectedNode = findNode(deptOptions.value, tableStore.params.catCode);
    if (selectedNode) {
      form.value.catId = selectedNode.id;
    }
  }
  open.value = true;
  title.value = "新增数据分类";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id;
  getDataCategory(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改数据分类";
  });
}
/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row?.id;
  if (!_id) return;
  getDataCategory(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "数据分类详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["DataCategoryRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDataCategory(form.value).then(() => {
          proxy.$modal.msgSuccess(t('common.message.editSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
      } else {
        addDataCategory(form.value).then(() => {
          proxy.$modal.msgSuccess(t('common.message.addSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
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
    .confirm('是否确认删除编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDataCategory(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

async function loadDataLevelOptions() {
  dataLevelLoading.value = true;
  try {
    const res = await listAllDataLevel({});
    const rows = res?.data || res?.data?.rows || [];
    dataLevelOptions.value = rows.map((r) => ({
      ...r,
      label: r?.shortName || r?.name || "-",
      value: r?.id,
    }));
  } finally {
    dataLevelLoading.value = false;
  }
}

function openDataLevelDialog() {
  dataLevelDialogTitle.value = "新增分级";
  dataLevelDialogOpen.value = true;
}

function handleBatchLevel() {
  if (!store.rows.length) return;
  batchDataLevel.value = null;
  batchLevelOpen.value = true;
  loadDataLevelOptions();
}

async function submitBatchLevel() {
  if (!batchDataLevel.value) {
    proxy.$modal.msgWarning("请选择数据分级");
    return;
  }
  await batchDataLevelApi({
    ids: store.rows.map((r) => r.id),
    dataLevelId: batchDataLevel.value,
  });
  proxy.$modal.msgSuccess("设置成功");
  batchLevelOpen.value = false;
  tableRef.value.getList();
}

function handleMapping(row) {
  if (!row?.id) return;
  if (row.desensitizationRulesId) {
    ruleDialogRef.value?.open({
      id: row.desensitizationRulesId,
      title: "修改脱敏规则",
    });
  } else {
    ruleDialogRef.value?.open({
      title: "设置脱敏",
      form: { dataCategoryId: row.id },
    });
  }
}

function handleViewRule(row) {
  if (row.desensitizationRulesFlag != 1 || !row.desensitizationRulesId) return;
  ruleDialogRef.value?.open({
    id: row.desensitizationRulesId,
    title: "查看脱敏规则",
    readonly: true,
  });
}

function handleDialogSuccess() {
  tableRef.value.getList();
}

loadDataLevelOptions();
</script>

<style scoped lang="scss">
</style>
