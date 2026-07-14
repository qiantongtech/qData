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
    <el-dialog v-model="visible" :title="td('dpp.taskLog.taskExecutionLog', '任务执行日志')" :draggable="true" class="medium-dialog" @close="handleClose">
        <div class="graph-log-container" ref="containerRef" v-loading="loading">
            <!-- 上方 X6 图 -->
            <div class="graph-container" ref="graphRef" :style="{ height: graphHeight + 'px' }"></div>
            <TeleportContainer />
            <!-- 分隔条 -->
            <div class="divider" @mousedown="startDrag"></div>
            <!-- 下方日志 -->
            <div class="log-container" :style="{ height: logHeight + 'px' }">
                <div v-if="hasLogSummary" class="log-summary">
                    <div class="summary-item">
                        <span>{{ td('dpp.taskLog.inputRecords', '输入') }}</span>
                        <strong>{{ displayMetric(logSummary.inputRecords) }}</strong>
                    </div>
                    <div class="summary-item">
                        <span>{{ td('dpp.taskLog.outputRecords', '输出') }}</span>
                        <strong>{{ displayMetric(logSummary.outputRecords) }}</strong>
                    </div>
                    <div class="summary-item summary-node">
                        <span>{{ td('dpp.taskLog.nodeNames', '节点') }}</span>
                        <strong>{{ displayNodes(logSummary.nodeNames) }}</strong>
                    </div>
                    <div class="summary-item">
                        <span>{{ td('dpp.taskLog.successCount', '成功') }}</span>
                        <strong>{{ displayMetric(logSummary.successCount) }}</strong>
                    </div>
                    <div class="summary-item">
                        <span>{{ td('dpp.taskLog.failedCount', '失败') }}</span>
                        <strong>{{ displayMetric(logSummary.failedCount) }}</strong>
                    </div>
                    <div class="summary-item">
                        <span>{{ td('dpp.taskLog.stoppedCount', '停止') }}</span>
                        <strong>{{ displayMetric(logSummary.stoppedCount) }}</strong>
                    </div>
                </div>
                <el-scrollbar :style="{ height: logBodyHeight + 'px' }">
                    <pre class="log-text">{{ logContent }}</pre>
                </el-scrollbar>
            </div>
        </div>
        <template #footer>
            <div style="text-align: right">
                <el-button @click="handleClose">{{ td('common.button.close', '关闭') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, computed, onBeforeUnmount, nextTick, defineComponent } from "vue";
import { Graph } from "@antv/x6";
import { ElMessage } from "element-plus";
import NodeView from "@/views/dpp/components/nodeView";
import { getLogByTaskInstanceId, getTaskInfo } from "@/api/dpp/task/etlTask";
import { register, getTeleport } from "@antv/x6-vue-shape";
import { baseConfig, cuPort } from "@/utils/graph";
import { DagreLayout } from '@antv/layout';

const { td } = useDefaultLang();
const TeleportContainer = defineComponent(getTeleport());

// 状态变量
const visible = ref(false);
const containerRef = ref(null);
const graphRef = ref(null);
const logContent = ref("");
const logSummary = ref({});
const polling = ref(false);
const graphHeight = ref(450);
const logHeight = ref(300);
let graph = null;

// 拖拽调整高度
let startY = 0;
let startGraphHeight = 0;

const hasLogSummary = computed(() => {
    const summary = logSummary.value || {};
    // DataX 会返回输入输出量、成功失败数这些摘要；普通 DS 日志没有这些字段。
    return [
        summary.inputRecords,
        summary.outputRecords,
        summary.successCount,
        summary.failedCount,
        summary.stoppedCount,
    ].some((item) => item !== undefined && item !== null) || (Array.isArray(summary.nodeNames) && summary.nodeNames.length > 0);
});

const logBodyHeight = computed(() => Math.max(logHeight.value - (hasLogSummary.value ? 48 : 0), 120));

const displayMetric = (value) => {
    return value === undefined || value === null || value === "" ? "-" : value;
};

const displayNodes = (nodeNames) => {
    return Array.isArray(nodeNames) && nodeNames.length > 0 ? nodeNames.join(" / ") : "-";
};

const updateLogSummary = (data = {}) => {
    // 后端返回什么摘要就展示什么，没有值时页面继续只显示原始日志。
    logSummary.value = {
        inputRecords: data.inputRecords,
        outputRecords: data.outputRecords,
        nodeNames: data.nodeNames,
        successCount: data.successCount,
        failedCount: data.failedCount,
        stoppedCount: data.stoppedCount,
    };
};

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
    updateLogSummary(res.data);
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
    logSummary.value = {};
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

.log-summary {
    display: grid;
    grid-template-columns: 88px 88px minmax(160px, 1fr) 76px 76px 76px;
    align-items: center;
    gap: 8px;
    min-height: 40px;
    padding: 6px 10px;
    border-bottom: 1px solid #1f2937;
    background: #111827;
    color: #d1d5db;
    font-family: Arial, sans-serif;
}

.summary-item {
    min-width: 0;
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    line-height: 18px;
}

.summary-item span {
    color: #9ca3af;
    white-space: nowrap;
}

.summary-item strong {
    min-width: 0;
    color: #f9fafb;
    font-size: 13px;
    font-weight: 600;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.summary-node {
    min-width: 0;
}

.log-text {
    margin: 0;
    padding: 10px;
    white-space: pre-wrap;
    word-wrap: break-word;
}

@media (max-width: 900px) {
    .log-summary {
        grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    .summary-node {
        grid-column: 1 / -1;
    }
}
</style>
