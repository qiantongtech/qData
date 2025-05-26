<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    v-hasPermi="['client:client:add']"
                    @mousedown="(e) => e.preventDefault()"
                >
                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="warning"
                    plain
                    @click="handleExport"
                    v-hasPermi="['client:client:export']"
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
        height="374px"
        v-loading="loading"
        :data="clientList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
        <el-table-column v-if="columns[1].visible" label="应用名称" align="center" prop="name">
            <template #default="scope">
                {{ scope.row.name || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[2].visible" label="应用类型" align="center" prop="type">
            <template #default="scope">
                <dict-tag :options="auth_app_type" :value="scope.row.type" />
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[5].visible"
            label="允许授权的url"
            align="center"
            prop="allowUrl"
        >
            <template #default="scope">
                {{ scope.row.allowUrl || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" label="同步地址" align="center" prop="syncUrl">
            <template #default="scope">
                {{ scope.row.syncUrl || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[7].visible"
            label="应用图标"
            align="center"
            prop="logo"
            width="100"
        >
            <template #default="scope">
                <image-preview :src="scope.row.logo" :width="50" :height="50" />
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[8].visible"
            label="应用描述"
            align="center"
            prop="description"
        >
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[9].visible"
            label="是否公开"
            align="center"
            prop="publicFlag"
        >
            <template #default="scope">
                <dict-tag :options="auth_public" :value="scope.row.publicFlag" />
            </template>
        </el-table-column>
        <el-table-column v-if="columns[12].visible" label="创建人" align="center" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[14].visible"
            label="创建时间"
            align="center"
            prop="createTime"
            width="180"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[18].visible" label="备注" align="center" prop="remark">
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
                <el-button
                    link
                    type="primary"
                    icon="Edit"
                    @click="handleUpdate(scope.row)"
                    v-hasPermi="['client:client:edit']"
                    >修改</el-button
                >
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['client:client:remove']"
                    >删除</el-button
                >
                <el-button
                    link
                    type="primary"
                    icon="view"
                    @click="handleDetail(scope.row)"
                    v-hasPermi="['client:client:edit']"
                    >详情</el-button
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

    <!-- 新增或修改应用管理对话框 -->
    <el-dialog
        :title="title"
        v-model="open"
        width="800px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="clientRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="应用名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入应用名称" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="应用类型" prop="type">
                        <el-select v-model="form.type" placeholder="请选择应用类型">
                            <el-option
                                v-for="dict in auth_app_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="主页地址" prop="homepageUrl">
                        <el-input v-model="form.homepageUrl" placeholder="请输入主页地址" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="允许授权的url" prop="allowUrl">
                        <el-input
                            v-model="form.allowUrl"
                            type="textarea"
                            placeholder="请输入内容"
                        />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="同步地址" prop="syncUrl">
                        <el-input v-model="form.syncUrl" placeholder="请输入同步地址" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="应用图标" prop="logo">
                        <image-upload v-model="form.logo" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="应用描述" prop="description">
                        <el-input
                            v-model="form.description"
                            type="textarea"
                            placeholder="请输入内容"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否公开" prop="publicFlag">
                        <el-radio-group v-model="form.publicFlag">
                            <el-radio
                                v-for="dict in auth_public"
                                :key="dict.value"
                                :label="dict.value"
                                >{{ dict.label }}</el-radio
                            >
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" placeholder="请输入备注" />
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

    <!-- 应用管理详情对话框 -->
    <el-dialog
        :title="title"
        v-model="openDetail"
        width="800px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="clientRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="应用名称" prop="name">
                        <div>
                            {{ form.name }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="应用类型" prop="type">
                        <dict-tag :options="auth_app_type" :value="form.type" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="主页地址" prop="homepageUrl">
                        <div>
                            {{ form.homepageUrl }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="允许授权的url" prop="allowUrl">
                        <div>
                            {{ form.allowUrl }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="同步地址" prop="syncUrl">
                        <div>
                            {{ form.syncUrl }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="应用图标" prop="logo">
                        <image-preview :src="form.logo" :width="50" :height="50" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="应用描述" prop="description">
                        <div>
                            {{ form.description }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否公开" prop="publicFlag">
                        <dict-tag :options="auth_public" :value="form.publicFlag" />
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
        listClient,
        getClient,
        delClient,
        addClient,
        updateClient
    } from '@/api/att/client/client';

    const { proxy } = getCurrentInstance();
    const { auth_public, auth_app_type } = proxy.useDict('auth_public', 'auth_app_type');

    const clientList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '应用名称', visible: true },
        { key: 2, label: '应用类型', visible: true },
        { key: 3, label: '应用秘钥', visible: true },
        { key: 4, label: '主页地址', visible: true },
        { key: 5, label: '允许授权的url', visible: true },
        { key: 6, label: '同步地址', visible: true },
        { key: 7, label: '应用图标', visible: true },
        { key: 8, label: '应用描述', visible: true },
        { key: 9, label: '是否公开', visible: true },
        { key: 10, label: '是否有效', visible: true },
        { key: 11, label: '删除标志', visible: true },
        { key: 12, label: '创建人', visible: true },
        { key: 13, label: '创建人id', visible: true },
        { key: 14, label: '创建时间', visible: true },
        { key: 15, label: '更新人', visible: true },
        { key: 16, label: '更新人id', visible: true },
        { key: 17, label: '更新时间', visible: true },
        { key: 18, label: '备注', visible: true }
    ]);

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
        clientDetail: {},
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            id: null,
            name: null,
            type: null,
            secret: null,
            homepageUrl: null,
            allowUrl: null,
            syncUrl: null,
            logo: null,
            description: null,
            publicFlag: null,
            createTime: null
        },
        rules: {
            name: [{ required: true, message: '应用名称不能为空', trigger: 'blur' }],
            type: [{ required: true, message: '应用类型不能为空', trigger: 'change' }]
        }
    });

    const { queryParams, form, clientDetail, rules } = toRefs(data);

    /** 查询应用管理列表 */
    function getList() {
        loading.value = true;
        listClient(queryParams.value).then((response) => {
            clientList.value = response.data.rows;
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
            secret: null,
            homepageUrl: null,
            allowUrl: null,
            syncUrl: null,
            logo: null,
            description: null,
            publicFlag: null,
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
        proxy.resetForm('clientRef');
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
        title.value = '新增应用管理';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getClient(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改应用管理';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getClient(_id).then((response) => {
            form.value = response.data;
            openDetail.value = true;
            title.value = '应用管理详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['clientRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateClient(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addClient(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('新增成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                }
            }
        });
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const _ids = row.id || ids.value;
        proxy.$modal
            .confirm('是否确认删除应用管理编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delClient(_ids);
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
            'att/client/export',
            {
                ...queryParams.value
            },
            `client_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
