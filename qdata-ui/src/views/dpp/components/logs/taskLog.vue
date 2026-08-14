<template>
  <el-dialog
    :key="dialogKey"
    v-model="visible"
    :title="td('dpp.integratioTask.viewLog', '查看日志')"
    :draggable="true"
    class="medium-dialog"
    width="1200px"
    @close="handleClose"
  >
    <div class="log-dialog-container">
      <DetailInfo :data="taskInfo" :items="detailItems" mode="free">
        <template #statusSlot="{ data }">
          <StatusTag :status="data.currentStatus" />
        </template>
      </DetailInfo>
      <div class="log-toolbar">
        <el-input
          v-model="searchKeyword"
          :placeholder="td('dpp.integratioTask.searchLogContent', '搜索日志内容')"
          class="search-input"
          @keyup.enter="handleSearch"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <div class="toolbar-right">
          <span class="auto-refresh-label">{{ td("dpp.integratioTask.autoRefresh", "自动刷新") }}</span>
          <el-switch
            v-model="autoRefresh"
            @change="handleAutoRefreshChange"
            :disabled="!isRunning"
          />
          <!-- <el-button type="primary" @click="scrollToBottom" size="small">
            <el-icon class="button-icon-spacing"><ArrowDown /></el-icon>
            定位最新
          </el-button> -->
          <el-button type="warning" @click="handleDownload" size="small">
            <el-icon class="button-icon-spacing"><Download /></el-icon>
            {{ td("dpp.integratioTask.downloadLog", "下载日志") }}
          </el-button>
        </div>
      </div>
      <div
        class="log-content"
        ref="logContentRef"
        v-loading="loading"
        @scroll="handleLogScroll"
      >
        <div
          v-for="(log, index) in filteredLogs"
          :key="index"
          class="log-row"
          :class="getLogLevelClass(log.level)"
          :ref="
            (el) => {
              if (el) logRowRefs[index] = el;
            }
          "
        >
          <span class="log-line-number">{{ index + 1 }}</span>
          <span class="log-time-text" v-if="log.logTime">【{{ log.logTime }}】</span>
          <span v-if="log.stage" class="log-stage-text">{{ log.stage }}</span>
          <span class="log-level-text">{{ log.level }}</span>
          <span class="log-content-text" v-html="log.highlightedContent"></span>
        </div>
        <div v-if="!loading && filteredLogs.length === 0" class="log-empty">
          {{ td("dpp.integratioTask.noLogRecords", "暂无日志记录") }}
        </div>
      </div>
    </div>
    <template #footer>
      <div class="log-footer">
        <el-button @click="handleClose">{{ td("dpp.integratioTask.close", "关闭") }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
/**
 * 任务日志查看组件 (TaskLogDialog)
 * 用于展示 ETL 任务实例的运行日志，支持实时刷新、搜索过滤和日志下载功能。
 * 通过 `open` 方法接收 `taskInstanceId` 来展示对应任务的日志。
 */
import {
  ref,
  onBeforeUnmount,
  nextTick,
  computed,
  getCurrentInstance,
} from "vue";
import { Search, Download } from "@element-plus/icons-vue";
import DetailInfo from "@/components/DetailInfo/index.vue";
import StatusTag from "./StatusTag.vue";
import { getLogByTaskInstanceId } from "@/api/dpp/task/etlTask";
import { getLogByNodeInstanceId } from "@/api/dpp/instance/integratio";
import useDefaultLang from "@/composables/useDefaultLang";

const props = defineProps({
  instanceType: {
    type: String,
    default: "task",
  },
});

const { proxy } = getCurrentInstance();
const { td, locale } = useDefaultLang();

/** 弹窗可见性 */
const visible = ref(false);
/** 弹窗重建 key，用于清理拖拽后的历史位置 */
const dialogKey = ref(0);
/** 加载状态 */
const loading = ref(false);
/** 日志内容容器引用 */
const logContentRef = ref(null);
/** 日志行引用 */
const logRowRefs = ref([]);
/** 搜索关键词 */
const searchKeyword = ref("");
/** 自动刷新开关 */
const autoRefresh = ref(false);
/** 原始日志内容 */
const rawLogContent = ref("");
/** 原始日志列表 */
const rawLogList = ref([]);
/** 任务信息 */
const taskInfo = ref({});
/** 任务实例ID */
const taskInstanceId = ref(null);
/** 任务是否正在运行 */
const isRunning = ref(false);
/** 轮询控制标志 */
const polling = ref(false);
/** 自动刷新时是否跟随日志滚动到底部 */
const shouldAutoScroll = ref(true);
const BOTTOM_DISTANCE_THRESHOLD = 20;

const getLogDetail = (instanceId) =>
  props.instanceType === "node"
    ? getLogByNodeInstanceId({ nodeInstanceId: instanceId })
    : getLogByTaskInstanceId({ taskInstanceId: instanceId });

/**
 * 更新任务状态
 * @param {string} status - 任务状态
 */
const updateTaskStatus = (status) => {
  if (!taskInfo.value.taskInstance) {
    taskInfo.value.taskInstance = {};
  }
  taskInfo.value.taskInstance.status = status;
};

/** 详情展示项 */
const detailItems = computed(() => [
  { label: td("dpp.integratioTask.taskName", "任务名称"), key: "name" },
  { label: td("dpp.integratioTask.number", "编号"), key: "id" },
  {
    label: td("dpp.integratioTask.executeStatus", "执行状态"),
    key: "status",
    slot: "statusSlot",
  },
  {
    label: td("dpp.integratioTask.startTime", "开始时间"),
    key: "startTime",
    formatter: (d) => formatTime(d.startTime),
    className: "mt2 mb2",
  },

  {
    label: td("dpp.integratioTask.elapsed", "已运行"),
    key: "runDuration",
    className: "mt2 mb2",
  },
]);

/** 解析后的日志列表（计算属性） */
const logs = computed(() => rawLogList.value);

/** 根据搜索关键词过滤后的日志列表（计算属性） */
const filteredLogs = computed(() => {
  if (!searchKeyword.value) {
    return logs.value.map((log) => ({
      ...log,
      highlightedContent: log.detailContent || log.content,
    }));
  }

  const keyword = searchKeyword.value; // Keep original case for regex matching
  const lowerKeyword = keyword.toLowerCase();
  const highlightClass = "highlight";

  const regex = new RegExp(`(${keyword})`, "gi"); // Global and case-insensitive

  return logs.value
    .filter((log) => {
      const textToSearch = log.detailContent || log.content || "";
      const levelText = log.level || "";
      return (
        levelText.toLowerCase().includes(lowerKeyword) ||
        textToSearch.toLowerCase().includes(lowerKeyword)
      );
    })
    .map((log) => {
      let content = log.detailContent || log.content || "";
      // Replace only in content, not in level for highlighting
      if (content.toLowerCase().includes(lowerKeyword)) {
        content = content.replace(
          regex,
          `<span class="${highlightClass}">$1</span>`
        );
      }
      return {
        ...log,
        highlightedContent: content,
      };
    });
});

/**
 * 获取日志级别对应的样式类名
 * @param {string} level - 日志级别（ERROR/WARN/INFO/DEBUG）
 * @returns {string} 样式类名
 */
const getLogLevelClass = (level) => {
  const levels = {
    ERROR: "error",
    WARN: "warn",
    INFO: "info",
    DEBUG: "debug",
  };
  return levels[level] || "info";
};

/**
 * 格式化时间显示
 * @param {number|string} time - 时间戳或时间字符串
 * @returns {string} 格式化后的时间字符串
 */
const formatTime = (time) => {
  if (!time) return "-";
  return new Date(time).toLocaleString(locale.value, {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
};

/** 处理搜索，重置滚动到顶部 */
const handleSearch = () => {
  logRowRefs.value = []; // Clear refs before new search
  nextTick(() => {
    if (logContentRef.value) {
      // logContentRef.value.scrollTop = 0; // Remove initial scroll to top
      const firstHighlighted = logRowRefs.value.find(
        (el) => el && el.querySelector(".highlight")
      );
      if (firstHighlighted) {
        firstHighlighted.scrollIntoView({ behavior: "smooth", block: "start" });
      }
    }
  });
};

/** 滚动到日志底部 */
const scrollToBottom = () => {
  nextTick(() => {
    nextTick(() => {
      if (logContentRef.value) {
        logContentRef.value.scrollTop = logContentRef.value.scrollHeight;
        shouldAutoScroll.value = true;
      }
    });
  });
};

/** 判断日志滚动条是否接近底部 */
const isLogNearBottom = () => {
  const el = logContentRef.value;
  if (!el) return true;
  return (
    el.scrollHeight - el.scrollTop - el.clientHeight <=
    BOTTOM_DISTANCE_THRESHOLD
  );
};

/** 记录用户滚动位置，离开底部后自动刷新不再强制回到底部 */
const handleLogScroll = () => {
  shouldAutoScroll.value = isLogNearBottom();
};

/**
 * 自动刷新开关变化处理
 * @param {boolean} val - 开关状态
 */
const handleAutoRefreshChange = (val) => {
  polling.value = val;
  if (val && taskInstanceId.value) {
    fetchLog(taskInstanceId.value);
  }
};

/**
 * 轮询获取日志
 * @param {string} taskId - 任务实例ID
 */
const fetchLog = async (taskId) => {
  if (!polling.value) return;
  try {
    const needScrollToBottom = shouldAutoScroll.value || isLogNearBottom();
    const res = await getLogDetail(taskId);
    const {
      status,
      currentStatus,
      logList,
      taskName,
      startTime,
      duration,
      statusName,
    } = res.data;

    taskInfo.value = {
      ...res.data,
      name: taskName,
      id: taskId,
      startTime: startTime,
      runDuration: duration,
      status: status,
      currentStatus: currentStatus,
      statusName: statusName,
    };
    updateTaskStatus(status);
    const taskStatus = Number(status);

    isRunning.value = currentStatus === "running";

    if (isRunning.value) {
      if (logList && logList.length > rawLogList.value.length) {
        rawLogList.value = logList;
      }
    } else {
      rawLogList.value = logList || [];
    }

    if (needScrollToBottom) {
      scrollToBottom();
    }

    if ([5, 6, 7].includes(taskStatus)) {
      polling.value = false;
      autoRefresh.value = false;
      isRunning.value = false;
      return;
    }
  } catch (error) {
    console.error("获取日志失败", error);
  }

  if (polling.value) {
    setTimeout(() => fetchLog(taskId), 3000);
  }
};

/**
 * 打开日志弹窗
 * @param {string} taskId - 任务实例ID
 */
const open = async (taskId) => {
  dialogKey.value += 1;
  loading.value = true;
  taskInstanceId.value = taskId;
  visible.value = true;
  autoRefresh.value = false;
  shouldAutoScroll.value = true;
  searchKeyword.value = "";
  rawLogContent.value = "";
  rawLogList.value = [];
  logRowRefs.value = []; // Clear refs when opening dialog
  isRunning.value = false;

  await nextTick();

  const res = await getLogDetail(taskId);
  const {
    status,
    log,
    logList,
    taskName,
    startTime,
    duration,
    statusName,
    currentStatus,
  } = res.data;

  taskInfo.value = {
    name: taskName,
    id: taskId,
    startTime: startTime,
    runDuration: duration,
    status: status,
    currentStatus: currentStatus,
    statusName: statusName,
  };
  updateTaskStatus(status);
  rawLogContent.value = log || "";
  rawLogList.value = logList || []; // Populate rawLogList
  isRunning.value = currentStatus === "running";
  loading.value = false;
  scrollToBottom();

  if (isRunning.value) {
    autoRefresh.value = true;
    polling.value = true;
    setTimeout(() => fetchLog(taskId), 3000);
  }
};

/** 关闭弹窗，清理状态 */
const handleClose = () => {
  visible.value = false;
  polling.value = false;
  autoRefresh.value = false;
  shouldAutoScroll.value = true;
  rawLogContent.value = "";
  taskInfo.value = {};
};

/** 下载日志文件 */
const handleDownload = () => {
  const isNodeInstance = props.instanceType === "node";
  proxy.download(
    isNodeInstance
      ? "/dpp/etlNodeInstance/downloadLog"
      : "/dpp/etlTaskInstance/downloadLog",
    isNodeInstance
      ? { nodeInstanceId: taskInstanceId.value }
      : { taskInstanceId: taskInstanceId.value },
    `${taskInfo.value.name || "task"}.log`
  );
};

/** 组件卸载前停止轮询 */
onBeforeUnmount(() => {
  polling.value = false;
});

/** 暴露 open 方法供父组件调用 */
defineExpose({ open });
</script>

<style scoped>
.log-dialog-container {
  display: flex;
  flex-direction: column;
  height: 600px;
}

.log-header {
  display: flex;
  align-items: center;
  gap: 32px;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 12px;
}

.header-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-label {
  font-size: 13px;
  color: #909399;
}

.header-value {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

.log-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.search-input {
  width: 300px;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.auto-refresh-label {
  font-size: 13px;
  color: #606266;
}

.log-content {
  flex: 1;
  overflow-y: auto;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 4px 0;
  font-family: "Consolas", "Monaco", "Courier New", monospace;
  font-size: 13px;
  background-color: #f9f9f9;
}

.log-row {
  display: flex;
  align-items: flex-start;
  padding: 2px 12px;
  line-height: 1.6;
  margin-top: 2px;
}

.log-line-number {
  width: 30px;
  text-align: right;
  color: #909399;
  margin-right: 8px;
  flex-shrink: 0;
}

.log-time-text {
  color: #303133;
  margin-right: 8px;
  flex-shrink: 0;
}

.log-level-text {
  text-align: left;
  color: #606266;
  margin-right: 8px;
  flex-shrink: 0;
}

.log-stage-text {
  color: #606266;
  margin-right: 8px;
  flex-shrink: 0;
}

.log-content-text {
  color: #303133;
  flex: 1;
  word-break: break-all;
  white-space: pre-wrap;
}

.log-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #c0c4cc;
}

.log-row.error .log-level-text,
.log-row.error .log-content-text {
  color: #ef4444;
}

.log-row.warn .log-level-text,
.log-row.warn .log-content-text {
  color: #f59e0b;
}

.log-row.info .log-level-text,
.log-row.debug .log-level-text {
  color: #2a7bfd;
}

.log-row.info .log-content-text,
.log-row.debug .log-content-text {
  color: #303133;
}

.highlight {
  color: var(--el-color-danger); /* Highlight text in red */
  font-weight: bold;
}

.log-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.button-icon-spacing {
  margin-right: 4px;
}
</style>
