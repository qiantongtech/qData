<template>
  <!-- 非结构化数据 -->
  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item label="数据连接名称" prop="datasourceId"
        :rules="[{ required: true, message: '请选择数据连接名称', trigger: 'change' }]">
        <el-select v-model="localForm.datasourceId" placeholder="请选择数据连接名称" @change="handleDatasourceChange" filterable
          :loading="loading" :disabled="!props.isRegister && localForm.id && localForm.createType == '2'">
          <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName" :value="dict.id" />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="数据连接类型" prop="datasourceType">
        <el-input v-model="localForm.datasourceType" disabled />
      </el-form-item>
    </el-col>
  </el-row>

  <el-row :gutter="20">
    <el-col :span="24">
      <el-form-item label="文件路径" prop="filePath" :rules="[{ required: true, message: '请选择文件路径', trigger: 'blur' }]">
        <el-input style="width: 92%" v-model="localForm.filePath" placeholder="请输入文件路径" disabled />
        <el-button type="primary" @click="handleSearch" icon="Search">搜索</el-button>
      </el-form-item>
    </el-col>
  </el-row>

  <el-row :gutter="20" v-if="localForm.filePath">
    <el-col :span="24">
      <el-descriptions title="" :column="2" border>
        <el-descriptions-item v-for="(item, index) in fileDesc" :key="index" label-class-name="base-label"
          class-name="base-content">
          <template #label>
            <div class="cell-item">{{ item.label }}</div>
          </template>
          <span v-if="item.key == 'size'">{{ (item.value / 1024).toFixed(2) + "KB" }}</span>
          <span v-else>{{ item.value }}</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-col>
  </el-row>

  <el-dialog class="file-dialog" title="选择文件" width="900px" v-model="visibleDialog" draggable destroy-on-close
    :append-to="$refs['app-container']">
    <div class="file-main" v-loading="upload.isUploading">
      <div class="head">
        <el-upload ref="uploadRef" :limit="1" :headers="upload.headers" :action="upload.url"
          :disabled="upload.isUploading" :data="uploadData" :before-upload="handleBeforeUpload"
          :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :on-error="handleUploadError"
          :show-file-list="false">
          <el-button type="primary" size="small">上传文件</el-button>
        </el-upload>
        <div class="back">
          <el-text class="back-btn" type="primary" @click="handleBack">
            <el-icon>
              <Back />
            </el-icon>
            <span style="margin-left: 5px">返回</span>
          </el-text>
          <div class="catalogue">
            <!-- 默认展示根目录 -->
            <el-text type="primary" @click="handleCatalogue('/')">
              <span class="catalogue-text">{{ localForm.datasourceName }}</span>
            </el-text>
            <span class="catalogue-split" v-if="catalogues.length != 0"> / </span>
            <el-text type="primary" @click="handleCatalogue(item)" v-for="(item, index) in catalogues" :key="item">
              <span class="catalogue-text">{{ item }}</span> <span class="catalogue-split"
                v-if="index != catalogues.length - 1"> / </span>
            </el-text>
          </div>
        </div>
      </div>
      <!-- :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" -->
      <el-table height="380px" v-loading="fileListLoading" :data="currentPageData" row-key="id"
        @selection-change="handleSelectionChange" @row-click="handleRowClick">
        <el-table-column type="selection" width="55" :selectable="selectable" />
        <el-table-column label="文件名" prop="name" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            <div class="fileName">
              <img v-if="scope.row.directory" src="../../../../assets/da/asset/folder.svg" alt="" />
              <img v-else src="../../../../assets/da/asset/file.svg" alt="" style="width: 12px;height: 12px;margin-right: 5px;" />
              <span>{{ scope.row.name || "-" }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="文件大小" prop="size" :show-overflow-tooltip="{ effect: 'light' }" align="left">
          <template #default="scope">
            <span>{{ scope.row.directory ? "-" : (scope.row.size / 1024).toFixed(2) + "KB" || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" prop="lastModified" :show-overflow-tooltip="{ effect: 'light' }" align="left">
          <template #default="scope">
            {{ scope.row.lastModified || "-" }}
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="fileList.length > 0" :total="fileList.length" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/dataSource/dataSource.js";
import { getFileList } from "@/api/da/asset/asset.js";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user.js";
const userStore = useUserStore();
const emit = defineEmits(["update:form"]);
const { proxy } = getCurrentInstance();
const props = defineProps({
  form: { type: Object, default: () => ({}) },
  isRegister: Boolean
});
const queryParams = reactive({ pageNum: 1, pageSize: 10 });
const currentPageData = computed(() => {
  const startIndex = (queryParams.pageNum - 1) * queryParams.pageSize;
  const endIndex = startIndex + queryParams.pageSize;
  return fileList.value.slice(startIndex, endIndex);
});

/*** 上传文件参数 */
const upload = reactive({
  isUploading: false,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/da/daDatasource/file",
  fileSize: 50,
});
const uploadData = computed(() => {
  return {
    datasourceId: localForm.value.datasourceId,
    path: currPath.value,
  };
});
const handleBeforeUpload = () => {
  //   if (upload.fileSize) {
  //     const isLt = file.size / 1024 / 1024 < upload.fileSize;
  //     if (!isLt) {
  //       proxy.$modal.msgWarning(`上传文件大小不能超过 ${upload.fileSize} MB!`);
  //       return false;
  //     }
  //   }
  return true;
};
/**文件上传中处理 */
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};
// 上传失败
function handleUploadError(err) {
  console.log(err, "err");
  upload.isUploading = false;
  proxy.$modal.msgWarning("上传文件失败，请联系管理员");
}
/** 文件上传成功处理 */
const handleFileSuccess = (response, file) => {
  console.log(response, "response");
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  if (response.code == 200) {
    proxy.$modal.msgSuccess("上传结果：" + response.msg);
  } else {
    proxy.$modal.msgWarning("上传结果：" + response.msg);
  }
  getList();
};
const createTypeList = ref([]); // 数据源列表
let loading = ref(false);
const getDatasourceList = async () => {
  try {
    loading.value = true;
    const response = await listDaDatasourceNoKafkaByProjectCode({
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
    });
    createTypeList.value = response.data.filter((item) => item.datasourceType == "HDFS" || item.datasourceType == "FTP" || item.datasourceType == "OSS-ALIYUN");
  } finally {
    loading.value = false;
  }
};

const localForm = ref({ ...props.form });

// 同步 props.form 到 localForm

getDatasourceList();

// 数据源变化时
const handleDatasourceChange = async (id) => {
  const selected = createTypeList.value.find((item) => item.id == id);
  if (!selected) return;
  const { datasourceType, datasourceConfig, datasourceName } = selected;
  const config = JSON.parse(datasourceConfig);

  Object.assign(localForm.value, {
    datasourceType,
    datasourceName,
    dbname: config.dbname,
    datasourceId: id,
    filePath: "",
  });
  emit("update:form", localForm.value);
};

const fileDesc = ref([
  {
    key: "name",
    label: "文件名",
    value: "-",
  },
  {
    key: "type",
    label: "文件类型",
    value: "-",
  },
  {
    key: "size",
    label: "文件大小",
    value: "-",
  },
  {
    key: "path",
    label: "文件路径",
    value: "-",
  },
  {
    key: "createTime",
    label: "创建时间",
    value: "-",
  },
  {
    key: "lastModified",
    label: "修改时间",
    value: "-",
  },
  {
    key: "time",
    label: "访问时间",
    value: "-",
  },
]);
const getFileDesc = () => {
  fileDesc.value.forEach((item) => {
    if (single.value[item.key] !== undefined && single.value[item.key] != null) {
      item.value = single.value[item.key];
    }
  });
};

const visibleDialog = ref(false);
const handleSearch = () => {
  if (localForm.value.datasourceId) {
    visibleDialog.value = true;
    getList();
  } else {
    return proxy.$modal.msgWarning("未选择源数据库连接，请选择完成后重试");
  }
};
// 返回上级目录
const handleBack = () => {
  if (catalogues.value.length > 1) {
    currPath.value = "/" + catalogues.value.slice(0, catalogues.value.length - 1).join("/");
    getList();
  } else if (catalogues.value.length == 1) {
    currPath.value = "";
    getList();
  }
};
// 切换目录
const handleCatalogue = (path) => {
  if (path == "/") {
    currPath.value = "";
  } else {
    currPath.value = "/" + catalogues.value.slice(0, catalogues.value.indexOf(path) + 1).join("/");
  }
  getList();
};
const currPath = ref("");
const catalogues = computed(() => {
  // eslint-disable-next-line no-useless-escape
  let path = currPath.value.match(/[^\/]+/g) || [];
  return path;
});

const fileList = ref([]);
const fileListLoading = ref(false);
const getList = () => {
  fileListLoading.value = true;
  let param = {
    datasourceId: localForm.value.datasourceId,
    path: currPath.value,
  };
  getFileList(param)
    .then((res) => {
      if (res.code == 200) {
        fileList.value = res.data;
      }
    })
    .finally(() => {
      fileListLoading.value = false;
    });
};
const selectable = (row) => {
  if (single.value.path) {
    return single.value.name == row.name;
  } else {
    return !row.directory;
  }
};
const single = ref({});
const handleSelectionChange = (selection) => {
  if (selection.length == 0) {
    single.value = {};
  } else if (selection.length == 1) {
    single.value = selection[0];
  } else {
    return proxy.$modal.msgWarning("数量限制，只能选择一个文件");
  }
};
const handleRowClick = (row) => {
  if (!row.directory) return;
  currPath.value = row.path;
  getList();
};
const cancel = () => {
  visibleDialog.value = false;
  single.value = {};
  fileList.value = [];
  //   刷新path
  //   currPath.value = "";
};
const submitForm = () => {
  if (!single.value.path) {
    return proxy.$modal.msgWarning(`未选择文件，请选择文件后重试`);
  }
  //   赋值文件路径，文件描述
  Object.assign(localForm.value, {
    filePath: single.value.path,
  });
  emit("update:form", localForm.value);
  getFileDesc();
  cancel();
};
watchEffect(() => {
  localForm.value = { ...props.form };
  console.log(localForm.value, "localForm.value");
  if (props.form.fileInfo) {
    localForm.value.filePath = props.form.fileInfo.path;
    single.value = props.form.fileInfo;
    fileDesc.value.forEach((item) => {
      if (props.form.fileInfo[item.key] !== undefined && props.form.fileInfo[item.key] != null) {
        item.value = props.form.fileInfo[item.key];
      }
    });
  }
});
defineExpose({ fileDesc });
</script>

<style lang="scss" scoped>
.head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;

  .back {
    display: flex;
    align-items: center;

    .back-btn {
      cursor: pointer;
      display: flex;
      align-items: center;
      margin-right: 10px;
    }

    .catalogue {
      color: var(--el-color-primary);
      max-width: 500px;
      overflow: auto hidden;
      white-space: nowrap;

      &::-webkit-scrollbar {
        height: 2px;
      }
    }

    .catalogue-text {
      cursor: pointer;

      &:hover {
        text-decoration: underline;
      }
    }

    .catalogue-split {
      margin: 0 4px 0 2px;
    }
  }
}

:deep(.base-label) {
  width: 200px;

  .cell-item {
    font-weight: 500;
  }
}

.fileName {
  display: flex;
  align-items: center;

  img {
    width: 18px;
    margin-right: 5px;
  }

  .el-icon {
    font-size: 12px;
    color: var(--el-color-primary);
    margin-right: 5px;
  }
}

// 隐藏表头全选选择框
:deep(.el-table__header .el-checkbox) {
  display: none;
}
</style>
<style lang="scss">
.app-container .el-dialog.file-dialog {
  .el-dialog__body {
    height: 500px;

    .file-main {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
