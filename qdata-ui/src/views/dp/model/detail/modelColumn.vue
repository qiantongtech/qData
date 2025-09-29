<template>
    <!-- 属性字段  -->
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="38.5vh" v-loading="loading" :data="dpModelColumnList"
        @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column label="编号" type="index" width="60" align="left">
            <template #default="scope">
                <span>{{
                    (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1
                    }}</span>
            </template>
        </el-table-column>
        <!-- <el-table-column label="编号" align="left" prop="index" /> -->
        <el-table-column label="关联标准" align="left" prop="dataElemName">
            <template #default="scope">
                {{ scope.row.dataElemName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="中文名称" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="cnName">
            <template #default="scope">
                {{ scope.row.cnName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="英文名称" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="engName">
            <template #default="scope">
                {{ scope.row.engName || '-' }}
            </template>
        </el-table-column>

        <el-table-column label="数据类型" align="left" prop="columnType">
            <template #default="scope">
                {{ scope.row.columnType || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="描述" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
            width="250">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="属性长度" align="left" prop="columnLength">
            <template #default="scope">
                {{ scope.row.columnLength || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="是否主键" align="left" prop="pkFlag">
            <template #default="scope">
                <el-switch v-model="scope.row.pkFlag" :active-value="'1'" :inactive-value="'0'" disabled />
            </template>
        </el-table-column>

        <el-table-column label="创建人" align="left" prop="createBy" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="创建时间" align="left" prop="createTime" :show-overflow-tooltip="{ effect: 'light' }"
            width="150">
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
        </el-table-column>
        <el-table-column label="备注" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
        </el-table-column>

        <template #empty>
            <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
            </div>
        </template>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 新增或修改逻辑模型属性信息对话框 -->
    <el-dialog class="autoHeight" :title="title" v-model="open" width="800px" :append-to="$refs['app-container']"
        draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="dpModelColumnRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联标准" prop="dataElemId">
                        <el-select v-model="form.dataElemId" placeholder="请选择关联标准" @change="handleDatasourceChange"
                            filterable>
                            <el-option v-for="dict in DpData" :key="dict.id" :label="dict.name"
                                :value="dict.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="中文名称" prop="cnName">
                        <el-input v-model="form.cnName" placeholder="请输入中文名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="英文名称" prop="engName">
                        <el-input v-model="form.engName" placeholder="请输入英文名称"
                            @input="convertToUpperCase('engName', form.engName)" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据类型" prop="columnType">
                        <el-select v-model="form.columnType" placeholder="请选择数据类型">
                            <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="属性长度" prop="columnLength">
                        <el-input-number :step="1" step-strictly v-model="form.columnLength" style="width: 100%"
                            controls-position="right" :min="1" :max="9999999999" placeholder="请输入属性长度" />
                    </el-form-item>
                </el-col>

            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="描述" prop="modelComment">
                        <el-input v-model="form.modelComment" type="textarea" placeholder="请输入描述" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <!-- decimal、NUMERIC、number -->
                    <el-form-item label="小数位数" prop="columnScale">
                        <el-input-number :disabled="form.columnType !== 'DECIMAL' &&
                            form.columnType !== 'NUMBER' &&
                            form.columnType !== 'NUMERIC' &&
                            form.columnType !== 'FLOAT' &&
                            form.columnType !== 'DOUBLE'
                            " :step="1" step-strictly v-model="form.columnScale" style="width: 100%"
                            controls-position="right" :min="0" :max="9999999999" placeholder="请输入小数长度" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="默认值" prop="defaultValue">
                        <el-input v-model="form.defaultValue" placeholder="请输入默认值" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="是否主键" prop="pkFlag">
                        <el-radio-group v-model="form.pkFlag" @change="handlePkFlagChange">
                            <el-radio v-for="dict in dp_model_column_pk_flag" :key="dict.value" :value="dict.value">{{
                                dict.label }}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否必填" prop="nullableFlag">
                        <el-radio-group v-model="form.nullableFlag" :disabled="form.pkFlag == 1">
                            <el-radio v-for="dict in dp_model_column_nullable_flag" :key="dict.value"
                                :value="dict.value">{{
                                    dict.label }}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="备注">
                        <el-input type="textarea" placeholder="请输入备注" v-model="form.remark" :min-height="192" />
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

    <!-- 逻辑模型属性信息详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="dpModelColumnRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联数据标准" prop="dataElemId">
                        <div>
                            {{ form.dataElemId }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="英文名称" prop="engName">
                        <div>
                            {{ form.engName }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="中文名称" prop="cnName">
                        <div>
                            {{ form.cnName }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据类型" prop="columnType">
                        <div>
                            {{ form.columnType }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="属性长度" prop="columnLength">
                        <div>
                            {{ form.columnLength }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="小数长度" prop="columnScale">
                        <div>
                            {{ form.columnScale }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="默认值" prop="defaultValue">
                        <div>
                            {{ form.defaultValue }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否主键" prop="pkFlag">
                        <div>
                            {{ form.pkFlag }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="是否必填" prop="nullableFlag">
                        <div>
                            {{ form.nullableFlag }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="排序" prop="sortOrder">
                        <div>
                            {{ form.sortOrder }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="数据元id" prop="dataElemId">
                        <div>
                            {{ form.dataElemId }}
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
</template>

<script setup name="ComponentOne">
import {
    listDpModelColumn,
    getDpModelColumn,
    delDpModelColumns,
    addDpModelColumn,
    updateDpModelColumns
} from '@/api/dp/model/model';
import { getDpDataElemList } from '@/api/dp/dataElem/dataElem';
import { deptTreeSelectNoPermi } from '@/api/system/system/user.js';
const { proxy } = getCurrentInstance();
const { column_type, dp_model_column_pk_flag, dp_model_column_nullable_flag } = proxy.useDict(
    'column_type',
    'dp_model_column_pk_flag',
    'dp_model_column_nullable_flag'
);
const dpModelColumnList = ref([]);
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const deptList = ref([]);
const defaultSort = ref({ prop: 'createTime', order: 'desc' });
const DpData = ref([]);
const handlePkFlagChange = (value) => {
    if (value == 1) {
        form.value.nullableFlag = '1';
    }
}
const getDpDataElem = async () => {
    try {
        const response = await getDpDataElemList();
        DpData.value = response.data;
    } catch (error) {
        console.error('请求失败:', error);
    }
};
const handleDatasourceChange = (value) => {
    const selectedDatasource = DpData.value.find((item) => item.id === value);
    if (selectedDatasource) {
        form.value.dataElemName = selectedDatasource.name;
        form.value.cnName = selectedDatasource.name;
        form.value.engName = selectedDatasource.engName;
        form.value.columnType = selectedDatasource.columnType;
    }
};
const data = reactive({
    dpModelColumnDetail: {},
    form: {
        pkFlag: '0',
        nullableFlag: '0',
    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        modelId: null,
        engName: null,
        cnName: null,
        columnType: null,
        columnLength: null,
        columnScale: null,
        defaultValue: null,
        pkFlag: null,
        nullableFlag: null,
        sortOrder: null,
        authorityDept: null,
        dataElemId: null,
        createTime: null
    },
    rules: {
        // 示例规则（可以根据需求添加或修改）
        cnName: [{ required: true, message: '中文名称不能为空', trigger: 'blur' }],
        engName: [{ required: true, message: '英文名称不能为空', trigger: 'blur' }],
        columnType: [{ required: true, message: '数据类型不能为空', trigger: 'blur' }],
        columnLength: [{ required: true, message: '属性长度不能为空', trigger: 'blur' }],
        // pkFlag: [{ required: true, message: '是否主键不能为空', trigger: 'blur' }],
        // nullableFlag: [{ required: true, message: '是否必填不能为空', trigger: 'blur' }]
    }
});
const { queryParams, form, dpModelColumnDetail, rules } = toRefs(data);

const route = useRoute();
let modelId = route.query.id || 1;
// 监听 id 变化
watch(
    () => route.query.id,
    (newId) => {
        modelId = newId || 1; // 如果 id 为空，使用默认值 1
        getList();

        // getList();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
// 转换输入值为大写
const convertToUpperCase = (key, value) => {
    const uppercasedValue = value.replace(/[a-z]/g, (char) => char.toUpperCase());

    form.value[key] = uppercasedValue;

    console.log('🚀 ~ convertToUpperCase ~ form.value[key]:', form.value[key]);
};
/** 查询逻辑模型属性信息列表 */
function getList() {
    loading.value = true;
    listDpModelColumn({ ...queryParams.value, modelId }).then((response) => {
        dpModelColumnList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
        // 部门
        deptTreeSelectNoPermi().then((response) => {
            deptList.value = response.data;
            dpModelColumnList.value.forEach((item) => {
                // 递归查找树形结构中匹配的节点
                const findLabel = (tree) => {
                    for (let node of tree) {
                        if (node.id == item.authorityDept) {
                            return node.label;
                        }
                        if (node.children) {
                            const found = findLabel(node.children);
                            if (found) return found;
                        }
                    }
                    return null;
                };
                item.deptLabel = findLabel(deptList.value) || '-';
            });
        });
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
        modelId: null,
        engName: null,
        cnName: null,
        columnType: null,
        columnLength: null,
        columnScale: null,
        defaultValue: null,
        pkFlag: '0',
        nullableFlag: '0',
        sortOrder: null,
        authorityDept: null,
        dataElemId: null,
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
    proxy.resetForm('dpModelColumnRef');
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
    title.value = '新增逻辑模型属性信息';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDpModelColumn(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = '修改逻辑模型属性信息';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDpModelColumn(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = '逻辑模型属性信息详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['dpModelColumnRef'].validate((valid) => {
        if (valid) {
            form.value.modelId = modelId;
            if (form.value.id != null) {
                updateDpModelColumns(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDpModelColumn(form.value)
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
        .confirm('是否确认删除逻辑模型属性信息编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delDpModelColumns(_ids);
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
        'dp/modelColumn/export',
        {
            ...queryParams.value
        },
        `dpModelColumn_${new Date().getTime()}.xlsx`
    );
}
getDpDataElem();
</script>
