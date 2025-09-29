<template>
    <!-- <div class="pagecont-top" v-show="showSearch">
        <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="资产名称" prop="name">
                <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入标签名称" clearable
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
    </div> -->
    <div class="justify-between mb15">
        <!--    <el-row :gutter="15" class="btn-style">-->
        <!--      <el-col :span="1.5">-->
        <!--        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['Tag:AttTag:add']"-->
        <!--                   @mousedown="(e) => e.preventDefault()">-->
        <!--          <i class="iconfont-mini icon-xinzeng mr5"></i>新增-->
        <!--        </el-button>-->
        <!--      </el-col>-->
        <!--&lt;!&ndash;      <el-col :span="1.5">&ndash;&gt;-->
        <!--&lt;!&ndash;        <el-button type="warning" plain @click="handleExport" v-hasPermi="['Tag:AttTag:export']"&ndash;&gt;-->
        <!--&lt;!&ndash;                   @mousedown="(e) => e.preventDefault()">&ndash;&gt;-->
        <!--&lt;!&ndash;          <i class="iconfont-mini icon-download-line mr5"></i>导出&ndash;&gt;-->
        <!--&lt;!&ndash;        </el-button>&ndash;&gt;-->
        <!--&lt;!&ndash;      </el-col>&ndash;&gt;-->
        <!--    </el-row>-->


        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="374px" v-loading="loading" :data="AttTagList" @selection-change="handleSelectionChange">
        <el-table-column width="50" label="编号" align="center" prop="id" v-if="getColumnVisibility(1)">
            <template #default="scope">
                {{ scope.row.id || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="资产名称" align="left" prop="name" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="getColumnVisibility(2)">
            <template #default="scope">
                {{ scope.row.name || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="描述" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="getColumnVisibility(3)" width="250">
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="资产类目" align="left" prop="catName" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="getColumnVisibility(4)">
            <template #default="scope">
                {{ scope.row.catName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="资产类型" align="center" prop="type" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="getColumnVisibility(5)">
            <template #default="scope">
                <dict-tag :options="da_asset_type" :value="scope.row.type" />
            </template>
        </el-table-column>
        <el-table-column label="主题名称" align="left" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="getColumnVisibility(6)">
            <template #default="scope">
                {{
                    scope.row.daAssetThemeRelList?.length
                        ? scope.row.daAssetThemeRelList.map(item => item.themeName).join(', ')
                        : '-'
                }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" label="创建人" :show-overflow-tooltip="{ effect: 'light' }"
            align="left" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || "-" }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" label="创建时间" align="center" prop="createTime" width="150">
            <template #default="scope">
                {{
                    parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
            <template #default="scope">
                <!--           <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
                <!--                      v-hasPermi="['da:asset:asset:edit']">修改</el-button>-->
                <!--           <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"-->
                <!--                      v-hasPermi="['da:asset:asset:remove']">删除</el-button>-->
                <el-button link type="danger" icon="Delete" style="padding-left: 14px" @click="handleDelete(scope.row)"
                    v-hasPermi="['att:Rel:atttagassetrel:remove']">移除</el-button>
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

    <!-- 添加或修改标签管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="AttTagRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="编码" prop="code">
                        <el-input v-model="form.code" placeholder="请输入编码" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入名称" />
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
                    <el-form-item label="类目编码" prop="catCode">
                        <el-input v-model="form.catCode" placeholder="请输入类目编码" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="资产数量" prop="aeestCount">
                        <el-input v-model="form.aeestCount" placeholder="请输入资产数量" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="状态" prop="status">
                        <el-input v-model="form.status" placeholder="请输入状态" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="扩展信息别名" prop="allas">
                        <el-input v-model="form.allas" placeholder="请输入扩展信息别名" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="近义词" prop="nearSynonyms">
                        <el-input v-model="form.nearSynonyms" placeholder="请输入近义词" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="同义词" prop="synonyms">
                        <el-input v-model="form.synonyms" placeholder="请输入同义词" />
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

    <!-- 标签管理详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="AttTagRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="编码" prop="code">
                        <div>
                            {{ form.code }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="名称" prop="name">
                        <div>
                            {{ form.name }}
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
                    <el-form-item label="类目编码" prop="catCode">
                        <div>
                            {{ form.catCode }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="资产数量" prop="aeestCount">
                        <div>
                            {{ form.aeestCount }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="状态" prop="status">
                        <div>
                            {{ form.status }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="扩展信息别名" prop="allas">
                        <div>
                            {{ form.allas }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="近义词" prop="nearSynonyms">
                        <div>
                            {{ form.nearSynonyms }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="同义词" prop="synonyms">
                        <div>
                            {{ form.synonyms }}
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

<script setup name="asset">
import { listAttTag, getAttTag, delAttTag, addAttTag, updateAttTag } from "@/api/att/tag/tag.js";
import { pageListByIds } from "@/api/da/asset/asset.js";
import { defineProps } from "vue";
import { delByTagIdAndAesstId } from "@/api/att/tag/tagAssetRel.js";
const { proxy } = getCurrentInstance();
import { useRoute } from 'vue-router';
const route = useRoute();
const { da_assets_status, da_asset_source, da_asset_type } = proxy.useDict(
    'da_assets_status',
    'da_asset_source', "da_asset_type"
);
const AttTagList = ref([]);

// 列显隐信息
const columns = ref([
    { key: 1, label: "编号", visible: true },
    { key: 2, label: "资产名称", visible: true },
    { key: 3, label: "描述", visible: true },
    { key: 4, label: "资产类目", visible: true },
    { key: 5, label: "资产类型", visible: true },
    { key: 6, label: "主题名称", visible: true },
    { key: 7, label: "创建人", visible: true },
    { key: 8, label: "创建时间", visible: true },
]);
const getColumnVisibility = (key) => {
    const column = columns.value.find(col => col.key === key);
    // 如果没有找到对应列配置，默认显示
    if (!column) return true;
    // 如果找到对应列配置，根据visible属性来控制显示
    return column.visible;
};

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const defaultSort = ref({ prop: 'create_time', order: 'desc' });
const props = defineProps({
    ids: { type: Object, default: null }
});
const data = reactive({
    AttTagDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        name: null,
        description: null,
        catCode: null,
        aeestCount: null,
        status: null,
        allas: null,
        nearSynonyms: null,
        synonyms: null,
        createTime: null,

    },
    rules: {}
});

const { queryParams, form, AttTagDetail, rules } = toRefs(data);

/** 查询标签管理列表 */
function getList() {
    loading.value = true;

    console.log(props.ids, 6666)
    if (route.query.id != null) {
        pageListByIds({ tagIdList: route.query.id }).then(response => {
            AttTagList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

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
        code: null,
        name: null,
        description: null,
        catCode: null,
        aeestCount: null,
        status: '1',
        allas: null,
        nearSynonyms: null,
        synonyms: null,
        validFlag: true,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        remark: null
    };
    proxy.resetForm("AttTagRef");
}

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}

/** 重置按钮操作 */
function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id);
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
    title.value = "添加标签管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getAttTag(_id).then(response => {
        form.value = response.data;
        open.value = true;
        title.value = "修改标签管理";
    });
}


/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getAttTag(_id).then(response => {
        form.value = response.data;
        openDetail.value = true;
        title.value = "标签管理详情";
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs["AttTagRef"].validate(valid => {
        if (valid) {
            if (form.value.id != null) {
                updateAttTag(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                }).catch(error => {
                });
            } else {
                addAttTag(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功");
                    open.value = false;
                    getList();
                }).catch(error => {
                });
            }
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    let map = {
        tagId: props.ids.id,
        assetId: row.id,
    }
    proxy.$modal.confirm('是否确认移除为"' + _ids + '"的数据项？').then(function () {
        return delByTagIdAndAesstId(map);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("移除成功");
    }).catch(() => {
    });
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download('att/AttTag/export', {
        ...queryParams.value
    }, `AttTag_${new Date().getTime()}.xlsx`)
}

watch(
    () => props.ids,
    (newId) => {
        getList();
    },
    { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);


</script>
