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
    <div class="container console">
        <CodeShow v-model="currCode.log" enableMiniMap enableAutoScroll language="javalog" v-if="currCode.log" />
    </div>
</template>
<script setup name="">
import CodeShow from "@/components/SqlEditor/editorShow/index1.vue";
const currCode = ref({
    log: ""
});
const updateLog = (newLog) => {
    currCode.value.log = newLog || ''
};
defineExpose({ updateLog, })
</script>
<style lang="scss" scoped>
.console {
    display: flex;
    flex-direction: column;
    height: 50vh;

    .container-content {
        flex: 1;
        overflow: hidden;

        .console-view {
            width: 100%;
            height: 100%;
            display: flex;

            .codeEdit {
                flex: 1;
                position: relative;

                .read-json-editor {
                    width: 100%;
                    height: 100%;

                    :deep(.monaco-scrollable-element > .scrollbar > .slider) {
                        background: var(--el-color-primary) !important;
                    }
                }
            }
        }
    }
}
</style>
