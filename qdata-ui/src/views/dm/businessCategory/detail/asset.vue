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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
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
            详情
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
          >
            删除
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
      >
        <el-form-item label="编号:" prop="id">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item label="数据域" prop="name">
          <div class="form-readonly">{{ form.name ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="英文缩写" prop="engName">
          <div class="form-readonly">{{ form.engName ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="负责人" prop="ownerUserId">
          <div class="form-readonly">{{ form.ownerUserName || "-" }}</div>
        </el-form-item>
        <el-form-item label="负责人电话" prop="ownerUserPhoneNumber">
          <div class="form-readonly">
            {{ form.ownerUserPhoneNumber || "-" }}
          </div>
        </el-form-item>
        <el-form-item label="描述" prop="description" class="row-full">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="row-full">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>

        <el-form-item label="创建人" prop="createBy">
          <div class="form-readonly">
            {{ form.createBy }}
          </div>
        </el-form-item>

        <el-form-item label="创建时间" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item label="更新人" prop="createBy">
          <div class="form-readonly">
            {{ form.updateBy }}
          </div>
        </el-form-item>

        <el-form-item label="更新时间" prop="updateTime">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="BusinessLayerDataDomains">
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
    { label: "编号", prop: "id", width: 60, sortable: true },
    { label: "数据域名称", prop: "name", align: "left", minWidth: 150 },
    {
      label: "描述",
      prop: "description",
      align: "left",
      minWidth: 200,
      showOverflowTooltip: { effect: "light" },
    },
    { label: "英文缩写", prop: "engName", align: "left", minWidth: 120 },
    { label: "负责人", prop: "ownerUserName", width: 120 },
    { label: "负责人电话", prop: "ownerUserPhoneNumber", width: 120 },
    { label: "创建人", prop: "createBy", width: 120 },
    {
      label: "创建时间",
      prop: "createTime",
      width: 160,
      date: true,
      sortable: true,
    },
    { label: "操作", width: 150, fixed: "right", slot: "handle" },
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
    title.value = "数据域详情";
  });
}

function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除数据域名称为"' + row.name + '"的数据项？')
    .then(function () {
      return getDeletebyDomainId({
        businessCategoryId: props.businessLayerDetail.id,
        domainId: row.id,
      });
    })
    .then(() => {
      proxy.$modal.msgSuccess("删除成功");
      tableRef.value?.getList();
    })
    .catch(() => {});
}
</script>
