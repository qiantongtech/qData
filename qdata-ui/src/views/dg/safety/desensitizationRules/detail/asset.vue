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
            {{ td('common.button.details') }}
          </el-button>
          <!-- <el-button link type="primary" icon="Edit">Edit</el-button> -->
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
          >
            {{ td('common.button.delete') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog v-model="openDetail" width="800px" draggable>
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ td('dg.sensitiveList.sensitiveListDetail') }}
        </span>
      </template>

      <el-form
        ref="sensitiveDetailRef"
        :model="detailData"
        label-width="110px"
        class="column-form"
       :label-position="labelPosition">
        <el-form-item :label="td('common.texts.number') + ':'" prop="id" :label-position="labelPosition">
          <div class="form-readonly">{{ detailData.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.assetName')" prop="assetName" :label-position="labelPosition">
          <div class="form-readonly">{{ detailData.assetName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.tableNameLabel')" prop="assetTableName" :label-position="labelPosition">
          <div class="form-readonly">
            {{ detailData.assetTableName ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.fieldNameLabel')" prop="assetcolumnName" :label-position="labelPosition">
          <div class="form-readonly">
            {{ detailData.assetcolumnName ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.dataLevel')" prop="dataLevelName" :label-position="labelPosition">
          <div class="form-readonly">{{ detailData.dataLevelName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
          <div class="form-readonly">
            {{ getStatusLabel(detailData.validFlag) }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.description')" prop="assetDescription" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ detailData.assetDescription ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ detailData.remark ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdBy')" prop="createBy" :label-position="labelPosition">
          <div class="form-readonly">{{ detailData.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdTime')" prop="createTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(detailData.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy" :label-position="labelPosition">
          <div class="form-readonly">{{ detailData.updateBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(detailData.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openDetail = false">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DesensitizationRuleAssets">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  listDgDesensitizeListByRuleId,
  getDgDesensitizeList,
  delDgDesensitizeList,
} from "@/api/dg/safety/DgDesensitizeList";
import LevelBadge from "@/views/dg/safety/dataLevel/components/LevelBadge.vue";
import { getCurrentInstance, ref, reactive, watch } from "vue";

const { td } = useDefaultLang();
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
  if (v === true) return td('dg.desensitizationRules.detailStatusEnable');
  if (v === false) return td('dg.desensitizationRules.detailStatusDisable');
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
    .confirm(td('dg.desensitizationRules.confirmDeleteId', 'Are you sure to delete item with ID "{id}"?', { id: ids }))
    .then(() => delDgDesensitizeList(ids))
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
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
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: td('dg.sensitiveList.nameDesc'),
      prop: "assetName",
      align: "left",
      minWidth: 180,
      showOverflowTooltip: { effect: "light" },
      slot: "assetNameDesc",
    },
    {
      label: td('dg.sensitiveList.tableName'),
      prop: "assetTableName",
      align: "left",
      minWidth: 200,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dg.sensitiveList.fieldName'),
      prop: "assetcolumnName",
      align: "left",
      minWidth: 150,
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dg.sensitiveList.dataLevel'), prop: "dataLevelName", slot: "dataLevel", width: 120 },
    { label: td('common.texts.status'), prop: "validFlag", slot: "validFlag", minWidth: 120 },
    {
      label: td('common.texts.createdBy'),
      prop: "createBy",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: td('common.texts.operation'), width: 200, fixed: "right", slot: "handle" },
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
