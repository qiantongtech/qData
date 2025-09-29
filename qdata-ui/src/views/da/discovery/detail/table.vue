<template>
    <div class="justify-between mb15">
        <el-form style="margin-bottom: -12px; margin-left: -12px" class="btn-style" :model="queryParams" ref="queryRef"
            :inline="true" label-width="75px" @submit.prevent>
            <el-form-item label="关键字" prop="keyword">
                <el-input class="el-form-input-width" v-model="queryParams.keyword" placeholder="请输入英文名称或中文名称" clearable
                    @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                </el-button>
                <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                </el-button>
            </el-form-item>
        </el-form>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="60vh" v-loading="loading" :data="daDiscoveryTableList" :default-sort="defaultSort">
        <el-table-column v-if="getColumnVisibility(1)" label="英文名称" align="left" prop="tableName"
            :show-overflow-tooltip="{ effect: 'light' }" width="350">
            <template #default="scope">
                <span style="cursor: pointer; color: #1684fc" @click="handltableNamee(scope.row)">
                    {{ scope.row.tableName || '-' }}
                </span>
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" label="中文名称" align="left" prop="tableComment"
            :show-overflow-tooltip="{ effect: 'light' }" width="350">
            <template #default="scope">
                {{ scope.row.tableComment || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" label="行数" align="center" prop="dataCount">
            <template #default="scope">
                {{ scope.row.dataCount || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="字段数" align="center" width="80" prop="fieldCount">
            <template #default="scope">
                {{ scope.row.fieldCount || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" label="表结构变更标识" align="center" prop="changeFlag">
            <template #default="scope">
                <dict-tag v-if="scope.row.ignoreFlag == 0" :options="da_discovery_table_change_flag"
                    :value="scope.row.changeFlag" />
                <span v-if="scope.row.ignoreFlag == 1">-</span>
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" label="状态" align="center" prop="status">
            <template #default="scope">
                <dict-tag v-if="scope.row.ignoreFlag == 0" :options="da_discovery_table_change_status"
                    :value="scope.row.status" />
                <dict-tag v-if="scope.row.ignoreFlag == 1" :options="da_discovery_table_change_ignore_flag"
                    :value="scope.row.ignoreFlag" />
            </template>
        </el-table-column>
        <el-table-column label="操作" v-if="getColumnVisibility(7)" align="center" class-name="small-padding fixed-width"
            fixed="right" width="240">
            <template #default="scope">
                <el-button v-if="scope.row.status == 1 && scope.row.ignoreFlag == 0" link type="primary"
                    @click="getAssetThemeList(scope.row)"
                    v-hasPermi="['da:discoveryTable:edit']">提交</el-button>
                <el-button v-if="scope.row.status == 2 || scope.row.ignoreFlag == 1" link type="primary"
                    @click="handleUpdate(scope.row, 0, 1)"
                    v-hasPermi="['da:discoveryTable:edit']">撤回</el-button>
                <el-button link type="primary" @click="handleUpdate(scope.row, 1, 0)"
                    v-if="scope.row.status == 1 && scope.row.ignoreFlag == 0"
                    v-hasPermi="['da:discoveryTable:remove']">忽略</el-button>
            </template>
        </el-table-column>

        <template #empty>
            <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
            </div>
        </template>
    </el-table>
    <el-dialog title="选择资产归属" v-model="openAsset" class="warn-dialog-0314" width="500px"
        :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title"> 选择资产归属 </span>
        </template>
        <el-form ref="assetRef" :model="assetForm" :rules="assetRules" label-width="80px" @submit.prevent>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="中文名称" prop="assetName">
                        <el-input v-model="assetForm.assetName" placeholder="请输入中文名称" clearable />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="主题名称" prop="themeId">
                        <el-select v-model="assetForm.themeId" placeholder="请选择主题名称">
                            <el-option v-for="dict in themeList" :key="dict.id" :label="dict.name"
                                :value="dict.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="资产类目" prop="assetCode">
                        <el-tree-select filterable v-model="assetForm.assetCode" :data="assetCategoryList"
                            :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id"
                            placeholder="请选择所属类目" check-strictly />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="openAsset = false">取 消</el-button>
                <el-button type="primary" size="mini" @click="assetSubmit">确 定</el-button>
            </div>
        </template>
    </el-dialog>
    <TableInfoDialog :daDiscoveryTaskDetail="daDiscoveryTaskDetail" :visible="open" :title="title"
        @update:visible="open = $event" :data="form" />
</template>

<script setup name="DaDiscoveryTable">
import {
    getDaDiscoveryTable,
    getDaDiscoveryTableList,
    commitOrRevokeDiscoveryInfo
} from '@/api/da/discovery/discoveryTable';
import { getThemeList } from '@/api/att/theme/theme.js';
import { listAttAssetCat } from '@/api/att/cat/assetCat/assetCat.js';
import { getToken } from '@/utils/auth.js';
import TableInfoDialog from './tableDetail.vue';
import { useRoute } from 'vue-router';
const route = useRoute();
let assetId = route.query.id || 1;
const { proxy } = getCurrentInstance();
const daDiscoveryTableList = ref([]);
const {
    da_discovery_table_change_flag,
    da_discovery_table_change_status,
    da_discovery_table_change_ignore_flag,
    da_discovery_task_status
} = proxy.useDict(
    'da_discovery_table_change_flag',
    'da_discovery_table_change_status',
    'da_discovery_table_change_ignore_flag',
    'da_discovery_task_status'
);
const emit = defineEmits(['update']);
const row = ref({});
const themeList = ref([]);
const assetCategoryList = ref([]);

// 列显隐信息
const columns = ref([
    { key: 1, label: '英文名称', visible: true },
    { key: 2, label: '中文名称', visible: true },
    { key: 3, label: '行数', visible: true },
    { key: 4, label: '字段数', prop: 'tableComment', visible: true },
    { key: 5, label: '表结构变更标识', prop: 'tableComment', visible: true },
    { key: 6, label: '状态', prop: 'tableComment', visible: true },
    { key: 7, label: '操作', visible: true },
]);

const assetRef = ref(null);
const assetForm = ref({
    assetName: null,
    themeId: null,
    assetCode: null
});

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskId: null,
        tableName: null,
        tableComment: null,
        dataCount: null,
        fieldCount: null,
        changeFlag: null,
        status: null,
        ignoreFlag: null,
        createTime: null,
        keyword: null
    },
    rules: {},
    assetRules: {
        assetName: [{ required: true, validator: validateAssetName, trigger: 'blur' }],
        themeId: [{ required: true, validator: validateThemeId, trigger: 'change' }],
        assetCode: [{ required: true, validator: validateAssetCode, trigger: 'change' }]
    }
});

const { queryParams, form, rules, assetRules } = toRefs(data);

function validateAssetName(rule, value, callback) {
    if (!value) {
        callback(new Error('请输入中文名称'));
    } else {
        callback();
    }
}

function validateThemeId(rule, value, callback) {
    if (!value) {
        callback(new Error('请选择主题名称'));
    } else {
        callback();
    }
}

function validateAssetCode(rule, value, callback) {
    if (!value) {
        callback(new Error('请选择资产类目'));
    } else {
        callback();
    }
}

async function getAssetThemeList(rowTwo) {
    const response = await getThemeList();
    themeList.value = response.data;
    const res = await listAttAssetCat();
    assetCategoryList.value = proxy.handleTree(res.data, 'id', 'parentId');
    assetCategoryList.value = [
        {
            name: '资产类目',
            value: '',
            children: assetCategoryList.value
        }
    ];
    assetForm.value = {
        assetName: rowTwo.tableComment,
        themeId: null,
        assetCode: null
    };
    openAsset.value = true;
    row.value = rowTwo;
}

function assetSubmit() {
    assetRef.value.validate((valid) => {
        if (valid) {
            handleUpdate(row.value, '', 2);
        } else {
            return false;
        }
    });
}

function removeMetadataFields(row) {
    // 删除指定字段
    const fieldsToRemove = [
        'creatorId',
        'createBy',
        'createTime',
        'updaterId',
        'updateBy',
        'updateTime'
    ];

    fieldsToRemove.forEach((field) => {
        delete row[field];
    });

    return row;
}
/** 修改按钮操作 */
function handleUpdate(row, ignoreFlag, status) {
    let data = removeMetadataFields(row);
    const updatedData = {
        ...(ignoreFlag !== undefined && ignoreFlag !== null ? { ignoreFlag } : {}),
        ...(status !== undefined && status !== null ? { status } : {})
    };
    //暂时写死，等前端
    updatedData.id = data.id;
    updatedData.catCode = assetForm.value.assetCode;
    updatedData.themeId = assetForm.value.themeId;
    updatedData.assetName = assetForm.value.assetName;
    // 调用 updateDaDiscoveryTable 函数
    commitOrRevokeDiscoveryInfo(updatedData)
        .then((response) => {
            proxy.$modal.msgSuccess('操作成功');
            open.value = false;
            openAsset.value = false;
            getList();
            // 触发事件，向父组件传递数据
            emit('updateData', updatedData); // 这里 emit 事件
        })
        .catch((error) => {
            // 错误处理
            console.error('操作失败:', error);
        });
}

const getColumnVisibility = (key) => {
    const column = columns.value.find((col) => col.key === key);
    // 如果没有找到对应列配置，默认显示
    if (!column) return true;
    // 如果找到对应列配置，根据visible属性来控制显示
    return column.visible;
};
const props = defineProps({
    daDiscoveryTaskDetail: { type: Object, default: () => ({}) }
});
const open = ref(false);
const openAsset = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const defaultSort = ref({ prop: 'createTime', order: 'desc' });
const router = useRouter();

/*** 用户导入参数 */
const upload = reactive({
    // 是否显示弹出层（用户导入）
    open: false,
    // 弹出层标题（用户导入）
    title: '',
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的用户数据
    updateSupport: 0,
    // 设置上传的请求头部
    headers: { Authorization: 'Bearer ' + getToken() },
    // 上传的地址
    url: import.meta.env.VITE_APP_BASE_API + '/da/daDiscoveryTable/importData'
});

/** 查询数据发现库信息列表 */
function getList() {
    loading.value = true;
    getDaDiscoveryTableList({
        taskId: assetId,
        keyword: queryParams.value.keyword
    }).then((response) => {
        // 假设 response.data 是数组
        daDiscoveryTableList.value = response.data;

        // 如果 response.data 是数组，获取数组长度；如果是对象，获取对象键数量
        total.value = Array.isArray(response.data)
            ? response.data.length // 如果是数组，获取长度
            : Object.keys(response.data).length; // 如果是对象，获取键的数量

        loading.value = false;
    });
}

// 表单重置
function reset() {
    form.value = {
        id: null,
        taskId: null,
        tableName: null,
        tableComment: null,
        dataCount: null,
        fieldCount: null,
        changeFlag: null,
        status: null,
        ignoreFlag: null,
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
    proxy.resetForm('daDiscoveryTableRef');
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

/** 表字段詳情 */
function handltableNamee(row) {
    title.value = row.tableComment || '表信息';
    console.log('🚀 ~ handleUpdate ~ form.value :', form.value);
    const _id = row.id || ids.value;
    getDaDiscoveryTable(_id).then((response) => {
        form.value = response.data;
        console.log('🚀 ~ getDaDiscoveryTable ~   form.value:', form.value);
        open.value = true;
    });
}

getList();
</script>
<style scoped lang="scss"></style>
<style lang="scss">
.warn-dialog-0314 {
    .el-dialog__body {
        overflow: auto;
        height: 200px !important;
        padding: 20px 40px !important;
    }
}
</style>
