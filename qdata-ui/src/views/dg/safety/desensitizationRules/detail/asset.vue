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
  <div>
    <qt-wrap
      :columns="tableStore.columns"
      :tableRef="tableRef"
      :config="{ fullContent: false }"
    >
      <qt-table v-bind="tableStore" ref="tableRef">
        <template #assetNameDesc="{ row }">
          <div class="name-label task-title">
            <div class="task-title-row">
              <div class="task-name-ellipsis">
                <span
                  class="task-name-ellipsis__inner"
                  :title="row.assetName || '-'"
                >
                  {{ row.assetName || "-" }}
                </span>
              </div>
            </div>
            <div class="desc-text" :title="row.assetDescription">
              {{ row.assetDescription }}
            </div>
          </div>
        </template>
        <template #dataLevel="{ row }">
          <div
            style="display: flex; align-items: center"
            v-if="row.dataLevelName"
          >
            <LevelBadge :levelData="row.dataLevelName" />
          </div>
          <span v-else>-</span>
        </template>
        <template #validFlag="{ row }">
          <el-tag
            :type="
              String(row.validFlag ?? '') === '1' || row.validFlag === true
                ? 'success'
                : 'info'
            "
          >
            {{ getStatusLabel(row.validFlag) }}
          </el-tag>
        </template>
        <template #handle="{ row }">
          <el-button link type="primary" icon="View" @click="handleDetail(row)">
            {{ t('common.button.details') }}
          </el-button>
          <!-- <el-button link type="primary" icon="Edit">编辑</el-button> -->
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
          >
            {{ t('common.button.delete') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog v-model="openDetail" width="800px" draggable>
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          敏感清单详情
        </span>
      </template>

      <el-form
        ref="sensitiveDetailRef"
        :model="detailData"
        label-width="110px"
        class="column-form"
      >
        <el-form-item label="编号:" prop="id">
          <div class="form-readonly">{{ detailData.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="资产名称" prop="assetName">
          <div class="form-readonly">{{ detailData.assetName ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="表名" prop="assetTableName">
          <div class="form-readonly">
            {{ detailData.assetTableName ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="字段名" prop="assetcolumnName">
          <div class="form-readonly">
            {{ detailData.assetcolumnName ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="数据分级" prop="dataLevelName">
          <div class="form-readonly">{{ detailData.dataLevelName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="t('common.texts.status')" prop="validFlag">
          <div class="form-readonly">
            {{ getStatusLabel(detailData.validFlag) }}
          </div>
        </el-form-item>
        <el-form-item :label="t('common.texts.description')" prop="assetDescription" class="row-full">
          <div class="form-readonly textarea">
            {{ detailData.assetDescription ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="t('common.texts.remark')" prop="remark" class="row-full">
          <div class="form-readonly textarea">
            {{ detailData.remark ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="t('common.texts.createdBy')" prop="createBy">
          <div class="form-readonly">{{ detailData.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="t('common.texts.createdTime')" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(detailData.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="t('common.texts.updatedBy')" prop="updateBy">
          <div class="form-readonly">{{ detailData.updateBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="t('common.texts.updatedTime')" prop="updateTime">
          <div class="form-readonly">
            {{ parseTime(detailData.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openDetail = false">{{ t('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DesensitizationRuleAssets">
import { useI18n } from 'vue-i18n'
import {
  listDgDesensitizeListByRuleId,
  getDgDesensitizeList,
  delDgDesensitizeList,
} from "@/api/dg/safety/DgDesensitizeList";
import LevelBadge from "@/views/dg/safety/dataLevel/components/LevelBadge.vue";
import { getCurrentInstance, ref, reactive, watch } from "vue";

const { t } = useI18n();
const props = defineProps({
  ruleDetail: {
    type: Object,
    default: () => ({}),
  },
});

const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");

const openDetail = ref(false);
const detailData = ref({});

function getStatusLabel(v) {
  if (v === true) return "启用";
  if (v === false) return "禁用";
  const vv = v === 0 || v ? String(v) : "";
  const found = (dp_model_status.value || []).find(
    (d) => String(d.value) === vv
  );
  return found?.label || (vv ? vv : "-");
}

function handleDetail(row) {
  const id = row?.id;
  if (!id) return;
  getDgDesensitizeList(id).then((res) => {
    detailData.value = res?.data || {};
    openDetail.value = true;
  });
}

function handleDelete(row) {
  const ids = row?.id;
  if (!ids) return;
  proxy.$modal
    .confirm('是否确认删除编号为"' + ids + '"的数据项？')
    .then(() => delDgDesensitizeList(ids))
    .then(() => {
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
      tableRef.value?.getList();
    })
    .catch(() => {});
}

const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
    },
  },
  columns: [
    { label: t('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: "资产名称/描述",
      prop: "assetName",
      align: "left",
      minWidth: 180,
      showOverflowTooltip: { effect: "light" },
      slot: "assetNameDesc",
    },
    {
      label: "表名",
      prop: "assetTableName",
      align: "left",
      minWidth: 200,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "字段名",
      prop: "assetcolumnName",
      align: "left",
      minWidth: 150,
      showOverflowTooltip: { effect: "light" },
    },
    { label: "数据分级", prop: "dataLevelName", slot: "dataLevel", width: 120 },
    { label: t('common.texts.status'), prop: "validFlag", slot: "validFlag", minWidth: 120 },
    {
      label: t('common.texts.createdBy'),
      prop: "createBy",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: t('common.texts.operation'), width: 200, fixed: "right", slot: "handle" },
  ],
  func: async (params) => {
    if (!props.ruleDetail?.id) {
      return { data: { rows: [], total: 0 } };
    }
    const res = await listDgDesensitizeListByRuleId({
      ...(params || {}),
      ruleId: props.ruleDetail?.id,
    });
    const rawRows = res?.data?.rows || res?.rows || [];
    const rows = rawRows.map((r) => ({
      ...r,
      validFlag:
        r.validFlag === "1" || r.validFlag === 1 || r.validFlag === true,
    }));
    const total = res?.data?.total || res?.total || 0;
    return { data: { rows, total } };
  },
  params: {},
});

watch(
  () => props.ruleDetail?.id,
  () => {
    tableRef.value?.getList();
  }
);
</script>
