<template>
  <div class="component-upload-image">
    <el-upload
      multiple
      list-type="picture-card"
      ref="imageUpload"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :before-remove="handleDelete"
      :show-file-list="true"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit }"
      :data="uploadData"
      action="#"
      :http-request="requestUpload"
    >
      <el-icon class="avatar-uploader-icon">
        <plus />
      </el-icon>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip">
      请上传
      <template v-if="fileSize">
        大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
      </template>
      <template v-if="fileType">
        格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b>
      </template>
      的文件
    </div>

    <el-dialog v-model="dialogVisible" title="预览" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <img :src="dialogImageUrl" style="display: block; max-width: 100%; margin: 0 auto" />
    </el-dialog>
  </div>
</template>

<script setup>
import { avatarSystemContent } from "@/api/system/system/user.js";
const props = defineProps({
  // eslint-disable-next-line vue/require-default-prop
  modelValue: [String, Object, Array],
  // 图片数量限制
  limit: {
    type: Number,
    default: 5,
  },
  // 大小限制(MB)
  fileSize: {
    type: Number,
    default: 5,
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array,
    default: () => ["png", "jpg", "jpeg"],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: false,
  },
  // platform参数
  platForm: {
    type: String,
    default: "",
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits({});
const number = ref(0);
const uploadList = ref([]);
const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const fileList = ref([]);
const uploadData = ref({
  platForm: props.platForm,
});
const showTip = computed(() => props.isShowTip && (props.fileType || props.fileSize));

watch(
  () => props.modelValue,
  (val) => {
    if (val) {
      // 首先将值转为数组
      const list = Array.isArray(val) ? val : props.modelValue.split(",");
      // 然后将数组转为对象数组
      fileList.value = list.map((item) => {
        // 如果 item 是字符串，统一处理成对象
        if (typeof item === "string") {
          const url = item.indexOf(baseUrl) === -1 && props.platForm === "" ? baseUrl + item : item;
          return { name: url, url };
        }
        // 如果 item 已经是对象，直接返回
        return item;
      });
    } else {
      fileList.value = [];
      return [];
    }
  },
  { deep: true, immediate: true }
);
function dataURLtoBlob(base64Buf) {
  const arr = base64Buf.split(",");
  const typeItem = arr[0];
  const mime = typeItem.match(/:(.*?);/) ? typeItem.match(/:(.*?);/)[1] : "";
  const bstr = window.atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new Blob([u8arr], { type: mime });
}

/** 覆盖默认上传行为 */
function requestUpload() {}
// 上传前loading加载
function handleBeforeUpload(file) {
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgWarning(`大小超出限制，上传图片大小不能超过 ${props.fileSize} MB`);
      return false;
    }
  }
  if (file.type.indexOf("image/") == -1) {
    proxy.$modal.msgWarning("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件");
  } else {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      let formData = new FormData();
      const blob = dataURLtoBlob(reader.result);
      formData.append("avatarfile", blob, file.name);
      avatarSystemContent(formData).then((response) => {
        uploadList.value.push({ name: file.name, url: baseUrl + response.imgUrl });
        uploadedSuccessfully();
      });
    };
  }
  proxy.$modal.loading("正在上传图片，请稍候...");
  number.value++;
}

// 文件个数超出
function handleExceed() {
  proxy.$modal.msgWarning(`数量超出限制，上传文件数量不能超过 ${props.limit} 个`);
}

// 上传成功回调
function handleUploadSuccess(res, file) {
  console.log(res, file);
}

// 删除图片
function handleDelete(file) {
  const findex = fileList.value.map((f) => f.name).indexOf(file.name);
  if (findex > -1 && uploadList.value.length === number.value) {
    fileList.value.splice(findex, 1);
    emit("update:modelValue", listToString(fileList.value));
    return false;
  }
}

// 上传结束处理
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value.filter((f) => f.url !== undefined).concat(uploadList.value);
    console.log("fileList.value===========", fileList.value);
    uploadList.value = [];
    number.value = 0;
    emit("update:modelValue", listToString(fileList.value));
    proxy.$modal.closeLoading();
  }
}

// 上传失败
function handleUploadError() {
  proxy.$modal.msgWarning("上传图片失败，请联系管理员");
  proxy.$modal.closeLoading();
}

// 预览
function handlePictureCardPreview(file) {
  let url = file.url;
  if (!url.includes(baseUrl)) {
    url = baseUrl + url;
  }
  dialogImageUrl.value = url;
  dialogVisible.value = true;
}

// 对象转成指定字符串分隔
function listToString(list, sepa) {
  let strs = "";
  let separator = sepa || ",";
  for (let i in list) {
    if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
      strs += list[i].url + separator;
    }
  }
  return strs != "" ? strs.substr(0, strs.length - 1) : "";
}
</script>

<style scoped lang="scss">
// .el-upload--picture-card 控制加号部分
:deep(.hide .el-upload--picture-card) {
  opacity: 0;
  pointer-events: none;
  height: 0px !important;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 80px !important;
  height: 80px !important;
}

:deep(.el-upload--picture-card) {
  width: 80px !important;
  height: 80px !important;
  line-height: 80px !important;
}

:deep(.el-upload-list__item-thumbnail) {
  object-fit: cover;
  width: 100% !important;
  height: 100% !important;
  border-radius: 4px;
}
</style>
