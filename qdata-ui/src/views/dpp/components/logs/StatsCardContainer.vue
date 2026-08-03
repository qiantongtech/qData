<!-- 统计栏 -->
<template>
  <div class="stats-card-container" v-loading="loading">
    <div class="stats-card-layout">
      <div class="stats-card-list">
        <div
          v-for="(item, index) in cards"
          :key="index"
          class="stats-card-item"
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
              <div class="stats-content">
                <div class="stats-name">{{ item.name }}</div>
                <div class="stats-value">
                  {{ item.value
                  }}<span v-if="item.unit" class="stats-unit">{{
                    item.unit
                  }}</span>
                </div>
              </div>
              <div v-if="index < cards.length - 1" class="stats-divider"></div>
            </div>
          </el-tooltip>
        </div>
      </div>
      <div class="stats-time">
        {{ td("dpp.integratioTask.statsDeadline", "数据统计截止") }}:
        {{ statsTime }}
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
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(200px, 300px);
  grid-template-areas: "cards time";
  align-items: center;
  gap: 20px;
  padding: 0px 20px;

  .stats-card-list {
    grid-area: cards;
    min-width: 0;
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 20px;
    align-items: center;
  }

  .stats-card-item {
    min-width: 0;
  }

  .stats-card {
    display: flex;
    align-items: center;
    gap: 10px;
    width: 100%;
    height: 62px;
    padding: 12px 16px;
    background: transparent;
    border-radius: 10px;
    cursor: pointer;
    transition: all 0.25s ease;
    position: relative;

    &:hover {
      background: transparent;
    }

    &.active {
      background: transparent;
      box-shadow: none;
    }

    .stats-icon {
      width: 38px;
      height: 38px;
      flex-shrink: 0;
    }

    .stats-content {
      min-width: 0;
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 2px;

      .stats-name {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.45);
        font-weight: 400;
        font-family: PingFang SC;
      }

      .stats-value {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        font-size: 20px;
        font-weight: bold;
        color: #262626;

        .stats-unit {
          font-size: 14px;
          font-weight: 500;
          color: rgba(0, 0, 0, 0.65);
          margin-left: 2px;
          font-family: PingFang SC;
        }
      }
    }

    &.is-loading {
      pointer-events: none;
      cursor: not-allowed;
      opacity: 0.7;
    }
    .stats-divider {
      width: 1px;
      height: 40px;
      background: #e2e8f0;
      margin-left: auto;
      flex-shrink: 0;
    }
  }

  .stats-time {
    grid-area: time;
    justify-self: end;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    flex-wrap: wrap;
    gap: 8px;
    row-gap: 2px;
    width: 100%;
    padding-left: 16px;
    border-left: 1px solid #e2e8f0;
    font-size: 14px;
    line-height: 20px;
    text-align: right;
    color: rgba(0, 0, 0, 0.45);
    font-family: PingFang SC;
    overflow-wrap: anywhere;
    white-space: normal;
  }
}

@container (max-width: 1080px) {
  .stats-card-layout {
    grid-template-columns: minmax(0, 1fr) minmax(170px, 220px);
    grid-template-areas: "cards time";
    align-items: center;
    gap: 12px;
    padding: 14px 20px 16px;

    .stats-card-list {
      grid-template-columns: repeat(4, minmax(0, 1fr));
      gap: 12px;
    }

    .stats-time {
      padding-left: 12px;
      font-size: 13px;
    }
  }
}

@container (max-width: 700px) {
  .stats-card-layout {
    grid-template-columns: minmax(0, 1fr) minmax(136px, 160px);
    gap: 10px;
    padding: 12px 16px;

    .stats-card-list {
      grid-template-columns: repeat(4, minmax(0, 1fr));
      gap: 10px;
    }

    .stats-card {
      gap: 8px;
      padding: 10px 8px;

      .stats-icon {
        width: 32px;
        height: 32px;
      }

      .stats-content {
        .stats-value {
          font-size: 18px;

          .stats-unit {
            font-size: 13px;
          }
        }
      }

      .stats-divider {
        display: none;
      }
    }

    .stats-time {
      font-size: 13px;
      padding-left: 10px;
    }
  }
}

@container (max-width: 560px) {
  .stats-card-layout {
    grid-template-columns: minmax(0, 1fr);
    grid-template-areas:
      "time"
      "cards";
    align-items: stretch;

    .stats-card-list {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    .stats-time {
      justify-self: start;
      justify-content: flex-start;
      width: auto;
      max-width: 100%;
      padding-left: 0;
      border-left: none;
      text-align: left;
    }
  }
}

@container (max-width: 420px) {
  .stats-card-layout {
    .stats-card-list {
      grid-template-columns: 1fr;
    }

    .stats-card {
      .stats-icon {
        width: 32px;
        height: 32px;
      }
    }
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
