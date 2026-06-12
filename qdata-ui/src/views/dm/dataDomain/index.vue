<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
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
          {{ td('common.button.add', '新增') }}
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
            {{ td('common.button.update', '修改') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['dm:dataDomain:remove']"
          >
            {{ td('common.button.delete', '删除') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(row)"
            v-hasPermi="['dm:dataDomain:edit']"
          >
            {{ td('common.button.details', '详情') }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- 添加或修改数据域管理对话框 -->
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
      >
        <el-form-item :label="td('dm.dataDomain.name', '数据域名称')" prop="name">
          <el-input v-model="form.name" :placeholder="td('dm.dataDomain.namePlaceholder', '请输入数据域名称')" />
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.engName', '英文缩写')" prop="engName">
          <el-input v-model="form.engName" :placeholder="td('dm.dataDomain.engNamePlaceholder', '请输入英文缩写')" />
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerId', '负责人')" prop="ownerUserId">
          <el-select
            v-model="form.ownerUserId"
            filterable
            :placeholder="td('dm.dataDomain.ownerIdPlaceholder', '请选择负责人')"
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
        <el-form-item :label="td('dm.dataDomain.ownerPhone', '负责人电话')" prop="ownerUserPhoneNumber">
          <el-input
            v-model="form.ownerUserPhoneNumber"
            :placeholder="td('dm.dataDomain.ownerPhonePlaceholder', '请输入负责人电话')"
            disabled
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.description', '描述')" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder', '请输入描述')"
            :min-height="192"
            show-word-limit
            maxlength="500"
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.remark', '备注')" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder', '请输入备注')"
            :min-height="192"
            show-word-limit
            maxlength="500"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel', '取消') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ td('common.button.confirm', '确定') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 数据域管理详情对话框 -->
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
      <el-form ref="dataDomainDetailRef" :model="form" label-width="110px">
        <el-form-item :label="td('common.texts.number', '编号') + ':'" prop="id">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.dataDomain', '数据域')" prop="name">
          <div class="form-readonly">{{ form.name ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.engName', '英文缩写')" prop="engName">
          <div class="form-readonly">{{ form.engName ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerId', '负责人')" prop="ownerUserId">
          <div class="form-readonly">{{ form.ownerUserName || "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dm.dataDomain.ownerPhone', '负责人电话')" prop="ownerUserPhoneNumber">
          <div class="form-readonly">
            {{ form.ownerUserPhoneNumber || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.description', '描述')" prop="description">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark', '备注')" prop="remark">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.createdBy', '创建人')" prop="createBy">
              <div class="form-readonly">
                {{ form.createBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.createdTime', '创建时间')" prop="createTime">
              <div class="form-readonly">
                {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.updatedBy', '更新人')" prop="createBy">
              <div class="form-readonly">
                {{ form.updateBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.updatedTime', '更新时间')" prop="updateTime">
              <div class="form-readonly">
                {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.close', '关闭') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
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
        <div class="el-upload__text">{{ td('common.upload.dragOrClick', '将文件拖到此处，或点击上传') }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox
                v-model="upload.updateSupport"
              />{{ td('dm.dataDomain.updateExistingData', '是否更新已经存在的数据域数据') }}
            </div>
            <span>{{ td('common.upload.fileFormat', '仅允许导入xls、xlsx格式文件。') }}</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
            >
              {{ td('common.upload.downloadTemplate', '下载模板') }}
            </el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel', '取消') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm', '确定') }}</el-button>
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
    { label: td('common.texts.number', '编号'), prop: "id", width: 60, sortable: true },
    {
      label: td('dm.dataDomain.name', '数据域名称'),
      prop: "name",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td('common.texts.description', '描述'),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    { label: td('dm.dataDomain.engName', '英文缩写'), prop: "engName", align: "left" },
    { label: td('dm.dataDomain.ownerId', '负责人'), prop: "ownerUserName", align: "left" },
    {
      label: td('dm.dataDomain.ownerPhone', '负责人电话'),
      prop: "ownerUserPhoneNumber",
      align: "left",
      width: 140,
    },
    {
      label: td('common.texts.createdBy', '创建人'),
      prop: "createBy",
      showOverflowTooltip: true,
    },
    {
      label: td('common.texts.createdTime', '创建时间'),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 150,
      align: "left",
    },
    // { label: t('common.texts.remark'), prop: "remark", align: "left" },
    {
      label: td('common.texts.operation', '操作'),
      width: 220,
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
      label: td('dm.dataDomain.name', '数据域名称'),
      prop: "name",
      component: { is: "input", placeholder: td('dm.dataDomain.namePlaceholder', '请输入数据域名称') },
    },
    {
      label: td('dm.dataDomain.engName', '英文缩写'),
      prop: "engName",
      component: { is: "input", placeholder: td('dm.dataDomain.engNamePlaceholder', '请输入英文缩写') },
    },
    {
      label: td('dm.dataDomain.ownerId', '负责人'),
      prop: "ownerUserId",
      component: {
        is: "tree-select",
        data: managerOptions,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "userId",
        placeholder: td('dm.dataDomain.ownerIdPlaceholder', '请选择负责人'),
        checkStrictly: true,
      },
    },
  ],
});

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
  url: import.meta.env.VITE_APP_BASE_API + "/dm/dataDomain/importData",
});

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('dm.dataDomain.nameRequired', '数据分域名称不能为空'), trigger: "blur" }],
    engName: [
      { required: true, message: td('dm.dataDomain.engNameRequired', '英文缩写不能为空'), trigger: "blur" },
      { pattern: /^[a-zA-Z]+$/, message: td('dm.dataDomain.englishOnly', '只能输入英文字符'), trigger: "blur" },
    ],
    ownerUserId: [
      { required: true, message: td('dm.dataDomain.ownerRequired', '负责人不能为空'), trigger: "blur" },
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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  getManagerOptions();
  // 显式初始化负责人电话字段
  form.value.ownerUserPhoneNumber = null;
  open.value = true;
  title.value = td('dm.dataDomain.addTitle', '新增数据域');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getManagerOptions();
  const _id = row?.id || ids.value[0];
  getDataDomain(_id).then((response) => {
    form.value = response.data;

    open.value = true;
    title.value = td('dm.dataDomain.editTitle', '修改数据域');
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  getManagerOptions();
  const _id = row?.id || ids.value[0];
  getDataDomain(_id).then((response) => {
    form.value = response.data;

    openDetail.value = true;
    title.value = td('dm.dataDomain.detailTitle', '数据域详情');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dataDomainRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDataDomain(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess', '修改成功'));
            open.value = false;
            tableRef.value.getList();
          })
          .catch(() => {});
      } else {
        addDataDomain(form.value)
          .then(() => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess', '新增成功'));
            open.value = false;
            tableRef.value.getList();
          })
          .catch(() => {});
      }
    }
  });
}

// 当负责人改变时，更新电话号码
const handleContactChange = (selectedValue) => {
  const selectedUser = managerOptions.value.find(
    (user) => user.userId == selectedValue
  );
  form.value.ownerUserPhoneNumber = selectedUser?.phonenumber || "";
};
/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row?.id || ids.value;
  proxy.$modal
    .confirm(td('dm.dataDomain.confirmDelete', '是否确认删除数据域编号为"{id}"的数据项？').replace('{id}', _ids))
    .then(function () {
      return delDataDomain(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', '删除成功'));
    })
    .catch(() => {});
}

/** ---------------- 导入相关操作 -----------------**/
/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `dataDomain_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td('dm.dataDomain.importResult', '导入结果'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.getList();
};
/** ---------------------------------**/
</script>