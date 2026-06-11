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
  <el-dialog
    v-model="dialogVisible"
    class="deleteConfirmDialog"
    :title="'删除' + (deleTitle ? deleTitle : '')"
    width="520px"
    :close-on-click-modal="false"
    @close="handleClose"
    draggable
  >
    <div class="confirm-content">
      <!-- 警告信息 -->
      <div class="warning-message">
        <svg-icon iconClass="warning" class="tip-icon" /> 警告：{{
          deleTitle
        }}删除无法撤消！请慎重操作！
      </div>

      <div class="repo-info">
        该操作将永久删除编号
        <span class="repo-id">{{ deleteId }}</span>
        的数据项，同时可能取消与之关联的关系。<br />
        <br />
        为防止意外，确认继续操作请输入以下内容:
        <p>
          <span class="delete-id-display">{{ verificationText }}</span>
        </p>
      </div>

      <div class="input-section">
        <el-input
          v-model="inputValue"
          :placeholder="`请输入${verificationText}以确认继续操作`"
          clearable
          @input="handleInput"
          class="input-field"
        />
      </div>

      <div v-if="inputError" class="error-message">
        {{ inputError }}
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ t('common.button.cancel') }}</el-button>
        <el-button
          type="primary"
          @click="confirmDelete"
          :disabled="!isInputValid"
        >
          确认删除
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, computed } from "vue";

const { t } = useI18n();
const dialogVisible = ref(false);
const inputValue = ref("");
const inputError = ref("");
const deleteId = ref(null); // 接收id
const deleTitle = ref(); // 接收title
const repoName = ref(""); // 验证名称

// 计算验证文本，如果没有传入name则默认为"立即删除"
// 去除repoName中的空格
const verificationText = computed(() => {
  const cleanRepoName = (repoName.value || "立即删除").replace(/\s/g, "");
  // 如果有deleteId，则在验证文本中加入编号信息
  return deleteId.value
    ? `${cleanRepoName}编号 ${deleteId.value} 的数据项`
    : cleanRepoName;
});

// 计算输入是否有效
const isInputValid = computed(() => {
  return inputValue.value && inputValue.value === verificationText.value;
});
// 打开对话框
/**
 * 打开删除确认对话框
 * @param {string|number} id - 要删除的项目的ID
 * @param {string} title - 删除标题，显示在对话框标题和警告信息中
 * @param {string} [name] - 验证文本名称，用于确认删除操作的关键词，默认为"立即删除"
 */
// 打开对话框
const open = (id, title, name) => {
  dialogVisible.value = true;
  inputValue.value = "";
  inputError.value = "";
  deleTitle.value = title;
  deleteId.value = id; // 设置id
  // 去除传入的name参数中的空格
  repoName.value = (name || "").replace(/\s/g, "");
};

// 关闭对话框
const handleClose = () => {
  inputValue.value = "";
  inputError.value = "";
  deleteId.value = null;
  repoName.value = "";
};

// 输入变化时的处理
const handleInput = () => {
  if (inputValue.value === verificationText.value) {
    inputError.value = "";
  }
};

// 确认删除
const confirmDelete = () => {
  if (inputValue.value !== verificationText.value) {
    inputError.value = `请输入正确的验证文本: ${verificationText.value}`;
    return;
  }

  // 触发确认事件并传递id
  emit("confirm-delete", deleteId.value);
  dialogVisible.value = false;
  inputValue.value = "";
  inputError.value = "";
  deleteId.value = null;
  repoName.value = "";
};

// 暴露方法给父组件使用
const emit = defineEmits(["confirm-delete"]);
defineExpose({ open });
</script>

<style scoped lang="less">
.deleteConfirmDialog {
  .warning-message {
    background-color: #ffecec;
    color: #db2828;
    padding: 10px 20px;
    border: 1px solid red;
    border-radius: 4px;
    font-weight: bold;

    .tip-icon {
      font-size: 16px;
      margin-right: 7px;
    }
  }

  .repo-info {
    background-color: #ffe8e6;
    color: #333333;
    padding: 10px 20px;
    padding-bottom: 0px;
    margin-top: 20px;
    border: 1px solid red;
    border-radius: 4px;
    font-weight: normal;
    line-height: 1;
  }

  .repo-id {
    font-weight: normal;
    color: #333333;
    border-bottom: 1px dashed #333333;
  }

  .delete-id-display {
    margin-top: 10px;
    font-weight: normal;
    color: #db2828;

    border-bottom: 1px dashed #db2828;
  }

  .input-section {
    margin-top: 15px;
  }

  .input-field {
    width: 100%;
  }

  .error-message {
    color: #db2828;
    margin-top: 5px;
  }
}
</style>
<style>
.el-dialog.deleteConfirmDialog .el-dialog__body {
  padding: 20px !important;
}

.el-dialog.deleteConfirmDialog:not(.is-fullscreen) {
  margin-top: 30vh !important;
}
</style>