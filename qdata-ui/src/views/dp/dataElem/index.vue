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
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="td('dp.dataElem.treePlaceholder')"
        @node-click="handleNodeClick" />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
              :tableRef="tableRef"
            />
          </template>

          <template #actions-data>
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  @click="handleAdd"
                  v-hasPermi="['dp:dataElem:add']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
                </el-button>
              </el-col>
            </el-row>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
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

            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                v-hasPermi="['dp:dataElem:edit']"
              >{{ td('dp.common.edit') }}</el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                :disabled="row.status === '1'"
                v-hasPermi="['dp:dataElem:remove']"
              >{{ td('dp.common.delete') }}</el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['dp:dataElem:edit']"
              >{{ td('dp.common.details') }}</el-button>
            </template>
          </qt-table>
        </qt-wrap>
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
            <el-form-item :label="td('dp.dataElem.nameZh')" prop="name">
              <el-input v-model="form.name" :placeholder="td('dp.dataElem.nameZhPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.nameEn')" prop="engName">
              <el-input v-model="form.engName" :placeholder="td('dp.dataElem.nameEnPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.columnType')" prop="columnType" :label-position="labelPosition">
              <el-select v-model="form.columnType" :placeholder="td('dp.dataElem.columnTypePlaceholder')">
                <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.catCode')" prop="catCode" :label-position="labelPosition">
              <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id" :placeholder="td('dp.dataElem.catCodePlaceholder')"
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
            <el-form-item :label="td('dp.dataElem.documentType')" prop="description" :label-position="labelPosition">
              <el-select class="el-form-input-width" v-model="form.documentType" :placeholder="td('dp.dataElem.documentTypePlaceholder')" clearable
                @change="fetchSecondLevelDocs" style="width: 100%;">
                <el-option v-for="dict in dp_document_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.documentId')" prop="documentId" :label-position="labelPosition">
              <el-select class="el-form-input-width" v-model="form.documentId" :placeholder="td('dp.dataElem.documentIdPlaceholder')"
                style="width: 100%;">
                <el-option v-for="doc in secondLevelDocs" :key="doc.value" :label="doc.label" :value="doc.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.personCharge')" prop="personCharge" :label-position="labelPosition">
              <!--                <el-input v-model="form.managerId" placeholder="Please select the person in charge" />-->
              <el-select v-model="form.personCharge" @change="handleChange" filterable :placeholder="td('dp.dataElem.personChargePlaceholder')">
                <el-option v-for="item in managerOptions" :key="String(item.userId)" :label="item.nickName"
                  :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.contactNumber')" prop="contactNumber" :label-position="labelPosition">
              <el-input disabled v-model="form.contactNumber" :placeholder="td('dp.dataElem.contactNumberPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
<!--          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.type')" prop="type">
              <el-radio-group v-model="form.type" :disabled="form.id">
                <el-radio v-for="dict in dp_data_elem_code_type" :key="dict.value" :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>-->
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

<script setup name="DpDataElem">
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
const { column_type, sys_disable, dp_data_elem_code_type, dp_document_type } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_data_elem_code_type",
  "dp_document_type"
);
import { listDpDocument } from "@/api/dp/document/document";
const deptOptions = ref(undefined);
const tableRef = ref(null);
const DeptTreeRef = ref(null);

const tableStore = reactive({
  config: {
    initResquest: true,
  },
  columns: [
    { label: td("common.texts.number"), prop: "id", width: 60, align: "left", sortable: true },
    { label: td("dp.dataElem.nameZh"), prop: "name", width: 260, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("dp.dataElem.nameEn"), prop: "engName", width: 260, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.description"), prop: "description", width: 256, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("dp.dataElem.catCode"), prop: "catName", width: 160, align: "left" },
    { label: td("common.texts.createdBy"), prop: "createBy", width: 140, align: "left" },
    {
      label: td("common.texts.createdTime"),
      prop: "createTime",
      width: 150,
      align: "left",
      sortableKey: "create_time",
      sortable: true,
      date: true
    },
    { label: td("common.texts.status"), prop: "status", width: 80, align: "left", slot: "status" },
    { label: td("common.texts.remark"), prop: "remark", width: 200, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.operation"), slot: "action", width: 220, align: "center", fixed: "right" },
  ],
  func: listDpDataElem,
  params: {
    name: null,
    engName: null,
    catCode: null,
    type: "1",
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const searchStore = reactive({
  items: [
    {
      label: td("dp.dataElem.nameZh"),
      prop: "name",
      component: { is: "input", placeholder: td("dp.dataElem.nameZhPlaceholder") },
    },
    {
      label: td("dp.dataElem.nameEn"),
      prop: "engName",
      component: { is: "input", placeholder: td("dp.dataElem.nameEnPlaceholder") },
    },
  ],
});

let secondLevelDocs = ref([]);
const btnloading = ref(false);

const fetchSecondLevelDocs = async (type, preserveSelection = false) => {
  if (!type) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = "";
    }
    return;
  }

  try {
    btnloading.value = true;
    const res = await listDpDocument({ type });
    secondLevelDocs.value = (res.data.rows || []).map((d) => ({
      label: d.name,
      value: d.id,
    }));

    if (!preserveSelection) {
      form.value.documentId = "";
    }
  } catch (error) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = "";
    }
  } finally {
    btnloading.value = false;
  }
};

const open = ref(false);
const title = ref("");
const router = useRouter();

const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/dp/dataElem/importData",
});

const data = reactive({
  form: { status: "1" },
  rules: {
    name: [{ required: true, message: td("dp.dataElem.nameZhRequired"), trigger: "blur" }],
    engName: [
      { required: true, message: td("dp.dataElem.nameEnRequired"), trigger: "blur" },
      {
        pattern: /^[a-zA-Z_]+$/,
        message: td("dp.dataElem.nameEnPattern"),
        trigger: "blur",
      },
    ],
    catCode: [{ required: true, message: td("dp.dataElem.catCodeRequired"), trigger: "blur" }],
    columnType: [{ required: true, message: td("dp.dataElem.columnTypeRequired"), trigger: "change" }],
  },
});

const { form, rules } = toRefs(data);
const managerOptions = ref([]);

function handleChange(value) {
  const selectedManager = managerOptions.value.find((item) => item.userId === form.value.personCharge);
  form.value.contactNumber = selectedManager.phonenumber;
}

function cancel() {
  open.value = false;
  reset();
}

function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}

function reset() {
  form.value = {
    id: null,
    code: null,
    name: null,
    engName: null,
    catCode: null,
    type: "1",
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
  proxy.resetForm("dpDataElemRef");
}

function handleQuery() {
  tableStore.params.pageNum = 1;
}

function resetQuery() {
  DeptTreeRef.value?.resetTree?.();
  tableStore.params.catCode = "";
  handleQuery();
}

function getDeptTree() {
  listAttDataElemCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td("dp.dataElem.treeRootName"),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}

function handleAdd() {
  reset();
  if (tableStore.params.catCode) {
    form.value.catCode = tableStore.params.catCode;
  }
  open.value = true;
  title.value = td("dp.dataElem.addTitle");
}

function handleUpdate(row) {
  reset();
  getDpDataElem(row.id).then((response) => {
    form.value = response.data;
    if (response.data.personCharge != null || response.data.personCharge == "0") {
      form.value.personCharge = Number(response.data.personCharge);
    }
    if (form.value.documentId == -1) {
      form.value.documentId = null;
    }
    fetchSecondLevelDocs(form.value.documentType, true);

    open.value = true;
    title.value = td("dp.dataElem.editTitle");
  });
}

function handleDetail(row) {
  if (row.type == 1) {
    routeTo("/dp/dataElem/column/detail", row);
  } else {
    routeTo("/dp/dataElem/dict/detail", row);
  }
}

function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["dpDataElemRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDpDataElem({ ...form.value, documentId: form.value.documentId || -1 })
          .then((response) => {
            proxy.$modal.msgSuccess(td("common.message.editSuccess"));
            open.value = false;
            tableRef.value.refresh();
            submitLoading.value = false;
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      } else {
        addDpDataElem({ ...form.value, documentId: form.value.documentId || -1 })
          .then((response) => {
            proxy.$modal.msgSuccess(td("common.message.addSuccess"));
            open.value = false;
            tableRef.value.refresh();
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

function handleDelete(row) {
  proxy.$modal
    .confirm(td("dp.dataElem.confirmDelete", "", { id: row.id }))
    .then(function () {
      return delDpDataElem(row.id);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess"));
    })
    .catch(() => {});
}

function handleExport() {
  proxy.download(
    "dp/dpDataElem/export",
    {
      ...tableStore.params,
    },
    `dpDataElem_${new Date().getTime()}.xlsx`
  );
}

function handleImport() {
  upload.title = td("dp.dataElem.importTitle");
  upload.open = true;
}

function importTemplate() {
  proxy.download("system/user/importTemplate", {}, `dpDataElem_template_${new Date().getTime()}.xlsx`);
}

function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td("dp.dataElem.importResult"),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.refresh();
};

function handleStatusChange(id, row, e) {
  const text = e === "1" ? td("dp.dataElem.enableText") : td("dp.dataElem.disableText");
  proxy.$modal
    .confirm(td("dp.dataElem.confirmStatusChange", "", { text, name: row.name }))
    .then(function () {
      updateStatusDpDataElem(id, row.status).then((response) => {
        proxy.$modal.msgSuccess(td("common.message.operationSuccess"));
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
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
deptUserTree().then((response) => {
  managerOptions.value = response.data;
});
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
