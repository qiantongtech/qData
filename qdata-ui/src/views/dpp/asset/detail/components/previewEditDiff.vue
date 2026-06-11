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
    <!-- 数据预览的修改记录前后对比弹窗 -->
    <el-dialog v-model="visible" class="dialog" width="1200px" draggable destroy-on-close>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                前后比对
            </span>
        </template>
        <div class="center">

            <CodeDiff :old-string="oldStrToCompare" :new-string="newStrToCompare" :context="10"
                output-format="side-by-side" />
        </div>
        <!-- <template #footer>
            <el-button type="primary" @click="cancel">确认</el-button>
            <el-button icon="Stopwatch" @click="rollBack" :disabled="loading">回滚</el-button>
        </template> -->
    </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { CodeDiff } from 'v-code-diff'

const visible = ref(false)
const oldStrToCompare = ref('')
const newStrToCompare = ref('')
const loading = ref(false)
const id = ref(null)

function show(diffId, oldData, newData) {
    id.value = diffId
    oldStrToCompare.value = JSON.stringify(oldData, null, 2)
    newStrToCompare.value = JSON.stringify(newData, null, 2)
    visible.value = true
}
function close() {
    visible.value = false
}
function cancel() {
    close()
}

defineExpose({ show })
</script>

<style scoped>
.center {
    max-height: 600px;
    overflow-y: auto;
    overflow-x: hidden;
}
</style>
