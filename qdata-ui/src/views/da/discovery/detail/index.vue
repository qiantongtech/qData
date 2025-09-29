<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">{{ daDiscoveryTaskDetail.name || "-" }}</div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">编号</div>
              <div class="infotop-row-value">
                {{ daDiscoveryTaskDetail.id || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">数据发现类目</div>
              <div class="infotop-row-value">
                {{ daDiscoveryTaskDetail.catName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务状态</div>
              <div class="infotop-row-value">
                <dict-tag :options="da_discovery_task_status" :value="daDiscoveryTaskDetail.status" />
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">数据库连接</div>
              <div class="infotop-row-value">
                {{ daDiscoveryTaskDetail?.datasourceName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">调度周期</div>
              <div class="infotop-row-value" :title="cronToZh(daDiscoveryTaskDetail.cronExpression)">
                <span class="ellipsis-2">
                  {{ cronToZh(daDiscoveryTaskDetail.cronExpression) || "-" }}
                </span>
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">责任人</div>
              <div class="infotop-row-value">
                {{ daDiscoveryTaskDetail.contact || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">上次更新表数</div>
              <div class="infotop-row-value">
                {{ daDiscoveryTaskDetail.lastTableCount || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">最近执行时间</div>
              <div class="infotop-row-value">
                {{
                  parseTime(
                    daDiscoveryTaskDetail.lastExecuteTime,
                    "{y}-{m}-{d} {h}:{i}"
                  ) || "-"
                }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">下次执行时间</div>
              <div class="infotop-row-value">
                {{
                  parseTime(
                    daDiscoveryTaskDetail.updateTime,
                    "{y}-{m}-{d} {h}:{i}"
                  ) || "-"
                }}
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                <span class="ellipsis-2">
                  {{ daDiscoveryTaskDetail.description || "-" }}
                </span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick"
        v-hasPermi="['da:discoveryTable:list']">
        <el-tab-pane label="数据发现" name="0">
          <component-one :daDiscoveryTaskDetail="daDiscoveryTaskDetail"
            @updateData="getDaDiscoveryTaskDetailById"></component-one>
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="1">
          <BasicInfo :daDiscoveryTaskDetail="daDiscoveryTaskDetail"></BasicInfo>
        </el-tab-pane>

        <!-- <el-tab-pane label="组件二" name="2">
          <component-two></component-two>
        </el-tab-pane> -->
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="DaDiscoveryTask">
import { getDaDiscoveryTask } from "@/api/da/discovery/discoveryTask";
import { useRoute } from "vue-router";
import ComponentOne from "@/views/da/discovery/detail/table.vue";
import BasicInfo from "@/views/da/discovery/detail/info.vue";
const { proxy } = getCurrentInstance();
import { cronToZh } from "@/utils/cronUtils";
const { sys_job_status, sys_job_group, da_discovery_task_status } =
  proxy.useDict("sys_job_status", "sys_job_group", "da_discovery_task_status");

const activeName = ref("0");

const handleClick = (tab, event) => {
  console.log(tab, event);
};
const getReadableCronText = (cronExpression) => {
  if (!cronExpression) {
    return ""; // 如果 Cron 表达式为空，返回一个默认的错误信息
  }
  return cronstrue.toString(cronExpression, { locale: "zh_CN" });
};
const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1; // 如果 id 为空，使用默认值 1
    getDaDiscoveryTaskDetailById();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  daDiscoveryTaskDetail: {},
  form: {},
});

const { daDiscoveryTaskDetail, rules } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDaDiscoveryTaskDetailById() {
  console.log("zhixl");

  const _id = id;
  getDaDiscoveryTask(_id).then((response) => {
    daDiscoveryTaskDetail.value = response.data;
  });
}

getDaDiscoveryTaskDetailById();
</script>
