<template>
    <div class="justify-between mb15">
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="360" v-loading="loading" :data="dpDataElemAssetRelList"
        @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column label="编号" align="left" prop="id" width="50" />
        <el-table-column label="资产名称" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="assetName"
            width="300">
            <template #default="scope">
                {{ scope.row.assetName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="描述" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="description"
            width="380">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="数据表" align="left" prop="tableName" width="290">
            <template #default="scope">
                {{ scope.row.tableName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="关联字段" align="left" prop="columnName" width="300">
            <template #default="scope">
                {{ scope.row.columnName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="创建人" :show-overflow-tooltip="{ effect: 'light' }" align="left" width="120"
            prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || "-" }}
            </template>
        </el-table-column>
        <el-table-column label="创建时间" align="left" prop="createTime" width="150">
            <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
            }}</span>
            </template>
        </el-table-column>
        <el-table-column label="更新时间" align="left" prop="updateTime" width="300">
            <template #default="scope">
                <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</span>
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
</template>

<script setup name="ComponentOne">
import {
    listDpDataElemAssetRel,
    getDpDataElemAssetRel,
    delDpDataElemAssetRel,
    addDpDataElemAssetRel,
    updateDpDataElemAssetRel
} from '@/api/dp/dataElem/dataElem';

const { proxy } = getCurrentInstance();
const route = useRoute();

const dpDataElemAssetRelList = ref([]);

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

const data = reactive({
    dpDataElemAssetRelDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemType: null,
        dataElemId: null,
        assetId: null,
        tableName: null,
        columnId: null,
        columnName: null,
        createTime: null
    },
    rules: {}
});

const { queryParams, form, dpDataElemAssetRelDetail, rules } = toRefs(data);

queryParams.value.dataElemId = route.query.id;
// 监听 id 变化
watch(
    () => route.query.id,
    (newId) => {
        queryParams.value.dataElemId = newId;
        getList();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
/** 查询数据元数据资产关联信息列表 */
function getList() {
    loading.value = true;
    listDpDataElemAssetRel(queryParams.value).then((response) => {
        dpDataElemAssetRelList.value = response.data.rows;
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
        dataElemType: null,
        dataElemId: null,
        assetId: null,
        tableName: null,
        columnId: null,
        columnName: null,
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
    proxy.resetForm('dpDataElemAssetRelRef');
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
    title.value = '新增数据元数据资产关联信息';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDpDataElemAssetRel(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = '修改数据元数据资产关联信息';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDpDataElemAssetRel(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = '数据元数据资产关联信息详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['dpDataElemAssetRelRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateDpDataElemAssetRel(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDpDataElemAssetRel(form.value)
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
        .confirm('是否确认删除数据元数据资产关联信息编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delDpDataElemAssetRel(_ids);
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
        'dp/dpDataElemAssetRel/export',
        {
            ...queryParams.value
        },
        `dpDataElemAssetRel_${new Date().getTime()}.xlsx`
    );
}

getList();
</script>
