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
            <el-form-item :label="td('sys.system.config.paramName')" prop="configName" :label-position="labelPosition">
               <el-input
                  v-model="queryParams.configName"
                  :placeholder="td('sys.system.config.paramNamePlaceholder')"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="td('sys.system.config.paramKey')" prop="configKey" :label-position="labelPosition">
               <el-input
                  v-model="queryParams.configKey"
                  :placeholder="td('sys.system.config.paramKeyPlaceholder')"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="td('sys.system.config.systemBuiltIn')" prop="configType" :label-position="labelPosition">
               <el-select class="el-form-input-width" v-model="queryParams.configType" :placeholder="td('sys.system.config.systemBuiltIn')" clearable>
                  <el-option
                     v-for="dict in sys_yes_no"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item :label="td('common.texts.createdTime')">
               <el-date-picker
                  class="el-form-input-width"
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
                  type="primary"
                  plain
                  icon="Plus"
                  @click="handleAdd"
                  v-hasPermi="['system:config:add']"
               >{{ td('common.button.add') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="primary"
                  plain
                  icon="Edit"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['system:config:edit']"
               >{{ td('common.button.update') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="danger"
                  plain
                  icon="Delete"
                  :disabled="multiple"
                  @click="handleDelete"
                  v-hasPermi="['system:config:remove']"
               >{{ td('common.button.delete') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="warning"
                  plain
                  icon="Download"
                  @click="handleExport"
                  v-hasPermi="['system:config:export']"
               >{{ td('common.button.export') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="danger"
                  plain
                  icon="Refresh"
                  @click="handleRefreshCache"
                  v-hasPermi="['system:config:remove']"
               >{{ td('sys.system.config.refreshCache') }}</el-button>
            </el-col>
         </el-row>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table stripe height="60vh" v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column :label="td('sys.system.config.paramPrimaryKey')" align="center" prop="configId" />
            <el-table-column :label="td('sys.system.config.paramName')" align="center" prop="configName" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.system.config.paramKey')" align="center" prop="configKey" :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.system.config.paramValue')" align="center" prop="configValue" :show-overflow-tooltip="true" >
               <template #default="scope">
                  <span>{{ scope.row.configValue || "-" }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('sys.system.config.systemBuiltIn')" align="center" prop="configType">
               <template #default="scope">
                  <dict-tag :options="sys_yes_no" :value="scope.row.configType" />
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.remark')" align="center" prop="remark" :show-overflow-tooltip="true" />
            <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width"  fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:config:edit']" >{{ td('common.button.update') }}</el-button>
                  <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:config:remove']">{{ td('common.button.delete') }}</el-button>
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

      <!-- 添加或修改参数配置对话框 -->
      <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']"  draggable destroy-on-close>
         <el-form ref="configRef" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.config.paramName')" prop="configName">
                     <el-input v-model="form.configName" :placeholder="td('sys.system.config.paramNamePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.config.paramKey')" prop="configKey">
                     <el-input v-model="form.configKey" :placeholder="td('sys.system.config.paramKeyPlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.config.paramValue')" prop="configValue" :label-position="labelPosition">
                     <el-input v-model="form.configValue" :placeholder="td('sys.system.config.paramValuePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.config.systemBuiltIn')" prop="configType">
                     <el-radio-group v-model="form.configType">
                        <el-radio
                           v-for="dict in sys_yes_no"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
                     <el-input v-model="form.remark" type="textarea" :placeholder="td('sys.config.inputContent')" />
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

<script setup name="Config">
import { listConfig, getConfig, delConfig, addConfig, updateConfig, refreshCache } from "@/api/system/system/config.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { sys_yes_no } = proxy.useDict("sys_yes_no");

const configList = ref([]);
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
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    configName: undefined,
    configKey: undefined,
    configType: undefined
  },
  rules: {
    configName: [{ required: true, message: td('sys.system.config.paramNameRequired'), trigger: "blur" }],
    configKey: [{ required: true, message: td('sys.system.config.paramKeyRequired'), trigger: "blur" }],
    configValue: [{ required: true, message: td('sys.system.config.paramValueRequired'), trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询参数列表 */
function getList() {
  loading.value = true;
  listConfig(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    configList.value = response.rows;
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
    configId: undefined,
    configName: undefined,
    configKey: undefined,
    configValue: undefined,
    configType: "Y",
    remark: undefined
  };
  proxy.resetForm("configRef");
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.configId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('sys.system.config.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const configId = row.configId || ids.value;
  getConfig(configId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = td('sys.system.config.editTitle');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["configRef"].validate(valid => {
    if (valid) {
      if (form.value.configId != undefined) {
        updateConfig(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
        });
      } else {
        addConfig(form.value).then(response => {
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
  const configIds = row.configId || ids.value;
  proxy.$modal.confirm(td('sys.system.config.confirmDelete', { id: configIds })).then(function () {
    return delConfig(configIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("system/config/export", {
    ...queryParams.value
  }, `config_${new Date().getTime()}.xlsx`);
}

/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  refreshCache().then(() => {
    proxy.$modal.msgSuccess(td('sys.system.config.refreshCacheSuccess'));
  });
}

getList();
</script>
