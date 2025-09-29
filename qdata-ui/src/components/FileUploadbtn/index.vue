<template>
  <div class="upload-file">
    <el-upload :limit="limit" multiple :action="uploadFileUrl" :before-upload="handleBeforeUpload" :file-list="fileList"
      :on-error="handleUploadError" :on-exceed="handleExceed" :on-success="handleUploadSuccess" :headers="headers"
      class="upload-file-uploader" ref="fileUpload" :data="uploadData" :drag="dragFlag"
      :accept="fileType.map((ext) => '.' + ext).join(',')" :on-remove="handleRemove">
      <el-button type="primary" size="small" icon="Upload" plain>
        选择文件
      </el-button>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="isShowTip">
      仅支持上传
      <b style="color: #f56c6c">{{
        fileType.map((ext) => "." + ext).join(",")
      }}</b>
      格式的文件，大小不超过
      <b style="color: #f56c6c">{{ fileSize }}MB</b>
    </div>
  </div>
</template>
<script setup>
import { getToken } from "@/utils/auth";

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
  // 允许上传 exe, xls, xlsx 文件
  fileType: {
    type: Array,
    default: () => ["doc", "xls", "xlsx", "ppt", "txt", "pdf", "docx", "exe"], // 添加 xls 和 xlsx 格式
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
    default: true, // 默认显示删除按钮
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const number = ref(0);
const uploadList = ref([]);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + "/upload"); // 上传文件服务器地址
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

// 上传前校验文件类型
function handleBeforeUpload(file) {
  // 校验文件类型
  if (props.fileType.length) {
    const fileName = file.name.split(".");
    const fileExt = fileName[fileName.length - 1];
    const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
    if (!isTypeOk) {
      proxy.$modal.msgError(
        `文件格式不正确, 请上传${props.fileType.join("/")}格式文件!`
      );
      return false;
    }
  }

  // 校验文件大小
  const fileSize = file.size / 1024 / 1024;
  if (fileSize > props.fileSize) {
    proxy.$modal.msgError(`文件大小不能超过 ${props.fileSize}MB!`);
    return false;
  }

  // proxy.$modal.loading("正在上传文件，请稍候...");
  number.value++;
  return true;
}

// 文件个数超出
function handleExceed() {
  proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
}

// 上传失败
function handleUploadError(err) {
  proxy.$modal.msgError("上传文件失败");
}

// 上传成功回调
function handleUploadSuccess(res, file) {
  if (res.url) {
    uploadList.value.push({
      name: "/profile/" + res.path + res.filename,
      url: res.url,
    });
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

// 删除文件
function handleDelete(index) {
  fileList.value.splice(index, 1);
  emit("update:modelValue", listToString(fileList.value));
  emit("update:fileExt", null); // 更新文件后缀名
  emit("update:fileSize", null); // 更新文件大小
}

// 上传结束处理
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

// 获取文件名称
function getFileName(name) {
  if (name.lastIndexOf("/") > -1) {
    return name.slice(name.lastIndexOf("/") + 1);
  } else {
    return name;
  }
}

// 对象转成指定字符串分隔
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
  emit("handleRemove"); // 更新文件后缀名
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
