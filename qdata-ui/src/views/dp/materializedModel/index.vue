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
    <el-container>
      <DeptTree
        ref="DeptTreeRef"
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
            <!-- <el-dropdown
              @command="handleAdd"
              v-hasPermi="['dp:model:add']"
              class="create-dropdown"
              popper-class="create-table-dropdown-popper"
              @mousedown="(e) => e.preventDefault()"
              @visible-change="(val) => (dropdownVisible = val)"
            >
              <el-button type="primary" plain>
                <el-icon><Plus /></el-icon>
                <span>Create table</span>
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
                  <el-dropdown-item command="1">
                    <svg-icon iconClass="btn-model-detail-table" />
                    <span>Details list</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="2">
                    <svg-icon iconClass="btn-model-summary-table" />
                    <span>Summary table</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="3">
                    <svg-icon iconClass="btn-model-dimension-table" />
                    <span>Dimension table</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="4">
                    <svg-icon iconClass="btn-model-progress-table" />
                    <span>Application table</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown> -->
            <el-button
              type="primary"
              :disabled="single"
              plain
              @click="handleMaterialization"
              v-hasPermi="['dp:model:edit']"
              @mousedown="(e) => e.preventDefault()"
            >
              <svg-icon
                iconClass="send-plane-line"
                style="font-size: 14px; margin-right: 6px"
                :class="{
                  'icon-disabled': single,
                  'icon-normal': !single,
                }"
              />{{ td('dp.materializedModel.publishModel') }}
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #releaseStatus="{ row }">
              <dict-tag v-if="row.releaseStatus == 1" type="info">{{ td('dp.materializedModel.unpublished') }}</dict-tag>
              <dict-tag v-else-if="row.releaseStatus == 3" type="success">{{ td('dp.materializedModel.publishSuccess') }}</dict-tag>
              <dict-tag v-else-if="row.releaseStatus == 4" type="danger">{{ td('dp.materializedModel.publishFailed') }}</dict-tag>
              <span v-else>{{ row.releaseStatus }}</span>
            </template>
            <template #releaseDatabaseList="{ row }">
              <QtTagGroup
                type="double"
                is-datasource
                :items="parseReleaseDatabaseList(row.releaseDatabaseList)"
              />
            </template>
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
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['dp:model:edit']"
                >{{ td('common.button.details') }}</el-button
              >
              <el-button
                link
                type="primary"
                @click="handleRelease(row)"
                v-hasPermi="['dp:model:edit']"
              >
                <svg-icon
                  :iconClass="
                    row.releaseStatus == 1 ? 'send-plane-line' : 'zhongxinfabu'
                  "
                  style="margin-right: 4px"
                />
                {{ row.releaseStatus == 1 ? td('dp.materializedModel.release') : td('dp.materializedModel.reRelease') }}
              </el-button>
              <!-- <el-button
                link
                type="danger"
                icon="Delete"
                icon-class="icon-shanchu-huise"
                :disabled="row.status == 1"
                @click="handleDelete(row)"
                v-hasPermi="['dp:model:remove']"
                >{{ td('common.button.delete') }}</el-button
              > -->
              <!-- <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['dp:model:edit']"
                >{{ td('common.button.details') }}</el-button
              > -->
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
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
import {
  listDpModel,
  releaseList,
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
} from "@/api/dp/model/model";
import { useProjectStore } from "@/store/project/project";
import { getDatasourceIcon } from "@/utils/datasource";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import { getToken } from "@/utils/auth.js";
import { formatHierarchyDisplayName } from "../../../utils/dm/utils";
import { ref, reactive, getCurrentInstance } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const projectStore = useProjectStore();
const {
  dp_model_status,
  dp_model_create_type,
  dp_model_table_type,
  dp_model_table_case,
  table_type,
} = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type",
  "dp_model_table_type",
  "dp_model_table_case",
  "table_type"
);
const deptList = ref([]);
const userList = ref([]);
const dataLayerList = ref([]);
const deptOptions = ref(undefined);
const tableRef = ref(null);
const DeptTreeRef = ref(null);

let Materialization = ref(false);

/** Query the hierarchical tree structure of the data warehouse */
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

/** Query department drop-down tree structure */
function getDeptTree() {
  getDataLayerTree();
  projectStore.getModelDeptTree().then((data) => {
    deptOptions.value = data;
  });
  // Department
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

/*** User import parameters */
const upload = reactive({
  // Whether to display the pop-up layer (user import)
  open: false,
  // Popup layer title (user imported)
  title: "",
  // Whether to disable uploading
  isUploading: false,
  // Whether to update existing user data
  updateSupport: 0,
  // Set upload request headers
  headers: { Authorization: "Bearer " + getToken() },
  // Upload address
  url: import.meta.env.VITE_APP_BASE_API + "/dp/model/importData",
});

/** Enable disable switch */
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
      width: 320,
      showOverflowTooltip: { effect: "light" },
      formatter: (row) => formatHierarchyDisplayName(row, row.tableType),
    },
    {
      label: td('dp.model.tableType'),
      align: "left",
      width: 150,
      prop: "tableType",
      dict: "table_type",
      showOverflowTooltip: { effect: "light" },
    },

    {
      label: td('dp.materializedModel.releaseStatus'),
      prop: "releaseStatus",
      align: "center",
      width: 180,
      slot: "releaseStatus",
    },
    {
      label: td('dp.materializedModel.releaseDatabase'),
      prop: "releaseDatabaseList",
      align: "center",
      width: 300,
      slot: "releaseDatabaseList",
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
      width: 200,
      headerAlign: "center",
      align: "left",
      fixed: "right",
      slot: "action",
    },
  ],
  func: releaseList,
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
    //   label: "Name case",
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

// form reset
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

/** Search button action */
function handleQuery() {
  tableRef.value && tableRef.value.getList();
}

/** reset button action */
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

// Multiple selection box selected data
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
/** Modify button actions */
function handleUpdate(row) {
  console.log("row", row);
  reset();
  const _ID = row.id || ids.value;
  getDpModel(_ID).then((response) => {
    dataList.value = response.data;
    selectedType.value = String(dataList.value.type || "");
    open.value = true;
    title.value = td('dp.model.editTitle');
  });
}

/** Publish model button action */
function handleMaterialization() {
  const _ID = ids.value;
  Materialization.value = true;
  title.value = td('dp.materializedModel.publishModelTitle');
}

/** Publish/republish button actions */
function handleRelease(row) {
  ids.value = [row.id];
  Materialization.value = true;
  title.value = row.releaseStatus == 1 ? td('dp.materializedModel.publishModelTitle') : td('dp.materializedModel.reReleaseModelTitle');
}

/** Detail button operation */
function handleDetail(row) {
  routeTo("/dm/model/materializedModel/detail", row);
}

/** submit button */
function submitForm(obj) {
  console.log("obj", obj);
  if (obj.form.id != null) {
    updateDpModel(obj.form)
      .then((response) => {
        updateDpModelColumn(obj.tableData).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          handleQuery();
        });
      })
      .catch((error) => {});
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
          })
          .catch((dpModelColumnError) => {});
      })
      .catch((error) => {
        console.error("Failed to add:", error);
      });
  }
}

/** Delete button action */
function handleDelete(row) {
  const _IDs = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.model.confirmDelete').replace('<id>', _IDs))
    .then(function () {
      return delDpModelColumn(_IDs);
    })
    .then(() => {
      handleQuery();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
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

/** Parse a list of published data sources */
function parseReleaseDatabaseList(json) {
  try {
    const list = JSON.parse(json);
    return Array.isArray(list) ? list : [];
  } catch (e) {
    return [];
  }
}

getDeptTree();
</script>

<style scoped lang="less">
.create-dropdown {
  margin-right: 12px;
  vertical-align: middle;

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

.datasource-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  width: 100%;
  gap: 4px;
  overflow: hidden;
  max-height: 56px;
}

.datasource-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-color: #e8f0ff;
  background-color: #f0f7ff;
  color: #409eff;
  padding: 0 8px;
  height: 24px;
  line-height: 1;

  :deep(.el-tag__content) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }

  .datasource-icon {
    width: 14px;
    height: 14px;
    margin-right: 4px;
    display: block;
    object-fit: contain;
  }
}
</style>

<style lang="less">
.create-table-dropdown-popper {
  min-width: 120px !important;

  .el-dropdown-menu {
    padding: 4px 0 !important;
  }

  .el-dropdown-menu__item {
    padding: 6px 12px !important;
    font-size: 14px !important;
    line-height: 22px !important;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;

    .svg-icon {
      margin-right: 8px !important;
      font-size: 16px !important;
    }

    span {
      white-space: nowrap;
    }
  }
}
</style>
