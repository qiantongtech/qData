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
  <div class="upload-file">
    <el-upload :limit="limit" multiple :action="uploadFileUrl" :before-upload="handleBeforeUpload" :file-list="fileList"
      :on-error="handleUploadError" :on-exceed="handleExceed" :on-success="handleUploadSuccess" :headers="headers"
      class="upload-file-uploader" ref="fileUpload" :data="uploadData" :drag="dragFlag"
      :accept="fileType.map((ext) => '.' + ext).join(',')" :on-remove="handleRemove">
      <el-button type="primary" size="small" icon="Upload" plain>
        {{ t('common.upload.selectFile') }}
      </el-button>
    </el-upload>
    <!-- Upload tips -->
    <div class="el-upload__tip" v-if="isShowTip">
      {{ t('common.upload.supportedFormat') }}
      <b style="color: #f56c6c">{{
        fileType.map((ext) => "." + ext).join(",")
      }}</b>
      {{ t('common.upload.fileTypes') }}，{{ t('common.upload.fileSizeLimit') }}
      <b style="color: #f56c6c">{{ fileSize }}{{ t('common.upload.fileSizeMB') }}</b>
    </div>
  </div>
</template>
<script setup>
import { useI18n } from 'vue-i18n'
import { getToken } from "@/utils/auth";

const { t } = useI18n();

const props = defineProps({
  modelValue: [String, Object, Array],
  limit: {
    type: Number,
    default: 5,
  },
  fileSize: {
    type: Number,
    default: 5,
  },
  // Allow uploading exe, xls, xlsx files
  fileType: {
    type: Array,
    default: () => ["doc", "xls", "xlsx", "ppt", "txt", "pdf", "docx", "exe"], // Add xls and xlsx formats
  },
  isShowTip: {
    type: Boolean,
    default: true,
  },
  platForm: {
    type: String,
    default: "",
  },
  dragFlag: {
    type: Boolean,
    default: true,
  },
  showDelete: {
    type: Boolean,
    default: true, // Delete button is shown by default
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const number = ref(0);
const uploadList = ref([]);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + "/upload"); // Upload file server address
const headers = ref({ Authorization: "Bearer " + getToken() });
const fileList = ref([]);
const uploadData = ref({
  platForm: props.platForm,
});
const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize)
);

watch(
  () => props.modelValue,
  (val) => {
    if (val) {
      let temp = 1;
      const list = Array.isArray(val) ? val : props.modelValue.split(",");
      fileList.value = list.map((item) => {
        if (typeof item === "string") {
          item = { name: item, url: item };
        }
        item.uid = item.uid || new Date().getTime() + temp++;
        return item;
      });
    } else {
      fileList.value = [];
      return [];
    }
  },
  { deep: true, immediate: true }
);

// Verify file type before uploading
function handleBeforeUpload(file) {
  // Verify file type
  if (props.fileType.length) {
    const fileName = file.name.split(".");
    const fileExt = fileName[fileName.length - 1];
    const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
    if (!isTypeOk) {
      proxy.$modal.msgError(t('components.fileUploadbtn.fileFormatError', { fileTypes: props.fileType.join("/") }));
      return false;
    }
  }

  // Check file size
  const fileSize = file.size / 1024 / 1024;
  if (fileSize > props.fileSize) {
    proxy.$modal.msgError(t('components.fileUploadbtn.fileSizeError', { fileSize: props.fileSize }));
    return false;
  }

  // proxy.$modal.loading("Uploading files, please wait...");
  number.value++;
  return true;
}

// The number of files exceeds
function handleExceed() {
  proxy.$modal.msgError(t('components.fileUpload2.exceedLimit', { limit: props.limit }));
}

// Upload failed
function handleUploadError(err) {
  proxy.$modal.msgError(t('components.fileUploadbtn.uploadError'));
}

// Upload success callback
function handleUploadSuccess(res, file) {
  if (res.url) {
    uploadList.value.push({
      name: "/profile/" + res.path + res.filename,
      url: res.url,
    });
    if (res.size) {
      emit("update:fileSize", res.size); // Update file size
    }
    if (res.ext) {
      emit("update:fileExt", res.ext); // Update file extension
    }
    uploadedSuccessfully();
  } else {
    number.value--;
    proxy.$modal.closeLoading();
    proxy.$modal.msgError(res.msg);
    proxy.$refs.fileUpload.handleRemove(file);
    uploadedSuccessfully();
  }
}

// Delete files
function handleDelete(index) {
  fileList.value.splice(index, 1);
  emit("update:modelValue", listToString(fileList.value));
  emit("update:fileExt", null); // Update file extension
  emit("update:fileSize", null); // Update file size
}

// Upload end processing
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value
      .filter((f) => f.url !== undefined)
      .concat(uploadList.value);
    uploadList.value = [];
    number.value = 0;
    emit("update:modelValue", listToString(fileList.value));
    proxy.$modal.closeLoading();
  }
}

// Get file name
function getFileName(name) {
  if (name.lastIndexOf("/") > -1) {
    return name.slice(name.lastIndexOf("/") + 1);
  } else {
    return name;
  }
}

// Convert the object to the specified string delimited
function listToString(list, separator) {
  let strs = "";
  separator = separator || ",";
  for (let i in list) {
    if (list[i].url) {
      strs += list[i].url + separator;
    }
  }
  return strs !== "" ? strs.substr(0, strs.length - 1) : "";
}
function handleRemove() {
  emit("handleRemove"); // Update file extension
}
</script>

<style scoped lang="scss">
.upload-file {
  width: 100%;
}

.upload-file-uploader {
  margin-bottom: 5px;
  padding: 0 !important;
}

.filelistcont {
  margin-left: -40px;
  display: flex;
  align-items: center;

  .filelistcont-name {
    margin-right: 10px;
  }
}
</style>
