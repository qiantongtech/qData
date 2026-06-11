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
  <!-- // 数据库表 -->

  <el-form-item label="表名" prop="tableName">
    <el-input v-model="localForm.tableName" placeholder="表名" disabled />
  </el-form-item>
  <el-form-item label="注释" prop="tableComment">
    <el-input v-model="localForm.tableComment" placeholder="注释" disabled />
  </el-form-item>
  <el-form-item
      label="资产名称"
      prop="name"
      :rules="[{ required: true, message: '请输入资产名称', trigger: 'blur' }]"
  >
    <el-input v-model="localForm.name" placeholder="请输入资产名称" />
  </el-form-item>
  <el-form-item
      label="表类型"
      prop="tableType"
      :rules="[{ required: true, message: '请选择表类型', trigger: 'change' }]"
  >
    <el-select v-model="localForm.tableType" style="width: 100%">
      <el-option
          v-for="item in table_type"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      />
    </el-select>
  </el-form-item>
  <el-form-item
      label="数仓分层"
      prop="dataLayerId"
      :rules="[
      {
        required: true,
        message: '请选择数仓分层',
        trigger: 'change',
      },
    ]"
  >
    <el-tree-select
        v-model="localForm.dataLayerId"
        :data="dataLayerList"
        :loading="layerLoading"
        :props="{
        value: 'id',
        label: 'displayName',
        children: 'children',
      }"
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

  <template v-if="localForm.tableType != '4'">
    <el-form-item
        label="业务分类"
        prop="businessDomainId"
        :rules="[
        {
          required: true,
          message: '请选择业务分类',
          trigger: 'change',
        },
      ]"
    >
      <el-tree-select
          v-model="localForm.businessDomainId"
          :data="businessCategoryList"
          :loading="businessLoading"
          :props="{
          value: 'id',
          label: 'displayName',
          children: 'children',
        }"
          node-key="id"
          value-key="id"
          placeholder="请选择业务分类"
          check-strictly
          filterable
          clearable
          style="width: 100%"
      />
    </el-form-item>
    <el-form-item
        label="数据分域"
        prop="dataDomainId"
        :rules="[
        {
          required: true,
          message: '请选择数据分域',
          trigger: 'change',
        },
      ]"
    >
      <el-tree-select
          v-model="localForm.dataDomainId"
          :data="dataDomainList"
          :loading="domainLoading"
          :props="{
          value: 'id',
          label: 'displayName',
          children: 'children',
        }"
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
    <el-form-item
        label="所属主题"
        prop="themeDomainId"
        :rules="[
        {
          required: true,
          message: '请选择所属主题',
          trigger: 'change',
        },
      ]"
    >
      <el-tree-select
          v-model="localForm.themeDomainId"
          :data="themeDomainList"
          :loading="themeLoading"
          :props="{
          value: 'id',
          label: 'displayName',
          children: 'children',
        }"
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

  <el-form-item
      label="表命名规范"
      prop="tableCase"
      :rules="[
      {
        required: true,
        message: '请选择表命名规范',
        trigger: 'change',
      },
    ]"
  >
    <el-select
        v-model="localForm.tableCase"
        placeholder="请选择表命名规范"
        style="width: 100%"
    >
      <el-option
          v-for="dict in table_name_case"
          :key="dict.value"
          :label="dict.label"
          :value="Number(dict.value)"
      />
    </el-select>
  </el-form-item>
  <el-form-item
      label="表命名规范"
      prop="namingSpec"
      :rules="[
      {
        required: true,
        message: '表命名规范不能为空',
        trigger: 'change',
      },
    ]"
  >
    <el-input
        v-model="localForm.namingSpec"
        placeholder="系统生成的命名规范"
        disabled
    />
  </el-form-item>
  <qt-form-item
      label="数据连接名称"
      prop="datasourceId"
      :rules="[
      { required: true, message: '请选择数据连接名称', trigger: 'change' },
    ]"
      :tip="{ content: '选择该资产所依赖的数据连接，即数据源实例。' }"
  >
    <DatasourceList
        v-model="localForm.datasourceId"
        placeholder="请选择数据连接名称"
        @change="handleDatasourceChange"
        filterable
        :disabled="
        !props.isRegister && localForm.id && localForm.createType == '2'
      "
        flag="daAsset"
        :project="props.type == '1' ? true : false"
    />
  </qt-form-item>

  <el-form-item label="数据连接类型" prop="datasourceType">
    <el-input
        v-model="localForm.datasourceType"
        disabled
        placeholder="请选择数据连接类型"
    />
  </el-form-item>

  <el-form-item
      label="选择表"
      prop="tableName"
      :rules="[{ required: true, message: '请选择表', trigger: 'change' }]"
  >
    <el-select
        v-model="localForm.tableName"
        filterable
        remote
        :remote-method="remoteSearchTables"
        @visible-change="handleTableSelectVisible"
        placeholder="请选择表"
        @change="handleTableChange"
        :loading="loadingList"
        :disabled="
        !props.isRegister && localForm.id && localForm.createType == '2'
      "
    >
      <el-option
          v-for="item in tablesByDataSource"
          :key="item.tableName"
          :label="item.tableName"
          :value="item.tableName"
      />
    </el-select>
  </el-form-item>
</template>

<script setup>
import { ref, watch, getCurrentInstance, onMounted } from "vue";
import { ElMessage } from "element-plus";
import useUserStore from "@/store/system/user.js";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
  getDaDatasourceList,
} from "@/api/dpp/task/index.js";
import { dppNoPageList } from "@/api/da/asset/asset.js";
import {
  getDaDatasource,
  listDaDatasourceNoKafkaByProjectCode,
} from "@/api/da/dataSource/dataSource.js";
import DatasourceList from "@/components/Datasource/List.vue";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import { listBusinessCategory } from "@/api/dm/businessCategory/businessCategory";
import { listDataDomain } from "@/api/dm/dataDomain/dataDomain";
import { listThemeDomain } from "@/api/dm/themeDomain/themeDomain";
import { formatModelName, findInTree } from "@/utils/dm/utils";

const props = defineProps({
  form: Object,
  isRegister: Boolean,
  type: String,
});
const emit = defineEmits(["update:form"]);

const { proxy } = getCurrentInstance();
const { dpp_connection, table_type, table_name_case } = proxy.useDict(
    "dpp_connection",
    "table_type",
    "table_name_case"
);

const userStore = useUserStore();
const createTypeList = ref([]); // 数据源列表
let loading = ref(false);
const loadingList = ref(false);
const dppAssetList = ref([]);
const columnsByAssetTable = ref([]);
const tablesByDataSource = ref([]);

let layerLoading = ref(false);
let businessLoading = ref(false);
let domainLoading = ref(false);
let themeLoading = ref(false);
let dataLayerList = ref([]);
let businessCategoryList = ref([]);
let dataDomainList = ref([]);
let themeDomainList = ref([]);
const isInitializing = ref(false);
const isResetting = ref(false);

const localForm = ref({
  ...props.form,
  datasourceId: props.form.datasourceId == -1 ? null : props.form.datasourceId,
  tableName: props.form.tableName == -1 ? null : props.form.tableName,
});

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
  const targetType = currentType || localForm.value.tableType;
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

const generateModelName = (initialData = null) => {
  if (isResetting.value) return;

  const options = {
    dataLayerList: dataLayerList.value,
    dataLayerId: localForm.value.dataLayerId,
    tableType: localForm.value.tableType,
    themeDomainList: themeDomainList.value,
    themeDomainId: localForm.value.themeDomainId,
    businessCategoryList: businessCategoryList.value,
    businessDomainId: localForm.value.businessDomainId,
    dataDomainList: dataDomainList.value,
    dataDomainId: localForm.value.dataDomainId,
    modelName: localForm.value.tableName,
    tableCase: localForm.value.tableCase,
  };

  if (initialData) {
    // 合并初始数据中的属性到 options 中，以防列表还没加载完
    Object.assign(options, initialData);
  } else {
    if (isInitializing.value) return;
  }

  localForm.value.namingSpec = formatModelName(options);

  // 同步业务/主题代码
  if (localForm.value.tableType === "4") {
    const theme = findInTree(
        themeDomainList.value,
        localForm.value.themeDomainId
    );
    if (theme) localForm.value.themeDomainCode = theme.code;
  } else {
    const biz = findInTree(
        businessCategoryList.value,
        localForm.value.businessDomainId
    );
    if (biz) localForm.value.businessCategoryCode = biz.code;
  }
  emit("update:form", localForm.value);
};

// 通用数据获取函数
const fetchData = async (requestFn, params, loadingState) => {
  try {
    loadingState.value = true;
    const response = await requestFn(params);
    return response?.data || [];
  } finally {
    loadingState.value = false;
  }
};

const remoteSearchTables = async (query) => {
  const dsId = localForm.value?.datasourceId;
  if (!dsId) {
    tablesByDataSource.value = [];
    return;
  }
  tablesByDataSource.value = await fetchData(
      getTablesByDataSourceId,
      { datasourceId: dsId, tableName: query },
      loadingList
  );
};

const handleTableSelectVisible = (visible) => {
  if (visible) {
    remoteSearchTables("");
  }
};

// 数据源变化时
const handleDatasourceChange = async (id, selected) => {
  if (!selected) return;
  const { datasourceType, datasourceName, datasourceConfig, ip, port } =
      selected;
  const config = JSON.parse(datasourceConfig);

  Object.assign(localForm.value, {
    datasourceType,
    datasourceIp: ip,
    datasourceName,
    dbname: config.dbname,
    datasourceId: id,
  });
  localForm.value.tableName = "";
  emit("update:form", localForm.value);

  await remoteSearchTables("");
  columnsByAssetTable.value = [];
};

// 表变化时
const handleTableChange = (tableName) => {
  const selected = tablesByDataSource.value.find(
      (item) => item.tableName == tableName
  );
  if (!selected) return;
  localForm.value.tableName = tableName;
  localForm.value.tableComment = selected.tableComment;
  if (!localForm.value.name) {
    localForm.value.name = selected.tableComment || tableName;
  }
  emit("update:form", localForm.value);
  columnsByAssetTable.value = [];
};

watch(
    () => localForm.value.businessDomainId,
    (newVal) => {
      if (newVal) {
        fetchDataDomain(newVal);
      } else {
        dataDomainList.value = [];
      }
      if (!isInitializing.value) {
        localForm.value.dataDomainId = null;
      }
      emit("update:form", localForm.value);
    }
);

watch(
    () => localForm.value.tableType,
    (newVal) => {
      fetchAllOptions(newVal);
      if (!isInitializing.value) {
        localForm.value.dataLayerId = null;
        localForm.value.businessDomainId = null;
        localForm.value.dataDomainId = null;
        localForm.value.themeDomainId = null;
      }
      emit("update:form", localForm.value);
    }
);

watch(
    [
      () => localForm.value.dataLayerId,
      () => localForm.value.businessDomainId,
      () => localForm.value.dataDomainId,
      () => localForm.value.themeDomainId,
      () => localForm.value.tableName,
      () => localForm.value.tableCase,
      () => localForm.value.tableType,
      () => dataLayerList.value,
      () => businessCategoryList.value,
      () => dataDomainList.value,
      () => themeDomainList.value,
    ],
    () => {
      generateModelName();
    }
);

watchEffect(() => {
  localForm.value = {
    ...props.form,
    datasourceId:
        props.form.datasourceId == -1 ? null : props.form.datasourceId,
    tableName: props.form.tableName == -1 ? null : props.form.tableName,
  };
});

onMounted(async () => {
  isInitializing.value = true;
  await fetchAllOptions();
  if (localForm.value.businessDomainId) {
    await fetchDataDomain(localForm.value.businessDomainId);
  }
  isInitializing.value = false;
  generateModelName(localForm.value.id ? localForm.value : null);
});
</script>
