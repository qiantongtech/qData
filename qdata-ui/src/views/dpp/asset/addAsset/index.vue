<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top-wrap flex-items-center gap20">
      <el-form :inline="true" label-width="75px" @submit.prevent>
        <el-form-item :label="td('dpp.asset.addAsset.assetType')" style="margin-bottom: 0">
          <el-select
            v-model="baseState.assetType"
            :placeholder="td('dpp.asset.addAsset.assetTypePlaceholder')"
            style="width: 300px"
          >
            <el-option
              v-for="dict in da_asset_type.filter((item) => item.value == '1')"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="pagecont-top">
      <div class="infotop">
        <div class="main">
          <div class="flex-between-center mb10">
            <div class="flex-items-center title-wrap">
              <div class="h2-titles">{{ td('dpp.asset.addAsset.assetInfoComplete') }}</div>
              <div class="asset-hint" style="margin-left: 10px">
                {{ td('dpp.asset.addAsset.assetInfoHint') }}
              </div>
            </div>
            <div class="flex-items-center">
              <el-button type="primary" @click="handleOpenMetadataSelect()"
                >{{ td('dpp.asset.addAsset.selectMetadata') }}</el-button
              >
              <el-button
                @click="handleOpenBatchSetting"
                :disabled="!selectedRows.length"
                >{{ td('dpp.asset.addAsset.batchSetting') }}</el-button
              >
            </div>
          </div>

          <el-form :model="tableForm" ref="tableFormRef">
            <qt-table
              ref="tableRef"
              :columns="tableColumns"
              :func="listLocalData"
              :config="tableConfig"
            >
              <template #name="{ row, $index }">
                <el-form-item
                  :prop="'rows.' + $index + '.name'"
                  :rules="[
                    {
                      required: true,
                      message: td('dpp.asset.addAsset.assetNameRequired'),
                      trigger: 'blur',
                    },
                  ]"
                  label-width="0"
                  style="margin-bottom: 0"
                >
                  <el-input v-model="row.name" :placeholder="td('dpp.asset.addAsset.assetNamePlaceholder')" />
                </el-form-item>
              </template>
              <template #tableType="{ row, $index }">
                <el-form-item
                  :prop="'rows.' + $index + '.tableType'"
                  :rules="[
                    {
                      required: true,
                      message: td('dpp.asset.addAsset.tableTypeRequired'),
                      trigger: 'change',
                    },
                  ]"
                  label-width="0"
                  style="margin-bottom: 0"
                >
                  <el-select
                    v-model="row.tableType"
                    :placeholder="td('dpp.asset.addAsset.tableTypePlaceholder')"
                    @change="handleTableTypeChange(row)"
                  >
                    <el-option
                      v-for="item in table_type"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </template>
              <template #dataLayerId="{ row, $index }">
                <el-form-item
                  :prop="'rows.' + $index + '.dataLayerId'"
                  :rules="[
                    {
                      required: true,
                      message: td('dpp.asset.addAsset.dataLayerRequired'),
                      trigger: 'change',
                    },
                  ]"
                  label-width="0"
                  style="margin-bottom: 0"
                >
                  <el-tree-select
                    v-model="row.dataLayerId"
                    :data="options.dataLayerList"
                    :props="{
                      value: 'id',
                      label: 'displayName',
                      children: 'children',
                    }"
                    node-key="id"
                    value-key="id"
                    :placeholder="td('dpp.asset.addAsset.dataLayerPlaceholder')"
                    check-strictly
                    filterable
                    clearable
                    default-expand-all
                  />
                </el-form-item>
              </template>
              <template #businessCategoryId="{ row, $index }">
                <template v-if="!row.tableType || row.tableType != '4'">
                  <el-form-item
                    :prop="'rows.' + $index + '.businessCategoryId'"
                    :rules="[
                      {
                        required: true,
                        message: td('dpp.asset.addAsset.businessCategoryRequired'),
                        trigger: 'change',
                      },
                    ]"
                    label-width="0"
                    style="margin-bottom: 0"
                  >
                    <el-tree-select
                      v-model="row.businessCategoryId"
                      :data="options.businessCategoryList"
                      :props="{
                        value: 'id',
                        label: 'displayName',
                        children: 'children',
                      }"
                      node-key="id"
                      value-key="id"
                      :placeholder="td('dpp.asset.addAsset.businessCategoryPlaceholder')"
                      check-strictly
                      filterable
                      clearable
                      default-expand-all
                      @change="(val) => handleBusinessDomainChange(val, row)"
                    />
                  </el-form-item>
                </template>
                <span v-else>-</span>
              </template>
              <template #themeDomainId="{ row, $index }">
                <template v-if="!row.tableType || row.tableType == '4'">
                  <el-form-item
                    :prop="'rows.' + $index + '.themeDomainId'"
                    :rules="[
                      {
                        required: true,
                        message: td('dpp.asset.addAsset.themeDomainRequired'),
                        trigger: 'change',
                      },
                    ]"
                    label-width="0"
                    style="margin-bottom: 0"
                  >
                    <el-tree-select
                      v-model="row.themeDomainId"
                      :data="options.themeDomainList"
                      :props="{
                        value: 'id',
                        label: 'displayName',
                        children: 'children',
                      }"
                      node-key="id"
                      value-key="id"
                      :placeholder="td('dpp.asset.addAsset.themeDomainPlaceholder')"
                      check-strictly
                      filterable
                      clearable
                      default-expand-all
                      @change="(val) => handleThemeDomainChange(val, row)"
                    />
                  </el-form-item>
                </template>
                <span v-else>-</span>
              </template>
              <template #dataDomainId="{ row, $index }">
                <template v-if="!row.tableType || row.tableType != '4'">
                  <el-form-item
                    :prop="'rows.' + $index + '.dataDomainId'"
                    :rules="[
                      {
                        required: true,
                        message: td('dpp.asset.addAsset.dataDomainRequired'),
                        trigger: 'change',
                      },
                    ]"
                    label-width="0"
                    style="margin-bottom: 0"
                  >
                    <el-tree-select
                      v-model="row.dataDomainId"
                      :data="rowUiContext[row.tableId]?.dataDomainList || []"
                      :props="{
                        value: 'id',
                        label: 'displayName',
                        children: 'children',
                      }"
                      node-key="id"
                      value-key="id"
                      :placeholder="td('dpp.asset.addAsset.dataDomainPlaceholder')"
                      check-strictly
                      filterable
                      clearable
                      default-expand-all
                    />
                  </el-form-item>
                </template>
                <span v-else>-</span>
              </template>
              <template #tableCase="{ row, $index }">
                <el-form-item
                  :prop="'rows.' + $index + '.tableCase'"
                  :rules="[
                    {
                      required: true,
                      message: td('dpp.asset.addAsset.tableCaseRequired'),
                      trigger: 'change',
                    },
                  ]"
                  label-width="0"
                  style="margin-bottom: 0"
                >
                  <el-select v-model="row.tableCase" :placeholder="td('dpp.asset.addAsset.tableCasePlaceholder')">
                    <el-option
                      v-for="item in table_name_case"
                      :key="item.value"
                      :label="item.label"
                      :value="Number(item.value)"
                    />
                  </el-select>
                </el-form-item>
              </template>
              <template #action="{ row }">
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  @click="handleDeleteRow(row)"
                ></el-button>
              </template>
            </qt-table>
          </el-form>
        </div>
        <div class="button-style">
          <el-button @click="goBack" class="action-btn">{{ td('dpp.asset.addAsset.backToList') }}</el-button>
          <el-button
            type="primary"
            :disabled="!baseState.rows.length"
            :loading="uiState.publishLoading"
            @click="confirmPublish"
            class="action-btn"
            >{{ td('dpp.asset.addAsset.registerAndExit') }}</el-button
          >
        </div>
      </div>
    </div>
    <!-- 元数据选择弹窗 -->
    <MetadataSelectModal
      ref="metadataSelectRef"
      :hideTableIds="hideTableIds"
      @confirm="handleMetadataConfirm"
    />
    <!-- 批量设置弹窗 -->
    <el-dialog
      :title="td('dpp.asset.addAsset.batchSetting')"
      v-model="uiState.batchVisible"
      width="600px"
      append-to-body
      draggable
      :append-to="$refs['app-container']"
    >
      <el-form :model="batchForm" label-width="110px">
        <div class="hint-div ml0">
          <el-icon color="#2A7BFD" size="16px">
            <InfoFilled />
          </el-icon>
          <span>
            {{ td('dpp.asset.addAsset.batchSettingHint') }}
          </span>
        </div>
        <el-form-item :label="td('dpp.asset.addAsset.tableType')">
          <el-select
            v-model="batchForm.tableType"
            placeholder="{{ td('dpp.asset.addAsset.noChange') }}"
            class="full-width"
            @change="handleBatchTableTypeChange"
            clearable
          >
            <el-option label="{{ td('dpp.asset.addAsset.noChange') }}" :value="-1" />
            <el-option
              v-for="item in table_type"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="td('dpp.asset.addAsset.dataLayer')">
          <el-tree-select
            v-model="batchForm.dataLayerId"
            :data="[
            { id: -1, displayName: td('dpp.asset.addAsset.noChange') },
              ...options.dataLayerList,
            ]"
            :props="{
              value: 'id',
              label: 'displayName',
              children: 'children'
            }"
            node-key="id"
            value-key="id"
            :placeholder="td('dpp.asset.addAsset.noChange')"
            check-strictly
            filterable
            clearable
            default-expand-all
            class="full-width"
          />
        </el-form-item>
        <el-form-item
          v-if="
            !batchForm.tableType ||
            batchForm.tableType == -1 ||
            batchForm.tableType != '4'
          "
          :label="td('dpp.asset.addAsset.businessCategory')"
        >
          <el-tree-select
            v-model="batchForm.businessCategoryId"
            :data="[
              { id: -1, displayName: td('dpp.asset.addAsset.noChange') },
              ...options.businessCategoryList,
            ]"
            :props="{
              value: 'id',
              label: 'displayName',
              children: 'children'
            }"
            node-key="id"
            value-key="id"
            placeholder="{{ td('dpp.asset.addAsset.noChange') }}"
            check-strictly
            filterable
            clearable
            default-expand-all
            class="full-width"
            @change="handleBatchBusinessChange"
          />
        </el-form-item>
        <el-form-item
          v-if="
            !batchForm.tableType ||
            batchForm.tableType == -1 ||
            batchForm.tableType != '4'
          "
          :label="td('dpp.asset.addAsset.dataDomain')"
        >
          <el-tree-select
            v-model="batchForm.dataDomainId"
            :data="[
              { id: -1, displayName: td('dpp.asset.addAsset.noChange') },
              ...batchDataDomainList,
            ]"
            :loading="batchDomainLoading"
            :props="{
              value: 'id',
              label: 'displayName',
              children: 'children'
            }"
            node-key="id"
            value-key="id"
            placeholder="{{ td('dpp.asset.addAsset.noChange') }}"
            check-strictly
            filterable
            clearable
            default-expand-all
            class="full-width"
          />
        </el-form-item>
        <el-form-item
          v-if="
            !batchForm.tableType ||
            batchForm.tableType == -1 ||
            batchForm.tableType == '4'
          "
          :label="td('dpp.asset.addAsset.themeDomain')"
        >
          <el-tree-select
            v-model="batchForm.themeDomainId"
            :data="[
              { id: -1, displayName: td('dpp.asset.addAsset.noChange') },
              ...options.themeDomainList,
            ]"
            :props="{
              value: 'id',
              label: 'displayName',
              children: 'children'
            }"
            node-key="id"
            value-key="id"
            placeholder="{{ td('dpp.asset.addAsset.noChange') }}"
            check-strictly
            filterable
            clearable
            default-expand-all
            class="full-width"
          />
        </el-form-item>
        <el-form-item :label="td('dpp.asset.addAsset.tableCase')">
          <el-select
            v-model="batchForm.tableCase"
            placeholder="{{ td('dpp.asset.addAsset.noChange') }}"
            class="full-width"
            clearable
          >
            <el-option label="{{ td('dpp.asset.addAsset.noChange') }}" :value="-1" />
            <el-option
              v-for="item in table_name_case"
              :key="item.value"
              :label="item.label"
              :value="Number(item.value)"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="uiState.batchVisible = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="confirmBatchSetting"
            >{{ td('common.button.confirm') }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DaBatchPublish">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  computed,
  getCurrentInstance,
  ref,
  onMounted,
  reactive,
  watch,
  nextTick,
} from "vue";
import { useRouter } from "vue-router";
import { WarningFilled, InfoFilled } from "@element-plus/icons-vue";
import MetadataSelectModal from "@/views/dpp/components/MetadataSelectModal.vue";
import { ElMessage } from "element-plus";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import { listBusinessCategory } from "@/api/dm/businessCategory/businessCategory";
import { listDataDomain } from "@/api/dm/dataDomain/dataDomain";
import { listThemeDomain } from "@/api/dm/themeDomain/themeDomain";
import { addDaAsset } from "@/api/da/asset/asset.js";
import { usePageRefresh } from "@/composables/usePageRefresh";

const { td } = useDefaultLang();// --- 基础配置与工具 ---
const { proxy } = getCurrentInstance();
const router = useRouter();
const { setRefreshNeeded } = usePageRefresh("da_asset");
const { table_type, table_name_case, da_asset_type } = proxy.useDict(
  "table_type",
  "table_name_case",
  "da_asset_type"
);

// 1. 基础状态
const baseState = reactive({
  rows: [], // 资产列表数据
  assetType: "1", // 资产类型
});

// 2. UI与弹窗状态
const uiState = reactive({
  publishLoading: false, // 注册按钮 loading
  batchVisible: false, // 批量设置弹窗可见性
  activeTableId: null,
});

// 3. 引用定义
const tableFormRef = ref(null);
const tableRef = ref(null);
const metadataSelectRef = ref(null); // 元数据选择弹窗引用

// 4. 下拉选项数据
const options = reactive({
  dataLayerList: [],
  businessCategoryList: [],
  themeDomainList: [],
  loading: {
    layer: false,
    business: false,
    theme: false,
  },
});

// 5. 批量设置表单
const batchForm = reactive({
  tableType: -1,
  dataLayerId: -1,
  tableCase: -1,
  businessCategoryId: -1,
  dataDomainId: -1,
  themeDomainId: -1,
});

const rowUiContext = reactive({});

const batchDataDomainList = ref([]);
const batchDomainLoading = ref(false);
const selectedRows = ref([]);

// --- 计算属性 ---

// 表格表单数据包装
const tableForm = reactive({
  rows: computed(() => baseState.rows),
});

const hideTableIds = computed(() => {
  return baseState.rows.map((row) => row.tableId).join(",");
});

// --- 表格配置 ---

const tableColumns = [
  { type: "selection", width: 55 },
  { type: "index", label: td('common.texts.number'), width: 60 },
  {
    label: td('dpp.asset.addAsset.sourceTable'),
    width: 200,
    align: "left",
    list: [
      { prop: "tableName" },
      { prop: "tableComment", class: "color999 fz12" },
    ],
  },
  { label: td('dpp.asset.addAsset.assetName'), width: 200, slot: "name" },
  { label: td('dpp.asset.addAsset.tableType'), width: 140, slot: "tableType" },
  { label: td('dpp.asset.addAsset.dataLayer'), width: 180, slot: "dataLayerId" },
  { label: td('dpp.asset.addAsset.businessCategory'), slot: "businessCategoryId" },
  { label: td('dpp.asset.addAsset.dataDomain'), slot: "dataDomainId" },
  { label: td('dpp.asset.addAsset.themeDomain'), slot: "themeDomainId" },
  { label: td('dpp.asset.addAsset.tableCase'), width: 140, slot: "tableCase" },
  { label: td('common.texts.operation'), width: 100, slot: "action" },
];

const tableConfig = reactive({
  notPagination: true,
  table: {
    stripe: true,
    height: "calc(100vh - 290px)",
    onSelectionChange: (selection) => handleSelectionChange(selection),
  },
});

/**
 * 格式化树形数据，添加 displayName
 */
const formatTreeData = (list) => {
  return list.map((item) => {
    const newItem = { ...item };
    newItem.id = Number(item.id);
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

/**
 * 获取所有基础下拉选项
 */
const fetchAllOptions = () => {
  // 1. 获取数仓分层
  options.loading.layer = true;
  treeDataLayer({ validFlag: true })
    .then((res) => {
      const tree = res.data || res.rows || [];
      const processTree = (list) => {
        return list.map((item) => {
          const newItem = { ...item };
          newItem.id = Number(item.id);
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
      options.dataLayerList = processTree(tree);
    })
    .finally(() => {
      options.loading.layer = false;
    });

  // 2. 获取业务分类
  options.loading.business = true;
  listBusinessCategory({
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
      options.businessCategoryList = formatTreeData(tree);
    })
    .finally(() => {
      options.loading.business = false;
    });

  // 3. 获取主题域
  options.loading.theme = true;
  listThemeDomain({ pageNum: 1, pageSize: 1000, validFlag: true })
    .then((res) => {
      const tree = proxy.handleTree(
        res.data?.rows || res.data || res.rows || [],
        "id",
        "parentId"
      );
      options.themeDomainList = formatTreeData(tree);
    })
    .finally(() => {
      options.loading.theme = false;
    });
};

/**
 * 根据业务分类获取数据分域
 */
const fetchDataDomain = async (row) => {
  if (!row.businessCategoryId) {
    if (rowUiContext[row.tableId]) {
      rowUiContext[row.tableId].dataDomainList = [];
    }
    return;
  }

  if (!rowUiContext[row.tableId]) {
    rowUiContext[row.tableId] = { dataDomainList: [] };
  }

  try {
    const res = await listDataDomain({
      pageNum: 1,
      pageSize: 1000,
      orderByColumn: "create_time",
      isAsc: "descending",
      businessCategoryId: row.businessCategoryId,
      validFlag: true,
    });
    const tree = proxy.handleTree(
      res.data?.rows || res.data || res.rows || [],
      "id",
      "parentId"
    );
    rowUiContext[row.tableId].dataDomainList = formatTreeData(tree);
  } catch (error) {
    console.error("获取数据分域失败:", error);
  }
};

/**
 * 列表数据源方法
 */
const listLocalData = () => {
  return Promise.resolve({
    data: baseState.rows,
    total: baseState.rows.length,
  });
};

/**
 * 表类型切换处理
 */
const handleTableTypeChange = (row) => {
  row.businessCategoryId = null;
  row.dataDomainId = null;
  row.themeDomainId = null;
  row.businessCategoryCode = "";
  row.themeDomainCode = "";
  if (rowUiContext[row.tableId]) {
    rowUiContext[row.tableId].dataDomainList = [];
  }
};

/**
 * 业务分类切换处理
 */
const handleBusinessDomainChange = (val, row) => {
  row.dataDomainId = null;
  row.businessCategoryCode = "";
  if (val) {
    const biz = findInTree(options.businessCategoryList, val);
    row.businessCategoryCode = biz ? biz.code : "";
    fetchDataDomain(row);
  } else {
    if (rowUiContext[row.tableId]) {
      rowUiContext[row.tableId].dataDomainList = [];
    }
  }
};

/**
 * 主题域切换处理
 */
const handleThemeDomainChange = (val, row) => {
  row.themeDomainCode = "";
  if (val) {
    const theme = findInTree(options.themeDomainList, val);
    row.themeDomainCode = theme ? theme.code : "";
  }
};

const findInTree = (tree, id) => {
  for (const node of tree) {
    if (node.id === id) return node;
    if (node.children) {
      const result = findInTree(node.children, id);
      if (result) return result;
    }
  }
  return null;
};

// --- 监听器 ---

// 仅在列表长度变化时刷新表格展示
watch(
  () => baseState.rows.length,
  () => {
    tableRef.value?.resetQuery();
  }
);

/**
 * 打开元数据选择弹窗
 */
const handleOpenMetadataSelect = (row = null) => {
  // 记录当前操作的行，用于确认后的替换逻辑
  uiState.activeTableId = row?.tableId || null;
  metadataSelectRef.value.show();
};

/**
 * 打开批量设置弹窗
 */
const handleOpenBatchSetting = () => {
  // 重置批量设置表单为默认值（{{ td('dpp.asset.addAsset.noChange') }}状态）
  batchForm.tableType = -1;
  batchForm.dataLayerId = -1;
  batchForm.tableCase = -1;
  batchForm.businessCategoryId = -1;
  batchForm.dataDomainId = -1;
  batchForm.themeDomainId = -1;
  uiState.batchVisible = true;
};

/**
 * 表格选中项变化
 */
const handleSelectionChange = (selection) => {
  selectedRows.value = selection;
};

/**
 * 删除行
 */
const handleDeleteRow = (row) => {
  const index = baseState.rows.findIndex((r) => r.tableId === row.tableId);
  if (index > -1) {
    baseState.rows.splice(index, 1);
    selectedRows.value = selectedRows.value.filter(
      (s) => s.tableId !== row.tableId
    );
    delete rowUiContext[row.tableId];
  }
};

const handleBatchTableTypeChange = () => {
  batchForm.businessCategoryId = -1;
  batchForm.dataDomainId = -1;
  batchForm.themeDomainId = -1;
  batchDataDomainList.value = [];
};

const handleBatchBusinessChange = async (val) => {
  batchForm.dataDomainId = -1;
  if (val && val !== -1) {
    batchDomainLoading.value = true;
    try {
      const res = await listDataDomain({
        pageNum: 1,
        pageSize: 1000,
        orderByColumn: "create_time",
        isAsc: "descending",
        businessCategoryId: val,
        validFlag: true,
      });
      const tree = proxy.handleTree(
        res.data?.rows || res.data || res.rows || [],
        "id",
        "parentId"
      );
      batchDataDomainList.value = formatTreeData(tree);
    } finally {
      batchDomainLoading.value = false;
    }
  } else {
    batchDataDomainList.value = [];
  }
};

/**
 * 确认批量设置
 */
const confirmBatchSetting = async () => {
  for (const row of selectedRows.value) {
    // 1. 基础属性更新（若选中了有效值则更新）
    if (batchForm.tableType !== -1) row.tableType = batchForm.tableType;
    if (batchForm.dataLayerId !== -1) row.dataLayerId = batchForm.dataLayerId;
    if (batchForm.tableCase !== -1) row.tableCase = batchForm.tableCase;

    // 提取赋值方法以便复用
    const applyBusinessAttr = () => {
      if (batchForm.businessCategoryId !== -1) {
        row.businessCategoryId = batchForm.businessCategoryId;
        const biz = findInTree(
          options.businessCategoryList,
          batchForm.businessCategoryId
        );
        row.businessCategoryCode = biz ? biz.code : "";

        if (batchForm.dataDomainId !== -1) {
          if (!rowUiContext[row.tableId]) {
            rowUiContext[row.tableId] = { dataDomainList: [] };
          }
          rowUiContext[row.tableId].dataDomainList = JSON.parse(
            JSON.stringify(batchDataDomainList.value)
          );
          row.dataDomainId = batchForm.dataDomainId;
        } else {
          // 如果业务分类变了但数据分域选的是"不修改"，则需要根据新业务分类重置/获取数据分域
          // row.dataDomainId = null;
          // fetchDataDomain(row);
        }
      } else if (batchForm.dataDomainId !== -1) {
        row.dataDomainId = batchForm.dataDomainId;
      }
    };

    const applyThemeAttr = () => {
      if (batchForm.themeDomainId !== -1) {
        row.themeDomainId = batchForm.themeDomainId;
        const theme = findInTree(
          options.themeDomainList,
          batchForm.themeDomainId
        );
        row.themeDomainCode = theme ? theme.code : "";
      }
    };

    // 2. 业务属性更新：根据行当前的 tableType 决定赋值范围
    if (!row.tableType) {
      applyBusinessAttr();
      applyThemeAttr();
    } else if (row.tableType === "4") {
      // 应用表：仅赋值所属主题
      applyThemeAttr();
    } else {
      // 非应用表：仅赋值业务分类/数据分域
      applyBusinessAttr();
    }
  }
  uiState.batchVisible = false;
  // proxy.$modal.msgSuccess("批量设置成功");
};

/**
 * 确认注册
 */
async function confirmPublish() {
  if (!baseState.rows.length) {
    proxy.$modal.msgWarning(td('dpp.asset.addAsset.selectMetadataFirst'));
    return;
  }

  try {
    await tableFormRef.value.validate();
  } catch (error) {
    proxy.$modal.msgWarning(td('dpp.asset.addAsset.checkRequired'));
    return;
  }

  uiState.publishLoading = true;
  try {
    const requests = baseState.rows.map((row) => {
      const item = {
        name: row.name,
        type: baseState.assetType,
        tableType: row.tableType,
        dataLayerId: row.dataLayerId,
        businessCategoryId: row.businessCategoryId,
        businessCategoryCode: row.businessCategoryCode,
        dataDomainId: row.dataDomainId,
        themeDomainId: row.themeDomainId,
        themeDomainCode: row.themeDomainCode,
        tableCase: String(row.tableCase),
        tableId: row.tableId,
        datasourceId: row.datasourceId,
        tableName: row.tableName,
        tableComment: row.tableComment,
      };

      return addDaAsset(item);
    });

    await Promise.all(requests);
    proxy.$modal.msgSuccess(td('dpp.asset.addAsset.registerSuccess'));
    setRefreshNeeded();
    router.push({ path: "/da/asset" });
  } catch (error) {
    console.error("注册失败:", error);
  } finally {
    uiState.publishLoading = false;
  }
}

/**
 * 元数据选择确认处理
 */
const handleMetadataConfirm = (tables) => {
  if (!tables || !Array.isArray(tables) || tables.length === 0) return;

  const newRows = tables.map((table) => {
    return {
      tableId: table.id,
      name: table.tableComment || table.tableName,
      tableName: table.tableName,
      tableComment: table.tableComment,
      datasourceId: table.datasourceId,
      tableType: null, // 移除默认值
      dataLayerId: null,
      tableCase: table.tableName === table.tableName.toUpperCase() ? 1 : 2,
      businessCategoryId: null,
      businessCategoryCode: "",
      dataDomainId: null,
      themeDomainId: null,
      themeDomainCode: "",
    };
  });

  if (uiState.activeTableId) {
    const index = baseState.rows.findIndex(
      (r) => r.tableId === uiState.activeTableId
    );
    if (index > -1) {
      baseState.rows.splice(index, 1, ...newRows);
      delete rowUiContext[uiState.activeTableId];
      ElMessage.success(td('dpp.asset.addAsset.metadataUpdated'));
      uiState.activeTableId = null;
      return;
    }
  }
  baseState.rows.push(...newRows);
};

function goBack() {
  router.push({ path: "/da/asset" });
}

onMounted(() => {
  fetchAllOptions();
});
</script>

<style lang="scss" scoped >
.title-wrap {
  flex-wrap: wrap;
}

.h2-titles {
  white-space: nowrap;
}

.asset-hint {
  display: flex;
  align-items: center;
  font-size: 14px;
  line-height: 22px;
  font-family: Microsoft YaHei-Regular;
  color: #888;
  height: 32px;
  margin-left: -20px;
  .desc-icon {
    margin-right: 3px;
    height: 16px;
    width: 16px;
    color: #888;
  }
}

.api-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.custom-card {
  width: 100%;
  padding: 0px 20px;
  background: #fff;
  box-sizing: border-box;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 20px;

  .asset-type-select {
    display: flex;
    align-items: center;
    .label {
      font-weight: bold;
      font-size: 14px;
      color: #606266;
    }
  }
}

.pagecont-top {
  position: relative;
  padding-bottom: 20px;
}

.button-style {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 25px 35px 25px 0px;
  background: #fff;
  text-align: right;
  z-index: 10;
  border-top: 1px solid #f0f2f5;
}

.flex-between-center {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-items-center {
  display: flex;
  align-items: center;
}
.no-mb {
  margin-bottom: 0 !important;
}

.main {
  background-color: white;
}

.h2-desc {
  font-size: 14px;
  margin-bottom: 10px;
  color: #213547;
}

.full-width {
  width: 100%;
}

.gap20 {
  gap: 20px;
}

.asset-label {
  font-weight: bold;
  font-size: 14px;
  color: #606266;
}

.w250 {
  width: 250px;
}

.w300 {
  width: 300px;
}

.m0 {
  margin: 0;
}

.ml0 {
  margin-left: 0;
  margin-bottom: 10px;
}
.emptyBg {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #909399;
  font-size: 14px;
  img {
    width: 280px;
    margin-bottom: 10px;
  }
}

:deep(.emptyBg) {
  @extend .emptyBg;
}
</style>
