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
  <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
    showCancelButton :show-close="false" destroy-on-close :close-on-click-modal="false">
    <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading" :disabled="info" :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.nodeName', '节点名称')" prop="name" :rules="[
            { required: true, message: td('dpp.integration.nodeNameRequired', '请输入节点名称'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-input v-model="form.name" :placeholder="td('dpp.integration.nodeNamePlaceholder', '请输入节点名称')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.type', '类型')" prop="typeName" :label-position="labelPosition">
            <el-select v-model="form.taskParams.typeName" :placeholder="td('dpp.integration.typePlaceholder', '请输入类型')" filterable disabled>
              <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
            <el-input v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.uploadAttachment', '上传附件')" prop="taskParams.excelFile" :rules="[
            { required: true, message: td('dpp.integration.uploadAttachmentRequired', '请上传附件'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <!-- <FileUploadbtn :limit="1" v-model="form.taskParams.excelFile" :dragFlag="false" :file-type="['xlsx', 'xls']"
              :fileSize="50" @handleRemove="handleRemove" /> -->
            <FileUploadbtn :limit="1" v-model="form.taskParams.excelFile" :dragFlag="false" :fileSize="50"
              @handleRemove="handleRemove" :file-type="['xlsx', 'xls']" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.startRow', '起始行')" prop="taskParams.startData" :rules="[
            { required: true, message: td('dpp.integration.startRowRequired', '请输入起始行'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-input-number :step="1" step-strictly :placeholder="td('dpp.integration.startRowPlaceholder', '请输入起始行')" v-model="form.taskParams.startData"
              style="width: 100%" controls-position="right" :min="1" value-on-clear="min" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.startColumn', '起始列')" prop="taskParams.startColumn" :rules="[
            { required: true, message: td('dpp.integration.startColumnRequired', '请输入起始列'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-input-number :step="1" step-strictly :placeholder="td('dpp.integration.startColumnPlaceholder', '请输入起始列')" v-model="form.taskParams.startColumn"
              style="width: 100%" controls-position="right" :min="1" value-on-clear="min" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" plain @click="parseExcel" style="margin-left: 60px" :disabled="isButtonDisabled">
            {{ td('dpp.integration.parseExcel') }}
          </el-button>
        </el-col>
      </el-row>
      <el-divider content-position="center">
        <span class="blue-text">{{ td('dpp.integration.attributeFields', '属性字段') }}</span>
      </el-divider>
      <el-table stripe height="310px" v-loading="loadingList" :data="ColumnByAssettab">
        <el-table-column :label="td('common.display.index', '序号')" type="index" width="80" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.fieldName', '字段名称')" align="left" prop="columnName" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.columnName || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.fieldType', '字段类型')" align="left" prop="columnType">
          <template #default="scope">
            {{ scope.row.columnType || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.dateFormat', '日期格式')" align="left" prop="format">
          <template #default="scope">
            {{ scope.row.format || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="openDialog(scope.row)">{{ td('common.button.update') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">{{ td('common.button.save') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <excelUploadDialog :visible="open" :title="td('dpp.integration.attributeFieldEdit', '属性字段编辑')" @update:visible="open = $event" @confirm="handletaskConfig"
    :data="row" />
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { getToken } from "@/utils/auth.js";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey, getExcelColumn } from "@/api/dpp/task/index.js";
import excelUploadDialog from "../excelUpload.vue";
import FileUploadbtn from '@/components/FileUploadbtn/index1.vue'
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user.js";

const { td } = useDefaultLang();
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '' },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: {}
});
const emit = defineEmits(["update", "confirm"]);
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update", newValue);
  },
});
// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref();
// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
// 属性字段修改新增
const handletaskConfig = (form) => {
  ColumnByAssettab.value = ColumnByAssettab.value.map((column) => {
    if (column.id == form.id) {
      return { ...column, ...form };
    }
    return column;
  });
};

let dpModelRefs = ref();
let form = ref({});
const tableFields = ref([]); // 来源表格
// 计算属性：判断按钮是否禁用
const isButtonDisabled = computed(() => {
  console.log(form.value.taskParams.excelFile);
  return (
    !form.value.taskParams.startData ||
    !form.value.taskParams.startColumn ||
    !form.value.taskParams.excelFile
  );
});
// 获取列数据
const parseExcel = async (id) => {
  if (!form.value.taskParams.startData) {
    ElMessage.warning(td("dpp.integration.parseFailedAddStartRow", "解析失败，请添加起始行"));
    return;
  }
  if (!form.value.taskParams.startColumn) {
    ElMessage.warning(td("dpp.integration.parseFailedAddStartColumn", "解析失败，请添加起始列"));
    return;
  }
  if (!form.value.taskParams.excelFile) {
    ElMessage.warning(td("dpp.integration.parseFailedAddAttachment", "解析失败，请添加附件"));
    return;
  }
  loadingList.value = true;
  try {
    let res = await getExcelColumn({
      startData: form.value.taskParams.startData,
      startColumn: form.value.taskParams.startColumn,
      excelFile: form.value.taskParams.excelFile,
    });

    if (res?.data?.csvFile) {
      form.value.taskParams.csvFile = res.data.csvFile;
      ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
        id: index,
        columnName: item,
        columnType: "string",
      }));

      ElMessage.success(td("dpp.integration.excelParseSuccess", "Excel解析成功，请确认属性字段类型！"));
    } else {
      ElMessage.warning(td("dpp.integration.excelParseFailedNoData", "Excel解析失败，未获取到有效数据！"));
    }
  } catch (error) {
    if (response.code == 200)
      ElMessage.warning(td("dpp.integration.excelParseFailedCheckFile", "Excel解析失败，请检查文件格式或内容！"));
  } finally {
    loadingList.value = false;
  }
};

const off = () => {
  proxy.resetForm("dpModelRefs");
  // 清空表格字段数据
  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// 保存数据
const saveData = async () => {
  try {
    // 异步验证表单
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;
    if (
      form.value?.taskParams.type == "1" &&
      (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
    ) {
      return proxy.$message.warning(td("dpp.integration.validateFailedSelectFields", "校验未通过，请选择属性字段"));
    }
    // 如果没有 code，就调用接口获取唯一的 code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false; // 结束加载状态
      form.value.code = response.data; // 设置唯一的 code
    }
    const taskParams = form.value?.taskParams;
    taskParams.tableFields = ColumnByAssettab.value;
    taskParams.columnsList = ColumnByAssettab.value.map(({ columnName, columnType }) => ({
      colName: columnName,
      dataType: columnType,
    }));
    taskParams.columns = taskParams.tableFields.map((item) => {
      return {
        index: item.id,
        columnName: item.columnName,
        type: item.columnType,
        format: item.format
      };
    });
    emit("confirm", form.value);

  } finally {
    loadingList.value = false;
  }
};
const closeDialog = () => {
  off();
  // 关闭对话框
  emit("update", false);
};

// 监听属性变化
function deepCopy(data) {
  if (data === undefined || data === null) {
    return {}; // 或者返回一个默认值
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    return {}; // 或者返回一个默认值
  }
}
// 监听属性变化
watchEffect(() => {
  if (props.visible) {
    // 数据源
    form.value = deepCopy(props.currentNode.data);
    ColumnByAssettab.value = props.currentNode?.data.taskParams.tableFields;
  } else {
    off();
  }
});
// 文件删除
function handleRemove() {
  ColumnByAssettab.value = [];
  form.value.taskParams.excelFile = undefined;
}
</script>
<style scoped lang="scss">
.blue-text {
  color: #2666fb;
}
</style>
