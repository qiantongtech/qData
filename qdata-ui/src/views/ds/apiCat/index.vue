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
    <div class="app-container" ref="app-container">
        <GuideTip tip-id="cat/attApiCat.list" />
        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                v-show="showSearch" @submit.prevent>
                <el-form-item :label="td('ds.apiCat.title')" prop="name">
                    <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('ds.apiCat.searchPlaceholder')" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item :label="td('ds.apiCat.parentCategory')" prop="code">
                    <el-tree-select filterable class="el-form-input-width" v-model="queryParams.code"
                        :data="attApiCatOptions" :props="{ value: 'code', label: 'name', children: 'children' }"
                        value-key="id" :placeholder="td('ds.apiCat.parentPlaceholder')" check-strictly />
                </el-form-item>
                <el-form-item>
                    <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()"
                        v-hasPermi="['att:apiCat:query']">
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
                    </el-button>
                    <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
                    </el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:apiCat:add']"
                            @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button class="toggle-expand-all" type="primary" plain @click="toggleExpandAll">
                            <svg-icon v-if="isExpandAll" icon-class="toggle" />
                            <svg-icon v-else icon-class="expand" />
                            <span>{{ isExpandAll ? td('common.button.fold') : td('common.button.expand') }}</span>
                        </el-button>
                    </el-col>
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                        :columns="columns"></right-toolbar>
                </div>
            </div>
            <el-table v-if="refreshTable" v-loading="loading" :data="AttApiCatList" row-key="id"
                :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
                <el-table-column v-if="getColumnVisibility(0)"  :label="td('ds.apiCat.title')" align="left" prop="name" width="200"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>

                <el-table-column v-if="getColumnVisibility(1)"  :label="td('common.texts.description')" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
                    width="300">
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" :label="td('ds.apiCat.sortOrder')" align="left" prop="sortOrder" :show-overflow-tooltip="{ effect: 'light' }"
                    width="90">
                    <template #default="scope">
                        {{ scope.row.sortOrder }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(3)" :label="td('common.texts.createdBy')" align="center" prop="createBy">
                    <template #default="scope">
                        {{ scope.row.createBy || "-" }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                        }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(5)" :label="td('common.texts.status')" align="center" prop="validFlag">
                    <template #default="scope">
                        <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

                        <el-switch v-model="scope.row.validFlag" active-color="#13ce66" inactive-color="#ff4949"
                            @change="handleStatusChange(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>

                <el-table-column v-if="getColumnVisibility(6)" :label="td('common.texts.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                            v-hasPermi="['att:apiCat:edit']">{{ td('common.button.update') }}</el-button>
                        <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                            v-hasPermi="['att:apiCat:add']">{{ td('common.button.add') }}</el-button>
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                            v-hasPermi="['att:apiCat:remove']">{{ td('common.button.delete') }}</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
            /> -->
        </div>

      <!-- Add or modify data service category management dialog box -->
      <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
        </template>
        <el-form ref="AttApiCatRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="td('ds.apiCat.categoryName')" prop="name" :label-position="labelPosition">
                <el-input v-model="form.name" :placeholder="td('ds.apiCat.categoryNamePlaceholder')"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="td('ds.apiCat.parentCategory')" prop="parentId" :label-position="labelPosition">
                <el-tree-select filterable :disabled="form.id" v-model="form.parentId"
                                :data="attApiCatOptions" :props="{ value: 'id', label: 'name', children: 'children' }"
                                value-key="id" :placeholder="td('ds.apiCat.parentPlaceholder')" check-strictly />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20"> </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiCat.sortOrder')" prop="sortOrder" :label-position="labelPosition">
                            <el-input-number style="width: 100%" v-model="form.sortOrder" controls-position="right"
                                :min="0" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
                            <el-radio v-model="form.validFlag" :label="true">{{ td('ds.apiCat.enable') }}</el-radio>
                            <el-radio v-model="form.validFlag" :label="false">{{ td('ds.apiCat.disable') }}</el-radio>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
                            <el-input type="textarea" v-model="form.description" :placeholder="td('common.form.descriptionPlaceholder')"
                                :min-height="192" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
                            <el-input type="textarea" :placeholder="td('common.form.remarkPlaceholder')" v-model="form.remark" :min-height="192" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
                    <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- Data service category management details dialog box -->
        <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="AttApiCatRef" :model="form" label-width="80px" :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiCat.categoryNameInput')" prop="name" :label-position="labelPosition">
                            <div>
                                {{ form.name }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiCat.relatedParentId')" prop="parentId" :label-position="labelPosition">
                            <div>
                                {{ form.parentId }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiCat.categorySort')" prop="sortOrder" :label-position="labelPosition">
                            <div>
                                {{ form.sortOrder }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                            <div>
                                {{ form.description }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiCat.levelCode')" prop="code" :label-position="labelPosition">
                            <div>
                                {{ form.code }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
                            <div>
                                {{ form.remark }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="cancel">{{ td('common.button.close') }}</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- User import dialog -->
        <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']"
            draggable destroy-on-close>
            <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
                :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                <div class="el-upload__text" v-html="td('common.upload.dragOrClick')"></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                            <div class="el-upload__tip">
                                <el-checkbox v-model="upload.updateSupport" />{{td('ds.apiCat.importTip')}}
                            </div>
                            <span>{{td('ds.apiCat.uploadFormat')}}</span>
                        <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
                            @click="importTemplate">{{ td('common.upload.downloadTemplate') }}</el-link>
                    </div>
                </template>
            </el-upload>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="upload.open = false">{{ td('common.button.cancel') }}</el-button>
                    <el-button type="primary" :loading="submitLoading" @click="submitFileForm">{{ td('common.button.confirm') }}</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="ApiCat">
import {
    listAttApiCat,
    getAttApiCat,
    delAttApiCat,
    addAttApiCat,
    updateAttApiCat
} from '@/api/ds/apiCat/apiCat';
import { getToken } from '@/utils/auth.js';
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);

const AttApiCatList = ref([]);

// Show hidden information
const columns = ref([
  {key: 0, label: td('ds.apiCat.title'), visible: true},
  {key: 1, label: td('common.texts.description'), visible: true},
  {key: 2, label: td('ds.apiCat.sortOrder'), visible: true},
  {key: 3, label: td('common.texts.createdBy'), visible: true},
  {key: 4, label: td('common.texts.createdTime'), visible: true},
  {key: 5, label: td('common.texts.status'), visible: true},
  {key: 6, label: td('common.texts.remark'), visible: true }
]);

const getColumnVisibility = (key) => {
    const column = columns.value.find((col) => col.key === key);
    // If the corresponding column configuration is not found, it will be displayed by default.
    if (!column) return true;
    // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
    return column.visible;
};

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
const router = useRouter();
const attApiCatOptions = ref([]);
const isExpandAll = ref(false);
const refreshTable = ref(true);
/*** User import parameters */
const upload = reactive({
    // Whether to display the pop-up layer (user import)
    open: false,
    // Popup layer title (user imported)
    title: '',
    // Whether to disable uploading
    isUploading: false,
    // Whether to update existing user data
    updateSupport: 0,
    // Set upload request headers
    headers: { Authorization: 'Bearer ' + getToken() },
    // Upload address
    url: import.meta.env.VITE_APP_BASE_API + '/att/AttApiCat/importData'
});

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        parentId: null,
        sortOrder: null,
        description: null,
        code: null,
        createTime: null
    },
    rules: {
        name: [{ required: true, message: td('ds.apiCat.categoryNameRequired'), trigger: 'blur' }],
        parentId: [{ required: true, message: td('ds.apiCat.parentCategoryRequired'), trigger: 'blur' }]
    }
});

const { queryParams, form, rules } = toRefs(data);

/** Expand/collapse operations */
function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
        refreshTable.value = true;
    });
}

/** Query the data service category management list */
function getList() {
    loading.value = true;
    listAttApiCat(queryParams.value).then((response) => {
        AttApiCatList.value = proxy.handleTree(response.data, 'id', 'parentId');
        // total.value = response.data.total;
        loading.value = false;

        attApiCatOptions.value = [];
        const data = { id: 0, name: td('common.texts.topNode'), children: [] };
        data.children = proxy.handleTree(response.data, 'id', 'parentId');
        attApiCatOptions.value.push(data);
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
        name: null,
        parentId: null,
        sortOrder: 0,
        description: null,
        code: null,
        validFlag: true,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        remark: null
    };
    proxy.resetForm('AttApiCatRef');
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
function handleAdd(row) {
    reset();
    if (row != null && row.id) {
        form.value.parentId = row.id;
    } else {
        form.value.parentId = 0;
    }
    open.value = true;
    title.value = td('ds.apiCat.addCategory');
}

/** Modify button actions */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getAttApiCat(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = td('ds.apiCat.editCategory');
    });
}

/** Detail button operation */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getAttApiCat(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = td('ds.apiCat.detailCategory');
    });
}

/** submit button */
function submitForm() {
    if (submitLoading.value) return;
    submitLoading.value = true;
    proxy.$refs['AttApiCatRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateAttApiCat(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
                        open.value = false;
                        getList();
                        submitLoading.value = false;
                    })
                    .catch((error) => { submitLoading.value = false; });
            } else {
                addAttApiCat(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
                        open.value = false;
                        getList();
                        submitLoading.value = false;
                    })
                    .catch((error) => { submitLoading.value = false; });
            }
        } else {
            submitLoading.value = false;
        }
    });
}

/** Change enabled status value */
function handleStatusChange(row) {
    const text = row.validFlag === true ? td('ds.apiCat.enable') : td('ds.apiCat.disable');
    proxy.$modal
        .confirm(td('ds.apiCat.confirm' + (row.validFlag === true ? 'Enable' : 'Disable')) + row.name + td('ds.apiCat.confirmSuffix'))
        .then(function () {
            updateAttApiCat({ id: row.id, validFlag: row.validFlag }).then((response) => {
                proxy.$modal.msgSuccess(text + td('common.message.success'));
                getList();
            });
        })
        .catch(function () {
            row.validFlag = !row.validFlag;
        });
}

/** Delete button action */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal
        .confirm(td('ds.apiCat.deleteConfirm') + _ids + td('ds.apiCat.deleteConfirmSuffix'))
        .then(function () {
            return delAttApiCat(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
        })
        .catch(() => { });
}

/** Export button action */
function handleExport() {
    proxy.download(
        'att/AttApiCat/export',
        {
            ...queryParams.value
        },
        `AttApiCat_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
    upload.title = td('ds.apiCat.importTitle');
    upload.open = true;
}

/** Download template operation */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `AttApiCat_template_${new Date().getTime()}.xlsx`
    );
}

/** Submit upload file */
function submitFileForm() {
    if (submitLoading.value) return;
    submitLoading.value = true;
    proxy.$refs['uploadRef'].submit();
}

/**File upload is being processed */
const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
};

/** File upload successfully processed */
const handleFileSuccess = (response, file, fileList) => {
    upload.open = false;
    upload.isUploading = false;
    submitLoading.value = false;
    proxy.$refs['uploadRef'].handleRemove(file);
    proxy.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
        response.msg +
        '</div>',
        td('ds.apiCat.importResult'),
        { dangerouslyUseHTMLString: true }
    );
    getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
    if (link !== '' && link.indexOf('http') !== -1) {
        window.location.href = link;
        return;
    }
    if (link !== '') {
        if (link === router.currentRoute.value.path) {
            window.location.reload();
        } else {
            router.push({
                path: link,
                query: {
                    id: row.id
                }
            });
        }
    }
}

getList();
</script>