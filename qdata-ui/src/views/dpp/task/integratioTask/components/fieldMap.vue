<template>
<!-- 输出组件的字段映射   -->
  <div class="container">
    <el-form label-position="left" label-width="80px" :model="readerForm">
      <el-row>
        <!-- 左侧拖拽列表 -->
        <el-col :span="8" :offset="3">
          <p>来源表字段：</p>
          <!-- 全选复选框 -->
          <el-checkbox style="margin-top: -20px" v-model="leftSelectAll" :disabled="info"
            v-if="readerForm.tableFields.length > 0">全选</el-checkbox>
          <draggable tag="div" class="draggable-list" :list="readerForm.tableFields" animation="300" item-key="id" :disabled="info">
            <template v-slot:item="{ element, index }">
              <div class="draggable-item fixed-height">
                <div class="custom-draggable-item">
                  <el-checkbox class="checkbox" v-model="element.isChecked" @change="handleCheckedChange(index)"
                    :disabled="info">
                    <span class="column-name">{{ element.columnName }}</span>
                  </el-checkbox>
                  <img src="../../../../../assets/system/images/dpp/mop.png" class="icon" />
                </div>
              </div>
            </template>
          </draggable>
        </el-col>

        <!-- 中间箭头列 -->
        <el-col :span="4">
          <div class="arrow-container">
            <div v-for="(arrow, index) in arrowRows" :key="index" class="arrow-row fixed-height">
              <div class="circle"></div>
              <div class="arrow-line" :class="{ 'show-arrow': arrow.showArrow }"></div>
              <div class="circle"></div>
            </div>
          </div>
        </el-col>

        <!-- 右侧拖拽列表 -->
        <el-col :span="8">
          <p>目标字段：</p>
          <!-- 全选复选框，仅当不是 hdfs 且有字段时显示 -->
          <el-checkbox v-if="readerForm.toColumnsList.length > 0" :disabled="type == 'hdfs' || info"
            v-model="rightSelectAll" style="margin-top: -20px">
            全选
          </el-checkbox>
          <!-- 拖拽区域 -->
          <draggable tag="div" class="draggable-list" :list="readerForm.toColumnsList" animation="300" item-key="id" :disabled="info">
            <template v-slot:item="{ element, index }">
              <div class="draggable-item fixed-height">
                <div class="custom-draggable-item">
                  <!-- 使用 tooltip 提示禁用原因 -->
                  <el-tooltip v-if="type === 'hdfs'" content="HDFS 类型不可勾选" placement="top">
                    <el-checkbox class="checkbox" v-model="element.isChecked" :disabled="true">
                      <span class="column-name">{{ element.columnName }}</span>
                    </el-checkbox>
                  </el-tooltip>
                  <!-- 正常复选框 -->
                  <el-checkbox v-else class="checkbox" v-model="element.isChecked" @change="handleCheckedChange(index)"
                    :disabled="info">
                    <span class="column-name">{{ element.columnName }}</span>
                  </el-checkbox>

                  <!-- 图标 -->
                  <img src="../../../../../assets/system/images/dpp/mop.png" class="icon" />
                </div>
              </div>
            </template>
          </draggable>
        </el-col>

      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import { ref, watch, computed, defineExpose } from "vue";
import draggable from "vuedraggable";

// 定义 props
const props = defineProps({
  tableFields: {
    type: Array,
    default: () => [],
  },
  toColumnsList: {
    type: Array,
    default: () => [],
  },
  type: {
    type: String,
    default: '',
  },
  info: {
    type: Boolean,
    default: false,
  },
});

// 内部状态
const readerForm = ref({
  tableFields: [],
  toColumnsList: [],
});

// 初始化表单数据
const updateReaderForm = () => {
  readerForm.value.tableFields = Array.isArray(props.tableFields)
    ? props.tableFields.map((item) => ({
      ...item,
      isChecked: item.isChecked ?? false,
    }))
    : [];
  readerForm.value.toColumnsList = Array.isArray(props.toColumnsList)
    ? props.toColumnsList.map((item) => ({
      ...item,
      isChecked: props.type == 'hdfs' ? true : (item.isChecked ?? false),
    }))
    : [];
};

updateReaderForm();

// 监听 props 变化
watch(
  () => props.tableFields,
  (newVal) => {
    readerForm.value.tableFields = (newVal ?? []).map((item) => ({
      ...item,
      isChecked: item.isChecked ?? false,
    }));
  },
  { deep: true }
);

watch(
  () => props.toColumnsList,
  (newVal) => {
    readerForm.value.toColumnsList = (newVal ?? []).map((item) => ({
      ...item,
      isChecked: item.isChecked ?? false,
    }));
  },
  { deep: true }
);

// 判断对应行左右项是否都选中
const shouldShowArrow = (index) => {
  const fromChecked = readerForm.value.tableFields[index]?.isChecked || false;
  const toChecked = readerForm.value.toColumnsList[index]?.isChecked || false;
  return fromChecked && toChecked;
};

// 计算属性：每一行是否显示箭头
const arrowRows = computed(() => {
  // 获取较短的列表长度
  const length = Math.min(
    readerForm.value.toColumnsList.length,
    readerForm.value.tableFields.length
  );

  // 根据较短的列表来遍历
  return Array.from({ length }).map((_, index) => ({
    showArrow: shouldShowArrow(index),
  }));
});

// 全选计算属性：左侧
const leftSelectAll = computed({
  get() {
    return readerForm.value.tableFields.every((item) => item.isChecked);
  },
  set(value) {
    readerForm.value.tableFields.forEach((item) => {
      item.isChecked = value;
    });
  },
});

// 全选计算属性：右侧
const rightSelectAll = computed({
  get() {
    return readerForm.value.toColumnsList.every((item) => item.isChecked);
  },
  set(value) {
    readerForm.value.toColumnsList.forEach((item) => {
      item.isChecked = value;
    });
  },
});

const handleCheckedChange = (index) => {
  // 当单个项选中状态变化时，leftSelectAll 和 rightSelectAll 会自动通过计算属性更新
};

defineExpose({
  getColumns: () => ({
    fromColumns: readerForm.value.tableFields,
    toColumns: readerForm.value.toColumnsList,
  }),
});
</script>

<style lang="scss" scoped>
.container {
  margin-top: -20px;
}

/* 左右列表容器 */
.draggable-list {
  display: flex;
  flex-direction: column;
}

/* 固定高度 */
.fixed-height {
  height: 40px; // 根据需要调整固定高度
}

/* 拖拽项样式 */
.draggable-item {
  box-sizing: border-box;
  padding: 6px;
  background-color: #fdfdfd;
  border: 1px solid #eee;
  border-radius: 4px;
  margin-bottom: 0;
  cursor: move;
}

.custom-draggable-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.checkbox {
  width: 100%;
}

.icon {
  width: 16px;
  height: 16px;
}

/* 中间箭头列 */
.arrow-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  height: 100%;
  margin-top: 75px;
}

/* 每一行 */
.arrow-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0;
}

/* 小圆点 */
.circle {
  width: 10px;
  height: 10px;
  background-color: #0095ff;
  border-radius: 50%;
}

/* 箭头线：默认隐藏 */
.arrow-line {
  display: none;
}

/* 当条件满足时，显示整条横线和箭头 */
.arrow-line.show-arrow {
  display: block;
  width: 160px;
  height: 2px;
  background-color: #0095ff;
  position: relative;
}

/* 箭头尖 */
.arrow-line.show-arrow::after {
  content: "";
  position: absolute;
  right: 0;
  top: -3px;
  border-top: 4px solid transparent;
  border-bottom: 6px solid transparent;
  border-left: 8px solid #0095ff;
  transition: opacity 0.2s;
}
</style>
