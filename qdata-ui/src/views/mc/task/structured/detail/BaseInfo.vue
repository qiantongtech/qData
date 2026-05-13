
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
const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "mc_collect_scope",
  "mc_collect_mode"
);

const fields = [
  { key: "datasourceName", label: "数据连接名称" },
  { key: "dbType", label: "数据连接类型", dict: dicts.datasource_type },
  { key: "ip", label: "IP地址" },
  { key: "port", label: "端口号" },
  { key: "username", label: "账号" },
  { key: "cronExpression", label: "调度周期" },
  {
    key: "collectionScope",
    label: "采集范围",
    span: 2,
  },
  { key: "updateBy", label: "更新人" },
  { key: "updateTime", label: "更新时间" },
  { key: "createBy", label: "创建人" },
  { key: "createTime", label: "创建时间" },
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
