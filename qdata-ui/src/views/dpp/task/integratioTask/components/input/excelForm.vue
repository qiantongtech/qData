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
          <el-form-item :label="td('dpp.integration.nodeName', 'Node Name')" prop="name" :rules="[
            { required: true, message: td('dpp.integration.nodeNameRequired', 'Please enter node name'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-input v-model="form.name" :placeholder="td('dpp.integration.nodeNamePlaceholder', 'Please enter node name')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.type', 'Type')" prop="typeName" :label-position="labelPosition">
            <el-select v-model="form.taskParams.typeName" :placeholder="td('dpp.integration.typePlaceholder', 'Please enter type')" filterable disabled>
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
          <el-form-item :label="td('dpp.integration.uploadAttachment', 'Upload Attachment')" prop="taskParams.excelFile" :rules="[
            { required: true, message: td('dpp.integration.uploadAttachmentRequired', 'Please upload attachment'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <!-- <FileUploadbtn :limit="1" v-model="form.taskParams.excelFile" :dragFlag="false" :file-type="['xlsx', 'xls']"
              :fileSize="50" @handleRemove="handleRemove" /> -->
            <FileUploadbtn :limit="1" v-model="form.taskParams.excelFile" :dragFlag="false" :fileSize="50"
              @handleRemove="handleRemove" :file-type="['xlsx', 'xls']" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.startRow', 'Start Row')" prop="taskParams.startData" :rules="[
            { required: true, message: td('dpp.integration.startRowRequired', 'Please enter start row'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-input-number :step="1" step-strictly :placeholder="td('dpp.integration.startRowPlaceholder', 'Please enter start row')" v-model="form.taskParams.startData"
              style="width: 100%" controls-position="right" :min="1" value-on-clear="min" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dpp.integration.startColumn', 'Start Column')" prop="taskParams.startColumn" :rules="[
            { required: true, message: td('dpp.integration.startColumnRequired', 'Please enter start column'), trigger: 'change' },
          ]" :label-position="labelPosition">
            <el-input-number :step="1" step-strictly :placeholder="td('dpp.integration.startColumnPlaceholder', 'Please enter start column')" v-model="form.taskParams.startColumn"
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
        <span class="blue-text">{{ td('dpp.integration.attributeFields', 'Attribute Fields') }}</span>
      </el-divider>
      <el-table stripe height="310px" v-loading="loadingList" :data="ColumnByAssettab">
        <el-table-column :label="td('common.display.index', 'Index')" type="index" width="80" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.fieldName', 'Field Name')" align="left" prop="columnName" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.columnName || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.fieldType', 'Field Type')" align="left" prop="columnType">
          <template #default="scope">
            {{ scope.row.columnType || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.dateFormat', 'Date Format')" align="left" prop="format">
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
  <excelUploadDialog :visible="open" :title="td('dpp.integration.attributeFieldEdit', 'Attribute Field Edit')" @update:visible="open = $event" @confirm="handletaskConfig"
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
  graph: {},
  taskType: { type: String, default: "" },
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
// variable definition
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref();
// Modify
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
// Attribute fields modified and added
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
const tableFields = ref([]); // Source form
// Computed property: determine whether the button is disabled
const isButtonDisabled = computed(() => {
  console.log(form.value.taskParams.excelFile);
  return (
    !form.value.taskParams.startData ||
    !form.value.taskParams.startColumn ||
    !form.value.taskParams.excelFile
  );
});
// Get column data
const parseExcel = async (id) => {
  if (!form.value.taskParams.startData) {
    ElMessage.warning(td("dpp.integration.parseFailedAddStartRow", "Parse failed, please add start row"));
    return;
  }
  if (!form.value.taskParams.startColumn) {
    ElMessage.warning(td("dpp.integration.parseFailedAddStartColumn", "Parse failed, please add start column"));
    return;
  }
  if (!form.value.taskParams.excelFile) {
    ElMessage.warning(td("dpp.integration.parseFailedAddAttachment", "Parse failed, please add attachment"));
    return;
  }
  loadingList.value = true;
  try {
    let res = await getExcelColumn({
      startData: form.value.taskParams.startData,
      startColumn: form.value.taskParams.startColumn,
      excelFile: form.value.taskParams.excelFile,
      taskType: props.taskType,
    });

    if (res?.data?.csvFile) {
      form.value.taskParams.csvFile = res.data.csvFile;
      ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
        id: index,
        columnName: item,
        columnType: "string",
      }));

      ElMessage.success(td("dpp.integration.excelParseSuccess", "Excel parsed successfully, please confirm attribute field types!"));
    } else {
      ElMessage.warning(td("dpp.integration.excelParseFailedNoData", "Excel parse failed, no valid data obtained!"));
    }
  } catch (error) {
    if (response.code == 200)
      ElMessage.warning(td("dpp.integration.excelParseFailedCheckFile", "Excel parse failed, please check file format or content!"));
  } finally {
    loadingList.value = false;
  }
};

const off = () => {
  proxy.resetForm("dpModelRefs");
  // Clear table field data
  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// save data
const saveData = async () => {
  try {
    // Asynchronous validation form
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;
    if (
      form.value?.taskParams.type == "1" &&
      (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
    ) {
      return proxy.$message.warning(td("dpp.integration.validateFailedSelectFields", "Validation failed, please select attribute fields"));
    }
    // If there is no code, call the interface to get the unique code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false; // end loading state
      form.value.code = response.data; // Set unique code
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
  // Close dialog
  emit("update", false);
};

// Listen for property changes
function deepCopy(data) {
  if (data === undefined || data === null) {
    return {}; // Or return a default value
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    return {}; // Or return a default value
  }
}
// Listen for property changes
watchEffect(() => {
  if (props.visible) {
    // data source
    form.value = deepCopy(props.currentNode.data);
    ColumnByAssettab.value = props.currentNode?.data.taskParams.tableFields;
  } else {
    off();
  }
});
// File deletion
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
