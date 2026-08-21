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
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="td('att.common.cleanCatName')" prop="name">
          <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('att.common.cleanCatNamePlaceholder')" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item :label="td('att.common.parentCat')" prop="code">
          <el-tree-select class="el-form-input-width" v-model="queryParams.code" :data="attAssetCatOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id" :placeholder="td('att.common.parentCatPlaceholder')"
            check-strictly />
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
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
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['att:cleanCat:add']">{{ td('common.button.add') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button class="toggle-expand-all" type="primary" plain @click="toggleExpandAll">
              <svg-icon v-if="isExpandAll" icon-class="toggle" />
              <svg-icon v-else icon-class="expand" />
              <span>{{ isExpandAll ? td('common.button.fold') : td('common.button.expand') }}</span>
            </el-button>
          </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table height="60vh" v-if="refreshTable" v-loading="loading" :data="AttCleanCatList" row-key="id"
        :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column :label="td('att.common.cleanCatName')" align="left" prop="name" width="240"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>

        <el-table-column :label="td('common.texts.description')" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
          width="250">
          <template #default="scope">
            {{ scope.row.description || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.sortOrder')" align="left" prop="sortOrder" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.sortOrder }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.createdBy')" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.status')" align="center" prop="validFlag">
          <template #default="scope">
            <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

            <el-switch v-model="scope.row.validFlag" active-color="#13ce66" inactive-color="#ff4949"
              @change="handleStatusChange(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['att:cleanCat:edit']">{{ td('common.button.update') }}</el-button>
            <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
              v-hasPermi="['att:cleanCat:add']">{{ td('common.button.add') }}</el-button>
            <el-button link type="danger" icon="Delete" :disabled="scope.row.validFlag == true" @click="handleDelete(scope.row)"
              v-hasPermi="['att:cleanCat:remove']">{{ td('common.button.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
        @pagination="getList" />
    </div>

    <!-- Add or edit cleaning rule category management dialog -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-form ref="attCleanCatRef" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.categoryName')" prop="name" :label-position="labelPosition">
              <el-input v-model="form.name" :placeholder="td('att.common.cleanCatNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <!--            <el-form-item label="Category Sort" prop="sortOrder" :label-position="labelPosition">-->
          <!--&lt;!&ndash;              <el-input v-model="form.sortOrder" placeholder="Please enter category sort" />&ndash;&gt;-->
          <!--              <el-input-number v-model="form.sortOrder"  steps="1" :min="0"  placeholder="Please enter category sort" />-->
          <!--            </el-form-item>-->
          <el-col :span="12">
            <el-form-item :label="td('att.common.parentCat')" prop="parentId" :label-position="labelPosition">
              <el-tree-select :disabled="form.id" v-model="form.parentId" :data="attAssetCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }" value-key="id" :placeholder="td('att.common.parentCatPlaceholder')"
                check-strictly />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
              <el-input type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" v-model="form.description" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.sortOrder')" prop="sortOrder" :label-position="labelPosition">
              <el-input-number style="width: 100%" v-model="form.sortOrder" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
              <el-radio v-model="form.validFlag" :label="true">{{ td('att.common.enable') }}</el-radio>
              <el-radio v-model="form.validFlag" :label="false">{{ td('att.common.disable') }}</el-radio>
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
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CleanCat">
import { useI18n } from 'vue-i18n'
import useDefaultLang from "@/composables/useDefaultLang";
import { listAttCleanCat, getAttCleanCat, delAttCleanCat, addAttCleanCat, updateAttCleanCat } from "@/api/att/cat/cleanCat/cleanCat.js";
import { getToken } from "@/utils/auth.js";

const { t } = useI18n();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const AttCleanCatList = ref([]);
const attAssetCatOptions = ref([]);
// Column visibility information
const columns = ref([
  { key: 1, label: td('att.cleanCat.texts.name'), visible: true },
  { key: 2, label: td('att.common.parentCat'), visible: true },
  { key: 3, label: td('common.texts.sortOrder'), visible: true },
  { key: 4, label: td('common.texts.description'), visible: true },
  { key: 5, label: td('att.cleanCat.texts.hierarchyCode'), visible: true },
  { key: 8, label: td('common.texts.createdBy'), visible: true },
  { key: 10, label: td('common.texts.createdTime'), visible: true },
  { key: 14, label: td('common.texts.remark'), visible: true }
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
  // If no corresponding column configuration found, default to showing it
  if (!column) return true;
  // If corresponding column configuration found, control visibility based on the visible property
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
const refreshTable = ref(true);
const isExpandAll = ref(false);

/*** User import parameters */
const upload = reactive({
  // Whether to show the popup layer (user import)
  open: false,
  // Popup layer title (user import)
  title: "",
  // Whether to disable upload
  isUploading: false,
  // Whether to update existing user data
  updateSupport: 0,
  // Set upload request headers
  headers: { Authorization: "Bearer " + getToken() },
  // Upload URL
  url: import.meta.env.VITE_APP_BASE_API + "/att/attCleanCat/importData"
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
    name: [{ required: true, message: td('att.cleanCat.validations.nameRequired'), trigger: 'blur' }],
    parentId: [{ required: true, message: td('att.common.parentCatRequired'), trigger: 'blur' }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** Query cleaning rule category list */
function getList() {
  loading.value = true;
  listAttCleanCat(queryParams.value).then(response => {
    AttCleanCatList.value = proxy.handleTree(response.data, 'id');
    // total.value = response.data.length;
    loading.value = false;
  });
}

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// Reset form
function reset() {
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
    remark: null
  };
  proxy.resetForm("attCleanCatRef");
}

/** Search button operation */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** Reset button operation */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// Checkbox selection data
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.ID);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Toggle enable status value */
function handleStatusChange(row) {
  const text = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
  proxy.$modal
    .confirm(td('att.common.confirmStatusChangeGeneric', '', { status: text, name: row.name, type: td('att.common.cleanCatName') }))
    .then(function () {
      updateAttCleanCat({ id: row.id, validFlag: row.validFlag }).then((response) => {
        proxy.$modal.msgSuccess(td('att.common.statusSuccess', '', { status: text }));
        getList();
      }).catch((err) => {
        row.validFlag = !row.validFlag;
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

/** Expand/collapse operation */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

/** Sort trigger event */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** Add button operation */
function handleAdd(row) {
  reset();
  listAttCleanCat().then((response) => {
    attAssetCatOptions.value = [];
    const data = { id: 0, name: td('common.texts.topNode'), children: [] };
    data.children = proxy.handleTree(response.data, 'id', 'parentId');
    attAssetCatOptions.value.push(data);
  });
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = td('att.cleanCat.title.add');
}

function getDataTree() {
  listAttCleanCat().then((response) => {
    attAssetCatOptions.value = [];
    const data = { id: 0, name: td('common.texts.topNode'), children: [] };
    data.children = proxy.handleTree(response.data, 'id', 'parentId');
    attAssetCatOptions.value.push(data);
  });
}

/** Edit button operation */
async function handleUpdate(row) {
  reset();
  const response = await listAttCleanCat();
  attAssetCatOptions.value = [];
  // Filter node computed property
  const filteredDepts = response.data.filter((d) => {
    // Filter condition: remove items with target department ID or ancestors containing target department ID
    return d.ID !== row.id && !d.parentId.toString().split(',').includes(row.id.toString());
  });
  const data = { id: 0, name: td('common.texts.topNode'), children: [] };
  data.children = proxy.handleTree(filteredDepts, 'id', 'parentId');
  attAssetCatOptions.value.push(data);
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  getAttCleanCat(row.id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = td('att.cleanCat.title.edit');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _ID = row.ID || ids.value
  getAttCleanCat(_ID).then(response => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('att.cleanCat.title.detail');
  });
}

/** Submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["attCleanCatRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateAttCleanCat(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(error => {
          submitLoading.value = false;
        });
      } else {
        addAttCleanCat(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(error => {
          submitLoading.value = false;
        });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button operation */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm(td('att.cleanCat.messages.confirmDelete', '', { ids: _ids })).then(function () {
    return delAttCleanCat(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
  }).catch(() => { });
}

/** Export button operation */
function handleExport() {
  proxy.download('att/attCleanCat/export', {
    ...queryParams.value
  }, `AttCleanCat_${new Date().getTime()}.xlsx`)
}

/** ---------------- Import related operations -----------------**/
/** Import button operation */
function handleImport() {
  upload.title = td('att.cleanCat.importTitle');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `AttCleanCat_template_${new Date().getTime()}.xlsx`)
}

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
};

/** File upload in progress handler */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** File upload success handler */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", td('att.common.importResult'), { dangerouslyUseHTMLString: true });
  getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id
        }
      });
    }
  }
}

getList();
getDataTree();
</script>
