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
        >{{ td('dp.dataElem.codeMap.modify') }}</el-button>
        <el-button
          link
          type="danger"
          icon="Delete"
          @click="handleDelete(row)"
        >{{ td('dp.dataElem.codeMap.delete') }}</el-button>
      </template>
    </qt-table>
  </qt-wrap>

  <!-- Add or modify data element code mapping dialog box -->
  <el-dialog :title="title" v-model="open" width="800px" draggable>
    <el-form ref="dpCodeMapRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.dataElem.originalValue')" prop="originalValue">
            <el-input v-model="form.originalValue" :placeholder="td('dp.dataElem.originalValuePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.dataElem.codeMap.codeName')" prop="codeName">
            <el-select v-model="form.codeName" :placeholder="td('dp.dataElem.codeNameSelect')" @change="handleCodeNameChange">
              <el-option v-for="item in dpDataElemCodeList" :key="item.id" :label="item.codeName"
                :value="item.codeName" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.dataElem.codeMap.codeValue')" prop="codeValue">
            <el-input v-model="form.codeValue" :placeholder="td('dp.dataElem.codeValue')" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.dataElem.codeMap.remark')" prop="remark">
            <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">{{ td('dp.dataElem.codeMap.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ td('dp.dataElem.codeMap.confirm') }}</el-button>
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

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  config: {
    table: { height: 360 },
  },
  columns: [
    {
      label: td('dp.dataElem.codeMap.serialNumber'),
      prop: "id",
      width: 60,
      align: "left",
      sortable: true,
    },
    {
      label: td('dp.dataElem.originalValue'),
      prop: "originalValue",
      align: "left",
      width: 210,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataElem.codeMap.codeName'),
      prop: "codeName",
      align: "left",
      width: 220,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataElem.codeMap.codeValue'),
      prop: "codeValue",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataElem.codeMap.createBy'),
      prop: "createBy",
      align: "left",
    },
    {
      label: td('dp.dataElem.codeMap.createTime'),
      prop: "createTime",
      width: 200,
      align: "left",
      sortableKey: "create_time",
      sortable: true,
      date: true,
    },
    {
      label: td('dp.dataElem.codeMap.remark'),
      prop: "remark",
      align: "left",
      width: 320,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('dp.dataElem.codeMap.operation'),
      slot: "action",
      width: 300,
      align: "center",
      fixed: "right",
    },
  ],
  func: listDpCodeMap,
  params: {
    dataElemId: route.query.id,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const data = reactive({
  form: {},
  rules: {
    originalValue: [{ required: true, message: td('dp.dataElem.originalValueRequired'), trigger: 'blur' }],
    codeName: [{ required: true, message: td('dp.dataElem.codeNameRequired'), trigger: 'change' }]
  }
});

const { form, rules } = toRefs(data);

watch(
  () => route.query.id,
  (newId) => {
    tableStore.params.dataElemId = newId || -1;
    tableRef.value?.refresh();
    getDpDataElemCodeList();
  },
  { immediate: true }
);

function getDpDataElemCodeList() {
  if (tableStore.params.dataElemId == -1) return;
  listDpDataElemCode({ dataElemId: tableStore.params.dataElemId, pageSize: 1000 }).then((response) => {
    dpDataElemCodeList.value = response.data.rows;
  });
}

function cancel() {
  open.value = false;
  reset();
}

function reset() {
  form.value = {
    id: null,
    dataElemId: null,
    originalValue: null,
    codeName: null,
    codeValue: null,
    remark: null
  };
  proxy.resetForm('dpCodeMapRef');
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = td('dp.dataElem.addCodeMapTitle');
}

function handleUpdate(row) {
  reset();
  getDpCodeMap(row.id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dp.dataElem.editCodeMapTitle');
  });
}

function submitForm() {
  proxy.$refs['dpCodeMapRef'].validate((valid) => {
    if (valid) {
      form.value.dataElemId = tableStore.params.dataElemId;
      const api = form.value.id != null ? updateDpCodeMap : addDpCodeMap;
      api(form.value).then(() => {
        proxy.$modal.msgSuccess(td(form.value.id != null ? 'dp.dataElem.codeMap.updateSuccess' : 'dp.dataElem.codeMap.addSuccess'));
        open.value = false;
        tableRef.value.refresh();
      });
    }
  });
}

function handleDelete(row) {
  proxy.$modal
    .confirm(td('dp.dataElem.confirmDeleteCodeMap', '', { id: row.id }))
    .then(function () {
      return delDpCodeMap(row.id);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('dp.dataElem.codeMap.deleteSuccess'));
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

<style lang="scss" scoped>
.btn-style {
  margin-bottom: 15px;
}
</style>
