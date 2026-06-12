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
    <template #actions-data v-if="!isDetail">
      <el-button
        type="primary"
        plain
        @click="handleAdd"
        @mousedown="(e) => e.preventDefault()"
      >
        <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
      </el-button>
    </template>

    <qt-table v-bind="tableStore" ref="tableRef">
      <template #pkFlag="{ row }">
        <el-switch
          v-model="row.pkFlag"
          :active-value="'1'"
          :inactive-value="'0'"
          disabled
        />
      </template>
      <template #action="{ row }">
        <el-button link type="primary" icon="View" @click="handleDetail(row)"
          >{{ td('common.button.details') }}</el-button
        >
        <el-button
          v-if="!isDetail"
          link
          type="primary"
          icon="Edit"
          @click="handleUpdate(row)"
          >{{ td('common.button.update') }}</el-button
        >
        <el-button
          v-if="!isDetail"
          link
          type="danger"
          icon="Delete"
          @click="handleDelete(row)"
          >{{ td('common.button.delete') }}</el-button
        >
      </template>
    </qt-table>
  </qt-wrap>

  <!-- 新增或修改逻辑模型属性信息对话框 -->
  <el-dialog
    class="autoHeight"
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
    <el-form
      ref="dpModelColumnRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.relatedStandard')" prop="dataElemId">
            <el-select
              v-model="form.dataElemId"
              :placeholder="td('dp.modelForm.relatedStandardPlaceholder')"
              @change="handleDatasourceChange"
              filterable
              clearable
            >
              <el-option
                v-for="dict in DpData"
                :key="dict.id"
                :label="dict.name"
                :value="dict.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.model.chineseName')" prop="cnName">
            <el-input v-model="form.cnName" :placeholder="td('dp.model.chineseNamePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.model.englishName')" prop="engName">
            <el-input
              v-model="form.engName"
              :placeholder="td('dp.model.englishNamePlaceholder')"
              @input="convertToUpperCase('engName', form.engName)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.dataType')" prop="columnType">
            <el-select v-model="form.columnType" :placeholder="td('dp.modelForm.dataTypePlaceholder')">
              <el-option
                v-for="dict in column_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.attributeLength')" prop="columnLength">
            <el-input-number
              :step="1"
              step-strictly
              v-model="form.columnLength"
              style="width: 100%"
              controls-position="right"
              :min="1"
              :max="9999999999"
              :placeholder="td('dp.modelForm.attributeLengthPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <!-- decimal、NUMERIC、number -->
          <el-form-item :label="td('dp.modelForm.decimalPlaces')" prop="columnScale">
            <el-input-number
              :disabled="
                form.columnType !== 'DECIMAL' &&
                form.columnType !== 'NUMBER' &&
                form.columnType !== 'NUMERIC' &&
                form.columnType !== 'FLOAT' &&
                form.columnType !== 'DOUBLE'
              "
              :step="1"
              step-strictly
              v-model="form.columnScale"
              style="width: 100%"
              controls-position="right"
              :min="0"
              :max="9999999999"
              :placeholder="td('dp.modelForm.decimalPlacesPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.defaultValue')" prop="defaultValue">
            <el-input v-model="form.defaultValue" :placeholder="td('dp.modelForm.defaultValuePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="modelComment">
            <el-input
              v-model="form.modelComment"
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.remark')">
            <el-input
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="td('common.form.remarkPlaceholder')"
              v-model="form.remark"
              :min-height="192"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.isPrimaryKey')" prop="pkFlag">
            <el-radio-group v-model="form.pkFlag" @change="handlePkFlagChange">
              <el-radio
                v-for="dict in dp_model_column_pk_flag"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.modelForm.isRequired')" prop="nullableFlag">
            <el-radio-group
              v-model="form.nullableFlag"
              :disabled="form.pkFlag == 1"
            >
              <el-radio
                v-for="dict in dp_model_column_nullable_flag"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" size="mini" @click="submitForm"
          >{{ td('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>

  <!-- 逻辑模型属性信息详情对话框 -->
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
      ref="dpModelColumnDetailRef"
      :model="form"
      label-width="110px"
      class="column-form"
    >
      <el-form-item :label="td('common.texts.number')" prop="id">
        <div class="form-readonly">
          {{ form.id }}
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.relatedDataStandard')" prop="dataElemId">
        <div class="form-readonly">{{ form.dataElemName || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.model.chineseName')" prop="cnName">
        <div class="form-readonly">{{ form.cnName || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.model.englishName')" prop="engName">
        <div class="form-readonly">{{ form.engName || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.dataType')" prop="columnType">
        <div class="form-readonly">
          <dict-tag :options="column_type" :value="form.columnType" />
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.attributeLength')" prop="columnLength">
        <div class="form-readonly">{{ form.columnLength || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.decimalPlaces')" prop="columnScale">
        <div class="form-readonly">{{ form.columnScale ?? "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.defaultValue')" prop="defaultValue">
        <div class="form-readonly">{{ form.defaultValue || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.isPrimaryKey')" prop="pkFlag">
        <div class="form-readonly">
          <dict-tag :options="dp_model_column_pk_flag" :value="form.pkFlag" />
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.isRequired')" prop="nullableFlag">
        <div class="form-readonly">
          <dict-tag
            :options="dp_model_column_nullable_flag"
            :value="form.nullableFlag"
          />
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.sortOrder')" prop="sortOrder">
        <div class="form-readonly">{{ form.sortOrder || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('common.texts.description')" prop="modelComment" class="row-full">
        <div class="form-readonly textarea">
          {{ form.modelComment || "-" }}
        </div>
      </el-form-item>
      <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full">
        <div class="form-readonly textarea">{{ form.remark || "-" }}</div>
      </el-form-item>

      <el-form-item :label="td('common.texts.createdBy')" prop="createBy">
        <div class="form-readonly">
          {{ form.createBy }}
        </div>
      </el-form-item>

      <el-form-item :label="td('common.texts.createdTime')" prop="createTime">
        <div class="form-readonly">
          {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
        </div>
      </el-form-item>

      <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy">
        <div class="form-readonly">
          {{ form.updateBy }}
        </div>
      </el-form-item>

      <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime">
        <div class="form-readonly">
          {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">{{ td('common.button.close') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  listDpModelColumn,
  getDpModelColumn,
  delDpModelColumns,
  addDpModelColumn,
  updateDpModelColumns,
} from "@/api/dp/model/model";
import { getDpDataElemList } from "@/api/dp/dataElem/dataElem";
import { deptTreeSelectNoPermi } from "@/api/system/system/user.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const props = defineProps({
  isDetail: {
    type: Boolean,
    default: false,
  },
});
const { column_type, dp_model_column_pk_flag, dp_model_column_nullable_flag } =
  proxy.useDict(
    "column_type",
    "dp_model_column_pk_flag",
    "dp_model_column_nullable_flag"
  );
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const title = ref("");
const deptList = ref([]);
const DpData = ref([]);
const handlePkFlagChange = (value) => {
  if (value == 1) {
    form.value.nullableFlag = "1";
  }
};
const getDpDataElem = async () => {
  try {
    const response = await getDpDataElemList();
    DpData.value = response.data;
  } catch (error) {
    console.error("请求失败:", error);
  }
};
const handleDatasourceChange = (value) => {
  const selectedDatasource = DpData.value.find((item) => item.id === value);
  if (selectedDatasource) {
    form.value.dataElemName = selectedDatasource.name;
    form.value.cnName = selectedDatasource.name;
    form.value.engName = selectedDatasource.engName;
    form.value.columnType = selectedDatasource.columnType;
  }
};
const data = reactive({
  dpModelColumnDetail: {},
  form: {
    pkFlag: "0",
    nullableFlag: "0",
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    modelId: null,
    engName: null,
    cnName: null,
    columnType: null,
    columnLength: null,
    columnScale: null,
    defaultValue: null,
    pkFlag: null,
    nullableFlag: null,
    sortOrder: null,
    authorityDept: null,
    dataElemId: null,
    createTime: null,
  },
  rules: {
    cnName: [{ required: true, message: td('dp.dataElem.nameZhRequired'), trigger: "blur" }],
    engName: [{ required: true, message: td('dp.dataElem.nameEnRequired'), trigger: "blur" }],
    columnType: [
      { required: true, message: td('dp.modelForm.dataTypeRequired'), trigger: "blur" },
    ],
  },
});
const { queryParams, form, dpModelColumnDetail, rules } = toRefs(data);

const route = useRoute();
let modelId = route.query.id || 1;
const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      defaultSort: { prop: "id", order: "ascending" },
    },
  },
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },
    { label: td('dp.modelForm.relatedStandard'), prop: "dataElemName", align: "left", width: 240 },
    {
      label: td('dp.model.chineseName'),
      prop: "cnName",
      width: 240,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dp.model.englishName'),
      prop: "engName",
      width: 240,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.description'),
      prop: "description",
      align: "left",
      width: 250,
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dp.modelForm.dataType'), prop: "columnType", width: 120 },

    { label: td('dp.modelForm.attributeLength'), prop: "columnLength", width: 120 },
    {
      label: td('dp.modelForm.isPrimaryKey'),
      prop: "pkFlag",
      slot: "pkFlag",
    },
    {
      label: td('common.texts.createdBy'),
      prop: "createBy",
      width: 160,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.createdTime'),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      width: 160,
      date: true,
    },
    {
      label: td('common.texts.operation'),
      align: "center",
      fixed: "right",
      width: 240,
      slot: "action",
    },
  ],
  func: listWrapper,
  params: {},
  events: {
    formatData(data) {
      const findLabel = (tree, id) => {
        for (let node of tree) {
          if (node.id == id) return node.label;
          if (node.children) {
            const found = findLabel(node.children, id);
            if (found) return found;
          }
        }
        return null;
      };
      return data.map((item) => ({
        ...item,
        deptLabel: findLabel(deptList.value, item.authorityDept) || "-",
      }));
    },
  },
});
function listWrapper(params) {
  const p = { ...params, modelId };
  return listDpModelColumn(p);
}
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    modelId = newId || 1; // 如果 id 为空，使用默认值 1
    handleQuery();

    // getList();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
// 转换输入值为大写
const convertToUpperCase = (key, value) => {
  const uppercasedValue = value.replace(/[a-z]/g, (char) => char.toUpperCase());

  form.value[key] = uppercasedValue;

  console.log("🚀 ~ convertToUpperCase ~ form.value[key]:", form.value[key]);
};
function getList() {
  tableRef.value?.getList();
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
    modelId: null,
    engName: null,
    cnName: null,
    columnType: null,
    columnLength: null,
    columnScale: null,
    defaultValue: null,
    pkFlag: "0",
    nullableFlag: "0",
    sortOrder: null,
    authorityDept: null,
    dataElemId: null,
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
  proxy.resetForm("dpModelColumnRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  getList();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

// 排序由 qt-table 内部处理并写入 params

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('dp.modelForm.addModelProperty');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpModelColumn(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dp.modelForm.editModelProperty');
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDpModelColumn(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('dp.modelForm.modelPropertyDetail');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dpModelColumnRef"].validate((valid) => {
    if (valid) {
      form.value.modelId = modelId;
      if (form.value.id != null) {
        updateDpModelColumns(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addDpModelColumn(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.modelForm.confirmDeleteProperty', { id: _ids }))
    .then(function () {
      return delDpModelColumns(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "dp/modelColumn/export",
    {
      ...tableStore.params,
    },
    `dpModelColumn_${new Date().getTime()}.xlsx`
  );
}
function getDeptList() {
  deptTreeSelectNoPermi().then((response) => {
    deptList.value = response.data;
    getList();
  });
}
getDpDataElem();
getDeptList();
</script>
