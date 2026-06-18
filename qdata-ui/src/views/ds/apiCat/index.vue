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

      <!-- 添加或修改数据服务类目管理对话框 -->
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
                    <el-button type="primary" size="mini" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 数据服务类目管理详情对话框 -->
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

        <!-- 用户导入对话框 -->
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
                    <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm') }}</el-button>
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

const AttApiCatList = ref([]);

// 列显隐信息
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
    // 如果没有找到对应列配置，默认显示
    if (!column) return true;
    // 如果找到对应列配置，根据visible属性来控制显示
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
/*** 用户导入参数 */
const upload = reactive({
    // 是否显示弹出层（用户导入）
    open: false,
    // 弹出层标题（用户导入）
    title: '',
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的用户数据
    updateSupport: 0,
    // 设置上传的请求头部
    headers: { Authorization: 'Bearer ' + getToken() },
    // 上传的地址
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

/** 展开/折叠操作 */
function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
        refreshTable.value = true;
    });
}

/** 查询数据服务类目管理列表 */
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

// 取消按钮
function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
}

// 表单重置
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

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}

/** 重置按钮操作 */
function resetQuery() {
    proxy.resetForm('queryRef');
    handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
    ids.value = selection.map((item) => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
    queryParams.value.orderByColumn = column.prop;
    queryParams.value.isAsc = column.order;
    getList();
}

/** 新增按钮操作 */
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

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getAttApiCat(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = td('ds.apiCat.editCategory');
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getAttApiCat(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = td('ds.apiCat.detailCategory');
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['AttApiCatRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateAttApiCat(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addAttApiCat(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            }
        }
    });
}

/** 改变启用状态值 */
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

/** 删除按钮操作 */
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

/** 导出按钮操作 */
function handleExport() {
    proxy.download(
        'att/AttApiCat/export',
        {
            ...queryParams.value
        },
        `AttApiCat_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
    upload.title = td('ds.apiCat.importTitle');
    upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `AttApiCat_template_${new Date().getTime()}.xlsx`
    );
}

/** 提交上传文件 */
function submitFileForm() {
    proxy.$refs['uploadRef'].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
    upload.open = false;
    upload.isUploading = false;
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