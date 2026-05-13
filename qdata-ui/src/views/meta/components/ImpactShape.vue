<template>
    <div class="impact-shape">
        <div class="shape-wrap" id="impact_shape"></div>
        <TeleportContainer />
    </div>
</template>

<script setup name="ImpactShape">
    import { getTeleport } from '@antv/x6-vue-shape';
    import { createBaseGraph } from './common';
    import { onMounted } from 'vue';

    const props = defineProps({
        origin: {
            type: Object,
            required: true
        },
        targets: {
            type: Array,
            required: true
        },
        type: {
            type: String,
            default: 'Table'
        }
    });

    const TeleportContainer = defineComponent(getTeleport());

    // setupDefaultConfig();

    // 获取节点以及连线
    function getTableShapes() {
        const { origin } = props;
        const layout = {
            nodeWidth: 240,
            headerHeight: 40,
            fieldHeight: 28,
            sY: 100,
            sX: 300,
            height: 0,
            originHeight: 70
        };
        const edges = [];
        const nodes = [];
        props.targets.forEach((data) => {
            const fieldsHeight = data.fields.length * layout.fieldHeight;
            nodes.push({
                id: data.id,
                shape: data.shape,
                width: layout.nodeWidth,
                height: layout.headerHeight + fieldsHeight,
                data,
                x: layout.nodeWidth + layout.sX,
                y: layout.height
            });
            layout.height += layout.headerHeight + fieldsHeight + layout.sY;
            edges.push({
                id: origin.id + '_' + data.id,
                shape: 'er-relationship',
                source: { cell: origin.id, port: 'right' },
                target: { cell: data.id, port: 'left' }
            });
        });
        nodes.push({
            id: origin.id,
            shape: origin.shape,
            width: layout.nodeWidth,
            height: layout.originHeight,
            data: { ...origin },
            x: 0,
            y: layout.height / 2 - layout.originHeight / 2
        });
        return { nodes, edges };
    }

    function getFieldShapes() {
        const { target } = props;
        const layout = {
            nodeWidth: 240,
            nodeHeight: 36,
            sY: 100,
            sX: 300,
            height: 0
        };
        const edges = [];
        const nodes = [];
        props.origins.forEach((data) => {
            nodes.push({
                id: data.id,
                shape: data.shape,
                width: layout.nodeWidth,
                height: layout.nodeHeight,
                data,
                x: 0,
                y: layout.height
            });
            layout.height += layout.sY;
            edges.push({
                id: target.id + '_' + data.id,
                shape: 'er-relationship',
                source: { cell: data.id, port: 'right' },
                target: { cell: target.id, port: 'left' }
            });
        });
        nodes.push({
            id: target.id,
            shape: target.shape,
            width: layout.nodeWidth,
            height: layout.nodeHeight,
            data: { ...target },
            x: layout.nodeWidth + layout.sX,
            y: layout.height / 2 - layout.nodeHeight / 2
        });
        return { nodes, edges };
    }

    onMounted(() => {
        const container = document.getElementById('impact_shape');
        const { graph } = createBaseGraph(container);
        graph.clearCells();
        const fn = props.type == 'Table' ? getTableShapes : getFieldShapes;
        const { nodes, edges } = fn();
        graph.addNodes(nodes);
        graph.addEdges(edges);
        // 位置设置
        graph.centerContent();
        graph.zoomToFit({
            padding: {
                top: 40
            }
        });
        const zoom = graph.zoom();
        if (zoom > 1) {
            graph.zoomTo(1);
        }
    });
</script>

<style lang="scss" scoped>
    .impact-shape {
        width: 100%;
        height: 100%;

        .shape-wrap {
            width: 100%;
            height: 100%;
        }

        ::v-deep(.x6-port-body) {
            opacity: 0;
            transition: opacity 0.2s ease;
        }

        ::v-deep(.x6-node) {
            &:hover .x6-port-body {
                opacity: 1;
            }
        }
    }
</style>
