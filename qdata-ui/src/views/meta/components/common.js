import { Graph, Platform, Edge } from '@antv/x6';
import { register, getTeleport } from '@antv/x6-vue-shape';
import { Selection } from '@antv/x6-plugin-selection';
import TableNode from './TableNode.vue';
import TableInfoNode from './TableInfoNode.vue';
import FieldNode from './FieldNode.vue';
import DatabaseNode from './DatabaseNode.vue';

// 设置默认配置
export function setupDefaultConfig() {
    register({
        shape: 'FieldNode',
        width: 240,
        height: 40,
        component: FieldNode,
        ports: {
            groups: {
                top: {
                    position: 'top',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                right: {
                    position: 'right',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                bottom: {
                    position: 'bottom',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                left: {
                    position: 'left',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                }
            },
            items: [
                { id: 'top', group: 'top' },
                { id: 'right', group: 'right' },
                { id: 'bottom', group: 'bottom' },
                { id: 'left', group: 'left' }
            ]
        }
    });
    register({
        shape: 'TableInfoNode',
        width: 240,
        height: 40,
        component: TableInfoNode,
        ports: {
            groups: {
                top: {
                    position: 'top',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                right: {
                    position: 'right',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                bottom: {
                    position: 'bottom',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                left: {
                    position: 'left',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                }
            },
            items: [
                { id: 'top', group: 'top' },
                { id: 'right', group: 'right' },
                { id: 'bottom', group: 'bottom' },
                { id: 'left', group: 'left' }
            ]
        }
    });

    register({
        shape: 'TableNode',
        width: 240,
        height: 144,
        component: TableNode,
        ports: {
            groups: {
                top: {
                    position: 'top',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                right: {
                    position: 'right',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                bottom: {
                    position: 'bottom',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                left: {
                    position: 'left',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                }
            },
            items: [
                { id: 'top', group: 'top' },
                { id: 'right', group: 'right' },
                { id: 'bottom', group: 'bottom' },
                { id: 'left', group: 'left' }
            ]
        }
    });

    register({
        shape: 'DatabaseNode',
        width: 240,
        height: 144,
        component: DatabaseNode,
        ports: {
            groups: {
                top: {
                    position: 'top',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                right: {
                    position: 'right',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                bottom: {
                    position: 'bottom',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                },
                left: {
                    position: 'left',
                    attrs: {
                        circle: { r: 4, magnet: true, stroke: '#1890ff', fill: '#fff' }
                    }
                }
            },
            items: [
                { id: 'top', group: 'top' },
                { id: 'right', group: 'right' },
                { id: 'bottom', group: 'bottom' },
                { id: 'left', group: 'left' }
            ]
        }
    });

    Graph.registerConnector(
        'curveConnector',
        (sourcePoint, targetPoint) => {
            const hgap = Math.abs(targetPoint.x - sourcePoint.x);
            const offset = Math.max(hgap * 0.6, 80); // 控制曲线弯曲

            const path = new Path();
            // 起点直接用 sourcePoint
            path.appendSegment(Path.createSegment('M', sourcePoint.x, sourcePoint.y));
            // 曲线控制点
            path.appendSegment(
                Path.createSegment(
                    'C',
                    sourcePoint.x + offset,
                    sourcePoint.y,
                    targetPoint.x - offset,
                    targetPoint.y,
                    targetPoint.x,
                    targetPoint.y
                )
            );

            return path.serialize();
        },
        true
    );
    Edge.config({
        markup: [
            {
                tagName: 'path',
                selector: 'wrap',
                attrs: {
                    fill: 'none',
                    cursor: 'pointer',
                    stroke: 'transparent',
                    strokeLinecap: 'round'
                }
            },
            {
                tagName: 'path',
                selector: 'line',
                attrs: { fill: 'none', pointerEvents: 'none' }
            }
        ],
        connector: { name: 'curveConnector' },
        attrs: {
            wrap: { connection: true, strokeWidth: 10, strokeLinejoin: 'round' },
            line: {
                connection: true,
                stroke: '#A2B1C3',
                strokeWidth: 1,
                targetMarker: { name: 'classic', size: 6 }
            }
        }
    });
    // 注销边，防止重复注册报错
    Graph.unregisterEdge('er-relationship');
    // 注册边
    Graph.registerEdge('er-relationship', {
        inherit: 'edge',
        attrs: {
            line: {
                stroke: '#666',
                strokeWidth: 2,
                targetMarker: { name: 'classic', size: 8 }
            }
        }
    });
}

setupDefaultConfig();
// 创建并配置画布
export function createBaseGraph(container) {
    const graph = new Graph({
        container,
        mousewheel: {
            enabled: true,
            modifiers: 'ctrl',
            factor: 1.1,
            maxScale: 1.5,
            minScale: 0.5
        },
        highlighting: {
            magnetAdsorbed: {
                name: 'stroke',
                args: { attrs: { fill: '#fff', stroke: '#31d0c6', strokeWidth: 4 } }
            }
        },
        interacting: { magnetConnectable: false },
        connecting: {
            snap: true,
            allowBlank: false,
            allowLoop: false,
            highlight: true,
            sourceAnchor: { name: 'left', args: { dx: Platform.IS_SAFARI ? 4 : 8 } },
            targetAnchor: {
                name: 'right',
                args: { dx: Platform.IS_SAFARI ? 4 : -8 }
            },
            createEdge() {
                return graph.createEdge({
                    shape: 'er-relationship',
                    attrs: { line: { strokeDasharray: '5 5' } },
                    zIndex: -1
                });
            },
            validateConnection({ sourceMagnet, targetMagnet }) {
                if (!sourceMagnet || sourceMagnet.getAttribute('port-group') === 'in') return false;
                if (!targetMagnet || targetMagnet.getAttribute('port-group') !== 'in') return false;
                return true;
            }
        },
        background: {
            color: '#F2F7FA'
        },
        grid: { visible: true, type: 'dot', args: { color: '#e0e0e0' } }
    });
    graph.use(
        new Selection({
            multiple: true,
            rubberEdge: true,
            rubberNode: true,
            modifiers: 'shift',
            rubberband: true
        })
    );
    return { graph };
}
