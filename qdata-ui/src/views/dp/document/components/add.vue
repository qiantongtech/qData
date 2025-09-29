<template>
    <el-dialog :title="title" v-model="visible" class="warn-dialog" :append-to="$refs['app-container']" draggable>
        <el-form ref="formRef" :model="form" label-width="100px" @submit.prevent>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="标准号" prop="code" :rules="[
                        { required: true, message: '标准号不能为空', trigger: 'blur' }
                    ]">
                        <el-input v-model="form.code" placeholder="请输入标准号" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="标准名称" prop="name" :rules="[
                        { required: true, message: '标准名称不能为空', trigger: 'blur' }
                    ]">
                        <el-input v-model="form.name" placeholder="请输入标准名称" />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="标准状态" prop="status" :rules="[
                        { required: true, message: '标准状态不能为空', trigger: 'blur' }
                    ]">
                        <el-select style="width: 100%;" class="el-form-input-width" v-model="form.status"
                            placeholder="请选择标准状态">
                            <el-option v-for="dict in dp_document_status" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="标准类目" prop="catCode" :rules="[
                        { required: true, message: '标准类目不能为空', trigger: 'blur' }
                    ]">
                        <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                            :props="{ value: 'code', label: 'name', children: 'children' }" value-key="code"
                            placeholder="请选择标准类目" check-strictly />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="文件" prop="fileUrl" :rules="[
                        { required: true, message: '文件不能为空', trigger: 'change' }
                    ]">
                        <FileUploadbtn :limit="1" v-model:filename="form.fileName" v-model="form.fileUrl"
                            :dragFlag="false" :fileSize="100" @handleRemove="handleRemove" :isShowTip="false" />
                    </el-form-item>
                </el-col>

            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">

                <el-col :span="12">
                    <el-form-item label="发布机构名称" prop="issuingAgency">
                        <el-input v-model="form.issuingAgency" placeholder="请输入发布机构名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="版本号" prop="version">
                        <el-input v-model="form.version" placeholder="请输入版本号" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="发布日期" prop="releaseDate">
                        <el-date-picker clearable style="width: 100%" v-model="form.releaseDate" type="date"
                            value-format="YYYY-MM-DD" placeholder="请选择发布日期">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="实施日期" prop="implementationDate">
                        <el-date-picker clearable style="width: 100%" v-model="form.implementationDate" type="date"
                            value-format="YYYY-MM-DD" placeholder="请选择实施日期">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="废止日期" prop="abolitionDate">
                        <el-date-picker clearable style="width: 100%" v-model="form.abolitionDate" type="date"
                            value-format="YYYY-MM-DD" placeholder="请选择废止日期">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="close">取消</el-button>
                <el-button type="primary" size="mini" @click="submitForm" :loading="loading">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, nextTick, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();
import FileUploadbtn from "@/components/FileUploadbtn/index1.vue";
import { addDpDocument, updateDpDocument } from "@/api/dp/document/document";
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

const title = ref("标准弹窗");
const emit = defineEmits(["update-success"]);

const titleMap = {
    '1': "国家标准",
    '2': "行业标准",
    '3': "地方标准",
    '4': "团体标准",
};

/** 打开弹窗 */
function openModal(formData = {}, options = [], types) {
    deptOptions.value = options;
    type.value = types

    if (formData && formData.id) {
        Object.assign(form, formData);
        form.catCode = form.catCode != null ? String(form.catCode) : "";
        form.status = form.status != null ? String(form.status) : "";
        title.value = "修改" + (titleMap[type.value] || "标准");
    } else {
        clearForm();
        title.value = "新增" + (titleMap[type.value] || "标准");
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
                proxy.$modal.msgSuccess(form.id ? "修改成功" : "新增成功");
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
