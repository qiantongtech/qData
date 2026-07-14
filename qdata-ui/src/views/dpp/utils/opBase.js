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
import { DataUri, Shape } from "@antv/x6";
import { History } from "@antv/x6-plugin-history";
import { Export } from "@antv/x6-plugin-export";
import { Selection } from "@antv/x6-plugin-selection";
import "@/assets/styles/system/global.scss";
import { cuPort } from "@/utils/graph";
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import { getNodeUniqueKey } from "@/api/dpp/task/etlTask";
import { ElMessage } from "element-plus";
import { td } from "@/utils/i18n";
import { DagreLayout } from '@antv/layout';
import { register } from '@antv/x6-vue-shape';
import NodeView from "@/views/dpp/components/nodeView";
/**
 * Plug-in usage
 */
export const usePlugins = (graph) => {
  graph
    .use(
      new History({
        enabled: true,
      })
    )
    .use(
      new Selection({
        enabled: true,
        rubberband: true,
        showNodeSelectionBox: true,
      })
    )
    .use(new Export());
};
/**
 * Canvas scaling
 * @param {*} graph
 * @returns
 */
export const getCanvasScale = (graph) => {
  const scaleValue = graph.zoom();
  let result = parseFloat(scaleValue * 100).toFixed(0);
  return result;
};
/**
 * Custom html node
 */
export const useHtmlNode = (node) => {
  Shape.HTML.register({
    shape: "cu-data-node",
    width: 180,
    height: 60,
    html(cell) {
      const { name: nodeName, createPerson, icon, length, releaseState, taskParams } = cell.getData();
      const htmlContainer = document.createElement("div");
      htmlContainer.setAttribute("class", "cu_html_container");
      const htmlTop = document.createElement("img");
      htmlTop.setAttribute("class", "cu_html_top");
      // Determine icon source
      let iconSrc = taskParams.icon || icon || cell.getData().icons;
      // Check whether the icon is base64, if so, use it directly
      if (iconSrc && iconSrc.startsWith("data:image")) {
        htmlTop.setAttribute("src", iconSrc);
      } else if (iconSrc) {
        DataUri.imageToDataUri(iconSrc, function (nu, url) {
          htmlTop.src = url;
          // **Save base64 back to taskParams.icon**
          const newData = {
            ...cell.getData(),
            taskParams: { ...taskParams, icon: url }, // Update taskParams.icon
          };
          cell.setData(newData);
        });
      }
      // right text area
      const htmlText = document.createElement("div");
      htmlText.setAttribute("class", "cu_html_text");
      // Title
      const htmlTitle = document.createElement("div");
      htmlTitle.setAttribute("class", "cu_html_title");
      htmlTitle.innerText = nodeName;

      // Combined text content
      htmlText.appendChild(htmlTitle);
      // Combine entire nodes
      htmlContainer.appendChild(htmlTop);
      htmlContainer.appendChild(htmlText);

      return htmlContainer;
    },
  });
};
// Custom vue node
export const useVueNode = (graph) => {
  register({
    shape: "vue-node", // Custom node type
    component: NodeView,
    width: 36,
    height: 40,
    props: {
      // styletype: 2,
    },
  })
}
/**
 * Show connection stubs on nodes
 * @param {*} ports
 * @param {*} show
 */
export const showPorts = (ports, show) => {
  for (let i = 0, len = ports.length; i < len; i = i + 1) {
    ports[i].style.visibility = show ? "visible" : "hidden";
  }
};
export const renderGraphs = async (graph, savedData, styletype = 1) => {
  console.log("🚀 ~ renderGraphs ~ styletype:", styletype)
  if (!graph) {
    console.warn("renderGraph: graph not found");
    return;
  }

  if (!savedData) {
    console.warn("renderGraph: savedData not found");
    graph.clearCells();
    return;
  }

  // Do not clear the graph to avoid repeated rendering
  // graph.clearCells();

  const taskList = Array.isArray(savedData.taskDefinitionList) ? savedData.taskDefinitionList : [];
  const relations = Array.isArray(savedData.taskRelationJson) ? savedData.taskRelationJson : [];

  // Prepare nodes and edges
  const layoutNodes = taskList.map((task) => ({
    id: String(task.code), // Force conversion to string
    width: 36,
    height: 40,
    data: task,
  }));

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

  // Dagre layout
  const dagreLayout = new DagreLayout({
    type: 'dagre',
    rankdir: 'LR',
    nodesep: 50,
    ranksep: 50,
  });

  dagreLayout.layout({
    nodes: layoutNodes,
    edges: layoutEdges,
  });

  // Add nodes (remove duplicates)
  layoutNodes.forEach((n) => {
    if (!graph.getCellById(n.id)) {
      graph.addNode({
        id: n.id,
        shape: 'vue-node',
        component: NodeView,
        x: n.x || 0,
        y: n.y || 0,
        width: n.width,
        height: n.height,
        data: { ...n.data, styletype },
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
    }
  });

  // Add edges (remove duplication)
  layoutEdges.forEach((e) => {
    const edgeId = `${e.source}-${e.target}`; // Use source-target as unique id
    if (!graph.getCellById(edgeId)) {
      const sourceNode = graph.getCellById(e.source);
      const targetNode = graph.getCellById(e.target);
      if (!sourceNode || !targetNode) return;

      graph.addEdge({
        id: edgeId,
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
    }
  });
};

// Update node status
export const updateGraphNodes = (graph, nodeInstanceList) => {
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
      console.log("🚀 ~ updateGraphNodes ~ inst.status:", inst.status)
    }
  });
};
/**
 * Canvas clear
 */
export const handleRmNodes = (graph) => {
  graph.clearCells();
};

//Output component field processing
export const handleType2TaskParams = (fromColumns, toColumns) => {
  // target column and source column array
  const target_columns = [];
  const columns = [];

  // Get the shortest array length to avoid index out of bounds
  const minLength = Math.min(fromColumns.length, toColumns.length);

  // Traverse fromColumns and toColumns
  for (let i = 0; i < minLength; i++) {
    const fromCol = fromColumns[i];
    const toCol = toColumns[i];

    // Only if both are selected, add the corresponding column
    if (fromCol.isChecked && toCol.isChecked) {
      target_columns.push(toCol.columnName); // Add target column
      columns.push(fromCol.columnName); // Add source column
    }
  }

  // Return result object
  return {
    target_columns,
    columns,
  };
};

// Main function: Process task parameters according to different types
export const transNodeData = async (graph) => {
  const allNodes = JSON.parse(JSON.stringify(graph.getNodes()));
  const allEdges = JSON.parse(JSON.stringify(graph.getEdges()));
  // Process node data
  const tailNodes = allEdges.reduce((acc, edge) => {
    acc[edge.target.cell] = true;
    return acc;
  }, {});
  const isHeadNode = (code) => !tailNodes[code];

  const locations = [];
  const tasksMap = {};
  const taskDefinitionList = [];
  const sortedNodes = allNodes
    .filter((node) => node.shape === "cu-data-node")
    .sort((a, b) => {
      if (a.data?.taskParams?.type == 1 && b.data?.taskParams?.type != 1) return -1;
      if (a.data?.taskParams?.type != 1 && b.data?.taskParams?.type == 1) return 1;
      if (a.data?.taskParams?.type == 2) return 1;
      if (b.data?.taskParams?.type == 2) return -1;
      return 0;
    });
  // processing node
  // Build tasksMap
  for (const item of sortedNodes) {
    if (item.shape === "cu-data-node") {
      const code = item.id;
      tasksMap[code] = item.data;
    }
  }
  // processing node
  for (const item of sortedNodes) {
    if (item.shape === "cu-data-node") {
      const code = item.id;
      locations.push({
        taskCode: item.data.code,
        x: item.position.x,
        y: item.position.y,
      });
      taskDefinitionList.push({ ...item.data });
    }
  }

  // Handle task relationships
  const taskRelationJson = [];

  // Process all nodes
  allNodes.forEach((node) => {
    if (isHeadNode(node.id)) {
      // Get the tasks corresponding to the node from tasksMap
      const task = tasksMap[node.id];
      taskRelationJson.push({
        name: "",
        preTaskCode: 0,
        preTaskVersion: 0,
        postTaskCode: task?.code || 0,
        postTaskVersion: task?.version || 0,
        conditionType: "NONE",
        conditionParams: {},
      });
    }
  });
  // Process all edges
  allEdges.forEach((item) => {
    if (item.shape === "edge") {
      const sourceId = item.source.cell;
      const prevTask = tasksMap[sourceId];
      const targetId = item.target.cell;
      const task = tasksMap[targetId] || "";
      taskRelationJson.push({
        name: "",
        preTaskCode: prevTask?.code || 0,
        preTaskVersion: prevTask?.version || 0,
        postTaskCode: task?.code || 0,
        postTaskVersion: task?.version || 0,
        conditionType: "NONE",
        conditionParams: {},
      });
    }
  });
  return {
    locations,
    taskRelationJson,
    taskDefinitionList,
  };
};
function getAllConnectedEdges(graph, node) {
  const visited = new Set();
  const queue = [];
  const edges = [];
  let currentNode;
  // Queue the starting node and mark it as visited
  queue.push(node);
  visited.add(node);

  while (queue.length > 0) {
    currentNode = queue.shift();
    // Get all directly connected edges of the current node and add them to the result array
    const connectedEdges = graph.getConnectedEdges(currentNode);
    connectedEdges.forEach((edge) => {
      if (!edges.includes(edge)) {
        // Avoid adding the same edge repeatedly
        edges.push(edge);
      }
    });
    // Queue all neighboring nodes of the current node if they have not been visited yet
    const adjacentNodes = graph.getNeighbors(currentNode);
    adjacentNodes.forEach((adjacentNode) => {
      if (!visited.has(adjacentNode)) {
        visited.add(adjacentNode);
        queue.push(adjacentNode);
      }
    });
  }
  return edges;
}
export const validateGraph = (graph, flag) => {
  const nodes = graph.getNodes(); // Get all nodes
  const edges = graph.getEdges(); // Get all edges
  let valid = true;
  let errorMessages = [];

  if (nodes.length === 0) {
    const msg = td('dpp.utils.missingComponents');
    if (!flag) ElMessage.warning(msg);
    return { isValid: false, errorMessages: [msg] };
  }

  let inputNodeExists = false;
  let outputNodeExists = false;

  const addErrorMessage = (message) => {
    errorMessages.push(message);
  };

  // Table output component validation (type == 2)
  const validateType2TaskParams = (taskParams, node) => {
    if (!taskParams.tableFields || taskParams.tableFields.length === 0) {
      valid = false;
      addErrorMessage(`${node.data.name} ${td('dpp.utils.tableOutputFieldMapping')}`);
    } else {
      let { target_columns = [], columns = [] } = handleType2TaskParams(taskParams.tableFields, taskParams.toColumnsList);
      if (target_columns.length === 0 || columns.length === 0) {
        valid = false;
        addErrorMessage(`${node.data.name} ${td('dpp.utils.tableOutputFieldMapping')}`);
      }
    }
  };

  // All nodes cycle check
  nodes.forEach((node) => {
    const { data } = node;
    const taskParams = data?.taskParams;
    const componentType = data?.componentType;

    if (!taskParams) return;
    if (componentType == 41) {
      if (!taskParams.sequenceFieldName) {
        valid = false;
        addErrorMessage(`${data.name} ${td('dpp.utils.nodeInfoIncomplete')}`);
      }
      return;
    }
    if (componentType == 44 || componentType == 45) {
      if (!taskParams.plaintextField) {
        valid = false;
        addErrorMessage(`${data.name} ${td('dpp.utils.nodeInfoIncomplete')}`);
      }
      return;
    }
    if (!Array.isArray(taskParams.tableFields) || taskParams.tableFields.length == 0) {
      valid = false;
      addErrorMessage(`${data.name} ${td('dpp.utils.nodeInfoIncomplete')}`);
      return;
    }
    // Additional verification for specific types of components
    if (taskParams.type == "2") {
      validateType2TaskParams(taskParams, node);
    }
    // Mark input/output components
    if (taskParams.type == "1") inputNodeExists = data;
    if (taskParams.type == "2") outputNodeExists = data;
  });
  if (!inputNodeExists && !outputNodeExists) {
    valid = false;
    addErrorMessage(td('dpp.utils.missingInputOutput'));
  } else if (!inputNodeExists) {
    valid = false;
    addErrorMessage(td('dpp.utils.missingInput'));
  } else if (!outputNodeExists) {
    valid = false;
    addErrorMessage(td('dpp.utils.missingOutput'));
  }

  if (errorMessages.length > 0 && !flag) {
    ElMessage.warning(errorMessages[0]);
  }

  return { isValid: errorMessages.length === 0, errorMessages };
};

/**
 * Use graph.fromJSON to restore the data flow graph canvas processed by transNodeData
 */
export const renderGraph = (graph, savedData, width) => {
  if (!graph) {
    console.warn("renderGraph: graph not found");
    return;
  }

  if (!savedData) {
    console.warn("renderGraph: savedData not found");
    graph.clearCells();
    return;
  }

  graph.clearCells();

  const locations = Array.isArray(savedData.locations) ? savedData.locations : [];
  const taskList = Array.isArray(savedData.taskDefinitionList) ? savedData.taskDefinitionList : [];
  const relations = Array.isArray(savedData.taskRelationJson) ? savedData.taskRelationJson : [];

  // Add node
  locations.forEach((location) => {
    const nodeData = taskList.find((item) => item.code == location.taskCode);
    if (nodeData) {
      graph.addNode({
        id: String(location.taskCode), // Make sure the ID is a string
        shape: "cu-data-node",
        x: location.x,
        y: location.y,
        width: width || 170,
        height: 50,
        data: nodeData,
        ports: {
          ...cuPort,
          items: [
            { group: "top", id: "port-top" },
            { group: "bottom", id: "port-bottom" },
          ],
        },
      });
    }
  });

  // Add an edge and check whether the node exists before adding.
  relations.forEach((relation) => {
    const preId = String(relation?.preNodeCode);
    const postId = String(relation?.postNodeCode);

    if (!preId || !postId || preId === "0") return;

    const sourceNode = graph.getCellById(preId);
    const targetNode = graph.getCellById(postId);

    if (!sourceNode || !targetNode) {
      console.warn(td('dpp.utils.invalidEdge', '', { source: preId, target: postId }));
      return;
    }

    graph.addEdge({
      source: {
        cell: preId,
        port: "port-bottom",
      },
      target: {
        cell: postId,
        port: "port-top",
      },
      data: {
        sourceId: preId,
        targetId: postId,
      },
      attrs: {
        line: {
          stroke: "#2666FB",
          strokeWidth: 1,
          targetMarker: { name: "block", width: 12, height: 8 },
        },
      },
    });
  });
};
// Get code
export const fetchNodeUniqueKey = async ({scheduler}) => {
  try {
    const response = await getNodeUniqueKey({
      scheduler: scheduler,
      projectCode: userStore.projectCode || "133545087166112",
      projectId: userStore.projectId,
    });
    if (response.code == "200") {
      return response.data;
    }
    return null; // If there is no data, return null
  } catch (error) {
    return null; // Returns null on error
  }
};
// Get the superior node and encapsulate it into a drop-down box
export const createNodeSelect = (graph, currentNodeId) => {
  return graph
    .getNodes()
    .filter((node) => node.id !== currentNodeId && node?.data?.taskParams?.type !== 2) // Filter out the current node and nodes with taskParams.type 2
    .map((node) => ({
      label: node.data.name || td('dpp.utils.unknownNode'),
      value: node.id,
    }));
};
// All subordinate nodes of the current node
export const getAllChildNodes = (node, graph) => {
  const outgoingEdges = graph.getOutgoingEdges(node);
  const childNodes = [];
  if (outgoingEdges) {
    outgoingEdges.forEach((edge) => {
      const childNode = edge.getTargetCell();
      if (childNode) {
        childNodes.push(childNode);
        const grandChildren = getAllChildNodes(childNode, graph);
        childNodes.push(...grandChildren);
      }
    });
  }
  return childNodes;
};
// Find the parent node of the current node
export function getParentNode(currentNode, graph) {
  if (!currentNode || !graph) return null;

  const incomingEdges = graph.getIncomingEdges(currentNode) || [];
  if (incomingEdges.length === 0) return null;

  const parentEdge = incomingEdges[0]; // By default, only the first incoming edge is taken
  const parentNode = parentEdge?.getSourceCell?.();

  return parentNode?.isNode?.() ? parentNode : null;
}
export const createDataNode = (graph, data) => {
  return graph.createNode({
    shape: "cu-data-node",
    width: 180,
    height: 50,
    label: data?.label || data.name,
    data: {
      id: "",
      code: "", // component code
      taskType: data.taskType,
      name: data?.label || data.name, // name
      version: "0", // version number
      icon: data?.icon || data?.icons || "",
      componentType: data?.componentType || "",
      outputFields: [],
      inputFields: "",
      taskParams: {
        ...(data.componentType == 7 && {
          name: td('dpp.integration.apiInputComponent'),
          typeName: td('dpp.integration.apiInputComponent'),
          apiUrl: window.location.origin + import.meta.env.VITE_APP_BASE_API,
          apiMethod: "GET",
          returnFormat: 1,
          returnDataLine: ["$.data.rows"],
          pageFlag: 0,
          page: {
            pageNoKey: "pageNo", //The paging parameter key needs to be filled with ${pageNo} in the parameter. pageNo is the value of the current parameter.
            maxPage: 10, //Maximum number of pages
          },
          interval: 0, //Interval time in milliseconds (default 0)
          description: "",
          apiHeaders: [{
            "name": "Accept",
            "value": "*/*"
          }], //header
          inParams: {
            urlParams: [],
            type: 1,
            bodyParams: "",
          },
          outParams: [],
          outputFields: [],
        }),
        ...(data.componentType == 50 && {}),
        ...(data.componentType == 21 && {}),
        ...(data.componentType == 22 && {}),
        ...(data.componentType == 23 && {}),
        ...(data.componentType == 41 && {
          idGenerateType: '1',
        }),
        ...(data.componentType == 42 && {
          keyField: null,
          groupFields: [],
        }),
        ...(data.componentType == 43 && {
          keyField: null,
          groupTableFields: [],
        }),
        ...(data.componentType == 44 && {
          algorithm: "AES",
          secretKey: "",
          plaintextField: "",
          encryptedField: "",
        }),
        ...(data.componentType == 45 && {
          algorithm: "AES",
          secretKey: "",
          plaintextField: "",
          encryptedField: "",
        }),

        ...(data.componentType == 48 && {
          columnName: null,
          columnType: "string",
          format: null,
          length: null,
          precision: null,
          currencySymbol: null,
          decimalSymbol: null,
          groupingSymbol: null,
          defaultValue: null,
          nullIf: null,
        }),
        ...(data.componentType == 49 && {
          input: "", //input field
          output: "", //output fields
          unKnown: "", //Default value
        }),
        ...(data.componentType == 39 && {
          fieldDerivationType: "FIELD_DERIVE_CONCAT", //Operation type
          fieldDerivationName: "", //new field name
          fieldDerivationPrefix: "", //prefix
          fieldDerivationSuffix: "", //prefix
        }),
        ...(data.componentType == 47 && {
          selectedSourceField: "", //Field name
          targetFieldName: "", //target field
          defaultValueWhenUnmatched: "", //Default value if no match
        }),
        ...(data.componentType == 35 && {
          splitField: "",
          address: "", // Fields that need to be split
          splitType: "delimiter", // "delimiter"->delimiter or "regex"->regular expression
          delimiter: "", // Required when splitType is "delimiter"
          regex: "", // Required when splitType is "regex"
          enclosure: "", // Optional,
        }),
        ...(data.type == 1 && {
          querySql: "",
          csvFile: "",
          topic: "", //Topic
          clmt: "0", //connection status
          logicOperator: "and", //Table input logical connector
          datasource_id: "", // Source table data source id output
          asset_id: "", // Source table asset id input
          table_name: "", // Source table name input
          columns: "", // Source table synchronization field list input
          readerDatasource: {
            datasourceId: "",
            datasourceType: "",
            dbname: "",
          },
          readModeType: "1", // Reading method: 1: Full amount 2: ID increment 3: Time range increment Default is full amount
          idIncrementConfig: {
            //id increment
            incrementColumn: "", // Increment field
            incrementStart: "", // start value
          },
          dateIncrementConfig: {
            //time range increment
            logic: "and", // Logical operators: 1: and 2: or default and
            dateFormat: "yyyy-MM-dd", // Time format: yyyy-MM-dd or yyyy-MM-dd HH:mm:ss (manual input)
            column: [],
          },
          ...(data.componentType == 34 && {
            sortFields: [], //sort field
          }),

          ...(data.componentType == 6 && {
            path: "", //file path
            fileType: "csv", // File type "csv", "text"
            fieldDelimiter: ",", // separator default
            encoding: "UTF-8", //encoding
            compression: "", //Compression method, when fileType (file type) is the file compression method under csv, currently only supports gzip, bzip2, lzo, snappy
            hadoopConfig: "", //Configuration
            haveKerberos: false, //kerberos authentication
            kerberosKeytabFilePath: "", //Whether there is Kerberos authentication, the default is false, true, the configuration items kerberosKeytabFilePath and kerberosPrincipal are required
            kerberosPrincipal: "", //"Kerberos authentication principal name, such as xxxx/hadoopclient@xxx.xxx",
          }),
        }),
        parentId: "", //The id of the superior node
        config: "", //Configuration parameters
        typeName: data?.label || data.name, //Component type
        icon: data?.icon || data?.icons || "",
        taskType: data.taskType,
        type: data.type, // Component type 1: Input component 2: Output component
        batchSize: "1024", // One-time write volume
        tableFields: [], // Table output source table fields
        where: "", // where
        datasourceId: "",
        ...(data.type == 2 && {
          target_datasource_id: "", // Target data source id output
          target_asset_id: "", // Target asset id output
          target_table_name: "", // Target table name output
          target_columns: "", // Target table synchronization field list output
          writerDatasource: {
            datasourceId: "",
            datasourceType: "",
            dbname: "",
          },
          toColumnsList: [], // table input table fields
          postSql: "", // Post-SQL
          selectedColumns: [], // Update primary key
          selectedColumn: "",
          writeModeType: 2, //write mode
          preSql: "", //Pre-SQL
          ...(data.componentType == 93 && {
            path: "", //file path
            fileName: "", //File name
            fileType: "csv", // File type "csv", "text"
            fieldDelimiter: ",", // separator default
            encoding: "UTF-8", //encoding
            compression: "", //Compression method, when fileType (file type) is the file compression method under csv, currently only supports gzip, bzip2, lzo, snappy
            hadoopConfig: "", //Configuration
            haveKerberos: false, //kerberos authentication
            kerberosKeytabFilePath: "", //Whether there is Kerberos authentication, the default is false, true, the configuration items kerberosKeytabFilePath and kerberosPrincipal are required
            kerberosPrincipal: "", //"Kerberos authentication principal name, such as xxxx/hadoopclient@xxx.xxx",
            writerDatasource: {
              datasourceId: "",
              datasourceType: "",
              dbname: "",
            },
          }),
        }),
        ...(data.type == 3 && {
          mainArgs: {},
        }),
      },
    },
    ports: {
      ...cuPort, // Other connecting pile configurations
      items: [
        { group: "top", id: "port-top" },
        { group: "bottom", id: "port-bottom" },
      ],
    },
    options: {
      maxConnections: Infinity, // Maximum number of connections
    },
  });
};
// Return default taskParams based on componentType
export const getDefaultTaskParams = (data) => {
  console.log("🚀 ~ getDefaultTaskParams ~ data:", data.componentType);
  console.log("🚀 ~ getDefaultTaskParams ~ data.taskParams?.type:", data.taskParams?.type);

  const base = {
    inputFields: [],
    tableFields: [],
    outputFields: [],
  };

  if (data.componentType == 7) {
    return {
      ...base,
      name: "API输入组件",
      typeName: "API输入组件",
      apiUrl: window.location.origin + import.meta.env.VITE_APP_BASE_API,
      apiMethod: "GET",
      returnFormat: 1,
      returnDataLine: ["$.data.rows"],
      pageFlag: 0,
      page: {
        pageNoKey: "pageNo", //The paging parameter key needs to be filled with ${pageNo} in the parameter. pageNo is the value of the current parameter.
        maxPage: 10, //Maximum number of pages
      },
      interval: 0, //Interval time in milliseconds (default 0)
      description: "",
      apiHeaders: [], //header
      inParams: {
        urlParams: [],
        type: 1,
        bodyParams: "",
      },
      outParams: [],
      outputFields: [],
    };
  }
  if (data.componentType == 50) {
    return {
      ...base,
    };
  }
  if (data.componentType == 21) {
    return {
      ...base,
    };
  }
  if (data.componentType == 22) {
    return {
      ...base,
    };
  }
  if (data.componentType == 23) {
    return {
      ...base,
    };
  }
  if (data.componentType == 42) {
    return {
      ...base,
      keyField: null,
      groupFields: [],
    };
  }
  if (data.componentType == 42) {
    return {
      ...base,
      keyField: null,
      groupTableFields: [],
    };
  }
  if (data.componentType == 44 || data.componentType == 45) {
    return {
      ...base,
      algorithm: "AES",
      secretKey: "",
      plaintextField: "",
      encryptedField: "",
    };
  }
  if (data.componentType == 48) {
    return {
      ...base,
      columnName: null,
      columnType: "string",
      format: null,
      length: null,
      precision: null,
      currencySymbol: null,
      decimalSymbol: null,
      groupingSymbol: null,
      defaultValue: null,
      nullIf: null,
    };
  }

  if (data.componentType == 49) {
    return {
      ...base,
      input: "", //input field
      output: "", //output fields
      unKnown: "", //Default value
    };
  }
  if (data.componentType == 47) {
    return {
      ...base,
      selectedSourceField: "", //Field name
      targetFieldName: "", //target field
      defaultValueWhenUnmatched: "", //Default value if no match
    };
  }

  if (data.componentType == 35) {
    return {
      ...base,
      splitField: "",
      address: "",
      splitType: "delimiter",
      delimiter: "",
      regex: "",
      enclosure: "",
    };
  }
  if (data.componentType == 35) {
    return {
      ...base,
      selectedSourceField: "", //Field name
      targetFieldName: "", //target field
      defaultValueWhenUnmatched: "", //Default value if no match
    };
  }
  if (data.componentType == 34) {
    return {
      ...base,
      datasortFields: [],
    };
  }

  if (data.taskParams?.type == 1) {
    const commonReaderDatasource = {
      datasourceId: "",
      datasourceType: "",
      dbname: "",
    };

    const baseParams = {
      ...base,
      querySql: "",
      csvFile: "",
      inputFields: "", // Will be overwritten by base.inputFields to []
      topic: "",
      clmt: "0",
      logicOperator: "and",
      datasource_id: "",
      asset_id: "",
      table_name: "",
      columns: "",
      readerDatasource: { ...commonReaderDatasource },
      readModeType: "1",
      idIncrementConfig: {
        incrementColumn: "",
        incrementStart: "",
      },
      dateIncrementConfig: {
        logic: "and",
        dateFormat: "yyyy-MM-dd",
        column: [],
      },
    };

    if (data.componentType == 6) {
      Object.assign(baseParams, {
        path: "",
        fileType: "csv",
        fieldDelimiter: ",",
        encoding: "UTF-8",
        compression: "",
        hadoopConfig: "",
        haveKerberos: false,
        kerberosKeytabFilePath: "",
        kerberosPrincipal: "",
      });
    }

    return baseParams;
  }

  // If you need to enable the Writer part, just uncomment it.
  // if (data.taskParams?.type === 2) {
  //   const commonWriterDatasource = {
  //     datasourceId: "",
  //     datasourceType: "",
  //     dbname: "",
  //   };

  //   const baseParams = {
  //     ...base,
  //     target_datasource_id: "",
  //     target_asset_id: "",
  //     target_table_name: "",
  //     target_columns: "",
  //     writerDatasource: { ...commonWriterDatasource },
  //     toColumnsList: [],
  //     postSql: "",
  //     selectedColumns: [],
  //     selectedColumn: "",
  //     writeModeType: 2,
  //     preSql: "",
  //   };

  //   if (data.componentType === 93) {
  //     Object.assign(baseParams, {
  //       path: "",
  //       fileName: "",
  //       fileType: "csv",
  //       fieldDelimiter: ",",
  //       encoding: "UTF-8",
  //       compression: "",
  //       hadoopConfig: "",
  //       haveKerberos: false,
  //       kerberosKeytabFilePath: '',
  //       kerberosPrincipal: '',
  //     });
  //   }

  //   return baseParams;
  // }

  if (data.componentType == 31) {
    return {
      ...base,
      mainArgs: {},
    };
  }

  // Default
  return base;
};

let divMenuContainer = null;

export function createMenuDom({
  x,
  y,
  menuItems = [],
  container, // Directly pass the DOM node, not the id
  onHide,
}) {
  if (!container) {
    console.warn("A container DOM element must be provided");
    return;
  }

  // Clean up existing menus
  if (divMenuContainer) {
    if (container.contains(divMenuContainer)) {
      container.removeChild(divMenuContainer);
    }
    divMenuContainer = null;
    document.body.removeEventListener("click", onBodyClick);
  }

  // Create menu container
  divMenuContainer = document.createElement("div");
  divMenuContainer.className = "div-menu-container";
  Object.assign(divMenuContainer.style, {
    position: "absolute",
    left: `${x}px`,
    top: `${y}px`,
    zIndex: 1000,
    background: "#fff",
    border: "1px solid #ccc",
    borderRadius: "4px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
    minWidth: "140px",
    userSelect: "none",
  });

  // Add menu item
  menuItems.forEach(({ label, action }) => {
    const item = document.createElement("div");
    item.className = "div-menu-item";
    item.innerText = label;
    Object.assign(item.style, {
      padding: "8px 12px",
      cursor: "pointer",
      borderBottom: "1px solid #eee",
    });
    item.addEventListener("click", () => {
      action();
      hideMenu();
    });
    divMenuContainer.appendChild(item);
  });

  if (divMenuContainer.lastChild) {
    divMenuContainer.lastChild.style.borderBottom = "none";
  }

  container.appendChild(divMenuContainer);

  // Adjust the position to prevent the menu from exceeding the boundaries of the container
  const menuRect = divMenuContainer.getBoundingClientRect();
  const contRect = container.getBoundingClientRect();
  const THRESHOLD = 500;

  let newLeft = x;
  let newTop = y;

  const distRight = contRect.right - x;
  if (distRight <= THRESHOLD) {
    newLeft = x - menuRect.width;
  }
  const distBottom = contRect.bottom - y;
  if (distBottom <= THRESHOLD) {
    newTop = y - menuRect.height;
  }
  divMenuContainer.style.left = `${newLeft}px`;
  divMenuContainer.style.top = `${newTop}px`;

  divMenuContainer.addEventListener("click", (e) => e.stopPropagation());

  function hideMenu() {
    if (divMenuContainer) {
      divMenuContainer.style.display = "none";
      document.body.removeEventListener("click", onBodyClick);
      if (container.contains(divMenuContainer)) {
        container.removeChild(divMenuContainer);
      }
      divMenuContainer = null;
      if (typeof onHide === "function") onHide();
    }
  }

  function onBodyClick() {
    hideMenu();
  }

  document.body.addEventListener("click", onBodyClick);

  return {
    hide: hideMenu,
  };
}
// Determine whether arrays are the same
export function areFieldNamesEqual(fieldsA = [], fieldsB = []) {
  const namesB = new Set(fieldsB.map((f) => f.columnName));
  return fieldsA.every((f) => namesB.has(f.columnName));
}
/**
 * Check node name
 */
export function shouldAbortByName(graph, nodeData) {
  const newName = nodeData?.name?.trim();
  const currentcode = nodeData?.code;
  if (!newName || !currentcode) return false;
  const allNodes = graph?.getCells?.() || [];
  return allNodes.some((cell) => {
    if (!cell?.getProp) return false;
    const data = cell.getProp("data") || {};
    console.log("🚀currentcode2222", currentcode);
    return data.name == newName && cell.data.code != currentcode;
  });
}

export const exportGraphAsPNG = (
  graph,
  {
    fileName = "流程图",
    width = 1920,
    height = 1080,
    padding = 40,
    quality = 1,
    stylesheet
  } = {}
) => {
  if (!graph) {
    console.warn("exportGraphAsPNG: graph instance not found");
    return;
  }

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

  graph.exportPNG(fileName, {
    width,
    height,
    padding,
    quality,
    copyStyles: true,
    stylesheet: stylesheet || defaultStylesheet
  });
};
// Table input rules
// Table input rules
export function renameRuleToRuleConfig(data) {
  return data
    .filter(col => Array.isArray(col.cleanRuleList) && col.cleanRuleList.length > 0)
    .map(col => {
      return col.cleanRuleList.map(rule => {
        let parsedRule = {};
        try {
          parsedRule = JSON.parse(rule.rule); // The original rule is parsed into an object
        } catch (e) {
          console.warn(`Failed to parse rule JSON: ${rule.rule}`, e);
        }
        const ruleConfig = {
          ...parsedRule,
          columns: [col.columnName]
        };
        const { rule: _, ...rest } = rule;
        return {
          ...rest,
          columns: [col.columnName],
          ruleConfig
        };
      });
    })
    .flat();
}
