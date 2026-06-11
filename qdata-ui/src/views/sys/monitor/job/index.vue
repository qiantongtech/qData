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
         <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
            <el-form-item :label="t('sys.monitor.job.taskName')" prop="jobName">
               <el-input
                  v-model="queryParams.jobName"
                  :placeholder="t('sys.monitor.job.taskNamePlaceholder')"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="t('sys.monitor.job.taskGroup')" prop="jobGroup">
               <el-select v-model="queryParams.jobGroup" :placeholder="t('sys.monitor.job.taskGroupPlaceholder')" clearable class="el-form-input-width">
                  <el-option
                     v-for="dict in sys_job_group"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item :label="t('sys.monitor.job.taskStatus')" prop="status">
               <el-select v-model="queryParams.status" :placeholder="t('sys.monitor.job.taskStatusPlaceholder')" clearable class="el-form-input-width">
                  <el-option
                     v-for="dict in sys_job_status"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
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
                  type="primary"
                  plain
                  icon="Plus"
                  @click="handleAdd"
                  v-hasPermi="['monitor:job:add']"
               >{{ t('common.button.add') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="primary"
                  plain
                  icon="Edit"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['monitor:job:edit']"
               >{{ t('common.button.update') }}</el-button>
            </el-col>
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
                  type="warning"
                  plain
                  icon="Download"
                  @click="handleExport"
                  v-hasPermi="['monitor:job:export']"
               >{{ t('common.button.export') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="info"
                  plain
                  icon="Operation"
                  @click="handleJobLog"
                  v-hasPermi="['monitor:job:query']"
               >{{ t('sys.monitor.job.log') }}</el-button>
            </el-col>
         </el-row>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table stripe height="60vh" v-loading="loading" :data="jobList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column :label="t('sys.monitor.job.taskNo')" width="100" align="center" prop="jobId" />
            <el-table-column :label="t('sys.monitor.job.taskName')" align="center" prop="jobName" :show-overflow-tooltip="true" />
            <el-table-column :label="t('sys.monitor.job.taskGroup')" align="center" prop="jobGroup">
               <template #default="scope">
                  <dict-tag :options="sys_job_group" :value="scope.row.jobGroup" />
               </template>
            </el-table-column>
            <el-table-column :label="t('sys.monitor.job.invokeTarget')" align="center" prop="invokeTarget" :show-overflow-tooltip="true" width="300px"/>
            <el-table-column :label="t('sys.monitor.job.cronExpression')" align="center" prop="cronExpression" :show-overflow-tooltip="true" width="300px"/>
            <el-table-column :label="t('common.display.status')" align="center">
               <template #default="scope">
                  <el-switch
                     v-model="scope.row.status"
                     active-value="0"
                     inactive-value="1"
                     @change="handleStatusChange(scope.row)"
                  ></el-switch>
               </template>
            </el-table-column>
            <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <!-- <el-tooltip :content="t('common.button.update')" placement="top">
                     <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['monitor:job:edit']"></el-button>
                  </el-tooltip>
                  <el-tooltip :content="t('common.button.delete')" placement="top">
                     <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['monitor:job:remove']"></el-button>
                  </el-tooltip>
                  <el-tooltip :content="t('sys.monitor.job.executeOnce')" placement="top">
                     <el-button link type="primary" icon="CaretRight" @click="handleRun(scope.row)" v-hasPermi="['monitor:job:changeStatus']"></el-button>
                  </el-tooltip>
                  <el-tooltip :content="t('sys.monitor.job.taskDetail')" placement="top">
                     <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['monitor:job:query']"></el-button>
                  </el-tooltip>
                  <el-tooltip :content="t('sys.monitor.job.scheduleLog')" placement="top">
                     <el-button link type="primary" icon="Operation" @click="handleJobLog(scope.row)" v-hasPermi="['monitor:job:query']"></el-button>
                  </el-tooltip> -->
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['monitor:job:edit']">{{ t('common.button.update') }}</el-button>
                  <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['monitor:job:remove']">{{ t('common.button.delete') }}</el-button>
                  <el-popover  placement="bottom" :width="150" trigger="click">
                     <template #reference>
                        <el-button link type="primary"  icon="View">{{ t('common.button.more') }}</el-button>
                     </template>
                     <div style="width: 90px;" class="butgdlist">
                        <el-button style="padding-left: 14px;" link type="primary" icon="CaretRight" @click="handleRun(scope.row)" v-hasPermi="['monitor:job:changeStatus']">{{ t('sys.monitor.job.executeOnce') }}</el-button>
                        <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['monitor:job:query']">{{ t('sys.monitor.job.taskDetail') }}</el-button>
                        <el-button link type="primary" icon="Operation" @click="handleJobLog(scope.row)" v-hasPermi="['monitor:job:query']">{{ t('sys.monitor.job.scheduleLog') }}</el-button>
                     </div>
                  </el-popover>
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

      <!-- 添加或修改定时任务对话框 -->
      <el-dialog :title="title" v-model="open" width="850px" :append-to="$refs['app-container']" draggable destroy-on-close>
         <el-form ref="jobRef" :model="form" :rules="rules" label-width="100px">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.taskName')" prop="jobName">
                     <el-input v-model="form.jobName" :placeholder="t('sys.monitor.job.taskNamePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.taskGroupLabel')" prop="jobGroup">
                     <el-select v-model="form.jobGroup" :placeholder="t('sys.monitor.job.selectPlaceholder')">
                        <el-option
                           v-for="dict in sys_job_group"
                           :key="dict.value"
                           :label="dict.label"
                           :value="dict.value"
                        ></el-option>
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item prop="invokeTarget">
                     <template #label>
                        <span>
                           {{ t('sys.monitor.job.invokeMethod') }}
                           <el-tooltip placement="top">
                              <template #content>
                                 <div>
                                    {{ t('sys.monitor.job.beanExample') }}
                                    <br />{{ t('sys.monitor.job.classExample') }}
                                    <br />{{ t('sys.monitor.job.paramDesc') }}
                                 </div>
                              </template>
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                        </span>
                     </template>
                     <el-input v-model="form.invokeTarget" :placeholder="t('sys.monitor.job.invokeTargetPlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.cronExprLabel')" prop="cronExpression">
                     <el-input v-model="form.cronExpression" :placeholder="t('sys.monitor.job.cronExprPlaceholder')">
                        <template #append>
                           <el-button type="primary" @click="handleShowCron">
                              {{ t('sys.monitor.job.generate') }}
                              <i class="el-icon-time el-icon--right"></i>
                           </el-button>
                        </template>
                     </el-input>
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.jobId !== undefined">
                  <el-form-item :label="t('common.texts.status')">
                     <el-radio-group v-model="form.status">
                        <el-radio
                           v-for="dict in sys_job_status"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.execStrategy')" prop="misfirePolicy">
                     <el-radio-group v-model="form.misfirePolicy">
                        <el-radio-button value="1">{{ t('sys.monitor.job.immediateExec') }}</el-radio-button>
                        <el-radio-button value="2">{{ t('sys.monitor.job.executeOnce') }}</el-radio-button>
                        <el-radio-button value="3">{{ t('sys.monitor.job.giveUpExec') }}</el-radio-button>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.concurrent')" prop="concurrent">
                     <el-radio-group v-model="form.concurrent">
                        <el-radio-button value="0">{{ t('sys.monitor.job.allow') }}</el-radio-button>
                        <el-radio-button value="1">{{ t('sys.monitor.job.forbid') }}</el-radio-button>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="cancel">{{ t('common.button.cancel') }}</el-button>
               <el-button type="primary" @click="submitForm">{{ t('common.button.confirm') }}</el-button>
            </div>
         </template>
      </el-dialog>

     <el-dialog :title="t('sys.monitor.job.cronGenerator')" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close>
       <crontab ref="crontabRef" @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
     </el-dialog>

      <!-- 任务日志详细 -->
      <el-dialog :title="t('sys.monitor.job.taskDetail')" v-model="openView" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
         <el-form :model="form" label-width="120px">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.taskNoLabel')">
                     <div class="form-value-ifon">{{ form.jobId }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.taskNameLabel')">
                     <div class="form-value-ifon">{{ form.jobName }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.taskGroupLabel2')">
                     <div class="form-value-ifon">{{ jobGroupFormat(form) }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('common.display.createTime') + '：'">
                     <div class="form-value-ifon">{{ form.createTime }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.cronExprLabel2')">
                     <div class="form-value-ifon">{{ form.cronExpression }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.nextExecTime')">
                     <div class="form-value-ifon">{{ parseTime(form.nextValidTime) }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item :label="t('sys.monitor.job.invokeTargetMethod')">
                     <div class="form-value-ifon">{{ form.invokeTarget }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.taskStatusLabel')">
                     <div class="form-value-ifon" v-if="form.status == 0">{{ t('sys.monitor.job.normal') }}</div>
                     <div class="form-value-ifon" v-else-if="form.status == 1">{{ t('sys.monitor.job.paused') }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.concurrentLabel')">
                     <div class="form-value-ifon" v-if="form.concurrent == 0">{{ t('sys.monitor.job.allow') }}</div>
                     <div class="form-value-ifon" v-else-if="form.concurrent == 1">{{ t('sys.monitor.job.forbid') }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.monitor.job.execStrategy') + '：'">
                     <div class="form-value-ifon" v-if="form.misfirePolicy == 0">{{ t('sys.monitor.job.defaultStrategy') }}</div>
                     <div class="form-value-ifon" v-else-if="form.misfirePolicy == 1">{{ t('sys.monitor.job.immediateExec') }}</div>
                     <div class="form-value-ifon" v-else-if="form.misfirePolicy == 2">{{ t('sys.monitor.job.executeOnce') }}</div>
                     <div class="form-value-ifon" v-else-if="form.misfirePolicy == 3">{{ t('sys.monitor.job.giveUpExec') }}</div>
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="openView = false">{{ t('common.button.close') }}</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Job">
import { useI18n } from 'vue-i18n'
import { listJob, getJob, delJob, addJob, updateJob, runJob, changeJobStatus } from "@/api/system/monitor/job.js";
import Crontab from '@/components/Crontab/index.vue'

const { t } = useI18n();
const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_job_group, sys_job_status } = proxy.useDict("sys_job_group", "sys_job_status");

const jobList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const openView = ref(false);
const openCron = ref(false);
const expression = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobName: undefined,
    jobGroup: undefined,
    status: undefined
  },
  rules: {
    jobName: [{ required: true, message: t('sys.monitor.job.taskNameRequired'), trigger: "blur" }],
    invokeTarget: [{ required: true, message: t('sys.monitor.job.invokeTargetRequired'), trigger: "blur" }],
    cronExpression: [{ required: true, message: t('sys.monitor.job.cronExprRequired'), trigger: "change" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询定时任务列表 */
function getList() {
  loading.value = true;
  listJob(queryParams.value).then(response => {
    jobList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 任务组名字典翻译 */
function jobGroupFormat(row, column) {
  return proxy.selectDictLabel(sys_job_group.value, row.jobGroup);
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    jobId: undefined,
    jobName: undefined,
    jobGroup: undefined,
    invokeTarget: undefined,
    cronExpression: undefined,
    misfirePolicy: 1,
    concurrent: 1,
    status: "0"
  };
  proxy.resetForm("jobRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.jobId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

// 更多操作触发
function handleCommand(command, row) {
  switch (command) {
    case "handleRun":
      handleRun(row);
      break;
    case "handleView":
      handleView(row);
      break;
    case "handleJobLog":
      handleJobLog(row);
      break;
    default:
      break;
  }
}

// 任务状态修改
function handleStatusChange(row) {
  let text = row.status === "0" ? t('sys.monitor.job.enable') : t('sys.monitor.job.disable');
  proxy.$modal.confirm(t('sys.monitor.job.confirmStatusChange', { text: text, name: row.jobName })).then(function () {
    return changeJobStatus(row.jobId, row.status);
  }).then(() => {
    proxy.$modal.msgSuccess(text + t('common.message.success'));
  }).catch(function () {
    row.status = row.status === "0" ? "1" : "0";
  });
}

/* 立即执行一次 */
function handleRun(row) {
  proxy.$modal.confirm(t('sys.monitor.job.confirmImmediateExec', { name: row.jobName })).then(function () {
    return runJob(row.jobId, row.jobGroup);
  }).then(() => {
    proxy.$modal.msgSuccess(t('sys.monitor.job.execSuccess'));})
  .catch(() => {});
}

/** 任务详细信息 */
function handleView(row) {
  getJob(row.jobId).then(response => {
    form.value = response.data;
    openView.value = true;
  });
}

/** cron表达式按钮操作 */
function handleShowCron() {
  expression.value = form.value.cronExpression;
  openCron.value = true;
}

/** 确定后回传值 */
function crontabFill(value) {
  form.value.cronExpression = value;
}

/** 任务日志列表查询 */
function handleJobLog(row) {
  const jobId = row.jobId || 0;
  router.push('/monitor/job-log/index/' + jobId)
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = t('sys.monitor.job.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const jobId = row.jobId || ids.value;
  getJob(jobId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = t('sys.monitor.job.editTitle');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["jobRef"].validate(valid => {
    if (valid) {
      if (form.value.jobId != undefined) {
        updateJob(form.value).then(response => {
          proxy.$modal.msgSuccess(t('common.message.editSuccess'));
          open.value = false;
          getList();
        });
      } else {
        addJob(form.value).then(response => {
          proxy.$modal.msgSuccess(t('common.message.addSuccess'));
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobIds = row.jobId || ids.value;
  proxy.$modal.confirm(t('sys.monitor.job.confirmDelete', { ids: jobIds })).then(function () {
    return delJob(jobIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("monitor/job/export", {
    ...queryParams.value,
  }, `job_${new Date().getTime()}.xlsx`);
}

getList();
</script>
