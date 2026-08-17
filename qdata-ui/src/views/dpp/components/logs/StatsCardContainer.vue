<!-- 统计栏 -->
<template>
  <div class="stats-card-container" v-loading="loading">
    <div class="stats-card-layout">
      <div class="stats-card-list">
        <div
          v-for="(item, index) in cards"
          :key="index"
          class="stats-card-item-wrapper"
        >
          <el-tooltip
            :disabled="!item.tip"
            effect="light"
            placement="top"
            popper-class="stats-card-tooltip"
            :show-after="150"
            :offset="-8"
          >
            <template #content>
              <div class="stats-card-tooltip__content">
                <div class="stats-card-tooltip__desc">{{ item.tip }}</div>
              </div>
            </template>
            <div
              class="stats-card"
              :class="{
                active: selectedIndex === index && showPanel,
                'is-loading': loading,
              }"
              @click="handleCardClick(index)"
            >
              <img
                v-if="item.iconSrc || item.icon"
                class="stats-icon"
                :src="item.iconSrc || getIconPath(item.icon)"
                :alt="item.name"
              />

                <div class="stats-name">{{ item.name }}</div>
                <div class="stats-value">
                  {{ item.value
                  }}<span v-if="item.unit" class="stats-unit">{{
                    item.unit
                  }}</span>
                </div>
            </div>
          </el-tooltip>
          <div v-if="index < cards.length - 1" class="stats-divider"></div>
        </div>
      </div>
      <div class="stats-time-wrapper">
         <span class="stats-time">{{ td("dpp.integratioTask.statsDeadline", "数据统计截止") }}:
        {{ statsTime }}
           </span>
        <el-button
          link
          type="primary"
          class="stats-refresh-btn"
          @click.stop="handleRefresh"
          :disabled="loading"
        >
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup name="StatsCardContainer">
/**
 * StatsCardContainer 组件
 * 用于展示一系列统计卡片，支持卡片点击交互和数据刷新功能。
 *
 * @props {
 *   cards: Array<{ icon?: string; name: string; value: any; unit?: string }>; // 卡片数据列表
 *   selectedIndex: number; // 当前选中的卡片索引
 *   showPanel: boolean;    // 是否显示面板（影响选中卡片样式）
 *   statsTime: string;     // 统计数据截止时间
 * }
 *
 * @emits {
 *   (event: 'cardClick', index: number): void; // 卡片点击事件，返回点击的卡片索引
 *   (event: 'refresh'): void;                  // 刷新数据事件
 * }
 */
import { Refresh } from "@element-plus/icons-vue";
import useDefaultLang from "@/composables/useDefaultLang";
import clockIcon from "@/assets/images/system/stats/clock.svg";
import warningIcon from "@/assets/images/system/stats/warning.svg";
import calendarIcon from "@/assets/images/system/stats/calendar.svg";
import checkIcon from "@/assets/images/system/stats/check.svg";

const { td } = useDefaultLang();

const props = defineProps({
  cards: {
    type: Array,
    default: () => [],
  },
  selectedIndex: {
    type: Number,
    default: -1,
  },
  showPanel: {
    type: Boolean,
    default: false,
  },
  statsTime: {
    type: String,
    default: "",
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["cardClick", "refresh"]);

const iconPathMap = {
  clock: clockIcon,
  warning: warningIcon,
  calendar: calendarIcon,
  check: checkIcon,
};

function getIconPath(iconName) {
  return iconPathMap[iconName] || iconPathMap.clock;
}

function handleCardClick(index) {
  if (props.loading) return; // Prevent click during loading
  emit("cardClick", index);
}

function handleRefresh() {
  emit("refresh");
}
</script>

<style scoped lang="scss">
.stats-card-container {
  container-type: inline-size;
  margin-bottom: 16px;
  background: #fff;
  border-radius: 2px;
}

.stats-card-layout {
  position: relative;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  gap: 16px;
}

.stats-card-list {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: flex-start;
  gap: 16px 0;
  flex: 1;
}

.stats-card-item-wrapper {
  display: flex;
  align-items: center;
  flex: 1;
  max-width: 280px;
}

.stats-card {
  display: flex;
  align-items: center;
  background: transparent;
  cursor: pointer;
  transition: all 0.25s ease;
  position: relative;

  &:hover {
    opacity: 0.8;
  }

  &.active {
    opacity: 1;
  }

  .stats-icon {
    width: 14px;
    height: 14px;
    flex-shrink: 0;
    margin-right: 10px;
  }

  .stats-name {
    color: rgba(0, 0, 0, 0.45);
    font-size: 14px;
    font-weight: normal;
    white-space: nowrap;
    line-height: 24px;
    font-family: PingFang SC, PingFangSC-Regular, sans-serif;
  }

  .stats-value {
    display: flex;
    align-items: baseline;
    color: rgba(0, 0, 0, 0.75);
    font-size: 14px;
    font-weight: 500;
    white-space: nowrap;
    line-height: 24px;
    font-family: PingFangSC-Medium, PingFang SC, Microsoft YaHei, sans-serif;
    margin-left: 4px;

    .stats-unit {
      font-size: 14px;
      font-weight: 500;
      color: rgba(0, 0, 0, 0.65);
      margin-left: 2px;
      font-family: PingFangSC-Medium, PingFang SC, Microsoft YaHei, sans-serif;
    }
  }

  &.is-loading {
    pointer-events: none;
    cursor: not-allowed;
    opacity: 0.7;
  }
}

.stats-divider {
  width: 1px;
  height: 16px;
  background: #e2e8f0;
  margin: 0 auto;
  flex-shrink: 0;
}

.stats-time-wrapper {
  display: flex;
  align-items: center;
  margin-left: auto;

  .stats-time {
    color: rgba(0, 0, 0, 0.45);
    font-size: 14px;
    font-weight: normal;
    white-space: nowrap;
    line-height: 24px;
    font-family: PingFang SC, PingFangSC-Regular, sans-serif;
    margin-right: 8px;
  }
}

@container (max-width: 1080px) {
  .stats-card-layout {
    padding: 10px 16px;
  }
}

@container (max-width: 700px) {
  .stats-card-layout {
    padding: 10px 16px;
  }
  .stats-card-list {
    gap: 12px 16px;
  }
  .stats-divider {
    display: none;
  }
  .stats-card {
    .stats-icon {
      width: 14px;
      height: 14px;
    }
    .stats-value {
      font-size: 14px;
    }
  }
}

@container (max-width: 560px) {
  .stats-card-layout {
    flex-direction: column;
    align-items: flex-start;
  }
  .stats-time-wrapper {
    margin-left: 0;
    margin-top: 8px;
  }
}
</style>

<style lang="scss">
.stats-card-tooltip.el-popper {
  color: #1f2329;
  background: #ffffff;
  border: 1px solid #dcdfe6;
  box-shadow: 0 2px 8px rgba(31, 35, 41, 0.12);

  .el-popper__arrow::before {
    background: #ffffff;
    border-color: #dcdfe6;
  }
}

.stats-card-tooltip__content {
  max-width: 280px;
  line-height: 1.6;
  word-break: break-word;
}

.stats-card-tooltip__desc {
  color: #4b5563;
}
</style>
