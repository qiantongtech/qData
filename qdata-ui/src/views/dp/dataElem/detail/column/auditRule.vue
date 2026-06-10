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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button type="primary" plain @click="openRuleSelector(undefined)"
                    @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-xinzeng mr5"></i>关联
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="400" v-loading="loading" :data="dataList" :default-sort="defaultSort">
        <el-table-column :label="t('common.texts.number')" type="index" width="60" align="left">
            <template #default="scope">
                <span>{{
                    (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1
                    }}</span>
            </template>
        </el-table-column>
        <el-table-column label="稽查名称" prop="name" align="left" width="200">
            <template #default="scope">
                {{ scope.row.name || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="稽查规则" prop="ruleName" align="left" width="200">
            <template #default="scope">
                {{ scope.row.ruleName || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.description')" prop="ruleDescription" align="left" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.ruleDescription || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="质量维度" prop="qualityDim" align="left" width="100">
            <template #default="scope">
                <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.createdBy')" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="createBy"
            width="120">
            <template #default="scope">
                {{ scope.row.createBy || "-" }}
            </template>
        </el-table-column>
        <!--  sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
        <el-table-column :label="t('common.texts.createdTime')" align="left" prop="createTime" width="150">
            <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
            }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.updatedTime')" align="left" prop="updateTime" width="300">
            <template #default="scope">
                <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.status')" align="left" prop="status">
            <template #default="scope">
                {{ scope.row.status == '1' ? '上线' : '下线' }}
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="180">
            <template #default="scope">
                <!-- <el-button link type="primary" icon="view"
                    @click="openRuleDialog(scope.row, scope.$index + 1, true)">查看</el-button> -->
                <el-button link type="primary" icon="Edit"
                    @click="openRuleDialog(scope.row, scope.$index + 1)">{{ t('common.button.update') }}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleRuleDelete(scope.row)">{{ t('common.button.delete') }}</el-button>

            </template>
        </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    <RuleSelectorDialog ref="ruleSelectorDialog" @confirm="RuleSelectorconfirm" :dppQualityTaskObjSaveReqVO="[]"
        :type="3" />
</template>

<script setup name="dataElemAudit">
import { useI18n } from 'vue-i18n'
import { ref, watch } from 'vue';

;

const { proxy } = getCurrentInstance();

const props = defineProps({
    dataElemId: {
        required: true,
        type: String
    },
    ruleType: {
        required: true,
        type: String,
        default: 1
    }
});

import { listDpDataElemRuleRel, dpDataElemRuleRel, putDpDataElemRuleRel, DlEPutDpDataElemRuleRel } from '@/api/dp/dataElem/dataElem';
import RuleSelectorDialog from '@/views/da/quality/qualityTask/components/ruleBase.vue';

const { t } = useI18n();
const { att_rule_audit_q_dimension, att_rule_audit_type, att_rule_level, } = proxy.useDict(
    'att_rule_audit_q_dimension',
    'att_rule_audit_type',
    'att_rule_level'
);

const loading = ref(true);
const showSearch = ref(true);
const defaultSort = ref({ prop: 'createTime', order: 'desc' });
const total = ref(0);
const dataList = ref([]);
const typeName = ref(null);

const data = reactive({
    form: {
        name: null,
        level: null,
        type: null,
        qualityDim: null,
    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemId: null,
        type: 1
    },
    defaultProps: {
        children: 'children',
        label: 'name',
        isLeaf: 'isLeaf'
    }
});
const { queryParams, form, defaultProps } = toRefs(data);
const open = ref(false);
const title = ref('');
const sourceName = ref('');
queryParams.value.dataElemId = props.dataElemId;

watch(sourceName, (val) => {
    proxy.$refs.tree.filter(val);
});

let ruleSelectorDialog = ref()
const openRuleSelector = (row) => {
    ruleSelectorDialog.value.openDialog(undefined, undefined, undefined);
}
const openRuleDialog = (row, index, falg) => {
    console.log("🚀 ~ openRuleDialog ~ row33:", row)
    ruleSelectorDialog.value.openDialog({ ...row, ruleConfig: row.rule, }, index, falg);
};
function RuleSelectorconfirm(obj, mode) {
    loading.value = true;
    let api = obj?.id ? putDpDataElemRuleRel : dpDataElemRuleRel;
    api({ ...obj, dataElemId: props.dataElemId, type: 1, ruleId: obj.ruleCode, }).then((res) => {
        console.log("🚀 ~ RuleSelectorconfirm ~ obj:", obj)
        if (res.code == 200) {
            proxy.$message.success(res.msg);
            open.value = false;
            getList();
        }
    });
    loading.value = false;
    ruleSelectorDialog.value.closeDialog();
}





//查询数据元列表
function getList() {
    loading.value = true;
    listDpDataElemRuleRel(queryParams.value).then((response) => {
        dataList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}

/** 删除按钮操作 */
function handleRuleDelete(row) {
    const _ids = row.id;
    proxy.$modal
        .confirm('是否确认删除关联稽查规则数据？')
        .then(function () {
            return DlEPutDpDataElemRuleRel(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
        })
        .catch(() => { });
}


getList();
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

// 设置只有叶子节点有多选框
:deep(.el-tree-node) {
    .is-leaf+.el-checkbox .el-checkbox__inner {
        display: inline-block !important;
    }

    .el-checkbox__input>.el-checkbox__inner {
        display: none;
    }
}
</style>
