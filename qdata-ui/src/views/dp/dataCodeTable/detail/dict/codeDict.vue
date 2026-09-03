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
  <qt-wrap :columns="tableStore.columns" :tableRef="tableRef" :config="wrapConfig">
    <template #actions-data>
      <el-row :gutter="15" class="btn-style">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            @click="handleAdd"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
          </el-button>
        </el-col>
      </el-row>
    </template>

    <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
      <template #action="{ row }">
        <el-button
          link
          type="primary"
          icon="Edit"
          @click="handleUpdate(row)"
        >{{ td('dp.dataCode.codeDict.modify') }}</el-button>
        <el-button
          link
          type="danger"
          icon="Delete"
          @click="handleDelete(row)"
        >{{ td('dp.dataCode.codeDict.delete') }}</el-button>
      </template>
    </qt-table>
  </qt-wrap>

  <!-- Add or modify data element code dialog box -->
  <el-dialog :title="title" v-model="open" width="800px" draggable>
    <el-form ref="dpDataElemCodeRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.dataCode.codeValue')" prop="codeValue">
            <el-input v-model="form.codeValue" :placeholder="td('dp.dataCode.codeValuePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.dataCode.codeName')" prop="codeName">
            <el-input v-model="form.codeName" :placeholder="td('dp.dataCode.codeNamePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.dataCode.codeDict.remark')" prop="remark">
            <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
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
</template>

<script setup name="ComponentOne">
import {
  listDpDataElemCode,
  getDpDataElemCode,
  delDpDataElemCode,
  addDpDataElemCode,
  updateDpDataElemCode,
  validateCodeValue
} from '@/api/dp/dataElem/dataElem';
import useDefaultLang from "@/composables/useDefaultLang";
import { ref, reactive, toRefs, watch, getCurrentInstance } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const tableRef = ref(null);
const open = ref(false);
const title = ref('');
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  config: {
    sort: true,
    initResquest: true,
    table: {
      stripe: true,
      height: 360,
      onSelectionChange: (selection) => {
        ids.value = selection.map((item) => item.id);
        single.value = selection.length != 1;
        multiple.value = !selection.length;
      },
    },
  },
  columns: [
    {
      label: td('dp.dataCode.codeDict.serialNumber'),
      prop: "id",
      width: 60,
      align: "left",
      sortable: true,
    },
    {
      label: td('dp.dataCode.codeValue'),
      prop: "codeValue",
      align: "left",
      width: 160,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeName'),
      prop: "codeName",
      align: "left",
      width: 220,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeDict.createBy'),
      prop: "createBy",
      width: 160,
      align: "left",
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeDict.createTime'),
      prop: "createTime",
      width: 220,
      align: "left",
      sortableKey: "create_time",
      sortable: true,
      date: true,
    },
    {
      label: td('dp.dataCode.codeDict.remark'),
      prop: "remark",
      align: "left",
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeDict.operation'),
      slot: "action",
      width: 180,
      align: "center",
      fixed: "right",
    },
  ],
  func: listDpDataElemCode,
  params: {
    pageNum: 1,
    pageSize: 10,
    dataElemId: route.query.id,
    codeValue: null,
    codeName: null,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const data = reactive({
  form: {},
  rules: {
    codeValue: [
      { required: true, message: td('dp.dataCode.codeValueRequired'), trigger: 'blur' },
      { validator: validatorCodeValue, trigger: 'blur' }
    ],
    codeName: [{ required: true, message: td('dp.dataCode.codeNameRequired'), trigger: 'blur' }]
  }
});

const { form, rules } = toRefs(data);

watch(
  () => route.query.id,
  (newId) => {
    tableStore.params.dataElemId = newId || -1;
    if (newId) {
      tableRef.value?.getList();
    }
  },
  { immediate: true }
);

function validatorCodeValue(rule, value, callback) {
  if (value !== null && value !== undefined) {
    var params = {
      id: form.value.id || null,
      dataElemId: route.query.id,
      codeValue: value
    };
    validateCodeValue(params).then((res) => {
      if (res.data == 0) {
        callback(new Error(td('dp.dataCode.codeValueDuplicate')));
      } else {
        callback();
      }
    });
  } else {
    callback();
  }
}

// Cancel button
function cancel() {
  open.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    dataElemId: null,
    codeValue: null,
    codeName: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm('dpDataElemCodeRef');
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('dp.dataCode.addCodeTitle');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpDataElemCode(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dp.dataCode.editCodeTitle');
  });
}

/** submit button */
function submitForm() {
  proxy.$refs['dpDataElemCodeRef'].validate((valid) => {
    form.value.dataElemId = route.query.id;
    if (valid) {
      if (form.value.id != null) {
        updateDpDataElemCode(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('dp.dataCode.codeDict.updateSuccess'));
            open.value = false;
            tableRef.value.getList();
            proxy.$bus.emit('data_elem_code_change');
          });
      } else {
        addDpDataElemCode(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('dp.dataCode.codeDict.addSuccess'));
            open.value = false;
            tableRef.value.getList();
            proxy.$bus.emit('data_elem_code_change');
          });
      }
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.dataCode.confirmDeleteCode', '', { id: _ids }))
    .then(function () {
      return delDpDataElemCode(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$bus.emit('data_elem_code_change');
      proxy.$modal.msgSuccess(td('dp.dataCode.codeDict.deleteSuccess'));
    })
    .catch(() => { });
}
</script>
