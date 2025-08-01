<template>
  <div class="app-container" ref="app-container">
    <el-container>
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入数据集成类目名称'" ref="DeptTreeRef"
        @node-click="handleNodeClick" />
      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="任务名称" prop="name">
              <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入任务名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="任务状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable class="el-form-input-width">
                <el-option v-for="dict in dpp_etl_task_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
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
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button type="primary" plain @click="openTaskConfigDialog" v-hasPermi="['dpp:etl:etltask:add']">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe height="58vh" v-loading="loading" :data="dppEtlTaskList" :default-sort="defaultSort">
            <el-table-column v-if="getColumnVisibility(0)" label="编号" width="100" align="center" prop="id" />
            <el-table-column v-if="getColumnVisibility(2)" label="任务名称" :show-overflow-tooltip="true" align="left"
              prop="name" width="200">
              <template #default="scope">
                <div class="justify">
                  <img :src="getDatasourceIcon(scope.row.draftJson)" alt="" :style="getDatasourceIcon(scope.row.draftJson)?'width: 20px;margin-right: 5px;':''">
                  <span>{{ scope.row.name || "-" }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" label="任务描述" :show-overflow-tooltip="true" align="left"
              prop="description" width="240">
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" label="任务类目" :show-overflow-tooltip="true" align="left"
              prop="catName" width="140">
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" label="任务状态" width="80" align="center" prop="releaseState">
              <template #default="scope">
                <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
                  :inactive-value="getStatus(scope.row.status)" @change="handleStatusChange(scope.row.id, scope.row)"
                  :disabled="scope.row.status == '-1'" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" label="调度状态" width="80" align="center" prop="schedulerState">
              <template #default="scope">
                <el-switch v-model="scope.row.schedulerState" active-color="#13ce66" inactive-color="#ff4949"
                  active-value="1" inactive-value="0" :disabled="scope.row.status != '1'"
                  @change="handleschedulerState(scope.row.id, scope.row)" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="执行策略" width="80" :show-overflow-tooltip="true"
              align="center" prop="executionType">
              <template #default="scope">
                <dict-tag :options="dpp_etl_task_execution_type" :value="scope.row.executionType" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" label="调度周期" :show-overflow-tooltip="true" align="left"
              prop="cronExpression" width="240">
              <template #default="scope">
                {{ cronToZh(scope.row.cronExpression) || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" label="配置状态" :show-overflow-tooltip="true" align="center"
              prop="status" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status == -1 ? 'warning' : 'success'">{{ scope.row.status == -1 ? "草稿" : "完成"
                }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" label="最近运行时间" align="center" width="160"
              :show-overflow-tooltip="true" prop="lastExecuteTime">
              <template #default="scope">
                <span>{{
                  parseTime(
                    scope.row.lastExecuteTime,
                    "{y}-{m}-{d} {h}:{i}:{s}"
                  ) || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="220">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" :disabled="scope.row.status == 1"
                  @click="routeTo('/dpp/etl/lntegratio', scope.row)"
                  v-hasPermi="['dpp:etl:etltask:edit']">配置转化</el-button>
                <el-button link type="primary" icon="view" @click="
                  routeTo('/dpp/etl/indodeev', {
                    ...scope.row,
                    info: true,
                  })
                  " v-hasPermi="['dpp:etl:etltask:info']">详情</el-button>

                <el-popover placement="bottom" :width="150" trigger="click">
                  <template #reference>
                    <el-button link type="primary" icon="ArrowDown">更多</el-button>
                  </template>
                  <div style="width: 100px" class="butgdlist">
                    <el-button link style="padding-left: 14px" type="primary" icon="Operation"
                      @click="handleJobLog(scope.row)" :disabled="scope.row.schedulerState == '1'"
                      v-hasPermi="['dpp:etl:etltask:query']">调度周期</el-button>
                    <el-button link type="primary" icon="Stopwatch" @click="handleDataView(scope.row)"
                      v-hasPermi="['da:discovery:discoverytask:edit']">运行实例</el-button>
                    <el-button link type="primary" icon="VideoPlay" :disabled="scope.row.status == 0"
                      @click="handleExecuteOnce(scope.row)"
                      v-hasPermi="['da:discovery:discoverytask:once']">执行一次</el-button>
                    <el-button link type="danger" icon="Delete" :disabled="scope.row.status == 1"
                      @click="handleDelete(scope.row)" v-hasPermi="['dpp:etl:etltask:remove']">删除</el-button>
                  </div>
                </el-popover></template>
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
    <DataViewDialog :visible="DataView" :taskType="1" @update:visible="DataView = $event" @confirm="submitForm"
      :data="form" title="运行实例" />
    <el-dialog title="调度周期" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close :appendTo="'#app'">
      <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression">
      </crontab>
      <!--      <crontab-->
      <!--        ref="crontabRef"-->
      <!--        @hide="openCron = false"-->
      <!--        @fill="crontabFill"-->
      <!--        :expression="expression"-->
      <!--        :Crontab="false"-->
      <!--      >-->
      <!--      </crontab>-->
    </el-dialog>
    <!-- 新增 -->
    <taskConfigDialog :visible="taskConfigDialogVisible" title="新增任务" @update:visible="taskConfigDialogVisible = $event"
      @save="handleSave" @confirm="handleConfirm" :data="nodeData" :userList="userList" :deptOptions="deptOptions"
      :info="route.query.info" />
  </div>
</template>

<script setup name="DppEtlTask">
import {
  listDppEtlTask,
  delDppEtlTask,
  addDppEtlTask,
  updateDppEtlTask,
  updateReleaseSchedule,
  updateReleaseJobTask,
  releaseTaskCrontab,
  startDppEtlTask,
  createEtlTaskFront
} from "@/api/dpp/etl/dppEtlTask";
import { cronToZh } from "@/utils/cronUtils";
import Crontab from "@/components/Crontab/index.vue";
import DataViewDialog from "@/views/dpp/etl/components/DataViewDialog.vue";
const userStore = useUserStore();
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";
import { listAttTaskCat } from "@/api/att/cat/attTaskCat/attTaskCat";
import DeptTree from "@/components/DeptTree";
import taskConfigDialog from "./components/taskConfigDialog.vue";
import { deptUserTree } from "@/api/system/system/user.js";

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
const getStatus = (status) => {
  if (status == '-1') {
    return '-1'
  } else {
    return '0'
  }
}
// 任务配置
const taskConfigDialogVisible = ref(false);
let nodeData = ref({ taskConfig: {}, name: null });//新增无id时为空数据
let userList = ref([]);
// 保存并关闭
const handleSave = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    draftJson: JSON.stringify({
      ...form,
    }),
  }
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      getList();
    }
  })
}
// 保存并完善
const handleConfirm = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    draftJson: JSON.stringify({
      ...form,
    }),
  }
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      getList();
      routeTo('/dpp/etl/lntegratio', res.data);
    }
  })
};
const openTaskConfigDialog = () => {
  taskConfigDialogVisible.value = true;
};

const { proxy } = getCurrentInstance();
const { dpp_etl_task_status, dpp_etl_task_execution_type } = proxy.useDict(
  "dpp_etl_task_status",
  "dpp_etl_task_execution_type"
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

/** 下拉树结构 */
function getDeptTree() {
  listAttTaskCat({
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
  }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "数据集成类目",
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
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
let expression = ref("");
/** 运行实例按钮操作 */
function handleJobLog(data) {
  row.value = "";
  row.value = data || "";
  openCron.value = true;
  expression.value = data.cronExpression || "";
  console.log("🚀 ~ handleJobLog ~ expression.value:", expression.value);
}
function handleschedulerState(id, row, e) {
  const text = row.schedulerState == "1" ? "上线" : "下线";

  // 弹出确认框
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据集成调度状态吗？')
    .then(function () {
      loading.value = true;
      // 调用后台接口更新调度状态
      updateReleaseSchedule({
        id,
        schedulerState: row.schedulerState,
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      })
        .then((response) => {
          proxy.$modal.msgSuccess("操作成功");
        })
        .catch((error) => {
          // 处理失败时的恢复操作
          row.schedulerState = row.schedulerState == "1" ? "0" : "1"; // 恢复之前的状态
        })
        .finally(() => {
          loading.value = false; // 无论成功失败都停止加载
        });
    })
    .catch((error) => {
      // 失败时恢复状态
      row.schedulerState = row.schedulerState == "1" ? "0" : "1";
    });
}
/** 改变启用状态值 */
function handleStatusChange(id, row, e) {
  const text = row.status == "1" ? "上线" : "下线";

  // 弹出确认框
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据集成任务吗？')
    .then(function () {
      loading.value = true; // 开始加载
      // 调用后台接口更新发布状态
      updateReleaseJobTask({
        id,
        releaseState: row.status,
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      })
        .then((response) => {
          proxy.$modal.msgSuccess("操作成功");
        })
        .catch((error) => {
          // 失败时恢复状态
          row.status = row.status === "1" ? "0" : "1";
        })
        .finally(() => {
          loading.value = false; // 无论成功失败都停止加载
        });
    })
    .catch((error) => {
      // 失败时恢复状态
      row.status = row.status === "1" ? "0" : "1";
    });
}
const handleExecuteOnce = async (row) => {
  if (!row?.id) {
    proxy.$modal.msgError("无效的任务 ID");
    return;
  }
  loading.value = true;
  try {
    const res = await startDppEtlTask(row.id);

    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess("执行成功");
    } else {
      proxy.$modal.msgError(res?.msg || "执行失败");
    }
  } finally {
    loading.value = false;
  }
};
let DataView = ref(false);
/** 运行实例接口 */
function handleDataView(row) {
  form.value = row;
  DataView.value = true;
}
// 列显隐信息
const columns = ref([
  { key: 0, label: "编号", visible: true },
  { key: 1, label: "任务名称", visible: true },
  { key: 2, label: "任务描述", visible: true },
  { key: 9, label: "任务类目", visible: true },
  { key: 3, label: "任务状态", visible: true },
  { key: 4, label: "执行策略", visible: true },
  { key: 5, label: "调度周期", visible: true },
  { key: 7, label: "配置状态", visible: true },
  { key: 6, label: "最近运行时间", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const loading = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const total = ref(0);
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    type: null,
    name: null,
    status: null,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);
watch(
  () => userStore.projectId,
  () => {
    getList();
    getDeptTree();
  }
);
/** 查询数据集成任务列表 */
function getList() {
  loading.value = true;
  queryParams.value.projectCode = userStore.projectCode;
  queryParams.value.projectId = userStore.projectId;
  listDppEtlTask(queryParams.value).then((response) => {
    dppEtlTaskList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    type: null,
    name: null,
    status: null,
  };
  proxy.resetForm("dppEtlTaskRef");
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
  queryParams.value.catCode = "";
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  handleQuery();
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["dppEtlTaskRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDppEtlTask(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addDppEtlTask(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除数据集成任务编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDppEtlTask(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => { });
}

function routeTo(link, row) {
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
          id: row.id,
          info: row.info,
        },
      });
    }
  }
}

onActivated(() => {
  getList();
  getDeptTree();
});
</script>
<style scoped lang="scss">
::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #f3f8ff !important;
    border: 0px solid #6ba7ff !important;
    color: #2666fb !important;
  }
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
  transition: width 0.2s;
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
