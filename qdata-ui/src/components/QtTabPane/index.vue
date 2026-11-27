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

  Business License: https://docs.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
    <el-tab-pane v-bind="$attrs">
        <template #default="scope">
            <slot name="default" v-bind="scope" />
        </template>

        <template #label>
            <div class="label-wrap">
                <slot name="label" >
                    {{ $attrs.label }}
                </slot>
                <el-tooltip
                    v-bind="props.tip"
                    :effect="props.tip.effect || 'light'"
                    :placement="props.tip.placement || 'top'"
                    v-if="props.tip && Object.keys(props.tip).length"
                >
                    <el-icon class="tip-icon"> <InfoFilled /> </el-icon>
                    <template #content v-if="props.tip.custom">
                        <div class="tip-content" v-html="props.tip.content"></div>
                    </template>
                </el-tooltip>
            </div>
        </template>
    </el-tab-pane>
</template>

<script setup name="QtTabPane">
    const props = defineProps({
        tip: {
            type: Object
        }
    });
</script>

<style lang="scss" scoped>
    .label-wrap{
        display: flex;
        align-items: center;
        gap: 2px;
        .el-icon{
             color: #888;
        }
    }
</style>
