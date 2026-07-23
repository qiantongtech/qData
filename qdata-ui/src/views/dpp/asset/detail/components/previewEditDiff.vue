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
    <!-- Data preview before and after modification record comparison pop-up window -->
    <el-dialog v-model="visible" class="dialog" width="1200px" draggable destroy-on-close>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ td('dpp.asset.detail.diff.title') }}
            </span>
        </template>
        <div class="center">

            <CodeDiff :old-string="oldStrToCompare" :new-string="newStrToCompare" :context="10"
                output-format="side-by-side" />
        </div>
        <!-- <template #footer>
            <el-button type="primary" @click="cancel">Confirm</el-button>
            <el-button icon="Stopwatch" @click="rollBack" :disabled="loading">Rollback</el-button>
        </template> -->
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref } from 'vue'
import { CodeDiff } from 'v-code-diff'

const { td } = useDefaultLang();

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
