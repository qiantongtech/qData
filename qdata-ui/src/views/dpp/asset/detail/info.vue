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
import useDefaultLang from "@/composables/useDefaultLang"

const { td } = useDefaultLang();
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
    { key: "tableType", label: td('dpp.asset.dataType', '数据类型'), dictOptions: table_type.value },
    { key: "tagNames", label: td('dpp.asset.dataTag', '数据标签'), type: "tags" },
    { key: "createBy", label: td('common.texts.createdBy', '创建人') },
    { key: "createUserPhoneNumber", label: td('dpp.asset.contactNumber', '联系方式') },
    { key: "updateBy", label: td('common.texts.updatedBy', '更新人') },
    { key: "updateUserPhoneNumber", label: td('dpp.asset.contactNumber', '联系方式') },
  ];

  if (props.form1.type == 1) {
    return [
      { key: "datasourceName", label: td('dpp.asset.datasourceName', '数据连接名称') },
      { key: "datasourceType", label: td('dpp.asset.datasourceType', '数据连接类型') },
      { key: "datasourceIp", label: td('dpp.asset.datasourceIp', '数据连接名称IP') },
      { key: "dataCount", label: td('dpp.asset.rowCount', '行数') },
      { key: "fieldCount", label: td('dpp.asset.columnCount', '列数') },
      ...commonFields,
    ];
  } else if (props.form1.type == 2) {
    return [
      { key: "appName", label: td('dpp.asset.appName', '应用名称') },
      { key: "developerName", label: td('dpp.asset.developerName', '开发者') },
      { key: "url", label: td('dpp.asset.serviceUrl', '服务地址') },
      { key: "httpMethod", label: td('dpp.asset.httpMethod', '请求类型') },
      ...commonFields,
    ];
  } else if (props.form1.type == 7) {
    return [
      { key: "datasourceName", label: td('dpp.asset.datasourceName', '数据连接名称') },
      { key: "datasourceType", label: td('dpp.asset.datasourceType', '数据连接类型') },
      { key: "fileName", label: td('dpp.asset.fileName', '文件名') },
      { key: "fileType", label: td('dpp.asset.fileType', '文件类型') },
      { key: "fileSize", label: td('dpp.asset.fileSize', '文件大小（字节）') },
      { key: "filePath", label: td('dpp.asset.filePath', '文件路径') },
      { key: "fileLastModified", label: td('common.texts.updatedTime', '更新时间'), type: "time" },
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
