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
          {{ td('common.button.add') }}
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
          v-hasPermi="['dg:desensitizerules:remove']"
        >
          {{ td('common.button.delete') }}
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
            {{ td('common.button.details') }}
          </el-button>
          <!-- :disabled="row.validFlag === true" -->
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['dg:desensitizerules:edit']"
          >
            {{ td('common.button.update') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            :disabled="row.validFlag === true"
            @click="handleDelete(row)"
            v-hasPermi="['dg:desensitizerules:remove']"
          >
            {{ td('common.button.delete') }}
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
import useDefaultLang from "@/composables/useDefaultLang"
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

const { td } = useDefaultLang();
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
  { label: td('dg.desensitizationRules.detailStatusDisable'), value: false },
  { label: td('dg.desensitizationRules.detailStatusEnable'), value: true },
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
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: td('dg.desensitizationRules.nameDesc'),
      prop: "name",
      slot: "nameDesc",
      align: "left",
      minWidth: 240,
    },
    {
      label: td('dg.sensitiveList.dataCategory'),
      prop: "dataCategoryName",
      align: "left",
      minWidth: 180,
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dg.desensitizationRules.applicationScene'), slot: "applicationScene", width: 120 },
    { label: td('dg.desensitizationRules.maskType'), prop: "maskType", slot: "maskType", width: 120 },
    { label: td('common.texts.status'), prop: "validFlag", slot: "validFlag", minWidth: 120 },
    {
      label: td('common.texts.createdBy'),
      prop: "createBy",
      width: 120,
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
    { label: td('common.texts.operation'), width: 220, fixed: "right", slot: "handle" },
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
      label: td('dg.desensitizationRules.ruleName'),
      prop: "name",
      component: { is: "input", placeholder: td('dg.desensitizationRules.ruleNamePlaceholder') },
    },
    {
      label: td('dg.sensitiveList.dataCategory'),
      prop: "dataCategoryId",
      component: {
        is: "tree-select",
        placeholder: td('dg.sensitiveList.dataCategoryPlaceholder'),
        data: allDataCategoryList,
        props: { label: "name", value: "id", children: "children" },
        "check-strictly": true,
        filterable: true,
      },
    },
    {
      label: td('common.texts.status'),
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: td('common.form.statusPlaceholder'),
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
  const text = e === true ? td('dg.desensitizationRules.detailStatusEnable') : td('dg.desensitizationRules.detailStatusDisable');
  const dataForm = { id, validFlag: row.validFlag };
  proxy.$modal
    .confirm(td('dg.desensitizationRules.confirmStatus', 'Are you sure to "{text}" desensitization rule "{name}"?', { text: text, name: row.name }))
    .then(function () {
      updateDesensitizeRules(dataForm).then(() => {
        proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

function handleAdd() {
  getDataCategoryList();
  ruleDialogRef.value?.open({ title: td('dg.desensitizationRules.addTitle') });
}

function handleUpdate(row) {
  getDataCategoryList();
  ruleDialogRef.value?.open({ id: row?.id, title: td('dg.desensitizationRules.editTitle') });
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
    .confirm('Are you sure to delete the data item numbered "' + ids + '"?')
    .then(function () {
      return delDesensitizeRules(ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}*/

function handleDelete(row) {
  const invalidIds = [];
  const message=ref(td('dg.desensitizationRules.confirmDeleteSimple'));
  if (row?.id) {
    invalidIds.push(row.id);
    message.value=td('dg.desensitizationRules.confirmDeleteId', 'Are you sure to delete item with ID "{id}"?', { id: row.id })
  }else {
    store.rows.forEach(item => {
      // When validFlag is false, record id
      if (item.validFlag === false) {
        invalidIds.push(item.id);
      }
    });
    message.value=td('dg.desensitizationRules.confirmDeleteCount', 'Can delete {canDelete}, cannot delete {cannotDelete}. Delete the deletable items?', { canDelete: invalidIds.length, cannotDelete: store.rows.length-invalidIds.length })
  }
  proxy.$modal
      .confirm(message.value)
      .then(function () {
        return delDesensitizeRules(invalidIds);
      })
      .then(() => {
        tableRef.value.getList();
        proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
      })
      .catch(() => {});
}

</script>
<style>

</style>

