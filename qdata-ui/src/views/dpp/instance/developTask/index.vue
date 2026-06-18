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
    <el-container style="90%">
      <DeptTree
        ref="DeptTreeRef"
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="
          td(
            'dpp.instance.developTask.inputCategoryName',
            '请输入数据开发类目名称'
          )
        "
        @node-click="handleNodeClick"
      />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            
            v-show="showSearch"
            @submit.prevent
          >
            <el-form-item
              :label="
                td('dpp.instance.developTask.nodeInstanceName', '节点实例名称')
              "
              prop="name"
            >
              <el-input
                class="el-form-input-width"
                v-model="queryParams.name"
                :placeholder="
                  td(
                    'dpp.instance.developTask.inputNodeInstanceName',
                    '请输入节点实例名称'
                  )
                "
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item
              :label="td('dpp.instance.developTask.taskName', '任务名称')"
              prop="taskInstanceName"
            >
              <el-input
                class="el-form-input-width"
                v-model="queryParams.taskInstanceName"
                :placeholder="
                  td('dpp.instance.developTask.inputTaskName', '请输入任务名称')
                "
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item
              :label="
                td('dpp.instance.developTask.executionStatus', '执行状态')
              "
              prop="status"
            >
              <el-select
                v-model="queryParams.status"
                :placeholder="
                  td(
                    'dpp.instance.developTask.selectExecutionStatus',
                    '请选择执行状态'
                  )
                "
                clearable
                class="el-form-input-width"
              >
                <el-option
                  v-for="dict in dpp_etl_node_instance"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item
              :label="td('dpp.instance.developTask.executionTime')"
              prop="time"
            >
              <el-date-picker
                class="el-form-input-width"
                v-model="queryParams.time"
                @change="handleTimeChange"
                value-format="YYYY-MM-DD"
                type="daterange"
                range-separator="-"
                :start-placeholder="td('common.form.startDatePlaceholder')"
                :end-placeholder="td('common.form.endDatePlaceholder')"
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button
                plain
                type="primary"
                @click="handleQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22377 mr5"></i
                >{{ td("common.button.search", "查询") }}
              </el-button>
              <el-button
                @click="resetQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22378 mr5"></i
                >{{ td("common.button.reset", "重置") }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div class="pagecont-bottom">
          <div class="justify-between mb15">
            <div class="justify-end top-right-btn">
              <right-toolbar
                v-model:showSearch="showSearch"
                @queryTable="getList"
                :columns="columns"
              ></right-toolbar>
            </div>
          </div>
          <el-table
            stripe
            height="500px"
            v-loading="loading"
            :data="dppEtlTaskLogList"
            @selection-change="handleSelectionChange"
            :default-sort="defaultSort"
            @sort-change="handleSortChange"
          >
            <el-table-column
              v-if="getColumnVisibility(0)"
              width="150"
              :label="td('dpp.instance.developTask.id', '编号')"
              align="left"
              prop="id"
            />
            <el-table-column
              v-if="getColumnVisibility(1)"
              :show-overflow-tooltip="{ effect: 'light' }"
              :label="
                td('dpp.instance.developTask.nodeInstanceName', '节点实例名称')
              "
              align="left"
              prop="name"
              width="300"
            >
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(2)"
              :show-overflow-tooltip="{ effect: 'light' }"
              :label="td('dpp.instance.developTask.taskName', '任务名称')"
              align="left"
              prop="taskInstanceName"
              width="400"
            >
              <template #default="scope">
                {{ scope.row.taskInstanceName || "-" }}
              </template>
            </el-table-column>

            <el-table-column
              v-if="getColumnVisibility(3)"
              :label="td('dpp.instance.developTask.executionType', '执行类型')"
              width="140"
              :show-overflow-tooltip="{ effect: 'light' }"
              align="left"
              prop="commandType"
            >
              <template #default="scope">
                <dict-tag
                  :options="dpp_etl_task_instance_command_type"
                  :value="scope.row.commandType"
                />
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(4)"
              width="140"
              :label="
                td('dpp.instance.developTask.executionStatus', '执行状态')
              "
              align="left"
              prop="status"
            >
              <template #default="scope">
                <dict-tag
                  :options="dpp_etl_node_instance"
                  :value="scope.row.status.trim()"
                />
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(5)"
              width="160"
              :label="td('dpp.instance.developTask.startTime', '开始时间')"
              align="left"
              prop="startTime"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.startTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(6)"
              width="160"
              :label="td('dpp.instance.developTask.endTime', '结束时间')"
              align="left"
              prop="endTime"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.endTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>

            <!--            <el-table-column-->
            <!--                v-if="getColumnVisibility(7)"-->
            <!--                width="90"-->
            <!--                label="抽取量"-->
            <!--                 align="left"-->
            <!--                prop="extractionCount"-->
            <!--            >-->
            <!--                <template #default="scope">-->
            <!--                    {{ '-' }}-->
            <!--                </template>-->
            <!--            </el-table-column>-->
            <!--            <el-table-column-->
            <!--                v-if="getColumnVisibility(8)"-->
            <!--                width="90"-->
            <!--                label="写入量"-->
            <!--                 align="left"-->
            <!--                prop="writeCount"-->
            <!--            >-->
            <!--                <template #default="scope">-->
            <!--                    {{ '-' }}-->
            <!--                </template>-->
            <!--            </el-table-column>-->
            <el-table-column
              v-if="getColumnVisibility(9)"
              width="160"
              :label="
                td('dpp.instance.developTask.responsiblePerson', '责任人')
              "
              align="left"
              prop="createBy"
            >
              <template #default="scope">
                {{ scope.row.personChargeName || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(10)"
              :label="td('dpp.instance.developTask.createBy', '创建人')"
              :show-overflow-tooltip="true"
              align="left"
              prop="createBy"
              width="140"
            >
              <template #default="scope">
                {{ scope.row.createBy || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(11)"
              :label="td('dpp.instance.developTask.createTime', '创建时间')"
              align="left"
              prop="create_time"
              width="150"
              sortable="custom"
              column-key="create_time"
              :sort-orders="['descending', 'ascending']"
            >
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :label="td('dpp.instance.developTask.operation', '操作')"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
              width="200"
            >
              <template #default="scope">
                <el-button
                  link
                  type="primary"
                  icon="View"
                  @click="logDetailCatList(scope.row)"
                  >{{
                    td("dpp.instance.developTask.viewLog", "查看日志")
                  }}</el-button
                >
                <el-button
                  link
                  type="warning"
                  icon="Download"
                  @click="handleExport(scope.row)"
                  @mousedown="(e) => e.preventDefault()"
                >
                  {{ td("dpp.instance.developTask.downloadLog", "下载日志") }}
                </el-button>
              </template>
            </el-table-column>

            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>{{ td("common.noData") }}</p>
              </div>
            </template>
          </el-table>
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />

          <el-dialog
            :title="td('dpp.instance.developTask.viewLog', '查看日志')"
            v-model="open"
            width="1200px"
            :append-to="$refs['app-container']"
            draggable
            destroy-on-close
          >
            <div v-html="formattedText"></div>
            <template #footer>
              <div class="dialog-footer">
                <el-button @click="cancel">{{
                  td("common.button.close", "关闭")
                }}</el-button>
              </div>
            </template>
          </el-dialog>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup name="Develop">
import {
  defineEmits,
  defineProps,
  ref,
  computed,
  watch,
  getCurrentInstance,
} from "vue";
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import { listAttDataDevCat } from "@/api/att/cat/dataDevCat/dataDevCat";
import {
  listDppEtlNodeInstance,
  getDppEtlNodeInstance,
  delDppEtlNodeInstance,
  addDppEtlNodeInstance,
  updateDppEtlNodeInstance,
  logDetailCat,
} from "@/api/dpp/instance/integratio";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user";
const { proxy } = getCurrentInstance();
import DeptTree from "@/components/DeptTree/index.vue";
let activeName = ref("first");
const { dpp_etl_node_instance } = proxy.useDict("dpp_etl_node_instance");
const { dpp_etl_node_type, dpp_etl_task_instance_command_type } = proxy.useDict(
  "dpp_etl_node_type",
  "dpp_etl_task_instance_command_type"
);
const dppEtlTaskLogList = ref([]);
// 列显隐状态
const columnVisible = ref({
  0: true,
  1: true,
  2: true,
  3: true,
  4: true,
  5: true,
  6: true,
  9: true,
  10: true,
  11: true,
});

// 列配置（使用计算属性，确保国际化文本能响应语言切换）
const columns = computed(() => [
  {
    key: 0,
    label: td("dpp.instance.developTask.id", "编号"),
    visible: columnVisible.value[0],
  },
  {
    key: 1,
    label: td("dpp.instance.developTask.nodeInstanceName", "节点实例名称"),
    visible: columnVisible.value[1],
  },
  {
    key: 2,
    label: td("dpp.instance.developTask.taskName", "任务名称"),
    visible: columnVisible.value[2],
  },
  {
    key: 3,
    label: td("dpp.instance.developTask.executionType", "执行类型"),
    visible: columnVisible.value[3],
  },
  {
    key: 4,
    label: td("dpp.instance.developTask.executionStatus", "执行状态"),
    visible: columnVisible.value[4],
  },
  {
    key: 5,
    label: td("dpp.instance.developTask.startTime", "开始时间"),
    visible: columnVisible.value[5],
  },
  {
    key: 6,
    label: td("dpp.instance.developTask.endTime", "结束时间"),
    visible: columnVisible.value[6],
  },
  {
    key: 9,
    label: td("dpp.instance.developTask.responsiblePerson", "责任人"),
    visible: columnVisible.value[9],
  },
  {
    key: 10,
    label: td("dpp.instance.developTask.createBy", "创建人"),
    visible: columnVisible.value[10],
  },
  {
    key: 11,
    label: td("dpp.instance.developTask.createTime", "创建时间"),
    visible: columnVisible.value[11],
  },
]);

// 监听 RightToolbar 对 columns 的修改，同步到 columnVisible
watch(
  columns,
  (newColumns) => {
    newColumns.forEach((col) => {
      if (columnVisible.value[col.key] !== undefined) {
        columnVisible.value[col.key] = col.visible;
      }
    });
  },
  { deep: true }
);

const getColumnVisibility = (key) => {
  return columnVisible.value[key] !== undefined
    ? columnVisible.value[key]
    : true;
};
const userStore = useUserStore();
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();
const emit = defineEmits(["resetCat"]);

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
  url: import.meta.env.VITE_APP_BASE_API + "/dpp/dppEtlTaskLog/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    type: null,
    name: null,
    code: null,
    version: null,
    projectId: null,
    projectCode: null,
    personCharge: null,
    locations: null,
    description: null,
    timeout: null,
    extractionCount: null,
    writeCount: null,
    status: null,
    dsId: null,
    createTime: null,
    startTime: null,
    endTime: null,
    time: [],
    taskType: "3",
    catCode: null,
    orderByColumn: "start_time",
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

function handleTimeChange(value) {
  if (!value) {
    handleTimeClear();
    return;
  }
  queryParams.value.startTime = value[0] + " 00:00:00";
  queryParams.value.endTime = value[1] + " 23:59:59";
}
function handleTimeClear() {
  queryParams.value.startTime = null;
  queryParams.value.endTime = null;
}
/** 查询数据集成任务-日志列表 */
function getList() {
  loading.value = true;
  queryParams.value.projectCode = userStore.projectCode;
  listDppEtlNodeInstance(queryParams.value).then((response) => {
    dppEtlTaskLogList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}
let msg = ref();
async function logDetailCatList(row) {
  msg.value = {};
  const response = await logDetailCat(row.id);
  if (response && response) {
    msg.value = response.msg;
    open.value = true;
  }
}
const formattedText = computed(() => {
  console.log("msg.value", msg.value);

  return msg.value.replace(/\n/g, "<br>"); // 将换行符替换为 <br> 标签
});
/** 导出按钮操作 */
async function handleExport(row) {
  proxy.download(
    "/dpp/etlNodeInstance/downloadLog",
    {
      nodeInstanceId: row.id,
    },
    `${row.name}.log`
  );
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
    type: null,
    name: null,
    code: null,
    version: null,
    projectId: null,
    projectCode: null,
    personCharge: null,
    locations: null,
    description: null,
    timeout: null,
    extractionCount: null,
    writeCount: null,
    status: null,
    dsId: null,
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
  proxy.resetForm("dppEtlTaskLogRef");
}
let deptOptions = ref([]);
/** 下拉树结构 */
function getDeptTree() {
  listAttDataDevCat({
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    validFlag: true,
  }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td("dpp.instance.developTask.dataDevCategory", "数据开发类目"),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
const DeptTreeRef = ref(null); /** 重置按钮操作 */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  handleTimeClear();
  proxy.resetForm("queryRef");
  queryParams.value.catCode = null;
  emit("resetCat");
  handleQuery();
}
function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  queryParams.value.pageNum = 1;
  handleQuery();
}
// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('dpp.developTask.addLogTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDppEtlNodeInstance(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dpp.developTask.editLogTitle');
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDppEtlNodeInstance(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('dpp.developTask.logDetailTitle');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dppEtlTaskLogRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDppEtlNodeInstance(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addDppEtlNodeInstance(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dpp.developTask.confirmDeleteLog', '', { id: _ids }))
    .then(function () {
      return delDppEtlNodeInstance(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
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

// 监听projectCode数据变化
watch(
  () => userStore.projectCode,
  (projectCode) => {
    if (projectCode) {
      getList();
    }
  },
  { immediate: true } // 立即触发，防止数据已存在的情况
);
getDeptTree();
getList();
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
  // margin: 13px 15px;
}

.pagecont-bottom {
  min-height: auto;
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

.ellipsis-container {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
