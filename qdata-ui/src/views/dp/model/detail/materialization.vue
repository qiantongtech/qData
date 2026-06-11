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
  <!--  逻辑发布的弹窗  -->
  <el-dialog
    v-model="localVisible"
    :title="title"
    draggable
    class="warn-dialog"
    destroy-on-close
  >
    <!-- <div class="centered-text">
      您将对选择的{{
        ids?.length
      }}个逻辑模型进行逻辑发布，请选择数据资产的数据连接
    </div> -->
    <el-form
      ref="dpModelRefs"
      :model="form"
      :rules="rules"
      label-width="100px"
      @submit.prevent
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            label="数据库连接"
            prop="datasourceId"
            :rules="[
              {
                required: true,
                message: '请选择数据库连接',
                trigger: 'change',
              },
            ]"
          >
            <DatasourceList
              v-model="form.datasourceId"
              placeholder="请选择数据连接"
              @change="handleDatasourceChange"
              filterable
              flag="dpModel"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="数据库类型" prop="datasourceType">
            <el-input
              v-model="form.datasourceType"
              placeholder="请选择数据库类型"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="数据库地址" prop="ip">
            <el-input
              v-model="form.ip"
              placeholder="请输入数据库地址"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="发布模式" prop="releaseMode">
            <el-radio-group v-model="form.releaseMode">
              <el-radio label="1">删除重建</el-radio>
              <el-radio label="2">增量发布</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.description')">
            <el-input
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="t('common.form.descriptionPlaceholder')"
              v-model="form.description"
              :min-height="192"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.remark')">
            <el-input
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="t('common.form.remarkPlaceholder')"
              v-model="form.remark"
              :min-height="192"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">{{ t('common.button.cancel') }}</el-button>
        <el-button type="primary" :loading="loading" @click="confirmDialog">
          {{ loading ? "发布中" : "确认" }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import {
  createMaterializedTable,
  getDaDatasourceList,
} from "@/api/dp/model/model";
import {
  defineProps,
  defineEmits,
  ref,
  computed,
  watch,
  getCurrentInstance,
} from "vue";
import DatasourceList from "@/components/Datasource/List.vue";
import {
  getDatasourceData,
  getAvailableDatasource,
} from "@/components/Datasource/utils.js";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "发布模型" },
  ids: { type: Array, default: () => [] },
});
let createTypeList = ref();
// 监听 `visible` 的变化
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      getDaDatasourceListList();
    }
  }
);

const getDaDatasourceListList = async () => {
  try {
    const response = await getDatasourceData();
    createTypeList.value = getAvailableDatasource(response, "dpModel");
  } catch (error) {
    console.error("请求失败:", error);
  }
};

const emit = defineEmits(["update:dialogFormVisible", "confirm"]);

// 处理弹窗显示状态
const localVisible = computed({
  get() {
    return props.visible;
  },
  set(value) {
    emit("update:dialogFormVisible", value);
  },
});

const handleDatasourceChange = (value, selectedDatasource) => {
  if (selectedDatasource) {
    form.value.ip = selectedDatasource.ip;
    form.value.datasourceConfig = selectedDatasource.datasourceConfig;
    form.value.datasourceType = selectedDatasource.datasourceType;
    form.value.datasourceName = selectedDatasource.datasourceName;
    form.value.port = selectedDatasource.port;
  }
};

const form = ref({
  datasourceId: "",
  datasourceType: "",
  ip: "",
  datasourceConfig: "",
  port: "",
  datasourceName: "",
  releaseMode: "1",
});

const rules = ref({
  datasourceId: [
    { required: true, message: "请选择数据连接", trigger: "blur" },
  ],
  releaseMode: [
    { required: true, message: "请选择发布模式", trigger: "change" },
  ],
});

const loading = ref(false);

const closeDialog = () => {
  form.value = {
    datasourceId: "",
    datasourceType: "",
    ip: "",
    datasourceConfig: "",
    port: "",
    datasourceName: "",
    releaseMode: "1",
  };
  localVisible.value = false;
  proxy.resetForm("dpModelRefs");
};
const confirmDialog = async () => {
  try {
    const isValid = await new Promise((resolve, reject) => {
      proxy.$refs["dpModelRefs"].validate((valid) => {
        if (valid) {
          resolve(true);
        } else {
          reject("表单验证失败");
        }
      });
    });

    if (isValid) {
      const confirmMessage =
        form.value.releaseMode === "1"
          ? "确认选择删除重建吗？ 将清空原表数据并重新创建物理表结构。此操作不可逆转，请谨慎操作！"
          : "确认选择增量发布吗？保留原表数据，仅追加新增的字段列。适用于非破坏性的安全变更。";

      await proxy.$modal.confirm(confirmMessage, t('common.message.systemPrompt'), {
        confirmButtonText: "确认",
        cancelButtonText: t('common.button.cancel'),
        type: "warning",
      });

      if (form.value.releaseMode === "2") {
        const supportedTypes = ["oracle11", "mysql", "DM8"];
        if (!supportedTypes.includes(form.value.datasourceType)) {
          proxy.$message.warning(
            "增量发布暂只支持oracle、MySqL、DM8这三种类型"
          );
          loading.value = false;
          return;
        }
      }
      loading.value = true;
      try {
        // 创建发布表格
        const response = await createMaterializedTable({
          modelId: props.ids,
          ...form.value,
        });
        console.log(response);

        // 提交数据
        emit("confirm", form.value);

        // 关闭对话框
        closeDialog();
        // 提示成功
        proxy.$modal.msgSuccess(response.msg);
      } catch (error) {
        console.error(error);
      } finally {
        loading.value = false;
      }
    }
  } catch (error) {}
};
</script>

<style scoped lang="less">
.warn-dialog .el-dialog__body {
  max-height: 500px;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}

.dialog-footer .el-button {
  margin-left: 10px;
}

.centered-text {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 10%;
  text-align: center;
  font-size: 14px;
  color: #333;
}
</style>
