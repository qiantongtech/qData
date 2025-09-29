<template>
  <div class="basicInfo">
    <el-descriptions title="" :column="2" border>
      <el-descriptions-item v-for="(item, index) in fileDesc" :key="index" label-class-name="base-label"
        :span="item.span" class-name="base-content">
        <template #label>
          <div class="cell-item">{{ item.label }}</div>
        </template>
        <div v-if="item.key == 'status'">
          <dict-tag :options="da_discovery_task_status" :value="daDiscoveryTaskDetail.status" />
        </div>
        <div v-else-if="item.key == 'countSubmitted'">
          <ProgressBar :completed="daDiscoveryTaskDetail.countSubmitted"
            :inProgress="daDiscoveryTaskDetail.countPending" :notStarted="daDiscoveryTaskDetail.countIgnoreFlag"
            :total="21" />
        </div>
        <div v-else-if="item.key == 'createTime'">
          {{
            parseTime(
              daDiscoveryTaskDetail.createTime,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}
        </div>
        <div v-else-if="item.key == 'updateTime'">
          {{
            parseTime(
              daDiscoveryTaskDetail.updateTime,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}
        </div>
        <div v-else>{{ getDescValue(item) }}</div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script setup name="BasicInfo">
import ProgressBar from "@/views/da/discovery/components/progressBar.vue";
import moment from "moment";
import { cronToZh } from "@/utils/cronUtils";
const { proxy } = getCurrentInstance();
const { da_discovery_task_status } = proxy.useDict("da_discovery_task_status");
const props = defineProps({
  daDiscoveryTaskDetail: {
    type: Object,
    default: () => { },
  },
});
const fileDesc = computed(() => {
  return table.value;
});
const table = ref([
  {
    key: "datasourceType",
    label: "数据库类型",
    value: "",
  },
  {
    key: "datasourceName",
    label: "数据库连接",
    value: "",
  },

  {
    key: "ip",
    label: "数据库地址",
    value: "",
  },

  {
    key: "contactNumber",
    label: "联系电话",
    value: "",
  },
  {
    key: "countSubmitted",
    label: "资产提交进展",
    value: "",
    countSubmitted: 0,
    countPending: 0,
    countIgnoreFlag: 0,
  },
  {
    key: "email",
    label: "报警邮箱",
    value: "",
  },
  {
    key: "createTime",
    label: "创建时间",
    value: "",
  },
  {
    key: "createBy",
    label: "创建人",
    value: "",
  },
  {
    key: "updateTime",
    label: "更新时间",
    value: "",
    type: "time",
  },
  {
    key: "updateBy",
    label: "更新人",
    value: "",
  },
  {
    key: "remark",
    label: "备注",
    value: "",
    span: 24,
  },
]);
const getDescValue = (row) => {
  let detail = { ...props.daDiscoveryTaskDetail };
  if (props.daDiscoveryTaskDetail) {
    if (row.type == "time") {
      row.value = moment(detail[row.key]).format("YYYY-MM-DD");
    } else if (row.key == "countSubmitted") {
      row.countSubmitted = detail[row.countSubmitted];
      row.countPending = detail[row.countPending];
      row.countIgnoreFlag = detail[row.countIgnoreFlag];
    } else {
      row.value = detail[row.key];
    }
  }
  return row.value !== null && row.value !== undefined && row.value !== "" ? row.value : "-";
};
</script>
<style lang="scss" scoped>
:deep(.base-label) {
  width: 200px;

  .cell-item {
    font-weight: 500;
  }
}
</style>
