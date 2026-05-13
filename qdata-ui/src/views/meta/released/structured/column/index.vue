<template>
  <div class="app-container">
    <guide-tip tip-id="meta/unreleased/structured/column" />
    <qt-wrap :columns="tableStroe.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStroe.params"
          :tableRef="tableRef"
          :config="{ permi: ['md:released:structured:column:query'] }"
        />
      </template>
      <qt-table v-bind="tableStroe" ref="tableRef">
        <template #domain-name="scope">
          {{ getDomainPath(scope.row.domainId) }}
        </template>

        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetailClick(row)"
            v-hasPermi="['md:released:structured:column:detail']"
          >
            详情
          </el-button>
          <!-- <el-button
            link
            type="primary"
            @click="handleDetailClick(row, 'LineageAnalysis')"
            v-hasPermi="['md:released:structured:column:detail']"
          >
            <svg-icon icon-class="meta-lineage" class="handle-svg-icon" />
            血缘分析
          </el-button> -->
          <el-popover
            placement="bottom"
            :width="120"
            popper-class="handle-popover"
            trigger="click"
          >
            <template #reference>
              <el-button
                link
                type="primary"
                icon="ArrowDown"
                v-hasPermi="['md:released:structured:column:detail']"
                >更多</el-button
              >
            </template>

            <el-button
              link
              type="primary"
              @click="handleDetailClick(row, 'ImpactAnalysis')"
              v-hasPermi="['md:released:structured:column:detail']"
            >
              <svg-icon icon-class="meta-impact" class="handle-svg-icon" />
              影响分析
            </el-button>
            <el-button
              link
              type="primary"
              @click="handleDetailClick(row, 'VersionManagement')"
              v-hasPermi="['md:released:structured:column:detail']"
            >
              <svg-icon icon-class="meta-version" class="handle-svg-icon" />
              版本与变更
            </el-button>
          </el-popover>
        </template>
      </qt-table>
    </qt-wrap>
  </div>
</template>

<script setup name="UnreleasedStructuredColumn">
import { getCurrentInstance, reactive, ref } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import { listColumn } from "@/api/mc/unreleased/column.js";
import { listDb } from "@/api/mc/unreleased/db";
import { listTable } from "@/api/mc/unreleased/table";
import { useRouter } from "vue-router";

const BASE_URL = "/meta/released/structured/column";

const { proxy } = getCurrentInstance();

const router = useRouter();

const store = reactive({
  rows: [],
  domains: [],
  treeDomains: [],
  metaDatabases: [],
  metaTables: [],
});

const tableRef = ref();
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
      label: "编号",
      prop: "id",
      sortable: true,
      width: 70,
    },
    {
      label: "所属库名",
      prop: "dbName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
    },
    {
      label: "所属表名",
      prop: "tableName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
    },
    {
      label: "字段名称",
      align: "left",
      prop: "columnName",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
      align: "left",
      link: {
        external: handleDetailClick,
      },
    },
    {
      label: "字段注释",
      prop: "columnComment",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
    },
    {
      label: "描述",
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "业务域",
      prop: "domainId",
      slot: "domain-name",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "数据标准",
      prop: "dataElemName",
      width: 110,
    },
    {
      label: "数据质量",
      prop: "dataQuality",
      width: 90,
      sortable: true,
    },
    {
      label: "字段长度",
      prop: "columnLength",
      width: 90,
      sortable: true,
    },
    {
      label: "字段精度",
      prop: "columnPrecision",
      width: 90,
      sortable: true,
    },
    {
      label: "字段小数",
      prop: "columnScale",
      width: 90,
      sortable: true,
    },
    {
      label: "默认值",
      prop: "defaultValue",
      width: 110,
    },
    {
      label: "是否主键",
      prop: "pkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: "是否外键",
      prop: "fkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: "是否可空",
      prop: "nullableFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: "更新人",
      prop: "updateBy",
      width: 120,
    },
    {
      label: "更新时间",
      prop: "updateTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: "创建人",
      prop: "createBy",
      width: 120,
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: "操作",
      width: 240,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listColumn,
  params: {
    status: "1",
    dataType: 1,
  },
});

const searchStore = reactive({
  items: [
    {
      label: "字段名称",
      prop: "columnName",
      component: {
        is: "input",
      },
    },
    {
      label: "字段注释",
      prop: "columnComment",
      component: {
        is: "input",
      },
    },
    {
      label: "业务域",
      prop: "domainCode",
      component: {
        is: "tree-select",
        filterable: true,
        data: store.treeDomains,
        props: { value: "code", label: "name", children: "children" },
        valueKey: "id",
        checkStrictly: true,
        defaultExpandAll: true,
      },
    },
    {
      label: "所属库名",
      prop: "dbId",
      component: {
        is: "select",
        options: store.metaDatabases,
      },
    },

    {
      label: "所属表名",
      prop: "tableId",
      component: {
        is: "select",
        options: store.metaTables,
      },
    },
  ],
});

// 获取业务域路径
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

// 获取业务域列表
function getDomains() {
  listDomain().then((res) => {
    store.domains = [...res.data];
    store.treeDomains.splice(0, store.treeDomains.length);
    const domains = {
      id: 0,
      code: 0,
      name: "顶级节点",
      children: proxy.handleTree(res.data, "id", "parentId"),
    };
    store.treeDomains.push(domains);
  });
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

// 获取表元素列表
function getMetaTables() {
  store.metaTables.splice(0, store.metaTables.length);
  return listTable({ pageSize: 1000 }).then((res) => {
    res.data.rows.forEach((item) => {
      store.metaTables.push({
        value: item.id,
        label: item.tableName,
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

// getDomains();
getMetaDatabases();
getMetaTables();
</script>
