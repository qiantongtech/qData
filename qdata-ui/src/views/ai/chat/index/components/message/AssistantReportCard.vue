<template>
  <div class="ai-report-card">
    <div class="ai-report-header">
      <div class="ai-report-title">
        <el-icon><Cpu /></el-icon>
        <span>{{ data?.header || "智能洞察" }}</span>
      </div>
    </div>
    <div class="ai-report-summary" v-if="data?.summary || data?.code == 500">
      <el-icon v-if="data?.isLoading && data?.code !== 500" class="is-loading"
        ><Loading
      /></el-icon>
      <span
        :style="data?.isLoading && data?.code !== 500 ? 'margin-left: 8px' : ''"
        >{{ data.summary }}</span
      >
    </div>
    <el-tabs
      v-if="data?.code !== 500 && data?.tabs?.length > 0"
      v-model="activeTab"
      class="ai-report-tabs"
    >
      <el-tab-pane
        v-for="tab in data?.tabs || []"
        :key="tab.key"
        :name="tab.key"
        :label="tab.label"
        lazy
      >
        <div
          class="ai-report-tab-content"
          v-loading="data?.isLoading"
          element-loading-text="加载中..."
        >
          <div v-if="!data?.isLoading" class="ai-report-tab-body">
            <div
              v-if="tab.chart && activeTab == tab.key"
              class="ai-report-chart-container"
            >
              <div class="ai-report-chart-header">
                <div class="ai-report-chart-actions">
                  <el-button
                    link
                    type="primary"
                    :icon="Download"
                    @click="handleDownloadChart"
                  >
                    下载图表
                  </el-button>
                </div>
              </div>
              <div class="ai-report-chart" ref="chartEl" />
            </div>
            <div v-else-if="tab.table" class="ai-report-table-wrap">
              <template v-if="tab.table.rows && tab.table.rows.length > 0">
                <div class="ai-report-table-header">
                  <el-button
                    link
                    type="primary"
                    :icon="Download"
                    @click="handleExport"
                    v-if="data?.conversationId && data?.messageId"
                  >
                    导出
                  </el-button>
                </div>
                <el-table :data="getCurrentPageData(tab.table)">
                  <el-table-column
                    v-for="col in tab.table.columns || []"
                    :key="col.prop || col.label"
                    :prop="col.prop"
                    :label="col.label"
                    :min-width="col.minWidth || 100"
                    show-overflow-tooltip
                    align="center"
                  />
                </el-table>
                <div
                  class="ai-report-table-pagination"
                  v-if="tab.table.rows?.length > pageSize"
                >
                  <el-pagination
                    v-model:current-page="currentPage"
                    v-model:page-size="pageSize"
                    :total="tab.table.rows.length"
                    :page-sizes="[10, 20, 50]"
                    layout="total, sizes, prev, pager, next"
                    size="small"
                    background
                    @current-change="handlePageChange"
                    @size-change="handleSizeChange"
                  />
                </div>
              </template>
              <div v-else class="ai-report-empty">
                <el-empty description="暂无明细数据" :image-size="60" />
              </div>
            </div>
            <div
              v-else-if="tab.code && activeTab == tab.key"
              class="ai-report-code"
            >
              <div class="ai-report-code-header">
                <div class="ai-report-code-actions">
                  <el-button link type="primary" @click="handleCopySql"
                    >复制</el-button
                  >
                </div>
              </div>
              <div class="ai-report-sql-container">
                <SqlEditor
                  :modelValue="tab.code"
                  readonly
                  :key="`sql-${activeTab}`"
                />
              </div>
            </div>
            <div v-else class="ai-report-empty">
              <el-empty description="暂无数据" :image-size="60" />
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { Cpu, Loading, Download } from "@element-plus/icons-vue";
import { ElLoading } from "element-plus";
import * as echarts from "echarts";
import { useClipboard } from "@vueuse/core";
import { ChatMessageApi } from "@/api/ai/chat/message";
import { saveAs } from "file-saver";
import SqlEditor from "@/components/SqlEditor/index2.vue";

const props = defineProps({
  data: {
    type: Object,
    required: true,
  },
});

const { proxy } = getCurrentInstance();
const message = proxy?.$modal;
const { copy } = useClipboard();

const activeTab = ref(
  props.data?.tabs?.find((t) => t.key === "viz")?.key ||
    props.data?.tabs?.find((t) => t.key === "sql")?.key ||
    props.data?.tabs?.[0]?.key ||
    ""
);

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);

const getCurrentPageData = (table) => {
  const rows = table.rows || [];
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return rows.slice(start, end);
};

const handlePageChange = (val) => {
  currentPage.value = val;
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1;
};

// 监听数据变化
watch(
  () => props.data,
  (newData) => {
    if (newData?.tabs?.length > 0) {
      const exists = newData.tabs.some((t) => t.key == activeTab.value);
      if (!exists || !activeTab.value) {
        // 优先选择可视化或 Text2SQL 标签
        const priorityTab =
          newData.tabs.find((t) => t.key === "viz") ||
          newData.tabs.find((t) => t.key === "sql");
        activeTab.value = priorityTab ? priorityTab.key : newData.tabs[0].key;
      }
    }
    // 只在非加载状态下尝试渲染
    if (!newData?.isLoading) {
      nextTick(() => {
        renderChart();
      });
    }
  },
  { deep: true, immediate: true }
);

// 监听标签页切换，重置分页并渲染图表
watch(activeTab, (newTab, oldTab) => {
  if (newTab !== oldTab) {
    currentPage.value = 1;
    nextTick(() => {
      renderChart();
    });
  }
});

const chartEl = ref(null);
let chart;

const buildOption = (chartData) => {
  // 过滤无效的 xAxis 数据，确保 label 不为 null
  const xAxis = (chartData?.xAxis || []).map((x) =>
    x == null || x == undefined ? "" : x
  );
  const type = chartData?.type || "bar";
  const isPie = type == "pie";

  const series = [];

  if (type == "bar") {
    const seriesList = chartData?.series || [];

    // 如果只有一组数据，使用带背景槽的样式
    if (seriesList.length == 1) {
      const yData = (seriesList[0].data || []).map((v) =>
        v == null || v == undefined ? 0 : v
      );
      const maxYData = Math.max(...yData, 0);
      const bgData = yData.map(() => (maxYData > 0 ? maxYData * 1.2 : 0));

      series.push({
        name: "bg",
        type: "bar",
        barWidth: 30,
        barGap: "0%",
        data: bgData,
        itemStyle: {
          color: "#EEF6FD ",
        },
        silent: true,
      });

      series.push({
        name: seriesList[0].name || "数量",
        type: "bar",
        barWidth: 30,
        barGap: "-100%",
        z: 10,
        itemStyle: {
          borderRadius: [2, 2, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "#5D8EFE" },
            { offset: 1, color: "#1F61FB" },
          ]),
        },
        data: yData,
      });
    } else {
      // 多组数据正常展示
      seriesList.forEach((s) => {
        series.push({
          name: s.name,
          type: "bar",
          barWidth: 20,
          data: s.data,
          itemStyle: {
            borderRadius: [2, 2, 0, 0],
          },
        });
      });
    }
  } else {
    (chartData?.series || []).forEach((s) => {
      const base = {
        name: s.name,
        type: type,
        data: s.data,
      };

      if (type == "line") {
        base.smooth = true;
        base.areaStyle = {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "rgba(37, 127, 255, 0.3)" },
            { offset: 1, color: "rgba(37, 127, 255, 0)" },
          ]),
        };
      } else if (type == "pie") {
        base.radius = ["40%", "70%"];
        base.avoidLabelOverlap = false;
        base.itemStyle = {
          borderRadius: 10,
          borderColor: "#fff",
          borderWidth: 2,
        };
        base.data = xAxis.map((name, i) => ({
          name: name,
          value: s.data[i],
        }));
      }
      series.push(base);
    });
  }

  const option = {
    tooltip: {
      trigger: isPie ? "item" : "axis",
      axisPointer: { type: "shadow" },
      formatter: (params) => {
        if (isPie)
          return `${params.name}: ${params.value} (${params.percent}%)`;
        const arr = params.filter((item) => item.seriesName !== "bg");
        if (arr.length == 0) return "";
        let res = `${arr[0].name}<br/>`;
        arr.forEach((item) => {
          res += `${item.marker}${item.seriesName}: ${item.value}<br/>`;
        });
        return res;
      },
    },
    legend: { show: false },
    grid: { left: 40, right: 10, top: 10, bottom: isPie ? 20 : 35 },
    series,
    dataZoom: isPie
      ? []
      : [
          {
            type: "inside",
            start: 0,
            end: xAxis.length > 16 ? (15 / xAxis.length) * 100 : 100,
          },
          {
            start: 0,
            end: xAxis.length > 16 ? (15 / xAxis.length) * 100 : 100,
          },
        ],
  };

  if (!isPie) {
    option.xAxis = {
      type: "category",
      data: xAxis,
      axisTick: { show: false },
      axisLine: {
        show: true,
        lineStyle: { color: "#d9d9d9" },
      },
      axisLabel: {
        margin: 14,
        fontSize: 12,
        color: "#595959",
        fontFamily: "PingFang SC",
      },
    };
    option.yAxis = {
      type: "value",
      splitLine: {
        lineStyle: { type: "dashed", color: "#f0f0f0" },
      },
    };
  }

  return option;
};

const getChartDom = () => {
  const el = chartEl.value;
  if (Array.isArray(el)) {
    // 找到当前活跃标签页下的图表容器
    return el.find((dom) => {
      // 检查 dom 是否在当前活跃的标签内容中
      return dom && dom.offsetParent !== null;
    });
  }
  return el;
};

const renderChart = async () => {
  await nextTick();
  // 再次等待以确保 DOM 渲染完成（特别是在切换标签时）
  await new Promise((resolve) => setTimeout(resolve, 50));

  const tab = (props.data?.tabs || []).find((t) => t.key == activeTab.value);
  if (!tab?.chart) return;
  const dom = getChartDom();
  if (!dom) return;

  if (chart) {
    if (chart.getDom() !== dom) {
      chart.dispose();
      chart = echarts.init(dom);
    }
  } else {
    chart = echarts.init(dom);
  }

  chart.setOption(buildOption(tab.chart), true);
  // 确保在渲染后立即调整大小以适应新容器
  chart.resize();
};

const handleCopySql = async () => {
  const tab = (props.data?.tabs || []).find((t) => t.key == activeTab.value);
  const code = tab?.code;
  if (!code) return;
  await copy(code);
  message?.msgSuccess?.("复制成功！");
};

/** 下载图表 */
const handleDownloadChart = () => {
  if (!chart) return;
  const url = chart.getDataURL({
    type: "png",
    pixelRatio: 2,
    backgroundColor: "#fff",
  });
  const link = document.createElement("a");
  link.href = url;
  link.download = `图表_${new Date().getTime()}.png`;
  link.click();
};

/** 导出明细 */
const handleExport = async () => {
  const { conversationId, messageId } = props.data;
  if (!conversationId || !messageId) return;

  const loading = ElLoading.service({
    lock: true,
    text: "正在导出，请稍候...",
    background: "rgba(0, 0, 0, 0.7)",
  });

  try {
    const res = await ChatMessageApi.exportDetailData({
      conversationId,
      messageId,
    });
    const blob = new Blob([res], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
    saveAs(blob, `明细数据_${new Date().getTime()}.xlsx`);
    message?.msgSuccess?.("导出成功！");
  } catch (err) {
    console.error("Export failed:", err);
    message?.msgError?.("导出失败，请重试！");
  } finally {
    loading.close();
  }
};

onMounted(async () => {
  await renderChart();
  window.addEventListener("resize", renderChart);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", renderChart);
  if (chart) {
    chart.dispose();
    chart = undefined;
  }
});
</script>

<style scoped lang="scss">
.ai-report-card {
  border: 1px solid #e5e6e7;
  border-radius: 2px;
  background-color: #fff;
  padding: 16px;
  margin-top: 12px;
  text-align: left;

  width: 100%;
  min-width: 600px;
  box-sizing: border-box;
}

.is-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.ai-report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ai-report-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.ai-report-summary {
  margin-top: 8px;
  font-size: 14px;
  color: #333;
  line-height: 22px;
}

.ai-report-tabs {
  margin-top: 10px;
}

.ai-report-tab-content {
  min-height: 240px;
  position: relative;
  display: flex;
  flex-direction: column;
}

.ai-report-chart {
  height: 320px;
  width: 100%;
}

.ai-report-code {
  margin: 0;
  padding: 10px 10px 12px 10px;
  border-radius: 2px;
  background: #f5f7fa;
  border: 1px solid #e5e6e7;
  color: #333;
  font-size: 13px;
  overflow: hidden;
}

.ai-report-table-wrap {
  width: 100%;
  background-color: #fff;
}

.ai-report-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
  min-height: 240px;
}

:deep(.el-table) {
  --el-table-header-bg-color: #f8f8f9;
  --el-table-header-text-color: #515a6e;
  --el-table-row-hover-bg-color: #f5f7fa;

  thead th {
    font-weight: 600;
    height: 36px;
    font-size: 13px;
  }

  td {
    height: 40px;
  }
}

.ai-report-chart-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 8px;
}

.ai-report-table-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 8px;
}

.ai-report-table-pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.ai-report-code-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 8px;
}

.ai-report-sql-container {
  border: 1px solid #e5e6e7;
  border-radius: 2px;
  overflow: hidden;

  :deep(.CodeMirror) {
    min-height: 150px;
    max-height: 400px;
  }
}

.ai-report-code-title {
  font-size: 13px;
  font-weight: 600;
  color: #333;
}

.ai-report-code-body {
  margin: 0;
  text-align: left;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas,
    "Liberation Mono", "Courier New", monospace;
  line-height: 18px;
  white-space: pre;
  overflow: auto;
}
</style>
