<template>
  <div class="app-container">
    <guide-tip tip-id="meta/unreleased/structured/table" />
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
              :config="{ permi: ['mc:unreleased:structured:table:query'] }"
            />
          </template>
          <template #actions-data>
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDeleteColumnClick"
            >
              {{ td("common.button.delete") }}
            </el-button>
          </template>
          <qt-table v-bind="tableStroe" ref="tableRef">
            <template #domain-name="scope">
              {{ getDomainPath(scope.row.domainId) }}
            </template>

            <template #status="scope">
              <el-switch
                v-if="scope.row.status != undefined"
                v-model="scope.row.status"
                active-value="1"
                inactive-value="0"
                @change="handleStatusChange(scope.row, $event)"
              />
            </template>

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
                icon="Edit"
                :disabled="row.status == 1"
                @click="handleEditClick(row)"
              >
                {{ td("common.button.update") }}
              </el-button>
              <el-popover
                placement="bottom"
                :width="120"
                popper-class="handle-popover"
                trigger="click"
              >
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">
                    {{ td("common.button.more") }}
                  </el-button>
                </template>
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  :disabled="row.status == 1"
                  @click="handleDeleteClick(row)"
                >
                  {{ td("common.button.delete") }}
                </el-button>
                <el-button
                  link
                  type="primary"
                  @click="handleDetailClick(row, 'VersionManagement')"
                >
                  <svg-icon icon-class="meta-version" class="handle-svg-icon" />
                  {{ td("meta.released.structured.table.versionManagement") }}
                </el-button>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
  </div>
</template>

<script setup name="UnreleasedStructuredTable">
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, ref, getCurrentInstance, computed } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import {
  listTable,
  delTable,
  updateTableStatus,
  batchDeleteCheck,
} from "@/api/mc/unreleased/table";
import { useRoute, useRouter } from "vue-router";
import { listDb } from "@/api/mc/unreleased/db";
import SourceSystemTree from "@/views/mc/task/structured/components/SourceSystemTree.vue";
import { ElMessageBox, ElMessage } from "element-plus";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const router = useRouter();
const route = useRoute();
const sourceSystemTreeRef = ref();
const store = reactive({
  domains: [],
  treeDomains: [],
  rows: [],
  metaDatabases: [],
  metaTables: [],
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
      prop: "selection",
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
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
      align: "left",
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
      label: td("meta.released.structured.table.version"),
      prop: "version",
      width: 90,
      hide: false,
    },
    {
      label: td("common.texts.status"),
      prop: "status",
      width: 90,
      slot: "status",
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
      prop: "handle",
      width: 220,
      fixed: "right",
      slot: "handle",
      hide: false,
    },
  ],
  func: listTable,
  params: {
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

// 新增
function handleAddClick() {
  router.push({
    path: route.path + "/add",
  });
}

// 修改
function handleEditClick(row) {
  router.push({
    path: route.path + "/edit",
    query: {
      id: row.id,
    },
  });
}

// 删除选中行
function handleDeleteColumnClick() {
  if (!store.rows.length) return;
  const ids = store.rows.map((item) => item.id);
  store.loading = true;
  batchDeleteCheck(ids).then((res) => {
    const { canDeleteCount, cannotDeleteCount, canDeleteIds } = res.data;
    store.loading = false;
    ElMessageBox.confirm(
      td(
        "meta.unreleased.structured.table.list.confirmBatchDelete",
        "可删除{canDelete}个，不可删除{cannotDelete}个，是否删除可删部分"
      )
        .replace("{canDelete}", canDeleteCount)
        .replace("{cannotDelete}", cannotDeleteCount),
      td("common.message.systemPrompt", "系统提示"),
      {
        confirmButtonText: td("common.button.confirm", "确定"),
        cancelButtonText: td("common.button.cancel", "取消"),
        type: "warning",
      }
    )
      .then(() => {
        if (!canDeleteIds.length) {
          ElMessage.success(td("common.message.deleteSuccess"));
          return;
        }
        return delTable(canDeleteIds.toString());
      })
      .then((res) => {
        if (!res) return;
        ElMessage.success(td("common.message.deleteSuccess"));
        tableRef.value.getList();
      });
  });
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(
    td(
      "meta.unreleased.structured.table.list.confirmDelete",
      "是否确认删除编号为{id}的数据项？"
    ).replace("{id}", row.id),
    td("common.message.systemPrompt", "系统提示"),
    {
      confirmButtonText: td("common.button.confirm", "确定"),
      cancelButtonText: td("common.button.cancel", "取消"),
      type: "warning",
    }
  )
    .then(() => {
      return delTable(row.id);
    })
    .then(() => {
      ElMessage.success(td("common.message.deleteSuccess"));
      tableRef.value.getList();
    });
}

// 详情
function handleDetailClick(row, tab) {
  router.push({
    path: route.path + "/detail",
    query: {
      id: row.id,
      tab: typeof tab === "string" ? tab : undefined,
      table_status: 1,
    },
  });
}

// 切换状态
function handleStatusChange(row, status) {
  const action =
    status == 1
      ? td("meta.unreleased.structured.table.list.publish", "发布")
      : td("meta.unreleased.structured.table.list.unpublish", "取消发布");
  ElMessageBox.confirm(
    td(
      "meta.unreleased.structured.table.list.confirmStatusChange",
      "是否确认{action}数据编号为{id}的表元数据吗？"
    )
      .replace("{action}", action)
      .replace("{id}", row.id),
    td("common.message.systemPrompt", "系统提示"),
    {
      confirmButtonText: td("common.button.confirm", "确定"),
      cancelButtonText: td("common.button.cancel", "取消"),
      type: "warning",
    }
  )
    .then(() => {
      return updateTableStatus({
        id: row.id,
        status,
      });
    })
    .then(() => {
      ElMessage.success(
        td(
          "meta.unreleased.structured.table.list.statusChangeSuccess",
          "编号为{id}的表元数据{action}成功!"
        )
          .replace("{id}", row.id)
          .replace("{action}", action)
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}

getMetaDatabases();
</script>
