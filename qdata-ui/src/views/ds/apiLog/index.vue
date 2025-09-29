<template>
  <div class="app-container" ref="app-container">
    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入API服务类目'" ref="DeptTreeRef"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="95px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="API服务名称" prop="apiName">
              <el-input class="el-form-input-width" v-model="queryParams.apiName" placeholder="请输入API服务名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select class="el-form-input-width" v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option v-for="dict in ds_api_log_res_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="调用时间">
              <el-date-picker class="el-form-input-width" v-model="daterangeCreateTime" value-format="YYYY-MM-DD"
                type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
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
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="apiLogList" @selection-change="handleSelectionChange"
            :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column v-if="getColumnVisibility(1)" label="编号" align="center" prop="id" width="80" />
            <el-table-column v-if="getColumnVisibility(2)" :show-overflow-tooltip="{ effect: 'light' }" label="API服务名称"
              align="left" prop="apiName" width="300">
              <template #default="scope">
                {{ scope.row.apiName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" :show-overflow-tooltip="{ effect: 'light' }" label="API服务类目"
              align="left" prop="catName" width="160">
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="调用者IP" align="left" prop="callerIp" width="130"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.callerIp || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" label="调用接口地址" align="left" prop="callerUrl" width="250"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.callerUrl || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" label="调用数据量" align="center" prop="callerSize" width="120"
              sortable="custom" column-key="caller_size" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                {{ scope.row.callerSize || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" label="调用耗时(秒)" align="center" prop="callerTime" width="120"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.callerTime / 1000 || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(8)" label="状态" align="center" prop="status" width="120"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #header>
                <div class="justify-center">
                  <span style="margin-right: 5px;">服务状态</span>
                  <el-tooltip effect="light" content="当状态为开启则可以被调用，并且同步发布到资源门户中" placement="top">
                    <el-icon class="tip-icon">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </div>
              </template>
              <template #default="scope">
                <dict-tag :options="ds_api_log_res_status" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" label="调用时间" align="center" prop="createTime" width="170"
              sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="getColumnVisibility(10)" label="操作" align="center"
              class-name="small-padding fixed-width" fixed="right" width="200">
              <template #default="scope">
                <!--                <el-button link type="primary" icon="view" @click="routeTo('/ds/logDetail/dsApiLogDetail', scope.row)"-->
                <!--                  v-hasPermi="['ds:apiLog:edit']">查看日志</el-button>-->
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['ds:apiLog:edit']">详情</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['ds:apiLog:remove']">删除</el-button>
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

    <!-- 添加或修改API服务调用日志对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="apiLogRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调用url" prop="callerUrl">
              <el-input v-model="form.callerUrl" placeholder="请输入调用url" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用参数" prop="callerParams">
              <el-input v-model="form.callerParams" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调用开始时间" prop="callerStartDate">
              <el-date-picker clearable style="width: 100%" v-model="form.callerStartDate" type="date"
                value-format="YYYY-MM-DD" placeholder="请选择调用开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调用结束时间" prop="callerEndDate">
              <el-date-picker clearable style="width: 100%" v-model="form.callerEndDate" type="date"
                value-format="YYYY-MM-DD" placeholder="请选择调用结束时间">
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
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in ds_api_log_res_status" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
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
    <el-dialog :title="title" v-model="openDetail" width="1000px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="apiLogRef" :model="form" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API服务名称">
              <div class="form-readonly">
                {{ form.apiName || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调用者IP">
              <div class="form-readonly">
                {{ form.callerIp || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用接口地址">
              <div class="form-readonly">
                {{ form.callerUrl || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用参数">
              <div class="form-readonly textarea">
                {{ form.callerParams || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调用时间">
              <div class="form-readonly">
                {{ parseTime(form.createTime, '{y}-{m}-{d} {h}:{i}') }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调用耗时(秒)">
              <div class="form-readonly">
                {{ form.callerTime / 1000 || '-' }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="请求方式">
              <div>
                <dict-tag :options="ds_api_bas_info_api_method_type" :value="form.reqMethod" />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调用数据量">
              <div class="form-readonly">
                {{ form.callerSize || "-" }}
              </div>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="异常信息记录">
              <div class="form-readonly textarea">
                {{ form.MSG || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <dict-tag :options="ds_api_log_res_status" :value="form.status" />
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的API服务调用日志数据
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

<script setup name="ApiLog">
import {
  listApiLog,
  getApiLog,
  delApiLog,
  addApiLog,
  updateApiLog,
} from "@/api/ds/apiLog/apiLog";
import { getToken } from "@/utils/auth.js";
import { listAttApiCat } from "@/api/ds/apiCat/apiCat";
import DeptTree from "@/components/DeptTree";
import { da } from "element-plus/es/locale/index.mjs";

const { proxy } = getCurrentInstance();
const { ds_api_log_res_status, ds_api_bas_info_api_method_type } = proxy.useDict(
  'ds_api_log_res_status',
  'ds_api_bas_info_api_method_type'
);

const apiLogList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "API服务名称", visible: true },
  { key: 3, label: "API服务类目", visible: true },
  { key: 4, label: "调用者IP", visible: true },
  { key: 5, label: "调用接口地址", visible: true },
  { key: 6, label: "调用数据量", visible: true },
  { key: 7, label: "调用耗时(秒)", visible: true },
  { key: 8, label: "状态", visible: true },
  { key: 9, label: "调用时间", visible: true },
  { key: 10, label: "操作", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const defaultSort = ref({ columnKey: "create_time", order: "desc" });

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
  url: import.meta.env.VITE_APP_BASE_API + "/ds/apiLog/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    apiId: null,
    callerId: null,
    createTime: null,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);


function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  handleQuery();
}

function getApiCatList() {
  listAttApiCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "API服务类目",
        value: "",
        children: deptOptions.value,
      },
    ];
  });
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

/** 查询API服务调用日志列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime.value && "" != daterangeCreateTime.value) {
    queryParams.value.params["beginCreateTime"] =
      daterangeCreateTime.value[0] + " 00:00:00";
    queryParams.value.params["endCreateTime"] =
      daterangeCreateTime.value[1] + " 23:59:59";
  }
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
    REMARK: null,
  };
  proxy.resetForm("apiLogRef");
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
  proxy.resetForm("queryRef");
  daterangeCreateTime.value = [];
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.ID);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange({ column, prop, order }) {
  console.log("column?.columnKey::" + column?.columnKey);
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增API服务调用日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _ID = row.ID || ids.value;
  getApiLog(_ID).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改API服务调用日志";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _ID = row.id || ids.value;
  console.log("_ID::" + _ID);
  getApiLog(_ID).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "API服务调用日志详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["apiLogRef"].validate((valid) => {
    if (valid) {
      if (form.value.ID != null) {
        updateApiLog(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addApiLog(form.value)
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
  const _IDs = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除API服务调用日志编号为"' + _IDs + '"的数据项？')
    .then(function () {
      return delApiLog(_IDs);
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
    "ds/apiLog/export",
    {
      ...queryParams.value,
    },
    `apiLog_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "API服务调用日志导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `apiLog_template_${new Date().getTime()}.xlsx`
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
  //包含http直接跳转
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      //是当前页面直接刷新
      window.location.reload();
    } else {
      //跳转路由
      router.push({
        path: link,
        query: {
          id: row.id,
        },
      });
    }
  }
}
queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
getList();
getApiCatList();
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
