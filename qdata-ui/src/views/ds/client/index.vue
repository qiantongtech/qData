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
              @click="handleAdd"
              v-hasPermi="['att:client:add']"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
        </el-row>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
        <template #logo="{ row }">
          <div class="clientInfo">
            <div>
              <image-preview :src="row.logo || noDataImg" :width="50" :height="50" />
            </div>
          </div>
        </template>
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['att:client:edit']"
            >{{ td('common.button.update') }}</el-button
          >
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetail(row)"
            v-hasPermi="['att:client:query']"
            >{{ td('common.button.details') }}</el-button
          >
          <el-popover placement="bottom" :width="150" trigger="click">
            <template #reference>
              <el-button link type="primary" icon="ArrowDown">{{ td('common.button.more') }}</el-button>
            </template>
            <div style="width: 100px" class="butgdlist">
              <el-button
                link
                style="padding-left: 14px"
                type="primary"
                icon="Refresh"
                @click="handleReset(row)"
                v-hasPermi="['att:client:edit']"
                >{{ td('ds.client.resetSecret') }}</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['att:client:remove']"
                >{{ td('common.button.delete') }}</el-button
              >
            </div>
          </el-popover>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Add or modify application dialog box -->
    <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="clientRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.client.appName')" prop="name" :label-position="labelPosition">
              <el-input v-model="form.name" :placeholder="td('ds.client.appNamePlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
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
            <el-form-item :label="td('ds.client.appIcon')" prop="logo" :label-position="labelPosition">
              <image-upload v-model="form.logo" limit="1" :fileType="pdf" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag" :label-position="labelPosition">
              <el-radio-group v-model="form.publicFlag">
                <el-radio v-for="dict in auth_public" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
           <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <el-input v-model="form.description" type="textarea"  maxlength="256字符" show-word-limit
 :placeholder="td('common.form.descriptionPlaceholder')" />
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
          <el-col :span="24">
            <el-form-item :label="td('ds.client.details.id')" prop="id">
              <div>{{ form.id || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.client.appSecret')" prop="secret" :label-position="labelPosition">
              <div>{{ form.secret || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.client.appName')" prop="name">
              <div>{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.client.appIcon')" prop="logo" :label-position="labelPosition">
              <image-preview :src="form.logo || noDataImg" :width="50" :height="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.client.appType')" prop="type">
              <dict-tag :options="auth_app_type" :value="form.type" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag">
              <dict-tag :options="auth_public" :value="form.publicFlag" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.client.homepageUrl')" prop="homepageUrl" :label-position="labelPosition">
              <div>{{ form.homepageUrl || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('ds.client.syncUrl')" prop="syncUrl" :label-position="labelPosition">
              <div>{{ form.syncUrl || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
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
import { reactive, ref, onActivated, getCurrentInstance, toRefs } from "vue";
import { useRouter } from "vue-router";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { auth_public, auth_app_type } = proxy.useDict(
  "auth_public",
  "auth_app_type"
);
const noDataImg = new URL('../../../assets/system/images/D.png', import.meta.url).href;

const tableRef = ref(null);
const router = useRouter();

const open = ref(false);
const openDetail = ref(false);
const title = ref("");

const searchStore = reactive({
  items: [
    {
      label: td('ds.client.appName'),
      prop: "name",
      component: { is: "input", placeholder: td('ds.client.appNamePlaceholder') }
    },
    {
      label: td('ds.client.appType'),
      prop: "type",
      component: {
        is: "select",
        placeholder: td('ds.client.appTypePlaceholder'),
        options: auth_app_type
      }
    },
    {
      label: td('ds.client.isPublic'),
      prop: "publicFlag",
      component: {
        is: "select",
        placeholder: td('ds.client.isPublicPlaceholder'),
        options: auth_public
      }
    }
  ]
});

const tableStore = reactive({
  func: listClient,
  params: {
    name: null,
    type: null,
    publicFlag: null,
    orderByColumn: "create_time",
    isAsc: "desc"
  },
  config: {
    initResquest: true,
    beforeRequest: (params) => {
      let p = { ...params };
      if (p.orderByColumn === 'createTime') {
        p.orderByColumn = 'create_time';
      }
      return p;
    }
  },
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 60, align: "left", sortable: true },
    { label: td('ds.client.appName'), prop: "name", width: 260, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.client.appType'), prop: "type", width: 120, align: "center", dict: "auth_app_type" },
    { label: td('common.texts.description'), prop: "description", width: 256, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.client.appIcon'), prop: "logo", width: 100, align: "left", slot: "logo" },
    { label: td('ds.client.isPublic'), prop: "publicFlag", width: 100, align: "center", dict: "auth_public" },
    { label: td('common.texts.createdBy'), prop: "createBy", width: 120, align: "center" },
    { label: td('common.texts.createdTime'), prop: "createTime", width: 160, align: "center", sortable: "custom", date: true },
    { label: td('common.texts.remark'), prop: "remark", width: 256, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('common.texts.operation'), width: 280, align: "center", fixed: "right", slot: "action" }
  ]
});

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('ds.client.appNameRequired'), trigger: "blur" }],
    type: [{ required: true, message: td('ds.client.appTypeRequired'), trigger: "change" }],
  },
});

const { form, rules } = toRefs(data);

/*** User import parameters */
const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/att/client/importData",
});

function handleQuery() {
  tableStore.params.pageNum = 1;
}

function resetQuery() {
  tableStore.params.name = null;
  tableStore.params.type = null;
  tableStore.params.publicFlag = null;
}

function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

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

function handleAdd() {
  reset();
  open.value = true;
  title.value = td('ds.client.addApp');
  form.value.publicFlag = "1";
}

function handleUpdate(row) {
  reset();
  const _id = row.id;
  getClient(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('ds.client.editApp');
  });
}

function handleReset(row) {
  const _id = row.id;
  proxy.$modal
    .confirm(td('ds.client.resetSecretConfirm'))
    .then(function () {
      resetSecret(_id).then((res) => {
        proxy.$modal.msgSuccess(td('ds.client.newSecret') + res.data);
        tableRef.value.refresh();
      });
    });
}

function handleDetail(row) {
  routeTo("/ds/client/clientDetail", row);
}

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
            tableRef.value.refresh();
            submitLoading.value = false;
          })
          .catch((error) => { submitLoading.value = false; });
      } else {
        addClient(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.client.addSuccess'));
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
  const _ids = row.id;
  proxy.$modal
    .confirm(td('ds.client.deleteConfirm') + _ids + td('ds.client.deleteConfirmSuffix'))
    .then(function () {
      return delClient(_ids);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** ---------------- Import related operations ------------------**/
function handleImport() {
  upload.title = td('ds.client.importTitle');
  upload.open = true;
}

function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `client_template_${new Date().getTime()}.xlsx`
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
    td('ds.client.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.refresh();
};

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

onActivated(() => {
  tableRef.value?.refresh();
});

</script>

<style scoped lang="scss">
.clientInfo {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
</style>
