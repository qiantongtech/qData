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
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStore.params"
          @query="handleQueryClick"
          @reset="handleResetQueryClick"
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['dg:dgdesensitizelist:add']"
        >
          {{ td('common.button.add') }}
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
          v-hasPermi="['dg:dgdesensitizelist:remove']"
        >
          {{ td('common.button.delete') }}
        </el-button>
      </template>

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
          <el-switch
            v-model="row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="true"
            :inactive-value="false"
            @change="(e) => handleStatusChange(row.id, row, e)"
          />
        </template>

        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            :disabled="row.validFlag === true"
            v-hasPermi="['dg:dgdesensitizelist:edit']"
          >
            {{ td('common.button.update') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            :disabled="row.validFlag === true"
            @click="handleDelete(row)"
            v-hasPermi="['dg:dgdesensitizelist:remove']"
          >
            {{ td('common.button.delete') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(row)"
            v-hasPermi="['dg:dgdesensitizelist:query']"
          >
            {{ td('common.button.details') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="sensitiveRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
       :label-position="labelPosition">
        <AssetColumnSelect v-model="form" />
        <qt-form-item :label="td('common.texts.status')" prop="validFlag">
          <el-radio-group v-model="form.validFlag">
            <el-radio
              v-for="opt in validFlagOptions"
              :key="opt.label"
              :label="opt.value"
            >
              {{ opt.label }}
            </el-radio>
          </el-radio-group>
        </qt-form-item>
        <el-form-item :label="td('common.texts.description')" prop="assetDescription" class="row-full" :label-position="labelPosition">
          <el-input
            v-model="form.assetDescription"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder')"
            :min-height="192"
            show-word-limit
            maxlength="500"
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
          <el-input
            v-model="form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder')"
            :min-height="192"
            show-word-limit
            maxlength="500"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      :title="title"
      v-model="openDetail"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="sensitiveDetailRef"
        :model="form"
        label-width="110px"
        class="column-form"
       :label-position="labelPosition">
        <el-form-item :label="td('common.texts.number') + ':'" prop="id" :label-position="labelPosition">
          <div class="form-readonly">{{ form.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.assetName')" prop="assetName" :label-position="labelPosition">
          <div class="form-readonly">{{ form.assetName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.dataCategory')" prop="dataCategoryName" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.dataCategoryName ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.dataLevel')" prop="dataLevelName" :label-position="labelPosition">
          <div v-if="form.dataLevelName">
            <LevelBadge :levelData="form.dataLevelName" />
          </div>
          <div v-else class="form-readonly">-</div>
        </el-form-item>

        <el-form-item :label="td('dg.sensitiveList.tableNameLabel')" prop="assetTableName" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.assetTableName || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dg.sensitiveList.fieldNameLabel')" prop="assetcolumnName" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.assetcolumnName || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
          <el-tag v-if="form.validFlag === true" type="primary">{{ td('dg.sensitiveList.enabled') }}</el-tag>
          <el-tag v-else-if="form.validFlag === false" type="danger"
            >{{ td('dg.sensitiveList.disabled') }}</el-tag
          >
        </el-form-item>
        <el-form-item :label="td('common.texts.description')" prop="assetDescription" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ form.assetDescription ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdBy')" prop="createBy" :label-position="labelPosition">
          <div class="form-readonly">{{ form.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdTime')" prop="createTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.updateBy ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SensitiveList">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  listDgDesensitizeList,
  getDgDesensitizeList,
  addDgDesensitizeList,
  updateDgDesensitizeList,
  delDgDesensitizeList,
} from "@/api/dg/safety/DgDesensitizeList";
import AssetColumnSelect from "./components/AssetColumnSelect.vue";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";
import LevelBadge from "@/views/dg/safety/dataLevel/components/LevelBadge.vue";
import { getCurrentInstance, onMounted, reactive, ref, toRefs } from "vue";
import {delDesensitizeWhitelist} from "@/api/dg/safety/whitelist/desensitizeWhitelist.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");

const store = reactive({
  rows: [],
});

const dataCategoryList = ref([]);
const allDataCategoryList = ref([]);

function getStatusLabel(v) {
  if (v === true) return td('dg.sensitiveList.enabled');
  if (v === false) return td('dg.sensitiveList.disabled');
  const vv = v === 0 || v ? String(v) : "";
  const found = (dp_model_status.value || []).find(
    (d) => String(d.value) === vv
  );
  return found?.label || (vv ? vv : "-");
}

function initDataCategoryOptions() {
  selectTreeDataCategory().then((res) => {
    const rawData = res?.data || [];
    allDataCategoryList.value = rawData;
    const processTree = (nodes) => {
      return nodes.map((node) => {
        const newNode = { ...node };
        newNode.disabled = String(node.type) === "1";
        if (node.children && node.children.length > 0) {
          newNode.children = processTree(node.children);
        }
        return newNode;
      });
    };
    dataCategoryList.value = processTree(rawData);
  });
}

onMounted(() => {
  initDataCategoryOptions();
});

const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetail,
    },
  },
  columns: [
    { type: "selection", width: 55, align: "left" },
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: td('dg.sensitiveList.nameDesc'),
      prop: "assetName",
      align: "left",
      width: 260,
      slot: "assetNameDesc",
    },
    {
      label: td('dg.sensitiveList.dataCategory'),
      prop: "dataCategoryName",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dg.sensitiveList.dataLevel'),
      prop: "dataLevelName",
      slot: "dataLevel",
      align: "left",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dg.sensitiveList.tableName'),
      prop: "assetTableName",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dg.sensitiveList.fieldName'),
      prop: "assetcolumnName",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.status'),
      slot: "validFlag",
    },
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
    {
      label: td('common.texts.operation'),
      width: 220,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listDgDesensitizeList,
  params: {
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});
const validFlagOptions = [
  { label: td('dg.sensitiveList.disabled'), value: false },
  { label: td('dg.sensitiveList.enabled'), value: true },
];
const searchStore = reactive({
  items: [
    {
      label: td('dg.sensitiveList.assetName'),
      prop: "assetName",
      align: "left",
      component: { is: "input", placeholder: td('dg.sensitiveList.assetNamePlaceholder') },
    },
    {
      label: td('dg.sensitiveList.dataCategory'),
      prop: "dataCategoryId",
      component: {
        is: "tree-select",
        data: allDataCategoryList,
        props: { value: "id", label: "name", children: "children" },
        valueKey: "id",
        placeholder: td('dg.sensitiveList.dataCategoryPlaceholder'),
        checkStrictly: true,
      },
    },
    {
      label: td('common.texts.status'),
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: td('common.form.statusPlaceholder'),
        options: validFlagOptions,
      },
    },
  ],
});

function handleQueryClick() {
  tableRef.value.getList();
}
function handleResetQueryClick() {
  tableRef.value.resetQuery();
}

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === true ? td('dg.sensitiveList.enabled') : td('dg.sensitiveList.disabled');
  proxy.$modal
    .confirm(td('dg.sensitiveList.confirmStatus', '确认要"{text}","{name}"吗？', { text: text, name: row.assetName || "-" }))
    .then(function () {
      updateDgDesensitizeList({ id, validFlag: row.validFlag }).then(() => {
        proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.validFlag = row.validFlag === true ? false : true;
    });
}

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const data = reactive({
  form: {},
  rules: {
    validFlag: [{ required: true, message: td('common.form.statusRequired'), trigger: "change" }],
  },
});

const { form, rules } = toRefs(data);

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    assetId: null,
    assetcolumnId: null,
    assetcolumnName: null,
    assetcolumnComment: null,
    dataCategoryId: null,
    assetName: null,
    assetDescription: null,
    dataCategoryCode: null,
    dataCategoryName: null,
    dataLevelName: null,
    assetTableName: null,
    assetTableComment: null,
    validFlag: false,
    remark: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  };
  proxy.resetForm("sensitiveRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  initDataCategoryOptions();
  open.value = true;
  title.value = td('dg.sensitiveList.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  initDataCategoryOptions();
  const _id = row?.id;
  getDgDesensitizeList(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dg.sensitiveList.editTitle');
  });
}
/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  initDataCategoryOptions();
  const _id = row?.id;
  getDgDesensitizeList(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('dg.sensitiveList.detailTitle');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["sensitiveRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDgDesensitizeList(form.value).then(() => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
      } else {
        addDgDesensitizeList(form.value).then(() => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
/*function handleDelete(row) {
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    _ids = store.rows.map((item) => item.id).join(",");

  }
  if (!_ids) return;

  proxy.$modal
    .confirm('是否确认删除编号为"' + _ids + '"的数据项？')
    .then(() => {
      delDgDesensitizeList(_ids).then(() => {
        tableRef.value.getList();
        proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
      });
    })
    .catch(() => {
      // 用户取消删除操作
    });
}*/
function handleDelete(row) {
  const invalidIds = [];
  const message=ref(td('dg.sensitiveList.confirmDeleteSimple'));
  if (row?.id) {
    invalidIds.push(row.id);
    message.value=td('dg.sensitiveList.confirmDeleteId', '是否确认删除编号为{id}的数据项？', { id: row.id })
  }else {
    store.rows.forEach(item => {
      // 当 validFlag 为 false 时，记录 id
      if (item.validFlag === false) {
        invalidIds.push(item.id);
      }
    });
    message.value=td('dg.sensitiveList.confirmDeleteCount', '可删除{canDelete}个，不可删除{cannotDelete}个，是否删除可删部分', { canDelete: invalidIds.length, cannotDelete: store.rows.length-invalidIds.length })
  }
  proxy.$modal
      .confirm(message.value)
      .then(() => {
        delDgDesensitizeList(invalidIds).then(() => {
          tableRef.value.getList();
          proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
        });
      })
      .catch(() => {
        // 用户取消删除操作
      });
}

</script>
