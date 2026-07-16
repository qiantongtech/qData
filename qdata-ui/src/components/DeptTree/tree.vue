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
    <el-aside :style="{ width: `${leftWidth}px`, marginLeft: leftWidth == 0 ? '-15px' : '0px', }" class="left-pane">
        <div class="left-tree">
            <div class="head-container">
                <el-tree class="dept-tree" :data="deptOptions" :props="{ label: 'name', children: 'children' }"
                    :filter-node-method="filterNode" ref="deptTreeRef" node-key="id" highlight-current
                    :default-expanded-keys="expandedKeys" @node-click="handleNodeClick"
                    :default-expand-all="defaultExpand">
                    <template #default="{ node, data }">
                        <span class="custom-tree-node">
                            <img class="node-icon" src="@/assets/images/da/asset/img-icon-three.png" alt=""
                                v-if="node.childNodes.length" />
                            <el-icon class="zjiconimg colorwxz" v-show="!node.isCurrent && node.childNodes.length == 0">
                                <Tickets />
                            </el-icon>
                            <el-icon class="zjiconimg colorxz" v-show="node.isCurrent && node.childNodes.length == 0">
                                <Tickets />
                            </el-icon>
                            <span class="treelable" @click="getNode(node)">
                                {{ node.label }}
                            </span>
                        </span>
                    </template>
                </el-tree>
            </div>
        </div>
    </el-aside>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch, getCurrentInstance } from "vue";
import { ArrowLeft, ArrowRight, Tickets } from "@element-plus/icons-vue";

const { proxy } = getCurrentInstance();

const props = defineProps({
    deptOptions: Array,
    leftWidth: {
        type: Number,
        default: 300,
    },
    placeholder: {
        type: String,
        default: '',
      },
    defaultExpand: {
        type: Boolean,
        default: false,
    },
});

const emit = defineEmits(["node-click", "update:deptName", "update:leftWidth"]);

const deptName = ref("");
const deptTreeRef = ref(null);
const leftWidth = ref(props.leftWidth);
const expandedKeys = ref([]);

// After deptOptions is loaded, set the first-level node expansion
watch(
    () => props.deptOptions,
    (val) => {
        if (Array.isArray(val) && val.length > 0) {
            expandedKeys.value = val.map((item) => item.id); // Expand the first layer
        }
    },
    { immediate: true }
);

// Filter nodes
const filterNode = (value, data) => {
    if (!value) return true;
    return data.name.indexOf(value) !== -1;
};

watch(deptName, (val) => {
    if (deptTreeRef.value) {
        deptTreeRef.value.filter(val);
    }
});

watch(
    () => props.leftWidth,
    (val) => {
        leftWidth.value = val;
    }
);

// Drag and drop logic
const isResizing = ref(false);
let startX = 0;
const startResize = (event) => {
    isResizing.value = true;
    startX = event.clientX;
    document.addEventListener("mousemove", updateResize);
    document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
    isResizing.value = false;
    document.removeEventListener("mousemove", updateResize);
    document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
    if (isResizing.value) {
        const delta = event.clientX - startX;
        leftWidth.value += delta;
        startX = event.clientX;
        requestAnimationFrame(() => { });
    }
};

// Collapse and expand
const toggleCollapse = () => {
    if (leftWidth.value === 0) {
        leftWidth.value = 300;
    } else {
        leftWidth.value = 0;
    }
    emit("update:leftWidth", leftWidth.value);
};

function handleNodeClick(data) {
    emit("node-click", data);
}

const getNode = (node) => {
    console.log(node);
};

const resetTree = () => {
    if (deptTreeRef.value) {
        proxy.$refs.deptTreeRef.setCurrentKey(null);
    }
};

defineExpose({ resetTree });
</script>

<style scoped lang="scss">
.left-pane {
    background-color: #ffffff;
    overflow: hidden;
    height: 660px;
    display: flex;
    flex-direction: column;
}

.left-tree {
    padding: 15px;
    flex: 1;
    overflow-y: auto;
    /* Exceeding display scroll bar */
    scrollbar-width: thin;
}

.left-tree::-webkit-scrollbar {
    width: 6px;
}

.left-tree::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 3px;
}

.left-tree::-webkit-scrollbar-track {
    background: transparent;
}

.custom-tree-node {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0 36px 0 12px;

    .node-icon {
        width: 18px;
        height: 18px;
    }

    .treelable {
        margin-left: 10px;
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        font-family: PingFang SC;
        font-weight: 400;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.85);
    }
}

.zjiconimg {
    font-size: 12px;
}

.colorxz {
    color: #358cf3;
}

.colorwxz {
    color: var(--el-color-primary);
}

.resize-bar {
    height: 660px;
    cursor: ew-resize;
    display: flex;
    align-items: center;
    justify-content: center;
}

.resize-handle-sx {
    width: 15px;
    text-align: center;
    position: relative;
}

.zjsx {
    display: none;
    width: 5px;
    height: 50px;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
}

.collapse-icon {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 28px;
    color: #aaa;
    cursor: pointer;
    z-index: 10;
    padding: 5px;
}

:deep(.dept-tree) {
    &.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
        background: rgba(51, 103, 252, 0.06) !important;
        border: none;

        .custom-tree-node .treelable {
            color: var(--el-color-primary);
        }
    }

    .el-tree-node__content {
        position: relative;

        .el-tree-node__expand-icon {
            position: absolute;
            right: 10px;
            color: transparent;

            &>svg {
                background: url("@/assets/images/da/asset/arrow.png") no-repeat;
                background-size: 100% 100%;
            }
        }
    }
}
</style>
