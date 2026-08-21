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
    label: td('dp.dataCode.personCharge'),
    value: "",
  },
  {
    key: "contactNumber",
    label: td('dp.dataCode.contactNumber'),
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
