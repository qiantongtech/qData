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

    <GuideTip tip-id="da/executeSqlQuery" />

    <div class="head-container">
      <div class="head-title">
        {{ nodeData.name !== "" ? nodeData.name : td('da.dataQuery.pageTitle') }}
      </div>
      <div class="head-btns">
        <div class="head-select">
          <!-- Dropdown select box attached after title -->
          <el-select v-model="queryParams.id" :placeholder="td('da.dataQuery.datasourcePlaceholder')" class="head-select-el" style="width: 300px;">
            <el-option v-for="item in TablesByDataSource" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </div>
        <el-button plain type="primary" size="small" @click="handleQuery" :loading="loadings">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
        </el-button>
        <el-button type="primary" size="small" @click="handleClear">{{ td('common.button.clear') }}</el-button>
      </div>
    </div>
    <el-container style="90%">
      <!-- Tree: data source -> table -> field -->
      <DeptTree :deptOptions="TablesByDataSource" :leftWidth="leftWidth" :placeholder="td('da.dataQuery.datasourceSearchPlaceholder')" ref="DeptTreeRef"
        @node-click="handleTreeNodeClick" @nodeload-click="loadTreeData" :loading="loading" />
      <el-main style="padding: 0;">
        <div class="pagecont-bottom" style="padding: 0;">
          <!-- SQL editor -->
          <Editor ref="editorRef" :model-value="queryParams.sqlText" @update:model-value="handleChange"
            @query="handleQuery" />
        </div>
      </el-main>
    </el-container>
    <TableInfoDialog :visible="dialogVisible" :title="td('da.dataQuery.resultTitle')" @update:visible="dialogVisible = $event"
      :queryParams="queryParams" :spl="spl" />
  </div>
</template>

<script setup name="DataQuery">
import useDefaultLang from "@/composables/useDefaultLang"
import { ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import Editor from "@/components/SqlEditor/editor/index1.vue";
import TableInfoDialog from ".//components/queryResult.vue";
import DeptTree from "@/components/DeptTree/lazy";
import {
  getColumnByAssetId,
} from '@/api/dpp/task/index.js';
import {
  listDaDatasource
} from '@/api/da/dataSource/dataSource';
import {
  tableList,
  getDaDatasourceList,
  columnsList,
} from "@/api/dp/model/model";
import { executeSqlQuery } from "@/api/da/dataSource/dataSource";
import { encrypt } from "@/utils/aesEncrypt";

const { td } = useDefaultLang();
const leftWidth = ref(300);
const loading = ref(false);
const dialogVisible = ref(false);

const queryParams = ref({
  pageNum: 1,
  pageSize: 20,
  sqlText: "",
  id: "", // Selected data source ID for querying
  datasourceType: '',
});
let spl = ref('')

const TablesByDataSource = ref([]); // Tree top level is data source node

const editorRef = ref(null);
const nodeData = ref({ name: "", taskConfig: {} });
// 1. Get data source list, construct tree root nodes
const getDatasourcesTree = async () => {
  loading.value = true;
  try {
    const res = await listDaDatasource({
      pageSize: 9999,
      datasourceType: "DM8,Oracle11,MySql,Oracle,Kingbase8,Hive,Doris,SQL_Server,SQL_Server2008,PostgreSQL",
    });
    TablesByDataSource.value = res.data.rows.map((ds) => ({
      id: ds.id,
      name: ds.datasourceName,
      isLeaf: false,
      children: [],
      dbname: ds?.datasourceConfig && ds.datasourceConfig.trim()
        ? JSON.parse(ds.datasourceConfig).dbname
        : undefined,
      sid: ds?.datasourceConfig && ds.datasourceConfig.trim()
        ? JSON.parse(ds.datasourceConfig).sid
        : undefined,
      datasourceType: ds.datasourceType,
      level: 1
    }));
    console.log("🚀 ~ getDatasourcesTree ~ res.data.rows:", res.data.rows)
  } finally {
    loading.value = false;
  }
};

// 2. Lazy load tables or fields
const loadTreeData = async (node, resolveSuccess, resolveFail) => {
  console.log("🚀 ~ loadTreeData ~ node:", node)
  if (!node) {
    return resolveSuccess([]);
  }
  try {
    if (node.level == 1) {
      try {
        queryParams.value.id = node?.data?.id
        const res = await tableList(node.data.id);
        const tables = res.data.map((table) => ({
          id: table.tableName,
          name: table.tableName,
          isLeaf: false,
          children: [],
          level: 2,
          datasourceType: node.data.datasourceType
        }));
        resolveSuccess(tables);
      } catch (err) {
        resolveFail();
      }
    } else if (node.level == 2) {
      try {
        queryParams.value.id = node?.parent?.data?.id
        const parentDs = node.parent.data;
        const res = await getColumnByAssetId({
          id: parentDs.id,
          tableName: node.data.id,
        });
        const columns = res.data.map((col) => ({
          id: col.columnName,
          name: col.columnName,
          level: 3,
          isLeaf: true,
        }));
        resolveSuccess(columns);
      } catch (err) {
        resolveFail();
      }
    } else {
      console.log('3333');

      resolveSuccess([]);
    }
  } finally {

  }
};

// 3. Click tree node to insert SQL
function handleTreeNodeClick({ type, expandedRootId, payload, }, node) {
  queryParams.value.id = expandedRootId;
  if (type === 'node') {
    const data = payload;
    if (!data?.name) return;
    if (data.level === 1) {
      queryParams.value.datasourceType = data.datasourceType;
      return;
    }

    // Click table or field to insert corresponding name
    const currentSql = queryParams.value.sqlText || "";
    const suffix = currentSql.endsWith(" ") || currentSql === "" ? "" : " ";
    queryParams.value.sqlText = currentSql + suffix + data.name;

    if (data.level === 2) {
      queryParams.value.datasourceType = node.parent.data.datasourceType
    }

  } else if (type === 'sql') {
    const currentSql = queryParams.value.sqlText || "";
    const suffix = currentSql.endsWith(" ") || currentSql === "" ? "" : " ";
    queryParams.value.sqlText = currentSql + suffix + payload;
  }
}
// 4. Editor content change
function handleChange(val) {
  queryParams.value.sqlText = val;
}
let DeptTreeRef = ref()
let loadings = ref()

// 5. Query button
async function handleQuery() {
  spl.value = editorRef.value?.getEditorSelectedOrAll();
  console.log("🚀 ~ handleQuery ~ spl:", spl)

  const errors = [];
  if (!spl.value) {
    errors.push(td('da.dataQuery.sqlRequired'));
  }

  // Validate data source ID
  if (!queryParams.value.id) {
    errors.push(td('da.dataQuery.datasourceRequired'));
  }
  if (errors.length) {
    ElMessage.warning(errors.join("，"));
    return;
  }
  // Execute SQL query
  dialogVisible.value = false;
  try {
    loadings.value = true;
    const res = await executeSqlQuery({
      ...queryParams.value,
      sqlText: encrypt(spl.value),
    });
    // sqlText: encrypt(spl),
    if (res.code === 200) {
      dialogVisible.value = true;
    } else {
      ElMessage.warning(res.msg || td('da.dataQuery.queryFailed'));
    }
  } finally {
    loadings.value = false;
  }
}

// 6. Clear editor
function handleClear() {
  queryParams.value.sqlText = "";
}

getDatasourcesTree();
</script>

<style scoped lang="less">
.app-container {
  height: calc(87vh - 300px);

  .head-container {
    height: 50px;
    background: #fff;
    box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
    padding: 0 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    .head-title {
      font-size: 16px;
      color: var(--el-color-primary);
      display: flex;
      align-items: center;

      &::before {
        content: "";
        display: inline-block;
        background: var(--el-color-primary);
        width: 6px;
        height: 16px;
        border-radius: 2px;
        margin-right: 10px;
      }
    }

    .head-btns {
      display: flex;
      align-items: center;

      .head-select {
        margin-right: 10px;
        padding: 10px 0;
      }

      .el-button {
        height: 28px;
        margin-left: 5px;
      }
    }
  }
}

.sql-editor {
  width: 100%;
}
</style>
