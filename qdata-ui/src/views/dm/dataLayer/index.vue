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
    <GuideTip tip-id="dm/dataLayer.list" />
    <el-container>
      <!-- tree on left -->
      <DeptTree
        :deptOptions="layerTreeOptions"
        :leftWidth="leftWidth"
        :placeholder="td('dm.dataLayer.namePlaceholder', 'Please enter data layer name')"
        ref="layerTreeRef"
        @node-click="handleNodeClick"
        :title="td('dm.dataLayer.title', 'Data Warehouse Layer')"
      >
      </DeptTree>

      <!-- Right list -->
      <el-main class="main-content">
        <!-- Top information card -->
        <layerInfoCard v-if="currentLayer" class="mb15" :layer="currentLayer" />

        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
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
              @click="handleAdd"
              v-hasPermi="['dm:dataLayer:add']"
            >
              {{ td('common.button.add', 'Add') }}
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                v-hasPermi="['dm:dataLayer:edit']"
              >
                {{ td('common.button.update', 'Edit') }}
              </el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['dm:dataLayer:remove']"
              >
                {{ td('common.button.delete', 'Delete') }}
              </el-button>
              <el-button
                link
                type="primary"
                icon="View"
                @click="handleDetail(row)"
                v-hasPermi="['dm:dataLayer:edit']"
              >
                {{ td('common.button.details', 'Details') }}
              </el-button>
            </template>

            <template #status="{ row }">
              <el-switch
                v-model="row.status"
                active-value="1"
                inactive-value="0"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="handleStatusChange(row)"
              />
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- Add or modify specification dialog box -->
    <el-dialog
      :title="title"
      v-model="open"
      :append-to="$refs['app-container']"
      draggable
      width="800px"
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="specificationRef"
        :model="form"
        :rules="rules"
        label-width="140px"
        @submit.prevent
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.dataLayer.prefixName', 'Table Prefix')" prop="prefixName" :label-position="labelPosition">
              <el-input v-model="form.prefixName" :placeholder="td('dm.dataLayer.prefixNamePlaceholder', 'Please enter table prefix')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('dm.dataLayer.businessEngName', 'Business English Abbreviation')" prop="businessEngName" :label-position="labelPosition">
              <el-input
                v-model="form.businessEngName"
                :placeholder="td('dm.dataLayer.businessEngNamePlaceholder', 'Please enter business English abbreviation')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.dataLayer.ownerUserId', 'Responsible Person')" prop="ownerUserId" :label-position="labelPosition">
              <el-select
                v-model="form.ownerUserId"
                filterable
                :placeholder="td('dm.dataLayer.ownerUserIdPlaceholder', 'Please select responsible person')"
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
            <el-form-item :label="td('dm.dataLayer.ownerUserPhoneNumber', 'Responsible Person Phone')" prop="ownerUserPhoneNumber" :label-position="labelPosition">
              <el-input
                v-model="form.ownerUserPhoneNumber"
                :placeholder="td('dm.dataLayer.ownerUserPhoneNumberPlaceholder', 'Please enter responsible person phone')"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.status', 'Status')" prop="status" :label-position="labelPosition">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >
                  {{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description', 'Description')" prop="description" :label-position="labelPosition">
              <el-input
                v-model="form.description"
                type="textarea"
                :placeholder="td('common.form.descriptionPlaceholder', 'Please enter description')"
                :min-height="192"
                show-word-limit
                maxlength="500"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel', 'Cancel') }}</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm', 'Confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Specification Details Dialog Box -->
    <el-dialog
      :title="td('dm.dataLayer.specificationDetail', 'Specification Detail')"
      v-model="openDetail"
      :append-to="$refs['app-container']"
      draggable
      width="800px"
    >
      <el-form ref="specificationDetailRef" :model="form" label-width="140px" :label-position="labelPosition">
        <el-form-item :label="td('common.texts.number', 'No.') + ':'" prop="id" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dm.dataLayer.prefixName', 'Table Prefix')" prop="prefixName" :label-position="labelPosition">
          <div class="form-readonly">{{ form.prefixName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataLayer.businessEngName', 'Business English Abbreviation')" prop="businessEngName" :label-position="labelPosition">
          <div class="form-readonly">{{ form.businessEngName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataLayer.ownerUserId', 'Responsible Person')" prop="ownerUserName" :label-position="labelPosition">
          <div class="form-readonly">{{ form.ownerUserName || "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataLayer.ownerUserPhoneNumber', 'Responsible Person Phone')" prop="ownerUserPhoneNumber" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.ownerUserPhoneNumber || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.status', 'Status')" prop="status" :label-position="labelPosition">
          <dict-tag :options="sys_normal_disable" :value="form.status" />
        </el-form-item>
        <el-form-item :label="td('common.texts.description', 'Description')" prop="description" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.createdBy', 'Created By')" prop="createBy" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.createBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.createdTime', 'Created Time')" prop="createTime" :label-position="labelPosition">
              <div class="form-readonly">
                {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.updatedBy', 'Updated By')" prop="createBy" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.updateBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.updatedTime', 'Updated Time')" prop="updateTime" :label-position="labelPosition">
              <div class="form-readonly">
                {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelDetail">{{ td('common.button.close', 'Close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataLayer">
import useDefaultLang from "@/composables/useDefaultLang"
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import {
  listDataLayerSpecification,
  getDataLayerSpecification,
  addDataLayerSpecification,
  updateDataLayerSpecification,
  delDataLayerSpecification,
} from "@/api/dm/dataLayerSpecification/dataLayerSpecification.js";
import DeptTree from "@/components/DeptTree";
import { deptUserTree, getUser } from "@/api/system/system/user.js";
import layerInfoCard from "./components/layerInfoCard.vue";
import {
  computed,
  getCurrentInstance,
  nextTick,
  onMounted,
  reactive,
  ref,
  toRefs,
} from "vue";

// Import necessary icon components
import { FolderOpened, Folder, Tickets } from "@element-plus/icons-vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");
const leftWidth = ref(300); // Initial left width
const layerTreeOptions = ref([]);
const currentLayer = ref(null);
const layerTreeRef = ref(null);
const managerOptions = ref([]);
const tableRef = ref(null);
const activeDropdownNodeId = ref(null);

function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

function handleOwnerChange(val) {
  const selected = managerOptions.value.find((item) => item.userId === val);
  if (selected) {
    form.value.ownerUserName = selected.nickName;
    // Update the phone number of the person in charge
    form.value.ownerUserPhoneNumber = selected.phonenumber || "";
  }
}

const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const tableStore = reactive({
  config: {
    stripe: true,
    table: {
      rowKey: "id",
      defaultSort: { prop: "createTime", order: "descending" },
      onSelectionChange: function (selection) {
        ids.value = selection.map((item) => item.id);
        single.value = selection.length !== 1;
        multiple.value = !selection.length;
      },
    },
  },
  columns: [
    // { type: "selection", width: 55, align: "left" },
    { label: td('common.texts.number', 'No.'), prop: "id", width: 60, sortable: true },
    {
      label: td('dm.dataLayer.prefixName', 'Table Prefix'),
      prop: "prefixName",
      align: "left",
      width: 180,
      showOverflowTooltip: true,
    },
    {
      label: td('common.texts.description', 'Description'),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td('dm.dataLayer.businessEngName', 'Business English Abbreviation'),
      prop: "businessEngName",
      align: "left",
      width: 140,
    },

    {
      label: td('common.texts.status', 'Status'),
      prop: "status",
      align: "left",
      width: 100,
      slot: "status",
    },
    { label: td('dm.dataLayer.ownerUserId', 'Responsible Person'), prop: "ownerUserName", align: "left", width: 120 },
    {
      label: td('dm.dataLayer.ownerUserPhoneNumber', 'Responsible Person Phone'),
      prop: "ownerUserPhoneNumber",
      align: "left",
      width: 140,
    },

    {
      label: td('common.texts.createdBy', 'Created By'),
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: true,
    },
    {
      label: td('common.texts.createdTime', 'Created Time'),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 150,
      align: "left",
    },
    {
      label: td('common.texts.operation', 'Operation'),
      width: 240,
      slot: "action",
      fixed: "right",
    },
  ],
  func: listDataLayerSpecification,
  params: {
    dataLayerId: computed(() => currentLayer.value?.id || null),
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('dm.dataLayer.prefixName', 'Table Prefix'),
      prop: "prefixName",
      component: { is: "input", placeholder: td('dm.dataLayer.prefixNamePlaceholder', 'Please enter table prefix') },
    },
    {
      label: td('dm.dataLayer.businessEngName', 'Business English Abbreviation'),
      prop: "businessEngName",
      component: { is: "input", placeholder: td('dm.dataLayer.businessEngNamePlaceholder', 'Please enter business English abbreviation') },
    },
    {
      label: td('dm.dataLayer.ownerUserId', 'Responsible Person'),
      prop: "ownerUserId",
      component: {
        is: "tree-select",
        data: managerOptions,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "ID",
        placeholder: td('dm.dataLayer.ownerUserIdPlaceholder', 'Please select responsible person'),
        checkStrictly: true,
      },
    },
  ],
});

const open = ref(false);
const openDetail = ref(false);
const title = ref("");

const data = reactive({
  form: {},
  rules: {
    prefixName: [
      { required: true, message: td('dm.dataLayer.prefixNameRequired', 'Table prefix cannot be empty'), trigger: "blur" },
    ],
    businessEngName: [
      { required: true, message: td('dm.dataLayer.businessEngNameRequired', 'Business English abbreviation cannot be empty'), trigger: "blur" },
      { pattern: /^[a-zA-Z]+$/, message: td('dm.dataLayer.englishOnly', 'Only English characters are allowed'), trigger: "blur" },
    ],
    ownerUserId: [
      { required: true, message: td('dm.dataLayer.ownerUserIdRequired', 'Responsible person cannot be empty'), trigger: "blur" },
    ],
  },
});

const { form, rules } = toRefs(data);

/** query tree */
function getTree() {
  treeDataLayer().then((response) => {
    layerTreeOptions.value = response.data;
    nextTick(() => {
      let targetNode = null;
      const findNode = (nodes) => {
         for (let node of nodes) {
          if (node.name === "操作数据层") {
            targetNode = node;
            return;
          }
          if (node.children && node.children.length > 0) {
            findNode(node.children);
          }
        }
      };
      findNode(layerTreeOptions.value);

      if (targetNode) {
        if (layerTreeRef.value && layerTreeRef.value.setCurrentKey) {
          layerTreeRef.value.setCurrentKey(targetNode.id);
        }
        currentLayer.value = targetNode;
        tableRef.value?.getList();
      } else if (layerTreeOptions.value.length > 0) {
        const firstNode = layerTreeOptions.value[0];
        if (layerTreeRef.value && layerTreeRef.value.setCurrentKey) {
          layerTreeRef.value.setCurrentKey(firstNode.id);
        }
        currentLayer.value = firstNode;
        tableRef.value?.getList();
      }
    });
  });
}

/** Node click event */
function handleNodeClick(data) {
  if (data.parentId != 0) {
    currentLayer.value = data;
    tableRef.value?.getList();
  }
}

// Handling tree node clicks
function handleTreeNodeClick(node) {
  // If it is a first-level node, it will only expand/collapse and will not trigger selection.
  if (node.level === 1) {
    if (node.expanded) {
      node.collapse();
    } else {
      node.expand();
    }
  } else {
    // If it is not a first-level node, execute normal node click logic.
    currentLayer.value = node.data;
    tableRef.value?.getList();
  }
}

// Cancel button
function cancel() {
  open.value = false;
  reset();
}

// Close details
function cancelDetail() {
  openDetail.value = false;
}

// form reset
function reset() {
  form.value = {
    id: null,
    dataLayerId: currentLayer.value ? currentLayer.value.id : null,
    prefixName: null,
    businessEngName: null,
    ownerUserId: null,
    ownerUserName: null,
    ownerUserPhoneNumber: null,
    status: "0",
    description: null,
  };
  proxy.resetForm("specificationRef");
}

/** Add button operation */
function handleAdd() {
  reset();
  if (!currentLayer.value) {
    proxy.$modal.msgError(td('dm.dataLayer.pleaseSelectLayer', 'Please select a data layer on the left first'));
    return;
  }
  open.value = true;
  title.value = td('dm.dataLayer.addSpecification', 'Add Specification');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row?.id || ids.value[0];
  getDataLayerSpecification(_id).then((response) => {
    form.value = response.data;
    if (form.value.ownerUserName && !form.value.ownerUserId) {
      const selected = managerOptions.value.find(
        (item) => item.nickName === form.value.ownerUserName
      );
      if (selected) {
        form.value.ownerUserId = selected.userId;
      }
    }
    open.value = true;
    title.value = td('dm.dataLayer.editSpecification', 'Edit Specification');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row?.id || ids.value[0];
  getDataLayerSpecification(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["specificationRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDataLayerSpecification(form.value).then(() => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess', 'Updated successfully'));
          open.value = false;
          tableRef.value?.getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      } else {
        addDataLayerSpecification(form.value).then(() => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess', 'Added successfully'));
          open.value = false;
          tableRef.value?.getList();
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
  const _ids = row?.id || ids.value;
  proxy.$modal
    .confirm(td('dm.dataLayer.confirmDeleteSpecification', 'Are you sure to delete specification "{id}"?', { id: _ids }))
    .then(function () {
      return delDataLayerSpecification(_ids);
    })
    .then(() => {
      tableRef.value?.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', 'Deleted successfully'));
    })
    .catch(() => {});
}

/** Status modification */
function handleStatusChange(row) {
  let text = row.status === "0" ? td('dm.dataLayer.enableText', 'Enable') : td('dm.dataLayer.disableText', 'Disable');
  proxy.$modal
    .confirm(td('dm.dataLayer.confirmStatusChangeSpecification', 'Are you sure to "{text}" specification "{id}"?', { text, id: row.id }))
    .then(function () {
      return updateDataLayerSpecification({ id: row.id, status: row.status });
    })
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.operationSuccess', 'Operation successful'));
    })
    .catch(function () {
      // Restore switch state
      row.status = row.status === "0" ? "1" : "0";
    });
}

onMounted(() => {
  getTree();
  getManagerOptions();
});
</script>

<style scoped lang="scss">
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.treelabel {
  flex: 1;
  cursor: pointer;

  &:hover {
    color: var(--el-color-primary);
  }
}

.iconimg,
.zjiconimg {
  margin-right: 6px;
  font-size: 16px;
}

.colorxz {
  color: var(--el-color-primary);
}

.colorwxz {
  color: var(--el-text-color-secondary);
}

.operation-trigger {
  padding: 4px;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: var(--el-fill-color-light);
  }

  &.is-active {
    background-color: var(--el-fill-color);
  }
}

.action-icon {
  font-size: 14px;
  color: var(--el-text-color-secondary);
}
</style>
