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
            <el-form-item :label="td('sys.system.notice.noticeTitle')" prop="noticeTitle">
               <el-input v-model="queryParams.noticeTitle" :placeholder="td('sys.system.notice.noticeTitlePlaceholder')" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('sys.system.notice.operPerson')" prop="createBy">
               <el-input v-model="queryParams.createBy" :placeholder="td('sys.system.notice.operPersonPlaceholder')" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('sys.system.notice.type')" prop="noticeType">
               <el-select v-model="queryParams.noticeType" :placeholder="td('sys.system.notice.noticeType')" clearable class="el-form-input-width">
                  <el-option v-for="dict in sys_notice_type" :key="dict.value" :label="dict.label"
                     :value="dict.value" />
               </el-select>
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
         <div class="justify-between mb15">
            <el-row :gutter="10" class="btn-style">
               <el-col :span="1.5">
                  <el-button type="primary" plain icon="Plus" @click="handleAdd"
                     v-hasPermi="['system:notice:add']">{{ td('common.button.add') }}</el-button>
               </el-col>
               <!-- <el-col :span="1.5">
               <el-button
                  type="primary"
                  plain
                  icon="Edit"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['system:notice:edit']"
               >{{ td('common.button.update') }}</el-button>
            </el-col> -->
               <el-col :span="1.5">
                  <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                     v-hasPermi="['system:notice:remove']">{{ td('common.button.delete') }}</el-button>
               </el-col>
            </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table stripe v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column :label="td('common.display.index')" align="center" prop="noticeId" width="80" />
            <el-table-column :label="td('sys.system.notice.noticeTitle')" align="center" prop="noticeTitle"
               :show-overflow-tooltip="{ effect: 'light' }" />
            <el-table-column :label="td('sys.system.notice.noticeType')" align="center" prop="noticeType" width="100">
               <template #default="scope">
                  <dict-tag :options="sys_notice_type" :value="scope.row.noticeType" />
               </template>
            </el-table-column>
            <el-table-column :label="td('sys.system.notice.isTop')" align="center" prop="topFlag" width="100">
               <template #default="scope">
                  <dict-tag :options="sys_is_or_not" :value="scope.row.topFlag" />
               </template>
            </el-table-column>
            <el-table-column :label="td('sys.system.notice.popup')" align="center" prop="alertFlag" width="100">
               <template #default="scope">
                  <dict-tag :options="sys_is_or_not" :value="scope.row.alertFlag" />
               </template>
            </el-table-column>
            <el-table-column :label="td('sys.system.notice.effectTime')" align="center" width="220">
               <template #default="scope">
                  <span>
                     {{ parseTime(scope.row.alertStartTime, '{y}-{m}-{d}') }}
                     ~
                     {{ parseTime(scope.row.alertEndTime, '{y}-{m}-{d}') }}
                  </span>
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.status')" align="center" prop="status" width="100">
               <template #default="scope">
                  <dict-tag :options="sys_notice_status" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column :label="td('sys.system.notice.creator')" align="center" prop="createBy" width="100" />
            <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="100">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:notice:edit']">{{ td('common.button.update') }}</el-button>
                  <el-button link type="primary" icon="View" @click="handleView(scope.row)"
                     v-hasPermi="['system:notice:detail']">{{ td('common.button.details') }}</el-button>
                  <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:notice:remove']">{{ td('common.button.delete') }}</el-button>
               </template>
            </el-table-column>
         </el-table>

         <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
      </div>

      <!-- 添加或修改公告对话框 -->
      <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
         destroy-on-close>
         <el-form ref="noticeRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.notice.noticeTitle')" prop="noticeTitle">
                     <el-input v-model="form.noticeTitle" :placeholder="td('sys.system.notice.noticeTitlePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.notice.noticeType')" prop="noticeType">
                     <el-select v-model="form.noticeType" :placeholder="td('sys.system.notice.selectPlaceholder')">
                        <el-option v-for="dict in sys_notice_type" :key="dict.value" :label="dict.label"
                           :value="dict.value"></el-option>
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.notice.effectTime')">
                     <el-date-picker class="el-form-input-width" v-model="dateRange" value-format="YYYY-MM-DD HH:mm:ss"
                        type="daterange" range-separator="-" :start-placeholder="td('common.form.startDatePlaceholder')" :end-placeholder="td('common.form.endDatePlaceholder')"
                        :default-time="[new Date(2000, 1, 1, 0, 0, 0),
                        new Date(2000, 1, 1, 23, 59, 59)]"></el-date-picker>

                     <!--                     <el-date-picker-->
                     <!--                         v-model="dateRange"-->
                     <!--                         type="daterange"-->
                     <!--                         :start-placeholder="td('common.form.startTimePlaceholder')"-->
                     <!--                         :end-placeholder="td('common.form.endTimePlaceholder')"-->
                     <!--                         value-format="YYYY-MM-DD HH:mm:ss"-->
                     <!--                         :default-time="['00:00:00', '23:59:59']"-->
                     <!--                         style="width: 100%;"-->
                     <!--                         :teleported="true"-->
                     <!--                         append-to-body-->
                     <!--                     />-->
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.notice.isTopLabel')" prop="topFlag">
                     <el-radio-group v-model="form.topFlag">
                        <el-radio v-for="dict in sys_is_or_not" :key="dict.value" :value="+dict.value">{{ dict.label
                           }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.notice.isPopupLabel')">
                     <el-radio-group v-model="form.alertFlag">
                        <el-radio v-for="dict in sys_is_or_not" :key="dict.value" :value="+dict.value">{{ dict.label
                           }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('common.texts.status')">
                     <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in sys_notice_status" :key="dict.value" :value="dict.value">{{ dict.label
                           }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>

               <el-col :span="24">
                  <el-form-item :label="td('sys.system.notice.content')">
                     <editor v-model="form.noticeContentText" :min-height="192" :max-height="300" />
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
               <el-button type="primary" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Notice">
import { listNotice, getNoticeOne, delNotice, addNotice, updateNotice } from "@/api/system/system/notice.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { sys_notice_status, sys_notice_type, sys_is_or_not } = proxy.useDict("sys_notice_status", "sys_notice_type", "sys_is_or_not");
const router = useRouter();

const noticeList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);

const data = reactive({
   form: {
   },
   queryParams: {
      pageNum: 1,
      pageSize: 10,
      noticeTitle: undefined,
      createBy: undefined,
      status: undefined
   },
   rules: {
      noticeTitle: [{ required: true, message: td('sys.system.notice.noticeTitleRequired'), trigger: "blur" }],
      // alertFlag: [{ required: true, message: "是否弹窗不能为空", trigger: "blur" }],
      // topFlag: [{ required: true, message: "是否置顶不能为空", trigger: "blur" }],
      noticeType: [{ required: true, message: td('sys.system.notice.noticeTypeRequired'), trigger: "change" }]
   },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询公告列表 */
function getList() {
   loading.value = true;
   listNotice(queryParams.value).then(response => {
      noticeList.value = response.rows;
      total.value = response.total;
      loading.value = false;
   });
}

/** 取消按钮 */
function cancel() {
   open.value = false;
   reset();
}

/** 表单重置 */
function reset() {
   form.value = {
      noticeId: undefined,
      noticeTitle: undefined,
      noticeType: undefined,
      noticeContent: undefined,
      noticeContentText: undefined,
      status: "0",
      topFlag: 0,
      alertFlag: 0,
      alertStartTime: null,
      alertEndTime: null
   };
   dateRange.value = [];
   proxy.resetForm("noticeRef");
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
   ids.value = selection.map(item => item.noticeId);
   single.value = selection.length != 1;
   multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
   reset();
   open.value = true;
   title.value = td('sys.system.notice.addTitle');
}

/**修改按钮操作 */
function handleUpdate(row) {
   reset();
   const noticeId = row.noticeId || ids.value;
   getNoticeOne(noticeId).then(response => {
      form.value = response.data;
      dateRange.value = [
         response.data.alertStartTime,
         response.data.alertEndTime
      ];
      open.value = true;
      title.value = td('sys.system.notice.editTitle');
   });
}

function handleView(row) {
   router.push({ path: '/sys/system/notice/detail', query: { id: row.noticeId } });
}

/** 提交按钮 */
function submitForm() {
   // 将 dateRange 拆分为字段
   form.value.alertStartTime = dateRange.value?.[0] || null;
   form.value.alertEndTime = dateRange.value?.[1] || null;

   proxy.$refs["noticeRef"].validate(valid => {
      if (valid) {
         if (form.value.noticeId != undefined) {
            updateNotice(form.value).then(response => {
               proxy.$modal.msgSuccess(td('common.message.editSuccess'));
               open.value = false;
               getList();
            });
         } else {
            addNotice(form.value).then(response => {
               proxy.$modal.msgSuccess(td('common.message.addSuccess'));
               open.value = false;
               getList();
            });
         }
      }
   });
}

/** 删除按钮操作 */
function handleDelete(row) {
   const noticeIds = row.noticeId || ids.value
   proxy.$modal.confirm(td('sys.system.notice.confirmDelete', { id: noticeIds })).then(function () {
      return delNotice(noticeIds);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
   }).catch(() => { });
}

getList();
</script>
