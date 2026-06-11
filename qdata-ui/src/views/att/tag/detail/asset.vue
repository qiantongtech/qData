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
  <qt-wrap
    :columns="tableStore.columns"
    :tableRef="tableRef"
    :config="{ fullContent: false, actions: { table: { search: false } } }"
  >
    <qt-table v-bind="tableStore" ref="tableRef">
      <template #themeNames="{ row }">
        {{
          row.daAssetThemeRelList?.length
            ? row.daAssetThemeRelList.map((item) => item.themeName).join(", ")
            : "-"
        }}
      </template>
      <template #action="{ row }">
        <el-button
          link
          type="danger"
          icon="Delete"
          style="padding-left: 14px"
          @click="handleDelete(row)"
          >{{ t('common.button.delete') }}</el-button
        >
      </template>
    </qt-table>
  </qt-wrap>

  <!-- 添加或修改标签管理对话框 -->
  <el-dialog
    :title="title"
    v-model="open"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="AttTagRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="编码" prop="code">
            <el-input v-model="form.code" placeholder="请输入编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.texts.name')" prop="name">
            <el-input v-model="form.name" :placeholder="t('common.form.namePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('common.texts.description')" prop="description">
            <el-input
              v-model="form.description"
              :placeholder="t('common.form.descriptionPlaceholder')"
              maxlength="500个字符"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类目编码" prop="catCode">
            <el-input v-model="form.catCode" placeholder="请输入类目编码" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产数量" prop="aeestCount">
            <el-input v-model="form.aeestCount" placeholder="请输入资产数量" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.texts.status')" prop="status">
            <el-input v-model="form.status" placeholder="请输入状态" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="扩展信息别名" prop="allas">
            <el-input v-model="form.allas" placeholder="请输入扩展信息别名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="近义词" prop="nearSynonyms">
            <el-input v-model="form.nearSynonyms" placeholder="请输入近义词" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="同义词" prop="synonyms">
            <el-input v-model="form.synonyms" placeholder="请输入同义词" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.texts.remark')" prop="remark">
            <el-input
              v-model="form.remark"
              :placeholder="t('common.form.remarkPlaceholder')"
              maxlength="500个字符"
              show-word-limit
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

  <!-- 标签管理详情对话框 -->
  <el-dialog
    :title="title"
    v-model="openDetail"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="AttTagRef" :model="form" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="编码" prop="code">
            <div>
              {{ form.code }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.texts.name')" prop="name">
            <div>
              {{ form.name }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('common.texts.description')" prop="description">
            <div>
              {{ form.description }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类目编码" prop="catCode">
            <div>
              {{ form.catCode }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产数量" prop="aeestCount">
            <div>
              {{ form.aeestCount }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.texts.status')" prop="status">
            <div>
              {{ form.status }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="扩展信息别名" prop="allas">
            <div>
              {{ form.allas }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="近义词" prop="nearSynonyms">
            <div>
              {{ form.nearSynonyms }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="同义词" prop="synonyms">
            <div>
              {{ form.synonyms }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.texts.remark')" prop="remark">
            <div>
              {{ form.remark }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ t('common.button.close') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="asset">
import { useI18n } from 'vue-i18n'
import {
  listAttTag,
  getAttTag,
  delAttTag,
  addAttTag,
  updateAttTag,
} from "@/api/att/tag/tag.js";
import { pageListByIds } from "@/api/da/asset/asset.js";
import { defineProps } from "vue";
import { delByTagIdAndAesstId } from "@/api/att/tag/tagAssetRel.js";
const { proxy } = getCurrentInstance();
import { useRoute } from "vue-router";

const { t } = useI18n();
const route = useRoute();
const { da_assets_status, da_asset_source, da_asset_type } = proxy.useDict(
  "da_assets_status",
  "da_asset_source",
  "da_asset_type"
);
const AttTagList = ref([]);

const open = ref(false);
const openDetail = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const title = ref("");

const defaultSort = ref({ prop: "create_time", order: "desc" });
const props = defineProps({
  ids: { type: Object, default: null },
});
const data = reactive({
  AttTagDetail: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: null,
    name: null,
    description: null,
    catCode: null,
    aeestCount: null,
    status: null,
    allas: null,
    nearSynonyms: null,
    synonyms: null,
    createTime: null,
  },
  rules: {},
});

const { queryParams, form, AttTagDetail, rules } = toRefs(data);

const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      height: 374,
      defaultSort: { prop: "create_time", order: "descending" },
    },
  },
  columns: [
    { label: t('common.texts.number'), prop: "id", width: 60, sortable: true },
    {
      label: "资产名称",
      prop: "name",
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.description'),
      prop: "description",
      align: "left",
      showOverflowTooltip: { effect: "light" },
      width: 230,
    },
    {
      label: "资产类目",
      prop: "catName",
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },

    { label: "资产类型", prop: "type", dict: "da_asset_type" },
    {
      label: "主题名称",
      prop: "daAssetThemeRelList",
      showOverflowTooltip: { effect: "light" },
      slot: "themeNames",
    },
    {
      label: t('common.texts.createdBy'),
      prop: "createBy",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 160,
    },

    { label: t('common.texts.operation'), width: 120, fixed: "right", slot: "action" },
  ],
  func: (params) => pageListByIds({ tagIdList: route.query.id, ...params }),
  params: queryParams.value,
});
function handleQueryClick() {
  tableRef.value && tableRef.value.getList();
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
    id: null,
    code: null,
    name: null,
    description: null,
    catCode: null,
    aeestCount: null,
    status: "1",
    allas: null,
    nearSynonyms: null,
    synonyms: null,
    validFlag: true,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("AttTagRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  handleQueryClick();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  handleQueryClick();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加标签管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getAttTag(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改标签管理";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttTag(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "标签管理详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["AttTagRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateAttTag(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(t('common.message.editSuccess'));
            open.value = false;
            handleQueryClick();
          })
          .catch((error) => {});
      } else {
        addAttTag(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(t('common.message.addSuccess'));
            open.value = false;
            handleQueryClick();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  let map = {
    tagId: props.ids.id,
    assetId: row.id,
  };
  proxy.$modal
    .confirm('是否确认删除为"' + _ids + '"的数据项？')
    .then(function () {
      return delByTagIdAndAesstId(map);
    })
    .then(() => {
      handleQueryClick();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

function handleDeleteAll() {
  if (!ids.value.length) return;
  proxy.$modal
    .confirm("是否确认批量删除选中的数据项？")
    .then(function () {
      return Promise.all(
        ids.value.map((id) =>
          delByTagIdAndAesstId({ tagId: props.ids.id, assetId: id })
        )
      );
    })
    .then(() => {
      handleQueryClick();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "att/AttTag/export",
    {
      ...queryParams.value,
    },
    `AttTag_${new Date().getTime()}.xlsx`
  );
}

watch(
  () => props.ids,
  (newId) => {
    handleQueryClick();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
</script>
