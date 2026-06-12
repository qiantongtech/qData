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

        <div v-else-if="item.key == 'createTime'">
          {{
            parseTime(
              daDiscoveryTaskDetail.createTime,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}
        </div>
        <div v-else-if="item.key == 'createType'">
          <dict-tag :options="dp_model_create_type" :value="daDiscoveryTaskDetail.createType" />
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
import useDefaultLang from "@/composables/useDefaultLang"
import moment from "moment";
import { cronToZh } from "@/utils/cronUtils";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dp_model_status, dp_model_create_type } = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type"
);
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
    key: "personChargeName",
    label: td('dp.dataElem.personCharge'),
    value: "",
  },
  {
    key: "contactNumber",
    label: td('dp.dataElem.contactNumber'),
    value: "",
  },

  {
    key: "createBy",
    label: td('common.texts.createdBy'),
    value: "",
  },
  {
    key: "createTime",
    label: td('common.texts.createdTime'),
    value: "",
  },
  {
    key: "updateBy",
    label: td('common.texts.updatedBy'),
    value: "",
  },
  {
    key: "updateTime",
    label: td('common.texts.updatedTime'),
    value: "",
    type: "time",
  },
  {
    key: "remark",
    label: td('common.texts.remark'),
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
