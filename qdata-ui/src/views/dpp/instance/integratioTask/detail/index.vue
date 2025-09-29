<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          <div class="task-item">
            <!-- 正方形编号 -->
            <div class="task-id" style=" aspect-ratio: auto !important">
              {{ dppEtlTaskDetail.id || '-' }}
            </div>
            <!-- 名称 -->
            <div class="task-name">
              {{ dppEtlTaskDetail.names || '' }}
            </div>
          </div>
        </div>
        <el-row :gutter="2">
          <!-- <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">编号</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail?.id || '-' }}
              </div>
            </div>
          </el-col> -->
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务实例名称</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail?.name || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">
                {{ parseTime(dppEtlTaskDetail.createTime, '{y}-{m}-{d} {h}:{i}') }}

              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">执行状态</div>
              <div class="infotop-row-value">
                <dict-tag :options="dpp_etl_node_instance" :value="dppEtlTaskDetail.status" />
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建人</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail?.createBy || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">责任人</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail?.personChargeName || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">执行类型</div>
              <div class="infotop-row-value">

                <dict-tag :options="dpp_etl_task_instance_command_type" :value="dppEtlTaskDetail.commandType" />

              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">开始时间</div>
              <div class="infotop-row-value">
                {{
                  parseTime(
                    dppEtlTaskDetail.startTime,
                    "{y}-{m}-{d} {h}:{i}"
                  ) || "-"
                }}
              </div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">结束时间</div>
              <div class="infotop-row-value">
                {{
                  parseTime(
                    dppEtlTaskDetail.endTime,
                    "{y}-{m}-{d} {h}:{i}"
                  ) || "-"
                }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom" v-loading="loading">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="任务流程" name="1" key="1">
          <processNode ref="compRef" />
        </el-tab-pane>
        <el-tab-pane label="任务日志" name="2" key="2">
          <instanceLog ref="compReftwo" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from "vue-router";
import processNode from "./processNode.vue";
import instanceLog from "./instanceLog.vue";
import { reactive, ref, toRefs, watch, getCurrentInstance } from "vue";
import { getLogByTaskInstanceId, getTaskInfo } from "@/api/dpp/task/etlTask";

const { proxy } = getCurrentInstance();
const { dpp_etl_node_type, dpp_etl_task_instance_command_type, dpp_etl_node_instance } = proxy.useDict(
  "dpp_etl_node_type",
  "dpp_etl_task_instance_command_type",
  "dpp_etl_node_instance"
);
const activeName = ref("1");
const showSearch = ref(true);
const route = useRoute();
let loading = ref(false);
let compReftwo = ref(null);
const data = reactive({
  dppEtlTaskDetail: {},
  form: {}
});
const { dppEtlTaskDetail } = toRefs(data);
// function getTask(id) {
//   if (!id) return;
//   loading.value = true;
//   dppEtlTask(id).then(response => {
//     dppEtlTaskDetail.value = {
//       ...response.data,
//       ...JSON.parse(response.data.draftJson || "{}"),
//       catName: response.data.catName
//     };
//     loading.value = false;

//   });
// }
let compRef = ref(null);
let logContent = ref("");
let polling = ref(false);
const getTask = async (taskId) => {
  if (!taskId) return
  const res = await getTaskInfo(taskId);
  dppEtlTaskDetail.value = { ...res.data.taskInstance, names: res.data.name };
  compRef.value?.updateFlow(res.data);

  return res.data;
};

let timer = null;

const fetchLog = async (taskId) => {
  if (!taskId || !polling.value) return;

  const res = await getLogByTaskInstanceId({ taskInstanceId: taskId });
  const { status, log, nodeInstanceList } = res.data;
  logContent.value = log;
  compRef.value?.updateGraphNode(nodeInstanceList);
  compReftwo.value?.updateLog(log);
  const s = Number(status);
  if ([5, 6, 7].includes(s)) {
    polling.value = false;
    return;
  }
  if (polling.value) {
    timer = setTimeout(() => {
      if (!polling.value) return;
      fetchLog(taskId);
    }, 3000);
  }
};
watch(
  () => route.query.id,
  async (newId) => {
    const taskData = await getTask(newId);
    polling.value = true;
    fetchLog(newId);
  },
  { immediate: true }
);


// 清理函数
const clearPolling = () => {
  polling.value = false;
  if (timer) {
    clearTimeout(timer);
    timer = null;
  }
};
onUnmounted(clearPolling);
onDeactivated(() => {
  clearPolling();
  dppEtlTaskDetail.value = { taskConfig: {}, name: null };
  logContent.value = "";
  activeName.value = "1";
});
const handleClick = (tab, event) => {
  console.log(tab, event);
};
</script>

<style lang="less" scoped>
.pagecont-bottom {
  min-height: calc(100vh - 350px) !important;
}
</style>
