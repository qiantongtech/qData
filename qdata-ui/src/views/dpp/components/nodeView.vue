<template>
    <div class="node-component">
        <!-- 上半部分 -->
        <div class="node-top">
            <img :src="iconWhite" class="node-icon" alt="icon" />
            <div class="node-status-wrapper">
                <img :src="statusIcon" class="node-status" v-if="nodeData.styletype != 2" />
                <div class="status-info">{{ statusTip }}</div>
            </div>
        </div>
        <!-- 下半部分 -->
        <div class="node-bottom" @click.stop="emitClick">{{ nodeData.name }}</div>
    </div>
</template>

<script setup>
import { inject, ref, onBeforeUnmount, computed } from "vue"
const emits = defineEmits(["nodeClick"])
const getNode = inject("getNode")
const node = getNode ? getNode() : null
let props = defineProps({
    styletype: Number,
});
if (!node) {
    console.warn("NodeView: 没有获取到节点实例")
}

// 响应式节点数据
const nodeData = ref(node ? node.getData() : {})

// 监听数据变化
const handleChangeData = ({ current }) => {
    nodeData.value = { ...current }
    console.log("节点数据更新:", nodeData.value)
}

if (node) {
    node.on("change:data", handleChangeData)
}

onBeforeUnmount(() => {
    if (node) node.off("change:data", handleChangeData)
})

// 图标处理
const icon = computed(() => nodeData.value.taskParams?.icon || "/img/icon-default.png")

const iconWhite = computed(() => {
    const newIcon = icon.value
    if (newIcon.startsWith("data:image/svg+xml;base64,")) {
        try {
            const svgText = atob(newIcon.split(",")[1])
            const whiteSvgText = svgText.replace(/fill=".*?"/g, 'fill="#ffffff"')
            return "data:image/svg+xml;base64," + btoa(whiteSvgText)
        } catch {
            return newIcon
        }
    }
    return newIcon
})

// 状态配置
const toolbar = [
    { id: "1", icon: "zzzx", tip: "正在执行" },
    { id: "5", icon: "tz", tip: "停止" },
    { id: "6", icon: "sb", tip: "失败" },
    { id: "7", icon: "cg", tip: "成功" },
    { id: "14", icon: "dd", tip: "等待" },
]

const statusItem = computed(() => {
    return toolbar.find(item => String(item.id) == String(nodeData.value.status)) || toolbar[4]
})

const statusIcon = computed(() => {
    const iconFile = statusItem.value ? statusItem.value.icon : "default"
    return new URL(`/src/assets/dpp/etl/${iconFile}.svg`, import.meta.url).href
})

const statusTip = computed(() => statusItem.value?.tip || "未知状态")

const emitClick = () => {
    emits("nodeClick", nodeData.value)
}
</script>

<style lang="less" scoped>
.node-component {
    position: relative;
    height: 40px;
    width: 36px;
    cursor: pointer;
    font-family: Arial, sans-serif;
    background: url("@/assets/dpp/etl/bg.svg");
    background-size: 100% 100%;
}

.node-top {
    position: relative;
    height: 36px;
    width: 36px;

    .node-icon {
        position: absolute;
        top: 50%;
        left: 50%;
        width: 20px;
        height: 20px;
        transform: translate(-50%, -50%);
        filter: brightness(0) invert(1) opacity(0.8);
    }

    .node-status-wrapper {
        position: absolute;
        top: -10px;
        right: -16px;

        .node-status {
            padding: 2px 6px;
            border-radius: 50%;
            font-size: 12px;
            display: inline-block;
            position: relative;
        }

        .status-info {
            display: none;
            position: absolute;
            bottom: 100%;
            left: 50%;
            transform: translateX(-50%);
            margin-bottom: 6px;
            padding: 2px 5px;
            background: #ffffff;
            border-radius: 5px;
            border: 1px solid #add8e6;
            white-space: nowrap;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            z-index: 10;
            opacity: 0;
            transition: opacity 0.2s;
            font-size: 14px;
            color: #5D6F80;

            &::before {
                content: "";
                position: absolute;
                top: 100%;
                left: 50%;
                transform: translateX(-50%);
                width: 0;
                height: 0;
                border-left: 6px solid transparent;
                border-right: 6px solid transparent;
                border-top: 6px solid #add8e6;
            }

            &::after {
                content: "";
                position: absolute;
                top: 100%;
                left: 50%;
                transform: translateX(-50%) translateY(-1px);
                width: 0;
                height: 0;
                border-left: 5px solid transparent;
                border-right: 5px solid transparent;
                border-top: 5px solid #ffffff;
            }
        }

        &:hover .status-info {
            display: block;
            opacity: 1;
        }
    }
}

.node-bottom {
    max-width: 150px;
    position: absolute;
    bottom: -20px;
    left: 50%;
    transform: translateX(-50%);
    text-align: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-weight: 400;
    font-size: 12px;
    color: #333333;

}
</style>
