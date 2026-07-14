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

    <GuideTip tip-id="da/daAssetApply.list" />

    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="td('da.assetApply.catTreePlaceholder')" ref="DeptTreeRef"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                   v-show="showSearch" @submit.prevent>
            <el-form-item :label="td('da.assetApply.assetName')" prop="assetName" :label-position="labelPosition">
              <el-input class="el-form-input-width" v-model="queryParams.assetName" :placeholder="td('da.assetApply.assetNamePlaceholder')" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('da.assetApply.topicName')" prop="themeName">
              <el-input class="el-form-input-width" v-model="queryParams.themeName" :placeholder="td('da.assetApply.topicNamePlaceholder')" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('da.assetApply.applicant')" prop="createBy" :label-position="labelPosition">
              <el-input class="el-form-input-width" v-model="queryParams.createBy" :placeholder="td('da.assetApply.applicantPlaceholder')" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('da.assetApply.auditStatus')" prop="status">
              <el-select class="el-form-input-width" clearable v-model="queryParams.status" :placeholder="td('da.assetApply.auditStatusPlaceholder')">
                <el-option v-for="dict in da_asset_apply_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
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

        <div class="pagecont-bottom pagecont-bottoms">
          <div class="justify-between mb15">
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="daAssetApplyList" @selection-change="handleSelectionChange"
            :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column v-if="getColumnVisibility(1)" :label="td('da.assetApply.assetName')" align="left" prop="assetName" width="200"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.assetName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(2)" :label="td('da.assetApply.englishName')" align="left" prop="assetTableName" width="280"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.assetTableName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" :label="td('da.assetApply.assetCat')" align="left" prop="catAssetName"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.catAssetName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" :label="td('da.assetApply.topicName')" align="left" prop="themeName" width="150"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.themeName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" :label="td('da.assetApply.applyProject')" align="left" prop="projectName" width="150"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.projectName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(8)" :label="td('da.assetApply.applyTime')" align="center" prop="createTime" width="160"
              :show-overflow-tooltip="{ effect: 'light' }" sortable="custom" column-key="create_time"
              :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" :label="td('da.assetApply.applicant')" align="center" prop="createBy" width="100"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.createBy || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" :label="td('da.assetApply.auditStatus')" align="center" prop="status" width="150"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                <dict-tag :options="da_asset_apply_status" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" v-if="getColumnVisibility(9)" align="center"
              class-name="small-padding fixed-width" fixed="right" width="140">
              <template #default="scope">
                <el-button link v-if="scope.row.status == 1" type="primary" icon="Stamp"
                  @click="handleUpdate(scope.row)" v-hasPermi="['da:assetApply:edit']">{{ td('da.assetApply.audit') }}</el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['da:assetApply:edit']">{{ td('common.button.details') }}</el-button>
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

    <!-- Add or edit data asset application dialog -->
    <el-dialog :title="title" v-model="open" width="1000px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="daAssetApplyRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.assetName')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.assetName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.englishName')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.assetTableName }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.datasource')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.datasourceName ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.dbAddress')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.datasourceIp ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.dbTypeLabel')" prop="datasourceType" :label-position="labelPosition">
              <dict-tag :options="datasource_type" :value="form.datasourceType" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.assetApply.assetDesc')" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.applyProject')" prop="projectCode" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.projectName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.contactPhone')" prop="phonenumber" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.phonenumber }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.assetApply.applyReason')" prop="applyReason" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.applyReason ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.assetApply.auditResult')" prop="status" :label-position="labelPosition">
              <el-radio-group v-model="form.status" @change="handleStatusChange">
                <el-radio :value="2">{{ td('da.assetApply.reject') }}</el-radio>
                <el-radio :value="3">{{ td('da.assetApply.approve') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.status == 2">
          <el-col :span="24">
            <el-form-item :label="td('da.assetApply.rejectReason')" prop="approvalReason" :label-position="labelPosition">
              <el-input type="textarea" :min-height="192" v-model="form.approvalReason" :placeholder="td('da.assetApply.rejectReasonPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Data asset application detail dialog -->
    <el-dialog :title="title" v-model="openDetail" width="1000px" :append-to="$refs['app-container']" draggable>
      <el-form :model="form" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.assetName')" prop="assetName">
              <div class="form-readonly">
                {{ form.assetName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.englishName')" prop="assetTableName" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.assetTableName }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.datasource')" prop="datasourceName" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.datasourceName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.dbAddress')" prop="datasourceIp" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.datasourceIp }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.dbTypeLabel')" prop="datasourceType" :label-position="labelPosition">
              <dict-tag :options="datasource_type" :value="form.datasourceType" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.assetApply.assetDesc')" prop="description" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.applyProject')" prop="projectName" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.projectName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.applyStatus')" prop="status" :label-position="labelPosition">
              <dict-tag :options="da_asset_apply_status" :value="form.status" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.applicant')" prop="createBy">
              <div class="form-readonly">
                {{ form.createBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.contactPhone')" prop="phonenumber" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.phonenumber }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.assetApply.applyTime')" prop="createTime" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.createTime }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.assetApply.applyReason')" prop="applyReason" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.applyReason ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.assetApply.approvalReason')" prop="approvalReason" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.approvalReason ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">{{ td('common.upload.dragOrClick') }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('common.upload.updateExistingData') }}
            </div>
            <span>{{ td('common.upload.fileFormat') }}</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">{{ td('common.upload.downloadTemplate') }}</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm') }}</el-button>
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
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { da_asset_apply_status, datasource_type } = proxy.useDict(
  "da_asset_apply_status",
  "datasource_type"
);
const daAssetApplyList = ref([]);

// Column visibility information
const columns = ref([
  { key: 1, label: td('da.assetApply.assetName'), visible: true },
  { key: 2, label: td('da.assetApply.englishName'), visible: true },
  { key: 3, label: td('da.assetApply.assetCat'), visible: true },
  { key: 4, label: td('da.assetApply.topicName'), visible: true },
  { key: 5, label: td('da.assetApply.applyProject'), visible: true },
  { key: 6, label: td('da.assetApply.applyTime'), visible: true },
  { key: 7, label: td('da.assetApply.applicant'), visible: true },
  { key: 8, label: td('da.assetApply.auditStatus'), visible: true },
  { key: 9, label: td('common.texts.operation'), visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If no corresponding column configuration found, default to showing it
  if (!column) return true;
  // If corresponding column configuration found, control visibility based on the visible property
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
const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Whether currently dragging
const projectOptions = ref([]);
let startX = 0; // Initial position when mouse is pressed
/*** 用户导入参数 */
const upload = reactive({
  // Whether to show the popup layer (user import)
  open: false,
  // Popup layer title (user import)
  title: "",
  // Whether to disable upload
  isUploading: false,
  // Whether to update existing user data
  updateSupport: 0,
  // Set upload request headers
  headers: { Authorization: "Bearer " + getToken() },
  // Upload URL
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
    status: [{ required: true, message: td('da.assetApply.auditResultRequired'), trigger: "change" }],
    approvalReason: [
      { required: true, message: td('da.assetApply.rejectReasonRequired'), trigger: "blur" },
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
    const delta = event.clientX - startX; // Calculate mouse movement distance
    leftWidth.value += delta; // Modify left width
    startX = event.clientX; // Update start position
    // Use requestAnimationFrame to reduce page redraw frequency
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
        name: td('da.assetApply.catRootName'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}

/** Query data asset application list */
function getList() {
  loading.value = true;
  listDaAssetApply(queryParams.value).then((response) => {
    daAssetApplyList.value = response.data.rows;
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

// Reset form
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

/** Search button operation */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
const DeptTreeRef = ref(null);
/** Reset button operation */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.catAssetCode = "";
  proxy.resetForm("queryRef");
  handleQuery();
}

// Checkbox selection data
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Sort trigger event */
function handleSortChange({ column, prop, order }) {
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('da.assetApply.addTitle');
}

/** Edit button operation */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAssetApply(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('da.assetApply.auditTitle');
    form.value.status = null;
  });
  listAttProject().then((response) => {
    projectOptions.value = response.data.rows;
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAssetApply(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('da.assetApply.detailTitle');
  });
}

/** Submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["daAssetApplyRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDaAssetApply(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('da.assetApply.editSuccess'));
            open.value = false;
            getList();
            submitLoading.value = false;
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      } else {
        addDaAssetApply(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('da.assetApply.addSuccess'));
            open.value = false;
            getList();
            submitLoading.value = false;
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button operation */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('da.assetApply.confirmDelete', '', { id: _ids }))
    .then(function () {
      return delDaAssetApply(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('da.assetApply.deleteSuccess'));
    })
    .catch(() => { });
}

/** Export button operation */
function handleExport() {
  proxy.download(
    "da/daAssetApply/export",
    {
      ...queryParams.value,
    },
    `daAssetApply_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations -----------------**/
/** Import button operation */
function handleImport() {
  upload.title = td('da.assetApply.importTitle');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `daAssetApply_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/** File upload in progress handler */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** File upload success handler */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    td('da.assetApply.importResult'),
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

// Upload attachment style adjustment
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
