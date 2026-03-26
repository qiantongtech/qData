<template>
  <div class="layer-info-card">
    <h3 class="layer-info-card-title">
      <!-- <i class="iconfont-mini icon-a-zu22377 mr5 layer-info-card-icon"></i> -->
      {{ layer.name }}
      <span v-if="layer.engName">({{ layer.engName }})</span>
    </h3>
    <div class="layer-info-card-body">
      <div class="layer-info-card-row">
        <span class="layer-info-card-label layer-info-card-label--short">
          业务定义：
        </span>
        <span
          class="layer-info-card-value text-ellipsis"
          :title="layer.description"
          >{{ layer.description || "暂无描述" }}</span
        >
      </div>
      <div v-if="layer.engName" class="layer-info-card-row">
        <span class="layer-info-card-label layer-info-card-label--long">
          命名核心标准：
        </span>
        <span class="layer-info-card-badge">
          表名前缀固定为 {{ layer.engName }}_
        </span>
      </div>
    </div>
  </div>
</template>

<script setup name="layerInfoCard">
import { computed } from "vue";

const props = defineProps({
  layer: {
    type: Object,
    required: true,
  },
});

const layer = computed(() => props.layer || {});
</script>

<style scoped lang="scss">
.layer-info-card {
  padding: 15px;
  background-color: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.layer-info-card-title {
  display: flex;
  align-items: center;
  margin-top: 0;
  color: #409eff;
}

.layer-info-card-icon {
  font-size: 18px;
}

.layer-info-card-body {
  display: flex;
  flex-direction: column;
  gap: 10px;
  font-size: 13px;
  color: #606266;
}

.layer-info-card-row {
  display: flex;
  align-items: center;
}

.layer-info-card-label {
  color: #909399;
}

.layer-info-card-label--short {
  min-width: 70px;
}

.layer-info-card-label--long {
  min-width: 90px;
}

.layer-info-card-value {
  flex: 1 1 auto;
}

.layer-info-card-badge {
  padding: 2px 8px;
  color: #1890ff;
  background: #e8f4ff;
  border-radius: 4px;
}
</style>
