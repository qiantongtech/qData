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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="da/executeSqlQuery" />

    <div class="head-container">
      <div class="head-title">
        {{ nodeData.name !== "" ? nodeData.name : "数据查询" }}
      </div>
      <div class="head-btns">
        <div class="head-select">
          <!-- 标题后附加下拉选择框 -->
          <el-select v-model="queryParams.id" placeholder="请选择数据源" class="head-select-el" style="width: 300px;">
            <el-option v-for="item in TablesByDataSource" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </div>
        <el-button plain type="primary" size="small" @click="handleQuery" :loading="loadings">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ t('common.button.query') }}
        </el-button>
        <el-button type="primary" size="small" @click="handleClear">{{ t('common.button.clear') }}</el-button>
      </div>
    </div>
    <el-container style="90%">
      <!-- 树：数据源->表->字段 -->
      <DeptTree :deptOptions="TablesByDataSource" :leftWidth="leftWidth" :placeholder="'请输入数据源名称'" ref="DeptTreeRef"
        @node-click="handleTreeNodeClick" @nodeload-click="loadTreeData" :loading="loading" />
      <el-main style="padding: 0;">
        <div class="pagecont-bottom" style="padding: 0;">
          <!-- SQL 编辑器 -->
          <Editor ref="editorRef" :model-value="queryParams.sqlText" @update:model-value="handleChange"
            @query="handleQuery" />
        </div>
      </el-main>
    </el-container>
    <TableInfoDialog :visible="dialogVisible" title="查询结果" @update:visible="dialogVisible = $event"
      :queryParams="queryParams" :spl="spl" />
  </div>
</template>

<script setup name="DataQuery">
import { useI18n } from 'vue-i18n'
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

const { t } = useI18n();
const leftWidth = ref(300);
const loading = ref(false);
const dialogVisible = ref(false);

const queryParams = ref({
  pageNum: 1,
  pageSize: 20,
  sqlText: "",
  id: "", // 选中数据源 id，用于查询
  datasourceType: '',
});
let spl = ref('')

const TablesByDataSource = ref([]); // 树顶级为数据源节点

const editorRef = ref(null);
const nodeData = ref({ name: "", taskConfig: {} });
// 1. 获取数据源列表，构造树根节点（数据源）
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

// 2. 懒加载表或字段
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

// 3. 点击树节点插入SQL
function handleTreeNodeClick({ type, expandedRootId, payload, }, node) {
  queryParams.value.id = expandedRootId;
  if (type === 'node') {
    const data = payload;
    if (!data?.name) return;
    if (data.level === 1) {
      queryParams.value.datasourceType = data.datasourceType;
      return;
    }

    // 点击表或字段插入对应名字
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
// 4. 编辑器内容变化
function handleChange(val) {
  queryParams.value.sqlText = val;
}
let DeptTreeRef = ref()
let loadings = ref()

// 5. 查询按钮
async function handleQuery() {
  spl.value = editorRef.value?.getEditorSelectedOrAll();
  console.log("🚀 ~ handleQuery ~ spl:", spl)

  const errors = [];
  if (!spl.value) {
    errors.push("请输入 SQL 语句！");
  }

  // 校验数据源 id
  if (!queryParams.value.id) {
    errors.push("请选择数据源！");
  }
  if (errors.length) {
    ElMessage.warning(errors.join("，"));
    return;
  }
  // 执行 SQL 查询
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
      ElMessage.warning(res.msg || "查询失败");
    }
  } finally {
    loadings.value = false;
  }
}

// 6. 清空编辑器
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
