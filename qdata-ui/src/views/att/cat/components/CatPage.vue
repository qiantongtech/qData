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
        >{{ t('common.button.add') }}</el-button
      >
      <el-button
        type="danger"
        plain
        icon="Delete"
        :disabled="!selection.rows.length"
        v-hasPermi="[`${permBase}:remove`]"
        @click="handleDeleteSelected"
      >
        {{ t('common.button.delete') }}
      </el-button>
      <el-button
        class="toggle-expand-all"
        type="primary"
        plain
        @click="toggleExpandAll"
      >
        <svg-icon v-if="defaultExpandAll" icon-class="toggle" />
        <svg-icon v-else icon-class="expand" />
        <span>{{ defaultExpandAll ? t('common.button.collapse') : t('common.button.expand') }}</span>
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
          >{{ t('common.button.update') }}</el-button
        >
        <el-button
          link
          type="primary"
          icon="Plus"
          @click="handleAdd(row)"
          v-hasPermi="[`${permBase}:add`]"
          >{{ t('common.button.add') }}</el-button
        >
        <el-button
          link
          type="danger"
          icon="Delete"
          @click="handleDelete(row)"
          v-hasPermi="[`${permBase}:remove`]"
          :disabled="row.validFlag"
          >{{ t('common.button.delete') }}</el-button
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
  nameLabel: { type: String, default: "类目名称" },
  titleBase: { type: String, default: "类目" },
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

const { t } = useI18n();
const userStore = useUserStore();
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
      label: `${props.nameLabel}`,
      prop: "name",
      align: "left",
      width: 200,
      align: "left",
    },
    {
      label: t('common.texts.description'),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: { effect: "light" },
    },
    { label: t('common.texts.status'), prop: "validFlag", slot: "validFlag", align: "center" },
    {
      label: "排序",
      prop: "sortOrder",
      sortable: true,
      sortableKey: "sortOrder",
    },
    {
      label: t('common.texts.remark'),
      prop: "remark",
      width: 200,
      showOverflowTooltip: { effect: "light" },
    },
    { label: t('common.texts.createdBy'), prop: "createBy" },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: t('common.texts.operation'), width: 240, slot: "action", align: "center" },
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
      label: props.nameLabel,
      prop: "name",
      align: "left",
      component: { is: "input" },
    },
    {
      label: "上级类目",
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
  const text = row.validFlag === true ? "启用" : "禁用";
  proxy.$modal
    .confirm(
      '确认要"' + text + '","' + row.name + '"' + props.titleBase + "吗？"
    )
    .then(function () {
      props
        .updateFunc({
          id: row.id,
          parentId: row.parentId,
          validFlag: row.validFlag,
        })
        .then(() => {
          proxy.$modal.msgSuccess(text + "成功");
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
  const root = { id: 0, name: "顶级节点", children: [] };
  root.children = proxy.handleTree(source, "id", "parentId");
  treeOptions.value.push(root);
}

function handleAdd(row) {
  props.listFunc().then((response) => {
    buildTreeOptions(response.data);
    const parentId = row && row.id ? row.id : 0;
    catEditDialogRef.value.open({
      title: `添加${props.titleBase}`,
      nameLabel: props.nameLabel,
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
      title: `修改${props.titleBase}`,
      nameLabel: props.nameLabel,
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
        proxy.$modal.msgSuccess(t('common.message.editSuccess'));
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
        proxy.$modal.msgSuccess(t('common.message.addSuccess'));
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
    .confirm("是否确认删除" + props.titleBase + '编号为"' + id + '"的数据项？')
    .then(function () {
      return props.delFunc(id);
    })
    .then(() => {
      handleQueryClick();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
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
          `可删除${canDeleteCount}个，不可删除${cannotDeleteCount}个，是否删除可删部分`,
          t('common.message.systemPrompt'),
          {
            confirmButtonText: t('common.button.confirm'),
            cancelButtonText: t('common.button.cancel'),
            type: "warning",
          }
        ).then(() => {
          if (canDeleteCount === 0) {
            ElMessage.success("执行成功");
            return;
          } else {
            return props.delFunc(canDeleteIds).then(() => {
              ElMessage.success(t('common.message.deleteSuccess'));
              tableRef.value.getList();
            });
          }
        });
      })
      .finally(() => {});
  } else {
    ElMessageBox.confirm(
      `可删除${selection.rows.length}个，不可删除0个，是否删除可删部分`,
      t('common.message.systemPrompt'),
      { confirmButtonText: t('common.button.confirm'), cancelButtonText: t('common.button.cancel'), type: "warning" }
    )
      .then(() => props.delFunc(ids))
      .then(() => {
        ElMessage.success(t('common.message.deleteSuccess'));
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
        message: `${props.nameLabel}不能为空`,
        trigger: "blur",
      },
    ],
    parentId: [
      { required: true, message: "上级类目不能为空", trigger: "blur" },
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

