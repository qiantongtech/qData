/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

import { i18n } from '@/plugins/vueI18n';
import { Shape } from '@antv/x6';
import { Keyboard } from '@antv/x6-plugin-keyboard'
/**
 * Integration of all configuration items of antv x6
 */
export const baseConfig = {
  // Set the range of node movement within the canvas
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
        padding: 10,  // Control the spacing between edges and nodes
        avoid: true,   // Enable node avoidance
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
    allowBlank: false,  // Disable connections to white space
    allowEdge: false,   // Disallow an edge from connecting to another edge
    allowNode: false,   // Disallow edges from connecting to the center of a node
    allowPort: true,    // Only allowed to connect to connecting posts
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
  // Shortcut key bindings
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

// public connecting pile
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

// node type
export const typeList = [
  { value: 1, label:  i18n.global.t('common.graph.inputComponent') },
  { value: 2, label:  i18n.global.t('common.graph.outputComponent') },
  { value: 3, label:  i18n.global.t('common.graph.transformComponent') }
];
export const toolbar = [
  {
    id: "zoom-out",
    icon: "icon-toolbar.png",
    tip:  i18n.global.t('common.graph.zoomOut'),
  },
  {
    id: "zoom-in",
    icon: "icon-toolbar-seven.png",
    tip:  i18n.global.t('common.graph.zoomIn'),
  },
  // {
  //   id: "full-screen",
  //   icon: "toolbar (7).png",
  //   isFull: false,
  //   tip: "full screen",
  // },
  // {
  //   id: "undo",
  //   icon: "toolbar (6).png",
  //   tip: "Undo",
  // },
  // {
  //   id: "redo",
  //   icon: "toolbar (5).png",
  //   tip: "redo",
  // },
  {
    id: "auto-fit",
    icon: "icon-toolbar-three.png",
    tip:  i18n.global.t('common.graph.resetView'),
  },
  {
    id: "export",
    icon: "icon-toolbar-one.png",
    tip:  i18n.global.t('common.button.export'),
  },
  {
    id: "reset",
    icon: "icon-toolbar-two.png",
    tip:  i18n.global.t('common.button.reset'),
  },
];
