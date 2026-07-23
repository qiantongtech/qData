<template>
  <div class="asset-column-select">
    <el-form-item :label="td('dg.sensitiveList.assetName')" :prop="assetProp" :rules="rules.assetId">
      <el-select
        v-model="internalValue.assetId"
        filterable
        remote
        reserve-keyword
        :placeholder="td('dg.sensitiveList.assetNamePlaceholder')"
        :remote-method="remoteMethod"
        :loading="loadingAsset"
        @change="handleAssetChange"
        default-expand-all
        style="width: 100%"
      >
        <el-option
          v-for="item in assetOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </el-form-item>

    <el-form-item
      :label="td('dg.sensitiveList.fieldNameLabel')"
      :prop="columnProp"
      :rules="rules.assetcolumnId"
    >
      <el-select
        v-model="internalValue.assetcolumnId"
        filterable
        :placeholder="td('dg.sensitiveList.fieldNamePlaceholder')"
        :disabled="!internalValue.assetId"
        @change="handleColumnChange"
        style="width: 100%"
      >
        <el-option
          v-for="item in columnOptions"
          :key="item.id"
          :label="item.columnName || '-'"
          :value="item.id"
        />
      </el-select>
    </el-form-item>

    <el-form-item
      :label="td('dg.sensitiveList.dataCategory')"
      :prop="categoryProp"
      :rules="rules.dataCategoryId"
    >
      <el-tree-select
        v-model="internalValue.dataCategoryId"
        :data="dataCategoryList"
        :placeholder="td('dg.sensitiveList.dataCategoryPlaceholder')"
        filterable
        clearable
        check-strictly
        :loading="dataCategoryLoading"
        @change="handleCategoryChange"
        style="width: 100%"
        :props="{ label: 'name', value: 'id', children: 'children' }"
      />
    </el-form-item>
  </div>
</template>

<script setup name="AssetColumnSelect">
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, watch, onMounted, reactive } from "vue";

const { td } = useDefaultLang();
import { listDaAsset } from "@/api/da/asset/asset";
import { listDaAssetColumn } from "@/api/da/asset/assetColumn.js";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({
      assetId: null,
      assetcolumnId: null,
      assetcolumnName: null,
      assetcolumnComment: null,
      dataCategoryId: null,
      assetTableName: null,
      assetTableComment: null,
      assetName: null,
    }),
  },
  assetProp: {
    type: String,
    default: "assetId",
  },
  columnProp: {
    type: String,
    default: "assetcolumnId",
  },
  categoryProp: {
    type: String,
    default: "dataCategoryId",
  },
});

const emit = defineEmits(["update:modelValue", "change"]);

const internalValue = reactive({ ...props.modelValue });

const loadingAsset = ref(false);
const dataCategoryLoading = ref(false);
const assetOptions = ref([]);
const columnOptions = ref([]);
const dataCategoryList = ref([]);

const rules = {
  assetId: [{ required: true, message: td('dg.sensitiveList.assetNameRequired'), trigger: "change" }],
  assetcolumnId: [
    { required: true, message: td('dg.sensitiveList.fieldNameRequired'), trigger: "change" },
  ],
  dataCategoryId: [
    { required: true, message: td('dg.sensitiveList.dataCategoryRequired'), trigger: "change" },
  ],
};

// Search assets remotely
const remoteMethod = (query) => {
  loadingAsset.value = true;
  listDaAsset({ name: query, pageNum: 1, pageSize: 50 })
    .then((res) => {
      assetOptions.value = res.data.rows;
      loadingAsset.value = false;
    })
    .catch(() => {
      loadingAsset.value = false;
    });
};

// Asset changes
const handleAssetChange = (val) => {
  const selectedAsset = assetOptions.value.find((item) => item.id === val);
  if (selectedAsset) {
    internalValue.assetTableName = selectedAsset.tableName;
    internalValue.assetTableComment =
      selectedAsset.comment || selectedAsset.name;
    internalValue.assetName = selectedAsset.name;
  }
  internalValue.assetcolumnId = null;
  internalValue.assetcolumnName = null;
  internalValue.assetcolumnComment = null;
  columnOptions.value = [];
  fetchColumns(val);
  emitUpdate();
};

// Get field list
const fetchColumns = (assetId) => {
  if (!assetId) return;
  listDaAssetColumn({ assetId, pageNum: 1, pageSize: 1000 }).then((res) => {
    columnOptions.value = res.data.rows;
    // If there is already a selected field ID but no field list is loaded, automatic matching can be performed here.
    if (internalValue.assetcolumnId) {
      const selectedColumn = columnOptions.value.find(
        (item) => item.id === internalValue.assetcolumnId
      );
      if (selectedColumn && !internalValue.assetcolumnName) {
        internalValue.assetcolumnName = selectedColumn.columnName;
        internalValue.assetcolumnComment = selectedColumn.columnComment;
      }
    }
  });
};

// Field changes
const handleColumnChange = (val) => {
  const selectedColumn = columnOptions.value.find((item) => item.id === val);
  if (selectedColumn) {
    internalValue.assetcolumnName = selectedColumn.columnName;
    internalValue.assetcolumnComment = selectedColumn.columnComment;
  }
  emitUpdate();
};

// Get the data classification list
const fetchCategoryTree = () => {
  dataCategoryLoading.value = true;
  selectTreeDataCategory()
    .then((res) => {
      const rawData = res?.data || [];
      const processTree = (nodes) => {
        return nodes.map((node) => {
          const newNode = { ...node };
          newNode.disabled = String(node.type) === "1";
          if (node.children && node.children.length > 0) {
            newNode.children = processTree(node.children);
          }
          return newNode;
        });
      };
      dataCategoryList.value = processTree(rawData);
      dataCategoryLoading.value = false;
    })
    .catch(() => {
      dataCategoryLoading.value = false;
    });
};

// Data classification changes
const handleCategoryChange = (val) => {
  const findNode = (list, id) => {
    for (const item of list) {
      if (item.id === id) return item;
      if (item.children) {
        const res = findNode(item.children, id);
        if (res) return res;
      }
    }
    return null;
  };
  const selectedNode = findNode(dataCategoryList.value, val);
  if (selectedNode) {
    internalValue.dataCategoryName = selectedNode.name;
    internalValue.dataCategoryCode = selectedNode.code;
  } else {
    internalValue.dataCategoryName = null;
    internalValue.dataCategoryCode = null;
  }
  emitUpdate();
};

const emitUpdate = () => {
  emit("update:modelValue", { ...internalValue });
  emit("change", { ...internalValue });
};

watch(
  () => props.modelValue,
  (newVal) => {
    Object.assign(internalValue, newVal);
    if (newVal.assetId && !columnOptions.value.length) {
      fetchColumns(newVal.assetId);
    }
  },
  { deep: true }
);

onMounted(() => {
  fetchCategoryTree();
  // Initial loading of the first 50 assets
  remoteMethod("");
});
</script>

<style scoped>
.asset-column-select {
  width: 100%;
}
</style>
