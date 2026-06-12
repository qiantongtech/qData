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
    <!-- Header 字段（type == 3） -->
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

    <!-- 入参字段（type == 1） -->
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
    <!-- 出参字段（type == 2） -->
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
// 接收父组件传递的form对象（其中包含 daAssetApiParamList）和其他属性
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
  // 如果没有子节点，且 columnType 是 Object 或 Array，则重置为 string
  if (["Object", "Array"].includes(row.columnType)) {
    row.columnType = "string";
  }

  return false;
};

// 弹窗状态和表单数据
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
// 计算属性：按 type 分组过滤数据
const headerList = computed(() =>
    daAssetApiParamList.value.filter((item) => Number(item.type) == 3)
);
const inputList = computed(() =>
    daAssetApiParamList.value.filter((item) => Number(item.type) == 1)
);
const outputList = computed(() =>
    daAssetApiParamList.value.filter((item) => Number(item.type) == 2)
);
// 新增操作（顶级记录）
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
//     type: type  // 直接使用数字型
//   };
//   open.value = true;
// };
// 直接新增一行数据到表格
const handleAdd = (type) => {
  const newRow = {
    id: uuidv4(), // 使用当前时间戳作为唯一 ID
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
// 行新增操作（在已有记录下增加子节点）
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
    type: row.type, // 继承父级的 type
    parentId: row.id,
  };
  submitCU(newRow);
  // open.value = true;
};

// 修改操作
const handleUpdate = (type, row) => {
  // 将选中行赋值给弹窗表单数据
  form2.value = { ...row };
  open.value = true;
};
const findPosi = (array, targetId, path = "") => {
  for (let i = 0; i < array.length; i++) {
    const item = array[i];
    if (item.id === targetId) {
      return path + i; // 返回当前节点的索引作为路径
    }
    if (item.daAssetApiParamList && item.daAssetApiParamList.length > 0) {
      // 递归查找子节点
      const childPath = `${path}${i}.daAssetApiParamList.`;
      const result = findPosi(item.daAssetApiParamList, targetId, childPath);
      if (result !== null) {
        return result; // 找到则返回路径
      }
    }
  }
  return null; // 没找到返回 null
};

let inputForm = ref();
let headerForm = ref();
let outputForm = ref();
// 删除操作：递归删除节点（支持树形结构删除）
const handleDelete = (type, row) => {
  if (deleteNodeById(daAssetApiParamList.value, row.id)) {
    daAssetApiParamList.value = [...daAssetApiParamList.value];
  }
};
// 校驗
const validateForms = async () => {
  try {
    const [inputValid, headerValid, outputValid] = await Promise.all([
      inputForm.value.validate(),
      headerForm.value.validate(),
      outputForm.value.validate(),
    ]);

    if (inputValid && headerValid && outputValid) {
      console.log("所有表单校验通过，执行提交操作");
      return true;
    } else {
      console.warn("有表单校验未通过");
      return false;
    }
  } catch (error) {
    console.error("表单校验出错", error);
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

// 递归更新节点（查找后更新）
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

// 在指定 parentId 的位置新增或更新子节点
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

// 新增/编辑提交操作：根据 type 判断操作（顶级或子节点）
const submitCU = (value) => {
  if (Number(value.type) === 3) {
    // 顶级记录
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
    // 入参（1）或出参（2）均采用树形结构处理
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

// 取消操作，清空弹窗表单数据并关闭弹窗
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

// 同步外部传入的 form 数据
watch(
    () => props.form,
    (newVal) => {
      localForm.value = { ...newVal };
      // 同步合并的参数列表（注意：若外部传入的 daAssetApiParamList 更新时，也需要同步过来）
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
    margin: 0; // 去掉默认 margin
    display: flex;
    align-items: center;
    height: 100%; // 让其撑满表格单元格高度
  }
}
</style>
