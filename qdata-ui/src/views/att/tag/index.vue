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
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="att/attTag.list" />

    <el-container>
      <DeptTree
        ref="DeptTreeRef"
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="'请输入标签管理类目'"
        @node-click="handleNodeClick"
      />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQueryClick"
              @reset="handleResetQueryClick"
            />
          </template>
          <template #actions-data>
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd"
              v-hasPermi="['att:tag:add']"
            >
              {{ t('common.button.add') }}
            </el-button>
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDelete"
              v-hasPermi="['att:tag:remove']"
            >
              {{ t('common.button.delete') }}
            </el-button>
          </template>
          <qt-table v-bind="tableStore" ref="tableRef">
            <template #status="scope">
              <el-switch
                v-model="scope.row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
                @change="(e) => handleStatusChange(scope.row.id, scope.row, e)"
              />
            </template>
            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                :disabled="row.status == 1"
                v-hasPermi="['att:tag:edit']"
                >{{ t('common.button.update') }}</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                :disabled="row.status == 1"
                @click="handleDelete(row)"
                v-hasPermi="['att:tag:remove']"
                >{{ t('common.button.delete') }}</el-button
              >
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(row)"
                v-hasPermi="['att:tag:query']"
                >{{ t('common.button.details') }}</el-button
              >
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- 添加或修改标签管理对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="AttTagRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="标签管理类目" prop="catCode">
              <el-tree-select
                filterable
                v-model="form.catCode"
                :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }"
                value-key="ID"
                placeholder="请选择标签管理类目"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="标签名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入标签名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="近义词" prop="nearSynonyms" tip="如果有多个请用英文逗号分隔">
              <el-input
                v-model="form.nearSynonyms"
                placeholder="请输入近义词"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="同义词" prop="synonyms" tip="如果有多个请用英文逗号分隔">
              <el-input v-model="form.synonyms" placeholder="请输入同义词" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <qt-form-item :label="t('common.texts.status')" prop="status" :tip="{content:'启用状态表示该标签可用于打标数据资产；禁用后无法再被使用，但已有标签仍保留。'}">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dp_model_status"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}</el-radio
                >
              </el-radio-group>
            </qt-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="t('common.texts.description')" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :placeholder="t('common.form.descriptionPlaceholder')"
                maxlength="500个字符"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="t('common.texts.remark')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                :placeholder="t('common.form.remarkPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ t('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" @click="submitForm"
            >{{ t('common.button.confirm') }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AttTag">
import { useI18n } from 'vue-i18n'
import {
  listAttTag,
  getAttTag,
  delAttTag,
  addAttTag,
  updateAttTag,
} from "@/api/att/tag/tag.js";
import DeptTree from "@/components/DeptTree/index.vue";
import { listAttTagCat } from "@/api/att/cat/tagCat/tagCat.js";
import { getCurrentInstance, ref, reactive, toRefs } from "vue";
import { useRouter } from "vue-router";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");
const router = useRouter();

const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const store = reactive({
  rows: [],
});
function getDeptTree() {
  listAttTagCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "标签管理类目",
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetail,
    },
  },
  columns: [
    { label: t('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: "标签名称",
      prop: "name",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
      link: {
        external: handleDetail,
      },
    },
    {
      label: "标签管理类目",
      prop: "catName",
      align: "left",
      width: 150,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.description'),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "资产数量",
      prop: "aeestCount",
      sortable: true,
      sortableKey: "aeest_count",
      width: 120,
      showOverflowTooltip: { effect: "light" },
      tip:{
        content: "当前已关联此标签的数据资产总数"
      }
    },
    {
        label: t('common.texts.status'),
        prop: "status",
        slot: "status",
        minWidth: 120,
        tip:{
          content: "启用状态表示该标签可用于打标数据资产；禁用后无法再被使用，但已有标签仍保留。"
        }
    },
    {
      label: t('common.texts.createdBy'),
      prop: "createBy",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    {
      label: t('common.texts.operation'),
      width: 240,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listAttTag,
  params: {
    catCode: null,
  },
});

const searchStore = reactive({
  items: [
    {
      label: "标签名称",
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: "请输入标签名称" },
    },
    {
      label: t('common.texts.createdBy'),
      prop: "createBy",
      component: { is: "input", placeholder: "请输入创建人" },
    },
    {
      label: t('common.texts.status'),
      prop: "status",
      component: {
        is: "select",
        placeholder: t('common.form.statusPlaceholder'),
        options: dp_model_status,
      },
    },
  ],
});

function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  tableRef.value.getList();
}

function handleQueryClick() {
  tableRef.value.getList();
}
let DeptTreeRef = ref(null);
function handleResetQueryClick() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = null;
  tableRef.value.resetQuery();
}

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? "启用" : "禁用";
  let dataForm = {
    id: id,
    status: row.status,
  };
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"标签吗？')
    .then(function () {
      updateAttTag(dataForm).then((response) => {
        proxy.$modal.msgSuccess(t('common.message.msgOpSuccess'));
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}

const open = ref(false);
const title = ref("");
const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: "标签名称不能为空", trigger: "blur" }],
    catCode: [
      { required: true, message: "标签管理类目不能为空", trigger: "change" },
    ],
  },
});

const { form, rules } = toRefs(data);

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    description: null,
    catCode: null,
    aeestCount: null,
    status: "1",
    nearSynonyms: null,
    synonyms: null,
    remark: null,
  };
  proxy.resetForm("AttTagRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增标签";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id;
  getAttTag(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改标签";
  });
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
/** 详情按钮操作 */
function handleDetail(row) {
  routeTo("/da/tag/detail", row);
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["AttTagRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        form.value.status = null;
        updateAttTag(form.value).then((response) => {
          proxy.$modal.msgSuccess(t('common.message.editSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
      } else {
        addAttTag(form.value).then((response) => {
          proxy.$modal.msgSuccess(t('common.message.addSuccess'));
          open.value = false;
          tableRef.value.getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    _ids = store.rows.map((item) => item.id).join(",");
  }
  if (!_ids) return;

  proxy.$modal
    .confirm('是否确认删除标签编号为"' + _ids + '"的数据项？')
    .then(function () {
      return Array.isArray(_ids)
        ? Promise.all(_ids.map((id) => delAttTag(id)))
        : delAttTag(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
    })
    .catch(() => {
      // 用户取消删除操作
    });
}

getDeptTree();
</script>
