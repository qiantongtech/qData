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
          选择元数据
        </span>
      </template>
      <div class="field-wrap" v-if="visible">
        <div class="search-bar">
          <el-input
            v-model="tableQueryParams.keyWord"
            placeholder="请输入元数据名称或表注释搜索"
            class="content-search-input"
            clearable
            :prefix-icon="Search"
            @input="handleInput"
            @keyup.enter="handleSearch"
            @clear="handleSearch"
          />
        </div>
        <div class="module-body infotop technical-info">
          <!-- 左侧：来源系统树 -->
          <div class="column-box">
            <div class="box-title">来源系统架构</div>
            <div class="box-content">
              <SourceSystemTree
                ref="sourceSystemTreeRef"
                :initial-left-width="300"
                @node-click="handleNodeClick"
                class="source-system-tree"
              />
            </div>
          </div>

          <!-- 中间：表列表 -->
          <div class="column-box">
            <div class="box-title">表列表</div>
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
                    {{ row.dssetFlag ? "已注册" : "未注册" }}
                  </el-tag>
                </template>
              </qt-table>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button
            type="primary"
            :disabled="selectedTableList.length === 0"
            @click="handleConfirm"
            >确定</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
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

// --- 状态定义 ---
const visible = ref(false);
const sourceSystemTreeRef = ref(null);
const tableRef = ref(null);

const selectedTableList = ref([]);

// --- 表列表配置 ---
const tableColumns = [
  {
    type: "selection",
    width: 50,
    selectable: (row) => !row.dssetFlag,
    "reserve-selection": true,
  },
  {
    label: "库名",
    prop: "dbName",
    align: "left",
    minWidth: 100,
    showOverflowTooltip: true,
  },
  {
    label: "表名称",
    prop: "tableName",
    align: "left",
    minWidth: 150,
    showOverflowTooltip: true,
  },
  {
    label: "表注释",
    prop: "tableComment",
    align: "left",
    minWidth: 150,
    showOverflowTooltip: true,
  },
  {
    label: "描述",
    prop: "description",
    align: "left",
    minWidth: 150,
    showOverflowTooltip: true,
  },
  {
    label: "注册状态",
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
  dataType: 1, // 结构化
  assetType: undefined,
  hideTableIds: props.hideTableIds,
});

// 监听 hideTableIds 变化
watch(
  () => props.hideTableIds,
  (val) => {
    tableQueryParams.hideTableIds = val;
  }
);

// --- 监听器 ---
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

// --- 方法定义 ---

/**
 * 初始化弹窗状态
 */
const initDialog = () => {
  resetState();
  nextTick(() => {
    tableRef.value?.elTableRef?.clearSelection();
    tableRef.value?.resetQuery();
  });
};

/**
 * 重置状态
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
 * 外部调用的显示方法
 */
const show = () => {
  visible.value = true;
  initDialog();
};

/**
 * 输入处理（防抖搜索）
 */
const handleInput = debounce(() => {
  tableRef.value?.resetQuery();
}, 500);

/**
 * 搜索处理（立即搜索）
 */
const handleSearch = () => {
  handleInput.cancel();
  tableRef.value?.resetQuery();
};

// 组件卸载前清除防抖计时器
onBeforeUnmount(() => {
  handleInput.cancel();
});

/**
 * 树节点点击处理
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
 * 表勾选变化（多选）
 */
const handleSelectionChange = (selection) => {
  selectedTableList.value = selection;
};

/**
 * 取消处理
 */
const handleCancel = () => {
  visible.value = false;
};

/**
 * 确认处理
 */
const handleConfirm = () => {
  if (selectedTableList.value.length > 0) {
    // 仅返回必要的标准字段，避免携带冗余的 API 内部字段
    const results = selectedTableList.value.map((table) => ({
      id: table.id,
      tableName: table.tableName,
      tableComment: table.tableComment,
      datasourceId: table.datasourceId,
    }));
    emit("confirm", results);
    visible.value = false;
  } else {
    proxy.$modal.msgWarning("请至少选择一张表");
  }
};

// 暴露方法给父组件
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
