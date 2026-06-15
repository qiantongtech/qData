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
      <div class="pagecont-top">
         <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true">
            <el-form-item :label="td('sys.monitor.online.loginAddr')" prop="ipaddr">
               <el-input v-model="queryParams.ipaddr" :placeholder="td('sys.monitor.online.loginAddrPlaceholder')" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('sys.monitor.online.userName')" prop="userName">
               <el-input v-model="queryParams.userName" :placeholder="td('sys.monitor.online.userNamePlaceholder')" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
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
      <div class="pagecont-bottom">

         <el-table height="60vh" stripe v-loading="loading"
            :data="onlineList.slice((pageNum - 1) * pageSize, pageNum * pageSize)" style="width: 100%;">
            <el-table-column :label="td('common.display.index')" width="80" type="index" align="center">
               <template #default="scope">
                  <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('sys.monitor.online.sessionId')" align="center" prop="tokenId" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.online.loginName')" align="center" prop="userName" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.online.dept')" align="center" prop="deptName" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.online.host')" align="center" prop="ipaddr" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.online.loginLocation')" align="center" prop="loginLocation" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.online.os')" align="center" prop="os" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.online.browser')" align="center" prop="browser" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.online.loginTime')" align="center" prop="loginTime" width="180">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.loginTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="danger" icon="Delete" @click="handleForceLogout(scope.row)"
                     v-hasPermi="['monitor:online:forceLogout']">{{ td('sys.monitor.online.forceLogout') }}</el-button>
               </template>
            </el-table-column>
         </el-table>

         <pagination v-show="total > 0" :total="total" v-model:page="pageNum" v-model:limit="pageSize" />
      </div>
   </div>
</template>

<script setup name="Online">
import useDefaultLang from "@/composables/useDefaultLang";
import { forceLogout, list as initData } from "@/api/system/monitor/online.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const onlineList = ref([]);
const loading = ref(true);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

const queryParams = ref({
   ipaddr: undefined,
   userName: undefined
});

/** 查询登录日志列表 */
function getList() {
   loading.value = true;
   initData(queryParams.value).then(response => {
      onlineList.value = response.rows;
      total.value = response.total;
      loading.value = false;
   });
}

/** 搜索按钮操作 */
function handleQuery() {
   pageNum.value = 1;
   getList();
}

/** 重置按钮操作 */
function resetQuery() {
   proxy.resetForm("queryRef");
   handleQuery();
}

/** 强退按钮操作 */
function handleForceLogout(row) {
   proxy.$modal.confirm(td('sys.monitor.online.confirmForceLogout', { name: row.userName })).then(function () {
      return forceLogout(row.tokenId);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
   }).catch(() => { });
}

getList();
</script>

<style scoped lang="scss"></style>
