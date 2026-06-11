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
  <!-- 创建表 -->
  <el-dialog :title="td('sys.tool.genCreate.title')" v-model="visible" width="800px" top="5vh"  :append-to="$refs['app-container']" draggable destroy-on-close>
    <span>{{ td('sys.tool.genCreate.createTableStmt') }}</span>
    <el-input type="textarea" :rows="10" :placeholder="td('sys.tool.genCreate.inputTextPlaceholder')" v-model="content"></el-input>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="handleImportTable">{{ td('common.button.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { createTable } from "@/api/system/tool/gen.js";

const { td } = useDefaultLang();
const visible = ref(false);
const content = ref("");
const { proxy } = getCurrentInstance();
const emit = defineEmits(["ok"]);

/** 显示弹框 */
function show() {
  visible.value = true;
}

/** 导入按钮操作 */
function handleImportTable() {
  if (content.value === "") {
    proxy.$modal.msgError(td('sys.tool.genCreate.inputCreateStmt'));
    return;
  }
  createTable({ sql: content.value }).then(res => {
    proxy.$modal.msgSuccess(res.msg);
    if (res.code === 200) {
      visible.value = false;
      emit("ok");
    }
  });
}

defineExpose({
  show,
});
</script>
