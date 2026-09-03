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
  <qt-wrap :columns="tableStore.columns" :tableRef="tableRef"     :config="{ fullContent: false, actions: { table: { search: false } } }">
    <template #actions-data>
      <el-row :gutter="15" class="btn-style">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            @click="openRuleSelector(undefined)"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.dataElem.detail.relate') }}
          </el-button>
        </el-col>
      </el-row>
    </template>

    <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
      <template #status="{ row }">
        {{ row.status == '1' ? td('dp.dataElem.detail.online') : td('dp.dataElem.detail.offline') }}
      </template>
      <template #action="{ row, $index }">
        <el-button
          link
          type="primary"
          icon="Edit"
          @click="openRuleDialog(row, $index + 1)"
        >{{ td('common.button.update') }}</el-button>
        <el-button
          link
          type="danger"
          icon="Delete"
          @click="handleRuleDelete(row)"
        >{{ td('common.button.delete') }}</el-button>
      </template>
    </qt-table>
  </qt-wrap>

  <RuleSelectorDialog ref="ruleSelectorDialog" @confirm="RuleSelectorconfirm" :type="3" />
</template>

<script setup name="dataElemClean">
import useDefaultLang from "@/composables/useDefaultLang"
import { ref } from 'vue';
import RuleSelectorDialog from '@/views/dpp/task/integratioTask/components/clean/rule/ruleBase.vue';
import { listDpDataElemRuleRel, dpDataElemRuleRel, putDpDataElemRuleRel, DlEPutDpDataElemRuleRel } from '@/api/dp/dataElem/dataElem';

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const props = defineProps({
  dataElemId: {
    required: true,
    type: String
  },
  ruleType: {
    required: true,
    type: String,
    default: "2"
  }
});

const tableRef = ref(null);
const ruleSelectorDialog = ref(null);

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  config: {
    table: { height: 400 },
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
      label: td('dp.dataElem.detail.cleanName'),
      prop: "name",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataElem.detail.cleanRuleName'),
      prop: "ruleName",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('common.texts.description'),
      prop: "ruleDescription",
      align: "left",
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataElem.detail.dimension'),
      prop: "dimensionType",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('common.texts.status'),
      prop: "status",
      width: 100,
      align: "left",
      slot: "status",
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
    {
      label: td('common.texts.operation'),
      slot: "action",
      width: 180,
      align: "center",
      fixed: "right",
    },
  ],
  func: listDpDataElemRuleRel,
  params: {
    dataElemId: props.dataElemId,
    type: 2,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const openRuleSelector = (row) => {
  ruleSelectorDialog.value.openDialog(row);
};

function RuleSelectorconfirm(obj) {
  let api = obj?.id ? putDpDataElemRuleRel : dpDataElemRuleRel;
  api({
    ...obj,
    rule: obj.ruleConfig,
    dataElemId: props.dataElemId,
    type: 2,
    ruleId: obj.ruleCode,
    ruleDescription: obj.ruleDesc,
    dimensionType: obj.parentName
  }).then((res) => {
    if (res.code == 200) {
      proxy.$message.success(res.msg);
      tableRef.value.refresh();
    }
  });
  ruleSelectorDialog.value.closeDialog();
}

const openRuleDialog = (row, index) => {
  ruleSelectorDialog.value.openDialog({
    ...row,
    ruleDesc: row.ruleDescription,
    dimensionType: row.parentName,
    ruleConfig: row.rule
  }, index);
};

function handleRuleDelete(row) {
  proxy.$modal
    .confirm(td('dp.dataElem.detail.confirmDeleteClean'))
    .then(function () {
      return DlEPutDpDataElemRuleRel(row.id);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}
</script>

<style lang="scss" scoped>
.btn-style {
  margin-bottom: 15px;
}
</style>
