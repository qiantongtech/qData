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
