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
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"

        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item
          :label="td('dpp.setting.dataDevCat.dataDevCatName')"
          prop="name"
        >
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            :placeholder="td('dpp.setting.dataDevCat.inputDataDevCatName')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item
          :label="td('dpp.setting.dataDevCat.parentCat')"
          prop="code"
        >
          <el-tree-select
            filterable
            class="el-form-input-width"
            v-model="queryParams.code"
            :data="attDataDevCatOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }"
            value-key="id"
            :placeholder="td('dpp.setting.dataDevCat.selectParent')"
            check-strictly
          />
        </el-form-item>
        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i
            >{{ td("common.button.query") }}
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i
            >{{ td("common.button.reset") }}
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
              v-hasPermi="['att:dataDevCat:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i
              >{{ td("common.button.add") }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              class="toggle-expand-all"
              type="primary"
              plain
              @click="toggleExpandAll"
            >
              <svg-icon v-if="isExpandAll" icon-class="toggle" />
              <svg-icon v-else icon-class="expand" />
              <span>{{
                isExpandAll
                  ? td("common.button.fold")
                  : td("common.button.expand")
              }}</span>
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar
            v-model:showSearch="showSearch"
            @queryTable="getList"
          ></right-toolbar>
        </div>
      </div>
      <el-table
        height="60vh"
        v-if="refreshTable"
        v-loading="loading"
        :data="AttDataDevCatList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column
          :label="td('dpp.setting.dataDevCat.dataDevCatName')"
          align="left"
          prop="name"
          width="200"
          :show-overflow-tooltip="{ effect: 'light' }"
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
          width="250"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.setting.dataDevCat.sortOrder')"
          align="left"
          prop="sortOrder"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="80"
        >
          <template #default="scope">
            {{ scope.row.sortOrder }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.createdBy')"
          align="center"
          prop="createBy"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.createdTime')"
          align="center"
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
          :label="td('common.texts.status')"
          align="center"
          prop="validFlag"
        >
          <template #default="scope">
            <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

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
              v-hasPermi="['att:dataDevCat:edit']"
              >{{ td("common.button.update") }}</el-button
            >
            <el-button
              link
              type="primary"
              icon="Plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['att:dataDevCat:add']"
              >{{ td("common.button.add") }}</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              :disabled="scope.row.validFlag === true"
              @click="handleDelete(scope.row)"
              v-hasPermi="['att:dataDevCat:remove']"
              >{{ td("common.button.delete") }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- Add or modify the data development category management dialog box -->
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
        ref="AttDataDevCatRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.dataDevCat.categoryName')"
              prop="name"
             :label-position="labelPosition">
              <el-input
                v-model="form.name"
                :placeholder="td('dpp.setting.dataDevCat.inputDataDevCatName')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.dataDevCat.parentCat')"
              prop="parentId"
             :label-position="labelPosition">
              <el-tree-select
                filterable
                :disabled="form.id"
                v-model="form.parentId"
                :data="attDataDevCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('dpp.setting.dataDevCat.selectParent')"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20"> </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.dataDevCat.sortOrder')"
              prop="sortOrder"
             :label-position="labelPosition">
              <el-input-number
                style="width: 100%"
                v-model="form.sortOrder"
                controls-position="right"
                :min="0"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
              <el-radio v-model="form.validFlag" :label="true">{{
                td("dpp.setting.dataDevCat.enable")
              }}</el-radio>
              <el-radio v-model="form.validFlag" :label="false">{{
                td("dpp.setting.dataDevCat.disable")
              }}</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
              <el-input
                type="textarea"
                v-model="form.description"
                :placeholder="td('common.form.descriptionPlaceholder')"
                :min-height="192"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
              <el-input
                type="textarea"
                :placeholder="td('common.form.remarkPlaceholder')"
                v-model="form.remark"
                :min-height="192"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{
            td("common.button.cancel")
          }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{
            td("common.button.confirm")
          }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Data development category management details dialog box -->
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
        </span>
      </template>
      <el-form ref="AttDataDevCatRef" :model="form" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.dataDevCat.categoryNameDetail')"
              prop="name"
             :label-position="labelPosition">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.dataDevCat.parentId')"
              prop="parentId"
             :label-position="labelPosition">
              <div>
                {{ form.parentId }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.dataDevCat.categorySort')"
              prop="sortOrder"
             :label-position="labelPosition">
              <div>
                {{ form.sortOrder }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('common.texts.description')"
              prop="description"
             :label-position="labelPosition">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.dataDevCat.levelCode')"
              prop="code"
             :label-position="labelPosition">
              <div>
                {{ form.code }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <div>
                {{ form.remark }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{
            td("common.button.close")
          }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog
      :title="upload.title"
      v-model="upload.open"
      width="800px"
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
        <div class="el-upload__text">{{ td("common.upload.dragOrClick") }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{
                td("dpp.setting.dataDevCat.updateExistData")
              }}
            </div>
            <span>{{ td("common.upload.fileFormat") }}</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >{{ td("dpp.setting.dataDevCat.downloadTemplate") }}</el-link
            >
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{
            td("common.button.cancel")
          }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{
            td("common.button.confirm")
          }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataDevCat">
import {
  ref,
  reactive,
  watch,
  nextTick,
  computed,
  onMounted,
  onActivated,
} from "vue";
import { useRouter } from "vue-router";
import { toRefs } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";

//  dataDevCat
import {
  listAttDataDevCat,
  getAttDataDevCat,
  delAttDataDevCat,
  addAttDataDevCat,
  updateAttDataDevCat,
  hasDataDevelopmentTask,
  isDataDevCatNameUsed,
  getDataDevelopmentTaskCount,
} from "@/api/att/cat/dataDevCat/dataDevCat";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user";

const { td } = useDefaultLang();
const userStore = useUserStore();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const originalName = ref("");
const warningRequestOptions = { hideErrorMessage: true };

function showRequestWarning(error) {
  if (error === "cancel" || error === "close") return;
  const message = error?.message || error;
  if (message) {
    proxy.$modal.msgWarning(message);
  }
}

async function withRequestWarning(requestPromise) {
  try {
    return await requestPromise;
  } catch (error) {
    showRequestWarning(error);
    throw error;
  }
}

const AttDataDevCatList = ref([]);

// Show hidden status
const columnVisible = ref({
  1: true,
  2: true,
  3: true,
  4: true,
  5: true,
  8: true,
  10: true,
  14: true,
});

// Column configuration (use computed properties to ensure internationalized text responds to language switches)
const columns = computed(() => [
  {
    key: 1,
    label: td("dpp.setting.dataDevCat.categoryNameDetail"),
    visible: columnVisible.value[1],
  },
  {
    key: 2,
    label: td("dpp.setting.dataDevCat.parentId"),
    visible: columnVisible.value[2],
  },
  {
    key: 3,
    label: td("dpp.setting.dataDevCat.categorySort"),
    visible: columnVisible.value[3],
  },
  {
    key: 4,
    label: td("common.texts.description"),
    visible: columnVisible.value[4],
  },
  {
    key: 5,
    label: td("dpp.setting.dataDevCat.levelCode"),
    visible: columnVisible.value[5],
  },
  {
    key: 8,
    label: td("common.texts.createdBy"),
    visible: columnVisible.value[8],
  },
  {
    key: 10,
    label: td("common.texts.createdTime"),
    visible: columnVisible.value[10],
  },
  {
    key: 14,
    label: td("common.texts.remark"),
    visible: columnVisible.value[14],
  },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
  return column.visible;
};

const attDataDevCatOptions = ref([]);
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
const refreshTable = ref(true);
const isExpandAll = ref(false);
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
  url: import.meta.env.VITE_APP_BASE_API + "/att/AttDataDevCat/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    parentId: null,
    sortOrder: null,
    description: null,
    code: null,
    createTime: null,
  },
  rules: {
    name: [
      {
        required: true,
        message: td("dpp.setting.dataDevCat.nameRequired"),
        trigger: "blur",
      },
      {
        validator: validateDataDevCatName,
        trigger: "blur",
      },
    ],
    parentId: [
      {
        required: true,
        message: td("dpp.setting.dataDevCat.parentRequired"),
        trigger: "blur",
      },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

watch(
  () => userStore.projectId,
  () => {
    getList();
  }
);

/** Expand/collapse operations */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

/** Query the data development category management list */
function getList() {
  loading.value = true;
  queryParams.value.projectId = userStore.projectId;
  queryParams.value.projectCode = userStore.projectCode;
  listAttDataDevCat(queryParams.value, warningRequestOptions)
    .then((response) => {
      const rows = response?.data || [];
      AttDataDevCatList.value = proxy.handleTree(rows, "id", "parentId");
      // total.value = response.data.total;

      attDataDevCatOptions.value = [];
      const data = { id: 0, name: td('common.texts.topNode'), children: [] };
      data.children = proxy.handleTree(rows, "id", "parentId");
      attDataDevCatOptions.value.push(data);
    })
    .catch(showRequestWarning)
    .finally(() => {
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
  originalName.value = "";
  form.value = {
    id: null,
    name: null,
    parentId: null,
    sortOrder: 0,
    description: null,
    code: null,
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
  proxy.resetForm("AttDataDevCatRef");
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
function handleAdd(row) {
  reset();
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = td("dpp.setting.dataDevCat.addDataDevCat", "新增数据开发类目");
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getAttDataDevCat(_id, warningRequestOptions)
    .then((response) => {
      form.value = response.data;
      originalName.value = response.data.name || "";
      open.value = true;
      title.value = td(
        "dpp.setting.dataDevCat.editDataDevCat",
        "修改数据开发类目"
      );
    })
    .catch(showRequestWarning);
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttDataDevCat(_id, warningRequestOptions)
    .then((response) => {
      form.value = response.data;
      openDetail.value = true;
      title.value = td(
        "dpp.setting.dataDevCat.dataDevCatDetail",
        "数据开发类目管理详情"
      );
    })
    .catch(showRequestWarning);
}

/** Change enabled status value */
async function handleStatusChange(row) {
  if (row.validFlag === true && row.parentId != null && Number(row.parentId) !== 0) {
    const parent = findCategoryById(AttDataDevCatList.value, row.parentId);
    if (!parent || parent.validFlag !== true) {
      row.validFlag = false;
      proxy.$modal.msgWarning(
        td(
          "dpp.setting.dataDevCat.enableParentFirst",
          '请先启用父节点“{name}”，再启用当前节点。',
          { name: parent?.name || row.parentId }
        )
      );
      return;
    }
  }

  const text = row.validFlag === true ? td('dpp.setting.dataDevCat.enable') : td('dpp.setting.dataDevCat.disable');
  const isDisabling = row.validFlag === false;
  const confirmMessage = isDisabling && row.children?.length
    ? td(
        'dpp.setting.dataDevCat.confirmDisableParent',
        '停用父类目将同步影响子类目，请确认'
      )
    : td('dpp.setting.dataDevCat.confirmChangeStatus', '', { status: text, name: row.name });

  try {
    await proxy.$modal.confirm(confirmMessage);

    if (isDisabling) {
      const response = await withRequestWarning(hasDataDevelopmentTask(row.id, warningRequestOptions));
      if (response.data === true) {
        row.validFlag = true;
        proxy.$modal.msgWarning(
          td(
            'dpp.setting.dataDevCat.developmentTaskExistsCannotDisable',
            '存在数据开发任务，不允许禁用'
          )
        );
        return;
      }
    }

    await withRequestWarning(updateAttDataDevCat({
      id: row.id,
      projectId: row.projectId,
      projectCode: row.projectCode,
      name: row.name,
      parentId: row.parentId,
      sortOrder: row.sortOrder,
      description: row.description,
      validFlag: row.validFlag,
      code: row.code,
      remark: row.remark,
    }, warningRequestOptions));
    proxy.$modal.msgSuccess(text + td('common.message.success'));
    getList();
  } catch (error) {
    row.validFlag = !row.validFlag;
  }
}

function findCategoryById(categories, id) {
  for (const category of categories) {
    if (String(category.id) === String(id)) return category;
    const child = findCategoryById(category.children || [], id);
    if (child) return child;
  }
  return null;
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["AttDataDevCatRef"].validate(async (valid) => {
    if (valid) {
      if (form.value.id != null) {
        try {
          if ((form.value.name || "").trim() !== originalName.value.trim()) {
            const response = await withRequestWarning(
              getDataDevelopmentTaskCount(form.value.id, warningRequestOptions)
            );
            const taskCount = Number(response.data || 0);
            if (taskCount > 0) {
              await proxy.$modal.confirm(
                td(
                  "dpp.setting.dataDevCat.confirmRenameWithTaskCount",
                  "该类目已被 {count} 个数据开发任务使用，修改名称后任务归属显示将同步变化。",
                  { count: taskCount }
                )
              );
            }
          }
          await withRequestWarning(updateAttDataDevCat(form.value, warningRequestOptions));
          proxy.$modal.msgSuccess(td("common.message.editSuccess"));
          open.value = false;
          getList();
        } catch (error) {
          // The request interceptor displays API errors; cancelling only stops submission.
        } finally {
          submitLoading.value = false;
        }
      } else {
        form.value.projectId = userStore.projectId;
        form.value.projectCode = userStore.projectCode;
        addAttDataDevCat(form.value, warningRequestOptions)
          .then((response) => {
            proxy.$modal.msgSuccess(td("common.message.addSuccess"));
            open.value = false;
            getList();
            submitLoading.value = false;
          })
          .catch((error) => {
            showRequestWarning(error);
            submitLoading.value = false;
          });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

async function validateDataDevCatName(_rule, value, callback) {
  if (!value || form.value.id != null) {
    callback();
    return;
  }
  try {
    const response = await isDataDevCatNameUsed({
      parentId: form.value.parentId,
      name: value.trim(),
    }, warningRequestOptions);
    if (response.data === true) {
      callback(new Error(td("dpp.setting.dataDevCat.nameUsed", "名称已被使用")));
      return;
    }
    callback();
  } catch (error) {
    callback(new Error(td("dpp.setting.dataDevCat.nameValidationFailed", "名称校验失败")));
  }
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(
      td(
        "dpp.setting.dataDevCat.confirmDelete",
        '是否确认删除数据开发类目管理编号为"{id}"的数据项？'
      ).replace("{id}", _ids)
    )
    .then(function () {
      return delAttDataDevCat(_ids, warningRequestOptions);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess"));
    })
    .catch(showRequestWarning);
}

/** Export button action */
function handleExport() {
  proxy.download(
    "att/AttDataDevCat/export",
    {
      ...queryParams.value,
    },
    `AttDataDevCat_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
  upload.title = td('dpp.setting.dataDevCat.dataDevCatImport');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `AttDataDevCat_template_${new Date().getTime()}.xlsx`
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
    td('dpp.setting.dataDevCat.importResult'),
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

onMounted(() => {
  getList();
});

onActivated(() => {
  getList();
});
</script>
