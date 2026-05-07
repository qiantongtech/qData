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
  <div class="app-container" v-loading="store.loading">
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
              :config="{ permi: ['mc:instance:structured:query'] }"
            />
          </template>
          <template #actions-data>
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDeleteColumnClick"
              v-hasPermi="['mc:instance:structured:remove']"
            >
              删除
            </el-button>
          </template>
          <qt-table v-bind="tableStroe" ref="tableRef">
            <template #task-status="scope">
              <el-switch
                v-model="scope.row.taskStatus"
                active-value="1"
                inactive-value="0"
              />
            </template>

            <template #date-range="{ row }">
              {{ row.startTime }} ~ {{ row.endTime }}
            </template>

            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="View"
                @click="handleViewClick(row)"
                v-hasPermi="['mc:instance:structured:log:view']"
              >
                查看日志
              </el-button>
              <el-button
                link
                type="warning"
                icon="Download"
                @click="handleDownloadClick(row)"
                v-hasPermi="['mc:instance:structured:log:download']"
              >
                下载日志
              </el-button>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <LogDialog v-model="dialog.open" v-bind="dialog" />
  </div>
</template>

<script setup name="InstanceStructured">
import { reactive, computed, getCurrentInstance, ref } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { listTaskInstance, delTaskInstance } from "@/api/mc/task/taskInstance";
import { getParentLabelPath } from "@/utils/anivia";
import { getTaskInstanceLog } from "@/api/mc/task/taskInstanceLog";
import LogDialog from "@/components/LogDialog/index.vue";
import SourceSystemTree from "@/views/mc/task/structured/components/SourceSystemTree.vue";

const { proxy } = getCurrentInstance();

const sourceSystemTreeRef = ref();
const store = reactive({
  treeDomains: [],
  loading: false,
  rows: [],
});

const tableRef = ref(null);
const tableStroe = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
    },
    {
      label: "编号",
      prop: "id",
      width: 60,
    },
    {
      label: "任务名称",
      prop: "name",
      minWidth: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "来源系统",
      prop: "sourceSystemName",
      minWidth: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "采集表数量",
      prop: "totalCount",
      width: 140,
    },
    {
      label: "采集状态",
      prop: "status",
      width: 140,
      dict: "mc_task_instance_status",
    },
    {
      label: "采集耗时(s)",
      prop: "duration",
      width: 120,
    },
    {
      label: "采集起止时间",
      slot: "date-range",
      width: 340,
      showOverflowTooltip: {
        effect: "light",
      },
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
      sortableKey: "create_time",
      width: 160,
      date: true,
    },
    {
      label: "操作",
      slot: "handle",
      width: 220,
      fixed: "right",
    },
  ],
  func: listTaskInstance,
  params: {},
  events: {
    formatParams(params) {
      if (!params.time || !params.time.length) return params;
      const { time, ...other } = { ...params };
      other.createTimeStart = time[0];
      other.createTimeEnd = time[1];
      return other;
    },
  },
});

const searchStore = reactive({
  items: [
    {
      label: "任务名称",
      prop: "name",
      component: {
        is: "input",
      },
    },

    {
      label: "创建时间",
      prop: "time",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: "开始日期",
        endPlaceholder: "结束日期",
      },
    },
  ],
});

const dialog = reactive({
  open: false,
  content: "",
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

  if (data.type === "SOURCE") {
    tableStroe.params.sourceSystemId = data.id;
  } else if (data.type === "DATASOURCE") {
    tableStroe.params.datasourceId = data.id;
  } else if (data.type === "DATABASE") {
    tableStroe.params.taskId = data.taskId;
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
  tableRef.value?.resetQuery();
}

function handleViewClick(row) {
  getTaskInstanceLog(row.id).then((res) => {
    dialog.content = res.data?.logContent || "暂无日志";
    dialog.open = true;
  });
}

// 下载日志
function handleDownloadClick(row) {
  getTaskInstanceLog(row.id).then((res) => {
    const content = res.data?.logContent || "暂无日志";
    proxy.downloadContent(content, `${row.name}_${row.id}_日志.log`);
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
      return delTaskInstance(ids);
    })
    .then(() => {
      ElMessage.success("删除成功");
      tableRef.value.getList();
    });
}
</script>
