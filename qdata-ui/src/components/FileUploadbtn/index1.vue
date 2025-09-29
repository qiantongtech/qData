<template>
    <div class="upload-file" :class="{ 'has-file': fileList.length > 0 }">
        <el-upload :limit="limit" :action="uploadFileUrl" :before-upload="handleBeforeUpload"
            :on-success="handleUploadSuccess" :on-error="handleUploadError" :on-exceed="handleExceed" :headers="headers"
            :data="uploadData" :drag="dragFlag" :file-list="fileList"
            :accept="fileType.map((ext) => '.' + ext).join(',')" :show-file-list="false" :on-remove="handleRemove">
            <el-button type="primary" size="small" icon="Upload" plain>
                选择文件
            </el-button>
        </el-upload>

        <!-- 上传提示 -->
        <div class="el-upload__tip" v-if="isShowTip">
            仅支持上传
            <b style="color: #f56c6c">{{ fileType.join(', ') }}</b>
            格式的文件，大小不超过
            <b style="color: #f56c6c">{{ fileSize }}MB</b>
        </div>

        <!-- 自定义文件展示 -->
        <ul class="custom-file-list">
            <li v-for="(file, index) in fileList" :key="file.uid" class="file-item">
                <el-icon>
                    <Document />
                </el-icon>
                <span class="file-name" @click="handleFilePreview(file.url)">
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
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
import handleFilePreview from '@/utils/filePreview.js'
import { Document, Download, Delete } from '@element-plus/icons-vue'

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

const emit = defineEmits(['update:modelValue', 'update:filename'])

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
        ElMessage.warning(`文件格式不正确，请上传 ${props.fileType.join('/')} 格式文件`)
        return false
    }
    const size = file.size / 1024 / 1024
    if (size > props.fileSize) {
        ElMessage.warning(`大小超出限制，文件大小不能超过 ${props.fileSize}MB`)
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
        if (res.filename) {
            emit('update:filename', res.filename)
        }
        if (res.ext) {
            emit("update:fileExt", res.ext); // 更新文件后缀名
        }
        uploadedSuccessfully();
    } else {
        number.value--;
        proxy.$modal.closeLoading();
        proxy.$modal.msgWarning("上传文件失败，请重试");
        proxy.$refs.fileUpload.handleRemove(file);
        uploadedSuccessfully();
    }
}
function handleUploadError() {
    ElMessage.warning('上传文件失败，请重试')
}

function handleExceed() {
    ElMessage.warning(`数量超过限制，最多只能上传 ${props.limit} 个文件`)
}

function handleDelete(index) {
    fileList.value.splice(index, 1)
    emit('update:modelValue', fileList.value.map((f) => f.url).join(','))
}

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
    margin-top: -3px;
    margin-bottom: -7px;

}

.has-file {
    margin-top: 0px;
    margin-bottom: -17px;
}

.custom-file-list {
    list-style: none;
    padding: 0;
    margin: 0;
    margin-top: -5px;
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
