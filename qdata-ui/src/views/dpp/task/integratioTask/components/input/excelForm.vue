<template>
  <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
    showCancelButton :show-close="false" destroy-on-close :close-on-click-modal="false">
    <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading" :disabled="info">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="节点名称" prop="name" :rules="[
            { required: true, message: '请输入节点名称', trigger: 'change' },
          ]">
            <el-input v-model="form.name" placeholder="请输入节点名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="typeName">
            <el-select v-model="form.taskParams.typeName" placeholder="请输入类型" filterable disabled>
              <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
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
          <el-form-item label="上传附件" prop="taskParams.excelFile" :rules="[
            { required: true, message: '请上传附件', trigger: 'change' },
          ]">
            <!-- <FileUploadbtn :limit="1" v-model="form.taskParams.excelFile" :dragFlag="false" :file-type="['xlsx', 'xls']"
              :fileSize="50" @handleRemove="handleRemove" /> -->
            <FileUploadbtn :limit="1" v-model="form.taskParams.excelFile" :dragFlag="false" :fileSize="50"
              @handleRemove="handleRemove" :file-type="['xlsx', 'xls']" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起始行" prop="taskParams.startData" :rules="[
            { required: true, message: '请输入起始行', trigger: 'change' },
          ]">
            <el-input-number :step="1" step-strictly placeholder="请输入起始行" v-model="form.taskParams.startData"
              style="width: 100%" controls-position="right" :min="1" value-on-clear="min" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="起始列" prop="taskParams.startColumn" :rules="[
            { required: true, message: '请输入起始列', trigger: 'change' },
          ]">
            <el-input-number :step="1" step-strictly placeholder="请输入起始列" v-model="form.taskParams.startColumn"
              style="width: 100%" controls-position="right" :min="1" value-on-clear="min" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" plain @click="parseExcel" style="margin-left: 60px" :disabled="isButtonDisabled">
            解析Excel
          </el-button>
        </el-col>
      </el-row>
      <el-divider content-position="center">
        <span class="blue-text">属性字段</span>
      </el-divider>
      <el-table stripe height="310px" v-loading="loadingList" :data="ColumnByAssettab">
        <el-table-column label="序号" type="index" width="80" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段名称" align="left" prop="columnName" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.columnName || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="字段类型" align="left" prop="columnType">
          <template #default="scope">
            {{ scope.row.columnType || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="日期格式" align="left" prop="format">
          <template #default="scope">
            {{ scope.row.format || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="openDialog(scope.row)">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
  <excelUploadDialog :visible="open" title="属性字段编辑" @update:visible="open = $event" @confirm="handletaskConfig"
    :data="row" />
</template>
<script setup>
import { getToken } from "@/utils/auth.js";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey, getExcelColumn } from "@/api/dpp/task/index.js";
import excelUploadDialog from "../excelUpload.vue";
import FileUploadbtn from '@/components/FileUploadbtn/index1.vue'
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user.js";
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: {}
});
const emit = defineEmits(["update", "confirm"]);
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update", newValue);
  },
});
// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref();
// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
// 属性字段修改新增
const handletaskConfig = (form) => {
  ColumnByAssettab.value = ColumnByAssettab.value.map((column) => {
    if (column.id == form.id) {
      return { ...column, ...form };
    }
    return column;
  });
};

let dpModelRefs = ref();
let form = ref({});
const tableFields = ref([]); // 来源表格
// 计算属性：判断按钮是否禁用
const isButtonDisabled = computed(() => {
  console.log(form.value.taskParams.excelFile);
  return (
    !form.value.taskParams.startData ||
    !form.value.taskParams.startColumn ||
    !form.value.taskParams.excelFile
  );
});
// 获取列数据
const parseExcel = async (id) => {
  if (!form.value.taskParams.startData) {
    ElMessage.warning("解析失败，请添加起始行");
    return;
  }
  if (!form.value.taskParams.startColumn) {
    ElMessage.warning("解析失败，请添加起始列");
    return;
  }
  if (!form.value.taskParams.excelFile) {
    ElMessage.warning("解析失败，请添加附件");
    return;
  }
  loadingList.value = true;
  try {
    let res = await getExcelColumn({
      startData: form.value.taskParams.startData,
      startColumn: form.value.taskParams.startColumn,
      excelFile: form.value.taskParams.excelFile,
    });

    if (res?.data?.csvFile) {
      form.value.taskParams.csvFile = res.data.csvFile;
      ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
        id: index,
        columnName: item,
        columnType: "string",
      }));

      ElMessage.success("Excel解析成功，请确认属性字段类型！");
    } else {
      ElMessage.warning("Excel解析失败，未获取到有效数据！");
    }
  } catch (error) {
    if (response.code == 200)
      ElMessage.warning("Excel解析失败，请检查文件格式或内容！");
  } finally {
    loadingList.value = false;
  }
};

const off = () => {
  proxy.resetForm("dpModelRefs");
  // 清空表格字段数据
  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// 保存数据
const saveData = async () => {
  try {
    // 异步验证表单
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;
    if (
      form.value?.taskParams.type == "1" &&
      (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
    ) {
      return proxy.$message.warning("校验未通过，请选择属性字段");
    }
    // 如果没有 code，就调用接口获取唯一的 code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false; // 结束加载状态
      form.value.code = response.data; // 设置唯一的 code
    }
    const taskParams = form.value?.taskParams;
    taskParams.tableFields = ColumnByAssettab.value;
    taskParams.columnsList = ColumnByAssettab.value.map(({ columnName, columnType }) => ({
      colName: columnName,
      dataType: columnType,
    }));
    taskParams.columns = taskParams.tableFields.map((item) => {
      return {
        index: item.id,
        columnName: item.columnName,
        type: item.columnType,
        format: item.format
      };
    });
    emit("confirm", form.value);

  } finally {
    loadingList.value = false;
  }
};
const closeDialog = () => {
  off();
  // 关闭对话框
  emit("update", false);
};

// 监听属性变化
function deepCopy(data) {
  if (data === undefined || data === null) {
    return {}; // 或者返回一个默认值
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    return {}; // 或者返回一个默认值
  }
}
// 监听属性变化
watchEffect(() => {
  if (props.visible) {
    // 数据源
    form.value = deepCopy(props.currentNode.data);
    ColumnByAssettab.value = props.currentNode?.data.taskParams.tableFields;
  } else {
    off();
  }
});
// 文件删除
function handleRemove() {
  ColumnByAssettab.value = [];
  form.value.taskParams.excelFile = undefined;
}
</script>
<style scoped lang="scss">
.blue-text {
  color: #2666fb;
}
</style>
