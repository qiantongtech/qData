
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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
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
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDeleteColumnClick"
            >
              删除
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
                修改
              </el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetailClick(row)"
              >
                详情
              </el-button>
              <el-popover
                placement="bottom"
                :width="150"
                trigger="click"
                popper-class="handle-popover"
              >
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">
                    更多
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
                    采集实例
                  </el-button>
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status == '0'"
                    @click="handleRunClick(row)"
                  >
                    执行一次
                  </el-button>
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == '1'"
                    @click="handleDeleteClick(row)"
                  >
                    删除
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
      title="Cron表达式生成器"
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
      >
        <el-form-item label="来源系统" prop="sourceSystemId">
          <el-tree-select
            filterable
            v-model="dialog.form.sourceSystemId"
            :data="store.sourceSystems"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择来源系统"
            check-strictly
            @change="handleDomainChange"
            default-expand-all
          />
        </el-form-item>

        <el-form-item label="任务名称" prop="name">
          <el-input v-model="dialog.form.name" placeholder="请输入任务名称" />
        </el-form-item>

        <el-form-item label="数据连接名称" prop="datasourceId">
          <el-select
            v-model="dialog.form.datasourceId"
            placeholder="请选择数据连接名称"
            @change="handleDatasourceChange"
          >
            <el-option
              v-for="item in store.datasources"
              :key="item.id"
              :label="item.datasourceName"
              :value="item.id"
              :disabled="
                !['MySql', 'Oracle11', 'Oracle', 'PostgreSQL', 'Hive'].includes(
                  item.datasourceType
                )
              "
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="数据库类型" prop="dbType">
          <el-input
            v-model="dialog.form.dbType"
            disabled
            placeholder="请输入数据库类型"
          />
        </el-form-item>

        <el-form-item label="IP" prop="ip">
          <el-input v-model="dialog.form.ip" disabled placeholder="请输入ip" />
        </el-form-item>

        <el-form-item label="端口号" prop="port">
          <el-input
            v-model="dialog.form.port"
            disabled
            placeholder="请输入端口号"
          />
        </el-form-item>

        <el-form-item label="账号" prop="username">
          <el-input
            v-model="dialog.form.username"
            disabled
            placeholder="请输入账号"
          />
        </el-form-item>

        <qt-form-item
          label="调度周期"
          prop="cronExpression"
          :rules="[
            {
              required: true,
              message: '请配置调度周期',
              trigger: 'blur',
            },
          ]"
          :tip="{
            content: '支持Cron表达式，如 0 0 * * * 表示每天0点执行',
          }"
        >
          <el-input
            v-model="dialog.form.cronExpression"
            placeholder="请配置调度周期"
          >
            <template #append>
              <el-button
                type="primary"
                @click="handleOpenCronClick"
                style="background-color: #2666fb; color: #fff"
              >
                配置
                <i class="el-icon-time el-icon--right"></i>
              </el-button>
            </template>
          </el-input>
        </qt-form-item>

        <el-form-item label="责任人" prop="leader">
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
            placeholder="请选择责任人"
            check-strictly
            @change="handleUserChange"
          />
        </el-form-item>

        <el-form-item label="责任人电话" prop="leaderPhone">
          <el-input
            v-model="dialog.form.leaderPhone"
            disabled
            placeholder="请输入责任人电话"
          />
        </el-form-item>

        <el-form-item
          label="采集模式"
          class="row-full"
          prop="collectionMode"
          v-if="false"
        >
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

        <el-form-item label="采集范围" class="row-full" prop="collectionScope">
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
            >
              <el-transfer
                v-model="dialog.form.tables"
                :data="dialog.tableList"
                :props="{ label: 'label', key: 'dbName' }"
                filterable
                :filter-method="onFilterTransfer"
                filter-placeholder="请输入物理库名称"
                :titles="['来源库', '已选来源库']"
                style="--el-transfer-panel-width: 320px"
              />
            </el-form-item>
          </div>
        </el-form-item>

        <el-form-item label="描述" class="row-full" prop="description">
          <el-input
            v-model="dialog.form.description"
            type="textarea"
            placeholder="请输入描述"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>

        <el-form-item label="备注" class="row-full" prop="remark">
          <el-input
            v-model="dialog.form.remark"
            type="textarea"
            placeholder="请输入备注"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancelClick">取消</el-button>
          <el-button type="primary" @click="handleConfirmClick">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="McTaskStructured">
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

const { td } = useDefaultLang();
// 表单验证规则
const rules = {
  sourceSystemId: [
    { required: true, message: "请选择来源系统", trigger: "change" },
  ],
  name: [
    { required: true, message: "请输入任务名称", trigger: "blur" },
    { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur" },
  ],
  datasourceId: [
    { required: true, message: "请选择数据连接名称", trigger: "change" },
  ],
  dbType: [
    {
      required: true,
      message: "请输入数据库类型",
      trigger: ["blur", "change"],
    },
  ],
  ip: [
    { required: true, message: "请输入数据库ip", trigger: ["blur", "change"] },
  ],
  port: [
    {
      required: true,
      message: "请输入数据库端口",
      trigger: ["blur", "change"],
    },
  ],
  username: [
    {
      required: true,
      message: "请输入数据库用户名",
      trigger: ["blur", "change"],
    },
  ],
  cronExpression: [
    { required: true, message: "请配置调度周期", trigger: "change" },
  ],
  collectionMode: [
    { required: true, message: "请选择采集模式", trigger: "change" },
  ],
  collectionScope: [
    { required: true, message: "请选择采集范围", trigger: "change" },
  ],
  tables: [
    {
      required: true,
      validator: (rule, value, callback) => {
        if (
          dialog.form.collectionScope == "1" &&
          (!value || value.length === 0)
        ) {
          callback(new Error("请选择已选来源库"));
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
  "mc_collect_mode"
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
      label: "编号",
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: "任务名称",
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
      label: "描述",
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "来源系统",
      prop: "sourceSystemName",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "数据库类型",
      prop: "dbType",
      dict: "datasource_type",
      width: 100,
    },
    {
      label: "数据源名称",
      prop: "datasourceName",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    // {
    //   label: "采集方式",
    //   prop: "collectionMode",
    //   width: 100,
    //   dict: "mc_collect_mode",
    // },

    {
      label: "任务状态",
      prop: "status",
      width: 100,
      slot: "task-status",
    },
    {
      label: "调度状态",
      prop: "schedulerStatus",
      width: 100,
      slot: "scheduler-status",
    },
    {
      label: "责任人",
      prop: "personChargeName",
      width: 120,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "最近运行时间",
      prop: "lastExecuteTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: "创建人",
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      width: 160,
      date: true,
    },
    {
      label: "操作",
      width: 220,
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
      label: "任务名称",
      prop: "name",
      component: {
        is: "input",
      },
    },

    {
      label: "创建时间",
      prop: "time",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: computed(() => td("common.form.startDatePlaceholder")),
        endPlaceholder: computed(() => td("common.form.endDatePlaceholder")),
      },
    },
  ],
});

// 新增/修改弹窗
const DEFAULT_FORM = {
  collectionMode: "1",
  collectionScope: "2",
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
      ElMessage.success("执行成功");
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
  dialog.title = "新增采集任务";
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
  dialog.title = "修改任务";
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
    dialog.form = res.data;
    handleDatasourceChange(res.data.datasourceId, false);
  });
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(`是否确认删除编号为${row.id}的数据项？`, "系统提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      return delTask(row.id);
    })
    .then(() => {
      ElMessage.success("删除成功");
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
      `可删除${canDeleteCount}个，不可删除${cannotDeleteCount}个，是否删除可删部分`,
      "系统提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    )
      .then(() => {
        if (!canDeleteIds.length) {
          ElMessage.success("删除成功");
          return;
        }
        return delTask(canDeleteIds);
      })
      .then((res) => {
        if (!res) return;
        ElMessage.success("删除成功");
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
  ElMessageBox.confirm(
    `是否确认${status == 1 ? "发布" : "取消发布"}${row.name}任务？`,
    "系统提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
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
        `${row.name}${status == 1 ? "发布" : "取消发布"}任务成功!`
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}

// 切换调度状态
function handleSchedulerStatusChange(row, status) {
  ElMessageBox.confirm(
    `是否确认${status == 0 ? "下线" : "上线"}${row.name}的调度任务？`,
    "系统提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
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
        `${row.name}${status == 1 ? "上线" : "下线"}调度任务成功!`
      );
      row.schedulerStatus = status;
    })
    .catch(() => {
      row.schedulerStatus = status == "1" ? "0" : "1";
    });
}

getDatasources();
getUserList();
getAllSourceSystems();
</script>

<style lang="scss" scoped>
</style>
