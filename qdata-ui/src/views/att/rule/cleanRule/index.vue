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

        <GuideTip tip-id="att/attCleanRule.list" />

        <el-container style="90%">
            <DeptTree :deptOptions="processedData" ref="DeptTreeRef" :leftWidth="leftWidth" :placeholder="td('att.common.ruleCategoryPlaceholder')"
                @node-click="handleNodeClick" />

            <el-main>
                <div class="pagecont-top" v-show="showSearch">
                    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                        v-show="showSearch" @submit.prevent>
                        <el-form-item :label="td('att.common.ruleName')" prop="name" :label-position="labelPosition">
                            <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('att.common.ruleNamePlaceholder')"
                                clearable @keyup.enter="handleQuery" />
                        </el-form-item>
                        <!-- <el-form-item label="number" prop="code">
                            <el-input class="el-form-input-width" v-model="queryParams.code" :placeholder="td('att.common.codePlaceholder')"
                                clearable @keyup.enter="handleQuery" />
                        </el-form-item> -->

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
                            <!--                            <el-col :span="1.5">-->
                            <!--                                <el-button type="primary" plain @click="handleAdd"-->
                            <!--                                    v-hasPermi="['att:rule:attcleanrule:add']" @mousedown="(e) => e.preventDefault()">-->
                            <!--                                    <i class="iconfont-mini icon-xincheng mr5"></i>New-->
                            <!--                                </el-button>-->
                            <!--                            </el-col>-->
                        </el-row>
                        <div class="justify-end top-right-btn">
                            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                                :columns="columns"></right-toolbar>
                        </div>
                    </div>
                    <el-table stripe v-loading="loading" :data="attCleanRuleList"
                        @selection-change="handleSelectionChange" :default-sort="defaultSort"
                        @sort-change="handleSortChange">
                        <!--                        <el-table-column type="selection" width="55" align="center" />-->
                        <el-table-column v-if="getColumnVisibility(0)" :label="td('common.texts.number')" align="left" prop="code" width="80" />
                        <el-table-column v-if="getColumnVisibility(1)" :label="td('att.cleanRule.table.name')" width="200" align="left" prop="name"
                            :show-overflow-tooltip="{ effect: 'light' }">
                            <template #default="scope">
                                {{ scope.row.name || '-' }}
                            </template>
                        </el-table-column>
                        <!--                      <el-table-column label="status" align="left" prop="validFlag" width="80" >-->
                        <!--                        <template #default="scope">-->
                        <!--                          &lt;!&ndash;              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>&ndash;&gt;-->

                        <!--                          <el-switch-->
                        <!--                              v-model="scope.row.validFlag"-->
                        <!--                              active-color="#13ce66"-->
                        <!--                              inactive-color="#ff4949"-->
                        <!--                              @change="handleStatusChange(scope.row)"-->
                        <!--                          >-->
                        <!--                          </el-switch>-->
                        <!--                        </template>-->
                        <!--                      </el-table-column>-->
                        <el-table-column v-if="getColumnVisibility(2)" :label="td('att.cleanRule.table.type')" width="180" align="left"
                            prop="type">
                            <template #default="scope">
                                {{ scope.row.catName || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(4)" :label="td('common.texts.description')" width="480" align="left"
                            prop="description">
                            <template #default="scope">
                                {{ scope.row.description || '-' }}
                            </template>
                        </el-table-column>
                        <!--                        <el-table-column v-if="getColumnVisibility(3)" label="Rule Level" width="120" align="center"-->
                        <!--                            prop="level">-->
                        <!--                            <template #default="scope">-->
                        <!--                                <dict-tag :options="att_rule_level" :value="scope.row.level" />-->
                        <!--                            </template>-->
                        <!--                        </el-table-column>-->


                        <el-table-column v-if="getColumnVisibility(6)" :label="td('att.cleanRule.table.useCase')" width="500" align="left"
                            prop="level">
                            <template #default="scope">
                                {{ scope.row.useCase || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(5)" :label="td('att.cleanRule.table.example')" width="600" align="left" prop="type">
                            <template #default="scope">
                                {{ scope.row.example || '-' }}
                            </template>
                        </el-table-column>
                        <!--                        <el-table-column label="Operation" align="center" class-name="small-padding fixed-width" fixed="right"-->
                        <!--                            width="120">-->
                        <!--                            <template #default="scope">-->
                        <!--                                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
                        <!--                                    v-hasPermi="['att:rule:attcleanrule:edit']">Edit</el-button>-->
                        <!--                                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"-->
                        <!--                                    v-hasPermi="['att:rule:attcleanrule:remove']">Delete</el-button>-->
                        <!--                            </template>-->
                        <!--                        </el-table-column>-->

                        <template #empty>
                            <div class="emptyBg">
                                <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
                                <p>{{td('common.noData')}}</p>
                            </div>
                        </template>
                    </el-table>

                    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                        v-model:limit="queryParams.pageSize" @pagination="getList" />
                </div>
            </el-main>
        </el-container>

        <!-- Add or modify cleaning rule dialog box -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="attCleanRuleRef" :model="form" :rules="rules" label-width="80px" @submit.prevent :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('att.common.ruleName')" prop="name" :label-position="labelPosition">
                            <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('att.common.code')" prop="code" :label-position="labelPosition">
                            <el-input v-model="form.code" :placeholder="td('att.common.codePlaceholder')" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('att.common.ruleType')" prop="type" :label-position="labelPosition">
                            <el-tree-select v-model="form.type" :data="processedData"
                                :props="{ value: 'id', label: 'name', children: 'children' }" value-key="id"
                                :placeholder="td('att.common.ruleTypePlaceholder')" check-strictly />

                        </el-form-item>

                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
                            <el-radio v-model="form.validFlag" :label="true">{{ td('att.common.enable') }}</el-radio>
                            <el-radio v-model="form.validFlag" :label="false">{{ td('att.common.disable') }}</el-radio>
                        </el-form-item>

                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('att.common.useCase')" prop="useCase" :label-position="labelPosition">
                            <el-input type="textarea" v-model="form.useCase" :placeholder="td('att.common.useCasePlaceholder')" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item :label="td('att.common.example')" prop="example" :label-position="labelPosition">
                            <el-input type="textarea" v-model="form.example" :placeholder="td('att.common.examplePlaceholder')" />
                        </el-form-item>
                    </el-col>

                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                            <el-input type="textarea" v-model="form.description" :placeholder="td('common.form.descriptionPlaceholder')" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <!--                <el-row :gutter="20">-->
                <!--                    <el-col :span="24">-->
                <!--                        <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">-->
                <!--                            <el-input type="textarea" v-model="form.remark" :placeholder="td('common.form.remarkPlaceholder')" />-->
                <!--                        </el-form-item>-->
                <!--                    </el-col>-->
                <!--                </el-row>-->
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
                    <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- Cleaning Rule Details Dialog Box -->
        <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="attCleanRuleRef" :model="form" label-width="80px" :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('att.common.ruleName')" prop="name">
                            <div>
                                {{ form.name }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('att.common.ruleType')" prop="type" :label-position="labelPosition">
                            <dict-tag :options="processedData" :value="form.type" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('att.common.ruleLevel')" prop="level" :label-position="labelPosition">
                            <dict-tag :options="att_rule_level" :value="form.level" />
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

<script setup name="CleanRule">
import {
    listAttCleanRule,
    getAttCleanRule,
    delAttCleanRule,
    addAttCleanRule,
    updateAttCleanRule
} from '@/api/att/rule/cleanRule';
import { getToken } from '@/utils/auth.js';
import DeptTree from '@/components/DeptTree';
import { computed } from 'vue';
import { listAttCleanCat } from "@/api/att/cat/cleanCat/cleanCat.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { att_rule_level, att_rule_clean_type } = proxy.useDict(
    'att_rule_level',
    'att_rule_clean_type'
);
const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed // Initial left width
let Materialization = ref(false);
const startResize = (event) => {
    isResizing.value = true;
    startX = event.clientX;
    document.addEventListener('mousemove', updateResize);
    document.addEventListener('mouseup', stopResize);
};
const stopResize = () => {
    isResizing.value = false;
    document.removeEventListener('mousemove', updateResize);
    document.removeEventListener('mouseup', stopResize);
};
const updateResize = (event) => {
    if (isResizing.value) {
        const delta = event.clientX - startX; // Calculate mouse movement distance
        leftWidth.value += delta; // Modify left width
        startX = event.clientX; // Update starting position
        // Use requestAnimationFrame to reduce page redraw frequency
        requestAnimationFrame(() => { });
    }
};
const attCleanRuleList = ref([]);
const processedData = ref([]);
const dataMapCat = new Map();

function handleNodeClick(data) {
    if (data.id == 0) {
        data.id = null;
    }
    queryParams.value.catCode = data.code;
    queryParams.value.pageNum = 1;
    handleQuery();
}
// Show hidden information
const columns = ref([
    { key: 1, label: td('att.cleanRule.texts.name'), visible: true },
    { key: 2, label: td('att.cleanRule.texts.type'), visible: true },
    { key: 3, label: td('att.cleanRule.texts.level'), visible: true },
    { key: 4, label: td('common.texts.description'), visible: true },
    { key: 13, label: td('common.texts.remark'), visible: true }
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
    url: import.meta.env.VITE_APP_BASE_API + '/att/attCleanRule/importData'
});

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        validFlag: true,
        code: null
    },
    rules: {
        name: [{ required: true, message: td('att.common.ruleNameRequired'), trigger: 'blur' }],
        type: [{ required: true, message: td('att.common.ruleTypeRequired'), trigger: 'change' }],
        level: [{ required: true, message: td('att.common.ruleLevelRequired'), trigger: 'change' }],
        code: [{ required: true, message: td('att.common.codeRequired'), trigger: 'change' }],
    }
});

const { queryParams, form, rules } = toRefs(data);

/** Query the list of cleaning rules */
function getList() {
    loading.value = true;
    listAttCleanRule(queryParams.value).then((response) => {
        response.data.rows.forEach(obj => {
            let name = dataMapCat.get(obj.type);
            obj.catName = name;
        });
        attCleanRuleList.value = response.data.rows;
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
        name: null,
        type: null,
        level: 1,
        description: null,
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
    proxy.resetForm('attCleanRuleRef');
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
    queryParams.value.catCode = '';
    queryParams.value.pageNum = 1;
    proxy.resetForm('queryRef');
    handleQuery();
}

/** Change enabled status value */
function handleStatusChange(row) {
    const status = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
    proxy.$modal
        .confirm(td('att.common.confirmStatusChangeGeneric', '', { status, name: row.name, type: td('att.common.dataDoc') }))
        .then(function () {
            updateAttCleanRule({ id: row.id, validFlag: row.validFlag }).then((response) => {
                proxy.$modal.msgSuccess(td('att.common.statusSuccess', '', { status }));
                getList();
            });
        })
        .catch(function () {
            row.validFlag = !row.validFlag;
        });
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
    form.value.type = queryParams.value.type;
    open.value = true;
    title.value = td('att.cleanRule.title.add');
}

/** Modify button actions */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getAttCleanRule(_id).then((response) => {
        //Filter out createTime
        delete response.data.createTime;
        delete response.data.updateTime;
        form.value = response.data;
        open.value = true;
        title.value = td('att.cleanRule.title.edit');
    });
}

/** Detail button operation */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getAttCleanRule(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = td('att.cleanRule.title.detail');
    });
}

/** submit button */
function submitForm() {
    if (submitLoading.value) return;
    submitLoading.value = true;
    proxy.$refs['attCleanRuleRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateAttCleanRule(form.value)
                    .then((response) => {
                        submitLoading.value = false;
                        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
                        open.value = false;
                        getList();
                    })
                    .catch((error) => {
                        submitLoading.value = false;
                    });
            } else {
                addAttCleanRule(form.value)
                    .then((response) => {
                        submitLoading.value = false;
                        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
                        open.value = false;
                        getList();
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

/** Delete button action */
function handleDelete(row) {
    console.log(row, 'row');
    console.log(row.id, 'row');
    const _ids = row.id || ids.value;
    const _name = row.name;
    proxy.$modal
        .confirm(td('att.cleanRule.deleteConfirm', '', { ids: _ids }))
        .then(function () {
            return delAttCleanRule(_ids);
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
        'att/attCleanRule/export',
        {
            ...queryParams.value
        },
        `attCleanRule_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
    upload.title = td('att.cleanRule.importTitle');
    upload.open = true;
}

/** Download template operation */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `attCleanRule_template_${new Date().getTime()}.xlsx`
    );
}

/** Submit upload file */
function submitFileForm() {
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
    proxy.$refs['uploadRef'].handleRemove(file);
    proxy.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
        response.msg +
        '</div>',
        td('att.common.importResult'),
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
function getDeptTree() {
    listAttCleanCat({ validFlag: true }).then((response) => {
        response.data.forEach(obj => {
            dataMapCat.set(obj.id + "", obj.name);
        });
        getList();
        processedData.value = proxy.handleTree(response.data, "id", "parentId");
        processedData.value = [
            {
                name: td('att.cleanRule.cleanRuleCategory'),
                value: "",
                id: 0,
                children: processedData.value,
            },
        ];
        console.log(processedData.value, "safsdfsd")
    });
};
getDeptTree();
</script>
<style scoped lang="scss">
.app-container {
    margin: 13px 15px;
}

.el-main {
    padding: 2px 0px;
    // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}
</style>
