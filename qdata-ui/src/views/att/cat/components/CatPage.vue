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
  <qt-wrap
    :columns="tableStore.columns"
    :tableRef="tableRef"
    v-loading="loading"
  >
    <template #search>
      <qt-search-bar
        v-bind="searchStore"
        :params="tableStore.params"
        :tableRef="tableRef"
      />
    </template>
    <template #actions-data>
      <el-button
        type="primary"
        plain
        icon="Plus"
        @click="handleAdd()"
        v-hasPermi="[`${permBase}:add`]"
        >{{ td('common.button.add') }}</el-button
      >
      <el-button
        type="danger"
        plain
        icon="Delete"
        :disabled="!selection.rows.length"
        v-hasPermi="[`${permBase}:remove`]"
        @click="handleDeleteSelected"
      >
        {{ td('common.button.delete') }}
      </el-button>
      <el-button
        class="toggle-expand-all"
        type="primary"
        plain
        @click="toggleExpandAll"
      >
        <svg-icon v-if="defaultExpandAll" icon-class="toggle" />
        <svg-icon v-else icon-class="expand" />
        <span>{{ defaultExpandAll ? td('common.button.fold') : td('common.button.expand') }}</span>
      </el-button>
    </template>
    <qt-table v-bind="tableStore" :key="tableKey" ref="tableRef">
      <template #validFlag="{ row }">
        <el-switch
          v-model="row.validFlag"
          active-color="#13ce66"
          inactive-color="#ff4949"
          @change="handleStatusChange(row)"
        />
      </template>
      <template #action="{ row }">
        <el-button
          link
          type="primary"
          icon="Edit"
          @click="handleUpdate(row)"
          v-hasPermi="[`${permBase}:edit`]"
          >{{ td('common.button.update') }}</el-button
        >
        <el-button
          link
          type="primary"
          icon="Plus"
          @click="handleAdd(row)"
          v-hasPermi="[`${permBase}:add`]"
          >{{ td('common.button.add') }}</el-button
        >
        <el-button
          link
          type="danger"
          icon="Delete"
          @click="handleDelete(row)"
          v-hasPermi="[`${permBase}:remove`]"
          :disabled="row.validFlag"
          >{{ td('common.button.delete') }}</el-button
        >
      </template>
    </qt-table>
  </qt-wrap>

  <CatEditDialog
    ref="catEditDialogRef"
    @cancel="onDialogCancel"
    @submit="onDialogSubmit"
  />
</template>

<script setup>
import { useI18n } from 'vue-i18n'

const props = defineProps({
  listFunc: { type: Function, required: true },
  getFunc: { type: Function, required: true },
  delFunc: { type: Function, required: true },
  addFunc: { type: Function, required: true },
  updateFunc: { type: Function, required: true },
  batchDelCheckFunc: { type: Function, required: false },
  nameLabel: { type: String, default: "Category Name" },
  titleBase: { type: String, default: "Category" },
  permBase: { type: String, required: true },
  checkProjectParams: { type: Boolean, default: false },
});

import {
  ref,
  reactive,
  toRefs,
  getCurrentInstance,
  watch,
  computed,
} from "vue";
import CatEditDialog from "./catEditDialog.vue";
import useUserStore from "@/store/system/user";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const userStore = useUserStore();

const effectiveNameLabel = computed(() => props.nameLabel || td('att.common.categoryName'));
const effectiveTitleBase = computed(() => props.titleBase || td('att.common.category'));
const { proxy } = getCurrentInstance();
const catEditDialogRef = ref();
const appContainerRef = ref();
const isExpandAll = ref(false);
const tableKey = ref(0);
const tableRef = ref(null);
const selection = reactive({ rows: [] });

const treeOptions = ref([]);

const tableStore = reactive({
  config: {
    stripe: true,
    notPagination: true,
    notPaginationParams: true,
    sort: true,
    table: {
      rowKey: "id",
      defaultExpandAll: false,
      defaultSort: { prop: "sortOrder", order: "ascending" },
      treeProps: { children: "children", hasChildren: "hasChildren" },
      onSelectionChange: function (rows) {
        selection.rows = rows;
      },
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
      selectable: function (row) {
        return true;
      },
    },
    {
      label: `${effectiveNameLabel.value}`,
      prop: "name",
      align: "left",
      width: 220,
      align: "left",
    },
    {
      label: computed(()=>td('common.texts.description')),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: { effect: "light" },
    },
    { label: computed(()=>td('common.texts.status')), prop: "validFlag", slot: "validFlag", align: "center" },
    {
      label: computed(()=>td('common.texts.sortOrder')),
      prop: "sortOrder",
      sortable: true,
      sortableKey: "sortOrder",
    },
    {
      label: computed(()=>td('common.texts.remark')),
      prop: "remark",
      width: 200,
      showOverflowTooltip: { effect: "light" },
    },
    { label: computed(()=>td('common.texts.createdBy')), prop: "createBy" },
    {
      label: computed(()=>td('common.texts.createdTime')),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: computed(()=>td('common.texts.operation')), width: 240, slot: "action", align: "center" },
  ],
  func: props.listFunc,
  params: {},
  events: {
    formatData: (data) => proxy.handleTree(data, "id", "parentId"),
  },
});

const searchStore = reactive({
  items: [
    {
      label: effectiveNameLabel.value,
      prop: "name",
      align: "left",
      component: { is: "input" },
    },
    {
      label: td('att.common.parentCat'),
      prop: "code",
      component: {
        is: "tree-select",
        data: treeOptions,
        props: { value: "code", label: "name", children: "children" },
        valueKey: "id",
        checkStrictly: true,
      },
    },
  ],
  config: { permi: [`${props.permBase}:query`] },
});
const defaultExpandAll = computed({
  get() {
    return tableStore.config.table.defaultExpandAll;
  },
  set(val) {
    tableStore.config.table.defaultExpandAll = val;
  },
});
function toggleExpandAll() {
  defaultExpandAll.value = !defaultExpandAll.value;
  tableRef.value.reload();
}
function handleQueryClick() {
  tableRef.value && tableRef.value.getList();
}

if (props.checkProjectParams) {
  watch(
    () => userStore.projectCode,
    (val) => {
      if (val) {
        tableStore.params.projectCode = val;
        tableStore.params.projectId = userStore.projectId;
        handleQueryClick();
      }
    },
    { immediate: true }
  );
}

function handleStatusChange(row) {
  const text = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
  proxy.$modal
    .confirm(
      td('att.common.confirmStatusChangeGeneric').replace('<status>', text).replace('<name>', row.name).replace('<type>', effectiveTitleBase.value)
    )
    .then(function () {
      props
        .updateFunc({
          id: row.id,
          parentId: row.parentId,
          validFlag: row.validFlag,
        })
        .then(() => {
          proxy.$modal.msgSuccess(td('att.common.statusSuccess').replace('<status>', text));
          handleQueryClick();
        })
        .catch(() => {
          row.validFlag = !row.validFlag;
        });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

function buildTreeOptions(source) {
  treeOptions.value = [];
  const root = { id: 0, name: td('common.texts.topNode'), children: [] };
  root.children = proxy.handleTree(source, "id", "parentId");
  treeOptions.value.push(root);
}

function handleAdd(row) {
  props.listFunc().then((response) => {
    buildTreeOptions(response.data);
    const parentId = row && row.id ? row.id : 0;
    catEditDialogRef.value.open({
      title: td('att.common.add') + effectiveTitleBase.value,
      nameLabel: effectiveNameLabel.value,
      treeOptions: treeOptions.value,
      form: { parentId },
      rules: rules.value,
    });
  });
}

async function handleUpdate(row) {
  const response = await props.listFunc();
  const filtered = response.data.filter((d) => {
    return (
      d.ID !== row.id &&
      !d.parentId.toString().split(",").includes(row.id.toString())
    );
  });
  buildTreeOptions(filtered);
  props.getFunc(row.id).then((res) => {
    catEditDialogRef.value.open({
      title: td('att.common.edit') + effectiveTitleBase.value,
      nameLabel: effectiveNameLabel.value,
      treeOptions: treeOptions.value,
      form: res.data,
      rules: rules.value,
    });
  });
}

function onDialogSubmit(payload) {
  if (props.checkProjectParams) {
    payload.projectId = userStore.projectId;
    payload.projectCode = userStore.projectCode;
  }
  if (payload.id != null) {
    props
      .updateFunc(payload)
      .then(() => {
        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
        handleQueryClick();
        catEditDialogRef.value.close();
      })
      .catch(() => {
        catEditDialogRef.value.stopLoading();
      });
  } else {
    props
      .addFunc(payload)
      .then(() => {
        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
        handleQueryClick();
        catEditDialogRef.value.close();
      })
      .catch(() => {
        catEditDialogRef.value.stopLoading();
      });
  }
}

function onDialogCancel() {}

function handleDelete(row) {
  const id = row.id;
  proxy.$modal
    .confirm(td('att.common.confirmDeleteCat').replace('<titleBase>', effectiveTitleBase.value).replace('<id>', id))
    .then(function () {
      return props.delFunc(id);
    })
    .then(() => {
      handleQueryClick();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}
let loading = ref(false);
function handleDeleteSelected() {
  if (!selection.rows.length) return;
  const ids = selection.rows.map((item) => item.id);
  if (props.batchDelCheckFunc) {
    loading.value = true;
    props
      .batchDelCheckFunc(ids)
      .then((res) => {
        loading.value = false;

        const {
          cannotDeleteCount = 0,
          canDeleteIds = [],
          canDeleteCount = 0,
        } = res?.data || {};
        return ElMessageBox.confirm(
          td('att.common.deleteConfirmCount').replace('<canDeleteCount>', canDeleteCount).replace('<cannotDeleteCount>', cannotDeleteCount),
          td('common.message.systemPrompt'),
          {
            confirmButtonText: td('common.button.confirm'),
            cancelButtonText: td('common.button.cancel'),
            type: "warning",
          }
        ).then(() => {
          if (canDeleteCount === 0) {
            ElMessage.success(td('common.message.msgOpSuccess'));
            return;
          } else {
            return props.delFunc(canDeleteIds).then(() => {
              ElMessage.success(td('common.message.deleteSuccess'));
              tableRef.value.getList();
            });
          }
        });
      })
      .finally(() => {});
  } else {
    ElMessageBox.confirm(
      td('att.common.deleteConfirmCount').replace('<canDeleteCount>', selection.rows.length).replace('<cannotDeleteCount>', 0),
      td('common.message.systemPrompt'),
      { confirmButtonText: td('common.button.confirm'), cancelButtonText: td('common.button.cancel'), type: "warning" }
    )
      .then(() => props.delFunc(ids))
      .then(() => {
        ElMessage.success(td('common.message.deleteSuccess'));
        tableRef.value.getList();
      });
  }
}

const data = reactive({
  form: {},
  rules: {
    name: [
      {
        required: true,
        message: td('att.common.nameRequired', { name: effectiveNameLabel.value }),
        trigger: "blur",
      },
    ],
    parentId: [
      { required: true, message: td('att.common.parentCatRequired'), trigger: "blur" },
    ],
  },
});
const { form, rules } = toRefs(data);

defineExpose({
  refresh: handleQueryClick,
  tableStore,
});

props.listFunc().then((response) => buildTreeOptions(response.data));
</script>

