<template>
  <el-dialog
    :title="dialogTitle"
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
     :label-position="labelPosition">
      <el-form-item :label="td('dg.dataLevel.levelName')" prop="name" :label-position="labelPosition">
        <el-input v-model="form.name" :placeholder="td('dg.dataLevel.levelNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="td('dg.dataLevel.levelAbbr')" prop="shortName" :label-position="labelPosition">
        <el-input
          v-model="form.shortName"
          :placeholder="td('dg.dataLevel.levelAbbrPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="td('dg.dataLevel.sensitiveLevel')" prop="sensitiveLevel" :label-position="labelPosition">
        <el-input
          v-model="form.sensitiveLevel"
          :placeholder="td('dg.dataLevel.sensitiveLevelInputPlaceholder')"
          @input="form.sensitiveLevel = form.sensitiveLevel.replace(/[^\d]/g, '')"
          @blur="handleSensitiveLevelChange"
        />
      </el-form-item>

      <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
        <el-radio-group v-model="form.validFlag">
          <el-radio :label="td('dg.dataLevel.disabledLabel')" :value="false" />
          <el-radio :label="td('dg.dataLevel.enabledLabel')" :value="true" />
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
        <el-input
          v-model="form.description"
          type="textarea"
          :placeholder="td('common.form.descriptionPlaceholder')"
          :min-height="192"
          show-word-limit
          maxlength="500"
        />
      </el-form-item>
      <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
        <el-input
          v-model="form.remark"
          type="textarea"
          :placeholder="td('common.form.remarkPlaceholder')"
          :min-height="192"
          show-word-limit
          maxlength="500"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm"
          >{{ td('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="DataLevelDialog">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  addDataLevel,
  getDataLevel,
  updateDataLevel,
  listAllDataLevel,
} from "@/api/dg/dataLevel/dataLevel";
import { ref, watch, computed, getCurrentInstance } from "vue";

const { td } = useDefaultLang();
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
    default: '',
  },
});

const dialogTitle = computed(() => props.title || td('dg.dataLevel.detailTitle'));

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
  name: [{ required: true, message: td('dg.dataLevel.levelNameRequired'), trigger: "blur" }],
  shortName: [
    { required: true, message: td('dg.dataLevel.levelAbbrRequired'), trigger: "blur" },
    {
      min: 2,
      max: 10,
      message: td('dg.dataLevel.levelAbbrLength'),
      trigger: "blur",
    },
    {
      pattern: /^[a-zA-Z0-9_]+$/,
      message: td('dg.dataLevel.levelAbbrPattern'),
      trigger: "blur",
    },
  ],
  sensitiveLevel: [
    { required: true, message: td('dg.dataLevel.sensitiveLevelRequired'), trigger: "change" },
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
          callback(new Error(td('dg.dataLevel.sensitiveLevelInvalid')));
          return;
        }
        callback();
      },
      trigger: "blur",
    },
  ],
  validFlag: [{ required: true, message: td('common.form.statusRequired'), trigger: "change" }],
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
      // Compatible field names
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
    // Decimal input: limited to [1, 100] after rounding down
    return Math.min(100, Math.max(1, floored));
  } else {
    // Integer input
    if (floored <= 0) return 1;
    if (floored >= 100) return 100;
    return floored; // 1 ~ 99 keep original value
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
        proxy.$modal.msgSuccess(formData.id ? td('common.message.editSuccess') : td('common.message.addSuccess'));
        emit("success");
        handleClose();
      })
      .finally(() => {
        submitLoading.value = false;
      });
  });
}
</script>
