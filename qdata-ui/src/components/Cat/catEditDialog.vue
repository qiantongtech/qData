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
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="上级类目" prop="parentId">
            <el-tree-select
              filterable
              v-model="form.parentId"
              :data="treeOptions"
              :props="{ value: 'id', label: 'name', children: 'children' }"
              value-key="id"
              placeholder="请选择上级"
              check-strictly
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="effectiveNameLabel" prop="name">
            <el-input
              v-model="form.name"
              :placeholder="effectiveNamePlaceholder"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-col :span="24">
        <el-form-item label="状态" prop="validFlag">
          <el-radio v-model="form.validFlag" :label="true">启用</el-radio>
          <el-radio v-model="form.validFlag" :label="false">禁用</el-radio>
        </el-form-item>
      </el-col>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="排序" prop="sortOrder">
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
          <el-form-item label="描述" prop="description">
            <el-input
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              placeholder="请输入描述"
              v-model="form.description"
              :min-height="192"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              placeholder="请输入备注"
              v-model="form.remark"
              :min-height="192"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onCancel">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
/**
 * CatEditDialog 组件使用说明
 *
 * 该组件用于类目（Category）的编辑和新增操作。
 * 不再使用 props 传递数据，而是通过 expose 出的 open 方法进行调用。
 *
 * 使用方法：
 * 1. 在父组件中引入并放置组件：
 *    <CatEditDialog ref="catEditDialogRef" @submit="handleDialogSubmit" />
 *
 * 2. 在 script 中定义 ref：
 *    const catEditDialogRef = ref();
 *
 * 3. 调用 open 方法打开弹窗：
 *    catEditDialogRef.value.open({
 *      title: "新增类目",          // 弹窗标题
 *      nameLabel: "类目名称",      // 名称字段的 label
 *      treeOptions: [...],        // 上级类目树形数据
 *      form: { ... },             // 表单初始数据（如果是修改，传入当前行数据；如果是新增，传入默认值或部分预设值）
 *      rules: { ... }             // (可选) 表单校验规则，如果不传则使用默认规则
 *    });
 *
 * 4. 监听 @submit 事件获取结果：
 *    const handleDialogSubmit = (formData) => {
 *      // 调用接口保存 formData
 *      // 保存成功后无需手动关闭弹窗，弹窗会在点击确定且校验通过后自动关闭（或者根据业务需求调整）
 *      // 注意：目前的实现是校验通过后自动关闭弹窗并 emit submit。
 *    };
 */

import { ref, reactive, computed, nextTick } from "vue";

const emit = defineEmits(["submit", "cancel"]);

const visible = ref(false);
const formRef = ref();

// 组件内部状态
const title = ref("");
const nameLabel = ref("类目名称");
const treeOptions = ref([]);
const customRules = ref(null);

// 默认表单数据
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
const effectiveNamePlaceholder = computed(() => `请输入${nameLabel.value}`);

// 默认校验规则
const defaultRules = {
  name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
  parentId: [{ required: true, message: "上级类目不能为空", trigger: "blur" }],
  code: [{ required: true, message: "编码不能为空", trigger: "blur" }],
};

// 计算最终使用的规则，优先使用传入的 customRules
const currentRules = computed(() => {
  if (customRules.value) {
    return customRules.value;
  }
  // 动态更新默认规则中的 message
  const rules = JSON.parse(JSON.stringify(defaultRules));
  if (rules.name && rules.name[0]) {
    rules.name[0].message = `${nameLabel.value}不能为空`;
  }
  return rules;
});

/**
 * 打开弹窗的方法
 * @param {Object} options 配置项
 */
const open = (options = {}) => {
  title.value = options.title || "编辑";
  nameLabel.value = options.nameLabel || "类目名称";
  treeOptions.value = options.treeOptions || [];
  customRules.value = options.rules || null;

  // 初始化表单数据
  // 如果传入了 form，则合并到 defaultForm 中（深拷贝避免引用问题）
  // 注意：这里假设 options.form 包含了需要回显的数据
  if (options.form) {
    form.value = JSON.parse(
      JSON.stringify({ ...defaultForm, ...options.form })
    );
  } else {
    form.value = JSON.parse(JSON.stringify(defaultForm));
  }

  visible.value = true;

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
      emit("submit", JSON.parse(JSON.stringify(form.value)));
      visible.value = false;
    }
  });
};

// 暴露 open 方法给父组件
defineExpose({
  open,
});
</script>
