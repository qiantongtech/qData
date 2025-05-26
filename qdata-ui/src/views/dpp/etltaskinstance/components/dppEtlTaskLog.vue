<template>
  <div class="pagecont-top" v-show="showSearch">
    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="100px"
      v-show="showSearch" @submit.prevent>
      <div>
        <el-form-item label="节点实例名称" prop="name">
          <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入节点实例名称" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="任务名称" prop="taskInstanceName">
          <el-input class="el-form-input-width" v-model="queryParams.taskInstanceName" placeholder="请输入任务名称" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="执行状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择执行状态" clearable class="el-form-input-width">
            <el-option v-for="dict in dpp_etl_node_instance" :key="dict.value" :label="dict.label"
              :value="dict.value" />
          </el-select>
        </el-form-item>
      </div>
      <el-form-item label="执行时间" prop="time">
        <el-date-picker class="el-form-input-width" v-model="queryParams.time" @change="handleTimeChange"
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
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
      </div>
    </div>
    <el-table stripe height="500px" v-loading="loading" :data="dppEtlTaskLogList"
      @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column v-if="getColumnVisibility(0)" width="80" label="编号" align="center" prop="id" />
      <el-table-column v-if="getColumnVisibility(1)" show-overflow-tooltip label="节点实例名称" align="left" prop="name">
        <template #default="scope">
          {{ scope.row.name || "-" }}
        </template>
      </el-table-column>
      <el-table-column v-if="getColumnVisibility(2)" show-overflow-tooltip label="任务名称" align="left"
        prop="taskInstanceName">
        <template #default="scope">
          {{ scope.row.taskInstanceName || "-" }}
        </template>
      </el-table-column>

      <el-table-column v-if="getColumnVisibility(3)" label="执行类型" width="120" :show-overflow-tooltip="true" align="left"
        prop="commandType">
        <template #default="scope">
          <dict-tag :options="dpp_etl_task_instance_command_type" :value="scope.row.commandType" />
        </template>
      </el-table-column>
      <el-table-column v-if="getColumnVisibility(4)" width="100" label="执行状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="dpp_etl_node_instance" :value="scope.row.status.trim()" />
        </template>
      </el-table-column>
      <el-table-column v-if="getColumnVisibility(5)" width="160" label="开始时间" align="center" prop="startTime"
        show-overflow-tooltip>
        <template #default="scope">
          {{ scope.row.startTime || "-" }}
        </template>
      </el-table-column>
      <el-table-column v-if="getColumnVisibility(6)" width="160" label="结束时间" align="center" prop="endTime"
        show-overflow-tooltip>
        <template #default="scope">
          {{ scope.row.endTime || "-" }}
        </template>
      </el-table-column>

      <!--            <el-table-column-->
      <!--                v-if="getColumnVisibility(7)"-->
      <!--                width="90"-->
      <!--                label="抽取量"-->
      <!--                align="center"-->
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
      <!--                align="center"-->
      <!--                prop="writeCount"-->
      <!--            >-->
      <!--                <template #default="scope">-->
      <!--                    {{ '-' }}-->
      <!--                </template>-->
      <!--            </el-table-column>-->
      <el-table-column v-if="getColumnVisibility(9)" width="100" label="责任人" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.personChargeName || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="操作" header-align="center" class-name="small-padding fixed-width" fixed="right"
        width="200">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="logDetailCatList(scope.row)">查看日志</el-button>

          <el-button link type="warning" @click="handleExport(scope.row)" @mousedown="(e) => e.preventDefault()">
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

    <el-dialog title="查看日志" v-model="open" width="1200px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <div v-html="formattedText"></div>
      <!-- <template #footer>
              <div class="dialog-footer">
                  <el-button @click="open = false">关 闭</el-button>
              </div>
          </template> -->
    </el-dialog>
  </div>
</template>

<script setup name="DppEtlTaskLog">
import { defineEmits, defineProps } from "vue";

import {
  listDppEtlNodeInstance,
  getDppEtlNodeInstance,
  delDppEtlNodeInstance,
  addDppEtlNodeInstance,
  updateDppEtlNodeInstance,
  logDetailCat,
} from "@/api/dpp/etl/dppEtlNodeInstance";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user";
const { proxy } = getCurrentInstance();
let activeName = ref("first");
const { dpp_etl_node_instance } = proxy.useDict("dpp_etl_node_instance");
const { dpp_etl_node_type, dpp_etl_task_instance_command_type } = proxy.useDict(
  "dpp_etl_node_type",
  "dpp_etl_task_instance_command_type"
);
const dppEtlTaskLogList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "编号", visible: true },
  { key: 1, label: "节点实例名称", visible: true },
  { key: 2, label: "任务名称", visible: true },
  { key: 3, label: "执行类型", visible: true },
  { key: 4, label: "执行状态", visible: true },
  { key: 5, label: "开始时间", visible: true },
  { key: 6, label: "结束时间", visible: true },
  { key: 9, label: "责任人", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
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
    taskType: "1",
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

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  handleTimeClear();
  proxy.resetForm("queryRef");
  queryParams.value.catCode = null;
  emit("resetCat");
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
  title.value = "新增数据集成任务-日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDppEtlNodeInstance(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改数据集成任务-日志";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDppEtlNodeInstance(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "数据集成任务-日志详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dppEtlTaskLogRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDppEtlNodeInstance(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addDppEtlNodeInstance(form.value)
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
    .confirm('是否确认删除数据集成任务-日志编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDppEtlNodeInstance(_ids);
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

defineExpose({
  handleQuery,
  queryParams,
});
</script>
<style scoped lang="less">
::v-deep(.el-tabs__header) {
  margin: 0 0 0px;
  padding: 0px 15px 0px 15px;
  position: relative;
  background-color: #fff;
}

.app-container .pagecont-bottom {
  min-height: calc(100vh - 301px);
}
</style>
