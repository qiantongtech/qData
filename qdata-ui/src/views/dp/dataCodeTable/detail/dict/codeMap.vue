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
        >{{ td('dp.dataCode.codeMap.modify') }}</el-button>
        <el-button
          link
          type="danger"
          icon="Delete"
          @click="handleDelete(row)"
        >{{ td('dp.dataCode.codeMap.delete') }}</el-button>
      </template>
    </qt-table>
  </qt-wrap>

  <!-- Add or modify data element code mapping dialog box -->
  <el-dialog :title="title" v-model="open" width="800px" draggable>
    <el-form ref="dpCodeMapRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.dataCode.originalValue')" prop="originalValue">
            <el-input v-model="form.originalValue" :placeholder="td('dp.dataCode.originalValuePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.dataCode.codeMap.codeName')" prop="codeName">
            <el-select v-model="form.codeName" :placeholder="td('dp.dataCode.codeNameSelect')" @change="handleCodeNameChange">
              <el-option v-for="item in dpDataElemCodeList" :key="item.id" :label="item.codeName"
                :value="item.codeName" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.dataCode.codeMap.codeValue')" prop="codeValue">
            <el-input v-model="form.codeValue" :placeholder="td('dp.dataCode.codeValue')" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.dataCode.codeMap.remark')" prop="remark">
            <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">{{ td('dp.dataCode.codeMap.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ td('dp.dataCode.codeMap.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
import {
  listDpCodeMap,
  getDpCodeMap,
  delDpCodeMap,
  addDpCodeMap,
  updateDpCodeMap,
  listDpDataElemCode
} from '@/api/dp/dataElem/dataElem';
import useDefaultLang from "@/composables/useDefaultLang";
import { ref, reactive, toRefs, watch, getCurrentInstance } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const tableRef = ref(null);
const dpDataElemCodeList = ref([]);
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
      label: td('dp.dataCode.codeMap.serialNumber'),
      prop: "id",
      width: 60,
      align: "left",
      sortable: true,
    },
    {
      label: td('dp.dataCode.originalValue'),
      prop: "originalValue",
      align: "left",
      width: 210,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeMap.codeName'),
      prop: "codeName",
      align: "left",
      width: 220,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeMap.codeValue'),
      prop: "codeValue",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeMap.createBy'),
      prop: "createBy",
      align: "left",
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeMap.createTime'),
      prop: "createTime",
      width: 200,
      align: "left",
      sortableKey: "create_time",
      sortable: true,
      date: true,
    },
    {
      label: td('dp.dataCode.codeMap.remark'),
      prop: "remark",
      align: "left",
      width: 320,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataCode.codeMap.operation'),
      slot: "action",
      width: 180,
      align: "center",
      fixed: "right",
    },
  ],
  func: listDpCodeMap,
  params: {
    pageNum: 1,
    pageSize: 10,
    dataElemId: route.query.id,
    originalValue: null,
    codeName: null,
    codeValue: null,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const data = reactive({
  form: {},
  rules: {
    originalValue: [{ required: true, message: td('dp.dataCode.originalValueRequired'), trigger: 'blur' }],
    codeName: [{ required: true, message: td('dp.dataCode.codeNameRequired'), trigger: 'change' }]
  }
});

const { form, rules } = toRefs(data);

watch(
  () => route.query.id,
  (newId) => {
    tableStore.params.dataElemId = newId || -1;
    if (newId) {
      tableRef.value?.getList();
      getDpDataElemCodeList();
    }
  },
  { immediate: true }
);

function getDpDataElemCodeList() {
  listDpDataElemCode({ dataElemId: route.query.id, pageNum: 1, pageSize: 1000 }).then((response) => {
    dpDataElemCodeList.value = response.data.rows;
  });
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
    originalValue: null,
    codeName: null,
    codeValue: null,
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
  proxy.resetForm('dpCodeMapRef');
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('dp.dataCode.addCodeMapTitle');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpCodeMap(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dp.dataCode.editCodeMapTitle');
  });
}

/** submit button */
function submitForm() {
  proxy.$refs['dpCodeMapRef'].validate((valid) => {
    if (valid) {
      form.value.dataElemId = route.query.id;
      if (form.value.id != null) {
        updateDpCodeMap(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('dp.dataCode.codeMap.updateSuccess'));
            open.value = false;
            tableRef.value.getList();
          });
      } else {
        addDpCodeMap(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('dp.dataCode.codeMap.addSuccess'));
            open.value = false;
            tableRef.value.getList();
          });
      }
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.dataCode.confirmDeleteCodeMap', '', { id: _ids }))
    .then(function () {
      return delDpCodeMap(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('dp.dataCode.codeMap.deleteSuccess'));
    })
    .catch(() => { });
}

function handleCodeNameChange(value) {
  const selectedCode = dpDataElemCodeList.value.find((item) => item.codeName === value);
  if (selectedCode) {
    form.value.codeValue = selectedCode.codeValue;
  }
}

proxy.$bus.on('data_elem_code_change', () => {
  getDpDataElemCodeList();
});
</script>
