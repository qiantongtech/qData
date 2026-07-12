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

        <GuideTip tip-id="da/daSensitiveLevel/daSensitiveLevel.list" />

        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                v-show="showSearch" @submit.prevent>
                <el-form-item :label="td('da.security.levelName')" prop="sensitiveLevel" :label-position="labelPosition">
                    <el-input class="el-form-input-width" v-model="queryParams.sensitiveLevel" :placeholder="td('da.security.levelNamePlaceholder')"
                        clearable @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item :label="td('da.security.replaceRule')" prop="sensitiveRule" :label-position="labelPosition">
                    <el-select class="el-form-input-width" v-model="queryParams.sensitiveRule" :placeholder="td('da.security.replaceRulePlaceholder')"
                        clearable>
                        <el-option v-for="dict in da_sensitive_level_rule" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
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
                        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['da:sensitiveLevel:add']"
                            @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                        </el-button>
                    </el-col>
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                        :columns="columns"></right-toolbar>
                </div>
            </div>
            <el-table stripe v-loading="loading" :data="daSensitiveLevelList" @selection-change="handleSelectionChange"
                :default-sort="defaultSort" @sort-change="handleSortChange">
                <!--       <el-table-column type="selection" width="55" align="center" />-->
                <el-table-column v-if="getColumnVisibility(1)" :label="td('da.security.columnVisibility.id')" align="center" prop="id" width="80">
                    <template #default="scope">
                        {{ scope.row.id || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" :label="td('da.security.columnVisibility.sensitiveLevelName')" align="center" prop="sensitiveLevel"  width="170">
                    <template #default="scope">
                        {{ scope.row.sensitiveLevel || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(3)" width="350" :label="td('da.security.columnVisibility.description')" align="left" prop="description">
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" :label="td('da.security.columnVisibility.replaceRule')" align="center" prop="sensitiveRule" width="140">
                    <template #default="scope">
                        <dict-tag :options="da_sensitive_level_rule" :value="scope.row.sensitiveRule" />
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(5)" :label="td('da.security.columnVisibility.replaceContent')" align="center" prop="maskCharacter" width="140">
                    <template #default="scope">
                        {{ scope.row.maskCharacter || '-' }}
                    </template>
                </el-table-column>

                <el-table-column v-if="getColumnVisibility(6)" :label="td('da.security.columnVisibility.createdBy')" width="120" align="center" prop="createBy"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(7)" :label="td('da.security.columnVisibility.createdTime')" align="center" prop="createTime" width="160"
                    sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']">
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}')
                            }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(8)" :label="td('da.security.columnVisibility.onlineStatus')" align="center" prop="onlineFlag"
                    width="160">
                    <template #default="scope">
                        <el-switch v-model="scope.row.onlineFlag" active-color="#13ce66" inactive-color="#ff4949"
                            active-value="1" inactive-value="0" @change="handleStatusChange(scope.row)" />
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(9)" :label="td('da.security.columnVisibility.remark')" align="left" prop="remark"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(10)" :label="td('common.texts.operation')" align="center"
                    class-name="small-padding fixed-width" fixed="right" width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                            v-hasPermi="['da:sensitiveLevel:edit']">{{ td('common.button.update') }}</el-button>
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                            v-hasPermi="['da:sensitiveLevel:remove']">{{ td('common.button.delete') }}</el-button>
                        <!--           <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"-->
                        <!--                      v-hasPermi="['da:sensitiveLevel:edit']">详情</el-button>-->
                        <!--           <el-button link type="primary" icon="view" @click="routeTo('/da/sensitiveLevel/daSensitiveLevelDetail',scope.row)"-->
                        <!--                      v-hasPermi="['da:sensitiveLevel:edit']">复杂详情</el-button>-->
                    </template>
                </el-table-column>

                <template #empty>
                    <div class="emptyBg">
                        <img src="../../../../assets/images/system/no_data/empty-nodata.png" alt="" />
                        <p>{{td('common.noData')}}</p>
                    </div>
                </template>
            </el-table>

            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>

        <!-- 新增或修改敏感等级对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="daSensitiveLevelRef" :model="form" :rules="rules" label-width="132px" @submit.prevent :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.levelName')" prop="sensitiveLevel">
                            <el-input v-model="form.sensitiveLevel" :placeholder="td('da.security.levelNamePlaceholder')" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.replaceRule')" prop="sensitiveRule" :label-position="labelPosition">
                            <el-select v-model="form.sensitiveRule" :placeholder="td('da.security.replaceRulePlaceholder')">
                                <el-option v-for="dict in da_sensitive_level_rule" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20" v-if="form.sensitiveRule != '1' && form.sensitiveRule != null">
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.startCharPos')" prop="startCharLoc" :label-position="labelPosition">
                            <el-input v-model="form.startCharLoc" :placeholder="td('da.security.startCharPosPlaceholder')" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.endCharPos')" prop="endCharLoc" :label-position="labelPosition">
                            <el-input v-model="form.endCharLoc" :placeholder="td('da.security.endCharPosPlaceholder')" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.replaceContent')" prop="maskCharacter" :label-position="labelPosition">
                            <el-input v-model="form.maskCharacter" :placeholder="td('da.security.replaceContentPlaceholder')" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.description')" prop="description" :label-position="labelPosition">
                            <el-input v-model="form.description" type="textarea" :placeholder="td('da.security.descriptionPlaceholder')" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.onlineStatus')" prop="onlineFlag" :label-position="labelPosition">
                            <el-radio-group v-model="form.onlineFlag">
                                <el-radio v-for="dict in da_sensitive_status" :key="dict.value" :value="dict.value">{{
                                    dict.label }}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
                            <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
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

        <!-- 敏感等级详情对话框 -->
        <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                    <el-icon size="20" style="color: #909399; font-size: 16px">
                        <InfoFilled />
                    </el-icon>
                </span>
            </template>
            <el-form ref="daSensitiveLevelRef" :model="form" label-width="80px" :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.sensitiveLevelName')" prop="sensitiveLevel" :label-position="labelPosition">
                            <div>
                                {{ form.sensitiveLevel }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.replaceRule')" prop="sensitiveRule">
                            <dict-tag :options="da_sensitive_level_rule" :value="form.sensitiveRule" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.startCharPos')" prop="startCharLoc" :label-position="labelPosition">
                            <div>
                                {{ form.startCharLoc }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.endCharPos')" prop="endCharLoc" :label-position="labelPosition">
                            <div>
                                {{ form.endCharLoc }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.replaceContent')" prop="maskCharacter" :label-position="labelPosition">
                            <div>
                                {{ form.maskCharacter }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.onlineFlag')" prop="onlineFlag" :label-position="labelPosition">
                            <div>
                                {{ form.onlineFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('da.security.description')" prop="description" :label-position="labelPosition">
                            <div>
                                {{ form.description }}
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
                <div class="el-upload__text">{{ td('common.upload.dragOrClick') }}</div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <div class="el-upload__tip">
                            <el-checkbox v-model="upload.updateSupport" />{{ td('common.upload.updateExistingData') }}
                        </div>
                        <span>{{ td('common.upload.fileFormat') }}</span>
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

<script setup name="SensitiveLevel">
import {
    listDaSensitiveLevel,
    getDaSensitiveLevel,
    delDaSensitiveLevel,
    addDaSensitiveLevel,
    updateDaSensitiveLevel,
    updateStatus
} from '@/api/da/security/sensitiveLevel/sensitiveLevel';
import { getToken } from '@/utils/auth.js';
import { updateDaAsset } from '@/api/da/asset/asset.js';
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { da_sensitive_level_rule, da_sensitive_status } = proxy.useDict(
    'da_sensitive_level_rule',
    'da_sensitive_status'
);
const daSensitiveLevelList = ref([]);

// 列显隐信息
const columns = ref([
    { key: 1, label: td('da.security.columnVisibility.id'), visible: true },
    { key: 2, label: td('da.security.columnVisibility.sensitiveLevelName'), visible: true },
    { key: 3, label: td('da.security.columnVisibility.description'), visible: true },
    { key: 4, label: td('da.security.columnVisibility.replaceRule'), visible: true },
    { key: 5, label: td('da.security.columnVisibility.replaceContent'), visible: true },
    { key: 6, label: td('da.security.columnVisibility.createdBy'), visible: true },
    { key: 7, label: td('da.security.columnVisibility.createdTime'), visible: true },
    { key: 8, label: td('da.security.columnVisibility.onlineStatus'), visible: true },
    { key: 9, label: td('da.security.columnVisibility.remark'), visible: true },
    { key: 10, label: td('common.texts.operation'), visible: true }
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
const defaultSort = ref({ columnKey: 'reate_time', order: 'desc' });
const router = useRouter();

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
    url: import.meta.env.VITE_APP_BASE_API + '/da/daSensitiveLevel/importData'
});

const data = reactive({
    form: {
        onlineFlag: 0
    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        sensitiveLevel: null,
        sensitiveRule: null,
        startCharLoc: null,
        endCharLoc: null,
        maskCharacter: null,
        onlineFlag: null,
        description: null,
        createTime: null
    },
    rules: {
        sensitiveLevel: [{ required: true, message: td('da.security.levelNameRequired'), trigger: 'blur' }],
        maskCharacter: [{ required: true, message: td('da.security.replaceContentRequired'), trigger: 'blur' }],
        sensitiveRule: [{ required: true, message: td('da.security.replaceRuleRequired'), trigger: 'blur' }]
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询敏感等级列表 */
function getList() {
    loading.value = true;
    listDaSensitiveLevel(queryParams.value).then((response) => {
        daSensitiveLevelList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
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
        sensitiveLevel: null,
        sensitiveRule: null,
        startCharLoc: null,
        endCharLoc: null,
        maskCharacter: null,
        onlineFlag: '0',
        description: null,
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
    proxy.resetForm('daSensitiveLevelRef');
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
function handleSortChange({ column, prop, order }) {
    queryParams.value.orderByColumn = column?.columnKey || prop;
    queryParams.value.isAsc = column.order;
    getList();
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = td('da.security.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDaSensitiveLevel(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = td('da.security.editTitle');
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDaSensitiveLevel(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = td('da.security.detailTitle');
    });
}

/** 提交按钮 */
function submitForm() {
    if (submitLoading.value) return;
    submitLoading.value = true;
    proxy.$refs['daSensitiveLevelRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateDaSensitiveLevel(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('da.security.editSuccess'));
                        open.value = false;
                        getList();
                        submitLoading.value = false;
                    })
                    .catch((error) => {
                        submitLoading.value = false;
                    });
            } else {
                addDaSensitiveLevel(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('da.security.addSuccess'));
                        open.value = false;
                        getList();
                        submitLoading.value = false;
                    })
                    .catch((error) => {
                        submitLoading.value = false;
                    });
            }
        } else {
            submitLoading.value = false;
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal
        .confirm(td('da.security.confirmDelete', '', { id: _ids }))
        .then(function () {
            return delDaSensitiveLevel(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess(td('da.security.deleteSuccess'));
        })
        .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download(
        'da/daSensitiveLevel/export',
        {
            ...queryParams.value
        },
        `daSensitiveLevel_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
    upload.title = td('da.security.importTitle');
    upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `daSensitiveLevel_template_${new Date().getTime()}.xlsx`
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
        td('da.security.importResult'),
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

/** 启用禁用开关 */
function handleStatusChange(row) {
    const text = row.onlineFlag === '1' ? td('da.security.online') : td('da.security.offline');
    proxy.$modal
        .confirm(td('da.security.confirmStatusChange', '', { text: text, name: row.sensitiveLevel }))
        .then(function () {
            updateStatus(row.id, row.onlineFlag)
                .then((response) => {
                    proxy.$modal.msgSuccess(td('da.security.statusSuccess', '', { text: text }));
                    getList();
                })
                .catch((error) => {
                    row.onlineFlag = !row.onlineFlag;
                });
        })
        .catch(function () {
            row.onlineFlag = !row.onlineFlag;
        });
}

getList();
</script>
