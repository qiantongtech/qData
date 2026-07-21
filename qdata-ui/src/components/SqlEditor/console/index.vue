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
          <el-button class="result-icon" type="primary" @click="handleSearch" icon="Search">{{ td('components.sqlEditorConsole.fetchLatestData', 'Fetch Latest Data') }}</el-button>
          <el-empty :description="td('common.noData')" />
        </div>
      </template>
      <template v-if="currItem.type == 'history'">
        <div class="history-view">
          <el-empty :description="td('common.noData')" />
        </div>
      </template>
    </div>
  </div>
</template>
<script setup name="EditorConsole">
import CodeShow from "@/components/SqlEditor/editorShow/index.vue";
import { getRunTaskInstance, getLogByTaskInstanceId } from "@/api/dpp/task/index.js";
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
// #region currDrag and drop the pop-up box
const currHeight = ref(345); // Initial left width
const isCurrResizing = ref(false); // Determine whether dragging is in progress
let startY = 0; // Initial position when mouse is pressed
const resizeCurrDialog = (event) => {
  isCurrResizing.value = true;
  startY = event.clientY;
  // Use requestAnimationFrame to reduce redraw frequency
  document.addEventListener("mousemove", updateCurrResize);
  document.addEventListener("mouseup", stopCurrResize);
};
const updateCurrResize = (event) => {
  if (isCurrResizing.value) {
    const delta = startY - event.clientY; // Calculate mouse movement distance
    currHeight.value += delta; // Modify left width
    startY = event.clientY; // Update starting position
    if (currHeight.value > 720) {
      currHeight.value = 720;
      return;
    } else if (currHeight.value < 150) {
      currHeight.value = 150;
      return;
    }
    // Use requestAnimationFrame to reduce page redraw frequency
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
  // Clear polling log
  clearTimeout(timer.value);
  timer.value = null;
  emits("close");
};
const currItem = computed(() => {
  return props.currValue;
});
// console
// #region console pop-up drag and drop
const consoleWidth = ref(1300); // Initial left width
const isCurrResizingC = ref(false); // Determine whether dragging is in progress
let startXC = 0; // Initial position when mouse is pressed
const resizeCurrDialogC = (event) => {
  isCurrResizingC.value = true;
  startXC = event.clientX;
  // Use requestAnimationFrame to reduce redraw frequency
  document.addEventListener("mousemove", updateCurrResizeC);
  document.addEventListener("mouseup", stopCurrResizeC);
};
const updateCurrResizeC = (event) => {
  if (isCurrResizingC.value) {
    const delta = startXC - event.clientX; // Calculate mouse movement distance
    consoleWidth.value += delta; // Modify left width
    startXC = event.clientX; // Update starting position
    if (consoleWidth.value > 1500) {
      consoleWidth.value = 1500;
      return;
    } else if (consoleWidth.value < 100) {
      consoleWidth.value = 100;
      return;
    }
    // Use requestAnimationFrame to reduce page redraw frequency
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
const treeData = computed(() => ([
  {
    label: "FlinkSubmit",
    value: "1",
    unit: td('common.time.second', 'second'),
    children: [
      {
        label: td('components.sqlEditorConsole.checkJob', 'Check Job'),
        value: "9",
        unit: td('common.time.millisecond', 'millisecond'),
      },
      {
        label: td('components.sqlEditorConsole.executeJob', 'Execute Job'),
        value: "1",
        unit: td('common.time.second', 'second'),
        children: [
          {
            label: td('components.sqlEditorConsole.buildConfig', 'Build Configuration'),
            value: "31",
            unit: td('common.time.millisecond', 'millisecond'),
          },
        ],
      },
    ],
  },
]));

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
  // Console initialization loading log
  if (currItem.value.type == "console") {
    getInstanceId(currItem.value.data.id);
  }
});
// Result
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

        //Organization tree background color and right line color
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
              background: url("@/assets/images/da/asset/arrow.png") no-repeat;
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
