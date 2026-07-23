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
    <el-aside :style="{ width: `${leftWidth}px`, marginLeft: leftWidth == 0 ? '-15px' : '0px' }" class="left-pane">
        <div class="left-tree">
            <div class="head-container">
                <el-input v-model="deptName" :placeholder="placeholder" clearable prefix-icon="Search"
                    style="margin-bottom: 20px" />
            </div>
            <div class="head-container">
                <el-tree :data="deptOptions" :props="{ label: 'name', children: 'children' }"
                    :filter-node-method="filterNode" ref="deptTreeRef" node-key="id" highlight-current
                    :default-expanded-keys="expandedKeys" @node-click="handleNodeClick"
                    :default-expand-all="defaultExpand">
                    <template #default="{ node, data }">
                        <span class="custom-tree-node">
                            <!-- first level -->
                            <el-icon class="iconimg colorxz" v-if="node.expanded && node.level === 1">
                                <FolderOpened />
                            </el-icon>
                            <el-icon class="iconimg colorxz" v-if="!node.expanded && node.level === 1">
                                <Folder />
                            </el-icon>

                            <!-- All levels with child nodes -->
                            <el-icon class="iconimg colorxz"
                                v-if="node.expanded && node.childNodes.length && node.level > 1">
                                <FolderOpened />
                            </el-icon>
                            <el-icon class="iconimg colorxz"
                                v-if="!node.expanded && node.childNodes.length && node.level > 1">
                                <Folder />
                            </el-icon>

                            <!-- Node without child nodes -->
                            <el-icon class="zjiconimg colorwxz"
                                v-show="!node.isCurrent && (!node.childNodes.length || node.childNodes.length === 0)">
                                <Tickets />
                            </el-icon>
                            <el-icon class="zjiconimg colorxz"
                                v-show="node.isCurrent && (!node.childNodes.length || node.childNodes.length === 0)">
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

    <!-- drag bar -->
    <div class="resize-bar" @mousedown="startResize">
        <div class="resize-handle-sx">
            <span class="zjsx"></span>
            <el-icon v-if="leftWidth == 0" @click.stop="toggleCollapse" class="collapse-icon">
                <ArrowRight />
            </el-icon>
            <el-icon v-else class="collapse-icon" @click.stop="toggleCollapse">
                <ArrowLeft />
            </el-icon>
        </div>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch, onMounted } from 'vue';
const { proxy } = getCurrentInstance();
const props = defineProps({
    deptOptions: Array,
    leftWidth: {
        type: Number,
        default: 300
    },
    placeholder: {
        type: String,
        default: ''
    },
    defaultExpand: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits(['node-click', 'update:deptName', 'update:leftWidth']);

const deptName = ref('');
const deptTreeRef = ref(null);
const leftWidth = ref(props.leftWidth);
const expandedKeys = ref([]);

// After deptOptions is loaded, set the first-level node expansion
watch(
    () => props.deptOptions,
    (val) => {
        if (Array.isArray(val) && val.length > 0) {
            console.log(val);
            expandedKeys.value = val.map((item) => item.id); // Expand only the first layer
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
    document.addEventListener('mousemove', updateResize);
    document.addEventListener('mouseup', stopResize);
};
const stopResize = () => {
    isResizing.value = false;
    document.removeEventListener('mousemove', updateResize);
    document.removeEventListener('mouseup', stopResize);
};
const updateResize = (event) => {
    if (isResizing.value) {
        const delta = event.clientX - startX; // Calculate mouse movement distance
        leftWidth.value += delta; // Modify left width
        startX = event.clientX; // Update starting position
        // Use requestAnimationFrame to reduce page redraw frequency
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
    emit('update:leftWidth', leftWidth.value);
};

function handleNodeClick(data) {
    emit('node-click', data);
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
.left-wrapper {
    display: flex;
    height: 100%;
}

.left-pane {
    background-color: #ffffff;
    overflow: hidden;
}

.left-tree {
    padding: 15px 15px 15px 15px;
    scrollbar-width: none;
    -ms-overflow-style: none;
}

.el-aside {
    padding: 2px 0px;
    margin-bottom: 0px;
    background-color: #f0f2f5;
}

.custom-tree-node {
    display: flex;
    align-items: center;
}

.treelable {
    margin-left: 5px;
}

.zjiconimg {
    font-size: 12px;
}

.colorxz {
    color: #358cf3;
}

.colorwxz {
    color: #afd1fa;
}

.iconimg {
    font-size: 15px;
}

.resize-bar {
    height: 86vh;
    cursor: ew-resize;
    background-color: #f0f2f5;
    display: flex;
    align-items: center;
    justify-content: center;
}

.resize-handle-sx {
    width: 15px;
    text-align: center;
    position: relative;
    /* Must be added to locate collapse-icon */
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
    /* true center */
    font-size: 28px;
    color: #aaa;
    cursor: pointer;
    z-index: 10;
    padding: 5px;
}
</style>
