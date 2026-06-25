<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="da/dataQuality/dataQualityTasks.list" />

    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="td('da.qualityTask.deptTreePlaceholder')" ref="DeptTreeRef"
        @node-click="handleNodeClick" />
      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
            v-show="showSearch" @submit.prevent>
            <el-form-item :label="td('da.qualityTask.taskName')" prop="taskName">
              <el-input class="el-form-input-width" v-model="queryParams.taskName" :placeholder="td('da.qualityTask.taskNamePlaceholder')" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('da.qualityTask.taskStatus')" prop="status">
              <el-select v-model="queryParams.status" :placeholder="td('da.qualityTask.taskStatusPlaceholder')" clearable class="el-form-input-width">
                <el-option v-for="dict in da_discovery_task_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
              </el-button>
              <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
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
                  <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe v-loading="loading" :data="DppQualityTaskEvaluateList" :default-sort="defaultSort"
            @sort-change="handleSortChange">
            <el-table-column v-if="getColumnVisibility(1)" :label="td('da.qualityTask.columnVisibility.id')" align="center" prop="id" width="80" />
            <el-table-column v-if="getColumnVisibility(2)" :label="td('da.qualityTask.columnVisibility.taskName')" align="left" prop="taskName"
              :show-overflow-tooltip="{ effect: 'light' }" width="200">
              <template #default="scope">
                {{ scope.row.taskName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" :label="td('da.qualityTask.columnVisibility.belongCat')" align="center" prop="catName"
              :show-overflow-tooltip="{ effect: 'light' }" width="150">
              <template #default="scope">
                {{ scope.row.catName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" :label="td('da.qualityTask.columnVisibility.description')" width="200" align="left" prop="description"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.description || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" :label="td('da.qualityTask.columnVisibility.inspectionTargetCount')" align="center" prop="taskObjNum" width="80"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.taskObjNum || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" :label="td('da.qualityTask.columnVisibility.inspectionRuleCount')" align="center" prop="taskEvaluateNum"
              width="160" :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.taskEvaluateNum || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(7)" :label="td('da.qualityTask.columnVisibility.executionStrategy')" align="center" prop="strategy">
              <template #default="scope">
                <dict-tag :options="dpp_etl_task_execution_type" :value="scope.row.strategy" />
              </template>
            </el-table-column>


            <el-table-column v-if="getColumnVisibility(8)" :label="td('da.qualityTask.columnVisibility.schedulePeriod')" align="center" prop="cycle"
              :show-overflow-tooltip="{ effect: 'light' }" width="240">
              <template #default="scope">
                {{ cronToZh(scope.row.cycle) || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" :label="td('da.qualityTask.columnVisibility.lastExecutionTime')" align="center" prop="lastExecuteTime"
              width="160" :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ parseTime(scope.row.lastExecuteTime, '{y}-{m}-{d} {h}:{i}') || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(10)" width="120" :label="td('da.qualityTask.columnVisibility.createdBy')" align="center" prop="createBy"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(11)" :label="td('da.qualityTask.columnVisibility.createdTime')" align="center" prop="createTime" width="150"
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
                  <span>{{ td('da.qualityTask.columnVisibility.status') }}</span>
                  <el-tooltip effect="light" :content="td('da.qualityTask.statusTooltip')" placement="top">
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
            <el-table-column v-if="getColumnVisibility(13)" :label="td('da.qualityTask.columnVisibility.remark')" width="200" align="left" prop="remark"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>

            <el-table-column v-if="getColumnVisibility(14)" :label="td('common.texts.operation')" align="center"
              class-name="small-padding fixed-width" fixed="right" width="240">
              <template #default="scope">
                <!--  :disabled="scope.row.status == 1" -->
                <el-button link type="primary" icon="Edit" @click="routeTo('/da/quality/qualityTask/edit', {
                  ...scope.row,
                })" v-hasPermi="['da:qualityTask:edit']" :disabled="scope.row.status != 1">
                  {{ td('da.qualityTask.config') }}</el-button>
                <el-button link type="primary" icon="view" @click="
                  routeTo('/da/quality/qualityTask/detail', {
                    ...scope.row,
                    info: true,
                  })
                  " v-hasPermi="['da:qualityTask:info']">{{ td('common.button.details') }}</el-button>

                <el-popover placement="bottom" :width="150" trigger="click">
                  <template #reference>
                    <el-button link type="primary" icon="ArrowDown">{{ td('common.button.more') }}</el-button>
                  </template>
                  <div style="width: 100px" class="butgdlist">
                    <el-button link type="primary" icon="VideoPlay" style="padding-left: 14px"
                      @click="handleExecuteOnce(scope.row)" v-hasPermi="['da:qualityTask:once']"
                      :disabled="scope.row.status == 1">{{ td('da.qualityTask.executeOnce') }}</el-button>
                    <el-button link type="primary" icon="Stopwatch" @click="handleDataView(scope.row)"
                      v-hasPermi="['da:qualityTask:edit']">{{ td('da.qualityTask.executionLog') }}</el-button>
                    <el-button link type="danger" icon="Delete" :disabled="scope.row.status != 1"
                      @click="handleDelete(scope.row)" v-hasPermi="['da:qualityTask:remove']">{{ td('common.button.delete') }}</el-button>
                    <el-button link icon="Operation" @click="handleJobLog(scope.row)" type="primary"
                      :disabled="scope.row.status != 1" v-hasPermi="['da:qualityTask:schedule']">{{ td('da.qualityTask.schedulePeriodLabel') }}</el-button>
                  </div>
                </el-popover></template>
            </el-table-column>
            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
                <p>{{td('common.noData')}}</p>
              </div>
            </template>
          </el-table>
          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-main>
    </el-container>
    <DataViewDialog :visible="DataView" :taskType="3" @update:visible="DataView = $event" :data="form" :title="td('da.qualityTask.executionLog')" />
    <el-dialog :title="td('da.qualityTask.cronTitle')" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close :appendTo="'#app'">
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
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";
import DeptTree from "@/components/DeptTree";
import { deptUserTree } from "@/api/system/system/user.js";
import { ref } from "vue";
import { listAttQualityCat } from "@/api/att/cat/qualityCat/qualityCat.js";
import useDefaultLang from "@/composables/useDefaultLang";

const userStore = useUserStore();
const { td } = useDefaultLang();
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
      proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
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
        name: td('da.qualityTask.catRootName'),
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
  const text = row?.status == "1" ? td('da.qualityTask.offline') : td('da.qualityTask.online');
  proxy.$modal
    .confirm(td('da.qualityTask.confirmStatusChange', { text, name: row.taskName }))
    .then(function () {
      loading.value = true;
      updateDppQualityTaskStatus({
        id: row.id,
        status: Number(row.status)
      })
        .then((response) => {
          proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
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
    proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
    getList();
  });
}
const handleExecuteOnce = async (row) => {
  if (!row?.id) {
    proxy.$modal.msgWarning(td('da.qualityTask.invalidTaskId'));
    return;
  }
  loading.value = true;
  try {
    const res = await startDppQualityTask(row.id);

    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess(td('da.qualityTask.executeSuccess'));
    } else {
      proxy.$modal.msgWarning(res?.msg || td('da.qualityTask.executeFailed'));
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
  { key: 1, label: td('da.qualityTask.columnVisibility.id'), visible: true },
  { key: 2, label: td('da.qualityTask.columnVisibility.taskName'), visible: true },
  { key: 3, label: td('da.qualityTask.columnVisibility.belongCat'), visible: true },
  { key: 4, label: td('da.qualityTask.columnVisibility.description'), visible: true },
  { key: 5, label: td('da.qualityTask.columnVisibility.inspectionTargetCount'), visible: true },
  { key: 6, label: td('da.qualityTask.columnVisibility.inspectionRuleCount'), visible: true },
  { key: 7, label: td('da.qualityTask.columnVisibility.executionStrategy'), visible: true },
  { key: 8, label: td('da.qualityTask.columnVisibility.schedulePeriod'), visible: true },
  { key: 9, label: td('da.qualityTask.columnVisibility.lastExecutionTime'), visible: true },
  { key: 10, label: td('da.qualityTask.columnVisibility.createdBy'), visible: true },
  { key: 11, label: td('da.qualityTask.columnVisibility.createdTime'), visible: true },
  { key: 12, label: td('da.qualityTask.columnVisibility.status'), visible: true },
  { key: 13, label: td('da.qualityTask.columnVisibility.remark'), visible: true },
  { key: 14, label: td('common.texts.operation'), visible: true },
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
    .confirm(td('da.qualityTask.confirmDelete', { id: _ids }))
    .then(function () {
      return delDppQualityTask(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
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
