<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
            label-width="68px"
        >
            <el-form-item label="类别名称" prop="name">
                <el-input
                    v-model="queryParams.name"
                    placeholder="请输入类别名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="上级类目" prop="parentId">
                <el-tree-select
                    v-model="form.parentId"
                    :data="attModelCatOptions"
                    :props="{ value: 'id', label: 'name', children: 'children' }"
                    value-key="ID"
                    placeholder="请选择上级"
                    check-strictly
                />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    icon="Plus"
                    @click="handleAdd"
                    v-hasPermi="['att:cat:dataelemcat:add']"
                    >新增</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button type="info" plain icon="Sort" @click="toggleExpandAll"
                    >展开/折叠</el-button
                >
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table
            v-if="refreshTable"
            v-loading="loading"
            :data="attDataElemCatList"
            row-key="ID"
            :default-expand-all="isExpandAll"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        >
            <el-table-column label="类别名称" prop="name" />
            <el-table-column label="描述" align="left" prop="description" />
            <el-table-column label="层级编码" align="center" prop="code" />
            <el-table-column label="是否有效" align="center" prop="validFlag" />
            <el-table-column label="备注" align="left" prop="remark" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button
                        link
                        type="primary"
                        icon="Edit"
                        @click="handleUpdate(scope.row)"
                        v-hasPermi="['att:cat:dataelemcat:edit']"
                        >修改</el-button
                    >
                    <el-button
                        link
                        type="primary"
                        icon="Plus"
                        @click="handleAdd(scope.row)"
                        v-hasPermi="['att:cat:dataelemcat:add']"
                        >新增</el-button
                    >
                    <el-button
                        link
                        type="primary"
                        icon="Delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['att:cat:dataelemcat:remove']"
                        >删除</el-button
                    >
                </template>
            </el-table-column>
        </el-table>

        <!-- 新增或修改逻辑模型类目管理对话框 -->
        <el-dialog
            :title="title"
            v-model="open"
            width="800px"
            :append-to="$refs['app-container']"
            draggable
            destroy-on-close
        >
            <el-form ref="attModelCatRef" :model="form" :rules="rules" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="编码" prop="code">
                            <el-input v-model="form.code" placeholder="请输入编码" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="类别名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入类别名称" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20"> </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="类别排序" prop="sortOrder">
                            <el-input v-model="form.sortOrder" placeholder="请输入类别排序" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="上级类目" prop="parentId">
                            <el-tree-select
                                v-model="form.parentId"
                                :data="attModelCatOptions"
                                :props="{ value: 'id', label: 'name', children: 'children' }"
                                value-key="ID"
                                placeholder="请选择上级"
                                check-strictly
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="是否有效" prop="validFlag">
                            <el-radio v-model="form.validFlag" :label="true">启用</el-radio>
                            <el-radio v-model="form.validFlag" :label="false">禁用</el-radio>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="描述">
                            <el-input
                                type="textarea"
                                v-model="form.description"
                                :min-height="192"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注">
                            <el-input type="textarea" v-model="form.remark" :min-height="192" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="AttDataElemCat">
    import {
        listAttDataElemCat,
        getAttDataElemCat,
        delAttDataElemCat,
        addAttDataElemCat,
        updateAttDataElemCat
    } from '@/api/att/cat/attDataElemCat/attDataElemCat.js';

    const { proxy } = getCurrentInstance();

    const attDataElemCatList = ref([]);
    const attDataElemCatOptions = ref([]);
    const open = ref(false);
    const loading = ref(true);
    const showSearch = ref(true);
    const title = ref('');
    const isExpandAll = ref(false);
    const refreshTable = ref(true);

    const data = reactive({
        form: {
            validFlag: true
        },
        queryParams: {
            name: null,
            parentId: null,
            createTime: null
        },
        rules: {
            code: [{ required: true, message: '编码不能为空', trigger: 'blur' }],
            name: [{ required: true, message: '类别名称不能为空', trigger: 'blur' }],
            parentId: [{ required: true, message: '关联上级ID不能为空', trigger: 'blur' }]
        }
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询数据元类目管理列表 */
    function getList() {
        loading.value = true;
        listAttDataElemCat(queryParams.value).then((response) => {
            attDataElemCatList.value = proxy.handleTree(response.data, 'id', 'parentId');
            loading.value = false;
        });
    }

    /** 查询数据元类目管理下拉树结构 */

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
            sortOrder: null,
            description: null,
            code: null,
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
        proxy.resetForm('attDataElemCatRef');
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        getList();
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
        listAttDataElemCat().then((response) => {
            attDataElemCatOptions.value = [];
            const data = { id: 0, name: '顶级节点', children: [] };
            data.children = proxy.handleTree(response.data, 'id', 'parentId');
            attDataElemCatOptions.value.push(data);
        });
        if (row != null && row.id) {
            form.value.parentId = row.id;
        } else {
            form.value.parentId = 0;
        }
        open.value = true;
        title.value = '新增数据元类目管理';
    }

    /** 展开/折叠操作 */
    function toggleExpandAll() {
        refreshTable.value = false;
        isExpandAll.value = !isExpandAll.value;
        nextTick(() => {
            refreshTable.value = true;
        });
    }

    /** 修改按钮操作 */
    async function handleUpdate(row) {
        reset();
        // await getTreeselect();
        const response = await listAttDataElemCat();
        attDataElemCatOptions.value = [];
        // 过滤节点的计算属性
        const filteredDepts = response.data.filter((d) => {
            // 过滤条件：去掉目标部门ID或者祖先中包含目标部门ID的项
            return d.ID !== row.ID && !d.parentId.toString().split(',').includes(row.ID.toString());
        });
        const data = { ID: 0, NAME: '顶级节点', children: [] };
        data.children = proxy.handleTree(filteredDepts, 'ID', 'parentId');
        attDataElemCatOptions.value.push(data);
        if (row != null) {
            form.value.parentId = row.parentId;
        }
        getAttDataElemCat(row.id).then((response) => {
            //把createTime过滤掉
            delete response.data.createTime;
            delete response.data.updateTime;
            form.value = response.data;

            open.value = true;
            title.value = '修改数据元类目管理';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['attModelCatRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateAttDataElemCat(form.value).then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    });
                } else {
                    addAttDataElemCat(form.value).then((response) => {
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
            .confirm('是否确认删除数据元类目管理编号为"' + row.id + '"的数据项？')
            .then(function () {
                return delAttDataElemCat(row.id);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    getList();
</script>
