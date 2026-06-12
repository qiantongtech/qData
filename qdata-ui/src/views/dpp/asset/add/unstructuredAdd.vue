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
  <!-- 非结构化数据 -->
  <qt-form-item
      :label="td('dpp.asset.add.table.datasourceName')"
      prop="datasourceId"
      :rules="[
      { required: true, message: td('dpp.asset.add.table.datasourceNameRequired'), trigger: 'change' },
    ]"
      :tip="{ content: td('dpp.asset.add.table.datasourceNameTip') }"
  >
    <DatasourceList
        v-model="localForm.datasourceId"
        :placeholder="td('dpp.asset.add.table.datasourceNamePlaceholder')"
        @change="handleDatasourceChange"
        filterable
        :loading="loading"
        :disabled="
        !props.isRegister && localForm.id && localForm.createType == '2'
      "
        flag="daAssetUnstructured"
        :project="props.type == '1' ? true : false"
    />
  </qt-form-item>

  <el-form-item :label="td('dpp.asset.add.table.datasourceType')" prop="datasourceType">
    <el-input
        v-model="localForm.datasourceType"
        disabled
        :placeholder="td('dpp.asset.add.table.datasourceTypePlaceholder')"
    />
  </el-form-item>

  <el-form-item
      :label="td('dpp.asset.add.unstructured.filePath')"
      prop="filePath"
      class="row-full"
      :rules="[{ required: true, message: td('dpp.asset.add.unstructured.filePathRequired'), trigger: 'blur' }]"
  >
    <el-input
        style="width: 92%"
        v-model="localForm.filePath"
        :placeholder="td('dpp.asset.add.unstructured.filePathPlaceholder')"
        disabled
    />
    <el-button type="primary" @click="handleSearch" icon="Search"
    >{{ td('dpp.asset.add.unstructured.search') }}</el-button
    >
  </el-form-item>

  <div
      v-if="localForm.filePath"
      class="file-desc-row row-full"
      style="margin-bottom: 20px"
  >
    <el-descriptions title="" :column="2" border>
      <el-descriptions-item
          v-for="(item, index) in fileDesc"
          :key="index"
          label-class-name="base-label"
          class-name="base-content"
      >
        <template #label>
          <div class="cell-item">{{ item.label }}</div>
        </template>
        <span v-if="item.key == 'size'">{{
            (item.value / 1024).toFixed(2) + "KB"
          }}</span>
        <span v-else>{{ item.value }}</span>
      </el-descriptions-item>
    </el-descriptions>
  </div>

  <el-dialog
      class="file-dialog"
      :title="td('dpp.asset.add.unstructured.selectFile')"
      width="900px"
      v-model="visibleDialog"
      draggable
      destroy-on-close
      :append-to="$refs['app-container']"
  >
    <div class="file-main" v-loading="upload.isUploading">
      <div class="head">
        <el-upload
            ref="uploadRef"
            :limit="1"
            :headers="upload.headers"
            :action="upload.url"
            :disabled="upload.isUploading"
            :data="uploadData"
            :before-upload="handleBeforeUpload"
            :on-progress="handleFileUploadProgress"
            :on-success="handleFileSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
        >
          <el-button type="primary" size="small">{{ td('dpp.asset.add.unstructured.uploadFile') }}</el-button>
        </el-upload>
        <div class="back">
          <el-text class="back-btn" type="primary" @click="handleBack">
            <el-icon>
              <Back />
            </el-icon>
            <span style="margin-left: 5px">{{ td('common.button.return') }}</span>
          </el-text>
          <div class="catalogue">
            <!-- 默认展示根目录 -->
            <el-text type="primary" @click="handleCatalogue('/')">
              <span class="catalogue-text">{{ localForm.datasourceName }}</span>
            </el-text>
            <span class="catalogue-split" v-if="catalogues.length != 0">
              /
            </span>
            <el-text
                type="primary"
                @click="handleCatalogue(item)"
                v-for="(item, index) in catalogues"
                :key="item"
            >
              <span class="catalogue-text">{{ item }}</span>
              <span
                  class="catalogue-split"
                  v-if="index != catalogues.length - 1"
              >
                /
              </span>
            </el-text>
          </div>
        </div>
      </div>
      <!-- :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" -->
      <el-table
          height="380px"
          v-loading="fileListLoading"
          :data="currentPageData"
          row-key="id"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" :selectable="selectable" />
        <el-table-column
            :label="td('dpp.asset.add.unstructured.fileName')"
            prop="name"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            <div class="fileName">
              <img
                  v-if="scope.row.directory"
                  src="../../../../assets/da/asset/folder.svg"
                  alt=""
              />
              <img
                  v-else
                  src="../../../../assets/da/asset/file.svg"
                  alt=""
                  style="width: 12px; height: 12px; margin-right: 5px"
              />
              <span>{{ scope.row.name || "-" }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            :label="td('dpp.asset.add.unstructured.fileSize')"
            prop="size"
            :show-overflow-tooltip="{ effect: 'light' }"
            align="left"
        >
          <template #default="scope">
            <span>{{
                scope.row.directory
                    ? "-"
                    : (scope.row.size / 1024).toFixed(2) + "KB" || "-"
              }}</span>
          </template>
        </el-table-column>
        <el-table-column
            :label="td('common.texts.updatedTime')"
            prop="lastModified"
            :show-overflow-tooltip="{ effect: 'light' }"
            align="left"
        >
          <template #default="scope">
            {{ scope.row.lastModified || "-" }}
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="fileList.length > 0"
          :total="fileList.length"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
      />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" size="mini" @click="submitForm"
        >{{ td('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { listDaDatasource } from "@/api/da/dataSource/dataSource.js";
import { getFileList } from "@/api/da/asset/asset.js";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user.js";
import DatasourceList from '@/components/Datasource/List.vue'

const { td } = useDefaultLang();
const userStore = useUserStore();
const emit = defineEmits(["update:form"]);
const { proxy } = getCurrentInstance();
const props = defineProps({
  form: { type: Object, default: () => ({}) },
  isRegister: Boolean,
  type: String,
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
  url: import.meta.env.VITE_APP_BASE_API + "/da/dataSource/file",
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
  proxy.$modal.msgWarning(td('dpp.asset.add.unstructured.uploadFailed'));
}
/** 文件上传成功处理 */
const handleFileSuccess = (response, file) => {
  console.log(response, "response");
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  if (response.code == 200) {
    proxy.$modal.msgSuccess(td('dpp.asset.add.unstructured.uploadResult') + response.msg);
  } else {
    proxy.$modal.msgWarning(td('dpp.asset.add.unstructured.uploadResult') + response.msg);
  }
  getList();
};
const createTypeList = ref([]); // 数据源列表
let loading = ref(false);
// const getDatasourceList = async () => {
//   try {
//     loading.value = true;
//     const response = await listDaDatasource({
//       projectCode: userStore.projectCode,
//       projectId: userStore.projectId,
//     });
//     createTypeList.value = response.data.filter(
//       (item) =>
//         item.datasourceType == "HDFS" ||
//         item.datasourceType == "FTP" ||
//         item.datasourceType == "OSS-ALIYUN"
//     );
//   } finally {
//     loading.value = false;
//   }
// };
const getDatasourceList = async () => {
  try {
    loading.value = true;
    const response = await listDaDatasource({
      pageSize: 9999,
      projectCode: props.type == "1" ? userStore.projectCode : undefined,
      projectId: props.type == "1" ? userStore.projectId : undefined,
      datasourceType: "FTP,OSS-ALIYUN,HDFS",
    });
    createTypeList.value = response.data.rows;
  } finally {
    loading.value = false;
  }
};
const localForm = ref({ ...props.form });

// 同步 props.form 到 localForm

getDatasourceList();

// 数据源变化时
const handleDatasourceChange = async (id,selected) => {
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
    label: td('dpp.asset.add.unstructured.fileName'),
    value: "-",
  },
  {
    key: "type",
    label: td('dpp.asset.add.unstructured.fileType'),
    value: "-",
  },
  {
    key: "size",
    label: td('dpp.asset.add.unstructured.fileSize'),
    value: "-",
  },
  {
    key: "path",
    label: td('dpp.asset.add.unstructured.filePath'),
    value: "-",
  },
  {
    key: "createTime",
    label: td('common.texts.createdTime'),
    value: "-",
  },
  {
    key: "lastModified",
    label: td('dpp.asset.add.unstructured.modifiedTime'),
    value: "-",
  },
  {
    key: "time",
    label: td('dpp.asset.add.unstructured.accessTime'),
    value: "-",
  },
]);
const getFileDesc = () => {
  fileDesc.value.forEach((item) => {
    if (
        single.value[item.key] !== undefined &&
        single.value[item.key] != null
    ) {
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
    return proxy.$modal.msgWarning(td('dpp.asset.add.unstructured.noDatasource'));
  }
};
// 返回上级目录
const handleBack = () => {
  if (catalogues.value.length > 1) {
    currPath.value =
        "/" + catalogues.value.slice(0, catalogues.value.length - 1).join("/");
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
    currPath.value =
        "/" +
        catalogues.value.slice(0, catalogues.value.indexOf(path) + 1).join("/");
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
    return proxy.$modal.msgWarning(td('dpp.asset.add.unstructured.singleFileLimit'));
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
    return proxy.$modal.msgWarning(td('dpp.asset.add.unstructured.noFileSelected'));
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
      if (
          props.form.fileInfo[item.key] !== undefined &&
          props.form.fileInfo[item.key] != null
      ) {
        item.value = props.form.fileInfo[item.key];
      }
    });
  }
});
defineExpose({ fileDesc });
</script>

<style lang="scss" scoped>
.file-desc-row {
  margin-bottom: 15px;
}
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
