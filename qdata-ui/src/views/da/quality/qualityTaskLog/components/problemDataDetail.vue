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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
    <el-dialog v-model="visible" title="问题数据详情" class="medium-dialog" @close="handleClose" destroy-on-close>
        <el-descriptions v-if="parsedFields.length" :column="1" border label-class-name="desc-label">
            <el-descriptions-item v-for="(item, index) in parsedFields" :key="index" :label="item.name">
                {{ item.value }}
            </el-descriptions-item>
        </el-descriptions>
        <template #footer>
            <el-button @click="handleClose">{{ t('common.button.close') }}</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, defineExpose, watch } from 'vue'

const { t } = useI18n();
const visible = ref(false)
const detailData = ref(null)
const parsedFields = ref([])

function open(data, value) {
    console.log("🚀 ~ open ~ value:", value)
    detailData.value = data
    visible.value = true
    try {
        const json = JSON.parse(data.dataJsonStr || '{}')

        const labelMap = (value && Array.isArray(value.evaColumns))
            ? value.evaColumns.reduce((map, col) => {
                map[col.name.toLowerCase()] = col.label || col.name
                return map
            }, {})
            : {}

        parsedFields.value = Object.entries(json).map(([key, val]) => ({
            name: labelMap[key.toLowerCase()] || key,
            value: val,
        }))
    } catch (e) {
        parsedFields.value = []
    }
}

function handleClose() {
    visible.value = false
    detailData.value = null
    parsedFields.value = []
}

defineExpose({
    open,
    close: handleClose,
})
</script>
<style scoped lang="scss">
:deep(.el-descriptions__label) {
    width: 300px !important;
    white-space: nowrap;
}
</style>
