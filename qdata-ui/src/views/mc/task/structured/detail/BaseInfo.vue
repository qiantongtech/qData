
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
  <div class="base-info">
    <el-descriptions :column="2" border>
      <el-descriptions-item
        v-for="item in fields"
        :key="item.key"
        :label="item.label"
        :span="item.span || 1"
        label-class-name="label-column"
      >
        <div class="data-column">
          <template v-if="item.key === 'collectionScope'">
            <el-tooltip :content="collectionScopeText" placement="top">
              <span class="ellipsis">{{ collectionScopeText }}</span>
            </el-tooltip>
          </template>
          <template v-else>
            <dict-tag
              v-if="item.dict"
              :options="toValue(item.dict)"
              :value="infos[item.key]"
            />
            <span v-else>{{ getFormatValue(infos[item.key]) }}</span>
          </template>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script setup name="SynchronizeTaskBaseInfo">
import { computed, getCurrentInstance, toValue } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "mc_collect_scope",
  "mc_collect_mode"
);

const fields = [
  { key: "datasourceName", label: td("mc.task.structured.datasourceName") },
  {
    key: "dbType",
    label: td("mc.task.structured.dbType"),
    dict: dicts.datasource_type,
  },
  { key: "ip", label: td("mc.task.structured.ip") },
  { key: "port", label: td("mc.task.structured.port") },
  { key: "username", label: td("mc.task.structured.username") },
  { key: "cronExpression", label: td("mc.task.structured.cronExpression") },
  {
    key: "collectionScope",
    label: td("mc.task.structured.collectionScope"),
    span: 2,
  },
  { key: "updateBy", label: td("common.texts.updatedBy") },
  { key: "updateTime", label: td("common.texts.updatedTime") },
  { key: "createBy", label: td("common.texts.createdBy") },
  { key: "createTime", label: td("common.texts.createdTime") },
];

const props = defineProps({
  detail: {
    type: Object,
    required: true,
  },
});

const infos = computed(() => {
  const { datasourceDO } = props.detail;
  const datasourceConfig = datasourceDO?.datasourceConfig
    ? JSON.parse(datasourceDO?.datasourceConfig)
    : {};
  return {
    ...props.detail,
    datasourceName: datasourceDO?.datasourceName,
    ip: datasourceDO?.ip,
    port: datasourceDO?.port,
    username: datasourceConfig.username,
  };
});

const collectionScopeText = computed(() => {
  const list = props.detail?.scopeSaveReqVOS || [];
  return list
    .map((i) => (i.schemaName ? `${i.dbName}.${i.schemaName}` : i.dbName))
    .join(", ");
});
</script>

<style lang="scss" scoped>
::v-deep(.label-column) {
  width: 200px;
  font-weight: 500 !important;
}
.ellipsis {
  display: inline-block;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
