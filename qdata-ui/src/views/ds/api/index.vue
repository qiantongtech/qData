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
    <GuideTip tip-id="ds/dsApi.list" />

    <el-container>
      <DeptTree
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="td('ds.api.apiCategoryPlaceholder')"
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
          <template #actions-data>
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  @click="routeToAdd('/ds/api/add')"
                  v-hasPermi="['ds:api:add']"
                >
                  <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                </el-button>
              </el-col>
            </el-row>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
            <template #status="{ row }">
              <el-switch
                v-model="row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
                @change="handleStatusChange(row)"
              />
            </template>

            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="routeTo('/ds/api/edit', row)"
                v-hasPermi="['ds:api:edit']"
                >{{ td('common.button.update') }}</el-button
              >
              <el-button
                link
                type="primary"
                icon="view"
                @click="routeTo('/ds/api/detail', row)"
                v-hasPermi="['ds:api:edit']"
                >{{ td('common.button.details') }}</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['ds:api:remove']"
                >{{ td('common.button.delete') }}</el-button
              >
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- User import dialog -->
    <el-dialog
      :title="upload.title"
      v-model="upload.open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text" v-html="td('common.upload.dragOrClick')"></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('ds.api.importTip') }}
            </div>
            <span>{{ td('ds.apiCat.uploadFormat') }}</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >{{ td('common.upload.downloadTemplate') }}</el-link
            >
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitFileForm">{{
            td('common.button.confirm')
          }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DaApi">
import {
  listDsApi,
  delDsApi,
  releaseDataApi,
  cancelDataApi,
} from "@/api/ds/api/api.js";
import { getToken } from "@/utils/auth.js";
import DeptTree from "@/components/DeptTree";
import { listAttApiCat } from "@/api/ds/apiCat/apiCat";
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, ref, onActivated, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const {
  ds_api_log_status,
  ds_api_bas_info_api_method_type,
  ds_api_bas_info_res_data_type,
} = proxy.useDict(
  "ds_api_log_status",
  "ds_api_bas_info_api_method_type",
  "ds_api_bas_info_res_data_type"
);

const router = useRouter();
const tableRef = ref(null);
const DeptTreeRef = ref(null);

const deptOptions = ref(undefined);
const leftWidth = ref(300); // Initial left width

const searchStore = reactive({
  items: [
    {
      label: td('ds.api.apiName'),
      prop: "name",
      component: { is: "input", placeholder: td('ds.api.apiNamePlaceholder') }
    },
    {
      label: td('common.texts.status'),
      prop: "status",
      component: {
        is: "select",
        placeholder: td('ds.api.statusPlaceholder'),
        options: ds_api_log_status
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
  func: listDsApi,
  params: {
    name: null,
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
        p.beginCreateTime = p.daterangeCreateTime[0];
        p.endCreateTime = p.daterangeCreateTime[1];
      } else {
        p.beginCreateTime = undefined;
        p.endCreateTime = undefined;
      }
      delete p.daterangeCreateTime;
      return p;
    }
  },
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 60, align: "left", sortable: true },
    { label: td('ds.api.apiName'), prop: "name", width: 260, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.api.serviceCategory'), prop: "catName", width: 160, align: "left", tag: { class: "task-cat-ellipsis" } },
    { label: td('common.texts.description'), prop: "description", width: 256, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.api.apiVersion'), prop: "apiVersion", width: 80, align: "center" },
    { label: td('ds.api.apiPath'), prop: "apiUrl", minWidth: 200, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.api.requestType'), prop: "reqMethod", width: 120, align: "left", dict: "ds_api_bas_info_api_method_type" },
    { label: td('ds.api.returnFormat'), prop: "resDataType", width: 120, align: "left", dict: "ds_api_bas_info_res_data_type" },
    { label: td('common.texts.createdBy'), prop: "createBy", width: 120, align: "center", showOverflowTooltip: { effect: 'light' } },
    { label: td('common.texts.createdTime'), prop: "createTime", width: 160, align: "center", sortable: true, date: true },
    { label: td('common.texts.status'), prop: "status", width: 100, align: "center", slot: "status" },
    { label: td('common.texts.remark'), prop: "remark", width: 256, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('common.texts.operation'), width: 220, align: "center", fixed: "right", slot: "action" }
  ]
});

/*** User import parameters */
const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/ds/dsApi/importData",
});

function handleNodeClick(data) {
  tableStore.params.catCode = data.code || null;
  handleQuery();
}

function getApiCatList() {
  listAttApiCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('ds.api.apiCategory'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}

function handleQuery() {
  tableStore.params.pageNum = 1;
  tableRef.value?.refresh();
}

function resetQuery() {
  DeptTreeRef.value?.resetTree?.();
  tableStore.params.catCode = null;
  tableStore.params.daterangeCreateTime = [];
  handleQuery();
}

/** Enable disable switch */
function handleStatusChange(row) {
  const text = row.status === "1" ? td('ds.api.online') : td('ds.api.offline');
  proxy.$modal
    .confirm(td('ds.api.confirm' + (row.status === "1" ? 'Online' : 'Offline')) + '"' + row.name + '"' + td('ds.api.confirmSuffix2'))
    .then(function () {
      if (row.status === "1") {
        return releaseDataApi(row.id);
      } else {
        return cancelDataApi(row.id);
      }
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + td('common.message.success'));
      tableRef.value.refresh();
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

/** Delete button action */
function handleDelete(row) {
  const _IDs = row.id;
  proxy.$modal
    .confirm(td('ds.api.deleteConfirm') + _IDs + td('ds.api.deleteConfirmSuffix'))
    .then(function () {
      return delDsApi(_IDs);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** ---------------- Import related operations ------------------**/
function handleImport() {
  upload.title = td('ds.api.importTitle');
  upload.open = true;
}

function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `dsApi_template_${new Date().getTime()}.xlsx`
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
    td('ds.api.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.refresh();
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

function routeToAdd(link) {
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
