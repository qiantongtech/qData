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
                    <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
    </div>

    <el-table stripe height="400" v-loading="loading" :data="dpDataElemList" @selection-change="handleSelectionChange"
        :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column v-if="getColumnVisibility(0)" :label="td('common.texts.number')" align="left" prop="id" width="50" />
        <el-table-column v-if="getColumnVisibility(1)" :label="td('dp.dataElem.nameZh')" :show-overflow-tooltip="{ effect: 'light' }"
            align="left" prop="name" width="200">
            <template #default="scope">
                {{ scope.row.name || "-" }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" :label="td('dp.dataElem.nameEn')" :show-overflow-tooltip="{ effect: 'light' }"
            align="left" prop="engName" width="200">
            <template #default="scope">
                {{ scope.row.engName || "-" }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" width="240" :label="td('common.texts.description')" align="left" prop="description"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.description || "-" }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" width="100" :label="td('dp.dataElem.type')" align="left" prop="type">
            <template #default="scope">
                <dict-tag :options="dp_data_elem_code_type" :value="scope.row.type" />
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" :label="td('dp.dataElem.catCode')" width="120"
            :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="catCode">
            <template #default="scope">
                {{ scope.row.catName || "-" }}
            </template>
        </el-table-column>

        <el-table-column v-if="getColumnVisibility(10)" :label="td('common.texts.createdBy')" :show-overflow-tooltip="{ effect: 'light' }"
            align="left" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || "-" }}
            </template>
        </el-table-column>
        <!--  sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
        <el-table-column v-if="getColumnVisibility(11)" :label="td('common.texts.createdTime')" align="left" prop="createTime" width="150">
            <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                    }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" width="80" :label="td('common.texts.status')" align="left" prop="status">
            <template #default="scope">
                <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
                    inactive-value="0" @change="
                        (e) => handleStatusChange(scope.row.id, scope.row, e)
                    " />
            </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="getColumnVisibility(15)">
            <template #default="scope">
                {{ scope.row.remark || "-" }}
            </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="200">
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                    v-hasPermi="['dp:dataElem:dataelem:edit']">{{ td('dp.common.edit') }}
                </el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                    v-hasPermi="['dp:dataElem:dataelem:remove']">{{ td('dp.common.delete') }}
                </el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                    v-hasPermi="['dp:dataElem:dataelem:edit']">{{ td('dp.common.details') }}
                </el-button>
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


    <!-- Add or modify data element dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="dpDataElemRef" :model="form" :rules="rules" label-width="100px" @submit.prevent :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.dataElem.nameZh')" prop="name" :label-position="labelPosition">
                        <el-input v-model="form.name" :placeholder="td('dp.dataElem.nameZhPlaceholder')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dp.dataElem.nameEn')" prop="engName" :label-position="labelPosition">
                        <el-input v-model="form.engName" :placeholder="td('dp.dataElem.nameEnPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dp.dataElem.catCode')" prop="catCode" :label-position="labelPosition">
                        <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                            :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id"
                            :placeholder="td('dp.dataElem.catCodePlaceholder')" check-strictly />
                    </el-form-item>
                </el-col>

                <el-col :span="12">
                    <el-form-item :label="td('dp.dataElem.columnType')" prop="columnType" :label-position="labelPosition">
                        <el-select v-model="form.columnType" :placeholder="td('dp.dataElem.columnTypePlaceholder')">
                            <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
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
                    <el-form-item :label="td('dp.dataElem.personCharge')" prop="personCharge" :label-position="labelPosition">
                        <!--                <el-input v-model="form.managerId" placeholder="Please select the person in charge" />-->
                        <el-select v-model="form.personCharge" @change="handleChange" filterable :placeholder="td('dp.dataElem.personChargePlaceholder')">
                            <el-option v-for="item in managerOptions" :key="item.userId" :label="item.nickName"
                                :value="item.userId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dp.dataElem.contactNumber')" prop="contactNumber" :label-position="labelPosition">
                        <el-input disabled v-model="form.contactNumber" :placeholder="td('dp.dataElem.contactNumberPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <!-- <el-col :span="12">
                    <el-form-item label="type" prop="type" :label-position="labelPosition">
                        <el-radio-group v-model="form.type" disabled>
                            <el-radio v-for="dict in dp_data_elem_code_type" :key="dict.value" :label="dict.value">{{
                                dict.label }}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col> -->
                <el-col :span="12">
                    <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
                        <el-radio-group v-model="form.status">
                            <el-radio v-for="dict in sys_disable" :key="dict.value" :label="dict.value">{{
                                dict.label }}
                            </el-radio>
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
                <el-button size="mini" @click="cancel">{{ td('dp.common.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="submitForm">{{ td('dp.common.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
        destroy-on-close>
        <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
            :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
            :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
            <el-icon class="el-icon--upload">
                <upload-filled />
            </el-icon>
            <div class="el-upload__text">{{ td('common.upload.dragOrClick') }}</div>
            <template #tip>
                <div class="el-upload__tip text-center">
                    <div class="el-upload__tip">
                        <el-checkbox v-model="upload.updateSupport" />{{ td('common.upload.updateExistingData') }}
                    </div>
                    <span>{{ td('common.upload.fileFormat') }}</span>
                    <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
                        @click="importTemplate">{{ td('common.upload.downloadTemplate') }}
                    </el-link>
                </div>
            </template>
        </el-upload>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="upload.open = false">{{ td('dp.common.cancel') }}</el-button>
                <el-button type="primary" @click="submitFileForm">{{ td('dp.common.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>

</template>

<script setup name="DpDataElem">
import DeptTree from "@/components/DeptTree";
import {
    listDpDataElem,
    getDpDataElem,
    delDpDataElem,
    addDpDataElem,
    updateDpDataElem,
    updateStatusDpDataElem,
} from "@/api/dp/dataElem/dataElem";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataElemCat } from "@/api/att/cat/dataElemCat/dataElemCat";
import { getToken } from "@/utils/auth.js";
import { useRoute } from 'vue-router';
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const route = useRoute();
const props = defineProps({
    activeName: { type: Number, default: null },
});
const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
    "column_type",
    "sys_disable",
    "dp_data_elem_code_type"
);
const deptOptions = ref(undefined);
const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed // Initial left width

const dpDataElemList = ref([]);
const dpDataElemRuleRelList = ref([]);

// Show hidden information
const columns = ref([
    { key: 0, label: td('common.texts.number'), visible: true },
    { key: 1, label: td('dp.dataElem.nameZh'), visible: true },
    { key: 2, label: td('dp.dataElem.nameEn'), visible: true },
    { key: 7, label: td('common.texts.description'), visible: true },
    { key: 3, label: td('dp.dataElem.type'), visible: true },
    { key: 4, label: td('dp.dataElem.catCode'), visible: true },
    { key: 10, label: td('common.texts.createdBy'), visible: true },
    { key: 11, label: td('common.texts.createdTime'), visible: true },
    { key: 5, label: td('common.texts.status'), visible: true },
    { key: 6, label: td('common.texts.description'), visible: true },
]);

const getColumnVisibility = (key) => {
    const column = columns.value.find((col) => col.key === key);
    // If the corresponding column configuration is not found, it will be displayed by default.
    if (!column) return true;
    // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
    return column.visible;
};

const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedDpDataElemRuleRel = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

/*** User import parameters */
const upload = reactive({
    // Whether to display the pop-up layer (user import)
    open: false,
    // Popup layer title (user imported)
    title: "",
    // Whether to disable uploading
    isUploading: false,
    // Whether to update existing user data
    updateSupport: 0,
    // Set upload request headers
    headers: { Authorization: "Bearer " + getToken() },
    // Upload address
    url: import.meta.env.VITE_APP_BASE_API + "/dp/dataElem/importData",
});

const data = reactive({
    form: { status: "0" },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        engName: null,
        catCode: null,
        type: null,
        documentId: null,
    },
    rules: {
        name: [{ required: true, message: td('dp.dataElem.nameZhRequired'), trigger: "blur" }],
        engName: [
            { required: true, message: td('dp.dataElem.nameEnRequired'), trigger: "blur" },
            {
                pattern: /^[a-zA-Z_]+$/,
                message: td('dp.dataElem.nameEnPattern'),
                trigger: "blur",
            },
        ],
        catCode: [{ required: true, message: td('dp.dataElem.catCodeRequired'), trigger: "blur" }],
        columnType: [
            { required: true, message: td('dp.dataElem.columnTypeRequired'), trigger: "change" },
        ],
    },
});

const { queryParams, form, rules } = toRefs(data);
const managerOptions = ref([]);
/** Query data element list */
function getList() {
    loading.value = true;
    if (!queryParams.value.documentId) {
        queryParams.value.documentId = route.query.id;
    }
    queryParams.value.type = Number(props.activeName) - 1;
    listDpDataElem(queryParams.value).then((response) => {
        dpDataElemList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
    deptUserTree().then((response) => {
        managerOptions.value = response.data;
    });
}
function handleChange(value) {
    const selectedManager = managerOptions.value.find(
        (item) => item.userId === form.value.personCharge
    );
    form.value.contactNumber = selectedManager.phonenumber; // Store the complete object into form
}
// Cancel button
function cancel() {
    open.value = false;
    reset();
}
function handleNodeClick(data) {
    queryParams.value.catCode = data.code;
    handleQuery();
}
const startResize = (event) => {
    isResizing.value = true;
    startX = event.clientX;
    document.addEventListener("mousemove", updateResize);
    document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
    isResizing.value = false;
    document.removeEventListener("mousemove", updateResize);
    document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
    if (isResizing.value) {
        const delta = event.clientX - startX; // Calculate mouse movement distance
        leftWidth.value += delta; // Modify left width
        startX = event.clientX; // Update starting position
        // Use requestAnimationFrame to reduce page redraw frequency
        requestAnimationFrame(() => { });
    }
}; /** Query department drop-down tree structure */
// form reset
function reset() {
    form.value = {
        id: null,
        code: null,
        name: null,
        engName: null,
        catCode: null,
        type: "1",
        personCharge: null,
        contactNumber: null,
        columnType: null,
        status: "0",
        description: null,
        validFlag: null,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        remark: null,
        documentId: null,
    };
    dpDataElemRuleRelList.value = [];
    proxy.resetForm("dpDataElemRef");
}

/** Search button action */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
const DeptTreeRef = ref(null);
/** reset button action */
function resetQuery() {
    if (DeptTreeRef.value?.resetTree) {
        DeptTreeRef.value.resetTree();
    }
    queryParams.value.catCode = "";
    queryParams.value.pageNum = 1;
    reset();
    proxy.resetForm("queryRef");
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
function getDeptTree() {
    listAttDataElemCat().then((response) => {
        deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
        deptOptions.value = [
            {
                name: td('dp.dataElem.treeRootName'),
                value: "",
                id: 0,
                children: deptOptions.value,
            },
        ];
    });
}
/** Add button operation */
function handleAdd() {
    reset();
    form.value.type = String(Number(props.activeName) - 1)
    console.log("🚀 ~ handleAdd ~ form.value.type:", form.value.type)
    if (queryParams.value.catCode) {
        form.value.catCode = queryParams.value.catCode;
    }
    open.value = true;
    title.value = td('dp.dataElem.addTitle');
}

/** Modify button actions */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDpDataElem(_id).then((response) => {
        form.value = response.data;
        dpDataElemRuleRelList.value = response.data.dpDataElemRuleRelList;
        form.value.personCharge = response.data.personCharge && String(response.data.personCharge).trim() !== ''
            ? Number(response.data.personCharge)
            : null;
        open.value = true;
        title.value = td('dp.dataElem.editTitle');
    });
}

/** Detail button operation */
function handleDetail(row) {
    if (row.type == 1) {
        routeTo("/dp/dataElem/dataElemDetail", row);
    } else {
        routeTo("/dp/dataElem/dataElemCodeDetail", row);
    }
}

/** submit button */
function submitForm() {
    proxy.$refs["dpDataElemRef"].validate((valid) => {
        if (valid) {
            form.value.dpDataElemRuleRelList = dpDataElemRuleRelList.value;
            if (form.value.id != null) {
                updateDpDataElem(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                form.value.documentId = queryParams.value.documentId
                addDpDataElem(form.value)
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

/** Delete button action */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal
        .confirm(td('dp.dataElem.confirmDelete', '', { id: _ids }))
        .then(function () {
            return delDpDataElem(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
        })
        .catch(() => { });
}

/** Data metadata rule association information number */
function rowDpDataElemRuleRelIndex({ row, rowIndex }) {
    row.index = rowIndex + 1;
}

/** New button operation for data metadata rule association information */
function handleAddDpDataElemRuleRel() {
    let obj = {};
    obj.ruleType = "";
    obj.ruleId = "";
    obj.ruleConfig = "";
    obj.remark = "";
    dpDataElemRuleRelList.value.push(obj);
}

/** Data metadata rule association information delete button operation */
function handleDeleteDpDataElemRuleRel() {
    if (checkedDpDataElemRuleRel.value.length == 0) {
        proxy.$modal.msgWarning(td('dp.dataElem.selectToDeleteWarning'));
    } else {
        const dpDataElemRuleRels = dpDataElemRuleRelList.value;
        const checkedDpDataElemRuleRels = checkedDpDataElemRuleRel.value;
        dpDataElemRuleRelList.value = dpDataElemRuleRels.filter(function (item) {
            return checkedDpDataElemRuleRels.indexOf(item.index) == -1;
        });
    }
}

/** Checkbox selected data */
function handleDpDataElemRuleRelSelectionChange(selection) {
    checkedDpDataElemRuleRel.value = selection.map((item) => item.index);
}

/** Export button action */
function handleExport() {
    proxy.download(
        "dp/dpDataElem/export",
        {
            ...queryParams.value,
        },
        `dpDataElem_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
    upload.title = td('dp.dataElem.importTitle');
    upload.open = true;
}

/** Download template operation */
function importTemplate() {
    proxy.download(
        "system/user/importTemplate",
        {},
        `dpDataElem_template_${new Date().getTime()}.xlsx`
    );
}

/** Submit upload file */
function submitFileForm() {
    proxy.$refs["uploadRef"].submit();
}

/**File upload is being processed */
const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
};

/** File upload successfully processed */
const handleFileSuccess = (response, file, fileList) => {
    upload.open = false;
    upload.isUploading = false;
    proxy.$refs["uploadRef"].handleRemove(file);
    proxy.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
        response.msg +
        "</div>",
        td('dp.dataElem.importResult'),
        { dangerouslyUseHTMLString: true }
    );
    getList();
};

/** Enable disable switch */
function handleStatusChange(id, row, e) {
    const text = e === "1" ? td('dp.dataElem.enableText') : td('dp.dataElem.disableText');
    proxy.$modal
        .confirm(td('dp.dataElem.confirmStatusChange', '', { text, name: row.name }))
        .then(function () {
            updateStatusDpDataElem(id, row.status).then((response) => {
                proxy.$modal.msgSuccess(td('common.message.operationSuccess'));
            });
        })
        .catch(function () {
            row.status = row.status === "1" ? "0" : "1";
        });
}
/** ---------------------------------**/

function routeTo(link, row) {
    if (link !== "" && link.indexOf("http") !== -1) {
        window.location.href = link;
        return;
    }
    if (link !== "") {
        if (link === router.currentRoute.value.path) {
            window.location.reload();
        } else {
            router.push({
                path: link,
                query: {
                    id: row.id,
                },
            });
        }
    }
}
queryParams.value.documentId = route.query.id;
queryParams.value.type = Number(props?.activeName) - 1;

getDeptTree();
getList();
</script>
<style scoped lang="scss">
::v-deep {
    .selectlist .el-tag.el-tag--info {
        background: #f3f8ff !important;
        border: 0px solid #6ba7ff !important;
        color: #2666fb !important;
    }
}

.app-container {
    margin: 13px 15px;
}

.el-main {
    padding: 2px 0px;
    // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

//Upload attachment style adjustment
::v-deep {

    // .el-upload-list{
    //    display: flex;
    // }
    .el-upload-list__item {
        width: 100%;
        height: 25px;
    }
}
</style>
