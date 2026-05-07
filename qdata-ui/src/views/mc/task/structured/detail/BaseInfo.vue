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
