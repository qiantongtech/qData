<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="att/attTag.list" />

    <el-container style="90%">
      <DeptTree ref="DeptTreeRef" :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入标签类目'"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="标签名称" prop="name">
              <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入标签名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="描述" prop="description">
              <el-input class="el-form-input-width" v-model="queryParams.description" placeholder="请输入描述" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button plain type="primary" v-hasPermi="['att:tag:query']" @click="handleQuery"
                @mousedown="(e) => e.preventDefault()">
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
                <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:tag:add']"
                  @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="AttTagList" @selection-change="handleSelectionChange"
            @sort-change="handleSortChange">
            <!-- <el-table-column type="selection" width="55" align="center" /> -->
            <el-table-column v-if="getColumnVisibility(1)" label="编号" align="center" prop="id" width="50" />
            <el-table-column v-if="getColumnVisibility(2)" label="标签名称" :show-overflow-tooltip="{ effect: 'light' }"
              width="200" align="left" prop="name">
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" label="标签类目" width="130"
              :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="catName">
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="描述" :show-overflow-tooltip="{ effect: 'light' }"
              align="left" prop="aeestCount" width="300">
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" label="资产数量" :show-overflow-tooltip="{ effect: 'light' }"
              align="left" prop="aeestCount">
              <template #default="scope">
                {{ scope.row.aeestCount || 0 }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" label="创建人" :show-overflow-tooltip="{ effect: 'light' }"
              width="120" align="left" prop="createBy">
              <template #default="scope">
                {{ scope.row.createBy || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" label="创建时间" align="center" prop="createTime" width="150"
              sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']"> <template
                #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(8)" label="状态" align="center" prop="status">
              <template #default="scope">
                <!-- {{ dp_model_status }}
                                <dict-tag :options="dp_model_status" :value="scope.row.status" /> -->
                <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
                  inactive-value="0" @change="
                    (e) => handleStatusChange(scope.row.id, scope.row, e)
                  " />
              </template>
            </el-table-column>
            <el-table-column label="备注" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }"
              v-if="getColumnVisibility(15)">
              <template #default="scope">
                {{ scope.row.remark || "-" }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                  :disabled="scope.row.status == 1" v-hasPermi="['att:tag:edit']">修改</el-button>
                <el-button link type="danger" icon="Delete" :disabled="scope.row.status == 1"
                  @click="handleDelete(scope.row)" v-hasPermi="['att:tag:remove']">删除</el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['att:tag:query']">详情</el-button>
                <!-- <el-button link type="primary" icon="view" @click="routeTo('/dp/model/dpModelDetail', scope.row)"
                  v-hasPermi="['dp:model:model:edit']">复杂详情</el-button> -->
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

    <!-- 添加或修改标签管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="AttTagRef" :model="form" :rules="rules" label-width="110px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="标签名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入标签名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="标签类目" prop="catCode">
              <!-- <el-input v-model="form.contact" placeholder="请输入联系人" /> -->
              <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID" placeholder="请选择标签类目"
                check-strictly />
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
          <el-col :span="24">
            <el-form-item label="近义词" prop="nearSynonyms">
              <el-input v-model="form.nearSynonyms" placeholder="请输入标签名称的近义词，多个请用逗号分隔" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="同义词" prop="synonyms">
              <el-input v-model="form.synonyms" placeholder="请输入标签名称的同义词，多个请用逗号分隔" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dp_model_status" :key="dict.value" :value="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
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
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!--    <my-form-dialog v-model:visible="open" :title="title"  @confirm="submitForm"-->
    <!--                    :catCode="queryParams.catCode" :deptOptions="deptOptions" />-->
    <!--    <MaterializationDialog :title="title" :visible="Materialization"-->
    <!--                           @update:dialogFormVisible="Materialization = $event" :ids="ids" @confirm="getList" />-->
  </div>
</template>

<script setup name="Tag">
import {
  listAttTag,
  getAttTag,
  delAttTag,
  addAttTag,
  updateAttTag,
} from "@/api/att/tag/tag.js";
import { getToken } from "@/utils/auth.js";
import DeptTree from "@/components/DeptTree/index.vue";
import { listAttTagCat } from "@/api/att/cat/tagCat/tagCat.js";
import { getCurrentInstance, ref } from "vue";
const { proxy } = getCurrentInstance();
const { dp_model_status, } = proxy.useDict(
  "dp_model_status",
);
const AttTagList = ref([]);
const deptOptions = ref(undefined);

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "标签名称", visible: true },
  { key: 3, label: "标签类目", visible: true },
  { key: 4, label: "描述", visible: true },
  { key: 5, label: "资产数量", visible: true },
  { key: 7, label: "创建人", visible: true },
  { key: 6, label: "创建时间", visible: true },
  { key: 8, label: "状态", visible: true },
  { key: 15, label: "备注", visible: true },
]);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
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
function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  queryParams.value.pageNum = 1;
  handleQuery();
}
/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? "启用" : "禁用";
  let dataForm = {
    id: id,
    status: row.status,
  };
  console.log(dataForm, row.status);
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"标签吗？')
    .then(function () {
      updateAttTag(dataForm).then((response) => {
        proxy.$modal.msgSuccess("操作成功");
        getList();
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

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
  url: import.meta.env.VITE_APP_BASE_API + "/att/AttTag/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: null,
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
  rules: {
    name: [{ required: true, message: "标签名称不能为空", trigger: "blur" }],
    // status: [{ required: true, message: "发布状态为必填项", trigger: "change" }],
    catCode: [
      { required: true, message: "标签类目不能为空", trigger: "change" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询标签管理列表 */
function getList() {
  loading.value = true;
  listAttTag(queryParams.value).then((response) => {
    AttTagList.value = response.data.rows;
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
    code: null,
    name: null,
    description: null,
    catCode: null,
    aeestCount: null,
    status: "1",
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
    remark: null,
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
  title.value = "新增标签";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getAttTag(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改标签";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  routeTo("/att/tag/detail", row);
  // routeTo("/dp/model/dpModelDetail", row);
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["AttTagRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        form.value.status = null;
        updateAttTag(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addAttTag(form.value)
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
    .confirm('是否确认删除标签编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delAttTag(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "att/AttTag/export",
    {
      ...queryParams.value,
    },
    `AttTag_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "标签管理导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `AttTag_template_${new Date().getTime()}.xlsx`
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
/** ---------------------------------**/
function getDeptTree() {
  listAttTagCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "标签类目",
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
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

.ellipsis-container {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
