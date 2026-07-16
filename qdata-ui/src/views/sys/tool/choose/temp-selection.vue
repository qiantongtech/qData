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
  <el-dialog
    :title="td('sys.tool.chooseSelection.title')"
    v-model="visible"
    width="1200px"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
    @close="cancel"
  >
    <el-form
      class="btn-style"
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"

     :label-position="labelPosition">
      <el-form-item :label="td('sys.tool.chooseSelection.dictName')" prop="dictName" :label-position="labelPosition">
        <el-input
          v-model="queryParams.dictName"
          :placeholder="td('sys.tool.chooseSelection.dictNamePlaceholder')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="td('sys.tool.chooseSelection.dictType')" prop="dictType" :label-position="labelPosition">
        <el-input
          v-model="queryParams.dictType"
          :placeholder="td('sys.tool.chooseSelection.dictTypePlaceholder')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
        <el-select
          v-model="queryParams.status"
          :placeholder="td('sys.tool.chooseSelection.dictStatus')"
          clearable
          class="el-form-input-width"
        >
          <el-option
            v-for="dict in sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="td('common.texts.createdTime')" :label-position="labelPosition">
        <el-date-picker
          class="el-form-input-width"
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          :start-placeholder="td('common.form.startDatePlaceholder')"
          :end-placeholder="td('common.form.endDatePlaceholder')"
        ></el-date-picker>
      </el-form-item>
      <el-form-item :label-position="labelPosition">
        <el-button
          plain
          type="primary"
          @click="handleQuery"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      ref="multipletableRef"
      stripe
      height="300px"
      v-loading="loading"
      :data="dataList"
      reserve-selection
      row-key="dictId"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        :label="td('sys.tool.chooseSelection.dictNo')"
        align="center"
        prop="dictId"
        width="85"
      />
      <el-table-column
        :label="td('sys.tool.chooseSelection.dictName')"
        align="center"
        prop="dictName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="td('sys.tool.chooseSelection.dictType')"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <router-link
            :to="'/system/dict-data/index/' + scope.row.dictId"
            class="link-type"
          >
            <span>{{ scope.row.dictType }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="td('common.texts.remark')"
        align="center"
        prop="remark"
        :show-overflow-tooltip="true"
      />
      <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" size="mini" @click="confirm">
          {{ td('common.button.confirm') }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="Dict">
import { listType, getType, delType } from "@/api/system/system/dict/type.js";
import { ref } from "vue"
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const dataList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const dateRange = ref([]);
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dictName: undefined,
    dictType: undefined,
    status: undefined,
  },
  rules: {
    dictName: [
      { required: true, message: td('sys.tool.chooseSelection.dictNameRequired'), trigger: "blur" },
    ],
    dictType: [
      { required: true, message: td('sys.tool.chooseSelection.dictTypeRequired'), trigger: "blur" },
    ],
  },
});
const { queryParams, form, rules } = toRefs(data);

// -------------------------------------------
const visible = ref(false);
// Define multiple selection data
const multiple = ref([]);
// Define the last checked data == used for comparison and deletion
const oldSelection = ref([]);
// Whether to switch between pages
const isAuto = ref(false);
// Current interface table
const multipletableRef = ref();

const emit = defineEmits(["open", "confim", "cancel"]);

/** Multi-select box selection event */
function handleSelectionChange(selection) {
  // console.log(selection, "===handleSelectionChange");
  if (selection.length > 0) {
    // If the selected value is not a null value and one less value is selected
    if (oldSelection.value.length > selection.length) {
      oldSelection.value.forEach((item) => {
        let index = selection.findIndex((ece) => ece.dictId == item.dictId);
        if (index == -1) {
          multiple.value = multiple.value.filter(
            (ece) => item.dictId != ece.dictId
          );
        }
      });
    }
    if (multiple.value.length > 0) {
      selection.forEach((item) => {
        let index = multiple.value.findIndex(
          (ece) => ece.dictId == item.dictId
        );
        if (index == -1) {
          multiple.value.push(item);
        }
      });
    } else {
      multiple.value.push(...selection);
    }
  } else {
    // If it is not caused by paging
    if (!isAuto.value) {
      // If a value is selected, cancel to no value selected
      oldSelection.value.forEach((item) => {
        let index = selection.findIndex((ece) => ece.dictId == item.dictId);
        if (index == -1) {
          multiple.value = multiple.value.filter(
            (ece) => item.dictId != ece.dictId
          );
        }
      });
    }
  }
  oldSelection.value = selection;
}
/**
 * Select the checkbox of the table
 * @param {Array} rows Array of selected objects
 * @param {Boolean} ignoreSelectable Whether to ignore optional
 */
function setSelectionRow(rows, ignoreSelectable) {
  // Select data
  if (rows.length > 0) {
    rows.forEach((row) => {
      let data = dataList.value.filter((item) => item.dictId == row.dictId);
      if (data.length > 0) {
        multipletableRef.value.toggleRowSelection(data[0], undefined, ignoreSelectable);
      }
    });
  }
}
function rest(){
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  oldSelection.value = []
}
/**
 * Open selection box
 * @param {Array} val array of selected objects
 */
function open(val) {
  visible.value = true;
  multiple.value = [...val];
  getList();
}
/**
 * Cancel button
 * @description When canceling the button, reset all states
 */
function cancel() {
  rest();
  visible.value = false;
}
/**
 * OK button
 * @description When confirming the button, emit the confirm event so that the parent component receives the selected data
 */
function confirm() {
  if (multiple.value.length == 0) {
    proxy.$modal.msgWarning(td('sys.tool.chooseSelection.noDataSelected'));
    return;
  }
  emit("confirm", [...multiple.value]);
  rest();
  visible.value = false;
}
/** Query dictionary type list */
function getList() {
  loading.value = true;
  listType(proxy.addDateRange(queryParams.value, dateRange.value)).then(
    async (response) => {
      dataList.value = response.rows;
      total.value = response.total;
      loading.value = false;
      // Initialization and paging switching selection logic
      isAuto.value = true;
      await nextTick();
      setSelectionRow(multiple.value);
      isAuto.value = false;
    }
  );
}
/** Search button action */
function handleQuery() {
  getList();
}

/** reset button action */
function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.value.pageNum = 1;
  handleQuery();
}
defineExpose({ open });
</script>
