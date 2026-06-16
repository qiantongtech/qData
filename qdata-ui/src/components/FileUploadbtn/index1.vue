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
    <div class="upload-file">
        <el-upload :limit="limit" :action="uploadFileUrl" :before-upload="handleBeforeUpload"
            :on-success="handleUploadSuccess" :on-error="handleUploadError" :on-exceed="handleExceed" :headers="headers"
            :data="uploadData" :drag="dragFlag" :file-list="fileList"
            :accept="fileType.map((ext) => '.' + ext).join(',')" :show-file-list="false" :on-remove="handleRemove">
            <el-button type="primary" size="small" icon="Upload" plain>
                {{ t('common.upload.selectFile') }}
            </el-button>
        </el-upload>

        <!-- 上传提示 -->
        <div class="el-upload__tip" v-if="isShowTip">
            {{ t('common.upload.supportedFormat') }}
            <b style="color: #f56c6c">{{ fileType.join(', ') }}</b>
            {{ t('common.upload.fileTypes') }}，{{ t('common.upload.fileSizeLimit') }}
            <b style="color: #f56c6c">{{ fileSize }}{{ t('common.upload.fileSizeMB') }}</b>
        </div>

        <!-- 自定义文件展示 -->
        <ul class="custom-file-list">
            <li v-for="(file, index) in fileList" :key="file.uid" class="file-item">
                <el-icon>
                    <Document />
                </el-icon>
                <span class="file-name" @click="handleView(file)">
                    {{ getFileName(file.name || file.url) }}
                </span>
                <span class="file-date">{{ file.uploadDate || formatDate(file.uid) }}</span>
                <el-icon @click="handleDownload(file)" class="icon-btn">
                    <Download />
                </el-icon>
                <el-icon v-if="showDelete" @click="handleRemove(file)" class="icon-btn">
                    <Delete />
                </el-icon>
            </li>
        </ul>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
import { Document, Download, Delete } from '@element-plus/icons-vue'

const { t } = useI18n();

const props = defineProps({
    modelValue: [Array, String],
    limit: { type: Number, default: 5 },
    fileSize: { type: Number, default: 5 },
    fileType: { type: Array, default: () => ['doc', 'docx', 'pdf', 'xls', 'xlsx', 'ppt', 'txt'] },
    isShowTip: { type: Boolean, default: true },
    dragFlag: { type: Boolean, default: true },
    showDelete: { type: Boolean, default: true },
    platForm: { type: String, default: '' },
})

const emit = defineEmits(['update:modelValue'])

const fileList = ref([])
const uploadFileUrl = import.meta.env.VITE_APP_BASE_API + '/upload'
const headers = { Authorization: 'Bearer ' + getToken() }
const uploadData = { platForm: props.platForm }
function handleRemove(file) {
    const index = fileList.value.findIndex(f => f.uid === file.uid)
    if (index !== -1) {
        fileList.value.splice(index, 1)
        emit('update:modelValue', fileList.value.map(f => f.url).join(','))
    }
    emit("handleRemove");
}
watch(
    () => props.modelValue,
    (val) => {
        if (val) {
            let temp = 1
            const list = Array.isArray(val) ? val : val.split(',')
            fileList.value = list.map((item) => {
                if (typeof item === 'string') {
                    item = { name: item, url: item }
                }
                item.uid = item.uid || new Date().getTime() + temp++
                return item
            })
        } else {
            fileList.value = []
        }
    },
    { immediate: true }
)

function handleBeforeUpload(file) {
    const ext = file.name.split('.').pop().toLowerCase()
    if (!props.fileType.includes(ext)) {
        ElMessage.error(t('components.fileUploadbtn.fileFormatError', { fileTypes: props.fileType.join('/') }))
        return false
    }
    const size = file.size / 1024 / 1024
    if (size > props.fileSize) {
        ElMessage.error(t('components.fileUploadbtn.fileSizeError', { fileSize: props.fileSize }))
        return false
    }
    return true
}
// 上传成功回调
function handleUploadSuccess(res, file) {
    if (res.url) {
        fileList.value.push({
            name: "/profile/" + res.path + res.filename,
            url: res.url,
        });
        emit('update:modelValue', fileList.value.map((f) => f.url).join(','))
        if (res.size) {
            emit("update:fileSize", res.size); // 更新文件大小
        }
        if (res.ext) {
            emit("update:fileExt", res.ext); // 更新文件后缀名
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
function handleUploadError() {
    ElMessage.error(t('components.fileUploadbtn.uploadError'))
}

function handleExceed() {
    ElMessage.warning(t('components.fileUploadbtn.exceedLimit', { limit: props.limit }))
}

function handleDelete(index) {
    fileList.value.splice(index, 1)
    emit('update:modelValue', fileList.value.map((f) => f.url).join(','))
}
const base64Encode = (str) => {
    return btoa(
        encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function (match, p1) {
            return String.fromCharCode("0x" + p1);
        })
    );
};
const handleView = (row) => {
    const rpUrl = import.meta.env.VITE_RP_VIEW_URL;
    const baseUrl = import.meta.env.VITE_APP_BASE_API;
    const fullUrl = `${baseUrl}${row.url.trim()}`;
    console.log(fullUrl);
    // 获取屏幕尺寸
    const screenWidth = window.screen.width;
    const screenHeight = window.screen.height;
    // 设置窗口尺寸为屏幕尺寸的一部分，例如60%
    const width = screenWidth * 0.7;
    const height = screenHeight * 0.7;
    // 计算窗口居中时的左上角位置
    const left = (screenWidth - width) / 2;
    const top = (screenHeight - height) / 2;
    // 打开新窗口并居中
    const newWindow = window.open(rpUrl + "/onlinePreview?url=" + encodeURIComponent(base64Encode(fullUrl)), "", `scrollbars=yes, width=${width}, height=${height}, top=${top}, left=${left}`);
    if (window.focus) {
        newWindow.focus();
    }
};
function handleDownload(file) {
    const baseUrl = import.meta.env.VITE_APP_BASE_API;
    const fullUrl = `${baseUrl}${file.url.trim()}`;
    const link = document.createElement('a')
    console.log("🚀 ~ handleDownload ~ fullUrl:", fullUrl)
    link.href = fullUrl
    link.download = getFileName(file.name)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
}

function getFileName(name) {
    return name?.split('/').pop() || ''
}

function formatDate(uid) {
    const d = new Date(uid)
    return d.toISOString().slice(0, 10)
}
</script>

<style scoped>
.upload-file {
    width: 100%;
}

.custom-file-list {
    list-style: none;
    padding: 0;
    margin: 10px 0;
}

.file-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
    gap: 8px;
}

.file-name {
    color: #409eff;
    cursor: pointer;
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.file-date {
    color: #999;
    font-size: 12px;
}

.icon-btn {
    cursor: pointer;
    color: #666;
}

.icon-btn:hover {
    color: #f56c6c;
}
</style>
