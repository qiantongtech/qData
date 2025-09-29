<template>
  <div class="container console" :style="{ height: `${currHeight}px` }">
    <div class="move" @mousedown="resizeCurrDialog"></div>
    <div class="container-header">
      <span class="title">{{ currItem.name }}</span>
      <span class="close" @click="closeCurrDialog">
        <el-icon>
          <Minus />
        </el-icon>
      </span>
    </div>
    <div class="container-content">
      <template v-if="currItem.type == 'console'">
        <div class="console-view">
          <div class="leftTree" :style="`width: calc(100% - ${consoleWidth}px);`">
            <el-tree class="console-tree" style="min-width: 240px" :data="treeData" :props="{
              children: 'children',
              label: 'label',
            }" default-expand-all highlight-current :expand-on-click-node="false" @node-click="handleNodeClick">
              <template #default="{ node, data }">
                <span class="custom-tree-node">
                  <el-icon class="icon">
                    <CircleCheckFilled />
                  </el-icon>
                  <!-- <el-icon class="icon"><CircleCloseFilled /></el-icon> -->
                  <span class="label">{{ node.label }}</span>
                  <span class="value">{{ data.value }}{{ data.unit }}</span>
                </span>
              </template>
            </el-tree>
          </div>
          <div class="codeEdit" :style="`width: ${consoleWidth}px;`">

            <div class="codeEdit-move" @mousedown="resizeCurrDialogC"></div>
            <CodeShow v-model="currCode.log" enableMiniMap enableAutoScroll language="javalog" />
          </div>
        </div>
      </template>
      <template v-if="currItem.type == 'result'">
        <div class="result-view">
          <el-button class="result-icon" type="primary" @click="handleSearch" icon="Search">获取最新数据</el-button>
          <el-empty description="暂无数据" />
        </div>
      </template>
      <template v-if="currItem.type == 'history'">
        <div class="history-view">
          <el-empty description="暂无数据" />
        </div>
      </template>
    </div>
  </div>
</template>
<script setup name="EditorConsole">
import CodeShow from "@/components/SqlEditor/editorShow/index.vue";
import { getRunTaskInstance, getLogByTaskInstanceId } from "@/api/dpp/task/index.js";
// #region curr弹框拖拽
const currHeight = ref(345); // 初始左侧宽度
const isCurrResizing = ref(false); // 判断是否正在拖拽
let startY = 0; // 鼠标按下时的初始位置
const resizeCurrDialog = (event) => {
  isCurrResizing.value = true;
  startY = event.clientY;
  // 使用 requestAnimationFrame 减少重绘频率
  document.addEventListener("mousemove", updateCurrResize);
  document.addEventListener("mouseup", stopCurrResize);
};
const updateCurrResize = (event) => {
  if (isCurrResizing.value) {
    const delta = startY - event.clientY; // 计算鼠标移动距离
    currHeight.value += delta; // 修改左侧宽度
    startY = event.clientY; // 更新起始位置
    if (currHeight.value > 720) {
      currHeight.value = 720;
      return;
    } else if (currHeight.value < 150) {
      currHeight.value = 150;
      return;
    }
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => { });
  }
};
const stopCurrResize = () => {
  isCurrResizing.value = false;
  document.removeEventListener("mousemove", resizeCurrDialog);
  document.removeEventListener("mouseup", stopCurrResize);
};
// #endregion
const props = defineProps({
  currValue: {
    type: Object,
    default: () => {
      return {
        name: "",
      };
    },
  },
});
const emits = defineEmits(["close"]);
const closeCurrDialog = () => {
  // 清除轮询日志
  clearTimeout(timer.value);
  timer.value = null;
  emits("close");
};
const currItem = computed(() => {
  return props.currValue;
});
// 控制台
// #region console弹框拖拽
const consoleWidth = ref(1300); // 初始左侧宽度
const isCurrResizingC = ref(false); // 判断是否正在拖拽
let startXC = 0; // 鼠标按下时的初始位置
const resizeCurrDialogC = (event) => {
  isCurrResizingC.value = true;
  startXC = event.clientX;
  // 使用 requestAnimationFrame 减少重绘频率
  document.addEventListener("mousemove", updateCurrResizeC);
  document.addEventListener("mouseup", stopCurrResizeC);
};
const updateCurrResizeC = (event) => {
  if (isCurrResizingC.value) {
    const delta = startXC - event.clientX; // 计算鼠标移动距离
    consoleWidth.value += delta; // 修改左侧宽度
    startXC = event.clientX; // 更新起始位置
    if (consoleWidth.value > 1500) {
      consoleWidth.value = 1500;
      return;
    } else if (consoleWidth.value < 100) {
      consoleWidth.value = 100;
      return;
    }
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => { });
  }
};
const stopCurrResizeC = () => {
  isCurrResizingC.value = false;
  document.removeEventListener("mousemove", resizeCurrDialogC);
  document.removeEventListener("mouseup", stopCurrResizeC);
};
// #endregion

const currCode = ref({
  log: "",
});
const handleNodeClick = () => {
  // currCode.value = e;
};
const treeData = ref([
  {
    label: "FlinkSubmit",
    value: "1",
    unit: "秒",
    children: [
      {
        label: "检查作业",
        value: "9",
        unit: "毫秒",
      },
      {
        label: "执行作业",
        value: "1",
        unit: "秒",
        children: [
          {
            label: "构建配置信息",
            value: "31",
            unit: "毫秒",
          },
        ],
      },
    ],
  },
]);
const instanceId = ref();
const getInstanceId = (id) => {
  getRunTaskInstance({ taskId: Number(id) }).then((res) => {
    if (res.code == 200) {
      instanceId.value = res.data;
      if (res.data != null) {
        getCode();
      }
    }
  });
};
let timer = ref(null);
let status = ref(null);
const getCode = () => {
  getLogByTaskInstanceId({ taskInstanceId: instanceId.value }).then((res) => {
    if (res.code == 200) {
      if (status.value != res.data.status) {
        status.value = res.data.status;
        currCode.value.log = res.data.log;
      }
      if (res.data.status == 5 || res.data.status == 6 || res.data.status == 7) {
        clearTimeout(timer.value);
        timer.value = null;
        return;
      } else {
        timer.value = setTimeout(() => {
          getCode();
        }, 1000);
      }
    }
  });
};

onMounted(() => {
  // 控制台初始化加载日志
  if (currItem.value.type == "console") {
    getInstanceId(currItem.value.data.id);
  }
});
// 结果
const handleSearch = () => { };
defineExpose({ currHeight, getInstanceId });
</script>
<style lang="scss" scoped>
.console {
  //   position: absolute;
  //   bottom: 15px;
  position: fixed;
  user-select: auto;
  z-index: 999;
  height: 345px;
  display: block;
  //   border-radius: 5px;
  width: 1630px;
  max-width: 1630px;
  max-height: 720px;
  min-height: 40px;
  box-sizing: border-box;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-left: none;
  border-bottom: none;

  .move {
    position: absolute;
    user-select: none;
    width: 100%;
    height: 10px;
    top: -5px;
    left: 0px;
    cursor: row-resize;
  }

  .container-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 40px;
    padding: 0 20px;
    border-bottom: 1px solid rgb(147 147 147 / 6%);
    background-color: #f9f9f9;

    .title {
      overflow: visible;
      font-weight: 500;
      font-size: 16px;
      font-family: xPingFang SC;
      white-space: nowrap;
      text-overflow: ellipsis;
      color: #333;
      display: flex;
      align-items: center;

      &::before {
        display: inline-block;
        content: "";
        width: 6px;
        height: 16px;
        border-radius: 3px;
        background: var(--el-color-primary);
        margin-right: 8px;
      }
    }

    .close {
      cursor: pointer;
      display: inline-flex;
      justify-content: center;
      align-items: center;
      width: 32px;
      height: 32px;
      border-radius: 50%;
      font-size: 16px;
      color: var(--el-color-primary);

      &:hover {
        background-color: rgb(0, 0, 0, 0.06);
      }
    }
  }

  .container-content {
    height: calc(100% - 40px);
    overflow: hidden;
    background-color: #fcfcfc;

    .console-view {
      width: 100%;
      height: 100%;
      display: flex;

      :deep(.leftTree) {
        width: calc(100% - 1060px);
        overflow-x: auto;

        .console-tree {
          background-color: #fcfcfc;
        }

        //组织树 背景颜色 及右边线颜色
        .console-tree.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
          background: rgba(51, 103, 252, 0.06) !important;
          border: none;

          .custom-tree-node {

            .label,
            .value {
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
            font-size: 12px;

            &>svg {
              background: url("@/assets/da/asset/arrow.png") no-repeat;
              background-size: 100% 100%;
            }
          }
        }

        .el-tree-node__content {
          height: 30px !important;
        }

        .custom-tree-node {
          display: flex;
          align-items: center;
          padding: 0 20px;

          .icon {
            color: #63e25c;
          }

          .label {
            margin: 10px;
          }
        }
      }

      .codeEdit {
        height: 100%;
        position: relative;

        .codeEdit-move {
          position: absolute;
          user-select: none;
          width: 10px;
          height: 100%;
          top: 0px;
          left: -5px;
          cursor: col-resize;
          transition: all 0.3s ease;
          border-right: 5px solid rgba(255, 255, 255, 0);
          border-left: 5px solid rgba(255, 255, 255, 0);
          z-index: 1;

          &:hover {
            border-right: 5px solid rgba(0, 0, 0, 0.1);
            border-left: 5px solid rgba(0, 0, 0, 0.1);
          }
        }

        .read-json-editor {
          border-left: 1px solid rgba(0, 0, 0, 0.06);
        }
      }
    }

    .result-view {
      width: 100%;
      height: 100%;
      position: relative;

      .result-icon {
        position: absolute;
        right: 0;
        top: 0;
      }
    }

    .history-view {
      width: 100%;
      height: 100%;
      position: relative;

    }
  }

  .overflow-guard {
    background-color: red;
  }
}
</style>
