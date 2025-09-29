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
        <span v-else-if="item.key == 'createType'">{{ item.value == 1 ? "虚拟资产创建" : "完整资产创建" }}</span>
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
const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_document_status } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_document_status"
);
const fileDesc = ref([
  { key: "releaseDate", label: "发布日期" },
  { key: "implementationDate", label: "实施日期" },
  { key: "abolitionDate", label: "废止日期" },
  // { key: "fileName", label: "文件名称" },
  { key: "fileUrl", label: "文件" },
  { key: "createBy", label: "创建人" },
  { key: "createTime", label: "创建时间" },
  {
    key: "updateBy",
    label: "更新人",
    value: "",
  },
  {
    key: "updateTime",
    label: "更新时间",
    value: "",
    type: "time",
  },
  { key: "remark", label: "备注", span: 2 },]);
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
