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
   <!-- 授权用户 -->
   <el-dialog :title="td('sys.system.roleAuth.selectUser')" v-model="visible" width="830px" top="5vh" append-to-body>
      <el-form :model="queryParams" ref="queryRef" :inline="true">
         <el-form-item :label="td('sys.system.roleAuth.userName')" prop="userName">
            <el-input v-model="queryParams.userName" :placeholder="td('sys.system.roleAuth.userNamePlaceholder')" clearable style="width: 180px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item :label="td('sys.system.roleAuth.phone')" prop="phonenumber">
            <el-input v-model="queryParams.phonenumber" :placeholder="td('sys.system.roleAuth.phonePlaceholder')" clearable style="width: 180px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">{{ td('sys.system.roleAuth.searchBtn') }}</el-button>
            <el-button icon="Refresh" @click="resetQuery">{{ td('common.button.reset') }}</el-button>
         </el-form-item>
      </el-form>
      <el-row>
         <el-table @row-click="clickRow" ref="refTable" :data="userList" @selection-change="handleSelectionChange"
            height="260px">
            <el-table-column type="selection" width="55"></el-table-column>
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
         </el-table>
      </el-row>
      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize" @pagination="getList" />
      <template #footer>
         <div class="dialog-footer">
            <el-button type="primary" @click="handleSelectUser">{{ td('common.button.confirm') }}</el-button>
            <el-button @click="visible = false">{{ td('common.button.cancel') }}</el-button>
         </div>
      </template>
   </el-dialog>
</template>

<script setup name="SelectUser">
import useDefaultLang from "@/composables/useDefaultLang";
import { authUserSelectAll, unallocatedUserList } from "@/api/system/system/role.js";

const { td } = useDefaultLang();
const props = defineProps({
   roleId: {
      type: [Number, String]
   }
});

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const userList = ref([]);
const visible = ref(false);
const total = ref(0);
const userIds = ref([]);

const queryParams = reactive({
   pageNum: 1,
   pageSize: 10,
   roleId: undefined,
   userName: undefined,
   phonenumber: undefined
});

// 显示弹框
function show() {
   queryParams.roleId = props.roleId;
   getList();
   visible.value = true;
}

/**选择行 */
function clickRow(row) {
   proxy.$refs["refTable"].toggleRowSelection(row);
}

// 多选框选中数据
function handleSelectionChange(selection) {
   userIds.value = selection.map(item => item.userId);
}

// 查询表数据
function getList() {
   unallocatedUserList(queryParams).then(res => {
      userList.value = res.rows;
      total.value = res.total;
   });
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

const emit = defineEmits(["ok"]);
/** 选择授权用户操作 */
function handleSelectUser() {
   const roleId = queryParams.roleId;
   const uIds = userIds.value.join(",");
   if (uIds == "") {
      proxy.$modal.msgError(td('sys.system.roleAuth.selectAssignUser'));
      return;
   }
   authUserSelectAll({ roleId: roleId, userIds: uIds }).then(res => {
      proxy.$modal.msgSuccess(res.msg);
      visible.value = false;
      emit("ok");
   });
}

defineExpose({
   show,
});
</script>
