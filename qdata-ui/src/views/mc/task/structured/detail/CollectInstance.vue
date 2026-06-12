
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
  <div class="collect-instance" v-loading="store.loading">
    <qt-wrap
      :columns="tableStroe.columns"
      :tableRef="tableRef"
      :config="{ fullContent: false, actions: { table: { search: false } } }"
    >
      <qt-table v-bind="tableStroe">
        <template #table-count="{ row }">
          {{ row.totalCount }}（{{ td("common.texts.success") }}
          {{ row.successCount }}，{{ td("mc.task.structured.fail") }}
          {{ row.failCount }}）
        </template>

        <template #change-count="{ row }">
          {{ td("mc.task.structured.add") }} {{ row.addCount }}，{{
            td("mc.task.structured.delete")
          }}
          {{ row.delCount }}，{{ td("mc.task.structured.change") }}
          {{ row.updateCount }}
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
            {{ td("mc.task.structured.viewLog") }}
          </el-button>
          <el-button
            link
            type="warning"
            icon="Download"
            @click="handleDownloadClick(row)"
            v-hasPermi="['mc:instance:structured:log:download']"
          >
            {{ td("mc.task.structured.downloadLog") }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <LogDialog v-model="dialog.open" v-bind="dialog" />
  </div>
</template>

<script setup name="CollectInstance">
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import { listTaskInstance } from "@/api/mc/task/taskInstance";
import { getTaskInstanceLog } from "@/api/mc/task/taskInstanceLog";
import LogDialog from "@/components/LogDialog/index.vue";
import { getCurrentInstance, reactive } from "vue";

const { proxy } = getCurrentInstance();

const props = defineProps({
  detail: {
    type: Object,
    required: true,
  },
});

const store = reactive({
  loading: false,
});

const tableStroe = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      border: true,
      rowKey: "dictId",
      defaultSort: { prop: "create_time", order: "descending" },
    },
  },
  columns: [
    {
      label: td("common.texts.number"),
      prop: "id",
      width: 60,
    },
    {
      label: td("mc.task.structured.collectionScope"),
      prop: "collectionScope",
      dict: "mc_collect_scope",
      width: 120,
    },
    {
      label: td("mc.task.structured.collectTableCount"),
      slot: "table-count",
      minWidth: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.changeCount"),
      slot: "change-count",
      minWidth: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.runStatus"),
      prop: "status",
      dict: "mc_task_instance_status",
      width: 90,
    },
    {
      label: td("mc.task.structured.failCause"),
      prop: "failCause",
      minWidth: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.collectDuration"),
      prop: "duration",
      width: 120,
    },
    {
      label: td("mc.task.structured.collectTimeRange"),
      slot: "date-range",
      width: 340,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("common.texts.operation"),
      slot: "handle",
      width: 220,
      fixed: "right",
    },
  ],
  func: listTaskInstance,
  params: {
    taskId: props.detail.id,
  },
});

const dialog = reactive({
  open: false,
  content: "",
});

function handleViewClick(row) {
  store.loading = true;
  getTaskInstanceLog(row.id).then((res) => {
    dialog.content = res.data?.logContent || td("common.noLog");
    store.loading = false;
    dialog.open = true;
  });
}

// 下载日志
function handleDownloadClick(row) {
  store.loading = true;
  getTaskInstanceLog(row.id).then((res) => {
    const content = res.data?.logContent || td("common.noLog");
    const taskName = props.detail.name || "task";
    const instanceId = String(row.id).replace(/[^\w\-]/g, "_");
    const fileName = td("mc.instance.structured.logFileName", {
      name: taskName,
      id: instanceId,
    });
    proxy.downloadContent(content, fileName);
    store.loading = false;
  });
}
</script>
