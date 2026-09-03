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
  <div class="app-container-inner">
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef"     :config="{ fullContent: false, actions: { table: { search: false } } }">
      <!-- 数据操作按钮插槽 -->
      <template #actions-data>
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAdd">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Upload" @click="handleImport">{{ td('common.button.import') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport">{{ td('common.button.export') }}</el-button>
          </el-col>
        </el-row>
      </template>

      <!-- 表格组件 -->
      <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
        <!-- 状态列插槽 -->
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
        <!-- 操作列插槽 -->
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['dp:dataElem:dataelem:edit']"
          >{{ td('dp.common.edit') }}</el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['dp:dataElem:dataelem:remove']"
          >{{ td('dp.common.delete') }}</el-button>
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetail(row)"
            v-hasPermi="['dp:dataElem:dataelem:edit']"
          >{{ td('dp.common.details') }}</el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Add or modify data element dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" draggable>
      <el-form ref="dpDataElemRef" :model="form" :rules="rules" label-width="100px">
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
            <el-form-item :label="td('dp.dataElem.catCode')" prop="catCode">
              <el-tree-select
                filterable
                v-model="form.catCode"
                :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('dp.dataElem.catCodePlaceholder')"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.columnType')" prop="columnType">
              <el-select v-model="form.columnType" :placeholder="td('dp.dataElem.columnTypePlaceholder')">
                <el-option
                  v-for="dict in column_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description">
              <el-input v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.personCharge')" prop="personCharge">
              <el-select
                v-model="form.personCharge"
                @change="handlePersonChange"
                filterable
                :placeholder="td('dp.dataElem.personChargePlaceholder')"
              >
                <el-option
                  v-for="item in managerOptions"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.dataElem.contactNumber')" prop="contactNumber">
              <el-input disabled v-model="form.contactNumber" :placeholder="td('dp.dataElem.contactNumberPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark">
              <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('dp.common.cancel') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ td('dp.common.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" draggable destroy-on-close>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
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
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
            >{{ td('common.upload.downloadTemplate') }}</el-link>
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
const route = useRoute();
const router = useRouter();

const props = defineProps({
  activeName: { type: Number, default: null },
});

const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_data_elem_code_type"
);

const tableRef = ref(null);
const uploadRef = ref(null);
const deptOptions = ref([]);
const managerOptions = ref([]);
const open = ref(false);
const title = ref("");

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  params: {
    documentId: route.query.id,
    type: Number(props.activeName) - 1,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
  columns: [
    { label: td("common.texts.number"), prop: "id", width: 60, align: "left", sortable: true },
    { label: td("dp.dataElem.nameZh"), prop: "name", width: 200, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("dp.dataElem.nameEn"), prop: "engName", width: 200, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.description"), prop: "description", width: 240, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("dp.dataElem.type"), prop: "type", width: 100, align: "left", dict: "dp_data_elem_code_type" },
    { label: td("dp.dataElem.catCode"), prop: "catName", width: 120, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.createdBy"), prop: "createBy", width: 120, align: "left" },
    { label: td("common.texts.createdTime"), prop: "createTime", width: 160, align: "left", sortable: true, date: true },
    { label: td("common.texts.status"), slot: "status", width: 100, align: "center" },
    { label: td("common.texts.remark"), prop: "remark", width: 200, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.operation"), slot: "action", width: 220, align: "center", fixed: "right" },
  ],
  func: listDpDataElem,
});

const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/dp/dataElem/importData",
});

const data = reactive({
  form: { status: "0" },
  rules: {
    name: [{ required: true, message: td('dp.dataElem.nameZhRequired'), trigger: "blur" }],
    engName: [
      { required: true, message: td('dp.dataElem.nameEnRequired'), trigger: "blur" },
      { pattern: /^[a-zA-Z_]+$/, message: td('dp.dataElem.nameEnPattern'), trigger: "blur" },
    ],
    catCode: [{ required: true, message: td('dp.dataElem.catCodeRequired'), trigger: "blur" }],
    columnType: [{ required: true, message: td('dp.dataElem.columnTypeRequired'), trigger: "change" }],
  },
});

const { form, rules } = toRefs(data);

/** 查询分类树 */
function getDeptTree() {
  listAttDataElemCat().then((response) => {
    const treeData = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('dp.dataElem.treeRootName'),
        value: "",
        id: 0,
        children: treeData,
      },
    ];
  });
}

/** 查询负责人列表 */
function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

/** 负责人变更 */
function handlePersonChange() {
  const selectedManager = managerOptions.value.find((item) => item.userId === form.value.personCharge);
  if (selectedManager) {
    form.value.contactNumber = selectedManager.phonenumber;
  }
}

/** 重置表单 */
function reset() {
  form.value = {
    id: null,
    code: null,
    name: null,
    engName: null,
    catCode: null,
    type: String(tableStore.params.type),
    personCharge: null,
    contactNumber: null,
    columnType: null,
    status: "0",
    description: null,
    remark: null,
    documentId: tableStore.params.documentId,
  };
  proxy.resetForm("dpDataElemRef");
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('dp.dataElem.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getDpDataElem(row.id).then((response) => {
    form.value = response.data;
    if (form.value.personCharge) {
      form.value.personCharge = Number(form.value.personCharge);
    }
    open.value = true;
    title.value = td('dp.dataElem.editTitle');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dpDataElemRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDpDataElem(form.value).then(() => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          tableRef.value.refresh();
        });
      } else {
        addDpDataElem(form.value).then(() => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          tableRef.value.refresh();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || tableRef.value.selection.map(item => item.id);
  proxy.$modal
    .confirm(td('dp.dataElem.confirmDelete', '', { id: _ids }))
    .then(() => delDpDataElem(_ids))
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** 详情按钮操作 */
function handleDetail(row) {
  const path = row.type == 1 ? "/dp/dataElem/dataElemDetail" : "/dp/dataElem/dataElemCodeDetail";
  router.push({ path, query: { id: row.id } });
}

/** 状态修改 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? td('dp.dataElem.enableText') : td('dp.dataElem.disableText');
  proxy.$modal
    .confirm(td('dp.dataElem.confirmStatusChange', '', { text, name: row.name }))
    .then(() => updateStatusDpDataElem(id, row.status))
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.operationSuccess'));
    })
    .catch(() => {
      row.status = row.status === "1" ? "0" : "1";
    });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "dp/dpDataElem/export",
    { ...tableStore.params },
    `dpDataElem_${new Date().getTime()}.xlsx`
  );
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = td('dp.dataElem.importTitle');
  upload.open = true;
}

/** 下载模板 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {}, `dpDataElem_template_${new Date().getTime()}.xlsx`);
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/** 文件上传中 */
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

/** 文件上传成功 */
const handleFileSuccess = (response, file) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    td('dp.dataElem.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.refresh();
};

watch(() => props.activeName, (val) => {
  tableStore.params.type = Number(val) - 1;
  tableRef.value?.refresh();
});

getDeptTree();
getManagerOptions();
</script>

<style scoped lang="scss">
.app-container-inner {
  padding: 10px 0;
}

::v-deep {
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}
</style>
