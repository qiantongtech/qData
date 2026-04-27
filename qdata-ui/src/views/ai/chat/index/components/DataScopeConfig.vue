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
  <div class="data-scope-config app-container" ref="app-container">
    <!-- 1. 当前数据范围展示 (Summary Bar) -->
    <div class="summary-bar">
      <div class="summary-left-col">
        <div class="title-info">
          <img src="@/assets/ai/gpt-new.svg" class="robot-icon" />
          <span class="title">{{ title || "新对话" }}</span>
        </div>
      </div>
      <div class="summary-middle-col">
        <!-- 维表数量展示 -->
        <el-tooltip
          v-if="dimensionTableNames?.length"
          effect="light"
          placement="top"
        >
          <template #content>
            <div
              v-for="name in dimensionTableNames"
              :key="name"
              style="margin: 4px 0"
            >
              {{
                mergedTableCommentMap[name]
                  ? `${name}(${mergedTableCommentMap[name]})`
                  : name
              }}
            </div>
          </template>
          <div class="dim-tag" @click="showConfig = !showConfig">
            + {{ dimensionTableNames.length }}张维表
          </div>
        </el-tooltip>

        <el-form inline :disabled="disabled" class="summary-form">
          <el-form-item label="数据范围" required>
            <el-input
              :model-value="
                factTableName
                  ? factTableComment
                    ? `${factTableName}(${factTableComment})`
                    : factTableName
                  : ''
              "
              readonly
              placeholder="请配置当前数据范围"
              class="scope-input"
              :class="{ active: showConfig }"
              @click="showConfig = !showConfig"
            >
              <template #suffix>
                <el-icon class="arrow-icon">
                  <component :is="showConfig ? ArrowUp : ArrowDown" />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>

        <!-- 外部按钮插槽 (清空/上下滚动等) -->
        <div class="extra-slot">
          <slot name="extra"></slot>
        </div>
      </div>
    </div>

    <!-- 2. 配置面板 (3-step selection) -->
    <el-collapse-transition>
      <div class="config-panel" v-show="showConfig">
        <el-form
          class="steps-container"
          :disabled="disabled"
          label-width="auto"
        >
          <!-- Step 1: 数据源 -->
          <el-form-item label=" 数据源" required>
            <DatasourceList
              v-model="internalDatasourceId"
              :disabled="disabled"
              placeholder="请选择数据源"
              filterable
              @change="onDatasourceChange"
              flag="daQuality"
              class="config-select"
            />
          </el-form-item>

          <!-- Step 2: 事实表 -->
          <el-form-item label="事实表" required>
            <el-select
              v-model="internalFactTableName"
              :disabled="disabled"
              filterable
              remote
              remote-show-suffix
              :remote-method="remoteSearchFactTables"
              :loading="factTableLoading"
              @visible-change="handleFactTableSelectVisible"
              placeholder="请选择事实表"
              @change="onFactTableChange"
              class="config-select"
            >
              <el-option
                v-for="item in factTableOptions"
                :key="item.tableName"
                :label="
                  item.tableComment
                    ? `${item.tableName}(${item.tableComment})`
                    : item.tableName
                "
                :value="item.tableName"
              />
            </el-select>
          </el-form-item>

          <!-- Step 3: 关联维表 -->
          <el-form-item label="关联维表" required>
            <el-select
              v-model="internalDimensionTableNames"
              :disabled="disabled"
              filterable
              remote
              remote-show-suffix
              multiple
              collapse-tags
              collapse-tags-tooltip
              :remote-method="remoteSearchDimensionTables"
              :loading="dimensionTableLoading"
              @visible-change="handleDimensionTableSelectVisible"
              placeholder="请选择关联维表"
              @change="onDimensionTableChange"
              class="config-select"
            >
              <el-option
                v-for="item in dimensionTableOptions"
                :key="item.tableName"
                :label="
                  item.tableComment
                    ? `${item.tableName}(${item.tableComment})`
                    : item.tableName
                "
                :value="item.tableName"
              />
            </el-select>
          </el-form-item>
        </el-form>

        <div class="panel-footer">
          <el-button
            v-if="!disabled"
            type="primary"
            @click="handleConfirm"
            :disabled="
              !internalDatasourceId ||
              !internalFactTableName ||
              !internalDimensionTableNames?.length
            "
          >
            确认范围并开始问答
          </el-button>
          <el-button
            v-if="
              joinConditionMatchFlag === false ||
              joinConditionMatchFlag === 0 ||
              joinConditionMatchFlag === null
            "
            type="primary"
            plain
            @click="openAssociationDialog(conversationId)"
          >
            设置关联关系
          </el-button>
        </div>
      </div>
    </el-collapse-transition>

    <!-- 3. 设置关联关系弹窗 -->
    <el-dialog
      v-model="associationVisible"
      title="设置关联关系"
      width="1200px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
      @closed="handleCloseAssociationDialog"
    >
      <qt-table v-bind="associationTableStore" ref="associationTableRef">
        <template #selectedDimensionTable="scope">
          <el-select
            v-model="scope.row.selectedDimensionTable"
            placeholder="请选择维度表"
            clearable
            @change="onRowDimensionTableChange(scope.row)"
            style="width: 100%"
          >
            <el-option
              v-for="name in internalDimensionTableNames"
              :key="name"
              :label="
                mergedTableCommentMap[name]
                  ? `${name}(${mergedTableCommentMap[name]})`
                  : name
              "
              :value="name"
            />
          </el-select>
        </template>
        <template #selectedDimensionColumn="scope">
          <el-select
            v-model="scope.row.selectedDimensionColumn"
            placeholder="请选择维表字段"
            clearable
            :disabled="!scope.row.selectedDimensionTable"
            style="width: 100%"
            :class="{
              'is-error':
                scope.row.selectedDimensionTable &&
                !scope.row.selectedDimensionColumn,
            }"
          >
            <el-option
              v-for="col in dimensionTableColumnsMap[
                scope.row.selectedDimensionTable
              ] || []"
              :key="col.columnName"
              :label="
                col.columnComment
                  ? `${col.columnName}(${col.columnComment})`
                  : col.columnName
              "
              :value="col.columnName"
            />
          </el-select>
        </template>
      </qt-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="associationVisible = false"
            >取 消</el-button
          >
          <el-button
            type="primary"
            size="mini"
            @click="handleSaveAssociations"
            :loading="savingAssociations"
          >
            确 定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed, nextTick } from "vue";
import { Coin, ArrowDown, ArrowUp, Setting } from "@element-plus/icons-vue";
import DatasourceList from "@/components/Datasource/List.vue";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
} from "@/api/dpp/task/index.js";
import { ChatConversationApi } from "@/api/ai/chat/conversation/index.js";
import { ElMessage, ElMessageBox } from "element-plus";

const props = defineProps({
  title: {
    type: String,
    default: "新对话",
  },
  datasourceId: {
    type: [String, Number],
    default: "",
  },
  factTableName: {
    type: String,
    default: "",
  },
  factTableComment: {
    type: String,
    default: "",
  },
  dimensionTableNames: {
    type: Array,
    default: () => [],
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  initialShowConfig: {
    type: Boolean,
    default: false,
  },
  joinConditionMatchFlag: {
    type: [Boolean, Number, Object],
    default: true, // true or 1: 不需要匹配，false or 0: 需要匹配
  },
  conversationId: {
    type: [String, Number],
    default: null,
  },
  tableCommentMap: {
    type: Object,
    default: () => ({}),
  },
});

const emit = defineEmits([
  "update:datasourceId",
  "update:factTableName",
  "update:factTableComment",
  "update:dimensionTableNames",
  "confirm",
  "confirm-associations",
  "open-association-dialog",
]);

const handleOpenAssociationConfirm = (conversationId) => {
  // 如果是事件对象，则忽略它，优先使用传入的 conversationId，然后是 props 里的 conversationId
  const id =
    conversationId && typeof conversationId !== "object"
      ? conversationId
      : props.conversationId || currentConversationId.value;

  if (!id) {
    console.warn("未提供有效的会话 ID，无法打开确认框");
    return;
  }
  ElMessageBox.confirm(
    "关联关系无法自动识别，是否需要手动设置关联关系？",
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(() => {
      // 直接触发弹窗显示逻辑
      openAssociationDialog(id);
    })
    .catch(() => {
      // 用户取消，不执行任何操作
    });
};
// props.initialShowConfig
const showConfig = ref(false);

// watch(
//   () => props.initialShowConfig,
//   (val) => {
//     showConfig.value = val;
//   }
// );

watch(showConfig, (val) => {
  if (val) {
    internalDatasourceId.value = props.datasourceId;
    internalFactTableName.value = props.factTableName;
    internalFactTableComment.value = props.factTableComment;
    internalDimensionTableNames.value = [...props.dimensionTableNames];
  }
});

watch(
  () => props.conversationId,
  () => {
    showConfig.value = false;
  }
);

const internalDatasourceId = ref(props.datasourceId);
const internalFactTableName = ref(props.factTableName);
const internalFactTableComment = ref(props.factTableComment);
const internalDimensionTableNames = ref([...props.dimensionTableNames]);

const factTableOptions = ref([]);
const factTableLoading = ref(false);
const dimensionTableOptions = ref([]);
const dimensionTableLoading = ref(false);
const localTableCommentMap = ref({});

const mergedTableCommentMap = computed(() => ({
  ...props.tableCommentMap,
  ...localTableCommentMap.value,
}));

// 同步外部 props 到内部 state
watch(
  () => props.datasourceId,
  (val) => (internalDatasourceId.value = val),
  { immediate: true }
);
watch(
  () => props.factTableName,
  (val) => {
    internalFactTableName.value = val;
    // 如果有值，且不在 options 里，手动加一个以供渲染
    if (val && !factTableOptions.value.some((o) => o.tableName === val)) {
      factTableOptions.value.push({
        tableName: val,
        tableComment: props.factTableComment,
      });
    }
  },
  { immediate: true }
);
watch(
  () => props.factTableComment,
  (val) => (internalFactTableComment.value = val),
  { immediate: true }
);
watch(
  () => props.dimensionTableNames,
  (val) => {
    internalDimensionTableNames.value = [...(val || [])];
    // 如果有值，且不在 options 里，手动加一个以供渲染
    (val || []).forEach((name) => {
      if (!dimensionTableOptions.value.some((o) => o.tableName === name)) {
        dimensionTableOptions.value.push({
          tableName: name,
          tableComment: mergedTableCommentMap.value[name] || "",
        });
      }
    });
  },
  { deep: true, immediate: true }
);

watch(
  () => props.tableCommentMap,
  () => {
    // 当外部注释映射变化时，强制触发维表和事实表选项的渲染补齐
    if (
      internalFactTableName.value &&
      !factTableOptions.value.some(
        (o) => o.tableName === internalFactTableName.value
      )
    ) {
      factTableOptions.value.push({
        tableName: internalFactTableName.value,
        tableComment:
          internalFactTableComment.value ||
          mergedTableCommentMap.value[internalFactTableName.value] ||
          "",
      });
    }

    (internalDimensionTableNames.value || []).forEach((name) => {
      if (!dimensionTableOptions.value.some((o) => o.tableName === name)) {
        dimensionTableOptions.value.push({
          tableName: name,
          tableComment: mergedTableCommentMap.value[name] || "",
        });
      }
    });
  },
  { deep: true, immediate: true }
);

const onDatasourceChange = async (id) => {
  internalDatasourceId.value = id;
  internalFactTableName.value = "";
  internalFactTableComment.value = "";
  internalDimensionTableNames.value = [];
  factTableOptions.value = [];
  dimensionTableOptions.value = [];
  localTableCommentMap.value = {};
};

const mergeTableCommentMap = (list) => {
  const next = { ...localTableCommentMap.value };
  for (const item of list || []) {
    if (item?.tableName) {
      next[item.tableName] = item.tableComment || next[item.tableName] || "";
    }
  }
  localTableCommentMap.value = next;
};

const remoteSearchFactTables = async (query) => {
  if (factTableLoading.value) return;
  factTableLoading.value = true;
  if (!internalDatasourceId.value) {
    factTableOptions.value = [];
    factTableLoading.value = false;
    return;
  }
  try {
    const res = await getTablesByDataSourceId({
      datasourceId: internalDatasourceId.value,
      tableName: query,
    });
    const newOptions = res?.data || [];
    // 确保当前选中的值始终在选项中
    if (
      internalFactTableName.value &&
      !newOptions.some((o) => o.tableName === internalFactTableName.value)
    ) {
      newOptions.unshift({
        tableName: internalFactTableName.value,
        tableComment: internalFactTableComment.value,
      });
    }
    factTableOptions.value = newOptions;
    mergeTableCommentMap(factTableOptions.value);
  } finally {
    factTableLoading.value = false;
  }
};

const handleFactTableSelectVisible = (visible) => {
  if (visible) {
    // 如果已经有数据了，且不是强制刷新，可以考虑不调
    // 但为了确保数据是最新的，通常还是会调一次，这里加上 loading 判断防止重复
    if (!factTableLoading.value) {
      remoteSearchFactTables("");
    }
  }
};

const onFactTableChange = (val) => {
  const selectedTable =
    factTableOptions.value.find((item) => item.tableName == val) || null;
  internalFactTableComment.value =
    selectedTable?.tableComment || mergedTableCommentMap.value[val] || "";
};

const remoteSearchDimensionTables = async (query) => {
  if (dimensionTableLoading.value) return;
  dimensionTableLoading.value = true;
  if (!internalDatasourceId.value) {
    dimensionTableOptions.value = [];
    dimensionTableLoading.value = false;
    return;
  }
  try {
    const res = await getTablesByDataSourceId({
      datasourceId: internalDatasourceId.value,
      tableName: query,
    });
    const newOptions = res?.data || [];
    // 确保当前选中的值始终在选项中
    internalDimensionTableNames.value.forEach((name) => {
      if (!newOptions.some((o) => o.tableName === name)) {
        newOptions.unshift({
          tableName: name,
          tableComment: mergedTableCommentMap.value[name] || "",
        });
      }
    });
    dimensionTableOptions.value = newOptions;
    mergeTableCommentMap(dimensionTableOptions.value);
  } finally {
    dimensionTableLoading.value = false;
  }
};

const handleDimensionTableSelectVisible = (visible) => {
  if (visible) {
    if (!dimensionTableLoading.value) {
      remoteSearchDimensionTables("");
    }
  }
};

const onDimensionTableChange = (names) => {
  internalDimensionTableNames.value = names;
};

const handleConfirm = () => {
  showConfig.value = false;

  emit("update:datasourceId", internalDatasourceId.value);
  emit("update:factTableName", internalFactTableName.value);
  emit("update:factTableComment", internalFactTableComment.value);
  emit("update:dimensionTableNames", internalDimensionTableNames.value);

  emit("confirm", {
    datasourceId: internalDatasourceId.value,
    factTableName: internalFactTableName.value,
    factTableComment: internalFactTableComment.value,
    dimensionTableNames: internalDimensionTableNames.value,
    tableCommentMap: mergedTableCommentMap.value,
  });
};

// --- 设置关联关系弹窗相关逻辑 ---
const associationVisible = ref(false);
const savingAssociations = ref(false);
const factTableColumns = ref([]);
const dimensionTableColumnsMap = ref({});
const currentConversationId = ref(null);

const associationTableRef = ref(null);
const associationTableStore = ref({
  config: {
    sort: false,
    notPagination: true,
    table: {
      stripe: true,
    },
  },
  columns: [
    { type: "index", label: "编号", width: 60, align: "center" },
    {
      label: "字段名称",
      prop: "columnName",
      minWidth: 150,
      showOverflowTooltip: true,
    },
    {
      label: "中文名称",
      prop: "columnComment",
      minWidth: 150,
      showOverflowTooltip: true,
    },
    { label: "数据类型", prop: "columnType", width: 150, align: "center" },
    {
      label: "维度表",
      minWidth: 200,
      slot: "selectedDimensionTable",
    },
    {
      label: "维表字段",
      minWidth: 200,
      slot: "selectedDimensionColumn",
    },
  ],
  func: async () => {
    // 1. 获取事实表字段
    const factRes = await getColumnByAssetId({
      id: internalDatasourceId.value,
      tableName: internalFactTableName.value,
    });
    factTableColumns.value = (factRes?.data || []).map((col) => ({
      ...col,
      selectedDimensionTable: "",
      selectedDimensionColumn: "",
    }));

    // 2. 获取所有维表字段
    const dimensionPromises = internalDimensionTableNames.value.map(
      (tableName) =>
        getColumnByAssetId({
          id: internalDatasourceId.value,
          tableName: tableName,
        }).then((res) => ({ tableName, columns: res?.data || [] }))
    );

    const dimensionResults = await Promise.all(dimensionPromises);
    const nextMap = {};
    for (const res of dimensionResults) {
      nextMap[res.tableName] = res.columns;
    }
    dimensionTableColumnsMap.value = nextMap;

    return { data: factTableColumns.value };
  },
  params: {},
});

const openAssociationDialog = async (conversationId) => {
  // 如果是事件对象，则忽略它，优先使用传入的 conversationId，然后是 props 里的 conversationId，最后是 currentConversationId
  const id =
    conversationId && typeof conversationId !== "object"
      ? conversationId
      : props.conversationId || currentConversationId.value;

  if (!id) {
    ElMessage.warning("无法确定当前会话 ID，请先保存配置并开始问答。");
    return;
  }
  currentConversationId.value = id;
  associationVisible.value = true;
};

const handleCloseAssociationDialog = () => {
  factTableColumns.value = [];
  dimensionTableColumnsMap.value = {};
  currentConversationId.value = null;
};
const onRowDimensionTableChange = (row) => {
  // 切换维度表时，重置当前行的字段选择
  row.selectedDimensionColumn = "";
};
const handleSaveAssociations = async () => {
  if (!currentConversationId.value) return;

  // 校验逻辑：选了维度表必须选字段
  const invalidRow = factTableColumns.value.find(
    (row) => row.selectedDimensionTable && !row.selectedDimensionColumn
  );
  if (invalidRow) {
    ElMessage.warning(
      `字段 [${invalidRow.columnName}] 已选择维度表，请选择对应的维表字段`
    );
    return;
  }

  // 组合数据：只组合并发送维度表和字段均已选择的数据
  const associations = factTableColumns.value
    .filter((row) => row.selectedDimensionTable && row.selectedDimensionColumn)
    .map((row) => ({
      dimensionTable: row.selectedDimensionTable,
      factColumnName: row.columnName,
      dimensionColumnName: row.selectedDimensionColumn,
    }));

  if (associations.length === 0) {
    ElMessage.warning("请至少设置一个关联关系");
    return;
  }

  savingAssociations.value = true;
  try {
    await ChatConversationApi.setAssociations({
      id: currentConversationId.value,
      associations: JSON.stringify(associations),
    });
    ElMessage.success("设置关联关系成功");
    associationVisible.value = false;
    // 触发确认，重新开始问答（或者由父组件处理）
    emit("confirm-associations");
  } catch (error) {
    console.error("设置关联关系失败:", error);
  } finally {
    savingAssociations.value = false;
  }
};

defineExpose({
  openAssociationDialog,
  handleOpenAssociationConfirm,
  showConfig,
});

onMounted(() => {
  // if (internalDatasourceId.value) {
  //   remoteSearchFactTables("");
  //   remoteSearchDimensionTables("");
  // }
});
</script>

<style lang="scss" scoped>
.data-scope-config.app-container {
  background: #e5e6e7 !important;
  width: 100%;
  margin: 0 !important;
  padding: 0 !important;
  min-height: auto !important;
  border-bottom: 1px solid #f0f1f2;
}

.summary-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  padding-right: 0px;
  min-height: 69px;
  background: #fff;
  box-sizing: border-box;
  flex-wrap: wrap;

  .summary-left-col {
    display: flex;
    align-items: center;
    flex-shrink: 0;
    margin-right: 20px;
    padding: 10px 0;
    .title-info {
      display: flex;
      align-items: center;
      gap: 12px;

      .robot-icon {
        width: 32px;
        height: 32px;
        flex-shrink: 0;
      }

      .title {
        font-family: Microsoft YaHei, Microsoft YaHei;
        font-weight: bold;
        font-size: 18px;
        color: #333333;
        line-height: 38px;
      }
    }
  }

  .summary-middle-col {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 12px;
    flex: 1;
    min-width: 0;
    margin-right: 12px;
    flex-wrap: wrap;
    padding: 10px 0;

    .dim-tag {
      width: 93px;
      height: 32px;
      background: #f0f5ff;
      border-radius: 16px;
      font-family: Microsoft YaHei, Microsoft YaHei;
      font-weight: 400;
      font-size: 14px;
      color: #3367fc;
      line-height: 36px;
      text-align: left;
      font-style: normal;
      text-transform: none;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      flex-shrink: 0;
    }

    .summary-form {
      margin: 0;
      :deep(.el-form-item) {
        margin-bottom: 0;
        margin-right: 0;
        display: flex;
        align-items: center;
      }
      :deep(.el-form-item__label) {
        font-weight: 500;
        color: #333;
        padding-right: 8px;
        height: 32px;
        line-height: 32px;
      }
      :deep(.el-form-item__content) {
        height: 32px;
        line-height: 32px;
        display: flex;
        align-items: center;
      }
    }

    .scope-input {
      width: 240px;
      :deep(.el-input__wrapper) {
        cursor: pointer;
        padding-right: 8px;
        height: 32px;
        box-sizing: border-box;
      }
      :deep(.el-input__inner) {
        cursor: pointer;
        font-size: 13px;
        color: #333;
      }
    }
  }

  .summary-right-col {
    display: flex;
    align-items: center;
    gap: 12px;
    height: 100%;
    flex-shrink: 0;

    .scope-selector {
      width: 240px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 32px;
      padding: 0 12px;
      background: #fff;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.3s;
      margin-right: 4px;

      &:hover:not(.disabled) {
        border-color: #409eff;
      }

      &.active {
        border-color: #409eff;
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
      }

      &.disabled {
        cursor: pointer;
        background-color: #fcfcfd;
        color: #c0c4cc;
      }

      .selected-text {
        font-size: 13px;
        color: #333;
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;

        &.placeholder {
          color: #c0c4cc;
        }
      }

      .arrow-icon {
        font-size: 14px;
        color: #c0c4cc;
        margin-left: 8px;
        flex-shrink: 0;
      }
    }

    .extra-slot {
      display: flex;
      align-items: center;
      flex-shrink: 0;
    }
  }
}

.config-panel {
  padding: 20px 16px 20px 20px;
  background: #fcfcfd;
  font-family: Microsoft YaHei, Microsoft YaHei;
  .steps-container {
    display: flex;
    align-items: flex-start;
    gap: 89px;
    margin-bottom: 16px;
    flex-wrap: wrap;

    :deep(.el-form-item) {
      flex: 1;
      min-width: 280px;
      margin-bottom: 10px;
      margin-right: 0;
    }

    :deep(.el-form-item__label) {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }
  }

  .config-select {
    width: 100% !important;
    :deep(.el-input__wrapper) {
      background: #fff;
    }
  }

  .panel-footer {
    display: flex;
    justify-content: flex-end;

    .confirm-btn {
      padding: 0 24px;
      height: 36px;
      font-size: 14px;
      border-radius: 4px;
    }
  }
}

@media screen and (max-width: 1200px) {
  .summary-bar {
    .summary-left-col {
      width: 100%;
    }
    .summary-middle-col {
      width: 100%;
      justify-content: flex-start;
      margin-right: 0;
    }
  }
  .config-panel {
    .steps-container {
      gap: 20px;
      :deep(.el-form-item) {
        flex: 1;
        min-width: calc(33.33% - 20px);
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .config-panel {
    .steps-container {
      :deep(.el-form-item) {
        min-width: 100%;
      }
    }
  }
}

:deep(.el-select .el-select__tags .el-tag) {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}

:deep(.table-header-cell) {
  background-color: #fcfcfd !important;
  color: #333;
  font-weight: 600;
}

:deep(.el-dialog__body) {
  padding: 10px 20px;
}

:deep(.el-select.is-error .el-input__wrapper) {
  box-shadow: 0 0 0 1px #f56c6c inset !important;
}
</style>
