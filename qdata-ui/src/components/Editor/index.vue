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
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/upload"); // Uploaded image server address
const headers = ref({
  Authorization: "Bearer " + getToken()
});

const props = defineProps({
  /* Editor content */
  modelValue: {
    type: String,
  },
  /* height */
  height: {
    type: Number,
    default: null,
  },
  /* minimum height */
  minHeight: {
    type: Number,
    default: null,
  },
  /* read only */
  readOnly: {
    type: Boolean,
    default: false,
  },
  /* Upload file size limit (MB) */
  fileSize: {
    type: Number,
    default: 5,
  },
  /* Type (base64 format, url format) */
  type: {
    type: String,
    default: "url",
  },
  /* Default placeholder */
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
    // Toolbar configuration
    toolbar: [
      ["bold", "italic", "underline", "strike"],      // Bold italic underline strikethrough
      ["blockquote", "code-block"],                   // Quote code block
      [{ list: "ordered" }, { list: "bullet" }],      // Ordered and unordered lists
      [{ indent: "-1" }, { indent: "+1" }],           // indent
      [{ size: ["small", false, "large", "huge"] }],  // font size
      [{ header: [1, 2, 3, 4, 5, 6, false] }],        // Title
      [{ color: [] }, { background: [] }],            // Font color, font background color
      [{ align: [] }],                                // Alignment
      ["clean"],                                      // clear text format
      ["link", "image", "video"]                      // Links, pictures, videos
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

// If the upload address is set, customize the image upload event
onMounted(() => {
  // Setting CSS variables for internationalizing Quill editor toolbar text
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

  // Manually update the display text of a toolbar button
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

// Update Quill editor toolbar labels
function updateQuillToolbarLabels() {
  setTimeout(() => {
    // Update title dropdown label
    const headerPicker = document.querySelector('.ql-snow .ql-picker.ql-header');
    if (headerPicker) {
      const label = headerPicker.querySelector('.ql-picker-label');
      if (label) {
        // Directly set the display text of the label (create a span to display the text)
        let labelText = label.querySelector('.ql-picker-label-text');
        if (!labelText) {
          labelText = document.createElement('span');
          labelText.className = 'ql-picker-label-text';
          label.appendChild(labelText);
        }

        // Function to update label text
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

        // Update once during initialization
        updateHeaderLabel();

        // Add event listener to monitor option changes
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

    // Update font dropdown label
    const fontPicker = document.querySelector('.ql-snow .ql-picker.ql-font');
    if (fontPicker) {
      const fontLabel = fontPicker.querySelector('.ql-picker-label');
      if (fontLabel) {
        // Directly set the display text of the label
        let fontLabelText = fontLabel.querySelector('.ql-picker-label-text');
        if (!fontLabelText) {
          fontLabelText = document.createElement('span');
          fontLabelText.className = 'ql-picker-label-text';
          fontLabel.appendChild(fontLabelText);
        }

        // Function to update label text
        const updateFontLabel = () => {
          const value = fontLabel.getAttribute('data-value');
          if (value === 'serif') fontLabelText.textContent = t('components.editor.serifFont');
          else if (value === 'monospace') fontLabelText.textContent = t('components.editor.monospaceFont');
          else fontLabelText.textContent = t('components.editor.standardFont');
        };

        // Update once during initialization
        updateFontLabel();

        // Add event listener to monitor option changes
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

    // Update link tooltip
    document.querySelectorAll('.ql-snow .ql-tooltip[data-mode="link"]').forEach((tooltip) => {
      tooltip.setAttribute('data-link-placeholder', t('components.editor.linkPlaceholder'));
    });

    // Update video tooltips
    document.querySelectorAll('.ql-snow .ql-tooltip[data-mode="video"]').forEach((tooltip) => {
      tooltip.setAttribute('data-video-placeholder', t('components.editor.videoPlaceholder'));
    });

    // Update save button
    document.querySelectorAll('.ql-snow .ql-tooltip.ql-editing a.ql-action').forEach((action) => {
      action.setAttribute('data-save-text', t('components.editor.save'));
    });
  }, 100);
}

// Check format and size before uploading
function handleBeforeUpload(file) {
  const type = ["image/jpeg", "image/jpg", "image/png", "image/svg"];
  const isJPG = type.includes(file.type);
  //Check file format
  if (!isJPG) {
    proxy.$modal.msgError(t('components.editor.imageFormatError'));
    return false;
  }
  // Proof file size
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgError(t('components.editor.fileSizeError', { size: props.fileSize }));
      return false;
    }
  }
  return true;
}

// Upload successfully processed
function handleUploadSuccess(res, file) {
  // If the upload is successful
  if (res.url) {
    // Get rich text instance
    let quill = toRaw(quillEditorRef.value).getQuill();
    // Get cursor position
    let length = quill.selection.savedRange.index;
    // Insert a picture, res.url is the picture link address returned by the server
    quill.insertEmbed(length, "image", import.meta.env.VITE_APP_BASE_API + '/profile/' + res.path + res.filename);
    // Adjust the cursor to the end
    quill.setSelection(length + 1);
  } else {
    proxy.$modal.msgError(t('components.editor.imageUploadFailed'));
  }
}

// Upload failure handling
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
/* Override Quill default styles with stronger selector weights */
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
/* Hide the pseudo-element of the drop-down menu label and list item, and use the textContent we set to display the text */
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
