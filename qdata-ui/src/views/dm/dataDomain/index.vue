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
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['dm:dataDomain:add']"
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
            v-hasPermi="['dm:dataDomain:edit']"
          >
            {{ td('common.button.update', 'Edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['dm:dataDomain:remove']"
          >
            {{ td('common.button.delete', 'Delete') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(row)"
            v-hasPermi="['dm:dataDomain:edit']"
          >
            {{ td('common.button.details', 'Details') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Add or modify data field management dialog box -->
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
        ref="dataDomainRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
       :label-position="labelPosition">
        <el-form-item :label="td('dm.dataDomain.name', 'Data Domain Name')" prop="name" :label-position="labelPosition">
          <el-input v-model="form.name" :placeholder="td('dm.dataDomain.namePlaceholder', 'Please enter data domain name')" />
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.engName', 'English Abbreviation')" prop="engName" :label-position="labelPosition">
          <el-input v-model="form.engName" :placeholder="td('dm.dataDomain.engNamePlaceholder', 'Please enter English abbreviation')" />
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerId', 'Responsible Person')" prop="ownerUserId" :label-position="labelPosition">
          <el-select
            v-model="form.ownerUserId"
            filterable
            :placeholder="td('dm.dataDomain.ownerIdPlaceholder', 'Please select responsible person')"
            @change="handleContactChange"
          >
            <el-option
              v-for="item in managerOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerPhone', 'Responsible Person Phone')" prop="ownerUserPhoneNumber" :label-position="labelPosition">
          <el-input
            v-model="form.ownerUserPhoneNumber"
            :placeholder="td('dm.dataDomain.ownerPhonePlaceholder', 'Please enter responsible person phone')"
            disabled
          />
        </el-form-item>
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
        <el-form-item :label="td('common.texts.remark', 'Remark')" prop="remark" :label-position="labelPosition">
          <el-input
            v-model="form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder', 'Please enter remark')"
            :min-height="192"
            show-word-limit
            maxlength="500"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel', 'Cancel') }}</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm', 'Confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Data Domain Management Details Dialog Box -->
    <el-dialog
      :title="title"
      v-model="openDetail"
      :append-to="$refs['app-container']"
      draggable
      width="800px"
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="dataDomainDetailRef" :model="form" label-width="110px" :label-position="labelPosition">
        <el-form-item :label="td('common.texts.number', 'No.') + ':'" prop="id" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.dataDomain', 'Data Domain')" prop="name" :label-position="labelPosition">
          <div class="form-readonly">{{ form.name ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.engName', 'English Abbreviation')" prop="engName" :label-position="labelPosition">
          <div class="form-readonly">{{ form.engName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerId', 'Responsible Person')" prop="ownerUserId" :label-position="labelPosition">
          <div class="form-readonly">{{ form.ownerUserName || "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerPhone', 'Responsible Person Phone')" prop="ownerUserPhoneNumber" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.ownerUserPhoneNumber || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.description', 'Description')" prop="description" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark', 'Remark')" prop="remark" :label-position="labelPosition">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
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
          <el-button @click="cancel">{{ td('common.button.close', 'Close') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog
      :title="upload.title"
      v-model="upload.open"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
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
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">{{ td('common.upload.dragOrClick', 'Drag file here, or click to upload') }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox
                v-model="upload.updateSupport"
              />{{ td('dm.dataDomain.updateExistingData', 'Update existing data domain data') }}
            </div>
            <span>{{ td('common.upload.fileFormat', 'Only xls, xlsx format files are allowed.') }}</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
            >
              {{ td('common.upload.downloadTemplate', 'Download Template') }}
            </el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel', 'Cancel') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm', 'Confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataDomain">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  getDataDomain,
  addDataDomain,
  updateDataDomain,
  listDataDomain,
  delDataDomain,
} from "@/api/dm/dataDomain/dataDomain.js";
import { deptUserTree, getUser } from "@/api/system/system/user.js";
import { getToken } from "@/utils/auth.js";
import {
  computed,
  getCurrentInstance,
  onMounted,
  reactive,
  ref,
  toRefs,
} from "vue";


const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);

const tableRef = ref(null);

const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const managerOptions = ref([]);

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
      label: td('dm.dataDomain.name', 'Data Domain Name'),
      prop: "name",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
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
    { label: td('dm.dataDomain.engName', 'English Abbreviation'), prop: "engName", align: "left" },
    { label: td('dm.dataDomain.ownerId', 'Responsible Person'), prop: "ownerUserName", align: "left" },
    {
      label: td('dm.dataDomain.ownerPhone', 'Responsible Person Phone'),
      prop: "ownerUserPhoneNumber",
      align: "left",
      width: 140,
    },
    {
      label: td('common.texts.createdBy', 'Created By'),
      prop: "createBy",
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
    // { label: t('common.texts.remark'), prop: "remark", align: "left" },
    {
      label: td('common.texts.operation', 'Operation'),
      width: 240,
      slot: "action",
      fixed: "right",
    },
  ],
  func: listDataDomain,
  params: {},
});

const searchStore = reactive({
  items: [
    {
      label: td('dm.dataDomain.name', 'Data Domain Name'),
      prop: "name",
      component: { is: "input", placeholder: td('dm.dataDomain.namePlaceholder', 'Please enter data domain name') },
    },
    {
      label: td('dm.dataDomain.engName', 'English Abbreviation'),
      prop: "engName",
      component: { is: "input", placeholder: td('dm.dataDomain.engNamePlaceholder', 'Please enter English abbreviation') },
    },
    {
      label: td('dm.dataDomain.ownerId', 'Responsible Person'),
      prop: "ownerUserId",
      component: {
        is: "tree-select",
        data: managerOptions,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "userId",
        placeholder: td('dm.dataDomain.ownerIdPlaceholder', 'Please select responsible person'),
        checkStrictly: true,
      },
    },
  ],
});

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
  url: import.meta.env.VITE_APP_BASE_API + "/dm/dataDomain/importData",
});

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('dm.dataDomain.nameRequired', 'Data domain name cannot be empty'), trigger: "blur" }],
    engName: [
      { required: true, message: td('dm.dataDomain.engNameRequired', 'English abbreviation cannot be empty'), trigger: "blur" },
      { pattern: /^[a-zA-Z]+$/, message: td('dm.dataDomain.englishOnly', 'Only English characters are allowed'), trigger: "blur" },
    ],
    ownerUserId: [
      { required: true, message: td('dm.dataDomain.ownerRequired', 'Responsible person cannot be empty'), trigger: "blur" },
    ],
  },
});

const { form, rules } = toRefs(data);

function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

onMounted(() => {
  getManagerOptions();
});

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    name: null,
    engName: null,
    ownerUserId: null,
    ownerUserPhoneNumber: null,
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
  proxy.resetForm("dataDomainRef");
}

/** Add button operation */
function handleAdd() {
  reset();
  getManagerOptions();
  // Explicitly initialize the person in charge phone field
  form.value.ownerUserPhoneNumber = null;
  open.value = true;
  title.value = td('dm.dataDomain.addTitle', 'Add Data Domain');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  getManagerOptions();
  const _id = row?.id || ids.value[0];
  getDataDomain(_id).then((response) => {
    form.value = response.data;

    open.value = true;
    title.value = td('dm.dataDomain.editTitle', 'Edit Data Domain');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  getManagerOptions();
  const _id = row?.id || ids.value[0];
  getDataDomain(_id).then((response) => {
    form.value = response.data;

    openDetail.value = true;
    title.value = td('dm.dataDomain.detailTitle', 'Data Domain Detail');
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["dataDomainRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDataDomain(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess', 'Updated successfully'));
            open.value = false;
            tableRef.value.getList();
            submitLoading.value = false;
          })
          .catch(() => {
            submitLoading.value = false;
          });
      } else {
        addDataDomain(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess', 'Added successfully'));
            open.value = false;
            tableRef.value.getList();
            submitLoading.value = false;
          })
          .catch(() => {
            submitLoading.value = false;
          });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

// Update phone number when person in charge changes
const handleContactChange = (selectedValue) => {
  const selectedUser = managerOptions.value.find(
    (user) => user.userId == selectedValue
  );
  form.value.ownerUserPhoneNumber = selectedUser?.phonenumber || "";
};
/** Delete button action */
function handleDelete(row) {
  const _ids = row?.id || ids.value;
  proxy.$modal
    .confirm(td('dm.dataDomain.confirmDelete', 'Are you sure to delete data domain "<id>"?').replace('<id>', _ids))
    .then(function () {
      return delDataDomain(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', 'Deleted successfully'));
    })
    .catch(() => {});
}

/** ---------------- Import related operations ------------------**/
/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `dataDomain_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**File upload is being processed */
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

/** File upload successfully processed */
const handleFileSuccess = (response, file) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td('dm.dataDomain.importResult', 'Import Result'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.getList();
};
/** ---------------------------------**/
</script>