<template>
  <div
    v-if="visible"
    class="running-tasks-panel"
    v-infinite-scroll="loadMore"
    :infinite-scroll-disabled="loading || noMore"
    :infinite-scroll-distance="20"
    ref="panelRef"
  >
    <div class="panel-header">
      <div class="panel-title">
        <span class="title-text">{{ title }}</span>
        <span class="title-count">{{ total }}</span>
      </div>
      <div class="panel-actions">
        <el-button link type="primary" @click="$emit('update:visible', false)">
          <i class="iconfont-mini icon-guanbi mr0"></i>
        </el-button>
      </div>
    </div>

    <div class="panel-subtitle">
      <span v-if="status === 'running'">{{
        td("dpp.integratioTask.currentRunningTasks", "当前运行中 {count} 个任务", {
          count: total,
        })
      }}</span>
      <span v-else>{{ title }}: {{ total }}</span>
      <el-button link type="primary" @click="refresh" class="subtitle-refresh">
        <el-icon><Refresh /></el-icon>
      </el-button>
    </div>

    <div class="panel-content">
      <div
        v-for="task in tasks"
        :key="task.id"
        class="task-card"
        @click="handleTaskClick(task)"
      >
        <div class="task-header">
          <div class="task-icon">
            <img :src="getDatasourceIcon(task.draftJson)" :alt="task.name" />
          </div>
          <div class="task-name">{{ task.name }}</div>
          <StatusTag :status="task.currentStatus || status || 'idle'" />
        </div>

        <div class="task-info">
          <div class="info-row">
            <span class="info-label">{{ td("dpp.integratioTask.currentNodeStage", "当前节点/阶段") }}</span>
            <span class="info-value">{{ task.currentNode || "-" }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">{{ td("dpp.integratioTask.startTime", "开始时间") }}</span>
            <span class="info-value">{{
              parseTime(task.startTime, "{y}-{m}-{d} {h}:{i}:{s}") || "-"
            }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">{{ td("dpp.integratioTask.runDuration", "运行时长") }}</span>
            <span class="info-value duration-green">{{
              task.duration || "-"
            }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">{{ td("dpp.integratioTask.latestLog", "最新日志") }}</span>
          </div>
        </div>
        <div class="task-logs">
          <div class="logs-header"></div>
          <div v-if="fakeLogs.length > 0">
            <div
              v-for="(log, logIndex) in fakeLogs"
              :key="logIndex"
              class="log-item"
            >
              <span class="log-time">{{ log.time }}</span>
              <span :class="['log-level', log.level]">{{ log.level }}</span>
              <span class="log-content">{{ log.content }}</span>
            </div>
          </div>
          <div v-else class="empty-logs">{{ td("dpp.integratioTask.noLogs", "暂无日志") }}</div>
        </div>

        <div class="task-actions">
          <el-button
            link
            type="primary"
            @click.stop="emit('viewRealTimeLog', task)"
          >
            <el-icon class="mr5"><Document /></el-icon>
            {{ td("dpp.integratioTask.viewRealTimeLog", "查看实时日志") }}
          </el-button>
          <el-button
            link
            type="primary"
            @click.stop="emit('viewInstance', task)"
          >
            <el-icon class="mr5"><List /></el-icon>
            {{ td("dpp.integratioTask.viewInstance", "查看实例") }}
          </el-button>
        </div>
      </div>

      <div v-if="loading" class="loading-tip">
        <span>{{ td("dpp.integratioTask.loading", "加载中...") }}</span>
      </div>
      <div v-if="noMore && tasks.length > 0" class="loading-tip">
        <span>{{ td("dpp.integratioTask.allDisplayed", "已显示全部{title}", { title }) }}</span>
      </div>
      <div v-if="tasks.length === 0 && !loading" class="empty-state">
        <div>{{ td("dpp.integratioTask.noDataWithTitle", "暂无{title}", { title }) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup name="RunningTasksPanel">
/**
 * RunningTasksPanel 组件
 * 用于展示运行中的任务列表，支持无限滚动加载、任务详情查看、日志查看、实例查看及刷新功能。
 *
 * @props {
 *   visible: boolean; // 控制面板的显示与隐藏
 *   title: string;    // 面板标题
 *   tasks: Array<Object>; // 任务列表数据
 *   loading: boolean; // 是否正在加载更多任务
 *   total: number;    // 任务总数
 * }
 *
 * @emits {
 *   (event: 'update:visible', value: boolean): void; // 更新面板可见状态
 *   (event: 'viewRealTimeLog', task: Object): void;  // 查看实时日志事件
 *   (event: 'viewInstance', task: Object): void;     // 查看实例事件
 *   (event: 'taskClick', task: Object): void;         // 任务卡片点击事件
 *   (event: 'loadMore'): void;                       // 加载更多任务事件
 *   (event: 'refresh'): void;                        // 刷新任务列表事件
 * }
 */
import { ref, computed, onMounted } from "vue";
import { Refresh } from "@element-plus/icons-vue";
import useDefaultLang from "@/composables/useDefaultLang";
import StatusTag from "./StatusTag.vue";

const { td, locale } = useDefaultLang();

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: "",
  },
  tasks: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  total: {
    type: Number,
    default: 0,
  },
  status: {
    type: String,
    default: "running",
  },
});

const emit = defineEmits([
  "update:visible",
  "viewRealTimeLog",
  "viewInstance",
  "taskClick",
  "loadMore",
  "refresh",
]);
const getDatasourceIcon = (json) => {
  let type = json && JSON.parse(json).taskType;
  switch (type) {
    case "FLINK":
      return new URL("@/assets/images/common/icon-flink-one.svg", import.meta.url).href;
    case "SPARK":
      return new URL("@/assets/images/common/icon-spark-one.svg", import.meta.url).href;
    default:
      return null;
  }
};
const noMore = computed(
  () => props.tasks.length >= props.total || props.total === 0
);

const componentMounted = ref(false);

const panelRef = ref(null); // Declare the ref

onMounted(() => {
  componentMounted.value = true;
});

// Expose this function to the parent
const isScrollable = () => {
  if (!panelRef.value) return false;
  // Check if content height is greater than client height
  return panelRef.value.scrollHeight > panelRef.value.clientHeight;
};
defineExpose({
  isScrollable,
});

function handleTaskClick(task) {
  emit("taskClick", task);
}

function loadMore() {
  if (!componentMounted.value || props.loading || noMore.value) return;
  emit("loadMore");
}

function refresh() {
  emit("refresh");
}

const fakeLogs = computed(() => {
  const logs = [];
  const levels = ["INFO", "WARN", "ERROR"];
  const messages = [
    td("dpp.integratioTask.taskStarted", "任务开始执行"),
    td("dpp.integratioTask.dataLoading", "数据加载中..."),
  ];

  for (let i = 0; i < 2; i++) {
    const date = new Date(Date.now() - i * 60 * 1000);
    const time = date.toLocaleTimeString(locale.value, { hour12: false });
    const level = levels[Math.floor(Math.random() * levels.length)];
    const content = messages[Math.floor(Math.random() * messages.length)];
    logs.unshift({ time, level, content });
  }
  return logs;
});
</script>

<style scoped lang="scss">
.running-tasks-panel {
  position: fixed;
  right: 0;
  top: 94px;
  width: 450px;
  height: calc(100vh - 94px);
  background: #fff;
  border-radius: 2px;
  padding: 20px;
  overflow-y: auto;
  box-shadow: -4px 0 20px rgba(0, 0, 0, 0.08);
  z-index: 100;

  .panel-header {
    padding-bottom: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    border-bottom: 1px solid #f1f5f9;

    .panel-title {
      display: flex;
      align-items: center;
      gap: 8px;

      .title-text {
        font-size: 16px;
        font-weight: 600;
        color: #1e293b;
      }

      .title-count {
        background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
        color: #fff;
        font-size: 12px;
        font-weight: 600;
        padding: 2px 10px;
        border-radius: 10px;
      }
    }

    .panel-actions {
      display: flex;
      gap: 8px;

      .el-button--text {
        font-size: 18px;
        color: #94a3b8;
        padding: 0;
        width: 28px;
        height: 28px;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          color: #3b82f6;
          background: rgba(59, 130, 246, 0.1);
          border-radius: 4px;
        }
      }
    }
  }

  .panel-subtitle {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 12px;
    color: #94a3b8;
    margin-bottom: 16px;

    .subtitle-refresh {
      padding: 0;
      margin: 0;
      height: auto;
      font-size: 14px;
      color: #94a3b8;

      &:hover {
        color: #3b82f6;
      }
    }
  }

  .panel-content {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .task-card {
    background: #fff;
    border-radius: 2px;
    padding: 16px;
    cursor: pointer;
    transition: all 0.2s ease;
    border: 1px solid #e2e8f0;

    &:hover {
      background: #f8fafc;
      border-color: #cbd5e1;
    }

    .task-header {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 14px;

      .task-icon {
        width: 30px;
        height: 30px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        background: #3b82f6;
        color: #fff;
      }

      .task-name {
        flex: 1;
        font-size: 14px;
        font-weight: 600;
        color: #1e293b;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .task-info {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin-bottom: 14px;

      .info-row {
        display: flex;
        align-items: center;
        gap: 8px;

        .info-label {
          font-size: 12px;
          color: #94a3b8;
          flex-shrink: 0;
          width: 90px;
        }

        .info-value {
          font-size: 12px;
          color: #475569;
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;

          &.duration-green {
            color: #22c55e;
          }
        }
      }
    }

    .task-logs {
      background: #f8fafc;
      border-radius: 4px;
      padding: 10px 12px;
      margin-bottom: 14px;
      border: 1px solid #e2e8f0;

      .logs-header {
        margin-bottom: 8px;

        .logs-title {
          font-size: 12px;
          font-weight: 500;
          color: #64748b;
        }
      }

      .logs-title-small {
        font-size: 12px;
        color: #64748b;
        margin-bottom: 6px;
      }

      .logs-loading {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        padding: 8px 0;
        font-size: 12px;
        color: #94a3b8;

        .el-icon {
          font-size: 14px;
        }
      }

      .log-item {
        display: flex;
        align-items: flex-start;
        gap: 8px;
        font-size: 12px;
        margin-bottom: 8px;

        &:last-child {
          margin-bottom: 0;
        }

        .log-time {
          color: #94a3b8;
          flex-shrink: 0;
        }

        .log-level {
          flex-shrink: 0;
          font-weight: 600;

          &.INFO {
            color: #3b82f6;
          }

          &.WARN {
            color: #f59e0b;
          }

          &.ERROR {
            color: #ef4444;
          }
        }

        .log-content {
          color: #475569;
          word-break: break-all;
          flex: 1;
        }
      }

      .empty-logs {
        text-align: center;
        color: #94a3b8;
        font-size: 12px;
      }
    }

    .task-actions {
      display: flex;
      gap: 16px;
      padding-top: 12px;
      border-top: 1px dashed #e2e8f0;

      .el-button--text {
        font-size: 12px;
        color: #3b82f6;
        padding: 0;
      }
    }
  }

  .loading-tip {
    text-align: center;
    padding: 12px;
    font-size: 12px;
    color: #94a3b8;
  }

  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 48px 20px;
    color: #94a3b8;

    div {
      font-size: 14px;
    }
  }
}
</style>
