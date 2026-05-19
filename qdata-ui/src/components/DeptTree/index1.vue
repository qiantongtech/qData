<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
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
          :placeholder="placeholder"
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
</template>

<script setup>
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

const { proxy } = getCurrentInstance();
const props = defineProps({
  deptOptions: {
    type: Array,
    default: null,
  },
  type: {
    type: String,
    default: "asset", // 默认资产类型
  },
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
});
const emit = defineEmits(["node-click", "update:deptName", "update:leftWidth"]);

const projectStore = useProjectStore();
const loading = ref(false);

// 优先使用传入的 props，否则根据 type 使用 store 中的缓存
const treeData = computed(() => {
  if (props.deptOptions) return props.deptOptions;
  return props.type === "model"
    ? projectStore.modelDeptTree
    : projectStore.assetDeptTree;
});

// 1. 初始化高度
const qtWrapheight = ref("86vh");
// 定义 ResizeObserver 实例，用于监听高度变化
let resizeObserver = null;

// 2. 封装获取高度的方法
const getQtWrapHeight = () => {
  // 确保 DOM 已挂载后再获取
  const qtWrap = document.querySelector(".qt-wrap");
  const elMain = document.querySelector(".el-main");
  const element = qtWrap || elMain;

  if (element) {
    // 用 offsetHeight 更稳定（包含边框/内边距），也可根据需求用 clientHeight（仅内容+内边距）
    qtWrapheight.value = element.offsetHeight + "px";
  } else {
    qtWrapheight.value = "86vh"; // 兜底默认值
  }
};
// 3. 组件挂载后执行（核心：确保 DOM 已渲染）
onMounted(async () => {
  // 首次获取高度
  getQtWrapHeight();

  // 如果没有传入 props 且 store 中也没有数据，则自动请求一次
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

  // 监听高度变化（适配动态高度场景）
  const qtWrap = document.querySelector(".qt-wrap");
  const elMain = document.querySelector(".el-main");
  const targetElement = qtWrap || elMain;

  if (targetElement) {
    resizeObserver = new ResizeObserver(() => {
      getQtWrapHeight();
    });
    resizeObserver.observe(targetElement);
  }

  // 额外：监听窗口resize，防止布局变化导致高度失效
  window.addEventListener("resize", getQtWrapHeight);
});

// 4. 组件卸载时销毁监听（防止内存泄漏）
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
    ids.push(node.id); // 当前层 id
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
    requestAnimationFrame(() => {});
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
  max-height: var(--qt-wrap-height); // 限制最大高度
  overflow-y: auto; // 超过高度时出现滚动条
  scrollbar-width: thin; // 火狐滚动条细一些
  -ms-overflow-style: none; // IE滚动条隐藏
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
    // border: 1px solid var(--el-color-primary);
  }

  .el-input__prefix {
    color: var(--el-color-primary);
  }
}

:deep(.dept-tree) {
  //组织树 背景颜色 及右边线颜色
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
        background: url("@/assets/da/asset/arrow.svg") no-repeat;
        background-size: 100% 100%;
        transform: rotate(-90deg);
      }
    }
  }
}
</style>
