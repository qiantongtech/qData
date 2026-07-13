/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

/**
 * Store public methods for some node operations
 */
import { DataUri, Shape } from '@antv/x6';
import { History } from '@antv/x6-plugin-history';
import { Export } from '@antv/x6-plugin-export';
import { Selection } from '@antv/x6-plugin-selection';
import '@/assets/styles/system/global.scss'
import { cuPort } from '@/utils/graph';
import useUserStore from "@/store/system/user";

const userStore = useUserStore();
import {
  getNodeUniqueKey,
} from "@/api/dpp/task/index.js";
import sxPng from '@/assets/images/common/dpp/img-sx.png';
import xxPng from '@/assets/images/common/dpp/img-xx.png';

/**
 * Plug-in usage
 */
export const usePlugins = graph => {
  graph
    .use(
      new History({
        enabled: true
      })
    )
    .use(
      new Selection({
        enabled: true,
        rubberband: true,
        showNodeSelectionBox: true
      })
    )
    .use(new Export());
};
/**
 * Canvas scaling
 * @param {*} graph
 * @returns
 */
export const getCanvasScale = graph => {
  const scaleValue = graph.zoom();
  let result = parseFloat(scaleValue * 100).toFixed(0);
  return result;
};

/**
 * Show connection stubs on nodes
 * @param {*} ports
 * @param {*} show
 */
export const showPorts = (ports, show) => {
  for (let i = 0, len = ports.length; i < len; i = i + 1) {
    ports[i].style.visibility = show ? 'visible' : 'hidden';
  }
};
/**
 * Canvas clear
 */
export const handleRmNodes = graph => {
  graph.clearCells();
};


/**
 * Use graph.fromJSON to restore the data flow graph canvas processed by transNodeData
 */
export const renderGraph = (graph, savedData, width) => {
  graph.clearCells();
  savedData.locations.forEach((location) => {
    const nodeData = savedData.taskDefinitionList.find(item => item.code == location.taskCode);
    if (nodeData) {
      const node = graph.addNode({
        id: location.taskCode, // Use saved ID
        shape: 'cu-data-node',
        x: location.x,
        y: location.y,
        width: width || 170,
        height: 50,
        data: nodeData,
        ports: {
          ...cuPort,
          items: [
            { group: 'top', id: 'port-top' },
            { group: 'bottom', id: 'port-bottom' },
          ]
        }
      });
    }
  });

  // restore edge
  savedData.taskRelationJson.forEach((relation) => {
    if (relation.postNodeCode) {
      if (relation.preNodeCode != 0) {
        graph.addEdge({
          source: {
            cell: relation.preNodeCode,
            port: 'port-bottom',
          },
          target: {
            cell: relation.postNodeCode,
            port: 'port-top',
          },
          data: {
            sourceId: relation.preNodeCode,
            targetId: relation.postNodeCode
          },
          attrs: {
            line: {
              stroke: '#2666FB',  // edge color
              strokeWidth: 1,
              targetMarker: { name: 'block', width: 12, height: 8 },
            }
          },
        });
      }
    }
  });
};
export const fetchNodeUniqueKey = async () => {
  try {
    const response = await getNodeUniqueKey({
      projectCode: userStore.projectCode || "133545087166112",
      projectId: userStore.projectId,
    });
    if (response.code == '200') {
      return response.data;
    }
    return null; // If there is no data, return null
  } catch (error) {
    console.error("Error fetching node unique key:", error);
    return null; // Returns null on error
  }
};



let sxBase64 = '';
let xxBase64 = '';
function convertToBase64(src) {
  return new Promise((resolve, reject) => {
    DataUri.imageToDataUri(src, (err, dataUri) => {
      if (err) reject(err);
      else resolve(dataUri);
    });
  });
}
Promise.all([convertToBase64(sxPng), convertToBase64(xxPng)])
  .then(([sxDataUri, xxDataUri]) => {
    sxBase64 = sxDataUri;
    xxBase64 = xxDataUri;
  });

/**
 * Custom html node
 */
export const useHtmlNode = (node) => {
  Shape.HTML.register({
    shape: 'cu-data-node',
    width: 180,
    height: 60,
    html(cell) {
      const { name: nodeName, createPerson, icon, length, releaseState, taskParams = {} } = cell.getData();

      const htmlContainer = document.createElement('div');
      htmlContainer.setAttribute('class', 'cu_html_container');
      htmlContainer.style.position = 'relative'; // Absolute positioning reference

      const htmlTop = document.createElement('img');
      htmlTop.setAttribute('class', 'cu_html_top');

      let iconSrc = taskParams.icon || icon;

      // Process the icon, if it is base64, use it directly, otherwise convert to base64
      if (iconSrc && iconSrc.startsWith('data:image')) {
        htmlTop.src = iconSrc;
      } else if (iconSrc) {
        DataUri.imageToDataUri(iconSrc, (nu, url) => {
          htmlTop.src = url;
          // Update taskParams.icon
          cell.setData({
            ...cell.getData(),
            taskParams: { ...taskParams, icon: url },
          });
        });
      }

      const htmlText = document.createElement('div');
      htmlText.setAttribute('class', 'cu_html_text');

      if (taskParams.falg) {
        const tagImage = document.createElement('img');
        tagImage.setAttribute('class', 'cu_html_tag_image');
        const isOffline = releaseState == '-2' || releaseState == '0';

        // Directly use the base64 icon converted in advance to ensure that the image is displayed when exporting
        tagImage.src = isOffline ? xxBase64 : sxBase64;
        tagImage.style.position = 'absolute';
        tagImage.style.right = '-3px';
        tagImage.style.top = '0px';

        htmlText.appendChild(tagImage);
      }

      const htmlTitle = document.createElement('div');
      htmlTitle.setAttribute('class', 'cu_html_title');
      htmlTitle.innerText = nodeName;

      htmlText.appendChild(htmlTitle);
      htmlContainer.appendChild(htmlTop);
      htmlContainer.appendChild(htmlText);

      return htmlContainer;
    },
  });
};

/**
 * Update online and offline status
 */
export const updateNodeImage = (cell, releaseState) => {
  const currentData = cell.getData();
  const newData = {
    ...currentData,
    releaseState,
    taskParams: {
      ...currentData.taskParams,
      imagePath: releaseState == '-2' || releaseState == '0' ? xxPng : sxPng,
    },
  };
  cell.setData(newData);
};


export const waitForAllImagesLoaded = (container) => {
  const images = container.querySelectorAll('img')
  const promises = []

  images.forEach((img) => {
    if (img.complete && img.naturalHeight !== 0) {
      return
    }
    promises.push(
      new Promise((resolve) => {
        img.onload = resolve
        img.onerror = resolve
      })
    )
  })

  return Promise.all(promises)
}

export const exportGraphAsPNG = async (
  graph,
  {
    fileName = "流程图",
    width = 1920,
    height = 1080,
    padding = 20,
    quality = 1,
    stylesheet,
  } = {}
) => {
  if (!graph) {
    console.warn("exportGraphAsPNG: graph instance not found");
    return;
  }
  const container = graph.container;
  await waitForAllImagesLoaded(container);
  const defaultStylesheet = `
.cu_html_container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  background: white;
  border: 1px solid #ddd;
  border-radius: 2px;
  padding: 8px;
  height: 33px !important;
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  overflow: hidden;
}

.cu_html_top {
  width: 30px;
  height: 30px;
  margin-right: 10px;
}

.cu_html_text {
  display: flex;
  flex-direction: row;
  align-items: center;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cu_html_title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cu_html_tag {
  position: absolute;
  top: 0px;
  right: 5px;
  width: 50px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  font-size: 12px;
  font-weight: bold;
  color: white;
  transform: skewX(-20deg);
  border-radius: 4px;
}
`;

  await graph.exportPNG(fileName, {
    width,
    height,
    padding,
    quality,
    copyStyles: true,
    stylesheet: stylesheet || defaultStylesheet,
  });
};

