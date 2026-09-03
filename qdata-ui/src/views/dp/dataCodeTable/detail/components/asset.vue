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
  <qt-wrap :columns="tableStore.columns" :tableRef="tableRef" :config="wrapConfig">
    <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
    </qt-table>
  </qt-wrap>
</template>

<script setup name="ComponentOne">
import {
  listDpDataElemAssetRel,
  getDpDataElemAssetRel,
  delDpDataElemAssetRel,
  addDpDataElemAssetRel,
  updateDpDataElemAssetRel
} from '@/api/dp/dataElem/dataElem';
import useDefaultLang from "@/composables/useDefaultLang";
import { ref, reactive, toRefs, watch, getCurrentInstance } from 'vue';
import { useRoute } from 'vue-router';

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const route = useRoute();

const tableRef = ref(null);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  config: {
    sort: true,
    initResquest: true,
    table: {
      stripe: true,
      height: 360,
      onSelectionChange: (selection) => {
        ids.value = selection.map((item) => item.id);
        single.value = selection.length != 1;
        multiple.value = !selection.length;
      },
    },
  },
  columns: [
    {
      label: td('common.texts.number'),
      prop: "id",
      width: 60,
      align: "left",
      sortable: true,
    },
    {
      label: td('dp.dataCode.asset.assetName'),
      prop: "assetName",
      align: "left",
      width: 300,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('common.texts.description'),
      prop: "description",
      align: "left",
      width: 380,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.asset.tableName'),
      prop: "tableName",
      align: "left",
      width: 290,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.asset.columnName'),
      prop: "columnName",
      align: "left",
      width: 300,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('common.texts.createdBy'),
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      align: "left",
      sortableKey: "create_time",
      sortable: true,
      date: true,
    },
    {
      label: td('common.texts.updatedTime'),
      prop: "updateTime",
      width: 150,
      align: "left",
      sortableKey: "update_time",
      sortable: true,
      date: true,
    },
  ],
  func: listDpDataElemAssetRel,
  params: {
    pageNum: 1,
    pageSize: 10,
    dataElemType: null,
    dataElemId: route.query.id,
    assetId: null,
    tableName: null,
    columnId: null,
    columnName: null,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const data = reactive({
  dpDataElemAssetRelDetail: {},
  form: {},
  rules: {}
});

const { form, dpDataElemAssetRelDetail, rules } = toRefs(data);

// Monitor id changes
watch(
  () => route.query.id,
  (newId) => {
    tableStore.params.dataElemId = newId;
    tableRef.value?.getList();
  },
  { immediate: true }
);

// form reset
function reset() {
  form.value = {
    id: null,
    dataElemType: null,
    dataElemId: null,
    assetId: null,
    tableName: null,
    columnId: null,
    columnName: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm('dpDataElemAssetRelRef');
}

/** Add button operation */
function handleAdd() {
  reset();
  proxy.$modal.msgWarning("Add logic not implemented in this refactor");
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpDataElemAssetRel(_id).then((response) => {
    form.value = response.data;
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.dataCode.confirmDeleteAsset', 'Are you sure to delete the data element-asset relation with ID "{id}"?', { id: _ids }))
    .then(function () {
      return delDpDataElemAssetRel(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** Export button action */
function handleExport() {
  proxy.download(
    'dp/dpDataElemAssetRel/export',
    {
      ...tableStore.params
    },
    `dpDataElemAssetRel_${new Date().getTime()}.xlsx`
  );
}
</script>
