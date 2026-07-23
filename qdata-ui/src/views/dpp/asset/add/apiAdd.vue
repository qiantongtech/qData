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
  <el-form-item
      :label="td('dpp.asset.add.api.appName')"
      prop="daAssetApi.appName"
      :rules="[{ required: true, message: td('dpp.asset.add.api.appNameRequired'), trigger: 'blur' }]"
  >
    <el-input
        v-model="localForm.daAssetApi.appName"
        :placeholder="td('dpp.asset.add.api.appNamePlaceholder')"
    />
  </el-form-item>
  <el-form-item
      :label="td('dpp.asset.add.api.developerName')"
      prop="daAssetApi.developerName"
      :rules="[{ required: true, message: td('dpp.asset.add.api.developerNameRequired'), trigger: 'blur' }]"
  >
    <el-input
        v-model="localForm.daAssetApi.developerName"
        :placeholder="td('dpp.asset.add.api.developerNamePlaceholder')"
    />
  </el-form-item>

  <el-form-item
      :label="td('dpp.asset.add.api.serviceUrl')"
      prop="daAssetApi.url"
      :rules="[{ required: true, message: td('dpp.asset.add.api.serviceUrlRequired'), trigger: 'blur' }]"
  >
    <el-input v-model="localForm.daAssetApi.url" :placeholder="td('dpp.asset.add.api.serviceUrlPlaceholder')" />
  </el-form-item>
  <el-form-item
      :label="td('dpp.asset.add.api.httpMethod')"
      prop="daAssetApi.httpMethod"
      :rules="[{ required: true, message: td('dpp.asset.add.api.httpMethodRequired'), trigger: 'blur' }]"
  >
    <el-select
        v-model="localForm.daAssetApi.httpMethod"
        :placeholder="td('dpp.asset.add.api.httpMethodPlaceholder')"
    >
      <el-option
          v-for="dict in da_asset_api_method"
          :key="dict.value"
          :label="dict.label"
          :value="dict.value"
      />
    </el-select>
  </el-form-item>

  <div class="tableForm row-full">
    <!-- Header field (type == 3) -->
    <el-form
        :model="{ headerList }"
        :rules="rules"
        ref="headerForm"
        label-width="0"
    >
      <div class="btn-style">
        {{ td('dpp.asset.add.api.headerFields') }}
        <el-button
            plain
            type="primary"
            class="add-link"
            @click="handleAdd(3)"
            @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dpp.asset.add.api.addParam') }}
        </el-button>
      </div>
      <el-table
          :data="headerList"
          row-key="id"
          border
          default-expand-all
          :tree-props="{
          children: 'daAssetApiParamList',
          hasChildren: 'hasChildren',
        }"
      >
        <el-table-column :label="td('dpp.asset.add.api.index')" width="100" align="left" fixed="left">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
            :label="td('dpp.asset.add.api.key')"
            fixed="left"
            align="left"
            prop="name"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`headerList[${findPosi(headerList, row.id)}].name`"
                :rules="rules.name"
            >
              <el-input v-model="row.name" :placeholder="td('dpp.asset.add.api.keyNamePlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column
            :label="td('common.texts.description')"
            fixed="left"
            align="left"
            prop="remark"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`headerList[${findPosi(headerList, row.id)}].remark`"
                :rules="rules.fieldDefault"
            >
              <el-input v-model="row.remark" :placeholder="td('common.form.descriptionPlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column
            :label="td('dpp.asset.add.api.value')"
            fixed="left"
            align="left"
            prop="defaultValue"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`headerList[${findPosi(headerList, row.dataSculptor)}].defaultValue`"
                :rules="rules.defaultValue"
            >
              <el-input v-model="row.defaultValue" :placeholder="td('dpp.asset.add.api.defaultValuePlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column
            :label="td('common.texts.operation')"
            align="center"
            class-name="small-padding fixed-width"
        >
          <template #default="{ row }">
            <!-- <el-button link type="primary" icon="Edit" @click="handleUpdate(3, row)">{{ t('common.button.update') }}</el-button> -->
            <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(3, row)"
            >{{ td('common.button.delete') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <!-- Input parameter field (type == 1) -->
    <el-form
        :model="{ inputList }"
        :rules="rules"
        ref="inputForm"
        label-width="0"
    >
      <div class="btn-style">
        {{ td('dpp.asset.add.api.inputFields') }}
        <el-button
            plain
            type="primary"
            class="add-link"
            @click="handleAdd(1)"
            @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dpp.asset.add.api.addParam') }}
        </el-button>
      </div>

      <el-table
          :data="inputList"
          class="tableStyle"
          row-key="id"
          border
          default-expand-all
          :tree-props="{
          children: 'daAssetApiParamList',
          hasChildren: 'hasChildren',
        }"
      >
        <el-table-column :label="td('dpp.asset.add.api.index')" width="100" align="left" fixed="left">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>

        <el-table-column
            :label="td('dpp.asset.add.api.paramName')"
            fixed="left"
            align="left"
            prop="name"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`inputList[${findPosi(inputList, row.id)}].name`"
                :rules="rules.name"
            >
              <el-input v-model="row.name" :placeholder="td('dpp.asset.add.api.paramNamePlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('common.texts.description')"
            fixed="left"
            align="left"
            prop="remark"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`inputList[${findPosi(inputList, row.id)}].remark`"
                :rules="rules.fieldDefault"
            >
              <el-input v-model="row.remark" :placeholder="td('common.form.descriptionPlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('dpp.asset.add.api.isNull')"
            fixed="left"
            align="left"
            prop="requestFlag"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`inputList[${findPosi(inputList, row.id)}].requestFlag`"
                :rules="rules.requestFlag"
            >
              <el-checkbox
                  v-model="row.requestFlag"
                  :true-label="'1'"
                  :false-label="'0'"
              >
              </el-checkbox>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column
            :label="td('dpp.asset.add.api.paramType')"
            fixed="left"
            align="left"
            prop="columnType"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`inputList[${findPosi(inputList, row.id)}].columnType`"
                :rules="rules.columnType"
            >
              <el-select v-model="row.columnType" :placeholder="td('dpp.asset.add.api.paramTypePlaceholder')">
                <el-option
                    v-for="dict in da_asset_api_column_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                    :disabled="
                    hasChildren(row) &&
                    !['Object', 'Array'].includes(dict.value)
                  "
                />
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('dpp.asset.add.api.exampleValue')"
            fixed="left"
            align="left"
            prop="exampleValue"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`inputList[${findPosi(inputList, row.id)}].exampleValue`"
                :rules="rules.fieldDefault"
            >
              <el-input v-model="row.fieldDefault" :placeholder="td('dpp.asset.add.api.exampleValuePlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('dpp.asset.add.api.defaultValue')"
            fixed="left"
            align="left"
            prop="defaultValue"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`inputList[${findPosi(inputList, row.id)}].defaultValue`"
                :rules="rules.defaultValue"
            >
              <el-input v-model="row.defaultValue" :placeholder="td('dpp.asset.add.api.defaultValuePlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('common.texts.operation')"
            align="center"
            class-name="small-padding fixed-width"
        >
          <template #default="{ row }">
            <el-button
                link
                type="primary"
                icon="Plus"
                @click="handleAddRow(1, row)"
            >{{ td('common.button.add') }}</el-button
            >
            <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(1, row)"
            >{{ td('common.button.delete') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <!-- Output parameter field (type == 2) -->
    <el-form
        :model="{ outputList }"
        :rules="rules"
        ref="outputForm"
        label-width="0"
    >
      <div class="btn-style">
        {{ td('dpp.asset.add.api.outputFields') }}
        <el-button
            plain
            type="primary"
            class="add-link"
            @click="handleAdd(2)"
            @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dpp.asset.add.api.addParam') }}
        </el-button>
      </div>

      <el-table
          :data="outputList"
          row-key="id"
          border
          default-expand-all
          :tree-props="{
          children: 'daAssetApiParamList',
          hasChildren: 'hasChildren',
        }"
      >
        <el-table-column :label="td('dpp.asset.add.api.index')" width="100" align="left" fixed="left">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
            :label="td('dpp.asset.add.api.paramName')"
            fixed="left"
            align="left"
            prop="name"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`outputList[${findPosi(outputList, row.id)}].name`"
                :rules="rules.name"
            >
              <el-input v-model="row.name" :placeholder="td('dpp.asset.add.api.paramNamePlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('common.texts.description')"
            fixed="left"
            align="left"
            prop="remark"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`outputList[${findPosi(outputList, row.id)}].remark`"
                :rules="rules.fieldDefault"
            >
              <el-input v-model="row.remark" :placeholder="td('common.form.descriptionPlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('dpp.asset.add.api.dataType')"
            fixed="left"
            align="left"
            prop="columnType"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-select v-model="row.columnType" :placeholder="td('dpp.asset.add.api.paramTypePlaceholder')">
              <el-option
                  v-for="dict in da_asset_api_column_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                  :disabled="
                  hasChildren(row) && !['Object', 'Array'].includes(dict.value)
                "
              />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('dpp.asset.add.api.exampleValue')"
            fixed="left"
            align="left"
            prop="exampleValue"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="{ row, $index }">
            <el-form-item
                :prop="`outputList[${findPosi(outputList, row.id)}].exampleValue`"
                :rules="rules.fieldDefault"
            >
              <el-input v-model="row.exampleValue" :placeholder="td('dpp.asset.add.api.exampleValuePlaceholder')" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column
            :label="td('common.texts.operation')"
            align="center"
            class-name="small-padding fixed-width"
        >
          <template #default="{ row }">
            <el-button
                link
                type="primary"
                icon="Plus"
                @click="handleAddRow(2, row)"
            >{{ td('common.button.add') }}</el-button
            >
            <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(2, row)"
            >{{ td('common.button.delete') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, reactive, computed, watch, getCurrentInstance } from "vue";
import { v4 as uuidv4 } from "uuid";

const { td } = useDefaultLang();
// Receive the form object (which contains daAssetApiParamList) and other properties passed by the parent component
const props = defineProps({
  form: Object,
  createTypeList: Array,
});
const emit = defineEmits(["update:form"]);

const { proxy } = getCurrentInstance();
const { da_asset_api_column_type, da_asset_api_method } = proxy.useDict(
    "da_asset_api_column_type",
    "da_asset_api_method"
);
const localForm = ref({ ...props.form });

const daAssetApiParamList = ref(props.form.daAssetApiParamList || []);
const hasChildren = (row) => {
  const hasChild =
      Array.isArray(row.daAssetApiParamList) &&
      row.daAssetApiParamList.length > 0;
  if (hasChild) {
    if (["Object", "Array"].includes(row.columnType)) {
    } else {
      row.columnType = "Object";
    }
    return true;
  }
  // If there are no child nodes and columnType is Object or Array, reset to string
  if (["Object", "Array"].includes(row.columnType)) {
    row.columnType = "string";
  }

  return false;
};

// Pop-up status and form data
let open = ref(false);
const form2 = ref({
  id: "",
  name: "",
  columnType: "",
  requestFlag: "0",
  columnType: "string",
  status: "",
  remark: "",
  type: "",
});
// Computed properties: filter data grouped by type
const headerList = computed(() =>
    daAssetApiParamList.value.filter((item) => Number(item.type) == 3)
);
const inputList = computed(() =>
    daAssetApiParamList.value.filter((item) => Number(item.type) == 1)
);
const outputList = computed(() =>
    daAssetApiParamList.value.filter((item) => Number(item.type) == 2)
);
// Add new operation (top level record)
// const handleAdd = (type) => {
//   form2.value = {
//     id: "",
//     name: '',
//     fieldExtent: '',
//     columnType: '',
//     fieldDefault: '',
//     fieldRequest: '',
//     status: '',
//     remark: "",
//     type: type // Use numeric type directly
//   };
//   open.value = true;
// };
// Add a row of data directly to the table
const handleAdd = (type) => {
  const newRow = {
    id: uuidv4(), // Use current timestamp as unique ID
    name: "",
    fieldExtent: "",
    columnType: "string",
    fieldDefault: "",
    fieldRequest: "",
    status: "",
    remark: "",
    requestFlag: "0",
    type: type,
  };
  submitCU(newRow);
};

const rules = {
  name: [{ required: true, message: td('dpp.asset.add.api.paramNameRequired'), trigger: "blur" }],
  columnType: [
    { required: true, message: td('dpp.asset.add.api.paramTypeRequired'), trigger: "change" },
  ],
};
// Row new operation (add child nodes to existing records)
const handleAddRow = (type, row) => {
  const newRow = {
    id: uuidv4(),
    name: "",
    fieldExtent: "",
    columnType: "string",
    fieldDefault: "",
    fieldRequest: "",
    status: "",
    remark: "",
    requestFlag: "0",
    type: row.type, // Inherit the parent's type
    parentId: row.id,
  };
  submitCU(newRow);
  // open.value = true;
};

// Modify operations
const handleUpdate = (type, row) => {
  // Assign the selected row to the pop-up form data
  form2.value = { ...row };
  open.value = true;
};
const findPosi = (array, targetId, path = "") => {
  for (let i = 0; i < array.length; i++) {
    const item = array[i];
    if (item.id === targetId) {
      return path + i; // Returns the index of the current node as a path
    }
    if (item.daAssetApiParamList && item.daAssetApiParamList.length > 0) {
      // Find child nodes recursively
      const childPath = `${path}${i}.daAssetApiParamList.`;
      const result = findPosi(item.daAssetApiParamList, targetId, childPath);
      if (result !== null) {
        return result; // If found, return the path
      }
    }
  }
  return null; // Not found returns null
};

let inputForm = ref();
let headerForm = ref();
let outputForm = ref();
// Delete operation: recursively delete nodes (supports tree structure deletion)
const handleDelete = (type, row) => {
  if (deleteNodeById(daAssetApiParamList.value, row.id)) {
    daAssetApiParamList.value = [...daAssetApiParamList.value];
  }
};
// Verify
const validateForms = async () => {
  try {
    const [inputValid, headerValid, outputValid] = await Promise.all([
      inputForm.value.validate(),
      headerForm.value.validate(),
      outputForm.value.validate(),
    ]);

    if (inputValid && headerValid && outputValid) {
      console.log("All form validations passed; submitting");
      return true;
    } else {
      console.warn("Some form validations failed");
      return false;
    }
  } catch (error) {
    console.error("Form validation error", error);
    return false;
  }
};

const deleteNodeById = (nodes, idToDelete) => {
  for (let i = 0; i < nodes.length; i++) {
    const node = nodes[i];
    if (node.id === idToDelete) {
      nodes.splice(i, 1);
      return true;
    }
    if (node.daAssetApiParamList && node.daAssetApiParamList.length > 0) {
      if (deleteNodeById(node.daAssetApiParamList, idToDelete)) {
        return true;
      }
    }
  }
  return false;
};

// Update nodes recursively (update after search)
const updateNodeInTree = (tree, node) => {
  for (let i = 0; i < tree.length; i++) {
    if (tree[i].id === node.id) {
      tree[i] = { ...tree[i], ...node };
      return true;
    } else if (
        tree[i].daAssetApiParamList &&
        tree[i].daAssetApiParamList.length
    ) {
      const updated = updateNodeInTree(tree[i].daAssetApiParamList, node);
      if (updated) return true;
    }
  }
  return false;
};

// Add or update a child node at the specified parentId
const buildTree = (tree, parentId, newNode) => {
  tree.forEach((node) => {
    if (node.id === parentId) {
      if (!node.daAssetApiParamList) node.daAssetApiParamList = [];
      const existingIndex = node.daAssetApiParamList.findIndex(
          (child) => child.id === newNode.id
      );
      if (existingIndex !== -1) {
        node.daAssetApiParamList[existingIndex] = {
          ...node.daAssetApiParamList[existingIndex],
          ...newNode,
        };
      } else {
        node.daAssetApiParamList.push(newNode);
      }
    } else if (node.daAssetApiParamList && node.daAssetApiParamList.length) {
      buildTree(node.daAssetApiParamList, parentId, newNode);
    }
  });
};

// Add/edit submission operation: determine the operation (top-level or sub-node) based on type
const submitCU = (value) => {
  if (Number(value.type) === 3) {
    // top record
    const index = daAssetApiParamList.value.findIndex(
        (item) => item.id === value.id
    );
    if (index !== -1) {
      daAssetApiParamList.value[index] = {
        ...daAssetApiParamList.value[index],
        ...value,
      };
    } else {
      daAssetApiParamList.value.push(value);
    }
  } else if (Number(value.type) === 1 || Number(value.type) === 2) {
    // Incoming parameters (1) or outgoing parameters (2) are processed using a tree structure.
    const updated = updateNodeInTree(daAssetApiParamList.value, value);
    if (!updated) {
      if (!value.parentId) {
        daAssetApiParamList.value.push(value);
      } else {
        buildTree(daAssetApiParamList.value, value.parentId, value);
      }
    }
  }
  open.value = false;
};

// Cancel the operation, clear the pop-up form data and close the pop-up window
const cancelCU = () => {
  form2.value = {
    id: "",
    name: "",
    columnType: "",
    fieldExtent: "",
    fieldDefault: "",
    fieldRequest: "",
    status: "",
    remark: "",
    type: "",
  };
  open.value = false;
};

// Synchronize externally incoming form data
watch(
    () => props.form,
    (newVal) => {
      localForm.value = { ...newVal };
      // Synchronize the merged parameter list (note: if the externally passed daAssetApiParamList is updated, it also needs to be synchronized)
      daAssetApiParamList.value = newVal.daAssetApiParamList || [];
    },
    { deep: true }
);
defineExpose({
  validateForms,
});
</script>

<style scoped lang="less">
.tableStyle {
  font-size: 14px;
  margin: 0px !important;

  ::v-deep {
    th.el-table__cell > .cell {
      padding: 0 5px !important;
      font-style: normal;
      text-transform: none;
    }

    .el-table__row {
      .el-table__cell {
        padding: 4px 0 !important;
      }
    }

    .el-table__3-wrapper th {
      padding: 4px 0;
    }
  }
}

.home {
  display: flex;
  flex-direction: column;
  height: 88vh;

  .clearfix {
    width: 100%;
    height: 36px;
    background-color: #f8f8f9;
    display: flex;
    align-items: center;
    padding-left: 10px;
    margin-bottom: 10px;
  }

  .clearfix span {
    display: flex;
    align-items: center;
  }

  .blue-bar {
    background-color: #2666fb;
    width: 5px;
    height: 20px;
    margin-right: 10px;
  }
}

.option-item {
  cursor: pointer;
}

::v-deep.el-select-dropdown__item {
  max-width: 569px !important;
}

.el-input,
.el-select {
  width: 100%;
}

.select-width {
  width: 98%;
}

.add-link {
  margin: 10px;
}

.sort-section {
  font-size: 14px;
  height: 40px;
  margin: 5px 0;
  display: flex;
  justify-content: space-between;
}

.sql-editor {
  height: 300px;
  margin: 10px 10px;
}

.allowDrag {
  cursor: pointer;
}

.sql-editor-container {
  position: relative;
}

.sql-parse-btn-container {
  position: absolute;
  top: -30px;
  right: 10px;
  z-index: 10;
}

.tableForm {
  margin-bottom: 15px;
  .el-form-item {
    margin: 0; // Remove default margin
    display: flex;
    align-items: center;
    height: 100%; // Let it fill the height of the table cell
  }
}
</style>
