<template>
  <!-- 资产血缘 tab -->
  <div class="containerServer" v-loding="loding">
    <el-empty description=" 暂无资产血缘" v-if="noData">
    </el-empty>
    <div id="containerServer-container" ref="graphContainer"></div>
    <TeleportContainer />
  </div>
</template>

<script setup name="lineage">
import DataProcessingDagNode from "../components/lineageItem.vue";
import { Graph, Path, Edge, Platform } from "@antv/x6";
import { Selection } from "@antv/x6-plugin-selection";
import { register, getTeleport } from "@antv/x6-vue-shape";
import { dataLineage } from "@/api/da/asset/asset";
import { DagreLayout } from '@antv/layout';
const { proxy } = getCurrentInstance();
const props = defineProps({
  form1: {
    type: Object,
    default: () => ({}),
  },
});

let graph = null;

const initGraph = () => {
  graph = new Graph({
    container: proxy.$refs.graphContainer,
    width: 1644,
    height: 620,
    autoResize: true,
    interactive: true,
    panning: { enabled: true, eventTypes: ["leftMouseDown", "mouseWheel"] },
    mousewheel: { enabled: true, modifiers: "ctrl", factor: 1.1, maxScale: 1.5, minScale: 0.5 },
    highlighting: { magnetAdsorbed: { name: "stroke", args: { attrs: { fill: "#fff", stroke: "#31d0c6", strokeWidth: 4 } } } },
    interacting: { magnetConnectable: false },
    connecting: {
      snap: true,
      allowBlank: false,
      allowLoop: false,
      highlight: true,
      sourceAnchor: { name: "left", args: { dx: Platform.IS_SAFARI ? 4 : 8 } },
      targetAnchor: { name: "right", args: { dx: Platform.IS_SAFARI ? 4 : -8 } },
      createEdge() {
        return graph.createEdge({ shape: "data-processing-curve", attrs: { line: { strokeDasharray: "5 5" } }, zIndex: -1 });
      },
      validateConnection({ sourceMagnet, targetMagnet }) {
        if (!sourceMagnet || sourceMagnet.getAttribute("port-group") === "in") return false;
        if (!targetMagnet || targetMagnet.getAttribute("port-group") !== "in") return false;
        return true;
      },
    },
  });

  graph.use(new Selection({ multiple: true, rubberEdge: true, rubberNode: true, modifiers: "shift", rubberband: true }));
};

Graph.registerConnector("curveConnector", (sourcePoint, targetPoint) => {
  const hgap = Math.abs(targetPoint.x - sourcePoint.x);
  const offset = Math.max(hgap * 0.6, 80); // 控制曲线弯曲

  const path = new Path();
  // 起点直接用 sourcePoint
  path.appendSegment(Path.createSegment("M", sourcePoint.x, sourcePoint.y));
  // 曲线控制点
  path.appendSegment(Path.createSegment(
    "C",
    sourcePoint.x + offset,
    sourcePoint.y,
    targetPoint.x - offset,
    targetPoint.y,
    targetPoint.x,
    targetPoint.y
  ));

  return path.serialize();
}, true);
Edge.config({
  markup: [
    { tagName: "path", selector: "wrap", attrs: { fill: "none", cursor: "pointer", stroke: "transparent", strokeLinecap: "round" } },
    { tagName: "path", selector: "line", attrs: { fill: "none", pointerEvents: "none" } },
  ],
  connector: { name: "curveConnector" },
  attrs: { wrap: { connection: true, strokeWidth: 10, strokeLinejoin: "round" }, line: { connection: true, stroke: "#A2B1C3", strokeWidth: 1, targetMarker: { name: "classic", size: 6 } } },
});

Graph.registerEdge("data-processing-curve", Edge, true);

register({
  shape: "data-processing-dag-node",
  width: 180,
  height: 48,
  component: DataProcessingDagNode,
  ports: {
    groups: {
      in: { position: "left", attrs: { circle: { r: 4, magnet: true, stroke: "transparent", strokeWidth: 1, fill: "transparent", } } },
      out: { position: { name: "right", args: { dx: -32 } }, attrs: { circle: { r: 4, magnet: true, stroke: "transparent", strokeWidth: 1, fill: "transparent" } } },
    },
  },
});

const TeleportContainer = getTeleport();

// ====================== 节点/边状态 ======================
const nodeStatusList = [
  { id: "node-0", status: "success" },
  { id: "node-1", status: "success" },
  { id: "node-2", status: "success" },
  { id: "node-3", status: "success" },
  { id: "node-4", status: "error", statusMsg: "错误信息示例" },
];

const edgeStatusList = [
  { id: "edge-0", status: "success" },
  { id: "edge-1", status: "success" },
  { id: "edge-2", status: "success" },
  { id: "edge-3", status: "success" },
];

const showNodeStatus = () => {
  nodeStatusList.forEach((item) => {
    const node = graph.getCellById(item.id);
    if (!node) return;
    const data = node.getData();
    node.setData({ ...data, status: item.status, statusMsg: item.statusMsg });
  });
};

const excuteAnimate = () => {
  graph.getEdges().forEach((edge) => {
    edge.attr({ line: { stroke: "#3471F9" } });
    edge.attr("line/strokeDasharray", 5);
    edge.attr("line/style/animation", "running-line 30s infinite linear");
  });
};

const stopAnimate = () => {
  graph.getEdges().forEach((edge) => {
    edge.attr("line/strokeDasharray", 0);
    edge.attr("line/style/animation", "");
  });
  edgeStatusList.forEach((item) => {
    const edge = graph.getCellById(item.id);
    if (!edge) return;
    if (item.status === "success") edge.attr("line/stroke", "#52c41a");
    if (item.status === "error") edge.attr("line/stroke", "#ff4d4f");
  });
};
let noData = ref(false);
const loading = ref(false);
const getData = async () => {
  const defaultData = await dataLineage(props.form1.id);

  const nodes = [];
  const edges = [];
  const tasks = defaultData?.data?.tasks || [];
  const tables = defaultData?.data?.tables || [];
  const rels = defaultData?.data?.rels || [];

  if (tasks.length === 0 && tables.length === 0) {
    noData.value = true;       //
    graph.clearCells();
    loading.value = false;
    return;
  }
  loading.value = false;
  noData.value = false;
  defaultData.data.tasks.forEach(task => {
    nodes.push({
      id: String(task.id),
      shape: 'data-processing-dag-node',
      label: task.name,
      zIndex: 1,
      data: { ...task, type1: task.type, type: 'TASK', },
    });
  });

  defaultData.data.tables.forEach(table => {
    nodes.push({
      id: String(table.id),
      shape: 'data-processing-dag-node',
      label: table.name,
      zIndex: 1,
      data: { ...table, type: 'TABLE', },
    });
  });

  defaultData.data.rels.forEach((rel, idx) => {
    edges.push({
      id: `edge-${idx}`,
      shape: 'data-processing-curve',
      source: String(rel.startNodeId),
      target: String(rel.endNodeId),
      data: rel.properties,
      zIndex: -1
    });
  });
  nodes.forEach(node => {
    const nodeId = String(node.id)
    const hasChildFromRels = edges.some(e => String(e.source) === nodeId)

    let hasChildFromProps = false
    if (node.data.type === 'TABLE') {
      hasChildFromProps = Array.isArray(node.data.tableToTaskRels) && node.data.tableToTaskRels.length > 0
    } else if (node.data.type === 'TASK') {
      hasChildFromProps = Array.isArray(node.data.taskToTableRels) && node.data.taskToTableRels.length > 0
    }

    const hasChild = hasChildFromRels || hasChildFromProps
    if (hasChild) node.data.leaf = true

    node.data.collapsed = true
  })
  if (nodes.length === 1) {
    await graph.fromJSON({ nodes, edges });
    graph.zoomTo(1, { absolute: true, minScale: 0.5, maxScale: 1.5 });
    return;
  }

  const dagreLayout = new DagreLayout({
    type: 'dagre',
    rankdir: 'LR',
    nodesep: 50,
    ranksep: 80,
  });

  const layoutData = dagreLayout.layout({
    nodes: nodes.map(n => ({ id: n.id, width: 180, height: 48 })),
    edges: edges.map(e => ({ source: e.source, target: e.target })),
  });

  layoutData.nodes.forEach(pos => {
    const node = nodes.find(n => n.id === pos.id);
    if (node) {
      node.x = pos.x;
      node.y = pos.y;
      // highlighting: { magnetAdsorbed: { name: "stroke", args: { attrs: { fill: "#fff", stroke: "#31d0c6", strokeWidth: 4 } } } },
    }
  });
  await graph.fromJSON({ nodes, edges });
  if (nodes.length != 1) {
    graph.zoomToFit({ padding: 50 });
  }

  excuteAnimate();

  const activeNode = nodes.find(n => n.data.type === 'TASK');
  if (activeNode) graph.select(activeNode.id);

  setTimeout(() => {
    showNodeStatus();
    stopAnimate();
  }, 2000);
};



onMounted(() => {
  nextTick(() => {
    initGraph();
    getData();
  });
});
</script>

<style lang="scss">
@keyframes running-line {
  to {
    stroke-dashoffset: -1000;
  }
}
</style>

<style lang="scss" scoped>
.containerServer {
  width: 100%;
  height: 620px;

  #containerServer-container {
    width: 100%;
    height: 100%;
  }
}
</style>
