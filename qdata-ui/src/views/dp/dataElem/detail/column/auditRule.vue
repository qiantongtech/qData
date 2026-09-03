<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
江苏 Qiantong Technology Co., Ltd.

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
      <template #qualityDim="{ row }">
        <dict-tag :options="att_rule_audit_q_dimension" :value="row.dimensionType" />
      </template>
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

  <RuleSelectorDialog ref="ruleSelectorDialog" @confirm="RuleSelectorconfirm" :dppQualityTaskObjSaveReqVO="[]"
    :type="3" />
</template>

<script setup name="dataElemAudit">
import useDefaultLang from "@/composables/useDefaultLang"
import { ref } from 'vue';
import { listDpDataElemRuleRel, dpDataElemRuleRel, putDpDataElemRuleRel, DlEPutDpDataElemRuleRel } from '@/api/dp/dataElem/dataElem';
import RuleSelectorDialog from '@/views/da/quality/qualityTask/components/ruleBase.vue';

const { proxy } = getCurrentInstance();

const props = defineProps({
  dataElemId: {
    required: true,
    type: String
  },
  ruleType: {
    required: true,
    type: String,
    default: "1"
  }
});

const { td } = useDefaultLang();
const { att_rule_audit_q_dimension } = proxy.useDict(
  'att_rule_audit_q_dimension',
  'att_rule_audit_type',
  'att_rule_level'
);

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
      label: td('dp.dataElem.detail.auditName'),
      prop: "name",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataElem.detail.auditRuleName'),
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
      label: td('dp.dataElem.detail.qualityDimension'),
      prop: "qualityDim",
      align: "left",
      width: 160,
      slot: "qualityDim",
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
      label: td('common.texts.status'),
      prop: "status",
      width: 100,
      align: "left",
      slot: "status",
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
    type: 1,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const openRuleSelector = () => {
  ruleSelectorDialog.value.openDialog(undefined, undefined, undefined);
}

const openRuleDialog = (row, index) => {
  ruleSelectorDialog.value.openDialog({ ...row, ruleConfig: row.rule }, index);
};

function RuleSelectorconfirm(obj) {
  let api = obj?.id ? putDpDataElemRuleRel : dpDataElemRuleRel;
  api({ ...obj, dataElemId: props.dataElemId, type: 1, ruleId: obj.ruleCode }).then((res) => {
    if (res.code == 200) {
      proxy.$message.success(res.msg);
      tableRef.value.refresh();
    }
  });
  ruleSelectorDialog.value.closeDialog();
}

function handleRuleDelete(row) {
  proxy.$modal
    .confirm(td('dp.dataElem.detail.confirmDeleteAudit'))
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
