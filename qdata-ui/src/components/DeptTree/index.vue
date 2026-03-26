<template>
  <el-aside
    :style="{
      width: `${leftWidth}px`,
      marginLeft: leftWidth == 0 ? '-15px' : '0px',
      '--qt-wrap-height': qtWrapheight,
    }"
    class="left-pane"
  >
    <div class="left-tree">
      <!-- <div class="tree-header">
        <el-icon class="header-icon"><Histogram /></el-icon>
        <span class="header-title">{{ title }}</span>
      </div> -->
      <div class="head-container mb10">
        <el-input
          class="filter-tree"
          size="large"
          v-model="deptName"
          :placeholder="placeholder"
          clearable
          prefix-icon="Search"
        />
      </div>
      <div class="head-container">
        <el-tree
          class="dept-tree"
          :data="processedData"
          :props="{ label: 'name', children: 'children' }"
          :filter-node-method="filterNode"
          ref="deptTreeRef"
          node-key="id"
          highlight-current
          :default-expanded-keys="expandedKeys"
          @node-click="handleNodeClick"
          @node-expand="handleNodeExpand"
          @node-collapse="handleNodeCollapse"
          :default-expand-all="defaultExpand"
        >
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <!-- 第一级 -->
              <el-icon
                class="iconimg colorxz"
                v-if="node.expanded && node.level === 1"
              >
                <FolderOpened />
              </el-icon>
              <el-icon
                class="iconimg colorxz"
                v-if="!node.expanded && node.level === 1"
              >
                <Folder />
              </el-icon>

              <!-- 有子节点的所有层级 -->
              <el-icon
                class="iconimg colorxz"
                v-if="node.expanded && node.childNodes.length && node.level > 1"
              >
                <FolderOpened />
              </el-icon>
              <el-icon
                class="iconimg colorxz"
                v-if="
                  !node.expanded && node.childNodes.length && node.level > 1
                "
              >
                <Folder />
              </el-icon>

              <!-- 无子节点的节点 -->
              <el-icon
                class="zjiconimg colorwxz"
                v-show="
                  !node.isCurrent &&
                  (!node.childNodes.length || node.childNodes.length === 0)
                "
              >
                <Tickets />
              </el-icon>
              <el-icon
                class="zjiconimg colorxz"
                v-show="
                  node.isCurrent &&
                  (!node.childNodes.length || node.childNodes.length === 0)
                "
              >
                <Tickets />
              </el-icon>

              <el-tooltip
                class="box-item"
                effect="dark"
                :content="node.label"
                placement="top-start"
                :disabled="node.label.length < 10"
              >
                <span class="treelabel" @click="getNode(node)">
                  {{ node.label }}
                </span>
              </el-tooltip>

              <!-- 操作入口 -->
              <el-dropdown
                v-if="editable"
                trigger="click"
                @command="(cmd) => handleCommand(cmd, data)"
                @visible-change="(v) => handleDropdownVisibleChange(v, data.id)"
              >
                <span
                  class="operation-trigger"
                  :class="{ 'is-active': activeDropdownNodeId === data.id }"
                  @click.stop
                >
                  <el-icon class="action-icon">
                    <MoreFilled />
                  </el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu class="dept-tree-dropdown">
                    <el-dropdown-item :icon="Plus" command="add"
                      >新增子级</el-dropdown-item
                    >
                    <el-dropdown-item
                      :icon="CopyDocument"
                      command="addSibling"
                      v-if="data.id !== '0'"
                      >新增同级</el-dropdown-item
                    >
                    <el-dropdown-item
                      :icon="Edit"
                      command="edit"
                      v-if="data.id !== '0'"
                      >编辑</el-dropdown-item
                    >
                    <el-dropdown-item
                      :icon="Delete"
                      command="delete"
                      v-if="data.id !== '0'"
                      class="delete-item"
                      >删除</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
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
      <el-icon
        v-if="leftWidth == 0"
        @click.stop="toggleCollapse"
        class="collapse-icon"
      >
        <ArrowRight />
      </el-icon>
      <el-icon v-else class="collapse-icon" @click.stop="toggleCollapse">
        <ArrowLeft />
      </el-icon>
    </div>
  </div>
  <CatEditDialog ref="catEditDialogRef" @submit="handleCatSubmit" />
</template>

<script setup>
import {
  ref,
  defineProps,
  defineEmits,
  watch,
  computed,
  getCurrentInstance,
  onMounted,
  onUnmounted,
  nextTick,
} from "vue";
import {
  Plus,
  CopyDocument,
  Edit,
  Delete,
  Operation,
  MoreFilled,
  FolderOpened,
  Folder,
  Tickets,
  ArrowRight,
  ArrowLeft,
  Histogram,
} from "@element-plus/icons-vue";

const { proxy } = getCurrentInstance();
const props = defineProps({
  deptOptions: Array,
  leftWidth: {
    type: Number,
    default: 300,
  },
  placeholder: {
    type: String,
    default: "请输入部门名称",
  },
  defaultExpand: {
    type: Boolean,
    default: false,
  },
  editable: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: "类目",
  },
  api: {
    type: Object,
    default: () => ({}),
  },
  extraParams: {
    type: Object,
    default: () => ({}),
  },
});
const emit = defineEmits([
  "node-click",
  "update:deptName",
  "update:leftWidth",
  "node-add",
  "node-edit",
  "node-delete",
]);

// 1. 初始化高度
const qtWrapheight = ref("86vh");
let resizeObserver = null;
import CatEditDialog from "@/components/Cat/catEditDialog";
const catEditDialogRef = ref(null);
const processedData = ref([]);

const dialogTreeOptions = computed(() => {
  return [
    {
      id: "0",
      name: props.title,
      children: processedData.value,
    },
  ];
});

function handleNodeAdd(data) {
  if (props.api.add) {
    catEditDialogRef.value.open({
      title: "新增" + props.title,
      nameLabel: props.title + "名称",
      treeOptions: dialogTreeOptions.value,
      form: {
        parentId: data.id,
        validFlag: true,
        ...props.extraParams,
      },
    });
  } else {
    emit("node-add", data);
  }
}

function handleNodeAddSibling(data) {
  const parentId = data.parentId || "0";
  if (props.api.add) {
    catEditDialogRef.value.open({
      title: "新增" + props.title + "同级",
      nameLabel: props.title + "名称",
      treeOptions: dialogTreeOptions.value,
      form: {
        parentId: parentId,
        validFlag: true,
        ...props.extraParams,
      },
    });
  } else {
    emit("node-add", { ...data, parentId });
  }
}

function handleCommand(command, data) {
  switch (command) {
    case "add":
      handleNodeAdd(data);
      break;
    case "addSibling":
      handleNodeAddSibling(data);
      break;
    case "edit":
      handleNodeEdit(data);
      break;
    case "delete":
      handleNodeDelete(data);
      break;
  }
}

function handleNodeEdit(data) {
  if (props.api.get) {
    props.api.get(data.id).then((response) => {
      catEditDialogRef.value.open({
        title: "编辑" + props.title,
        nameLabel: props.title + "名称",
        treeOptions: dialogTreeOptions.value,
        form: response.data,
      });
    });
  } else {
    emit("node-edit", data);
  }
}

function handleNodeDelete(data) {
  if (props.api.del) {
    proxy.$modal
      .confirm('是否确认删除"' + data.name + '"？')
      .then(function () {
        if (props.api.del) {
          return props.api.del(data.id);
        }
      })
      .then(() => {
        proxy.$modal.msgSuccess("删除成功");
        // 如果删除的是当前选中的节点，清空选中状态
        if (currentNodeKey.value === data.id) {
          currentNodeKey.value = null;
          emit("node-click", {});
        }
        getDeptTree();
      });
  } else {
    emit("node-delete", data);
  }
}

function handleCatSubmit(formData) {
  if (formData.id) {
    if (props.api.update) {
      props.api.update(formData).then((response) => {
        proxy.$modal.msgSuccess("修改成功");
        getDeptTree();
      });
    }
  } else {
    if (props.api.add) {
      props.api.add(formData).then((response) => {
        proxy.$modal.msgSuccess("新增成功");
        // 如果是新增，自动展开父节点
        if (
          formData.parentId &&
          !expandedKeys.value.includes(formData.parentId)
        ) {
          expandedKeys.value.push(formData.parentId);
        }
        getDeptTree();
      });
    }
  }
}

function getDeptTree() {
  if (props.api.list) {
    props.api.list(props.extraParams).then((response) => {
      const tree = proxy.handleTree(response.data, "id", "parentId");
      processedData.value = [
        {
          name: props.title,
          value: "",
          id: 0,
          children: tree,
        },
      ];
      const defaultExpanded = [0];
      if (expandedKeys.value.length === 0) {
        expandedKeys.value = defaultExpanded;
      } else if (!expandedKeys.value.some((k) => String(k) === "0")) {
        expandedKeys.value.unshift(0);
      }
      if (currentNodeKey.value) {
        nextTick(() => {
          deptTreeRef.value.setCurrentKey(currentNodeKey.value);
        });
      }
    });
  } else if (props.deptOptions && props.deptOptions.length > 0) {
    processedData.value = props.deptOptions;
  }
}

watch(
  () => props.extraParams,
  (val) => {
    if (props.api.list) {
      getDeptTree();
    }
  },
  { deep: true }
);

watch(
  () => props.deptOptions,
  (val) => {
    if (!props.api.list && Array.isArray(val)) {
      processedData.value = val;
    }
  },
  { immediate: true }
);

onMounted(() => {
  if (props.api.list) {
    getDeptTree();
  }
});

// 高度监听逻辑
const getQtWrapHeight = () => {
  const element = document.querySelector(".qt-wrap");
  if (element) {
    qtWrapheight.value = element.offsetHeight + "px";
  } else {
    qtWrapheight.value = "86vh";
  }
};

onMounted(() => {
  getQtWrapHeight();

  const targetElement = document.querySelector(".qt-wrap");
  if (targetElement) {
    resizeObserver = new ResizeObserver(() => {
      getQtWrapHeight();
    });
    resizeObserver.observe(targetElement);
  }

  window.addEventListener("resize", getQtWrapHeight);
});

onUnmounted(() => {
  if (resizeObserver) {
    const targetElement = document.querySelector(".qt-wrap");
    if (targetElement) {
      resizeObserver.unobserve(targetElement);
    }
    resizeObserver.disconnect();
  }
  window.removeEventListener("resize", getQtWrapHeight);
});

const deptName = ref("");
const deptTreeRef = ref(null);
const leftWidth = ref(props.leftWidth);
const expandedKeys = ref([]);
const currentNodeKey = ref(null);
const activeDropdownNodeId = ref(null);

function handleDropdownVisibleChange(visible, nodeId) {
  activeDropdownNodeId.value = visible ? nodeId : null;
}

function handleNodeExpand(data) {
  if (!expandedKeys.value.includes(data.id)) {
    expandedKeys.value.push(data.id);
  }
}

function handleNodeCollapse(data) {
  const index = expandedKeys.value.indexOf(data.id);
  if (index > -1) {
    expandedKeys.value.splice(index, 1);
  }
}

function getIdsByLevel(nodes, level = 2, currentLevel = 1) {
  let ids = [];
  if (!nodes || currentLevel > level) return ids;

  for (const node of nodes) {
    ids.push(node.id);
    if (node.children && node.children.length > 0) {
      ids = ids.concat(getIdsByLevel(node.children, level, currentLevel + 1));
    }
  }
  return ids;
}

watch(
  () => props.deptOptions,
  (val) => {
    if (Array.isArray(val) && val.length > 0) {
      if (
        val.length === 1 &&
        val[0] &&
        String(val[0].id) === "0" &&
        Array.isArray(val[0].children)
      ) {
        expandedKeys.value = [val[0].id];
      } else {
        expandedKeys.value = getIdsByLevel(val, 1);
      }
    }
  },
  { immediate: true }
);

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
    requestAnimationFrame(() => {});
  }
};

// 折叠展开
const toggleCollapse = () => {
  leftWidth.value = leftWidth.value === 0 ? 300 : 0;
  emit("update:leftWidth", leftWidth.value);
};

function handleNodeClick(data) {
  currentNodeKey.value = data.id;
  emit("node-click", data);
}

const getNode = (node) => {
  console.log(node);
};

const resetTree = () => {
  if (deptTreeRef.value) {
    deptTreeRef.value.setCurrentKey(null);
  }
};

const setCurrentKey = (key) => {
  if (deptTreeRef.value) {
    deptTreeRef.value.setCurrentKey(key);
    currentNodeKey.value = key;
  }
};

defineExpose({ resetTree, getDeptTree, setCurrentKey, deptTreeRef });
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

.tree-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding-left: 10px;

  .header-icon {
    font-size: 20px;
    color: #409eff;
    margin-right: 10px;
    font-weight: bold;
  }

  .header-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    font-family: PingFang SC;
  }
}

.el-aside {
  padding: 2px 0px;
  margin-bottom: 0px;
  background-color: #f0f2f5;
}

.custom-tree-node {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 0 12px 0 12px;
  overflow: hidden;
  min-width: 0;

  .treelabel {
    margin-left: 10px;
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-family: PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.85);
    min-width: 0;
  }

  .operation-trigger {
    display: none;
    margin-left: 2px;
    cursor: pointer;
    align-items: center;
    transform: rotate(90deg); /* 旋转 90 度实现竖向三个点 */
    flex-shrink: 0;

    &.is-active {
      display: flex;
    }
  }

  &:hover {
    .operation-trigger {
      display: flex;
    }
  }

  .action-icon {
    font-size: 16px;
    color: #999;
  }
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
  height: v-bind(qtWrapheight); /* 使用 CSS 变量绑定高度 */
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
</style>

<style lang="scss">
.dept-tree-dropdown {
  background-color: #ffffff !important;
  border: 1px solid #ebeef5 !important;
  padding: 4px 0 !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1) !important;

  .el-dropdown-menu__item {
    color: #606266 !important;
    font-size: 14px !important;
    padding: 8px 16px !important;

    &:hover {
      background-color: #f5f7fa !important;
      color: #409eff !important;
    }

    &.delete-item {
      color: #f56c6c !important;
      border-top: 1px solid #f0f2f5;
      margin-top: 4px;
      padding-top: 12px !important;

      &:hover {
        background-color: #fef0f0 !important;
        color: #f56c6c !important;
      }
    }

    .el-icon {
      margin-right: 8px;
    }
  }

  .el-popper__arrow::before {
    background-color: #ffffff !important;
    border: 1px solid #ebeef5 !important;
  }
}
</style>
