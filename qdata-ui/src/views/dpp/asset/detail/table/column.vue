<template>
    <!-- 资产字段 tab -->
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <!--      <el-col :span="1.5">-->
            <!--        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['da:assetColumn:assetcolumn:add']"-->
            <!--                   @mousedown="(e) => e.preventDefault()">-->
            <!--          <i class="iconfont-mini icon-xinzeng mr5"></i>新增-->
            <!--        </el-button>-->
            <!--      </el-col>-->
            <!--      <el-col :span="1.5">-->
            <!--        <el-button type="warning" plain @click="handleExport" v-hasPermi="['da:assetColumn:assetcolumn:export']"-->
            <!--                   @mousedown="(e) => e.preventDefault()">-->
            <!--          <i class="iconfont-mini icon-download-line mr5"></i>导出-->
            <!--        </el-button>-->
            <!--      </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
    </div>
    <el-table stripe v-loading="loading" :data="daAssetColumnList" @selection-change="handleSelectionChange"
        :default-sort="defaultSort" @sort-change="handleSortChange">
        <!--        <el-table-column type="selection" width="55"  align="left" />-->
        <el-table-column v-if="columns[0].visible" label="中文名称" align="left" prop="columnComment"
            :show-overflow-tooltip="{ effect: 'light' }" width="185">
            <template #default="scope">
                {{ scope.row.columnComment || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[1].visible" label="英文名称" align="left" prop="columnName"
            :show-overflow-tooltip="{ effect: 'light' }" width="180">
            <template #default="scope">
                {{ scope.row.columnName || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[2].visible" label="字段类型" align="left" prop="columnType"
            :show-overflow-tooltip="{ effect: 'light' }" width="140">
            <template #default="scope">
                {{ scope.row.columnType || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[3].visible" label="字段长度" align="left" prop="columnLength"
            :show-overflow-tooltip="{ effect: 'light' }" width="80">
            <template #default="scope">
                {{ scope.row.columnLength || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[4].visible" label="小数位" align="left" prop="columnScale" width="80"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.columnScale || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[5].visible" label="是否可空" align="left" prop="nullableFlag" width="80">
            <template #default="scope">
                <el-switch v-model="scope.row.nullableFlag" :active-value="'1'" :inactive-value="'0'" disabled />
            </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" label="是否主键" width="100" align="left" prop="pkFlag">
            <template #default="scope">
                <el-switch v-model="scope.row.pkFlag" :active-value="'1'" :inactive-value="'0'" disabled />
            </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" label="描述" align="left" prop="description" width="260"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" label="敏感等级" align="left" prop="sensitiveLevelName" width="100">
            <template #default="scope">
                {{ scope.row.sensitiveLevelName || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[9].visible" label="关联代码表" width="200" align="left" prop="dataElemCodeFlag"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                <!--                  <el-switch v-show="!scope.row.dataElemCodeFlag" v-model="scope.row.dataElemCodeFlag" :active-value="'1'" :inactive-value="'0'" disabled />-->
                <span v-show="scope.row.dataElemCodeFlag">
                    {{ scope.row.dataElemCodeName || '-' }}
                </span>
            </template>
        </el-table-column>

        <el-table-column v-if="columns[10].visible" label="关联数据元" align="left" prop="relDataElmeFlag" width="200"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                <span v-show="scope.row.dataElemCodeFlag">
                    {{ scope.row.relDataElmeName || '-' }}
                </span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[11].visible" width="120" label="创建人" align="left" prop="createBy"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[12].visible" label="创建时间" align="left" prop="createTime" width="150"
            sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']">
            <template #default="scope">
                <span>{{
                    parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[13].visible" label="备注" width="200" align="left" prop="remark"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>

        <el-table-column label="操作" v-if="columns[14].visible" align="left" class-name="small-padding fixed-width"
            fixed="right" width="100">
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                    v-hasPermi="['da:assetColumn:edit']">修改</el-button>
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

    <!-- 新增或修改数据资产对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="daAssetRef" :model="form" :rules="rules" label-width="100px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="中文名称" prop="columnComment">
                        <el-input v-model="form.columnComment" placeholder="请输入中文名称" disabled />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="英文名称" prop="columnName">
                        <el-input v-model="form.columnName" placeholder="请输入英文名称" disabled />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="字段类型" prop="columnType">
                        <el-input v-model="form.columnType" placeholder="请输入字段类型" disabled />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="字段长度" prop="columnLength">
                        <el-input v-model="form.columnLength" placeholder="请输入字段长度" disabled />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="小数位" prop="columnScale">
                        <el-input-number :step="1" step-strictly v-model="form.columnScale" style="width: 100%"
                            controls-position="right" :min="0" :max="100" placeholder="请输入小数长度" disabled />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否可空" prop="nullableFlag">
                        <el-radio-group v-model="form.nullableFlag">
                            <el-radio v-for="dict in dp_model_column_pk_flag" :key="dict.value" :value="dict.value"
                                disabled>{{ dict.label }}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="是否主键" prop="pkFlag">
                        <el-radio-group v-model="form.pkFlag">
                            <el-radio v-for="dict in dp_model_column_pk_flag" :key="dict.value" :value="dict.value"
                                disabled>
                                {{ dict.label }}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="敏感等级" prop="sensitiveLevelId">
                        <el-select v-model="form.sensitiveLevelId" filterable clearable placeholder="请选择敏感等级"
                            @change="val => form.sensitiveLevelId = val ?? null">
                            <el-option v-for="item in daSensitiveLevelList" :key="item.id" :label="item.sensitiveLevel"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联代码表" prop="dataElemCodeFlag">
                        <el-radio-group v-model="form.dataElemCodeFlag">
                            <el-radio v-for="dict in dp_model_column_pk_flag" :key="dict.value" :value="dict.value">
                                {{ dict.label }}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="选择代码表" prop="dataElemCodeId">
                        <el-select v-model="form.dataElemCodeId" :disabled="form.dataElemCodeFlag == '0'" clearable
                            filterable placeholder="选择代码表">
                            <el-option v-for="item in codeTableList" :key="item.id" :label="item.name" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联数据元" prop="relDataElmeFlag">
                        <el-radio-group v-model="form.relDataElmeFlag">
                            <el-radio v-for="dict in dp_model_column_pk_flag" :key="dict.value" :value="dict.value">
                                {{ dict.label }}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="选择数据元" prop="elementId">
                        <el-select v-model="form.elementId" multiple :disabled="form.relDataElmeFlag == '0'" clearable
                            filterable placeholder="请选择数据元">
                            <el-option v-for="item in elementList" :key="item.id" :label="item.name" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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

    <!-- 数据资产详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="daAssetRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="中文名称" prop="columnComment">
                        <div>
                            {{ form.columnComment }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="英文名称" prop="columnName">
                        <div>
                            {{ form.columnName }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="字段类型" prop="columnType">
                        <div>
                            {{ form.columnType }}
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
                    <el-form-item label="是否可空" prop="nullableFlag">
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
                    <el-form-item label="敏感等级" prop="sensitiveLevelName">
                        <div>
                            {{ form.sensitiveLevelName }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="关联代码表" prop="dataElemCodeFlag">
                        <div>
                            {{ form.dataElemCodeFlag }}
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
                    <el-form-item label="描述" prop="description">
                        <div>
                            {{ form.description }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="字段描述" prop="remark">
                        <div>
                            {{ form.remark }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="small" @click="cancel">关 闭</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="ComponentOne">
import {
    listDaAsset,
    getDaAsset,
    delDaAsset,
    addDaAsset,
    updateDaAsset
} from '@/api/da/asset/asset';
import {
    listDaAssetColumn,
    getDaAssetColumn,
    updateDaAssetColumn
} from '@/api/da/asset/assetColumn.js';
import { listDaSensitiveLevel } from '@/api/da/security/sensitiveLevel/sensitiveLevel';
import { listDpDataElem } from '@/api/dp/dataElem/dataElem';
import { useRoute } from 'vue-router';
import { ref } from "vue";

const { proxy } = getCurrentInstance();
const { column_type, dp_model_column_pk_flag, dp_model_column_nullable_flag } = proxy.useDict(
    'column_type',
    'dp_model_column_pk_flag',
    'dp_model_column_nullable_flag'
);

const daAssetColumnList = ref([]);
const daSensitiveLevelList = ref([]);
const codeTableList = ref([]); //代码表
const elementList = ref([]); //数据元
const defaultSort = ref({ columnKey: 'create_time', order: 'desc' });
// 列显隐信息
const columns = ref([
    { key: 0, label: '中文名称', visible: true },
    { key: 1, label: '英文名称', visible: true },
    { key: 2, label: '字段类型', visible: true },
    { key: 3, label: '字段长度', visible: true },
    { key: 4, label: '小数位', visible: true },
    { key: 5, label: '是否可空', visible: true },
    { key: 6, label: '是否主键', visible: true },
    { key: 7, label: '描述', visible: true },
    { key: 8, label: '敏感等级', visible: true },
    { key: 9, label: '关联代码表', visible: true },
    { key: 10, label: '关联数据元', visible: true },
    { key: 11, label: '创建人', visible: true },
    { key: 12, label: '创建时间', visible: true },
    { key: 13, label: '备注', visible: true },
    { key: 14, label: '操作', visible: true },
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
const data = reactive({
    daAssetDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        catCode: null,
        themeId: null,
        datasourceId: null,
        tableName: null,
        tableComment: null,
        dataCount: null,
        fieldCount: null,
        STATUS: null,
        description: null,
        createTime: null
    },
    rules: {
        // columnComment: [{ required: true, message: '请输入中文名称', trigger: 'blur' }],
        // columnName: [{ required: true, message: '请输入英文名称', trigger: 'blur' }],
        dataElemCodeId: [{ required: false, message: '请选择代码表', trigger: 'blur' }],
        elementId: [{ required: false, message: '请选择数据元', trigger: 'blur' }],
        // pkFlag: [{ required: true, message: '请选择是否主键', trigger: 'blur' }],
        // nullableFlag: [{ required: true, message: '请选择是是否必填', trigger: 'blur' }],
        // dataElemCodeFlag: [{ required: true, message: '请选择是是否必填', trigger: 'blur' }],
        // relDataElmeFlag: [
        //     { required: true, message: '请选择是是否关联数据元', trigger: 'blur' }
        // ],
        // relCleanFlag: [
        //     { required: true, message: '请选择是是否关联清洗规则', trigger: 'blur' }
        // ],
        relAuditFlag: [{ required: true, message: '请选择是是否关联稽查规则', trigger: 'blur' }]
    }
});

const { queryParams, form, daAssetDetail, rules } = toRefs(data);
const route = useRoute();
let assetId = route.query.id || 1;
watch(
    () => route.query.id,
    (newId) => {
        assetId = newId || 1;
        getList();
    },
    { immediate: true }
);

/** 查询数据资产列表 */
function getList() {
    loading.value = true;
    queryParams.value.assetId = assetId;
    listDaAssetColumn(queryParams.value).then((response) => {
        daAssetColumnList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}

/** 获取敏感等级列表 */
function getDaSensitiveLevelList() {
    listDaSensitiveLevel().then((response) => {
        daSensitiveLevelList.value = response.data.rows;
        //将id转换为String类型
        daSensitiveLevelList.value.forEach((item) => {
            item.id = item.id.toString();
        });
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
        catCode: null,
        themeId: null,
        datasourceId: null,
        tableName: null,
        tableComment: null,
        dataCount: null,
        fieldCount: null,
        STATUS: null,
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
    proxy.resetForm('daAssetRef');
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
    queryParams.value.orderByColumn = column?.columnKey || prop;
    queryParams.value.isAsc = column.order;
    getList();
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = '新增数据资产';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDaAssetColumn(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = '修改数据资产';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDaAsset(_id).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = '数据资产详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['daAssetRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                delete form.value.updateTime;

                updateDaAssetColumn(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDaAsset(form.value)
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
        .confirm('是否确认删除数据资产编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delDaAsset(_ids);
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
        'da/asset/export',
        {
            ...queryParams.value
        },
        `daAsset_${new Date().getTime()}.xlsx`
    );
}

/** 获取数据元列表 */
function getElementList() {
    listDpDataElem({ type: 1 }).then((response) => {
        elementList.value = response.data.rows;
    });
}
/** 获取代码表列表 */
function getCodeTableList() {
    listDpDataElem({ type: 2 }).then((response) => {
        codeTableList.value = response.data.rows;
        codeTableList.value.forEach((item) => {
            item.id = item.id.toString();
        });
    });
}

getElementList();
getCodeTableList();
getDaSensitiveLevelList();
</script>
