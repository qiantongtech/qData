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
            <el-form-item :label="t('sys.system.dept.deptName')" prop="deptName">
               <el-input v-model="queryParams.deptName" :placeholder="t('sys.system.dept.deptNamePlaceholder')" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="t('common.texts.status')" prop="status">
               <el-select v-model="queryParams.status" :placeholder="t('sys.system.dept.deptStatus')" clearable class="el-form-input-width">
                  <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                     :value="dict.value" />
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
      <div class="pagecont-bottom">
         <div class="justify-between mb15">
            <el-row :gutter="10" class="btn-style">
               <el-col :span="1.5">
                  <el-button type="primary" plain icon="Plus" @click="handleAdd"
                     v-hasPermi="['system:dept:add']">{{ t('common.button.add') }}</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button type="info" plain icon="Sort" @click="toggleExpandAll">{{ t('common.button.un_fold') }}</el-button>
               </el-col>
            </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table v-if="refreshTable" height="60vh" v-loading="loading" :data="deptList" row-key="deptId"
            :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
            <el-table-column prop="deptName" :label="t('sys.system.dept.deptName')" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="orderNum" :label="t('sys.system.dept.sort')"></el-table-column>
            <el-table-column prop="status" :label="t('common.texts.status')">
               <template #default="scope">
                  <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column :label="t('common.texts.createdTime')" align="center" prop="createTime" width="160">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:dept:edit']">{{ t('common.button.update') }}</el-button>
                  <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                     v-hasPermi="['system:dept:add']">{{ t('common.button.add') }}</el-button>
                  <el-button v-if="scope.row.parentId != 0" link type="danger" icon="Delete"
                     @click="handleDelete(scope.row)" v-hasPermi="['system:dept:remove']">{{ t('common.button.delete') }}</el-button>
               </template>
            </el-table-column>
         </el-table>
      </div>

      <!-- 添加或修改部门对话框 -->
      <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
         destroy-on-close>
         <el-form ref="deptRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
               <el-col :span="24" v-if="form.parentId !== 0">
                  <el-form-item :label="t('sys.system.dept.parentDept')" prop="parentId">
                     <el-tree-select v-model="form.parentId" :data="deptOptions"
                        :props="{ value: 'deptId', label: 'deptName', children: 'children' }" value-key="deptId"
                        :placeholder="t('sys.system.dept.selectParentDept')" check-strictly />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.system.dept.deptName')" prop="deptName">
                     <el-input v-model="form.deptName" :placeholder="t('sys.system.dept.deptNamePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.system.dept.showSort')" prop="orderNum">
                     <el-input-number style="width:100%" v-model="form.orderNum" controls-position="right" :min="0" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.system.dept.leader')" prop="leader">
                     <el-input v-model="form.leader" :placeholder="t('sys.system.dept.leaderPlaceholder')" maxlength="20" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.system.dept.contactPhone')" prop="phone">
                     <el-input v-model="form.phone" :placeholder="t('sys.system.dept.contactPhonePlaceholder')" maxlength="11" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.system.dept.email')" prop="email">
                     <el-input v-model="form.email" :placeholder="t('sys.system.dept.emailPlaceholder')" maxlength="50" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="t('sys.system.dept.deptStatus')">
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
               <el-button @click="cancel">{{ t('common.button.cancel') }}</el-button>
               <el-button type="primary" @click="submitForm">{{ t('common.button.confirm') }}</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Dept">
import { useI18n } from 'vue-i18n'
import { listDept, getDept, delDept, addDept, updateDept, listDeptExcludeChild } from "@/api/system/system/dept.js";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
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
      parentId: [{ required: true, message: t('sys.system.dept.parentDeptRequired'), trigger: "blur" }],
      deptName: [{ required: true, message: t('sys.system.dept.deptNameRequired'), trigger: "blur" }],
      orderNum: [{ required: true, message: t('sys.system.dept.showSortRequired'), trigger: "blur" }],
      email: [{ type: "email", message: t('sys.system.dept.emailRequired'), trigger: ["blur", "change"] }],
      phone: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: t('sys.system.dept.phoneRequired'), trigger: "blur" }]
   },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询部门列表 */
function getList() {
   loading.value = true;
   listDept(queryParams.value).then(response => {
      deptList.value = proxy.handleTree(response.data, "deptId");
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

/** 搜索按钮操作 */
function handleQuery() {
   getList();
}

/** 重置按钮操作 */
function resetQuery() {
   proxy.resetForm("queryRef");
   handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
   reset();
   listDept().then(response => {
      deptOptions.value = proxy.handleTree(response.data, "deptId");
   });
   if (row != undefined) {
      form.value.parentId = row.deptId;
   }
   open.value = true;
   title.value = t('sys.system.dept.addTitle');
}

/** 展开/折叠操作 */
function toggleExpandAll() {
   refreshTable.value = false;
   isExpandAll.value = !isExpandAll.value;
   nextTick(() => {
      refreshTable.value = true;
   });
}

/** 修改按钮操作 */
function handleUpdate(row) {
   reset();
   listDeptExcludeChild(row.deptId).then(response => {
      deptOptions.value = proxy.handleTree(response.data, "deptId");
   });
   getDept(row.deptId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = t('sys.system.dept.editTitle');
   });
}

/** 提交按钮 */
function submitForm() {
   proxy.$refs["deptRef"].validate(valid => {
      if (valid) {
         if (form.value.deptId != undefined) {
            updateDept(form.value).then(response => {
               proxy.$modal.msgSuccess(t('common.message.editSuccess'));
               open.value = false;
               getList();
            });
         } else {
            addDept(form.value).then(response => {
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
   proxy.$modal.confirm(t('sys.system.dept.confirmDelete', { name: row.deptName })).then(function () {
      return delDept(row.deptId);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
   }).catch(() => { });
}

getList();
</script>
