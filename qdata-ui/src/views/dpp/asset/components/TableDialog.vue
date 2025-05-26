<template>
  <el-dialog v-model="dialogVisible" draggable destroy-on-close>
    <template #header>
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ dialogTitle }}
      </span>
    </template>

    <el-form ref="localFormsRef" :rules="rules2" :model="localForms" label-width="140px">
      <template v-if="localForms.type == '3'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="键：" prop="name">
              <el-input v-model="localForms.name" placeholder="请输入字段名称" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="值：" prop="defaultValue">
              <el-input v-model="localForms.defaultValue" placeholder="请输入字段默认值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="字段描述：" prop="remark">
              <el-input v-model="localForms.remark" type="textarea" placeholder="请输入字段描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <template v-if="localForms.type == '1'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数名称：" prop="name">
              <el-input v-model="localForms.name" placeholder="请输入字段名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否允许为空：" prop="requestFlag">
              <el-radio-group v-model="localForms.requestFlag">
                <el-radio v-for="dict in requestOptions" :key="dict.id" :value="dict.itemValue">
                  {{ dict.itemText }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="字段描述：" prop="remark">
              <el-input v-model="localForms.remark" type="textarea" placeholder="请输入字段描述" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="字段类型：" prop="fieldType">
              <el-select v-model="localForms.fieldType" placeholder="请选择字段类型">
                <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="示例值" prop="exampleValue">
              <el-input v-model="localForms.exampleValue" placeholder="请输入字段默认值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="默认值" prop="defaultValue">
              <el-input v-model="localForms.defaultValue" placeholder="请输入默认值" />
            </el-form-item>
          </el-col>
        </el-row>


      </template>
      <template v-if="localForms.type == '2'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数名称" prop="name">
              <el-input v-model="localForms.name" placeholder="请输入字段名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据类型：" prop="fieldType">
              <el-select v-model="localForms.fieldType" placeholder="请选择字段类型">
                <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="示例值" prop="exampleValue">
              <el-input v-model="localForms.exampleValue" placeholder="请输入字段默认值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="字段描述：" prop="remark">
              <el-input v-model="localForms.remark" type="textarea" placeholder="请输入字段描述" />
            </el-form-item>
          </el-col>

        </el-row>
      </template>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="confirm">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="AddList">
import { getCurrentInstance, reactive, watch, ref } from "vue";
import { v4 as uuidv4 } from "uuid";

const dialogTitle = computed(() => {
  switch (localForms.type) {
    case '1':
      return '新增入参字段';
    case '2':
      return '新增出参字段';
    case '3':
      return '新增Header字段';
    default:
      return '新增字段';
  }
});
// 获取 proxy 和字典数据
const { proxy } = getCurrentInstance();
const { column_type } = proxy.useDict("column_type");

// Props 与 emits，注意新增 update:visible 用于 v-model 绑定
const props = defineProps({
  // dialogTitle: { type: String, default: "表格数据" },
  visible: { type: Boolean, default: false },
  formcode: { type: Object, default: () => ({}) },
});
const emit = defineEmits(["confirm", "cancelCU", "update:visible"]);

// 数据字典选项
const requestOptions = [
  { id: 1, itemValue: "1", itemText: "不必填" },
  { id: 2, itemValue: "0", itemText: "必填" },
];
const statusOptions = [
  { id: 1, itemValue: "1", itemText: "启用" },
  { id: 2, itemValue: "0", itemText: "禁用" },
];

// 表单 model 使用 reactive 对象（el-form 要求 model 为可写普通对象）
const localForms = reactive({
  name: '',
  fieldType: '',
  fieldExtent: '',
  fieldDefault: '',
  fieldRequest: '',
  status: '',
  type: '',
  id: '',
  remark: '',
  requestFlag: '',
  parentId: undefined,
});

// 表单 ref
const localFormsRef = ref();

// 内部的对话框 visible 状态：初始值来自 props.visible
const dialogVisible = ref(props.visible);

// 将 props.visible 和内部变量 dialogVisible 同步
watch(
  () => props.visible,
  (newVal) => {
    dialogVisible.value = newVal;
  }
);
watch(
  () => dialogVisible.value,
  (val) => {
    emit("update:visible", val);
  }
);

// 表单回显逻辑，根据 props.formcode 回填
watch(
  () => props.formcode,
  (newVal) => {
    if (newVal && newVal.id) {
      Object.assign(localForms, {
        name: newVal.name || '',
        fieldType: newVal.fieldType || '',
        fieldExtent: newVal.fieldExtent || '',
        fieldDefault: newVal.fieldDefault || '',
        fieldRequest: newVal.fieldRequest || '',
        status: newVal.status || '',
        remark: newVal.remark || '',
        requestFlag: newVal.requestFlag || '',
        id: newVal.id || '',
        type: newVal.type || '',
        parentId: newVal.parentId || undefined,
      });
    } else {
      Object.assign(localForms, {
        id: '',
        type: newVal.type || '',
        name: newVal.name || '',
        fieldType: newVal.fieldType || '',
        fieldExtent: newVal.fieldExtent || '',
        fieldDefault: newVal.fieldDefault || '',
        fieldRequest: newVal.fieldRequest || '',
        status: newVal.status || '0',
        remark: newVal.remark || '',
        requestFlag: newVal.requestFlag || '1',
        parentId: newVal.parentId || undefined,
      });
    }
  },
  { immediate: true, deep: true }
);

// 校验规则
const rules2 = {
  name: [{ required: true, message: "请输入字段名称", trigger: "blur" }],
  requestFlag: [{ required: true, message: "请选择是否必填", trigger: "change" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }],
};

// 确认按钮逻辑
function confirm() {
  localFormsRef.value.validate((valid) => {
    if (valid) {
      if (!localForms.id) {
        localForms.id = uuidv4();
      }
      emit("confirm", { ...localForms });
    } else {
      console.warn("表单验证失败");
    }
  });
}

// 取消按钮逻辑，同时关闭对话框
function handleClose() {
  dialogVisible.value = false; // 内部关闭
  emit("cancelCU", false);
}
</script>
