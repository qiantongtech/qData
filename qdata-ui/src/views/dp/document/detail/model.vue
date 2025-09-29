<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dp:model:add']"
                    @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                        <el-button type="primary" :disabled="single" plain @click="handleMaterialization"
                            v-hasPermi="['dp:model:edit']" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-xiugai--copy mr5"></i>物化
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" plain :disabled="multiple" @click="handleDelete"
                            v-hasPermi="['dp:model:remove']" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
                        </el-button>
                    </el-col> -->
            <!-- <el-col :span="1.5">
                <el-button type="info" plain @click="handleImport" v-hasPermi="['dp:model:export']"
                  @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="warning" plain @click="handleExport" v-hasPermi="['dp:model:export']"
                  @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-download-line mr5"></i>导出
                </el-button>
              </el-col> -->
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="400" v-loading="loading" :data="dpModelList" @selection-change="handleSelectionChange"
        :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column v-if="getColumnVisibility(0)" label="编号" width="50" align="left" prop="id" />
        <el-table-column v-if="getColumnVisibility(1)" label="英文名称" :show-overflow-tooltip="{ effect: 'light' }"
            align="left" prop="modelName" width="200">
            <template #default="scope">
                {{ scope.row.modelName || "-" }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" label="中文名称" :show-overflow-tooltip="{ effect: 'light' }"
            align="left" prop="modelComment" width="180">
            <template #default="scope">
                {{ scope.row.modelComment || "-" }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" label="逻辑模型类目" width="100"
            :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="catName">
            <template #default="scope">
                {{ scope.row.catName || "-" }}
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(10)" label="创建人" align="left" prop="createBy" width="120">
            <template #default="scope">
                {{ scope.row.createBy || "-" }}
            </template>
        </el-table-column>
        <el-table-column label="创建时间" v-if="getColumnVisibility(11)" align="left" prop="createTime" width="180">
            <template #default="scope">
                <span>{{
                    parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                    }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="状态" width="120" align="left" prop="status">
            <template #default="scope">
                <!-- {{ dp_model_status }}
                                <dict-tag :options="dp_model_status" :value="scope.row.status" /> -->
                <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
                    inactive-value="0" @change="
                        (e) => handleStatusChange(scope.row.id, scope.row, e)
                    " />
            </template>
        </el-table-column>
        <el-table-column label="备注" v-if="getColumnVisibility(5)" align="left" prop="remark"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <!-- <el-table-column v-if="getColumnVisibility(5)" width="130" label="创建方式" :show-overflow-tooltip="{effect: 'light'}"
              align="left" prop="createType">
              <template #default="scope">
                <dict-tag :options="dp_model_create_type" :value="scope.row.createType" />
              </template>
            </el-table-column> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                    :disabled="scope.row.status == 1" v-hasPermi="['dp:model:edit']">修改</el-button>
                <el-button link type="danger" icon="Delete" :disabled="scope.row.status == 1"
                    @click="handleDelete(scope.row)" v-hasPermi="['dp:model:remove']">删除</el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                    v-hasPermi="['dp:model:edit']">详情</el-button>
                <!-- <el-button link type="primary" icon="view" @click="routeTo('/dp/model/dpModelDetail', scope.row)"
                  v-hasPermi="['dp:model:edit']">复杂详情</el-button> -->
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

    <!-- 逻辑模型详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
                <el-icon size="20" style="color: #909399; font-size: 16px">
                    <InfoFilled />
                </el-icon>
            </span>
        </template>
        <el-form ref="dpModelRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="英文名称" prop="modelName">
                        <div>
                            {{ form.modelName }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="中文名称" prop="modelComment">
                        <div>
                            {{ form.modelComment }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="类目编码" prop="catCode">
                        <div>
                            {{ form.catCode }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="状态" prop="status">
                        <dict-tag :options="dp_model_status" :value="form.status" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="创建方式" prop="createType">
                        <dict-tag :options="dp_model_create_type" :value="form.createType" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="联系人" prop="contact">
                        <div>
                            {{ form.contact }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="联系电话" prop="contactNumber">
                        <div>
                            {{ form.contactNumber }}
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
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">关 闭</el-button>
            </div>
        </template>
    </el-dialog>
    <my-form-dialog v-model:visible="open" :title="title" @submit="handleFormSubmit" :deptList="deptList"
        :column_type="column_type" :userList="userList" @confirm="submitForm" :dataList="dataList"
        :catCode="queryParams.catCode" :deptOptions="deptOptions" :documentId="form.documentId" , type="3" />
    <MaterializationDialog :title="title" :visible="Materialization"
        @update:dialogFormVisible="Materialization = $event" :ids="ids" @confirm="getList"
        :documentId="form.documentId" />
</template>
<script setup name="DpModel">
import { deptUserTree } from "@/api/system/system/user.js";
import { deptTreeSelectNoPermi } from "@/api/system/system/user.js";
import DeptTree from "@/components/DeptTree";
import MyFormDialog from "@/views/dp/model/components/add.vue";
import MaterializationDialog from "@/views/dp/model/detail/materialization.vue";
import {
    listDpModel,
    getDpModel,
    delDpModel,
    delDpModelColumn,
    addDpModel,
    updateDpModelColumn,
    updateDpModel,
    listAttModelCat,
    dpModelColumn,
    updateStatusDpDataModel,
} from "@/api/dp/model/model";
import { getToken } from "@/utils/auth.js";
import { ref, reactive, getCurrentInstance } from "vue";
import { useRoute } from 'vue-router';
const route = useRoute();
const { proxy } = getCurrentInstance();
const { dp_model_status, dp_model_create_type } = proxy.useDict(
    "dp_model_status",
    "dp_model_create_type"
);
const dpModelList = ref([]);
const deptList = ref([]);
const userList = ref([]);
const deptOptions = ref(undefined);

const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
let Materialization = ref(false);
const startResize = (event) => {
    isResizing.value = true;
    startX = event.clientX;
    document.addEventListener("mousemove", updateResize);
    document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
    isResizing.value = false;
    document.removeEventListener("mousemove", updateResize);
    document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
    if (isResizing.value) {
        const delta = event.clientX - startX; // 计算鼠标移动距离
        leftWidth.value += delta; // 修改左侧宽度
        startX = event.clientX; // 更新起始位置
        // 使用 requestAnimationFrame 来减少页面重绘频率
        requestAnimationFrame(() => { });
    }
};
const selectable = (row) => {
    return row.status != 0;
};

/** 查询部门下拉树结构 */
function getDeptTree() {
    listAttModelCat().then((response) => {
        deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
        deptOptions.value = [
            {
                name: "逻辑模型类目",
                value: "",
                id: 0,
                children: deptOptions.value,
            },
        ];
    });
    // 部门
    deptTreeSelectNoPermi().then((response) => {
        deptList.value = response.data;
    });
    deptUserTree().then((res) => {
        userList.value = res.data;
        console.log("userList", userList.value);
    });
}
// 列显隐信息
const columns = ref([
    { key: 0, label: "编号", visible: true },
    { key: 1, label: "英文名称", visible: true },
    { key: 2, label: "中文名称", visible: true },
    { key: 3, label: "逻辑模型类目", visible: true },
    { key: 10, label: "创建人", visible: true },
    { key: 11, label: "创建时间", visible: true },
    { key: 4, label: "状态", visible: true },
    { key: 5, label: "备注", visible: true },
]);

const getColumnVisibility = (key) => {
    const column = columns.value.find((col) => col.key === key);
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
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

/*** 用户导入参数 */
const upload = reactive({
    // 是否显示弹出层（用户导入）
    open: false,
    // 弹出层标题（用户导入）
    title: "",
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的用户数据
    updateSupport: 0,
    // 设置上传的请求头部
    headers: { Authorization: "Bearer " + getToken() },
    // 上传的地址
    url: import.meta.env.VITE_APP_BASE_API + "/dp/model/importData",
});

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
    const text = e === "1" ? "启用" : "禁用";
    proxy.$modal
        .confirm('确认要"' + text + '","' + row.modelComment + '"逻辑模型吗？')
        .then(function () {
            updateStatusDpDataModel(id, row.status).then((response) => {
                proxy.$modal.msgSuccess("操作成功");
            });
        })
        .catch(function () {
            row.status = row.status === "1" ? "0" : "1";
        });
}

const data = reactive({
    form: { status: "1" },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        modelName: null,
        modelComment: null,
        catCode: null,
        documentId: null,
    },
    rules: {
        modelName: [
            { required: true, message: "模型编码不能为空", trigger: "blur" },
        ],
        modelComment: [
            { required: true, message: "模型名称不能为空", trigger: "blur" },
        ],
        catCode: [{ required: true, message: "类目编码不能为空", trigger: "blur" }],
        status: [{ required: true, message: "状态不能为空", trigger: "change" }],
        createType: [
            { required: true, message: "创建方式不能为空", trigger: "change" },
        ],
    },
});

const { queryParams, form, rules } = toRefs(data);
function handleNodeClick(data) {
    queryParams.value.catCode = data.code;
    queryParams.value.pageNum = 1;
    handleQuery();
}
/** 查询逻辑模型列表 */
function getList() {
    if (!queryParams.value.documentId) {
        queryParams.value.documentId = route.query.id;
    }
    loading.value = true;
    listDpModel(queryParams.value).then((response) => {
        dpModelList.value = response.data.rows;
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
        modelName: null,
        modelComment: null,
        catCode: null,
        status: null,
        createType: null,
        datasourceId: null,
        contact: null,
        contactNumber: null,
        description: null,
        validFlag: null,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        remark: null,
        documentId: null,
    };
    proxy.resetForm("dpModelRef");
}

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
const DeptTreeRef = ref(null);
/** 重置按钮操作 */
function resetQuery() {
    if (DeptTreeRef.value?.resetTree) {
        DeptTreeRef.value.resetTree();
    }
    queryParams.value.catCode = "";
    queryParams.value.pageNum = 1;
    reset();
    proxy.resetForm("queryRef");
    handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
    console.log("selection", selection);
    ids.value = selection.map((item) => item.id);
    console.log("selection.length ", selection.length);
    single.value = selection.length == 0 ? true : false;
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
    dataList.value = {};
    reset();
    open.value = true;
    title.value = "新增逻辑模型";
}
let dataList = ref({});
/** 修改按钮操作 */
function handleUpdate(row) {
    console.log("row", row);
    reset();
    const _ID = row.id || ids.value;
    getDpModel(_ID).then((response) => {
        dataList.value = response.data;
        open.value = true;
        title.value = "修改逻辑模型";
    });
}

/** 物化按钮操作 */
function handleMaterialization() {
    const _ID = ids.value;
    // getDpModel(_ID).then(response => {
    //   form.value = response.data;

    // });
    Materialization.value = true;
    title.value = "逻辑物化";
}
/** 详情按钮操作 */
function handleDetail(row) {
    routeTo("/dp/model/detail", row);
}

/** 提交按钮 */
function submitForm(obj) {
    console.log("obj", obj);
    if (obj.form.id != null) {

        updateDpModel({ ...obj.form, documentId: queryParams.value.documentId })
            .then((response) => {
                updateDpModelColumn(obj.tableData).then((response) => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            })
            .catch((error) => { });
    } else {
        addDpModel({ ...obj.form, documentId: queryParams.value.documentId })
            .then((response) => {
                const id = response.data;
                const updatedTableData = obj.tableData.map((item) => ({
                    ...item,
                    modelId: id,
                }));
                dpModelColumn(updatedTableData)
                    .then((dpModelColumnResponse) => {
                        proxy.$modal.msgSuccess("新增成功");
                        open.value = false;
                        getList();
                    })
                    .catch((dpModelColumnError) => { });
            })
            .catch((error) => {
                console.error("新增失败:", error);
            });
    }
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _IDs = row.id || ids.value;
    proxy.$modal
        .confirm('是否确认删除逻辑模型编号为"' + _IDs + '"的数据项？')
        .then(function () {
            return delDpModelColumn(_IDs);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess("删除成功");
        })
        .catch(() => { });
}

function routeTo(link, row) {
    if (link !== "" && link.indexOf("http") !== -1) {
        window.location.href = link;
        return;
    }
    if (link !== "") {
        if (link === router.currentRoute.value.path) {
            window.location.reload();
        } else {
            router.push({
                path: link,
                query: {
                    id: row.id,
                },
            });
        }
    }
}
queryParams.value.documentId = route.query.id;
getList();
getDeptTree();
</script>
<style scoped lang="scss">
::v-deep {
    .selectlist .el-tag.el-tag--info {
        background: #f3f8ff !important;
        border: 0px solid #6ba7ff !important;
        color: #2666fb !important;
    }
}
</style>
