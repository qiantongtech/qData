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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="app-container" ref="app-container">
    <el-container>
      <DeptTree
        ref="DeptTreeRef"
        :deptOptions="deptOptions"
        :placeholder="'请输入分层/分域/主题名称'"
        @node-click="handleNodeClick"
      />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
            />
          </template>

          <template #actions-data>
            <!-- <el-dropdown
              @command="handleAdd"
              v-hasPermi="['dp:model:add']"
              class="create-dropdown"
              popper-class="create-table-dropdown-popper"
              @mousedown="(e) => e.preventDefault()"
              @visible-change="(val) => (dropdownVisible = val)"
            >
              <el-button type="primary" plain>
                <el-icon><Plus /></el-icon>
                <span>创建表</span>
                <div class="divider"></div>
                <el-icon
                  class="arrow-icon el-icon--right"
                  :class="{ 'is-reverse': dropdownVisible }"
                >
                  <ArrowDown />
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu class="create-table-dropdown-menu">
                  <el-dropdown-item command="1">
                    <svg-icon iconClass="btn-model-detail-table" />
                    <span>明细表</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="2">
                    <svg-icon iconClass="btn-model-summary-table" />
                    <span>汇总表</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="3">
                    <svg-icon iconClass="btn-model-dimension-table" />
                    <span>维度表</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="4">
                    <svg-icon iconClass="btn-model-progress-table" />
                    <span>应用表</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown> -->
            <el-button
              type="primary"
              :disabled="single"
              plain
              @click="handleMaterialization"
              v-hasPermi="['dp:model:edit']"
              @mousedown="(e) => e.preventDefault()"
            >
              <svg-icon
                iconClass="send-plane-line"
                style="font-size: 14px; margin-right: 6px"
                :class="{
                  'icon-disabled': single,
                  'icon-normal': !single,
                }"
              />发布模型
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #releaseStatus="{ row }">
              <dict-tag v-if="row.releaseStatus == 1" type="info">未发布</dict-tag>
              <dict-tag v-else-if="row.releaseStatus == 3" type="success">发布成功</dict-tag>
              <dict-tag v-else-if="row.releaseStatus == 4" type="danger">发布失败</dict-tag>
              <span v-else>{{ row.releaseStatus }}</span>
            </template>
            <template #releaseDatabaseList="{ row }">
              <QtTagGroup
                type="double"
                is-datasource
                :items="parseReleaseDatabaseList(row.releaseDatabaseList)"
              />
            </template>
            <template #status="{ row }">
              <el-switch
                v-model="row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
                @change="(e) => handleStatusChange(row.id, row, e)"
              />
            </template>
            <template #createTime="{ row }">
              <span>{{
                parseTime(row.createTime, "{y}-{m}-{d} {h}:{i}")
              }}</span>
            </template>
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['dp:model:edit']"
                >详情</el-button
              >
              <el-button
                link
                type="primary"
                @click="handleRelease(row)"
                v-hasPermi="['dp:model:edit']"
              >
                <svg-icon
                  :iconClass="
                    row.releaseStatus == 1 ? 'send-plane-line' : 'zhongxinfabu'
                  "
                  style="margin-right: 4px"
                />
                {{ row.releaseStatus == 1 ? "发布" : "重新发布" }}
              </el-button>
              <!-- <el-button
                link
                type="danger"
                icon="Delete"
                icon-class="icon-shanchu-huise"
                :disabled="row.status == 1"
                @click="handleDelete(row)"
                v-hasPermi="['dp:model:remove']"
                >删除</el-button
              > -->
              <!-- <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['dp:model:edit']"
                >详情</el-button
              > -->
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
    <my-form-dialog
      v-model:visible="open"
      :title="title"
      @submit="handleFormSubmit"
      :deptList="deptList"
      :column_type="column_type"
      :userList="userList"
      @confirm="submitForm"
      :dataList="dataList"
      :catCode="tableStore.params.catCode"
      :deptOptions="deptOptions"
      :type="selectedType"
    />
    <MaterializationDialog
      :title="title"
      :visible="Materialization"
      @update:dialogFormVisible="Materialization = $event"
      :ids="ids"
      @confirm="handleQuery"
    />
  </div>
</template>
<script setup name="DpModel">
import { deptUserTree } from "@/api/system/system/user.js";
import { deptTreeSelectNoPermi } from "@/api/system/system/user.js";
import DeptTree from "@/components/DeptTree/index1.vue";
import MyFormDialog from "@/views/dp/model/components/add.vue";
import MaterializationDialog from "@/views/dp/model/detail/materialization.vue";
import {
  listDpModel,
  releaseList,
  getDpModel,
  delDpModel,
  delDpModelColumn,
  addDpModel,
  updateDpModelColumn,
  updateDpModel,
  listAttModelCat,
  getTreeData,
  dpModelColumn,
  updateStatusDpDataModel,
} from "@/api/dp/model/model";
import { useProjectStore } from "@/store/project/project";
import { getDatasourceIcon } from "@/utils/datasource";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import { getToken } from "@/utils/auth.js";
import { formatHierarchyDisplayName } from "../../../utils/dm/utils";
import { ref, reactive, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();
const projectStore = useProjectStore();
const {
  dp_model_status,
  dp_model_create_type,
  dp_model_table_type,
  dp_model_table_case,
  table_type,
} = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type",
  "dp_model_table_type",
  "dp_model_table_case",
  "table_type"
);
const deptList = ref([]);
const userList = ref([]);
const dataLayerList = ref([]);
const deptOptions = ref(undefined);
const tableRef = ref(null);
const DeptTreeRef = ref(null);

let Materialization = ref(false);

/** 查询数仓分层树结构 */
function getDataLayerTree() {
  treeDataLayer().then((res) => {
    const tree = res.data || res.rows || [];
    const processTree = (list) => {
      return list.map((item) => {
        const newItem = { ...item };
        newItem.id = Number(item.id);
        const abbreviation = item.engName || item.shortName;
        newItem.displayName = abbreviation
          ? `${item.name} / ${abbreviation}`
          : item.name;
        if (item.children && item.children.length) {
          newItem.children = processTree(item.children);
        }
        return newItem;
      });
    };
    dataLayerList.value = processTree(tree);
  });
}

/** 查询部门下拉树结构 */
function getDeptTree() {
  getDataLayerTree();
  projectStore.getModelDeptTree().then((data) => {
    deptOptions.value = data;
  });
  // 部门
  deptTreeSelectNoPermi().then((response) => {
    deptList.value = response.data;
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
    console.log("userList", userList.value);
  });
}

const open = ref(false);
const openDetail = ref(false);
const dropdownVisible = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const title = ref("");
const router = useRouter();
const selectedType = ref("");

/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/dp/model/importData",
});

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? "启用" : "禁用";
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.modelComment + '"逻辑模型吗？')
    .then(function () {
      updateStatusDpDataModel(id, row.status).then((response) => {
        proxy.$modal.msgSuccess("操作成功");
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

const data = reactive({
  form: { status: "1" },
  rules: {
    modelName: [
      { required: true, message: "模型编码不能为空", trigger: "blur" },
    ],
    modelComment: [
      { required: true, message: "模型名称不能为空", trigger: "blur" },
    ],
    catCode: [{ required: true, message: "类目编码不能为空", trigger: "blur" }],
    status: [{ required: true, message: "状态不能为空", trigger: "change" }],
    createType: [
      { required: true, message: "创建方式不能为空", trigger: "change" },
    ],
  },
});

const { form, rules } = toRefs(data);

const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: handleSelectionChange,
      honRowDblclick: handleDetail,
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
    },
    { label: "编号", prop: "id", width: 60, sortable: true },
    {
      label: "模型信息",
      width: 450,
      align: "left",
      info: {
        title: (row) =>
          row.modelComment + (row.modelName ? ` (${row.modelName})` : ""),
        desc: "description",
        click: (row) => handleDetail(row),
      },
    },
    {
      label: "归属层级",
      align: "left",
      width: 320,
      showOverflowTooltip: { effect: "light" },
      formatter: (row) => formatHierarchyDisplayName(row, row.tableType),
    },
    {
      label: "表类型",
      align: "left",
      width: 150,
      prop: "tableType",
      dict: "table_type",
      showOverflowTooltip: { effect: "light" },
    },

    {
      label: "发布状态",
      prop: "releaseStatus",
      align: "center",
      width: 100,
      slot: "releaseStatus",
    },
    {
      label: "已发布数据源",
      prop: "releaseDatabaseList",
      align: "center",
      width: 300,
      slot: "releaseDatabaseList",
    },
    {
      label: "创建人",
      width: 120,
      align: "left",
      list: [
        { prop: "createBy", class: "person-charge-ellipsis" },
        { prop: "createUserPhoneNumber" },
      ],
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 150,
      sortable: true,
      date: true,
    },
    {
      label: "操作",
      width: 175,
      headerAlign: "center",
      align: "left",
      fixed: "right",
      slot: "action",
    },
  ],
  func: releaseList,
  params: {
    modelName: null,
    modelComment: null,
    catCode: null,
    id: null,
    status: null,
    dataLayerId: null,
    tableType: null,
    tableCase: null,
    businessCategoryCode: null,
    dataDomainId: null,
    themeDomainCode: null,
  },
});

const searchStore = reactive({
  items: [
    { label: "中文名称", prop: "modelComment", component: { is: "input" } },
    { label: "英文名称", prop: "modelName", component: { is: "input" } },
    {
      label: "数仓分层",
      prop: "dataLayerId",
      type: "select",
      component: {
        is: "tree-select",
        data: dataLayerList,
        props: {
          value: "id",
          label: "displayName",
          children: "children",
        },
        "node-key": "id",
        filterable: true,
        clearable: true,
        "default-expand-all": true,
      },
    },
    {
      label: "表类型",
      prop: "tableType",
      type: "select",
      component: { is: "select", options: table_type },
    },
    // {
    //   label: "命名大小写",
    //   prop: "tableCase",
    //   type: "select",
    //   component: { is: "select", options: dp_model_table_case },
    // },
    // {
    //   label: "状态",
    //   prop: "status",
    //   type: "select",
    //   component: { is: "select", options: dp_model_status },
    // },
  ],
});

function handleNodeClick(data) {
  if (data.type === "0") {
    return;
  }
  tableStore.params.businessCategoryCode = null;
  tableStore.params.dataDomainId = null;
  tableStore.params.themeDomainCode = null;
  tableStore.params.dataLayerId = null;
  tableStore.params.catCode = null;
  tableStore.params.id = null;
  if (data.type === "1") {
    tableStore.params.businessCategoryCode = data.otherData?.code;
  } else if (data.type === "2") {
    tableStore.params.dataDomainId = data.id;
    tableStore.params.businessCategoryCode = data.otherData?.code;
  } else if (data.type === "3") {
    tableStore.params.themeDomainCode = data.otherData?.code;
  } else if (data.type === "5") {
    tableStore.params.dataLayerId = data.id;
  }
  handleQuery();
}

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    ID: null,
    modelName: null,
    modelComment: null,
    catCode: null,
    status: null,
    createType: null,
    datasourceId: null,
    contact: null,
    contactNumber: null,
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
  proxy.resetForm("dpModelRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  tableRef.value && tableRef.value.getList();
}

/** 重置按钮操作 */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = null;
  tableStore.params.id = null;
  tableStore.params.businessCategoryCode = null;
  tableStore.params.dataDomainId = null;
  tableStore.params.themeDomainCode = null;
  tableStore.params.dataLayerId = null;
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  console.log("selection", selection);
  ids.value = selection.map((item) => item.id);
  console.log("selection.length ", selection.length);
  single.value = selection.length == 0 ? true : false;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd(type) {
  selectedType.value = typeof type === "string" ? type : "1";
  dataList.value = {};
  reset();
  open.value = true;
  title.value = "新增逻辑模型";
}
let dataList = ref({});
/** 修改按钮操作 */
function handleUpdate(row) {
  console.log("row", row);
  reset();
  const _ID = row.id || ids.value;
  getDpModel(_ID).then((response) => {
    dataList.value = response.data;
    selectedType.value = String(dataList.value.type || "");
    open.value = true;
    title.value = "修改逻辑模型";
  });
}

/** 发布模型按钮操作 */
function handleMaterialization() {
  const _ID = ids.value;
  Materialization.value = true;
  title.value = "发布模型";
}

/** 发布/重新发布按钮操作 */
function handleRelease(row) {
  ids.value = [row.id];
  Materialization.value = true;
  title.value = row.releaseStatus == 1 ? "发布模型" : "重新发布模型";
}

/** 详情按钮操作 */
function handleDetail(row) {
  routeTo("/dm/model/materializedModel/detail", row);
}

/** 提交按钮 */
function submitForm(obj) {
  console.log("obj", obj);
  if (obj.form.id != null) {
    updateDpModel(obj.form)
      .then((response) => {
        updateDpModelColumn(obj.tableData).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          handleQuery();
        });
      })
      .catch((error) => {});
  } else {
    addDpModel(obj.form)
      .then((response) => {
        const id = response.data;
        const updatedTableData = obj.tableData.map((item) => ({
          ...item,
          modelId: id,
        }));
        dpModelColumn(updatedTableData)
          .then((dpModelColumnResponse) => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            handleQuery();
          })
          .catch((dpModelColumnError) => {});
      })
      .catch((error) => {
        console.error("新增失败:", error);
      });
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _IDs = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除逻辑模型编号为"' + _IDs + '"的数据项？')
    .then(function () {
      return delDpModelColumn(_IDs);
    })
    .then(() => {
      handleQuery();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id,
        },
      });
    }
  }
}

/** 解析已发布数据源列表 */
function parseReleaseDatabaseList(json) {
  try {
    const list = JSON.parse(json);
    return Array.isArray(list) ? list : [];
  } catch (e) {
    return [];
  }
}

getDeptTree();
</script>

<style scoped lang="less">
.create-dropdown {
  margin-right: 12px;
  vertical-align: middle;

  .arrow-icon {
    transition: transform 0.3s;
    &.is-reverse {
      transform: rotate(180deg);
    }
  }

  .divider {
    width: 1px;
    height: 12px;
    background: currentColor;
    opacity: 0.3;
    margin: 0 4px 0 12px;
  }

  .el-button:hover,
  .el-button:focus,
  .el-button:active {
    .divider {
      opacity: 0.6;
    }
  }
}

.datasource-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  width: 100%;
  gap: 4px;
  overflow: hidden;
  max-height: 56px;
}

.datasource-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-color: #e8f0ff;
  background-color: #f0f7ff;
  color: #409eff;
  padding: 0 8px;
  height: 24px;
  line-height: 1;

  :deep(.el-tag__content) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }

  .datasource-icon {
    width: 14px;
    height: 14px;
    margin-right: 4px;
    display: block;
    object-fit: contain;
  }
}
</style>

<style lang="less">
.create-table-dropdown-popper {
  min-width: 120px !important;

  .el-dropdown-menu {
    padding: 4px 0 !important;
  }

  .el-dropdown-menu__item {
    padding: 6px 12px !important;
    font-size: 14px !important;
    line-height: 22px !important;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;

    .svg-icon {
      margin-right: 8px !important;
      font-size: 16px !important;
    }

    span {
      white-space: nowrap;
    }
  }
}
</style>
