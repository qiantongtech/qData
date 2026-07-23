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
          <el-form-item :label="td('dm.businessCategory.parentCategory', 'Parent Category')" prop="parentId" :label-position="labelPosition">
            <el-tree-select
              filterable
              v-model="form.parentId"
              :data="treeOptions"
              :props="{ value: 'id', label: 'name', children: 'children' }"
              value-key="id"
              :placeholder="td('dm.businessCategory.parentPlaceholder', 'Please select parent')"
              check-strictly
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.name', 'Business Category Name')" prop="name" :label-position="labelPosition">
            <el-input v-model="form.name" :placeholder="td('dm.businessCategory.namePlaceholder', 'Please enter business category name')" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.engName', 'English Abbreviation')" prop="engName" :label-position="labelPosition">
            <el-input
              v-model="form.engName"
              :placeholder="td('dm.businessCategory.engNamePlaceholder', 'Please enter English abbreviation')"
              @input="handleEngNameInput"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.dataDomain', 'Related Data Domain')" prop="domainIds" :label-position="labelPosition">
            <el-select
              v-model="form.domainIds"
              :placeholder="td('dm.businessCategory.dataDomainIdsPlaceholder', 'Please select related data domain')"
              filterable
              clearable
              multiple
              collapse-tags
              collapse-tags-tooltip
              style="width: 100%"
            >
              <el-option
                v-for="item in dataDomainOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.ownerId', 'Responsible Person')" prop="ownerId" :label-position="labelPosition">
            <el-select
              v-model="form.ownerId"
              filterable
              :placeholder="td('dm.businessCategory.ownerIdPlaceholder', 'Please select responsible person')"
              style="width: 100%"
              @change="handleContactChange"
            >
              <el-option
                v-for="item in managerOptions"
                :key="item.userId"
                :label="item.nickName"
                :value="item.userId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.ownerPhone', 'Responsible Person Phone')" prop="ownerPhone" :label-position="labelPosition">
            <el-input
              v-model="form.ownerPhone"
              :placeholder="td('dm.businessCategory.ownerPhonePlaceholder', 'Please enter responsible person phone')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.status', 'Status')" prop="validFlag" :label-position="labelPosition">
            <el-radio v-model="form.validFlag" :label="false">{{ td('dm.businessCategory.disableText', 'Disable') }}</el-radio>
            <el-radio v-model="form.validFlag" :label="true">{{ td('dm.businessCategory.enableText', 'Enable') }}</el-radio>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description', 'Description')" prop="description" :label-position="labelPosition">
            <el-input
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder', 'Please enter description')"
              v-model="form.description"
              :rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.remark', 'Remark')" prop="remark" :label-position="labelPosition">
            <el-input
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.remarkPlaceholder', 'Please enter remark')"
              v-model="form.remark"
              :rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onCancel">{{ td('common.button.cancel', 'Cancel') }}</el-button>
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >{{ td('common.button.confirm', 'Confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, computed, nextTick, getCurrentInstance } from "vue";
import { deptUserTree } from "@/api/system/system/user";
import { listBusinessCategory } from "@/api/dm/businessCategory/businessCategory";
import { listDataDomain } from "@/api/dm/dataDomain/dataDomain.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const emit = defineEmits(["submit", "cancel"]);

const visible = ref(false);
const loading = ref(false);
const formRef = ref();

// Component internal state
const title = ref("");
const treeOptions = ref([]);
const dataDomainOptions = ref([]);
const customRules = ref(null);
const managerOptions = ref([]);

// Cache original tree data
const rawTreeData = ref([]);

// Default form data
const defaultForm = {
  parentId: 0,
  name: "",
  engName: "",
  domainIds: [],
  domainList: [],
  ownerId: undefined,
  ownerPhone: "",
  validFlag: false,
  sortOrder: 0,
  description: "",
  remark: "",
};

const form = ref({ ...defaultForm });

// Default validation rules
const defaultRules = {
  name: [{ required: true, message: td('dm.businessCategory.nameRequired', 'Business category name cannot be empty'), trigger: "blur" }],
  engName: [
    { required: true, message: td('dm.businessCategory.engNameRequired', 'English abbreviation cannot be empty'), trigger: "blur" },
    { pattern: /^[a-zA-Z]+$/, message: td('dm.dataDomain.englishOnly', 'Only English characters are allowed'), trigger: "blur" },
  ],
  parentId: [{ required: true, message: td('dm.businessCategory.categoryEmpty', 'Parent category cannot be empty'), trigger: "blur" }],
  domainIds: [
    {
      required: true,
      type: "array",
      message: td('dm.businessCategory.dataDomainRequired', 'Please select related data domain'),
      trigger: "change",
    },
  ],
};

// Calculate the final rules used, giving priority to the passed customRules
const currentRules = computed(() => {
  return customRules.value || defaultRules;
});

const getManagerOptions = () => {
  return deptUserTree().then((response) => {
    managerOptions.value = response.data || [];
    if (visible.value && form.value.ownerId && !form.value.ownerPhone) {
      const selectedUser = (managerOptions.value || []).find(
        (user) => user.userId == form.value.ownerId
      );
      form.value.ownerPhone = selectedUser?.phonenumber || "";
    }
  });
};

const handleContactChange = (value) => {
  const selectedUser = (managerOptions.value || []).find(
    (user) => user.userId == value
  );
  form.value.ownerPhone = selectedUser?.phonenumber || "";
};

const handleEngNameInput = (value) => {
  form.value.engName = (value || "").replace(/[^a-zA-Z]/g, "");
};

/** Get a list of data fields */
const getDataDomainOptions = () => {
  return listDataDomain({
    orderByColumn: "create_time",
    isAsc: "descending",
  }).then((response) => {
    dataDomainOptions.value = response?.data?.rows || [];
  });
};

/** Refresh tree data */
const refreshTreeData = () => {
  return listBusinessCategory().then((response) => {
    rawTreeData.value = response?.data || [];
  });
};

/** Processing tree structure display, including filtering logic */
const processTreeData = (excludeId) => {
  let rows = [...rawTreeData.value];
  if (excludeId) {
    rows = rows.filter(
      (d) =>
        d.id !== excludeId &&
        !d.parentId?.toString().split(",").includes(excludeId.toString())
    );
  }
  treeOptions.value = [
    {
      id: 0,
      name: td('common.texts.topNode', 'Top Node'),
      children: proxy.handleTree(rows, "id", "parentId"),
    },
  ];
};

/** Get the superior classification tree */
const getTreeData = (excludeId) => {
  return refreshTreeData().then(() => {
    processTreeData(excludeId);
  });
};

/**
 * How to open a pop-up window
 * @param {Object} options configuration items
 */
const open = (options = {}) => {
  title.value = options.title || td('dm.common.edit', 'Edit');
  customRules.value = options.rules || null;

  // 1. Obtain the associated data fields (reacquire each time it is opened and in reverse chronological order)
  getDataDomainOptions();

  // 2. Get the person in charge
  if (!managerOptions.value?.length) {
    getManagerOptions();
  }

  // 3. Initialize form data
  if (options.form) {
    form.value = JSON.parse(
      JSON.stringify({ ...defaultForm, ...options.form })
    );
    // If domainList exists, initialize domainIds
    if (form.value.domainList && form.value.domainList.length > 0) {
      form.value.domainIds = form.value.domainList.map(
        (item) => item.dataDomainId
      );
    } else if (form.value.domainId) {
      // Compatible with old data
      form.value.domainIds = [form.value.domainId];
    } else if (form.value.domainIds && Array.isArray(form.value.domainIds)) {
      // If domainIds already exists and is an array, there is no need to reset.
      // If the backend returns a string array and options is a number, it is recommended to convert it into a number to ensure echo.
      form.value.domainIds = form.value.domainIds.map((id) =>
        isNaN(Number(id)) ? id : Number(id)
      );
    } else {
      form.value.domainIds = [];
    }
  } else {
    form.value.domainIds = [];
  }

  visible.value = true;
  // 4. Get the superior classification tree (with internal cache)
  getTreeData(form.value.id);

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
      loading.value = true;
      // Convert domainIds back to domainList before submitting
      const submitForm = JSON.parse(JSON.stringify(form.value));
      submitForm.domainList = (submitForm.domainIds || []).map((id) => {
        const item = dataDomainOptions.value.find((opt) => opt.id === id);
        return {
          dataDomainId: id,
          dataDomainName: item ? item.name : "",
        };
      });
      emit("submit", submitForm);
    }
  });
};

const close = () => {
  visible.value = false;
  loading.value = false;
};

const stopLoading = () => {
  loading.value = false;
};

// Expose the open method to the parent component
defineExpose({
  open,
  close,
  stopLoading,
  refreshTreeData,
});
</script>
