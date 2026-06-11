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
    >
      <div class="h2-title row-full">基础信息</div>
      <qt-form-item
        v-if="!form.id"
        label="创建方式"
        prop="createType"
        :tip="{
          content: `手工录入：手动添加标准数据元，逐步构建一个完整的逻辑数据模型。<br/>发布表生成：系统自动扫描已存在的物理数据库表（如 MySQL、Oracle 中的表），提取其结构信息（列名、类型、主键等），并反向生成对应的逻辑模型。`,
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
      <el-form-item v-else label="表类型" prop="tableType">
        <el-select v-model="form.tableType" disabled style="width: 100%">
          <el-option
            v-for="item in table_type"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数仓分层" prop="dataLayerId">
        <el-tree-select
          v-model="form.dataLayerId"
          :data="dataLayerList"
          :loading="layerLoading"
          :props="{ value: 'id', label: 'displayName', children: 'children' }"
          node-key="id"
          value-key="id"
          placeholder="请选择数仓分层"
          check-strictly
          filterable
          default-expand-all
          clearable
          style="width: 100%"
        />
      </el-form-item>

      <template v-if="form.tableType != '4'">
        <el-form-item label="业务分类" prop="businessDomainId">
          <el-tree-select
            v-model="form.businessDomainId"
            :data="businessCategoryList"
            :loading="businessLoading"
            :props="{ value: 'id', label: 'displayName', children: 'children' }"
            node-key="id"
            value-key="id"
            placeholder="请选择业务分类"
            check-strictly
            filterable
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="数据分域" prop="dataDomainId">
          <el-tree-select
            v-model="form.dataDomainId"
            :data="dataDomainList"
            :loading="domainLoading"
            :props="{ value: 'id', label: 'displayName', children: 'children' }"
            node-key="id"
            value-key="id"
            placeholder="请选择数据分域"
            check-strictly
            filterable
            clearable
            style="width: 100%"
          />
        </el-form-item>
      </template>

      <template v-else>
        <el-form-item label="所属主题" prop="themeDomainId">
          <el-tree-select
            v-model="form.themeDomainId"
            :data="themeDomainList"
            :loading="themeLoading"
            :props="{ value: 'id', label: 'displayName', children: 'children' }"
            node-key="id"
            value-key="id"
            placeholder="请选择所属主题"
            check-strictly
            filterable
            clearable
            style="width: 100%"
          />
        </el-form-item>
      </template>
      <el-form-item label="表英文名称" prop="modelName">
        <el-input
          v-model="form.modelName"
          placeholder="请输入表英文名称"
          @input="form.modelName = form.modelName.replace(/[^A-Za-z0-9_]/g, '')"
        />
      </el-form-item>
      <el-form-item label="表中文名称" prop="modelComment">
        <el-input v-model="form.modelComment" placeholder="请输入表中文名称" />
      </el-form-item>
      <el-form-item label="完整表名" prop="tableCase">
        <el-select
          v-model="form.tableCase"
          placeholder="请选择完整表名"
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
        <el-form-item label="标准类型" prop="description">
          <el-select
            class="el-form-input-width"
            v-model="form.documentType"
            placeholder="请选择标准类型"
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
        <el-form-item label="标准登记" prop="documentId">
          <el-select
            class="el-form-input-width"
            v-model="form.documentId"
            placeholder="请选择标准登记"
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
      <el-form-item label="责任人" prop="contact">
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
          placeholder="请选择责任人"
          check-strictly
          @change="handleContactChange"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactNumber">
        <el-input
          v-model="form.contactNumber"
          placeholder="请输入联系电话"
          disabled
        />
      </el-form-item>

      <qt-form-item
        :label="t('common.texts.status')"
        prop="status"
        :tip="{
          content:
            '启用状态表示该逻辑模型可用于后续开发、关联标准数据元或生成物理表；禁用后无法被引用。',
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
      <el-form-item :label="t('common.texts.description')" prop="description" class="row-full">
        <el-input
          v-model="form.description"
          type="textarea"
          maxlength="500个字符"
          show-word-limit
          :placeholder="t('common.form.descriptionPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="t('common.texts.remark')" prop="remark" class="row-full">
        <el-input
          v-model="form.remark"
          type="textarea"
          maxlength="500个字符"
          show-word-limit
          :placeholder="t('common.form.remarkPlaceholder')"
        />
      </el-form-item>

      <template v-if="form.createType == 2 && !form.id">
        <div class="h2-title row-full">数据源</div>
        <el-form-item
          label="数据连接名称"
          prop="datasourceId"
          :rules="[
            {
              required: true,
              message: '请选择数据连接名称',
              trigger: 'change',
            },
          ]"
        >
          <DatasourceList
            v-model="form.datasourceId"
            placeholder="请选择数据连接名称"
            @change="handleDatasourceChange"
            filterable
            flag="dpModel"
          />
        </el-form-item>
        <el-form-item label="数据库类型" prop="datasourceType">
          <el-input
            v-model="form.datasourceType"
            placeholder="请选择数据库类型"
            disabled
          />
        </el-form-item>
        <el-form-item label="数据库地址" prop="ip">
          <el-input v-model="form.ip" placeholder="请选择数据库类型" disabled />
        </el-form-item>
        <el-form-item
          label="选择表"
          prop="tableName"
          :rules="[
            { required: true, message: '表不能为空', trigger: 'change' },
          ]"
        >
          <el-select
            v-model="form.tableName"
            placeholder="请选择表"
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

    <div class="h2-title">属性字段</div>
    <el-button
      style="margin-bottom: 5px; margin-top: 10px"
      type="primary"
      plain
      @click="handleAdd"
      size="small"
      @mousedown="(e) => e.preventDefault()"
    >
      <i class="iconfont-mini icon-xinzeng mr5"></i>{{ t('common.button.add') }}
    </el-button>
    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column :label="t('common.texts.number')" type="index" align="left" width="60" />
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
            >编辑</el-button
          >
          <el-button link type="danger" icon="Delete" @click="deleteRow(row)"
            >{{ t('common.button.delete') }}</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">{{ t('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="confirmDialog">确认</el-button>
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
import { useI18n } from 'vue-i18n'

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

const { t } = useI18n();
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

// --- 响应式变量声明 (提前至顶部) ---
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
    newItem.id = Number(item.id); // 强制转换为数字以匹配回显
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
  // 数仓分层
  layerLoading.value = true;
  const p1 = treeDataLayer()
    .then((res) => {
      const tree = res.data || res.rows || [];
      const processTree = (list) => {
        return list.map((item) => {
          const newItem = { ...item };
          newItem.id = Number(item.id); // 强制转换为数字以匹配回显
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
    // 主题域
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
    // 业务分类
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
    // 修改模式下的第一次回显，直接使用 formatModelName 传参并拼接表英文名
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

  // 同步业务/主题代码
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
      // 修改模式下，优先使用 props.dataList.tableType
      const currentType =
        props.dataList && props.dataList.tableType
          ? String(props.dataList.tableType)
          : props.type;
      // 并行请求，不使用 await 阻塞
      fetchAllOptions(currentType);

      if (props.dataList && props.dataList.id) {
        const echoData = { ...props.dataList };
        // 映射业务分类 ID，处理详情接口返回的 businessCategoryId
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

        // 初始化命名大小写模式
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

        // 获取列信息
        getDpModelColumnList({ modelId: form.value.id }).then((res) => {
          tableData.value = res.data || [];
        });
      } else {
        reset();
      }

      // 延迟结束初始化状态，确保 echo 赋值引起的 watch 不会触发 generateModelName
      setTimeout(() => {
        isInitializing.value = false;
        // 初始化完成后手动触发一次名称生成，确保回显时 namingSpec 正确
        // 修改模式下传入 props.dataList 使用 formatHierarchyName 传参回显
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
    console.error("请求失败:", error);
  }
};
// 表
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
    const response = await getDpModelColumnList({ modelId: form.value.id }); // 传递 `form` 数据
    tableData.value = response.data;
    loading.value = false;
    // 处理返回的数据
  } catch (error) {
    console.error("请求失败:", error);
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
    emit("update:visible", newValue); // 使用 emit 触发父组件更新
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
      { required: true, message: "表中文名称不能为空", trigger: "blur" },
    ],
    modelName: [
      { required: true, message: "表英文名称不能为空", trigger: "blur" },
      {
        pattern: /^[A-Za-z0-9_]*$/,
        message: "表名只能包含字母、数字和下划线",
        trigger: "blur",
      },
    ],
    tableCase: [
      { required: true, message: "完整表名不能为空", trigger: "change" },
    ],
    createType: [
      { required: true, message: "创建方式不能为空", trigger: "change" },
    ],
    catCode: [
      { required: true, message: "逻辑模型类目不能为空", trigger: "change" },
    ],
    dataLayerId: [
      { required: true, message: "数仓分层不能为空", trigger: "change" },
    ],
  };

  if (props.type === "4") {
    return {
      ...baseRules,
      themeDomainId: [
        { required: true, message: "主题域不能为空", trigger: "change" },
      ],
    };
  } else {
    return {
      ...baseRules,
      businessDomainId: [
        { required: true, message: "业务分类不能为空", trigger: "change" },
      ],
      dataDomainId: [
        { required: true, message: "数据分域不能为空", trigger: "change" },
      ],
    };
  }
});

const columns = ref([
  {
    prop: "dataElemName",
    label: "关联标准",
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "cnName",
    label: "表中文名称",
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "engName",
    label: "表英文名称",
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "description",
    align: "left",
    label: t('common.texts.description'),
    align: "left",
    showOverflowTooltip: true,
    width: "250",
  },
  {
    prop: "columnType",
    label: "数据类型",
    align: "center",
    width: "100",
    showOverflowTooltip: true,
  },
  { prop: "columnLength", label: "属性长度", width: "80", align: "center" },
  { prop: "pkFlag", label: "是否主键", width: "80", align: "center" },
  { type: "button", label: t('common.texts.operation'), width: "150", align: "center" },
]);
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
function getDeptLabel(row) {
  // 递归查找树形结构中匹配的节点
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
//表字段的新增
function handleFormSubmit(formData) {
  console.log("提交的表单数据:", formData);
  if (formData.index !== undefined && formData.index !== null) {
    // 如果存在 index，则直接修改对应索引的数据
    tableData.value[formData.index] = { ...formData };
    console.log("数据已修改:", tableData.value[formData.index]);
  } else {
    // 如果没有 index，则新增数据
    tableData.value.push({ ...formData });
    console.log("新数据已新增:", formData);
  }

  console.log("当前表格数据:", tableData.value);
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
        proxy.$message.warning("操作失败，请添加属性字段");
        return;
      }

      // 确保 tableType 和 tableCase 格式正确
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
