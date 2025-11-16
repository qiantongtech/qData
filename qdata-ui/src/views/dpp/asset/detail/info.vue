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
  <!-- 资产详情 tab -->
  <div class="basicInfo">
    <el-descriptions title="" :column="2" border>
      <el-descriptions-item v-for="(item, index) in fileDesc" :key="index" label-class-name="base-label"
        :span="item.span" class-name="base-content">
        <template #label>
          <div class="cell-item">{{ item.label }}</div>
        </template>
        <span v-if="item.key == 'tags'">
          <template v-if="item.value.length != 0">
            <el-tag v-for="tag in item.value" :key="tag" class="mr10">
              {{ tag }}
            </el-tag>
          </template>
          <template v-else>-</template>
        </span>
        <span v-else-if="item.key == 'createType'">{{ item.value == 1 ? "虚拟资产创建" : "完整资产创建" }}</span>
        <div v-else-if="item.key == 'createTime'">
            {{
            parseTime(
                form1.createTime,
                "{y}-{m}-{d} {h}:{i}"
            ) || "-"
            }}
        </div>
        <div v-else-if="item.key == 'updateTime'">
            {{
            parseTime(
                form1.updateTime,
                "{y}-{m}-{d} {h}:{i}"
            ) || "-"
            }}
        </div>
        <span v-else>{{ getDescValue(item) }}</span>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script setup name="BasicInfo">
const props = defineProps({
  form1: {
    type: Object,
    default: () => { },
  },
});
const fileDesc = computed(() => {
  if (props.form1.type == 1) {
    return table.value;
  } else if (props.form1.type == 2) {
    return api.value;
  } else if (props.form1.type == 7) {
    return file.value;
  }
});
const table = ref([
  {
    key: "datasourceName",
    label: "数据连接名称",
    value: "",
  },
  {
    key: "datasourceType",
    label: "数据连接类型",
    value: "",
  },
  {
    key: "dbname",
    label: "数据连接实例",
    value: "",
  },
  {
    key: "datasourceIp",
    label: "数据库连接IP",
    value: "",
  },
  {
      key: "dataCount",
      label: "行数",
      value: "",
  },
  {
      key: "fieldCount",
      label: "列数",
      value: "",
  },
  {
      key: "createBy",
      label: "创建人",
      value: "-",
  },
  {
      key: "createTime",
      label: "创建时间",
      value: "-",
  },
  {
      key: "updateBy",
      label: "更新人",
      value: "-",
  },
  {
      key: "updateTime",
      label: "更新时间",
      value: "-",
  },
  {
      key: "remark",
      label: "备注",
      value: "",
      span: 24,
  },
]);
const api = ref([
  {
    key: "appName",
    label: "应用名称",
    value: "",
  },
  {
    key: "developerName",
    label: "开发者",
    value: "",
  },
  {
    key: "url",
    label: "服务地址",
    value: "",
  },
  {
    key: "httpMethod",
    label: "请求类型",
    value: "",
  },
    {
        key: "createBy",
        label: "创建人",
        value: "-",
    },
    {
        key: "createTime",
        label: "创建时间",
        value: "-",
    },
    {
        key: "updateBy",
        label: "更新人",
        value: "-",
    },
    {
        key: "updateTime",
        label: "更新时间",
        value: "-",
    },
    {
        key: "remark",
        label: "备注",
        value: "",
        span: 24,
    },
]);
const file = ref([
  {
    key: "datasourceName",
    label: "数据连接名称",
    value: "",
  },
  {
    key: "datasourceType",
    label: "数据连接类型",
    value: "",
  },
  {
    key: "fileName",
    label: "文件名",
    value: "-",
  },
  {
    key: "fileType",
    label: "文件类型",
    value: "-",
  },
  {
    key: "fileSize",
    label: "文件大小（字节）",
    value: "-",
  },
  {
    key: "filePath",
    label: "文件路径",
    value: "-",
  },
  {
      key: "createBy",
      label: "创建人",
      value: "-",
  },
  {
    key: "fileCreateTime",
    label: "创建时间",
    value: "-",
  },
  {
      key: "updateBy",
      label: "更新人",
      value: "-",
  },
  {
    key: "fileLastModified",
    label: "更新时间",
    value: "-",
  },
  // {
  //   key: "fileTime",
  //   label: "访问时间",
  //   value: "-",
  // },
  {
    key: "remark",
    label: "备注",
    value: "",
    span: 24,
  },
]);
const getDescValue = (row) => {
  let detail = { ...props.form1 };
  if (props.form1) {
    if (props.form1.type == 2) {
      detail = { ...detail, ...props.form1.daAssetApi };
    }
    if (props.form1.type == 7) {
      detail = {
        ...detail,
        ...{
          fileName: props.form1.fileInfo?.name,
          filePath: props.form1.fileInfo?.path,
          fileType: props.form1.fileInfo?.type,
          fileSize: props.form1.fileInfo?.size,
          fileCreateTime: props.form1.fileInfo?.createTime,
          fileLastModified: props.form1.fileInfo?.lastModified,
          fileTime: props.form1.fileInfo?.time,
        },
      };
    }
    row.value = detail[row.key];
  }
  return row.value !== null && row.value !== undefined ? row.value : "-";
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
