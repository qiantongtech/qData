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
  <el-aside
    :style="{
      width: `${leftWidth}px`,
      marginLeft: leftWidth == 0 ? '-15px' : '0px',
      '--qt-wrap-height': qtWrapheight,
    }"
    class="left-pane"
  >
    <div class="left-tree">
      <div class="head-container">
        <el-input
          class="filter-tree"
          size="large"
          v-model="deptName"
          :placeholder="effectivePlaceholder"
          clearable
          prefix-icon="Search"
        />
      </div>
      <div class="head-container" v-loading="loading" style="min-height: 200px">
        <el-tree
          v-if="treeData && treeData.length > 0"
          class="dept-tree"
          :data="treeData"
          :props="{ label: 'name', children: 'children' }"
          :filter-node-method="filterNode"
          ref="deptTreeRef"
          node-key="id"
          highlight-current
          :default-expanded-keys="expandedKeys"
          @node-click="handleNodeClick"
          :default-expand-all="defaultExpand"
        >
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <!-- first level -->
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

              <!-- All levels with child nodes -->
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

              <!-- Node without child nodes -->
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

              <span class="treelable" @click="getNode(node)">
                <span class="label-text">{{ node.label }}</span>
                <el-tooltip
                  v-if="node.level == 1 && data?.otherData?.tooltipStr"
                  :content="data.otherData.tooltipStr"
                  placement="top"
                  effect="light"
                >
                  <img
                    class="level1-icon"
                    src="@/assets/icons/svg/question-line.svg"
                    alt=""
                  />
                </el-tooltip>
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
</template>

<script setup>
import { useI18n } from 'vue-i18n';
import {
  ref,
  defineProps,
  defineEmits,
  watch,
  getCurrentInstance,
  onMounted,
  onUnmounted,
  computed,
} from "vue";
import {
  FolderOpened,
  Folder,
  Tickets,
} from "@element-plus/icons-vue";
import { useProjectStore } from "@/store/project/project";
const { t } = useI18n();
const effectivePlaceholder = computed(() => props.placeholder || t('components.deptTree.searchPlaceholder'));

const { proxy } = getCurrentInstance();
const props = defineProps({
  deptOptions: {
    type: Array,
    default: null,
  },
  type: {
    type: String,
    default: "asset", // Default asset type
  },
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

const projectStore = useProjectStore();
const loading = ref(false);

// Prioritize using the incoming props, otherwise use the cache in the store according to type
const treeData = computed(() => {
  if (props.deptOptions) return props.deptOptions;
  return props.type === "model"
    ? projectStore.modelDeptTree
    : projectStore.assetDeptTree;
});

// 1. Initialize height
const qtWrapheight = ref("86vh");
// Define a ResizeObserver instance to monitor height changes
let resizeObserver = null;

// 2. Encapsulate the method of obtaining height
const getQtWrapHeight = () => {
  // Make sure the DOM is mounted before retrieving it
  const qtWrap = document.querySelector(".qt-wrap");
  const elMain = document.querySelector(".el-main");
  const element = qtWrap || elMain;

  if (element) {
    // Using offsetHeight is more stable (including borders/padding), or clientHeight (only content + padding) can be used according to needs.
    qtWrapheight.value = element.offsetHeight + "px";
  } else {
    qtWrapheight.value = "86vh"; // pocket default value
  }
};
// 3. Execute after the component is mounted (core: ensure that the DOM has been rendered)
onMounted(async () => {
  // Get height for the first time
  getQtWrapHeight();

  // If no props are passed in and there is no data in the store, it will be automatically requested once.
  if (!props.deptOptions) {
    if (props.type === "model" && projectStore.modelDeptTree.length === 0) {
      loading.value = true;
      await projectStore.getModelDeptTree();
      loading.value = false;
    } else if (
      props.type === "asset" &&
      projectStore.assetDeptTree.length === 0
    ) {
      loading.value = true;
      await projectStore.getAssetDeptTree();
      loading.value = false;
    }
  }

  // Monitor height changes (adapt to dynamic height scenarios)
  const qtWrap = document.querySelector(".qt-wrap");
  const elMain = document.querySelector(".el-main");
  const targetElement = qtWrap || elMain;

  if (targetElement) {
    resizeObserver = new ResizeObserver(() => {
      getQtWrapHeight();
    });
    resizeObserver.observe(targetElement);
  }

  // Extra: Monitor window resize to prevent height failure caused by layout changes
  window.addEventListener("resize", getQtWrapHeight);
});

// 4. Destroy the listener when the component is unloaded (to prevent memory leaks)
onUnmounted(() => {
  if (resizeObserver) {
    const qtWrap = document.querySelector(".qt-wrap");
    const elMain = document.querySelector(".el-main");
    const targetElement = qtWrap || elMain;

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

function getIdsByLevel(nodes, level = 2, currentLevel = 1) {
  let ids = [];
  if (!nodes || currentLevel > level) return ids;

  for (const node of nodes) {
    ids.push(node.id); // Current layer id
    if (node.children && node.children.length > 0) {
      ids = ids.concat(getIdsByLevel(node.children, level, currentLevel + 1));
    }
  }

  return ids;
}
watch(
  treeData,
  (val) => {
    if (Array.isArray(val) && val.length > 0) {
      expandedKeys.value = getIdsByLevel(val, 1);
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
    const delta = event.clientX - startX; // Calculate mouse movement distance
    leftWidth.value += delta; // Modify left width
    startX = event.clientX; // Update starting position
    // Use requestAnimationFrame to reduce page redraw frequency
    requestAnimationFrame(() => {});
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
    deptTreeRef.value.setCurrentKey(null);
  }
  deptName.value = "";
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
  max-height: var(--qt-wrap-height); // Limit maximum height
  overflow-y: auto; // Scroll bar appears when height is exceeded
  scrollbar-width: thin; // Firefox scroll bars are thinner
  -ms-overflow-style: none; // IE scroll bar hidden
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
  padding: 0 36px 0 12px;



  .treelable {
    margin-left: 10px;
    flex: 1;
    display: flex;
    align-items: center;
    overflow: hidden;
    font-family: PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.85);

    .label-text {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      max-width: 100%;
    }
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

.level1-icon {
  width: 16px;
  height: 16px;
  margin-left: 6px;
  cursor: pointer;
  display: inline-block;
}

.resize-bar {
  height: v-bind(qtWrapheight);
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

:deep(.filter-tree) {
  margin-bottom: 16px;

  .el-input__wrapper {
    // border: 1px solid var(--el-color-primary);
  }

  .el-input__prefix {
    color: var(--el-color-primary);
  }
}

:deep(.dept-tree) {
  //Organization tree background color and right line color
  &.el-tree--highlight-current
    .el-tree-node.is-current
    > .el-tree-node__content {
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

      & > svg {
        background: url("@/assets/images/da/asset/icon-arrow.svg") no-repeat;
        background-size: 100% 100%;
        transform: rotate(-90deg);
      }
    }
  }
}
</style>
