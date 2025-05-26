<template>
    <div class="justify-between mb15">
        <!--    <el-row :gutter="15" class="btn-style">-->
        <!--      <el-col :span="1.5">-->
        <!--        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['apiLog:apiLog:add']"-->
        <!--                   @mousedown="(e) => e.preventDefault()">-->
        <!--          <i class="iconfont-mini icon-xinzeng mr5"></i>新增-->
        <!--        </el-button>-->
        <!--      </el-col>-->
        <!--      <el-col :span="1.5">-->
        <!--        <el-button type="warning" plain @click="handleExport" v-hasPermi="['apiLog:apiLog:export']"-->
        <!--                   @mousedown="(e) => e.preventDefault()">-->
        <!--          <i class="iconfont-mini icon-download-line mr5"></i>导出-->
        <!--        </el-button>-->
        <!--      </el-col>-->
        <!--    </el-row>-->
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
        :data="apiLogList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="ID" />
        <el-table-column v-if="columns[4].visible" label="调用者ip" align="center" prop="callerIp">
            <template #default="scope">
                {{ scope.row.callerIp || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[9].visible"
            label="调用数据量"
            align="center"
            prop="callerSize"
        >
            <template #default="scope">
                {{ scope.row.callerSize || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[10].visible"
            label="调用耗时(毫秒)"
            align="center"
            prop="callerTime"
        >
            <template #default="scope">
                {{ scope.row.callerTime || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[12].visible" label="状态" align="center" prop="STATUS">
            <template #default="scope">
                <dict-tag :options="ds_api_log_res_status" :value="scope.row.STATUS" />
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[17].visible"
            label="创建时间"
            align="center"
            prop="createTime"
            width="180"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[21].visible" label="备注" align="center" prop="REMARK">
            <template #default="scope">
                {{ scope.row.REMARK || '-' }}
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
                    v-hasPermi="['apiLog:apiLog:edit']"
                    >修改</el-button
                >
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['apiLog:apiLog:remove']"
                    >删除</el-button
                >
                <el-button
                    link
                    type="primary"
                    icon="view"
                    @click="handleDetail(scope.row)"
                    v-hasPermi="['apiLog:apiLog:edit']"
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

    <!-- 添加或修改API服务调用日志对话框 -->
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
        <el-form ref="apiLogRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="调用url" prop="callerUrl">
                        <el-input v-model="form.callerUrl" placeholder="请输入调用url" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="调用参数" prop="callerParams">
                        <el-input
                            v-model="form.callerParams"
                            type="textarea"
                            placeholder="请输入内容"
                        />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="调用开始时间" prop="callerStartDate">
                        <el-date-picker
                            clearable
                            v-model="form.callerStartDate"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择调用开始时间"
                        >
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="调用结束时间" prop="callerEndDate">
                        <el-date-picker
                            clearable
                            v-model="form.callerEndDate"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择调用结束时间"
                        >
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="调用数据量" prop="callerSize">
                        <el-input v-model="form.callerSize" placeholder="请输入调用数据量" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="调用耗时(毫秒)" prop="callerTime">
                        <el-input v-model="form.callerTime" placeholder="请输入调用耗时(毫秒)" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="信息记录" prop="MSG">
                        <el-input v-model="form.MSG" type="textarea" placeholder="请输入内容" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="状态" prop="STATUS">
                        <el-radio-group v-model="form.STATUS">
                            <el-radio
                                v-for="dict in ds_api_log_res_status"
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
                    <el-form-item label="备注" prop="REMARK">
                        <el-input v-model="form.REMARK" placeholder="请输入备注" />
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

    <!-- API服务调用日志详情对话框 -->
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
        <el-form ref="apiLogRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="调用url" prop="callerUrl">
                        <div>
                            {{ form.callerUrl }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="调用参数" prop="callerParams">
                        <div>
                            {{ form.callerParams }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="调用开始时间" prop="callerStartDate">
                        <el-date-picker
                            clearable
                            v-model="form.callerStartDate"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择调用开始时间"
                        >
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="调用结束时间" prop="callerEndDate">
                        <el-date-picker
                            clearable
                            v-model="form.callerEndDate"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择调用结束时间"
                        >
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="调用数据量" prop="callerSize">
                        <div>
                            {{ form.callerSize }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="调用耗时(毫秒)" prop="callerTime">
                        <div>
                            {{ form.callerTime }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="信息记录" prop="MSG">
                        <div>
                            {{ form.MSG }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="状态" prop="STATUS">
                        <dict-tag :options="ds_api_log_res_status" :value="form.STATUS" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="备注" prop="REMARK">
                        <div>
                            {{ form.REMARK }}
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
        listApiLog,
        getApiLog,
        delApiLog,
        addApiLog,
        updateApiLog
    } from '@/api/ds/apiLog/apiLog';

    const { proxy } = getCurrentInstance();
    const { ds_api_log_res_status } = proxy.useDict('ds_api_log_res_status');

    const apiLogList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '调用API服务Id', visible: true },
        { key: 2, label: '调用者id', visible: true },
        { key: 3, label: '调用者', visible: true },
        { key: 4, label: '调用者ip', visible: true },
        { key: 5, label: '调用url', visible: true },
        { key: 6, label: '调用参数', visible: true },
        { key: 7, label: '调用开始时间', visible: true },
        { key: 8, label: '调用结束时间', visible: true },
        { key: 9, label: '调用数据量', visible: true },
        { key: 10, label: '调用耗时(毫秒)', visible: true },
        { key: 11, label: '信息记录', visible: true },
        { key: 12, label: '状态', visible: true },
        { key: 13, label: '是否有效', visible: true },
        { key: 14, label: '删除标志', visible: true },
        { key: 15, label: '创建人', visible: true },
        { key: 16, label: '创建人id', visible: true },
        { key: 17, label: '创建时间', visible: true },
        { key: 18, label: '更新人', visible: true },
        { key: 19, label: '更新人id', visible: true },
        { key: 20, label: '更新时间', visible: true },
        { key: 21, label: '备注', visible: true }
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
        apiLogDetail: {},
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            apiId: null,
            callerId: null,
            createTime: null
        },
        rules: {}
    });

    const { queryParams, form, apiLogDetail, rules } = toRefs(data);

    /** 查询API服务调用日志列表 */
    function getList() {
        loading.value = true;
        listApiLog(queryParams.value).then((response) => {
            apiLogList.value = response.data.rows;
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
            ID: null,
            apiId: null,
            callerId: null,
            callerBy: null,
            callerIp: null,
            callerUrl: null,
            callerParams: null,
            callerStartDate: null,
            callerEndDate: null,
            callerSize: null,
            callerTime: null,
            MSG: null,
            STATUS: null,
            validFlag: null,
            delFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            REMARK: null
        };
        proxy.resetForm('apiLogRef');
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
        ids.value = selection.map((item) => item.ID);
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
        title.value = '新增API服务调用日志';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _ID = row.ID || ids.value;
        getApiLog(_ID).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改API服务调用日志';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _ID = row.ID || ids.value;
        getApiLog(_ID).then((response) => {
            form.value = response.data;
            openDetail.value = true;
            title.value = 'API服务调用日志详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['apiLogRef'].validate((valid) => {
            if (valid) {
                if (form.value.ID != null) {
                    updateApiLog(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addApiLog(form.value)
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
        const _IDs = row.ID || ids.value;
        proxy.$modal
            .confirm('是否确认删除API服务调用日志编号为"' + _IDs + '"的数据项？')
            .then(function () {
                return delApiLog(_IDs);
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
            'ds/apiLog/export',
            {
                ...queryParams.value
            },
            `apiLog_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
