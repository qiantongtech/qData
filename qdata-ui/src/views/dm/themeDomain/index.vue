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
          :tableRef="tableRef"
          :visible-count="4"
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['dm:themeDomain:add']"
        >
          {{ td('common.button.add', 'Add') }}
        </el-button>
        <el-button
          class="extend-btn"
          type="primary"
          plain
          @click="toggleExpandAll"
        >
          <svg-icon v-if="defaultExpandAll" icon-class="toggle" />
          <svg-icon v-else icon-class="expand" />
          <span>{{ defaultExpandAll ? td('common.button.fold', 'Collapse') : td('common.button.expand', 'Expand') }}</span>
        </el-button>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef">
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['dm:themeDomain:edit']"
          >
            {{ td('common.button.update', 'Edit') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="handleAdd(row)"
            v-hasPermi="['dm:themeDomain:add']"
          >
            {{ td('common.button.add', 'Add') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['dm:themeDomain:remove']"
          >
            {{ td('common.button.delete', 'Delete') }}
          </el-button>
        </template>

        <template #validFlag="{ row }">
          <el-switch
            v-model="row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(row)"
          >
          </el-switch>
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
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="themeDomainRef"
        :model="form"
        :rules="rules"
        label-width="130px"
        @submit.prevent
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.name', 'Theme Domain Name')" prop="name" :label-position="labelPosition">
              <el-input v-model="form.name" :placeholder="td('dm.themeDomain.namePlaceholder', 'Please enter theme domain name')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.parentId', 'Parent Theme Domain')" prop="parentId" :label-position="labelPosition">
              <el-tree-select
                filterable
                v-model="form.parentId"
                :data="attDataElemCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('dm.themeDomain.parentIdPlaceholder', 'Please select parent theme domain')"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.dataLayerId', 'Data Layer')" prop="dataLayerId" :label-position="labelPosition">
              <el-tree-select
                filterable
                default-expand-all
                v-model="form.dataLayerId"
                :data="dataLayerOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('dm.themeDomain.dataLayerIdPlaceholder', 'Please select data layer')"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.engName', 'English Abbreviation')" prop="engName" :label-position="labelPosition">
              <el-input v-model="form.engName" :placeholder="td('dm.themeDomain.engNamePlaceholder', 'Please enter English abbreviation')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.ownerId', 'Responsible Person')" prop="ownerUserId" :label-position="labelPosition">
              <el-select
                v-model="form.ownerUserId"
                filterable
                :placeholder="td('dm.themeDomain.ownerIdPlaceholder', 'Please select responsible person')"
                @change="handleOwnerChange"
              >
                <el-option
                  v-for="item in managerOptions"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.ownerPhone', 'Responsible Person Phone')" prop="ownerUserPhoneNumber" :label-position="labelPosition">
              <el-input
                v-model="form.ownerUserPhoneNumber"
                :placeholder="td('dm.themeDomain.ownerPhonePlaceholder', 'Please enter responsible person phone')"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description', 'Description')" prop="description" :label-position="labelPosition">
              <el-input
                v-model="form.description"
                type="textarea"
                maxlength="256"
                :min-height="256"
                show-word-limit
                :placeholder="td('common.form.descriptionPlaceholder', 'Please enter description')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark', 'Remark')" prop="remark" :label-position="labelPosition">
              <el-input
                v-model="form.remark"
                type="textarea"
                maxlength="256字符"
                show-word-limit
                :placeholder="td('common.form.remarkPlaceholder', 'Please enter remark')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel', 'Cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm"
            >{{ td('common.button.confirm', 'Confirm') }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataElemCat">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  getThemeDomain,
  addThemeDomain,
  updateThemeDomain,
  listThemeDomain,
  delThemeDomain,
} from "@/api/dm/themeDomain/themeDomain.js";
import { deptUserTree, getUser } from "@/api/system/system/user.js";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import QtWrap from "@/components/QtWrap";
import QtTable from "@/components/QtTable";
import QtSearchBar from "@/components/QtSearchBar";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const attDataElemCatOptions = ref([]);
const managerOptions = ref([]);
const dataLayerOptions = ref([]);
const open = ref(false);
const title = ref("");
const tableRef = ref(null);

// Add computed properties to control expand/collapse
const defaultExpandAll = computed({
  get() {
    return tableStore.config.table.defaultExpandAll;
  },
  set(val) {
    tableStore.config.table.defaultExpandAll = val;
  },
});

const data = reactive({
  form: {},
  queryParams: {
    name: null,
    code: null,
    dataLayerId: null,
    ownerUserId: null,
  },
  rules: {
    name: [{ required: true, message: td('dm.themeDomain.nameRequired', 'Theme domain name cannot be empty'), trigger: "blur" }],
    parentId: [
      { required: true, message: td('dm.themeDomain.parentIdRequired', 'Parent theme domain cannot be empty'), trigger: "blur" },
    ],
    engName: [
      { required: true, message: td('dm.themeDomain.engNameRequired', 'English abbreviation cannot be empty'), trigger: "blur" },
      { pattern: /^[a-zA-Z]+$/, message: td('dm.themeDomain.englishOnly', 'Only English characters are allowed'), trigger: "blur" },
    ],
    ownerUserId: [
      { required: true, message: td('dm.themeDomain.ownerRequired', 'Responsible person cannot be empty'), trigger: "blur" },
    ],
    dataLayerId: [
      { required: true, message: td('dm.themeDomain.dataLayerIdRequired', 'Data layer cannot be empty'), trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

const tableStore = reactive({
  config: {
    stripe: true,
    notPagination: true,
    notPaginationParams: true,
    table: {
      rowKey: "id",
      defaultExpandAll: false,
      lazy: false,
      load: null,
      treeProps: { children: "children", hasChildren: "hasChildren" },
      defaultSort: { prop: "createTime", order: "descending" },
    },
  },
  columns: [
    {
      label: td('dm.themeDomain.name', 'Theme Domain Name'),
      prop: "name",
      width: 200,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.description', 'Description'),
      prop: "description",
      width: 250,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dm.themeDomain.engName', 'English Abbreviation'),
      prop: "engName",
      width: 200,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dm.themeDomain.dataLayerId', 'Data Layer'), prop: "dataLayerName", align: "left", width: 140 },
    { label: td('dm.themeDomain.ownerId', 'Responsible Person'), prop: "ownerUserName", align: "left" },
    {
      label: td('dm.themeDomain.ownerPhone', 'Responsible Person Phone'),
      prop: "ownerUserPhoneNumber",
      width: 140,
      align: "left",
    },
    { label: td('common.texts.status', 'Status'), prop: "validFlag", slot: "validFlag", width: 100 },
    { label: td('common.texts.remark', 'Remark'), prop: "remark", align: "left" },

    { label: td('common.texts.createdBy', 'Created By'), prop: "createBy", align: "left" },
    {
      label: td('common.texts.createdTime', 'Created Time'),
      prop: "createTime",
      sortable: true,
      date: true,
      width: 180,
    },
    { label: td('common.texts.operation', 'Operation'), width: 220, slot: "action", fixed: "right" },
  ],
  func: listThemeDomain,
  params: queryParams,
  events: {
    formatData: (data) => proxy.handleTree(data, "id", "parentId"),
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('dm.themeDomain.name', 'Theme Domain Name'),
      prop: "name",
      component: { is: "input", placeholder: td('dm.themeDomain.namePlaceholder', 'Please enter theme domain name') },
    },
    {
      label: td('dm.themeDomain.parentId', 'Parent Theme Domain'),
      prop: "code",
      component: {
        is: "tree-select",
        data: attDataElemCatOptions,
        props: { value: "code", label: "name", children: "children" },
        valueKey: "id",
        placeholder: td('dm.themeDomain.parentIdPlaceholder', 'Please select parent theme domain'),
        checkStrictly: true,
      },
    },
    {
      label: td('dm.themeDomain.dataLayerId', 'Data Layer'),
      prop: "dataLayerId",
      component: {
        is: "tree-select",
        data: dataLayerOptions,
        props: { value: "id", label: "name", children: "children" },
        valueKey: "id",
        placeholder: td('dm.themeDomain.dataLayerIdPlaceholder', 'Please select data layer'),
        checkStrictly: true,
      },
    },
    {
      label: td('dm.themeDomain.ownerId', 'Responsible Person'),
      prop: "ownerUserId",
      component: {
        is: "select",
        options: computed(() =>
          managerOptions.value.map((item) => ({
            label: item.nickName,
            value: item.userId,
          }))
        ),
        placeholder: td('dm.themeDomain.ownerIdPlaceholder', 'Please select responsible person'),
      },
    },
  ],
});

/** Query the subject domain management list */
function getList() {
  tableRef.value?.getList();
}

function getDataTree() {
  listThemeDomain().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: td('common.texts.topNode'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
}

function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

function getDataLayerTree() {
  treeDataLayer().then((response) => {
    const disableRoot = (list) => {
      return list.map((item) => {
        const newItem = { ...item };
        if (!item.parentId || item.parentId === 0 || item.parentId === "0") {
          newItem.disabled = true;
        }
        if (item.children && item.children.length) {
          newItem.children = disableRoot(item.children);
        }
        return newItem;
      });
    };
    dataLayerOptions.value = disableRoot(response.data);
  });
}

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
    parentId: null,
    description: null,
    code: null,
    engName: null,
    ownerUserId: null,
    ownerUserPhoneNumber: null,
    dataLayerId: null,
    // validFlag: true,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("themeDomainRef");
}

/** Search button action */
function handleQuery() {
  getList();
}
/** Change enabled status value */
function handleStatusChange(row) {
  const text = row.validFlag === true ? td('dm.themeDomain.enableText', 'Enable') : td('dm.themeDomain.disableText', 'Disable');
  proxy.$modal
    .confirm(td('dm.themeDomain.confirmStatusChange', 'Are you sure to "{text}" theme domain "{name}"?', { text, name: row.name }))
    .then(() => {
      updateThemeDomain({
        id: row.id,
        parentId: row.parentId,
        validFlag: row.validFlag,
      })
        .then((response) => {
          proxy.$modal.msgSuccess(td('common.message.operationSuccess', 'Operation successful'));
          getList();
        })
        .catch((err) => {
          row.validFlag = !row.validFlag;
        });
    })
    .catch(() => {
      row.validFlag = !row.validFlag;
    });
}

/** reset button action */
function resetQuery() {
  Object.keys(queryParams.value).forEach((key) => {
    queryParams.value[key] = null;
  });
  handleQuery();
}

/** Update phone number when person in charge changes */
const handleOwnerChange = (selectedValue) => {
  const selectedUser = managerOptions.value.find(
    (user) => user.userId == selectedValue
  );
  form.value.ownerUserPhoneNumber = selectedUser?.phonenumber || "";
};

/** Add button operation */
function handleAdd(row) {
  reset();
  // getTreeselect();
  listThemeDomain().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: td('common.texts.topNode'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = td('dm.themeDomain.addTitle', 'Add Theme Domain');
}

/** Expand/collapse operations */
function toggleExpandAll() {
  defaultExpandAll.value = !defaultExpandAll.value;
  tableRef.value.reload();
}

/** Modify button actions */
async function handleUpdate(row) {
  reset();
  // await getTreeselect();
  const response = await listThemeDomain();
  attDataElemCatOptions.value = [];
  // Filter computed properties of nodes
  const filteredDepts = response.data.filter((d) => {
    // Filter condition: Remove the target department ID or items whose ancestors contain the target department ID.
    return (
      d.ID !== row.id &&
      !d.parentId.toString().split(",").includes(row.id.toString())
    );
  });
  const data = { id: 0, name: td('common.texts.topNode'), children: [] };
  data.children = proxy.handleTree(filteredDepts, "id", "parentId");
  attDataElemCatOptions.value.push(data);
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  getThemeDomain(row.id).then((response) => {
    //Filter out createTime
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;

    open.value = true;
    title.value = td('dm.themeDomain.editTitle', 'Edit Theme Domain');
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["themeDomainRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateThemeDomain(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess', 'Updated successfully'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      } else {
        addThemeDomain(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess', 'Added successfully'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  proxy.$modal
    .confirm(td('dm.themeDomain.confirmDelete', 'Are you sure to delete theme domain "{name}"?', { name: row.name }))
    .then(function () {
      return delThemeDomain(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', 'Deleted successfully'));
    })
    .catch(() => {});
}

// initialization data
onMounted(() => {
  getDataTree();
  getManagerOptions();
  getDataLayerTree();
});
</script>
<style scoped lang="scss">
.extend-btn {
  .svg-icon {
    font-size: 12px;
    margin-right: 6px;
  }
}
</style>
