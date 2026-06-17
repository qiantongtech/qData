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

    <GuideTip tip-id="att/client.list" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
        v-show="showSearch" @submit.prevent>
        <!-- <el-form-item :label="td('ds.client.detail.id')" prop="id" :label-position="labelPosition">
          <el-input class="el-form-input-width" v-model="queryParams.id" placeholder="请输入编号" clearable
            @keyup.enter="handleQuery" />
        </el-form-item> -->
        <el-form-item :label="td('ds.client.appName')" prop="name" :label-position="labelPosition">
          <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('ds.client.appNamePlaceholder')" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item :label="td('ds.client.appType')" prop="type" :label-position="labelPosition">
          <el-select class="el-form-input-width" v-model="queryParams.type" :placeholder="td('ds.client.appTypePlaceholder')" clearable>
            <el-option v-for="dict in auth_app_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag" :label-position="labelPosition">
          <el-select class="el-form-input-width" v-model="queryParams.publicFlag" :placeholder="td('ds.client.isPublicPlaceholder')" clearable>
            <el-option v-for="dict in auth_public" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" v-hasPermi="['att:client:query']" @click="handleQuery"
            @mousedown="(e) => e.preventDefault()">
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:client:add']"
              @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
          <!--         <el-col :span="1.5">
           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['att:client:edit']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['att:client:remove']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="info" plain  @click="handleImport" v-hasPermi="['att:client:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="warning" plain @click="handleExport" v-hasPermi="['att:client:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-download-line mr5"></i>导出
           </el-button>
         </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="clientList" @selection-change="handleSelectionChange"
        :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column v-if="getColumnVisibility(0)" width="50" :label="td('common.texts.number')"  align="center" prop="id" />
        <el-table-column v-if="getColumnVisibility(1)" width="200" :label="td('ds.client.appName')"
          :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" :label="td('ds.client.appType')" align="center" prop="type">
          <template #default="scope">
            <dict-tag :options="auth_app_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" :show-overflow-tooltip="{ effect: 'light' }" :label="td('common.texts.description')"
          align="left" prop="description" width="300">
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(16)" :label="td('ds.client.appIcon')"
          :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
          <template #default="scope">
            <div class="clientInfo">
              <div>
                <image-preview :src="scope.row.logo || noDataImg" :width="50" :height="50" />

              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column width="100" v-if="getColumnVisibility(4)" :label="td('ds.client.isPublic')" align="center" prop="publicFlag">
          <template #default="scope">
            <dict-tag :options="auth_public" :value="scope.row.publicFlag" />
          </template>
        </el-table-column>
        <!--       <el-table-column v-if="getColumnVisibility(5)" label="允许授权的url" align="center" prop="allowUrl">
         <template #default="scope">
           {{ scope.row.allowUrl || '-' }}
         </template>
       </el-table-column>-->
        <!--       <el-table-column v-if="getColumnVisibility(6)" label="同步地址" align="center" prop="syncUrl">
         <template #default="scope">
           {{ scope.row.syncUrl || '-' }}
         </template>
       </el-table-column>-->
        <!--       <el-table-column v-if="getColumnVisibility(7)" label="应用图标" align="center" prop="logo" width="100">
         <template #default="scope">
           <image-preview :src="scope.row.logo" :width="50" :height="50"/>
         </template>
       </el-table-column>-->

        <el-table-column v-if="getColumnVisibility(12)" :label="td('common.texts.createdBy')" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(14)" :label="td('common.texts.createdTime')" align="center" prop="createTime" width="150"
          sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']"> <template
            #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }"
          v-if="getColumnVisibility(15)">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="280">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['att:client:edit']">{{td('common.button.update')}}</el-button>
            <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
              v-hasPermi="['att:client:query']">{{td('common.button.details')}}</el-button>
            <el-popover placement="bottom" :width="150" trigger="click">
              <template #reference>
                <el-button link type="primary" icon="ArrowDown">{{td('common.button.more')}}</el-button>
              </template>
              <div style="width: 100px" class="butgdlist">
                <el-button link style="padding-left: 14px" type="primary" icon="Refresh" @click="handleReset(scope.row)"
                  v-hasPermi="['att:client:edit']">{{ td('ds.client.resetSecret') }}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['att:client:remove']">{{td('common.button.delete')}}</el-button>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt="" />
            <p>{{td('common.noData')}}</p>
          </div>
        </template>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </div>

    <!-- 新增或修改应用对话框 -->
    <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="clientRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appName')" prop="name" :label-position="labelPosition">
              <el-input v-model="form.name" :placeholder="td('ds.client.appNamePlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appType')" prop="type" :label-position="labelPosition">
              <el-select v-model="form.type" :placeholder="td('ds.client.appTypePlaceholder')">
                <el-option v-for="dict in auth_app_type" :key="dict.value" :label="dict.label"
                           :value="dict.value"></el-option>
              </el-select>
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

        <!-- <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.homepageUrl')" prop="homepageUrl" :label-position="labelPosition">
              <el-input v-model="form.homepageUrl" placeholder="请输入主页地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.syncUrl')" prop="syncUrl" :label-position="labelPosition">
              <el-input v-model="form.syncUrl" placeholder="请输入同步地址" />
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('ds.client.appIcon')" prop="logo" :label-position="labelPosition">
              <image-upload v-model="form.logo" limit="1" :fileType="pdf" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag" :label-position="labelPosition">
              <el-radio-group v-model="form.publicFlag">
                <el-radio v-for="dict in auth_public" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{td('common.button.cancel')}}</el-button>
          <el-button type="primary" size="mini" @click="submitForm">{{td('common.button.confirm')}}</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 应用详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="clientRef" :model="form" label-width="100px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.detail.id')" prop="id">
              <div>{{ form.id || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appSecret')" prop="secret" :label-position="labelPosition">
              <div>{{ form.secret || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appName')" prop="name">
              <div>{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appIcon')" prop="logo" :label-position="labelPosition">
              <image-preview :src="form.logo || noDataImg" :width="50" :height="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.appType')" prop="type">
              <dict-tag :options="auth_app_type" :value="form.type" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag">
              <dict-tag :options="auth_public" :value="form.publicFlag" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.homepageUrl')" prop="homepageUrl" :label-position="labelPosition">
              <div>{{ form.homepageUrl || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('ds.client.syncUrl')" prop="syncUrl" :label-position="labelPosition">
              <div>{{ form.syncUrl || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('ds.client.authPath')" prop="allowUrl" :label-position="labelPosition">
              <div>{{ form.allowUrl || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <div>{{ form.description || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{td('common.button.close')}}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text" v-html="td('common.upload.dragOrClick')"></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('ds.client.importTip') }}
            </div>
            <span>{{ td('common.upload.fileFormat') }}</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">{{ td('common.upload.downloadTemplate') }}</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{td('common.button.cancel')}}</el-button>
          <el-button type="primary" @click="submitFileForm">{{td('common.button.confirm')}}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Client">
import {
  listClient,
  getClient,
  delClient,
  addClient,
  updateClient,
  resetSecret,
} from "@/api/ds/client/client";
import { getToken } from "@/utils/auth.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { auth_public, auth_app_type } = proxy.useDict(
  "auth_public",
  "auth_app_type"
);
const noDataImg = new URL('../../../assets/system/images/D.png', import.meta.url).href
const clientList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: td('common.texts.number'), visible: true },
  { key: 1, label: td('ds.client.appName'), visible: true },
  { key: 3, label: td('ds.client.appType'), visible: true },
  { key: 2, label: td('common.texts.description'), visible: true },
  { key: 16, label: td('ds.client.appIcon'), visible: true },
  { key: 4, label: td('ds.client.isPublic'), visible: true },
  { key: 12, label: td('common.texts.createdBy'), visible: true },
  { key: 14, label: td('common.texts.createdTime'), visible: true },
  { key: 15, label: td('common.texts.remark'), visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
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
  url: import.meta.env.VITE_APP_BASE_API + "/att/client/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: null,
    name: null,
    type: null,
    secret: null,
    homepageUrl: null,
    allowUrl: null,
    syncUrl: null,
    logo: null,
    description: null,
    publicFlag: null,
    createTime: null,
  },
  rules: {
    name: [{ required: true, message: td('ds.client.appNameRequired'), trigger: "blur" }],
    type: [{ required: true, message: td('ds.client.appTypeRequired'), trigger: "change" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询应用列表 */
function getList() {
  loading.value = true;
  listClient(queryParams.value).then((response) => {
    clientList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
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
    type: null,
    secret: null,
    homepageUrl: null,
    allowUrl: null,
    syncUrl: null,
    logo: null,
    description: null,
    publicFlag: null,
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
  proxy.resetForm("clientRef");
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
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('ds.client.addApp');

  data.form.publicFlag = "1";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getClient(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('ds.client.editApp');
  });
}

/** 重置秘钥按钮操作 */
function handleReset(row) {
  const _id = row.id || ids.value;

  proxy.$modal
    .confirm(td('ds.client.resetSecretConfirm'))
    .then(function () {
      resetSecret(_id).then((res) => {
        proxy.$modal.msgSuccess(td('ds.client.newSecret') + res.data);
        getList();
      });
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
  // reset();
  // const _id = row.id || ids.value;
  // getClient(_id).then((response) => {
  //   form.value = response.data;
  //   openDetail.value = true;
  //   title.value = "应用详情";
  // });
  routeTo("/ds/client/clientDetail", row);
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["clientRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateClient(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.client.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addClient(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('ds.client.addSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => { });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('ds.client.deleteConfirm') + _ids + td('ds.client.deleteConfirmSuffix'))
    .then(function () {
      return delClient(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "att/client/export",
    {
      ...queryParams.value,
    },
    `client_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = td('ds.client.importTitle');
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `client_template_${new Date().getTime()}.xlsx`
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
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    td('ds.client.importResult'),
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
</script>

<style scoped lang="scss">
.clientInfo {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

// :deep {
//   .el-popper.is-dark {
//     max-width: 900px !important;
//     max-height: 400px;
//     font-size: 14px;
//     text-align: start;
//   }
// }</style>