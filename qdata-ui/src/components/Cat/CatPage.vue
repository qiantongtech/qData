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
  nameLabel: { type: String, default: t('components.catPage.nameLabel') },
  titleBase: { type: String, default: t('components.catPage.titleBase') },
  permBase: { type: String, required: true },
});

import { ref, reactive, toRefs, getCurrentInstance } from "vue";
import CatEditDialog from "./catEditDialog.vue";

const { t } = useI18n();
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
    { label: props.nameLabel, prop: "name", width: 200, align: "left" },
    {
      label: t('common.texts.description'),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: { effect: "light" },
    },
    { label: t('common.texts.status'), prop: "validFlag", slot: "validFlag", align: "center" },
    {
      label: t('components.catPage.sortOrder'),
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
    { label: props.nameLabel, prop: "name", component: { is: "input" } },
    {
      label: t('components.catPage.parentCat'),
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

function handleStatusChange(row) {
  const text = row.validFlag === true ? t('components.catPage.enable') : t('components.catPage.disable');
  proxy.$modal
    .confirm(
      t('components.catPage.confirmEnableDisable', { text, name: row.name, titleBase: props.titleBase })
    )
    .then(function () {
      props
        .updateFunc({
          id: row.id,
          parentId: row.parentId,
          validFlag: row.validFlag,
        })
        .then(() => {
          proxy.$modal.msgSuccess(t('components.catPage.operationSuccess', { text }));
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
  const root = { id: 0, name: t('components.catPage.topNode'), children: [] };
  root.children = proxy.handleTree(source, "id", "parentId");
  treeOptions.value.push(root);
}

function handleAdd(row) {
  props.listFunc().then((response) => {
    buildTreeOptions(response.data);
    const parentId = row && row.id ? row.id : 0;
    catEditDialogRef.value.open({
      title: t('components.catPage.addTitle', { titleBase: props.titleBase }),
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
      d.id !== row.id &&
      !(
        d.parentId != null &&
        d.parentId.toString().split(",").includes(row.id.toString())
      )
    );
  });
  buildTreeOptions(filtered);
  props.getFunc(row.id).then((res) => {
    catEditDialogRef.value.open({
      title: t('components.catPage.modifyTitle', { titleBase: props.titleBase }),
      nameLabel: props.nameLabel,
      treeOptions: treeOptions.value,
      form: res.data,
      rules: rules.value,
    });
  });
}

function onDialogSubmit(payload) {
  if (payload.id != null) {
    props
      .updateFunc(payload)
      .then(() => {
        proxy.$modal.msgSuccess(t('common.message.editSuccess'));
        handleQueryClick();
      })
      .catch(() => {});
  } else {
    props
      .addFunc(payload)
      .then(() => {
        proxy.$modal.msgSuccess(t('common.message.addSuccess'));
        handleQueryClick();
      })
      .catch(() => {});
  }
}

function onDialogCancel() {}

function handleDelete(row) {
  const id = row.id;
  proxy.$modal
    .confirm(t('components.catPage.deleteConfirm', { titleBase: props.titleBase, id }))
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
          t('components.catPage.batchDeleteConfirm', { canDeleteCount, cannotDeleteCount }),
          t('common.message.systemPrompt'),
          {
            confirmButtonText: t('common.button.confirm'),
            cancelButtonText: t('common.button.cancel'),
            type: "warning",
          }
        ).then(() => {
          if (canDeleteCount === 0) {
            ElMessage.success(t('components.catPage.executeSuccess'));
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
      t('components.catPage.batchDeleteAllConfirm', { count: selection.rows.length }),
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
        message: t('components.catEditDialog.nameRequired', { nameLabel: props.nameLabel }),
        trigger: "blur",
      },
    ],
    parentId: [
      { required: true, message: t('components.catEditDialog.parentIdRequired'), trigger: "blur" },
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

