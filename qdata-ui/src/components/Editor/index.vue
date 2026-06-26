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
  <div>
    <el-upload
      :action="uploadUrl"
      :before-upload="handleBeforeUpload"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      name="file"
      :show-file-list="false"
      :headers="headers"
      class="editor-img-uploader"
      v-if="type == 'url'"
    >
      <i ref="uploadRef" class="editor-img-uploader"></i>
    </el-upload>
  </div>
  <div class="editor">
    <quill-editor
      ref="quillEditorRef"
      v-model:content="content"
      contentType="html"
      @textChange="(e) => $emit('update:modelValue', content)"
      :options="options"
      :style="styles"
    />
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n';
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import { getToken } from "@/utils/auth";
const { t } = useI18n();

const { proxy } = getCurrentInstance();

const quillEditorRef = ref();
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/upload"); // 上传的图片服务器地址
const headers = ref({
  Authorization: "Bearer " + getToken()
});

const props = defineProps({
  /* 编辑器的内容 */
  modelValue: {
    type: String,
  },
  /* 高度 */
  height: {
    type: Number,
    default: null,
  },
  /* 最小高度 */
  minHeight: {
    type: Number,
    default: null,
  },
  /* 只读 */
  readOnly: {
    type: Boolean,
    default: false,
  },
  /* 上传文件大小限制(MB) */
  fileSize: {
    type: Number,
    default: 5,
  },
  /* 类型（base64格式、url格式） */
  type: {
    type: String,
    default: "url",
  },
  /* 默认占位符 */
  placeholder: {
    type: String,
    default: "",
  }
});

const effectivePlaceholder = computed(() => props.placeholder || t('components.editor.placeholder'));

const options = ref({
  theme: "snow",
  bounds: document.body,
  debug: "warn",
  modules: {
    // 工具栏配置
    toolbar: [
      ["bold", "italic", "underline", "strike"],      // 加粗 斜体 下划线 删除线
      ["blockquote", "code-block"],                   // 引用  代码块
      [{ list: "ordered" }, { list: "bullet" }],      // 有序、无序列表
      [{ indent: "-1" }, { indent: "+1" }],           // 缩进
      [{ size: ["small", false, "large", "huge"] }],  // 字体大小
      [{ header: [1, 2, 3, 4, 5, 6, false] }],        // 标题
      [{ color: [] }, { background: [] }],            // 字体颜色、字体背景颜色
      [{ align: [] }],                                // 对齐方式
      ["clean"],                                      // 清除文本格式
      ["link", "image", "video"]                      // 链接、图片、视频
    ],
  },
  placeholder: effectivePlaceholder.value,
  readOnly: props.readOnly
});

const styles = computed(() => {
  let style = {};
  if (props.minHeight) {
    style.minHeight = `${props.minHeight}px`;
  }
  if (props.height) {
    style.height = `${props.height}px`;
  }
  return style;
});

const content = ref("");
watch(() => props.modelValue, (v) => {
  if (v !== content.value) {
    content.value = v === undefined ? "<p></p>" : v;
  }
}, { immediate: true });

// 如果设置了上传地址则自定义图片上传事件
onMounted(() => {
  // 设置 CSS 变量用于国际化 Quill 编辑器的工具栏文本
  const editorStyles = document.documentElement.style;
  editorStyles.setProperty('--ql-text', t('components.editor.text'));
  editorStyles.setProperty('--ql-heading-1', t('components.editor.heading1'));
  editorStyles.setProperty('--ql-heading-2', t('components.editor.heading2'));
  editorStyles.setProperty('--ql-heading-3', t('components.editor.heading3'));
  editorStyles.setProperty('--ql-heading-4', t('components.editor.heading4'));
  editorStyles.setProperty('--ql-heading-5', t('components.editor.heading5'));
  editorStyles.setProperty('--ql-heading-6', t('components.editor.heading6'));
  editorStyles.setProperty('--ql-standard-font', t('components.editor.standardFont'));
  editorStyles.setProperty('--ql-serif-font', t('components.editor.serifFont'));
  editorStyles.setProperty('--ql-monospace-font', t('components.editor.monospaceFont'));
  editorStyles.setProperty('--ql-link-placeholder', t('components.editor.linkPlaceholder'));
  editorStyles.setProperty('--ql-video-placeholder', t('components.editor.videoPlaceholder'));
  editorStyles.setProperty('--ql-save', t('components.editor.save'));

  // 手动更新工具栏按钮的显示文本
  updateQuillToolbarLabels();

  if (props.type == 'url') {
    let quill = quillEditorRef.value.getQuill();
    let toolbar = quill.getModule("toolbar");
    toolbar.addHandler("image", (value) => {
      if (value) {
        proxy.$refs.uploadRef.click();
      } else {
        quill.format("image", false);
      }
    });
  }
});

// 更新 Quill 编辑器工具栏标签
function updateQuillToolbarLabels() {
  setTimeout(() => {
    // 更新标题下拉菜单标签
    const headerPicker = document.querySelector('.ql-snow .ql-picker.ql-header');
    if (headerPicker) {
      const label = headerPicker.querySelector('.ql-picker-label');
      if (label) {
        // 直接设置标签的显示文本（创建一个 span 来显示文本）
        let labelText = label.querySelector('.ql-picker-label-text');
        if (!labelText) {
          labelText = document.createElement('span');
          labelText.className = 'ql-picker-label-text';
          label.appendChild(labelText);
        }

        // 更新标签文本的函数
        const updateHeaderLabel = () => {
          const value = label.getAttribute('data-value');
          if (value === '1') labelText.textContent = t('components.editor.heading1');
          else if (value === '2') labelText.textContent = t('components.editor.heading2');
          else if (value === '3') labelText.textContent = t('components.editor.heading3');
          else if (value === '4') labelText.textContent = t('components.editor.heading4');
          else if (value === '5') labelText.textContent = t('components.editor.heading5');
          else if (value === '6') labelText.textContent = t('components.editor.heading6');
          else labelText.textContent = t('components.editor.text');
        };

        // 初始化时更新一次
        updateHeaderLabel();

        // 添加事件监听器，监听选项变化
        headerPicker.addEventListener('click', updateHeaderLabel);
      }

      const items = headerPicker.querySelectorAll('.ql-picker-item');
      items.forEach((item, index) => {
        const value = item.getAttribute('data-value');
        if (value === '1') item.textContent = t('components.editor.heading1');
        else if (value === '2') item.textContent = t('components.editor.heading2');
        else if (value === '3') item.textContent = t('components.editor.heading3');
        else if (value === '4') item.textContent = t('components.editor.heading4');
        else if (value === '5') item.textContent = t('components.editor.heading5');
        else if (value === '6') item.textContent = t('components.editor.heading6');
        else if (value === '') item.textContent = t('components.editor.text');
      });
    }

    // 更新字体下拉菜单标签
    const fontPicker = document.querySelector('.ql-snow .ql-picker.ql-font');
    if (fontPicker) {
      const fontLabel = fontPicker.querySelector('.ql-picker-label');
      if (fontLabel) {
        // 直接设置标签的显示文本
        let fontLabelText = fontLabel.querySelector('.ql-picker-label-text');
        if (!fontLabelText) {
          fontLabelText = document.createElement('span');
          fontLabelText.className = 'ql-picker-label-text';
          fontLabel.appendChild(fontLabelText);
        }

        // 更新标签文本的函数
        const updateFontLabel = () => {
          const value = fontLabel.getAttribute('data-value');
          if (value === 'serif') fontLabelText.textContent = t('components.editor.serifFont');
          else if (value === 'monospace') fontLabelText.textContent = t('components.editor.monospaceFont');
          else fontLabelText.textContent = t('components.editor.standardFont');
        };

        // 初始化时更新一次
        updateFontLabel();

        // 添加事件监听器，监听选项变化
        fontPicker.addEventListener('click', updateFontLabel);
      }

      const items = fontPicker.querySelectorAll('.ql-picker-item');
      items.forEach((item) => {
        const value = item.getAttribute('data-value');
        if (value === '') item.textContent = t('components.editor.standardFont');
        else if (value === 'serif') item.textContent = t('components.editor.serifFont');
        else if (value === 'monospace') item.textContent = t('components.editor.monospaceFont');
      });
    }

    // 更新链接工具提示
    document.querySelectorAll('.ql-snow .ql-tooltip[data-mode="link"]').forEach((tooltip) => {
      tooltip.setAttribute('data-link-placeholder', t('components.editor.linkPlaceholder'));
    });

    // 更新视频工具提示
    document.querySelectorAll('.ql-snow .ql-tooltip[data-mode="video"]').forEach((tooltip) => {
      tooltip.setAttribute('data-video-placeholder', t('components.editor.videoPlaceholder'));
    });

    // 更新保存按钮
    document.querySelectorAll('.ql-snow .ql-tooltip.ql-editing a.ql-action').forEach((action) => {
      action.setAttribute('data-save-text', t('components.editor.save'));
    });
  }, 100);
}

// 上传前校检格式和大小
function handleBeforeUpload(file) {
  const type = ["image/jpeg", "image/jpg", "image/png", "image/svg"];
  const isJPG = type.includes(file.type);
  //检验文件格式
  if (!isJPG) {
    proxy.$modal.msgError(t('components.editor.imageFormatError'));
    return false;
  }
  // 校检文件大小
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgError(t('components.editor.fileSizeError', { size: props.fileSize }));
      return false;
    }
  }
  return true;
}

// 上传成功处理
function handleUploadSuccess(res, file) {
  // 如果上传成功
  if (res.url) {
    // 获取富文本实例
    let quill = toRaw(quillEditorRef.value).getQuill();
    // 获取光标位置
    let length = quill.selection.savedRange.index;
    // 插入图片，res.url为服务器返回的图片链接地址
    quill.insertEmbed(length, "image", import.meta.env.VITE_APP_BASE_API + '/profile/' + res.path + res.filename);
    // 调整光标到最后
    quill.setSelection(length + 1);
  } else {
    proxy.$modal.msgError(t('components.editor.imageUploadFailed'));
  }
}

// 上传失败处理
function handleUploadError() {
  proxy.$modal.msgError(t('components.editor.imageUploadFailed'));
}
</script>

<style>
.editor-img-uploader {
  display: none;
}
.editor, .ql-toolbar {
  white-space: pre-wrap !important;
  line-height: normal !important;
}
.quill-img {
  display: none;
}
/* 使用更强的选择器权重覆盖 Quill 默认样式 */
:deep(.ql-snow .ql-tooltip[data-mode="link"])::before {
  content: attr(data-link-placeholder) !important;
}
:deep(.ql-snow .ql-tooltip.ql-editing a.ql-action)::after {
  border-right: 0px !important;
  content: attr(data-save-text) !important;
  padding-right: 0px !important;
}
:deep(.ql-snow .ql-tooltip[data-mode="video"])::before {
  content: attr(data-video-placeholder) !important;
}
.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}
.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: var(--ql-text, "文本");
}
/* 隐藏下拉菜单标签和列表项的伪元素，使用我们设置的 textContent 来显示文本 */
.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  opacity: 0 !important;
  visibility: hidden !important;
}
.ql-snow .ql-picker.ql-header .ql-picker-label .ql-picker-label-text {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  color: inherit;
  font-size: inherit;
}
.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  opacity: 0 !important;
  visibility: hidden !important;
}
.ql-snow .ql-picker.ql-font .ql-picker-label .ql-picker-label-text {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  color: inherit;
  font-size: inherit;
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: var(--ql-heading-1, "标题1");
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: var(--ql-heading-2, "标题2");
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: var(--ql-heading-3, "标题3");
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: var(--ql-heading-4, "标题4");
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: var(--ql-heading-5, "标题5");
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: var(--ql-heading-6, "标题6");
}
.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: var(--ql-standard-font, "标准字体");
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: var(--ql-serif-font, "衬线字体");
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: var(--ql-monospace-font, "等宽字体");
}
</style>
