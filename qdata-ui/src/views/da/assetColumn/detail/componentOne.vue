<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    v-hasPermi="['assetColumn:daAssetColumn:add']"
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
                    v-hasPermi="['assetColumn:daAssetColumn:export']"
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
        :data="daAssetColumnList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
        <el-table-column v-if="columns[1].visible" label="资产id" align="center" prop="assetId">
            <template #default="scope">
                {{ scope.row.assetId || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[2].visible"
            label="中文名称/英文名称"
            align="center"
            prop="columnName"
        >
            <template #default="scope">
                {{ scope.row.columnName || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[3].visible"
            label="字段注释/中文名称"
            align="center"
            prop="columnComment"
        >
            <template #default="scope">
                {{ scope.row.columnComment || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[4].visible"
            label="数据类型"
            align="center"
            prop="columnType"
        >
            <template #default="scope">
                {{ scope.row.columnType || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[5].visible" label="长度" align="center" prop="columnLength">
            <template #default="scope">
                {{ scope.row.columnLength || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" label="小数位" align="center" prop="columnScale">
            <template #default="scope">
                {{ scope.row.columnScale || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[7].visible"
            label="是否必填"
            align="center"
            prop="nullableFlag"
        >
            <template #default="scope">
                {{ scope.row.nullableFlag || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" label="是否主键" align="center" prop="pkFlag">
            <template #default="scope">
                {{ scope.row.pkFlag || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[9].visible"
            label="默认值"
            align="center"
            prop="defaultValue"
        >
            <template #default="scope">
                {{ scope.row.defaultValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[10].visible"
            label="是否代码"
            align="center"
            prop="dataElemCodeFlag"
        >
            <template #default="scope">
                {{ scope.row.dataElemCodeFlag || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[11].visible"
            label="代码id"
            align="center"
            prop="dataElemCodeId"
        >
            <template #default="scope">
                {{ scope.row.dataElemCodeId || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[12].visible"
            label="敏感等级id"
            align="center"
            prop="sensitiveLevelId"
        >
            <template #default="scope">
                {{ scope.row.sensitiveLevelId || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[13].visible"
            label="关联数据元"
            align="center"
            prop="relDataElmeFlag"
        >
            <template #default="scope">
                {{ scope.row.relDataElmeFlag || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[14].visible"
            label="关联清洗规则"
            align="center"
            prop="relCleanFlag"
        >
            <template #default="scope">
                {{ scope.row.relCleanFlag || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[15].visible"
            label="关联稽查规则"
            align="center"
            prop="relAuditFlag"
        >
            <template #default="scope">
                {{ scope.row.relAuditFlag || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[16].visible" label="描述" align="center" prop="description">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[19].visible" label="创建人" align="center" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[21].visible"
            label="创建时间"
            align="center"
            prop="createTime"
            width="180"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[25].visible" label="备注" align="center" prop="remark">
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
                    v-hasPermi="['assetColumn:daAssetColumn:edit']"
                    >修改</el-button
                >
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['assetColumn:daAssetColumn:remove']"
                    >删除</el-button
                >
                <el-button
                    link
                    type="primary"
                    icon="view"
                    @click="handleDetail(scope.row)"
                    v-hasPermi="['assetColumn:daAssetColumn:edit']"
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

    <!-- 新增或修改数据资产字段对话框 -->
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
        <el-form ref="daAssetColumnRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="资产id" prop="assetId">
                        <el-input v-model="form.assetId" placeholder="请输入资产id" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="中文名称/英文名称" prop="columnName">
                        <el-input v-model="form.columnName" placeholder="请输入中文名称/英文名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="字段注释/中文名称" prop="columnComment">
                        <el-input
                            v-model="form.columnComment"
                            placeholder="请输入字段注释/中文名称"
                        />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="长度" prop="columnLength">
                        <el-input v-model="form.columnLength" placeholder="请输入长度" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="小数位" prop="columnScale">
                        <el-input v-model="form.columnScale" placeholder="请输入小数位" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="是否必填" prop="nullableFlag">
                        <el-input v-model="form.nullableFlag" placeholder="请输入是否必填" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否主键" prop="pkFlag">
                        <el-input v-model="form.pkFlag" placeholder="请输入是否主键" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="默认值" prop="defaultValue">
                        <el-input v-model="form.defaultValue" placeholder="请输入默认值" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否代码" prop="dataElemCodeFlag">
                        <el-input v-model="form.dataElemCodeFlag" placeholder="请输入是否代码" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="代码id" prop="dataElemCodeId">
                        <el-input v-model="form.dataElemCodeId" placeholder="请输入代码id" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="敏感等级id" prop="sensitiveLevelId">
                        <el-input v-model="form.sensitiveLevelId" placeholder="请输入敏感等级id" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联数据元" prop="relDataElmeFlag">
                        <el-input v-model="form.relDataElmeFlag" placeholder="请输入关联数据元" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="关联清洗规则" prop="relCleanFlag">
                        <el-input v-model="form.relCleanFlag" placeholder="请输入关联清洗规则" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联稽查规则" prop="relAuditFlag">
                        <el-input v-model="form.relAuditFlag" placeholder="请输入关联稽查规则" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="form.description" placeholder="请输入描述" />
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

    <!-- 数据资产字段详情对话框 -->
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
        <el-form ref="daAssetColumnRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="资产id" prop="assetId">
                        <div>
                            {{ form.assetId }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="中文名称/英文名称" prop="columnName">
                        <div>
                            {{ form.columnName }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="字段注释/中文名称" prop="columnComment">
                        <div>
                            {{ form.columnComment }}
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
                    <el-form-item label="长度" prop="columnLength">
                        <div>
                            {{ form.columnLength }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="小数位" prop="columnScale">
                        <div>
                            {{ form.columnScale }}
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
                    <el-form-item label="是否主键" prop="pkFlag">
                        <div>
                            {{ form.pkFlag }}
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
                    <el-form-item label="是否代码" prop="dataElemCodeFlag">
                        <div>
                            {{ form.dataElemCodeFlag }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="代码id" prop="dataElemCodeId">
                        <div>
                            {{ form.dataElemCodeId }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="敏感等级id" prop="sensitiveLevelId">
                        <div>
                            {{ form.sensitiveLevelId }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联数据元" prop="relDataElmeFlag">
                        <div>
                            {{ form.relDataElmeFlag }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="关联清洗规则" prop="relCleanFlag">
                        <div>
                            {{ form.relCleanFlag }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联稽查规则" prop="relAuditFlag">
                        <div>
                            {{ form.relAuditFlag }}
                        </div>
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
</template>

<script setup name="ComponentOne">
    import {
        listDaAssetColumn,
        getDaAssetColumn,
        delDaAssetColumn,
        addDaAssetColumn,
        updateDaAssetColumn
    } from '@/api/da/assetColumn/daAssetColumn';

    const { proxy } = getCurrentInstance();

    const daAssetColumnList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '资产id', visible: true },
        { key: 2, label: '中文名称/英文名称', visible: true },
        { key: 3, label: '字段注释/中文名称', visible: true },
        { key: 4, label: '数据类型', visible: true },
        { key: 5, label: '长度', visible: true },
        { key: 6, label: '小数位', visible: true },
        { key: 7, label: '是否必填', visible: true },
        { key: 8, label: '是否主键', visible: true },
        { key: 9, label: '默认值', visible: true },
        { key: 10, label: '是否代码', visible: true },
        { key: 11, label: '代码id', visible: true },
        { key: 12, label: '敏感等级id', visible: true },
        { key: 13, label: '关联数据元', visible: true },
        { key: 14, label: '关联清洗规则', visible: true },
        { key: 15, label: '关联稽查规则', visible: true },
        { key: 16, label: '描述', visible: true },
        { key: 17, label: '是否有效', visible: true },
        { key: 18, label: '删除标志', visible: true },
        { key: 19, label: '创建人', visible: true },
        { key: 20, label: '创建人id', visible: true },
        { key: 21, label: '创建时间', visible: true },
        { key: 22, label: '更新人', visible: true },
        { key: 23, label: '更新人id', visible: true },
        { key: 24, label: '更新时间', visible: true },
        { key: 25, label: '备注', visible: true }
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
        daAssetColumnDetail: {},
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            assetId: null,
            columnName: null,
            columnComment: null,
            columnType: null,
            columnLength: null,
            columnScale: null,
            nullableFlag: null,
            pkFlag: null,
            defaultValue: null,
            dataElemCodeFlag: null,
            dataElemCodeId: null,
            sensitiveLevelId: null,
            relDataElmeFlag: null,
            relCleanFlag: null,
            relAuditFlag: null,
            description: null,
            createTime: null
        },
        rules: {}
    });

    const { queryParams, form, daAssetColumnDetail, rules } = toRefs(data);

    /** 查询数据资产字段列表 */
    function getList() {
        loading.value = true;
        listDaAssetColumn(queryParams.value).then((response) => {
            daAssetColumnList.value = response.data.rows;
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
            assetId: null,
            columnName: null,
            columnComment: null,
            columnType: null,
            columnLength: null,
            columnScale: null,
            nullableFlag: null,
            pkFlag: null,
            defaultValue: null,
            dataElemCodeFlag: null,
            dataElemCodeId: null,
            sensitiveLevelId: null,
            relDataElmeFlag: null,
            relCleanFlag: null,
            relAuditFlag: null,
            description: null,
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
        proxy.resetForm('daAssetColumnRef');
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
        title.value = '新增数据资产字段';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getDaAssetColumn(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改数据资产字段';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getDaAssetColumn(_id).then((response) => {
            form.value = response.data;
            openDetail.value = true;
            title.value = '数据资产字段详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['daAssetColumnRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateDaAssetColumn(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addDaAssetColumn(form.value)
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
            .confirm('是否确认删除数据资产字段编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delDaAssetColumn(_ids);
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
            'da/daAssetColumn/export',
            {
                ...queryParams.value
            },
            `daAssetColumn_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
