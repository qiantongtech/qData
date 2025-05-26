<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top" v-if="search">
        <!-- <el-button class="zhankaishouqi" type="primary" :icon="showSearch ? 'ArrowDownBold' : 'ArrowUpBold'" @click="toggleSearch()">{{showSearch ? '收起' : '展开'}}</el-button> -->
        <el-button class="zhankaishouqi" type="primary"  @click="toggleSearch()">
          <span>{{showSearch ? '收起' : '展开'}}</span>
          <el-icon v-if="showSearch" style="margin-left:5px"><ArrowUpBold /></el-icon>
          <el-icon v-else style="margin-left:5px"><ArrowDownBold /></el-icon>
        </el-button>
      </el-tooltip>
    </el-row>
  </div>
</template>

<script setup>
const props = defineProps({
  /* 是否显示检索条件 */
  showSearch: {
    type: Boolean,
    default: true,
  },
  /* 显隐列信息 */
  columns: {
    type: Array,
  },
  /* 是否显示检索图标 */
  search: {
    type: Boolean,
    default: true,
  },
  /* 显隐列类型（transfer穿梭框、checkbox复选框） */
  showColumnsType: {
    type: String,
    default: "checkbox",
  },
  /* 右外边距 */
  gutter: {
    type: Number,
    default: 10,
  },
})

const emits = defineEmits(['update:showSearch', 'queryTable']);

// 显隐数据
const value = ref([]);
// 弹出层标题
const title = ref("显示/隐藏");
// 是否显示弹出层
const open = ref(false);

const style = computed(() => {
  const ret = {};
  if (props.gutter) {
    ret.marginRight = `0px`;
  }
  return ret;
});

// 搜索
function toggleSearch() {
  emits("update:showSearch", !props.showSearch);
}

// 刷新
function refresh() {
  emits("queryTable");
}

// 右侧列表元素变化
function dataChange(data) {
  for (let item in props.columns) {
    const key = props.columns[item].key;
    props.columns[item].visible = !data.includes(key);
  }
}

// 打开显隐列dialog
function showColumn() {
  open.value = true;
}

if (props.showColumnsType == 'transfer') {
  // 显隐列初始默认隐藏列
  for (let item in props.columns) {
    if (props.columns[item].visible === false) {
      value.value.push(parseInt(item));
    }
  }
}

// 勾选
function checkboxChange(event, label) {
  props.columns.filter(item => item.label == label)[0].visible = event;
}

</script>

<style lang='scss' scoped>
:deep(.el-transfer__button) {
  border-radius: 50%;
  display: block;
  margin-left: 0px;
}
:deep(.el-transfer__button:first-child) {
  margin-bottom: 10px;
}
:deep(.el-dropdown-menu__item) {
  line-height: 30px;
  padding: 0 17px;
}
</style>
