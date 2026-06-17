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
  <el-dialog
    v-model="visibleDialog"
    :draggable="true"
    class="medium-dialog"
    :title="currentNode?.data?.name"
    showCancelButton
    :show-close="false"
    destroy-on-close
    :close-on-click-modal="false"
  >
    <el-form
      ref="dpModelRefs"
      :model="form"
      label-width="110px"
      @submit.prevent
      v-loading="loading"
      :disabled="info"
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.nodeName', '节点名称')"
            prop="name"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.nodeNameRequired',
                  '请输入节点名称'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-model="form.name"
              :placeholder="
                td('dpp.integration.nodeNamePlaceholder', '请输入节点名称')
              "
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.type', '类型')"
            prop="typeName"
           :label-position="labelPosition">
            <el-select
              v-model="form.taskParams.typeName"
              :placeholder="td('dpp.integration.typePlaceholder', '请输入类型')"
              filterable
              disabled
            >
              <el-option
                v-for="dict in typeList"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            :label="td('common.texts.description')"
            prop="description"
           :label-position="labelPosition">
            <el-input
              v-model="form.description"
              type="textarea"
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.uploadAttachment', '上传附件')"
            prop="taskParams.file"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.uploadAttachmentRequired',
                  '请上传附件'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <FileUploadbtn
              :limit="1"
              v-model="form.taskParams.file"
              :dragFlag="false"
              :file-type="['csv']"
              :fileSize="50"
              @handleRemove="handleRemove"
              :showDelete="!info"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-button
            type="primary"
            plain
            @click="parseExcel"
            style="margin-left: 60px"
            :disabled="isButtonDisabled"
          >
            {{ td("dpp.integration.parseCsv", "解析csv") }}
          </el-button>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">{{
          td("dpp.integration.attributeFields", "属性字段")
        }}</span>
      </el-divider>
      <!-- <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="parseExcel"
              v-hasPermi="['dpp:etl:etltask:add']"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i> 解析Excel
            </el-button>
          </el-col>
        </el-row>
      </div> -->
      <el-table
        stripe
        height="310px"
        v-loading="loadingList"
        :data="ColumnByAssettab"
      >
        <el-table-column
          :label="td('common.display.index', '序号')"
          type="index"
          width="80"
          align="left"
        >
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.integration.fieldName', '字段名称')"
          align="left"
          prop="columnName"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.columnName || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.integration.fieldType', '字段类型')"
          align="left"
          prop="columnType"
        >
          <template #default="scope">
            {{ scope.row.columnType || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.integration.dateFormat', '日期格式')"
          align="left"
          prop="format"
        >
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
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="openDialog(scope.row)"
              >{{ td("common.button.update") }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{
          td("common.button.close")
        }}</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">{{
          td("common.button.save")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
  <excelUploadDialog
    :visible="open"
    :title="td('dpp.integration.attributeFieldEdit', '属性字段编辑')"
    @update:visible="open = $event"
    @confirm="handletaskConfig"
    :data="row"
  />
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { getToken } from "@/utils/auth.js";
import { typeList } from "@/utils/graph.js";
import {
  getNodeUniqueKey,
  getExcelColumn,
  getCsvColumn,
} from "@/api/dpp/task/index.js";
import excelUploadDialog from "../excelUpload.vue";
import FileUploadbtn from "@/components/FileUploadbtn/index1.vue";
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user.js";

const { td } = useDefaultLang();
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
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
const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + "/upload"); // 上传文件服务器地址
/*** 用户导入参数 */
const upload = reactive({
  // 是否禁用上传
  isUploading: false,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
});
// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
const handletaskConfig = (form) => {
  // 找到对应的 id 并更新 ColumnByAssettab 中的相应项
  ColumnByAssettab.value = ColumnByAssettab.value.map((column) => {
    if (column.id == form.id) {
      // 更新匹配 id 的项
      return { ...column, ...form }; // 或者根据需要做其他的合并方式
    }
    return column; // 对于不匹配的项，保持不变
  });
};

let dpModelRefs = ref();
let form = ref({});
const tableFields = ref([]); // 来源表格
// 计算属性：判断按钮是否禁用
const isButtonDisabled = computed(() => {
  return !form.value.taskParams.file;
});
// 获取列数据
const parseExcel = async (id) => {
  if (!form.value.taskParams.file) {
    ElMessage.warning(
      td("dpp.integration.csvParseFailedAddAttachment")
    );
    return;
  }

  loading.value = true; // Assuming 'loading' is a global loading state variable
  try {
    let res = await getCsvColumn({
      file: form.value.taskParams.file,
    });

    if (res?.data?.csvFile) {
      form.value.taskParams.csvFile = res.data.csvFile;
      ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
        id: index,
        columnName: item,
        columnType: "string",
      }));
      ElMessage.success(
        td("dpp.integration.csvParseSuccess")
      );
    } else {
      ElMessage.warning(
        td("dpp.integration.csvParseFailedNoData")
      );
    }
  } catch (error) {
    ElMessage.warning(
      td("dpp.integration.csvParseError")
    );
    console.error(error);
  } finally {
    loading.value = false; // Ensure loading is turned off regardless of success or failure
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
      return proxy.$message.warning(
        td("dpp.integration.validateFailedSelectFields")
      );
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
    taskParams.columnsList = ColumnByAssettab.value.map(
      ({ columnName, columnType }) => ({
        colName: columnName,
        dataType: columnType,
      })
    );
    taskParams.columns = taskParams.tableFields.map((item) => {
      return {
        index: item.id,
        columnName: item.columnName,
        type: item.columnType,
        format: item.format,
      };
    });
    emit("confirm", form.value);
  } catch (error) {
    console.error("保存数据失败:", error);
    loading.value = false; // 确保错误发生时也结束加载状态
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
// 上传前校验文件类型
function handleBeforeUpload(file) {
  // 校检文件类型
  let fileType = ["csv"];
  const fileName = file.name.split(".");
  const fileExt = fileName[fileName.length - 1];
  const isTypeOk = fileType.indexOf(fileExt) >= 0;
  if (!isTypeOk) {
    proxy.$modal.msgWarning(
      td(
        "dpp.integration.fileFormatIncorrect",
        "文件格式不正确, 请上传csv格式文件!"
      )
    );
    return false;
  }
  // 校验文件大小
  const maxSize = 50; // 最大文件大小，单位MB
  const fileSize = file.size / 1024 / 1024;
  if (fileSize > maxSize) {
    proxy.$modal.msgWarning(td('dpp.integration.fileSizeExceeded', '', { maxSize }));
    return false;
  }
  return true;
}

// 文件删除
function handleRemove() {
  ColumnByAssettab.value = [];
  form.value.taskParams.file = undefined;
}
</script>
<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

.upload-file {
  width: 100%;
}

.upload-file-uploader {
  margin-bottom: 5px;
}

.filelistcont {
  display: flex;
  align-items: center;

  .filelistcont-name {
    margin-right: 10px;
  }
}
</style>
