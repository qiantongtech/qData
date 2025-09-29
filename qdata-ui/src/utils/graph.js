import { Shape } from '@antv/x6';
import { Keyboard } from '@antv/x6-plugin-keyboard'
/**
 * antv x6的所有配置项整合
 */
export const baseConfig = {
  // 设置节点移动的范围在画布内
  translating: {
    restrict: true
  },
  grid: false,
  background: {
    color: 'transparent'
  },
  mousewheel: { enabled: true, pointerEvents: true, showNodeSelectionBox: true, pointerEvents: "none", zoomAtMousePosition: true, modifiers: 'ctrl', minScale: 0.5, maxScale: 3 },
  scaling: {
    min: 0.5,
    max: 2
  },
  panning: {
    enabled: true,
    modifiers: 'alt'
  },
  connecting: {
    router: {
      name: 'manhattan',
      args: {
        padding: 10,  // 控制边缘与节点之间的间距
        avoid: true,   // 启用避开节点功能
      }
    },
    connector: {
      name: 'rounded',
      args: {
        radius: 8
      }
    },
    snap: true,
    connectionPoint: 'anchor',
    allowBlank: false,  // 禁止连接到空白区域
    allowEdge: false,   // 禁止边连接到另一条边
    allowNode: false,   // 禁止边连接到节点的中心
    allowPort: true,    // 只允许连接到连接桩
    highlight: true,
    snap: {
      radius: 20
    },
    createEdge() {
      return new Shape.Edge({
        attrs: {
          line: {
            stroke: '#2666FB',
            strokeWidth: 1,
            targetMarker: {
              name: 'block',
              width: 12,
              height: 8
            }
          }
        },
      });
    },
  },
  highlighting: {
    magnetAdsorbed: {
      name: 'stroke',
      args: {
        attrs: {
          fill: '#028FA6',
          stroke: '#028FA6'
        }
      }
    }
  },
  edgeAvailable: {
    name: 'stroke',
    args: {
      padding: 4,
      color: '#ff0000',
      width: 3,
    },
  },
  interacting: function (cellView) {
    return true;
  },
  // 快捷键绑定
  bindShortcuts(graph) {
    // graph.use(new Keyboard());
    graph.bindKey(['delete', 'backspace'], () => {
      const cells = graph.getSelectedCells();
      if (cells.length > 0) {
        graph.removeCells(cells);
      }
      return false;
    });

  }
};

// 公用的连接桩
export const cuPort = {
  groups: {
    top: {
      position: 'top',
      attrs: {
        circle: {
          r: 7,
          magnet: true,
          stroke: '#5F95FF',
          strokeWidth: 1,
          fill: '#fff',
          style: {
            visibility: 'hidden',
          }
        }
      }
    },
    right: {

      position: 'right',
      attrs: {
        circle: {
          r: 7,
          magnet: true,
          stroke: '#5F95FF',
          strokeWidth: 1,
          fill: '#fff',
          style: {
            visibility: 'hidden',
          }
        }
      }
    },
    bottom: {
      position: 'bottom',
      attrs: {
        circle: {
          r: 7,
          magnet: true,
          stroke: '#5F95FF',
          strokeWidth: 1,
          fill: '#fff',
          style: {
            visibility: 'hidden',
          }
        }
      }
    },
    left: {
      position: 'left',
      attrs: {
        circle: {
          r: 7,
          magnet: true,
          stroke: '#5F95FF',
          strokeWidth: 1,
          fill: '#fff',
          style: {
            visibility: 'hidden',
          }
        }
      }
    },
  },
  items: [{ group: 'top' }, { group: 'right' }, { group: 'bottom' }, { group: 'left' }]
};

// 节点的类型
export const typeList = [
  { value: 1, label: '输入组件' },
  { value: 2, label: '输出组件' },
  { value: 3, label: '转换组件' }
];
export const toolbar = ref([
  {
    id: "zoom-out",
    icon: "toolbar (1).png",
    tip: "缩小",
  },
  {
    id: "zoom-in",
    icon: "toolbar (8).png",
    tip: "放大",
  },
  // {
  //   id: "full-screen",
  //   icon: "toolbar (7).png",
  //   isFull: false,
  //   tip: "全屏",
  // },
  // {
  //   id: "undo",
  //   icon: "toolbar (6).png",
  //   tip: "撤销",
  // },
  // {
  //   id: "redo",
  //   icon: "toolbar (5).png",
  //   tip: "重做",
  // },
  {
    id: "auto-fit",
    icon: "toolbar (4).png",
    tip: "重置视角",
  },
  {
    id: "export",
    icon: "toolbar (2).png",
    tip: "导出",
  },
  {
    id: "reset",
    icon: "toolbar (3).png",
    tip: "重置",
  },
]);
