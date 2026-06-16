<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="app-container" ref="app-container">
    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="td('ds.apiLog.apiServiceCategoryPlaceholder')" ref="DeptTreeRef"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
            v-show="showSearch" @submit.prevent>
            <el-form-item :label="td('ds.apiLog.apiServiceName')" prop="apiName">
              <el-input class="el-form-input-width" v-model="queryParams.apiName" :placeholder="td('ds.apiLog.apiServiceNamePlaceholder')" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('common.texts.status')" prop="status">
              <el-select class="el-form-input-width" v-model="queryParams.status" :placeholder="td('common.form.statusPlaceholder')" clearable>
                <el-option v-for="dict in ds_api_log_res_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item :label="td('common.texts.createdTime')">
              <el-date-picker class="el-form-input-width" v-model="daterangeCreateTime" value-format="YYYY-MM-DD"
                type="daterange" range-separator="-" :start-placeholder="td('common.form.startDatePlaceholder')" :end-placeholder="td('common.form.endDatePlaceholder')"></el-date-picker>
            </el-form-item>

            <el-form-item>
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
              </el-button>
              <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
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
            <el-table-column v-if="getColumnVisibility(1)" :label="td('common.texts.number')"  align="center" prop="id" width="80" />
            <el-table-column v-if="getColumnVisibility(2)" :show-overflow-tooltip="{ effect: 'light' }" :label="td('ds.apiLog.apiServiceName')"
              align="left" prop="apiName" width="300">
              <template #default="scope">
                {{ scope.row.apiName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" :show-overflow-tooltip="{ effect: 'light' }" :label="td('ds.apiLog.apiServiceCategory')"
              align="left" prop="catName" width="160">
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" :label="td('ds.apiLog.callerIp')" align="left" prop="callerIp" width="130"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.callerIp || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" :label="td('ds.apiLog.callerUrl')" align="left" prop="callerUrl" width="250"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.callerUrl || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" :label="td('ds.apiLog.callerSize')" align="center" prop="callerSize" width="120"
              sortable="custom" column-key="caller_size" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                {{ scope.row.callerSize || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" :label="td('ds.apiLog.callerTime') + '(s)'" align="center" prop="callerTime" width="120"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.callerTime / 1000 || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(8)" :label="td('common.texts.status')" align="center" prop="status" width="130"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #header>
                <div class="justify-center">
                  <span style="margin-right: 5px;">{{td('ds.apiLog.serviceStatus')}}</span>
                  <el-tooltip effect="light" :content="td('ds.apiLog.statusTip')" placement="top">
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
            <el-table-column v-if="getColumnVisibility(9)" :label="td('common.texts.createdTime')" align="center" prop="createTime" width="170"
              sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="getColumnVisibility(10)" :label="td('common.texts.operation')" align="center"
              class-name="small-padding fixed-width" fixed="right" width="200">
              <template #default="scope">
                <!--                <el-button link type="primary" icon="view" @click="routeTo('/ds/logDetail/dsApiLogDetail', scope.row)"-->
                <!--                  v-hasPermi="['ds:apiLog:edit']">查看日志</el-button>-->
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['ds:apiLog:query']">{{td('common.button.details')}}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['ds:apiLog:remove']">{{td('common.button.delete')}}</el-button>
              </template>
            </el-table-column>

            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>{{td('common.noData')}}</p>
              </div>
            </template>
          </el-table>

          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-main>
    </el-container>

    <!-- 添加或修改API服务调用日志对话框 -->
    <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="apiLogRef" :model="form" :rules="rules" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerUrlInput')" prop="callerUrl">
              <el-input v-model="form.callerUrl" :placeholder="td('ds.apiLog.callerUrlInputPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.callerParamsInput')" prop="callerParams">
              <el-input v-model="form.callerParams" type="textarea" :placeholder="td('ds.apiLog.callerParamsInputPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerStartDate')" prop="callerStartDate">
              <el-date-picker clearable style="width: 100%" v-model="form.callerStartDate" type="date"
                value-format="YYYY-MM-DD" :placeholder="td('ds.apiLog.callerStartDatePlaceholder')">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerEndDate')" prop="callerEndDate">
              <el-date-picker clearable style="width: 100%" v-model="form.callerEndDate" type="date"
                value-format="YYYY-MM-DD" :placeholder="td('ds.apiLog.callerEndDatePlaceholder')">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerSize')" prop="callerSize">
              <el-input v-model="form.callerSize" :placeholder="td('ds.apiLog.callerSize')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerTime') + '(ms)'" prop="callerTime">
              <el-input v-model="form.callerTime" :placeholder="td('ds.apiLog.callerTimePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.infoRecord')" prop="MSG">
              <el-input v-model="form.MSG" type="textarea" :placeholder="td('ds.apiLog.contentPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in ds_api_log_res_status" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.remark')" prop="REMARK">
              <el-input v-model="form.REMARK" :placeholder="td('common.form.remarkPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{td('common.button.cancel')}}</el-button>
          <el-button type="primary" size="mini" @click="submitForm">{{td('common.button.confirm')}}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- API服务调用日志详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="apiLogRef" :model="form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.apiServiceName')">
              <div class="form-readonly">
                {{ form.apiName || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerIp')">
              <div class="form-readonly">
                {{ form.callerIp || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.callerUrl')">
              <div class="form-readonly">
                {{ form.callerUrl || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.callerParamsInput')">
              <div class="form-readonly textarea">
                {{ form.callerParams || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.createdTime')">
              <div class="form-readonly">
                {{ parseTime(form.createTime, '{y}-{m}-{d} {h}:{i}') }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerTime') + '(s)'">
              <div class="form-readonly">
                {{ form.callerTime / 1000 || '-' }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.requestMethod')">
              <div>
                <dict-tag :options="ds_api_bas_info_api_method_type" :value="form.reqMethod" />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerSize')">
              <div class="form-readonly">
                {{ form.callerSize || "-" }}
              </div>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.exceptionRecord')">
              <div class="form-readonly textarea">
                {{ form.MSG || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status">
              <dict-tag :options="ds_api_log_res_status" :value="form.status" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{td('common.button.close')}}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text" v-html="td('common.upload.dragOrClick')"></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('ds.apiLog.importTip') }}
            </div>
            <span>{{ td('common.upload.fileFormat') }}</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">{{ td('common.upload.downloadTemplate') }}</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{td('common.button.cancel')}}</el-button>
          <el-button type="primary" @click="submitFileForm">{{td('common.button.confirm')}}</el-button>
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
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { ds_api_log_res_status, ds_api_bas_info_api_method_type } = proxy.useDict(
  'ds_api_log_res_status',
  'ds_api_bas_info_api_method_type'
);

const apiLogList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: td('common.texts.number'), visible: true },
  { key: 2, label: td('ds.apiLog.apiServiceName'), visible: true },
  { key: 3, label: td('ds.apiLog.apiServiceCategory'), visible: true },
  { key: 4, label: td('ds.apiLog.callerIp'), visible: true },
  { key: 5, label: td('ds.apiLog.callerUrl'), visible: true },
  { key: 6, label: td('ds.apiLog.callerSize'), visible: true },
  { key: 7, label: td('ds.apiLog.callerTime') + '(s)', visible: true },
  { key: 8, label: td('common.texts.status'), visible: true },
  { key: 9, label: td('common.texts.createdTime'), visible: true },
  { key: 10, label: td('common.texts.operation'), visible: true },
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
        name: td('ds.apiLog.apiServiceCategory'),
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
  title.value = td('ds.apiLog.addLog');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _ID = row.ID || ids.value;
  getApiLog(_ID).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('ds.apiLog.editLog');
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
    title.value = td('ds.apiLog.detailLog');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["apiLogRef"].validate((valid) => {
    if (valid) {
      if (form.value.ID != null) {
        updateApiLog(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.apiLog.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addApiLog(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.apiLog.addSuccess'));
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
    .confirm(td('ds.apiLog.deleteConfirm') + _IDs + td('ds.apiLog.deleteConfirmSuffix'))
    .then(function () {
      return delApiLog(_IDs);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
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
  upload.title = td('ds.apiLog.importTitle');
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
    td('ds.apiLog.importResult'),
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