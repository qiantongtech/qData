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
    <div v-if="visible" :class="['guide-tip', config.type]">
        <div class="tip-header">
            <span class="tip-title" ref="titleRef">
                <svg-icon v-if="config.type === 'warning' || config.type === 'danger'" iconClass="warning" class="tip-icon" />
                <svg-icon v-if="config.type === 'remind'" iconClass="remind" class="tip-icon" />
                <span v-html="config.title"></span>
            </span>
            <div class="header-buttons">
                <el-button v-if="config.type !== 'danger'" class="btn-never-show" @click="neverShow">{{ t('common.button.neverShow') }}</el-button>
                <el-button :class="['btn-close', config.type]" @click="close">{{ t('common.button.close') }}</el-button>
            </div>
        </div>
        <div v-if="config.content" class="tip-content" v-html="config.content" @click="handleClick"></div>
        <div v-if="config.extensionContent" class="tip-extension" @click="handleClick">
            <div class="tip-extension-header">
                <span v-if="config.extensionLabel" class="tip-extension-label">{{ config.extensionLabel }}</span>
            </div>
            <div class="tip-extension-content" :title="extensionPlainText" v-html="config.extensionContent"></div>
        </div>
    </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, onMounted, onActivated, computed } from 'vue'
import { guideTipConfig } from './guideTipConfig'
import { useRouter } from 'vue-router'
import useUserStore from "@/store/system/user";

const { t, te } = useI18n();
const userStore = useUserStore()
const STORAGE_KEY = 'guide_tip_status'

const props = defineProps({
    tipId: { type: String, required: true }
})

const router = useRouter()
const visible = ref(true)

const tipIdToI18nKey = {
  'index': 'index',
  'att/attAuditRule.list': 'attAuditRule',
  'att/attCleanRule.list': 'attCleanRule',
  'att/attProject.list': 'attProject',
  'att/attTheme.list': 'attTheme',
  'att/client.list': 'attClient',
  'cat/attQualityCat.list': 'attQualityCat',
  'cat/attApiCat.list': 'attApiCat',
  'dp/dpModel.list': 'dpModel',
  'dp/dpDataElem.list': 'dpDataElem',
  'da/daDatasource.list': 'daDatasource',
  'da/daAsset.list': 'daAsset',
  'da/executeSqlQuery': 'executeSqlQuery',
  'da/dataQuality/dataQualityTasks.list': 'dataQualityTasks',
  'da/daSensitiveLevel/daSensitiveLevel.list': 'daSensitiveLevel',
  'dpp/tasker/dppEtlTask.list': 'dppEtlTask',
  'dpp/tasker/dpptaskerddv.list': 'dpptaskerddv',
  'dpp/etltaskinstance/dppEtlTaskInstance.list': 'dppEtlTaskInstance',
  'dpp/dpp/attTaskCat': 'attTaskCat',
  'dpp/dpp/AttDataDevCat.list': 'AttDataDevCat',
  'dpp/dpp/AttProjectUserRel.list': 'AttProjectUserRel',
  'ds/dsApi.list': 'dsApi',
  'dp/dpDocument': 'dpDocument',
  'dm/dataLayer.list': 'dataLayer',
  'mc/task/structured': 'structured',
  'meta/unreleased/structured/table': 'unreleasedTable'
}

const config = computed(() => {
  const original = guideTipConfig[props.tipId] || {};
  const i18nKey = tipIdToI18nKey[props.tipId];
  if (i18nKey) {
    const titleKey = `guide.${i18nKey}.title`;
    const contentKey = `guide.${i18nKey}.content`;
    const extensionLabelKey = `guide.${i18nKey}.extensionLabel`;
    const extensionContentKey = `guide.${i18nKey}.extensionContent`;
    const title = t(titleKey);
    const content = t(contentKey);
    const extensionLabel = te(extensionLabelKey) ? t(extensionLabelKey) : original.extensionLabel;
    const extensionContent = te(extensionContentKey) ? t(extensionContentKey) : original.extensionContent;
    return {
      title: title !== titleKey ? title : original.title,
      content: content !== contentKey ? content : original.content,
      extensionLabel,
      extensionContent,
      type: original.type,
      version: original.version
    };
  }
  return original;
})
const extensionPlainText = computed(() => (config.value.extensionContent || '').replace(/<[^>]*>/g, ''))

// Get storage object
function getGuideTipStorage() {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) {
        return JSON.parse(stored)
    }
    return {}
}

// Generate storage keys, differentiated by user
function getStorageKey() {
    return `${userStore.id}_${props.tipId}_v${config.value.version}`
}

// Whether the current guideTip is displayed
function isGuideTipShown() {
    if (!config.value.version) return true
    const storage = getGuideTipStorage()
    const key = getStorageKey()
    return !storage[key] || storage[key].status === 'shown'
}

// Update guideTip status
function setGuideTipStatus(status) {
    if (!config.value.version) return
    const storage = getGuideTipStorage()
    const key = getStorageKey()
    storage[key] = { status, timestamp: Date.now() }
    localStorage.setItem(STORAGE_KEY, JSON.stringify(storage))
}

// Check display status when activated
function checkVisible() {
    visible.value = isGuideTipShown()
}

onMounted(() => {
    checkVisible()
})
onActivated(() => {
    checkVisible()
})

// Don't remind again
function neverShow() {
    setGuideTipStatus('hidden')
    visible.value = false
}

function close() {
    visible.value = false
}

// Click content processing
function handleClick(event) {
    if (event.target.tagName.toLowerCase() === 'a') return
    const funcName = event.target.dataset.func
    if (!funcName) return
    const link = event.target.dataset.link || ''
    const id = event.target.dataset.id
    const info = event.target.dataset.info
    const row = { id, info }
    if (methods[funcName] && typeof methods[funcName] === 'function') {
        methods[funcName](link, row)
    }
}

// Methods defined within components
const methods = {
    routeTo(link, row) {
        if (link !== '' && link.indexOf('http') !== -1) {
            window.location.href = link
            return
        }
        if (link !== '') {
            if (link === router.currentRoute.value.path) {
                window.location.reload()
            } else {
                router.push({
                    path: link,
                    query: {
                        id: row.id,
                        info: row.info
                    }
                })
            }
        }
    }
}
</script>
<style lang="less" scoped>
@font-title: 'PingFang SC', 'Microsoft YaHei';
@font-content: 'Microsoft YaHei';
@color-text-title: #000;
@color-text-content: #666;

.guide-tip {
    width: 100%;
    margin: 0 0 15px 0;
    padding: 8px 19px 8px 19px;
    position: relative;
    border-radius: 2px;

    &.remind {
        background-color: #fff7e6;
        border: 1px solid #FFE58F;
    }

    &.warning {
        background-color: #FCEAEA;
        border: 1px solid #FFACAE;
    }

    &.danger {
        background-color: #FFACAE;
        border: 1px solid #FE4F4F;
    }

    &.info {
        background-color: #fff;
        border: 1px solid #ddd;
    }

    .tip-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .tip-title {
            font-family: @font-title;
            font-weight: 600;
            font-size: 14px;
            color: @color-text-title;
            line-height: 24px;
            display: flex;
            align-items: center;

            .tip-icon {
                font-size: 16px;
                margin-right: 7px;
            }
        }

        .header-buttons {
            display: flex;
            margin-top: 2px;

            .el-button {
                height: 22px;
                font-family: PingFangSC-Regular, PingFangSC-Regular;
                font-weight: normal;
                font-size: 12px;
                line-height: 18px;
                text-align: left;
                text-transform: none;
                padding: 2px 4px;
                border-radius: 2px;

                &.btn-never-show {
                    background-color: #fff;
                    color: #565656;
                    border: 1px solid #ffffff;
                }

                &.btn-close.remind {
                    background-color: #F88825;
                    color: #fff;
                    border: none;
                    padding: 4px 7px;
                    margin-left: 9px;
                }

                &.btn-close.warning, &.btn-close.danger,  {
                    background-color: #FE4F4F;
                    color: #fff;
                    border: none;
                    padding: 4px 7px;
                    margin-left: 9px;

                }
            }
        }
    }

    .tip-content {
        font-family: @font-content;
        font-weight: 400;
        font-size: 14px;
        color: @color-text-content;
        line-height: 22px;
        margin-top: 0px;
        padding-left: 23px;
        cursor: default;
    }

    .tip-extension {
        display: flex;
        align-items: center;
        width: 100%;
        margin-left: 0;
        margin-top: 6px;
        padding-left: 0;
        padding-top: 6px;
        box-sizing: border-box;
        border-top: 1px dashed rgba(254, 79, 79, 0.28);
        font-family: @font-content;
        font-size: 13px;
        line-height: 20px;
        color: #555;

        .tip-extension-header {
            display: flex;
            flex: none;
            align-items: center;
            min-height: 20px;

            &::after {
                width: 1px;
                height: 12px;
                margin: 0 10px;
                background: rgba(217, 54, 62, 0.25);
                content: '';
            }

            .tip-extension-label {
                padding-bottom: 1px;
                border-bottom: 2px solid #d9363e;
                color: #d9363e;
                font-weight: 600;
            }
        }

        .tip-extension-content {
            min-width: 0;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            font-weight: 400;
        }
    }

}
</style>
<style>
.tip-content a,
.tip-extension-content a {
    color: var(--el-color-primary);
}
.guide-tip .guide-tip-announcement-brand {
    display: inline-block;
    background: linear-gradient(90deg, #1677ff 0%, #7c3aed 45%, #1677ff 100%);
    background-size: 200% auto;
    color: transparent;
    font-weight: 700;
    -webkit-background-clip: text;
    background-clip: text;
    animation: guide-tip-announcement-shine 3s linear infinite;
}
.guide-tip .guide-tip-announcement-dict-tag {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 22px;
    margin: 0 2px;
    padding: 0 8px;
    box-sizing: border-box;
    border: 0;
    border-radius: 2px;
    background: rgba(19, 90, 251, 0.06);
    color: #0f62ff;
    font-size: 12px;
    line-height: 22px;
    vertical-align: 1px;
    white-space: nowrap;
}
.guide-tip .guide-tip-announcement-keyword {
    background: linear-gradient(transparent 68%, rgba(255, 193, 7, 0.38) 0);
    color: #3f3f46;
    font-weight: 600;
}
@keyframes guide-tip-announcement-shine {
    to {
        background-position: -200% center;
    }
}
@media (prefers-reduced-motion: reduce) {
    .guide-tip .guide-tip-announcement-brand {
        animation: none;
    }
}
.clickable {
    color: var(--el-color-primary);
    cursor: pointer;
}
</style>
