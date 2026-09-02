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

    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item :label="td('att.common.projectName')" prop="name" :label-position="labelPosition">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            :placeholder="td('att.common.projectNamePlaceholder')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('att.common.manager')" prop="managerId" :label-position="labelPosition">
          <el-select
            v-model="queryParams.managerId"
            class="el-form-input-width"
            @change="handleChange"
            filterable
            :placeholder="td('att.common.pleaseSelectManager')"
          >
            <el-option
              v-for="item in managerOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
            v-hasPermi="['att:project:query']"
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['att:project:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
          <!-- <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            :disabled="single"
                            @click="handleUpdate"
                            v-hasPermi="['att:project:edit']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xiugai--copy mr5"></i>{{ td('common.button.update') }}
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" plain :disabled="multiple" @click="handleDelete"
                            v-hasPermi="['att:project:remove']" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i>{{ td('common.button.delete') }}
                        </el-button>
                    </el-col> -->
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
        v-loading="loading"
        :data="attProjectList"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <!-- <el-table-column type="selection" width="55" align="center" /> -->
        <!--       <el-table-column v-if="getColumnVisibility(0)" label="number" align="center" prop="id" />-->
        <el-table-column
          :label="td('common.texts.number')"
          prop="id"
          width="80"
          align="center"
          v-if="getColumnVisibility(1)"
        >
          <template #default="scope">
            {{ scope.row.id || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('att.common.projectName')"
          align="left"
          prop="name"
          v-if="getColumnVisibility(2)"
          width="200"
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.description')"
          align="left"
          prop="description"
          :show-overflow-tooltip="{ effect: 'light' }"
          v-if="getColumnVisibility(3)"
          width="300"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('att.common.manager')"
          align="center"
          prop="managerId"
          v-if="getColumnVisibility(4)"
        >
          <template #default="scope">
            {{ scope.row.nickName || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('att.common.contactWay')"
          align="center"
          prop="managerPhone"
          v-if="getColumnVisibility(5)"
          width="120"
        >
          <template #default="scope">
            {{ scope.row.managerPhone || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(7)"
          :label="td('common.texts.createdBy')"
          :show-overflow-tooltip="{ effect: 'light' }"
          align="left"
          prop="createBy"
          width="120"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <!--   sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
        <el-table-column
          v-if="getColumnVisibility(6)"
          :label="td('att.common.createTime')"
          align="center"
          prop="createTime"
          width="150"
        >
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.status')"
          align="center"
          prop="validFlag"
          v-if="getColumnVisibility(8)"
        >
          <template #default="scope">
            <el-switch
              v-model="scope.row.validFlag"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.remark')"
          align="left"
          width="200"
          prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }"
          v-if="getColumnVisibility(9)"
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
              v-hasPermi="['att:project:edit']"
              >{{ td('common.button.update') }}</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['att:project:remove']"
              >{{ td('common.button.delete') }}</el-button
            >
            <el-button
              link
              type="primary"
              icon="view"
              v-hasPermi="['att:project:query']"
              @click="handleDetail(scope.row)"
              >{{ td('common.button.details') }}</el-button
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
    </div>

    <!-- Add or modify project dialog box -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
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
        :label-position="labelPosition"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.projectName')" prop="name">
              <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.manager')" prop="managerId">
              <!--                <el-input v-model="form.managerId" placeholder="Please select the person in charge" />-->
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
                >
                </el-option>
              </el-select>
              <!--                <el-cascader :options="managerOptions" :show-all-levels="false"></el-cascader>-->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('att.common.contactWay')" prop="managerPhone" :label-position="labelPosition">
              <el-input
                v-model="form.managerPhone"
                :placeholder="td('att.common.contactWayPlaceholder')"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <el-input
                v-model="form.description"
                type="textarea"
                :placeholder="td('common.form.descriptionPlaceholder')"
                :min-height="192"
                show-word-limit
                maxlength="256字符"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
              <el-radio-group v-model="form.validFlag">
                <el-radio :label="true">{{ td('att.common.enable') }}</el-radio>
                <el-radio :label="false">{{ td('att.common.disable') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <el-input
                type="textarea"
                v-model="form.remark"
                :placeholder="td('common.form.remarkPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm"
            >{{ td('common.button.confirm') }}</el-button>
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
      <el-form ref="daAssetApplyRef" :model="form" label-width="90px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.number') + ':'" prop="id" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.id }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.projectName') + ':'" prop="name" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.project.texts.manager') + ':'" prop="assetTableName" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.nickName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('att.project.texts.contact') + ':'" prop="managerPhone" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.managerPhone ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.createdBy') + ':'" prop="createBy" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.createBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.createdTime') + ':'" prop="createTime" :label-position="labelPosition">
              <div class="form-readonly">
                {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.updatedBy') + ':'" prop="createBy" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.updateBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.updatedTime') + ':'" prop="updateTime" :label-position="labelPosition">
              <div class="form-readonly">
                {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status') + ':'" prop="validFlag" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.validFlag ? td('att.common.enable') : td('att.common.disable') }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.remark ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="openDetail = false">{{ td('common.button.close') }}</el-button>
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
// import { deptUserTree } from "@/api/system/system/user.js";
import { getToken } from "@/utils/auth.js";
import { deptUserTree } from "@/api/system/system/user.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { dp_model_status } = proxy.useDict("dp_model_status");

const attProjectList = ref([]);

// Show hidden information
const columns = ref([
  { key: 1, label: td('common.texts.number'), visible: true },
  { key: 2, label: td('att.common.projectName'), visible: true },
  { key: 3, label: td('common.texts.description'), visible: true },
  { key: 4, label: td('att.common.manager'), visible: true },
  { key: 5, label: td('att.common.contactWay'), visible: true },
  { key: 6, label: td('common.texts.createdTime'), visible: true },
  { key: 7, label: td('common.texts.createdBy'), visible: true },
  { key: 8, label: td('common.texts.status'), visible: true },
  { key: 9, label: td('common.texts.remark'), visible: true },
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
const managerOptions = ref([]);

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
  url: import.meta.env.VITE_APP_BASE_API + "/att/attProject/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
  },
  rules: {
    managerId: [{ required: true, message: td('att.project.validations.managerRequired'), trigger: "blur" }],
    name: [{ required: true, message: td('att.project.validations.nameRequired'), trigger: "blur" }],
    // managerId: [{ required: true, message: "Responsible person cannot be empty", trigger: "blur" }],
    // validFlag: [{ required: true, message: 'Is it valid and cannot be empty', trigger: 'change' }]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** Query project list */
function getList() {
  loading.value = true;
  listAttProject(queryParams.value).then((response) => {
    attProjectList.value = response.data.rows;
    console.log(response.data.rows, "response.data.rows");
    total.value = response.data.total;
    loading.value = false;
  });
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}
function handleChange(value) {
  const selectedManager = managerOptions.value.find(
    (item) => item.userId === form.value.managerId
  );
  form.value.managerPhone = selectedManager.phonenumber; // Store the complete object into form
}
// function getUserTree(){
//    deptUserTree().then(response => {
//     managerOptions.value = response.data;
//   })
// }

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
      editProjectStatus(row.id, status).then((response) => {
        proxy.$modal.msgSuccess(td('att.common.statusSuccess', '', { status: text }));
        getList();
      });
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
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("attProjectRef");
}

/** Search button action */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** reset button action */
function resetQuery() {
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

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('att.project.title.add');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  console.log(_id, "22");
  getAttProject(_id).then((response) => {
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    open.value = true;
    title.value = td('att.project.title.edit');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttProject(_id).then((response) => {
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
      if (form.value.id != null) {
        updateAttProject(form.value)
          .then((response) => {
            submitLoading.value = false;
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));

            open.value = false;
            getList();
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      } else {
        addAttProject(form.value)
          .then((response) => {
            submitLoading.value = false;
            if (response.code === 200) {
              proxy.$modal.msgSuccess(td('common.message.addSuccess'));
              open.value = false;
              getList();
            }
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
  const _ids = row?.id || ids.value;
  if (!_ids || (Array.isArray(_ids) && _ids.length === 0)) {
    return proxy.$modal.msgWarning(td('common.message.selectRecord'));
  }
  proxy.$modal
    .confirm(td('att.project.message.confirmDelete', '', { ids: Array.isArray(_ids) ? _ids.join(',') : _ids }))
    .then(function () {
      return delAttProject(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** Export button action */
function handleExport() {
  proxy.download(
    "att/attProject/export",
    {
      ...queryParams.value,
    },
    `attProject_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
  upload.title = td('att.project.importTitle');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `attProject_template_${new Date().getTime()}.xlsx`
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
    td('att.project.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  getList();
};
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

getList();
// getUserTree();
</script>
