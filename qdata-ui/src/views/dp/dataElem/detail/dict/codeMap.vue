<template>
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
    <el-table stripe height="360" v-loading="loading" :data="dpCodeMapList" @selection-change="handleSelectionChange"
        :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column label="编号" align="left" prop="id" width="50" />
        <el-table-column label="原始值" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="originalValue"
            width="210">
            <template #default="scope">
                {{ scope.row.originalValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="代码名" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="codeName"
            width="220">
            <template #default="scope">
                {{ scope.row.codeName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="代码值" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="codeValue"
            width="180">
            <template #default="scope">
                {{ scope.row.codeValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="创建人" align="left" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="创建时间" align="left" prop="createTime" width="200">
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
        </el-table-column>
        <el-table-column label="备注" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="remark" width="320">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="300">
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

    <!-- 新增或修改数据元代码映射对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
        <el-form ref="dpCodeMapRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="原始值" prop="originalValue">
                        <el-input v-model="form.originalValue" placeholder="请输入原始值" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="代码名" prop="codeName">
                        <el-select v-model="form.codeName" placeholder="请选择代码名" @change="handleCodeNameChange">
                            <el-option v-for="item in dpDataElemCodeList" :key="item.id" :label="item.codeName"
                                :value="item.codeName" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="代码值" prop="codeValue">
                        <el-input v-model="form.codeValue" placeholder="代码值" disabled />
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
</template>

<script setup name="ComponentOne">
import {
    listDpCodeMap,
    getDpCodeMap,
    delDpCodeMap,
    addDpCodeMap,
    updateDpCodeMap
} from '@/api/dp/dataElem/dataElem';
import { listDpDataElemCode } from '@/api/dp/dataElem/dataElem';
const route = useRoute();
const { proxy } = getCurrentInstance();

const dpCodeMapList = ref([]);
const dpDataElemCodeList = ref([]);

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
const id = ref(route.query.id || 1);

watch(
    () => route.query.id,
    (newId) => {
        if (newId !== id.value) {
            id.value = newId || -1;
            queryParams.value.dataElemId = id.value;
            getList();
            getDpDataElemCodeList();
        }
    },
    { immediate: true }
);

const data = reactive({
    dpCodeMapDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemId: null,
        originalValue: null,
        codeName: null,
        codeValue: null,
        createTime: null
    },
    rules: {
        originalValue: [{ required: true, message: '原始值不能为空', trigger: 'blur' }],
        codeName: [{ required: true, message: '代码名不能为空', trigger: 'change' }]
    }
});

const { queryParams, form, dpCodeMapDetail, rules } = toRefs(data);

/** 查询数据元代码映射列表 */
function getList() {
    if (id.value == -1) {
        return;
    }
    loading.value = true;
    queryParams.value.dataElemId = id.value;
    listDpCodeMap(queryParams.value).then((response) => {
        dpCodeMapList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}

function getDpDataElemCodeList() {
    if (id.value == -1) {
        return;
    }
    queryParams.value.dataElemId = id.value;
    listDpDataElemCode(queryParams.value).then((response) => {
        dpDataElemCodeList.value = response.data.rows;
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
        dataElemId: null,
        originalValue: null,
        codeName: null,
        codeValue: null,
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
    proxy.resetForm('dpCodeMapRef');
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
    title.value = '新增数据元代码映射';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDpCodeMap(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = '修改数据元代码映射';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDpCodeMap(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = '数据元代码映射详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['dpCodeMapRef'].validate((valid) => {
        if (valid) {
            form.value.dataElemId = id.value;
            if (form.value.id != null) {
                updateDpCodeMap(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDpCodeMap(form.value)
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
        .confirm('是否确认删除数据元代码映射编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delDpCodeMap(_ids);
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
        'dp/dpCodeMap/export',
        {
            ...queryParams.value
        },
        `dpCodeMap_${new Date().getTime()}.xlsx`
    );
}

// 新增代码名选择处理函数
function handleCodeNameChange(value) {
    const selectedCode = dpDataElemCodeList.value.find((item) => item.codeName === value);
    if (selectedCode) {
        form.value.codeValue = selectedCode.codeValue;
    }
}

getList();
getDpDataElemCodeList();

proxy.$bus.on('data_elem_code_change', (data) => {
    getDpDataElemCodeList();
});
</script>
