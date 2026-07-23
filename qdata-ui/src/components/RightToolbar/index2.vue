<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-tooltip class="item" effect="dark" :content="showSearch ? t('components.rightToolbar.hideSearch') : t('components.rightToolbar.showSearch')" placement="top" v-if="search">
        <!-- <el-button class="zhankaishouqi" type="primary" :icon="showSearch ? 'ArrowDownBold' : 'ArrowUpBold'" @click="toggleSearch()">{{showSearch ? t('components.rightToolbar.collapse') : t('components.rightToolbar.expand')}}</el-button> -->
        <el-button class="zhankaishouqi" type="primary"  @click="toggleSearch()">
          <span>{{showSearch ? t('components.rightToolbar.collapse') : t('components.rightToolbar.expand')}}</span>
          <el-icon v-if="showSearch" style="margin-left:5px"><ArrowUpBold /></el-icon>
          <el-icon v-else style="margin-left:5px"><ArrowDownBold /></el-icon>
        </el-button>
      </el-tooltip>
    </el-row>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'

const { t } = useI18n();
const props = defineProps({
  /* Whether to display search conditions */
  showSearch: {
    type: Boolean,
    default: true,
  },
  /* Show and hide column information */
  columns: {
    type: Array,
  },
  /* Whether to display the search icon */
  search: {
    type: Boolean,
    default: true,
  },
  /* Show and hide column types (transfer shuttle box, checkbox checkbox) */
  showColumnsType: {
    type: String,
    default: "checkbox",
  },
  /* right margin */
  gutter: {
    type: Number,
    default: 10,
  },
})

const emits = defineEmits(['update:showSearch', 'queryTable']);

// Explicit data
const value = ref([]);
// Popup layer title
const title = ref(t('components.rightToolbar.showHide'));
// Whether to display popup layer
const open = ref(false);

const style = computed(() => {
  const ret = {};
  if (props.gutter) {
    ret.marginRight = `0px`;
  }
  return ret;
});

// Search
function toggleSearch() {
  emits("update:showSearch", !props.showSearch);
}

// Refresh
function refresh() {
  emits("queryTable");
}

// Changes in list elements on the right
function dataChange(data) {
  for (let item in props.columns) {
    const key = props.columns[item].key;
    props.columns[item].visible = !data.includes(key);
  }
}

// Open the visible column dialog
function showColumn() {
  open.value = true;
}

if (props.showColumnsType == 'transfer') {
  // Show and hide columns. The columns are initially hidden by default.
  for (let item in props.columns) {
    if (props.columns[item].visible === false) {
      value.value.push(parseInt(item));
    }
  }
}

// Check
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
