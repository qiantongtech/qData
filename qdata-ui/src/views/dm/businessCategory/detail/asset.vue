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
            {{ td('common.button.details', '详情') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
          >
            {{ td('common.button.delete', '删除') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- 数据域管理详情对话框 -->
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
        <el-form-item :label="td('common.texts.number', '编号') + ':'" prop="id" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.dataDomain', '数据域')" prop="name" :label-position="labelPosition">
          <div class="form-readonly">{{ form.name ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.engName', '英文缩写')" prop="engName" :label-position="labelPosition">
          <div class="form-readonly">{{ form.engName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerId', '负责人')" prop="ownerUserId" :label-position="labelPosition">
          <div class="form-readonly">{{ form.ownerUserName || "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerPhone', '负责人电话')" prop="ownerUserPhoneNumber" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.ownerUserPhoneNumber || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.description', '描述')" prop="description" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark', '备注')" prop="remark" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>

        <el-form-item :label="td('common.texts.createdBy', '创建人')" prop="createBy" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.createBy }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.createdTime', '创建时间')" prop="createTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedBy', '更新人')" prop="createBy" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.updateBy }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedTime', '更新时间')" prop="updateTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.close', '关闭') }}</el-button>
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

// 取消按钮
function cancel() {
  openDetail.value = false;
  reset();
}

// 表单重置
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
    remark: null,
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
    { label: td('common.texts.number', '编号'), prop: "id", width: 60, sortable: true },
    { label: td('dm.dataDomain.name', '数据域名称'), prop: "name", align: "left", minWidth: 150 },
    {
      label: td('common.texts.description', '描述'),
      prop: "description",
      align: "left",
      minWidth: 200,
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dm.dataDomain.engName', '英文缩写'), prop: "engName", align: "left", minWidth: 120 },
    { label: td('dm.dataDomain.ownerId', '负责人'), prop: "ownerUserName", width: 120 },
    { label: td('dm.dataDomain.ownerPhone', '负责人电话'), prop: "ownerUserPhoneNumber", width: 120 },
    { label: td('common.texts.createdBy', '创建人'), prop: "createBy", width: 120 },
    {
      label: td('common.texts.createdTime', '创建时间'),
      prop: "createTime",
      width: 160,
      date: true,
      sortable: true,
    },
    { label: td('common.texts.operation', '操作'), width: 150, fixed: "right", slot: "handle" },
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
    title.value = td('dm.dataDomain.detailTitle', '数据域详情');
  });
}

function handleDelete(row) {
  proxy.$modal
    .confirm(td('dm.dataDomain.confirmDeleteByName', '是否确认删除数据域名称为"<name>"的数据项？').replace('<name>', row.name))
    .then(function () {
      return getDeletebyDomainId({
        businessCategoryId: props.businessLayerDetail.id,
        domainId: row.id,
      });
    })
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', '删除成功'));
      tableRef.value?.getList();
    })
    .catch(() => {});
}
</script>
