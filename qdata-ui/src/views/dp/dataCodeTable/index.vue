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

    <GuideTip tip-id="dp/dpDataElem.list" />

    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="td('dp.dataCode.treePlaceholder')"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
            v-show="showSearch" @submit.prevent>
            <el-form-item :label="td('dp.dataCode.nameZh')" prop="name" :label-position="labelPosition">
              <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('dp.dataCode.nameZhPlaceholder')" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('dp.dataCode.nameEn')" prop="engName" :label-position="labelPosition">
              <el-input class="el-form-input-width" v-model="queryParams.engName" :placeholder="td('dp.dataCode.nameEnPlaceholder')" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('dp.common.query') }}
              </el-button>
              <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('dp.common.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="pagecont-bottom">
          <div class="justify-between mb15">
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dp:dataElem:add']"
                  @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="dpDataElemList" @selection-change="handleSelectionChange"
            :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column v-if="getColumnVisibility(0)" :label="td('common.texts.number')" align="left" prop="id" width="50" />
            <el-table-column v-if="getColumnVisibility(1)" :label="td('dp.dataCode.nameZh')" :show-overflow-tooltip="{ effect: 'light' }"
              align="left" prop="name" width="200">
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(2)" :label="td('dp.dataCode.nameEn')" :show-overflow-tooltip="{ effect: 'light' }"
              align="left" prop="engName" width="200">
              <template #default="scope">
                {{ scope.row.engName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" width="240" :label="td('common.texts.description')" align="left" prop="description"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" :label="td('dp.dataCode.catCode')" width="180"
              :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="catCode">
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>

            <el-table-column v-if="getColumnVisibility(10)" :label="td('common.texts.createdBy')" :show-overflow-tooltip="{ effect: 'light' }"
              align="left" prop="createBy" width="140">
              <template #default="scope">
                {{ scope.row.createBy || "-" }}
              </template>
            </el-table-column>
            <!--  sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
            <el-table-column v-if="getColumnVisibility(11)" :label="td('common.texts.createdTime')" align="left" prop="createTime" width="150">
              <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                  }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" width="80" :label="td('common.texts.status')" align="left" prop="status">
              <template #default="scope">
                <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
                  inactive-value="0" @change="
                    (e) => handleStatusChange(scope.row.id, scope.row, e)
                  " />
              </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }"
              v-if="getColumnVisibility(15)">
              <template #default="scope">
                {{ scope.row.remark || "-" }}
              </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="200">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                  v-hasPermi="['dp:dataElem:edit']">{{ td('dp.common.edit') }}
                </el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" :disabled="scope.row.status === '1'"
                  v-hasPermi="['dp:dataElem:remove']">{{ td('dp.common.delete') }}
                </el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['dp:dataElem:edit']">{{ td('dp.common.details') }}
                </el-button>
              </template>
            </el-table-column>

            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
                <p>{{td('common.noData')}}</p>
              </div>
            </template>
          </el-table>

          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-main>
    </el-container>

    <!-- Add or modify data element dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="dpDataElemRef" :model="form" :rules="rules" label-width="100px" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.nameZh')" prop="name">
              <el-input v-model="form.name" :placeholder="td('dp.dataCode.nameZhPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.nameEn')" prop="engName">
              <el-input v-model="form.engName" :placeholder="td('dp.dataCode.nameEnPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.columnType')" prop="columnType" :label-position="labelPosition">
              <el-select v-model="form.columnType" :placeholder="td('dp.dataCode.columnTypePlaceholder')">
                <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.catCode')" prop="catCode" :label-position="labelPosition">
              <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id" :placeholder="td('dp.dataCode.catCodePlaceholder')"
                check-strictly />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <el-input v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.documentType')" prop="description" :label-position="labelPosition">
              <el-select class="el-form-input-width" v-model="form.documentType" :placeholder="td('dp.dataCode.documentTypePlaceholder')" clearable
                @change="fetchSecondLevelDocs" style="width: 100%;">
                <el-option v-for="dict in dp_document_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.documentId')" prop="documentId" :label-position="labelPosition">
              <el-select class="el-form-input-width" v-model="form.documentId" :placeholder="td('dp.dataCode.documentIdPlaceholder')"
                style="width: 100%;">
                <el-option v-for="doc in secondLevelDocs" :key="doc.value" :label="doc.label" :value="doc.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.personCharge')" prop="personCharge" :label-position="labelPosition">
              <!--                <el-input v-model="form.managerId" placeholder="Please select the person in charge" />-->
              <el-select v-model="form.personCharge" @change="handleChange" filterable :placeholder="td('dp.dataCode.personChargePlaceholder')">
                <el-option v-for="item in managerOptions" :key="String(item.userId)" :label="item.nickName"
                  :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataCode.contactNumber')" prop="contactNumber" :label-position="labelPosition">
              <el-input disabled v-model="form.contactNumber" :placeholder="td('dp.dataCode.contactNumberPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_disable" :key="dict.value" :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
              <el-input type="textarea" :placeholder="td('common.form.remarkPlaceholder')" v-model="form.remark" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('dp.common.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{ td('dp.common.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload">
          <upload-filled />
        </el-icon>
        <div class="el-upload__text">{{ td('common.upload.dragOrClick') }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('common.upload.updateExistingData') }}
            </div>
            <span>{{ td('common.upload.fileFormat') }}</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">{{ td('common.upload.downloadTemplate') }}
            </el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('dp.common.cancel') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('dp.common.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="DpDataCodeTable">
import DeptTree from "@/components/DeptTree";
import {
  listDpDataElem,
  getDpDataElem,
  delDpDataElem,
  addDpDataElem,
  updateDpDataElem,
  updateStatusDpDataElem,
} from "@/api/dp/dataElem/dataElem";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataElemCat } from "@/api/att/cat/dataElemCat/dataElemCat";
import { getToken } from "@/utils/auth.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { column_type, sys_disable, dp_document_type } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_document_type"
); import {
  listDpDocument,
} from "@/api/dp/document/document";
const deptOptions = ref(undefined);
const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed // Initial left width

const dpDataElemList = ref([]);
const dpDataElemRuleRelList = ref([]);

// Show hidden information
const columns = ref([
  { key: 0, label: td('common.texts.number'), visible: true },
  { key: 1, label: td('dp.dataCode.nameZh'), visible: true },
  { key: 2, label: td('dp.dataCode.nameEn'), visible: true },
  { key: 7, label: td('common.texts.description'), visible: true },
  { key: 4, label: td('dp.dataCode.catCode'), visible: true },
  { key: 10, label: td('common.texts.createdBy'), visible: true },
  { key: 11, label: td('common.texts.createdTime'), visible: true },
  { key: 5, label: td('common.texts.status'), visible: true },
  { key: 6, label: td('common.texts.description'), visible: true },
]);
let secondLevelDocs = ref([]);
const btnloading = ref(false); // loading state

const fetchSecondLevelDocs = async (type, preserveSelection = false) => {
  if (!type) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = '';
    }
    return;
  }

  try {
    btnloading.value = true;
    const res = await listDpDocument({ type });
    secondLevelDocs.value = (res.data.rows || []).map(d => ({
      label: d.name,
      value: d.id,
    }));

    // Clear only if not retaining selection
    if (!preserveSelection) {
      form.value.documentId = '';
    }
  } catch (error) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = '';
    }
  } finally {
    btnloading.value = false;
  }
}


const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
  return column.visible;
};

const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedDpDataElemRuleRel = ref([]);
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
  url: import.meta.env.VITE_APP_BASE_API + "/dp/dataElem/importData",
});

const data = reactive({
  form: { status: "1" },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    engName: null,
    catCode: null,
    type: "2",
    description: "",
  },
  rules: {
    name: [{ required: true, message: td('dp.dataCode.nameZhRequired'), trigger: "blur" }],
    engName: [
      { required: true, message: td('dp.dataCode.nameEnRequired'), trigger: "blur" },
      {
        pattern: /^[a-zA-Z_]+$/,
        message: td('dp.dataCode.nameEnPattern'),
        trigger: "blur",
      },
    ],
    catCode: [{ required: true, message: td('dp.dataCode.catCodeRequired'), trigger: "blur" }],
    columnType: [
      { required: true, message: td('dp.dataCode.columnTypeRequired'), trigger: "change" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);
const managerOptions = ref([]);
/** Query data element list */
function getList() {
  loading.value = true;
  listDpDataElem(queryParams.value).then((response) => {
    dpDataElemList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}
function handleChange(value) {
  const selectedManager = managerOptions.value.find(
    (item) => item.userId === form.value.personCharge
  );
  form.value.contactNumber = selectedManager.phonenumber; // Store the complete object into form
}
// Cancel button
function cancel() {
  open.value = false;
  reset();
}
function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  handleQuery();
}
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
    requestAnimationFrame(() => { });
  }
}; /** Query department drop-down tree structure */
// form reset
function reset() {
  form.value = {
    id: null,
    code: null,
    name: null,
    engName: null,
    catCode: null,
    type: "2",
    personCharge: null,
    contactNumber: null,
    columnType: null,
    status: "1",
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
  dpDataElemRuleRelList.value = [];
  proxy.resetForm("dpDataElemRef");
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
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Sorting trigger events */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}
function getDeptTree() {
  listAttDataElemCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('dp.dataCode.treeRootName'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
/** Add button operation */
function handleAdd() {
  reset();
  if (queryParams.value.catCode) {
    form.value.catCode = queryParams.value.catCode;
  }
  open.value = true;
  title.value = td('dp.dataCode.addTitle');
}
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpDataElem(_id).then((response) => {
    form.value = response.data;
    dpDataElemRuleRelList.value = response.data.dpDataElemRuleRelList;
    if (response.data.personCharge != null || response.data.personCharge == '0') {
      form.value.personCharge = Number(response.data.personCharge);
    }
    if (form.value.documentId == -1) {
      form.value.documentId = null;
    }
    // Keep selected standard registration values when modifying
    fetchSecondLevelDocs(form.value.documentType, true);

    open.value = true;
    title.value = td('dp.dataCode.editTitle');
  });
}

/** Detail button operation */
function handleDetail(row) {
  if (row.type == 1) {
    routeTo("/dp/dataCodeTable/column/detail", row);
  } else {
    routeTo("/dp/dataCodeTable/dict/detail", row);
  }
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["dpDataElemRef"].validate((valid) => {
    if (valid) {
      form.value.dpDataElemRuleRelList = dpDataElemRuleRelList.value;
      if (form.value.id != null) {
        updateDpDataElem({ ...form.value, documentId: form.value.documentId || -1 })
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            getList();
            submitLoading.value = false;
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      } else {
        addDpDataElem({ ...form.value, documentId: form.value.documentId || -1 })
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            getList();
            submitLoading.value = false;
          })
          .catch((error) => {
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
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.dataCode.confirmDelete', '', { id: _ids }))
    .then(function () {
      return delDpDataElem(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** Data metadata rule association information number */
function rowDpDataElemRuleRelIndex({ row, rowIndex }) {
  row.index = rowIndex + 1;
}

/** New button operation for data metadata rule association information */
function handleAddDpDataElemRuleRel() {
  let obj = {};
  obj.ruleType = "";
  obj.ruleId = "";
  obj.ruleConfig = "";
  obj.remark = "";
  dpDataElemRuleRelList.value.push(obj);
}

/** Data metadata rule association information delete button operation */
function handleDeleteDpDataElemRuleRel() {
  if (checkedDpDataElemRuleRel.value.length == 0) {
    proxy.$modal.msgWarning(td('dp.dataCode.selectToDeleteWarning'));
  } else {
    const dpDataElemRuleRels = dpDataElemRuleRelList.value;
    const checkedDpDataElemRuleRels = checkedDpDataElemRuleRel.value;
    dpDataElemRuleRelList.value = dpDataElemRuleRels.filter(function (item) {
      return checkedDpDataElemRuleRels.indexOf(item.index) == -1;
    });
  }
}

/** Checkbox selected data */
function handleDpDataElemRuleRelSelectionChange(selection) {
  checkedDpDataElemRuleRel.value = selection.map((item) => item.index);
}

/** Export button action */
function handleExport() {
  proxy.download(
    "dp/dpDataElem/export",
    {
      ...queryParams.value,
    },
    `dpDataElem_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
  upload.title = td('dp.dataCode.importTitle');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `dpDataElem_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**File upload is being processed */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** File upload successfully processed */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    td('dp.dataCode.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  getList();
};

/** Enable disable switch */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? td('dp.dataCode.enableText') : td('dp.dataCode.disableText');
  proxy.$modal
    .confirm(td('dp.dataCode.confirmStatusChange','',{text,name: row.name}))
    .then(function () {
      updateStatusDpDataElem(id, row.status).then((response) => {
        proxy.$modal.msgSuccess(td('common.message.operationSuccess'));
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}
/** ---------------------------------**/

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
getList();
</script>
<style scoped lang="scss">
::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #f3f8ff !important;
    border: 0px solid #6ba7ff !important;
    color: #2666fb !important;
  }
}

.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

//Upload attachment style adjustment
::v-deep {

  // .el-upload-list{
  //    display: flex;
  // }
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}
</style>
