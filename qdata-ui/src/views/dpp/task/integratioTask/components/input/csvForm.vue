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
          <el-form-item label="上传附件" prop="taskParams.file" :rules="[
            { required: true, message: '请上传附件', trigger: 'change' },
          ]">
            <FileUploadbtn :limit="1" v-model="form.taskParams.file" :dragFlag="false" :file-type="['csv']"
              :fileSize="50" @handleRemove="handleRemove" :showDelete="!info" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" plain @click="parseExcel" style="margin-left: 60px" :disabled="isButtonDisabled">
            解析csv
          </el-button>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">属性字段</span>
      </el-divider>
      <!-- <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="parseExcel"
              v-hasPermi="['dpp:etl:etltask:add']"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i> 解析Excel
            </el-button>
          </el-col>
        </el-row>
      </div> -->
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
import {
  getNodeUniqueKey,
  getExcelColumn,
  getCsvColumn,
} from "@/api/dpp/task/index.js";
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
const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + "/upload"); // 上传文件服务器地址
/*** 用户导入参数 */
const upload = reactive({
  // 是否禁用上传
  isUploading: false,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
});
// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
const handletaskConfig = (form) => {
  // 找到对应的 id 并更新 ColumnByAssettab 中的相应项
  ColumnByAssettab.value = ColumnByAssettab.value.map((column) => {
    if (column.id == form.id) {
      // 更新匹配 id 的项
      return { ...column, ...form }; // 或者根据需要做其他的合并方式
    }
    return column; // 对于不匹配的项，保持不变
  });
};

let dpModelRefs = ref();
let form = ref({});
const tableFields = ref([]); // 来源表格
// 计算属性：判断按钮是否禁用
const isButtonDisabled = computed(() => {
  return !form.value.taskParams.file;
});
// 获取列数据
const parseExcel = async (id) => {
  if (!form.value.taskParams.file) {
    ElMessage.warning("解析失败，请添加附件");
    return;
  }

  loading.value = true; // Assuming 'loading' is a global loading state variable
  try {
    let res = await getCsvColumn({
      file: form.value.taskParams.file,
    });

    if (res?.data?.csvFile) {
      form.value.taskParams.csvFile = res.data.csvFile;
      ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
        id: index,
        columnName: item,
        columnType: "string",
      }));
      ElMessage.success("CSV 解析成功，请确认属性字段类型！");
    } else {
      ElMessage.warning("CSV 解析失败，未获取到有效数据！");
    }
  } catch (error) {
    ElMessage.warning("解析文件时发生错误，请检查后重试");
    console.error(error);
  } finally {
    loading.value = false; // Ensure loading is turned off regardless of success or failure
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
      return proxy.$message.warning("解析失败，请选择属性字段");
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

  } catch (error) {
    console.error("保存数据失败:", error);
    loading.value = false; // 确保错误发生时也结束加载状态
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
// 上传前校验文件类型
function handleBeforeUpload(file) {
  // 校检文件类型
  let fileType = ["csv"];
  const fileName = file.name.split(".");
  const fileExt = fileName[fileName.length - 1];
  const isTypeOk = fileType.indexOf(fileExt) >= 0;
  if (!isTypeOk) {
    proxy.$modal.msgWarning(`文件格式不正确, 请上传csv格式文件!`);
    return false;
  }
  // 校验文件大小
  const maxSize = 50; // 最大文件大小，单位MB
  const fileSize = file.size / 1024 / 1024;
  if (fileSize > maxSize) {
    proxy.$modal.msgWarning(`大小超出限制，文件大小不能超过 ${maxSize}MB!`);
    return false;
  }
  return true;
}

// 文件删除
function handleRemove() {
  ColumnByAssettab.value = [];
  form.value.taskParams.file = undefined;
}
</script>
<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

.upload-file {
  width: 100%;
}

.upload-file-uploader {
  margin-bottom: 5px;
}

.filelistcont {
  display: flex;
  align-items: center;

  .filelistcont-name {
    margin-right: 10px;
  }
}
</style>
