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
  <el-dialog
    v-model="visible"
    :title="title"
    width="800px"
    draggable
    destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="currentRules"
      label-width="150px"
      @submit.prevent
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('att.common.parentCat')" prop="parentId" :label-position="labelPosition">
            <el-tree-select
              filterable
              v-model="form.parentId"
              :data="treeOptions"
              :props="{ value: 'id', label: 'name', children: 'children' }"
              value-key="id"
              :placeholder="td('att.common.parentCatPlaceholder')"
              check-strictly
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="effectiveNameLabel" prop="name" :label-position="labelPosition">
            <el-input
              v-model="form.name"
              :placeholder="effectiveNamePlaceholder"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-col :span="24" v-if="!hideStatusAndSort">
        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
          <el-radio v-model="form.validFlag" :label="true">{{ td('att.common.enable') }}</el-radio>
          <el-radio v-model="form.validFlag" :label="false">{{ td('att.common.disable') }}</el-radio>
        </el-form-item>
      </el-col>
      <el-row :gutter="20" v-if="!hideStatusAndSort">
        <el-col :span="24">
          <el-form-item :label="td('att.common.sortOrder')" prop="sortOrder" :label-position="labelPosition">
            <el-input-number
              style="width: 100%"
              v-model="form.sortOrder"
              controls-position="right"
              :min="0"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
            <el-input
              type="textarea"
              maxlength="256字符"
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
          <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
            <el-input
              type="textarea"
              maxlength="256字符"
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
        <el-button @click="onCancel">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="onSubmit" :loading="loading">{{ td('common.button.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'

/**
 * CatEditDialog component usage guide
 *
 * This component handles category (Category) edit and add operations.
 * Instead of passing data via props, it is called through the exposed open method.
 *
 * Usage:
 * 1. Import and place the component in the parent:
 *    <CatEditDialog ref="catEditDialogRef" @submit="handleDialogSubmit" />
 *
 * 2. Define ref in script:
 *    const catEditDialogRef = ref();
 *
 * 3. Call open method to show the dialog:
 *    catEditDialogRef.value.open({
 *      title: "Add Category",          // Dialog title
 *      nameLabel: "Category Name",     // Label for the name field
 *      treeOptions: [...],             // Parent category tree data
 *      form: { ... },                  // Initial form data (pass current row data for edit, or defaults for add)
 *      rules: { ... }                  // (Optional) Validation rules, uses defaults if not provided
 *    });
 *
 * 4. Listen to @submit event for results:
 *    const handleDialogSubmit = (formData) => {
 *      // Call API to save formData
 *      // No need to manually close the dialog after saving — it auto-closes on successful validation
 *      // Note: current implementation auto-closes and emits submit after validation passes
 *    };
 */

import { ref, computed, nextTick } from "vue";
import { ElMessageBox } from "element-plus";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { t } = useI18n();
const emit = defineEmits(["submit", "cancel"]);

const visible = ref(false);
const loading = ref(false);
const formRef = ref();

// Component internal state
const title = ref("");
const nameLabel = ref(td('att.common.categoryName'));
const treeOptions = ref([]);
const customRules = ref(null);
const dialogType = ref("");
const oldParentId = ref();

// Default form data
const defaultForm = {
  parentId: undefined,
  name: "",
  validFlag: true,
  sortOrder: 0,
  description: "",
  remark: "",
};

const form = ref({ ...defaultForm });

const effectiveNameLabel = computed(() => nameLabel.value);
const effectiveNamePlaceholder = computed(() => td('att.common.namePlaceholder'));
const hideStatusAndSort = computed(() => dialogType.value === "dataCategory");

// Default validation rules
const defaultRules = {
  name: [{ required: true, message: td('common.form.nameRequired'), trigger: "blur" }],
  parentId: [{ required: true, message: td('att.common.parentCatRequired'), trigger: "blur" }],
  code: [{ required: true, message: td('att.common.codeRequired'), trigger: "blur" }],
};

// Compute final rules to use, preferring passed-in customRules
const currentRules = computed(() => {
  const rules = customRules.value
    ? Object.fromEntries(Object.entries(customRules.value).map(([key, value]) => [key, [...value]]))
    : JSON.parse(JSON.stringify(defaultRules));
  if (rules.name && rules.name[0]) {
    rules.name[0].message = td('att.common.nameRequired', { name: nameLabel.value });
  }
  rules.name = [
    ...(rules.name || []),
    {
      validator: (_rule, value, callback) => {
        const name = String(value || "");
        if (/\s/.test(name)) return callback(new Error('类目名称不能包含空白字符'));
        if (!/[A-Za-z0-9\u4e00-\u9fa5]/.test(name)) return callback(new Error('类目名称不能仅由符号组成'));
        callback();
      },
      trigger: "blur",
    },
    { max: 50, message: '类目名称长度不能超过50个字符', trigger: "blur" },
  ];
  rules.sortOrder = [
    {
      validator: (_rule, value, callback) => {
        if (!Number.isInteger(Number(value)) || Number(value) < 0) {
          callback(new Error('排序值不合法，请输入非负整数'));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ];
  return rules;
});

/**
 * Open dialog method
 * @param {Object} options Configuration options
 */
const open = (options = {}) => {
  title.value = options.title || td('att.common.edit');
  nameLabel.value = options.nameLabel || td('att.common.categoryName');
  treeOptions.value = options.treeOptions || [];
  customRules.value = options.rules || null;
  dialogType.value = options.type || "";

  // Initialize form data
  // If form is provided, merge into defaultForm (deep copy to avoid reference issues)
  // Note: this assumes options.form contains the data to populate
  if (options.form) {
    form.value = JSON.parse(
      JSON.stringify({ ...defaultForm, ...options.form })
    );
    oldParentId.value = form.value.parentId;
  } else {
    form.value = JSON.parse(JSON.stringify(defaultForm));
    oldParentId.value = form.value.parentId;
  }

  visible.value = true;

  // Reset validation state
  nextTick(() => {
    formRef.value?.clearValidate();
  });
};

const onCancel = () => {
  visible.value = false;
  emit("cancel");
};

const onSubmit = () => {
  const submit = () => formRef.value?.validate((valid) => {
    if (valid) {
      loading.value = true;
      emit("submit", JSON.parse(JSON.stringify(form.value)));
    }
  });
  if (form.value.id && form.value.parentId !== oldParentId.value) {
    ElMessageBox.confirm(
      '修改上级类目会影响该类目下任务的归属路径，请确认。',
      '系统提示',
      { type: 'warning' }
    ).then(submit).catch(() => {});
    return;
  }
  submit();
};

const close = () => {
  visible.value = false;
  loading.value = false;
};

const stopLoading = () => {
  loading.value = false;
};

// Expose open method to parent component
defineExpose({
  open,
  close,
  stopLoading,
});
</script>
