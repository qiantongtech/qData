<template>
  <div class="app-container">
    <!-- <guide-tip tip-id="meta/unreleased/structured/table" /> -->
    <el-container>
      <SourceSystemTree
        ref="sourceSystemTreeRef"
        @node-click="handleNodeClick"
        @data-loaded="handleTreeDataLoaded"
      />
      <el-main class="main-content">
        <qt-wrap :columns="tableStroe.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStroe.params"
              @query="handleQueryClick"
              @reset="handleResetQueryClick"
            />
          </template>
          <qt-table v-bind="tableStroe" ref="tableRef">
            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetailClick(row)"
              >
                {{ td("common.button.details") }}
              </el-button>

              <el-button
                link
                type="primary"
                @click="handleDetailClick(row, 'ColumnList')"
              >
                <svg-icon icon-class="meta-column" class="handle-svg-icon" />
                {{ td("meta.released.structured.table.columnList") }}
              </el-button>
              <el-button
                link
                type="primary"
                @click="handleDetailClick(row, 'VersionManagement')"
              >
                <svg-icon icon-class="meta-version" class="handle-svg-icon" />
                {{ td("meta.released.structured.table.versionManagement") }}
              </el-button>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
  </div>
</template>

<script setup name="ReleasedStructuredTable">
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, ref, getCurrentInstance, computed } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import { listTable } from "@/api/mc/unreleased/table";

import { useRouter } from "vue-router";
import { listDb } from "@/api/mc/unreleased/db";
import SourceSystemTree from "@/views/mc/task/structured/components/SourceSystemTree.vue";

const { td } = useDefaultLang();
const BASE_URL = "/dg/meta/comparison";

const { proxy } = getCurrentInstance();

const router = useRouter();
const sourceSystemTreeRef = ref();
const store = reactive({
  domains: [],
  treeDomains: [],
  metaDatabases: [],
  rows: [],
});

const tableRef = ref(null);
const tableStroe = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "createTime", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetailClick,
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
      hide: false,
    },
    {
      label: td("common.texts.number"),
      prop: "id",
      sortable: true,
      width: 60,
      hide: false,
    },
    {
      label: td("meta.released.structured.table.dbName"),
      prop: "dbName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
      hide: false,
    },
    {
      label: td("meta.released.structured.table.tableName"),
      prop: "tableName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
      link: {
        external: handleDetailClick,
      },
      hide: false,
    },
    {
      label: td("meta.released.structured.table.tableComment"),
      prop: "tableComment",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
      hide: false,
    },
    {
      label: td("common.texts.description"),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
      hide: false,
    },
    {
      label: td("meta.released.structured.table.sourceSystem"),
      prop: "sourceSystemName",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
      hide: false,
    },
    {
      label: td("meta.released.structured.table.version"),
      prop: "version",
      width: 90,
      hide: false,
    },
    {
      label: td("common.texts.updatedBy"),
      prop: "updateBy",
      width: 120,
      hide: false,
    },
    {
      label: td("common.texts.updatedTime"),
      prop: "updateTime",
      sortable: true,
      width: 160,
      date: true,
      hide: false,
    },
    {
      label: td("common.texts.createdBy"),
      prop: "createBy",
      width: 120,
      hide: false,
    },
    {
      label: td("common.texts.createdTime"),
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
      hide: false,
    },
    {
      label: td("common.texts.operation"),
      width: 280,
      fixed: "right",
      slot: "handle",
      hide: false,
    },
  ],
  func: listTable,
  params: {
    // status: "1",
    dataType: 1,
  },
  events: {
    formatData: function (data) {
      data.forEach((item) => {
        item.version = proxy.formatVersion(item.version);
      });
      return data;
    },
  },
});

const searchStore = reactive({
  items: [
    {
      label: td("meta.released.structured.table.tableName"),
      prop: "tableName",
      component: {
        is: "input",
      },
    },
    {
      label: td("meta.released.structured.table.tableComment"),
      prop: "tableComment",
      component: {
        is: "input",
      },
    },

    {
      label: td("meta.released.structured.table.dbName"),
      prop: "dbId",
      component: {
        is: "select",
        options: store.metaDatabases,
      },
    },
  ],
});

// 获取来源系统路径
const getDomainPath = computed(() => {
  return function (id) {
    let domainName = getParentLabelPath(store.treeDomains, id, {
      idKey: "id",
      labelKey: "name",
      childrenKey: "children",
    });
    const idx = domainName.indexOf("/");
    return idx == -1 ? domainName : domainName.slice(idx + 1);
  };
});

function handleTreeDataLoaded({ treeData, flatData }) {
  store.treeDomains = treeData;
}

// 节点单击事件
function handleNodeClick(data) {
  // 清除之前的筛选
  tableStroe.params.sourceSystemId = undefined;
  tableStroe.params.datasourceId = undefined;
  tableStroe.params.taskId = undefined;
  tableStroe.params.dbId = undefined;

  if (data.type === "SOURCE") {
    tableStroe.params.sourceSystemId = data.id;
  } else if (data.type === "DATASOURCE") {
    tableStroe.params.datasourceId = data.id;
  } else if (data.type === "DATABASE") {
    tableStroe.params.taskId = data.taskId;
    tableStroe.params.dbId = data.id;
  }
  tableRef.value.getList();
}

// 搜索按钮操作
function handleQueryClick() {
  tableRef.value?.getList();
}

// 重置按钮操作
function handleResetQueryClick() {
  if (sourceSystemTreeRef.value?.resetTree) {
    sourceSystemTreeRef.value.resetTree();
  }
  tableStroe.params.sourceSystemId = null;
  tableStroe.params.datasourceId = null;
  tableStroe.params.taskId = null;
  tableStroe.params.dbId = null;
  tableRef.value?.resetQuery();
}

// 获取库元素列表
function getMetaDatabases() {
  store.metaDatabases.splice(0, store.metaDatabases.length);
  return listDb({ pageSize: 1000 }).then((res) => {
    res.data.rows.forEach((item) => {
      store.metaDatabases.push({
        value: item.id,
        label: item.dbName,
      });
    });
    return res;
  });
}

// 详情
function handleDetailClick(row, tab) {
  router.push({
    path: BASE_URL + "/detail",
    query: {
      id: row.id,
      tab: typeof tab === "string" ? tab : undefined,
      released: "1",
    },
  });
}

getMetaDatabases();
</script>
