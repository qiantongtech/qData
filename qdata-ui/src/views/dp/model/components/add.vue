<template>
  <el-dialog v-model="visibleDialog" draggable class="large-dialog" destroy-on-close>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="dpModelRef" :model="form" :rules="rules" label-width="110px" @submit.prevent>
      <el-form-item label="创建方式" prop="createType">
        <el-radio-group v-model="form.createType">
          <el-radio v-for="dict in dp_model_create_type" :key="dict.value" :value="dict.value">{{ dict.label
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <div class="h2-title">基础信息</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="中文名称" prop="modelComment">
            <el-input v-model="form.modelComment" placeholder="请输入中文名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="英文名称" prop="modelName">
            <el-input v-model="form.modelName" placeholder="请输入英文名称"
              @input="convertToUpperCase('modelName', form.modelName)" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="逻辑模型类目" prop="catCode">
            <!-- <el-input v-model="form.contact" placeholder="请输入联系人" /> -->
            <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID" placeholder="请选择逻辑模型类目"
              check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系人" prop="contact">
            <el-tree-select filterable v-model="form.contact" :data="userList" :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }" value-key="ID" placeholder="请选择联系人" check-strictly @change="handleContactChange" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-model="form.contactNumber" placeholder="请输入联系电话" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="type != 3">
        <el-col :span="12">
          <el-form-item label="标准类型" prop="description">
            <el-select class="el-form-input-width" v-model="form.documentType" placeholder="请选择类型" clearable
              @change="fetchSecondLevelDocs" style="width: 100%;">
              <el-option v-for="dict in dp_document_type" :key="dict.value" :label="dict.label"
                :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="标准登记" prop="documentId">
            <el-select class="el-form-input-width" v-model="form.documentId" placeholder="请选择标准进行绑定"
              style="width: 100%;" clearable>
              <el-option v-for="doc in secondLevelDocs" :key="doc.value" :label="doc.label" :value="doc.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio v-for="dict in dp_model_status" :key="dict.value" :value="dict.value">{{ dict.label
              }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>

      <div v-if="form.createType == 2">
        <el-divider content-position="center">
          <span class="blue-text">数据源</span>
        </el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库连接" prop="datasourceId" :rules="[
              {
                required: true,
                message: '请选择数据库连接',
                trigger: 'change',
              },
            ]">
              <el-select v-model="form.datasourceId" placeholder="请选择数据连接" @change="handleDatasourceChange" filterable>
                <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                  :value="dict.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="datasourceType">
              <el-input v-model="form.datasourceType" placeholder="请输入数据库类型" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库地址" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入数据库类型" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选择表" prop="tableName" :rules="[
              { required: true, message: '表不能为空', trigger: 'change' },
            ]">
              <el-select v-model="form.tableName" placeholder="请选择表" @change="handleChange(true)" filterable>
                <el-option v-for="item in TablesByDataSource" :key="item.tableName" :label="item.tableName"
                  :value="item.tableName" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>


    <div class="h2-title">属性字段</div>
    <el-button style="margin-bottom: 5px;margin-top: 10px;" type="primary" plain @click="handleAdd" size="small"
      @mousedown="(e) => e.preventDefault()">
      <i class="iconfont-mini icon-xinzeng mr5"></i>新增
    </el-button>
    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column label="编号" type="index" align="left" width="60" />
      <el-table-column v-for="column in columns" :key="column.prop" :prop="column.prop" :label="column.label"
        :width="column.width" :align="column.align" :show-overflow-tooltip="{ effect: 'light' }">
        <template v-if="column.prop === 'pkFlag'" #default="{ row }">
          <el-switch v-model="row[column.prop]" :active-value="'1'" :inactive-value="'0'" disabled />
        </template>
        <template v-if="column.prop === 'authorityDept'" #default="{ row }">
          {{ getDeptLabel(row) }}
        </template>
        <template v-else-if="column.type === 'button'" #default="{ row, $index }">
          <el-button link type="primary" icon="Edit" @click="editRow(row, $index)">编辑</el-button>
          <el-button link type="danger" icon="Delete" @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="confirmDialog">确认</el-button>
      </div>
    </template>
  </el-dialog>

  <columnAdd :visible="addDialog" @update:dialogFormVisible="addDialog = $event" @confirm="handleFormSubmit"
    :deptOptions="deptOptions" :userList="userList" :deptList="deptList" :row="selectedRow" :data="form" />
</template>

<script setup>
const { proxy } = getCurrentInstance();
import {
  listDpDocument,
} from "@/api/dp/document/document";

import {
  getDaDatasourceList,
  tableList,
  columnsList,
} from "@/api/dp/model/model";
import columnAdd from "./columnAdd";
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import { getDpModelColumnList } from "@/api/dp/model/model";
const { dp_model_status, dp_model_create_type, dp_document_type } = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type",
  "dp_document_type"
);
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  deptOptions: { type: Array, default: () => [] },
  column_type: { type: Array, default: () => [] },
  userList: { type: Array, default: () => [] },
  deptList: { type: Array, default: () => [] },
  dataList: { type: Object, default: () => { } },
  catCode: { type: Object, default: () => { } },
  type: { type: String, default: "" },
});
let loading = ref(false);
watch(
  () => props.visible,
  (newVal) => {
    getDaDatasourceListList();
    if (newVal) {

      if (props.dataList && props.dataList.id) {
        Object.assign(form.value, props.dataList);
        form.value.documentId = Number(form.value.documentId) || '';
        fetchSecondLevelDocs(form.value.documentType, true);
        form.value.contact = Number(form.value.contact) || '';
        form.value.createType = '1';
        if (form.value.createType == 2) {
          getTablesByDataSourceIdList();
          form.value.tableName = form.value.modelName;
          handleChange(false);
        } else {
          fetchDpModelColumnList();
          form.value.tableName = form.value.modelName;
        }
        if (form.value.documentId == -1) {
          form.value.documentId = null;
        }
      } else {
        form.value = {
          documentId: '',
          modelName: "",
          modelComment: "",
          createType: "1",
          catCode: props.catCode,
          contact: "",
          contactNumber: "",
          description: "",
          dataConnection: "",
          dbType: "",
          dbAddress: "",
          dataTable: "",
          datasourceName: "",
          datasourceType: "",
          datasourceConfig: "",
          ip: "",
          port: "",
          status: "0",
          datasourceId: "",
          datasourceType: "",
          ip: "",
          tableName: "",
        };
        TablesByDataSource.value = [];
        tableData.value = [];
        tableData.value = [];
      }
    }
  }
);

let secondLevelDocs = ref([]);
const btnloading = ref(false);

const fetchSecondLevelDocs = async (type, preserveSelection = false) => {
  if (!type) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = '';
    }
    return;
  }

  try {
    btnloading.value = true;
    const res = await listDpDocument({ type });
    secondLevelDocs.value = (res.data.rows || []).map(d => ({
      label: d.name,
      value: d.id,
    }));

    // 只有在不是保留选择的情况下才清空
    if (!preserveSelection) {
      form.value.documentId = '';
    }
  } catch (error) {
    secondLevelDocs.value = [];
    if (!preserveSelection) {
      form.value.documentId = '';
    }
  } finally {
    btnloading.value = false;
  }
}


let createTypeList = ref();
const getDaDatasourceListList = async () => {
  try {
    const response = await getDaDatasourceList();
    createTypeList.value = response.data;
  } catch (error) {
    console.error("请求失败:", error);
  }
};
// 表
let TablesByDataSource = ref([]);
const getTablesByDataSourceIdList = async () => {
  try {
    const response = await tableList(form.value.datasourceId);
    TablesByDataSource.value = response.data;
  } catch (error) { }
};
const fetchDpModelColumnList = async () => {
  try {
    loading.value = true;
    console.log("props.dataList.id", form.value.id);
    const response = await getDpModelColumnList({ modelId: form.value.id }); // 传递 `form` 数据
    tableData.value = response.data;
    loading.value = false;
    console.log(
      "🚀 ~ fetchDpMode111lColumnList ~ 1111   tableData.value :",
      tableData.value
    );
    // 处理返回的数据
  } catch (error) {
    console.error("请求失败:", error);
  }
};
const getColumnByAssetIdList = async (isOld) => {
  loading.value = true;
  const response = await columnsList({
    modelId: form.value.id,
    id: form.value.datasourceId,
    tableName: form.value.tableName,
    type: form.value.datasourceType,
    isOld: isOld,
  });
  tableData.value = response.data;
  loading.value = false;
  console.log(
    "🚀 ~ getColumnByAssetIdList ~  2222response.data:",
    response.data
  );
};
const handleDatasourceChange = (value) => {
  const selectedDatasource = createTypeList.value.find(
    (item) => item.id === value
  );
  if (selectedDatasource) {
    form.value.tableName = "";
    TablesByDataSource.value = [];
    tableData.value = [];
    form.value.ip = selectedDatasource.ip;
    form.value.datasourceConfig = selectedDatasource.datasourceConfig;
    form.value.datasourceType = selectedDatasource.datasourceType;
    form.value.datasourceName = selectedDatasource.datasourceName;
    form.value.port = selectedDatasource.port;
    getTablesByDataSourceIdList();
  }
};
const handleChange = (isOld) => {
  const table = TablesByDataSource.value.find(
    (item) => item.tableName == form.value.tableName
  );
  if (table) {
    form.value.modelComment = table.tableComment;
    form.value.modelName = table.tableName;
  }
  tableData.value = [];

  getColumnByAssetIdList(isOld);
};
let addDialog = ref(false);
let selectedRow = ref({});
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue); // 使用 emit 触发父组件更新
  },
});

const emit = defineEmits(["update:dialogFormVisible", "confirm", "submit"]);
const form = ref({
  modelName: "",
  modelComment: "",
  catCode: "",
  createType: "1",
  contact: "",
  contactNumber: "",
  description: "",
  dataConnection: "",
  dbType: "",
  dbAddress: "",
  dataTable: "",
  status: "0",
});

const rules = ref({
  modelComment: [
    { required: true, message: "表中文名称表不能为空", trigger: "blur" },
  ],
  modelName: [
    { required: true, message: "英文名称不能为空", trigger: "blur" },
    {
      pattern: /^[A-Za-z][A-Za-z0-9_]*$/,
      message: "表名只能包含字母、数字和下划线，且必须以字母开头",
      trigger: "blur",
    },
  ],
  // status: [{ required: true, message: "发布状态不能为空", trigger: "change" }],
  catCode: [{ required: true, message: "逻辑模型类目不能为空", trigger: "change" }],
  // documentType: [
  //   {
  //     validator: (rule, value, callback) => {
  //       if (value && !form.value.documentId) {
  //         callback(new Error('请选择标准登记'));
  //       } else {
  //         callback();
  //       }
  //     },
  //     trigger: 'change'
  //   }
  // ],
  // documentId: [
  //   {
  //     validator: (rule, value, callback) => {
  //       if (form.value.documentType && !value) {
  //         callback(new Error('请选择标准登记'));
  //       } else {
  //         callback();
  //       }
  //     },
  //     trigger: 'change'
  //   }
  // ]
});
const tableData = ref([]);

const columns = ref([
  {
    prop: "dataElemName",
    label: "关联标准",
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "cnName",
    label: "中文名称",
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "engName",
    label: "英文名称",
    align: "left",
    width: "250",
    showOverflowTooltip: true,
  },
  {
    prop: "description",
    label: "描述",
    align: "left",
    showOverflowTooltip: true,
    width: "250",
  },
  {
    prop: "columnType",
    label: "数据类型",
    align: "center",
    width: "100",
    showOverflowTooltip: true,
  },
  { prop: "columnLength", label: "属性长度", width: "80", align: "center" },
  { prop: "pkFlag", label: "是否主键", width: "80", align: "center" },
  { type: "button", label: "操作", width: "150", align: "center" },
]);
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
function getDeptLabel(row) {
  // 递归查找树形结构中匹配的节点
  const findLabel = (tree) => {
    for (let node of tree) {
      if (node.id == row.authorityDept) {
        console.log("node", node);

        return node.label;
      }
      if (node.children) {
        const found = findLabel(node.children);
        if (found) return found;
      }
    }
    return null;
  };
  return findLabel(props.deptList) || "-";
}
//表字段的新增
function handleFormSubmit(formData) {
  console.log("提交的表单数据:", formData);
  if (formData.index !== undefined && formData.index !== null) {
    // 如果存在 index，则直接修改对应索引的数据
    tableData.value[formData.index] = { ...formData };
    console.log("数据已修改:", tableData.value[formData.index]);
  } else {
    // 如果没有 index，则新增数据
    tableData.value.push({ ...formData });
    console.log("新数据已新增:", formData);
  }

  console.log("当前表格数据:", tableData.value);
}

function handleAdd() {
  selectedRow.value = {};
  addDialog.value = true;
  return
  proxy.$refs["dpModelRef"].validate((valid) => {
    if (valid) {
      selectedRow.value = {};
      addDialog.value = true;
    } else {
      proxy.$message.warning("添加失败，基本信息填写完整后才能继续操作");
    }
  });
}

const editRow = (row, i) => {
  selectedRow.value = {};
  selectedRow.value = { ...row, index: i };
  addDialog.value = true;
};

const deleteRow = (row) => {
  const index = tableData.value.indexOf(row);
  if (index !== -1) {
    tableData.value.splice(index, 1);
  }
};

const closeDialog = () => {
  form.value = {
    modelName: "",
    modelComment: "",
    catCode: "",
    createType: "1",
    contact: "",
    contactNumber: "",
    description: "",
    dataConnection: "",
    dbType: "",
    dbAddress: "",
    dataTable: "",
  };
  tableData.value = [];
  proxy.resetForm("dpModelRef");
  emit("update:visible", false);
};

const confirmDialog = () => {
  if (!tableData.value || tableData.value.length === 0) {
    proxy.$message.warning("操作失败，请添加属性字段");
    return;
  }

  proxy.$refs["dpModelRef"].validate((valid) => {
    if (valid) {
      console.log('form.value', form.value);
      if (!form.value.id) {

        emit("update:visible", false);
        emit("confirm", {
          form: { ...form.value, documentId: form.value.documentId || -1, },
          tableData: tableData.value,
        });
      } else {
        const updatedTableData = tableData.value.map((item) => ({
          ...item,
          modelId: form.value.id,
        }));
        emit("confirm", { form: { ...form.value, documentId: form.value.documentId || -1, }, tableData: updatedTableData, modelId: form.value.id, });
      }
      closeDialog();
    }
  });
};
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

.dialog {
  min-height: 300px;
  max-height: 900px;
  overflow: auto;
}
</style>
