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

        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
            <!-- 搜索栏插槽 -->
            <template #search>
                <qt-search-bar
                    v-bind="searchStore"
                    :params="tableStore.params"
                    @query="handleQuery"
                    @reset="resetQuery"
                    :tableRef="tableRef"
                />
            </template>

            <!-- 数据操作按钮插槽 -->
            <template #actions-data>
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            @click="handleAdd"
                            v-hasPermi="['da:sensitiveLevel:add']"
                        >
                            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                        </el-button>
                    </el-col>
                </el-row>
            </template>

            <!-- 表格组件 -->
            <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
                <!-- 状态切换插槽 -->
                <template #status="{ row }">
                    <el-switch
                        v-model="row.onlineFlag"
                        active-value="1"
                        inactive-value="0"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        @change="handleStatusChange(row)"
                    />
                </template>

                <!-- 操作列插槽 -->
                <template #action="{ row }">
                    <el-button
                        link
                        type="primary"
                        icon="Edit"
                        @click="handleUpdate(row)"
                        v-hasPermi="['da:sensitiveLevel:edit']"
                    >{{ td('common.button.update') }}</el-button>
                    <el-button
                        link
                        type="danger"
                        icon="Delete"
                        @click="handleDelete(row)"
                        v-hasPermi="['da:sensitiveLevel:remove']"
                    >{{ td('common.button.delete') }}</el-button>
                </template>
            </qt-table>
        </qt-wrap>

        <!-- Add or edit sensitive level dialog -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="daSensitiveLevelRef" :model="form" :rules="rules" label-width="132px" @submit.prevent :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.levelName')" prop="sensitiveLevel">
                            <el-input v-model="form.sensitiveLevel" :placeholder="td('da.security.levelNamePlaceholder')" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.replaceRule')" prop="sensitiveRule" :label-position="labelPosition">
                            <el-select v-model="form.sensitiveRule" :placeholder="td('da.security.replaceRulePlaceholder')">
                                <el-option v-for="dict in da_sensitive_level_rule" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20" v-if="form.sensitiveRule != '1' && form.sensitiveRule != null">
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.startCharPos')" prop="startCharLoc" :label-position="labelPosition">
                            <el-input v-model="form.startCharLoc" :placeholder="td('da.security.startCharPosPlaceholder')" @input="form.startCharLoc = $event.replace(/\D/g, '')" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.endCharPos')" prop="endCharLoc" :label-position="labelPosition">
                            <el-input v-model="form.endCharLoc" :placeholder="td('da.security.endCharPosPlaceholder')" @input="form.endCharLoc = $event.replace(/\D/g, '')" />
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
                        <el-form-item :label="td('da.security.description')" prop="description" :label-position="labelPosition">
                            <el-input v-model="form.description" type="textarea" :placeholder="td('da.security.descriptionPlaceholder')"  maxlength="256字符" show-word-limit />
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

        <!-- Sensitive level detail dialog -->
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
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.sensitiveLevelName')" prop="sensitiveLevel" :label-position="labelPosition">
                            <div>
                                {{ form.sensitiveLevel }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.replaceRule')" prop="sensitiveRule">
                            <dict-tag :options="da_sensitive_level_rule" :value="form.sensitiveRule" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.startCharPos')" prop="startCharLoc" :label-position="labelPosition">
                            <div>
                                {{ form.startCharLoc }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.endCharPos')" prop="endCharLoc" :label-position="labelPosition">
                            <div>
                                {{ form.endCharLoc }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.replaceContent')" prop="maskCharacter" :label-position="labelPosition">
                            <div>
                                {{ form.maskCharacter }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.onlineFlag')" prop="onlineFlag" :label-position="labelPosition">
                            <div>
                                {{ form.onlineFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('da.security.description')" prop="description" :label-position="labelPosition">
                            <div>
                                {{ form.description }}
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
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { da_sensitive_level_rule, da_sensitive_status } = proxy.useDict(
    'da_sensitive_level_rule',
    'da_sensitive_status'
);

const tableRef = ref(null);
const open = ref(false);
const openDetail = ref(false);
const title = ref('');

const tableStore = reactive({
    config: {
        initResquest: true,
    },
    columns: [
        { label: td('da.security.columnVisibility.id'), prop: "id", width: 80, align: "center", sortable: true },
        { label: td('da.security.columnVisibility.sensitiveLevelName'), prop: "sensitiveLevel", width: 170, align: "center" },
        { label: td('da.security.columnVisibility.description'), prop: "description", width: 350, align: "left", showOverflowTooltip: { effect: "light" } },
        { label: td('da.security.columnVisibility.replaceRule'), prop: "sensitiveRule", width: 140, align: "center", dict: 'da_sensitive_level_rule' },
        { label: td('da.security.columnVisibility.replaceContent'), prop: "maskCharacter", width: 140, align: "center" },
        { label: td('da.security.columnVisibility.createdBy'), prop: "createBy", width: 120, align: "center", showOverflowTooltip: { effect: 'light' } },
        { label: td('da.security.columnVisibility.createdTime'), prop: "createTime", width: 160, align: "center", sortable: true, date: true },
        { label: td('da.security.columnVisibility.onlineStatus'), prop: "onlineFlag", width: 160, align: "center", slot: "status" },
        { label: td('common.texts.operation'), width: 240, align: "center", fixed: "right", slot: "action" }
    ],
    func: listDaSensitiveLevel,
    params: {
        sensitiveLevel: null,
        sensitiveRule: null,
    }
});

const searchStore = reactive({
    items: [
        {
            label: td('da.security.levelName'),
            prop: "sensitiveLevel",
            component: { is: "input", placeholder: td('da.security.levelNamePlaceholder') }
        },
        {
            label: td('da.security.replaceRule'),
            prop: "sensitiveRule",
            component: {
                is: "select",
                placeholder: td('da.security.replaceRulePlaceholder'),
                options: da_sensitive_level_rule
            }
        }
    ]
});

/*** User import parameters */
const upload = reactive({
    // Whether to show the popup layer (user import)
    open: false,
    // Popup layer title (user import)
    title: '',
    // Whether to disable upload
    isUploading: false,
    // Whether to update existing user data
    updateSupport: 0,
    // Set upload request headers
    headers: { Authorization: 'Bearer ' + getToken() },
    // Upload URL
    url: import.meta.env.VITE_APP_BASE_API + '/da/daSensitiveLevel/importData'
});

const data = reactive({
    form: {
        onlineFlag: 0
    },
    rules: {
        sensitiveLevel: [{ required: true, message: td('da.security.levelNameRequired'), trigger: 'blur' }],
        maskCharacter: [{ required: true, message: td('da.security.replaceContentRequired'), trigger: 'blur' }],
        sensitiveRule: [{ required: true, message: td('da.security.replaceRuleRequired'), trigger: 'blur' }],
        description: [{ max: 256, message: td('common.form.descriptionLimit', { count: 256 }), trigger: 'blur' }],
        startCharLoc: [
            { required: true, message: td('da.security.startCharPosRequired'), trigger: 'blur' },
            { pattern: /^\d+$/, message: td('da.security.startCharPosPattern'), trigger: 'blur' }
        ],
        endCharLoc: [
            { required: true, message: td('da.security.endCharPosRequired'), trigger: 'blur' },
            { pattern: /^\d+$/, message: td('da.security.endCharPosPattern'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (value && form.value.startCharLoc && Number(value) <= Number(form.value.startCharLoc)) {
                        callback(new Error(td('da.security.endCharPosLessThanStart')));
                    } else {
                        callback();
                    }
                },
                trigger: 'blur'
            }
        ]
    }
});

const { form, rules } = toRefs(data);

// Cancel button
function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
}

// Reset form
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
        createBy: null,
        createTime: null
    };
    proxy.resetForm('daSensitiveLevelRef');
}

/** Search button operation */
function handleQuery() {
    tableStore.params.pageNum = 1;
}

/** Reset button operation */
function resetQuery() {
    // tableStore.params will be reset by qt-search-bar internally, 
    // but we can manually reset specific fields if needed
}

/** Add button operation */
function handleAdd() {
    reset();
    open.value = true;
    title.value = td('da.security.addTitle');
}

/** Edit button operation */
function handleUpdate(row) {
    reset();
    getDaSensitiveLevel(row.id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = td('da.security.editTitle');
    });
}

/** Submit button */
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
                        tableRef.value.refresh();
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
                        tableRef.value.refresh();
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

/** Delete button operation */
function handleDelete(row) {
    proxy.$modal
        .confirm(td('da.security.confirmDelete', '', { id: row.id }))
        .then(function () {
            return delDaSensitiveLevel(row.id);
        })
        .then(() => {
            tableRef.value.refresh();
            proxy.$modal.msgSuccess(td('da.security.deleteSuccess'));
        })
        .catch(() => { });
}

/** ---------------- Import related operations -----------------**/
/** Import button operation */
function handleImport() {
    upload.title = td('da.security.importTitle');
    upload.open = true;
}

/** Download template operation */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `daSensitiveLevel_template_${new Date().getTime()}.xlsx`
    );
}

/** Submit upload file */
function submitFileForm() {
    proxy.$refs['uploadRef'].submit();
}

/** File upload in progress handler */
const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
};

/** File upload success handler */
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
    tableRef.value.refresh();
};
/** ---------------------------------**/

/** Toggle enable status value */
function handleStatusChange(row) {
    const text = row.onlineFlag === '1' ? td('da.security.online') : td('da.security.offline');
    proxy.$modal
        .confirm(td('da.security.confirmStatusChange', '', { text: text, name: row.sensitiveLevel }))
        .then(function () {
            updateStatus(row.id, row.onlineFlag)
                .then((response) => {
                    proxy.$modal.msgSuccess(td('da.security.statusSuccess', '', { text: text }));
                    tableRef.value.refresh();
                })
                .catch((error) => {
                    row.onlineFlag = row.onlineFlag === '1' ? '0' : '1';
                });
        })
        .catch(function () {
            row.onlineFlag = row.onlineFlag === '1' ? '0' : '1';
        });
}
</script>
