<template>
    <div class="app-container" ref="app-container">
        <el-container style="90%">
            <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入标准类目'"
                @node-click="handleNodeClick" />

            <el-main>
                <div class="pagecont-top" v-show="showSearch">
                    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                        v-show="showSearch" @submit.prevent>
                        <el-form-item label="标准信息" prop="keyWordParam">
                            <el-input class="el-form-input-width" v-model="queryParams.keyWordParam"
                                placeholder="请输入标准号或标准名称" clearable @keyup.enter="handleQuery" />
                        </el-form-item>
                        <el-form-item label="标准状态" prop="status">
                            <el-select class="el-form-input-width" v-model="queryParams.status" placeholder="请选择标准状态">
                                <el-option v-for="dict in dp_document_status" :key="dict.value" :label="dict.label"
                                    :value="dict.value"></el-option>
                            </el-select>
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
                </div>
                <div class="pagecont-bottom">
                    <div class="justify-between mb15">
                        <el-row :gutter="15" class="btn-style">
                            <el-col :span="1.5">
                                <el-button type="primary" plain @click="handleAdd"
                                    @mousedown="(e) => e.preventDefault()">
                                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                                </el-button>
                            </el-col>
                        </el-row>
                        <div class="justify-end top-right-btn">
                            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                                :columns="columns"></right-toolbar>
                        </div>
                    </div>
                    <el-table stripe v-loading="loading" :data="dpDataElemList"
                        @selection-change="handleSelectionChange" :default-sort="defaultSort"
                        @sort-change="handleSortChange">
                        <el-table-column v-if="getColumnVisibility(0)" label="编号" align="left" prop="id" width="50" />
                        <el-table-column v-if="getColumnVisibility(1)" label="标准号"
                            :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
                            <template #default="scope">
                                {{ scope.row.code || "-" }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(2)" label="标准名称"
                            :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
                            <template #default="scope">
                                {{ scope.row.name || "-" }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(7)" width="240" label="描述" align="left"
                            prop="description" :show-overflow-tooltip="{ effect: 'light' }">
                            <template #default="scope">
                                {{ scope.row.description || "-" }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(4)" label="标准类目"
                            :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="catCode">
                            <template #default="scope">
                                {{ scope.row.catName || "-" }}
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(10)" label="创建人"
                            :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="createBy">
                            <template #default="scope">
                                {{ scope.row.createBy || "-" }}
                            </template>
                        </el-table-column>
                        <!--  sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
                        <el-table-column v-if="getColumnVisibility(11)" label="创建时间" align="left" prop="createTime"
                            width="150">
                            <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                                || "-"
                                    }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(3)" label="标准状态" align="left" prop="status">
                            <template #default="scope">
                                <dict-tag :options="dp_document_status" :value="scope.row.status" />
                            </template>
                        </el-table-column>
                        <el-table-column label="备注" align="left" prop="remark"
                            :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(15)">
                            <template #default="scope">
                                {{ scope.row.remark || "-" }}
                            </template>
                        </el-table-column>


                        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                            width="200">
                            <template #default="scope">
                                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改
                                </el-button>
                                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)">详情
                                </el-button>
                                <el-popover placement="bottom" :width="150" trigger="click">
                                    <template #reference>
                                        <el-button link type="primary" icon="ArrowDown">更多</el-button>
                                    </template>
                                    <div style="width: 100px" class="butgdlist">
                                        <el-button link style="padding-left: 14px" type="primary" icon="View"
                                            @click="handleFilePreview(scope.row.fileUrl)"
                                            :disabled="!scope.row.fileUrl">预览</el-button>
                                        <el-button link type="primary" icon="Download" :disabled="!scope.row.fileUrl"
                                            @click="handleDownload(scope.row)">下载</el-button>

                                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除
                                        </el-button>
                                    </div>
                                </el-popover>
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
                </div>
            </el-main>
        </el-container>
        <!-- 标准弹窗 -->
        <StandardModal ref="standardModalRef" @update-success="handleQuery" />

    </div>
</template>

<script setup name="DpGroup">
import DeptTree from "@/components/DeptTree";
import {
    listDpDocument,
    getDpDocument,
    delDpDocument,
    addDpDocument,
    updateDpDocument,
    listAttDocumentCat
} from "@/api/dp/document/document";
import StandardModal from '../components/add'
import { deptUserTree } from "@/api/system/system/user.js";
import handleFilePreview from "@/utils/filePreview.js";
import { getToken } from "@/utils/auth.js";
const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_document_status } = proxy.useDict(
    "column_type",
    "sys_disable",
    "dp_document_status"
);
const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
/** 类型字典翻译 */
// function typeFormat(row) {
//   return proxy.selectDictLabel(dp_document_status.value, row.type);
// }

const dpDataElemList = ref([]);
const dpDataElemRuleRelList = ref([]);

// 列显隐信息
const columns = ref([
    { key: 0, label: "编号", visible: true },
    { key: 1, label: "标准号", visible: true },
    { key: 2, label: "标准名称", visible: true },
    { key: 7, label: "描述", visible: true },
    { key: 3, label: "标准类目", visible: true },
    { key: 10, label: "创建人", visible: true },
    { key: 11, label: "创建时间", visible: true },
    { key: 3, label: "标准状态", visible: true },
    { key: 15, label: "备注", visible: true },
]);
/** 预览文件 */
function handlePreview(row) {
    if (!row.fileUrl) {
        proxy.$message.warning("文件路径不存在，无法预览");
        return;
    }
    // 使用 window.open 打开文件，支持 pdf、图片等
    window.open(row.fileUrl, "_blank");
}
const handleDownload = (row) => {
    const baseUrl = import.meta.env.VITE_APP_BASE_API;
    const fullUrl = `${baseUrl}${row.fileUrl.trim()}`; // 去除可能的前后空格
    const a = document.createElement("a");
    a.href = fullUrl;
    a.download = row.fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
};
const getColumnVisibility = (key) => {
    const column = columns.value.find((col) => col.key === key);
    // 如果没有找到对应列配置，默认显示
    if (!column) return true;
    // 如果找到对应列配置，根据visible属性来控制显示
    return column.visible;
};

const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedDpDataElemRuleRel = ref([]);
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
    url: import.meta.env.VITE_APP_BASE_API + "/dp/dataElem/importData",
});

const data = reactive({
    form: { status: "0" },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        name: null,
        catCode: null,
        type:
            4,
    },
    rules: {
        code: [{ required: true, message: "中文不能为空", trigger: "blur" }],
        name: [
            { required: true, message: "英文不能为空", trigger: "blur" },
            {
                pattern: /^[a-zA-Z_]+$/,
                message: "只能包含英文字母和下划线",
                trigger: "blur",
            },
        ],
        catCode: [{ required: true, message: "类目编码不能为空", trigger: "blur" }],
        status: [{ required: true, message: "状态不能为空", trigger: "change" }],
        type: [{ required: true, message: "类型不能为空", trigger: "change" }],
        columnType: [
            { required: true, message: "字段类型不能为空", trigger: "change" },
        ],
    },
});

const { queryParams, form, rules } = toRefs(data);
const managerOptions = ref([]);
/** 查询国家标准列表 */
function getList() {
    loading.value = true;
    listDpDocument(queryParams.value).then((response) => {
        dpDataElemList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
    deptUserTree().then((response) => {
        managerOptions.value = response.data;
    });
}
function handleChange(value) {
    const selectedManager = managerOptions.value.find(
        (item) => item.userId === form.value.personCharge
    );
    form.value.contactNumber = selectedManager.phonenumber; // 将完整对象存储到 form 中
}
// 取消按钮
function cancel() {
    open.value = false;
    reset();
}
function handleNodeClick(data) {
    queryParams.value.catCode = data.code;
    handleQuery();
}
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
}; /** 查询部门下拉树结构 */
// 表单重置
function reset() {
    form.value = {
        ID: null,
        code: null,
        name: null,
        catCode: null,
        type: null,
        status: '0',
        issuingAgency: null,
        version: null,
        releaseDate: null,
        implementationDate: null,
        abolitionDate: null,
        standardUrl: null,
        validFlag: null,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        remark: null,
    };
    proxy.resetForm("dpDocumentRef");
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
function getDeptTree() {
    listAttDocumentCat().then((response) => {
        deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
        deptOptions.value = [
            {
                name: "标准类目",
                value: "",
                id: 0,
                children: deptOptions.value,
            },
        ];
    });
}
const standardModalRef = ref(null);
/** 新增按钮操作 */
function handleAdd() {
    standardModalRef.value.openModal({}, deptOptions.value, queryParams.value.type);
}

/** 修改按钮操作 */
function handleUpdate(row) {
    // const _id = row.id || ids.value;
    // getDpDocument(_id).then((response) => {
    //     form.value = response.data;
    //     dpDataElemRuleRelList.value = response.data.dpDataElemRuleRelList;
    //     open.value = true;
    //     title.value = "修改国家标准";
    // });
    standardModalRef.value.openModal(row, deptOptions.value, queryParams.value.type);
}

/** 详情按钮操作 */
function handleDetail(row) {
    routeTo("/dp/document/group/detail", row);

}

/** 提交按钮 */
function submitForm() {
    proxy.$refs["dpDataElemRef"].validate((valid) => {
        form.value.type = 1
        if (valid) {
            if (form.value.id != null) {
                updateDpDocument(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess("修改成功");
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDpDocument(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess("新增成功");
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
        .confirm('是否确认删除国家标准编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delDpDocument(_ids);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess("删除成功");
        })
        .catch(() => { });
}

/** 国家标准数据规则关联信息序号 */
function rowDpDataElemRuleRelIndex({ row, rowIndex }) {
    row.index = rowIndex + 1;
}

/** 国家标准数据规则关联信息新增按钮操作 */
function handleaddDpDocumentRuleRel() {
    let obj = {};
    obj.ruleType = "";
    obj.ruleId = "";
    obj.ruleConfig = "";
    obj.remark = "";
    dpDataElemRuleRelList.value.push(obj);
}

/** 国家标准数据规则关联信息删除按钮操作 */
function handleDeleteDpDataElemRuleRel() {
    if (checkedDpDataElemRuleRel.value.length == 0) {
        proxy.$modal.msgWarning(
            "未选择要删除的国家标准数据规则关联信息，请选择后重试"
        );
    } else {
        const dpDataElemRuleRels = dpDataElemRuleRelList.value;
        const checkedDpDataElemRuleRels = checkedDpDataElemRuleRel.value;
        dpDataElemRuleRelList.value = dpDataElemRuleRels.filter(function (item) {
            return checkedDpDataElemRuleRels.indexOf(item.index) == -1;
        });
    }
}

/** 复选框选中数据 */
function handleDpDataElemRuleRelSelectionChange(selection) {
    checkedDpDataElemRuleRel.value = selection.map((item) => item.index);
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download(
        "dp/dpDataElem/export",
        {
            ...queryParams.value,
        },
        `dpDataElem_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
    upload.title = "国家标准导入";
    upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
    proxy.download(
        "system/user/importTemplate",
        {},
        `dpDataElem_template_${new Date().getTime()}.xlsx`
    );
}

/** 提交上传文件 */
function submitFileForm() {
    proxy.$refs["uploadRef"].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
    upload.open = false;
    upload.isUploading = false;
    proxy.$refs["uploadRef"].handleRemove(file);
    proxy.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
        response.msg +
        "</div>",
        "导入结果",
        { dangerouslyUseHTMLString: true }
    );
    getList();
};

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
    const text = e === "1" ? "启用" : "禁用";
    proxy.$modal
        .confirm('确认要"' + text + '","' + row.name + '"国家标准吗？')
        .then(function () {
            updateStatusDpDataElem(id, row.status).then((response) => {
                proxy.$modal.msgSuccess("操作成功");
            });
        })
        .catch(function () {
            row.status = row.status === "1" ? "0" : "1";
        });
}
// function handleStatusChange(row) {
//   let text = row.status === "0" ? "启用" : "停用";
//   proxy.$modal
//     .confirm('确认要"' + text + '""' + row.roleName + '"角色吗?')
//     .then(function () {
//       return changeRoleStatus(row.roleId, row.status);
//     })
//     .then(() => {
//       proxy.$modal.msgSuccess(text + "成功");
//     })
//     .catch(function () {
//       row.status = row.status === "0" ? "1" : "0";
//     });
// }
/** ---------------------------------**/

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
getDeptTree();
getList();
</script>
<style scoped lang="scss">
::v-deep {
    .selectlist .el-tag.el-tag--info {
        background: #f3f8ff !important;
        border: 0px solid #6ba7ff !important;
        color: #2666fb !important;
    }
}

.app-container {
    margin: 13px 15px;
}

.el-main {
    padding: 2px 0px;
    // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

//上传附件样式调整
::v-deep {

    // .el-upload-list{
    //    display: flex;
    // }
    .el-upload-list__item {
        width: 100%;
        height: 25px;
    }
}
</style>
