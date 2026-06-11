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
  <div class="app-container" ref="appContainerRef">
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
          v-hasPermi="['dg:desensitizerules:add']"
        >
          {{ t('common.button.add') }}
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
          v-hasPermi="['dg:desensitizerules:remove']"
        >
          {{ t('common.button.delete') }}
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
            <div class="desc-text" :title="row.description">
              {{ row.description || "-" }}
            </div>
          </div>
        </template>
        <template #applicationScene="{ row }">
          <dict-tag
            :options="dg_application_scene"
            :value="row.applicationScene"
          />
        </template>
        <template #maskType="{ row }">
          <dict-tag :options="dg_mask_type" :value="row.maskType" />
        </template>
        <template #validFlag="scope">
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
            v-hasPermi="['dg:desensitizerules:query']"
          >
            {{ t('common.button.details') }}
          </el-button>
          <!-- :disabled="row.validFlag === true" -->
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['dg:desensitizerules:edit']"
          >
            {{ t('common.button.update') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            :disabled="row.validFlag === true"
            @click="handleDelete(row)"
            v-hasPermi="['dg:desensitizerules:remove']"
          >
            {{ t('common.button.delete') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>
    <RuleFormDialog
      ref="ruleDialogRef"
      :append-to="appContainerRef"
      :maskRuleOptions="dg_replace_rule"
      :get="getDesensitizeRules"
      :add="addDesensitizeRules"
      :update="updateDesensitizeRules"
      @success="handleDialogSuccess"
    />
  </div>
</template>

<script setup name="DesensitizationRules">
import { useI18n } from 'vue-i18n'
import {
  listDesensitizeRules,
  getDesensitizeRules,
  addDesensitizeRules,
  updateDesensitizeRules,
  delDesensitizeRules,
} from "@/api/dg/safety/desensitizeRules";
import { getCurrentInstance, ref, reactive, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";
import RuleFormDialog from "@/views/dg/safety/desensitizationRules/components/ruleFormDialog.vue";
import {delDesensitizeWhitelist} from "@/api/dg/safety/whitelist/desensitizeWhitelist.js";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { dg_application_scene, dg_mask_type, dg_replace_rule } = proxy.useDict(
  "dg_application_scene",
  "dg_mask_type",
  "dg_replace_rule"
);

const router = useRouter();
const dataCategoryList = ref([]);
const allDataCategoryList = ref([]);
async function getDataCategoryList() {
  try {
    const res = await selectTreeDataCategory();
    const rawData = res?.data || [];
    allDataCategoryList.value = rawData;
    const processTree = (nodes) => {
      return nodes
        .filter((node) => String(node.desensitizationRulesFlag) !== "1")
        .map((node) => {
          const newNode = { ...node };
          if (node.children && node.children.length > 0) {
            newNode.children = processTree(node.children);
          }
          return newNode;
        });
    };
    dataCategoryList.value = processTree(rawData);
  } catch {
    dataCategoryList.value = [];
  }
}

onMounted(() => {
  getDataCategoryList();
});

const store = reactive({ rows: [] });
const appContainerRef = ref(null);
const tableRef = ref(null);
const ruleDialogRef = ref(null);
const validFlagOptions = [
  { label: "禁用", value: false },
  { label: "启用", value: true },
];

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
    { type: "selection", width: 45 },
    { label: t('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: "脱敏规则名称/描述",
      prop: "name",
      slot: "nameDesc",
      align: "left",
      minWidth: 240,
    },
    {
      label: "数据分类",
      prop: "dataCategoryName",
      align: "left",
      minWidth: 180,
      showOverflowTooltip: { effect: "light" },
    },
    { label: "应用场景", slot: "applicationScene", width: 120 },
    { label: "脱敏方式", prop: "maskType", slot: "maskType", width: 120 },
    { label: t('common.texts.status'), prop: "validFlag", slot: "validFlag", minWidth: 120 },
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
    { label: t('common.texts.operation'), width: 220, fixed: "right", slot: "handle" },
  ],
  func: async (params) => {
    const res = await listDesensitizeRules(params);
    const rows = (res?.data?.rows || res?.rows || []).map((r) => ({
      ...r,
      validFlag:
        r.validFlag === "1" || r.validFlag === 1 || r.validFlag === true,
    }));
    const total = res?.data?.total || res?.total || 0;
    return { data: { rows, total } };
  },
  params: {
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const searchStore = reactive({
  items: [
    {
      label: "脱敏规则名称",
      prop: "name",
      component: { is: "input", placeholder: "请输入脱敏规则名称" },
    },
    {
      label: "数据分类",
      prop: "dataCategoryId",
      component: {
        is: "tree-select",
        placeholder: "请选择数据分类",
        data: allDataCategoryList,
        props: { label: "name", value: "id", children: "children" },
        "check-strictly": true,
        filterable: true,
      },
    },
    {
      label: t('common.texts.status'),
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: t('common.form.statusPlaceholder'),
        options: validFlagOptions,
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

function handleStatusChange(id, row, e) {
  const text = e === true ? "启用" : "禁用";
  const dataForm = { id, validFlag: row.validFlag };
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"脱敏规则吗？')
    .then(function () {
      updateDesensitizeRules(dataForm).then(() => {
        proxy.$modal.msgSuccess(t('common.message.msgOpSuccess'));
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

function handleAdd() {
  getDataCategoryList();
  ruleDialogRef.value?.open({ title: "新增脱敏规则" });
}

function handleUpdate(row) {
  getDataCategoryList();
  ruleDialogRef.value?.open({ id: row?.id, title: "修改脱敏规则" });
}

function handleDetail(row) {
  router.push({
    path: "/dg/safety/desensitizationRules/detail",
    query: { id: row?.id },
  });
}

function handleDialogSuccess() {
  tableRef.value.getList();
}

/*function handleDelete(row) {
  let ids = null;
  if (row?.id) ids = row.id;
  else ids = store.rows.map((x) => x.id).join(",");
  if (!ids) return;
  proxy.$modal
    .confirm('是否确认删除编号为"' + ids + '"的数据项？')
    .then(function () {
      return delDesensitizeRules(ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
    })
    .catch(() => {});
}*/

function handleDelete(row) {
  const invalidIds = [];
  const message=ref("确定要删除记录吗");
  if (row?.id) {
    invalidIds.push(row.id);
    message.value=`是否确认删除编号为${row.id} 的数据项？`
  }else {
    store.rows.forEach(item => {
      // 当 validFlag 为 false 时，记录 id
      if (item.validFlag === false) {
        invalidIds.push(item.id);
      }
    });
    message.value=`可删除${invalidIds.length}个，不可删除${store.rows.length-invalidIds.length}个，是否删除可删部分`
  }
  proxy.$modal
      .confirm(message.value)
      .then(function () {
        return delDesensitizeRules(invalidIds);
      })
      .then(() => {
        tableRef.value.getList();
        proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
      })
      .catch(() => {});
}

</script>
<style>

</style>

