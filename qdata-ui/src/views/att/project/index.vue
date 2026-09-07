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
    <GuideTip tip-id="att/attProject.list" />

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
              v-hasPermi="['att:project:add']"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
        </el-row>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
        <template #status="{ row }">
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
            v-hasPermi="['att:project:edit']"
          >{{ td('common.button.update') }}</el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['att:project:remove']"
          >{{ td('common.button.delete') }}</el-button>
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetail(row)"
            v-hasPermi="['att:project:query']"
          >{{ td('common.button.details') }}</el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Add or modify project dialog box -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="attProjectRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.projectName')" prop="name">
              <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.manager')" prop="managerId">
              <el-select
                v-model="form.managerId"
                @change="handleChange"
                filterable
                :placeholder="td('att.common.pleaseSelectManager')"
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
            <el-form-item :label="td('att.common.contactWay')" prop="managerPhone">
              <el-input
                v-model="form.managerPhone"
                :placeholder="td('att.common.contactWayPlaceholder')"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
       
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.status')" prop="validFlag">
              <el-radio-group v-model="form.validFlag">
                <el-radio :label="true">{{ td('att.common.enable') }}</el-radio>
                <el-radio :label="false">{{ td('att.common.disable') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
         <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :placeholder="td('common.form.descriptionPlaceholder')"
                :rows="4"
                show-word-limit
                 maxlength="256字符"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      :title="title"
      v-model="openDetail"
      width="1000px"
      :append-to="$refs['app-container']"
      draggable
    >
      <el-form :model="form" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.number') + ':'">
              <div class="form-readonly">{{ form.id }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('att.common.projectName') + ':'">
              <div class="form-readonly">{{ form.name }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('att.common.manager') + ':'">
              <div class="form-readonly">{{ form.nickName }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('att.common.contactWay') + ':'">
              <div class="form-readonly">{{ form.managerPhone || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description') + ':'">
              <div class="form-readonly textarea">{{ form.description || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.createdBy') + ':'">
              <div class="form-readonly">{{ form.createBy }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.createdTime') + ':'">
              <div class="form-readonly">{{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.updatedBy') + ':'">
              <div class="form-readonly">{{ form.updateBy }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.updatedTime') + ':'">
              <div class="form-readonly">{{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.status') + ':'">
              <div class="form-readonly">{{ form.validFlag ? td('att.common.enable') : td('att.common.disable') }}</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openDetail = false">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Project">
import {
  listAttProject,
  getAttProject,
  delAttProject,
  addAttProject,
  updateAttProject,
  editProjectStatus,
} from "@/api/att/project/project.js";
import { deptUserTree } from "@/api/system/system/user.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const tableRef = ref(null);
const submitLoading = ref(false);

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const managerOptions = ref([]);

const tableStore = reactive({
  config: {
    initResquest: true,
  },
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 60, align: "left", sortable: true },
    { label: td('att.common.projectName'), prop: "name", width: 260, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td('common.texts.description'), prop: "description", width: 256, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td('att.common.manager'), prop: "nickName", width: 160, align: "left" },
    { label: td('att.common.contactWay'), prop: "managerPhone", width: 160, align: "left" },
    { label: td('common.texts.createdBy'), prop: "createBy", width: 160, align: "left" },
    { label: td('common.texts.createdTime'), prop: "createTime", width: 160, align: "center", sortable: true, date: true },
    { label: td('common.texts.status'), prop: "validFlag", width: 100, align: "center", slot: "status" },
    { label: td('common.texts.operation'), width: 240, align: "center", slot: "action", fixed: "right" },
  ],
  func: listAttProject,
  params: {
    name: null,
    managerId: null,
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('att.common.projectName'),
      prop: "name",
      component: { is: "input", placeholder: td('att.common.projectNamePlaceholder'), clearable: true }
    },
    {
      label: td('att.common.manager'),
      prop: "managerId",
      component: {
        is: "select",
        placeholder: td('att.common.pleaseSelectManager'),
        filterable: true,
        options: managerOptions,
        props: { label: 'nickName', value: 'userId' }
      }
    },
  ],
});

const data = reactive({
  form: {},
  rules: {
    managerId: [{ required: true, message: td('att.project.validations.managerRequired'), trigger: "blur" }],
    name: [{ required: true, message: td('att.project.validations.nameRequired'), trigger: "blur" }],
  },
});

const { form, rules } = toRefs(data);

/** Query manager options */
function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

function handleChange(value) {
  const selectedManager = managerOptions.value.find(
    (item) => item.userId === form.value.managerId
  );
  if (selectedManager) {
    form.value.managerPhone = selectedManager.phonenumber;
  }
}

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

/** Change enabled status value */
function handleStatusChange(row) {
  const text = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
  const status = row.validFlag === true ? 1 : 0;
  proxy.$modal
    .confirm(td('att.common.confirmStatusChangeGeneric', '', { status: text, name: row.name, type: td('att.common.projectName') }))
    .then(function () {
      return editProjectStatus(row.id, status);
    })
    .then(() => {
      proxy.$modal.msgSuccess(td('att.common.statusSuccess', '', { status: text }));
      tableRef.value.refresh();
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

// form reset
function reset() {
  form.value = {
    id: null,
    name: null,
    managerId: null,
    managerPhone: null,
    description: null,
    validFlag: true,
  };
  proxy.resetForm("attProjectRef");
}

/** Search button action */
function handleQuery() {
  tableStore.params.pageNum = 1;
}

/** reset button action */
function resetQuery() {
  // If there was a tree, we would reset it here
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('att.project.title.add');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  getAttProject(row.id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('att.project.title.edit');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  getAttProject(row.id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('att.project.title.detail');
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["attProjectRef"].validate((valid) => {
    if (valid) {
      const api = form.value.id ? updateAttProject : addAttProject;
      api(form.value)
        .then(() => {
          submitLoading.value = false;
          proxy.$modal.msgSuccess(td(form.value.id ? 'common.message.editSuccess' : 'common.message.addSuccess'));
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
    .confirm(td('att.project.message.confirmDelete', '', { ids: row.id }))
    .then(function () {
      return delAttProject(row.id);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

getManagerOptions();
</script>
