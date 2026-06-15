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
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="td('sys.tool.genIndex.tableName')" prop="tableName">
          <el-input
            v-model="queryParams.tableName"
            :placeholder="td('sys.tool.genIndex.tableNamePlaceholder')"
            clearable
            class="el-form-input-width"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('sys.tool.genIndex.tableDesc')" prop="tableComment">
          <el-input
            v-model="queryParams.tableComment"
            :placeholder="td('sys.tool.genIndex.tableDescPlaceholder')"
            clearable
            class="el-form-input-width"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.createdTime')">
          <el-date-picker
            class="el-form-input-width"
            v-model="dateRange"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            :start-placeholder="td('common.form.startDatePlaceholder')"
            :end-placeholder="td('common.form.endDatePlaceholder')"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
              </el-button>
              <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
              </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div  class="pagecont-bottom">

      <div class="justify-between mb15">
      <el-row :gutter="10" class="btn-style">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Download"
            :disabled="multiple"
            @click="handleGenTable"
            v-hasPermi="['tool:gen:code']"
          >{{ td('sys.tool.genIndex.generate') }}</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Plus"
            @click="openCreateTable"
            v-hasRole="['admin']"
          >{{ td('sys.tool.genIndex.create') }}</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="info"
            plain
            icon="Upload"
            @click="openImportTable"
            v-hasPermi="['tool:gen:import']"
          >{{ td('common.button.import') }}</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleEditTable"
            v-hasPermi="['tool:gen:edit']"
          >{{ td('common.button.update') }}</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['tool:gen:remove']"
          >{{ td('common.button.delete') }}</el-button>
        </el-col>
      </el-row>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <el-table-column :label="td('sys.tool.genIndex.index')" type="index" width="80" align="center">
        <template #default="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="td('sys.tool.genIndex.tableName')"
        align="center"
        prop="tableName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="td('sys.tool.genIndex.tableDesc')"
        align="center"
        prop="tableComment"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="td('sys.tool.genIndex.entity')"
        align="center"
        prop="className"
        :show-overflow-tooltip="true"
      />
      <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="160" />
      <el-table-column :label="td('common.texts.updatedTime')" align="center" prop="updateTime" width="160" />
      <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width"  fixed="right" width="240">
        <template #default="scope">
          <el-tooltip :content="td('sys.tool.genIndex.preview')" placement="top">
            <el-button link type="primary" icon="View" @click="handlePreview(scope.row)" v-hasPermi="['tool:gen:preview']"></el-button>
          </el-tooltip>
          <el-tooltip :content="td('sys.tool.genIndex.edit')" placement="top">
            <el-button link type="primary" icon="Edit" @click="handleEditTable(scope.row)" v-hasPermi="['tool:gen:edit']"></el-button>
          </el-tooltip>
          <el-tooltip :content="td('common.button.delete')" placement="top">
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['tool:gen:remove']"></el-button>
          </el-tooltip>
<!--          <el-tooltip content="同步" placement="top">-->
<!--            <el-button link type="primary" icon="Refresh" @click="handleSynchDb(scope.row)" v-hasPermi="['tool:gen:edit']"></el-button>-->
<!--          </el-tooltip>-->
            <el-tooltip :content="td('sys.tool.genIndex.genCode')" placement="top">
              <el-button link type="primary" icon="Download" @click="handleGenTable(scope.row)" v-hasPermi="['tool:gen:code']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <!-- 预览界面 -->
    <el-dialog :title="preview.title" v-model="preview.open" width="80%" top="5vh" :append-to="$refs['app-container']" class="scrollbar" draggable destroy-on-close>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :key="value"
        >
          <!-- <div class="justify-between mb15">
              <div class="justify-end top-right-btn">
                  <el-link :underline="false" icon="DocumentCopy" v-copyText="value" v-copyText:callback="copyTextSuccess" style="float:right">&nbsp;{{ td('common.button.copy') }}</el-link>
              </div>
          </div> -->
          <div class="precont">
            <el-link :underline="false" icon="DocumentCopy" v-copyText="value" v-copyText:callback="copyTextSuccess" style="float:right">&nbsp;{{ td('common.button.copy') }}</el-link>
            <pre >{{ value }}</pre>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <import-table ref="importRef" @ok="handleQuery" />
    <create-table ref="createRef" @ok="handleQuery" />
  </div>
</template>

<script setup name="Gen">
import { listTable, previewTable, delTable, genCode, synchDb } from "@/api/system/tool/gen.js";
import router from "@/router/index.js";
import importTable from "./importTable.vue";
import createTable from "./createTable.vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const route = useRoute();
const { proxy } = getCurrentInstance();

const tableList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const tableNames = ref([]);
const dateRange = ref([]);
const uniqueId = ref("");

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tableName: undefined,
    tableComment: undefined
  },
  preview: {
    open: false,
    title: td('sys.tool.genIndex.codePreview'),
    data: {},
    activeName: "do.java"
  }
});

const { queryParams, preview } = toRefs(data);

onActivated(() => {
  const time = route.query.t;
  if (time != null && time != uniqueId.value) {
    uniqueId.value = time;
    queryParams.value.pageNum = Number(route.query.pageNum);
    dateRange.value = [];
    proxy.resetForm("queryForm");
    getList();
  }
})

/** 查询表集合 */
function getList() {
  loading.value = true;
  listTable(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    tableList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 生成代码操作 */
function handleGenTable(row) {
  const tbNames = row.tableName || tableNames.value;
  if (tbNames == "") {
    proxy.$modal.msgError(td('sys.tool.genIndex.selectDataToGenerate'));
    return;
  }
  if (row.genType === "1") {
    genCode(row.tableName).then(response => {
      proxy.$modal.msgSuccess(td('sys.tool.genIndex.genSuccessToCustomPath', { path: row.genPath }));
    });
  } else {
    proxy.$download.zip("/tool/gen/batchGenCode?tables=" + tbNames, "anivia.zip");
  }
}

/** 同步数据库操作 */
function handleSynchDb(row) {
  const tableName = row.tableName;
  proxy.$modal.confirm(td('sys.tool.genIndex.confirmSyncTable', { name: tableName })).then(function () {
    return synchDb(tableName);
  }).then(() => {
    proxy.$modal.msgSuccess(td('sys.tool.genIndex.syncSuccess'));
  }).catch(() => {});
}

/** 打开导入表弹窗 */
function openImportTable() {
  proxy.$refs["importRef"].show();
}

/** 打开创建表弹窗 */
function openCreateTable() {
  proxy.$refs["createRef"].show();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 预览按钮 */
function handlePreview(row) {
  previewTable(row.tableId).then(response => {
    preview.value.data = response.data;
    preview.value.open = true;
    preview.value.activeName = "do.java";
  });
}

/** 复制代码成功 */
function copyTextSuccess() {
  proxy.$modal.msgSuccess(td('common.message.copySuccess'));
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.tableId);
  tableNames.value = selection.map(item => item.tableName);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 修改按钮操作 */
function handleEditTable(row) {
  const tableId = row.tableId || ids.value[0];
  router.push({ path: "/tool/gen-edit/index/" + tableId, query: { pageNum: queryParams.value.pageNum } });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const tableIds = row.tableId || ids.value;
  proxy.$modal.confirm(td('sys.tool.genIndex.confirmDelete', { ids: tableIds })).then(function () {
    return delTable(tableIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
  }).catch(() => {});
}

getList();
</script>
<style scoped>
.precont{
  padding: 0px 15px;
  height: 444px;
  overflow-y: auto;
}
</style>
