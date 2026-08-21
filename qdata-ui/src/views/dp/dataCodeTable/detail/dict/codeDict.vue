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
                <el-button type="primary" plain @click="handleAdd" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="360" v-loading="loading" :data="dpDataElemCodeList"
        @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column :label="td('dp.dataCode.codeDict.serialNumber')" align="left" prop="id" width="50" />
        <el-table-column :label="td('dp.dataCode.codeValue')" align="left" prop="codeValue" width="160">
            <template #default="scope">
                {{ scope.row.codeValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataCode.codeName')" align="left" prop="codeName" width="220">
            <template #default="scope">
                {{ scope.row.codeName || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataCode.codeDict.createBy')" align="left" prop="createBy" width="160">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataCode.codeDict.createTime')" align="left" prop="createTime" width="220">
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataCode.codeDict.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dp.dataCode.codeDict.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="300">
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">{{ td('dp.dataCode.codeDict.modify') }}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">{{ td('dp.dataCode.codeDict.delete') }}</el-button>
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

    <!-- Add or modify data element code dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
        <el-form ref="dpDataElemCodeRef" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.dataCode.codeValue')" prop="codeValue" :label-position="labelPosition">
                        <el-input v-model="form.codeValue" :placeholder="td('dp.dataCode.codeValuePlaceholder')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dp.dataCode.codeName')" :show-overflow-tooltip="{ effect: 'light' }" prop="codeName" :label-position="labelPosition">
                        <el-input v-model="form.codeName" :placeholder="td('dp.dataCode.codeNamePlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('dp.dataCode.codeDict.remark')" :show-overflow-tooltip="{ effect: 'light' }" prop="remark" :label-position="labelPosition">
                        <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="ComponentOne">
import {
    listDpDataElemCode,
    getDpDataElemCode,
    delDpDataElemCode,
    addDpDataElemCode,
    updateDpDataElemCode,
    validateCodeValue
} from '@/api/dp/dataElem/dataElem';
import useDefaultLang from "@/composables/useDefaultLang";

const route = useRoute();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const dpDataElemCodeList = ref([]);

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
    dpDataElemCodeDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemId: null,
        codeValue: null,
        codeName: null,
        createTime: null
    },
    rules: {
        codeValue: [
            { required: true, message: td('dp.dataCode.codeValueRequired'), trigger: 'blur' },
            { validator: validatorCodeValue, trigger: 'blur' }
        ],
        codeName: [{ required: true, message: td('dp.dataCode.codeNameRequired'), trigger: 'blur' }]
    }
});

let id = route.query.id;

const { queryParams, form, dpDataElemCodeDetail, rules } = toRefs(data);
// Monitor id changes
watch(
    () => route.query.id,
    (newId) => {
        id = newId || -1; // If id is empty, the default value 1 is used
        getList();
    },
    { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
function validatorCodeValue(rule, value, callback) {
    if (value !== null && value !== undefined) {
        //Call the interface to determine whether there are duplicate values
        var params = {
            id: form.value.id || null,
            dataElemId: id,
            codeValue: value
        };
        validateCodeValue(params).then((res) => {
            if (res.data == 0) {
                callback(new Error(td('dp.dataCode.codeValueDuplicate')));
            } else {
                callback();
            }
        });
    } else {
        callback();
    }
}

/** Query the data element code list */
function getList() {
    if (id == -1) {
        return;
    }
    loading.value = true;
    queryParams.value.dataElemId = id;
    listDpDataElemCode(queryParams.value).then((response) => {
        dpDataElemCodeList.value = response.data.rows;
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
        dataElemId: null,
        codeValue: null,
        codeName: null,
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
    proxy.resetForm('dpDataElemCodeRef');
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
    title.value = td('dp.dataCode.addCodeTitle');
}

/** Modify button actions */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDpDataElemCode(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = td('dp.dataCode.editCodeTitle');
    });
}

/** submit button */
function submitForm() {
    proxy.$refs['dpDataElemCodeRef'].validate((valid) => {
        console.log(dpDataElemCodeDetail.value);
        form.value.dataElemId = id;
        if (valid) {
            if (form.value.id != null) {
                updateDpDataElemCode(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('dp.dataCode.codeDict.updateSuccess'));
                        open.value = false;
                        getList();
                        //event push
                        proxy.$bus.emit('data_elem_code_change');
                    })
                    .catch((error) => { });
            } else {
                addDpDataElemCode(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('dp.dataCode.codeDict.addSuccess'));
                        open.value = false;
                        getList();
                        //event push
                        proxy.$bus.emit('data_elem_code_change');
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
        .confirm(td('dp.dataCode.confirmDeleteCode', '', { id: _ids }))
        .then(function () {
            return delDpDataElemCode(_ids);
        })
        .then(() => {
            getList();
            //event push
            proxy.$bus.emit('data_elem_code_change');
            proxy.$modal.msgSuccess(td('dp.dataCode.codeDict.deleteSuccess'));
        })
        .catch(() => { });
}

// getList();
</script>
