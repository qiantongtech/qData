<template>
    <div class="app-container" ref="app-container">

        <GuideTip tip-id="att/attCleanRule.list" />

        <el-container style="90%">
            <DeptTree :deptOptions="processedData" ref="DeptTreeRef" :leftWidth="leftWidth" :placeholder="'请输入清洗规则类型'"
                @node-click="handleNodeClick" />

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
                            <!--                            <el-col :span="1.5">-->
                            <!--                                <el-button type="primary" plain @click="handleAdd"-->
                            <!--                                    v-hasPermi="['att:rule:attcleanrule:add']" @mousedown="(e) => e.preventDefault()">-->
                            <!--                                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增-->
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
                        <el-table-column v-if="getColumnVisibility(0)" label="编号" align="left" prop="code" width="80" />
                        <el-table-column v-if="getColumnVisibility(1)" label="规则名称" width="200" align="left" prop="name"
                            :show-overflow-tooltip="{ effect: 'light' }">
                            <template #default="scope">
                                {{ scope.row.name || '-' }}
                            </template>
                        </el-table-column>
                        <!--                      <el-table-column label="状态" align="left" prop="validFlag" width="80" >-->
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
                        <el-table-column v-if="getColumnVisibility(2)" label="规则类型" width="180" align="left"
                            prop="type">
                            <template #default="scope">
                                {{ scope.row.catName || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(4)" label="描述" width="480" align="left"
                            prop="description">
                            <template #default="scope">
                                {{ scope.row.description || '-' }}
                            </template>
                        </el-table-column>
                        <!--                        <el-table-column v-if="getColumnVisibility(3)" label="规则级别" width="120" align="center"-->
                        <!--                            prop="level">-->
                        <!--                            <template #default="scope">-->
                        <!--                                <dict-tag :options="att_rule_level" :value="scope.row.level" />-->
                        <!--                            </template>-->
                        <!--                        </el-table-column>-->


                        <el-table-column v-if="getColumnVisibility(6)" label="使用场景" width="500" align="left"
                            prop="level">
                            <template #default="scope">
                                {{ scope.row.useCase || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(5)" label="示例" width="600" align="left" prop="type">
                            <template #default="scope">
                                {{ scope.row.example || '-' }}
                            </template>
                        </el-table-column>
                        <!--                        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"-->
                        <!--                            width="120">-->
                        <!--                            <template #default="scope">-->
                        <!--                                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
                        <!--                                    v-hasPermi="['att:rule:attcleanrule:edit']">修改</el-button>-->
                        <!--                                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"-->
                        <!--                                    v-hasPermi="['att:rule:attcleanrule:remove']">删除</el-button>-->
                        <!--                            </template>-->
                        <!--                        </el-table-column>-->

                        <template #empty>
                            <div class="emptyBg">
                                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                                <p>暂无记录</p>
                            </div>
                        </template>
                    </el-table>

                    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                        v-model:limit="queryParams.pageSize" @pagination="getList" />
                </div>
            </el-main>
        </el-container>

        <!-- 新增或修改清洗规则对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="attCleanRuleRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
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
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="规则类型" prop="type">
                            <el-tree-select v-model="form.type" :data="processedData"
                                :props="{ value: 'id', label: 'name', children: 'children' }" value-key="id"
                                placeholder="请选择规则类型" check-strictly />

                        </el-form-item>

                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态" prop="validFlag">
                            <el-radio v-model="form.validFlag" :label="true">启用</el-radio>
                            <el-radio v-model="form.validFlag" :label="false">禁用</el-radio>
                        </el-form-item>

                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="场景" prop="useCase">
                            <el-input type="textarea" v-model="form.useCase" placeholder="请输入场景" />
                        </el-form-item>
                    </el-col>
                    <!--                    <el-col :span="12">-->
                    <!--                        <el-form-item label="规则级别" prop="level">-->
                    <!--                            <el-select v-model="form.level" placeholder="请选择规则级别">-->
                    <!--                                <el-option v-for="dict in att_rule_level" :key="dict.value" :label="dict.label"-->
                    <!--                                    :value="dict.value"></el-option>-->
                    <!--                            </el-select>-->
                    <!--                        </el-form-item>-->
                    <!--                    </el-col>-->
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
                <!--                <el-row :gutter="20">-->
                <!--                    <el-col :span="24">-->
                <!--                        <el-form-item label="备注" prop="remark">-->
                <!--                            <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />-->
                <!--                        </el-form-item>-->
                <!--                    </el-col>-->
                <!--                </el-row>-->
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="cancel">取 消</el-button>
                    <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 清洗规则详情对话框 -->
        <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="attCleanRuleRef" :model="form" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="规则名称" prop="name">
                            <div>
                                {{ form.name }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="规则类型" prop="type">
                            <dict-tag :options="processedData" :value="form.type" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="规则级别" prop="level">
                            <dict-tag :options="att_rule_level" :value="form.level" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="描述" prop="description">
                            <div>
                                {{ form.description }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
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
                            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的清洗规则数据
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
const { proxy } = getCurrentInstance();
const { att_rule_level, att_rule_clean_type } = proxy.useDict(
    'att_rule_level',
    'att_rule_clean_type'
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
// 列显隐信息
const columns = ref([
    { key: 1, label: '规则名称', visible: true },
    { key: 2, label: '规则类型', visible: true },
    { key: 3, label: '规则级别', visible: true },
    { key: 4, label: '描述', visible: true },
    { key: 13, label: '备注', visible: true }
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
        name: [{ required: true, message: '规则名称不能为空', trigger: 'blur' }],
        type: [{ required: true, message: '规则类型不能为空', trigger: 'change' }],
        level: [{ required: true, message: '规则级别不能为空', trigger: 'change' }],
        code: [{ required: true, message: '编号不能为空', trigger: 'change' }],
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询清洗规则列表 */
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
    queryParams.value.catCode = '';
    queryParams.value.pageNum = 1;
    proxy.resetForm('queryRef');
    handleQuery();
}

/** 改变启用状态值 */
function handleStatusChange(row) {
    const text = row.validFlag === true ? '启用' : '禁用';
    proxy.$modal
        .confirm('确认要"' + text + '","' + row.name + '"数据文档吗？')
        .then(function () {
            updateAttCleanRule({ id: row.id, validFlag: row.validFlag }).then((response) => {
                proxy.$modal.msgSuccess(text + '成功');
                getList();
            });
        })
        .catch(function () {
            row.validFlag = !row.validFlag;
        });
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
    form.value.type = queryParams.value.type;
    open.value = true;
    title.value = '新增清洗规则';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getAttCleanRule(_id).then((response) => {
        //把createTime过滤掉
        delete response.data.createTime;
        delete response.data.updateTime;
        form.value = response.data;
        open.value = true;
        title.value = '修改清洗规则';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getAttCleanRule(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = '清洗规则详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['attCleanRuleRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateAttCleanRule(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addAttCleanRule(form.value)
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
    console.log(row, 'row');
    console.log(row.id, 'row');
    const _ids = row.id || ids.value;
    const _name = row.name;
    proxy.$modal
        .confirm('是否确认删除编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delAttCleanRule(_ids);
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
        'att/attCleanRule/export',
        {
            ...queryParams.value
        },
        `attCleanRule_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
    upload.title = '清洗规则导入';
    upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `attCleanRule_template_${new Date().getTime()}.xlsx`
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
function getDeptTree() {
    listAttCleanCat({ validFlag: true }).then((response) => {
        response.data.forEach(obj => {
            dataMapCat.set(obj.id + "", obj.name);
        });
        getList();
        processedData.value = proxy.handleTree(response.data, "id", "parentId");
        processedData.value = [
            {
                name: "清洗规则类目",
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
