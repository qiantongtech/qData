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

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  config: {
    table: { height: 360 },
  },
  columns: [
    {
      label: td('dp.document.asset.id'),
      prop: "id",
      width: 60,
      align: "left",
      sortable: true,
    },
    {
      label: td('dp.document.asset.assetName'),
      prop: "assetName",
      align: "left",
      width: 300,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.document.asset.description'),
      prop: "description",
      align: "left",
      width: 380,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.document.asset.tableName'),
      prop: "tableName",
      align: "left",
      width: 290,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.document.asset.columnName'),
      prop: "columnName",
      align: "left",
      width: 300,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.document.asset.createBy'),
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.document.asset.createTime'),
      prop: "createTime",
      width: 150,
      align: "left",
      sortableKey: "create_time",
      sortable: true,
      date: true,
    },
    {
      label: td('dp.document.asset.updateTime'),
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
    dataElemId: route.query.id,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

// Monitor id changes
watch(
  () => route.query.id,
  (newId) => {
    tableStore.params.dataElemId = newId;
    tableRef.value?.refresh();
  }
);
</script>
