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
    <GuideTip tip-id="dp/dpModel.list" />

    <el-container>
      <DeptTree
        ref="DeptTreeRef"
        type="model"
        :deptOptions="deptOptions"
        :placeholder="td('dp.model.treePlaceholder')"
        @node-click="handleNodeClick"
      />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
            />
          </template>

          <template #actions-data>
            <el-dropdown
              @command="handleAdd"
              v-hasPermi="['dp:model:add']"
              class="create-dropdown"
              placement="bottom-start"
              popper-class="model-create-table-dropdown-popper"
              @mousedown="(e) => e.preventDefault()"
              @visible-change="(val) => (dropdownVisible = val)"
            >
              <el-button type="primary" plain>
                <el-icon><Plus /></el-icon>
                <span>{{ td('dp.model.createTable') }}</span>
                <div class="divider"></div>
                <el-icon
                  class="arrow-icon el-icon--right"
                  :class="{ 'is-reverse': dropdownVisible }"
                >
                  <ArrowDown />
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu class="create-table-dropdown-menu">
                  <el-dropdown-item
                    v-for="dict in table_type"
                    :key="dict.value"
                    :command="dict.value"
                  >
                    <svg-icon :iconClass="getIconClass(dict.value)" />
                    <div class="dropdown-info">
                      <div class="dropdown-label">{{ dict.label }}</div>
                      <div class="dropdown-remark">{{ dict.remark }}</div>
                    </div>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <!-- <el-button
              type="primary"
              :disabled="single"
              plain
              @click="handleMaterialization"
              v-hasPermi="['dp:model:edit']"
              @mousedown="(e) => e.preventDefault()"
            >
              <svg-icon
                iconClass="wh"
                style="font-size: 14px; margin-right: 6px"
                :class="{
                  'icon-disabled': single,
                  'icon-normal': !single,
                }"
              Release
            </el-button> -->
            <el-button
              type="danger"
              plain
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['dp:model:remove']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-shanchu-huise mr5"></i>{{ td('common.button.delete') }}
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #status="{ row }">
              <el-switch
                v-model="row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
                @change="(e) => handleStatusChange(row.id, row, e)"
              />
            </template>
            <template #createTime="{ row }">
              <span>{{
                parseTime(row.createTime, "{y}-{m}-{d} {h}:{i}")
              }}</span>
            </template>
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                :disabled="row.status == 1"
                v-hasPermi="['dp:model:edit']"
                >{{ td('common.button.update') }}</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                icon-class="icon-shanchu-huise"
                :disabled="row.status == 1"
                @click="handleDelete(row)"
                v-hasPermi="['dp:model:remove']"
                >{{ td('common.button.delete') }}</el-button
              >
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['dp:model:edit']"
                >{{ td('common.button.details') }}</el-button
              >
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- Logical Model Details Dialogue -->
    <el-dialog
      :title="title"
      v-model="openDetail"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
          <el-icon size="20" style="color: #909399; font-size: 16px">
            <InfoFilled />
          </el-icon>
        </span>
      </template>
      <el-form ref="dpModelRef" :model="form" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.chineseName')" prop="modelComment" :label-position="labelPosition">
              <div>
                {{ form.modelComment }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.englishName')" prop="modelName" :label-position="labelPosition">
              <div>
                {{ form.modelName }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.catCode')" prop="catCode" :label-position="labelPosition">
              <div>
                {{ form.catCode }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
              <dict-tag :options="dp_model_status" :value="form.status" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.createType')" prop="createType" :label-position="labelPosition">
              <dict-tag
                :options="dp_model_create_type"
                :value="form.createType"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.contact')" prop="contact" :label-position="labelPosition">
              <div>
                {{ form.contact }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.contactNumber')" prop="contactNumber" :label-position="labelPosition">
              <div>
                {{ form.contactNumber }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <my-form-dialog
      v-model:visible="open"
      :title="title"
      @submit="handleFormSubmit"
      :deptList="deptList"
      :column_type="column_type"
      :userList="userList"
      @confirm="submitForm"
      :dataList="dataList"
      :catCode="tableStore.params.catCode"
      :deptOptions="deptOptions"
      :type="selectedType"
    />
    <MaterializationDialog
      :title="title"
      :visible="Materialization"
      @update:dialogFormVisible="Materialization = $event"
      :ids="ids"
      @confirm="handleQuery"
    />
  </div>
</template>
<script setup name="DpModel">
import useDefaultLang from "@/composables/useDefaultLang"
import { deptUserTree } from "@/api/system/system/user.js";
import { deptTreeSelectNoPermi } from "@/api/system/system/user.js";
import DeptTree from "@/components/DeptTree/index1.vue";
import MyFormDialog from "@/views/dp/model/components/add.vue";
import MaterializationDialog from "@/views/dp/model/detail/materialization.vue";
import { useProjectStore } from "@/store/project/project";
import {
  listDpModel,
  getDpModel,
  delDpModel,
  delDpModelColumn,
  addDpModel,
  updateDpModelColumn,
  updateDpModel,
  listAttModelCat,
  getTreeData,
  dpModelColumn,
  updateStatusDpDataModel,
  batchDeleteCheck,
} from "@/api/dp/model/model";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import { getToken } from "@/utils/auth.js";
import { formatHierarchyDisplayName } from "../../../utils/dm/utils";
import { ref, reactive, getCurrentInstance } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const projectStore = useProjectStore();
const {
  dp_model_status,
  dp_model_create_type,
  table_type,
} = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type",
  "table_type"
);

function getIconClass(value) {
  const map = {
    1: "btn-model-detail-table",
    2: "btn-model-summary-table",
    3: "btn-model-dimension-table",
    4: "btn-model-progress-table",
  };
  return map[value] || "btn-model-detail-table";
}
const deptList = ref([]);
const userList = ref([]);
const dataLayerList = ref([]);
const deptOptions = ref(undefined);
const tableRef = ref(null);
const DeptTreeRef = ref(null);

let Materialization = ref(false);

/** Query silo tree structure */
function getDataLayerTree() {
  treeDataLayer().then((res) => {
    const tree = res.data || res.rows || [];
    const processTree = (list) => {
      return list.map((item) => {
        const newItem = { ...item };
        newItem.id = Number(item.id);
        const abbreviation = item.engName || item.shortName;
        newItem.displayName = abbreviation
          ? `${item.name} / ${abbreviation}`
          : item.name;
        if (item.children && item.children.length) {
          newItem.children = processTree(item.children);
        }
        return newItem;
      });
    };
    dataLayerList.value = processTree(tree);
  });
}

/** Query department lower tree pull structure */
function getDeptTree() {
  getDataLayerTree();
  projectStore.getModelDeptTree().then((data) => {
    deptOptions.value = data;
  });
  // Sector
  deptTreeSelectNoPermi().then((response) => {
    deptList.value = response.data;
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
    console.log("userList", userList.value);
  });
}

const open = ref(false);
const openDetail = ref(false);
const dropdownVisible = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const title = ref("");
const router = useRouter();
const selectedType = ref("");

/*** User Import Parameters */
const upload = reactive({
  // Whether to show the eject layer (user import)
  open: false,
  // Popup Layer Title (User Import)
  title: "",
  // Disable Upload
  isUploading: false,
  // Update existing user data
  updateSupport: 0,
  // Set the head of the request for upload
  headers: { Authorization: "Bearer " + getToken() },
  // Uploading Address
  url: import.meta.env.VITE_APP_BASE_API + "/dp/model/importData",
});

/** Enable disabled switches */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? td('dp.model.enableText') : td('dp.model.disableText');
  proxy.$modal
    .confirm(td('dp.model.confirmStatusChange').replace('<text>', text).replace('<name>', row.modelComment))
    .then(function () {
      updateStatusDpDataModel(id, row.status).then((response) => {
        proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

const data = reactive({
  form: { status: "1" },
  rules: {
    modelName: [
      { required: true, message: td('dp.model.modelNameRequired'), trigger: "blur" },
    ],
    modelComment: [
      { required: true, message: td('dp.model.modelCommentRequired'), trigger: "blur" },
    ],
    catCode: [{ required: true, message: td('dp.model.catCodeRequired'), trigger: "blur" }],
    status: [{ required: true, message: td('common.form.statusRequired'), trigger: "change" }],
    createType: [
      { required: true, message: td('dp.model.createTypeRequired'), trigger: "change" },
    ],
  },
});

const { form, rules } = toRefs(data);

const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: handleSelectionChange,
      honRowDblclick: handleDetail,
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
    },
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: td('dp.model.modelInfo'),
      width: 450,
      align: "left",
      info: {
        title: (row) =>
          row.modelComment + (row.modelName ? ` (${row.modelName})` : ""),
        desc: "description",
        click: (row) => handleDetail(row),
      },
    },
    {
      label: td('dp.model.hierarchy'),
      align: "left",
      width: 300,
      showOverflowTooltip: { effect: "light" },
      formatter: (row) => formatHierarchyDisplayName(row, row.tableType),
    },
    {
      label: td('dp.model.tableType'),
      align: "center",
      width: 130,
      prop: "tableType",
      dict: "table_type",
      showOverflowTooltip: { effect: "light" },
    },

    {
      label: td('common.texts.status'),
      prop: "status",
      slot: "status",
    },
    {
      label: td('common.texts.createdBy'),
      width: 120,
      align: "left",
      list: [
        { prop: "createBy", class: "person-charge-ellipsis" },
        { prop: "createUserPhoneNumber" },
      ],
    },
    {
      label: td('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      date: true,
    },
    {
      label: td('common.texts.operation'),
      width: 240,
      align: "center",
      fixed: "right",
      slot: "action",
    },
  ],
  func: listDpModel,
  params: {
    modelName: null,
    modelComment: null,
    catCode: null,
    id: null,
    status: null,
    dataLayerId: null,
    tableType: null,
    tableCase: null,
    businessCategoryCode: null,
    dataDomainId: null,
    themeDomainCode: null,
  },
});

const searchStore = reactive({
  items: [
    { label: td('dp.model.chineseName'), prop: "modelComment", component: { is: "input" } },
    { label: td('dp.model.englishName'), prop: "modelName", component: { is: "input" } },
    {
      label: td('dp.model.dataLayer'),
      prop: "dataLayerId",
      type: "select",
      component: {
        is: "tree-select",
        data: dataLayerList,
        props: {
          value: "id",
          label: "displayName",
          children: "children",
        },
        "node-key": "id",
        filterable: true,
        clearable: true,
        "default-expand-all": true,
      },
    },
    {
      label: td('dp.model.tableType'),
      prop: "tableType",
      type: "select",
      component: { is: "select", options: table_type },
    },
    // {
    //   Label: "name case,"
    //   prop: "tableCase",
    //   type: "select",
    //   component: { is: "select", options: dp_model_table_case },
    // },
    // {
    //   label: td('common.texts.status'),
    //   prop: "status",
    //   type: "select",
    //   component: { is: "select", options: dp_model_status },
    // },
  ],
});

function handleNodeClick(data) {
  if (data.type === "0") {
    return;
  }
  tableStore.params.businessCategoryCode = null;
  tableStore.params.dataDomainId = null;
  tableStore.params.themeDomainCode = null;
  tableStore.params.dataLayerId = null;
  tableStore.params.catCode = null;
  tableStore.params.id = null;
  if (data.type === "1") {
    tableStore.params.businessCategoryCode = data.otherData?.code;
  } else if (data.type === "2") {
    tableStore.params.dataDomainId = data.id;
    tableStore.params.businessCategoryCode = data.otherData?.code;
  } else if (data.type === "3") {
    tableStore.params.themeDomainCode = data.otherData?.code;
  } else if (data.type === "5") {
    tableStore.params.dataLayerId = data.id;
  }
  handleQuery();
}

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// Form Reset
function reset() {
  form.value = {
    ID: null,
    modelName: null,
    modelComment: null,
    catCode: null,
    status: null,
    createType: null,
    datasourceId: null,
    contact: null,
    contactNumber: null,
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("dpModelRef");
}

/** Search button operation */
function handleQuery() {
  tableRef.value && tableRef.value.getList();
}

/** Reset button operations */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = null;
  tableStore.params.id = null;
  tableStore.params.businessCategoryCode = null;
  tableStore.params.dataDomainId = null;
  tableStore.params.themeDomainCode = null;
  tableStore.params.dataLayerId = null;
  handleQuery();
}
// Multiple box selected data
function handleSelectionChange(selection) {
  console.log("selection", selection);
  ids.value = selection.map((item) => item.id);
  console.log("selection.length ", selection.length);
  single.value = selection.length == 0 ? true : false;
  multiple.value = !selection.length;
}

/** Add button operation */
function handleAdd(type) {
  selectedType.value = typeof type === "string" ? type : "1";
  dataList.value = {};
  reset();
  open.value = true;
  title.value = td('dp.model.addTitle');
}
let dataList = ref({});
/** Modify button operation */
function handleUpdate(row) {
  console.log("row", row);
  reset();
  const _ID = row.id || ids.value;
  getDpModel(_ID).then((response) => {
    dataList.value = response.data;
    selectedType.value = String(dataList.value.tableType || "");
    open.value = true;
    title.value = td('dp.model.editTitle');
  });
}

/** Release button operation */
function handleMaterialization() {
  const _ID = ids.value;
  // getDpModel(_ID).then(response => {
  //   form.value = response.data;
  //
  // });
  Materialization.value = true;
  title.value = td('dp.model.publishTitle');
}
/** Details button operation */
function handleDetail(row) {
  routeTo("/dm/model/detail", row);
}

/** Submit button */
function submitForm(obj) {
  if (submitLoading.value) return;
  submitLoading.value = true;
  console.log("obj", obj);
  if (obj.form.id != null) {
    updateDpModel(obj.form)
      .then((response) => {
        updateDpModelColumn(obj.tableData).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          handleQuery();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      })
      .catch((error) => {
        submitLoading.value = false;
      });
  } else {
    addDpModel(obj.form)
      .then((response) => {
        const id = response.data;
        const updatedTableData = obj.tableData.map((item) => ({
          ...item,
          modelId: id,
        }));
        dpModelColumn(updatedTableData)
          .then((dpModelColumnResponse) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            handleQuery();
            submitLoading.value = false;
          })
          .catch((dpModelColumnError) => {
            submitLoading.value = false;
          });
      })
      .catch((error) => {
        console.error("Failed to add:", error);
        submitLoading.value = false;
      });
  }
}

/** Remove button operation */
function handleDelete(row) {
  const _IDs = row.id || ids.value;
  batchDeleteCheck(_IDs).then((res) => {
    const { canDeleteCount, cannotDeleteCount, canDeleteIds } = res.data;
    proxy.$modal
      .confirm(
        td('dp.model.deleteCount').replace('<canDelete>', canDeleteCount).replace('<cannotDelete>', cannotDeleteCount),
        td('common.message.systemPrompt'),
        {
          confirmButtonText: td('common.button.confirm'),
          cancelButtonText: td('common.button.cancel'),
          type: "warning",
        }
      )
      .then(() => {
        if (!canDeleteIds.length) {
          proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
          return;
        }
        return delDpModelColumn(canDeleteIds.toString());
      })
      .then((res) => {
        if (!res) return;
        proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
        handleQuery();
      })
      .catch(() => {});
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

getDeptTree();
</script>

<style scoped lang="less">
.create-dropdown {
  vertical-align: middle;

  :deep(.el-button) {
    &:focus,
    &:focus-visible {
      outline: none;
      box-shadow: none !important;
    }
  }

  .arrow-icon {
    transition: transform 0.3s;
    &.is-reverse {
      transform: rotate(180deg);
    }
  }

  .divider {
    width: 1px;
    height: 12px;
    background: currentColor;
    opacity: 0.3;
    margin: 0 4px 0 12px;
  }

  .el-button:hover,
  .el-button:focus,
  .el-button:active {
    .divider {
      opacity: 0.6;
    }
  }
}
</style>

<style lang="less" scoped>
.model-create-table-dropdown-popper {
  min-width: 140px !important;

  .el-dropdown-menu {
    padding: 4px 0 !important;
  }

  .el-dropdown-menu__item {
    padding: 10px 16px !important;
    font-size: 14px !important;
    line-height: 1.5 !important;
    display: flex !important;
    align-items: flex-start !important;
    justify-content: flex-start !important;

    .svg-icon {
      margin-right: 12px !important;
      font-size: 18px !important;
      margin-top: 2px;
    }

    .dropdown-info {
      display: flex;
      flex-direction: column;
      flex: 1;
      overflow: hidden;
    }

    .dropdown-label {
      color: #333;
      font-size: 14px;
    }

    .dropdown-remark {
      color: #999;
      font-size: 12px;
      margin-top: 2px;
      white-space: normal;
      word-break: break-all;
    }
  }
}
</style>
