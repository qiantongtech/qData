<template>
  <div class="app-container">
    <guide-tip tip-id="meta/unreleased/structured/database" />
    <qt-wrap :columns="tableStroe.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStroe.params"
          :tableRef="tableRef"
          :config="{ permi: ['md:released:structured:db:query'] }"
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
            v-hasPermi="['md:released:structured:db:detail']"
          >
            {{ td("common.button.details") }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleDetailClick(row, 'TableList')"
            v-hasPermi="['md:released:structured:db:detail']"
          >
            <svg-icon
              icon-class="meta-table"
              class="handle-svg-icon"
            ></svg-icon>
            {{ td("meta.released.structured.database.tableList") }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleDetailClick(row, 'VersionManagement')"
            v-hasPermi="['md:released:structured:db:detail']"
          >
            <svg-icon
              icon-class="meta-version"
              class="handle-svg-icon"
            ></svg-icon>
            {{ td("meta.released.structured.database.versionManagement") }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>
  </div>
</template>

<script setup name="ReleasedStructuredDatabase">
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, ref, getCurrentInstance, computed } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { listDb } from "@/api/mc/unreleased/db.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import { useRouter } from "vue-router";

const { td } = useDefaultLang();
const BASE_URL = "/meta/released/structured/db";

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "meta_task_status",
  "meta_dw_layers"
);

const router = useRouter();

const store = reactive({
  rows: [],
  treeDomains: [],
});

// 列表
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
      label: td("common.texts.number"),
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: td("meta.released.structured.database.dbName"),
      prop: "dbName",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
      align: "left",
      link: {
        external: handleDetailClick,
      },
    },
    {
      label: td("common.texts.description"),
      prop: "description",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("meta.released.structured.database.businessDomain"),
      prop: "domainId",
      slot: "domain-name",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("meta.released.structured.database.dbType"),
      prop: "dbType",
      dict: "datasource_type",
      width: 90,
    },
    {
      label: td("meta.released.structured.database.dataQuality"),
      prop: "dataQuality",
      width: 90,
      sortable: true,
    },
    {
      label: td("meta.released.structured.database.tableCount"),
      prop: "tableCount",
      sortable: true,
      width: 90,
    },
    {
      label: td("meta.released.structured.database.version"),
      prop: "version",
      width: 90,
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
      width: 280,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listDb,
  params: {
    status: "1",
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

// 搜索项
const searchStore = reactive({
  items: [
    {
      label: td("meta.released.structured.database.dbName"),
      prop: "dbName",
      component: {
        is: "input",
      },
    },
    {
      label: td("meta.released.structured.database.dbType"),
      prop: "dbType",
      component: {
        is: "select",
        options: dicts.datasource_type,
      },
    },
    {
      label: td("meta.released.structured.database.businessDomain"),
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

// 点击详情
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
</script>
