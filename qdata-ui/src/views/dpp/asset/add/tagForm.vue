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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <!-- 打标新增标签弹窗 -->
  <el-dialog
    title="新增标签"
    v-model="visible"
    width="800px"
    :append-to="$parent?.$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        新增标签
      </span>
    </template>
    <el-form
      ref="AttTagRef"
      :model="formData"
      :rules="rules"
      label-width="110px"
      @submit.prevent
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="标签名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入标签名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="标签管理类目" prop="catCode">
            <el-tree-select
              filterable
              v-model="formData.catCode"
              :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }"
              value-key="ID"
              placeholder="请选择标签管理类目"
              check-strictly
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.description')" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="t('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="近义词" prop="nearSynonyms">
            <el-input
              v-model="formData.nearSynonyms"
              placeholder="请输入标签名称的近义词，多个请用逗号分隔"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="同义词" prop="synonyms">
            <el-input
              v-model="formData.synonyms"
              placeholder="请输入标签名称的同义词，多个请用逗号分隔"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="t('common.texts.status')" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio
                v-for="dict in dp_model_status"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.remark')" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="t('common.form.remarkPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ t('common.button.cancel') }}</el-button>
        <el-button type="primary" size="mini" @click="submitForm"
          >{{ t('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import {
  ref,
  reactive,
  defineProps,
  defineEmits,
  getCurrentInstance,
  computed,
  onMounted,
} from "vue";
import { addAttTag } from "@/api/att/tag/tag.js";

const { proxy } = getCurrentInstance();
const { dp_model_status } = proxy.useDict("dp_model_status");
import { listAttTagCat } from "@/api/att/cat/tagCat/tagCat.js";

const { t } = useI18n();
// 定义组件属性
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: "",
  },
});

let deptOptions = ref([]);

const emit = defineEmits(["update:modelValue", "confirm"]);

const formData = reactive({
  name: null,
  catCode: null,
  description: null,
  nearSynonyms: null,
  synonyms: null,
  status: "1",
  remark: null,
});
function getDeptTree() {
  listAttTagCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "标签管理类目",
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
getDeptTree();
const rules = {
  name: [{ required: true, message: "标签名称不能为空", trigger: "blur" }],
  catCode: [
    { required: true, message: "标签管理类目不能为空", trigger: "change" },
  ],
};

const visible = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit("update:modelValue", val);
  },
});

const AttTagRef = ref();

function cancel() {
  visible.value = false;
  resetForm();
}

function resetForm() {
  Object.assign(formData, {
    name: null,
    catCode: null,
    description: null,
    nearSynonyms: null,
    synonyms: null,
    status: "1",
    remark: null,
  });

  if (AttTagRef.value) {
    AttTagRef.value.resetFields();
  }
}

function submitForm() {
  AttTagRef.value.validate((valid) => {
    if (valid) {
      addAttTag(formData).then((response) => {
        proxy.$modal.msgSuccess(t('common.message.addSuccess'));
        visible.value = false;
        resetForm();
        emit("confirm");
      });
    }
  });
}

defineExpose({
  resetForm,
});
</script>

<style scoped></style>
