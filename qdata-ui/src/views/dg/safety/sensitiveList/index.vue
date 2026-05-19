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
          v-hasPermi="['dg:dgdesensitizelist:add']"
        >
          新增
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
          v-hasPermi="['dg:dgdesensitizelist:remove']"
        >
          删除
        </el-button>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef">
        <template #assetNameDesc="{ row }">
          <div class="name-label task-title">
            <div class="task-title-row">
              <div class="task-name-ellipsis">
                <span
                  class="task-name-ellipsis__inner"
                  :title="row.assetName || '-'"
                >
                  {{ row.assetName || "-" }}
                </span>
              </div>
            </div>
            <div class="desc-text" :title="row.assetDescription">
              {{ row.assetDescription }}
            </div>
          </div>
        </template>

        <template #dataLevel="{ row }">
          <div
            style="display: flex; align-items: center"
            v-if="row.dataLevelName"
          >
            <LevelBadge :levelData="row.dataLevelName" />
          </div>
          <span v-else>-</span>
        </template>

        <template #validFlag="{ row }">
          <el-switch
            v-model="row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="true"
            :inactive-value="false"
            @change="(e) => handleStatusChange(row.id, row, e)"
          />
        </template>

        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            :disabled="row.validFlag === true"
            v-hasPermi="['dg:dgdesensitizelist:edit']"
          >
            修改
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            :disabled="row.validFlag === true"
            @click="handleDelete(row)"
            v-hasPermi="['dg:dgdesensitizelist:remove']"
          >
            删除
          </el-button>
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(row)"
            v-hasPermi="['dg:dgdesensitizelist:query']"
          >
            详情
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

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
        ref="sensitiveRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
      >
        <AssetColumnSelect v-model="form" />
        <qt-form-item label="状态" prop="validFlag">
          <el-radio-group v-model="form.validFlag">
            <el-radio
              v-for="opt in validFlagOptions"
              :key="opt.label"
              :label="opt.value"
            >
              {{ opt.label }}
            </el-radio>
          </el-radio-group>
        </qt-form-item>
        <el-form-item label="描述" prop="assetDescription" class="row-full">
          <el-input
            v-model="form.assetDescription"
            type="textarea"
            placeholder="请输入描述"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="row-full">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      :title="title"
      v-model="openDetail"
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
        ref="sensitiveDetailRef"
        :model="form"
        label-width="110px"
        class="column-form"
      >
        <el-form-item label="编号:" prop="id">
          <div class="form-readonly">{{ form.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="资产名称" prop="assetName">
          <div class="form-readonly">{{ form.assetName ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="数据分类" prop="dataCategoryName">
          <div class="form-readonly">
            {{ form.dataCategoryName ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="数据分级" prop="dataLevelName">
          <div v-if="form.dataLevelName">
            <LevelBadge :levelData="form.dataLevelName" />
          </div>
          <div v-else class="form-readonly">-</div>
        </el-form-item>

        <el-form-item label="表名" prop="assetTableName">
          <div class="form-readonly">
            {{ form.assetTableName || "-" }}
          </div>
        </el-form-item>
        <el-form-item label="字段名" prop="assetcolumnName">
          <div class="form-readonly">
            {{ form.assetcolumnName || "-" }}
          </div>
        </el-form-item>
        <el-form-item label="状态" prop="validFlag">
          <el-tag v-if="form.validFlag === true" type="primary">启用</el-tag>
          <el-tag v-else-if="form.validFlag === false" type="danger"
            >禁用</el-tag
          >
        </el-form-item>
        <el-form-item label="描述" prop="assetDescription" class="row-full">
          <div class="form-readonly textarea">
            {{ form.assetDescription ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="row-full">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="创建人" prop="createBy">
          <div class="form-readonly">{{ form.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item label="更新人" prop="updateBy">
          <div class="form-readonly">
            {{ form.updateBy ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item label="更新时间" prop="updateTime">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SensitiveList">
import {
  listDgDesensitizeList,
  getDgDesensitizeList,
  addDgDesensitizeList,
  updateDgDesensitizeList,
  delDgDesensitizeList,
} from "@/api/dg/safety/DgDesensitizeList";
import AssetColumnSelect from "./components/AssetColumnSelect.vue";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";
import LevelBadge from "@/views/dg/safety/dataLevel/components/LevelBadge.vue";
import { getCurrentInstance, onMounted, reactive, ref, toRefs } from "vue";
import {delDesensitizeWhitelist} from "@/api/dg/safety/whitelist/desensitizeWhitelist.js";

const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");

const store = reactive({
  rows: [],
});

const dataCategoryList = ref([]);
const allDataCategoryList = ref([]);

function getStatusLabel(v) {
  if (v === true) return "启用";
  if (v === false) return "禁用";
  const vv = v === 0 || v ? String(v) : "";
  const found = (dp_model_status.value || []).find(
    (d) => String(d.value) === vv
  );
  return found?.label || (vv ? vv : "-");
}

function initDataCategoryOptions() {
  selectTreeDataCategory().then((res) => {
    const rawData = res?.data || [];
    allDataCategoryList.value = rawData;
    const processTree = (nodes) => {
      return nodes.map((node) => {
        const newNode = { ...node };
        newNode.disabled = String(node.type) === "1";
        if (node.children && node.children.length > 0) {
          newNode.children = processTree(node.children);
        }
        return newNode;
      });
    };
    dataCategoryList.value = processTree(rawData);
  });
}

onMounted(() => {
  initDataCategoryOptions();
});

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
    { type: "selection", width: 55, align: "left" },
    { label: "编号", prop: "id", width: 60, sortable: true },
    {
      label: "资产名称/描述",
      prop: "assetName",
      align: "left",
      width: 260,
      slot: "assetNameDesc",
    },
    {
      label: "数据分类",
      prop: "dataCategoryName",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "数据分级",
      prop: "dataLevelName",
      slot: "dataLevel",
      align: "left",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "表名",
      prop: "assetTableName",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "字段名",
      prop: "assetcolumnName",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "状态",
      slot: "validFlag",
    },
    {
      label: "创建人",
      prop: "createBy",
      width: 120,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    {
      label: "操作",
      width: 220,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listDgDesensitizeList,
  params: {
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});
const validFlagOptions = [
  { label: "禁用", value: false },
  { label: "启用", value: true },
];
const searchStore = reactive({
  items: [
    {
      label: "资产名称",
      prop: "assetName",
      align: "left",
      component: { is: "input", placeholder: "请输入资产名称" },
    },
    {
      label: "数据分类",
      prop: "dataCategoryId",
      component: {
        is: "tree-select",
        data: allDataCategoryList,
        props: { value: "id", label: "name", children: "children" },
        valueKey: "id",
        placeholder: "请选择数据分类",
        checkStrictly: true,
      },
    },
    {
      label: "状态",
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: "请选择状态",
        options: validFlagOptions,
      },
    },
  ],
});

function handleQueryClick() {
  tableRef.value.getList();
}
function handleResetQueryClick() {
  tableRef.value.resetQuery();
}

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === true ? "启用" : "禁用";
  proxy.$modal
    .confirm('确认要"' + text + '","' + (row.assetName || "-") + '"吗？')
    .then(function () {
      updateDgDesensitizeList({ id, validFlag: row.validFlag }).then(() => {
        proxy.$modal.msgSuccess("操作成功");
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.validFlag = row.validFlag === true ? false : true;
    });
}

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const data = reactive({
  form: {},
  rules: {
    validFlag: [{ required: true, message: "状态不能为空", trigger: "change" }],
  },
});

const { form, rules } = toRefs(data);

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
    assetId: null,
    assetcolumnId: null,
    assetcolumnName: null,
    assetcolumnComment: null,
    dataCategoryId: null,
    assetName: null,
    assetDescription: null,
    dataCategoryCode: null,
    dataCategoryName: null,
    dataLevelName: null,
    assetTableName: null,
    assetTableComment: null,
    validFlag: false,
    remark: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  };
  proxy.resetForm("sensitiveRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  initDataCategoryOptions();
  open.value = true;
  title.value = "新增脱敏清单";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  initDataCategoryOptions();
  const _id = row?.id;
  getDgDesensitizeList(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改脱敏清单";
  });
}
/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  initDataCategoryOptions();
  const _id = row?.id;
  getDgDesensitizeList(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "脱敏清单详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["sensitiveRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDgDesensitizeList(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          tableRef.value.getList();
        });
      } else {
        addDgDesensitizeList(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          tableRef.value.getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
/*function handleDelete(row) {
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    _ids = store.rows.map((item) => item.id).join(",");

  }
  if (!_ids) return;

  proxy.$modal
    .confirm('是否确认删除编号为"' + _ids + '"的数据项？')
    .then(() => {
      delDgDesensitizeList(_ids).then(() => {
        tableRef.value.getList();
        proxy.$modal.msgSuccess("删除成功");
      });
    })
    .catch(() => {
      // 用户取消删除操作
    });
}*/
function handleDelete(row) {
  const invalidIds = [];
  const message=ref("确定要删除记录吗");
  if (row?.id) {
    invalidIds.push(row.id);
    message.value=`是否确认删除编号为${row.id} 的数据项？`
  }else {
    store.rows.forEach(item => {
      // 当 validFlag 为 false 时，记录 id
      if (item.validFlag === false) {
        invalidIds.push(item.id);
      }
    });
    message.value=`可删除${invalidIds.length}个，不可删除${store.rows.length-invalidIds.length}个，是否删除可删部分`
  }
  proxy.$modal
      .confirm(message.value)
      .then(() => {
        delDgDesensitizeList(invalidIds).then(() => {
          tableRef.value.getList();
          proxy.$modal.msgSuccess("删除成功");
        });
      })
      .catch(() => {
        // 用户取消删除操作
      });
}

</script>
