<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="da/discoverytask.list" />

    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入数据发现类目名称'" ref="DeptTreeRef"
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
                <el-option v-for="dict in da_discovery_task_status" :key="dict.value" :label="dict.label"
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
                <el-button type="primary" plain @click="handleAdd" v-hasPermi="['da:discoveryTask:add']"
                  @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="daDiscoveryTaskList" :default-sort="defaultSort"
            @sort-change="handleSortChange">
            <!-- -->
            <el-table-column v-if="getColumnVisibility(0)" label="编号" width="80" align="center" prop="id" />
            <el-table-column v-if="getColumnVisibility(1)" label="任务名称" :show-overflow-tooltip="{ effect: 'light' }"
              align="left" prop="name" width="250">
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(2)" label="描述" width="240" align="left" prop="description"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.description || '-' }}
              </template>
            </el-table-column>
            <!-- <el-table-column v-if="getColumnVisibility(2)" label="数据连接名称" :show-overflow-tooltip="{effect: 'light'}" align="left"
              prop="datasourceName">
              <template #default="scope">
                {{ scope.row.datasourceName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" width="120" label="数据连接类型" :show-overflow-tooltip="{effect: 'light'}"
              align="left" prop="datasourceType">
              <template #default="scope">
                {{ scope.row.datasourceType || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="执行策略" align="center" prop="misfirePolicy">
              <template #default="scope">
                {{ scope.row.misfirePolicy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="责任人" :show-overflow-tooltip="{effect: 'light'}" align="center"
              width="100" prop="contact">
              <template #default="scope">
                {{ scope.row.contact || "-" }}
              </template>
            </el-table-column> -->

            <el-table-column v-if="getColumnVisibility(3)" width="100" label="上次变化表数"
              :show-overflow-tooltip="{ effect: 'light' }" align="center" prop="lastTableCount">
              <template #default="scope">
                {{ scope.row.lastTableCount || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="最近执行时间" align="center" prop="lastExecuteTime"
              width="150" sortable="custom" column-key="last_execute_time" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.lastExecuteTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" label="下次执行时间" align="center" prop="updateTime" width="150"
              sortable="custom" column-key="update_time" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{
                  scope.row.status == 0 ? parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}") : "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" width="120" label="创建人" align="center" prop="createBy"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" label="创建时间" align="center" prop="createTime" width="150"
              sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column label="任务状态" align="center" width="100" v-if="getColumnVisibility(8)">
              <template #header>
                <div class="justify-center">
                  <span style="margin-right: 5px">任务状态</span>
                  <el-tooltip effect="light" content="状态开启 = 任务上线 + 执行调度计划。请合理制定调度周期" placement="top">
                    <el-icon class="tip-icon">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </div>
              </template>
              <template #default="scope">
                <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
                  @change="handleStatusChange(scope.row)"></el-switch>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" label="备注" width="200" align="left" prop="remark"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" v-if="getColumnVisibility(10)" align="center"
              class-name="small-padding fixed-width" fixed="right" width="190">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                  :disabled="scope.row.status == 0" v-hasPermi="['da:discoveryTask:edit']">修改</el-button>
                <el-button link type="primary" icon="view" @click="
                  routeTo('/da/discovery/detail', scope.row)
                  " v-hasPermi="['da:discoveryTask:edit']">详情</el-button>

                <el-popover placement="bottom" :width="150" trigger="click">
                  <template #reference>
                    <el-button link type="primary" @click="isOk = !isOk" icon="ArrowDown">更多</el-button>
                  </template>
                  <div style="width: 100px" class="butgdlist">
                    <el-button link style="padding-left: 14px" type="primary" icon="Operation"
                      @click="handleJobLog(scope.row)" :disabled="scope.row.schedulerState == '1'"
                      v-hasPermi="['monitor:job:query']">调度周期</el-button>
                    <el-button link type="primary" icon="Document" @click="handleDataView(scope.row)"
                      v-hasPermi="['da:discoveryTask:edit']">运行实例</el-button>
                    <el-button link type="primary" icon="Stopwatch" :disabled="scope.row.status == 1"
                      @click="handleExecuteOnce(scope.row)" v-hasPermi="['da:discoveryTask:edit']">执行一次</el-button>
                    <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                      :disabled="scope.row.status == 0" v-hasPermi="['da:discoveryTask:remove']">删除</el-button>
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
    <CreateEditModal :deptOptions="deptOptions" :visible="open" :title="title" @update:visible="open = $event"
      @confirm="submitForm" :data="form" :userList="userList" :createTypeList="createTypeList" />
    <DataViewDialog :visible="DataView" @update:visible="DataView = $event" @confirm="submitForm" :data="form"
      title="运行实例" />

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
  </div>
</template>

<script setup name="Discovery">
import {
  listDaDiscoveryTask,
  getDaDiscoveryTask,
  delDaDiscoveryTask,
  addDaDiscoveryTask,
  updateDaDiscoveryTask,
  listJobLog,
  updateDaDiscoveryTaskStatus,
  updateDaDiscoveryTaskCronExpression,
  startDppEtlTask,
} from "@/api/da/discovery/discoveryTask";
import Crontab from "@/components/Crontab/index.vue";
import { getToken } from "@/utils/auth.js";
import { listUser } from "@/api/system/system/user.js";
import { deptUserTree } from "@/api/system/system/user.js";
import DeptTree from "@/components/DeptTree";
import { listAttDiscoverTaskCat } from "@/api/att/cat/discoverTaskCat/discoverTaskCat.js";
import {
  createMaterializedTable,
  getDaDatasourceList,
} from "@/api/dp/model/model";
import CreateEditModal from "./components/add.vue";
import DataViewDialog from "./components/instance.vue";
import { ref } from "vue";
function handleCommand(command, row) {
  switch (command) {
    case "handleRun":
      handleRun(row);
      break;
    case "handleView":
      handleView(row);
      break;
    case "handleJobLog":
      handleJobLog(row);
      break;
    default:
      break;
  }
}
const { proxy } = getCurrentInstance();
const { sys_job_status, sys_job_group, da_discovery_task_status } =
  proxy.useDict("sys_job_status", "sys_job_group", "da_discovery_task_status");
let openCron = ref(false);
const expression = ref("");
const defaultSort = ref({ columnKey: 'create_time', order: 'desc' });

const userList = ref([]);
const handleContactChange = (selectedValue) => {
  const selectedUser = userList.value.find(
    (user) => user.userId == selectedValue
  );
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
// 数据库
let createTypeList = ref();
const getDaDatasourceListList = async () => {
  try {
    const response = await getDaDatasourceList();
    createTypeList.value = response.data;
  } catch (error) {
    console.error("请求失败:", error);
  }
};

const deptOptions = ref([]);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
const isOk = ref(true);
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
  listAttDiscoverTaskCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "数据发现类目",
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
const daDiscoveryTaskList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "编号", visible: true },
  { key: 1, label: "任务名称", visible: true },
  { key: 2, label: "描述", visible: true },
  { key: 3, label: "上次变化表数", visible: true },
  { key: 4, label: "最近执行时间", visible: true },
  { key: 5, label: "下次执行时间", visible: true },
  { key: 6, label: "创建人", visible: true },
  { key: 7, label: "创建时间", visible: true },
  { key: 8, label: "任务状态", visible: true },
  { key: 9, label: "备注", visible: true },
  { key: 10, label: "操作", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const router = useRouter();

/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/da/daDiscoveryTask/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    status: null,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

/** 查询数据发现任务列表 */
function getList() {
  loading.value = true;
  listDaDiscoveryTask(queryParams.value).then((response) => {
    daDiscoveryTaskList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 排序触发事件 */
function handleSortChange({ column, prop, order }) {
  console.log("column?.columnKey::" + column?.columnKey);
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}


// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    datasourceId: null,
    status: null,
    cronExpression: null,
    contact: null,
    contactId: null,
    contactNumber: null,
    catCode: null,
    description: null,
    systemJobId: null,
    lastExecuteTime: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("daDiscoveryTaskRef");
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

let row = ref();

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  form.value.catCode = queryParams.value.catCode;
  title.value = "新增数据发现任务";
}
function removeMetadataFields(row) {
  // 删除指定字段
  const fieldsToRemove = [
    "creatorId",
    "createBy",
    "createTime",
    "updaterId",
    "updateBy",
    "updateTime",
  ];

  fieldsToRemove.forEach((field) => {
    delete row[field];
  });

  return row;
}

/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.status == 0 ? "上线" : "下线";
  proxy.$modal
    .confirm("确认要" + text + ',"' + row.name + '"任务吗？')
    .then(function () {
      updateTaskStatus(row);
    })
    .catch(function () {
      console.log("222", row.status);
      row.status = row.status === "0" ? "1" : "0";
    });
}

/** 运行实例按钮操作 */
function handleJobLog(data) {
  row.value = "";
  row.value = data;
  console.log("🚀 ~ handleJobLog ~   row.value :", row.value);
  openCron.value = true;
  expression.value = data.cronExpression;
}
/** 确定后回传值 */
function crontabFill(value) {
  console.log("🚀 ~ crontabFill ~ row.value:", row.value);
  row.value.cronExpression = value;
  submitFormCronExpression(row.value);
}
const handleExecuteOnce = async (row) => {
  if (!row?.id) {
    proxy.$modal.msgWarning("无效的任务id，请刷新后重试");
    return;
  }
  loading.value = true;
  try {
    const res = await startDppEtlTask(row.id);

    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess("执行成功");
    } else {
      proxy.$modal.msgWarning(res?.msg || "执行失败");
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
/** 修改按钮操作 */
function handleUpdate(row) {
  const _id = row.id || ids.value;
  getDaDiscoveryTask(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改数据发现任务";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDaDiscoveryTask(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "数据发现任务详情";
  });
  routeTo("/da/discovery", scope.row);
}

/** 提交按钮 */
function submitForm(data) {
  // proxy.$refs["daDiscoveryTaskRef"].validate((valid) => {
  //   if (valid) {
  if (data.id != null) {
    updateDaDiscoveryTask(removeMetadataFields(data))
      .then((response) => {
        proxy.$modal.msgSuccess("修改成功");
        open.value = false;
        getList();
      })
      .catch((error) => { });
  } else {
    addDaDiscoveryTask(data)
      .then((response) => {
        proxy.$modal.msgSuccess("新增成功");
        open.value = false;
        getList();
      })
      .catch((error) => { });
  }
  // }
  // });
}

/** 提交按钮 */
function updateTaskStatus(data) {
  const updateTaskStatusData = {
    id: data.id,
    status: data.status,
  };
  updateDaDiscoveryTaskStatus(removeMetadataFields(updateTaskStatusData))
    .then((response) => {
      proxy.$modal.msgSuccess("修改成功");
      open.value = false;
      getList();
    })
    .catch((error) => { });
}

/** 提交按钮 */
function submitFormCronExpression(data) {
  const updateTaskStatusData = {
    id: data.id,
    cronExpression: data.cronExpression,
  };
  updateDaDiscoveryTaskCronExpression(
    removeMetadataFields(updateTaskStatusData)
  )
    .then((response) => {
      proxy.$modal.msgSuccess("修改成功");
      open.value = false;
      getList();
    })
    .catch((error) => { });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除数据发现任务编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDaDiscoveryTask(_ids);
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
        },
      });
    }
  }
}

getDaDatasourceListList();
getList();
getDeptTree();
// queryParams.value.orderByColumn = defaultSort.value.columnKey;
// queryParams.value.isAsc = defaultSort.value.order;


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
