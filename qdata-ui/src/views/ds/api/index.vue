<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="ds/dsApi.list" />

    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入API服务类目'" ref="DeptTreeRef"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="95px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="API服务名称" prop="name">
              <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入API服务名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select class="el-form-input-width" v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option v-for="dict in ds_api_log_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
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
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button type="primary" plain @click="routeToAdd('/ds/api/add')" v-hasPermi="['ds:api:add']"
                  @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="dsApiList" @selection-change="handleSelectionChange"
            :default-sort="defaultSort" @sort-change="handleSortChange">
            <!--            <el-table-column type="selection" width="55" align="center" />-->
            <el-table-column v-if="getColumnVisibility(0)" label="编号" align="center" prop="id" width="80px" />
            <el-table-column :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(1)" label="API名称"
              width="300px" align="left" prop="name">
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(2)" label="API服务类目"
              min-width="160px" align="left" prop="catName">
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" label="描述" width="200" align="left" prop="description"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.description || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" width="80" label="API版本" align="center" prop="apiVersion">
              <template #default="scope">
                {{ scope.row.apiVersion || "-" }}
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="{ effect: 'light' }" v-if="getColumnVisibility(5)" label="API路径"
              min-width="200px" align="left" prop="apiUrl">
              <template #default="scope">
                {{ scope.row.apiUrl || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" width="80" label="请求类型" align="center" prop="reqMethod">
              <template #default="scope">
                <dict-tag :options="ds_api_bas_info_api_method_type" :value="scope.row.reqMethod" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" width="80" label="返回格式" align="center" prop="resDataType">
              <template #default="scope">
                <dict-tag :options="ds_api_bas_info_res_data_type" :value="scope.row.resDataType" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(8)" width="120" label="创建人" align="center" prop="createBy"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" label="创建时间" align="center" prop="createTime" width="150"
              sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(10)" label="状态" align="center" prop="status" width="70">
              <template #header>
                <div class="justify-center">
                  <span style="margin-right: 5px;">状态</span>
                  <el-tooltip effect="light" content="当状态为开启则可以被调用，并且同步发布到资源门户中" placement="top">
                    <el-icon class="tip-icon">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </div>
              </template>
              <template #default="scope">
                <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
                  inactive-value="0" @change="handleStatusChange(scope.row)" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(11)" label="备注" width="200" align="left" prop="remark"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(12)" label="操作" align="center"
              class-name="small-padding fixed-width" fixed="right" width="220">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="routeTo('/ds/api/edit', scope.row)"
                  v-hasPermi="['ds:api:edit']">修改</el-button>
                <el-button link type="primary" icon="view" @click="routeTo('/ds/api/detail', scope.row)"
                  v-hasPermi="['ds:api:edit']">详情</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['ds:api:remove']">删除</el-button>
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

    <!-- 添加或修改API服务对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="dsApiRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API服务名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入API服务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API版本" prop="apiVersion">
              <el-input v-model="form.apiVersion" placeholder="请输入API版本" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API路径" prop="apiUrl">
              <el-input v-model="form.apiUrl" placeholder="请输入API路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求方式" prop="reqMethod">
              <el-radio-group v-model="form.reqMethod">
                <el-radio v-for="dict in ds_api_bas_info_api_method_type" :key="dict.value" :label="dict.value">{{
                  dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务提供类型" prop="apiServiceType">
              <el-radio-group v-model="form.apiServiceType">
                <el-radio v-for="dict in ds_api_bas_info_api_service_type" :key="dict.value" :label="dict.value">{{
                  dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="返回结果类型" prop="resDataType">
              <el-select v-model="form.resDataType" placeholder="请选择返回结果类型">
                <el-option v-for="dict in ds_api_bas_info_res_data_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="IP黑名单多个，隔开" prop="denyIp">
              <el-input v-model="form.denyIp" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行配置JSON" prop="configJson">
              <el-input v-model="form.configJson" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="限流配置JSON" prop="limitJson">
              <el-input v-model="form.limitJson" placeholder="请输入限流配置JSON" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数" prop="reqParams">
              <el-input v-model="form.reqParams" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="返回参数" prop="resParams">
              <el-input v-model="form.resParams" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in ds_api_log_status" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
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

    <!-- API服务详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="dsApiRef" :model="form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API服务名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API版本" prop="apiVersion">
              <div>
                {{ form.apiVersion }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API路径" prop="apiUrl">
              <div>
                {{ form.apiUrl }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求方式" prop="reqMethod">
              <dict-tag :options="ds_api_bas_info_api_method_type" :value="form.reqMethod" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务提供类型" prop="apiServiceType">
              <dict-tag :options="ds_api_bas_info_api_service_type" :value="form.apiServiceType" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="返回结果类型" prop="resDataType">
              <dict-tag :options="ds_api_bas_info_res_data_type" :value="form.resDataType" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="IP黑名单多个，隔开" prop="denyIp">
              <div>
                {{ form.denyIp }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行配置JSON" prop="configJson">
              <div>
                {{ form.configJson }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="限流配置JSON" prop="limitJson">
              <div>
                {{ form.limitJson }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数" prop="reqParams">
              <div>
                {{ form.reqParams }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="返回参数" prop="resParams">
              <div>
                {{ form.resParams }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="描述" prop="DESCRIPTION">
              <div>
                {{ form.DESCRIPTION }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="STATUS">
              <dict-tag :options="ds_api_log_status" :value="form.STATUS" />
            </el-form-item>
          </el-col>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的API服务数据
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

<script setup name="DaApi">
import {
  listDsApi,
  getDsApi,
  delDsApi,
  addDsApi,
  updateDsApi,
  releaseDataApi,
  cancelDataApi,
} from "@/api/ds/api/api.js";
import { getToken } from "@/utils/auth.js";
import DeptTree from "@/components/DeptTree";
import { listAttApiCat } from "@/api/ds/apiCat/apiCat";
const { proxy } = getCurrentInstance();
const {
  ds_api_log_status,
  ds_api_bas_info_api_service_type,
  ds_api_bas_info_api_method_type,
  ds_api_bas_info_res_data_type,
} = proxy.useDict(
  "ds_api_log_status",
  "ds_api_bas_info_api_service_type",
  "ds_api_bas_info_api_method_type",
  "ds_api_bas_info_res_data_type"
);

const dsApiList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "编号", visible: true },
  { key: 1, label: "API名称", visible: true },
  { key: 2, label: "API服务类目", visible: true },
  { key: 3, label: "描述", visible: true },
  { key: 4, label: "API版本", visible: true },
  { key: 5, label: "API路径", visible: true },
  { key: 6, label: "请求类型", visible: true },
  { key: 7, label: "返回格式", visible: true },
  { key: 8, label: "创建人", visible: true },
  { key: 9, label: "创建时间", visible: true },
  { key: 10, label: "状态", visible: true },
  { key: 11, label: "备注", visible: true },
  { key: 12, label: "操作", visible: true },
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
const defaultSort = ref({ prop: "create_time", order: "desc" });
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
  url: import.meta.env.VITE_APP_BASE_API + "/ds/dsApi/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    NAME: null,
    STATUS: null,
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
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}

/** 查询API服务列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && "" != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  console.log(queryParams.value);

  listDsApi(queryParams.value).then((response) => {
    dsApiList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
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

/** 启用禁用开关 */
function handleStatusChange(row) {
  const text = row.status === "1" ? "上线" : "下线";
  proxy.$modal
    .confirm("确认要" + text + '"' + row.name + '"服务吗？')
    .then(function () {
      loading.value = true;
      if (row.status === "1") {
        releaseDataApi(row.id).then((response) => {
          proxy.$modal.msgSuccess(text + "成功");
          getList();
        });
      } else {
        cancelDataApi(row.id).then((response) => {
          proxy.$modal.msgSuccess(text + "成功");
          getList();
        });
      }
    })
    .catch(function () {
      if (row.status === "1") {
        row.status = "0";
      } else {
        row.status = "1";
      }
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
    NAME: null,
    apiVersion: null,
    apiUrl: null,
    reqMethod: null,
    apiServiceType: null,
    resDataType: null,
    denyIp: null,
    configJson: null,
    limitJson: null,
    reqParams: null,
    resParams: null,
    DESCRIPTION: null,
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
  proxy.resetForm("dsApiRef");
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
  daterangeCreateTime.value = [];
  queryParams.value.catCode = "";
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
  console.log(column, prop, order);
  console.log("column?.columnKey::" + column?.columnKey);
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增API服务";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _ID = row.ID || ids.value;
  getDsApi(_ID).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改API服务";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _ID = row.id || ids.value;
  getDsApi(_ID).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "API服务详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dsApiRef"].validate((valid) => {
    if (valid) {
      if (form.value.ID != null) {
        updateDsApi(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addDsApi(form.value)
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
    .confirm('是否确认删除API服务编号为"' + _IDs + '"的数据项？')
    .then(function () {
      return delDsApi(_IDs);
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
    "ds/dsApi/export",
    {
      ...queryParams.value,
    },
    `dsApi_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "API服务导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `dsApi_template_${new Date().getTime()}.xlsx`
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

function routeToAdd(link, row) {
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
      });
    }
  }
}
queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
onActivated(() => {
  getList();
});
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
