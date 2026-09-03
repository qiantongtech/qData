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
  <div class="app-container" ref="app-container">
    <el-container>
      <DeptTree
        v-bind="treeProps"
        :placeholder="td('dp.document.selectStandardCategory')"
        @node-click="handleNodeClick"
        ref="DeptTreeRef"
      />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <!-- 搜索栏插槽 -->
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
              :tableRef="tableRef"
            />
          </template>

          <!-- 数据操作按钮插槽 -->
          <template #actions-data>
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  @click="handleAdd"
                >
                  <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
                </el-button>
              </el-col>
            </el-row>
          </template>

          <!-- 表格组件 -->
          <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
            <!-- 操作列插槽 -->
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
              >{{ td('dp.common.edit') }}</el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
              >{{ td('dp.common.details') }}</el-button>

              <el-popover placement="bottom" :width="150" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">{{ td('dp.document.more') }}</el-button>
                </template>
                <div style="width: 100px" class="butgdlist">
                  <el-button
                    link
                    style="padding-left: 14px"
                    type="primary"
                    icon="View"
                    @click="handleFilePreview(row.fileUrl)"
                    :disabled="!row.fileUrl"
                  >{{ td('dp.document.previewBtn') }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="Download"
                    :disabled="!row.fileUrl"
                    @click="handleDownload(row)"
                  >{{ td('dp.document.downloadBtn') }}</el-button>
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(row)"
                  >{{ td('dp.common.delete') }}</el-button>
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
    <!-- Standard pop-up window -->
    <StandardModal ref="standardModalRef" @update-success="handleQuery" />
  </div>
</template>

<script setup name="Provincial">
import DeptTree from "@/components/DeptTree";
import {
  listDpDocument,
  delDpDocument,
  listAttDocumentCat,
} from "@/api/dp/document/document";
import StandardModal from "../components/add";
import handleFilePreview from "@/utils/filePreview.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const router = useRouter();

const tableRef = ref(null);
const DeptTreeRef = ref(null);
const standardModalRef = ref(null);

const treeProps = reactive({
  deptOptions: [],
});

const tableStore = reactive({
  params: {
    code: null,
    name: null,
    status: null,
    catCode: null,
    type: 3,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
  columns: [
    { label: td("common.texts.number"), prop: "id", width: 60, align: "left", sortable: true },
    { label: td("dp.document.standardCode"), prop: "code", width: 160, align: "left" },
    { label: td("dp.document.standardName"), prop: "name", width: 260, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.description"), prop: "description", width: 256, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("dp.document.standardCategory"), prop: "catName", width: 160, align: "left", tag: { class: "task-cat-ellipsis" } },
    { label: td("common.texts.createdBy"), prop: "createBy", width: 120, align: "left" },
    { label: td("common.texts.createdTime"), prop: "createTime", width: 160, align: "left", sortable: true, date: true },
    { label: td("dp.document.standardStatus"), prop: "status", width: 160, align: "left", dict: "dp_document_status" },
    { label: td("common.texts.remark"), prop: "remark", width: 200, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.operation"), slot: "action", width: 220, align: "center", fixed: "right" },
  ],
  func: listDpDocument,
});

const searchStore = reactive({
  items: [
    {
      label: td("dp.document.standardCode"),
      prop: "code",
      component: { is: "input", placeholder: td("dp.document.standardCodePlaceholder") },
    },
    {
      label: td("dp.document.standardName"),
      prop: "name",
      component: { is: "input", placeholder: td("dp.document.standardNamePlaceholder") },
    },
    {
      label: td("dp.document.standardStatus"),
      prop: "status",
      component: { is: "select", dict: "dp_document_status", placeholder: td("dp.document.standardStatusPlaceholder") },
    },
  ],
});

/** 查询分类树 */
function getDeptTree() {
  listAttDocumentCat({ validFlag: true }).then((response) => {
    const treeData = proxy.handleTree(response.data, "id", "parentId");
    treeProps.deptOptions = [
      {
        name: td("dp.document.treeRootName"),
        value: "",
        id: 0,
        children: treeData,
      },
    ];
  });
}

/** 树节点点击 */
function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}

/** 搜索按钮操作 */
function handleQuery() {
  tableStore.params.pageNum = 1;
}

/** 重置按钮操作 */
function resetQuery() {
  DeptTreeRef.value?.resetTree?.();
  tableStore.params.catCode = "";
}

/** 新增按钮操作 */
function handleAdd() {
  standardModalRef.value.openModal({}, treeProps.deptOptions, tableStore.params.type);
}

/** 修改按钮操作 */
function handleUpdate(row) {
  standardModalRef.value.openModal(row, treeProps.deptOptions, tableStore.params.type);
}

/** 详情按钮操作 */
function handleDetail(row) {
  router.push({
    path: "/dm/document/provincial/detail",
    query: { id: row.id },
  });
}

/** 下载操作 */
const handleDownload = (row) => {
  const baseUrl = import.meta.env.VITE_APP_BASE_API;
  const fullUrl = `${baseUrl}${row.fileUrl.trim()}`;
  const a = document.createElement("a");
  a.href = fullUrl;
  a.download = row.fileName;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
};

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id || tableRef.value.selection.map((item) => item.id);
  proxy.$modal
    .confirm(td("dp.document.confirmDelete", "", { id: ids }))
    .then(() => delDpDocument(ids))
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess"));
    })
    .catch(() => {});
}

getDeptTree();
</script>

<style scoped lang="scss">
.app-container {
  margin: 13px 15px;
}

.main-content {
  padding: 2px 0px;
}

::v-deep {
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}
</style>
