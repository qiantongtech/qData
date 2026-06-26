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
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button type="primary" plain @click="openRuleSelector" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.dataElem.detail.relate') }}
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>

    <el-table stripe height="400" v-loading="loading" :data="dataList">
        <el-table-column :label="td('common.texts.number')" type="index" width="60" align="left">
            <template #default="scope">
                <span>{{
                    (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1
                    }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.detail.cleanName')" prop="name" align="left" width="200">
            <template #default="scope">
                {{ scope.row.name || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.detail.cleanRuleName')" align="left" prop="ruleName" :show-overflow-tooltip="{ effect: 'light' }"
            width="200">
            <template #default="scope">
                {{ scope.row.ruleName || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.description')" prop="description" align="left" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.ruleDescription || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.detail.dimension')" align="left" prop="dimensionTypeension" :show-overflow-tooltip="{ effect: 'light' }"
            width="150">
            <template #default="scope">
                {{ scope.row.dimensionType || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.status')" align="left" prop="status">
            <template #default="scope">
                {{ scope.row.status == '1' ? td('dp.dataElem.detail.online') : td('dp.dataElem.detail.offline') }}
            </template>
        </el-table-column>
        <!-- <el-table-column label="规则级别" prop="level" align="left" width="100">
            <template #default="scope">
                {{ formatValue(scope.row.level, att_rule_level) || '-' }}
            </template>
        </el-table-column> -->
        <!-- <el-table-column label="规则类型" prop="type" align="left" width="100">
            <template #default="scope">
                {{ formatValue(scope.row.type, att_rule_clean_type) || '-' }}
            </template>
        </el-table-column> -->
        <el-table-column :label="td('common.texts.createdBy')" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="createBy"
            width="120">
            <template #default="scope">
                {{ scope.row.createBy || "-" }}
            </template>
        </el-table-column>
        <!--  sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
        <el-table-column :label="td('common.texts.createdTime')" align="left" prop="createTime" width="150">
            <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
            }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.updatedTime')" align="left" prop="updateTime" width="300">
            <template #default="scope">
                <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="180">
            <template #default="scope">
                <!-- <el-button link type="primary" icon="view"
                    @click="openRuleDialog(scope.row, scope.$index + 1, true)">查看</el-button> -->
                <el-button link type="primary" icon="Edit"
                    @click="openRuleDialog(scope.row, scope.$index + 1)">{{ td('common.button.update') }}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleRuleDelete(scope.row)">{{ td('common.button.delete') }}</el-button>
            </template>
        </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    <RuleSelectorDialog ref="ruleSelectorDialog" @confirm="RuleSelectorconfirm" :type="3" />
</template>

<script setup name="dataElemClean">
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, watch } from 'vue';
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
        default: 2
    }
});
const { att_rule_clean_type, att_rule_level } = proxy.useDict(
    'att_rule_clean_type',
    'att_rule_level'
);

const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const dataList = ref([]);
const typeName = ref(null);

const data = reactive({
    form: {
        name: null,
        level: null,
        type: null,
    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemId: null,
        type: 2
    },
});
const { queryParams, form, } = toRefs(data);
queryParams.value.dataElemId = props.dataElemId;
/** 删除按钮操作 */
function handleRuleDelete(row) {
    const _ids = row.id;
    proxy.$modal
        .confirm(td('dp.dataElem.detail.confirmDeleteClean'))
        .then(function () {
            return DlEPutDpDataElemRuleRel(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
        })
        .catch(() => { });
}
let ruleSelectorDialog = ref()
const openRuleSelector = (row) => {
    ruleSelectorDialog.value.openDialog(row,);
};
function RuleSelectorconfirm(obj, mode) {
    let api = obj?.id ? putDpDataElemRuleRel : dpDataElemRuleRel;
    loading.value = true;
    api({ ...obj, rule: obj.ruleConfig, dataElemId: props.dataElemId, type: 2, ruleId: obj.ruleCode, ruleDescription: obj.ruleDesc, dimensionType: obj.parentName }).then((res) => {
        if (res.code == 200) {
            proxy.$message.success(res.msg);
            open.value = false;
            getList();
        } else {
            proxy.$message.warning(res.msg);
        }
    });
    loading.value = false;
    ruleSelectorDialog.value.closeDialog();
}
// 表单重置
function reset() {
    form.value = {
        name: null,
        level: null,
        type: null,
        qualityDim: null,
    };
    typeName.value = null;
}
const openRuleDialog = (row, index, falg) => {

    ruleSelectorDialog.value.openDialog({ ...row, ruleDesc: row.ruleDescription, dimensionType: row.parentName, ruleConfig: row.rule, }, index, falg);
};
/** 查询数据元列表 */
function getList() {
    loading.value = true;
    listDpDataElemRuleRel(queryParams.value).then((response) => {
        dataList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}



getList();
</script>
<style lang="scss" scoped>
.base-info {
    margin-top: 5px;

    .type-name {
        color: #000;
        font-size: 20px;
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
