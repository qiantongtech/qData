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
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
        <el-form-item :label="t('sys.system.roleAuth.userName')" prop="userName">
          <el-input v-model="queryParams.userName" :placeholder="t('sys.system.roleAuth.userNamePlaceholder')" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item :label="t('sys.system.roleAuth.phone')" prop="phonenumber">
          <el-input v-model="queryParams.phonenumber" :placeholder="t('sys.system.roleAuth.phonePlaceholder')" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">{{ t('common.button.query') }}</el-button>
          <el-button icon="Refresh" @click="resetQuery">{{ t('common.button.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="openSelectUser"
              v-hasPermi="['system:role:add']">{{ t('sys.system.roleAuth.addUser') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="CircleClose" :disabled="multiple" @click="cancelAuthUserAll"
              v-hasPermi="['system:role:remove']">{{ t('sys.system.roleAuth.batchCancelAuth') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Close" @click="handleClose">{{ t('common.button.close') }}</el-button>
          </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column :label="t('sys.system.roleAuth.userName')" prop="userName" :show-overflow-tooltip="true" />
        <el-table-column :label="t('sys.system.roleAuth.userNickName')" prop="nickName" :show-overflow-tooltip="true" />
        <el-table-column :label="t('sys.system.roleAuth.email')" prop="email" :show-overflow-tooltip="true" />
        <el-table-column :label="t('sys.system.roleAuth.mobile')" prop="phonenumber" :show-overflow-tooltip="true" />
        <el-table-column :label="t('common.texts.status')" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.createdTime')" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="CircleClose" @click="cancelAuthUser(scope.row)"
              v-hasPermi="['system:role:remove']">{{ t('sys.system.roleAuth.cancelAuth') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize"
        @pagination="getList" />
    </div>
    <select-user ref="selectRef" :roleId="queryParams.roleId" @ok="handleQuery" />
  </div>
</template>

<script setup name="AuthUser">
import { useI18n } from 'vue-i18n'
import selectUser from "./selectUser.vue";
import { allocatedUserList, authUserCancel, authUserCancelAll } from "@/api/system/system/role.js";

const { t } = useI18n();
const route = useRoute();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const userList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const multiple = ref(true);
const total = ref(0);
const userIds = ref([]);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleId: route.params.roleId,
  userName: undefined,
  phonenumber: undefined,
});

/** 查询授权用户列表 */
function getList() {
  loading.value = true;
  allocatedUserList(queryParams).then((response) => {
    userList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 返回按钮 */
function handleClose() {
  const obj = { path: "/system/role" };
  proxy.$tab.closeOpenPage(obj);
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  userIds.value = selection.map((item) => item.userId);
  multiple.value = !selection.length;
}

/** 打开授权用户表弹窗 */
function openSelectUser() {
  proxy.$refs["selectRef"].show();
}

/** 取消授权按钮操作 */
function cancelAuthUser(row) {
  proxy.$modal
    .confirm(t('sys.system.roleAuth.confirmCancelAuth', { name: row.userName }))
    .then(function () {
      return authUserCancel({ userId: row.userId, roleId: queryParams.roleId });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(t('sys.system.roleAuth.cancelAuthSuccess'));
    })
    .catch(() => { });
}

/** 批量取消授权按钮操作 */
function cancelAuthUserAll(row) {
  const roleId = queryParams.roleId;
  const uIds = userIds.value.join(",");
  proxy.$modal
    .confirm(t('sys.system.roleAuth.confirmBatchCancel'))
    .then(function () {
      return authUserCancelAll({ roleId: roleId, userIds: uIds });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(t('sys.system.roleAuth.cancelAuthSuccess'));
    })
    .catch(() => { });
}

getList();
</script>
