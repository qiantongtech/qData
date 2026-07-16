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
    :title="title"
    v-model="visible"
    class="medium-dialog max-dialogs-status0"
    draggable
    width="90%"
  >
    <div class="flex-row">
      <!-- tree on left -->
      <div class="left-col">
        <DeptTree
          :deptOptions="deptOptions"
          :leftWidth="leftWidth"
          :placeholder="td('dpp.cleanRule.inputStandardDataElemCat', '请输入标准数据元类目')"
          @node-click="handleNodeClick"
          ref="DeptTreeRef"
          :showFilter="false"
          :show-background="false"
          style="height: 650px"
        />
      </div>

      <!-- divider -->
      <div class="divider"></div>

      <!-- Right table + pagination -->
      <div class="content-col" v-loading="loading">
        <!-- table -->
        <el-table
          :data="dpDataElemList"
          stripe
          @row-click="handleRowClick"
          :highlight-current-row="true"
          ref="tableRef"
          border
          height="62vh"
        >
          <el-table-column
            v-if="getColumnVisibility(0)"
            :label="td('common.texts.number', '编号')"
            align="left"
            prop="id"
            width="80"
          />
          <el-table-column
            v-if="getColumnVisibility(1)"
            :label="td('dpp.cleanRule.chineseName', '中文名称')"
            :show-overflow-tooltip="{ effect: 'light' }"
            width="80"
            align="left"
            prop="name"
          >
            <template #default="scope">{{ scope.row.name || "-" }}</template>
          </el-table-column>
          <el-table-column
            v-if="getColumnVisibility(2)"
            :label="td('dpp.cleanRule.englishName', '英文名称')"
            :show-overflow-tooltip="{ effect: 'light' }"
            width="80"
            align="left"
            prop="engName"
          >
            <template #default="scope">{{ scope.row.engName || "-" }}</template>
          </el-table-column>
          <el-table-column
            v-if="getColumnVisibility(3)"
            :label="td('dpp.cleanRule.type', '类型')"
            align="left"
            prop="type"
          >
            <template #default="scope">{{ typeFormat(scope.row) }}</template>
          </el-table-column>
          <el-table-column
            v-if="getColumnVisibility(6)"
            width="140"
            :label="td('dpp.cleanRule.metaDescription', '元描述')"
            align="left"
            prop="description"
            :show-overflow-tooltip="{ effect: 'light' }"
          >
            <template #default="scope">{{
              scope.row.description || "-"
            }}</template>
          </el-table-column>
          <el-table-column
            :label="td('common.texts.operation', '操作')"
            align="center"
            class-name="small-padding fixed-width"
            fixed="right"
            width="240"
          >
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="view"
                @click="showDialog(scope.row)"
                v-hasPermi="['dp:dataElem:dataelem:edit']"
                >{{ td('dpp.cleanRule.view', '查看') }}
              </el-button>
            </template>
          </el-table-column>
          <template #empty>
            <div class="emptyBg">
              <img
                src="../../../../../../../../assets/images/system/no_data/empty-nodata.png"
                alt=""
              />
              <p>{{ td('dpp.cleanRule.noData', '无数据') }}</p>
            </div>
          </template>
        </el-table>
        <!-- Pagination -->
        <div
          class="pagination-wrapper"
          style="margin-top: 10px; text-align: right"
        >
          <pagination
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
      </div>
    </div>

    <!-- bottom button -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCancel">{{ td('common.button.cancel', '取消') }}</el-button>
        <el-button
          type="primary"
          @click="handleConfirm"
          :disabled="!selectedRow"
          :loading="loading"
        >
          {{ td('common.button.save', '保存') }}
        </el-button>
      </div>
    </template>
    <CodeValueInput ref="dialogRef" @confirm="handleConfirm" />
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, reactive } from "vue";
const emit = defineEmits(["confirm"]);
import DeptTree from "@/components/DeptTree/tree.vue";
import { listDpDataElem } from "@/api/dp/dataElem/dataElem.js";
import { listDpDataElemCode } from "@/api/dp/dataElem/dataElem.js";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataElemCat } from "@/api/att/cat/dataElemCat/dataElemCat.js";
const { proxy } = getCurrentInstance();
const { dp_data_elem_code_type } = proxy.useDict("dp_data_elem_code_type");
import CodeValueInput from "./dataElemDetail.vue";

const { td } = useDefaultLang();
const deptOptions = ref(undefined);
const leftWidth = ref(240); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed // Initial left width
/** type dictionary translation */
function typeFormat(row) {
  return proxy.selectDictLabel(dp_data_elem_code_type.value, row.type);
}

const dpDataElemList = ref([]);
const dpDataElemRuleRelList = ref([]);

// Show hidden information
const columns = ref([
  { key: 1, label: td('dpp.cleanRule.chineseName', '中文名称'), visible: true },
  { key: 2, label: td('dpp.cleanRule.englishName', '英文名称'), visible: true },
  { key: 3, label: td('dpp.cleanRule.type', '类型'), visible: true },
  { key: 4, label: td('dpp.cleanRule.standardDataElemCat', '标准数据元类目'), visible: true },
  { key: 5, label: td('common.texts.status', '状态'), visible: true },
  { key: 6, label: td('dpp.cleanRule.metaDescription', '元描述'), visible: true },
]);
const dialogRef = ref();
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
function showDialog(row) {
  dialogRef.value.openDialog(row);
}
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
  return column.visible;
};

const open = ref(false);
const loading = ref(true);
const ids = ref([]);
const checkedDpDataElemRuleRel = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const data = reactive({
  form: { status: "0" },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    engName: null,
    catCode: null,
    type: 2,
  },
});

const { queryParams, form } = toRefs(data);
const managerOptions = ref([]);
/** Query data element list */
function getList() {
  loading.value = true;
  listDpDataElem(queryParams.value).then((response) => {
    dpDataElemList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}
// tree component pass value
function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  handleQuery();
}

// form reset
function reset() {
  form.value = {
    id: null,
    code: null,
    name: null,
    engName: null,
    catCode: null,
    type: "1",
    personCharge: null,
    contactNumber: null,
    columnType: null,
    status: "0",
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  dpDataElemRuleRelList.value = [];
  proxy.resetForm("dpDataElemRef");
}

const DeptTreeRef = ref(null);
/** reset button action */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.catCode = "";
  queryParams.value.pageNum = 1;
  selectedRow.value = null;
  reset();
  proxy.resetForm("queryRef");
}

function getDeptTree() {
  listAttDataElemCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('dpp.cleanRule.standardDataElemCat', '标准数据元类目'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}

const visible = ref(false);
/**
 * Open pop-up window
 * @param {String} dialogTitle pop-up window title
 */
function openDialog(dialogTitle = td('dpp.cleanRule.selectData', '选择数据')) {
  title.value = dialogTitle;
  visible.value = true;
  getDeptTree();
  getList();
}
const selectedRow = ref(null);
const tableRef = ref(null);
function handleRowClick(row) {
  selectedRow.value = row;
  if (tableRef.value) {
    tableRef.value.setCurrentRow(row); // Highlight
  }
  console.log("Selected row data:", row);
}
/**
 * Cancel
 */
function handleCancel() {
  visible.value = false;
  if (tableRef.value) {
    tableRef.value.setCurrentRow(null); // Clear selected row highlighting in table
  }
  resetQuery();
}
async function ElemCode(id) {
  if (id === -1) {
    return [];
  }
  loading.value = true;
  try {
    const response = await listDpDataElemCode({
      pageNum: 1,
      pageSize: 999,
      id,
    });
    return response.data.rows || [];
  } catch (error) {
    console.error("Request failed", error);
    return [];
  } finally {
    loading.value = false;
  }
}

/**
 * save
 */
async function handleConfirm() {
  if (!selectedRow.value) {
    proxy.$modal.msgWarning(td('dpp.cleanRule.selectRecord', '请选择一条记录'));
    return;
  }
  // const list = await ElemCode(selectedRow.value.id);
  emit("confirm", selectedRow.value);
  resetQuery();
  visible.value = false;
}

defineExpose({ openDialog });
</script>
<style scoped lang="scss">
.flex-row {
  display: flex;
  height: 71vh;
}

.left-col {
  width: 250px;
  overflow-y: auto;
}

.divider {
  width: 1px;
  background-color: #dcdfe6;
  margin: 0 20px;
  height: 700px;
}

.content-col {
  margin-top: 50px;
  flex: 1;
  display: flex;
  flex-direction: column;
  padding-right: 20px;
}

.el-table {
  flex: none;
  /* Do not occupy the parent container */
}

.pagination-wrapper {
  margin-top: 10px;
  text-align: right;
}

.emptyBg {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}
</style>
