<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="da/daAssetApply.list" />

    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入资产类目名称'" ref="DeptTreeRef"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="资产名称" prop="assetName">
              <el-input class="el-form-input-width" v-model="queryParams.assetName" placeholder="请输入资产名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="主题名称" prop="themeName">
              <el-input class="el-form-input-width" v-model="queryParams.themeName" placeholder="请输入主题名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="申请人" prop="createBy">
              <el-input class="el-form-input-width" v-model="queryParams.createBy" placeholder="请输入申请人" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="审核状态" prop="status">
              <el-select class="el-form-input-width" clearable v-model="queryParams.status" placeholder="请选择审核状态">
                <el-option v-for="dict in da_asset_apply_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
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

        <div class="pagecont-bottom pagecont-bottoms">
          <div class="justify-between mb15">
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="daAssetApplyList" @selection-change="handleSelectionChange"
            :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column v-if="getColumnVisibility(1)" label="资产名称" align="left" prop="assetName" width="200"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.assetName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(2)" label="英文名称" align="left" prop="assetTableName" width="280"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.assetTableName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" label="资产类目" align="left" prop="catAssetName"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.catAssetName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="主题名称" align="left" prop="themeName" width="150"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.themeName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" label="申请项目" align="left" prop="projectName" width="150"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.projectName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(8)" label="申请时间" align="center" prop="createTime" width="160"
              :show-overflow-tooltip="{ effect: 'light' }" sortable="custom" column-key="create_time"
              :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" label="申请人" align="center" prop="createBy" width="100"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.createBy || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" label="审核状态" align="center" prop="status" width="80"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                <dict-tag :options="da_asset_apply_status" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column label="操作" v-if="getColumnVisibility(9)" align="center"
              class-name="small-padding fixed-width" fixed="right" width="140">
              <template #default="scope">
                <el-button link v-if="scope.row.status == 1" type="primary" icon="Stamp"
                  @click="handleUpdate(scope.row)" v-hasPermi="['da:assetApply:edit']">审核</el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['da:assetApply:edit']">详情</el-button>
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

    <!-- 添加或修改数据资产申请对话框 -->
    <el-dialog :title="title" v-model="open" width="1000px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="daAssetApplyRef" :model="form" :rules="rules" label-width="100px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产名称">
              <div class="form-readonly">
                {{ form.assetName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="英文名称">
              <div class="form-readonly">
                {{ form.assetTableName }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据连接">
              <div class="form-readonly">
                {{ form.datasourceName ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库地址">
              <div class="form-readonly">
                {{ form.datasourceIp ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="数据库类型:" prop="datasourceType">
              <dict-tag :options="datasource_type" :value="form.datasourceType" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="资产描述">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请项目" prop="projectCode">
              <div class="form-readonly">
                {{ form.projectName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phonenumber">
              <div class="form-readonly">
                {{ form.phonenumber }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="申请理由:" prop="applyReason">
              <div class="form-readonly textarea">
                {{ form.applyReason ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="审核结果" prop="status">
              <el-radio-group v-model="form.status" @change="handleStatusChange">
                <el-radio :value="2">驳回</el-radio>
                <el-radio :value="3">通过</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.status == 2">
          <el-col :span="24">
            <el-form-item label="驳回原因" prop="approvalReason">
              <el-input type="textarea" :min-height="192" v-model="form.approvalReason" placeholder="请输入驳回原因" />
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

    <!-- 数据资产申请详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="1000px" :append-to="$refs['app-container']" draggable>
      <el-form :model="form" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产名称:" prop="assetName">
              <div class="form-readonly">
                {{ form.assetName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="英文名称:" prop="assetTableName">
              <div class="form-readonly">
                {{ form.assetTableName }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="数据连接:" prop="datasourceName">
              <div class="form-readonly">
                {{ form.datasourceName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库地址:" prop="datasourceIp">
              <div class="form-readonly">
                {{ form.datasourceIp }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="数据库类型:" prop="datasourceType">
              <dict-tag :options="datasource_type" :value="form.datasourceType" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="资产描述:" prop="description">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请项目:" prop="projectName">
              <div class="form-readonly">
                {{ form.projectName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请状态:" prop="status">
              <dict-tag :options="da_asset_apply_status" :value="form.status" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请人:" prop="createBy">
              <div class="form-readonly">
                {{ form.createBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话:" prop="phonenumber">
              <div class="form-readonly">
                {{ form.phonenumber }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请时间:" prop="createTime">
              <div class="form-readonly">
                {{ form.createTime }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="申请理由:" prop="applyReason">
              <div class="form-readonly textarea">
                {{ form.applyReason ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="审批理由:" prop="approvalReason">
              <div class="form-readonly textarea">
                {{ form.approvalReason ?? "-" }}
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

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据资产申请数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">取 消</el-button>
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AssetApply">
import {
  listDaAssetApply,
  getDaAssetApply,
  delDaAssetApply,
  addDaAssetApply,
  updateDaAssetApply,
} from "@/api/da/assetApply/assetApply";
import { listAttProject } from "@/api/att/project/project.js";
import { getToken } from "@/utils/auth.js";
import { listAttAssetCat } from "@/api/att/cat/assetCat/assetCat.js";
import DeptTree from "@/components/DeptTree";
const { proxy } = getCurrentInstance();
const { da_asset_apply_status, datasource_type } = proxy.useDict(
  "da_asset_apply_status",
  "datasource_type"
);
const daAssetApplyList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "资产名称", visible: true },
  { key: 2, label: "英文名称", visible: true },
  { key: 3, label: "资产类目", visible: true },
  { key: 4, label: "主题名称", visible: true },
  { key: 5, label: "申请项目", visible: true },
  { key: 6, label: "申请时间", visible: true },
  { key: 7, label: "申请人", visible: true },
  { key: 8, label: "审核状态", visible: true },
  { key: 9, label: "操作", visible: true },
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
const defaultSort = ref({ columnKey: "create_time", order: "desc" });
const router = useRouter();
const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
const projectOptions = ref([]);
let startX = 0; // 鼠标按下时的初始位置
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
  url: import.meta.env.VITE_APP_BASE_API + "/da/daAssetApply/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    assetId: null,
    assetName: null,
    projectId: null,
    projectCode: null,
    applyReason: null,
    approvalReason: null,
    status: null,
    sourceType: 0,
    createBy: null,
    themeName: null,
    createTime: null,
  },
  rules: {
    status: [{ required: true, message: "请选择审核结果", trigger: "change" }],
    approvalReason: [
      { required: true, message: "请输入驳回原因", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

function handleNodeClick(data) {
  queryParams.value.catAssetCode = data.code;
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
};

function handleStatusChange(value) {
  form.value.status = value;
}

function getAssetCat() {
  listAttAssetCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "资产类目",
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}

/** 查询数据资产申请列表 */
function getList() {
  loading.value = true;
  listDaAssetApply(queryParams.value).then((response) => {
    daAssetApplyList.value = response.data.rows;
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
    projectId: null,
    projectCode: null,
    applyReason: null,
    approvalReason: null,
    status: null,
    validFlag: null,
    delFlag: null,
    sourceType: 0,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("daAssetApplyRef");
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
  queryParams.value.catAssetCode = "";
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
function handleSortChange({ column, prop, order }) {
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加数据资产申请";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAssetApply(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "数据资产申请审核";
    form.value.status = null;
  });
  listAttProject().then((response) => {
    projectOptions.value = response.data.rows;
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAssetApply(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "数据资产申请详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["daAssetApplyRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDaAssetApply(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addDaAssetApply(form.value)
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
    .confirm('是否确认删除数据资产申请编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDaAssetApply(_ids);
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
    "da/daAssetApply/export",
    {
      ...queryParams.value,
    },
    `daAssetApply_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "数据资产申请导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `daAssetApply_template_${new Date().getTime()}.xlsx`
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
getAssetCat();
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
