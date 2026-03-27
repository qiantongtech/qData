<template>
  <div class="qt-search-bar">
    <el-form
      ref="formRef"
      :model="props.params"
      :inline="true"
      @submit.prevent
      v-bind="props.config?.form"
    >
      <el-form-item
        :label="item.label"
        :prop="item.prop"
        v-for="(item, index) in props.items"
        :key="item.prop"
        v-bind="getFormItemProps(item)"
        v-show="index < props.visibleCount ? true : store.expand"
      >
        <!-- 输入框 -->
        <el-input
          class="search-content"
          v-if="item.component.is == 'input'"
          v-model="props.params[item.prop]"
          clearable
          :placeholder="`请输入${item.label}`"
          v-bind="item.component"
          @keyup.enter="handleQueryClick"
        />

        <!-- 下拉框 -->
        <el-select
          class="search-content"
          v-if="item.component.is == 'select'"
          v-model="props.params[item.prop]"
          clearable
          :placeholder="`请选择${item.label}`"
          v-bind="item.component"
        >
          <el-option
            v-for="(option, index) in item.component.options"
            :key="index"
            v-bind="option"
          />
        </el-select>

        <!-- 时间选择器 -->
        <el-date-picker
          class="search-content"
          v-if="item.component.is == 'date-picker'"
          v-model="props.params[item.prop]"
          :type="item.component.type || 'date'"
          clearable
          :placeholder="`请选择${item.label}`"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          v-bind="item.component"
        />

        <!-- 树形选择框 -->
        <el-tree-select
          class="search-content"
          v-if="item.component.is == 'tree-select'"
          v-model="props.params[item.prop]"
          clearable
          :placeholder="`请选择${item.label}`"
          v-bind="item.component"
        />
      </el-form-item>
      <el-form-item class="search-btns">
        <el-button plain type="primary" @click="handleQueryClick">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="handleResetClick">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
        <el-button
          plain
          type="primary"
          v-if="store.length > props.visibleCount"
          @click="store.expand = !store.expand"
          class="extend-btn"
        >
          <svg-icon v-if="store.expand" icon-class="toggle" />
          <svg-icon v-else icon-class="expand" />
          <span>{{ store.expand ? "折叠" : "展开" }}</span>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup name="QtSearchBar">
import { reactive, ref } from "vue";

const props = defineProps({
  params: {
    type: Object,
    required: true,
  },
  config: {
    type: Object,
    default: () => {
      return {};
    },
  },
  items: {
    type: Array,
    required: true,
  },
  tableRef: {
    type: Object,
    default: () => {
      return {};
    },
  },
  visibleCount: {
    type: Number,
    default: 3,
  },
});

const emits = defineEmits(["query"]);

const formRef = ref(null);

const store = reactive({
  expand: false,
  length: props.items.length,
});

// 过滤form参数
function getFormItemProps(item) {
  const { component, ...data } = item;
  return data;
}

// 查询
function handleQueryClick() {
  emits("query");
  props.tableRef?.getList();
}

// 重置
function handleResetClick() {
  formRef.value.resetFields();
  emits("reset");
  props.tableRef?.resetQuery();
}
</script>

<style lang="scss" scoped>
.search-content {
  width: 210px;
}

.search-btns {
  .el-button {
    height: 30px;
    padding: 8px 11px;
    font-size: 12px;
  }
}
.extend-btn {
  .svg-icon {
    font-size: 12px;
    margin-right: 6px;
  }
}
</style>
