<template>
    <div class="app-container" ref="app-container">

        <GuideTip tip-id="att/attAuditRule.list" />

        <el-container style="90%">
            <DeptTree :deptOptions="processedData" :leftWidth="leftWidth" :placeholder="'请输入稽查规则类型'"
                @node-click="handleNodeClick" ref="DeptTreeRef" :default-expand="true" :type="1" />

            <el-main>
                <div class="pagecont-top" v-show="showSearch">
                    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                        v-show="showSearch" @submit.prevent>
                        <el-form-item label="规则名称" prop="name">
                            <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入规则名称"
                                clearable @keyup.enter="handleQuery" />
                        </el-form-item>
                        <el-form-item label="编号" prop="code">
                            <el-input class="el-form-input-width" v-model="queryParams.code" placeholder="请输入编号"
                                clearable @keyup.enter="handleQuery" />
                        </el-form-item>
                        <!-- <el-form-item label="规则级别" prop="level">
                            <el-select class="el-form-input-width" v-model="queryParams.level" placeholder="请选择规则类型"
                                clearable>
                                <el-option v-for="dict in att_rule_level" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="质量维度" prop="qualityDim">
                            <el-select class="el-form-input-width" v-model="queryParams.qualityDim"
                                placeholder="请选择质量维度" clearable>
                                <el-option v-for="dict in att_rule_audit_q_dimension" :key="dict.value"
                                    :label="dict.label" :value="dict.value" />
                            </el-select>
                        </el-form-item> -->

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
                                <!-- <el-button type="primary" plain @click="handleAdd"
                                    v-hasPermi="['att:rule:auditrule:add']" @mousedown="(e) => e.preventDefault()">
                                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                                </el-button> -->
                            </el-col>
                            <!--                            <el-col :span="1.5">-->
                            <!--                                <el-button type="primary" plain :disabled="single" @click="handleUpdate"-->
                            <!--                                    v-hasPermi="['att:rule:auditrule:edit']" @mousedown="(e) => e.preventDefault()">-->
                            <!--                                    <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
                            <!--                                </el-button>-->
                            <!--                            </el-col>-->
                            <!--                            <el-col :span="1.5">-->
                            <!--                                <el-button type="danger" plain :disabled="multiple" @click="handleDelete"-->
                            <!--                                    v-hasPermi="['att:rule:auditrule:remove']" @mousedown="(e) => e.preventDefault()">-->
                            <!--                                    <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
                            <!--                                </el-button>-->
                            <!--                            </el-col>-->
                        </el-row>
                        <div class="justify-end top-right-btn">
                            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                                :columns="columns"></right-toolbar>
                        </div>
                    </div>
                    <el-table stripe v-loading="loading" :data="attAuditRuleList"
                        @selection-change="handleSelectionChange" :default-sort="defaultSort"
                        @sort-change="handleSortChange">
                        <el-table-column v-if="getColumnVisibility(6)" label="编号" align="left" prop="code" width="80">
                            <template #default="scope">
                                {{ scope.row.code || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(1)" label="规则名称" align="left" prop="name"
                            :show-overflow-tooltip="{ effect: 'light' }" width="200">
                            <template #default="scope">
                                {{ scope.row.name || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(2)" label="质量维度" align="left" prop="qualityDim"
                            :show-overflow-tooltip="{ effect: 'light' }" width="80">
                            <template #default="scope">
                                <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.qualityDim" />
                            </template>
                        </el-table-column>
                        <el-table-column :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(5)"
                            label="描述" width="400" align="left" prop="description">
                            <template #default="scope">
                                {{ scope.row.description || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(3)" label="使用场景" width="500" align="left"
                            prop="level" :show-overflow-tooltip="{ effect: 'light' }">
                            <template #default="scope">
                                {{ scope.row.useCase || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(4)" label="示例" width="700" align="left" prop="type"
                            :show-overflow-tooltip="{ effect: 'light' }">
                            <template #default="scope">
                                {{ scope.row.example || '-' }}
                            </template>
                        </el-table-column>



                        <!-- <el-table-column
                            :show-overflow-tooltip="{effect: 'light'}"
                            v-if="getColumnVisibility(14)"
                            label="备注"
                            align="left"
                            prop="remark"
                        >
                            <template #default="scope">
                                {{ scope.row.remark || '-' }}
                            </template>
                        </el-table-column> -->
                        <!-- <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right"
                            width="120">
                            <template #default="scope">
                                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                                    v-hasPermi="['att:rule:auditrule:edit']">修改</el-button>
                                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                                    v-hasPermi="['att:rule:auditrule:remove']">删除</el-button>
                            </template>
                        </el-table-column> -->

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
            </el-main>
        </el-container>
        <!-- 新增或修改稽查规则对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="attAuditRuleRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="规则名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入规则名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="编号" prop="code">
                            <el-input v-model="form.code" placeholder="请输入编号" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="质量维度" prop="qualityDim">
                            <el-select v-model="form.qualityDim" placeholder="请选择质量维度">
                                <el-option v-for="dict in att_rule_audit_q_dimension" :key="dict.value"
                                    :label="dict.label" :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="规则类型" prop="type">
                            <el-select v-model="form.type" placeholder="请选择规则类型">
                                <el-option v-for="dict in att_rule_audit_type" :key="dict.value" :label="dict.label"
                                    :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="规则级别" prop="level">
                            <el-select v-model="form.level" placeholder="请选择规则级别">
                                <el-option v-for="dict in att_rule_level" :key="dict.value" :label="dict.label"
                                    :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row> -->
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="场景" prop="useCase">
                            <el-input type="textarea" v-model="form.useCase" placeholder="请输入场景" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="示例" prop="example">
                            <el-input type="textarea" v-model="form.example" placeholder="请输入示例" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="描述" prop="description">
                            <el-input type="textarea" v-model="form.description" placeholder="请输入规则描述" />
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



    </div>
</template>

<script setup name="AuditRule">
import {
    listAttAuditRule,
    getAttAuditRule,
    delAttAuditRule,
    addAttAuditRule,
    updateAttAuditRule
} from '@/api/att/rule/auditRule';
import { getToken } from '@/utils/auth.js';
import { computed } from 'vue';
import DeptTree from '@/components/DeptTree';
const { proxy } = getCurrentInstance();
const { att_rule_audit_type, att_rule_level, att_rule_audit_q_dimension } = proxy.useDict(
    'att_rule_audit_type',
    'att_rule_level',
    'att_rule_audit_q_dimension'
);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
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
        const delta = event.clientX - startX; // 计算鼠标移动距离
        leftWidth.value += delta; // 修改左侧宽度
        startX = event.clientX; // 更新起始位置
        // 使用 requestAnimationFrame 来减少页面重绘频率
        requestAnimationFrame(() => { });
    }
};
const processedData = ref([]);
processedData.value = computed(() => {
    return [
        {
            name: '质量维度',
            id: '',
            children: Array.isArray(att_rule_audit_q_dimension.value)
                ? att_rule_audit_q_dimension.value.map((item) => ({
                    name: item.label,
                    id: item.value,
                    validFlag: true,
                }))
                : []
        }
    ];
});
const attAuditRuleList = ref([]);
function handleNodeClick(data) {
    queryParams.value.qualityDim = data.id;
    queryParams.value.pageNum = 1;
    handleQuery();
}
// 列显隐信息
const columns = ref([
    { key: 6, label: '编号', visible: true },
    { key: 1, label: '规则名称', visible: true },
    { key: 2, label: '质量维度', visible: true },
    { key: 5, label: '描述', visible: true },
    { key: 3, label: '使用场景', visible: true },
    { key: 4, label: '示例', visible: true },
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
    url: import.meta.env.VITE_APP_BASE_API + '/att/auditRule/importData'
});

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        qualityDim: '',
        type: '',
        code: ""
    },
    rules: {
        name: [{ required: true, message: '规则名称不能为空', trigger: 'blur' }],
        qualityDim: [{ required: true, message: '质量维度不能为空', trigger: 'change' }],
        // type: [{ required: true, message: '规则类型不能为空', trigger: 'change' }],
        code: [{ required: true, message: '编号不能为空', trigger: 'change' }],
        // level: [{ required: true, message: '规则级别不能为空', trigger: 'change' }]
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询稽查规则列表 */
function getList() {
    loading.value = true;
    listAttAuditRule({ ...queryParams.value, validFlag: 1 }).then((response) => {
        attAuditRuleList.value = response.data.rows;
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
        name: null,
        qualityDim: null,
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
        type: '5', level: '1'
    };
    proxy.resetForm('attAuditRuleRef');
}

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
const DeptTreeRef = ref(null);
/** 重置按钮操作 */
function resetQuery() {
    if (DeptTreeRef.value?.resetTree) {
        DeptTreeRef.value.resetTree();
    }
    queryParams.value.qualityDim = '';
    queryParams.value.pageNum = 1;
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
    form.value.qualityDim = queryParams.value.qualityDim;
    form.value.type = '5';
    form.value.level = '1';
    open.value = true;
    title.value = '新增稽查规则';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getAttAuditRule(_id).then((response) => {
        //把createTime过滤掉
        delete response.data.createTime;
        delete response.data.updateTime;
        form.value = response.data;
        open.value = true;
        title.value = '修改稽查规则';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getAttAuditRule(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = '稽查规则详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['attAuditRuleRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateAttAuditRule(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addAttAuditRule(form.value)
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
        .confirm('是否确认删除稽查规则编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delAttAuditRule(_ids);
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
        'att/auditRule/export',
        {
            ...queryParams.value
        },
        `attAuditRule_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
    upload.title = '稽查规则导入';
    upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `attAuditRule_template_${new Date().getTime()}.xlsx`
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

getList();
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
