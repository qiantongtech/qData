<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="qt-wrap">
    <div
      :class="[config.search ? '' : 'qt-wrap--search']"
      v-if="$slots.search"
      v-show="store.search"
    >
      <slot name="search"></slot>
    </div>
    <div :class="['qt-wrap--content', config.fullContent ? 'full' : '']">
      <div class="qt-wrap--actions" v-if="config.actions.show">
        <div class="data-actions">
          <slot name="actions-data"></slot>
        </div>
        <div class="table-actions" v-if="config.actions.table.show">
          <el-tooltip effect="dark" content="隐藏搜索" placement="top">
            <el-button
              circle
              @click="store.search = !store.search"
              v-show="config.actions.table.search"
            >
              <i class="iconfont icon-a-chaxunxianxing"></i>
            </el-button>
          </el-tooltip>

          <el-tooltip effect="dark" :content="t('common.button.refresh')" placement="top">
            <el-button
              circle
              v-show="config.actions.table.refresh"
              @click="handleRefreshClick"
            >
              <i class="iconfont icon-a-shuaxinxianxing"></i>
            </el-button>
          </el-tooltip>

          <el-tooltip effect="dark" content="显隐列" placement="top">
            <el-dropdown
              trigger="click"
              :hide-on-click="false"
              v-show="config.actions.table.columns"
              popper-class="columns-popper"
            >
              <el-button circle icon="Menu" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="item in props.columns"
                    :key="item.prop"
                  >
                    <el-checkbox
                      v-show="item?.type != 'selection'"
                      :checked="!item.hide"
                      :label="item.label"
                      @change="handleCheckboxChange($event, item)"
                    />
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-tooltip>
        </div>
      </div>
      <div class="qt-wrap--main" v-if="$slots.default">
        <slot name="default" />
      </div>
    </div>
  </div>
</template>

<script setup name="QtWrap">
import { useI18n } from 'vue-i18n'
import { computed, reactive } from "vue";
import { merge } from "lodash-es";

const { t } = useI18n();
const DEFAULT_CONFIG = {
  fullContent: true,
  actions: {
    show: true,
    table: {
      show: true,
      search: true,
      refresh: true,
      columns: true,
    },
  },
};

const props = defineProps({
  config: {
    type: Object,
    default: () => {
      return {};
    },
  },
  columns: {
    type: Array,
    default: () => {
      return [];
    },
  },
  tableRef: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

const config = computed(() => {
  return merge({}, DEFAULT_CONFIG, props.config);
});

const store = reactive({
  search: true,
});

// 刷新
function handleRefreshClick() {
  props.tableRef.getList();
}

// 显隐列
function handleCheckboxChange(checked, item) {
  item.hide = !checked;
}
</script>

<style lang="scss" scoped>
.qt-wrap {
  width: 100%;
  height: 100%;
}
.qt-wrap--search {
  padding: 15px 15px 1px;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 5px 8px #8091a51a;
  margin-bottom: 15px;
}

.qt-wrap--content {
  background-color: #ffffff;
  border-radius: 2px;
}

.qt-wrap--content.full {
  flex: 1;
  padding: 13px 15px;
  min-height: calc(100vh - 250px);
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
}

.qt-wrap--actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.data-actions {
  ::v-deep(.el-button) {
    height: 28px;
    padding: 8px 11px;
    font-size: 12px;
  }
}

.data-actions,
.table-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  ::v-deep(.el-button + .el-button) {
    margin-left: 0;
  }
}
</style>

<style lang="scss">
.columns-popper {
  .el-dropdown-menu__item {
    line-height: 30px;
    padding: 0px 17px;
  }
}
</style>
