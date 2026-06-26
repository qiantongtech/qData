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
  <el-dialog :title="td('ds.client.singleTitle')" v-model="visible" width="1200px" :append-to="$refs['app-container']" draggable
    destroy-on-close @close="cancel">
    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
       :label-position="labelPosition">
      <el-form-item label="ID" prop="id" :label-position="labelPosition">
        <el-input style="width:240px" v-model="queryParams.id" :placeholder="td('ds.client.idPlaceholder')" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item :label="td('ds.client.appName')" prop="name" :label-position="labelPosition">
        <el-input style="width:240px" v-model="queryParams.name" :placeholder="td('ds.client.appNamePlaceholder')" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item :label="td('ds.client.appType')" prop="type" :label-position="labelPosition">
        <el-select style="width:240px" v-model="queryParams.type" :placeholder="td('ds.client.appTypePlaceholder')" clearable>
          <el-option v-for="dict in auth_app_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item :label="td('ds.client.appSecret')" prop="secret" :label-position="labelPosition">
        <el-input style="width:240px" v-model="queryParams.secret" :placeholder="td('ds.client.appSecretPlaceholder')" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item :label="td('ds.client.homepageUrl')" prop="homepageUrl" :label-position="labelPosition">
        <el-input style="width:240px" v-model="queryParams.homepageUrl" :placeholder="td('ds.client.homepageUrlPlaceholder')" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item :label="td('ds.client.syncUrl')" prop="syncUrl" :label-position="labelPosition">
        <el-input style="width:240px" v-model="queryParams.syncUrl" :placeholder="td('ds.client.syncUrlPlaceholder')" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item :label="td('ds.client.isPublic')" prop="publicFlag" :label-position="labelPosition">
        <el-select style="width:240px" v-model="queryParams.publicFlag" :placeholder="td('ds.client.isPublicPlaceholder')" clearable>
          <el-option v-for="dict in auth_public" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item :label="td('common.texts.createdTime')" prop="createTime" :label-position="labelPosition">
        <el-date-picker style="width:240px" clearable v-model="queryParams.createTime" type="date"
          value-format="YYYY-MM-DD" :placeholder="td('ds.client.createTimePlaceholder')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label-position="labelPosition">
        <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-table ref="tableRef" stripe height="300px" v-loading="loading" :data="dataList" highlight-current-row
      row-key="id" @current-change="handleCurrentChange">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column :label="td('ds.client.appName')" align="center" prop="name">
        <template #default="scope">
          {{ scope.row.name || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.appType')" align="center" prop="type">
        <template #default="scope">
          <dict-tag :options="auth_app_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.authPath')" align="center" prop="allowUrl">
        <template #default="scope">
          {{ scope.row.allowUrl || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.syncUrl')" align="center" prop="syncUrl">
        <template #default="scope">
          {{ scope.row.syncUrl || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.appIcon')" align="center" prop="logo" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.logo" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.description')" align="center" prop="description">
        <template #default="scope">
          {{ scope.row.description || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.isPublic')" align="center" prop="publicFlag">
        <template #default="scope">
          <dict-tag :options="auth_public" :value="scope.row.publicFlag" />
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.createdBy')" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.remark')" align="center" prop="remark">
        <template #default="scope">
          {{ scope.row.remark || '-' }}
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" size="mini" @click="confirm">
          {{ td('common.button.confirm') }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ClientSingle">
import useDefaultLang from "@/composables/useDefaultLang";
import { listClient } from "@/api/ds/client/client";
import { ref } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const { auth_public, auth_app_type } = proxy.useDict('auth_public', 'auth_app_type');

const dataList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
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
  }
});
const { queryParams, form } = toRefs(data);

// -------------------------------------------
const visible = ref(false);
// 定义单选数据
const single = ref();
// 当前界面table
const tableRef = ref();

const emit = defineEmits(["open", "confirm", "cancel"]);

/** 单选选中事件 */
function handleCurrentChange(selection) {
  if (selection) {
    single.value = selection;
  }
}

/**
 * 设置当前行
 * @param {Object} row 行对象
 * @returns 更改选中对象
 */
function setCurrentRow(row) {
  if (row) {
    let data = dataList.value.filter((item) => item.id == row.id);
    tableRef.value?.setCurrentRow(data[0]);
  }
}

/**
 * 打开选择框
 * @param {Array} val 选中的对象数组
 */
function open(val) {
  visible.value = true;
  single.value = val;
  resetQuery();
  getList();
}

/**
 * 取消按钮
 * @description 取消按钮时，重置所有状态
 */
function cancel() {
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  visible.value = false;
}

/**
 * 确定按钮
 * @description 确定按钮时，emit confirm 事件，以便父组件接收到选中的数据
 */
function confirm() {
  if (!single.value) {
    proxy.$modal.msgWarning(td('ds.client.noDataSelected'));
    return;
  }
  emit("confirm", single.value);
  visible.value = false;
}

/** 查询字典类型列表 */
function getList() {
  loading.value = true;
  listClient(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
    async (response) => {
      dataList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
      // 初始化及分页切换选中逻辑
      await nextTick();
      setCurrentRow(single.value);
    }
  );
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.value.pageNum = 1;
  handleQuery();
}

defineExpose({ open });
</script>