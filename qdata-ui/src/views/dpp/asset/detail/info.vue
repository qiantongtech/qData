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
  <div class="basicInfo">
    <DescriptionsInfo
      :title="''"
      :column="2"
      :border="true"
      :data="detailData"
      :items="itemsForDesc"
    >
      <!-- <template #tags="{ data }">
        <span v-if="!data?.tags || !data.tags.length">-</span>
        <template v-else>
          <el-tag v-for="tag in data.tags" :key="tag" class="mr10">
            {{ tag }}
          </el-tag>
        </template>
      </template> -->
    </DescriptionsInfo>
  </div>
</template>
<script setup name="BasicInfo">
const { proxy } = getCurrentInstance();
const { table_type } = proxy.useDict("table_type");

const props = defineProps({
  form1: {
    type: Object,
    default: () => {},
  },
});
const detailData = computed(() => {
  const base = { ...(props.form1 || {}) };
  if (props.form1?.type == 2 && props.form1?.daAssetApi) {
    Object.assign(base, props.form1.daAssetApi);
  }
  if (props.form1?.type == 7 && props.form1?.fileInfo) {
    Object.assign(base, {
      fileName: props.form1.fileInfo?.name,
      filePath: props.form1.fileInfo?.path,
      fileType: props.form1.fileInfo?.type,
      fileSize: props.form1.fileInfo?.size,
      fileCreateTime: props.form1.fileInfo?.createTime,
      fileLastModified: props.form1.fileInfo?.lastModified,
      fileTime: props.form1.fileInfo?.time,
    });
  }
  return base;
});

const itemsForDesc = computed(() => {
  const commonFields = [
    { key: "tableType", label: "数据类型", dictOptions: table_type.value },
    { key: "tagNames", label: "数据标签", type: "tags" },
    { key: "createBy", label: "创建人" },
    { key: "createUserPhoneNumber", label: "联系方式" },
    { key: "updateBy", label: "更新人" },
    { key: "updateUserPhoneNumber", label: "联系方式" },
  ];

  if (props.form1.type == 1) {
    return [
      { key: "datasourceName", label: "数据连接名称" },
      { key: "datasourceType", label: "数据连接类型" },
      { key: "datasourceIp", label: "数据连接名称IP" },
      { key: "dataCount", label: "行数" },
      { key: "fieldCount", label: "列数" },
      ...commonFields,
    ];
  } else if (props.form1.type == 2) {
    return [
      { key: "appName", label: "应用名称" },
      { key: "developerName", label: "开发者" },
      { key: "url", label: "服务地址" },
      { key: "httpMethod", label: "请求类型" },
      ...commonFields,
    ];
  } else if (props.form1.type == 7) {
    return [
      { key: "datasourceName", label: "数据连接名称" },
      { key: "datasourceType", label: "数据连接类型" },
      { key: "fileName", label: "文件名" },
      { key: "fileType", label: "文件类型" },
      { key: "fileSize", label: "文件大小（字节）" },
      { key: "filePath", label: "文件路径" },
      { key: "fileLastModified", label: "更新时间", type: "time" },
      ...commonFields,
    ];
  }
  return [...commonFields];
});
</script>
<style lang="scss" scoped>
:deep(.base-label) {
  width: 200px;

  .cell-item {
    font-weight: 500;
  }
}
</style>
