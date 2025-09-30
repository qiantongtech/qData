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
                            <!-- 第一级 -->
                            <el-icon class="iconimg colorxz" v-if="node.expanded && node.level === 1">
                                <FolderOpened />
                            </el-icon>
                            <el-icon class="iconimg colorxz" v-if="!node.expanded && node.level === 1">
                                <Folder />
                            </el-icon>

                            <!-- 有子节点的所有层级 -->
                            <el-icon class="iconimg colorxz"
                                v-if="node.expanded && node.childNodes.length && node.level > 1">
                                <FolderOpened />
                            </el-icon>
                            <el-icon class="iconimg colorxz"
                                v-if="!node.expanded && node.childNodes.length && node.level > 1">
                                <Folder />
                            </el-icon>

                            <!-- 无子节点的节点 -->
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

    <!-- 拖拽栏 -->
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
        default: '请输入部门名称'
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

// 等 deptOptions 加载后设置一级节点展开
watch(
    () => props.deptOptions,
    (val) => {
        if (Array.isArray(val) && val.length > 0) {
            console.log(val);
            expandedKeys.value = val.map((item) => item.id); // 只展开第一层
        }
    },
    { immediate: true }
);

// 过滤节点
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

// 拖拽逻辑
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
        const delta = event.clientX - startX; // 计算鼠标移动距离
        leftWidth.value += delta; // 修改左侧宽度
        startX = event.clientX; // 更新起始位置
        // 使用 requestAnimationFrame 来减少页面重绘频率
        requestAnimationFrame(() => { });
    }
};

// 折叠展开
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
    /* 必须加，用来定位 collapse-icon */
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
    /* 真正的居中 */
    font-size: 28px;
    color: #aaa;
    cursor: pointer;
    z-index: 10;
    padding: 5px;
}
</style>
