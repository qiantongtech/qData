<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qData Data Middle Platform (Commercial Edition)
  Software Copyright Registration No. 16069171
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qData 数据中台（商业版） | 软著登字第16069171号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="dpp/tasker/dppEtlTask.list" />
    <el-container>
      <DeptTree
        :api="api"
        :editable="true"
        :leftWidth="leftWidth"
        :placeholder="'请输入数据集成类目名称'"
        ref="DeptTreeRef"
        title="数据集成类目"
        @node-click="handleNodeClick"
        :extraParams="{
          projectCode: userStore.projectCode,
          projectId: userStore.projectId,
        }"
      />
      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
            />
          </template>
          <template #actions-data>
            <el-button type="primary" plain @click="openTaskConfigDialog">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #name="{ row }">
              <div class="name-label task-title">
                <div
                  class="justify task-title-row"
                  @click="
                    routeTo('/dpp/task/integratioTask/detail', {
                      ...row,
                      info: true,
                    })
                  "
                >
                  <img
                    :src="getDatasourceIcon(row.draftJson)"
                    alt=""
                    class="datasource-icon"
                    v-if="getDatasourceIcon(row.draftJson)"
                  />
                  <el-link
                    type="primary"
                    :underline="false"
                    class="task-name-text task-name-ellipsis"
                    :title="row.name"
                  >
                    {{ row.name || "-" }}
                  </el-link>
                  <el-tag
                    type="primary"
                    :underline="false"
                    class="task-cat-ellipsis"
                    :title="row.catName"
                  >
                    {{ row.catName || "-" }}
                  </el-tag>
                </div>
                <div class="text-ellipsis desc-text" :title="row.description">
                  {{ row.description || "-" }}
                </div>
              </div>
            </template>
            <template #releaseState="{ row }">
              <div class="flex-column fz12">
                <div class="flex-center">
                  <span class="black-label mr5">任务状态:</span>
                  <el-switch
                    v-model="row.status"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="1"
                    :inactive-value="getStatus(row.status)"
                    @change="handleStatusChange(row.id, row)"
                    :disabled="row.status == '-1'"
                  />
                </div>
                <div class="flex-center">
                  <span class="black-label mr5">调度状态:</span>
                  <el-switch
                    v-model="row.schedulerState"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="1"
                    inactive-value="0"
                    :disabled="row.status != '1'"
                    @change="handleschedulerState(row.id, row)"
                  />
                </div>
              </div>
            </template>
            <template #cronExpression="{ row }">
              <div class="flex-column fz14 grey-black-text">
                <div class="flex-center mb5">
                  <el-icon class="mr5"><Clock /></el-icon>
                  <span
                    class="text-ellipsis cron-text"
                    :title="cronToZh(row.cronExpression)"
                  >
                    {{ cronToZh(row.cronExpression) || "-" }}
                  </span>
                </div>
                <div class="flex-center">
                  <span class="mr5">执行策略:</span>
                  <dict-tag
                    :options="dpp_etl_task_execution_type"
                    :value="row.executionType"
                  />
                </div>
              </div>
            </template>
            <template #lastExecute="{ row }">
              <div class="flex-column fz14 last-execute-col">
                <template v-if="row.lastExecuteTime">
                  <div class="mb5">
                    <dict-tag
                      v-if="
                        row.lastExecuteStatus !== null &&
                        row.lastExecuteStatus !== undefined &&
                        row.lastExecuteStatus !== ''
                      "
                      :options="dpp_etl_task_instance"
                      :value="row.lastExecuteStatus"
                    />
                    <span v-else>-</span>
                  </div>
                  <span>
                    {{ parseTime(row.lastExecuteTime, "{y}-{m}-{d} {h}:{i}") }}
                  </span>
                </template>
                <template v-else>
                  <div class="mb5">
                    <el-tag type="infos" class="not-executed-tag"
                      >未执行</el-tag
                    >
                  </div>
                  <span>-</span>
                </template>
              </div>
            </template>
            <template #personChargeName="{ row }">
              <div class="flex-column fz14">
                <span
                  class="text-ellipsis person-charge-ellipsis"
                  :title="row.personCharge"
                  >{{ row.personChargeName || "-" }}</span
                >
                <span>{{ row.contactNumber || "-" }}</span>
              </div>
            </template>
            <template #createBy="{ row }">
              <div class="flex-column fz14">
                <span
                  class="text-ellipsis person-charge-ellipsis"
                  :title="row.personCharge"
                  >{{ row.createBy || "-" }}</span
                >
                <span>{{ row.createUserContactNumber || "-" }}</span>
              </div>
            </template>
            <template #executionType="{ row }">
              <dict-tag
                :options="dpp_etl_task_execution_type"
                :value="row.executionType"
              />
            </template>
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                :disabled="row.status == 1"
                @click="routeTo('/dpp/task/integratioTask/edit', row)"
                >配置任务</el-button
              >
              <el-button
                link
                type="primary"
                icon="view"
                @click="
                  routeTo('/dpp/task/integratioTask/detail', {
                    ...row,
                    info: true,
                  })
                "
                >详情</el-button
              >
              <el-popover placement="bottom" :width="150" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown"
                    >更多</el-button
                  >
                </template>
                <div style="width: 100px" class="butgdlist">
                  <el-button
                    link
                    style="padding-left: 14px"
                    type="primary"
                    icon="Operation"
                    @click="handleJobLog(row)"
                    :disabled="row.schedulerState == '1'"
                    >调度周期</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    >运行实例</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status != 1"
                    @click="handleExecuteOnce(row)"
                    >执行一次</el-button
                  >
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    @click="handleDelete(row)"
                    >删除</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="CopyDocument"
                    :disabled="row.status == 1"
                    @click="handleClone(row)"
                  >
                    克隆
                  </el-button>
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
    <instance
      :visible="DataView"
      :taskType="1"
      @update:visible="DataView = $event"
      @confirm="submitForm"
      :data="form"
      title="运行实例"
    />
    <el-dialog
      title="调度周期"
      v-model="openCron"
      :append-to="$refs['app-container']"
      destroy-on-close
      :appendTo="'#app'"
    >
      <crontab
        ref="crontabRef"
        @hide="openCron = false"
        @fill="crontabFill"
        :expression="expression"
      >
      </crontab>
    </el-dialog>
    <!-- 新增 -->
    <add
      :visible="taskConfigDialogVisible"
      title="新增任务"
      @update:visible="taskConfigDialogVisible = $event"
      @save="handleSave"
      @confirm="handleConfirm"
      :data="nodeData"
      :userList="userList"
      :info="route.query.info"
      :catCode="tableStore.params.catCode"
      :deptOptions="deptOptions"
    />
  </div>
</template>

<script setup name="DppIntegratioTask">
import {
  listDppEtlTask,
  delDppEtlTask,
  addDppEtlTask,
  updateDppEtlTask,
  updateReleaseSchedule,
  updateReleaseJobTask,
  releaseTaskCrontab,
  startDppEtlTask,
  createEtlTaskFront,
  copyCreateEtl,
} from "@/api/dpp/task/index.js";
import { usePageRefresh } from "@/composables/usePageRefresh";
import { cronToZh } from "@/utils/cronUtils";
import Crontab from "@/components/Crontab/index.vue";
import instance from "@/views/dpp/components/instance.vue";
const userStore = useUserStore();
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";
import {
  listAttTaskCat,
  getAttTaskCat,
  addAttTaskCat,
  updateAttTaskCat,
  delAttTaskCat,
} from "@/api/att/cat/taskCat/taskCat";
import DeptTree from "@/components/DeptTree";
import add from "./add/add.vue";
import { deptUserTree } from "@/api/system/system/user.js";
import { ref, reactive, getCurrentInstance, watch } from "vue";

const { proxy } = getCurrentInstance();

const api = {
  list: listAttTaskCat,
  get: getAttTaskCat,
  add: addAttTaskCat,
  update: updateAttTaskCat,
  del: delAttTaskCat,
};
const {
  dpp_etl_task_status,
  dpp_etl_task_execution_type,
  dpp_etl_task_instance,
} = proxy.useDict(
  "dpp_etl_task_status",
  "dpp_etl_task_execution_type",
  "dpp_etl_task_instance"
);

const route = useRoute();
const router = useRouter();

// Table and Search Store
const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
    },
  },
  columns: [
    { label: "编号", prop: "id", width: 60, sortable: true },
    {
      label: "任务信息",
      prop: "name",
      align: "left",
      slot: "name",
      width: 300,
    },
    {
      label: "运行控制",
      prop: "status",
      width: 130,
      slot: "releaseState",
      align: "left",
    },
    {
      label: "调度周期",
      prop: "cronExpression",
      width: 200,
      slot: "cronExpression",
      align: "left",
    },
    {
      label: "最近执行",
      width: 160,
      slot: "lastExecute",
      align: "left",
    },

    {
      label: "责任人",
      width: 120,
      slot: "personChargeName",
      align: "left",
    },
    {
      label: "创建人",
      slot: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: true,
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 150,
      align: "left",
    },

    {
      label: "操作",
      align: "center",
      fixed: "right",
      slot: "action",
      width: 240,
    },
  ],
  func: listWrapper,
  params: {
    catCode: null,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
  },
});

// User list for search
let userList = ref([]);
let deptOptions = ref([]);
function getDeptTree() {
  listAttTaskCat({
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    validFlag: true,
  }).then((response) => {
    deptOptions.value = [];
    var children = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "数据集成类目",
        value: "",
        id: 0,
        children: children,
      },
    ];
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
  });
}

const searchStore = reactive({
  items: [
    {
      label: "任务名称",
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: "请输入任务名称" },
    },
    {
      label: "任务状态",
      prop: "status",
      component: {
        is: "select",
        placeholder: "请选择任务状态",
        options: dpp_etl_task_status,
      },
    },
    {
      label: "责任人",
      prop: "personCharge",
      component: {
        is: "tree-select",
        data: userList,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "ID",
        placeholder: "请选择责任人",
        checkStrictly: true,
      },
    },
  ],
});

function listWrapper(params) {
  const p = { ...params };
  p.projectId = userStore.projectId;
  p.projectCode = userStore.projectCode;
  return listDppEtlTask(p);
}

function handleQuery() {
  tableRef.value.getList();
}

function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  handleQuery();
}

// 部门树
const leftWidth = ref(300);
const DeptTreeRef = ref(null);
function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}
// 任务配置
const taskConfigDialogVisible = ref(false);
let nodeData = ref({ taskConfig: {}, name: null });

const openTaskConfigDialog = () => {
  nodeData.value = { taskConfig: {}, name: null };
  taskConfigDialogVisible.value = true;
};

const handleSave = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    draftJson: JSON.stringify({ ...form }),
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      handleQuery();
    }
  });
};

const handleConfirm = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    draftJson: JSON.stringify({ ...form }),
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      handleQuery();
      routeTo("/dpp/task/integratioTask/edit", res.data);
    }
  });
};

// Actions
function handleStatusChange(id, row) {
  const text = row.status == "1" ? "上线" : "下线";
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据集成任务吗？')
    .then(function () {
      updateReleaseJobTask({
        id,
        releaseState: row.status,
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      })
        .then((response) => {
          proxy.$modal.msgSuccess("操作成功");
          handleQuery();
        })
        .catch((error) => {
          row.status = row.status === "1" ? "0" : "1";
        });
    })
    .catch((error) => {
      row.status = row.status === "1" ? "0" : "1";
    });
}

function handleschedulerState(id, row) {
  const text = row.schedulerState == "1" ? "上线" : "下线";
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据集成调度状态吗？')
    .then(function () {
      updateReleaseSchedule({
        id,
        schedulerState: row.schedulerState,
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      })
        .then((response) => {
          proxy.$modal.msgSuccess("操作成功");
          handleQuery();
        })
        .catch((error) => {
          row.schedulerState = row.schedulerState == "1" ? "0" : "1";
        });
    })
    .catch((error) => {
      row.schedulerState = row.schedulerState == "1" ? "0" : "1";
    });
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

// Cron and Instance
const openCron = ref(false);
const expression = ref("");
const row = ref({});

function handleJobLog(data) {
  row.value = data || {};
  expression.value = data.cronExpression || "";
  openCron.value = true;
}

function crontabFill(value) {
  row.value.crontab = value;
  releaseTaskCrontab({
    crontab: row.value.crontab,
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    id: row.value.id,
  }).then((response) => {
    proxy.$modal.msgSuccess("操作成功");
    handleQuery();
  });
}

const DataView = ref(false);
const form = ref({});
function handleDataView(row) {
  form.value = row;
  DataView.value = true;
}

function submitForm() {
  handleQuery();
}

const handleExecuteOnce = async (row) => {
  if (!row?.id) {
    proxy.$modal.msgWarning("无效的任务id，请刷新后重试");
    return;
  }
  try {
    const res = await startDppEtlTask(row.id);
    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess("执行成功");
    } else {
      proxy.$modal.msgWarning(res?.msg || "执行失败，请联系管理员");
    }
  } catch (e) {
    //
  }
};

const handleClone = (row) => {
  proxy.$modal
    .confirm(`确定要克隆任务【${row.name}】吗？`)
    .then(() => {
      return copyCreateEtl({
        id: Number(row.id),
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      });
    })
    .then(() => {
      handleQuery();
    })
    .catch(() => {});
};

function handleDelete(row) {
  const ids = row.id;
  proxy.$modal
    .confirm('是否确认删除数据集成任务编号为"' + ids + '"的数据项？')
    .then(function () {
      return delDppEtlTask(ids);
    })
    .then(() => {
      handleQuery();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

// Utils
const getDatasourceIcon = (json) => {
  let type = json && JSON.parse(json).taskType;
  switch (type) {
    case "FLINK":
      return new URL("@/assets/images/common/flink.svg", import.meta.url).href;
    case "SPARK":
      return new URL("@/assets/images/common/spark.svg", import.meta.url).href;
    default:
      return null;
  }
};

const getStatus = (status) => {
  if (status == "-1") {
    return "-1";
  } else {
    return "0";
  }
};

// Initialization
watch(
  () => userStore.projectId,
  () => {
    handleQuery();
    getDeptTree();
  }
);

if (userStore.projectId) {
  getDeptTree();
}
usePageRefresh("integratioTask", () => handleQuery());
</script>

<style scoped lang="scss">
.task-title-row {
  width: 100%;
  min-width: 0;
  gap: 6px;
  justify-content: flex-start !important;
  flex-wrap: nowrap;
}

.task-name-ellipsis {
  flex: 0 0 auto;
  min-width: 0;
  max-width: calc(100% - 90px);
}

:deep(.task-name-ellipsis .el-link__inner) {
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.task-cat-ellipsis {
  flex: 0 1 auto;
  min-width: 0;
  max-width: none;
  padding: 0 4px;
  height: 20px;
  line-height: 18px;
}

:deep(.name-label .task-cat-ellipsis .el-tag__content) {
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 12px !important;
}

.not-executed-tag {
  background-color: var(--el-fill-color-lighter);
  border-color: var(--el-border-color-lighter);
}
</style>


