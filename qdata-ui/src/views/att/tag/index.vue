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
    <GuideTip tip-id="att/attTag.list" />

    <el-container>
      <DeptTree
        ref="DeptTreeRef"
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="td('att.common.tagCatPlaceholder')"
        @node-click="handleNodeClick"
      />

      <el-main class="main-content">
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
              v-hasPermi="['att:tag:add']"
            >
              {{ td('common.button.add') }}
            </el-button>
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDelete"
              v-hasPermi="['att:tag:remove']"
            >
              {{ td('common.button.delete') }}
            </el-button>
          </template>
          <qt-table v-bind="tableStore" ref="tableRef">
            <template #status="scope">
              <el-switch
                v-model="scope.row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
                @change="(e) => handleStatusChange(scope.row.id, scope.row, e)"
              />
            </template>
            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                :disabled="row.status == 1"
                v-hasPermi="['att:tag:edit']"
                >{{ td('common.button.update') }}</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                :disabled="row.status == 1"
                @click="handleDelete(row)"
                v-hasPermi="['att:tag:remove']"
                >{{ td('common.button.delete') }}</el-button
              >
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['att:tag:query']"
                >{{ td('common.button.details') }}</el-button
              >
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- Add or modify the tag management dialog box -->
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
        ref="AttTagRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.tagCatName')" prop="catCode" :label-position="labelPosition">
              <el-tree-select
                filterable
                v-model="form.catCode"
                :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }"
                value-key="ID"
                :placeholder="td('att.common.tagCatPlaceholder')"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.tagName')" prop="name" :label-position="labelPosition">
              <el-input v-model="form.name" :placeholder="td('att.common.namePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.nearSynonyms')" prop="nearSynonyms" :tip="td('att.common.nearSynonymsTip')" :label-position="labelPosition">
              <el-input
                v-model="form.nearSynonyms"
                :placeholder="td('att.common.nearSynonymsPlaceholder')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('att.common.synonyms')" prop="synonyms" :tip="td('att.common.synonymsTip')" :label-position="labelPosition">
              <el-input v-model="form.synonyms" :placeholder="td('att.common.synonymsPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <qt-form-item :label="td('common.texts.status')" prop="status" :tip="{content: td('att.common.statusTip')}">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dp_model_status"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}</el-radio
                >
              </el-radio-group>
            </qt-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <el-input
                v-model="form.description"
                type="textarea"
                :placeholder="td('common.form.descriptionPlaceholder')"
                maxlength="256字符"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <el-input
                v-model="form.remark"
                type="textarea"
                maxlength="256字符"
                show-word-limit
                :placeholder="td('common.form.remarkPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm"
            >{{ td('common.button.confirm') }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AttTag">
import { useI18n } from 'vue-i18n'
import useDefaultLang from "@/composables/useDefaultLang";
import {
  listAttTag,
  getAttTag,
  delAttTag,
  addAttTag,
  updateAttTag,
} from "@/api/att/tag/tag.js";
import DeptTree from "@/components/DeptTree/index.vue";
import { listAttTagCat } from "@/api/att/cat/tagCat/tagCat.js";
import { getCurrentInstance, ref, reactive, toRefs } from "vue";
import { useRouter } from "vue-router";

const { t } = useI18n();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");
const submitLoading = ref(false);
const router = useRouter();

const deptOptions = ref(undefined);
const leftWidth = ref(300); // Initial left width
const store = reactive({
  rows: [],
});
function getDeptTree() {
  listAttTagCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('att.common.tagManagementCategory'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
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
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: td('att.tag.table.name'),
      prop: "name",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
      link: {
        external: handleDetail,
      },
    },
    {
      label: td('dpp.asset.add.tag.tagCategory'),
      prop: "catName",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.description'),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('att.common.assetCount'),
      prop: "aeestCount",
      sortable: true,
      sortableKey: "aeest_count",
      width: 130,
      showOverflowTooltip: { effect: "light" },
      tip:{
        content: td('att.common.assetCountTip')
      }
    },
    {
        label: td('common.texts.status'),
        prop: "status",
        slot: "status",
        minWidth: 120,
        tip:{
          content: td('att.common.statusTip')
        }
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
      width: 240,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listAttTag,
  params: {
    catCode: null,
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('att.common.tagName'),
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: td('dpp.asset.add.tag.tagNamePlaceholder') },
    },
    {
      label: td('common.texts.createdBy'),
      prop: "createBy",
      component: { is: "input", placeholder: td('att.common.createdByPlaceholder') },
    },
    {
      label: td('common.texts.status'),
      prop: "status",
      component: {
        is: "select",
        placeholder: td('common.form.statusPlaceholder'),
        options: dp_model_status,
      },
    },
  ],
});

function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  tableRef.value.getList();
}

function handleQueryClick() {
  tableRef.value.getList();
}
let DeptTreeRef = ref(null);
function handleResetQueryClick() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = null;
  tableRef.value.resetQuery();
}

/** Enable disable switch */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? td('att.common.enable') : td('att.common.disable');
  let dataForm = {
    id: id,
    status: row.status,
  };
  proxy.$modal
      .confirm(td('att.common.confirmStatusChange', '', { status: text, name: row.name }))
      .then(function () {
      updateAttTag(dataForm).then((response) => {
        proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

const open = ref(false);
const title = ref("");
const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('att.common.tagNameRequired'), trigger: "blur" }],
    catCode: [
      { required: true, message: td('att.common.tagCatRequired'), trigger: "change" },
    ],
  },
});

const { form, rules } = toRefs(data);

// Cancel button
function cancel() {
  open.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    name: null,
    description: null,
    catCode: null,
    aeestCount: null,
    status: "1",
    nearSynonyms: null,
    synonyms: null,
    remark: null,
  };
  proxy.resetForm("AttTagRef");
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('att.common.addTag');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id;
  getAttTag(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('att.common.editTag');
  });
}
function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id,
        },
      });
    }
  }
}
/** Detail button operation */
function handleDetail(row) {
  routeTo("/da/tag/detail", row);
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["AttTagRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        form.value.status = null;
        updateAttTag(form.value).then((response) => {
          submitLoading.value = false;
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
      } else {
        addAttTag(form.value).then((response) => {
          submitLoading.value = false;
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    _ids = store.rows.map((item) => item.id).join(",");
  }
  if (!_ids) return;

  proxy.$modal
    .confirm(td('att.common.confirmDelete', '', { ids: _ids }))
    .then(function () {
      return Array.isArray(_ids)
        ? Promise.all(_ids.map((id) => delAttTag(id)))
        : delAttTag(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {
      // User cancels deletion operation
    });
}

getDeptTree();
</script>
