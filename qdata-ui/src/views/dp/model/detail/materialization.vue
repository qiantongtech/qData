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
    :title="effectiveTitle"
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
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            :label="td('dp.materializedModel.databaseConnection')"
            prop="datasourceId"
            :rules="[
              {
                required: true,
                message: td('dp.materializedModel.selectDatabaseConnection'),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <DatasourceList
              v-model="form.datasourceId"
              :placeholder="td('dp.materializedModel.selectDatabaseConnection')"
              @change="handleDatasourceChange"
              filterable
              flag="dpModel"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.datasourceType')" prop="datasourceType" :label-position="labelPosition">
            <el-input
              v-model="form.datasourceType"
              :placeholder="td('dp.modelForm.datasourceTypePlaceholder')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.modelForm.datasourceAddress')" prop="ip" :label-position="labelPosition">
            <el-input
              v-model="form.ip"
              :placeholder="td('dp.modelForm.datasourceAddressPlaceholder')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.materializedModel.releaseMode')" prop="releaseMode" :label-position="labelPosition">
            <el-radio-group v-model="form.releaseMode">
              <el-radio label="1">{{ td('dp.materializedModel.deleteAndRecreate') }}</el-radio>
              <el-radio label="2">{{ td('dp.materializedModel.incrementalRelease') }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
            <el-input
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder')"
              v-model="form.description"
              :min-height="192"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
            <el-input
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.remarkPlaceholder')"
              v-model="form.remark"
              :min-height="192"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" :loading="loading" @click="confirmDialog">
          {{ loading ? td('dp.materializedModel.publishing') : td('common.button.confirm') }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
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

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "发布模型" },
  ids: { type: Array, default: () => [] },
});
const effectiveTitle = computed(() => props.title || td('dp.model.materialization.publishModel'));
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
    { required: true, message: td('dp.materializedModel.selectDatabaseConnection'), trigger: "blur" },
  ],
  releaseMode: [
    { required: true, message: td('dp.materializedModel.selectReleaseMode'), trigger: "change" },
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
          ? td('dp.materializedModel.confirmDeleteRecreate')
          : td('dp.materializedModel.confirmIncremental');

      await proxy.$modal.confirm(confirmMessage, td('common.message.systemPrompt'), {
        confirmButtonText: td('common.button.confirm'),
        cancelButtonText: td('common.button.cancel'),
        type: "warning",
      });

      if (form.value.releaseMode === "2") {
        const supportedTypes = ["oracle11", "mysql", "DM8"];
        if (!supportedTypes.includes(form.value.datasourceType)) {
          proxy.$message.warning(
            td('dp.materializedModel.incrementalSupportedTypes')
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
