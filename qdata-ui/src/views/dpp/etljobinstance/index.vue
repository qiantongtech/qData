<template>
  <div class="app-container" ref="app-container">
    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入作业类目名称'" ref="DeptTreeRef"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="实例名称" prop="name">
              <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入实例名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="执行状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择执行状态" clearable class="el-form-input-width">
                <el-option v-for="dict in dpp_etl_task_instance" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>

            <el-form-item label="执行时间" prop="time">
              <el-date-picker class="el-form-input-width" v-model="queryParams.time" @change="handleDateChange"
                value-format="YYYY-MM-DD" type="daterange" range-separator="-" start-placeholder="开始日期"
                end-placeholder="结束日期"></el-date-picker>
            </el-form-item>

            <el-form-item>
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
              </el-button>
              <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div class="pagecont-bottom">
          <div class="justify-between mb15">
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>
          </div>
          <el-table stripe height="570px" v-loading="loading" :data="dppEtlTaskList" row-key="dataId" lazy :load="load"
            :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
            <el-table-column label="作业实例名称" :show-overflow-tooltip="true" align="left" prop="name">
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column label="任务名称" :show-overflow-tooltip="true" align="left" prop="taskName" width="155">
              <template #default="scope">
                {{ scope.row.taskName || "-" }}
              </template>
            </el-table-column>
            <el-table-column label="执行类型" width="100" :show-overflow-tooltip="true" align="left" prop="commandType">
              <template #default="scope">
                <dict-tag v-if="scope.row.dataType == '1' || scope.row.dataType == '5'"
                  :options="dpp_etl_task_instance_command_type" :value="scope.row.commandType" />
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="调度时间" :show-overflow-tooltip="true" align="center" prop="scheduleTime" width="160">
              <template #default="scope">
                {{ scope.row.scheduleTime || "-" }}
              </template>
            </el-table-column>
            <el-table-column label="开始时间" :show-overflow-tooltip="true" align="center" prop="startTime" width="160">
              <template #default="scope">
                {{ scope.row.startTime || "-" }}
              </template>
            </el-table-column>
            <el-table-column label="结束时间" :show-overflow-tooltip="true" align="center" prop="endTime" width="160">
              <template #default="scope">
                {{ scope.row.endTime || "-" }}
              </template>
            </el-table-column>

            <el-table-column label="运行时长" :show-overflow-tooltip="true" align="left" prop="duration" width="80">
              <template #default="scope">
                {{ scope.row.duration || "-" }}
              </template>
            </el-table-column>

            <el-table-column label="运行次数" :show-overflow-tooltip="true" align="center" prop="runTimes" width="80">
              <template #default="scope">
                {{ scope.row.runTimes || "-" }}
              </template>
            </el-table-column>

            <el-table-column label="执行状态" width="80" :show-overflow-tooltip="true" align="center" fixed="right"
              prop="status">
              <template #default="scope">
                <dict-tag v-if="scope.row.dataType == '1'" :options="dpp_etl_task_instance" :value="scope.row.status" />
                <dict-tag v-else :options="dpp_etl_node_instance" :value="scope.row.status" />
              </template>
            </el-table-column>

            <el-table-column label="操作" header-align="center" class-name="small-padding fixed-width" fixed="right"
              width="180">
              <template #default="scope">
                <el-button v-if="scope.row.dataType == '1'" :disabled="scope.row.status !== '7' &&
                  scope.row.status !== '3' &&
                  scope.row.status !== '6' &&
                  scope.row.status !== '5'
                  " link type="primary" icon="View" @click="handleExecute(scope.row.id, 'REPEAT_RUNNING')">
                  重跑
                </el-button>
                <el-button v-if="scope.row.dataType != '1' && scope.row.dataType != '5'" link type="primary" icon="View"
                  @click="logDetailCatList(scope.row)">
                  查看日志
                </el-button>

                <el-button v-if="scope.row.dataType != '1' && scope.row.dataType != '5'" link type="warning"
                  @click="downloadLog(scope.row)" @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-download-line mr5"></i>下载日志
                </el-button>
              </template>
            </el-table-column>
            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
              </div>
            </template>
          </el-table>

          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-main>
    </el-container>
  </div>

  <!-- 日志详细 -->
  <el-dialog title="查看日志" v-model="open" width="1200px" :append-to="$refs['app-container']" draggable destroy-on-close>
    <div v-html="formattedText"></div>
  </el-dialog>
</template>

<script setup name="DppEtlTask">
import { execute } from "@/api/dpp/etl/dppEtlExecutors";
import {
  listDppEtlTreeList,
  subNodeList,
} from "@/api/dpp/etl/dppEtlTaskInstance";
import {
  listDppEtlNodeInstance,
  logDetailCat,
} from "@/api/dpp/etl/dppEtlNodeInstance";

const userStore = useUserStore();
import DeptTree from "@/components/DeptTree";
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";
import { listAttJobCat } from "@/api/att/cat/attJobCat/attJobCat";
import { ref } from "vue";

const { proxy } = getCurrentInstance();
const {
  dpp_etl_task_instance,
  dpp_etl_node_instance,
  dpp_etl_task_instance_command_type,
} = proxy.useDict(
  "dpp_etl_task_instance",
  "dpp_etl_node_instance",
  "dpp_etl_task_instance_command_type"
);
const deptOptions = ref([]);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => { });
  }
};

function handleDateChange(value) {
  queryParams.value.startTime = value[0];
  queryParams.value.endTime = value[1];
}

//加载子节点
function load(tree, treeNode, resolve) {
  var params = {
    taskInstanceId: tree.taskInstanceId,
    nodeInstanceId: tree.nodeInstanceId,
  };
  subNodeList(params).then((response) => {
    resolve(response.data);
  });
}

/** 下拉树结构 */
function getDeptTree() {
  listAttJobCat({ type: 4 }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "作业类目",
        value: "",
        children: deptOptions.value,
      },
    ];
  });
}

function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  queryParams.value.pageNum = 1;
  handleQuery();
}

const route = useRoute();
let openCron = ref(false);
const dppEtlTaskList = ref([]);
let row = ref();

const open = ref(false);
const loading = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const router = useRouter();

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    status: null,
    time: [],
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

watch(
  () => userStore.projectCode,
  (newCode) => {
    if (newCode) {
      getList();
      getDeptTree();
    }
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);

function handleExecute(taskInstanceId, executeType) {
  execute(taskInstanceId, executeType).then((response) => {
    if (response.code === 200) {
      proxy.$modal.msgSuccess("操作成功");
      getList();
    } else {
      proxy.$modal.msgError(response.msg);
    }
  });
}

/** 下载按钮操作 */
async function downloadLog(row) {
  const response = await logDetailCat(row.id);
  console.log(response, "response");

  if (response) {
    proxy.download(
      "/dpp/dppEtlNodeInstance/downloadLog",
      {
        handleMsg: row.logPath,
      },
      `${new Date().getTime()}.log`
    );
  }
}

let msg = ref();

async function logDetailCatList(row) {
  msg.value = {};
  const response = await logDetailCat(row.id);
  if (response && response.code === 200) {
    msg.value = response.msg;
    open.value = true;
  }
}
const formattedText = computed(() => {
  console.log("msg.value", msg.value);

  return msg.value.replace(/\n/g, "<br>");
});
/** 查询作业作业列表 */
function getList() {
  loading.value = true;
  queryParams.value.projectCode = userStore.projectCode;
  queryParams.value.projectId = userStore.projectId;
  listDppEtlTreeList({
    ...queryParams.value,
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
  }).then((response) => {
    dppEtlTaskList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

const DeptTreeRef = ref(null);

/** 重置按钮操作 */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.time = [];
  queryParams.value.catCode = "";
  queryParams.value.startTime = "";
  queryParams.value.endTime = "";
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  handleQuery();
}
</script>
<style scoped lang="scss">
::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #f3f8ff !important;
    border: 0px solid #6ba7ff !important;
    color: #2666fb !important;
  }
}

.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

//上传附件样式调整
::v-deep {

  // .el-upload-list{
  //    display: flex;
  // }
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}
</style>
