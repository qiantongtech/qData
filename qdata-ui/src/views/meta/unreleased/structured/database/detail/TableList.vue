<template>
  <qt-wrap
    :columns="tableStroe.columns"
    :tableRef="tableRef"
    :config="{ fullContent: false, actions: { table: { search: false } } }"
  >
    <template #actions-data v-if="route.query.table_status">
      <el-button
        type="primary"
        plain
        icon="Plus"
        @click="handleAddClick"
        v-hasPermi="['md:unreleased:structured:table:add']"
      >
        新增
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
          v-hasPermi="['md:unreleased:structured:table:detail']"
        >
          详情
        </el-button>

        <template
          v-if="
            (detail.status == '1' && route.query.table_status) ||
            detail.status == '0'
          "
        >
          <el-button
            link
            type="primary"
            :disabled="row.status == '1'"
            icon="Edit"
            @click="handleEditClick(row)"
            v-hasPermi="['md:unreleased:structured:table:edit']"
          >
            修改
          </el-button>
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
                v-hasPermi="[
                  'md:unreleased:structured:table:edit',
                  'md:unreleased:structured:table:remove',
                  'md:unreleased:structured:table:detail',
                ]"
              >
                更多
              </el-button>
            </template>
            <el-button
              link
              type="danger"
              icon="Delete"
              :disabled="row.status == 1"
              @click="handleDeleteClick(row)"
              v-hasPermi="['md:unreleased:structured:table:remove']"
            >
              删除
            </el-button>
            <el-button
              link
              type="primary"
              @click="handleDetailClick(row, 'ColumnList')"
            >
              <svg-icon icon-class="meta-column" class="handle-svg-icon" />
              字段列表
            </el-button>

            <el-button
              link
              type="primary"
              @click="handleDetailClick(row, 'VersionManagement')"
              v-hasPermi="['md:unreleased:structured:table:detail']"
            >
              <svg-icon icon-class="meta-version" class="handle-svg-icon" />
              版本与变更
            </el-button>
          </el-popover>
        </template>

        <template v-else>
          <el-button
            link
            type="primary"
            @click="handleDetailClick(row, 'ColumnList')"
          >
            <svg-icon icon-class="meta-column" class="handle-svg-icon" />
            字段列表
          </el-button>

          <el-button
            link
            type="primary"
            @click="handleDetailClick(row, 'VersionManagement')"
            v-hasPermi="['md:unreleased:structured:table:edit']"
          >
            <svg-icon icon-class="meta-version" class="handle-svg-icon" />
            版本与变更
          </el-button>
        </template>
      </template>
    </qt-table>
  </qt-wrap>
</template>

<script setup name="UnreleasedStructuredTable">
import { reactive, ref, getCurrentInstance, computed } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import {
  listTable,
  delTable,
  updateTableStatus,
} from "@/api/mc/unreleased/table";
import { useRoute, useRouter } from "vue-router";

const props = defineProps({
  detail: {
    type: Object,
    required: true,
  },
});

const { proxy } = getCurrentInstance();

const router = useRouter();
const route = useRoute();
const store = reactive({
  domains: [],
  treeDomains: [],
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
      invisible: props.detail.status == 1,
      // selectable: function (row) {
      //     return row.status === '0' ? true : false;
      // }
    },
    {
      label: "编号",
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: "库名",
      prop: "dbName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 140,
    },
    {
      label: "表名称",
      prop: "tableName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
      link: {
        external: handleDetailClick,
      },
    },
    {
      label: "表注释",
      prop: "tableComment",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
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
      label: "数据质量",
      prop: "dataQuality",
      width: 90,
    },
    {
      label: "版本号",
      prop: "version",
      width: 90,
      hide: true,
    },
    {
      label: "状态",
      prop: "status",
      width: 90,
      slot: "status",
      invisible: route.query.released,
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
      width: 280,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listTable,
  params: {
    dbId: props.detail.id,
    status: route.query.table_status ? "" : props.detail.status,
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

const baseUrl = computed(
  () =>
    `/meta/${route.query.released ? "released" : "unreleased"}/structured/table`
);

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

// 新增
function handleAddClick() {
  router.push({
    path: baseUrl.value + "/add",
    query: {
      dbId: props.detail.id,
    },
  });
}

// 修改
function handleEditClick(row) {
  router.push({
    path: baseUrl.value + "/edit",
    query: {
      id: row.id,
    },
  });
}

// 删除选中行
function handleDeleteColumnClick() {
  if (!store.rows.length) return;
  ElMessageBox.confirm(
    `可删除${store.rows.length}个，不可删除0个，是否删除可删部分`,
    "系统提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(() => {
      const ids = store.rows.map((item) => item.id);
      return delTable(ids);
    })
    .then(() => {
      ElMessage.success("删除成功");
      tableRef.value.getList();
    });
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(`是否确认删除编号为${row.id}的数据项？`, "系统提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      return delTable(row.id);
    })
    .then(() => {
      ElMessage.success("删除成功");
      tableRef.value.getList();
    });
}

// 详情
function handleDetailClick(row, tab) {
  router.push({
    path: baseUrl.value + "/detail",
    query: {
      id: row.id,
      tab: typeof tab === "string" ? tab : undefined,
    },
  });
}

// 切换状态
function handleStatusChange(row, status) {
  ElMessageBox.confirm(
    `是否确认${status == 1 ? "发布" : "取消发布"}数据编号为${
      row.id
    }的表元数据吗？`,
    "系统提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
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
        `编号为${row.id}的表元数据${status == 1 ? "发布" : "取消发布"}成功!`
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}

// getDomains();
</script>
