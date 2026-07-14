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
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="td('sys.client.clientId')" prop="id">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.id"
              :placeholder="td('sys.client.clientIdPlaceholder')"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('sys.client.clientName')" prop="name" :label-position="labelPosition">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.name"
              :placeholder="td('sys.client.clientNamePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
          </el-button>
          <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div  class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['auth:client:add']"
            >{{ td('common.button.add') }}</el-button>
          </el-col>
        </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh"  v-loading="loading" :data="clientList" @selection-change="handleSelectionChange">
        <el-table-column :label="td('sys.client.clientId')" align="center" prop="id" />
        <el-table-column :label="td('sys.client.clientSecret')" align="center" prop="secretKey"  width="300"/>
        <el-table-column :label="td('sys.client.clientName')" align="center" prop="name" width="120"/>
        <el-table-column :label="td('sys.client.clientType')" align="center" prop="type">
          <template #default="scope">
            <dict-tag :options="auth_app_type" :value="scope.row.type"/>
          </template>
        </el-table-column>
        <el-table-column :label="td('sys.client.clientIcon')" align="center" prop="icon" width="100">
          <template #default="scope">
            <image-preview :src="scope.row.icon" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column :label="td('sys.client.clientHomepage')" align="center" prop="homeUrl" >
        <template #default="scope">
            <span>{{ scope.row.homeUrl || "-" }}</span>
          </template>
        </el-table-column>
  <!--      <el-table-column :label="td('sys.client.syncUrl')" align="center" prop="syncUrl" />-->
        <el-table-column :label="td('sys.client.authorizedUrl')" align="center" prop="redirectUrl" width="150"/>
        <el-table-column :label="td('sys.client.isPublic')" align="center" prop="publicFlag">
          <template #default="scope">
            <dict-tag :options="auth_public" :value="scope.row.publicFlag"/>
          </template>
        </el-table-column>
        <el-table-column :label="td('sys.client.isActive')" align="center" prop="validFlag">
          <template #default="scope">
            <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.remark')" align="center" prop="remark">
        <template #default="scope">
            <span>{{ scope.row.remark || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['auth:client:edit']">{{ td('common.button.update') }}</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['auth:client:remove']">{{ td('common.button.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>

    <!-- Add or modify the application management dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']"  draggable destroy-on-close>
      <el-form ref="clientRef" :model="form" :rules="rules" label-width="110px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.client.clientHomepage')" prop="homeUrl" :label-position="labelPosition">
              <el-input v-model="form.homeUrl" :placeholder="td('sys.client.clientHomepagePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.client.syncUrl')" prop="syncUrl" :label-position="labelPosition">
              <el-input v-model="form.syncUrl" :placeholder="td('sys.client.syncUrlPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.client.clientName')" prop="name">
              <el-input v-model="form.name" :placeholder="td('sys.client.clientNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.client.clientType')" prop="type" :label-position="labelPosition">
              <el-select v-model="form.type" :placeholder="td('sys.client.selectClientType')">
                <el-option
                    v-for="dict in auth_app_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.client.isPublic')" prop="publicFlag" :label-position="labelPosition">
              <el-radio-group v-model="form.publicFlag">
                <el-radio
                    v-for="dict in auth_public"
                    :key="dict.value"
                    :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.client.isActive')" prop="validFlag" :label-position="labelPosition">
              <el-radio-group v-model="form.validFlag">
                <el-radio
                    v-for="dict in sys_valid"
                    :key="dict.value"
                    :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('sys.client.clientIcon')" prop="icon" :label-position="labelPosition">
              <div class="xgtpcont">
                  <ImageUpload class="sctplist" v-model="form.icon">
                  </ImageUpload>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('sys.client.authorizedUrl')" prop="redirectUrl" :label-position="labelPosition">
              <el-input v-model="form.redirectUrl" type="textarea" :placeholder="td('sys.client.inputContent')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <el-input v-model="form.remark" type="textarea" :placeholder="td('sys.client.inputContent')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Client">
import useDefaultLang from "@/composables/useDefaultLang";
import { listClient, getClient, delClient, addClient, updateClient } from "@/api/system/auth/client.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { auth_app_type } = proxy.useDict('auth_app_type');
const { sys_valid } = proxy.useDict('sys_valid');
const { auth_public } = proxy.useDict('auth_public');

const clientList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: null,
    secretKey: null,
    name: null,
    type: null,
    icon: null,
    homeUrl: null,
    syncUrl: null,
    redirectUrl: null,
    publicFlag: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  },
  rules: {
    secretKey: [
      { required: true, message: td('sys.client.clientSecretRequired'), trigger: "blur" }
    ],
    name: [
      { required: true, message: td('sys.client.clientNameRequired'), trigger: "blur" }
    ],
    type: [
      { required: true, message: td('sys.client.clientTypeRequired'), trigger: "change" }
    ],
    redirectUrl: [
      { required: true, message: td('sys.client.authorizedUrlRequired'), trigger: "blur" }
    ],
    publicFlag: [
      { required: true, message: td('sys.client.isPublicRequired'), trigger: "blur" }
    ],
    validFlag: [
      { required: true, message: td('sys.client.isActiveRequired'), trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** Query application management list */
function getList() {
  loading.value = true;
  listClient(queryParams.value).then(response => {
    clientList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// Cancel button
function cancel() {
  open.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    secretKey: null,
    name: null,
    type: null,
    icon: null,
    homeUrl: null,
    syncUrl: null,
    redirectUrl: null,
    publicFlag: '1',
    validFlag: '1',
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
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
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('sys.client.addTitle');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getClient(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = td('sys.client.editTitle');
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["clientRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateClient(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      } else {
        addClient(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm(td('sys.client.confirmDelete', { id: _ids })).then(function() {
    return delClient(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
  }).catch(() => {});
}

/** Export button action */
function handleExport() {
  proxy.download('auth/client/export', {
    ...queryParams.value
  }, `client_${new Date().getTime()}.xlsx`)
}

getList();
</script>

<style scoped lang="scss">

</style>
