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
  <!-- Mark the new label pop-up window -->
  <el-dialog
    :title="td('dpp.asset.add.tag.addTag')"
    v-model="visible"
    width="800px"
    :append-to="$parent?.$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ td('dpp.asset.add.tag.addTag') }}
      </span>
    </template>
    <el-form
      ref="AttTagRef"
      :model="formData"
      :rules="rules"
      label-width="110px"
      @submit.prevent
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dpp.asset.add.tag.tagName')" prop="name" :label-position="labelPosition">
            <el-input v-model="formData.name" :placeholder="td('dpp.asset.add.tag.tagNamePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dpp.asset.add.tag.tagCategory')" prop="catCode" :label-position="labelPosition">
            <el-tree-select
              filterable
              v-model="formData.catCode"
              :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }"
              value-key="ID"
              :placeholder="td('dpp.asset.add.tag.tagCategoryPlaceholder')"
              check-strictly
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
            <el-input
              v-model="formData.description"
              type="textarea"
              maxlength="256字符"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dpp.asset.add.tag.nearSynonyms')" prop="nearSynonyms" :label-position="labelPosition">
            <el-input
              v-model="formData.nearSynonyms"
              :placeholder="td('dpp.asset.add.tag.nearSynonymsPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dpp.asset.add.tag.synonyms')" prop="synonyms" :label-position="labelPosition">
            <el-input
              v-model="formData.synonyms"
              :placeholder="td('dpp.asset.add.tag.synonymsPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
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
          <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
            <el-input
              v-model="formData.remark"
              type="textarea"
              maxlength="256字符"
              show-word-limit
              :placeholder="td('common.form.remarkPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" size="mini" @click="submitForm"
          >{{ td('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
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

const { td } = useDefaultLang();
// Define component properties
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
        name: td('dpp.asset.add.tag.tagCategoryRoot'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
getDeptTree();
const rules = {
  name: [{ required: true, message: td('dpp.asset.add.tag.tagNameRequired'), trigger: "blur" }],
  catCode: [
    { required: true, message: td('dpp.asset.add.tag.tagCategoryRequired'), trigger: "change" },
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
        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
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
