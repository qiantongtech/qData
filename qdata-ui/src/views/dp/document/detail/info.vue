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
        :span="item.span || 1" class-name="base-content">
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
        <span v-else-if="item.key == 'status'">
          <dict-tag :options="dp_document_status" :value="getDescValue(item)" />
        </span>
        <div v-else-if="item.key == 'updateTime'">
          {{
            parseTime(
              form1.updateTime,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}
        </div>
        <div v-else-if="item.key == 'createTime'">
          {{
            parseTime(
              form1.createTime,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}
        </div>
        <div v-else-if="item.key == 'releaseDate'">
          {{
            parseTime(
              form1.releaseDate,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}

        </div>
        <div v-else-if="item.key == 'implementationDate'">
          {{
            parseTime(
              form1.implementationDate,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}

        </div>
        <div v-else-if="item.key == 'abolitionDate'">
          {{
            parseTime(
              form1.abolitionDate,
              "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}

        </div>
        <span v-else-if="item.key == 'createType'">{{ item.value == 1 ? td('dp.document.virtualAssetCreate') : td('dp.document.fullAssetCreate') }}</span>
        <span v-else>{{ getDescValue(item) }}</span>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script setup name="BasicInfo">
import useDefaultLang from "@/composables/useDefaultLang"

const { td } = useDefaultLang();
const props = defineProps({
  form1: {
    type: Object,
    default: () => { },
  },
});
const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_document_status } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_document_status"
);
const fileDesc = ref([
  { key: "releaseDate", label: td('dp.document.releaseDate') },
  { key: "implementationDate", label: td('dp.document.implementationDate') },
  { key: "abolitionDate", label: td('dp.document.abolitionDate') },
  // { key: "fileName", label: "文件名称" },
  { key: "fileUrl", label: td('dp.document.file') },
  { key: "createBy", label: td('common.texts.createdBy') },
  { key: "createTime", label: td('common.texts.createdTime') },
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
  { key: "remark", label: td('common.texts.remark'), span: 2 },]);
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
