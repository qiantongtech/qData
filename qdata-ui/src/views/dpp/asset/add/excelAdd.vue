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
  <!-- 矢量数据 -->
  <div v-if="visible">
    <template
        v-if="
        form.daAssetFiles.url != undefined &&
        form.daAssetFiles.url.indexOf('.xls') == -1 &&
        form.daAssetFiles.url.indexOf('.xlsx') == -1
      "
    >
      <el-form-item
          :label="td('dpp.asset.add.excel.uploadAttachment')"
          prop="daAssetFiles.url"
          :rules="[{ required: true, message: td('dpp.asset.add.excel.uploadAttachmentRequired'), trigger: 'change' }]"
      >
        <FileUploadbtn
            :limit="1"
            v-model="form.daAssetFiles.url"
            :dragFlag="false"
            :file-type="[]"
            :fileSize="50"
            @handleRemove="handleRemove"
            @customEvent="dataFileName"
        />
      </el-form-item>
      <div style="display: flex; align-items: center; padding-left: 20px">
        <el-button
            v-if="form.daAssetFiles.url.indexOf('.csv') != -1"
            type="primary"
            plain
            @click="parseExcel"
            :disabled="isButtonDisabled"
        >
          {{ td('dpp.asset.add.excel.parseCsv') }}
        </el-button>
      </div>
    </template>
    <template
        v-if="
        form.daAssetFiles.url == undefined ||
        form.daAssetFiles.url.indexOf('.xls') != -1 ||
        form.daAssetFiles.url.indexOf('.xlsx') != -1
      "
    >
      <el-form-item
          :label="td('dpp.asset.add.excel.uploadAttachment')"
          prop="daAssetFiles.url"
          :rules="[{ required: true, message: td('dpp.asset.add.excel.uploadAttachmentRequired'), trigger: 'change' }]"
      >
        <FileUploadbtn
            :limit="1"
            v-model="form.daAssetFiles.url"
            :dragFlag="false"
            :file-type="[]"
            :fileSize="50"
            @handleRemove="handleRemove"
            @customEvent="dataFileName"
        />
      </el-form-item>
      <el-form-item
          :label="td('dpp.asset.add.excel.startRow')"
          prop="daAssetFiles.startData"
          :rules="[{ required: true, message: td('dpp.asset.add.excel.startRowRequired'), trigger: 'change' }]"
      >
        <el-input-number
            :step="1"
            step-strictly
            :placeholder="td('dpp.asset.add.excel.startRowPlaceholder')"
            v-model="form.daAssetFiles.startData"
            style="width: 100%"
            controls-position="right"
            :min="1"
            value-on-clear="min"
        />
      </el-form-item>
    </template>
    <template
        v-if="
        form.daAssetFiles.url == undefined ||
        form.daAssetFiles.url.indexOf('.xls') != -1 ||
        form.daAssetFiles.url.indexOf('.xlsx') != -1
      "
    >
      <el-form-item
          :label="td('dpp.asset.add.excel.startColumn')"
          prop="daAssetFiles.startColumn"
          :rules="[{ required: true, message: td('dpp.asset.add.excel.startColumnRequired'), trigger: 'change' }]"
      >
        <el-input-number
            :step="1"
            step-strictly
            :placeholder="td('dpp.asset.add.excel.startColumnPlaceholder')"
            v-model="form.daAssetFiles.startColumn"
            style="width: 100%"
            controls-position="right"
            :min="1"
            value-on-clear="min"
        />
      </el-form-item>
      <div style="display: flex; align-items: center; padding-left: 20px">
        <el-button
            type="primary"
            plain
            @click="parseExcel"
            :disabled="isButtonDisabled"
        >
          {{ td('dpp.asset.add.excel.parseExcel') }}
        </el-button>
      </div>
    </template>
    <el-divider
        class="row-full"
        content-position="center"
        v-if="
        form.daAssetFiles.url &&
        (form.daAssetFiles.url.indexOf('.csv') != -1 ||
          form.daAssetFiles.url.indexOf('.xls') != -1 ||
          form.daAssetFiles.url.indexOf('.xlsx') != -1)
      "
    >
      <span class="blue-text">{{ td('dpp.asset.add.excel.attributeFields') }}</span>
    </el-divider>
    <el-table
        class="row-full"
        stripe
        height="310px"
        v-loading="loadingList"
        :data="ColumnByAssettab"
        v-if="
        form.daAssetFiles.url &&
        (form.daAssetFiles.url.indexOf('.csv') != -1 ||
          form.daAssetFiles.url.indexOf('.xls') != -1 ||
          form.daAssetFiles.url.indexOf('.xlsx') != -1)
      "
    >
      <el-table-column :label="td('dpp.asset.add.excel.index')" type="index" width="80" align="left">
        <template #default="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
          :label="td('dpp.asset.add.excel.fieldName')"
          align="left"
          prop="columnName"
          :show-overflow-tooltip="{ effect: 'light' }"
      >
        <template #default="scope">
          {{ scope.row.columnName || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="td('dpp.asset.add.excel.fieldType')" align="left" prop="columnType">
        <template #default="scope">
          {{ scope.row.columnType || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="td('dpp.asset.add.excel.dateFormat')" align="left" prop="format">
        <template #default="scope">
          {{ scope.row.format || "-" }}
        </template>
      </el-table-column>
      <el-table-column
          :label="td('common.texts.operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="240"
      >
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="openDialog(scope.row)"
          >{{ td('common.button.update') }}</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { getToken } from '@/utils/auth.js';
import { typeList } from '@/utils/graph.js';
import { getNodeUniqueKey, getExcelColumn, getCsvColumn } from '@/api/dpp/task/index.js';
const { proxy } = getCurrentInstance();
import useUserStore from '@/store/system/user.js';

const { td } = useDefaultLang();
const userStore = useUserStore();
const props = defineProps({
  title: { type: String, default: '' },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  objData: { type: Object, default: () => ({}) }
});
const emit = defineEmits(['update', 'confirm']);
const visible = ref(false);
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
let form = ref();
const tableFields = ref([]); // 来源表格
// 计算属性：判断按钮是否禁用
const isButtonDisabled = computed(() => {
  console.log(form.value.daAssetFiles.url);
  return (
      !form.value.daAssetFiles.startData ||
      !form.value.daAssetFiles.startColumn ||
      !form.value.daAssetFiles.url
  );
});
// 获取列数据
const parseExcel = async (id) => {
  if (form.value.daAssetFiles.url.indexOf('.csv') != -1) {
    if (!form.value.daAssetFiles.url) {
      ElMessage.warning(td('dpp.asset.add.excel.addAttachment'));
      return;
    }

    loading.value = true; // Assuming 'loading' is a global loading state variable
    try {
      let res = await getCsvColumn({
        file: form.value.daAssetFiles.url
      });

      if (res?.data?.csvFile) {
        form.value.daAssetFiles.csvFile = res.data.csvFile;
        ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
          id: index,
          columnName: item,
          columnType: 'string'
        }));
        ElMessage.success(td('dpp.asset.add.excel.csvParseSuccess'));
      } else {
        ElMessage.warning(td('dpp.asset.add.excel.csvParseFailed'));
      }
    } catch (error) {
      ElMessage.warning(td('dpp.asset.add.excel.parseError'));
      console.error(error);
    } finally {
      loading.value = false; // Ensure loading is turned off regardless of success or failure
    }
  } else {
    if (!form.value.daAssetFiles.startData) {
      ElMessage.warning(td('dpp.asset.add.excel.addStartRow'));
      return;
    }
    if (!form.value.daAssetFiles.startColumn) {
      ElMessage.warning(td('dpp.asset.add.excel.addStartColumn'));
      return;
    }
    if (!form.value.daAssetFiles.url) {
      ElMessage.warning(td('dpp.asset.add.excel.addAttachment'));
      return;
    }
    loadingList.value = true;
    try {
      let res = await getExcelColumn({
        startData: form.value.daAssetFiles.startData,
        startColumn: form.value.daAssetFiles.startColumn,
        excelFile: form.value.daAssetFiles.url
      });

      if (res?.data?.csvFile) {
        form.value.daAssetFiles.csvFile = res.data.csvFile;
        ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
          id: index,
          columnName: item,
          columnType: 'string'
        }));

        ElMessage.success(td('dpp.asset.add.excel.excelParseSuccess'));
      } else {
        ElMessage.warning(td('dpp.asset.add.excel.excelParseFailed'));
      }
    } catch (error) {
      if (response.code == 200) ElMessage.warning(td('dpp.asset.add.excel.excelParseFailedCheck'));
    } finally {
      loadingList.value = false;
    }
  }
};

const off = () => {
  proxy.resetForm('dpModelRefs');
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
        form.value?.daAssetFiles.type == '1' &&
        (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
    ) {
      return proxy.$message.warning(td('dpp.asset.add.excel.validateSelectFields'));
    }
    // 如果没有 code，就调用接口获取唯一的 code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || '133545087166112',
        projectId: userStore.projectId
      });
      loading.value = false; // 结束加载状态
      form.value.code = response.data; // 设置唯一的 code
    }
    const daAssetFiles = form.value?.daAssetFiles;
    daAssetFiles.tableFields = ColumnByAssettab.value;
    daAssetFiles.columnsList = ColumnByAssettab.value.map(({ columnName, columnType }) => ({
      colName: columnName,
      dataType: columnType
    }));
    daAssetFiles.columns = daAssetFiles.tableFields.map((item) => {
      return {
        index: item.id,
        columnName: item.columnName,
        type: item.columnType,
        format: item.format
      };
    });
    emit('confirm', form.value);
    emit('update', false);
  } finally {
    loadingList.value = false;
  }
};

function dataFileName(file) {
  form.value.daAssetFiles.name = file.originalFilename;
  form.value.daAssetFiles.type = file.ext;
  console.log(form.value, '451561649861');
}
const closeDialog = () => {
  off();
  // 关闭对话框
  emit('update', false);
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
const show = (data) => {
  visible.value = false;
  console.log(data, '文件类型。。。。。。。。。');
  if (data.createType == '2' && data.type == '6') {
    visible.value = true;
    form.value = deepCopy(data);
    ColumnByAssettab.value = data.daAssetFiles.tableFields;
  }
  if (data.id != undefined && data.type == '6') {
    visible.value = true;
    if (data.daAssetFiles == undefined) {
      data.daAssetFiles = {
        url: null,
        startData: '',
        tableFields: [],
        startColumn: ''
      };
    }
    form.value = deepCopy(data);
    ColumnByAssettab.value = data.daAssetFiles.tableFields;
  }
};
// 监听属性变化
// watchEffect(() => {
//   console.log(userStore)
//   if (props.visible) {
//     // 数据源
//     console.log(props.objData,'==========')
//     form.value = deepCopy(props.data);
//     ColumnByAssettab.value = props.data.daAssetFiles.tableFields;
//   } else {
//     off();
//   }
// });
// 文件删除
function handleRemove() {
  ColumnByAssettab.value = [];
  form.value.daAssetFiles.url = undefined;
}
defineExpose({ show, form });
</script>
<style scoped lang="scss">
.blue-text {
  color: #2666fb;
}
</style>
