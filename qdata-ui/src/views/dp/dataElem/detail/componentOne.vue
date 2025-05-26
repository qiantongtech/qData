<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    v-hasPermi="['dataElem:dpDataElemRuleRel:add']"
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
                    v-hasPermi="['dataElem:dpDataElemRuleRel:export']"
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
        :data="dpDataElemRuleRelList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
        <el-table-column
            v-if="columns[1].visible"
            label="数据元id"
            align="center"
            prop="dataElemId"
        >
            <template #default="scope">
                {{ scope.row.dataElemId || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[2].visible" label="规则类型" align="center" prop="ruleType">
            <template #default="scope">
                {{ scope.row.ruleType || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[3].visible" label="规则id" align="center" prop="ruleId">
            <template #default="scope">
                {{ scope.row.ruleId || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[4].visible"
            label="规则配置"
            align="center"
            prop="ruleConfig"
        >
            <template #default="scope">
                {{ scope.row.ruleConfig || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[13].visible" label="备注" align="center" prop="remark">
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
                    v-hasPermi="['dataElem:dpDataElemRuleRel:edit']"
                    >修改</el-button
                >
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['dataElem:dpDataElemRuleRel:remove']"
                    >删除</el-button
                >
                <el-button
                    link
                    type="primary"
                    icon="view"
                    @click="handleDetail(scope.row)"
                    v-hasPermi="['dataElem:dpDataElemRuleRel:edit']"
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

    <!-- 新增或修改数据元数据规则关联信息对话框 -->
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
        <el-form ref="dpDataElemRuleRelRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="数据元id" prop="dataElemId">
                        <el-input v-model="form.dataElemId" placeholder="请输入数据元id" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="规则id" prop="ruleId">
                        <el-input v-model="form.ruleId" placeholder="请输入规则id" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="规则配置" prop="ruleConfig">
                        <el-input
                            v-model="form.ruleConfig"
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

    <!-- 数据元数据规则关联信息详情对话框 -->
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
        <el-form ref="dpDataElemRuleRelRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="数据元id" prop="dataElemId">
                        <div>
                            {{ form.dataElemId }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="规则类型" prop="ruleType">
                        <div>
                            {{ form.ruleType }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="规则id" prop="ruleId">
                        <div>
                            {{ form.ruleId }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="规则配置" prop="ruleConfig">
                        <div>
                            {{ form.ruleConfig }}
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
        listDpDataElemRuleRel,
        getDpDataElemRuleRel,
        delDpDataElemRuleRel,
        addDpDataElemRuleRel,
        updateDpDataElemRuleRel
    } from '@/api/dp/dataElem/dpDataElemRuleRel';

    const { proxy } = getCurrentInstance();

    const dpDataElemRuleRelList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '数据元id', visible: true },
        { key: 2, label: '规则类型', visible: true },
        { key: 3, label: '规则id', visible: true },
        { key: 4, label: '规则配置', visible: true },
        { key: 5, label: '是否有效', visible: true },
        { key: 6, label: '删除标志', visible: true },
        { key: 7, label: '创建人', visible: true },
        { key: 8, label: '创建人id', visible: true },
        { key: 9, label: '创建时间', visible: true },
        { key: 10, label: '更新人', visible: true },
        { key: 11, label: '更新人id', visible: true },
        { key: 12, label: '更新时间', visible: true },
        { key: 13, label: '备注', visible: true }
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
        dpDataElemRuleRelDetail: {},
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            dataElemId: null,
            ruleType: null,
            ruleId: null,
            ruleConfig: null
        },
        rules: {}
    });

    const { queryParams, form, dpDataElemRuleRelDetail, rules } = toRefs(data);

    /** 查询数据元数据规则关联信息列表 */
    function getList() {
        loading.value = true;
        listDpDataElemRuleRel(queryParams.value).then((response) => {
            dpDataElemRuleRelList.value = response.data.rows;
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
            dataElemId: null,
            ruleType: null,
            ruleId: null,
            ruleConfig: null,
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
        proxy.resetForm('dpDataElemRuleRelRef');
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
        title.value = '新增数据元数据规则关联信息';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getDpDataElemRuleRel(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改数据元数据规则关联信息';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getDpDataElemRuleRel(_id).then((response) => {
            form.value = response.data;
            openDetail.value = true;
            title.value = '数据元数据规则关联信息详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['dpDataElemRuleRelRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateDpDataElemRuleRel(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addDpDataElemRuleRel(form.value)
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
            .confirm('是否确认删除数据元数据规则关联信息编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delDpDataElemRuleRel(_ids);
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
            'dp/dpDataElemRuleRel/export',
            {
                ...queryParams.value
            },
            `dpDataElemRuleRel_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
