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
  <div class="app-container-inner">
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef"     :config="{ fullContent: false, actions: { table: { search: false } } }"
      <!-- 数据操作按钮插槽 -->
      <template #actions-data>
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['dp:model:add']"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
            </el-button>
          </el-col>
        </el-row>
      </template>

      <!-- 表格组件 -->
      <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
        <!-- 状态列插槽 -->
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
        <!-- 操作列插槽 -->
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            :disabled="row.status == 1"
            v-hasPermi="['dp:model:edit']"
          >{{ td('dp.common.edit') }}</el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            :disabled="row.status == 1"
            v-hasPermi="['dp:model:remove']"
          >{{ td('dp.common.delete') }}</el-button>
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetail(row)"
            v-hasPermi="['dp:model:edit']"
          >{{ td('dp.common.details') }}</el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Logical Model Details Dialog Box -->
    <el-dialog :title="title" v-model="openDetail" width="800px" draggable>
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
          <el-icon size="20" style="color: #909399; font-size: 16px">
            <InfoFilled />
          </el-icon>
        </span>
      </template>
      <el-form ref="dpModelRef" :model="form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.englishName')" prop="modelName">
              <div>{{ form.modelName }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.model.chineseName')" prop="modelComment">
              <div>{{ form.modelComment }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.catCode')" prop="catCode">
              <div>{{ form.catCode }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status">
              <dict-tag :options="dp_model_status" :value="form.status" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.createType')" prop="createType">
              <dict-tag :options="dp_model_create_type" :value="form.createType" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.contact')" prop="contact">
              <div>{{ form.contact }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dp.model.detail.contactNumber')" prop="contactNumber">
              <div>{{ form.contactNumber }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.description')" prop="description">
              <div>{{ form.description }}</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openDetail = false">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Form Dialog for Add/Edit -->
    <my-form-dialog
      v-model:visible="open"
      :title="title"
      :deptList="deptList"
      :column_type="column_type"
      :userList="userList"
      @confirm="submitForm"
      :dataList="dataList"
      :catCode="tableStore.params.catCode"
      :deptOptions="deptOptions"
      :documentId="tableStore.params.documentId"
      type="3"
    />

    <!-- Materialization Dialog -->
    <MaterializationDialog
      :title="title"
      :visible="Materialization"
      @update:dialogFormVisible="Materialization = $event"
      :ids="ids"
      @confirm="tableRef.refresh()"
      :documentId="tableStore.params.documentId"
    />
  </div>
</template>

<script setup name="DpModel">
import { deptUserTree, deptTreeSelectNoPermi } from "@/api/system/system/user.js";
import MyFormDialog from "@/views/dp/model/components/add.vue";
import MaterializationDialog from "@/views/dp/model/detail/materialization.vue";
import {
  listDpModel,
  getDpModel,
  delDpModelColumn,
  addDpModel,
  updateDpModelColumn,
  updateDpModel,
  listAttModelCat,
  dpModelColumn,
  updateStatusDpDataModel,
} from "@/api/dp/model/model";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();

const { dp_model_status, dp_model_create_type, column_type } = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type",
  "column_type"
);

const tableRef = ref(null);
const deptOptions = ref([]);
const deptList = ref([]);
const userList = ref([]);
const open = ref(false);
const openDetail = ref(false);
const Materialization = ref(false);
const title = ref("");
const ids = ref([]);
const dataList = ref({});

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  params: {
    documentId: route.query.id,
    catCode: null,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
  columns: [
    { label: td("common.texts.number"), prop: "id", width: 60, align: "left", sortable: true },
    { label: td("dp.model.englishName"), prop: "modelName", width: 200, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("dp.model.chineseName"), prop: "modelComment", width: 180, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("dp.model.treeRootName"), prop: "catName", width: 120, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.createdBy"), prop: "createBy", width: 120, align: "left" },
    { label: td("common.texts.createdTime"), prop: "createTime", width: 180, align: "left", sortable: true, date: true },
    { label: td("common.texts.status"), slot: "status", width: 120, align: "center" },
    { label: td("common.texts.remark"), prop: "remark", width: 200, align: "left", showOverflowTooltip: { effect: "light" } },
    { label: td("common.texts.operation"), slot: "action", width: 240, align: "center", fixed: "right" },
  ],
  func: listDpModel,
});

const data = reactive({
  form: { status: "1" },
});

const { form } = toRefs(data);

/** 查询分类树及相关选项 */
function getDeptTree() {
  listAttModelCat({ validFlag: true }).then((response) => {
    const treeData = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('dp.model.treeRootName'),
        value: "",
        id: 0,
        children: treeData,
      },
    ];
  });
  deptTreeSelectNoPermi().then((response) => {
    deptList.value = response.data;
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
  });
}

/** 重置表单 */
function reset() {
  form.value = {
    id: null,
    modelName: null,
    modelComment: null,
    catCode: null,
    status: "1",
    createType: null,
    datasourceId: null,
    contact: null,
    contactNumber: null,
    description: null,
    remark: null,
    documentId: tableStore.params.documentId,
  };
  proxy.resetForm("dpModelRef");
}

/** 新增按钮操作 */
function handleAdd() {
  dataList.value = {};
  reset();
  open.value = true;
  title.value = td('dp.model.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getDpModel(row.id).then((response) => {
    dataList.value = response.data;
    open.value = true;
    title.value = td('dp.model.editTitle');
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  router.push({
    path: "/dm/model/detail",
    query: { id: row.id },
  });
}

/** 提交按钮 */
function submitForm(obj) {
  if (obj.form.id != null) {
    updateDpModel({ ...obj.form, documentId: tableStore.params.documentId }).then(() => {
      updateDpModelColumn(obj.tableData).then(() => {
        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
        open.value = false;
        tableRef.value.refresh();
      });
    });
  } else {
    addDpModel({ ...obj.form, documentId: tableStore.params.documentId }).then((response) => {
      const id = response.data;
      const updatedTableData = obj.tableData.map((item) => ({ ...item, modelId: id }));
      dpModelColumn(updatedTableData).then(() => {
        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
        open.value = false;
        tableRef.value.refresh();
      });
    });
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || tableRef.value.selection.map(item => item.id);
  proxy.$modal
    .confirm(td('dp.model.confirmDelete', '', { id: _ids }))
    .then(() => delDpModelColumn(_ids))
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** 状态修改 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? td('dp.model.enableText') : td('dp.model.disableText');
  proxy.$modal
    .confirm(td('dp.model.confirmStatusChange', '', { text, name: row.modelComment }))
    .then(() => updateStatusDpDataModel(id, row.status))
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.operationSuccess'));
    })
    .catch(() => {
      row.status = row.status === "1" ? "0" : "1";
    });
}

getDeptTree();
</script>

<style scoped lang="scss">
.app-container-inner {
  padding: 10px 0;
}
</style>
