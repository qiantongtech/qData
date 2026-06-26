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
    <el-dialog :title="title" v-model="visible" class="warn-dialog" :append-to="$refs['app-container']" draggable>
        <el-form ref="formRef" :model="form" label-width="100px" @submit.prevent :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.standardCode')" prop="code" :rules="[
                        { required: true, message: td('dp.document.standardCodeRequired'), trigger: 'blur' }
                    ]" :label-position="labelPosition">
                        <el-input v-model="form.code" :placeholder="td('dp.document.standardCodePlaceholder')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.standardName')" prop="name" :rules="[
                        { required: true, message: td('dp.document.standardNameRequired'), trigger: 'blur' }
                    ]" :label-position="labelPosition">
                        <el-input v-model="form.name" :placeholder="td('dp.document.standardNamePlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.standardStatus')" prop="status" :rules="[
                        { required: true, message: td('dp.document.standardStatusRequired'), trigger: 'blur' }
                    ]" :label-position="labelPosition">
                        <el-select style="width: 100%;" class="el-form-input-width" v-model="form.status"
                            :placeholder="td('dp.document.standardStatusPlaceholder')">
                            <el-option v-for="dict in dp_document_status" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.standardCategory')" prop="catCode" :rules="[
                        { required: true, message: td('dp.document.standardCategoryRequired'), trigger: 'blur' }
                    ]" :label-position="labelPosition">
                        <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                            :props="{ value: 'code', label: 'name', children: 'children' }" value-key="code"
                            :placeholder="td('dp.document.standardCategoryPlaceholder')" check-strictly />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.file')" prop="fileUrl" :rules="[
                        { required: true, message: td('dp.document.fileRequired'), trigger: 'change' }
                    ]" :label-position="labelPosition">
                        <FileUploadbtn :limit="1" v-model:filename="form.fileName" v-model="form.fileUrl"
                            :dragFlag="false" :fileSize="100" @handleRemove="handleRemove" :isShowTip="false" />
                    </el-form-item>
                </el-col>

            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                        <el-input v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">

                <el-col :span="12">
                    <el-form-item :label="td('dp.document.issuingAgency')" prop="issuingAgency" :label-position="labelPosition">
                        <el-input v-model="form.issuingAgency" :placeholder="td('dp.document.issuingAgencyPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.version')" prop="version" :label-position="labelPosition">
                        <el-input v-model="form.version" :placeholder="td('dp.document.versionPlaceholder')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.releaseDate')" prop="releaseDate" :label-position="labelPosition">
                        <el-date-picker clearable style="width: 100%" v-model="form.releaseDate" type="date"
                            value-format="YYYY-MM-DD" :placeholder="td('dp.document.releaseDatePlaceholder')">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.implementationDate')" prop="implementationDate" :label-position="labelPosition">
                        <el-date-picker clearable style="width: 100%" v-model="form.implementationDate" type="date"
                            value-format="YYYY-MM-DD" :placeholder="td('dp.document.implementationDatePlaceholder')">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dp.document.abolitionDate')" prop="abolitionDate" :label-position="labelPosition">
                        <el-date-picker clearable style="width: 100%" v-model="form.abolitionDate" type="date"
                            value-format="YYYY-MM-DD" :placeholder="td('dp.document.abolitionDatePlaceholder')">
                        </el-date-picker>
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
                <el-button size="mini" @click="close">{{ td('common.button.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="submitForm" :loading="loading">{{ td('common.button.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, reactive, nextTick, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();
import FileUploadbtn from "@/components/FileUploadbtn/index1.vue";
import { addDpDocument, updateDpDocument } from "@/api/dp/document/document";

const { td } = useDefaultLang();
const { column_type, sys_disable, dp_document_status } = proxy.useDict(
    "column_type",
    "sys_disable",
    "dp_document_status"
);

let deptOptions = ref([]);
const visible = ref(false);
const formRef = ref(null);
const loading = ref(false);   // 提交按钮 loading

const form = reactive({
    id: null,
    code: "",
    catCode: "",
    name: "",
    status: "1",
    standardUrl: "",
    issuingAgency: "",
    version: "",
    releaseDate: "",
    implementationDate: "",
    abolitionDate: "",
    fileName: "",
    fileUrl: "",
    remark: ""
});

const type = ref('1');

const title = ref(td('dp.document.standardModalTitle'));
const emit = defineEmits(["update-success"]);

const titleMap = {
    '1': td('dp.document.nationalStandard'),
    '2': td('dp.document.industryStandard'),
    '3': td('dp.document.provincialStandard'),
    '4': td('dp.document.groupStandard'),
};

/** 打开弹窗 */
function openModal(formData = {}, options = [], types) {
    deptOptions.value = options;
    type.value = types

    if (formData && formData.id) {
        Object.assign(form, formData);
        form.catCode = form.catCode != null ? String(form.catCode) : "";
        form.status = form.status != null ? String(form.status) : "";
        title.value = td('common.button.update') + (titleMap[type.value] || td('dp.document.standard'));
    } else {
        clearForm();
        title.value = td('common.button.add') + (titleMap[type.value] || td('dp.document.standard'));
    }

    visible.value = true;
    nextTick(() => formRef.value?.clearValidate());
}

/** 关闭弹窗 */
function close() {
    visible.value = false;
    clearForm();
}

/** 清空表单 */
function clearForm() {
    form.id = null;
    form.code = "";
    form.name = "";
    form.status = "1";
    form.standardUrl = "";
    form.issuingAgency = "";
    form.catCode = "";
    form.version = "";
    form.releaseDate = "";
    form.implementationDate = "";
    form.abolitionDate = "";
    form.fileName = "";
    form.fileUrl = "";
    form.remark = "";
    form.description = "";

    nextTick(() => formRef.value?.clearValidate());
}
/** 提交表单 */
function submitForm() {
    formRef.value.validate((valid) => {
        if (!valid) return;
        loading.value = true;

        const apiCall = form.id ? updateDpDocument : addDpDocument;
        apiCall({ ...form, type: type.value })
            .then(() => {
                proxy.$modal.msgSuccess(form.id ? td('common.message.editSuccess') : td('common.message.addSuccess'));
                visible.value = false;
                clearForm();
                emit("update-success");
            })
            .finally(() => {
                loading.value = false;
            });
    });
}

/** 文件移除 */
function handleRemove(file) {
    form.standardUrl = null;
    form.fileUrl = "";
}

defineExpose({ openModal, close });
</script>
