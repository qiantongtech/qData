<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px">
                <el-form-item label="数据资产类目名称" prop="name" label-width="130">
                    <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入数据资产类目名称" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="上级类目" prop="code">
                    <el-tree-select filterable class="el-form-input-width" v-model="queryParams.code"
                        :data="attAssetCatOptions" :props="{ value: 'code', label: 'name', children: 'children' }"
                        value-key="id" placeholder="请选择上级" check-strictly />
                </el-form-item>
                <el-form-item>
                    <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()"
                        v-hasPermi="['att:assetCat:query']">
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
                <el-row :gutter="10" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain icon="Plus" @click="handleAdd"
                            v-hasPermi="['att:assetCat:add']">新增</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button class="toggle-expand-all" type="primary" plain @click="toggleExpandAll">
                            <svg-icon v-if="isExpandAll" icon-class="toggle" />
                            <svg-icon v-else icon-class="expand" />
                            <span>{{ isExpandAll ? "折叠" : "展开" }}</span>
                        </el-button>
                    </el-col>
                </el-row>
                <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>

            <el-table height="60vh" v-if="refreshTable" v-loading="loading" :data="attAssetCatList" row-key="id"
                :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
                <el-table-column label="数据资产类目名称" align="left" prop="name" width="200"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>

                <el-table-column label="描述" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
                    width="250">
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="排序" align="left" prop="sortOrder" :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.sortOrder }}
                    </template>
                </el-table-column>
                <el-table-column label="创建人" align="center" prop="createBy">
                    <template #default="scope">
                        {{ scope.row.createBy || "-" }}
                    </template>
                </el-table-column>
                <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                        }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" align="center" prop="validFlag">
                    <template #default="scope">
                        <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

                        <el-switch v-model="scope.row.validFlag" active-color="#13ce66" inactive-color="#ff4949"
                            @change="handleStatusChange(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="备注" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                            v-hasPermi="['att:assetCat:edit']">修改</el-button>
                        <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                            v-hasPermi="['att:assetCat:add']">新增</el-button>
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                            v-hasPermi="['att:assetCat:remove']">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize" @pagination="getList" />
        </div>

        <!-- 新增或修改数据资产类目管理对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
            destroy-on-close>
            <el-form ref="attAssetCatRef" :model="form" :rules="rules" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="类目名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入数据资产类目名称" />
                        </el-form-item>
                    </el-col>
                    <!--            <el-form-item label="类别排序" prop="sortOrder">-->
                    <!--&lt;!&ndash;              <el-input v-model="form.sortOrder" placeholder="请输入类别排序" />&ndash;&gt;-->
                    <!--              <el-input-number v-model="form.sortOrder"  steps="1" :min="0"  placeholder="请输入类别排序" />-->
                    <!--            </el-form-item>-->
                    <el-col :span="12">
                        <el-form-item label="上级类目" prop="parentId">
                            <el-tree-select filterable :disabled="form.id" v-model="form.parentId"
                                :data="attAssetCatOptions" :props="{ value: 'id', label: 'name', children: 'children' }"
                                value-key="id" placeholder="请选择上级" check-strictly />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="描述">
                            <el-input type="textarea" placeholder="请输入描述" v-model="form.description"
                                :min-height="192" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20"> </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="排序" prop="sortOrder">
                            <el-input-number style="width: 100%" v-model="form.sortOrder" controls-position="right"
                                :min="0" />
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
                        <el-form-item label="备注">
                            <el-input type="textarea" placeholder="请输入备注" v-model="form.remark" :min-height="192" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="cancel">取 消</el-button>
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="AssetCat">
import {
    listAttAssetCat,
    getAttAssetCat,
    delAttAssetCat,
    addAttAssetCat,
    updateAttAssetCat
} from '@/api/att/cat/assetCat/assetCat.js';
const { proxy } = getCurrentInstance();

const attAssetCatList = ref([]);
const attAssetCatOptions = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref('');
const isExpandAll = ref(false);
const total = ref(0);
const refreshTable = ref(true);

const data = reactive({
    form: {},
    queryParams: {
        name: null,
        parentId: null
    },
    rules: {
        name: [{ required: true, message: '数据资产类目名称不能为空', trigger: 'blur' }],
        parentId: [{ required: true, message: '上级类目不能为空', trigger: 'blur' }]
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询数据资产类目管理列表 */
function getList() {
    loading.value = true;

    listAttAssetCat(queryParams.value).then((response) => {
        attAssetCatList.value = proxy.handleTree(response.data, 'id');
        // total.value = response.data.total;
        loading.value = false;
    });
}

/** 查询数据资产类目管理下拉树结构1 */

// 取消按钮
function cancel() {
    open.value = false;
    reset();
}

// 表单重置
function reset() {
    form.value = {
        id: null,
        name: null,
        parentId: null,
        sortOrder: 0,
        description: null,
        code: null,
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
    proxy.resetForm('attAssetCatRef');
}

/** 搜索按钮操作 */
function handleQuery() {
    getList();
}
/** 改变启用状态值 */
function handleStatusChange(row) {
    const text = row.validFlag === true ? '启用' : '禁用';
    proxy.$modal
        .confirm('确认要"' + text + '","' + row.name + '"数据资产类目吗？')
        .then(function () {
            updateAttAssetCat({ id: row.id, validFlag: row.validFlag }).then((response) => {
                proxy.$modal.msgSuccess(text + '成功');
                getList();
            }).catch((err) => {
                row.validFlag = !row.validFlag;
            });
        })
        .catch(function () {
            row.validFlag = !row.validFlag;
        });
}

/** 重置按钮操作 */
function resetQuery() {
    proxy.resetForm('queryRef');
    handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
    reset();
    // getTreeselect();
    listAttAssetCat().then((response) => {
        attAssetCatOptions.value = [];
        const data = { id: 0, name: '顶级节点', children: [] };
        data.children = proxy.handleTree(response.data, 'id', 'parentId');
        attAssetCatOptions.value.push(data);
    });
    if (row != null && row.id) {
        form.value.parentId = row.id;
    } else {
        form.value.parentId = 0;
    }
    open.value = true;
    title.value = '新增数据资产类目';
}

/** 展开/折叠操作 */
function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
        refreshTable.value = true;
    });
}
function getDataTree() {
    listAttAssetCat().then((response) => {
        attAssetCatOptions.value = [];
        const data = { id: 0, name: '顶级节点', children: [] };
        data.children = proxy.handleTree(response.data, 'id', 'parentId');
        attAssetCatOptions.value.push(data);
    });
}

/** 修改按钮操作 */
async function handleUpdate(row) {
    reset();
    // await getTreeselect();
    const response = await listAttAssetCat();
    attAssetCatOptions.value = [];
    // 过滤节点的计算属性
    const filteredDepts = response.data.filter((d) => {
        // 过滤条件：去掉目标部门ID或者祖先中包含目标部门ID的项
        return d.ID !== row.id && !d.parentId.toString().split(',').includes(row.id.toString());
    });
    const data = { id: 0, name: '顶级节点', children: [] };
    data.children = proxy.handleTree(filteredDepts, 'id', 'parentId');
    attAssetCatOptions.value.push(data);
    if (row != null) {
        form.value.parentId = row.parentId;
    }
    getAttAssetCat(row.id).then((response) => {
        //把createTime过滤掉
        delete response.data.createTime;
        delete response.data.updateTime;
        form.value = response.data;
        open.value = true;
        title.value = '修改数据资产类目';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['attAssetCatRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                updateAttAssetCat(form.value).then((response) => {
                    proxy.$modal.msgSuccess('修改成功');
                    open.value = false;
                    getList();
                });
            } else {
                addAttAssetCat(form.value).then((response) => {
                    proxy.$modal.msgSuccess('新增成功');
                    open.value = false;
                    getList();
                });
            }
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    proxy.$modal
        .confirm('是否确认删除数据资产类目管理编号为"' + row.name + '"的数据项？')
        .then(function () {
            return delAttAssetCat(row.id);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess('删除成功');
        })
        .catch(() => { });
}

getList();
getDataTree();
</script>
