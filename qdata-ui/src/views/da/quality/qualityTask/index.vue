<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="da/dataQuality/dataQualityTasks.list" />

    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入数据质量类目名称'" ref="DeptTreeRef"
        @node-click="handleNodeClick" />
      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="任务名称" prop="taskName">
              <el-input class="el-form-input-width" v-model="queryParams.taskName" placeholder="请输入任务名称" clearable
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
                <el-button type="primary" plain @click="routeTo('/da/quality/qualityTask/add', { row: null, })"
                  v-hasPermi="['da:qualityTask:add']" @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="DppQualityTaskEvaluateList" :default-sort="defaultSort"
            @sort-change="handleSortChange">
            <el-table-column v-if="getColumnVisibility(1)" label="编号" align="center" prop="id" width="80" />
            <el-table-column v-if="getColumnVisibility(2)" label="任务名称" align="left" prop="taskName"
              :show-overflow-tooltip="{ effect: 'light' }" width="200">
              <template #default="scope">
                {{ scope.row.taskName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" label="所属类目" align="center" prop="catName"
              :show-overflow-tooltip="{ effect: 'light' }" width="150">
              <template #default="scope">
                {{ scope.row.catName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="描述" width="200" align="left" prop="description"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.description || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" label="稽查对象数" align="center" prop="taskObjNum" width="80"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.taskObjNum || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" label="稽查规则数" align="center" prop="taskEvaluateNum"
              width="90" :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.taskEvaluateNum || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" label="执行策略" align="center" prop="strategy">
              <template #default="scope">
                <dict-tag :options="dpp_etl_task_execution_type" :value="scope.row.strategy" />
              </template>
            </el-table-column>


            <el-table-column v-if="getColumnVisibility(8)" label="调度周期" align="center" prop="cycle"
              :show-overflow-tooltip="{ effect: 'light' }" width="240">
              <template #default="scope">
                {{ cronToZh(scope.row.cycle) || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" label="上次执行时间" align="center" prop="lastExecuteTime"
              width="160" :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ parseTime(scope.row.lastExecuteTime, '{y}-{m}-{d} {h}:{i}') || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(10)" width="120" label="创建人" align="center" prop="createBy"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(11)" label="创建时间" align="center" prop="createTime" width="150"
              sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(12)" align="center" prop="status" width="150">
              <template #header>
                <div class="justify-center" style="display: flex; align-items: center; justify-content: center;">
                  <span>状态</span>
                  <el-tooltip effect="light" content="状态开启 = 任务上线 + 执行调度计划。请合理制定调度周期。" placement="top">
                    <el-icon class="tip-icon" style="margin-left: 4px;">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </div>
              </template>

              <template #default="scope">
                <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
                  @change="handleStatusChange(scope.row)">
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(13)" label="备注" width="200" align="left" prop="remark"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>

            <el-table-column v-if="getColumnVisibility(14)" label="操作" align="center"
              class-name="small-padding fixed-width" fixed="right" width="200">
              <template #default="scope">
                <!--  :disabled="scope.row.status == 1" -->
                <el-button link type="primary" icon="Edit" @click="routeTo('/da/quality/qualityTask/edit', {
                  ...scope.row,
                })" v-hasPermi="['da:qualityTask:edit']" :disabled="scope.row.status != 1">
                  配置</el-button>
                <el-button link type="primary" icon="view" @click="
                  routeTo('/da/quality/qualityTask/detail', {
                    ...scope.row,
                    info: true,
                  })
                  " v-hasPermi="['da:qualityTask:info']">详情</el-button>

                <el-popover placement="bottom" :width="150" trigger="click">
                  <template #reference>
                    <el-button link type="primary" icon="ArrowDown">更多</el-button>
                  </template>
                  <div style="width: 100px" class="butgdlist">
                    <el-button link type="primary" icon="VideoPlay" style="padding-left: 14px"
                      @click="handleExecuteOnce(scope.row)" v-hasPermi="['da:qualityTask:once']"
                      :disabled="scope.row.status == 1">执行一次</el-button>
                    <el-button link type="primary" icon="Stopwatch" @click="handleDataView(scope.row)"
                      v-hasPermi="['da:qualityTask:edit']">执行记录</el-button>
                    <el-button link type="danger" icon="Delete" :disabled="scope.row.status != 1"
                      @click="handleDelete(scope.row)" v-hasPermi="['da:qualityTask:remove']">删除</el-button>
                    <el-button link icon="Operation" @click="handleJobLog(scope.row)" type="primary"
                      :disabled="scope.row.status != 1" v-hasPermi="['da:qualityTask:schedule']">调度周期</el-button>
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
    <DataViewDialog :visible="DataView" :taskType="3" @update:visible="DataView = $event" :data="form" title="执行记录" />
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

<script setup name="QualityTask">
import { treeData } from "./data.js";
import {
  createEtlTaskFront
} from "@/api/dpp/task/index.js";
import { cronToZh } from "@/utils/cronUtils";
import Crontab from "@/components/Crontab/index.vue";
import DataViewDialog from "./components/instance.vue";
const userStore = useUserStore();
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";
import DeptTree from "@/components/DeptTree";
import { deptUserTree } from "@/api/system/system/user.js";
import { ref } from "vue";
import { listAttQualityCat } from "@/api/att/cat/qualityCat/qualityCat.js";
const defaultSort = ref({ columnKey: 'create_time', order: 'desc' });
import {
  listDppQualityTask,
  delDppQualityTask,
  updateDppQualityTaskStatus,
  startDppQualityTask,
  updateDaDiscoveryTaskCronExpression
} from "@/api/da/quality/qualityTask";;

const { proxy } = getCurrentInstance();
const { da_discovery_task_status, dpp_etl_task_execution_type, datasource_type, dpp_etl_task_process_type } =
  proxy.useDict(
    "da_discovery_task_status",
    "dpp_etl_task_execution_type",
    "datasource_type",
    "dpp_etl_task_process_type"
  );
const typaOptions = treeData.map((item) => {
  return {
    ...item,
    label: item.label,
    value: item.label
  }
})

/** 排序触发事件 */
function handleSortChange({ column, prop, order }) {
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

const getExecutionType = (executionType) => {
  console.log(executionType);

  return typaOptions.find((item) => item.value == executionType)?.label
}
const getStatus = (status) => {
  if (status == '-1') {
    return '-1'
  } else {
    return '0'
  }
}
// 任务配置
const taskConfigDialogVisible = ref(false);
let userList = ref([]);
let taskForm = ref({});
const handleAdd = () => {
  taskConfigDialogVisible.value = true;
}
// 保存并关闭
const handleSave = (form) => {
  const parms = {
    ...form,

    projectCode: userStore.projectCode,
    type: "3",//数据开发新增标识
  }
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      getList();
    }
  })
}
const deptOptions = ref([]);
const leftWidth = ref(300); // 初始左侧宽度
/** 下拉树结构 */
function getDeptTree() {
  listAttQualityCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "数据质量类目",
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
const DppQualityTaskEvaluateList = ref([]);
let row = ref();
let expression = ref("");
/** 运行实例按钮操作 */
function handleJobLog(data) {
  row.value = "";
  row.value = data || "";
  openCron.value = true;
  expression.value = data.cycle || "";
}
/** 改变启用状态值 */
function handleStatusChange(row, e) {
  const text = row?.status == "1" ? "下线" : "上线";
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.taskName + '"质量任务吗？')
    .then(function () {
      loading.value = true;
      updateDppQualityTaskStatus({
        id: row.id,
        status: Number(row.status)
      })
        .then((response) => {
          proxy.$modal.msgSuccess("操作成功");
        })
        .catch((error) => {
          row.status = row.status === "1" ? "0" : "1";
        })
        .finally(() => {
          loading.value = false;
        });
    })
    .catch((error) => {
      row.status = row.status === "1" ? "0" : "1";
    });
}
/** 确定后回传值 */
function crontabFill(value) {
  row.value.crontab = value;
  updateDaDiscoveryTaskCronExpression({
    cycle: row.value.cycle,
    status: '1',
    id: Number(row.value.id),
  }).then((response) => {
    proxy.$modal.msgSuccess("操作成功");
    getList();
  });
}
const handleExecuteOnce = async (row) => {
  if (!row?.id) {
    proxy.$modal.msgWarning("无效的任务id，请刷新后重试");
    return;
  }
  loading.value = true;
  try {
    const res = await startDppQualityTask(row.id);

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
// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "任务名称", visible: true },
  { key: 3, label: "所属类目", visible: true },
  { key: 4, label: "描述", visible: true },
  { key: 5, label: "稽查对象数", visible: true },
  { key: 6, label: "稽查规则数", visible: true },
  { key: 7, label: "执行策略", visible: true },
  { key: 8, label: "调度周期", visible: true },
  { key: 9, label: "上次执行时间", visible: true },
  { key: 10, label: "创建人", visible: true },
  { key: 11, label: "创建时间", visible: true },
  { key: 12, label: "状态", visible: true },
  { key: 13, label: "备注", visible: true },
  { key: 14, label: "操作", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  if (!column) return true;
  return column.visible;
};

const open = ref(false);
const loading = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const total = ref(0);
const router = useRouter();
const data = reactive({
  form: {

  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    type: null,
    taskName: null,
    status: null,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);



function getList() {
  loading.value = true;
  queryParams.value.projectCode = userStore.projectCode;
  queryParams.value.projectId = userStore.projectId;
  listDppQualityTask(queryParams.value).then((response) => {
    DppQualityTaskEvaluateList.value = response.data?.rows || [];
    total.value = response.data.total;
    loading.value = false;
  });


  // getDppQualityTask(23).then(r => {
  //   console.log(r, "999999999")
  // })
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
/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除数据质量任务编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDppQualityTask(_ids);
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
          id: row?.id,
          info: row?.info,
        },
      });
    }
  }
}

// onActivated(() => {
// });
onActivated(() => {
  getList();
});
getList();
getDeptTree();

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
}
</style>
