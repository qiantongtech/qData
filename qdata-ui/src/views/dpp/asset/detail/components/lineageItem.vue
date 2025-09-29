<template>
  <!-- 数据血缘节点 -->
  <div class="data-processing-dag-node">
    <div class="main-area" :class="{ act: nodeData.active }" @mouseenter="onMainMouseEnter"
      @mouseleave="onMainMouseLeave">
      <div class="main-info">
        <!-- {/* 节点类型icon */} -->
        <!-- <i class="node-logo" :style="{ backgroundImage: `url(${NODE_TYPE_LOGO[nodeData.type]})` }" /> -->
        <img class="node-logo" :src="NODE_TYPE_LOGO[nodeData.type]" alt="" />
        <el-popover :disabled="!currentNode.name && !currentNode.status" width="auto" title="" content=""
          placement="top">
          <template #default>
            <template v-if="nodeData.name == currentNode.name && currentNode.type === 'TABLE'">
              <div class="pop-class" @mouseenter="currentNode = nodeData" @mouseleave="currentNode = {}">
                <div class="li">数据表名称：{{ nodeData.name || '-' }}</div>
                <div class="li">数据库类型：{{ nodeData.datasourceType || '-' }}</div>
                <div class="li">数据源名称：{{ nodeData.datasourceName || '-' }}</div>
              </div>
            </template>
            <template v-if="nodeData.name == currentNode.name && currentNode.type == 'TASK'">
              <div class="pop-class" @mouseenter="currentNode = nodeData" @mouseleave="currentNode = {}">
                <div class="li">任务名称：{{ nodeData.name }}</div>
                <div class="li">任务类型：{{ getTaskTypeText(nodeData.type1) }} </div>
                <div class="li">执行引擎: {{ nodeData.taskType || '-' }} </div>
                <div class="li">上一次执行时间：{{
                  parseTime(
                    nodeData.taskTime,
                    "{y}-{m}-{d} {h}:{i}"
                  ) || "-"
                }}</div>
                <div class="li">上一次执行状态：{{ getTaskStatusText(nodeData.taskStatus) }}</div>
              </div>
            </template>
          </template>
          <template #reference>
            <div class="main-text" @mouseenter="currentNode = nodeData" @mouseleave="currentNode = {}">
              <div class="ellipsis-row node-name">{{ nodeData.name }}</div>
              <div class="ellipsis-row node-desc" v-if="nodeData.type == 'TABLE'">{{ nodeData.dbName }}</div>
            </div>
          </template>
        </el-popover>
      </div>
      <!-- {/* 节点状态信息 */} -->
      <div class="status-action">
        <template v-if="nodeData.taskStatus == '6'">
          <el-tooltip class="box-item" effect="dark" :content="nodeData.statusMsg" placement="top">
            <i class="status-icon status-icon-error" />
          </el-tooltip>
        </template>
        <template v-if="nodeData.taskStatus == '7'">
          <i class="status-icon status-icon-success" />
        </template>

        <!-- {/* 节点操作菜单 */} -->
        <!-- <div class="more-action-container">
          <i class="more-action" />
        </div> -->
      </div>
    </div>
    <template v-if="nodeData.leaf">
      <div class="plus-dag">
        <el-icon @click="handleCollapse(nodeData)" v-show="nodeData.collapsed">
          <Remove />
        </el-icon>
        <el-icon @click="handleCollapse(nodeData)" v-show="!nodeData.collapsed">
          <CirclePlus />
        </el-icon>
      </div>
    </template>
    <!-- {/* 添加下游节点 nodeData.type !== NodeType.OUTPUT*/} -->
    <!-- <el-dropdown popper-class="processing-node-menu" trigger="click">
          <span class="el-dropdown-link">
            <el-icon><CirclePlus /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-for="item in PROCESSING_TYPE_LIST" @click="clickPlusDragMenu(item.type)" :key="item.value">
                <i class="node-mini-logo" :style="{ backgroundImage: `url(${NODE_TYPE_LOGO[item.type]})` }" />
                <span>{{ item.name }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown> -->
  </div>
</template>
<script setup name="DataProcessingDagNode">
import { StringExt } from "@antv/x6";
// 状态映射表
const statusMap = {
  0: '提交成功',
  1: '正在执行',
  2: '准备暂停',
  3: '暂停',
  4: '准备停止',
  5: '停止',
  6: '失败',
  7: '成功',
  12: '延时执行',
  14: '串行等待',
  15: '准备锁定',
  16: '锁定',
};
const taskTypeMap = {
  1: '数据集成',
  2: "实时任务",
  3: "数据开发任务",
  4: "作业任务"
};
function getTaskTypeText(type) {
  return taskTypeMap[String(type)] || '-';
}
// 获取状态文字的方法
const getTaskStatusText = (status) => {
  return statusMap[status] ?? '-';
};
// 节点类型
const NodeType = {
  INPUT: "INPUT", // 数据输入
  FILTER: "FILTER", // 数据过滤
  JOIN: "JOIN", // 数据连接
  UNION: "UNION", // 数据合并
  AGG: "AGG", // 数据聚合
  OUTPUT: "OUTPUT", // 数据输出
};
// 不同节点类型的icon
const NODE_TYPE_LOGO = {
  TABLE: new URL("@/assets/dpp/asset/icon (1).png", import.meta.url).href, // 表
  TASK: new URL("@/assets/dpp/asset/icon (2).png", import.meta.url).href, // 任务
};
// 元素校验状态
const CellStatus = {
  DEFAULT: "default",
  SUCCESS: "success",
  ERROR: "error",
};

// 加工类型列表
const PROCESSING_TYPE_LIST = [
  {
    type: "FILTER",
    name: "数据筛选",
  },
  {
    type: "JOIN",
    name: "数据连接",
  },
  {
    type: "UNION",
    name: "数据合并",
  },
  {
    type: "AGG",
    name: "数据聚合",
  },

  {
    type: "OUTPUT",
    name: "数据输出",
  },
];

const props = defineProps({
  node: {
    type: Object,
    default: () => { },
  },
});
const nodeData = ref({});
const currentNode = ref({});
nodeData.value = props.node.getData();
nodeData.value.collapsed = true;
const cellChanged = (cell) => {
  cell.on("change:data", ({ current }) => {
    // console.log("🚀 ~ node.on ~ current:", current);
    nodeData.value = current;
    nodeData.value.collapsed = true;
  });
};
onMounted(() => {
  cellChanged(props.node);
});
/**
 * 根据起点初始下游节点的位置信息
 * @param node 起始节点
 * @param graph
 * @returns
 */
const getDownstreamNodePosition = (node, graph, dx = 250, dy = 100) => {
  // 找出画布中以该起始节点为起点的相关边的终点id集合
  const downstreamNodeIdList = [];
  graph.getEdges().forEach((edge) => {
    const originEdge = edge.toJSON()?.data;
    if (originEdge.source === node.id) {
      downstreamNodeIdList.push(originEdge.target);
    }
  });
  // 获取起点的位置信息
  const position = node.getPosition();
  let minX = Infinity;
  let maxY = -Infinity;
  graph.getNodes().forEach((graphNode) => {
    if (downstreamNodeIdList.indexOf(graphNode.id) > -1) {
      const nodePosition = graphNode.getPosition();
      // 找到所有节点中最左侧的节点的x坐标
      if (nodePosition.x < minX) {
        minX = nodePosition.x;
      }
      // 找到所有节点中最x下方的节点的y坐标
      if (nodePosition.y > maxY) {
        maxY = nodePosition.y;
      }
    }
  });

  return {
    x: minX !== Infinity ? minX : position.x + dx,
    y: maxY !== -Infinity ? maxY + dy : position.y,
  };
};

// 根据节点的类型获取ports
const getPortsByType = (type, nodeId) => {
  let ports = [];
  switch (type) {
    case NodeType.INPUT:
      ports = [
        {
          id: `${nodeId}-out`,
          group: "out",
        },
      ];
      break;
    case NodeType.OUTPUT:
      ports = [
        {
          id: `${nodeId}-in`,
          group: "in",
        },
      ];
      break;
    default:
      ports = [
        {
          id: `${nodeId}-in`,
          group: "in",
        },
        {
          id: `${nodeId}-out`,
          group: "out",
        },
      ];
      break;
  }
  return ports;
};
/**
 * 创建节点并添加到画布
 * @param type 节点类型
 * @param graph
 * @param position 节点位置
 * @returns
 */
const createNode = (type, graph, position) => {
  if (!graph) {
    return {};
  }
  let newNode = {};
  const sameTypeNodes = graph.getNodes().filter((item) => item.getData()?.type === type);
  const typeName = PROCESSING_TYPE_LIST?.find((item) => item.type === type)?.name;
  const id = StringExt.uuid();
  const node = {
    id,
    shape: "data-processing-dag-node",
    x: position?.x,
    y: position?.y,
    ports: getPortsByType(type, id),
    data: {
      name: `${typeName}_${sameTypeNodes.length + 1}`,
      type,
    },
  };
  newNode = graph.addNode(node);
  return newNode;
};
/**
 * 创建边并添加到画布
 * @param source
 * @param target
 * @param graph
 */
const createEdge = (source, target, graph) => {
  const edge = {
    id: StringExt.uuid(),
    shape: "data-processing-curve",
    source: {
      cell: source,
      port: `${source}-out`,
    },
    target: {
      cell: target,
      port: `${target}-in`,
    },
    zIndex: -1,
    data: {
      source,
      target,
    },
  };
  if (graph) {
    graph.addEdge(edge);
  }
};
// 创建下游的节点和边
const createDownstream = (type) => {
  const node = props.node;
  const { graph } = node.model || {};
  if (graph) {
    // 获取下游节点的初始位置信息
    const position = getDownstreamNodePosition(node, graph);
    // 创建下游节点
    const newNode = createNode(type, graph, position);
    const source = node.id;
    const target = newNode.id;
    // 创建该节点出发到下游节点的边
    createEdge(source, target, graph);
  }
};

// 点击添加下游+号
// eslint-disable-next-line no-unused-vars
const clickPlusDragMenu = (type) => {
  createDownstream(type);
};

// 展开
const handleCollapse = () => {
  const node = props.node;
  const { graph } = node.model || {};
  //   node.toggleCollapse();
  const collapsed = node.data.collapsed;
  nodeData.value.collapsed = !collapsed;
  node.data.collapsed = !collapsed;

  const run = (pre) => {
    const succ = graph.getSuccessors(pre, { distance: 1 });
    if (succ) {
      succ.forEach((item) => {
        item.toggleVisible(!collapsed);
        if (item.data.collapsed) {
          run(item);
        }
      });
    }
  };
  run(node);
};
// 鼠标进入矩形主区域的时候显示连接桩
const onMainMouseEnter = () => {
  const node = props.node;
  // 获取该节点下的所有连接桩
  const ports = node.getPorts() || [];
  ports.forEach((port) => {
    node.setPortProp(port.id, "attrs/circle", {
      fill: "#fff",
      stroke: "#85A5FF",
    });
  });
};

// 鼠标离开矩形主区域的时候隐藏连接桩
const onMainMouseLeave = () => {
  const node = props.node;
  // 获取该节点下的所有连接桩
  const ports = node.getPorts() || [];
  ports.forEach((port) => {
    node.setPortProp(port.id, "attrs/circle", {
      fill: "transparent",
      stroke: "transparent",
    });
  });
};
</script>
<style lang="scss" scoped>
.data-processing-dag-node {
  display: flex;
  flex-direction: row;
  align-items: center;
  z-index: 99999;
}

.main-area {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 0 12px;
  width: 180px;
  height: 48px;
  color: rgba(0, 0, 0, 65%);
  font-size: 12px;
  font-family: PingFangSC;
  line-height: 24px;
  background-color: #fff;
  border-radius: 4px;
  border: 1px solid #d5d7db;

  &.act {
    background-color: #ddf7ff;
  }

  &:hover {
    border: 1px solid rgba(0, 0, 0, 10%);
    box-shadow: 0 -2px 4px 0 rgba(209, 209, 209, 50%), 2px 2px 4px 0 rgba(217, 217, 217, 50%);
  }

  .main-info {
    display: flex;
    align-items: center;
    position: relative;

    .node-logo {
      display: inline-block;
      width: 24px;
      // height: 24px;
      background-repeat: no-repeat;
      background-position: center;
      background-size: 100%;
    }

    .main-text {
      width: 100px;
      margin-left: 6px;
      font-family: PingFang SC;

      .node-name {
        color: #5e7ce0;
        font-size: 14px;
        line-height: 1;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        margin-bottom: 5px;
      }

      .node-desc {
        line-height: 1;
        color: #999999;
        font-size: 12px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }
  }
}

.status-action {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.status-icon {
  display: inline-block;
  width: 24px;
  height: 24px;
}

.status-icon-error {
  background: url("https://gw.alipayobjects.com/mdn/rms_43231b/afts/img/A*SEISQ6My-HoAAAAAAAAAAAAAARQnAQ") no-repeat center center / 100% 100%;
}

.status-icon-success {
  background: url("https://gw.alipayobjects.com/mdn/rms_43231b/afts/img/A*6l60T6h8TTQAAAAAAAAAAAAAARQnAQ") no-repeat center center / 100% 100%;
}

.more-action-container {
  margin-left: 12px;
  width: 15px;
  height: 15px;
  text-align: center;
  cursor: pointer;
}

.more-action {
  display: inline-block;
  width: 3px;
  height: 15px;
  background: url("https://mdn.alipayobjects.com/huamei_f4t1bn/afts/img/A*tFw7SIy-ttQAAAAAAAAAAAAADtOHAQ/original") no-repeat center center / 100% 100%;
}

.plus-dag {
  // visibility: hidden;
  position: relative;
  margin-left: 2px;
  height: 48px;
  display: flex;
  align-items: center;
  color: var(--el-color-primary);

  .el-icon {
    border-radius: 50%;
    background: #fff;
  }
}

.plus-action {
  position: absolute;
  top: calc(50% - 8px);
  left: 0;
  width: 16px;
  height: 16px;
  background: url("https://mdn.alipayobjects.com/huamei_f4t1bn/afts/img/A*ScX2R4ODfokAAAAAAAAAAAAADtOHAQ/original") no-repeat center center / 100% 100%;
  cursor: pointer;
}

.plus-action:hover {
  background-image: url("https://mdn.alipayobjects.com/huamei_f4t1bn/afts/img/A*tRaoS5XhsuQAAAAAAAAAAAAADtOHAQ/original");
}

.plus-action:active,
.plus-action-selected {
  background-image: url("https://mdn.alipayobjects.com/huamei_f4t1bn/afts/img/A*k9cnSaSmlw4AAAAAAAAAAAAADtOHAQ/original");
}

.x6-node-selected .main-area {
  border-color: #3471f9;
}

// .x6-node-selected .plus-dag {
//   visibility: visible;
// }

.processing-node-menu {
  padding: 2px 0;
  width: 105px;
  background-color: #fff;
  box-shadow: 0 9px 28px 8px rgba(0, 0, 0, 5%), 0 6px 16px 0 rgba(0, 0, 0, 8%), 0 3px 6px -4px rgba(0, 0, 0, 12%);
  border-radius: 2px;
}

.processing-node-menu ul {
  margin: 0;
  padding: 0;
}

.processing-node-menu li {
  list-style: none;
}

.each-sub-menu {
  padding: 6px 12px;
  width: 100%;
}

.each-sub-menu:hover {
  background-color: rgba(0, 0, 0, 4%);
}

.each-sub-menu a {
  display: inline-block;
  width: 100%;
  height: 16px;
  font-family: PingFangSC;
  font-weight: 400;
  font-size: 12px;
  color: rgba(0, 0, 0, 65%);
}

.each-sub-menu span {
  margin-left: 8px;
  vertical-align: top;
}

.each-disabled-sub-menu a {
  cursor: not-allowed;
  color: rgba(0, 0, 0, 35%);
}

.node-mini-logo {
  display: inline-block;
  width: 16px;
  height: 16px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  vertical-align: top;
}
</style>
<style lang="scss">
.pop-class {
  .li {
    font-size: 12px;
    font-family: PingFang SC;
    margin-bottom: 8px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
