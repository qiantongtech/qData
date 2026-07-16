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
            <el-form-item :label="td('sys.system.dept.deptName')" prop="deptName" :label-position="labelPosition">
               <el-input v-model="queryParams.deptName" :placeholder="td('sys.system.dept.deptNamePlaceholder')" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('common.texts.status')" prop="status">
               <el-select v-model="queryParams.status" :placeholder="td('sys.system.dept.deptStatus')" clearable class="el-form-input-width">
                  <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
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
                     v-hasPermi="['system:dept:add']">{{ td('common.button.add') }}</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button type="info" plain icon="Sort" @click="toggleExpandAll">{{ td('common.button.un_fold') }}</el-button>
               </el-col>
            </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table v-if="refreshTable" height="60vh" v-loading="loading" :data="deptList" row-key="deptId"
            :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
            <el-table-column prop="deptName" :label="td('sys.system.dept.deptName')" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="orderNum" :label="td('sys.system.dept.sort')"></el-table-column>
            <el-table-column prop="status" :label="td('common.texts.status')">
               <template #default="scope">
                  <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="160">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:dept:edit']">{{ td('common.button.update') }}</el-button>
                  <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                     v-hasPermi="['system:dept:add']">{{ td('common.button.add') }}</el-button>
                  <el-button v-if="scope.row.parentId != 0" link type="danger" icon="Delete"
                     @click="handleDelete(scope.row)" v-hasPermi="['system:dept:remove']">{{ td('common.button.delete') }}</el-button>
               </template>
            </el-table-column>
         </el-table>
      </div>

      <!-- Add or modify department dialog box -->
      <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
         destroy-on-close>
         <el-form ref="deptRef" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
            <el-row :gutter="20">
               <el-col :span="24" v-if="form.parentId !== 0">
                  <el-form-item :label="td('sys.system.dept.parentDept')" prop="parentId" :label-position="labelPosition">
                     <el-tree-select v-model="form.parentId" :data="deptOptions"
                        :props="{ value: 'deptId', label: 'deptName', children: 'children' }" value-key="deptId"
                        :placeholder="td('sys.system.dept.selectParentDept')" check-strictly />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dept.deptName')" prop="deptName">
                     <el-input v-model="form.deptName" :placeholder="td('sys.system.dept.deptNamePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dept.showSort')" prop="orderNum" :label-position="labelPosition">
                     <el-input-number style="width:100%" v-model="form.orderNum" controls-position="right" :min="0" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dept.leader')" prop="leader" :label-position="labelPosition">
                     <el-input v-model="form.leader" :placeholder="td('sys.system.dept.leaderPlaceholder')" maxlength="20" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dept.contactPhone')" prop="phone" :label-position="labelPosition">
                     <el-input v-model="form.phone" :placeholder="td('sys.system.dept.contactPhonePlaceholder')" maxlength="11" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dept.email')" prop="email" :label-position="labelPosition">
                     <el-input v-model="form.email" :placeholder="td('sys.system.dept.emailPlaceholder')" maxlength="50" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dept.deptStatus')" :label-position="labelPosition">
                     <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label
                           }}</el-radio>
                     </el-radio-group>
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

<script setup name="Dept">
import useDefaultLang from "@/composables/useDefaultLang";
import { listDept, getDept, delDept, addDept, updateDept, listDeptExcludeChild } from "@/api/system/system/dept.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const deptList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const deptOptions = ref([]);
const isExpandAll = ref(true);
const refreshTable = ref(true);
const data = reactive({
   form: {},
   queryParams: {
      deptName: undefined,
      status: undefined
   },
   rules: {
      parentId: [{ required: true, message: td('sys.system.dept.parentDeptRequired'), trigger: "blur" }],
      deptName: [{ required: true, message: td('sys.system.dept.deptNameRequired'), trigger: "blur" }],
      orderNum: [{ required: true, message: td('sys.system.dept.showSortRequired'), trigger: "blur" }],
      email: [{ type: "email", message: td('sys.system.dept.emailRequired'), trigger: ["blur", "change"] }],
      phone: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: td('sys.system.dept.phoneRequired'), trigger: "blur" }]
   },
});

const { queryParams, form, rules } = toRefs(data);

/** Query department list */
function getList() {
   loading.value = true;
   listDept(queryParams.value).then(response => {
      deptList.value = proxy.handleTree(response.data, "deptId");
      loading.value = false;
   });

}

/** Cancel button */
function cancel() {
   open.value = false;
   reset();
}

/** form reset */
function reset() {
   form.value = {
      deptId: undefined,
      parentId: undefined,
      deptName: undefined,
      orderNum: 0,
      leader: undefined,
      phone: undefined,
      email: undefined,
      status: "0"
   };
   proxy.resetForm("deptRef");
}

/** Search button action */
function handleQuery() {
   getList();
}

/** reset button action */
function resetQuery() {
   proxy.resetForm("queryRef");
   handleQuery();
}

/** Add button operation */
function handleAdd(row) {
   reset();
   listDept().then(response => {
      deptOptions.value = proxy.handleTree(response.data, "deptId");
   });
   if (row != undefined) {
      form.value.parentId = row.deptId;
   }
   open.value = true;
   title.value = td('sys.system.dept.addTitle');
}

/** Expand/collapse operations */
function toggleExpandAll() {
   refreshTable.value = false;
   isExpandAll.value = !isExpandAll.value;
   nextTick(() => {
      refreshTable.value = true;
   });
}

/** Modify button actions */
function handleUpdate(row) {
   reset();
   listDeptExcludeChild(row.deptId).then(response => {
      deptOptions.value = proxy.handleTree(response.data, "deptId");
   });
   getDept(row.deptId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = td('sys.system.dept.editTitle');
   });
}

/** submit button */
function submitForm() {
   if (submitLoading.value) return;
   submitLoading.value = true;
   proxy.$refs["deptRef"].validate(valid => {
      if (valid) {
         if (form.value.deptId != undefined) {
            updateDept(form.value).then(response => {
               proxy.$modal.msgSuccess(td('common.message.editSuccess'));
               open.value = false;
               getList();
               submitLoading.value = false;
            }).catch(() => {
               submitLoading.value = false;
            });
         } else {
            addDept(form.value).then(response => {
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
   proxy.$modal.confirm(td('sys.system.dept.confirmDelete', { name: row.deptName })).then(function () {
      return delDept(row.deptId);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
   }).catch(() => { });
}

getList();
</script>
