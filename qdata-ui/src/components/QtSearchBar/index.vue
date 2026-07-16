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
        <!-- Input box -->
        <el-input
          class="search-content"
          v-if="item.component.is == 'input'"
          v-model="props.params[item.prop]"
          clearable
          :placeholder="t('components.qtSearchBar.inputPlaceholder', { label: item.label })"
          v-bind="item.component"
          @keyup.enter="handleQueryClick"
        />

        <!-- drop down box -->
        <el-select
          class="search-content"
          v-if="item.component.is == 'select'"
          v-model="props.params[item.prop]"
          clearable
          :placeholder="t('components.qtSearchBar.selectPlaceholder', { label: item.label })"
          v-bind="item.component"
        >
          <el-option
            v-for="(option, index) in item.component.options"
            :key="index"
            v-bind="option"
          />
        </el-select>

        <!-- time picker -->
        <el-date-picker
          class="search-content"
          v-if="item.component.is == 'date-picker'"
          v-model="props.params[item.prop]"
          :type="item.component.type || 'date'"
          clearable
          :placeholder="t('components.qtSearchBar.selectPlaceholder', { label: item.label })"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          v-bind="item.component"
        />

        <!-- tree selection box -->
        <el-tree-select
          class="search-content"
          v-if="item.component.is == 'tree-select'"
          v-model="props.params[item.prop]"
          clearable
          :placeholder="t('components.qtSearchBar.selectPlaceholder', { label: item.label })"
          v-bind="item.component"
        />
      </el-form-item>
      <el-form-item class="search-btns">
        <el-button plain type="primary" @click="handleQueryClick">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ t('common.button.query') }}
        </el-button>
        <el-button @click="handleResetClick">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ t('common.button.reset') }}
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
          <span>{{ store.expand ? t('common.button.fold') : t('common.button.expand') }}</span>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup name="QtSearchBar">
import { useI18n } from 'vue-i18n'
import { reactive, ref } from "vue";

const { t } = useI18n();
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

// Filter form parameters
function getFormItemProps(item) {
  const { component, ...data } = item;
  return data;
}

// Query
function handleQueryClick() {
  emits("query");
  props.tableRef?.getList();
}

// reset
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
