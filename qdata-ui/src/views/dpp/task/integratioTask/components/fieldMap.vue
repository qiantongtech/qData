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
<!-- Field mapping for output components   -->
  <div class="container">
    <el-form label-width="80px" :model="readerForm">
      <el-row>
        <!-- Drag list on the left -->
        <el-col :span="8" :offset="3">
          <p>{{ td('dpp.integration.sourceTableFields', 'Source Table Fields:') }}</p>
          <!-- Select all checkbox -->
          <el-checkbox style="margin-top: -20px" v-model="leftSelectAll" :disabled="info"
            v-if="readerForm.tableFields.length > 0">{{ td('dpp.integration.selectAll', 'Select All') }}</el-checkbox>
          <draggable tag="div" class="draggable-list" :list="readerForm.tableFields" animation="300" item-key="id" :disabled="info">
            <template v-slot:item="{ element, index }">
              <div class="draggable-item fixed-height">
                <div class="custom-draggable-item">
                  <el-checkbox class="checkbox" v-model="element.isChecked" @change="handleCheckedChange(index)"
                    :disabled="info">
                    <span class="column-name">{{ element.columnName }}</span>
                  </el-checkbox>
                  <img src="../../../../../assets/images/common/dpp/img-mop.png" class="icon" />
                </div>
              </div>
            </template>
          </draggable>
        </el-col>

        <!-- middle arrow column -->
        <el-col :span="4">
          <div class="arrow-container">
            <div v-for="(arrow, index) in arrowRows" :key="index" class="arrow-row fixed-height">
              <div class="circle"></div>
              <div class="arrow-line" :class="{ 'show-arrow': arrow.showArrow }"></div>
              <div class="circle"></div>
            </div>
          </div>
        </el-col>

        <!-- Drag list on the right -->
        <el-col :span="8">
          <p>{{ td('dpp.integration.targetFields', 'Target Fields:') }}</p>
          <!-- Select all checkbox, only displayed if it is not hdfs and there are fields -->
          <el-checkbox v-if="readerForm.toColumnsList.length > 0" :disabled="type == 'hdfs' || info"
            v-model="rightSelectAll" style="margin-top: -20px">
            {{ td('dpp.integration.selectAll', 'Select All') }}
          </el-checkbox>
          <!-- drag area -->
          <draggable tag="div" class="draggable-list" :list="readerForm.toColumnsList" animation="300" item-key="id" :disabled="info">
            <template v-slot:item="{ element, index }">
              <div class="draggable-item fixed-height">
                <div class="custom-draggable-item">
                  <!-- Use tooltip to prompt the reason for disabling -->
                  <el-tooltip v-if="type === 'hdfs'" :content="td('dpp.integration.hdfsNoSelect', 'HDFS type cannot be selected')" placement="top">
                    <el-checkbox class="checkbox" v-model="element.isChecked" :disabled="true">
                      <span class="column-name">{{ element.columnName }}</span>
                    </el-checkbox>
                  </el-tooltip>
                  <!-- normal checkbox -->
                  <el-checkbox v-else class="checkbox" v-model="element.isChecked" @change="handleCheckedChange(index)"
                    :disabled="info">
                    <span class="column-name">{{ element.columnName }}</span>
                  </el-checkbox>

                  <!-- icon -->
                  <img src="../../../../../assets/images/common/dpp/img-mop.png" class="icon" />
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
import useDefaultLang from "@/composables/useDefaultLang";
import draggable from "vuedraggable";
const { td } = useDefaultLang();

// Define props
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

// internal state
const readerForm = ref({
  tableFields: [],
  toColumnsList: [],
});

// Initialize form data
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

// Monitor props changes
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

// Determine whether the left and right items of the corresponding row are selected
const shouldShowArrow = (index) => {
  const fromChecked = readerForm.value.tableFields[index]?.isChecked || false;
  const toChecked = readerForm.value.toColumnsList[index]?.isChecked || false;
  return fromChecked && toChecked;
};

// Computed property: whether to display arrows in each row
const arrowRows = computed(() => {
  // Get shorter list length
  const length = Math.min(
    readerForm.value.toColumnsList.length,
    readerForm.value.tableFields.length
  );

  // Iterate over shorter lists
  return Array.from({ length }).map((_, index) => ({
    showArrow: shouldShowArrow(index),
  }));
});

// Select all computed properties: left
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

// Select all computed properties: right
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
  // When the selected state of a single item changes, leftSelectAll and rightSelectAll are automatically updated through calculated properties.
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

/* Left and right list containers */
.draggable-list {
  display: flex;
  flex-direction: column;
}

/* fixed height */
.fixed-height {
  height: 40px; // Adjust fixed height as needed
}

/* Drag item style */
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

/* middle arrow column */
.arrow-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  height: 100%;
  margin-top: 75px;
}

/* each line */
.arrow-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0;
}

/* small dots */
.circle {
  width: 10px;
  height: 10px;
  background-color: #0095ff;
  border-radius: 50%;
}

/* Arrow lines: hidden by default */
.arrow-line {
  display: none;
}

/* When the conditions are met, the entire horizontal line and arrow are displayed. */
.arrow-line.show-arrow {
  display: block;
  width: 160px;
  height: 2px;
  background-color: #0095ff;
  position: relative;
}

/* arrow tip */
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
