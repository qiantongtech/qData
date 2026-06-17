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
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.parentCategory', '上级分类')" prop="parentId">
            <el-tree-select
              filterable
              v-model="form.parentId"
              :data="treeOptions"
              :props="{ value: 'id', label: 'name', children: 'children' }"
              value-key="id"
              :placeholder="td('dm.businessCategory.parentPlaceholder', '请选择上级')"
              check-strictly
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.name', '业务分类名称')" prop="name">
            <el-input v-model="form.name" :placeholder="td('dm.businessCategory.namePlaceholder', '请输入业务分类名称')" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.engName', '英文缩写')" prop="engName">
            <el-input
              v-model="form.engName"
              :placeholder="td('dm.businessCategory.engNamePlaceholder', '请输入英文缩写')"
              @input="handleEngNameInput"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="td('dm.businessCategory.dataDomain', '关联数据域')" prop="domainIds">
            <el-select
              v-model="form.domainIds"
              :placeholder="td('dm.businessCategory.dataDomainIdsPlaceholder', '请选择关联数据域')"
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
          <el-form-item :label="td('dm.businessCategory.ownerId', '负责人')" prop="ownerId">
            <el-select
              v-model="form.ownerId"
              filterable
              :placeholder="td('dm.businessCategory.ownerIdPlaceholder', '请选择负责人')"
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
          <el-form-item :label="td('dm.businessCategory.ownerPhone', '负责人电话')" prop="ownerPhone">
            <el-input
              v-model="form.ownerPhone"
              :placeholder="td('dm.businessCategory.ownerPhonePlaceholder', '请输入负责人电话')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.status', '状态')" prop="validFlag">
            <el-radio v-model="form.validFlag" :label="false">{{ td('dm.businessCategory.disableText', '禁用') }}</el-radio>
            <el-radio v-model="form.validFlag" :label="true">{{ td('dm.businessCategory.enableText', '启用') }}</el-radio>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description', '描述')" prop="description">
            <el-input
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.descriptionPlaceholder', '请输入描述')"
              v-model="form.description"
              :rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.remark', '备注')" prop="remark">
            <el-input
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('common.form.remarkPlaceholder', '请输入备注')"
              v-model="form.remark"
              :rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onCancel">{{ td('common.button.cancel', '取消') }}</el-button>
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >{{ td('common.button.confirm', '确定') }}</el-button
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

// 组件内部状态
const title = ref("");
const treeOptions = ref([]);
const dataDomainOptions = ref([]);
const customRules = ref(null);
const managerOptions = ref([]);

// 缓存原始树数据
const rawTreeData = ref([]);

// 默认表单数据
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

// 默认校验规则
const defaultRules = {
  name: [{ required: true, message: td('dm.businessCategory.nameRequired', '业务分类名称不能为空'), trigger: "blur" }],
  engName: [
    { required: true, message: td('dm.businessCategory.engNameRequired', '英文缩写不能为空'), trigger: "blur" },
    { pattern: /^[a-zA-Z]+$/, message: td('dm.dataDomain.englishOnly', '只能输入英文字符'), trigger: "blur" },
  ],
  parentId: [{ required: true, message: td('dm.businessCategory.categoryEmpty', '上级分类不能为空'), trigger: "blur" }],
  domainIds: [
    {
      required: true,
      type: "array",
      message: td('dm.businessCategory.dataDomainRequired', '请选择关联数据域'),
      trigger: "change",
    },
  ],
};

// 计算最终使用的规则，优先使用传入的 customRules
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

/** 获取数据域列表 */
const getDataDomainOptions = () => {
  return listDataDomain({
    orderByColumn: "create_time",
    isAsc: "descending",
  }).then((response) => {
    dataDomainOptions.value = response?.data?.rows || [];
  });
};

/** 刷新树数据 */
const refreshTreeData = () => {
  return listBusinessCategory().then((response) => {
    rawTreeData.value = response?.data || [];
  });
};

/** 处理树结构显示，包含过滤逻辑 */
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
      name: td('dm.businessCategory.topNode', '顶级节点'),
      children: proxy.handleTree(rows, "id", "parentId"),
    },
  ];
};

/** 获取上级分类树 */
const getTreeData = (excludeId) => {
  return refreshTreeData().then(() => {
    processTreeData(excludeId);
  });
};

/**
 * 打开弹窗的方法
 * @param {Object} options 配置项
 */
const open = (options = {}) => {
  title.value = options.title || td('dm.common.edit', '修改');
  customRules.value = options.rules || null;

  // 1. 获取关联数据域（每次打开都重新获取且按时间倒序）
  getDataDomainOptions();

  // 2. 获取负责人
  if (!managerOptions.value?.length) {
    getManagerOptions();
  }

  // 3. 初始化表单数据
  if (options.form) {
    form.value = JSON.parse(
      JSON.stringify({ ...defaultForm, ...options.form })
    );
    // 如果存在 domainList，初始化 domainIds
    if (form.value.domainList && form.value.domainList.length > 0) {
      form.value.domainIds = form.value.domainList.map(
        (item) => item.dataDomainId
      );
    } else if (form.value.domainId) {
      // 兼容旧数据
      form.value.domainIds = [form.value.domainId];
    } else if (form.value.domainIds && Array.isArray(form.value.domainIds)) {
      // 已经有 domainIds，且是数组，则不需要重置。
      // 如果后端返回的是字符串数组，而 options 是数字，这里建议统一转成数字以保证回显
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
  // 4. 获取上级分类树（内部带缓存）
  getTreeData(form.value.id);

  // 重置校验状态
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
      // 提交前将 domainIds 转换回 domainList
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

// 暴露 open 方法给父组件
defineExpose({
  open,
  close,
  stopLoading,
  refreshTreeData,
});
</script>
