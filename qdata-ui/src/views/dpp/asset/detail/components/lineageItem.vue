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
  <!-- Data lineage node -->
  <div class="data-processing-dag-node">
    <div class="main-area" :class="{ act: nodeData.active }" @mouseenter="onMainMouseEnter"
      @mouseleave="onMainMouseLeave">
      <div class="main-info">
        <!-- {/* Node type icon */} -->
        <!-- <i class="node-logo" :style="{ backgroundImage: `url(${NODE_TYPE_LOGO[nodeData.type]})` }" /> -->
        <img class="node-logo" :src="NODE_TYPE_LOGO[nodeData.type]" alt="" />
        <el-popover :disabled="!currentNode.name && !currentNode.status" width="auto" title="" content=""
          placement="top">
          <template #default>
            <template v-if="nodeData.name == currentNode.name && currentNode.type === 'TABLE'">
              <div class="pop-class" @mouseenter="currentNode = nodeData" @mouseleave="currentNode = {}">
                <div class="li">{{ td('dpp.asset.detail.lineage.tableName') }}：{{ nodeData.name || '-' }}</div>
                <div class="li">{{ td('dpp.asset.detail.lineage.dbType') }}：{{ nodeData.datasourceType || '-' }}</div>
                <div class="li">{{ td('dpp.asset.detail.lineage.datasourceName') }}：{{ nodeData.datasourceName || '-' }}</div>
              </div>
            </template>
            <template v-if="nodeData.name == currentNode.name && currentNode.type == 'TASK'">
              <div class="pop-class" @mouseenter="currentNode = nodeData" @mouseleave="currentNode = {}">
                <div class="li">{{ td('dpp.asset.detail.lineage.taskName') }}：{{ nodeData.name }}</div>
                <div class="li">{{ td('dpp.asset.detail.lineage.taskType') }}：{{ getTaskTypeText(nodeData.type1) }} </div>
                <div class="li">{{ td('dpp.asset.detail.lineage.executionEngine') }}: {{ nodeData.taskType || '-' }} </div>
                <div class="li">{{ td('dpp.asset.detail.lineage.lastExecutionTime') }}：{{
                  parseTime(
                    nodeData.taskTime,
                    "{y}-{m}-{d} {h}:{i}"
                  ) || "-"
                }}</div>
                <div class="li">{{ td('dpp.asset.detail.lineage.lastExecutionStatus') }}：{{ getTaskStatusText(nodeData.taskStatus) }}</div>
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
      <!-- {/* Node status information */} -->
      <div class="status-action">
        <template v-if="nodeData.taskStatus == '6'">
          <el-tooltip class="box-item" effect="dark" :content="nodeData.statusMsg" placement="top">
            <i class="status-icon status-icon-error" />
          </el-tooltip>
        </template>
        <template v-if="nodeData.taskStatus == '7'">
          <i class="status-icon status-icon-success" />
        </template>

        <!-- {/* Node operation menu */} -->
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
    <!-- {/* Add downstream node nodeData.type !== NodeType.OUTPUT*/} -->
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
import useDefaultLang from "@/composables/useDefaultLang"
import { StringExt } from "@antv/x6";

const { td } = useDefaultLang();
// State mapping table
const statusMap = {
  0: td('dpp.asset.detail.lineage.statusSubmitSuccess'),
  1: td('dpp.asset.detail.lineage.statusExecuting'),
  2: td('dpp.asset.detail.lineage.statusPreparingPause'),
  3: td('dpp.asset.detail.lineage.statusPaused'),
  4: td('dpp.asset.detail.lineage.statusPreparingStop'),
  5: td('dpp.asset.detail.lineage.statusStopped'),
  6: td('dpp.asset.detail.lineage.statusFailed'),
  7: td('dpp.asset.detail.lineage.statusSuccess'),
  12: td('dpp.asset.detail.lineage.statusDelayed'),
  14: td('dpp.asset.detail.lineage.statusSerialWait'),
  15: td('dpp.asset.detail.lineage.statusPreparingLock'),
  16: td('dpp.asset.detail.lineage.statusLocked'),
};
const taskTypeMap = {
  1: td('dpp.asset.detail.lineage.taskTypeIntegration'),
  2: td('dpp.asset.detail.lineage.taskTypeRealtime'),
  3: td('dpp.asset.detail.lineage.taskTypeDevelopment'),
  4: td('dpp.asset.detail.lineage.taskTypeJob')
};
function getTaskTypeText(type) {
  return taskTypeMap[String(type)] || '-';
}
// How to get status text
const getTaskStatusText = (status) => {
  return statusMap[status] ?? '-';
};
// Node type
const NodeType = {
  INPUT: "INPUT", // Data entry
  FILTER: "FILTER", // Data filtering
  JOIN: "JOIN", // data connection
  UNION: "UNION", // Data merge
  AGG: "AGG", // Data aggregation
  OUTPUT: "OUTPUT", // data output
};
// Icons for different node types
const NODE_TYPE_LOGO = {
  TABLE: new URL("@/assets/images/dpp/asset/img-icon-one-one.png", import.meta.url).href, // table
  TASK: new URL("@/assets/images/dpp/asset/img-icon-two-one.png", import.meta.url).href, // Task
};
// Element validation status
const CellStatus = {
  DEFAULT: "default",
  SUCCESS: "success",
  ERROR: "error",
};

// Processing type list
const PROCESSING_TYPE_LIST = [
  {
    type: "FILTER",
    name: td('dpp.asset.detail.lineage.dataFilter'),
  },
  {
    type: "JOIN",
    name: td('dpp.asset.detail.lineage.dataJoin'),
  },
  {
    type: "UNION",
    name: td('dpp.asset.detail.lineage.dataUnion'),
  },
  {
    type: "AGG",
    name: td('dpp.asset.detail.lineage.dataAgg'),
  },

  {
    type: "OUTPUT",
    name: td('dpp.asset.detail.lineage.dataOutput'),
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
 * According to the location information of the initial downstream node of the starting point
 * @param node starting node
 * @param graph
 * @returns
 */
const getDownstreamNodePosition = (node, graph, dx = 250, dy = 100) => {
  // Find the set of endpoint IDs of the relevant edges starting from the start node in the canvas
  const downstreamNodeIdList = [];
  graph.getEdges().forEach((edge) => {
    const originEdge = edge.toJSON()?.data;
    if (originEdge.source === node.id) {
      downstreamNodeIdList.push(originEdge.target);
    }
  });
  // Get the location information of the starting point
  const position = node.getPosition();
  let minX = Infinity;
  let maxY = -Infinity;
  graph.getNodes().forEach((graphNode) => {
    if (downstreamNodeIdList.indexOf(graphNode.id) > -1) {
      const nodePosition = graphNode.getPosition();
      // Find the x-coordinate of the leftmost node among all nodes
      if (nodePosition.x < minX) {
        minX = nodePosition.x;
      }
      // Find the y coordinate of the x-lowest node among all nodes
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

// Get ports based on node type
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
 * Create node and add to canvas
 * @param type node type
 * @param graph
 * @param position node position
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
 * Create edges and add to canvas
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
// Create downstream nodes and edges
const createDownstream = (type) => {
  const node = props.node;
  const { graph } = node.model || {};
  if (graph) {
    // Get the initial location information of downstream nodes
    const position = getDownstreamNodePosition(node, graph);
    // Create downstream nodes
    const newNode = createNode(type, graph, position);
    const source = node.id;
    const target = newNode.id;
    // Create an edge from this node to the downstream node
    createEdge(source, target, graph);
  }
};

// Click to add downstream + number
// eslint-disable-next-line no-unused-vars
const clickPlusDragMenu = (type) => {
  createDownstream(type);
};

// Expand
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
// When the mouse enters the main rectangular area, the connecting piles are displayed.
const onMainMouseEnter = () => {
  const node = props.node;
  // Get all connection piles under this node
  const ports = node.getPorts() || [];
  ports.forEach((port) => {
    node.setPortProp(port.id, "attrs/circle", {
      fill: "#fff",
      stroke: "#85A5FF",
    });
  });
};

// Hide the connection pile when the mouse leaves the main rectangular area
const onMainMouseLeave = () => {
  const node = props.node;
  // Get all connection piles under this node
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
