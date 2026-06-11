<template>
  <el-dialog
    :title="title"
    v-model="dialogVisible"
    width="800px"
    draggable
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="110px"
      @submit.prevent
    >
      <el-form-item label="分级名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入分级名称" />
      </el-form-item>
      <el-form-item label="分级缩写" prop="shortName">
        <el-input
          v-model="form.shortName"
          placeholder="请输入分级缩写，允许英文、数字、下划线"
        />
      </el-form-item>
      <el-form-item label="敏感等级" prop="sensitiveLevel">
        <el-input
          v-model="form.sensitiveLevel"
          placeholder="请输入敏感等级，1-100之间的整数"
          @input="form.sensitiveLevel = form.sensitiveLevel.replace(/[^\d]/g, '')"
          @blur="handleSensitiveLevelChange"
        />
      </el-form-item>

      <el-form-item :label="t('common.texts.status')" prop="validFlag">
        <el-radio-group v-model="form.validFlag">
          <el-radio label="禁用" :value="false" />
          <el-radio label="启用" :value="true" />
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('common.texts.description')" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :placeholder="t('common.form.descriptionPlaceholder')"
          :min-height="192"
          show-word-limit
          maxlength="500个字符"
        />
      </el-form-item>
      <el-form-item :label="t('common.texts.remark')" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :placeholder="t('common.form.remarkPlaceholder')"
          :min-height="192"
          show-word-limit
          maxlength="500个字符"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">{{ t('common.button.cancel') }}</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm"
          >确认</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="DataLevelDialog">
import { useI18n } from 'vue-i18n'
import {
  addDataLevel,
  getDataLevel,
  updateDataLevel,
  listAllDataLevel,
} from "@/api/dg/dataLevel/dataLevel";
import { ref, watch, computed, getCurrentInstance } from "vue";

const { t } = useI18n();
const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  id: {
    type: [String, Number],
    default: null,
  },
  title: {
    type: String,
    default: "数据分级",
  },
});

const emit = defineEmits(["update:open", "success"]);

const { proxy } = getCurrentInstance();

const dialogVisible = computed({
  get: () => props.open,
  set: (val) => emit("update:open", val),
});

const formRef = ref();
const submitLoading = ref(false);
const allDataLevelList = ref([]);

const form = ref({
  id: null,
  name: "",
  shortName: "",
  sensitiveLevel: "",
  description: "",
  validFlag: false,
  remark: "",
});

const rules = {
  name: [{ required: true, message: "分级名称不能为空", trigger: "blur" }],
  shortName: [
    { required: true, message: "分级缩写不能为空", trigger: "blur" },
    {
      min: 2,
      max: 10,
      message: "分级缩写长度应在2到10个字符之间",
      trigger: "blur",
    },
    {
      pattern: /^[a-zA-Z0-9_]+$/,
      message: "分级缩写只能包含字母、数字和下划线",
      trigger: "blur",
    },
  ],
  sensitiveLevel: [
    { required: true, message: "敏感等级不能为空", trigger: "change" },
    {
      validator: (rule, value, callback) => {
        if (value === null || value === undefined || value === "") {
          callback();
          return;
        }
        const raw = String(value ?? "").trim();
        const str = raw.replace(/[。．，,·•﹒｡]/g, ".");
        const num = Number(str);
        if (Number.isNaN(num)) {
          callback(new Error("请输入有效的数字"));
          return;
        }
        callback();
      },
      trigger: "blur",
    },
  ],
  validFlag: [{ required: true, message: t('common.form.statusRequired'), trigger: "change" }],
};

watch(
  () => props.open,
  (newVal) => {
    if (newVal) {
      reset();
      getAllList();
      if (props.id) {
        handleUpdate(props.id);
      }
    }
  }
);

function reset() {
  form.value = {
    id: null,
    name: "",
    shortName: "",
    sensitiveLevel: "",
    description: "",
    validFlag: true,
    remark: "",
  };
  if (formRef.value) {
    formRef.value.resetFields();
  }
}

function getAllList() {
  listAllDataLevel()
    .then((response) => {
      allDataLevelList.value = response.data || [];
    })
    .catch(() => {
      allDataLevelList.value = [];
    });
}

function handleUpdate(id) {
  getDataLevel(id).then((res) => {
    const data = res.data || {};
    form.value = {
      ...form.value,
      ...data,
      // 兼容字段名
      name: data.name || data.levelName || "",
      shortName: data.shortName || data.levelCode || data.code || "",
      validFlag: data.validFlag ?? (data.status === "1" || data.status === 1),
    };
  });
}

function handleSensitiveLevelChange(e) {
  const value = e?.target?.value ?? form.value.sensitiveLevel;
  if (value === null || value === undefined || value === "") return;
  const normalized = normalizeSensitiveLevel(value);
  if (normalized === null) {
    form.value.sensitiveLevel = 1;
    return;
  }
  form.value.sensitiveLevel = normalized;
}

function handleClose() {
  dialogVisible.value = false;
  reset();
}

function normalizeSensitiveLevel(value) {
  const raw = String(value ?? "").trim();
  const str = raw.replace(/[。．，,·•﹒｡]/g, ".");
  const num = Number(str);
  if (Number.isNaN(num)) return null;

  const floored = Math.floor(num);
  const isDecimalInput = str.includes(".") || !Number.isInteger(num);

  if (isDecimalInput) {
    // 小数输入：向下取整后限制在 [1, 100]
    return Math.min(100, Math.max(1, floored));
  } else {
    // 整数输入
    if (floored <= 0) return 1;
    if (floored >= 100) return 100;
    return floored; // 1 ~ 99 保留原值
  }
}

function submitForm() {
  formRef.value.validate((valid) => {
    if (!valid) return;
    submitLoading.value = true;
    const formData = form.value;
    const req = formData.id
      ? updateDataLevel(formData)
      : addDataLevel(formData);

    req
      .then(() => {
        proxy.$modal.msgSuccess(formData.id ? t('common.message.editSuccess') : t('common.message.addSuccess'));
        emit("success");
        handleClose();
      })
      .finally(() => {
        submitLoading.value = false;
      });
  });
}
</script>
