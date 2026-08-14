<template>
  <el-tag
    :type="currentStatusInfo.type"
    :size="size"
    :class="[
      currentStatusInfo.className,
      { 'status-tag--small': size === 'small' },
    ]"
  >
    <span class="tag-content">
      <el-icon
        v-if="currentStatusInfo.isRunning"
        class="is-loading status-icon"
      >
        <component :is="currentStatusInfo.icon" />
      </el-icon>
      <img v-else :src="currentStatusInfo.icon" class="status-icon" />
      <span class="status-text">{{ currentStatusInfo.name }}</span>
    </span>
  </el-tag>
</template>

<script setup>
import { computed } from "vue";
import { Loading } from "@element-plus/icons-vue";
import useDefaultLang from "@/composables/useDefaultLang";
import iconSuccess from "@/assets/dpp/etl/instance/success.svg";
import iconFailed from "@/assets/dpp/etl/instance/failed.svg";
import iconIdle from "@/assets/dpp/etl/instance/idle.svg";

const { td } = useDefaultLang();

const props = defineProps({
  status: {
    type: String,
    default: "idle",
  },
  size: {
    type: String,
    default: "default",
  },
});

const statusMap = computed(() => ({
  running: {
    type: "primary",
    icon: Loading,
    isRunning: true,
    name: td("dpp.integratioTask.running", "运行中"),
  },
  success: {
    type: "success",
    icon: iconSuccess,
    name: td("dpp.integratioTask.success", "成功"),
  },
  failed: {
    type: "danger",
    icon: iconFailed,
    name: td("dpp.integratioTask.failed", "失败"),
  },
  idle: {
    type: "",
    className: "status-tag--idle",
    icon: iconIdle,
    name: td("dpp.integratioTask.idle", "空闲"),
  },
}));

const currentStatusInfo = computed(() => {
  const canonicalStatus = statusMap.value[props.status] ? props.status : "idle";
  return statusMap.value[canonicalStatus];
});
</script>

<style scoped lang="scss">
.tag-content {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.status-text {
  position: relative;
  //top: 1px;
}

.status-icon {
  width: 15px;
  height: 15px;
  &.is-loading {
    animation: rotating 2s linear infinite;
  }
}

.status-tag--idle {
  --el-tag-bg-color: #f3e8ff;
  --el-tag-border-color: #d8b4fe;
  --el-tag-text-color: #7e22ce;
}

:deep(.el-tag) {
  display: inline-flex;
  align-items: center;
  vertical-align: middle;
}

:deep(.el-tag__content) {
  display: inline-flex;
  align-items: center;
  vertical-align: middle;
}

@keyframes rotating {
  0% {
    transform: rotateZ(0deg);
  }
  100% {
    transform: rotateZ(360deg);
  }
}

.status-tag--small {
  :deep(.el-tag) {
    height: 20px;
    padding: 3px 6px;
    font-size: 10px !important;
    line-height: 18px;
    vertical-align: middle;
  }
  :deep(.el-tag__content) {
    display: inline-flex;
    align-items: center;
    vertical-align: middle;
  }
  .tag-content {
    gap: 0;
  }
  .status-text {
    font-size: 12px;
  }
  .status-icon {
    width: 12px;
    height: 12px;
    margin-right: 3px;
  }
}
</style>
