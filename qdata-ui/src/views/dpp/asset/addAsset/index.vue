<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top-wrap flex-items-center gap20">
      <el-form :inline="true" @submit.prevent>
        <el-form-item :label="td('dpp.asset.add.assetType')" style="margin-bottom: 0">
          <el-select
            v-model="baseState.assetType"
            :placeholder="td('dpp.asset.add.assetTypePlaceholder')"
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
              <div class="h2-titles">{{ td('dpp.asset.add.assetInfoComplete') }}</div>
              <div class="asset-hint" style="margin-left: 10px">
                {{ td('dpp.asset.add.assetInfoHint') }}
              </div>
            </div>
            <div class="flex-items-center">
              <el-button type="primary" @click="handleOpenMetadataSelect()"
                >{{ td('dpp.asset.add.selectMetadata') }}</el-button
              >
              <el-button
                @click="handleOpenBatchSetting"
                :disabled="!selectedRows.length"
                >{{ td('dpp.asset.add.batchSet') }}</el-button
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
                      message: td('dpp.asset.add.assetNameRequired'),
                      trigger: 'blur',
                    },
                  ]"
                  label-width="0"
                  style="margin-bottom: 0"
                >
                  <el-input v-model="row.name" :placeholder="td('dpp.asset.add.assetNamePlaceholder')" />
                </el-form-item>
              </template>
              <template #tableType="{ row, $index }">
                <el-form-item
                  :prop="'rows.' + $index + '.tableType'"
                  :rules="[
                    {
                      required: true,
                      message: td('dpp.asset.add.tableTypeRequired'),
                      trigger: 'change',
                    },
                  ]"
                  label-width="0"
                  style="margin-bottom: 0"
                >
                  <el-select
                    v-model="row.tableType"
                    :placeholder="td('dpp.asset.add.tableTypePlaceholder')"
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
                      message: td('dpp.asset.add.dataLayerRequired'),
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
                    :placeholder="td('dpp.asset.add.dataLayerPlaceholder')"
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
                        message: td('dpp.asset.add.businessCategoryRequired'),
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
                      :placeholder="td('dpp.asset.add.businessCategoryPlaceholder')"
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
                        message: td('dpp.asset.add.themeDomainRequired'),
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
                      :placeholder="td('dpp.asset.add.themeDomainPlaceholder')"
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
                        message: td('dpp.asset.add.dataDomainRequired'),
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
                      :placeholder="td('dpp.asset.add.dataDomainPlaceholder')"
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
                      message: td('dpp.asset.add.tableCaseRequired'),
                      trigger: 'change',
                    },
                  ]"
                  label-width="0"
                  style="margin-bottom: 0"
                >
                  <el-select v-model="row.tableCase" :placeholder="td('dpp.asset.add.tableCasePlaceholder')">
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
          <el-button @click="goBack" class="action-btn">{{ td('dpp.asset.add.returnToList') }}</el-button>
          <el-button
            type="primary"
            :disabled="!baseState.rows.length"
            :loading="uiState.publishLoading"
            @click="confirmPublish"
            class="action-btn"
            >{{ td('dpp.asset.add.registerAndExit') }}</el-button
          >
        </div>
      </div>
    </div>
    <!-- Metadata selection popup -->
    <MetadataSelectModal
      ref="metadataSelectRef"
      :hideTableIds="hideTableIds"
      @confirm="handleMetadataConfirm"
    />
    <!-- Batch settings pop-up window -->
    <el-dialog
      :title="td('dpp.asset.add.batchSetting')"
      v-model="uiState.batchVisible"
      width="600px"
      append-to-body
      draggable
      :append-to="$refs['app-container']"
    >
      <el-form :model="batchForm" label-width="110px" :label-position="labelPosition">
        <div class="hint-div ml0">
          <el-icon color="#2A7BFD" size="16px">
            <InfoFilled />
          </el-icon>
          <span>
            {{ td('dpp.asset.add.batchSettingHint') }}
          </span>
        </div>
        <el-form-item :label="td('dpp.asset.add.tableType')" :label-position="labelPosition">
          <el-select
              v-model="batchForm.tableType"
              :placeholder="td('dpp.asset.add.noChange')"
              class="full-width"
              @change="handleBatchTableTypeChange"
            clearable
          >
            <el-option :label="td('dpp.asset.add.noChange')" :value="-1" />
            <el-option
              v-for="item in table_type"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="td('dpp.asset.add.dataLayer')" :label-position="labelPosition">
          <el-tree-select
            v-model="batchForm.dataLayerId"
            :data="[
            { id: -1, displayName: td('dpp.asset.add.noChange') },
              ...options.dataLayerList,
            ]"
            :props="{
              value: 'id',
              label: 'displayName',
              children: 'children'
            }"
            node-key="id"
            value-key="id"
            :placeholder="td('dpp.asset.add.noChange')"
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
          :label="td('dpp.asset.add.businessCategory')"
         :label-position="labelPosition">
          <el-tree-select
            v-model="batchForm.businessCategoryId"
            :data="[
              { id: -1, displayName: td('dpp.asset.add.noChange') },
              ...options.businessCategoryList,
            ]"
            :props="{
              value: 'id',
              label: 'displayName',
              children: 'children'
            }"
            node-key="id"
            value-key="id"
            :placeholder="td('dpp.asset.add.noChange')"
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
          :label="td('dpp.asset.add.dataDomain')"
         :label-position="labelPosition">
          <el-tree-select
            v-model="batchForm.dataDomainId"
            :data="[
              { id: -1, displayName: td('dpp.asset.add.noChange') },
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
            :placeholder="td('dpp.asset.add.noChange')"
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
          :label="td('dpp.asset.add.themeDomain')"
         :label-position="labelPosition">
          <el-tree-select
            v-model="batchForm.themeDomainId"
            :data="[
              { id: -1, displayName: td('dpp.asset.add.noChange') },
              ...options.themeDomainList,
            ]"
            :props="{
              value: 'id',
              label: 'displayName',
              children: 'children'
            }"
            node-key="id"
            value-key="id"
            :placeholder="td('dpp.asset.add.noChange')"
            check-strictly
            filterable
            clearable
            default-expand-all
            class="full-width"
          />
        </el-form-item>
        <el-form-item :label="td('dpp.asset.add.tableCase')" :label-position="labelPosition">
          <el-select
            v-model="batchForm.tableCase"
            :placeholder="td('dpp.asset.add.noChange')"
            class="full-width"
            clearable
          >
            <el-option :label="td('dpp.asset.add.noChange')" :value="-1" />
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

const { td } = useDefaultLang();// --- Basic configuration and tools ---
const { proxy } = getCurrentInstance();
const router = useRouter();
const { setRefreshNeeded } = usePageRefresh("da_asset");
const { table_type, table_name_case, da_asset_type } = proxy.useDict(
  "table_type",
  "table_name_case",
  "da_asset_type"
);

// 1. Basic state
const baseState = reactive({
  rows: [], // Asset List Data
  assetType: "1", // Asset type
});

// 2. UI and pop-up window status
const uiState = reactive({
  publishLoading: false, // Register button loading
  batchVisible: false, // Set popup visibility in batches
  activeTableId: null,
});

// 3. Reference definition
const tableFormRef = ref(null);
const tableRef = ref(null);
const metadataSelectRef = ref(null); // Metadata selection pop-up window reference

// 4. Drop-down option data
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

// 5. Batch setting form
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

// --- Computed properties ---

// Form data packaging
const tableForm = reactive({
  rows: computed(() => baseState.rows),
});

const hideTableIds = computed(() => {
  return baseState.rows.map((row) => row.tableId).join(",");
});

// ---Table configuration ---

const tableColumns = [
  { type: "selection", width: 55 },
  { type: "index", label: td('common.texts.number'), width: 60 },
  {
    label: td('dpp.asset.add.table.sourceTable')+'/'+td('dpp.asset.add.table.tableComment'),
    width: 200,
    align: "left",
    list: [
      { prop: "tableName" },
      { prop: "tableComment", class: "color999 fz12" },
    ],
  },
  { label: td('dpp.asset.add.table.assetName'), width: 200, slot: "name" },
  { label: td('dpp.asset.add.table.tableType'), width: 140, slot: "tableType" },
  { label: td('dpp.asset.add.table.hierarchy'), width: 180, slot: "dataLayerId" },
  { label: td('dpp.asset.add.table.businessCategory'), slot: "businessCategoryId" },
  { label: td('dpp.asset.add.table.dataDomain'), slot: "dataDomainId" },
  { label: td('dpp.asset.add.table.theme'), slot: "themeDomainId" },
  { label: td('dpp.asset.add.table.tableNamingRule'), width: 140, slot: "tableCase" },
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
 * Format tree data, add displayName
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
 * Get all basic dropdown options
 */
const fetchAllOptions = () => {
  // 1. Obtain data warehouse stratification
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

  // 2. Get business classification
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

  // 3. Get the subject domain
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
 * Obtain data domain according to business classification
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
    console.error("Failed to fetch data domain:", error);
  }
};

/**
 * List data source methods
 */
const listLocalData = () => {
  return Promise.resolve({
    data: baseState.rows,
    total: baseState.rows.length,
  });
};

/**
 * Table type switching processing
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
 * Business classification switching processing
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
 * Subject domain switching processing
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

// --- Listener ---

// Only refresh the table display when the list length changes
watch(
  () => baseState.rows.length,
  () => {
    tableRef.value?.resetQuery();
  }
);

/**
 * Open the metadata selection popup
 */
const handleOpenMetadataSelect = (row = null) => {
  // The line that records the current operation is used for replacement logic after confirmation.
  uiState.activeTableId = row?.tableId || null;
  metadataSelectRef.value.show();
};

/**
 * Open the batch settings pop-up window
 */
const handleOpenBatchSetting = () => {
  // Reset the batch setting form to default value ({{ td('dpp.asset.add.noChange') }} state)
  batchForm.tableType = -1;
  batchForm.dataLayerId = -1;
  batchForm.tableCase = -1;
  batchForm.businessCategoryId = -1;
  batchForm.dataDomainId = -1;
  batchForm.themeDomainId = -1;
  uiState.batchVisible = true;
};

/**
 * Table selected item changes
 */
const handleSelectionChange = (selection) => {
  selectedRows.value = selection;
};

/**
 * Delete row
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
 * Confirm batch settings
 */
const confirmBatchSetting = async () => {
  for (const row of selectedRows.value) {
    // 1. Basic attribute update (update if valid value is selected)
    if (batchForm.tableType !== -1) row.tableType = batchForm.tableType;
    if (batchForm.dataLayerId !== -1) row.dataLayerId = batchForm.dataLayerId;
    if (batchForm.tableCase !== -1) row.tableCase = batchForm.tableCase;

    // Extract assignment methods for reuse
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
          // If the business classification changes but the data partition is selected as "Do not modify", you need to reset/obtain the data partition according to the new business classification.
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

    // 2. Business attribute update: determine the assignment range based on the current tableType of the row
    if (!row.tableType) {
      applyBusinessAttr();
      applyThemeAttr();
    } else if (row.tableType === "4") {
      // Application table: only assign values to the subject to which they belong
      applyThemeAttr();
    } else {
      // Non-application table: only assign business classification/data domain
      applyBusinessAttr();
    }
  }
  uiState.batchVisible = false;
  // proxy.$modal.msgSuccess("Batch setting successful");
};

/**
 * Confirm registration
 */
async function confirmPublish() {
  if (!baseState.rows.length) {
    proxy.$modal.msgWarning(td('dpp.asset.add.selectMetadataFirst'));
    return;
  }

  try {
    await tableFormRef.value.validate();
  } catch (error) {
    proxy.$modal.msgWarning(td('dpp.asset.add.checkRequired'));
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
    proxy.$modal.msgSuccess(td('dpp.asset.add.registerSuccess'));
    setRefreshNeeded();
    router.push({ path: "/da/asset" });
  } catch (error) {
    console.error("Registration failed:", error);
  } finally {
    uiState.publishLoading = false;
  }
}

/**
 * Metadata selection confirmation processing
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
      tableType: null, // Remove default value
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
      ElMessage.success(td('dpp.asset.add.metadataUpdated'));
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
