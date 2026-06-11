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
         <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item :label="t('sys.monitor.jobLog.taskName')" prop="jobName">
               <el-input
                  v-model="queryParams.jobName"
                  :placeholder="t('sys.monitor.jobLog.taskNamePlaceholder')"
                  clearable
                  style="width: 240px"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="t('sys.monitor.jobLog.taskGroup')" prop="jobGroup">
               <el-select
                  v-model="queryParams.jobGroup"
                  :placeholder="t('sys.monitor.jobLog.taskGroupPlaceholder')"
                  clearable
                  style="width: 240px"
               >
                  <el-option
                     v-for="dict in sys_job_group"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item :label="t('sys.monitor.jobLog.execStatus')" prop="status">
               <el-select
                  v-model="queryParams.status"
                  :placeholder="t('sys.monitor.jobLog.execStatusPlaceholder')"
                  clearable
                  style="width: 240px"
               >
                  <el-option
                     v-for="dict in sys_common_status"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item :label="t('sys.monitor.jobLog.execTime')" style="width: 308px">
               <el-date-picker
                  v-model="dateRange"
                  value-format="YYYY-MM-DD"
                  type="daterange"
                  range-separator="-"
                  :start-placeholder="td('common.form.startDatePlaceholder')"
                  :end-placeholder="td('common.form.endDatePlaceholder')"
               ></el-date-picker>
            </el-form-item>
            <el-form-item>
               <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ t('common.button.query') }}
               </el-button>
               <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
                  <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ t('common.button.reset') }}
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
                  v-hasPermi="['monitor:job:remove']"
               >{{ t('common.button.delete') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="danger"
                  plain
                  icon="Delete"
                  @click="handleClean"
                  v-hasPermi="['monitor:job:remove']"
               >{{ t('sys.monitor.jobLog.clearAll') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="warning"
                  plain
                  icon="Download"
                  @click="handleExport"
                  v-hasPermi="['monitor:job:export']"
               >{{ t('common.button.export') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="warning"
                  plain
                  icon="Close"
                  @click="handleClose"
               >{{ t('common.button.close') }}</el-button>
            </el-col>
         </el-row>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table stripe height="60vh" v-loading="loading" :data="jobLogList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column :label="t('sys.monitor.jobLog.logNo')" width="80" align="center" prop="jobLogId" />
            <el-table-column :label="t('sys.monitor.jobLog.taskName')" align="center" prop="jobName" :show-overflow-tooltip="true" />
            <el-table-column :label="t('sys.monitor.jobLog.taskGroup')" align="center" prop="jobGroup" :show-overflow-tooltip="true">
               <template #default="scope">
                  <dict-tag :options="sys_job_group" :value="scope.row.jobGroup" />
               </template>
            </el-table-column>
            <el-table-column :label="t('sys.monitor.jobLog.invokeTarget')" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
            <el-table-column :label="t('sys.monitor.jobLog.logInfo')" align="center" prop="jobMessage" :show-overflow-tooltip="true" />
            <el-table-column :label="t('sys.monitor.jobLog.execStatus')" align="center" prop="status">
               <template #default="scope">
                  <dict-tag :options="sys_common_status" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column :label="t('sys.monitor.jobLog.execTime')" align="center" prop="createTime" width="180">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width">
               <template #default="scope">
                  <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['monitor:job:query']">{{ t('sys.monitor.jobLog.detail') }}</el-button>
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

      <!-- 调度日志详细 -->
      <el-dialog :title="t('sys.monitor.jobLog.scheduleLogDetail')" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
         <el-form :model="form" label-width="80px">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.jobLog.logIndex')">
                     <div class="form-value-ifon">{{ form.jobLogId }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.jobLog.taskName')">
                     <div class="form-value-ifon">{{ form.jobName }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.jobLog.taskGroupLabel')">
                     <div class="form-value-ifon">{{ form.jobGroup }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.jobLog.execTime')">
                     <div class="form-value-ifon">{{ form.createTime }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item :label="t('sys.monitor.jobLog.invokeMethod')">
                     <div class="form-value-ifon">{{ form.invokeTarget }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item :label="t('sys.monitor.jobLog.logInfo')">
                     <div class="form-value-ifon">{{ form.jobMessage }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item :label="t('sys.monitor.jobLog.execStatus')">
                     <div class="form-value-ifon" v-if="form.status == 0">{{ t('sys.monitor.jobLog.normal') }}</div>
                     <div class="form-value-ifon" v-else-if="form.status == 1">{{ t('sys.monitor.jobLog.failed') }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item :label="t('sys.monitor.jobLog.exceptionInfo')" v-if="form.status == 1">
                     <div class="form-value-ifon">{{ form.exceptionInfo }}</div>
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="open = false">{{ t('common.button.close') }}</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="JobLog">
import { useI18n } from 'vue-i18n'
import { getJob } from "@/api/system/monitor/job.js";
import { listJobLog, delJobLog, cleanJobLog } from "@/api/system/monitor/jobLog.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { sys_common_status, sys_job_group } = proxy.useDict("sys_common_status", "sys_job_group");

const jobLogList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref([]);
const route = useRoute();

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dictName: undefined,
    dictType: undefined,
    status: undefined
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询调度日志列表 */
function getList() {
  loading.value = true;
  listJobLog(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    jobLogList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 返回按钮
function handleClose() {
  const obj = { path: "/monitor/job" };
  proxy.$tab.closeOpenPage(obj);
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
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.jobLogId);
  multiple.value = !selection.length;
}

/** 详细按钮操作 */
function handleView(row) {
  open.value = true;
  form.value = row;
}

/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal.confirm(t('sys.monitor.jobLog.confirmDelete', { ids: ids.value })).then(function () {
    return delJobLog(ids.value);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
  }).catch(() => {});
}

/** 清空按钮操作 */
function handleClean() {
  proxy.$modal.confirm(t('sys.monitor.jobLog.confirmClearAll')).then(function () {
    return cleanJobLog();
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(t('sys.monitor.jobLog.clearSuccess'));
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("monitor/jobLog/export", {
    ...queryParams.value,
  }, `job_log_${new Date().getTime()}.xlsx`);
}

(() => {
  const jobId = route.params && route.params.jobId;
  if (jobId !== undefined && jobId != 0) {
    getJob(jobId).then(response => {
      queryParams.value.jobName = response.data.jobName;
      queryParams.value.jobGroup = response.data.jobGroup;
      getList();
    });
  } else {
    getList();
  }
})();
</script>
<style scoped lang="scss">
.form-value-ifon{
   width:100%;
   border:1px solid #c0c4cc;
   padding: 0px 10px;
   min-height: 34px;
}

</style>
