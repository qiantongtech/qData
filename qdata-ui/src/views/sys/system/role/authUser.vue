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
      <el-form class="btn-style" :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
        <el-form-item :label="td('sys.system.roleAuth.userName')" prop="userName">
          <el-input v-model="queryParams.userName" :placeholder="td('sys.system.roleAuth.userNamePlaceholder')" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item :label="td('sys.system.roleAuth.phone')" prop="phonenumber">
          <el-input v-model="queryParams.phonenumber" :placeholder="td('sys.system.roleAuth.phonePlaceholder')" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">{{ td('common.button.query') }}</el-button>
          <el-button icon="Refresh" @click="resetQuery">{{ td('common.button.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="openSelectUser"
              v-hasPermi="['system:role:add']">{{ td('sys.system.roleAuth.addUser') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="CircleClose" :disabled="multiple" @click="cancelAuthUserAll"
              v-hasPermi="['system:role:remove']">{{ td('sys.system.roleAuth.batchCancelAuth') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Close" @click="handleClose">{{ td('common.button.close') }}</el-button>
          </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column :label="td('sys.system.roleAuth.userName')" prop="userName" :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.system.roleAuth.userNickName')" prop="nickName" :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.system.roleAuth.email')" prop="email" :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.system.roleAuth.mobile')" prop="phonenumber" :show-overflow-tooltip="true" />
        <el-table-column :label="td('common.texts.status')" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="CircleClose" @click="cancelAuthUser(scope.row)"
              v-hasPermi="['system:role:remove']">{{ td('sys.system.roleAuth.cancelAuth') }}</el-button>
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
import useDefaultLang from "@/composables/useDefaultLang";
import selectUser from "./selectUser.vue";
import { allocatedUserList, authUserCancel, authUserCancelAll } from "@/api/system/system/role.js";

const { td } = useDefaultLang();
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
    .confirm(td('sys.system.roleAuth.confirmCancelAuth', { name: row.userName }))
    .then(function () {
      return authUserCancel({ userId: row.userId, roleId: queryParams.roleId });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('sys.system.roleAuth.cancelAuthSuccess'));
    })
    .catch(() => { });
}

/** 批量取消授权按钮操作 */
function cancelAuthUserAll(row) {
  const roleId = queryParams.roleId;
  const uIds = userIds.value.join(",");
  proxy.$modal
    .confirm(td('sys.system.roleAuth.confirmBatchCancel'))
    .then(function () {
      return authUserCancelAll({ roleId: roleId, userIds: uIds });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('sys.system.roleAuth.cancelAuthSuccess'));
    })
    .catch(() => { });
}

getList();
</script>
