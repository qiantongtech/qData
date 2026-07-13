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
    <div class="pagecont-top">
      <h4 class="form-header h4">{{ td('sys.system.user.basicInfo') }}</h4>
      <el-form class="btn-style" :model="form" >
        <el-row>
          <el-col :span="8" :offset="2">
            <el-form-item :label="td('sys.system.user.userNameLabel')" prop="nickName">
              <el-input v-model="form.nickName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="2">
            <el-form-item :label="td('sys.system.user.loginAccount')" prop="userName">
              <el-input v-model="form.userName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <h4 class="form-header h4">{{ td('sys.system.user.roleInfo') }}</h4>
      <el-table stripe height="500px" v-loading="loading" :row-key="getRowKey" @row-click="clickRow" ref="roleRef"
        @selection-change="handleSelectionChange" :data="roles.slice((pageNum - 1) * pageSize, pageNum * pageSize)">
        <el-table-column :label="td('common.display.index')" width="80" type="index" align="center">
          <template #default="scope">
            <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column type="selection" :reserve-selection="true" width="55"></el-table-column>
        <el-table-column :label="td('sys.system.role.roleNo')" align="center" prop="roleId" />
        <el-table-column :label="td('sys.system.role.roleName')" align="center" prop="roleName" />
        <el-table-column :label="td('sys.system.role.permissionChar')" align="center" prop="roleKey" />
        <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="pageNum" v-model:limit="pageSize" />

      <el-form label-width="100px">
        <div style="text-align: center; margin-left: -120px; margin-top: 30px">
          <el-button type="primary" @click="submitForm()">{{ td('common.button.confirm') }}</el-button>
          <el-button @click="close()">{{ td('common.button.return') }}</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup name="AuthRole">
import useDefaultLang from "@/composables/useDefaultLang";
import { getAuthRole, updateAuthRole } from "@/api/system/system/user.js";

const { td } = useDefaultLang();
const route = useRoute();
const { proxy } = getCurrentInstance();

const loading = ref(true);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const roleIds = ref([]);
const roles = ref([]);
const form = ref({
  nickName: undefined,
  userName: undefined,
  userId: undefined,
});

/** Click to select row data */
function clickRow(row) {
  proxy.$refs["roleRef"].toggleRowSelection(row);
}

/** Multiple selection box selected data */
function handleSelectionChange(selection) {
  roleIds.value = selection.map((item) => item.roleId);
}

/** Save selected data number */
function getRowKey(row) {
  return row.roleId;
}

/** close button */
function close() {
  const obj = { path: "/system/user" };
  proxy.$tab.closeOpenPage(obj);
}

/** submit button */
function submitForm() {
  const userId = form.value.userId;
  const rIds = roleIds.value.join(",");
  updateAuthRole({ userId: userId, roleIds: rIds }).then((response) => {
    proxy.$modal.msgSuccess(td('sys.system.user.authSuccess'));
    close();
  });
}

(() => {
  const userId = route.params && route.params.userId;
  if (userId) {
    loading.value = true;
    getAuthRole(userId).then((response) => {
      form.value = response.user;
      roles.value = response.roles;
      total.value = roles.value.length;
      nextTick(() => {
        roles.value.forEach((row) => {
          if (row.flag) {
            proxy.$refs["roleRef"].toggleRowSelection(row);
          }
        });
      });
      loading.value = false;
    });
  }
})();
</script>
