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
    <el-upload
      multiple
      :action="uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :file-list="fileList"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-success="handleUploadSuccess"
      :show-file-list="false"
      :headers="headers"
      class="upload-file-uploader"
      ref="fileUpload"
      :data="uploadData"
      :drag="dragFlag"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text" style="width: 100%;">{{ t('common.upload.dragOrClick') }}</div>
    </el-upload>
    <!-- Upload tips -->
<!--    <div class="el-upload__tip" v-if="showTip">-->
<!--      Please upload-->
<!--      <template v-if="fileSize"> no larger than <b style="color: #f56c6c">{{ fileSize }}MB</b> </template>-->
<!--      <template v-if="fileType"> The format is 3333333 <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template>-->
<!--      files-->
<!--    </div>-->
    <!-- file list -->
    <transition-group class="upload-file-list el-upload-list el-upload-list--text" name="el-fade-in-linear" tag="ul">
      <!-- <li :key="file.uid" class="el-upload-list__item ele-upload-list__item-content" v-for="(file, index) in fileList">
        <el-link :href="`${baseUrl}${file.url}`" :underline="false" target="_blank">
          <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
        </el-link>
        <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
        <div class="ele-upload-list__item-content-action">
          <el-link :underline="false" @click="handleDelete(index)" type="danger">{{ t('common.button.delete') }}</el-link>
        </div>
      </li> -->
      <li :key="file.uid" class="filelistcont" v-for="(file, index) in fileList">
        <div class="filelistcont-name">
          <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
        </div>
        <div class="ele-upload-list__item-content-action">
          <el-link :underline="false" @click="handleDelete(index)" type="danger">{{ t('common.button.delete') }}</el-link>
        </div>
      </li>
    </transition-group>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { getToken } from "@/utils/auth";

const { t } = useI18n();
const props = defineProps({
  modelValue: [String, Object, Array],
  // Quantity limit
  limit: {
    type: Number,
    default: 5,
  },
  // Size limit(MB)
  fileSize: {
    type: Number,
    default: 5,
  },
  // File types, such as ['png', 'jpg', 'jpeg']
  fileType: {
    type: Array,
    default: () => ["doc", "xls", "ppt", "txt", "pdf", "docx"],
  },
  // Whether to display prompts
  isShowTip: {
    type: Boolean,
    default: true
  },
  // platform parameters
  platForm: {
    type: String,
    default: ""
  },
  // Whether to support drag and drop upload
  dragFlag: {
    type: Boolean,
    default: true
  }
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
  platForm: props.platForm
});
const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize)
);

watch(() => props.modelValue, val => {
  if (val) {
    let temp = 1;
    // First convert the value into an array
    const list = Array.isArray(val) ? val : props.modelValue.split(',');
    // Then convert the array into an object array
    fileList.value = list.map(item => {
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
},{ deep: true, immediate: true });

// Check format and size before uploading
function handleBeforeUpload(file) {
  // Proof file type
  if (props.fileType.length) {
    const fileName = file.name.split('.');
    const fileExt = fileName[fileName.length - 1];
    const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
    if (!isTypeOk) {
      proxy.$modal.msgError(t('components.fileUpload2.fileFormatError', { fileTypes: props.fileType.join("/") }));
      return false;
    }
  }
  // Proof file size
  // if (props.fileSize) {
  //   const isLt = file.size / 1024 / 1024 < props.fileSize;
  //   if (!isLt) {
  //     proxy.$modal.msgError(`Uploaded file size cannot exceed ${props.fileSize} MB!`);
  //     return false;
  //   }
  // }
  proxy.$modal.loading(t('components.fileUpload2.uploading'));
  number.value++;
  return true;
}

// The number of files exceeds
function handleExceed() {
  proxy.$modal.msgError(t('components.fileUpload2.exceedLimit', { limit: props.limit }));
}

// Upload failed
function handleUploadError(err) {
  proxy.$modal.msgError(t('components.fileUpload2.uploadError'));
}

// Upload success callback
function handleUploadSuccess(res, file) {
  if (res.url) {
    uploadList.value.push({ name: '/profile/' + res.path + res.filename, url: res.url });
    if (res.size) {
      emit("update:fileSize", res.size);  // Update file size
    }
    if (res.ext) {
      emit("update:fileExt", res.ext);  // Update file extension
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
  emit("update:fileExt", null);  // Update file extension
  emit("update:fileSize", null);  // Update file size

}

// Upload end processing
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value.filter(f => f.url !== undefined).concat(uploadList.value);
    uploadList.value = [];
    number.value = 0;
    emit("update:modelValue", listToString(fileList.value));
    proxy.$modal.closeLoading();
  }
}

// Get file name
function getFileName(name) {
  // If it is a url, then take the last name. If it is not returned directly,
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
  return strs != '' ? strs.substr(0, strs.length - 1) : '';
}
defineExpose({fileList,getFileName})
</script>

<style scoped lang="scss">
.upload-file {
  width: 100%;
}
.upload-file-uploader {
  margin-bottom: 5px;
}
// .upload-file-list .el-upload-list__item {
//   border: 1px solid #e4e7ed;
//   line-height: 2;
//   margin-bottom: 10px;
//   position: relative;
// }
// .upload-file-list .ele-upload-list__item-content {
//   display: flex;
//   justify-content: space-between;
//   align-items: center;
//   color: inherit;
// }
// .ele-upload-list__item-content-action .el-link {
//   margin-right: 10px;
// }
.filelistcont{
  display: flex;
  align-items: center;
  .filelistcont-name{
    margin-right: 10px;
  }
}
</style>
