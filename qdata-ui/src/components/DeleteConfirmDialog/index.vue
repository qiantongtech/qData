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
  <el-dialog
    v-model="dialogVisible"
    class="deleteConfirmDialog"
    :title="t('components.deleteConfirmDialog.delete') + (deleTitle ? deleTitle : '')"
    width="520px"
    :close-on-click-modal="false"
    @close="handleClose"
    draggable
  >
    <div class="confirm-content">
      <!-- warning message -->
      <div class="warning-message">
        <svg-icon iconClass="warning" class="tip-icon" /> {{ t('components.deleteConfirmDialog.warning') }}{{
          deleTitle
        }}{{ t('components.deleteConfirmDialog.cannotUndo') }}
      </div>

      <div class="repo-info">
        {{ t('components.deleteConfirmDialog.deletePrompt') }}
        <span class="repo-id">{{ deleteId }}</span>
        {{ t('components.deleteConfirmDialog.deletePromptSuffix') }}<br />
        <br />
        {{ t('components.deleteConfirmDialog.confirmInputPrompt') }}
        <p>
          <span class="delete-id-display">{{ verificationText }}</span>
        </p>
      </div>

      <div class="input-section">
        <el-input
          v-model="inputValue"
          :placeholder="t('components.deleteConfirmDialog.inputPlaceholder', { verificationText })"
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
          {{ t('components.deleteConfirmDialog.confirmDelete') }}
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
const deleteId = ref(null); // receive id
const deleTitle = ref(); // receive title
const repoName = ref(""); // Verify name

// Calculate the verification text. If no name is passed in, the default is "Delete immediately"
// Remove spaces in repoName
const verificationText = computed(() => {
  const cleanRepoName = (repoName.value || t('components.deleteConfirmDialog.defaultVerifyText')).replace(/\s/g, "");
  // If there is deleteId, add numbering information to the verification text
  return deleteId.value
    ? `${cleanRepoName}编号 ${deleteId.value} 的数据项`
    : cleanRepoName;
});

// Calculate whether the input is valid
const isInputValid = computed(() => {
  return inputValue.value && inputValue.value === verificationText.value;
});
// Open dialog
/**
 * Open delete confirmation dialog
 * @param {string|number} id - ID of the item to delete
 * @param {string} title - Remove the title, displayed in the dialog title and warning message
 * @param {string} [name] - Verification text name, keyword used to confirm the deletion operation, the default is "Delete immediately"
 */
// Open dialog
const open = (id, title, name) => {
  dialogVisible.value = true;
  inputValue.value = "";
  inputError.value = "";
  deleTitle.value = title;
  deleteId.value = id; // set id
  // Remove spaces from the name parameter passed in
  repoName.value = (name || "").replace(/\s/g, "");
};

// Close dialog
const handleClose = () => {
  inputValue.value = "";
  inputError.value = "";
  deleteId.value = null;
  repoName.value = "";
};

// Handling when input changes
const handleInput = () => {
  if (inputValue.value === verificationText.value) {
    inputError.value = "";
  }
};

// Confirm deletion
const confirmDelete = () => {
  if (inputValue.value !== verificationText.value) {
    inputError.value = t('components.deleteConfirmDialog.inputError', { verificationText: verificationText.value });
    return;
  }

  // Trigger the confirmation event and pass the id
  emit("confirm-delete", deleteId.value);
  dialogVisible.value = false;
  inputValue.value = "";
  inputError.value = "";
  deleteId.value = null;
  repoName.value = "";
};

// Expose methods for parent components to use
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