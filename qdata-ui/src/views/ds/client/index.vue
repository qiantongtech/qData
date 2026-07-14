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

    <GuideTip tip-id="att/client.list" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
        v-show="showSearch" @submit.prevent>
        <!-- <el-form-item :label="td('ds.client.details.id')" prop="id" :label-position="labelPosition">
          <el-input class="el-form-input-width" v-model="queryParams.id" placeholder="Please enter the number" clearable
            @keyup.enter="handleQuery" />
        </el-form-item> -->
        <el-form-item :label="td('ds.client.appName')" prop="name" :label-position="labelPosition">
          <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('ds.client.appNamePlaceholder')" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item :label="td('ds.client.appType')" prop="type" :label-position="labelPosition">
          <el-select class="el-form-input-width" v-model="queryParams.type" :placeholder="td('ds.client.appTypePlaceholder')" clearable>
            <el-option v-for="dict in auth_app_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag" :label-position="labelPosition">
          <el-select class="el-form-input-width" v-model="queryParams.publicFlag" :placeholder="td('ds.client.isPublicPlaceholder')" clearable>
            <el-option v-for="dict in auth_public" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" v-hasPermi="['att:client:query']" @click="handleQuery"
            @mousedown="(e) => e.preventDefault()">
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
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:client:add']"
              @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
          <!--         <el-col :span="1.5">
           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['att:client:edit']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xiugai--copy mr5"></i>Modify
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['att:client:remove']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-shanchu-huise mr5"></i>Delete
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="info" plain  @click="handleImport" v-hasPermi="['att:client:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-upload-cloud-line mr5"></i>Import
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="warning" plain @click="handleExport" v-hasPermi="['att:client:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-download-line mr5"></i>Export
           </el-button>
         </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="clientList" @selection-change="handleSelectionChange"
        :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column v-if="getColumnVisibility(0)" width="50" :label="td('common.texts.number')"  align="center" prop="id" />
        <el-table-column v-if="getColumnVisibility(1)" width="200" :label="td('ds.client.appName')"
          :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" :label="td('ds.client.appType')" align="center" prop="type">
          <template #default="scope">
            <dict-tag :options="auth_app_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" :show-overflow-tooltip="{ effect: 'light' }" :label="td('common.texts.description')"
          align="left" prop="description" width="300">
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(16)" :label="td('ds.client.appIcon')"
          :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
          <template #default="scope">
            <div class="clientInfo">
              <div>
                <image-preview :src="scope.row.logo || noDataImg" :width="50" :height="50" />

              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column width="100" v-if="getColumnVisibility(4)" :label="td('ds.client.isPublic')" align="center" prop="publicFlag">
          <template #default="scope">
            <dict-tag :options="auth_public" :value="scope.row.publicFlag" />
          </template>
        </el-table-column>
        <!--       <el-table-column v-if="getColumnVisibility(5)" label="Allow authorized url" align="center" prop="allowUrl">
         <template #default="scope">
           {{ scope.row.allowUrl || '-' }}
         </template>
       </el-table-column>-->
        <!--       <el-table-column v-if="getColumnVisibility(6)" label="Sync address" align="center" prop="syncUrl">
         <template #default="scope">
           {{ scope.row.syncUrl || '-' }}
         </template>
       </el-table-column>-->
        <!--       <el-table-column v-if="getColumnVisibility(7)" label="App icon" align="center" prop="logo" width="100">
         <template #default="scope">
           <image-preview :src="scope.row.logo" :width="50" :height="50"/>
         </template>
       </el-table-column>-->

        <el-table-column v-if="getColumnVisibility(12)" :label="td('common.texts.createdBy')" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(14)" :label="td('common.texts.createdTime')" align="center" prop="createTime" width="150"
          sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']"> <template
            #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }"
          v-if="getColumnVisibility(15)">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="280">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['att:client:edit']">{{td('common.button.update')}}</el-button>
            <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
              v-hasPermi="['att:client:query']">{{td('common.button.details')}}</el-button>
            <el-popover placement="bottom" :width="150" trigger="click">
              <template #reference>
                <el-button link type="primary" icon="ArrowDown">{{td('common.button.more')}}</el-button>
              </template>
              <div style="width: 100px" class="butgdlist">
                <el-button link style="padding-left: 14px" type="primary" icon="Refresh" @click="handleReset(scope.row)"
                  v-hasPermi="['att:client:edit']">{{ td('ds.client.resetSecret') }}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['att:client:remove']">{{td('common.button.delete')}}</el-button>
              </div>
            </el-popover>
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

    <!-- Add or modify application dialog box -->
    <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="clientRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appName')" prop="name" :label-position="labelPosition">
              <el-input v-model="form.name" :placeholder="td('ds.client.appNamePlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appType')" prop="type" :label-position="labelPosition">
              <el-select v-model="form.type" :placeholder="td('ds.client.appTypePlaceholder')">
                <el-option v-for="dict in auth_app_type" :key="dict.value" :label="dict.label"
                           :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <el-input v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.homepageUrl')" prop="homepageUrl" :label-position="labelPosition">
              <el-input v-model="form.homepageUrl" placeholder="Please enter the homepage address" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.syncUrl')" prop="syncUrl" :label-position="labelPosition">
              <el-input v-model="form.syncUrl" placeholder="Please enter the synchronization address" />
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.client.appIcon')" prop="logo" :label-position="labelPosition">
              <image-upload v-model="form.logo" limit="1" :fileType="pdf" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag" :label-position="labelPosition">
              <el-radio-group v-model="form.publicFlag">
                <el-radio v-for="dict in auth_public" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
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
    <!-- Application details dialog -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="clientRef" :model="form" label-width="100px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.details.id')" prop="id">
              <div>{{ form.id || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appSecret')" prop="secret" :label-position="labelPosition">
              <div>{{ form.secret || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appName')" prop="name">
              <div>{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appIcon')" prop="logo" :label-position="labelPosition">
              <image-preview :src="form.logo || noDataImg" :width="50" :height="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appType')" prop="type">
              <dict-tag :options="auth_app_type" :value="form.type" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag">
              <dict-tag :options="auth_public" :value="form.publicFlag" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.homepageUrl')" prop="homepageUrl" :label-position="labelPosition">
              <div>{{ form.homepageUrl || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.syncUrl')" prop="syncUrl" :label-position="labelPosition">
              <div>{{ form.syncUrl || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.authPath')" prop="allowUrl" :label-position="labelPosition">
              <div>{{ form.allowUrl || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <div>{{ form.description || "-" }}</div>
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
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text" v-html="td('common.upload.dragOrClick')"></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('ds.client.importTip') }}
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

<script setup name="Client">
import {
  listClient,
  getClient,
  delClient,
  addClient,
  updateClient,
  resetSecret,
} from "@/api/ds/client/client";
import { getToken } from "@/utils/auth.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { auth_public, auth_app_type } = proxy.useDict(
  "auth_public",
  "auth_app_type"
);
const noDataImg = new URL('../../../assets/system/images/D.png', import.meta.url).href
const clientList = ref([]);

// Show hidden information
const columns = ref([
  { key: 0, label: td('common.texts.number'), visible: true },
  { key: 1, label: td('ds.client.appName'), visible: true },
  { key: 3, label: td('ds.client.appType'), visible: true },
  { key: 2, label: td('common.texts.description'), visible: true },
  { key: 16, label: td('ds.client.appIcon'), visible: true },
  { key: 4, label: td('ds.client.isPublic'), visible: true },
  { key: 12, label: td('common.texts.createdBy'), visible: true },
  { key: 14, label: td('common.texts.createdTime'), visible: true },
  { key: 15, label: td('common.texts.remark'), visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
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
  url: import.meta.env.VITE_APP_BASE_API + "/att/client/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: null,
    name: null,
    type: null,
    secret: null,
    homepageUrl: null,
    allowUrl: null,
    syncUrl: null,
    logo: null,
    description: null,
    publicFlag: null,
    createTime: null,
  },
  rules: {
    name: [{ required: true, message: td('ds.client.appNameRequired'), trigger: "blur" }],
    type: [{ required: true, message: td('ds.client.appTypeRequired'), trigger: "change" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** Query application list */
function getList() {
  loading.value = true;
  listClient(queryParams.value).then((response) => {
    clientList.value = response.data.rows;
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
    id: null,
    name: null,
    type: null,
    secret: null,
    homepageUrl: null,
    allowUrl: null,
    syncUrl: null,
    logo: null,
    description: null,
    publicFlag: null,
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
  proxy.resetForm("clientRef");
}

/** Search button action */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** reset button action */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// Multiple selection box selected data
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Sorting trigger events */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('ds.client.addApp');

  data.form.publicFlag = "1";
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getClient(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('ds.client.editApp');
  });
}

/** Reset key button operation */
function handleReset(row) {
  const _id = row.id || ids.value;

  proxy.$modal
    .confirm(td('ds.client.resetSecretConfirm'))
    .then(function () {
      resetSecret(_id).then((res) => {
        proxy.$modal.msgSuccess(td('ds.client.newSecret') + res.data);
        getList();
      });
    });
}

/** Detail button operation */
function handleDetail(row) {
  // reset();
  // const _id = row.id || ids.value;
  // getClient(_id).then((response) => {
  //   form.value = response.data;
  //   openDetail.value = true;
  //   title.value = "Application Details";
  // });
  routeTo("/ds/client/clientDetail", row);
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["clientRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateClient(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.client.editSuccess'));
            open.value = false;
            getList();
            submitLoading.value = false;
          })
          .catch((error) => { submitLoading.value = false; });
      } else {
        addClient(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.client.addSuccess'));
            open.value = false;
            getList();
            submitLoading.value = false;
          })
          .catch((error) => { submitLoading.value = false; });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('ds.client.deleteConfirm') + _ids + td('ds.client.deleteConfirmSuffix'))
    .then(function () {
      return delClient(_ids);
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
    "att/client/export",
    {
      ...queryParams.value,
    },
    `client_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
  upload.title = td('ds.client.importTitle');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `client_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit upload file */
function submitFileForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
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
  submitLoading.value = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    td('ds.client.importResult'),
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

getList();
</script>

<style scoped lang="scss">
.clientInfo {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

// :deep {
//   .el-popper.is-dark {
//     max-width: 900px !important;
//     max-height: 400px;
//     font-size: 14px;
//     text-align: start;
//   }
// }</style>