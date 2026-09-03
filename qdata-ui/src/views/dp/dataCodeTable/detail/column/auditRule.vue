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
    <template #actions-data>
      <el-row :gutter="15" class="btn-style">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            @click="openRuleSelector(undefined)"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.dataCode.detail.relate') }}
          </el-button>
        </el-col>
      </el-row>
    </template>

    <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
      <template #qualityDim="{ row }">
        <dict-tag :options="att_rule_audit_q_dimension" :value="row.dimensionType" />
      </template>
      <template #status="{ row }">
        {{ row.status == '1' ? td('dp.dataCode.detail.online') : td('dp.dataCode.detail.offline') }}
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
    sort: true,
    initResquest: true,
    table: {
      stripe: true,
      height: 400,
    },
  },
  columns: [
    {
      label: td('common.texts.number'),
      type: "index",
      width: 60,
      align: "left",
    },
    {
      label: td('dp.dataCode.detail.auditName'),
      prop: "name",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.detail.auditRuleName'),
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
      label: td('dp.dataCode.detail.qualityDimension'),
      prop: "dimensionType",
      slot: "qualityDim",
      align: "left",
      width: 100,
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
      align: "left",
      slot: "status",
      width: 100,
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
    pageNum: 1,
    pageSize: 10,
    dataElemId: props.dataElemId,
    type: 1,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const openRuleSelector = () => {
  ruleSelectorDialog.value.openDialog(undefined, undefined, undefined);
}

const openRuleDialog = (row, index, flag) => {
  ruleSelectorDialog.value.openDialog({ ...row, ruleConfig: row.rule }, index, flag);
};

function RuleSelectorconfirm(obj) {
  let api = obj?.id ? putDpDataElemRuleRel : dpDataElemRuleRel;
  api({ ...obj, dataElemId: props.dataElemId, type: 1, ruleId: obj.ruleCode }).then((res) => {
    if (res.code == 200) {
      proxy.$message.success(res.msg);
      tableRef.value.getList();
      ruleSelectorDialog.value.closeDialog();
    }
  });
}

/** Delete button action */
function handleRuleDelete(row) {
  const _ids = row.id;
  proxy.$modal
    .confirm(td('dp.dataCode.detail.confirmDeleteAudit'))
    .then(function () {
      return DlEPutDpDataElemRuleRel(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}
</script>
<style lang="scss" scoped>
.base-info {
    margin-top: 5px;

    .type-name {
        color: #000;
        font-size: 18px;
        font-weight: bold;
    }

    .base-content {
        margin-top: 20px;
        padding-left: 25px;

        :deep(.el-form-item__label) {
            padding: 0 0 0 0 !important;
        }

        :deep(.el-form-item) {
            margin-bottom: 5px;
        }
    }
}

.hint-div {
    margin: 10px 0px 20px 20px;
    border-top: 1px solid rgba(204, 204, 204, 0.5);
    border-right: 1px solid rgba(204, 204, 204, 0.5);
    border-bottom: 1px solid #e5f1f8;
    border-left: 1px solid #e5f1f8;
    border-radius: 2px;
    padding: 10px;
    box-shadow: -1px 1px 2px #e5f1f8;
    display: flex;
    align-items: center;

    span {
        margin-left: 5px;
    }
}

// Set only leaf nodes to have multiple selection boxes
:deep(.el-tree-node) {
    .is-leaf+.el-checkbox .el-checkbox__inner {
        display: inline-block !important;
    }

    .el-checkbox__input>.el-checkbox__inner {
        display: none;
    }
}
</style>
