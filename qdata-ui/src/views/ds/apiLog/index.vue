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
    <el-container>
      <DeptTree
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="td('ds.apiLog.apiServiceCategoryPlaceholder')"
        ref="DeptTreeRef"
        @node-click="handleNodeClick"
      />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
              :tableRef="tableRef"
            />
          </template>

          <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
            <template #callerTime="{ row }">
              {{ row.callerTime ? row.callerTime / 1000 : "-" }}
            </template>
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['ds:apiLog:query']"
                >{{ td('common.button.details') }}</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['ds:apiLog:remove']"
                >{{ td('common.button.delete') }}</el-button
              >
            </template>
          </qt-table>
        </qt-wrap>
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
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{td('common.button.confirm')}}</el-button>
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
                {{ form.callerTime ? form.callerTime / 1000 : '-' }}
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
          <el-button type="primary" :loading="submitLoading" @click="submitFileForm">{{td('common.button.confirm')}}</el-button>
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
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, ref, onActivated, getCurrentInstance, toRefs } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { ds_api_log_res_status, ds_api_bas_info_api_method_type } = proxy.useDict(
  'ds_api_log_res_status',
  'ds_api_bas_info_api_method_type'
);

const tableRef = ref(null);
const DeptTreeRef = ref(null);

const deptOptions = ref(undefined);
const leftWidth = ref(300); // Initial left width
const open = ref(false);
const openDetail = ref(false);
const title = ref("");

const searchStore = reactive({
  items: [
    {
      label: td('ds.apiLog.apiServiceName'),
      prop: "apiName",
      component: { is: "input", placeholder: td('ds.apiLog.apiServiceNamePlaceholder') }
    },
    {
      label: td('common.texts.status'),
      prop: "status",
      component: {
        is: "select",
        placeholder: td('common.form.statusPlaceholder'),
        options: ds_api_log_res_status
      }
    },
    {
      label: td('common.texts.createdTime'),
      prop: "daterangeCreateTime",
      component: {
        is: "date-picker",
        type: "daterange",
        valueFormat: "YYYY-MM-DD",
        startPlaceholder: td('common.form.startDatePlaceholder'),
        endPlaceholder: td('common.form.endDatePlaceholder')
      }
    }
  ]
});

const tableStore = reactive({
  func: listApiLog,
  params: {
    apiName: null,
    status: null,
    catCode: "",
    daterangeCreateTime: [],
    orderByColumn: "create_time",
    isAsc: "desc"
  },
  config: {
    initResquest: true,
    beforeRequest: (params) => {
      let p = { ...params };
      if (p.daterangeCreateTime && p.daterangeCreateTime.length === 2) {
        p.beginCreateTime = p.daterangeCreateTime[0] + " 00:00:00";
        p.endCreateTime = p.daterangeCreateTime[1] + " 23:59:59";
      } else {
        p.beginCreateTime = undefined;
        p.endCreateTime = undefined;
      }
      delete p.daterangeCreateTime;
      
      if (p.orderByColumn === 'createTime') {
        p.orderByColumn = 'create_time';
      } else if (p.orderByColumn === 'callerSize') {
        p.orderByColumn = 'caller_size';
      }
      return p;
    }
  },
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 80, align: "center", sortable: true },
    { label: td('ds.apiLog.apiServiceName'), prop: "apiName", width: 300, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.apiLog.apiServiceCategory'), prop: "catName", width: 160, align: "left", tag: { class: "task-cat-ellipsis" }, showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.apiLog.callerIp'), prop: "callerIp", width: 130, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.apiLog.callerUrl'), prop: "callerUrl", width: 250, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.apiLog.callerSize'), prop: "callerSize", width: 120, align: "center", sortable: "custom" },
    { label: td('ds.apiLog.callerTime') + '(s)', prop: "callerTime", width: 120, align: "center", slot: "callerTime" },
    { label: td('common.texts.status'), prop: "status", width: 140, align: "center", dict: "ds_api_log_res_status" },
    { label: td('common.texts.createdTime'), prop: "createTime", width: 170, align: "center", sortable: "custom", date: true },
    { label: td('common.texts.operation'), width: 200, align: "center", fixed: "right", slot: "action" }
  ]
});

const data = reactive({
  form: {},
  rules: {},
});

const { form, rules } = toRefs(data);

/*** User import parameters */
const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/ds/apiLog/importData",
});

function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
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

function handleQuery() {
  tableStore.params.pageNum = 1;
}

function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  tableStore.params.daterangeCreateTime = [];
}

function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

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

function handleDetail(row) {
  reset();
  const _ID = row.id;
  getApiLog(_ID).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('ds.apiLog.detailLog');
  });
}

function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["apiLogRef"].validate((valid) => {
    if (valid) {
      if (form.value.ID != null) {
        updateApiLog(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.apiLog.editSuccess'));
            open.value = false;
            tableRef.value.refresh();
            submitLoading.value = false;
          })
          .catch((error) => { submitLoading.value = false; });
      } else {
        addApiLog(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.apiLog.addSuccess'));
            open.value = false;
            tableRef.value.refresh();
            submitLoading.value = false;
          })
          .catch((error) => { submitLoading.value = false; });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

function handleDelete(row) {
  const _IDs = row.id;
  proxy.$modal
    .confirm(td('ds.apiLog.deleteConfirm') + _IDs + td('ds.apiLog.deleteConfirmSuffix'))
    .then(function () {
      return delApiLog(_IDs);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** ---------------- Import related operations ------------------**/
function handleImport() {
  upload.title = td('ds.apiLog.importTitle');
  upload.open = true;
}

function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `apiLog_template_${new Date().getTime()}.xlsx`
  );
}

function submitFileForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["uploadRef"].submit();
}

const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  submitLoading.value = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    td('ds.apiLog.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.refresh();
};

onActivated(() => {
  tableRef.value?.refresh();
});

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
}

::v-deep {
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}
</style>
