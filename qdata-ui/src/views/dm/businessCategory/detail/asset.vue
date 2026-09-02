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
  <div ref="asset-container">
    <qt-wrap
      :columns="tableStore.columns"
      :tableRef="tableRef"
      :config="{ fullContent: false, actions: { table: { search: false } } }"
    >
      <qt-table v-bind="tableStore" ref="tableRef">
        <template #validFlag="scope">
          <el-switch
            v-model="scope.row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            disabled
          />
        </template>
        <template #handle="{ row }">
          <el-button link type="primary" icon="View" @click="handleDetail(row)">
            {{ td('common.button.details', 'Details') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
          >
            {{ td('common.button.delete', 'Delete') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Data Domain Management Details Dialog Box -->
    <el-dialog
      :title="title"
      v-model="openDetail"
      :append-to="$refs['asset-container']"
      draggable
      width="800px"
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>

      <el-form
        ref="dataDomainDetailRef"
        :model="form"
        label-width="110px"
        class="column-form"
       :label-position="labelPosition">
        <el-form-item :label="td('common.texts.number', 'No.') + ':'" prop="id" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.dataDomain', 'Data Domain')" prop="name" :label-position="labelPosition">
          <div class="form-readonly">{{ form.name ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.engName', 'English Abbreviation')" prop="engName" :label-position="labelPosition">
          <div class="form-readonly">{{ form.engName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerId', 'Responsible Person')" prop="ownerUserId" :label-position="labelPosition">
          <div class="form-readonly">{{ form.ownerUserName || "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerPhone', 'Responsible Person Phone')" prop="ownerUserPhoneNumber" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.ownerUserPhoneNumber || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.description', 'Description')" prop="description" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdBy', 'Created By')" prop="createBy" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.createBy }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.createdTime', 'Created Time')" prop="createTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedBy', 'Updated By')" prop="createBy" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.updateBy }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedTime', 'Updated Time')" prop="updateTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.close', 'Close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="BusinessLayerDataDomains">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  ref,
  reactive,
  watch,
  nextTick,
  toRefs,
  getCurrentInstance,
} from "vue";
import {
  listDataDomainlist,
  getDeletebyDomainId,
  getDataDomain,
} from "@/api/dm/dataDomain/dataDomain.js";
import { useRouter } from "vue-router";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const props = defineProps({
  businessLayerDetail: {
    type: Object,
    default: () => ({}),
  },
});

const router = useRouter();
const tableRef = ref(null);

const openDetail = ref(false);
const title = ref("");
const data = reactive({
  form: {},
});

const { form } = toRefs(data);

// Cancel button
function cancel() {
  openDetail.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    name: null,
    engName: null,
    ownerUserId: null,
    ownerUserPhoneNumber: null,
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
  };
}

const tableStore = reactive({
  config: {
    initResquest: false,
    table: {
      stripe: true,
      rowKey: "id",
    },
  },
  columns: [
    { label: td('common.texts.number', 'No.'), prop: "id", width: 60, sortable: true },
    { label: td('dm.dataDomain.name', 'Data Domain Name'), prop: "name", align: "left", minWidth: 150 },
    {
      label: td('common.texts.description', 'Description'),
      prop: "description",
      align: "left",
      minWidth: 200,
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dm.dataDomain.engName', 'English Abbreviation'), prop: "engName", align: "left", minWidth: 120 },
    { label: td('dm.dataDomain.ownerId', 'Responsible Person'), prop: "ownerUserName", width: 120 },
    { label: td('dm.dataDomain.ownerPhone', 'Responsible Person Phone'), prop: "ownerUserPhoneNumber", width: 120 },
    { label: td('common.texts.createdBy', 'Created By'), prop: "createBy", width: 120 },
    {
      label: td('common.texts.createdTime', 'Created Time'),
      prop: "createTime",
      width: 160,
      date: true,
      sortable: true,
    },
    { label: td('common.texts.operation', 'Operation'), width: 150, fixed: "right", slot: "handle" },
  ],
  func: listDataDomainlist,
  params: {
    businessDomainId: props.businessLayerDetail?.id,
    pageNum: 1,
    pageSize: 10,
  },
});

watch(
  () => props.businessLayerDetail.id,
  (newId) => {
    if (newId) {
      tableStore.params.businessDomainId = newId;
      nextTick(() => {
        tableRef.value?.getList();
      });
    }
  },
  { immediate: true }
);

function handleDetail(row) {
  reset();
  const _id = row?.id;
  getDataDomain(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('dm.dataDomain.detailTitle', 'Data Domain Detail');
  });
}

function handleDelete(row) {
  proxy.$modal
    .confirm(td('dm.dataDomain.confirmDeleteByName', 'Are you sure to delete data domain "{name}"?', { name: row.name }))
    .then(function () {
      return getDeletebyDomainId({
        businessCategoryId: props.businessLayerDetail.id,
        domainId: row.id,
      });
    })
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', 'Deleted successfully'));
      tableRef.value?.getList();
    })
    .catch(() => {});
}
</script>
