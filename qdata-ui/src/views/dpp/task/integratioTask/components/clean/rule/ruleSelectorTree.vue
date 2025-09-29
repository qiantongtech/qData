<template>
  <el-aside :style="{ width: `${leftWidth}px`, marginLeft: leftWidth == 0 ? '-15px' : '0px' }" class="left-pane">
    <div class="left-tree">
      <div class="head-container" v-if="props.showFilter">
        <el-input class="filter-tree" size="large" v-model="deptName" :placeholder="placeholder" clearable
          prefix-icon="Search" />
      </div>
      <div class="head-container">
        <el-tree class="dept-tree" :data="deptOptions" :props="{ label: 'name', children: 'children' }"
          :filter-node-method="filterNode" ref="deptTreeRef" node-key="id" highlight-current
          :default-expanded-keys="expandedKeys" @node-click="handleNodeClick" :default-expand-all="defaultExpand">
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <!-- 第一级 -->
              <!-- <el-icon class="iconimg colorxz" v-if="node.expanded && node.level === 1">
                <FolderOpened />
              </el-icon>
              <el-icon class="iconimg colorxz" v-if="!node.expanded && node.level === 1">
                <Folder />
              </el-icon> -->
              <!-- 第二级 -->
              <!-- <el-icon class="iconimg colorxz" v-if="node.expanded && node.childNodes.length && node.level == 2">
                <FolderOpened />
              </el-icon>
              <el-icon class="iconimg colorxz" v-if="!node.expanded && node.childNodes.length && node.level == 2">
                <Folder />
              </el-icon> -->
              <img class="node-icon" src="../../../../../../../assets/da/asset/folder.svg" alt=""
                   v-if="node.expanded && node.childNodes.length" />
              <img class="node-icon" src="../../../../../../../assets/da/asset/folder.svg" alt=""
                   v-if="!node.expanded && node.childNodes.length" />
              <!-- 子级 -->
              <img class="child-icon" src="../../../../../../../assets/da/asset/file.svg" alt=""
                   v-show="!node.isCurrent && node.childNodes.length == 0" />
              <img class="child-icon" src="../../../../../../../assets/da/asset/file.svg" alt=""
                   v-show="node.isCurrent && node.childNodes.length == 0" />
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
import { ref, defineProps, defineEmits, watch, onMounted } from "vue";
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
  height: {
    type: String,
    default: "87vh",
  },
  showFilter: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(["node-click", "update:deptName", "update:leftWidth"]);

const deptName = ref("");
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
.left-wrapper {
  display: flex;
  height: 100%;
}

.left-pane {
  background-color: #ffffff;
  overflow: hidden;
}

.left-tree {
  height: 72vh;
  padding: 15px 15px 15px 15px;
  scrollbar-width: none;
  -ms-overflow-style: none;
  box-shadow: none !important;

}

.el-aside {
  padding: 2px 0px;
  margin-bottom: 0px;
  background-color: none;


}

.custom-tree-node {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 0 36px 0 12px;

  .node-icon {
    width: 16px;
    height: 16px;
  }

  .child-icon {
    width: 16px;
    height: 16px;
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

.iconimg {
  font-size: 15px;
}

.resize-bar {
  cursor: ew-resize;
  background-color: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 72vh;
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

:deep(.filter-tree) {
  margin-bottom: 16px;

  .el-input__wrapper {
    border: 1px solid var(--el-color-primary);
  }

  .el-input__prefix {
    color: var(--el-color-primary);
  }
}

:deep(.dept-tree) {

  //组织树 背景颜色 及右边线颜色
  &.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background: rgba(51, 103, 252, 0.06) !important;
    border: none;

    .custom-tree-node {
      .treelable {
        color: var(--el-color-primary);
      }
    }
  }

  .el-tree-node__content {
    position: relative;

    .el-tree-node__expand-icon {
      position: absolute;
      right: 10px;
      color: transparent;
      font-size: 11px;
      width: 11px;
      height: 11px;

      &>svg {
        background: url("@/assets/da/asset/arrow.svg") no-repeat;
        background-size: 100% 100%;
        transform: rotate(-90deg);

      }
    }
  }
}
</style>
