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
         <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true">
            <el-form-item :label="td('logininfor.loginAddr')" prop="ipaddr">
               <el-input
                  v-model="queryParams.ipaddr"
                  :placeholder="td('logininfor.loginAddrPlaceholder')"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="td('logininfor.userName')" prop="userName">
               <el-input
                  v-model="queryParams.userName"
                  :placeholder="td('logininfor.userNamePlaceholder')"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="td('common.texts.status')" prop="status">
               <el-select
                  v-model="queryParams.status"
                  :placeholder="td('logininfor.loginStatus')"
                  clearable
                  class="el-form-input-width"
               >
                  <el-option
                     v-for="dict in sys_common_status"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item :label="td('logininfor.loginTime')">
               <el-date-picker
                  class="el-form-input-width"
                  v-model="dateRange"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  type="daterange"
                  range-separator="-"
                  :start-placeholder="td('common.form.startDatePlaceholder')"
                  :end-placeholder="td('common.form.endDatePlaceholder')"
                  :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
               ></el-date-picker>
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
                  type="danger"
                  plain
                  icon="Delete"
                  :disabled="multiple"
                  @click="handleDelete"
                  v-hasPermi="['monitor:logininfor:remove']"
               >{{ td('common.button.delete') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="danger"
                  plain
                  icon="Delete"
                  @click="handleClean"
                  v-hasPermi="['monitor:logininfor:remove']"
               >{{ td('logininfor.clearAll') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="primary"
                  plain
                  icon="Unlock"
                  :disabled="single"
                  @click="handleUnlock"
                  v-hasPermi="['monitor:logininfor:unlock']"
               >{{ td('logininfor.unlock') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="warning"
                  plain
                  icon="Download"
                  @click="handleExport"
                  v-hasPermi="['monitor:logininfor:export']"
               >{{ td('common.button.export') }}</el-button>
            </el-col>
         </el-row>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table stripe height="60vh" ref="logininforRef" v-loading="loading" :data="logininforList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column :label="td('logininfor.accessNo')" align="center" prop="infoId" />
            <el-table-column :label="td('logininfor.userName')" width="120" align="center" prop="userName" :show-overflow-tooltip="true" sortable="custom" :sort-orders="['descending', 'ascending']" />
            <el-table-column :label="td('logininfor.address')" align="center" prop="ipaddr" :show-overflow-tooltip="true" />
            <el-table-column :label="td('logininfor.loginLocation')" align="center" prop="loginLocation" :show-overflow-tooltip="true" />
            <el-table-column :label="td('logininfor.os')" align="center" prop="os" :show-overflow-tooltip="true" />
            <el-table-column :label="td('logininfor.browser')" align="center" prop="browser" :show-overflow-tooltip="true" />
            <el-table-column :label="td('logininfor.loginStatus')" align="center" prop="status">
               <template #default="scope">
                  <dict-tag :options="sys_common_status" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.description')" align="center" prop="msg" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.monitor.logininfor.accessTime')" align="center" prop="loginTime" sortable="custom" :sort-orders="['descending', 'ascending']" width="180">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.loginTime) }}</span>
               </template>
            </el-table-column>
         </el-table>

         <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
         />
      </div>
   </div>
</template>

<script setup name="Logininfor">
import { list, delLogininfor, cleanLogininfor, unlockLogininfor } from "@/api/system/monitor/logininfor.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { sys_common_status } = proxy.useDict("sys_common_status");

const logininforList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const selectName = ref("");
const total = ref(0);
const dateRange = ref([]);
const defaultSort = ref({ prop: "loginTime", order: "descending" });

// 查询参数
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  ipaddr: undefined,
  userName: undefined,
  status: undefined,
  orderByColumn: undefined,
  isAsc: undefined
});

/** 查询登录日志列表 */
function getList() {
  loading.value = true;
  list(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    logininforList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  queryParams.value.pageNum = 1;
  proxy.$refs["logininforRef"].sort(defaultSort.value.prop, defaultSort.value.order);
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.infoId);
  multiple.value = !selection.length;
  single.value = selection.length != 1;
  selectName.value = selection.map(item => item.userName);
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 删除按钮操作 */
function handleDelete(row) {
  const infoIds = row.infoId || ids.value;
  proxy.$modal.confirm(td('logininfor.confirmDelete', { ids: infoIds })).then(function () {
    return delLogininfor(infoIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
  }).catch(() => {});
}

/** 清空按钮操作 */
function handleClean() {
  proxy.$modal.confirm(td('logininfor.confirmClearAll')).then(function () {
    return cleanLogininfor();
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('logininfor.clearSuccess'));
  }).catch(() => {});
}

/** 解锁按钮操作 */
function handleUnlock() {
  const username = selectName.value;
  proxy.$modal.confirm(td('logininfor.confirmUnlock', { name: username })).then(function () {
    return unlockLogininfor(username);
  }).then(() => {
    proxy.$modal.msgSuccess(td('logininfor.unlockSuccess', { name: username }));
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("monitor/logininfor/export", {
    ...queryParams.value,
  }, `config_${new Date().getTime()}.xlsx`);
}

getList();
</script>
