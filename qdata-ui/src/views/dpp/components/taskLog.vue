<template>
    <el-dialog v-model="visible" title="任务执行日志" :draggable="true" class="medium-dialog" @close="handleClose">
        <div class="graph-log-container" ref="containerRef" v-loading="loading">
            <!-- 上方 X6 图 -->
            <div class="graph-container" ref="graphRef" :style="{ height: graphHeight + 'px' }"></div>
            <TeleportContainer />
            <!-- 分隔条 -->
            <div class="divider" @mousedown="startDrag"></div>
            <!-- 下方日志 -->
            <div class="log-container" :style="{ height: logHeight + 'px' }">
                <el-scrollbar :style="{ height: logHeight + 'px' }">
                    <pre class="log-text">{{ logContent }}</pre>
                </el-scrollbar>
            </div>
        </div>
        <template #footer>
            <div style="text-align: right">
                <el-button @click="handleClose">关闭</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, onBeforeUnmount, nextTick, defineComponent } from "vue";
import { Graph } from "@antv/x6";
import { ElMessage } from "element-plus";
import NodeView from "@/views/dpp/components/nodeView";
import { getLogByTaskInstanceId, getTaskInfo } from "@/api/dpp/task/etlTask";
import { register, getTeleport } from "@antv/x6-vue-shape";
import { baseConfig, cuPort } from "@/utils/graph";
import { DagreLayout } from '@antv/layout';
const TeleportContainer = defineComponent(getTeleport());

// 状态变量
const visible = ref(false);
const containerRef = ref(null);
const graphRef = ref(null);
const logContent = ref("");
const polling = ref(false);
const graphHeight = ref(450);
const logHeight = ref(300);
let graph = null;

// 拖拽调整高度
let startY = 0;
let startGraphHeight = 0;

const startDrag = (e) => {
    e.preventDefault();
    startY = e.clientY;
    startGraphHeight = graphHeight.value;
    document.addEventListener("mousemove", onDrag);
    document.addEventListener("mouseup", stopDrag);
};

const onDrag = (e) => {
    const dy = e.clientY - startY;
    if (!containerRef.value) return;
    const containerHeight = containerRef.value.clientHeight;
    const newGraphHeight = startGraphHeight + dy;
    if (newGraphHeight < 100 || newGraphHeight > containerHeight - 100) return;
    graphHeight.value = newGraphHeight;
    logHeight.value = containerHeight - newGraphHeight - 8;
    resizeGraphHeight();
};

const stopDrag = () => {
    document.removeEventListener("mousemove", onDrag);
    document.removeEventListener("mouseup", stopDrag);
};

// 调整 graph 高度
const resizeGraphHeight = () => {
    nextTick(() => {
        if (graph && graphRef.value) {
            graph.resize(1120, graphHeight.value);
        }
    });
};

// 初始化 X6 图
const initGraph = () => {
    if (!graph) {
        register({
            shape: "vue-node",
            component: NodeView,
            getComponentProps(node) {
                return { getNode: () => node };
            },
        });

        graph = new Graph({
            container: graphRef.value,
            background: { color: "#f5f5f5" },
            autoResize: false,
            panning: true,
            ...baseConfig,
            mousewheel: { enabled: true, zoomAtMousePosition: true, minScale: 0.5, maxScale: 3 },
        });
    }
};



const renderGraph = (graph, savedData) => {
    if (!graph) return;
    graph.clearCells();
    if (!savedData) return;

    const taskList = Array.isArray(savedData.taskDefinitionList)
        ? savedData.taskDefinitionList
        : [];
    const relations = Array.isArray(savedData.taskRelationJson)
        ? savedData.taskRelationJson
        : [];

    // 节点
    const layoutNodes = taskList.map((task) => ({
        id: String(task.code), // 强制转成字符串
        width: 36,
        height: 40,
        data: task,
    }));

    // 边，过滤条件改为严格判断 null/undefined
    const layoutEdges = relations
        .filter(
            (rel) =>
                rel &&
                rel.preNodeCode != null &&
                rel.postNodeCode != null &&
                String(rel.preNodeCode) !== '0'
        )
        .map((rel) => ({
            source: String(rel.preNodeCode),
            target: String(rel.postNodeCode),
        }));

    // Dagre 布局
    const dagreLayout = new DagreLayout({
        type: 'dagre',
        rankdir: 'LR',
        nodesep: 50,
        ranksep: 80,
    });

    dagreLayout.layout({
        nodes: layoutNodes,
        edges: layoutEdges,
    });

    // 添加节点
    layoutNodes.forEach((n) => {
        graph.addNode({
            id: n.id,
            shape: 'vue-node',
            component: NodeView,
            x: n.x || 0,
            y: n.y || 0,
            width: n.width,
            height: n.height,
            data: n.data,
            attrs: {
                body: { stroke: '#D3D8EA', strokeWidth: 1 },
            },
            ports: {
                ...cuPort,
                items: [
                    { group: 'left', id: 'port-left' },
                    { group: 'right', id: 'port-right' },
                ],
            },
        });
    });

    // 添加边
    layoutEdges.forEach((e) => {
        // 先确认节点存在再添加
        const sourceNode = graph.getCellById(e.source);
        const targetNode = graph.getCellById(e.target);
        if (!sourceNode || !targetNode) return;

        graph.addEdge({
            source: { cell: e.source, port: 'port-right' },
            target: { cell: e.target, port: 'port-left' },
            attrs: {
                line: {
                    stroke: '#D3D8EA',
                    strokeWidth: 1,
                    targetMarker: { name: 'block', width: 12, height: 8 },
                },
            },
        });
    });
};


// 更新节点状态
const updateGraphNodes = (graph, nodeInstanceList) => {
    if (!graph || !Array.isArray(nodeInstanceList)) return;
    const codeNodeMap = {};
    graph.getNodes().forEach((node) => {
        const code = node.getData()?.code;
        if (code) codeNodeMap[String(code)] = node;
    });
    nodeInstanceList.forEach((inst) => {
        const node = codeNodeMap[String(inst.nodeCode)];
        if (node) {
            const oldData = node.getData() || {};
            node.setData({ ...oldData, status: inst.status });
        }
    });
};

// 获取任务数据
const getTask = async (taskId) => {
    const res = await getTaskInfo(taskId);
    renderGraph(graph, res.data);
    return res.data;
};

// 轮询日志
const fetchLog = async (taskId) => {
    if (!polling.value) return;
    const res = await getLogByTaskInstanceId({ taskInstanceId: taskId });
    const { status, log, nodeInstanceList } = res.data;
    logContent.value = log;
    updateGraphNodes(graph, nodeInstanceList);
    const s = Number(status);
    if ([5, 6, 7].includes(s)) {
        polling.value = false;
        return;
    }
    if (polling.value) setTimeout(() => fetchLog(taskId), 3000);
};
let loading = ref(false)
// 打开弹窗
const open = async (taskId) => {
    loading.value = true;
    visible.value = true;
    await nextTick();
    initGraph();
    const taskData = await getTask(taskId);
    updateGraphNodes(graph, taskData.nodeInstanceList);
    polling.value = true;
    await fetchLog(taskId);
    loading.value = false;

};

// 关闭弹窗
const handleClose = () => {
    visible.value = false;
    polling.value = false;
    logContent.value = "";
    if (graph) {
        graph.getEdges().forEach((e) => e.remove());
        graph.getNodes().forEach((n) => n.remove());
    }
};

// 窗口 resize
const handleResize = () => {
    if (!graph || !graphRef.value || !containerRef.value) return;
    const containerHeight = containerRef.value.clientHeight;
    logHeight.value = containerHeight - graphHeight.value - 8;
    resizeGraphHeight();
};

window.addEventListener("resize", handleResize);
onBeforeUnmount(() => {
    polling.value = false;
    window.removeEventListener("resize", handleResize);
});

defineExpose({ open });
</script>

<style scoped>
.graph-log-container {
    display: flex;
    flex-direction: column;
    gap: 8px;
    height: 660px;
}

.graph-container {
    border: 1px solid #ebeef5;
    border-radius: 3px;
}

.divider {
    height: 2px;
    cursor: row-resize;
    background-color: #ebeef5;

}

.log-container {
    border: 1px solid #ebeef5;
    border-radius: 3px;
    background: #000;
    color: #0f0;
    font-family: monospace;
    overflow: hidden;
}

.log-text {
    white-space: pre-wrap;
    word-wrap: break-word;
}
</style>
