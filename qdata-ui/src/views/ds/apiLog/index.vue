<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
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
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
              <el-select class="el-form-input-width" v-model="queryParams.status" :placeholder="td('common.form.statusPlaceholder')" clearable>
                <el-option v-for="dict in ds_api_log_res_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item :label="td('common.texts.createdTime')" :label-position="labelPosition">
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
            <el-table-column v-if="getColumnVisibility(8)" :label="td('common.texts.status')" align="center" prop="status" width="140"
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
                <!--                  v-hasPermi="['ds:apiLog:edit']">View log</el-button>-->
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['ds:apiLog:query']">{{td('common.button.details')}}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['ds:apiLog:remove']">{{td('common.button.delete')}}</el-button>
              </template>
            </el-table-column>

            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
                <p>{{td('common.noData')}}</p>
              </div>
            </template>
          </el-table>

          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-main>
    </el-container>

    <!-- Add or modify API service call log dialog box -->
    <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="apiLogRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerUrlInput')" prop="callerUrl" :label-position="labelPosition">
              <el-input v-model="form.callerUrl" :placeholder="td('ds.apiLog.callerUrlInputPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.callerParamsInput')" prop="callerParams" :label-position="labelPosition">
              <el-input v-model="form.callerParams" type="textarea" :placeholder="td('ds.apiLog.callerParamsInputPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerStartDate')" prop="callerStartDate" :label-position="labelPosition">
              <el-date-picker clearable style="width: 100%" v-model="form.callerStartDate" type="date"
                value-format="YYYY-MM-DD" :placeholder="td('ds.apiLog.callerStartDatePlaceholder')">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerEndDate')" prop="callerEndDate" :label-position="labelPosition">
              <el-date-picker clearable style="width: 100%" v-model="form.callerEndDate" type="date"
                value-format="YYYY-MM-DD" :placeholder="td('ds.apiLog.callerEndDatePlaceholder')">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerSize')" prop="callerSize" :label-position="labelPosition">
              <el-input v-model="form.callerSize" :placeholder="td('ds.apiLog.callerSize')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerTime') + '(ms)'" prop="callerTime" :label-position="labelPosition">
              <el-input v-model="form.callerTime" :placeholder="td('ds.apiLog.callerTimePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.infoRecord')" prop="MSG" :label-position="labelPosition">
              <el-input v-model="form.MSG" type="textarea" :placeholder="td('ds.apiLog.contentPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in ds_api_log_res_status" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.remark')" prop="REMARK" :label-position="labelPosition">
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

    <!-- API service call log details dialog box -->
    <el-dialog :title="title" v-model="openDetail" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="apiLogRef" :model="form" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.apiServiceName')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.apiName || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerIp')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.callerIp || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.callerUrl')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.callerUrl || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.callerParamsInput')" :label-position="labelPosition">
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
            <el-form-item :label="td('ds.apiLog.callerTime') + '(s)'" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.callerTime / 1000 || '-' }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.requestMethod')" :label-position="labelPosition">
              <div>
                <dict-tag :options="ds_api_bas_info_api_method_type" :value="form.reqMethod" />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.apiLog.callerSize')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.callerSize || "-" }}
              </div>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.apiLog.exceptionRecord')" :label-position="labelPosition">
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

    <!-- User import dialog -->
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

// Show hidden information
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
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
  return column.visible;
};

const deptOptions = ref(undefined);
const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed // Initial left width
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

/*** User import parameters */
const upload = reactive({
  // Whether to display the pop-up layer (user import)
  open: false,
  // Popup layer title (user imported)
  title: "",
  // Whether to disable uploading
  isUploading: false,
  // Whether to update existing user data
  updateSupport: 0,
  // Set upload request headers
  headers: { Authorization: "Bearer " + getToken() },
  // Upload address
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
    const delta = event.clientX - startX; // Calculate mouse movement distance
    leftWidth.value += delta; // Modify left width
    startX = event.clientX; // Update starting position
    // Use requestAnimationFrame to reduce page redraw frequency
    requestAnimationFrame(() => { });
  }
};

/** Query API service call log list */
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

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// form reset
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

/** Search button action */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
const DeptTreeRef = ref(null);

/** reset button action */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.catCode = "";
  proxy.resetForm("queryRef");
  daterangeCreateTime.value = [];
  handleQuery();
}

// Multiple selection box selected data
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.ID);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Sorting trigger events */
function handleSortChange({ column, prop, order }) {
  console.log("column?.columnKey::" + column?.columnKey);
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('ds.apiLog.addLog');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _ID = row.ID || ids.value;
  getApiLog(_ID).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('ds.apiLog.editLog');
  });
}

/** Detail button operation */
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

/** submit button */
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

/** Delete button action */
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

/** Export button action */
function handleExport() {
  proxy.download(
    "ds/apiLog/export",
    {
      ...queryParams.value,
    },
    `apiLog_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
  upload.title = td('ds.apiLog.importTitle');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `apiLog_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**File upload is being processed */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** File upload successfully processed */
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
  //Contains http direct jump
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      //Refresh the current page directly
      window.location.reload();
    } else {
      //Jump route
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

//Upload attachment style adjustment
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