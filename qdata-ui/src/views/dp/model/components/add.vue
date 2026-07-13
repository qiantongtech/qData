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
    v-model="visibleDialog"
    draggable
    class="large-dialog"
    destroy-on-close
    @close="reset"
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form
      ref="dpModelRef"
      :model="form"
      :rules="rules"
      label-width="110px"
      @submit.prevent
      class="column-form"
     :label-position="labelPosition">
      <div class="h2-title row-full">{{ td('dp.modelForm.basicInfo') }}</div>
      <qt-form-item
        v-if="!form.id"
        :label="td('dp.modelForm.createType')"
        prop="createType"
        :tip="{
          content: td('dp.modelForm.createTypeTip'),
          custom: true,
        }"
      >
        <el-radio-group v-model="form.createType">
          <el-radio
            v-for="dict in dp_model_create_type"
            :key="dict.value"
            :value="dict.value"
            >{{ dict.label }}</el-radio
          >
        </el-radio-group>
      </qt-form-item>
      <el-form-item v-else :label="td('dp.modelForm.tableType')" prop="tableType" :label-position="labelPosition">
        <el-select v-model="form.tableType" disabled style="width: 100%">
          <el-option
            v-for="item in table_type"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.dataLayer')" prop="dataLayerId" :label-position="labelPosition">
        <el-tree-select
          v-model="form.dataLayerId"
          :data="dataLayerList"
          :loading="layerLoading"
          :props="{ value: 'id', label: 'displayName', children: 'children' }"
          node-key="id"
          value-key="id"
          :placeholder="td('dp.modelForm.dataLayerPlaceholder')"
          check-strictly
          filterable
          default-expand-all
          clearable
          style="width: 100%"
        />
      </el-form-item>

      <template v-if="form.tableType != '4'">
        <el-form-item :label="td('dp.modelForm.businessCategory')" prop="businessDomainId" :label-position="labelPosition">
          <el-tree-select
            v-model="form.businessDomainId"
            :data="businessCategoryList"
            :loading="businessLoading"
            :props="{ value: 'id', label: 'displayName', children: 'children' }"
            node-key="id"
            value-key="id"
            :placeholder="td('dp.modelForm.businessCategoryPlaceholder')"
            check-strictly
            filterable
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item :label="td('dp.modelForm.dataDomain')" prop="dataDomainId" :label-position="labelPosition">
          <el-tree-select
            v-model="form.dataDomainId"
            :data="dataDomainList"
            :loading="domainLoading"
            :props="{ value: 'id', label: 'displayName', children: 'children' }"
            node-key="id"
            value-key="id"
            :placeholder="td('dp.modelForm.dataDomainPlaceholder')"
            check-strictly
            filterable
            clearable
            style="width: 100%"
          />
        </el-form-item>
      </template>

      <template v-else>
        <el-form-item :label="td('dp.modelForm.themeDomain')" prop="themeDomainId" :label-position="labelPosition">
          <el-tree-select
            v-model="form.themeDomainId"
            :data="themeDomainList"
            :loading="themeLoading"
            :props="{ value: 'id', label: 'displayName', children: 'children' }"
            node-key="id"
            value-key="id"
            :placeholder="td('dp.modelForm.themeDomainPlaceholder')"
            check-strictly
            filterable
            clearable
            style="width: 100%"
          />
        </el-form-item>
      </template>
      <el-form-item :label="td('dp.modelForm.modelName')" prop="modelName" :label-position="labelPosition">
        <el-input
          v-model="form.modelName"
          :placeholder="td('dp.modelForm.modelNamePlaceholder')"
          @input="form.modelName = form.modelName.replace(/[^A-Za-z0-9_]/g, '')"
        />
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.modelComment')" prop="modelComment" :label-position="labelPosition">
        <el-input v-model="form.modelComment" :placeholder="td('dp.modelForm.modelCommentPlaceholder')" />
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.tableCase')" prop="tableCase" :label-position="labelPosition">
        <el-select
          v-model="form.tableCase"
          :placeholder="td('dp.modelForm.tableCasePlaceholder')"
          style="width: 100%"
        >
          <el-option
            v-for="option in namingOptions"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </el-select>
      </el-form-item>

      <template v-if="form.tableType != '3'">
        <el-form-item :label="td('dp.modelForm.documentType')" prop="description" :label-position="labelPosition">
          <el-select
            class="el-form-input-width"
            v-model="form.documentType"
            :placeholder="td('dp.modelForm.documentTypePlaceholder')"
            clearable
            @change="fetchSecondLevelDocs"
            style="width: 100%"
          >
            <el-option
              v-for="dict in dp_document_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="td('dp.modelForm.documentId')" prop="documentId" :label-position="labelPosition">
          <el-select
            class="el-form-input-width"
            v-model="form.documentId"
            :placeholder="td('dp.modelForm.documentIdPlaceholder')"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="doc in secondLevelDocs"
              :key="doc.value"
              :label="doc.label"
              :value="Number(doc.value)"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </template>
      <el-form-item :label="td('dp.modelForm.contact')" prop="contact" :label-position="labelPosition">
        <el-tree-select
          filterable
          v-model="form.contact"
          :data="userList"
          :props="{
            value: 'userId',
            label: 'nickName',
            children: 'children',
          }"
          value-key="ID"
          :placeholder="td('dp.modelForm.contactPlaceholder')"
          check-strictly
          @change="handleContactChange"
        />
      </el-form-item>
      <el-form-item :label="td('dp.modelForm.contactNumber')" prop="contactNumber" :label-position="labelPosition">
        <el-input
          v-model="form.contactNumber"
          :placeholder="td('dp.modelForm.contactNumberPlaceholder')"
          disabled
        />
      </el-form-item>

      <qt-form-item
        :label="td('common.texts.status')"
        prop="status"
        :tip="{
          content: td('dp.modelForm.statusTip'),
        }"
      >
        <el-radio-group v-model="form.status">
          <el-radio
            v-for="dict in dp_model_status"
            :key="dict.value"
            :value="dict.value"
            >{{ dict.label }}</el-radio
          >
        </el-radio-group>
      </qt-form-item>
      <el-form-item :label="td('common.texts.description')" prop="description" class="row-full" :label-position="labelPosition">
        <el-input
          v-model="form.description"
          type="textarea"
          maxlength="500"
          show-word-limit
          :placeholder="td('common.form.descriptionPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
        <el-input
          v-model="form.remark"
          type="textarea"
          maxlength="500"
          show-word-limit
          :placeholder="td('common.form.remarkPlaceholder')"
        />
      </el-form-item>

      <template v-if="form.createType == 2 && !form.id">
        <div class="h2-title row-full">{{ td('dp.modelForm.dataSource') }}</div>
        <el-form-item
          :label="td('dp.modelForm.datasourceName')"
          prop="datasourceId"
          :rules="[
            {
              required: true,
              message: td('dp.modelForm.datasourceRequired'),
              trigger: 'change',
            },
          ]"
         :label-position="labelPosition">
          <DatasourceList
            v-model="form.datasourceId"
            :placeholder="td('dp.modelForm.datasourceNamePlaceholder')"
            @change="handleDatasourceChange"
            filterable
            flag="dpModel"
          />
        </el-form-item>
        <el-form-item :label="td('dp.modelForm.datasourceType')" prop="datasourceType" :label-position="labelPosition">
          <el-input
            v-model="form.datasourceType"
            :placeholder="td('dp.modelForm.datasourceTypePlaceholder')"
            disabled
          />
        </el-form-item>
        <el-form-item :label="td('dp.modelForm.datasourceAddress')" prop="ip" :label-position="labelPosition">
          <el-input v-model="form.ip" :placeholder="td('dp.modelForm.datasourceAddressPlaceholder')" disabled />
        </el-form-item>
        <el-form-item
          :label="td('dp.modelForm.selectTable')"
          prop="tableName"
          :rules="[
            { required: true, message: td('dp.modelForm.tableRequired'), trigger: 'change' },
          ]"
         :label-position="labelPosition">
          <el-select
            v-model="form.tableName"
            :placeholder="td('dp.modelForm.selectTablePlaceholder')"
            filterable
            remote
            :remote-method="remoteSearchTables"
            @visible-change="handleTableSelectVisible"
            @change="handleChange(true)"
          >
            <el-option
              v-for="item in TablesByDataSource"
              :key="item.tableName"
              :label="item.tableName"
              :value="item.tableName"
            />
          </el-select>
        </el-form-item>
      </template>
    </el-form>

    <div class="h2-title">{{ td('dp.modelForm.attributeFields') }}</div>
    <el-button
      style="margin-bottom: 5px; margin-top: 10px"
      type="primary"
      plain
      @click="handleAdd"
      size="small"
      @mousedown="(e) => e.preventDefault()"
    >
      <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
    </el-button>
    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column :label="td('common.texts.number')" type="index" align="left" width="60" />
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :align="column.align"
        :show-overflow-tooltip="{ effect: 'light' }"
      >
        <template #header>
          <span v-if="!column.tip">{{ column.label }}</span>

          <div
            class="tip-content"
            style="display: flex; align-items: center; gap: 2px"
            v-else
          >
            {{ column.label }}
            <el-tooltip effect="light" placement="top">
              <template #content>
                <div class="tip-content" v-html="column.tip.content"></div>
              </template>
              <el-icon class="tip-icon" color="#888"> <InfoFilled /> </el-icon>
            </el-tooltip>
          </div>
        </template>
        <template v-if="column.prop === 'pkFlag'" #default="{ row }">
          <el-switch
            v-model="row[column.prop]"
            :active-value="'1'"
            :inactive-value="'0'"
            disabled
          />
        </template>
        <template v-if="column.prop === 'authorityDept'" #default="{ row }">
          {{ getDeptLabel(row) }}
        </template>
        <template
          v-else-if="column.type === 'button'"
          #default="{ row, $index }"
        >
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="editRow(row, $index)"
            >{{ td('common.button.update') }}</el-button
          >
          <el-button link type="danger" icon="Delete" @click="deleteRow(row)"
            >{{ td('common.button.delete') }}</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="confirmDialog">{{ td('common.button.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>

  <columnAdd
    :visible="addDialog"
    @update:dialogFormVisible="addDialog = $event"
    @confirm="handleFormSubmit"
    :deptOptions="deptOptions"
    :userList="userList"
    :deptList="deptList"
    :row="selectedRow"
    :data="form"
  />
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"

const { proxy } = getCurrentInstance();
import { listDpDocument } from "@/api/dp/document/document";
import { listDataLayerSpecification } from "@/api/dm/dataLayerSpecification/dataLayerSpecification";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import { listBusinessCategory } from "@/api/dm/businessCategory/businessCategory";
import { listDataDomain } from "@/api/dm/dataDomain/dataDomain";
import { listThemeDomain } from "@/api/dm/themeDomain/themeDomain";
import DatasourceList from "@/components/Datasource/List.vue";
import {
  getDatasourceData,
  getAvailableDatasource,
} from "@/components/Datasource/utils.js";
import {
  getDaDatasourceList,
  tableList,
  columnsList,
} from "@/api/dp/model/model";
import columnAdd from "./columnAdd";
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import { getDpModelColumnList } from "@/api/dp/model/model";
import { findInTree, formatModelName } from "../../../../utils/dm/utils";

const { td } = useDefaultLang();
const {
  dp_model_status,
  dp_model_create_type,
  dp_document_type,
  table_type,
  table_name_case,
} = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type",
  "dp_document_type",
  "table_type",
  "table_name_case"
);

const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  deptOptions: { type: Array, default: () => [] },
  column_type: { type: Array, default: () => [] },
  userList: { type: Array, default: () => [] },
  deptList: { type: Array, default: () => [] },
  dataList: { type: Object, default: () => {} },
  catCode: { type: Object, default: () => {} },
  type: { type: String, default: "" },
});

const emit = defineEmits(["update:dialogFormVisible", "confirm", "submit"]);

// --- Reactive variable declaration (advance to top) ---
let loading = ref(false);
let layerLoading = ref(false);
let businessLoading = ref(false);
let domainLoading = ref(false);
let themeLoading = ref(false);
let dataLayerList = ref([]);
let businessCategoryList = ref([]);
let dataDomainList = ref([]);
let themeDomainList = ref([]);
let tableData = ref([]);
let addDialog = ref(false);
let selectedRow = ref({});
const isResetting = ref(false);
const isInitializing = ref(false);

const form = ref({
  id: undefined,
  modelName: "",
  tableCase: 1,
  namingSpec: "",
  modelComment: "",
  catCode: props.catCode,
  createType: "1",
  contact: "",
  contactNumber: "",
  description: "",
  remark: "",
  status: "0",
  dataLayerId: null,
  businessDomainId: null,
  businessCategoryCode: "",
  dataDomainId: null,
  themeDomainId: null,
  themeDomainCode: "",
  tableType: props.type,
  datasourceId: "",
  datasourceType: "",
  datasourceName: "",
  datasourceConfig: "",
  ip: "",
  port: "",
  tableName: "",
  documentId: "",
  documentType: "",
});

// --------------------------------

const formatTreeData = (list) => {
  return list.map((item) => {
    const newItem = { ...item };
    newItem.id = Number(item.id); // Cast to number to match echo
    const abbreviation = item.engName || item.shortName;
    newItem.displayName = abbreviation
      ? `${item.name} / ${abbreviation}`
      : item.name;
    if (item.children && item.children.length) {
      newItem.children = formatTreeData(item.children);
    }
    return newItem;
  });
};

const fetchAllOptions = (currentType) => {
  const targetType = currentType || props.type || form.value.tableType;
  // Data warehouse stratification
  layerLoading.value = true;
  const p1 = treeDataLayer()
    .then((res) => {
      const tree = res.data || res.rows || [];
      const processTree = (list) => {
        return list.map((item) => {
          const newItem = { ...item };
          newItem.id = Number(item.id); // Cast to number to match echo
          const abbreviation = item.engName || item.shortName;
          newItem.displayName = abbreviation
            ? `${item.name} / ${abbreviation}`
            : item.name;
          if (!item.parentId || item.parentId === 0 || item.parentId === "0") {
            newItem.disabled = true;
          }
          if (item.children && item.children.length) {
            newItem.children = processTree(item.children);
          }
          return newItem;
        });
      };
      dataLayerList.value = processTree(tree);
    })
    .finally(() => {
      layerLoading.value = false;
    });

  let p2;
  if (targetType == "4") {
    // subject area
    themeLoading.value = true;
    p2 = listThemeDomain({ pageNum: 1, pageSize: 1000, validFlag: true })
      .then((res) => {
        const tree = proxy.handleTree(
          res.data?.rows || res.data || res.rows || [],
          "id",
          "parentId"
        );
        themeDomainList.value = formatTreeData(tree);
      })
      .finally(() => {
        themeLoading.value = false;
      });
  } else {
    // Business classification
    businessLoading.value = true;
    p2 = listBusinessCategory({
      pageNum: 1,
      pageSize: 1000,
      orderByColumn: "create_time",
      isAsc: "descending",
      validFlag: true,
    })
      .then((res) => {
        const tree = proxy.handleTree(
          res.data?.rows || res.data || res.rows || [],
          "id",
          "parentId"
        );
        businessCategoryList.value = formatTreeData(tree);
      })
      .finally(() => {
        businessLoading.value = false;
      });
  }
  return Promise.all([p1, p2]);
};

const fetchDataDomain = (businessDomainId) => {
  if (!businessDomainId) {
    dataDomainList.value = [];
    return Promise.resolve();
  }
  const query = {
    pageNum: 1,
    pageSize: 1000,
    orderByColumn: "create_time",
    isAsc: "descending",
    businessCategoryId: businessDomainId,
    validFlag: true,
  };
  domainLoading.value = true;
  return listDataDomain(query)
    .then((res) => {
      const tree = proxy.handleTree(
        res.data?.rows || res.data || res.rows || [],
        "id",
        "parentId"
      );
      dataDomainList.value = formatTreeData(tree);
    })
    .finally(() => {
      domainLoading.value = false;
    });
};

const generateModelName = (initialRow = null) => {
  if (isResetting.value) return;

  if (initialRow) {
    // To modify the first echo in mode, directly use formatModelName to pass parameters and splice the English name of the table
    form.value.namingSpec = formatModelName(form.value);
    return;
  }

  if (isInitializing.value) return;

  const options = {
    dataLayerList: dataLayerList.value,
    dataLayerId: form.value.dataLayerId,
    type: props.type,
    themeDomainList: themeDomainList.value,
    themeDomainId: form.value.themeDomainId,
    businessCategoryList: businessCategoryList.value,
    businessDomainId: form.value.businessDomainId,
    dataDomainList: dataDomainList.value,
    dataDomainId: form.value.dataDomainId,
    modelNameSuffix: form.value.modelName,
    tableCase: form.value.tableCase,
  };

  form.value.namingSpec = formatModelName(options);

  // Sync business/topic codes
  if (form.value.tableType === "4") {
    const theme = findInTree(themeDomainList.value, form.value.themeDomainId);
    form.value.themeDomainCode = theme ? theme.code : "";
  } else {
    const biz = findInTree(
      businessCategoryList.value,
      form.value.businessDomainId
    );
    form.value.businessCategoryCode = biz ? biz.code : "";
  }
};

watch(
  () => form.value.businessDomainId,
  (newVal) => {
    if (newVal) {
      fetchDataDomain(newVal);
    } else {
      dataDomainList.value = [];
    }
    if (!isInitializing.value) {
      form.value.dataDomainId = null;
    }
  }
);

watch(
  [
    () => form.value.dataLayerId,
    () => form.value.businessDomainId,
    () => form.value.dataDomainId,
    () => form.value.themeDomainId,
    () => form.value.modelName,
    () => form.value.tableCase,
  ],
  () => {
    generateModelName();
  }
);

watch(
  () => props.visible,
  (newVal) => {
    getDaDatasourceListList();
    if (newVal) {
      isInitializing.value = true;
      // In modification mode, props.dataList.tableType is used first
      const currentType =
        props.dataList && props.dataList.tableType
          ? String(props.dataList.tableType)
          : props.type;
      // Parallel requests without await blocking
      fetchAllOptions(currentType);

      if (props.dataList && props.dataList.id) {
        const echoData = { ...props.dataList };
        // Map the business category ID and process the businessCategoryId returned by the details interface
        if (echoData.businessCategoryId) {
          echoData.businessDomainId = echoData.businessCategoryId;
        }
        if (echoData.businessDomainId)
          echoData.businessDomainId = Number(echoData.businessDomainId);
        if (echoData.dataDomainId)
          echoData.dataDomainId = Number(echoData.dataDomainId);
        if (echoData.dataLayerId)
          echoData.dataLayerId = Number(echoData.dataLayerId);
        if (echoData.themeDomainId)
          echoData.themeDomainId = Number(echoData.themeDomainId);
        if (echoData.contact) echoData.contact = Number(echoData.contact);
        if (echoData.tableType) echoData.tableType = String(echoData.tableType);

        Object.assign(form.value, echoData);

        // Initialize naming case mode
        if (echoData.tableCase !== undefined && echoData.tableCase !== null) {
          form.value.tableCase = Number(echoData.tableCase);
        } else if (form.value.modelName) {
          form.value.tableCase =
            form.value.modelName === form.value.modelName.toUpperCase() ? 1 : 2;
        } else {
          form.value.tableCase = 1;
        }
        form.value.tableType = form.value.tableType || props.type;
        form.value.documentId =
          echoData.documentId && echoData.documentId != -1
            ? Number(echoData.documentId)
            : "";
        form.value.documentType = echoData.documentType
          ? String(echoData.documentType)
          : "";
        if (form.value.documentType) {
          fetchSecondLevelDocs(form.value.documentType, true);
        }

        // Get column information
        getDpModelColumnList({ modelId: form.value.id }).then((res) => {
          tableData.value = res.data || [];
        });
      } else {
        reset();
      }

      // Delay the end of the initialization state to ensure that the watch caused by echo assignment will not trigger generateModelName
      setTimeout(() => {
        isInitializing.value = false;
        // After the initialization is completed, manually trigger a name generation to ensure that the namingSpec is correct when echoed.
        // In the modification mode, pass in props.dataList and use formatHierarchyName to pass parameters and echo.
        generateModelName(
          props.dataList && props.dataList.id ? props.dataList : null
        );
      }, 200);
    }
  }
);
let secondLevelDocs = ref([]);
const btnloading = ref(false);
const fetchSecondLevelDocs = async (type, preserveSelection = false) => {
  if (!type) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = "";
    }
    return;
  }

  try {
    btnloading.value = true;
    const tempDocId = preserveSelection ? form.value.documentId : "";
    secondLevelDocs.value = [];
    const res = await listDpDocument({ type });
    secondLevelDocs.value = (res.data.rows || []).map((d) => ({
      label: d.name,
      value: d.id,
    }));

    if (tempDocId) {
      form.value.documentId = tempDocId;
    } else if (!preserveSelection) {
      form.value.documentId = "";
    }
  } catch (error) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = "";
    }
  } finally {
    btnloading.value = false;
  }
};

let createTypeList = ref();
const getDaDatasourceListList = async () => {
  try {
    const response = await getDatasourceData();
    createTypeList.value = getAvailableDatasource(response, "dpModel");
    console.log("createTypeList.value", createTypeList.value);
  } catch (error) {
    console.error("Request failed:", error);
  }
};
// table
let TablesByDataSource = ref([]);
const remoteSearchTables = async (query) => {
  if (!form.value.datasourceId) {
    TablesByDataSource.value = [];
    return;
  }
  try {
    const response = await tableList({
      datasourceId: form.value.datasourceId,
      tableName: query,
    });
    TablesByDataSource.value = response.data;
  } catch (error) {}
};
const fetchDpModelColumnList = async () => {
  try {
    loading.value = true;
    console.log("props.dataList.id", form.value.id);
    const response = await getDpModelColumnList({ modelId: form.value.id }); // Pass `form` data
    tableData.value = response.data;
    loading.value = false;
    // Process the returned data
  } catch (error) {
    console.error("Request failed:", error);
  }
};
const getColumnByAssetIdList = async (isOld) => {
  loading.value = true;
  const response = await columnsList({
    modelId: form.value.id,
    id: form.value.datasourceId,
    tableName: form.value.tableName,
    type: form.value.datasourceType,
    isOld: isOld,
  });
  tableData.value = response.data;
  loading.value = false;
};
const handleDatasourceChange = (value, selectedDatasource) => {
  if (selectedDatasource) {
    form.value.tableName = "";
    TablesByDataSource.value = [];
    tableData.value = [];
    form.value.ip = selectedDatasource.ip;
    form.value.datasourceConfig = selectedDatasource.datasourceConfig;
    form.value.datasourceType = selectedDatasource.datasourceType;
    form.value.datasourceName = selectedDatasource.datasourceName;
    form.value.port = selectedDatasource.port;
    remoteSearchTables();
  }
};
const handleTableSelectVisible = (visible) => {
  if (visible) {
    remoteSearchTables("");
  }
};
const handleChange = (isOld) => {
  const table = TablesByDataSource.value.find(
    (item) => item.tableName == form.value.tableName
  );
  if (table) {
    if (table.tableComment) {
      form.value.modelComment = table.tableComment;
    }
    form.value.modelName = table.tableName;
    form.value.modelNameSuffix = table.tableName;
    generateModelName();
  }
  tableData.value = [];

  getColumnByAssetIdList(isOld);
};
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue); // Use emit to trigger parent component updates
  },
});

const namingOptions = computed(() => {
  const options = {
    dataLayerList: dataLayerList.value,
    dataLayerId: form.value.dataLayerId,
    type: props.type,
    themeDomainList: themeDomainList.value,
    themeDomainId: form.value.themeDomainId,
    businessCategoryList: businessCategoryList.value,
    businessDomainId: form.value.businessDomainId,
    dataDomainList: dataDomainList.value,
    dataDomainId: form.value.dataDomainId,
    modelNameSuffix: form.value.modelName,
  };

  return [
    { label: formatModelName({ ...options, tableCase: 1 }), value: 1 },
    { label: formatModelName({ ...options, tableCase: 2 }), value: 2 },
  ];
});

const rules = computed(() => {
  const baseRules = {
    modelComment: [
      { required: true, message: td('dp.modelForm.modelNameRequired'), trigger: "blur" },
    ],
    modelName: [
      { required: true, message: td('dp.modelForm.modelNameEnRequired'), trigger: "blur" },
      {
        pattern: /^[A-Za-z0-9_]*$/,
        message: td('dp.modelForm.modelNamePattern'),
        trigger: "blur",
      },
    ],
    tableCase: [
      { required: true, message: td('dp.modelForm.tableCaseRequired'), trigger: "change" },
    ],
    createType: [
      { required: true, message: td('dp.modelForm.createTypeRequired'), trigger: "change" },
    ],
    catCode: [
      { required: true, message: td('dp.modelForm.catCodeRequired'), trigger: "change" },
    ],
    dataLayerId: [
      { required: true, message: td('dp.modelForm.dataLayerRequired'), trigger: "change" },
    ],
  };

  if (props.type === "4") {
    return {
      ...baseRules,
      themeDomainId: [
        { required: true, message: td('dp.modelForm.themeDomainRequired'), trigger: "change" },
      ],
    };
  } else {
    return {
      ...baseRules,
      businessDomainId: [
        { required: true, message: td('dp.modelForm.businessCategoryRequired'), trigger: "change" },
      ],
      dataDomainId: [
        { required: true, message: td('dp.modelForm.dataDomainRequired'), trigger: "change" },
      ],
    };
  }
});

const columns = ref([
  {
    prop: "dataElemName",
    label: td('dp.modelForm.relatedStandard'),
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "cnName",
    label: td('dp.modelForm.modelComment'),
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "engName",
    label: td('dp.modelForm.modelName'),
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "description",
    align: "left",
    label: td('common.texts.description'),
    align: "left",
    showOverflowTooltip: true,
    width: "250",
  },
  {
    prop: "columnType",
    label: td('dp.modelForm.dataType'),
    align: "center",
    width: "100",
    showOverflowTooltip: true,
  },
  { prop: "columnLength", label: td('dp.modelForm.attributeLength'), width: "80", align: "center" },
  { prop: "pkFlag", label: td('dp.modelForm.isPrimaryKey'), width: "80", align: "center" },
  { type: "button", label: td('common.texts.operation'), width: "150", align: "center" },
]);
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
function getDeptLabel(row) {
  // Recursively find matching nodes in a tree structure
  const findLabel = (tree) => {
    for (let node of tree) {
      if (node.id == row.authorityDept) {
        console.log("node", node);

        return node.label;
      }
      if (node.children) {
        const found = findLabel(node.children);
        if (found) return found;
      }
    }
    return null;
  };
  return findLabel(props.deptList) || "-";
}
//New table fields
function handleFormSubmit(formData) {
  console.log("Submitted form data:", formData);
  if (formData.index !== undefined && formData.index !== null) {
    // If an index exists, directly modify the data of the corresponding index.
    tableData.value[formData.index] = { ...formData };
    console.log("Data updated:", tableData.value[formData.index]);
  } else {
    // If there is no index, add new data
    tableData.value.push({ ...formData });
    console.log("New data added:", formData);
  }

  console.log("Current table data:", tableData.value);
}

function handleAdd() {
  selectedRow.value = {};
  addDialog.value = true;
  return;
  proxy.$refs["dpModelRef"].validate((valid) => {
    if (valid) {
      selectedRow.value = {};
      addDialog.value = true;
    } else {
      proxy.$message.warning("添加失败，基本信息填写完整后才能继续操作");
    }
  });
}

const editRow = (row, i) => {
  selectedRow.value = {};
  selectedRow.value = { ...row, index: i };
  addDialog.value = true;
};

const deleteRow = (row) => {
  const index = tableData.value.indexOf(row);
  if (index !== -1) {
    tableData.value.splice(index, 1);
  }
};

const reset = () => {
  isResetting.value = true;
  form.value = {
    id: undefined,
    modelName: "",
    modelNameSuffix: "",
    tableCase: 1,
    namingSpec: "",
    modelComment: "",
    catCode: props.catCode,
    createType: "1",
    contact: "",
    contactNumber: "",
    description: "",
    remark: "",
    status: "0",
    dataLayerId: null,
    businessDomainId: null,
    businessCategoryCode: "",
    dataDomainId: null,
    themeDomainId: null,
    themeDomainCode: "",
    tableType: props.type,
    datasourceId: "",
    datasourceType: "",
    datasourceName: "",
    datasourceConfig: "",
    ip: "",
    port: "",
    tableName: "",
    documentId: "",
    documentType: "",
  };
  tableData.value = [];
  TablesByDataSource.value = [];
  secondLevelDocs.value = [];
  if (proxy && proxy.$refs["dpModelRef"]) {
    proxy.$refs["dpModelRef"].resetFields();
  }
  setTimeout(() => {
    isResetting.value = false;
  }, 100);
};

const closeDialog = () => {
  emit("update:visible", false);
};

const confirmDialog = () => {
  if (!proxy || !proxy.$refs["dpModelRef"]) return;
  proxy.$refs["dpModelRef"].validate((valid) => {
    if (valid) {
      if (!tableData.value || tableData.value.length === 0) {
        proxy.$message.warning(td('dp.modelForm.addFieldWarning'));
        return;
      }

      // Make sure tableType and tableCase are in the correct format
      const { namingSpec, ...restForm } = form.value;
      const submitForm = {
        ...restForm,
        businessCategoryId: form.value.businessDomainId,
        documentId: form.value.documentId || null,
        tableType: form.value.tableType || props.type,
        tableCase: Number(form.value.tableCase),
      };

      if (!form.value.id) {
        emit("confirm", {
          form: submitForm,
          tableData: tableData.value,
        });
      } else {
        const updatedTableData = tableData.value.map((item) => ({
          ...item,
          modelId: form.value.id,
        }));
        emit("confirm", {
          form: submitForm,
          tableData: updatedTableData,
          modelId: form.value.id,
        });
      }
      closeDialog();
    }
  });
};
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

.dialog {
  min-height: 300px;
  max-height: 900px;
  overflow: auto;
}
</style>
