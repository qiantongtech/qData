<template>
  <div class="app-container" ref="app-container" v-loading="loading">
    <div class="head-container">
      <div class="head-title">
        {{
          nodeData.taskConfig.name != null
            ? nodeData.taskConfig.name
            : "作业任务"
        }}
      </div>
      <div class="head-btns">
        <el-button type="primary" size="small" @click="handleExportData" v-if="!route.query.info">
          <img src="@/assets/dpp/etl/title-act (1).svg" alt="">任务保存
        </el-button>
        <el-button type="primary" plain size="small" @click="routeTo('/dpp/job', '')">
          <img class="currImg" src="@/assets/dpp/etl/title (3).svg" alt="">
          <img class="act" src="@/assets/dpp/etl/title-act (3).svg" alt="">任务取消
        </el-button>
        <el-button type="primary" plain size="small" @click="openTaskConfigDialog" v-if="!route.query.info">
          <img class="currImg" src="@/assets/dpp/etl/title (4).svg" alt="">
          <img class="act" src="@/assets/dpp/etl/title-act (4).svg" alt="">任务配置
        </el-button>
        <el-button type="primary" plain size="small" @click="openTaskConfigDialog" v-else>
          <img class="currImg" src="@/assets/dpp/etl/title (4).svg" alt="">
          <img class="act" src="@/assets/dpp/etl/title-act (4).svg" alt="">任务详情
        </el-button>
        <el-button type="primary" plain v-if="!route.query.info" size="small" @click="selectTab('checkMessage')">
          <img class="currImg" src="@/assets/dpp/etl/title (2).svg" alt="">
          <img class="act" src="@/assets/dpp/etl/title-act (2).svg" alt="">任务检查
        </el-button>
        <!--        <el-button type="primary" size="small" @click="selectTab('log')"
          >执行一下</el-button
        >-->
      </div>
    </div>
    <div class="flex-container">
      <div class="left-pane" v-loading="loadingTree" v-if="!route.query.info">
        <div class="left-tree">
          <div class="">
            <el-tree class="dept-tree" :data="treeData" :props="{ label: 'label', children: 'children' }"
              ref="deptTreeRef" node-key="id" :filter-node-method="filterNode"
              :default-expanded-keys="defaultExpandedKeys" empty-text="加载中，请稍候" :default-expand-all="false">
              <template #default="{ node, data }">
                <template v-if="data.isSearchBox">
                  <div class="tree-search-box">
                    <el-input v-model="deptName" placeholder="请输入类目/任务名称检索" clearable prefix-icon="Search"
                      class="search-box" />
                  </div>
                </template>
                <!-- 正常的树节点 -->
                <template v-else>
                  <span class="custom-tree-node" @mousedown="startDrag($event, node, data)">
                    <img class="node-icon" src="@/assets/da/asset/folder.svg" alt=""
                      v-if="node.expanded && node.childNodes.length" />
                    <img class="node-icon" src="@/assets/da/asset/folder.svg" alt=""
                      v-if="!node.expanded && node.childNodes.length" />
                    <!-- 子级 -->
                    <!-- <el-icon class="zjiconimg colorwxz" v-show="!node.isCurrent && node.childNodes.length == 0">
                      <Tickets />
                    </el-icon>
                    <el-icon class="zjiconimg colorxz" v-show="node.isCurrent && node.childNodes.length == 0">
                      <Tickets />
                    </el-icon> -->
                    <img v-if="data.type == '1' && data.level != 1" :src="getDatasourceIcon(data.draftJson)" alt=""
                      :style="getDatasourceIcon(data.draftJson) ? 'width: 20px;' : ''" class="icon-img" />
                    <img v-if="
                      data.type == '3' &&
                      data.level != 1 &&
                      data?.status != '-2'
                    " :src="getDatasourceIcon2(data.datasourceType)"
                      :style="getDatasourceIcon2(data.datasourceType) ? 'width: 20px;' : ''" class="icon-img" />
                    <el-tooltip :content="data.label" placement="top">
                      <span class="treelable">{{ data.label }}</span>
                    </el-tooltip>
                    <span v-if="!data.dsId && data.dppEtlTaskCount">
                      ({{ data.dppEtlTaskCount }})
                    </span>
                  </span>
                </template>
              </template>
            </el-tree>
          </div>
        </div>
      </div>
      <!-- 右侧部分 -->
      <div class="right-pane">
        <div id="graphContainer" class="graph-container" ref="graphContainer"></div>

        <div class="toolbar">
          <template v-for="item in toolbar" :key="item.id">
            <el-tooltip class="box-item" effect="light" :content="item.tip" placement="bottom"
              v-if="!(route.query.info && item.tip == '重置')">
              <div class="toolbar-item" @click="toolbarClick(item)">
                <img :src="getAssetsFile(item.icon)" alt="" />
              </div>
            </el-tooltip>
          </template>
        </div>
        <div class="tabs-container" v-bind:style="tabAreaStyle">
          <!-- 图标放置在最右侧 -->
          <el-icon class="icon-right" @click="minimizeAction">
            <Minus />
          </el-icon>
          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane v-for="(tab, index) in tabs" :key="index" :name="tab.name">
              <template #label>
                <span>{{ tab.label }}</span>
              </template>
              <div class="tab-content" v-html="tab.content"></div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
    <component :is="currentFormComponent" :visible="drawer" :key="currentNode?.id || Date.now()" :title="title"
      @update:visible="closeDialog" @confirm="handleFormSubmit" :currentNode="currentNode" :userList="userList"
      :info="route.query.info" />
    <taskConfigDialog :visible="taskConfigDialogVisible" :title="!route.query.info ? '修改任务配置' : '任务详情'"
      @update:visible="taskConfigDialogVisible = $event" @confirm="handletaskConfig" :data="nodeData"
      :userList="userList" :info="route.query.info" />
  </div>
</template>
<script setup>
import { Graph } from "@antv/x6";
import { Dnd } from "@antv/x6-plugin-dnd";
import { baseConfig, cuPort, typeList } from "@/utils/graph";
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import DATSForm from "../components/base/datsForm.vue";
import OutputForm from "../components/outputForm.vue";
import ShellForm from "../components/base/shellForm.vue";
import taskConfigDialog from "../components/task.vue";
import useUserStore from "@/store/system/user";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttTaskCat } from "@/api/att/cat/taskCat/taskCat";

const userStore = useUserStore();
import {
  createProcessDefinition,
  etlTask,
  updateProcessDefinition,
  getDppEtlTaskListTree,
  delDppEtlTask,
  updateReleaseJobTask,
} from "@/api/dpp/task/index.js";

import { Selection } from "@antv/x6-plugin-selection";
import {
  useHtmlNode,
  showPorts,
  usePlugins,
  renderGraph,
  updateNodeImage,
  exportGraphAsPNG
} from "@/utils/opBase";

// 图标
const getDatasourceIcon = (json) => {
  let type = json && JSON.parse(json).taskType;
  switch (type) {
    case "FLINK":
      return new URL("@/assets/system/images/dpp/Flink.svg", import.meta.url).href;
    case "SPARK":
      return new URL("@/assets/system/images/dpp/Spark.svg", import.meta.url).href;
    default:
      return null;
  }
};
// 图标
const getDatasourceIcon2 = (type) => {
  switch (type) {
    case "DM":
      return new URL("@/assets/system/images/dpp/DM.png", import.meta.url).href;
    case "Oracle":
      return new URL("@/assets/system/images/dpp/oracle.png", import.meta.url).href;
    case "MYSQL":
      return new URL("@/assets/system/images/dpp/mysql.png", import.meta.url).href;
    case "Kingbase":
      return new URL("@/assets/system/images/dpp/kingBase.png", import.meta.url).href;
    case "Sqlerver":
      return new URL("@/assets/system/images/dpp/sqlServer.png", import.meta.url).href;
    case "PostgreSql":
      return new URL("@/assets/system/images/dpp/kafka.png", import.meta.url).href;
    case "Hive":
      return new URL("@/assets/system/images/dpp/Hive.png", import.meta.url).href;
    case "SparkSql":
      return new URL("@/assets/system/images/dpp/Spark.svg", import.meta.url).href;
    case "FlinkBatch":
      return new URL("@/assets/system/images/dpp/Flink.svg", import.meta.url).href;
    case "FlinkStream":
      return new URL("@/assets/system/images/dpp/Flink.svg", import.meta.url).href;
    default:
      return null;
  }
};
const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();
let id = route.query.id || 1;
const deptName = ref("");
const deptTreeRef = ref(null);
watch(deptName, (val) => {
  if (deptTreeRef.value) {
    deptTreeRef.value.filter(val);
  }
});
const filterNode = (value, data) => {
  if (data.isSearchBox) return true; // 搜索框节点始终显示
  if (data.falg) return true; // 搜索框节点始终显示

  if (!value) return true;
  return data.label.includes(value);
};

// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;
    if (id) {
      getList();
    }
  }
);
let hasUnsavedChanges = ref(false);
let nodeData = ref({ taskConfig: {} });
// 全局的Graph
let graph = null;
// 全局的dnd
let dnd = null;
// 抽屉
const drawer = ref(false);
// 任务配置
const taskConfigDialogVisible = ref(false);

// 存储当前组件的信息
const currentNode = ref({});
// 当前组件的上级组件的信息
const currentFormComponent = computed(() => {
  if (!drawer.value || !currentNode.value) return null;
  const type = currentNode.value?.data?.taskParams.type || "";
  const typaCode = currentNode.value?.data?.taskParams.typaCode || "";
  switch (type + "") {
    case "1":
      return OutputForm;
    case "3":
      if (typaCode == "SHELL") {
        return ShellForm;
      }
      return DATSForm;
    case "31":
      return TransformForm;
    case "91":
      return OutputForm;
    default:
      return DATSForm;
  }
});
// 撤销按钮
const undoDisabled = ref(null);
// 导出的数据
const exportData2 = ref("");
let loading = ref(false);

function getList() {
  loading.value = true;
  etlTask(route.query.id).then((response) => {
    nodeData.value = response.data;
    renderGraph(graph, nodeData.value, 300);
    loading.value = false;
    // 部门
  }).finally(() => {
    loading.value = false;
  })
}

let userList = ref([]);
let deptOptions = ref([]);
let treeData = ref([]);
let loadingTree = ref(false);
let defaultExpandedKeys = ref([]);

function collectNodeIds(data) {
  let nodeIds = [];
  data.forEach((node) => {
    // 如果 level 是 1 或 2，收集其 value
    if (node.level == 1 || node.level == 2) {
      nodeIds.push(node.id);
    }
    // 如果该节点有 children，则递归遍历子节点
    if (node.children && node.children.length > 0) {
      nodeIds = nodeIds.concat(collectNodeIds(node.children)); // 合并结果
    }
  });

  return nodeIds;
}

function getDeptTree() {
  loadingTree.value = true;
  Promise.all([
    getDppEtlTaskListTree().catch((err) => {
      console.error("获取任务树数据失败", err);
      return { data: [] };
    }),
    listAttTaskCat().catch((err) => {
      console.error("获取类别数据失败", err);
      return { data: [] };
    }),
    deptUserTree().catch((err) => {
      console.error("获取用户数据失败", err);
      return { data: [] };
    }),
  ])
    .then(([taskTreeRes, taskCatRes, userRes]) => {
      // 处理任务树数据
      treeData.value = [
        // {
        //   label: "新建任务",
        //   value: "",
        //   level: 1,
        //   type: 0,
        //   id: 0,
        //   children: [
        //     {
        //       id: 1,
        //       level: 2,
        //       label: "数据集成",
        //       value: "",
        //       children: [{ falg: true, label: "离线集成", value: "" }],
        //     },
        //     ...treeDatas,
        //   ],
        // },
        {
          label: "所有任务",
          level: 1,
          value: "",
          id: 3,
          children: [
            {
              value: "search-box",
              label: "搜索框",
              isSearchBox: true,
            },
            ...taskTreeRes.data,
            // ...taskTreeRes.data.map((item) => ({ ...item, level: 2 })),
          ],
        },
      ];
      console.log("🚀 ~ .then ~ treeData.value:", treeData.value);

      // 处理部门类别数据
      deptOptions.value = [
        {
          name: "数据集成类目",
          value: "",
          children: proxy.handleTree(
            taskCatRes.data.rows || [],
            "id",
            "treeId"
          ),
        },
      ];
      // 处理用户数据
      userList.value = userRes.data;

      defaultExpandedKeys.value = collectNodeIds(treeData.value);
    })
    .finally(() => {
      // 所有请求完成后，隐藏 loading
      loadingTree.value = false;
    });
}

if (route.query.id) {
  getList();
}

const closeDialog = (node) => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // 根据组件 ID 删除组件
  }
  drawer.value = false;
};
// 弹窗保存
const handleFormSubmit = (nodeData) => {
  const node = graph.getCellById(currentNode.value.id);
  if (node) {
    node.setProp(
      "data",
      JSON.parse(JSON.stringify({ ...node.getProp("data"), ...nodeData }))
    );
  }
  // 关闭抽屉（表单）
  drawer.value = false;
};

// 运行实例保存
const handletaskConfig = (form) => {
  nodeData.value.taskConfig = { ...form };
};

const transNodeData = (graph) => {
  let allNodes = JSON.parse(JSON.stringify(graph.getNodes())); // 深拷贝节点数据
  let allEdges = JSON.parse(JSON.stringify(graph.getEdges()));
  const tailNodes = {};
  allEdges.forEach((item) => {
    const targetId = item.target.cell;
    tailNodes[targetId] = true;
  });

  const isHeadNode = (code) => !tailNodes[code];
  var locations = [];
  var tasksMap = {};
  var taskDefinitionList = []; // 初始化 taskDefinitionList
  // 处理节点数据
  allNodes.forEach((item) => {
    if (item.shape === "cu-data-node") {
      const code = item.id;
      locations.push({
        taskCode: item.data.code,
        x: item.position.x,
        y: item.position.y,
      });
      tasksMap[code] = item.data;
      // 确保每个节点的 data 中包含 code
      taskDefinitionList.push({
        ...item.data,
        code: item.data.code, // 添加 code 字段
      });
    }
  });

  var taskRelationJson = allNodes
    .filter((node) => isHeadNode(node.id))
    .map((node) => {
      const task = tasksMap[node.id];
      return {
        name: "",
        preTaskCode: 0,
        preTaskVersion: 0,
        postTaskCode: task?.code || 0,
        postTaskVersion: task?.version || 0,
        conditionType: "NONE",
        conditionParams: {},
      };
    });

  // 处理边数据
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
let deletedNodeIds = ref([]);

// 大保存
const handleExportData = async () => {
  loading.value = true;

  try {
    if (!hasTaskConfig(nodeData.value)) {
      taskConfigDialogVisible.value = true;
      return;
    }
    const { isValid } = validateGraph();
    if (!isValid) {
      return;
    }
    // 转换节点数据
    exportData2.value = await transNodeData(graph);
    exportData2.value = {
      ...exportData2.value,
      taskRelationJson: JSON.stringify(exportData2.value.taskRelationJson),
      taskDefinitionList: JSON.stringify(exportData2.value.taskDefinitionList),
      projectCode: userStore.projectCode || "133545087166112",
      projectId: userStore.projectId,
      ...nodeData.value?.taskConfig,
      type: 4,
    };
    // 根据 nodeData.id 判断是更新还是创建
    const res = nodeData.value?.id
      ? await updateProcessDefinition({
        ...exportData2.value,
        id: nodeData.value.id,
      })
      : await createProcessDefinition(exportData2.value);

    // 成功后处理
    if (res.code == "200") {
      handleDelete(deletedNodeIds.value);
      handleSuccess();
    } else {
      proxy.$modal.msgWarning("操作失败，请检查必填项");
    }
  } catch (error) {
    handleError(error);
  } finally {
    loading.value = false;
  }
};

const hasTaskConfig = (nodeData) => {
  return nodeData?.taskConfig && Object.keys(nodeData.taskConfig).length > 0;
};

const handleSuccess = () => {
  taskConfigDialogVisible.value = false;
  hasUnsavedChanges.value = false;
  const message = nodeData.value.id ? "修改成功" : "新增成功";
  router.push("/dpp/job");
  proxy.$modal.msgSuccess(message);
};

const handleError = (error) => {
  console.error("操作失败:", error);
  if (!error.message) {
    proxy.$modal.msgWarning("操作失败，请检查必填项");
  }
};

const openTaskConfigDialog = () => {
  taskConfigDialogVisible.value = true;
};

useHtmlNode();
const startDrag = (e, treeNode, data) => {
  if (route.query?.info) return proxy.$modal.msgWarning("不可编辑，详情页面只能查看");
  if (data.disabled) return proxy.$modal.msgWarning("正在开发中，敬请期待");
  // 获取图形中的所有节点
  if (!data.type) return;
  let iconPath = "";
  if ((data.type == "3" || data.type == "54") && data?.status == "-2") {
    iconPath = data.icon;
  } else if (data.type == "3") {
    iconPath = getDatasourceIcon2(data.datasourceType); //new URL("@/assets/system/images/dpp/sjkftb.png", import.meta.url).href;
  } else if (data.type == "1") {
    iconPath = getDatasourceIcon(data.draftJson); // new URL("@/assets/system/images/dpp/sjjctb.png", import.meta.url).href;
  }
  const node = graph.createNode({
    shape: "cu-data-node",
    width: 250,
    height: 50,
    label: data?.label,
    data: {
      id: "",
      code: "", // 组件的 code
      taskType: "SUB_PROCESS",
      name: data?.label, // 名字
      version: "", // 版本号
      type: data?.type,
      componentType: "71",
      releaseState: data.status || "",

      taskParams: {
        type: data?.type,
        subTaskId: data.id || "", //子任务的id
        processDefinitionCode: data.extCode ? data.extCode : data.code,
        taskType: data.taskType,
        typaCode: data.typaCode,
        releaseState: data.status || "",
        icon: iconPath, // 动态获取路径
        falg: true,
      },
    },
    ports: {
      ...cuPort, // 其他连接桩配置
      items: [
        { group: "top", id: "port-top" },
        { group: "bottom", id: "port-bottom" },
      ],
    },
    options: {
      maxConnections: Infinity, // 最大连接数
    },
  });

  dnd.start(node, e); // 启动拖拽操作
};

// 删除创建的实例
async function handleDelete(id) {
  if (id && id.length > 0) {
    await delDppEtlTask(id);
  }
}

let divMenuContainer = null;
/**
 * 组件右键删除
 * @param {*}
 */
const createMenuDom = ({ x, y, node, edge, type }) => {
  if (route?.query?.info) return;
  // 清理现有的菜单容器
  if (divMenuContainer) {
    document.getElementById("graphContainer").removeChild(divMenuContainer);
  }
  // 创建新的菜单容器
  divMenuContainer = document.createElement("div");
  divMenuContainer.setAttribute("class", "div-menu-container");
  divMenuContainer.style.left = `${x + 30}px`;
  divMenuContainer.style.top = `${y}px`;

  // 创建菜单项（删除按钮）
  const divDeleteItem = document.createElement("div");
  divDeleteItem.setAttribute("class", "div-menu-item");
  divDeleteItem.innerText = type === 0 ? "删除组件" : "删除连接线";
  divDeleteItem.addEventListener("click", () => {
    hasUnsavedChanges.value = true;
    if (type === 0) {
      if (node.data.type === "3" && node.data.releaseState === -2) {
        deletedNodeIds.value.push(node.data.taskParams.subTaskId);
      }
      if (
        node?.data?.taskParams?.type == "3" &&
        node?.data?.releaseState == "-3"
      ) {
        return proxy.$modal.msgWarning("删除失败，上线任务不能操作请先下线");
      }
      graph.removeNode(node);
    } else {
      graph.removeEdge(edge); // 删除连接线
    }
    hideMenu();
  });

  // 创建分割线
  const elDivider = document.createElement("hr");
  elDivider.setAttribute("class", "el-divider");
  elDivider.style.margin = "0px 0"; // 为分隔线添加上下间距

  // 创建“上下线”按钮
  const divToggleStatusItem = document.createElement("div");
  divToggleStatusItem.setAttribute("class", "div-menu-item");

  // 判断当前状态：上线还是下线
  const isOffline =
    node?.data?.releaseState == "-2" || node?.data?.releaseState == "0";
  // 设置新的状态为 1 或 0
  let newStatus = isOffline ? "1" : "0";
  divToggleStatusItem.innerText = isOffline ? "上线" : "下线";
  divToggleStatusItem.addEventListener("click", () => {
    loading.value = true;
    updateReleaseJobTask({
      id: node?.data?.taskParams?.subTaskId, // 假设任务 ID 存储在 subTaskId
      releaseState: newStatus, // 传递 0 或 1
      projectCode: userStore.projectCode || "133545087166112",
      projectId: userStore.projectId,
    })
      .then((response) => {
        let Status;
        if (node.data.releaseState == -2) {
          Status = -3;
        } else if (node.data.releaseState == 0) {
          Status = 1;
        } else if (node.data.releaseState == 1) {
          Status = 0;
        } else if (node.data.releaseState == -3) {
          Status = -2;
        }
        node.data.releaseState = Status;
        // 调用 updateNodeImage 方法来更新节点的图像
        updateNodeImage(node, Status);
      })
      .finally(() => {
        loading.value = false;
      });

    hideMenu();
  });

  // 添加菜单项到容器
  divMenuContainer.appendChild(divDeleteItem);
  divMenuContainer.appendChild(elDivider); // 添加分隔线
  if (type === 0) {
    divMenuContainer.appendChild(divToggleStatusItem); // 仅对组件提供上下线功能
  }

  document.getElementById("graphContainer").appendChild(divMenuContainer);

  // 点击其他地方隐藏菜单
  const hideMenu = () => {
    if (divMenuContainer) {
      divMenuContainer.style.display = "none";
      document.body.removeEventListener("click", hideMenu);
    }
  };
  document.body.addEventListener("click", hideMenu, { once: true });

  // 移除菜单监听器
  divDeleteItem.addEventListener("click", () =>
    document.body.removeEventListener("click", hideMenu)
  );
  divToggleStatusItem.addEventListener("click", () =>
    document.body.removeEventListener("click", hideMenu)
  );
};

const minimizeAction = () => {
  tabAreaStyle.value.bottom = "-9999px";
};
let selectedEdge = ref(); // 外部变量，用于记录当前选中的边
// 定义方法来清空当前选中边的颜色
function clearSelectedEdge() {
  if (selectedEdge.value) {
    selectedEdge.value.prop("attrs/line/stroke", "#2666FB"); // 恢复默认颜色
    selectedEdge.value = null; // 清空选中的边
  }
}

const title = ref("");

// 初始化图表
function initializeGraph() {
  graph = new Graph({
    container: proxy.$refs.graphContainer,
    width: "100%",
    height: "60%",
    grid: false, //网格
    background: { color: "#ff0000" },
    autoResize: true, //画布自适应
    panning: true, //画布拖动
    interactive: true,
    ...baseConfig,
    //画布缩放
    mousewheel: {
      enabled: true,
      zoomAtMousePosition: true,
      minScale: 0.5,
      maxScale: 3,
    },
    selecting: {
      enabled: true,
      className: "my-selecting", // 自定义选中样式类名
    },
  });

  dnd = new Dnd({
    target: graph,
    scaled: false,
  });
  graph.use(
    new Selection({
      enabled: true,
      multiple: true,
      rubberband: true,
      movable: true,
      showNodeSelectionBox: true,
      pointerEvents: "none",
    })
  );
  // 插件
  baseConfig.bindShortcuts(graph);
  usePlugins(graph);
  undoDisabled.value = graph.canUndo();
}

// 绑定事件
function bindGraphEvents() {
  // 监听边的点击事件
  graph.on("edge:click", handleEdgeClick);

  graph.on("blank:click", clearSelectedEdge);

  graph.on("node:added", handleNodeAdded);

  graph.on("node:mouseenter", () => togglePortsVisibility(true));
  // 节点鼠标离开事件
  graph.on("node:mouseleave", () => togglePortsVisibility(false));

  graph.on("node:contextmenu", handleNodeContextMenu);

  graph.on("edge:contextmenu", handleEdgeContextMenu);

  graph.on("node:dblclick", handleNodeDblClick);
}

// 处理边的点击事件
function handleEdgeClick({ cell }) {
  const currentColor = cell.prop("attrs/line/stroke");

  clearSelectedEdge();

  const newColor = currentColor === "#f14f10" ? "#2666FB" : "#f14f10";
  cell.prop("attrs/line/stroke", newColor);

  selectedEdge.value = cell;
}

// 处理节点添加事件
function handleNodeAdded({ node }) {
  if (!loading.value) {
    hasUnsavedChanges.value = true;
    currentNode.value = {};
    currentNode.value = node;
    drawer.value = true; // 控制抽屉显示
  }
}

// 处理非输入节点
function handleNonInputNode(node) {
  const edges = graph.getEdges();
  edges.forEach((edge) => {
    if (edge.getTargetNode() == node) {
      drawer.value = true; // 控制抽屉显示
    }
  });
  drawer.value = true; // 控制抽屉显示
}

// 切换端口的显示状态
function togglePortsVisibility(visible) {
  const container = document.getElementById("graphContainer");
  const ports = container.querySelectorAll(".x6-port-body");
  showPorts(ports, visible);
}

// 处理节点右键菜单事件
function handleNodeContextMenu({ e, node }) {
  const pos = graph.clientToGraph(e.clientX, e.clientY);
  createMenuDom({ x: pos.x, y: pos.y, node, type: 0 });
}

// 处理边右键菜单事件
function handleEdgeContextMenu(event) {
  const edge = event.edge;
  const { x, y } = event;
  createMenuDom({ x: x, y: y, edge, type: 1 });
}

// 处理节点双击事件
function handleNodeDblClick({ node }) {
  if (node.data.taskParams.type == "3" && node.data?.releaseState == "-3") {
    return proxy.$modal.msgWarning("操作失败，上线任务不能修改请先下线");
  }
  graph.cleanSelection(); // 清除所有选中的元素
  clearSelectedEdge();
  currentNode.value = {};
  currentNode.value = node;
  drawer.value = true; // 控制抽屉显示
}

// 初始化函数
onMounted(() => {
  initializeGraph();
  bindGraphEvents();
  getDeptTree();
});
// 离开页面时提示
onBeforeRouteLeave((to, from, next) => {
  // 检查是否有未保存的更改
  if (hasUnsavedChanges.value) {
    ElMessageBox.confirm(
      "您已经编辑部分任务内容，是否放弃已编辑内容？", // 提示信息
      "提示", // 标题
      {
        confirmButtonText: "保存", // 确认按钮文本
        cancelButtonText: "放弃", // 取消按钮文本
        type: "warning", // 弹窗类型
      }
    )
      .then(() => {
        handleExportData();
        next(false);
        saveData();
      })
      .catch(() => {
        next();
        saveData();
      });
  } else {
    // 如果没有未保存的更改，直接跳转
    next();
  }
}); // 跳转并保存数据的逻辑（如果需要）
const saveData = async () => {
  // 假设这是保存数据的函数
  hasUnsavedChanges.value = false; // 保存后标记为无未保存内容
  // 执行保存操作
};

function routeTo(link, row) {
  hasUnsavedChanges.value = false;
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          // id: row.id,
        },
      });
    }
  }
}

// 工具
const toolbar = ref([
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
const toolbarClick = (item) => {
  switch (item.id) {
    // case "full-screen": {
    //   toggle();
    //   isfull.value = !isfull.value;
    //   break;
    // }
    case "zoom-in":
      graph.zoom(0.2);
      break;
    case "zoom-out":
      graph.zoom(-0.2);
      break;
    case "redo":
      if (graph.canRedo()) {
        graph.redo();
      }
      break;
    case "undo":
      if (graph.canUndo()) {
        graph.undo();
      }
      break;
    case "auto-fit":
      graph.centerContent();
      graph.zoomTo(1);
      break;
    case "export": {
      // graph.exportPNG("流程图", {
      //   width: 1920,
      //   height: 1080,
      //   padding: 20,
      //   quality: 0.9,
      // });
      exportGraphAsPNG(graph)
      break;
    }
    case "reset": {
      handleCancel();
      break;
    }
  }
};
const getAssetsFile = (url) => {
  return new URL(`/src/assets/dpp/etl/${url}`, import.meta.url).href;
};
// 重置操作逻辑
const handleCancel = () => {
  proxy.$modal
    .confirm(`点击重置将清除所有未保存的更改，您确定要继续吗？`)
    .then(() => {
      // 刷新当前页签
      proxy.$tab.refreshPage(route);
    });
};
// 用于控制当前选中的标签
const activeTab = ref("checkMessage");
const tabs = ref([
  { name: "checkMessage", label: "检查消息", content: "检查消息内容" },
  // { name: "log", label: "日志", content: "日志内容" },
]);
// 用于设置标签区域的样式
const tabAreaStyle = ref({
  position: "absolute",
  height: "300px",
  width: "100%",
  transition: "bottom 0.3s",
  bottom: "-9999px",
  /* left: 0px; */
  "background-color": "rgb(255, 255, 255)",
  "margin-left": "15px",
});
// 切换到任务检查标签
const selectTab = (tabName) => {
  activeTab.value = tabName;

  if (activeTab.value == "checkMessage") {
    isValidClick();
  }
  tabAreaStyle.value.bottom = "0px";
};
const validateGraph = (flag) => {
  let isValid = true;
  let errorMessages = [];
  const nodes = graph.getNodes();
  const edges = graph.getEdges();

  // 至少需要 2 个节点
  if (nodes.length < 2) {
    isValid = false;
    errorMessages.push("流程图至少需要 2 个节点");
  }

  // 至少有 1 条连线
  if (edges.length === 0) {
    isValid = false;
    errorMessages.push("流程图至少有 1 条连线");
  }

  // 检查是否有完全孤立的节点
  const unconnectedNodes = nodes.filter(
    (node) =>
      !edges.some(
        (edge) =>
          edge.getSourceCellId() == node.id || // 作为来源（出度）
          edge.getTargetCellId() == node.id // 作为目标（入度）
      )
  );

  // 如果有未连接的节点，报错
  if (unconnectedNodes.length > 0) {
    errorMessages.push("存在未连接的节点，请检查流程图");
  }
  // 如果校验未通过且 flag 为 false，则显示错误信息
  if (errorMessages.length > 0 && !flag) {
    ElMessage.warning('校验未通过，' + errorMessages[0]);
  }

  return { isValid: errorMessages.length == 0, errorMessages };
};

const isValidClick = (tab) => {
  const { isValid, errorMessages } = validateGraph(true);
  let message = "";
  // Check graph validation
  if (!isValid && errorMessages.length != 0) {
    message += "检查未通过:<br>" + errorMessages.join("<br>"); // Replace \n with <br> for HTML line breaks
  } else {
    message += "检查通过";
  }
  if (
    !nodeData.value?.taskConfig ||
    Object.keys(nodeData.value.taskConfig).length === 0
  ) {
    message += "<br>任务配置未填写";
  }

  tabs.value[0].content = message;
};
</script>
<style scoped lang="less">
.app-container {
  height: calc(87vh - 7px);
  overflow: hidden;

  .tabs-container {
    position: relative;
  }

  .icon-right {
    position: absolute;
    top: 10px;
    right: 30px;
    cursor: pointer;
    font-size: 20px;
    color: #666;
    z-index: 1000000;
  }

  .head-container {
    height: 50px;
    background: #fff;
    box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
    padding: 0px 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    .head-title {
      font-family: PingFang SC;
      font-size: 16px;
      color: #000000;
      display: flex;
      align-items: center;

      &::before {
        content: "";
        display: inline-block;
        background: var(--el-color-primary);
        width: 6px;
        height: 16px;
        border-radius: 2px;
        margin-right: 10px;
      }
    }

    .head-btns {
      img {
        margin-right: 6px;
      }

      .currImg {
        display: inline-block;
      }

      .act {
        display: none;
      }

      .el-button {
        height: 28px;

        &:hover {
          .act {
            display: inline-block;
          }

          .currImg {
            display: none;
          }
        }
      }
    }
  }
}

.flex-container {
  display: flex;
  height: calc(87vh - 60px);
  overflow: hidden;
}

.left-pane {
  width: 300px;
  background-color: #fff;
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  margin-right: 15px;

  .left-tree {
    padding: 15px;
    flex: 1;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }

  .icon-img {
    width: 15px;
    height: 15px;
  }
}

.right-pane {
  min-height: 864px;
  width: 20vw;
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;

  .toolbar {
    position: absolute;
    top: 16px;
    right: unset;
    bottom: unset;
    left: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    // opacity: 0.65;
    z-index: 100;

    .toolbar-item {
      display: inline-block;
      width: 34px;
      height: 32px;
      cursor: pointer;
      box-sizing: content-box;
      margin-right: 5px;

      &:hover {
        background-color: rgba(255, 255, 255, 0.2);
      }

      img {
        width: 100%;
        height: 100%;
      }
    }

    .search {
      :deep(.el-input__wrapper) {
        background: transparent;

        .el-input__inner {
          color: #fff;
        }
      }
    }
  }

  .graph-container {
    flex: 1;
    min-height: 70vh;
    box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  }

  .tabs-container {
    position: relative;

    .icon-right {
      position: absolute;
      top: 10px;
      right: 30px;
      font-size: 20px;
      color: #666;
      z-index: 10000;
      cursor: pointer;
    }
  }
}

.el-aside {
  padding: 2px 0;
  margin-bottom: 0;
  background-color: #f0f2f5;
}


.custom-tree-node {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 0 36px 0 12px;

  .node-icon {
    width: 16px;
    height: 16px;
  }

  .treelable {
    margin-left: 10px;
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-family: PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.85);
  }
}

.highlight {
  stroke: red;
  stroke-width: 2px;
}

// .icon-img {
//   width: 20px;
//   height: 20px;
//   display: inline-block;
//   vertical-align: middle;
// }
.custom-tabs {
  font-size: 14px;
  padding-left: 20px;
}

.graph-container {
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1) !important;
  min-height: 70vh !important;
}

:deep(.x6-widget-selection-box) {
  fill: rgba(0, 123, 255, 0.3);
  /* 设置选中框的填充颜色 */
  stroke: #007bff;
  /* 设置选中框的边框颜色 */
  opacity: 1;
  /* 设置选中框的透明度 */
  pointer-events: none !important;
}

:deep(.x6-graph-background) {
  background-color: white !important;
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1) !important;
}

:deep(.x6-graph-grid) {
  display: none;
}

:deep(.dept-tree) {

  //组织树 背景颜色 及右边线颜色
  &.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background: rgba(51, 103, 252, 0.06) !important;
    border: none;

    .custom-tree-node {
      .treelable {
        color: var(--el-color-primary);
      }
    }
  }

  .el-tree-node__content {
    position: relative;

    .el-tree-node__expand-icon {
      position: absolute;
      right: 10px;
      color: transparent;
      font-size: 11px;
      width: 11px;
      height: 11px;

      &>svg {
        background: url("@/assets/da/asset/arrow.svg") no-repeat;
        background-size: 100% 100%;
        transform: rotate(-90deg);

      }
    }
  }
}
</style>
