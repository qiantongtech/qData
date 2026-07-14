<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="app-container" ref="app-container">
    <guide-tip tip-id="mc/task/structured" />
    <el-container>
      <!-- <SourceSystemTree
        ref="sourceSystemTreeRef"
        @node-click="handleNodeClick"
        @data-loaded="handleTreeDataLoaded"
      /> -->
      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQueryClick"
              @reset="handleResetQueryClick"
            />
          </template>
          <template #actions-data>
            <el-button type="primary" plain @click="handleAddClick">
              <i class="iconfont-mini icon-xinzeng mr5"></i
              >{{ td("common.button.add") }}
            </el-button>
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDeleteColumnClick"
            >
              {{ td("common.button.delete") }}
            </el-button>
          </template>
          <qt-table v-bind="tableStore" ref="tableRef">
            <template #task-status="scope">
              <el-switch
                v-if="scope.row.status != undefined"
                v-model="scope.row.status"
                active-value="1"
                inactive-value="0"
                @change="handleTaskStatusChange(scope.row, $event)"
              />
            </template>

            <template #scheduler-status="scope">
              <el-switch
                v-if="scope.row.schedulerStatus != undefined"
                v-model="scope.row.schedulerStatus"
                active-value="1"
                inactive-value="0"
                @change="handleSchedulerStatusChange(scope.row, $event)"
              />
            </template>

            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                :disabled="row.status == '1'"
                @click="handleEditClick(row)"
              >
                {{ td("common.button.update") }}
              </el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetailClick(row)"
              >
                {{ td("common.button.details") }}
              </el-button>
              <el-popover
                placement="bottom"
                :width="150"
                trigger="click"
                popper-class="handle-popover"
              >
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">
                    {{ td("common.button.more") }}
                  </el-button>
                </template>
                <div style="width: 100px">
                  <el-button
                    link
                    type="primary"
                    icon="Document"
                    @click="handleInstanceClick(row)"
                    style="padding-left: 14px"
                  >
                    {{ td("mc.task.structured.collectInstance") }}
                  </el-button>
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status == '0'"
                    @click="handleRunClick(row)"
                  >
                    {{ td("mc.task.structured.executeOnce") }}
                  </el-button>
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == '1'"
                    @click="handleDeleteClick(row)"
                  >
                    {{ td("common.button.delete") }}
                  </el-button>
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- 调度周期弹窗 -->
    <el-dialog
      :title="td('mc.task.structured.cronGenerator')"
      v-model="cronDialog.open"
      :append-to="$refs['app-container']"
      destroy-on-close
    >
      <Crontab
        @hide="handleCloseCronClick"
        @fill="handleConfirmCronClick"
        :expression="cronDialog.data"
      />
    </el-dialog>

    <!-- 新增/修改弹窗 -->
    <el-dialog
      v-model="dialog.open"
      :title="dialog.title"
      width="1200"
      :loading="dialog.loading"
      @close="handleCancelClick"
    >
      <el-form
        :model="dialog.form"
        class="column-form"
        :rules="rules"
        ref="formRef"
        label-width="110px"
       :label-position="labelPosition">
        <el-form-item
          :label="td('mc.task.structured.sourceSystem')"
          prop="sourceSystemId"
         :label-position="labelPosition">
          <el-tree-select
            filterable
            v-model="dialog.form.sourceSystemId"
            :data="store.sourceSystems"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            :placeholder="td('mc.task.structured.sourceSystemPlaceholder')"
            check-strictly
            @change="handleDomainChange"
            default-expand-all
          />
        </el-form-item>

        <el-form-item :label="td('mc.task.structured.taskName')" prop="name" :label-position="labelPosition">
          <el-input
            v-model="dialog.form.name"
            :placeholder="td('mc.task.structured.taskNamePlaceholder')"
          />
        </el-form-item>

        <el-form-item
          :label="td('mc.task.structured.datasourceName')"
          prop="datasourceId"
         :label-position="labelPosition">
          <el-select
            v-model="dialog.form.datasourceId"
            :placeholder="td('mc.task.structured.datasourceNamePlaceholder')"
            @change="handleDatasourceChange"
          >
            <el-option
              v-for="item in store.datasources"
              :key="item.id"
              :label="item.datasourceName"
              :value="item.id"
              :disabled="
                !['DM8', 'MySql', 'Oracle11', 'Oracle', 'PostgreSQL', 'Hive'].includes(
                  item.datasourceType
                )
              "
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="td('mc.task.structured.dbType')" prop="dbType" :label-position="labelPosition">
          <el-input
            v-model="dialog.form.dbType"
            disabled
            :placeholder="td('mc.task.structured.dbTypePlaceholder')"
          />
        </el-form-item>

        <el-form-item :label="td('mc.task.structured.ip')" prop="ip" :label-position="labelPosition">
          <el-input
            v-model="dialog.form.ip"
            disabled
            :placeholder="td('mc.task.structured.ipPlaceholder')"
          />
        </el-form-item>

        <el-form-item :label="td('mc.task.structured.port')" prop="port" :label-position="labelPosition">
          <el-input
            v-model="dialog.form.port"
            disabled
            :placeholder="td('mc.task.structured.portPlaceholder')"
          />
        </el-form-item>

        <el-form-item
          :label="td('mc.task.structured.username')"
          prop="username"
         :label-position="labelPosition">
          <el-input
            v-model="dialog.form.username"
            disabled
            :placeholder="td('mc.task.structured.usernamePlaceholder')"
          />
        </el-form-item>

        <qt-form-item
          :label="td('mc.task.structured.cronExpression')"
          prop="cronExpression"
          :rules="[
            {
              required: true,
              message: td('mc.task.structured.cronRequired'),
              trigger: 'blur',
            },
          ]"
          :tip="{
            content: td('mc.task.structured.cronTip'),
          }"
        >
          <el-input
            v-model="dialog.form.cronExpression"
            :placeholder="td('mc.task.structured.cronExpressionPlaceholder')"
          >
            <template #append>
              <el-button
                type="primary"
                @click="handleOpenCronClick"
                style="background-color: #2666fb; color: #fff"
              >
                {{ td("mc.task.structured.cronConfig") }}
                <i class="el-icon-time el-icon--right"></i>
              </el-button>
            </template>
          </el-input>
        </qt-form-item>

        <el-form-item :label="td('mc.task.structured.leader')" prop="leader" :label-position="labelPosition">
          <el-tree-select
            filterable
            v-model="dialog.form.leader"
            :data="store.userList"
            :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }"
            value-key="userId"
            :placeholder="td('mc.task.structured.leaderPlaceholder')"
            check-strictly
            @change="handleUserChange"
          />
        </el-form-item>

        <el-form-item
          :label="td('mc.task.structured.leaderPhone')"
          prop="leaderPhone"
         :label-position="labelPosition">
          <el-input
            v-model="dialog.form.leaderPhone"
            disabled
            :placeholder="td('mc.task.structured.leaderPhonePlaceholder')"
          />
        </el-form-item>

        <el-form-item
          :label="td('mc.task.structured.collectionMode')"
          class="row-full"
          prop="collectionMode"
          v-if="false"
         :label-position="labelPosition">
          <el-radio-group v-model="dialog.form.collectionMode">
            <el-radio
              v-for="dict in toValue(dicts.mc_collect_mode)"
              :key="dict.value"
              :label="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
            :label="td('mc.task.structured.scheduler')"
            prop="scheduler"
            class="row-full"
            :label-position="labelPosition">
          <el-radio-group v-model="dialog.form.scheduler">
            <el-radio
                v-for="item in toValue(dicts.scheduler_type)"
                :key="item.value"
                :label="item.value"
            >
              {{ item.label }}
            </el-radio>
          </el-radio-group>
          <p style="
  flex-basis: 100%;align-items: center;line-height: 1;font-size: 12px;color: #888; margin-top: 10px;">
            {{ schedulerGuide.description }}
          </p>
        </el-form-item>

        <el-form-item
          :label="td('mc.task.structured.collectionScope')"
          class="row-full"
          prop="collectionScope"
         :label-position="labelPosition">
          <div class="collection-wrap">
            <el-radio-group v-model="dialog.form.collectionScope">
              <el-radio
                v-for="dict in toValue(dicts.mc_collect_scope)"
                :key="dict.value"
                :label="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>

            <el-form-item
              prop="tables"
              v-if="dialog.form.collectionScope == 1"
              label-width="0"
              style="margin-bottom: 0"
             :label-position="labelPosition">
              <el-transfer
                v-model="dialog.form.tables"
                :data="dialog.tableList"
                :props="{ label: 'label', key: 'dbName' }"
                filterable
                :filter-method="onFilterTransfer"
                :filter-placeholder="td('mc.task.structured.inputDbName')"
                :titles="[
                  td('mc.task.structured.selectSourceDb'),
                  td('mc.task.structured.selectSelectedDb'),
                ]"
                style="--el-transfer-panel-width: 320px"
              />
            </el-form-item>
          </div>
        </el-form-item>

        <el-form-item
          :label="td('common.texts.description')"
          class="row-full"
          prop="description"
         :label-position="labelPosition">
          <el-input
            v-model="dialog.form.description"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>

        <el-form-item
          :label="td('common.texts.remark')"
          class="row-full"
          prop="remark"
         :label-position="labelPosition">
          <el-input
            v-model="dialog.form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancelClick">{{
            td("common.button.cancel")
          }}</el-button>
          <el-button type="primary" @click="handleConfirmClick">
            {{ td("common.button.confirm") }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="McTaskStructured">
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import {
  reactive,
  ref,
  getCurrentInstance,
  toValue,
  computed,
  watch,
} from "vue";
import Crontab from "@/components/Crontab/index.vue";
import SourceSystemTree from "./components/SourceSystemTree.vue";
import { getParentLabelPath } from "@/utils/anivia.js";
import {
  listTask,
  addTask,
  getTask,
  updateTask,
  delTask,
  getRealtimeMcTaskScopeList,
  updateReleaseJobTask,
  updateReleaseSchedule,
  runJobOnce,
  sourceSystemTree,
  batchDeleteCheck,
} from "@/api/mc/task/task";
import { listDaDatasource } from "@/api/mc/dataSource/dataSource";
import { deptUserTree } from "@/api/system/system/user.js";
import { listValidSourceSystem } from "@/api/att/sourceSystem/sourceSystem";
import useDefaultLang from "@/composables/useDefaultLang";
import { checkApi } from "@/api/ds/api/api.js";
import { ElMessage } from "element-plus";

const { td } = useDefaultLang();
const rules = {
  sourceSystemId: [
    {
      required: true,
      message: td("mc.task.structured.sourceSystemRequired"),
      trigger: "change",
    },
  ],
  name: [
    {
      required: true,
      message: td("mc.task.structured.taskNameRequired"),
      trigger: "blur",
    },
    {
      min: 2,
      max: 20,
      message: td("mc.task.structured.taskNameLength"),
      trigger: "blur",
    },
  ],
  datasourceId: [
    {
      required: true,
      message: td("mc.task.structured.datasourceRequired"),
      trigger: "change",
    },
  ],
  dbType: [
    {
      required: true,
      message: td("mc.task.structured.dbTypeRequired"),
      trigger: ["blur", "change"],
    },
  ],
  ip: [
    {
      required: true,
      message: td("mc.task.structured.ipRequired"),
      trigger: ["blur", "change"],
    },
  ],
  port: [
    {
      required: true,
      message: td("mc.task.structured.portRequired"),
      trigger: ["blur", "change"],
    },
  ],
  username: [
    {
      required: true,
      message: td("mc.task.structured.usernameRequired"),
      trigger: ["blur", "change"],
    },
  ],
  cronExpression: [
    {
      required: true,
      message: td("mc.task.structured.cronRequired"),
      trigger: "change",
    },
  ],
  scheduler: [
    {
      required: true,
      message: td("mc.task.structured.schedulerRequired"),
      trigger: "change",
    },
  ],
  collectionMode: [
    {
      required: true,
      message: td("mc.task.structured.collectionModeRequired"),
      trigger: "change",
    },
  ],
  collectionScope: [
    {
      required: true,
      message: td("mc.task.structured.collectionScopeRequired"),
      trigger: "change",
    },
  ],
  tables: [
    {
      required: true,
      validator: (rule, value, callback) => {
        if (
          dialog.form.collectionScope == "1" &&
          (!value || value.length === 0)
        ) {
          callback(new Error(td("mc.task.structured.selectedDbRequired")));
        } else {
          callback();
        }
      },
      trigger: "change",
    },
  ],
};

const DETAIL_PATH = "/dg/meta/task/detail";

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "mc_collect_scope",
  "mc_collect_mode",
  "scheduler_type"
);

const router = useRouter();

const formRef = ref();
const sourceSystemTreeRef = ref();
const store = reactive({
  loading: false,
  rows: [],
  domains: [],
  treeDomains: [],
  sourceSystems: [],
  flatSourceSystems: [],
  datasources: [],
  userList: [],
});

function getAllSourceSystems() {
  listValidSourceSystem().then((res) => {
    store.sourceSystems = res.data;
    // 扁平化数据用于查找
    const flatten = (list) => {
      if (!Array.isArray(list)) return [];
      let result = [];
      list.forEach((item) => {
        result.push(item);
        if (item.children && item.children.length > 0) {
          result = result.concat(flatten(item.children));
        }
      });
      return result;
    };
    store.flatSourceSystems = flatten(res.data);
  });
}

function handleTreeDataLoaded({ treeData, flatData }) {
  store.treeDomains = treeData;
  store.domains = flatData;
}

// 节点单击事件
function handleNodeClick(data) {
  // 清除之前的筛选
  tableStore.params.sourceSystemId = undefined;
  tableStore.params.datasourceId = undefined;
  tableStore.params.id = undefined;
  tableStore.params.type = undefined;

  if (data.type === "SOURCE") {
    tableStore.params.sourceSystemId = data.id;
  } else if (data.type === "DATASOURCE") {
    tableStore.params.datasourceId = data.id;
    tableStore.params.type = data.type;
  } else if (data.type === "DATABASE") {
    tableStore.params.id = data.taskId;
  }
  tableRef.value.getList();
}

// 列表
const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetailClick,
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
    },
    {
      label: td("common.texts.number"),
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: td("mc.task.structured.taskName"),
      prop: "name",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      link: {
        external: handleDetailClick,
      },
    },
    {
      label: td("common.texts.description"),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.sourceSystem"),
      prop: "sourceSystemName",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.dbType"),
      prop: "dbType",
      dict: "datasource_type",
      width: 120,
    },
    {
      label: td("mc.task.structured.datasourceName"),
      prop: "datasourceName",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.scheduler"),
      prop: "scheduler",
      width: 150,
      formatter: (row) => getSchedulerLabel(row.scheduler),
    },
    // {
    //   label: td("mc.task.structured.collectionMode"),
    //   prop: "collectionMode",
    //   width: 100,
    //   dict: "mc_collect_mode",
    // },

    {
      label: td("mc.task.structured.taskStatus"),
      prop: "status",
      width: 100,
      slot: "task-status",
    },
    {
      label: td("mc.task.structured.schedulerStatus"),
      prop: "schedulerStatus",
      width: 120,
      slot: "scheduler-status",
    },
    {
      label: td("mc.task.structured.personCharge"),
      prop: "personChargeName",
      width: 140,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.lastExecuteTime"),
      prop: "lastExecuteTime",
      sortable: true,
      width: 180,
      date: true,
    },
    {
      label: td("common.texts.createdBy"),
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("common.texts.createdTime"),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      width: 160,
      date: true,
    },
    {
      label: td("common.texts.operation"),
      width: 260,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listTask,
  params: {},
  events: {
    formatParams(params) {
      if (!params.time || !params.time.length) return params;
      const { time, ...other } = { ...params };
      other.createTimeStart = time[0];
      other.createTimeEnd = time[1];
      return other;
    },
  },
});

// 搜索项
const searchStore = reactive({
  items: [
    {
      label: td("mc.task.structured.taskName"),
      prop: "name",
      component: {
        is: "input",
      },
    },

    {
      label: td("common.texts.createdTime"),
      prop: "time",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: computed(() =>
          td("common.form.startDatePlaceholder")
        ),
        endPlaceholder: computed(() => td("common.form.endDatePlaceholder")),
      },
    },
  ],
});

// 新增/修改弹窗
const DEFAULT_FORM = {
  collectionMode: "1",
  collectionScope: "2",
  // 老逻辑默认走 DS，新建任务不选时也保持这个默认值。
  scheduler: "QUARTZ",
  tables: [],
};
const dialog = reactive({
  open: false,
  title: "",
  loading: false,
  tableList: [],
  form: {
    ...DEFAULT_FORM,
  },
});

const schedulerGuide = computed(() => {
  if (dialog.form.scheduler === "DOLPHINSCHEDULER") {
    return {
      description: td(
          "dpp.integratioTask.dolphinSchedulerGuideDescription",
          "使用前请确保 DolphinScheduler 服务已启动。"
      ),
    };
  }

  return {
    description: td(
        "dpp.integratioTask.quartzGuideDescription",
        "由系统内置组件执行任务。"
    ),
  };
});

// 调度周期弹窗
const cronDialog = reactive({
  open: false,
  data: "",
});

// 获取来源系统路径
const getDomainPath = computed(() => {
  return function (id) {
    let domainName = getParentLabelPath(store.sourceSystems, id, {
      idKey: "id",
      labelKey: "name",
      childrenKey: "children",
    });
    const idx = domainName.indexOf("/");
    return idx == -1 ? domainName : domainName.slice(idx + 1);
  };
});

// 获取数据源列表
function getDatasources() {
  listDaDatasource().then((res) => {
    res.data.rows.forEach((item) => {
      item.datasourceConfig = item.datasourceConfig
        ? JSON.parse(item.datasourceConfig)
        : {};
    });
    store.datasources = res.data.rows;
  });
}

// 搜索按钮操作
function handleQueryClick() {
  tableRef.value?.getList();
}

// 重置按钮操作
function handleResetQueryClick() {
  if (sourceSystemTreeRef.value?.resetTree) {
    sourceSystemTreeRef.value.resetTree();
  }
  tableStore.params.sourceSystemId = null;
  tableStore.params.datasourceId = null;
  tableStore.params.id = null;
  tableRef.value?.resetQuery();
}

// 获取用户列表
function getUserList() {
  deptUserTree().then((res) => {
    store.userList = res.data;
  });
}

// 切换用户
function handleUserChange(id) {
  const data = store.userList.find((item) => item.userId === id);
  dialog.form.leaderPhone = data.phonenumber;
}

// 切换数据源
function handleDatasourceChange(id, falg = true) {
  const data = store.datasources.find((item) => item.id === id);
  dialog.form.ip = data.ip;
  dialog.form.port = data.port;
  dialog.form.username = data.datasourceConfig?.username;
  dialog.form.dbType = data.datasourceType;
  if (falg) {
    dialog.form.tables = [];
  }
  getRealtimeMcTaskScopeList(id).then((res) => {
    dialog.tableList = res.data.map((item) => ({
      ...item,
      label: item.schemaName
        ? `${item.dbName}.${item.schemaName}`
        : item.dbName,
    }));
  });
}

// 切换来源系统
function handleDomainChange(id) {
  const data = store.flatSourceSystems.find((item) => item.id === id);
  if (data) {
    dialog.form.sourceSystemId = data.id;
    dialog.form.sourceSystemName = data.name;
  }
}
function handleRunClick(val) {
  runJobOnce({ id: val.id }).then((res) => {
    if (res.code == 200) {
      ElMessage.success(td("mc.task.structured.executeSuccess"));
    } else {
    }
  });
}
// 打开调度周期弹窗
function handleOpenCronClick() {
  cronDialog.data = dialog.form.cronExpression;
  cronDialog.open = true;
}

// 关闭调度周期弹窗
function handleCloseCronClick() {
  cronDialog.open = false;
  cronDialog.data = "";
}

// 确认调度周期弹窗
function handleConfirmCronClick(data) {
  dialog.form.cronExpression = data;
  cronDialog.open = false;
}

// 点击详情
function handleDetailClick(row) {
  router.push({
    path: DETAIL_PATH,
    query: {
      id: row.id,
    },
  });
}

// 点击新增
function handleAddClick() {
  dialog.title = td("mc.task.structured.addTask");
  // 打开新增弹窗时兜底一次，避免上次编辑留下空值。
  dialog.form.scheduler = dialog.form.scheduler || "DOLPHINSCHEDULER";
  dialog.open = true;
  dialog.func = addTask;
}

// 取消新增/修改
function handleCancelClick() {
  formRef.value.resetFields();
  dialog.form = {
    ...DEFAULT_FORM,
  };
  dialog.tableList = [];
  dialog.loading = false;
  dialog.open = false;
}

// 确认新增/修改
async function handleConfirmClick() {
  if(!await handleSchedulerChange()){
    return;
  }
  const valid = await formRef.value.validate();
  if (!valid) return;
  dialog.loading = true;
  const { tables, ...params } = dialog.form;
  if (params.collectionScope == "1") {
    params.scopeSaveReqVOS = dialog.tableList.filter((item) =>
      tables.includes(item.dbName)
    );
  }
  try {
    await dialog.func(params);
    handleCancelClick();
    tableRef.value.getList();
  } catch (err) {
    console.error(err);
  } finally {
    dialog.loading = false;
  }
}

// 打开修改弹窗
function handleEditClick(row) {
  dialog.open = true;
  dialog.func = updateTask;
  dialog.title = td("mc.task.structured.editTask");
  getTask(row.id).then((res) => {
    if (res.data.scopeSaveReqVOS) {
      res.data.tables = res.data.scopeSaveReqVOS.map((item) => item.dbName);
    }
    // 确保回显时包含来源系统名称
    if (res.data.sourceSystemId && !res.data.sourceSystemName) {
      const system = store.flatSourceSystems.find(
        (item) => item.id === res.data.sourceSystemId
      );
      if (system) {
        res.data.sourceSystemName = system.name;
      }
    }
    dialog.form = {
      ...res.data,
      // 老任务没有 scheduler 字段时，页面按 DS 展示。
      scheduler: res.data.scheduler || "DOLPHINSCHEDULER",
    };
    handleDatasourceChange(res.data.datasourceId, false);
  });
}

function getSchedulerLabel(value) {
  // 列表里把库里的枚举值翻译成人能看懂的名字。
  return (
    toValue(dicts.scheduler_type).find((item) => item.value == value)?.label ||
    value ||
    "-"
  );
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(
    td("mc.task.structured.confirmDelete", '', { id: row.id }),
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return delTask(row.id);
    })
    .then(() => {
      ElMessage.success(td("common.message.deleteSuccess"));
      tableRef.value.getList();
    });
}

// 采集实例
function handleInstanceClick(row) {
  router.push({
    path: DETAIL_PATH,
    query: {
      id: row.id,
      tab: "CollectInstance",
    },
  });
}

// 删除选中行
function handleDeleteColumnClick() {
  if (!store.rows.length) return;
  const ids = store.rows.map((item) => item.id);
  store.loading = true;
  batchDeleteCheck(ids).then((res) => {
    const { canDeleteCount, cannotDeleteCount, canDeleteIds } = res.data;
    store.loading = false;
    ElMessageBox.confirm(
      td("mc.task.structured.confirmDeleteSelected", {
        count: canDeleteCount,
        notDeleteCount: cannotDeleteCount,
      }),
      td("common.message.systemPrompt"),
      {
        confirmButtonText: td("common.button.confirm"),
        cancelButtonText: td("common.button.cancel"),
        type: "warning",
      }
    )
      .then(() => {
        if (!canDeleteIds.length) {
          ElMessage.success(td("common.message.deleteSuccess"));
          return;
        }
        return delTask(canDeleteIds);
      })
      .then((res) => {
        if (!res) return;
        ElMessage.success(td("common.message.deleteSuccess"));
        tableRef.value.getList();
      });
  });
}

// 筛选表
function onFilterTransfer(value, item) {
  if (!value) return item;
  const txt = (item.label || item.dbName || "").toLowerCase();
  return txt.includes(value.toLowerCase());
}

// 切换任务状态
function handleTaskStatusChange(row, status) {
  const action =
    status == 1
      ? td("mc.task.structured.publish")
      : td("mc.task.structured.cancelPublish");
  ElMessageBox.confirm(
    td("mc.task.structured.confirmPublish", '',{
      action,
      name: row.name,
    }),
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return updateReleaseJobTask({
        id: row.id,
        status,
      });
    })
    .then(() => {
      ElMessage.success(
        td("mc.task.structured.publishSuccess",'', {
          name: row.name,
          action,
        })
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}

// 切换调度状态
function handleSchedulerStatusChange(row, status) {
  const action =
    status == 1
      ? td("mc.task.structured.online")
      : td("mc.task.structured.offline");
  ElMessageBox.confirm(
    td("mc.task.structured.confirmScheduler", '',{
      action,
      name: row.name,
    }),
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return updateReleaseSchedule({
        id: row.id,
        status,
      });
    })
    .then(() => {
      ElMessage.success(
        td("mc.task.structured.schedulerSuccess",'', {
          name: row.name,
          action,
        })
      );
      row.schedulerStatus = status;
    })
    .catch(() => {
      row.schedulerStatus = status == "1" ? "0" : "1";
    });
}

/**
 * DolphinScheduler调度器状态检查
 * @returns {Promise<void>}
 */
const handleSchedulerChange = async () => {
  if (dialog.form.scheduler !== "QUARTZ") {
    const resp = await checkApi();
    if (!resp.data) {
      proxy.$modal.msgWarning(td("dpp.integratioTask.upDs", "请启动DolphinScheduler调度器！"));
    }
    return resp.data;
  }
  return true;
};

getDatasources();
getUserList();
getAllSourceSystems();
</script>

<style lang="scss" scoped>
</style>
