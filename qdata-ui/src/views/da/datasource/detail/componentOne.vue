<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    v-hasPermi="['datasource:daDatasource:add']"
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
                    v-hasPermi="['datasource:daDatasource:export']"
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
        :data="daDatasourceList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
        <el-table-column
            v-if="columns[1].visible"
            label="数据源名称"
            align="center"
            prop="datasourceName"
        >
            <template #default="scope">
                {{ scope.row.datasourceName || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[2].visible"
            label="数据源类型"
            align="center"
            prop="datasourceType"
        >
            <template #default="scope">
                {{ scope.row.datasourceType || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[3].visible"
            label="数据源配置(json字符串)"
            align="center"
            prop="datasourceConfig"
        >
            <template #default="scope">
                {{ scope.row.datasourceConfig || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[4].visible" label="IP" align="center" prop="ip">
            <template #default="scope">
                {{ scope.row.ip || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[5].visible" label="端口号" align="center" prop="port">
            <template #default="scope">
                {{ scope.row.port || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[6].visible"
            label="数据库表数"
            align="center"
            prop="listCount"
        >
            <template #default="scope">
                {{ scope.row.listCount || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[7].visible"
            label="同步记录数"
            align="center"
            prop="syncCount"
        >
            <template #default="scope">
                {{ scope.row.syncCount || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[8].visible"
            label="同步数据量大小"
            align="center"
            prop="dataSize"
        >
            <template #default="scope">
                {{ scope.row.dataSize || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[9].visible" label="描述" align="center" prop="description">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[11].visible" label="创建人" align="center" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[13].visible"
            label="创建时间"
            align="center"
            prop="createTime"
            width="180"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[17].visible" label="备注" align="center" prop="remark">
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
                    v-hasPermi="['datasource:daDatasource:edit']"
                    >修改</el-button
                >
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['datasource:daDatasource:remove']"
                    >删除</el-button
                >
                <el-button
                    link
                    type="primary"
                    icon="view"
                    @click="handleDetail(scope.row)"
                    v-hasPermi="['datasource:daDatasource:edit']"
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

    <!-- 新增或修改数据源对话框 -->
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
        <el-form ref="daDatasourceRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="数据源名称" prop="datasourceName">
                        <el-input v-model="form.datasourceName" placeholder="请输入数据源名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="数据源配置(json字符串)" prop="datasourceConfig">
                        <el-input
                            v-model="form.datasourceConfig"
                            placeholder="请输入数据源配置(json字符串)"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="IP" prop="ip">
                        <el-input v-model="form.ip" placeholder="请输入IP" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="端口号" prop="port">
                        <el-input v-model="form.port" placeholder="请输入端口号" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据库表数" prop="listCount">
                        <el-input v-model="form.listCount" placeholder="请输入数据库表数" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="同步记录数" prop="syncCount">
                        <el-input v-model="form.syncCount" placeholder="请输入同步记录数" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="同步数据量大小" prop="dataSize">
                        <el-input v-model="form.dataSize" placeholder="请输入同步数据量大小" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="form.description" placeholder="请输入描述" />
                    </el-form-item>
                </el-col>
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

    <!-- 数据源详情对话框 -->
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
        <el-form ref="daDatasourceRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="数据源名称" prop="datasourceName">
                        <div>
                            {{ form.datasourceName }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据源类型" prop="datasourceType">
                        <div>
                            {{ form.datasourceType }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="数据源配置(json字符串)" prop="datasourceConfig">
                        <div>
                            {{ form.datasourceConfig }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="IP" prop="ip">
                        <div>
                            {{ form.ip }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="端口号" prop="port">
                        <div>
                            {{ form.port }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据库表数" prop="listCount">
                        <div>
                            {{ form.listCount }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="同步记录数" prop="syncCount">
                        <div>
                            {{ form.syncCount }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="同步数据量大小" prop="dataSize">
                        <div>
                            {{ form.dataSize }}
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
</template>

<script setup name="ComponentOne">
    import {
        listDaDatasource,
        getDaDatasource,
        delDaDatasource,
        addDaDatasource,
        updateDaDatasource
    } from '@/api/da/datasource/daDatasource';

    const { proxy } = getCurrentInstance();

    const daDatasourceList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '数据源名称', visible: true },
        { key: 2, label: '数据源类型', visible: true },
        { key: 3, label: '数据源配置(json字符串)', visible: true },
        { key: 4, label: 'IP', visible: true },
        { key: 5, label: '端口号', visible: true },
        { key: 6, label: '数据库表数（预留）', visible: true },
        { key: 7, label: '同步记录数（预留）', visible: true },
        { key: 8, label: '同步数据量大小（预留）', visible: true },
        { key: 9, label: '描述', visible: true },
        { key: 10, label: '是否有效', visible: true },
        { key: 11, label: '创建人', visible: true },
        { key: 12, label: '创建人id', visible: true },
        { key: 13, label: '创建时间', visible: true },
        { key: 14, label: '更新人', visible: true },
        { key: 15, label: '更新人id', visible: true },
        { key: 16, label: '更新时间', visible: true },
        { key: 17, label: '备注', visible: true }
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
        daDatasourceDetail: {},
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            datasourceName: null,
            datasourceType: null,
            datasourceConfig: null,
            ip: null,
            port: null,
            listCount: null,
            syncCount: null,
            dataSize: null,
            description: null,
            createTime: null
        },
        rules: {}
    });

    const { queryParams, form, daDatasourceDetail, rules } = toRefs(data);

    /** 查询数据源列表 */
    function getList() {
        loading.value = true;
        listDaDatasource(queryParams.value).then((response) => {
            daDatasourceList.value = response.data.rows;
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
            datasourceName: null,
            datasourceType: null,
            datasourceConfig: null,
            ip: null,
            port: null,
            listCount: null,
            syncCount: null,
            dataSize: null,
            description: null,
            validFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null
        };
        proxy.resetForm('daDatasourceRef');
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
        title.value = '新增数据源';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getDaDatasource(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改数据源';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getDaDatasource(_id).then((response) => {
            form.value = response.data;
            openDetail.value = true;
            title.value = '数据源详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['daDatasourceRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateDaDatasource(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addDaDatasource(form.value)
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
            .confirm('是否确认删除数据源编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delDaDatasource(_ids);
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
            'da/daDatasource/export',
            {
                ...queryParams.value
            },
            `daDatasource_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
