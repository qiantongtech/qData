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
            {{ td("common.button.details") }}
          </el-button>
          <!-- <el-button
            link
            type="primary"
            @click="handleDetailClick(row, 'LineageAnalysis')"
            v-hasPermi="['md:released:structured:column:detail']"
          >
            <svg-icon icon-class="meta-lineage" class="handle-svg-icon" />
            {{ td('meta.released.structured.column.lineageAnalysis') }}
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
                >{{ td("common.button.more") }}</el-button
              >
            </template>

            <el-button
              link
              type="primary"
              @click="handleDetailClick(row, 'ImpactAnalysis')"
              v-hasPermi="['md:released:structured:column:detail']"
            >
              <svg-icon icon-class="meta-impact" class="handle-svg-icon" />
              {{ td("meta.released.structured.column.impactAnalysis") }}
            </el-button>
            <el-button
              link
              type="primary"
              @click="handleDetailClick(row, 'VersionManagement')"
              v-hasPermi="['md:released:structured:column:detail']"
            >
              <svg-icon icon-class="meta-version" class="handle-svg-icon" />
              {{ td("meta.released.structured.column.versionManagement") }}
            </el-button>
          </el-popover>
        </template>
      </qt-table>
    </qt-wrap>
  </div>
</template>

<script setup name="UnreleasedStructuredColumn">
import useDefaultLang from "@/composables/useDefaultLang";
import { getCurrentInstance, reactive, ref, computed } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import { listColumn } from "@/api/mc/unreleased/column.js";
import { listDb } from "@/api/mc/unreleased/db";
import { listTable } from "@/api/mc/unreleased/table";
import { useRouter } from "vue-router";

const { td } = useDefaultLang();
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
      label: td("common.texts.number"),
      prop: "id",
      sortable: true,
      width: 70,
    },
    {
      label: td("meta.released.structured.column.dbName"),
      prop: "dbName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
    },
    {
      label: td("meta.released.structured.column.tableName"),
      prop: "tableName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
    },
    {
      label: td("meta.released.structured.column.columnName"),
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
      label: td("meta.released.structured.column.columnComment"),
      prop: "columnComment",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 230,
    },
    {
      label: td("common.texts.description"),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("meta.released.structured.column.businessDomain"),
      prop: "domainId",
      slot: "domain-name",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("meta.released.structured.column.dataStandard"),
      prop: "dataElemName",
      width: 110,
    },
    {
      label: td("meta.released.structured.column.dataQuality"),
      prop: "dataQuality",
      width: 90,
      sortable: true,
    },
    {
      label: td("meta.released.structured.column.columnLength"),
      prop: "columnLength",
      width: 90,
      sortable: true,
    },
    {
      label: td("meta.released.structured.column.columnPrecision"),
      prop: "columnPrecision",
      width: 90,
      sortable: true,
    },
    {
      label: td("meta.released.structured.column.columnScale"),
      prop: "columnScale",
      width: 90,
      sortable: true,
    },
    {
      label: td("meta.released.structured.column.defaultValue"),
      prop: "defaultValue",
      width: 110,
    },
    {
      label: td("meta.released.structured.column.pkFlag"),
      prop: "pkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: td("meta.released.structured.column.fkFlag"),
      prop: "fkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: td("meta.released.structured.column.nullableFlag"),
      prop: "nullableFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: td("common.texts.updatedBy"),
      prop: "updateBy",
      width: 120,
    },
    {
      label: td("common.texts.updatedTime"),
      prop: "updateTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: td("common.texts.createdBy"),
      prop: "createBy",
      width: 120,
    },
    {
      label: td("common.texts.createdTime"),
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: td("common.texts.operation"),
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
      label: td("meta.released.structured.column.columnName"),
      prop: "columnName",
      component: {
        is: "input",
      },
    },
    {
      label: td("meta.released.structured.column.columnComment"),
      prop: "columnComment",
      component: {
        is: "input",
      },
    },
    {
      label: td("meta.released.structured.column.businessDomain"),
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
      label: td("meta.released.structured.column.dbName"),
      prop: "dbId",
      component: {
        is: "select",
        options: store.metaDatabases,
      },
    },

    {
      label: td("meta.released.structured.column.tableName"),
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
      name: td('common.texts.topNode'),
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
