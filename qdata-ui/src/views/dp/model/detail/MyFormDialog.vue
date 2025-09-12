<template>
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="large-dialog"
    destroy-on-close
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form
      ref="dpModelRef"
      :model="form"
      :rules="rules"
      label-width="110px"
      @submit.prevent
    >
      <el-form-item label="创建方式" prop="createType">
        <el-radio-group v-model="form.createType">
          <el-radio
            v-for="dict in dp_model_create_type"
            :key="dict.value"
            :value="dict.value"
            >{{ dict.label }}</el-radio
          >
        </el-radio-group>
      </el-form-item>

      <el-divider content-position="center">
        <span class="blue-text">基本信息</span>
      </el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="中文名称" prop="modelComment">
            <el-input
              v-model="form.modelComment"
              placeholder="请输入中文名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="英文名称" prop="modelName">
            <el-input
              v-model="form.modelName"
              placeholder="请输入英文名称"
              @input="convertToUpperCase('modelName', form.modelName)"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio
                v-for="dict in dp_model_status"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="逻辑模型类目" prop="catCode">
            <!-- <el-input v-model="form.contact" placeholder="请输入联系人" /> -->
            <el-tree-select
              v-model="form.catCode"
              :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }"
              value-key="ID"
              placeholder="请选择逻辑模型类目"
              check-strictly
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系人" prop="contact">
            <el-tree-select
              v-model="form.contact"
              :data="userList"
              :props="{
                value: 'userId',
                label: 'nickName',
                children: 'children',
              }"
              value-key="ID"
              placeholder="请选择联系人"
              check-strictly
              @change="handleContactChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input
              v-model="form.contactNumber"
              placeholder="请输入联系电话"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="表描述" prop="description">
            <el-input
              v-model="form.description"
              type="textarea"
              placeholder="请输入描述"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <div v-if="form.createType == 2">
        <el-divider content-position="center">
          <span class="blue-text">数据源</span>
        </el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              label="数据库连接"
              prop="datasourceId"
              :rules="[
                {
                  required: true,
                  message: '请选择数据库连接',
                  trigger: 'change',
                },
              ]"
            >
              <el-select
                v-model="form.datasourceId"
                placeholder="请选择数据连接"
                @change="handleDatasourceChange"
                filterable
              >
                <el-option
                  v-for="dict in createTypeList"
                  :key="dict.id"
                  :label="dict.datasourceName"
                  :value="dict.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="datasourceType">
              <el-input
                v-model="form.datasourceType"
                placeholder="请输入数据库类型"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库地址" prop="ip">
              <el-input
                v-model="form.ip"
                placeholder="请输入数据库类型"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="选择表"
              prop="tableName"
              :rules="[
                { required: true, message: '请选择选择表', trigger: 'change' },
              ]"
            >
              <el-select
                v-model="form.tableName"
                placeholder="请选择表"
                @change="handleChange(true)"
                filterable
              >
                <el-option
                  v-for="item in TablesByDataSource"
                  :key="item.tableName"
                  :label="item.tableName"
                  :value="item.tableName"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>

    <el-divider content-position="center">
      <span class="blue-text">属性字段</span>
    </el-divider>

    <el-button
      style="margin-bottom: 5px"
      type="primary"
      plain
      @click="handleAdd"
      size="small"
      @mousedown="(e) => e.preventDefault()"
    >
      <i class="iconfont-mini icon-xinzeng mr5"></i>新增
    </el-button>
    <el-table
      :data="tableData"
      style="width: 100%"
      height="180px"
      v-loading="loading"
    >
      <el-table-column label="序号" type="index" align="center" width="60" />
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :align="column.align"
        :show-overflow-tooltip="true"
      >
        <template v-if="column.prop === 'pkFlag'" #default="{ row }">
          <el-switch
            v-model="row[column.prop]"
            :active-value="'1'"
            :inactive-value="'0'"
            disabled
          />
        </template>
        <template v-if="column.prop === 'authorityDept'" #default="{ row }">
          {{ getDeptLabel(row) }}
        </template>
        <template
          v-else-if="column.type === 'button'"
          #default="{ row, $index }"
        >
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="editRow(row, $index)"
            >编辑</el-button
          >
          <el-button link type="danger" icon="Delete" @click="deleteRow(row)"
            >删除</el-button
          >
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

  <DialogForm
    :visible="addDialog"
    @update:dialogFormVisible="addDialog = $event"
    @confirm="handleFormSubmit"
    :deptOptions="deptOptions"
    :userList="userList"
    :deptList="deptList"
    :row="selectedRow"
    :data="form"
  />
</template>

<script setup>
const { proxy } = getCurrentInstance();
import {
  getDaDatasourceList,
  tableList,
  columnsList,
} from "@/api/dp/model/dpModel";
import DialogForm from "./FieldFormDialog";
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import { getDpModelColumnList } from "@/api/dp/model/dpModel";
const { dp_model_status, dp_model_create_type } = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type"
);
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  deptOptions: { type: Array, default: () => [] },
  column_type: { type: Array, default: () => [] },
  userList: { type: Array, default: () => [] },
  deptList: { type: Array, default: () => [] },
  dataList: { type: Object, default: () => {} },
  catCode: { type: Object, default: () => {} },
});
let loading = ref(false);
watch(
  () => props.visible,
  (newVal) => {
    getDaDatasourceListList();
    if (newVal) {
      if (props.dataList && props.dataList.id) {
        Object.assign(form.value, props.dataList);
        form.value.contact = Number(form.value.contact);
        if (form.value.createType == 2) {
          getTablesByDataSourceIdList();
          form.value.tableName = form.value.modelName;
          handleChange(false);
        } else {
          fetchDpModelColumnList();
          form.value.tableName = form.value.modelName;
        }
      } else {
        form.value = {
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
const convertToUpperCase = (key, value) => {
  const uppercasedValue = value.replace(/[a-z]/g, (char) => char.toUpperCase());

  form.value[key] = uppercasedValue;

  console.log("🚀 ~ convertToUpperCase ~ form.value[key]:", form.value[key]);
};

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
  } catch (error) {}
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
    { required: true, message: "表中文名称为必填项", trigger: "blur" },
  ],
  modelName: [
    { required: true, message: "请输入英文名称", trigger: "blur" },
    {
      pattern: /^[A-Za-z][A-Za-z0-9_]*$/,
      message: "表名只能包含字母、数字和下划线，且必须以字母开头",
      trigger: "blur",
    },
  ],
  status: [{ required: true, message: "发布状态为必填项", trigger: "change" }],
  catCode: [{ required: true, message: "所属类目为必填项", trigger: "change" }],
});
const tableData = ref([]);

const columns = ref([
  {
    prop: "dataElemName",
    label: "关联标准",
    align: "left",
    width: "100",
    showOverflowTooltip: true,
  },
  {
    prop: "cnName",
    label: "中文名称",
    align: "left",
    showOverflowTooltip: true,
  },
  {
    prop: "engName",
    label: "英文名称",
    align: "left",
    showOverflowTooltip: true,
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
  { prop: "authorityDept", label: "权威部门", width: "100", align: "center" },
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
  proxy.$refs["dpModelRef"].validate((valid) => {
    if (valid) {
      selectedRow.value = {};
      addDialog.value = true;
    } else {
      proxy.$message.error("基本信息填写完整后才能继续操作。");
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
    // 如果tableData为空，显示错误提示
    proxy.$message.error("请选择表字段。");
    return; // 如果没有选择表字段，退出函数
  }

  proxy.$refs["dpModelRef"].validate((valid) => {
    if (valid) {
      if (!form.value.id) {
        emit("update:visible", false);
        emit("confirm", {
          form: form.value,
          tableData: tableData.value,
          modelId: form.value.id,
        });
      } else {
        const updatedTableData = tableData.value.map((item) => ({
          ...item,
          modelId: form.value.id,
        }));
        emit("confirm", { form: form.value, tableData: updatedTableData });
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
