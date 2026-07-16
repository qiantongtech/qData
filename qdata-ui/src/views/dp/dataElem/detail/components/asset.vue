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
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="360" v-loading="loading" :data="dpDataElemAssetRelList"
        @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column :label="td('dp.dataElem.asset.id')" align="left" prop="id" width="50" />
        <el-table-column :label="td('dp.dataElem.asset.assetName')" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="assetName"
            width="300">
            <template #default="scope">
                {{ scope.row.assetName || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.asset.description')" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="description"
            width="380">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.asset.tableName')" align="left" prop="tableName" width="290">
            <template #default="scope">
                {{ scope.row.tableName || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.asset.columnName')" align="left" prop="columnName" width="300">
            <template #default="scope">
                {{ scope.row.columnName || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.asset.createBy')" :show-overflow-tooltip="{ effect: 'light' }" align="left" width="120"
            prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || "-" }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.asset.createTime')" align="left" prop="createTime" width="150">
            <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
            }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataElem.asset.updateTime')" align="left" prop="updateTime" width="300">
            <template #default="scope">
                <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</span>
            </template>
        </el-table-column>

        <template #empty>
            <div class="emptyBg">
                <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
                <p>{{td('common.noData')}}</p>
            </div>
        </template>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
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

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const route = useRoute();

const dpDataElemAssetRelList = ref([]);

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const defaultSort = ref({ prop: 'createTime', order: 'desc' });

const data = reactive({
    dpDataElemAssetRelDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemType: null,
        dataElemId: null,
        assetId: null,
        tableName: null,
        columnId: null,
        columnName: null,
        createTime: null
    },
    rules: {}
});

const { queryParams, form, dpDataElemAssetRelDetail, rules } = toRefs(data);

queryParams.value.dataElemId = route.query.id;
// Monitor id changes
watch(
    () => route.query.id,
    (newId) => {
        queryParams.value.dataElemId = newId;
        getList();
    },
    { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
/** Query the data metadata asset related information list */
function getList() {
    loading.value = true;
    listDpDataElemAssetRel(queryParams.value).then((response) => {
        dpDataElemAssetRelList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}

// Cancel button
function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
}

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

/** Search button action */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}

/** reset button action */
function resetQuery() {
    proxy.resetForm('queryRef');
    handleQuery();
}

// Multiple selection box selected data
function handleSelectionChange(selection) {
    ids.value = selection.map((item) => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}

/** Sorting trigger events */
function handleSortChange(column, prop, order) {
    queryParams.value.orderByColumn = column.prop;
    queryParams.value.isAsc = column.order;
    getList();
}

/** Add button operation */
function handleAdd() {
    reset();
    open.value = true;
    title.value = td('dp.dataElem.asset.addTitle');
}

/** Modify button actions */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDpDataElemAssetRel(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = td('dp.dataElem.asset.editTitle');
    });
}

/** Detail button operation */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDpDataElemAssetRel(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = td('dp.dataElem.asset.detailTitle');
    });
}

/** submit button */
function submitForm() {
    proxy.$refs['dpDataElemAssetRelRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateDpDataElemAssetRel(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('dp.dataElem.asset.updateSuccess'));
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDpDataElemAssetRel(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('dp.dataElem.asset.addSuccess'));
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            }
        }
    });
}

/** Delete button action */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal
        .confirm(td('dp.dataElem.asset.deleteConfirm', '', { id: _ids }))
        .then(function () {
            return delDpDataElemAssetRel(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess(td('dp.dataElem.asset.deleteSuccess'));
        })
        .catch(() => { });
}

/** Export button action */
function handleExport() {
    proxy.download(
        'dp/dpDataElemAssetRel/export',
        {
            ...queryParams.value
        },
        `dpDataElemAssetRel_${new Date().getTime()}.xlsx`
    );
}

getList();
</script>
