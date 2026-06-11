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
          {{ t('common.button.add') }}
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
            tableStore.config.table.defaultExpandAll ? t('common.button.collapse') : t('common.button.expand')
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
            {{ t('common.button.update') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="handleAdd(row)"
            v-hasPermi="['dm:businesscategory:add']"
          >
            {{ t('common.button.add') }}
          </el-button>
          <el-popover placement="bottom" :width="150" trigger="click">
            <template #reference>
              <el-button link type="primary" icon="ArrowDown">{{ t('common.button.more') }}</el-button>
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
                {{ t('common.button.details') }}
              </el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['dm:businesscategory:remove']"
                :disabled="row.validFlag === true"
              >
                {{ t('common.button.delete') }}
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
import { useI18n } from 'vue-i18n'
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

const { t } = useI18n();
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
      label: "业务分类名称",
      prop: "name",
      align: "left",
      width: 250,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.description'),
      prop: "description",
      align: "left",
      width: 250,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "英文缩写",
      prop: "engName",
      align: "left",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "关联数据域",
      slot: "dataDomainName",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    { label: "负责人", prop: "owner", width: 100 },
    { label: "负责人电话", prop: "ownerPhone", width: 120 },
    { label: t('common.texts.status'), prop: "validFlag", width: 100, slot: "validFlag" },
    {
      label: t('common.texts.remark'),
      prop: "remark",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    { label: t('common.texts.createdBy'), prop: "createBy", width: 120 },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: t('common.texts.operation'), width: 250, fixed: "right", slot: "action" },
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
      label: "业务分类名称",
      prop: "name",
      component: { is: "input", placeholder: "请输入业务分类名称" },
    },
    {
      label: "上级业务分类",
      prop: "parentId",
      component: {
        is: "tree-select",
        data: businessLayerOptions,
        props: { value: "id", label: "name", children: "children" },
        valueKey: "id",
        placeholder: "请选择上级业务分类",
        checkStrictly: true,
        clearable: true,
        filterable: true,
      },
    },
    {
      label: "负责人",
      prop: "ownerId",
      component: {
        is: "tree-select",
        data: managerOptions,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "userId",
        placeholder: "请选择负责人",
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

/** 获取树结构数据 */
function getTreeData() {
  listBusinessCategory().then((response) => {
    const rows = response?.data || [];
    businessLayerOptions.value = [];
    const treeData = { id: 0, name: "顶级节点", children: [] };
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
  const text = row.validFlag ? "启用" : "禁用";
  proxy.$modal
    .confirm(`确认要"${text}","${row.name}"业务分类吗？`)
    .then(() =>
      updateBusinessCategory({
        id: row.id,
        parentId: row.parentId,
        validFlag: row.validFlag,
      })
    )
    .then(() => {
      proxy.$modal.msgSuccess(`${text}成功`);
      tableRef.value?.getList();
    })
    .catch(() => {
      row.validFlag = !row.validFlag;
    });
}
function handleAdd(row) {
  businessLayerEditDialogRef.value.open({
    title: "新增业务分类",
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
      title: "修改业务分类",
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
      proxy.$modal.msgSuccess(payload.id ? t('common.message.editSuccess') : t('common.message.addSuccess'));
      businessLayerEditDialogRef.value.close();
      businessLayerEditDialogRef.value.refreshTreeData(); // 刷新弹窗内部树缓存
      tableRef.value?.getList();
      getTreeData();
    })
    .catch(() => {
      businessLayerEditDialogRef.value.stopLoading();
    });
}
function handleDelete(row) {
  proxy.$modal
    .confirm(`是否确认删除业务分类名称为"${row.name}"的数据项？`)
    .then(() => delBusinessCategory(row.id))
    .then(() => {
      tableRef.value?.getList();
      getTreeData();
      businessLayerEditDialogRef.value.refreshTreeData(); // 刷新弹窗内部树缓存
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
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
