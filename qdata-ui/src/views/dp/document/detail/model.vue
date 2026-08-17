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
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          @click="handleAdd"
          v-hasPermi="['dp:model:add']"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar
        v-model:showSearch="showSearch"
        @queryTable="getList"
        :columns="columns"
      ></right-toolbar>
    </div>
  </div>
  <el-table
    stripe
    height="400"
    v-loading="loading"
    :data="dpModelList"
    @selection-change="handleSelectionChange"
    :default-sort="defaultSort"
    @sort-change="handleSortChange"
  >
    <el-table-column
      v-if="getColumnVisibility(0)"
      :label="td('common.texts.number')"
      width="50"
      align="left"
      prop="id"
    />
    <el-table-column
      v-if="getColumnVisibility(1)"
      :label="td('dp.model.englishName')"
      :show-overflow-tooltip="{ effect: 'light' }"
      align="left"
      prop="modelName"
      width="200"
    >
      <template #default="scope">
        {{ scope.row.modelName || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(2)"
      :label="td('dp.model.chineseName')"
      :show-overflow-tooltip="{ effect: 'light' }"
      align="left"
      prop="modelComment"
      width="180"
    >
      <template #default="scope">
        {{ scope.row.modelComment || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(3)"
      :label="td('dp.model.treeRootName')"
      width="100"
      :show-overflow-tooltip="{ effect: 'light' }"
      align="left"
      prop="catName"
    >
      <template #default="scope">
        {{ scope.row.catName || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(10)"
      :label="td('common.texts.createdBy')"
      align="left"
      prop="createBy"
      width="120"
    >
      <template #default="scope">
        {{ scope.row.createBy || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      :label="td('common.texts.createdTime')"
      v-if="getColumnVisibility(11)"
      align="left"
      prop="createTime"
      width="180"
    >
      <template #default="scope">
        <span>{{
          parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
        }}</span>
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(4)"
      :label="td('common.texts.status')"
      width="120"
      align="left"
      prop="status"
    >
      <template #default="scope">
        <el-switch
          v-model="scope.row.status"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="1"
          inactive-value="0"
          @change="(e) => handleStatusChange(scope.row.id, scope.row, e)"
        />
      </template>
    </el-table-column>
    <el-table-column
      :label="td('common.texts.remark')"
      v-if="getColumnVisibility(5)"
      align="left"
      prop="remark"
      :show-overflow-tooltip="{ effect: 'light' }"
    >
      <template #default="scope">
        {{ scope.row.remark || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      :label="td('common.texts.operation')"
      align="center"
      class-name="small-padding fixed-width"
      fixed="right"
      width="240"
    >
      <template #default="scope">
        <el-button
          link
          type="primary"
          icon="Edit"
          @click="handleUpdate(scope.row)"
          :disabled="scope.row.status == 1"
          v-hasPermi="['dp:model:edit']"
          >{{ td('dp.common.edit') }}</el-button
        >
        <el-button
          link
          type="danger"
          icon="Delete"
          :disabled="scope.row.status == 1"
          @click="handleDelete(scope.row)"
          v-hasPermi="['dp:model:remove']"
          >{{ td('dp.common.delete') }}</el-button
        >
        <el-button
          link
          type="primary"
          icon="view"
          @click="handleDetail(scope.row)"
          v-hasPermi="['dp:model:edit']"
          >{{ td('dp.common.details') }}</el-button
        >
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
        <p>{{td('common.noData')}}</p>
      </div>
    </template>
  </el-table>

  <pagination
    v-show="total > 0"
    :total="total"
    v-model:page="queryParams.pageNum"
    v-model:limit="queryParams.pageSize"
    @pagination="getList"
  />

  <!-- Logical Model Details Dialog Box -->
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
          <el-form-item :label="td('dp.model.englishName')" prop="modelName" :label-position="labelPosition">
            <div>
              {{ form.modelName }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.model.chineseName')" prop="modelComment" :label-position="labelPosition">
            <div>
              {{ form.modelComment }}
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
    :catCode="queryParams.catCode"
    :deptOptions="deptOptions"
    :documentId="form.documentId"
    ,
    type="3"
  />
  <MaterializationDialog
    :title="title"
    :visible="Materialization"
    @update:dialogFormVisible="Materialization = $event"
    :ids="ids"
    @confirm="getList"
    :documentId="form.documentId"
  />
</template>
<script setup name="DpModel">
import { deptUserTree } from "@/api/system/system/user.js";
import { deptTreeSelectNoPermi } from "@/api/system/system/user.js";
import DeptTree from "@/components/DeptTree";
import MyFormDialog from "@/views/dp/model/components/add.vue";
import MaterializationDialog from "@/views/dp/model/detail/materialization.vue";
import {
  listDpModel,
  getDpModel,
  delDpModel,
  delDpModelColumn,
  addDpModel,
  updateDpModelColumn,
  updateDpModel,
  listAttModelCat,
  dpModelColumn,
  updateStatusDpDataModel,
} from "@/api/dp/model/model";
import { getToken } from "@/utils/auth.js";
import { ref, reactive, getCurrentInstance } from "vue";
import { useRoute } from "vue-router";
import useDefaultLang from "@/composables/useDefaultLang";

const route = useRoute();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dp_model_status, dp_model_create_type } = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type"
);
const dpModelList = ref([]);
const deptList = ref([]);
const userList = ref([]);
const deptOptions = ref(undefined);

const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed // Initial left width
let Materialization = ref(false);
const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // Calculate mouse movement distance
    leftWidth.value += delta; // Modify left width
    startX = event.clientX; // Update starting position
    // Use requestAnimationFrame to reduce page redraw frequency
    requestAnimationFrame(() => {});
  }
};
const selectable = (row) => {
  return row.status != 0;
};

/** Query department drop-down tree structure */
function getDeptTree() {
  listAttModelCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('dp.model.treeRootName'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
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
// Show hidden information
const columns = ref([
  { key: 0, label: td('common.texts.number'), visible: true },
  { key: 1, label: td('dp.model.englishName'), visible: true },
  { key: 2, label: td('dp.model.chineseName'), visible: true },
  { key: 3, label: td('dp.model.treeRootName'), visible: true },
  { key: 10, label: td('common.texts.createdBy'), visible: true },
  { key: 11, label: td('common.texts.createdTime'), visible: true },
  { key: 4, label: td('common.texts.status'), visible: true },
  { key: 5, label: td('common.texts.remark'), visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
  return column.visible;
};

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

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
    .confirm(td('dp.model.confirmStatusChange', '', { text, name: row.modelComment }))
    .then(function () {
      updateStatusDpDataModel(id, row.status).then((response) => {
        proxy.$modal.msgSuccess(td('common.message.operationSuccess'));
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

const data = reactive({
  form: { status: "1" },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    modelName: null,
    modelComment: null,
    catCode: null,
    documentId: null,
  },
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

const { queryParams, form, rules } = toRefs(data);
function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  queryParams.value.pageNum = 1;
  handleQuery();
}
/** Query logical model list */
function getList() {
  if (!queryParams.value.documentId) {
    queryParams.value.documentId = route.query.id;
  }
  loading.value = true;
  listDpModel(queryParams.value).then((response) => {
    dpModelList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
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
    documentId: null,
  };
  proxy.resetForm("dpModelRef");
}

/** Search button action */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
const DeptTreeRef = ref(null);
/** reset button action */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.catCode = "";
  queryParams.value.pageNum = 1;
  reset();
  proxy.resetForm("queryRef");
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

/** Sorting trigger events */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** Add button operation */
function handleAdd() {
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
    open.value = true;
    title.value = td('dp.model.editTitle');
  });
}

/** Materialized button actions */
function handleMaterialization() {
  const _ID = ids.value;
  // getDpModel(_ID).then(response => {
  //   form.value = response.data;

  // });
  Materialization.value = true;
  title.value = td('dp.model.logicMaterialization');
}
/** Detail button operation */
function handleDetail(row) {
  routeTo("/dm/model/detail", row);
}

/** submit button */
function submitForm(obj) {
  console.log("obj", obj);
  if (obj.form.id != null) {
    updateDpModel({ ...obj.form, documentId: queryParams.value.documentId })
      .then((response) => {
        updateDpModelColumn(obj.tableData).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
        });
      })
      .catch((error) => {});
  } else {
    addDpModel({ ...obj.form, documentId: queryParams.value.documentId })
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
            getList();
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
    .confirm(td('dp.model.confirmDelete', '', { id: _IDs }))
    .then(function () {
      return delDpModelColumn(_IDs);
    })
    .then(() => {
      getList();
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
queryParams.value.documentId = route.query.id;
getList();
getDeptTree();
</script>
<style scoped lang="scss">
::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #f3f8ff !important;
    border: 0px solid #6ba7ff !important;
    color: #2666fb !important;
  }
}
</style>
