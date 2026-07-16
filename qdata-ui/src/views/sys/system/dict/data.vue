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
            <el-form-item :label="td('sys.system.dictData.dictName')" prop="dictType">
               <el-select v-model="queryParams.dictType" class="el-form-input-width">
                  <el-option
                     v-for="item in typeOptions"
                     :key="item.dictId"
                     :label="item.dictName"
                     :value="item.dictType"
                  />
               </el-select>
            </el-form-item>
            <el-form-item :label="td('sys.system.dictData.dictLabel')" prop="dictLabel">
               <el-input
                  v-model="queryParams.dictLabel"
                  :placeholder="td('sys.system.dictData.dictLabelPlaceholder')"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
               <el-select v-model="queryParams.status" :placeholder="td('sys.dictData.dataStatus')" clearable class="el-form-input-width">
                  <el-option
                     v-for="dict in sys_normal_disable"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
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
      <div  class="pagecont-bottom">
         <div class="justify-between mb15">
            <el-row :gutter="10" class="btn-style">
               <el-col :span="1.5">
                  <el-button
                     type="primary"
                     plain
                     icon="Plus"
                     @click="handleAdd"
                     v-hasPermi="['system:dict:add']"
                  >{{ td('common.button.add') }}</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button
                     type="primary"
                     plain
                     icon="Edit"
                     :disabled="single"
                     @click="handleUpdate"
                     v-hasPermi="['system:dict:edit']"
                  >{{ td('common.button.update') }}</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button
                     type="danger"
                     plain
                     icon="Delete"
                     :disabled="multiple"
                     @click="handleDelete"
                     v-hasPermi="['system:dict:remove']"
                  >{{ td('common.button.delete') }}</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button
                     type="warning"
                     plain
                     icon="Download"
                     @click="handleExport"
                     v-hasPermi="['system:dict:export']"
                  >{{ td('common.button.export') }}</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button
                     type="warning"
                     plain
                     icon="Close"
                     @click="handleClose"
                  >{{ td('common.button.close') }}</el-button>
               </el-col>
            </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <!-- <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange"> -->
         <el-table stripe height="60vh" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column :label="td('sys.system.dictData.dictCode')" align="center" prop="dictCode" />
            <el-table-column :label="td('sys.system.dictData.dictLabel')" align="center" prop="dictLabel">
               <template #default="scope">
                  <span v-if="(scope.row.listClass == '' || scope.row.listClass == 'default') && (scope.row.cssClass == '' || scope.row.cssClass == null)">{{ scope.row.dictLabel }}</span>
                  <el-tag v-else :type="scope.row.listClass == 'primary' ? '' : scope.row.listClass" :class="scope.row.cssClass">{{ scope.row.dictLabel }}</el-tag>
               </template>
            </el-table-column>
            <el-table-column :label="td('sys.dictData.dictValue')" align="center" prop="dictValue" />
            <el-table-column :label="td('sys.dictData.dictSort')" align="center" prop="dictSort" />
            <el-table-column :label="td('common.texts.status')" align="center" prop="status">
               <template #default="scope">
                  <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.remark')" align="center" prop="remark" :show-overflow-tooltip="true" />
            <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:dict:edit']">{{ td('common.button.update') }}</el-button>
                  <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:dict:remove']">{{ td('common.button.delete') }}</el-button>
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

      <!-- Add or modify parameter configuration dialog box -->
      <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
         <el-form ref="dataRef" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dictData.dictType')" :label-position="labelPosition">
                     <el-input v-model="form.dictType" :disabled="true" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dictData.dataLabel')" prop="dictLabel" :label-position="labelPosition">
                     <el-input v-model="form.dictLabel" :placeholder="td('sys.system.dictData.dataLabelPlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dictData.dataValue')" prop="dictValue" :label-position="labelPosition">
                     <el-input v-model="form.dictValue" :placeholder="td('sys.system.dictData.dataValuePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dictData.styleAttr')" prop="cssClass" :label-position="labelPosition">
                     <el-input v-model="form.cssClass" :placeholder="td('sys.system.dictData.styleAttrPlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dictData.showSort')" prop="dictSort" :label-position="labelPosition">
                     <el-input-number style="width:100%" v-model="form.dictSort" controls-position="right" :min="0" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.dictData.echoStyle')" prop="listClass" :label-position="labelPosition">
                     <el-select v-model="form.listClass">
                        <el-option
                           v-for="item in listClassOptions"
                           :key="item.value"
                           :label="item.label + '(' + item.value + ')'"
                           :value="item.value"
                        ></el-option>
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('common.texts.status')" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio
                           v-for="dict in sys_normal_disable"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
                     <el-input v-model="form.remark" type="textarea" :placeholder="td('sys.dictData.inputContent')"></el-input>
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

<script setup name="Data">
import useDefaultLang from "@/composables/useDefaultLang";
import useDictStore from '@/store/system/dict.js'
import { optionselect as getDictOptionselect, getType } from "@/api/system/system/dict/type.js";
import { listData, getData, delData, addData, updateData } from "@/api/system/system/dict/data.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const dataList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultDictType = ref("");
const typeOptions = ref([]);
const route = useRoute();
// Data label echo style
const listClassOptions = ref([
  { value: "default", label: td('sys.system.dictData.default') },
  { value: "primary", label: td('sys.system.dictData.primary') },
  { value: "success", label: td('sys.system.dictData.success') },
  { value: "info", label: td('sys.system.dictData.info') },
  { value: "warning", label: td('sys.system.dictData.warning') },
  { value: "danger", label: td('sys.system.dictData.danger') }
]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dictType: undefined,
    dictLabel: undefined,
    status: undefined
  },
  rules: {
    dictLabel: [{ required: true, message: td('sys.system.dictData.dataLabelRequired'), trigger: "blur" }],
    dictValue: [{ required: true, message: td('sys.system.dictData.dataValueRequired'), trigger: "blur" }],
    dictSort: [{ required: true, message: td('sys.system.dictData.dataSortRequired'), trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** Query dictionary type details */
function getTypes(dictId) {
  getType(dictId).then(response => {
    queryParams.value.dictType = response.data.dictType;
    defaultDictType.value = response.data.dictType;
    getList();
  });
}

/** Query dictionary type list */
function getTypeList() {
  getDictOptionselect().then(response => {
    typeOptions.value = response.data;
  });
}

/** Query dictionary data list */
function getList() {
  loading.value = true;
  listData(queryParams.value).then(response => {
    dataList.value = response.rows;
    total.value = response.total;
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
    dictCode: undefined,
    dictLabel: undefined,
    dictValue: undefined,
    cssClass: undefined,
    listClass: "default",
    dictSort: 0,
    status: "0",
    remark: undefined
  };
  proxy.resetForm("dataRef");
}

/** Search button action */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** Back button action */
function handleClose() {
  const obj = { path: "/system/dict" };
  proxy.$tab.closeOpenPage(obj);
}

/** reset button action */
function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.value.dictType = defaultDictType.value;
  handleQuery();
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('sys.system.dictData.addTitle');
  form.value.dictType = queryParams.value.dictType;
}

/** Multiple selection box selected data */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.dictCode);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const dictCode = row.dictCode || ids.value;
  getData(dictCode).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = td('sys.system.dictData.editTitle');
  });
}

/** submit button */
function submitForm() {
  proxy.$refs["dataRef"].validate(valid => {
    if (valid) {
      if (form.value.dictCode != undefined) {
        updateData(form.value).then(response => {
          useDictStore().removeDict(queryParams.value.dictType);
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
        });
      } else {
        addData(form.value).then(response => {
          useDictStore().removeDict(queryParams.value.dictType);
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const dictCodes = row.dictCode || ids.value;
  proxy.$modal.confirm(td('sys.system.dictData.confirmDelete', { code: dictCodes })).then(function() {
    return delData(dictCodes);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    useDictStore().removeDict(queryParams.value.dictType);
  }).catch(() => {});
}

/** Export button action */
function handleExport() {
  proxy.download("system/dict/data/export", {
    ...queryParams.value
  }, `dict_data_${new Date().getTime()}.xlsx`);
}

getTypes(route.params && route.params.dictId);
getTypeList();
</script>
