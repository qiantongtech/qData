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
          :label="td('dpp.setting.taskCat.taskCatName')"
          prop="name"
        >
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            :placeholder="td('dpp.setting.taskCat.inputTaskCatName')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('dpp.setting.taskCat.parentCat')" prop="code">
          <el-tree-select
            filterable
            class="el-form-input-width"
            v-model="queryParams.code"
            :data="attTaskCatOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }"
            value-key="id"
            :placeholder="td('dpp.setting.taskCat.selectParent')"
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
              v-hasPermi="['att:taskCat:add']"
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
        :data="AttTaskCatList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column
          :label="td('dpp.setting.taskCat.taskCatName')"
          align="left"
          prop="name"
          width="240"
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
          width="300"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.setting.taskCat.sortOrder')"
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
              v-hasPermi="['att:taskCat:edit']"
              >{{ td("common.button.update") }}</el-button
            >
            <el-button
              link
              type="primary"
              icon="Plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['att:taskCat:add']"
              >{{ td("common.button.add") }}</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['att:taskCat:remove']"
              >{{ td("common.button.delete") }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
            /> -->
    </div>

    <!-- 添加或修改数据集成数据集成类目管理对话框 -->
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
        ref="AttTaskCatRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.taskCat.categoryName')"
              prop="name"
             :label-position="labelPosition">
              <el-input
                v-model="form.name"
                :placeholder="td('dpp.setting.taskCat.inputTaskCatName')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.taskCat.parentCat')"
              prop="parentId"
             :label-position="labelPosition">
              <el-tree-select
                filterable
                :disabled="form.id"
                v-model="form.parentId"
                :data="attTaskCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('dpp.setting.taskCat.selectParent')"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20"> </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.taskCat.sortOrder')"
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
                td("dpp.setting.taskCat.enable")
              }}</el-radio>
              <el-radio v-model="form.validFlag" :label="false">{{
                td("dpp.setting.taskCat.disable")
              }}</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
              <el-input
                type="textarea"
                :placeholder="td('common.form.descriptionPlaceholder')"
                v-model="form.description"
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
          <el-button type="primary" size="mini" @click="submitForm">{{
            td("common.button.confirm")
          }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 数据集成数据集成类目管理详情对话框 -->
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
      <el-form ref="AttTaskCatRef" :model="form" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.taskCat.categoryNameDetail')"
              prop="name"
             :label-position="labelPosition">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.setting.taskCat.parentId')"
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
              :label="td('dpp.setting.taskCat.categorySort')"
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
              :label="td('dpp.setting.taskCat.levelCode')"
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

    <!-- 用户导入对话框 -->
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
                td("dpp.setting.taskCat.updateExistData")
              }}
            </div>
            <span>{{ td("common.upload.fileFormat") }}</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >{{ td("dpp.setting.taskCat.downloadTemplate") }}</el-link
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

<script setup name="TaskCat">
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

//  taskCat
import {
  listAttTaskCat,
  getAttTaskCat,
  delAttTaskCat,
  addAttTaskCat,
  updateAttTaskCat,
} from "@/api/att/cat/taskCat/taskCat";
import { getToken } from "@/utils/auth";
import useUserStore from "@/store/system/user";

const { td } = useDefaultLang();
const userStore = useUserStore();
const { proxy } = getCurrentInstance();

const AttTaskCatList = ref([]);

// 列显隐状态
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

// 列配置（使用计算属性，确保国际化文本能响应语言切换）
const columns = computed(() => [
  {
    key: 1,
    label: td("dpp.setting.taskCat.categoryNameDetail"),
    visible: columnVisible.value[1],
  },
  {
    key: 2,
    label: td("dpp.setting.taskCat.parentId"),
    visible: columnVisible.value[2],
  },
  {
    key: 3,
    label: td("dpp.setting.taskCat.categorySort"),
    visible: columnVisible.value[3],
  },
  {
    key: 4,
    label: td("common.texts.description"),
    visible: columnVisible.value[4],
  },
  {
    key: 5,
    label: td("dpp.setting.taskCat.levelCode"),
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
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const attTaskCatOptions = ref([]);
const isExpandAll = ref(false);
const refreshTable = ref(true);
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

/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/att/AttTaskCat/importData",
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
        message: td("dpp.setting.taskCat.nameRequired"),
        trigger: "blur",
      },
    ],
    parentId: [
      {
        required: true,
        message: td("dpp.setting.taskCat.parentRequired"),
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
/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

/** 查询数据集成数据集成类目管理列表 */
function getList() {
  loading.value = true;
  queryParams.value.projectId = userStore.projectId;
  queryParams.value.projectCode = userStore.projectCode;
  listAttTaskCat(queryParams.value).then((response) => {
    AttTaskCatList.value = proxy.handleTree(response.data, "id", "parentId");
    total.value = response.data.total;
    loading.value = false;

    attTaskCatOptions.value = [];
    const data = { id: 0, name: td('common.texts.topNode'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attTaskCatOptions.value.push(data);
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
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
    remark: null,
  };
  proxy.resetForm("AttTaskCatRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }

  open.value = true;
  title.value = td("dpp.setting.taskCat.addTaskCat", "新增数据集成类目");
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getAttTaskCat(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td("dpp.setting.taskCat.editTaskCat", "修改数据集成类目");
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttTaskCat(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td("dpp.setting.taskCat.taskCatDetail", "数据集成类目详情");
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["AttTaskCatRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateAttTaskCatd(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td("common.message.editSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        form.value.projectId = userStore.projectId;
        form.value.projectCode = userStore.projectCode;
        addAttTaskCatd(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td("common.message.addSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? td('dpp.setting.taskCat.enable') : td('dpp.setting.taskCat.disable');
  proxy.$modal
    .confirm(td('dpp.setting.taskCat.confirmChangeStatus', '', { status: text, name: row.name }))
    .then(function () {
      updateAttTaskCatd({ id: row.id, validFlag: row.validFlag })
        .then((response) => {
          proxy.$modal.msgSuccess(text + td('common.message.success'));
          getList();
        })
        .catch((err) => {
          row.validFlag = !row.validFlag;
        });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(
      td(
        "dpp.setting.taskCat.confirmDelete",
        '是否确认删除数据集成类目管理编号为"{id}"的数据项？'
      ).replace("{id}", _ids)
    )
    .then(function () {
      return delAttTaskCatd(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess"));
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "att/AttTaskCat/export",
    {
      ...queryParams.value,
    },
    `AttTaskCat_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = td('dpp.setting.taskCat.taskCatImport');
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `AttTaskCat_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alertd(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td('dpp.setting.taskCat.importResult'),
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
