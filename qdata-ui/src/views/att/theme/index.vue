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

        <GuideTip tip-id="att/attTheme.list" />

        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                v-show="showSearch" @submit.prevent>
                <el-form-item :label="td('att.common.themeName')" prop="name" :label-position="labelPosition">
                    <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('common.form.namePlaceholder')" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <!-- <el-form-item label="描述" prop="description">
                    <el-input class="el-form-input-width" v-model="queryParams.description" :placeholder="td('common.form.descriptionPlaceholder')"
                        clearable @keyup.enter="handleQuery" />
                </el-form-item> -->
                <el-form-item>
                    <el-button plain type="primary" v-hasPermi="['att:theme:query']" @click="handleQuery"
                        @mousedown="(e) => e.preventDefault()">
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
                        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:theme:add']"
                            @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                        </el-button>
                    </el-col>
                    <!-- <el-col :span="1.5">
                        <el-button type="primary" plain :disabled="single" @click="handleUpdate"
                            v-hasPermi="['att:theme:theme:edit']" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" plain :disabled="multiple" @click="handleDelete"
                            v-hasPermi="['att:theme:theme:remove']" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
                        </el-button>
                    </el-col> -->
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                        :columns="columns"></right-toolbar>
                </div>
            </div>
            <el-table stripe v-loading="loading" :data="attThemeList" @selection-change="handleSelectionChange"
                :default-sort="defaultSort" @sort-change="handleSortChange">
                <!-- <el-table-column type="selection" width="55" align="center" /> -->
                <el-table-column v-if="getColumnVisibility(0)" :label="td('common.texts.number')" align="center" prop="id" width="60" />
                <!--       <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />-->
                <el-table-column v-if="getColumnVisibility(1)" :label="td('att.theme.table.name')" align="left" prop="name" width="200">
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" :label="td('att.theme.table.icon')" align="center" prop="icon" width="100">
                    <template #default="scope">
                        <image-preview :src="scope.row.icon || noDataImg" :width="50" :height="50" />
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(3)" :label="td('common.texts.description')"
                    align="left" prop="description" width="300">
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(10)" :label="td('common.texts.sortOrder')"
                    align="left" prop="sortOrder" width="50">
                    <template #default="scope">
                        {{ scope.row.sortOrder || '-' }}
                    </template>
                </el-table-column>

                <el-table-column v-if="getColumnVisibility(7)" :label="td('common.texts.createdBy')" :show-overflow-tooltip="{ effect: 'light' }"
                    align="left" prop="createBy">
                    <template #default="scope">
                        {{ scope.row.createBy || "-" }}
                    </template>
                </el-table-column>
                <!-- column-key="create_time" :sort-orders="['descending', 'ascending']"   sortable="custom"-->
                <el-table-column v-if="getColumnVisibility(6)" :label="td('common.texts.createdTime')" align="center" prop="createTime"
                    width="150">
                    <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                    }}</span>
                    </template>
                </el-table-column>
                <el-table-column :label="td('common.texts.status')" align="center" prop="validFlag" width="120" v-if="getColumnVisibility(4)">
                    <template #default="scope">
                        <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

                        <el-switch v-model="scope.row.validFlag" active-color="#13ce66" inactive-color="#ff4949"
                            @change="handleStatusChange(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(5)" :label="td('common.texts.remark')"
                    align="left" prop="remark">
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>

                <el-table-column :label="td('common.texts.handle')" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                            v-hasPermi="['att:theme:edit']">{{ td('common.button.update') }}</el-button>
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                            v-hasPermi="['att:theme:remove']">{{ td('common.button.delete') }}</el-button>
                        <el-button link v-hasPermi="['att:theme:query']" type="primary" icon="view"
                            @click="handleDetail(scope.row)">{{ td('common.button.details') }}</el-button>

                    </template>
                </el-table-column>

                <template #empty>
                    <div class="emptyBg">
                        <img src="../../../assets/images/system/no_data/empty-nodata.png" alt="" />
                        <p>{{td('common.noData')}}</p>
                    </div>
                </template>
            </el-table>

            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>

        <!-- 新增或修改主题对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="attThemeRef" :model="form" :rules="rules" label-width="80px" @submit.prevent :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col>
                        <el-form-item :label="td('att.common.themeName')" prop="name">
                            <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                            <el-input type="textarea" v-model="form.description" :placeholder="td('common.form.descriptionPlaceholder')" />
                        </el-form-item>
                    </el-col>
                </el-row>


                <el-row>
                    <el-col :span="24">
                        <el-form-item :label="td('att.common.icon')" prop="icon" :label-position="labelPosition">
                            <image-upload :limit="1" v-model="form.icon" :width="50" :height="50" />

                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('att.common.sortOrder')" prop="sortOrder" :label-position="labelPosition">
                            <el-input-number style="width: 100%" v-model="form.sortOrder" controls-position="right"
                                :min="0" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
                            <el-radio v-model="form.validFlag" :label="true">{{ td('att.common.enable') }}</el-radio>
                            <el-radio v-model="form.validFlag" :label="false">{{ td('att.common.disable') }}</el-radio>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
                            <el-input type="textarea" v-model="form.remark" :placeholder="td('common.form.remarkPlaceholder')" />
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

        <!-- 主题详情对话框 -->
        <el-dialog :title="title" v-model="openDetail" width="1000px" :append-to="$refs['app-container']" draggable>
            <el-form ref="daAssetApplyRef" :model="form" label-width="90px" :label-position="labelPosition">
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.number')+':'" prop="id" :label-position="labelPosition">
                            <div class="form-readonly">
                                {{ form.id }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('att.common.themeName') + ':'" prop="name" :label-position="labelPosition">
                            <div class="form-readonly">
                                {{ form.name }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item :label="td('att.common.icon')+':'" prop="icon" :label-position="labelPosition">
                            <image-preview :src="form.icon || noDataImg" :width="50" :height="50" />

                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                            <div class="form-readonly textarea">
                                {{ form.description ?? "-" }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.createdBy')+':'" prop="createBy" :label-position="labelPosition">
                            <div class="form-readonly">
                                {{ form.createBy }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.createdTime') + ':'" prop="createTime" :label-position="labelPosition">
                            <div class="form-readonly">
                                {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}

                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.updatedBy')+':'" prop="createBy" :label-position="labelPosition">
                            <div class="form-readonly">
                                {{ form.updateBy }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.updatedTime')+':'" prop="updateTime" :label-position="labelPosition">
                            <div class="form-readonly">
                                {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('common.texts.status') + ':'" prop="validFlag" :label-position="labelPosition">
                            <div class="form-readonly">
                                {{ form.validFlag ? td('att.common.enable') : td('att.common.disable') }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
                            <div class="form-readonly textarea">
                                {{ form.remark ?? "-" }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="openDetail = false">{{ td('common.button.close') }} </el-button>
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

<script setup name="Theme">
import {
    listAttTheme,
    getAttTheme,
    delAttTheme,
    addAttTheme,
    updateAttTheme
} from '@/api/att/theme/theme.js';
import { getToken } from '@/utils/auth.js';
import useDefaultLang from "@/composables/useDefaultLang";
const noDataImg = new URL('@/assets/images/system/D.png', import.meta.url).href

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const attThemeList = ref([]);
// 列显隐信息
const columns = ref([
    { key: 0, label: td('common.texts.number'), visible: true },
    { key: 1, label: td('att.theme.texts.name'), visible: true },
    { key: 2, label: td('att.theme.texts.icon'), visible: true },
    { key: 3, label: td('common.texts.description'), visible: true },
    { key: 10, label: td('att.theme.texts.sortOrder'), visible: true },
    { key: 7, label: td('common.texts.createdBy'), visible: true },
    { key: 6, label: td('common.texts.createdTime'), visible: true },
    { key: 4, label: td('common.texts.status'), visible: true },
    { key: 5, label: td('common.texts.remark'), visible: true }
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
    url: import.meta.env.VITE_APP_BASE_API + '/att/attTheme/importData'
});

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        orderByColumn: 'sortOrder,createTime',
        description: null
    },
    rules: {
        name: [{ required: true, message: td('att.common.themeNameRequired'), trigger: 'blur' }],
        // icon: [{ required: true, message: td('att.common.iconRequired'), trigger: "blur" }],
        // sortOrder: [{ required: true, message: td('att.theme.sortOrderRequired'), trigger: 'blur' }],
        // description: [{ required: true, message: td('common.form.descriptionRequired'), trigger: 'blur' }],
        // validFlag: [{ required: true, message: td('common.form.statusRequired'), trigger: 'blur' }]
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询主题列表 */
function getList() {
    loading.value = true;
    listAttTheme(queryParams.value).then((response) => {
        attThemeList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}
/** 改变启用状态值 */
function handleStatusChange(row) {
    const status = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
    proxy.$modal
        .confirm(td('att.common.confirmStatusChangeGeneric', '', { status: status, type: td('att.theme.themeWord') }).replace('<name>', row.name))
        .then(function () {
            updateAttTheme({ id: row.id, validFlag: row.validFlag }).then((response) => {
                proxy.$modal.msgSuccess(td('att.common.statusSuccess', '', { status: status }));
                getList();
            });
        })
        .catch(function () {
            row.validFlag = !row.validFlag;
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
        icon: null,
        sortOrder: 0,
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
    proxy.resetForm('attThemeRef');
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
function handleAdd() {
    reset();
    open.value = true;
    title.value = td('att.theme.title.add');
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getAttTheme(_id).then((response) => {
        delete response.data.createTime;
        delete response.data.updateTime;
        form.value = response.data;
        open.value = true;
        title.value = td('att.theme.title.edit');
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getAttTheme(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = td('att.theme.title.detail');
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['attThemeRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateAttTheme(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addAttTheme(form.value)
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

/** 删除按钮操作 */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal
        .confirm(td('att.theme.deleteConfirm').replace('<ids>', _ids))
        .then(function () {
            return delAttTheme(_ids);
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
        'att/attTheme/export',
        {
            ...queryParams.value
        },
        `attTheme_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
    upload.title = td('att.theme.importTitle');
    upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `attTheme_template_${new Date().getTime()}.xlsx`
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

getList();
</script>
