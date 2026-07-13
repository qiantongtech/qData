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
  <!-- Import table -->
  <el-dialog :title="td('sys.tool.genImport.title')" v-model="visible" width="800px" top="5vh" :append-to="$refs['app-container']"  draggable destroy-on-close>
    <el-form :model="queryParams" ref="queryRef" :inline="true" :label-position="labelPosition">
      <el-form-item :label="td('sys.tool.genImport.tableName')" prop="tableName" :label-position="labelPosition">
        <el-input
          v-model="queryParams.tableName"
          :placeholder="td('sys.tool.genImport.tableNamePlaceholder')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label-position="labelPosition">
        <!-- <el-button type="primary" icon="Search" @click="handleQuery">Search</el-button>
        <el-button icon="Refresh" @click="resetQuery">{{ td('common.button.reset') }}</el-button> -->
        <el-button plain type="primary" @click="handleQuery">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
        </el-button>
        <el-button @click="resetQuery">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
        </el-button>
      </el-form-item>
    </el-form>
      <el-table @row-click="clickRow" ref="table" :data="dbTableList" @selection-change="handleSelectionChange" height="380px">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="tableName" :label="td('sys.tool.genImport.tableName')" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="tableComment" :label="td('sys.tool.genImport.tableDesc')" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="createTime" :label="td('common.texts.createdTime')"></el-table-column>
        <el-table-column prop="updateTime" :label="td('common.texts.updatedTime')"></el-table-column>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="handleImportTable">{{ td('common.button.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { listDbTable, importTable } from "@/api/system/tool/gen.js";

const { td } = useDefaultLang();
const total = ref(0);
const visible = ref(false);
const tables = ref([]);
const dbTableList = ref([]);
const { proxy } = getCurrentInstance();

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined
});

const emit = defineEmits(["ok"]);

/** Query parameter list */
function show() {
  getList();
  visible.value = true;
}

/** Click to select row */
function clickRow(row) {
  proxy.$refs.table.toggleRowSelection(row);
}

/** Multiple selection box selected data */
function handleSelectionChange(selection) {
  tables.value = selection.map(item => item.tableName);
}

/** Query table data */
function getList() {
  listDbTable(queryParams).then(res => {
    dbTableList.value = res.rows;
    total.value = res.total;
  });
}

/** Search button action */
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}

/** reset button action */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** Import button actions */
function handleImportTable() {
  const tableNames = tables.value.join(",");
  if (tableNames == "") {
    proxy.$modal.msgError(td('sys.tool.genImport.selectTableToImport'));
    return;
  }
  importTable({ tables: tableNames }).then(res => {
    proxy.$modal.msgSuccess(res.msg);
    if (res.code === 200) {
      visible.value = false;
      emit("ok");
    }
  });
}

defineExpose({
  show,
});
</script>
