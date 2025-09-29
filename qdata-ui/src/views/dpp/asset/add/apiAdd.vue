<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item label="应用名称" prop="daAssetApi.appName" :rules="[
        { required: true, message: '请输入资产描述', trigger: 'blur' },
      ]">
        <el-input v-model="localForm.daAssetApi.appName" placeholder="请输入应用名称" />
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item label="开发者" prop="daAssetApi.developerName" :rules="[
        { required: true, message: '请输入资产描述', trigger: 'blur' },
      ]">
        <el-input v-model="localForm.daAssetApi.developerName" placeholder="请输入开发者" />
      </el-form-item>
    </el-col>
  </el-row>

  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item label="服务地址" prop="daAssetApi.url" :rules="[
        { required: true, message: '请输入资产描述', trigger: 'blur' },
      ]">
        <el-input v-model="localForm.daAssetApi.url" placeholder="请输入服务地址" />
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item label="请求类型" prop="daAssetApi.httpMethod" :rules="[
        { required: true, message: '请输入请求类型', trigger: 'blur' },
      ]">
        <el-select v-model="localForm.daAssetApi.httpMethod" placeholder="请选择请求类型">
          <el-option v-for="dict in da_asset_api_method" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
    </el-col>
  </el-row>

  <div class="tableForm">
    <!-- Header 字段（type == 3） -->
    <el-form :model="{ headerList }" :rules="rules" ref="headerForm" label-width="0">

      <div class="3-text">
        Header 字段
        <el-link type="primary" class="add-link" icon="el-icon-circle-plus-outline" @click="handleAdd(3)">
          新增参数
        </el-link>
      </div>
      <el-table :data="headerList" row-key="id" border default-expand-all
        :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
        <el-table-column label="序号" width="100" align="left" fixed="left">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="键" fixed="left" align="left" prop="name" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`headerList[${findPosi(headerList, row.id)}].name`" :rules="rules.name">
              <el-input v-model="row.name" placeholder="请输入键名" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="描述" fixed="left" align="left" prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`headerList[${findPosi(headerList, row.id)}].remark`" :rules="rules.fieldDefault">
              <el-input v-model="row.remark" placeholder="请输入描述" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="值" fixed="left" align="left" prop="defaultValue"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`headerList[${findPosi(headerList, row.dataSculptor)}].defaultValue`"
              :rules="rules.defaultValue">
              <el-input v-model="row.defaultValue" placeholder="请输入默认值" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleUpdate(3, row)">修改</el-button>
            <el-button type="danger" link @click="handleDelete(3, row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </el-form>

    <!-- 入参字段（type == 1） -->
    <el-form :model="{ inputList }" :rules="rules" ref="inputForm" label-width="0">

      <div class="3-text">
        入参字段
        <el-link type="primary" class="add-link" icon="el-icon-circle-plus-outline" @click="handleAdd(1)">
          新增参数
        </el-link>
      </div>

      <el-table :data="inputList" class="tableStyle" row-key="id" border default-expand-all
        :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">

        <el-table-column label="序号" width="100" align="left" fixed="left">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>

        <el-table-column label="参数名称" fixed="left" align="left" prop="name"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`inputList[${findPosi(inputList, row.id)}].name`" :rules="rules.name">
              <el-input v-model="row.name" placeholder="请输入参数名称" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="描述" fixed="left" align="left" prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`inputList[${findPosi(inputList, row.id)}].remark`" :rules="rules.fieldDefault">
              <el-input v-model="row.remark" placeholder="请输入描述" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="是否为空" fixed="left" align="left" prop="requestFlag"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`inputList[${findPosi(inputList, row.id)}].requestFlag`" :rules="rules.requestFlag">
              <el-checkbox v-model="row.requestFlag" :true-label="'1'" :false-label="'0'" </el-checkbox>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="参数类型" fixed="left" align="left" prop="columnType"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`inputList[${findPosi(inputList, row.id)}].columnType`" :rules="rules.columnType">
              <el-select v-model="row.columnType" placeholder="请选择字段类型">
                <el-option v-for="dict in da_asset_api_column_type" :key="dict.value" :label="dict.label"
                  :value="dict.value" :disabled="hasChildren(row) && !['Object', 'Array'].includes(dict.value)" />
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="示例值" fixed="left" align="left" prop="exampleValue"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`inputList[${findPosi(inputList, row.id)}].exampleValue`" :rules="rules.fieldDefault">
              <el-input v-model="row.fieldDefault" placeholder="请输入示例值" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="默认值" fixed="left" align="left" prop="defaultValue"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`inputList[${findPosi(inputList, row.id)}].defaultValue`" :rules="rules.defaultValue">
              <el-input v-model="row.defaultValue" placeholder="请输入默认值" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="{ row }">
            <el-button link type="primary" icon="icon-xinzeng" @click="handleAddRow(1, row)">新增</el-button>
            <el-button type="danger" link @click="handleDelete(1, row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </el-form>

    <!-- 出参字段（type == 2） -->
    <el-form :model="{ outputList }" :rules="rules" ref="outputForm" label-width="0">

      <div class="3-text">
        出参字段
        <el-link type="primary" class="add-link" icon="el-icon-circle-plus-outline" @click="handleAdd(2)">
          新增参数
        </el-link>
      </div>

      <el-table :data="outputList" row-key="id" border default-expand-all
        :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
        <el-table-column label="序号" width="100" align="left" fixed="left">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>

        <el-table-column label="参数名称" fixed="left" align="left" prop="name"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`outputList[${findPosi(outputList, row.id)}].name`" :rules="rules.name">
              <el-input v-model="row.name" placeholder="请输入参数名称" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="描述" fixed="left" align="left" prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`outputList[${findPosi(outputList, row.id)}].remark`" :rules="rules.fieldDefault">
              <el-input v-model="row.remark" placeholder="请输入描述" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="数据类型" fixed="left" align="left" prop="columnType"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-select v-model="row.columnType" placeholder="请选择字段类型">
              <el-option v-for="dict in da_asset_api_column_type" :key="dict.value" :label="dict.label"
                :value="dict.value" :disabled="hasChildren(row) && !['Object', 'Array'].includes(dict.value)" />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column label="示例值" fixed="left" align="left" prop="exampleValue"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="{ row, $index }">
            <el-form-item :prop="`outputList[${findPosi(outputList, row.id)}].exampleValue`"
              :rules="rules.fieldDefault">
              <el-input v-model="row.exampleValue" placeholder="请输入示例值" />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="{ row }">
            <el-button link type="primary" icon="icon-xinzeng" @click="handleAddRow(2, row)">新增</el-button>
            <el-button type="danger" link @click="handleDelete(2, row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

  </div>
  <!-- 弹窗组件 -->
  <tableDialog v-model="open" dialogTitle="添加字段信息" :visible="open" :formcode="form2" @confirm="submitCU"
    @echoSelected="submitCU" @cancelCU="cancelCU" />
</template>

<script setup>
import { ref, reactive, computed, watch, getCurrentInstance } from "vue";
import tableDialog from "./tableAdd.vue";
import { v4 as uuidv4 } from "uuid";

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
  name: [{ required: true, message: "请输入参数名称", trigger: "blur" }],
  columnType: [
    { required: true, message: "请选择参数类型", trigger: "change" },
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
    th.el-table__cell>.cell {
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

.3-text {
  font-size: 14px;
  margin-bottom: 3px;
  margin: 10px 0;
}

.add-link {
  margin-left: 10px;
  margin: 10px 0;
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
  .el-form-item {
    margin: 0; // 去掉默认 margin
    display: flex;
    align-items: center;
    height: 100%; // 让其撑满表格单元格高度
  }
}
</style>
