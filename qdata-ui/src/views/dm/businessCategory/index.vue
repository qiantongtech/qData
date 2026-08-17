<template>
  <div class="app-container" ref="app-container">
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStore.params"
          @query="handleQuery"
          @reset="handleReset"
        />
      </template>

      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd()"
          v-hasPermi="['dm:businesscategory:add']"
          @mousedown="(e) => e.preventDefault()"
        >
          {{ td('common.button.add', 'Add') }}
        </el-button>
        <el-button
          class="toggle-expand-all"
          type="primary"
          plain
          @click="toggleExpandAll"
        >
          <svg-icon
            v-if="tableStore.config.table.defaultExpandAll"
            icon-class="toggle"
          />
          <svg-icon v-else icon-class="expand" />
          <span>{{
            tableStore.config.table.defaultExpandAll ? td('common.button.fold', 'Collapse') : td('common.button.expand', 'Expand')
          }}</span>
        </el-button>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef">
        <template #dataDomainName="{ row }">
          <TagClamp
            :tags="(row.domainList || []).map((item) => item.dataDomainName)"
            :max-lines="2"
          />
        </template>
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
            :disabled="row.validFlag === true"
            v-hasPermi="['dm:businesscategory:edit']"
          >
            {{ td('common.button.update', 'Edit') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="handleAdd(row)"
            v-hasPermi="['dm:businesscategory:add']"
          >
            {{ td('common.button.add', 'Add') }}
          </el-button>
          <el-popover placement="bottom" :width="150" trigger="click">
            <template #reference>
              <el-button link type="primary" icon="ArrowDown">{{ td('common.button.more', 'More') }}</el-button>
            </template>
            <div style="width: 100px" class="butgdlist">
              <el-button
                link
                type="primary"
                icon="View"
                @click="handleDetail(row)"
                v-hasPermi="['dm:businesscategory:remove']"
                style="padding-left: 14px"
              >
                {{ td('common.button.details', 'Details') }}
              </el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['dm:businesscategory:remove']"
                :disabled="row.validFlag === true"
              >
                {{ td('common.button.delete', 'Delete') }}
              </el-button>
            </div>
          </el-popover>
        </template>
      </qt-table>
    </qt-wrap>

    <BusinessLayerEditDialog
      ref="businessLayerEditDialogRef"
      @submit="onDialogSubmit"
    />
  </div>
</template>

<script setup name="BusinessCategory">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  listBusinessCategory,
  getBusinessCategory,
  delBusinessCategory,
  addBusinessCategory,
  updateBusinessCategory,
} from "@/api/dm/businessCategory/businessCategory";
import { listDataDomain } from "@/api/dm/dataDomain/dataDomain.js";
import BusinessLayerEditDialog from "./components/businessLayerEditDialog.vue";
import { computed, ref, reactive, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";
import { deptUserTree } from "@/api/system/system/user";
import TagClamp from "@/components/TagClamp";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const router = useRouter();

const tableRef = ref(null);
const businessLayerEditDialogRef = ref();
const businessLayerOptions = ref([]);
const managerOptions = ref([]);

const tableStore = reactive({
  config: {
    stripe: true,
    sort: true,
    notPagination: true,
    table: {
      height: "60vh",
      rowKey: "id",
      defaultExpandAll: false,
      treeProps: { children: "children", hasChildren: "hasChildren" },
      defaultSort: { prop: "createTime", order: "descending" },
    },
  },
  columns: [
    {
      label: td('dm.businessCategory.name', 'Business Category Name'),
      prop: "name",
      align: "left",
      width: 250,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.description', 'Description'),
      prop: "description",
      align: "left",
      width: 250,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dm.businessCategory.engName', 'English Abbreviation'),
      prop: "engName",
      align: "left",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dm.businessCategory.dataDomain', 'Related Data Domain'),
      slot: "dataDomainName",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dm.businessCategory.ownerId', 'Responsible Person'), prop: "owner", width: 100 },
    { label: td('dm.businessCategory.ownerPhone', 'Responsible Person Phone'), prop: "ownerPhone", width: 120 },
    { label: td('common.texts.status', 'Status'), prop: "validFlag", width: 100, slot: "validFlag" },
    {
      label: td('common.texts.remark', 'Remark'),
      prop: "remark",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('common.texts.createdBy', 'Created By'), prop: "createBy", width: 120 },
    {
      label: td('common.texts.createdTime', 'Created Time'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: td('common.texts.operation', 'Operation'), width: 250, fixed: "right", slot: "action" },
  ],
  func: listBusinessCategory,
  params: {
    orderByColumn: "create_time",
    isAsc: "descending",
  },
  events: {
    formatData: (data) => {
      const rows = (data || []).map((row) => {
        return {
          ...row,
          engName: row?.engName ?? row?.shortName,
          owner: row?.owner ?? row?.ownerName,
          domainId: row?.domainId ?? row?.dataDomainId,
        };
      });
      return proxy.handleTree(rows, "id", "parentId");
    },
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('dm.businessCategory.name', 'Business Category Name'),
      prop: "name",
      component: { is: "input", placeholder: td('dm.businessCategory.namePlaceholder', 'Please enter business category name') },
    },
    {
      label: td('dm.businessCategory.upperCategory', 'Parent Business Category'),
      prop: "parentId",
      component: {
        is: "tree-select",
        data: businessLayerOptions,
        props: { value: "id", label: "name", children: "children" },
        valueKey: "id",
        placeholder: td('dm.businessCategory.parentIdPlaceholder', 'Please select parent business category'),
        checkStrictly: true,
        clearable: true,
        filterable: true,
      },
    },
    {
      label: td('dm.businessCategory.ownerId', 'Responsible Person'),
      prop: "ownerId",
      component: {
        is: "tree-select",
        data: managerOptions,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "userId",
        placeholder: td('dm.businessCategory.ownerIdPlaceholder', 'Please select responsible person'),
        clearable: true,
        filterable: true,
        checkStrictly: true,
      },
    },
  ],
});

function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data || [];
  });
}

/** Get tree structure data */
function getTreeData() {
  listBusinessCategory().then((response) => {
    const rows = response?.data || [];
    businessLayerOptions.value = [];
    const treeData = { id: 0, name: td('common.texts.topNode'), children: [] };
    treeData.children = proxy.handleTree(rows, "id", "parentId");
    businessLayerOptions.value.push(treeData);
  });
}

function handleQuery() {
  tableRef.value?.getList();
}
function handleReset() {
  tableRef.value?.resetQuery();
}
function handleDetail(row) {
  router.push({ path: "/dm/businessCategory/detail", query: { id: row.id } });
}
function handleStatusChange(row) {
  const text = row.validFlag ? td('dm.businessCategory.enableText', 'Enable') : td('dm.businessCategory.disableText', 'Disable');
  proxy.$modal
    .confirm(td('dm.businessCategory.confirmStatusChange', 'Are you sure to "{text}" business category "{name}"?', { text, name: row.name }))
    .then(() =>
      updateBusinessCategory({
        id: row.id,
        parentId: row.parentId,
        validFlag: row.validFlag,
      })
    )
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.operationSuccess', 'Operation successful'));
      tableRef.value?.getList();
    })
    .catch(() => {
      row.validFlag = !row.validFlag;
    });
}
function handleAdd(row) {
  businessLayerEditDialogRef.value.open({
    title: td('dm.businessCategory.addTitle', 'Add Business Category'),
    managerOptions: managerOptions.value,
    form: { parentId: row?.id || 0, validFlag: false, sortOrder: 0 },
  });
}

function handleUpdate(row) {
  getBusinessCategory(row.id).then((res) => {
    const mappedForm = {
      ...res.data,
      domainId: res.data?.domainId ?? res.data?.dataDomainId,
      engName: res.data?.engName ?? res.data?.shortName,
    };
    businessLayerEditDialogRef.value.open({
      title: td('dm.businessCategory.editTitle', 'Edit Business Category'),
      managerOptions: managerOptions.value,
      form: mappedForm,
    });
  });
}
function onDialogSubmit(payload) {
  const apiCall =
    payload.id != null
      ? updateBusinessCategory(payload)
      : addBusinessCategory(payload);
  apiCall
    .then(() => {
      proxy.$modal.msgSuccess(payload.id ? td('common.message.editSuccess', 'Updated successfully') : td('common.message.addSuccess', 'Added successfully'));
      businessLayerEditDialogRef.value.close();
      businessLayerEditDialogRef.value.refreshTreeData(); // Refresh the internal tree cache of the pop-up window
      tableRef.value?.getList();
      getTreeData();
    })
    .catch(() => {
      businessLayerEditDialogRef.value.stopLoading();
    });
}
function handleDelete(row) {
  proxy.$modal
    .confirm(td('dm.businessCategory.confirmDelete', 'Are you sure to delete business category "{name}"?', { name: row.name }))
    .then(() => delBusinessCategory(row.id))
    .then(() => {
      tableRef.value?.getList();
      getTreeData();
      businessLayerEditDialogRef.value.refreshTreeData(); // Refresh the internal tree cache of the pop-up window
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', 'Deleted successfully'));
    })
    .catch(() => {});
}
function toggleExpandAll() {
  tableStore.config.table.defaultExpandAll =
    !tableStore.config.table.defaultExpandAll;
  tableRef.value?.reload();
}

getTreeData();
getManagerOptions();
</script>
