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
    <GuideTip tip-id="att/attTheme.list" />

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
              v-hasPermi="['att:theme:add']"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
        </el-row>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
        <template #icon="{ row }">
          <image-preview :src="row.icon || noDataImg" :width="50" :height="50" />
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
            v-hasPermi="['att:theme:edit']"
          >{{ td('common.button.update') }}</el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['att:theme:remove']"
          >{{ td('common.button.delete') }}</el-button>
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(row)"
            v-hasPermi="['att:theme:query']"
          >{{ td('common.button.details') }}</el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Add or modify theme dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <el-form ref="attThemeRef" :model="form" :rules="rules" label-width="80px" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col>
            <el-form-item :label="td('att.common.themeName')" prop="name">
              <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description">
              <el-input type="textarea" v-model="form.description" :placeholder="td('common.form.descriptionPlaceholder')"  maxlength="256字符" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('att.common.icon')" prop="icon">
              <image-upload :limit="1" v-model="form.icon" :width="50" :height="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.sortOrder')" prop="sortOrder">
              <el-input-number style="width: 100%" v-model="form.sortOrder" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="validFlag">
              <el-radio v-model="form.validFlag" :label="true">{{ td('att.common.enable') }}</el-radio>
              <el-radio v-model="form.validFlag" :label="false">{{ td('att.common.disable') }}</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Theme details dialog -->
    <el-dialog :title="title" v-model="openDetail" width="1000px" :append-to="$refs['app-container']" draggable>
      <el-form ref="attThemeDetailRef" :model="form" label-width="90px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.number')+':'" prop="id">
              <div class="form-readonly">{{ form.id }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.themeName') + ':'" prop="name">
              <div class="form-readonly">{{ form.name }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.icon')+':'" prop="icon">
              <image-preview :src="form.icon || noDataImg" :width="50" :height="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description') + ':'" prop="description">
              <div class="form-readonly textarea">{{ form.description ?? "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.createdBy')+':'" prop="createBy">
              <div class="form-readonly">{{ form.createBy }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.createdTime') + ':'" prop="createTime">
              <div class="form-readonly">{{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.updatedBy')+':'" prop="updateBy">
              <div class="form-readonly">{{ form.updateBy }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.updatedTime')+':'" prop="updateTime">
              <div class="form-readonly">{{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status') + ':'" prop="validFlag">
              <div class="form-readonly">{{ form.validFlag ? td('att.common.enable') : td('att.common.disable') }}</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="openDetail = false">{{ td('common.button.close') }} </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
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
        <div class="el-upload__text">{{ td('common.upload.dragOrClick') }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('common.upload.updateExistingData') }}
            </div>
            <span>{{ td('common.upload.fileFormat') }}</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">{{ td('common.upload.downloadTemplate') }}</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Theme">
import {
  listAttTheme,
  getAttTheme,
  delAttTheme,
  addAttTheme,
  updateAttTheme
} from '@/api/att/theme/theme.js';
import { getToken } from '@/utils/auth.js';
import useDefaultLang from "@/composables/useDefaultLang";

const noDataImg = new URL('@/assets/images/system/D.png', import.meta.url).href
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const tableRef = ref(null);
const submitLoading = ref(false);
const open = ref(false);
const openDetail = ref(false);
const title = ref('');

/*** User import parameters */
const upload = reactive({
  open: false,
  title: '',
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: 'Bearer ' + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + '/att/attTheme/importData'
});

const searchStore = reactive({
  items: [
    {
      label: td('att.common.themeName'),
      prop: "name",
      component: { is: "input", placeholder: td('common.form.namePlaceholder') }
    }
  ]
});

const tableStore = reactive({
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 60, align: "left", sortable: true },
    { label: td('att.theme.table.name'), prop: "name", width: 200, align: "left" },
    { label: td('att.theme.table.icon'), prop: "icon", width: 100, align: "center", slot: "icon" },
    { label: td('common.texts.description'), prop: "description", width: 300, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td('common.texts.sortOrder'), prop: "sortOrder", width: 80, align: "left" },
    { label: td('common.texts.createdBy'), prop: "createBy", width: 120, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td('common.texts.createdTime'), prop: "createTime", width: 150, align: "center", sortable: true, date: true },
    { label: td('common.texts.status'), prop: "validFlag", width: 120, align: "center", slot: "validFlag" },
    { label: td('common.texts.handle'), prop: "action", width: 240, align: "center", slot: "action", fixed: "right" }
  ],
  func: listAttTheme,
  params: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    orderByColumn: 'sortOrder,createTime',
    description: null
  }
});

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('att.common.themeNameRequired'), trigger: 'blur' }],
  }
});

const { form, rules } = toRefs(data);

/** Search button action */
function handleQuery() {
  tableStore.params.pageNum = 1;
}

/** reset button action */
function resetQuery() {
  tableStore.params.name = null;
  tableStore.params.description = null;
}

/** Change enabled status value */
function handleStatusChange(row) {
  const statusText = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
  proxy.$modal
    .confirm(td('att.common.confirmStatusChangeGeneric', '', { status: statusText, type: td('att.theme.themeWord'), name: row.name }))
    .then(() => {
      return updateAttTheme({ id: row.id, validFlag: row.validFlag });
    })
    .then(() => {
      proxy.$modal.msgSuccess(td('att.common.statusSuccess', '', { status: statusText }));
      tableRef.value.refresh();
    })
    .catch(() => {
      row.validFlag = !row.validFlag;
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
    id: null,
    name: null,
    icon: null,
    sortOrder: 0,
    description: null,
    validFlag: true,
    remark: null
  };
  proxy.resetForm('attThemeRef');
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('att.theme.title.add');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  getAttTheme(row.id).then((response) => {
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    open.value = true;
    title.value = td('att.theme.title.edit');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  getAttTheme(row.id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('att.theme.title.detail');
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs['attThemeRef'].validate((valid) => {
    if (valid) {
      const api = form.value.id ? updateAttTheme : addAttTheme;
      const msg = form.value.id ? td('common.message.editSuccess') : td('common.message.addSuccess');
      api(form.value)
        .then(() => {
          submitLoading.value = false;
          proxy.$modal.msgSuccess(msg);
          open.value = false;
          tableRef.value.refresh();
        })
        .catch(() => {
          submitLoading.value = false;
        });
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  proxy.$modal
    .confirm(td('att.theme.deleteConfirm', '', { ids: row.id }))
    .then(() => {
      return delAttTheme(row.id);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** Export button action */
function handleExport() {
  proxy.download(
    'att/attTheme/export',
    { ...tableStore.params },
    `attTheme_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
function handleImport() {
  upload.title = td('att.theme.importTitle');
  upload.open = true;
}

function importTemplate() {
  proxy.download(
    'system/user/importTemplate',
    {},
    `attTheme_template_${new Date().getTime()}.xlsx`
  );
}

function submitFileForm() {
  proxy.$refs['uploadRef'].submit();
}

const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

const handleFileSuccess = (response, file) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs['uploadRef'].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    '</div>',
    td('att.common.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.refresh();
};
</script>

