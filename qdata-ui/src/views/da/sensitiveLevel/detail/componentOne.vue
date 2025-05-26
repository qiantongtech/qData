<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    v-hasPermi="['sensitiveLevel:daSensitiveLevel:add']"
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
                    v-hasPermi="['sensitiveLevel:daSensitiveLevel:export']"
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
        :data="daSensitiveLevelList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
        <el-table-column
            v-if="columns[1].visible"
            label="敏感级别"
            align="center"
            prop="sensitiveLevel"
        >
            <template #default="scope">
                {{ scope.row.sensitiveLevel || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[2].visible"
            label="敏感规则"
            align="center"
            prop="sensitiveRule"
        >
            <template #default="scope">
                {{ scope.row.sensitiveRule || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[3].visible"
            label="起始字符位置"
            align="center"
            prop="startCharLoc"
        >
            <template #default="scope">
                {{ scope.row.startCharLoc || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[4].visible"
            label="截止字符位置"
            align="center"
            prop="endCharLoc"
        >
            <template #default="scope">
                {{ scope.row.endCharLoc || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[5].visible"
            label="遮盖字符"
            align="center"
            prop="maskCharacter"
        >
            <template #default="scope">
                {{ scope.row.maskCharacter || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[6].visible"
            label="上下线标识"
            align="center"
            prop="onlineFlag"
        >
            <template #default="scope">
                {{ scope.row.onlineFlag || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" label="描述" align="center" prop="description">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[10].visible" label="创建人" align="center" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[12].visible"
            label="创建时间"
            align="center"
            prop="createTime"
            width="180"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[16].visible" label="备注" align="center" prop="remark">
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
                    v-hasPermi="['sensitiveLevel:daSensitiveLevel:edit']"
                    >修改</el-button
                >
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['sensitiveLevel:daSensitiveLevel:remove']"
                    >删除</el-button
                >
                <el-button
                    link
                    type="primary"
                    icon="view"
                    @click="handleDetail(scope.row)"
                    v-hasPermi="['sensitiveLevel:daSensitiveLevel:edit']"
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

    <!-- 新增或修改敏感等级对话框 -->
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
        <el-form ref="daSensitiveLevelRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="敏感级别" prop="sensitiveLevel">
                        <el-input v-model="form.sensitiveLevel" placeholder="请输入敏感级别" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="敏感规则" prop="sensitiveRule">
                        <el-input v-model="form.sensitiveRule" placeholder="请输入敏感规则" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="起始字符位置" prop="startCharLoc">
                        <el-input v-model="form.startCharLoc" placeholder="请输入起始字符位置" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="截止字符位置" prop="endCharLoc">
                        <el-input v-model="form.endCharLoc" placeholder="请输入截止字符位置" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="遮盖字符" prop="maskCharacter">
                        <el-input v-model="form.maskCharacter" placeholder="请输入遮盖字符" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="上下线标识" prop="onlineFlag">
                        <el-input v-model="form.onlineFlag" placeholder="请输入上下线标识" />
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

    <!-- 敏感等级详情对话框 -->
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
        <el-form ref="daSensitiveLevelRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="敏感级别" prop="sensitiveLevel">
                        <div>
                            {{ form.sensitiveLevel }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="敏感规则" prop="sensitiveRule">
                        <div>
                            {{ form.sensitiveRule }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="起始字符位置" prop="startCharLoc">
                        <div>
                            {{ form.startCharLoc }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="截止字符位置" prop="endCharLoc">
                        <div>
                            {{ form.endCharLoc }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="遮盖字符" prop="maskCharacter">
                        <div>
                            {{ form.maskCharacter }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="上下线标识" prop="onlineFlag">
                        <div>
                            {{ form.onlineFlag }}
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
        listDaSensitiveLevel,
        getDaSensitiveLevel,
        delDaSensitiveLevel,
        addDaSensitiveLevel,
        updateDaSensitiveLevel
    } from '@/api/da/sensitiveLevel/daSensitiveLevel';

    const { proxy } = getCurrentInstance();

    const daSensitiveLevelList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '敏感级别', visible: true },
        { key: 2, label: '敏感规则', visible: true },
        { key: 3, label: '起始字符位置', visible: true },
        { key: 4, label: '截止字符位置', visible: true },
        { key: 5, label: '遮盖字符', visible: true },
        { key: 6, label: '上下线标识', visible: true },
        { key: 7, label: '描述', visible: true },
        { key: 8, label: '是否有效', visible: true },
        { key: 9, label: '删除标志', visible: true },
        { key: 10, label: '创建人', visible: true },
        { key: 11, label: '创建人id', visible: true },
        { key: 12, label: '创建时间', visible: true },
        { key: 13, label: '更新人', visible: true },
        { key: 14, label: '更新人id', visible: true },
        { key: 15, label: '更新时间', visible: true },
        { key: 16, label: '备注', visible: true }
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
        daSensitiveLevelDetail: {},
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            sensitiveLevel: null,
            sensitiveRule: null,
            startCharLoc: null,
            endCharLoc: null,
            maskCharacter: null,
            onlineFlag: null,
            description: null,
            createTime: null
        },
        rules: {}
    });

    const { queryParams, form, daSensitiveLevelDetail, rules } = toRefs(data);

    /** 查询敏感等级列表 */
    function getList() {
        loading.value = true;
        listDaSensitiveLevel(queryParams.value).then((response) => {
            daSensitiveLevelList.value = response.data.rows;
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
            sensitiveLevel: null,
            sensitiveRule: null,
            startCharLoc: null,
            endCharLoc: null,
            maskCharacter: null,
            onlineFlag: null,
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
        proxy.resetForm('daSensitiveLevelRef');
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
        title.value = '新增敏感等级';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getDaSensitiveLevel(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改敏感等级';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getDaSensitiveLevel(_id).then((response) => {
            form.value = response.data;
            openDetail.value = true;
            title.value = '敏感等级详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['daSensitiveLevelRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateDaSensitiveLevel(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addDaSensitiveLevel(form.value)
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
            .confirm('是否确认删除敏感等级编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delDaSensitiveLevel(_ids);
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
            'da/daSensitiveLevel/export',
            {
                ...queryParams.value
            },
            `daSensitiveLevel_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
