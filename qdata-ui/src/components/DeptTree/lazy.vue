<template>
    <el-aside :style="{ width: `${leftWidth}px`, marginLeft: leftWidth == 0 ? '-15px' : '0px' }" class="left-pane">
        <div class="left-tree" v-loading="loading">
            <!-- 搜索框 -->
            <el-input class="filter-tree" size="large" v-model="deptName" :placeholder="placeholder" clearable
                prefix-icon="Search" />

            <!-- 树 -->
            <el-tree class="dept-tree" ref="deptTreeRef" :data="deptOptions" node-key="id" highlight-current
                :props="{ label: 'name', children: 'children', isLeaf: 'isLeaf' }" :lazy="true" :load="handleNodeLoad"
                :default-expand-all="defaultExpand" :filter-node-method="filterNode"
                @node-contextmenu="onNodeContextMenu">
                <template #default="{ node, data }">
                    <span class="custom-tree-node" @dblclick.stop="handleNodeClick(data, node, 'node')">
                        <!-- 数据源/层级图标 -->
                        <img v-if="node.level === 1" :src="getDatasourceIcon(data.datasourceType)" class="node-icon" />
                        <img v-if="node.level === 2" src="@/assets/system/images/dpp/sr.png" class="node-icon" />
                        <img v-if="node.level === 3" src="@/assets/system/images/dpp/zt.png" class="node-icon" />
                        <!-- label -->
                        <span class="treelable">{{ node.label }}</span>

                        <!-- 状态图标 -->
                        <el-icon v-if="data.loadSuccess" style="color: #22c55e; margin-left: 6px" class="iconimg"
                            title="加载成功">
                            <CircleCheckFilled />
                        </el-icon>
                        <el-icon v-if="data.loadError" style="color: #facc15; margin-left: 6px; cursor: pointer"
                            class="iconimg" @click.stop="retryLoad(node)" title="加载失败，点击重试">
                            <WarnTriangleFilled />
                        </el-icon>
                    </span>
                </template>
            </el-tree>

            <!-- 右键菜单 -->
            <div v-if="contextMenuVisible" :style="{ top: `${contextMenuY}px`, left: `${contextMenuX}px` }"
                class="context-menu" @click.stop>
                <ul>
                    <li @click="generateSQL('SELECT')">SELECT</li>
                </ul>
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
import { ref, watch, onMounted, onBeforeUnmount, getCurrentInstance } from "vue";
import { debounce } from "lodash-es";
import {
    ArrowLeft,
    ArrowRight,
    CircleCheckFilled,
    WarnTriangleFilled,
} from "@element-plus/icons-vue";

const { proxy } = getCurrentInstance();

const props = defineProps({
    deptOptions: { type: Array, default: () => [] },
    leftWidth: { type: Number, default: 300 },
    placeholder: { type: String, default: "请输入部门名称" },
    defaultExpand: { type: Boolean, default: false },
    loading: { type: Boolean, default: false },
});
const emit = defineEmits(["node-click", "update:deptName", "update:leftWidth", "nodeload-click"]);



const deptName = ref("");
const deptTreeRef = ref(null);
const leftWidth = ref(props.leftWidth);
const isResizing = ref(false);
let startX = 0;

const contextMenuVisible = ref(false);
const contextMenuX = ref(0);
const contextMenuY = ref(0);
const contextMenuNode = ref(null);

// 图标
const getDatasourceIcon = (type) => {
    switch (type) {
        case "DM8": return new URL("@/assets/system/images/dpp/DM.png", import.meta.url).href;
        case "Oracle11": return new URL("@/assets/system/images/dpp/oracle.png", import.meta.url).href;
        case "MySql": return new URL("@/assets/system/images/dpp/mysql.png", import.meta.url).href;
        case "Hive": return new URL("@/assets/system/images/dpp/Hive.png", import.meta.url).href;
        case "Sqlerver": return new URL("@/assets/system/images/dpp/sqlServer.png", import.meta.url).href;
        case "Kafka": return new URL("@/assets/system/images/dpp/kafka.png", import.meta.url).href;
        case "HDFS": return new URL("@/assets/system/images/dpp/hdfs.png", import.meta.url).href;
        case "SHELL": return new URL("@/assets/system/images/dpp/SHELL.png", import.meta.url).href;
        case "Kingbase8": return new URL("@/assets/system/images/dpp/kingBase.png", import.meta.url).href;
        case "SQL_Server": return new URL("@/assets/system/images/dpp/SQL_Server.svg", import.meta.url).href;
        default: return null;
    }
};

// 右键菜单
const onNodeContextMenu = (event, data, node) => {
    event.preventDefault();
    if (node.level !== 2) {
        contextMenuVisible.value = false;
        return;
    }
    contextMenuVisible.value = true;
    contextMenuX.value = event.clientX;
    contextMenuY.value = event.clientY;
    contextMenuNode.value = { data, node };
};

const generateSQL = (type) => {
  if (!contextMenuNode.value) return;
  const { data, node } = contextMenuNode.value;
  const parentData = node.parent ? node.parent.data : null;
  const dbname = parentData?.dbname;
  const datasourceType = parentData?.datasourceType;
  const sid = parentData?.sid; // schema
  const tableName = data.name || null;

  const fields = (node.childNodes || [])
      .map((childNode) => childNode.data?.name)
      .filter(Boolean)
      .join(", ") || "*";

  let fromPart = tableName;
  console.log("🚀 ~ generateSQL ~ datasourceType:", datasourceType)
  if (
      (datasourceType == "Kingbase8" ||
          datasourceType == "SQL_Server")
  ) {
    fromPart = dbname ? `${dbname}.${sid}.${tableName}` : tableName;
  } else {
    fromPart = dbname ? `${dbname}.${tableName}` : tableName;
  }
  const sql = `SELECT ${fields} FROM ${fromPart};`;
  contextMenuVisible.value = false;
  handleNodeClick(sql, node, "sql");
};

// 点击空白关闭右键菜单
const onClickOutside = () => {
    contextMenuVisible.value = false;
};

// 拖拽
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
        let newWidth = leftWidth.value + delta;
        if (newWidth < 0) newWidth = 0;
        leftWidth.value = newWidth;
        startX = event.clientX;
        emit("update:leftWidth", newWidth);
    }
};

const toggleCollapse = () => {
    leftWidth.value = leftWidth.value === 0 ? 300 : 0;
    emit("update:leftWidth", leftWidth.value);
};

const findExpandedLevelOneId = (node) => {
    if (!node) return null;

    if (node.level === 1) {
        if (node.expanded && node.data.loadError !== true) {
            return node.data.id;
        }
        return null;
    }
    return findExpandedLevelOneId(node.parent);
};


const handleNodeClick = (payload, node, type = "node") => {
    contextMenuVisible.value = false;
    if (payload?.level === 1) return;

    const parent = node?.parent?.data || null;
    const children = node?.childNodes?.map(n => n.data) || [];

    const expandedRootId = findExpandedLevelOneId(node);
    console.log("🚀 ~ handleNodeClick ~ expandedRootId:", expandedRootId)
    emit("node-click", { type, payload, parent, children, expandedRootId }, node);
};


// 懒加载
const handleNodeLoad = (node, resolve) => {
    node.data.loadError = false;
    node.data.loadSuccess = false;
    emit(
        "nodeload-click",
        node,
        (children) => {
            node.data.loadSuccess = true;
            resolve(children);
        },
        () => {
            node.data.loadError = true;
            resolve([]);
        }
    );
};

const retryLoad = (node) => {
    if (!node) return;
    node.data.loadError = false;
    node.data.loadSuccess = false;
    node.loaded = false;
    node.expand();
};

// 树过滤
const filterNode = (value, data) => {
    if (!value) return true;
    return data.name.includes(value);
};
const filterTree = debounce((val) => {
    if (deptTreeRef.value) deptTreeRef.value.filter(val);
}, 300);

watch(deptName, (val) => {
    emit("update:deptName", val);
    filterTree(val);
});
watch(() => props.leftWidth, (val) => {
    leftWidth.value = val;
});

onMounted(() => {
    window.addEventListener("click", onClickOutside);
});
onBeforeUnmount(() => {
    window.removeEventListener("click", onClickOutside);
});
</script>

<style scoped lang="scss">
.left-pane {
    background-color: #ffffff;
    overflow: hidden;
    height: 80vh;
}

.left-tree {
    height: 80vh;
    overflow-y: auto;
    overflow-x: hidden;
    scrollbar-width: thin;
    -ms-overflow-style: auto;
}

.left-tree::-webkit-scrollbar {
    width: 6px;
    height: 6px;
}

.left-tree::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 3px;
}

.left-tree::-webkit-scrollbar-track {
    background: transparent;
}


/* 自定义节点样式 */
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
        font-size: 14px;
        color: rgba(0, 0, 0, 0.85);
    }
}

.iconimg {
    font-size: 15px;
}

/* 拖拽栏 */
.resize-bar {
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

/* 搜索框样式 */
:deep(.filter-tree) {
    margin-bottom: 16px;

    .el-input__wrapper {
        border: 1px solid var(--el-color-primary);
    }

    .el-input__prefix {
        color: var(--el-color-primary);
    }
}

/* 树选中样式 */
:deep(.dept-tree) {
    &.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
        background: rgba(51, 103, 252, 0.06) !important;
        border: none;

        .custom-tree-node {
            .treelable {
                color: var(--el-color-primary);
            }
        }
    }
}

/* 右键菜单 */
.context-menu {
    position: fixed;
    background-color: white;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    border-radius: 4px;
    z-index: 1000;
    user-select: none;
}

.context-menu ul {
    margin: 0;
    padding: 8px 0;
    list-style: none;
}

.context-menu li {
    padding: 6px 20px;
    cursor: pointer;
    white-space: nowrap;
}

.context-menu li:hover {
    background-color: #f0f0f0;
}
</style>
