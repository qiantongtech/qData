<template>
  <div class="app-container" ref="app-container">
    <el-dialog
      v-model="visible"
      class="metadata-select-dialog max-dialogs-status0"
      width="80%"
      top="10vh"
      append-to-body
      draggable
      :append-to="$refs['app-container']"
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ td('dpp.metadataSelect.selectMetadata', 'Select Metadata') }}
        </span>
      </template>
      <div class="field-wrap" v-if="visible">
        <div class="search-bar">
          <el-input
            v-model="tableQueryParams.keyWord"
            :placeholder="td('dpp.metadataSelect.searchPlaceholder', 'Please enter metadata name or table comment to search')"
            class="content-search-input"
            clearable
            :prefix-icon="Search"
            @input="handleInput"
            @keyup.enter="handleSearch"
            @clear="handleSearch"
          />
        </div>
        <div class="module-body infotop technical-info">
          <!-- Left: source phylogenetic tree -->
          <div class="column-box">
            <div class="box-title">{{ td('dpp.metadataSelect.sourceSystem', 'Source System Architecture') }}</div>
            <div class="box-content">
              <SourceSystemTree
                ref="sourceSystemTreeRef"
                :initial-left-width="300"
                @node-click="handleNodeClick"
                class="source-system-tree"
              />
            </div>
          </div>

          <!-- Middle: table list -->
          <div class="column-box">
            <div class="box-title">{{ td('dpp.metadataSelect.tableList', 'Table List') }}</div>
            <div class="box-content">
              <qt-table
                ref="tableRef"
                :columns="tableColumns"
                :func="listTable"
                :params="tableQueryParams"
                :config="tableConfig"
              >
                <template #dssetFlag="{ row }">
                  <el-tag :type="row.dssetFlag ? 'success' : 'info'">
                    {{ row.dssetFlag ? td('dpp.metadataSelect.registered', 'Registered') : td('dpp.metadataSelect.notRegistered', 'Not Registered') }}
                  </el-tag>
                </template>
              </qt-table>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancel">{{ td('common.button.cancel', 'Cancel') }}</el-button>
          <el-button
            type="primary"
            :disabled="selectedTableList.length === 0"
            @click="handleConfirm"
            >{{ td('common.button.confirm', 'Confirm') }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import {
  ref,
  reactive,
  watch,
  getCurrentInstance,
  nextTick,
  onBeforeUnmount,
} from "vue";
import SourceSystemTree from "@/views/mc/task/structured/components/SourceSystemTree.vue";
import { listTable } from "@/api/mc/unreleased/table";
import { Search } from "@element-plus/icons-vue";
import { debounce } from "lodash-es";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  hideTableIds: {
    type: String,
    default: "",
  },
});

const emit = defineEmits(["update:modelValue", "confirm"]);

// ---State definition ---
const visible = ref(false);
const sourceSystemTreeRef = ref(null);
const tableRef = ref(null);

const selectedTableList = ref([]);

// --- Table list configuration ---
const tableColumns = [
  {
    type: "selection",
    width: 50,
    selectable: (row) => !row.dssetFlag,
    "reserve-selection": true,
  },
  {
    label: td('dpp.metadataSelect.dbName', 'Database Name'),
    prop: "dbName",
    align: "left",
    minWidth: 100,
    showOverflowTooltip: true,
  },
  {
    label: td('dpp.metadataSelect.tableName', 'Table Name'),
    prop: "tableName",
    align: "left",
    minWidth: 150,
    showOverflowTooltip: true,
  },
  {
    label: td('dpp.metadataSelect.tableComment', 'Table Comment'),
    prop: "tableComment",
    align: "left",
    minWidth: 150,
    showOverflowTooltip: true,
  },
  {
    label: td('common.texts.description', 'Description'),
    prop: "description",
    align: "left",
    minWidth: 150,
    showOverflowTooltip: true,
  },
  {
    label: td('dpp.metadataSelect.registerStatus', 'Registration Status'),
    width: 100,
    slot: "dssetFlag",
  },
];

const tableConfig = {
  table: {
    stripe: true,
    "row-key": "id",
    onSelectionChange: (selection) => handleSelectionChange(selection),
  },
  pagination: {
    layout: "total, prev, pager, next",
  },
};

const tableQueryParams = reactive({
  sourceSystemId: undefined,
  datasourceId: undefined,
  taskId: undefined,
  dbId: undefined,
  tableName: "",
  tableComment: "",
  keyWord: "",
  dataType: 1, // structured
  assetType: undefined,
  hideTableIds: props.hideTableIds,
});

// Monitor hideTableIds changes
watch(
  () => props.hideTableIds,
  (val) => {
    tableQueryParams.hideTableIds = val;
  }
);

// --- Listener ---
watch(
  () => props.modelValue,
  (val) => {
    visible.value = val;
    if (val) {
      initDialog();
    }
  }
);

watch(visible, (val) => {
  emit("update:modelValue", val);
  if (!val) {
    resetState();
  }
});

// ---Method definition ---

/**
 * Initialize pop-up window state
 */
const initDialog = () => {
  resetState();
  nextTick(() => {
    tableRef.value?.elTableRef?.clearSelection();
    tableRef.value?.resetQuery();
  });
};

/**
 * reset state
 */
const resetState = () => {
  selectedTableList.value = [];
  tableQueryParams.sourceSystemId = undefined;
  tableQueryParams.datasourceId = undefined;
  tableQueryParams.taskId = undefined;
  tableQueryParams.dbId = undefined;
  tableQueryParams.tableName = "";
  tableQueryParams.keyWord = "";
  tableQueryParams.assetType = undefined;
};

/**
 * Display method of external call
 */
const show = () => {
  visible.value = true;
  initDialog();
};

/**
 * Input processing (anti-shake search)
 */
const handleInput = debounce(() => {
  tableRef.value?.resetQuery();
}, 500);

/**
 * Search Processing (Search Now)
 */
const handleSearch = () => {
  handleInput.cancel();
  tableRef.value?.resetQuery();
};

// Clear the anti-shake timer before component uninstallation
onBeforeUnmount(() => {
  handleInput.cancel();
});

/**
 * Tree node click processing
 */
const handleNodeClick = (data) => {
  tableQueryParams.sourceSystemId = undefined;
  tableQueryParams.datasourceId = undefined;
  tableQueryParams.taskId = undefined;
  tableQueryParams.dbId = undefined;
  selectedTableList.value = [];
  tableRef.value?.elTableRef?.clearSelection();

  if (data.type === "SOURCE") {
    tableQueryParams.sourceSystemId = data.id;
  } else if (data.type === "DATASOURCE") {
    tableQueryParams.datasourceId = data.id;
  } else if (data.type === "DATABASE") {
    tableQueryParams.taskId = data.taskId;
    tableQueryParams.dbId = data.id;
  }

  tableRef.value?.resetQuery();
};

/**
 * Table check changes (multiple selections)
 */
const handleSelectionChange = (selection) => {
  selectedTableList.value = selection;
};

/**
 * Cancel processing
 */
const handleCancel = () => {
  visible.value = false;
};

/**
 * Confirm processing
 */
const handleConfirm = () => {
  if (selectedTableList.value.length > 0) {
    // Only return necessary standard fields to avoid carrying redundant API internal fields
    const results = selectedTableList.value.map((table) => ({
      id: table.id,
      tableName: table.tableName,
      tableComment: table.tableComment,
      datasourceId: table.datasourceId,
    }));
    emit("confirm", results);
    visible.value = false;
  } else {
    proxy.$modal.msgWarning(td('dpp.metadataSelect.selectAtLeastOneTable', 'Please select at least one table'));
  }
};

// Expose methods to parent component
defineExpose({
  show,
});
</script>

<style lang="scss" scoped>
.metadata-select-dialog {
  :deep(.el-dialog__body) {
    padding: 0;
    height: 75vh;
    overflow: hidden;
    display: flex;
    flex-direction: column;
  }
}

.field-wrap {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 15px;
  overflow: hidden;
}

.search-bar {
  display: flex;
  justify-content: flex-end;
  padding-bottom: 10px;
  background-color: #fff;

  .content-search-input {
    width: 320px;
    :deep(.el-input__wrapper) {
      box-shadow: 0 0 0 1px #dcdfe6 inset !important;
      &.is-focus {
        box-shadow: 0 0 0 1px var(--el-color-primary) inset !important;
      }
    }
  }
}

.module-head {
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #e6e6e6;
  background-color: #fff;
}

.module-body {
  margin-bottom: 0;
  flex: 1;
  display: flex;
  overflow: hidden;
}

.technical-info {
  display: flex;
  flex: 1;
  margin-bottom: 0;
  overflow: hidden;
}

.column-box {
  overflow: hidden;
  border: 1px solid #e6e6e6;
  display: flex;
  flex-direction: column;
  &:first-child {
    width: 350px;
    border-right: 0;

    .box-content {
      padding: 0;
    }

    :deep(.left-pane) {
      width: 100% !important;
      height: 68vh !important;
      margin-left: 0 !important;
      background-color: #fff !important;

      .left-tree {
        width: 100%;
        height: 100%;
        padding: 10px 0;
        display: flex;
        flex-direction: column;
        box-shadow: none !important;
        .head-container:first-child {
          padding: 0 10px 0px;
          margin-bottom: 0px;
        }

        .tree-wrapper {
          flex: 1;
          overflow: auto;
          padding: 0 5px;
        }

        .el-tree {
          background-color: transparent;
        }

        .el-input__wrapper {
          box-shadow: 0 0 0 1px #dcdfe6 inset !important;
          &.is-focus {
            box-shadow: 0 0 0 1px var(--el-color-primary) inset !important;
          }
        }
      }
    }
    :deep(.resize-bar) {
      display: none;
    }
  }

  &:nth-child(2) {
    flex: 1;
  }
}

.box-title {
  background-color: #f6f8fa;
  padding-left: 20px;
  font-size: 14px;
  line-height: 32px;
  font-weight: 600;
  color: #747678;
}

.box-content {
  flex: 1;
  padding: 10px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.section-header {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
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
    width: 120px;
    margin-bottom: 10px;
  }
}

:deep(.emptyBg) {
  @extend .emptyBg;
}
</style>
<style>
.el-dialog.max-dialogs-status0 .el-dialog__body {
  padding: 0 !important;
  padding-left: 10px !important;
}
</style>
