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
      label-width="140px"
      @submit.prevent
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('components.catEditDialog.parentIdLabel')" prop="parentId" :label-position="labelPosition">
            <el-tree-select
              filterable
              v-model="form.parentId"
              :data="treeOptions"
              :props="{ value: 'id', label: 'name', children: 'children' }"
              value-key="id"
              :placeholder="t('components.catEditDialog.parentIdPlaceholder')"
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
      <el-col :span="24">
        <el-form-item :label="t('common.texts.status')" prop="validFlag" :label-position="labelPosition">
          <el-radio v-model="form.validFlag" :label="true">{{ t('components.catEditDialog.enable') }}</el-radio>
          <el-radio v-model="form.validFlag" :label="false">{{ t('components.catEditDialog.disable') }}</el-radio>
        </el-form-item>
      </el-col>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('components.catEditDialog.sortOrder')" prop="sortOrder" :label-position="labelPosition">
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
          <el-form-item :label="t('common.texts.description')" prop="description" :label-position="labelPosition">
            <el-input
              type="textarea"
              :maxlength="500"
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
          <el-form-item :label="t('common.texts.remark')" prop="remark" :label-position="labelPosition">
            <el-input
              type="textarea"
              :maxlength="500"
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
        <el-button @click="onCancel">{{ t('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="onSubmit">{{ t('common.button.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'

/**
 * CatEditDialog component usage instructions
 *
 * This component is used for editing and adding operations to categories.
 * No longer use props to pass data, but call it through the exposed open method.
 *
 * How to use:
 * 1. Introduce and place the component in the parent component:
 *    <CatEditDialog ref="catEditDialogRef" @submit="handleDialogSubmit" />
 *
 * 2. Define ref in script:
 *    const catEditDialogRef = ref();
 *
 * 3. Call the open method to open the pop-up window:
 *    catEditDialogRef.value.open({
 * title: "New Category", // Pop-up window title
 * nameLabel: "Category name", // label of the name field
 * treeOptions: [...], // Parent category tree data
 * form: { ... }, // Initial data of the form (if it is modified, pass in the current row data; if it is added, pass in the default value or part of the preset value)
 * rules: { ... } // (optional) form validation rules, if not passed, the default rules will be used
 *    });
 *
 * 4. Listen to the @submit event to get the results:
 *    const handleDialogSubmit = (formData) => {
 * // Call the interface to save formData
 * // There is no need to manually close the pop-up window after successful saving. The pop-up window will automatically close after clicking OK and passing the verification (or adjust according to business needs)
 * // Note: The current implementation is to automatically close the pop-up window and emit submit after the verification is passed.
 *    };
 */

import { ref, reactive, computed, nextTick } from "vue";

const { t } = useI18n();
const emit = defineEmits(["submit", "cancel"]);

const visible = ref(false);
const formRef = ref();

// Component internal state
const title = ref("");
const nameLabel = ref(t('components.catEditDialog.nameLabel'));
const treeOptions = ref([]);
const customRules = ref(null);

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
const effectiveNamePlaceholder = computed(() => t('components.catEditDialog.namePlaceholder', { nameLabel: nameLabel.value }));

// Default validation rules
const defaultRules = {
  name: [{ required: true, message: t('common.form.nameRequired'), trigger: "blur" }],
  parentId: [{ required: true, message: t('components.catEditDialog.parentIdRequired'), trigger: "blur" }],
  code: [{ required: true, message: t('components.catEditDialog.codeRequired'), trigger: "blur" }],
};

// Calculate the final rules used, giving priority to the passed customRules
const currentRules = computed(() => {
  if (customRules.value) {
    return customRules.value;
  }
  // Dynamically update the message in the default rule
  const rules = JSON.parse(JSON.stringify(defaultRules));
  if (rules.name && rules.name[0]) {
    rules.name[0].message = `${nameLabel.value}不能为空`;
  }
  return rules;
});

/**
 * How to open a pop-up window
 * @param {Object} options configuration items
 */
const open = (options = {}) => {
  title.value = options.title || t('components.catEditDialog.editTitle');
  nameLabel.value = t('components.catEditDialog.nameLabel');
  treeOptions.value = options.treeOptions || [];
  customRules.value = options.rules || null;

  // Initialize form data
  // If form is passed in, it will be merged into defaultForm (deep copy to avoid reference problems)
  // Note: This assumes that options.form contains the data that needs to be echoed
  if (options.form) {
    form.value = JSON.parse(
      JSON.stringify({ ...defaultForm, ...options.form })
    );
  } else {
    form.value = JSON.parse(JSON.stringify(defaultForm));
  }

  visible.value = true;

  // Reset verification status
  nextTick(() => {
    formRef.value?.clearValidate();
  });
};

const onCancel = () => {
  visible.value = false;
  emit("cancel");
};

const onSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      emit("submit", JSON.parse(JSON.stringify(form.value)));
      visible.value = false;
    }
  });
};

// Expose the open method to the parent component
defineExpose({
  open,
});
</script>
