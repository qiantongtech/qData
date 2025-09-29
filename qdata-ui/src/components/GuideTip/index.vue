<template>
    <div v-if="visible" :class="['guide-tip', config.type]">
        <div class="tip-header">
            <span class="tip-title" ref="titleRef">
                <svg-icon v-if="config.type === 'warning' || config.type === 'danger'" iconClass="warning" class="tip-icon" />
                <svg-icon v-if="config.type === 'remind'" iconClass="remind" class="tip-icon" />
                <span v-html="config.title"></span>
            </span>
            <div class="header-buttons">
                <el-button v-if="config.type !== 'danger'" class="btn-never-show" @click="neverShow">不再展示</el-button>
                <el-button :class="['btn-close', config.type]" @click="close">关闭</el-button>
            </div>
        </div>
        <div v-if="config.content" class="tip-content" v-html="config.content" @click="handleClick"></div>
    </div>
</template>

<script setup>
import { ref, onMounted, onActivated, computed } from 'vue'
import { guideTipConfig } from './guideTipConfig'
import { useRouter } from 'vue-router'
import useUserStore from "@/store/system/user";

const userStore = useUserStore()
const STORAGE_KEY = 'guide_tip_status'

const props = defineProps({
    tipId: { type: String, required: true }
})

const router = useRouter()
const visible = ref(true)

const config = computed(() => guideTipConfig[props.tipId] || {})

// 获取存储对象
function getGuideTipStorage() {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) {
        return JSON.parse(stored)
    }
    return {}
}

// 生成存储 key，按用户区分
function getStorageKey() {
    return `${userStore.id}_${props.tipId}_v${config.value.version}`
}

// 当前 guideTip 是否显示
function isGuideTipShown() {
    if (!config.value.version) return true
    const storage = getGuideTipStorage()
    const key = getStorageKey()
    return !storage[key] || storage[key].status === 'shown'
}

// 更新 guideTip 状态
function setGuideTipStatus(status) {
    if (!config.value.version) return
    const storage = getGuideTipStorage()
    const key = getStorageKey()
    storage[key] = { status, timestamp: Date.now() }
    localStorage.setItem(STORAGE_KEY, JSON.stringify(storage))
}

// 激活时检查显示状态
function checkVisible() {
    visible.value = isGuideTipShown()
}

onMounted(() => {
    checkVisible()
})
onActivated(() => {
    checkVisible()
})

// 不再提醒
function neverShow() {
    setGuideTipStatus('hidden')
    visible.value = false
}

function close() {
    visible.value = false
}

// 点击内容处理
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

// 在组件内定义的方法
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
}
</style>
<style>
.tip-content a {
    color: var(--el-color-primary);
}
.clickable {
    color: var(--el-color-primary);
    cursor: pointer;
}
</style>