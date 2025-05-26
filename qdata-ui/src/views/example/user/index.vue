<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch">
            <el-form
                class="btn-style"
                :model="queryParams"
                ref="queryRef"
                :inline="true"
                label-width="75px"
            >
                <el-form-item label="ID" prop="id">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.id"
                        placeholder="请输入ID"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="类型名称" prop="name">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.name"
                        placeholder="请输入类型名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="是否有效" prop="validFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.validFlag"
                        placeholder="请输入是否有效"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="删除标志" prop="delFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.delFlag"
                        placeholder="请输入删除标志"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="创建人" prop="createBy">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.createBy"
                        placeholder="请输入创建人"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="创建人id" prop="creatorId">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.creatorId"
                        placeholder="请输入创建人id"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="创建时间" prop="createTime">
                    <el-date-picker
                        class="el-form-input-width"
                        clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择创建时间"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="更新人" prop="updateBy">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.updateBy"
                        placeholder="请输入更新人"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="更新人id" prop="updaterId">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.updaterId"
                        placeholder="请输入更新人id"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="更新时间" prop="updateTime">
                    <el-date-picker
                        class="el-form-input-width"
                        clearable
                        v-model="queryParams.updateTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择更新时间"
                    >
                    </el-date-picker>
                </el-form-item>

                <el-form-item>
                    <el-button
                        plain
                        type="primary"
                        @click="handleQuery"
                        @mousedown="(e) => e.preventDefault()"
                    >
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
                        <el-button
                            type="primary"
                            plain
                            @click="handleAdd"
                            v-hasPermi="['user:userType:add']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            :disabled="single"
                            @click="handleUpdate"
                            v-hasPermi="['user:userType:edit']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="danger"
                            plain
                            :disabled="multiple"
                            @click="handleDelete"
                            v-hasPermi="['user:userType:remove']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="warning"
                            plain
                            @click="handleExport"
                            v-hasPermi="['user:userType:export']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-download-line mr5"></i>导出
                        </el-button>
                    </el-col>
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar
                        v-model:showSearch="showSearch"
                        @queryTable="getList"
                        :columns="columns"
                    ></right-toolbar>
                </div>
            </div>
            <el-table
                stripe
                height="498px"
                v-loading="loading"
                :data="userTypeList"
                @selection-change="handleSelectionChange"
                :default-sort="{ prop: 'createTime', order: 'descending' }"
            >
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
                <el-table-column
                    v-if="columns[1].visible"
                    label="类型名称"
                    align="center"
                    prop="name"
                >
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[2].visible"
                    label="是否有效"
                    align="center"
                    prop="validFlag"
                >
                    <template #default="scope">
                        {{ scope.row.validFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[3].visible"
                    label="删除标志"
                    align="center"
                    prop="delFlag"
                >
                    <template #default="scope">
                        {{ scope.row.delFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[4].visible"
                    label="创建人"
                    align="center"
                    prop="createBy"
                >
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[5].visible"
                    label="创建人id"
                    align="center"
                    prop="creatorId"
                >
                    <template #default="scope">
                        {{ scope.row.creatorId || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[6].visible"
                    label="创建时间"
                    align="center"
                    prop="createTime"
                    width="180"
                    sortable
                >
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[7].visible"
                    label="更新人"
                    align="center"
                    prop="updateBy"
                >
                    <template #default="scope">
                        {{ scope.row.updateBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[8].visible"
                    label="更新人id"
                    align="center"
                    prop="updaterId"
                >
                    <template #default="scope">
                        {{ scope.row.updaterId || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[9].visible"
                    label="更新时间"
                    align="center"
                    prop="updateTime"
                    width="180"
                    sortable
                >
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="columns[10].visible"
                    label="备注"
                    align="center"
                    prop="remark"
                >
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                    class-name="small-padding fixed-width"
                    fixed="right"
                    width="240"
                >
                    <template #default="scope">
                        <!--            <el-button link type="primary" icon="View" @click="handleView(scope.row)"-->
                        <!--                       v-hasPermi="['monitor:operlog:query']">详细</el-button>-->
                        <el-button
                            link
                            type="primary"
                            icon="Edit"
                            @click="handleUpdate(scope.row)"
                            v-hasPermi="['user:userType:edit']"
                            >修改</el-button
                        >
                        <el-button
                            link
                            type="danger"
                            icon="Delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['user:userType:remove']"
                            >删除</el-button
                        >
                    </template>
                </el-table-column>

                <template #empty>
                    <div class="emptyBg">
                        <img src="@/assets/system/images/no_data/noData.png" alt="" />
                        <p>暂无记录</p>
                    </div>
                </template>
            </el-table>

            <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
            />
        </div>

        <!-- 添加或修改用户类型对话框 -->
        <el-dialog
            :title="title"
            v-model="open"
            width="800px"
            :append-to="$refs['app-container']"
            draggable
            destroy-on-close
        >
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="userTypeRef" :model="form" :rules="rules" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="类型名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入类型名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否有效" prop="validFlag">
                            <el-input v-model="form.validFlag" placeholder="请输入是否有效" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="删除标志" prop="delFlag">
                            <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                            <el-input
                                v-model="form.remark"
                                type="textarea"
                                placeholder="请输入内容"
                            />
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

<script setup name="UserType">
    import {
        listUserType,
        getUserType,
        delUserType,
        addUserType,
        updateUserType
    } from '@/api/example/user/userType';

    const { proxy } = getCurrentInstance();

    const userTypeList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '类型名称', visible: true },
        { key: 2, label: '是否有效', visible: true },
        { key: 3, label: '删除标志', visible: true },
        { key: 4, label: '创建人', visible: true },
        { key: 5, label: '创建人id', visible: true },
        { key: 6, label: '创建时间', visible: true },
        { key: 7, label: '更新人', visible: true },
        { key: 8, label: '更新人id', visible: true },
        { key: 9, label: '更新时间', visible: true },
        { key: 10, label: '备注', visible: true }
    ]);

    const open = ref(false);
    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref('');

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            id: null,
            name: null,
            validFlag: null,
            delFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null
        },
        rules: {}
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询用户类型列表 */
    function getList() {
        loading.value = true;
        // listUserType(queryParams.value).then(response => {
        var response = {
            data: {}
        };
        (response.data = {
            rows: [
                {
                    id: 1,
                    name: '测试模版',
                    content: '${test}测试模版',
                    category: 0,
                    msgLevel: 0,
                    validFlag: true,
                    delFlag: false,
                    createBy: '',
                    creatorId: 1,
                    createTime: '2024-11-01',
                    updateBy: null,
                    updaterId: 1,
                    updateTime: '2024-11-08',
                    remark: null
                },
                {
                    id: 2,
                    name: '测试',
                    content: '2',
                    category: 0,
                    msgLevel: 0,
                    validFlag: true,
                    delFlag: false,
                    createBy: 'admin',
                    creatorId: 1,
                    createTime: '2024-11-20',
                    updateBy: null,
                    updaterId: null,
                    updateTime: '2024-11-20',
                    remark: null
                }
            ],
            total: 2
        }),
            (userTypeList.value = response.data.rows);
        total.value = response.data.total;
        loading.value = false;
        // });
    }

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
        proxy.resetForm('userTypeRef');
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

    /** 新增按钮操作 */
    function handleAdd() {
        reset();
        open.value = true;
        title.value = '新增用户类型';
    }

    /** 修改按钮操作 */
    function handleView(row) {
        proxy.$router.push('/example/complexdetails'); // 内部页面路径
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getUserType(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改用户类型';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['userTypeRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateUserType(form.value).then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    });
                } else {
                    addUserType(form.value).then((response) => {
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
        const _ids = row.id || ids.value;
        proxy.$modal
            .confirm('是否确认删除用户类型编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delUserType(_ids);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    /** 导出按钮操作 */
    function handleExport() {
        proxy.download(
            'example/userType/export',
            {
                ...queryParams.value
            },
            `userType_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
<style scoped lang="scss">
    .app-container {
        background-color: #f0f2f5;
        padding: 0px;
    }
    .pagecont-top {
        padding: 15px 15px 1px 15px;
        background-color: #ffffff;
        border-radius: 2px;
        box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.1);
    }
    .pagecont-bottom {
        margin-top: 15px;
        padding: 13px 15px;
        background-color: #ffffff;
        border-radius: 2px;
        box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.1);
    }
    .mb15 {
        margin-bottom: 15px;
    }
</style>
