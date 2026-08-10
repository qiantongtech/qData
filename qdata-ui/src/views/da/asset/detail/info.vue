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
    { key: "tableType", label: td('dpp.asset.dataType', 'Data Type'), dictOptions: table_type.value },
    { key: "tagNames", label: td('dpp.asset.dataTag', 'Data Tags'), type: "tags" },
    { key: "createBy", label: td('common.texts.createdBy', 'Created By') },
    { key: "createUserPhoneNumber", label: td('dpp.asset.contactNumber', 'Contact Info') },
    { key: "updateBy", label: td('common.texts.updatedBy', 'Updated By') },
    { key: "updateUserPhoneNumber", label: td('dpp.asset.contactNumber', 'Contact Info') },
  ];

  if (props.form1.type == 1) {
    return [
      { key: "datasourceName", label: td('dpp.asset.datasourceName', 'Data Connection Name') },
      { key: "datasourceType", label: td('dpp.asset.datasourceType', 'Data Connection Type') },
      { key: "datasourceIp", label: td('dpp.asset.datasourceIp', 'Data Connection IP') },
      { key: "dataCount", label: td('dpp.asset.rowCount', 'Row Count') },
      { key: "fieldCount", label: td('dpp.asset.columnCount', 'Column Count') },
      ...commonFields,
    ];
  } else if (props.form1.type == 2) {
    return [
      { key: "appName", label: td('dpp.asset.appName', 'Application Name') },
      { key: "developerName", label: td('dpp.asset.developerName', 'Developer') },
      { key: "url", label: td('dpp.asset.serviceUrl', 'Service URL') },
      { key: "httpMethod", label: td('dpp.asset.httpMethod', 'Request Type') },
      ...commonFields,
    ];
  } else if (props.form1.type == 7) {
    return [
      { key: "datasourceName", label: td('dpp.asset.datasourceName', 'Data Connection Name') },
      { key: "datasourceType", label: td('dpp.asset.datasourceType', 'Data Connection Type') },
      { key: "fileName", label: td('dpp.asset.fileName', 'File Name') },
      { key: "fileType", label: td('dpp.asset.fileType', 'File Type') },
      { key: "fileSize", label: td('dpp.asset.fileSize', 'File Size (bytes)') },
      { key: "filePath", label: td('dpp.asset.filePath', 'File Path') },
      { key: "fileLastModified", label: td('common.texts.updatedTime', 'Updated Time'), type: "time" },
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
