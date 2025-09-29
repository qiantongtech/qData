<template>
    <div class="app-container" ref="app-container">

        <GuideTip tip-id="da/daSensitiveLevel/daSensitiveLevel.list" />

        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                v-show="showSearch" @submit.prevent>
                <el-form-item label="级别名称" prop="sensitiveLevel">
                    <el-input class="el-form-input-width" v-model="queryParams.sensitiveLevel" placeholder="请输入敏感级别名称"
                        clearable @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="替换规则" prop="sensitiveRule">
                    <el-select class="el-form-input-width" v-model="queryParams.sensitiveRule" placeholder="请选择替换规则"
                        clearable>
                        <el-option v-for="dict in da_sensitive_level_rule" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                    </el-button>
                    <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
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
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
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
                <el-table-column v-if="getColumnVisibility(1)" label="编号" align="center" prop="id" width="80">
                    <template #default="scope">
                        {{ scope.row.id || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" label="敏感级别名称" align="center" prop="sensitiveLevel">
                    <template #default="scope">
                        {{ scope.row.sensitiveLevel || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(3)" width="350" label="描述" align="left" prop="description">
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" label="替换规则" align="center" prop="sensitiveRule">
                    <template #default="scope">
                        <dict-tag :options="da_sensitive_level_rule" :value="scope.row.sensitiveRule" />
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(5)" label="替换内容" align="center" prop="maskCharacter">
                    <template #default="scope">
                        {{ scope.row.maskCharacter || '-' }}
                    </template>
                </el-table-column>

                <el-table-column v-if="getColumnVisibility(6)" label="创建人" width="120" align="center" prop="createBy"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(7)" label="创建时间" align="center" prop="createTime" width="160"
                    sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']">
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}')
                            }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(8)" label="在线状态" align="center" prop="onlineFlag"
                    width="100">
                    <template #default="scope">
                        <el-switch v-model="scope.row.onlineFlag" active-color="#13ce66" inactive-color="#ff4949"
                            active-value="1" inactive-value="0" @change="handleStatusChange(scope.row)" />
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(9)" label="备注" align="left" prop="remark"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(10)" label="操作" align="center"
                    class-name="small-padding fixed-width" fixed="right" width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                            v-hasPermi="['da:sensitiveLevel:edit']">修改</el-button>
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                            v-hasPermi="['da:sensitiveLevel:remove']">删除</el-button>
                        <!--           <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"-->
                        <!--                      v-hasPermi="['da:sensitiveLevel:edit']">详情</el-button>-->
                        <!--           <el-button link type="primary" icon="view" @click="routeTo('/da/sensitiveLevel/daSensitiveLevelDetail',scope.row)"-->
                        <!--                      v-hasPermi="['da:sensitiveLevel:edit']">复杂详情</el-button>-->
                    </template>
                </el-table-column>

                <template #empty>
                    <div class="emptyBg">
                        <img src="../../../../assets/system/images/no_data/noData.png" alt="" />
                        <p>暂无记录</p>
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
            <el-form ref="daSensitiveLevelRef" :model="form" :rules="rules" label-width="100px" @submit.prevent>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="级别名称" prop="sensitiveLevel">
                            <el-input v-model="form.sensitiveLevel" placeholder="请输入敏感级别名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="替换规则" prop="sensitiveRule">
                            <el-select v-model="form.sensitiveRule" placeholder="请选择替换规则">
                                <el-option v-for="dict in da_sensitive_level_rule" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20" v-if="form.sensitiveRule != '1' && form.sensitiveRule != null">
                    <el-col :span="12">
                        <el-form-item label="起始字符位置" prop="startCharLoc">
                            <el-input v-model="form.startCharLoc" placeholder="请输入起始字符位置" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="截止字符位置" prop="endCharLoc">
                            <el-input v-model="form.endCharLoc" placeholder="请输入截止字符位置" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="替换内容" prop="maskCharacter">
                            <el-input v-model="form.maskCharacter" placeholder="请输入替换内容" />
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
                        <el-form-item label="在线状态" prop="onlineFlag">
                            <el-radio-group v-model="form.onlineFlag">
                                <el-radio v-for="dict in da_sensitive_status" :key="dict.value" :value="dict.value">{{
                                    dict.label }}</el-radio>
                            </el-radio-group>
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
                    <el-button size="mini" @click="cancel">取 消</el-button>
                    <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
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
            <el-form ref="daSensitiveLevelRef" :model="form" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="敏感级别名称" prop="sensitiveLevel">
                            <div>
                                {{ form.sensitiveLevel }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="替换规则" prop="sensitiveRule">
                            <dict-tag :options="da_sensitive_level_rule" :value="form.sensitiveRule" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="起始字符位置" prop="startCharLoc">
                            <div>
                                {{ form.startCharLoc }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="截止字符位置" prop="endCharLoc">
                            <div>
                                {{ form.endCharLoc }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="替换内容" prop="maskCharacter">
                            <div>
                                {{ form.maskCharacter }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="上下线标识" prop="onlineFlag">
                            <div>
                                {{ form.onlineFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="描述" prop="description">
                            <div>
                                {{ form.description }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="备注" prop="remark">
                            <div>
                                {{ form.remark }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="cancel">关 闭</el-button>
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
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <div class="el-upload__tip">
                            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的敏感等级数据
                        </div>
                        <span>仅允许导入xls、xlsx格式文件。</span>
                        <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
                            @click="importTemplate">下载模板</el-link>
                    </div>
                </template>
            </el-upload>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="upload.open = false">取 消</el-button>
                    <el-button type="primary" @click="submitFileForm">确 定</el-button>
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

const { proxy } = getCurrentInstance();
const { da_sensitive_level_rule, da_sensitive_status } = proxy.useDict(
    'da_sensitive_level_rule',
    'da_sensitive_status'
);
const daSensitiveLevelList = ref([]);

// 列显隐信息
const columns = ref([
    { key: 1, label: '编号', visible: true },
    { key: 2, label: '敏感级别名称', visible: true },
    { key: 3, label: '描述', visible: true },
    { key: 4, label: '替换规则', visible: true },
    { key: 5, label: '替换内容', visible: true },
    { key: 6, label: '创建人', visible: true },
    { key: 7, label: '创建时间', visible: true },
    { key: 8, label: '在线状态', visible: true },
    { key: 9, label: '备注', visible: true },
    { key: 10, label: '操作', visible: true }
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
        sensitiveLevel: [{ required: true, message: '敏感级别名称不能为空', trigger: 'blur' }],
        maskCharacter: [{ required: true, message: '替换内容不能为空', trigger: 'blur' }],
        sensitiveRule: [{ required: true, message: '替换规则不能为空', trigger: 'blur' }]
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
    title.value = '新增敏感等级';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDaSensitiveLevel(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = '修改敏感等级';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDaSensitiveLevel(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = '敏感等级详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['daSensitiveLevelRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateDaSensitiveLevel(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDaSensitiveLevel(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('新增成功');
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
        .confirm('是否确认删除敏感等级编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delDaSensitiveLevel(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess('删除成功');
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
    upload.title = '敏感等级导入';
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
        '导入结果',
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
    const text = row.onlineFlag === '1' ? '上线' : '下线';
    proxy.$modal
        .confirm('确认要' + text + '"' + row.sensitiveLevel + '"敏感等级吗？')
        .then(function () {
            updateStatus(row.id, row.onlineFlag)
                .then((response) => {
                    proxy.$modal.msgSuccess(text + '成功');
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
