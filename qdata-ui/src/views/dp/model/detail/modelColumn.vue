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

  <!-- Add or modify logical model attribute information dialog box -->
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
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.relatedStandard')" prop="dataElemId" :label-position="labelPosition">
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
          <el-form-item :label="td('dp.model.chineseName')" prop="cnName" :label-position="labelPosition">
            <el-input v-model="form.cnName" :placeholder="td('dp.model.chineseNamePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.model.englishName')" prop="engName" :label-position="labelPosition">
            <el-input
              v-model="form.engName"
              :placeholder="td('dp.model.englishNamePlaceholder')"
              @input="convertToUpperCase('engName', form.engName)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.dataType')" prop="columnType" :label-position="labelPosition">
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
          <el-form-item :label="td('dp.modelForm.attributeLength')" prop="columnLength" :label-position="labelPosition">
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
          <!-- decimal, NUMERIC, number -->
          <el-form-item :label="td('dp.modelForm.decimalPlaces')" prop="columnScale" :label-position="labelPosition">
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
          <el-form-item :label="td('dp.modelForm.defaultValue')" prop="defaultValue" :label-position="labelPosition">
            <el-input v-model="form.defaultValue" :placeholder="td('dp.modelForm.defaultValuePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="modelComment" :label-position="labelPosition">
            <el-input
              v-model="form.modelComment"
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
            <el-input
              type="textarea"
              maxlength="500"
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
          <el-form-item :label="td('dp.modelForm.isPrimaryKey')" prop="pkFlag" :label-position="labelPosition">
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
          <el-form-item :label="td('dp.modelForm.isRequired')" prop="nullableFlag" :label-position="labelPosition">
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

  <!-- Logical model attribute information details dialog box -->
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
     :label-position="labelPosition">
      <el-form-item :label="td('common.texts.number')" prop="id" :label-position="labelPosition">
        <div class="form-readonly">
          {{ form.id }}
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.relatedDataStandard')" prop="dataElemId" :label-position="labelPosition">
        <div class="form-readonly">{{ form.dataElemName || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.model.chineseName')" prop="cnName" :label-position="labelPosition">
        <div class="form-readonly">{{ form.cnName || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.model.englishName')" prop="engName" :label-position="labelPosition">
        <div class="form-readonly">{{ form.engName || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.dataType')" prop="columnType" :label-position="labelPosition">
        <div class="form-readonly">
          <dict-tag :options="column_type" :value="form.columnType" />
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.attributeLength')" prop="columnLength" :label-position="labelPosition">
        <div class="form-readonly">{{ form.columnLength || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.decimalPlaces')" prop="columnScale" :label-position="labelPosition">
        <div class="form-readonly">{{ form.columnScale ?? "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.defaultValue')" prop="defaultValue" :label-position="labelPosition">
        <div class="form-readonly">{{ form.defaultValue || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.isPrimaryKey')" prop="pkFlag" :label-position="labelPosition">
        <div class="form-readonly">
          <dict-tag :options="dp_model_column_pk_flag" :value="form.pkFlag" />
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.isRequired')" prop="nullableFlag" :label-position="labelPosition">
        <div class="form-readonly">
          <dict-tag
            :options="dp_model_column_nullable_flag"
            :value="form.nullableFlag"
          />
        </div>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.sortOrder')" prop="sortOrder" :label-position="labelPosition">
        <div class="form-readonly">{{ form.sortOrder || "-" }}</div>
      </el-form-item>
      <el-form-item :label="td('common.texts.description')" prop="modelComment" class="row-full" :label-position="labelPosition">
        <div class="form-readonly textarea">
          {{ form.modelComment || "-" }}
        </div>
      </el-form-item>
      <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
        <div class="form-readonly textarea">{{ form.remark || "-" }}</div>
      </el-form-item>

      <el-form-item :label="td('common.texts.createdBy')" prop="createBy" :label-position="labelPosition">
        <div class="form-readonly">
          {{ form.createBy }}
        </div>
      </el-form-item>

      <el-form-item :label="td('common.texts.createdTime')" prop="createTime" :label-position="labelPosition">
        <div class="form-readonly">
          {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
        </div>
      </el-form-item>

      <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy" :label-position="labelPosition">
        <div class="form-readonly">
          {{ form.updateBy }}
        </div>
      </el-form-item>

      <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime" :label-position="labelPosition">
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
    console.error("Request failed:", error);
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
// Monitor id changes
watch(
  () => route.query.id,
  (newId) => {
    modelId = newId || 1; // If id is empty, the default value 1 is used
    handleQuery();

    // getList();
  },
  { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
// Convert input value to uppercase
const convertToUpperCase = (key, value) => {
  const uppercasedValue = value.replace(/[a-z]/g, (char) => char.toUpperCase());

  form.value[key] = uppercasedValue;

  console.log("🚀 ~ convertToUpperCase ~ form.value[key]:", form.value[key]);
};
function getList() {
  tableRef.value?.getList();
}

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// form reset
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

/** Search button action */
function handleQuery() {
  getList();
}

/** reset button action */
function resetQuery() {
  getList();
}

// Multiple selection box selected data
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

// Sorting is handled internally by qt-table and written to params

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('dp.modelForm.addModelProperty');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpModelColumn(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dp.modelForm.editModelProperty');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDpModelColumn(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('dp.modelForm.modelPropertyDetail');
  });
}

/** submit button */
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

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.modelForm.confirmDeleteProperty').replace('<id>', _ids))
    .then(function () {
      return delDpModelColumns(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** Export button action */
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
