<template>
  <el-dialog
      :title="title"
      v-model="visible"
      width="920px"
      :append-to="appendToTarget"
      draggable
      @closed="reset"
  >
    <template #header>
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        :disabled="isReadonly"
        @submit.prevent
        class="column-form"
    >
      <el-form-item label="脱敏规则名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入脱敏规则名称" />
      </el-form-item>
      <el-form-item label="数据分类" prop="dataCategoryId">
        <el-tree-select
            v-if="!form.id"
            v-model="form.dataCategoryId"
            :data="dataCategoryList"
            placeholder="请选择数据分类"
            filterable
            clearable
            check-strictly
            default-expand-all
            :loading="dataCategoryLoading"
            style="width: 100%"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            :disabled="!!form.id || categoryFixed"
        />
        <el-input
            v-else
            v-model="form.dataCategoryName"
            placeholder="请选择数据分类"
            disabled
        />
      </el-form-item>

      <!-- 👇 应用场景：改为复选框组 -->
      <qt-form-item
          label="应用场景"
          prop="applicationScene"
          :tip="{ content: '用于确定规则应用到资产侧还是服务侧。' }"
      >
        <el-checkbox-group v-model="form.applicationScene">
          <el-checkbox
              v-for="dict in dg_application_scene"
              :key="dict.value"
              :label="dict.value"
              :disabled="dict.label == '数据服务'"
          >
            {{ dict.label }}
          </el-checkbox>
        </el-checkbox-group>
      </qt-form-item>

      <qt-form-item
          label="脱敏方式"
          prop="maskType"
          :tip="{ content: '底层脱敏：写入/存储侧；展示脱敏：查询/展示侧。' }"
      >
        <el-radio-group v-model="form.maskType">
          <el-radio
              v-for="dict in dg_mask_type"
              :key="dict.value"
              :label="dict.value"
              :disabled="dict.label !== '展示脱敏'"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </qt-form-item>
      <el-form-item label="转换规则" prop="replaceRule">
        <el-select v-model="form.replaceRule" placeholder="请选择转换规则">
          <el-option
              v-for="opt in maskRuleOptions"
              :key="opt.value"
              v-bind="opt"
              :disabled="opt.label !== '自定义掩码'"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="替换内容" prop="replaceContent">
        <el-input v-model="form.replaceContent" placeholder="请输入替换内容" />
      </el-form-item>
      <qt-form-item
          label="脱敏区间"
          prop="intervalList"
          :tip="{
          content:
            '脱敏区间：设置需要脱敏的字符位置范围（从 1 开始计数）。例如：脱敏第 4 到第 10 位，则起始值为 4，末尾值为 10。',
        }"
          class="row-full"
      >
        <div class="range-panel">
          <div
              class="range-row"
              v-for="(it, idx) in form.intervalList"
              :key="idx"
          >
            <div class="range-label">区间 {{ idx + 1 }}：</div>
            <el-input-number
                v-model="it.startNum"
                :min="1"
                :controls="false"
                placeholder="起始位置"
                class="range-input"
            />
            <el-input-number
                v-model="it.endNum"
                :min="1"
                :controls="false"
                placeholder="截止位置"
                class="range-input"
            />
            <el-button
                link
                type="danger"
                icon="Delete"
                :disabled="form.intervalList.length <= 1 || isReadonly"
                @click="removeRange(idx)"
            />
            <el-button
                v-if="idx == form.intervalList.length - 1 && !isReadonly"
                link
                type="primary"
                icon="Plus"
                @click="addRange"
            >
              添加区间
            </el-button>
          </div>
        </div>
      </qt-form-item>
      <el-form-item label="状态" prop="validFlag" class="row-full">
        <el-radio-group v-model="form.validFlag">
          <el-radio :label="false">禁用</el-radio>
          <el-radio :label="true">启用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述" prop="description" class="row-full">
        <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入描述"
            maxlength="500个字符"
            show-word-limit
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark" class="row-full">
        <el-input
            v-model="form.remark"
            type="textarea"
            maxlength="500个字符"
            show-word-limit
            placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="close">{{
            isReadonly ? "关 闭" : "取 消"
          }}</el-button>
        <el-button
            v-if="!isReadonly"
            type="primary"
            size="mini"
            :loading="loading"
            @click="submit"
        >
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="RuleFormDialog">
import { computed, getCurrentInstance, nextTick, onMounted, ref } from "vue";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";
const emit = defineEmits(["success"]);
const props = defineProps({
  appendTo: { type: [String, Object], default: undefined },
  maskRuleOptions: { type: Array, default: () => [] },
  get: { type: Function, required: true },
  add: { type: Function, required: true },
  update: { type: Function, required: true },
});
const { proxy } = getCurrentInstance();
const { dg_application_scene, dg_mask_type } = proxy.useDict(
    "dg_application_scene",
    "dg_mask_type"
);
const appendToTarget = computed(() => props.appendTo || undefined);
const visible = ref(false);
const loading = ref(false);
const title = ref("");
const formRef = ref(null);
const categoryFixed = ref(false);
const isReadonly = ref(false);
const dataCategoryLoading = ref(false);
const dataCategoryList = ref([]);

const rules = {
  name: [{ required: true, message: "脱敏规则名称不能为空", trigger: "blur" }],
  dataCategoryId: [
    { required: true, message: "数据分类不能为空", trigger: "change" },
  ],
  // 应用场景复选框校验（必须选一个）
  applicationScene: [
    { required: true, message: "应用场景不能为空", trigger: "blur" },
    { type: "array", message: "应用场景格式错误", trigger: "change" },
  ],
  maskType: [
    { required: true, message: "脱敏方式不能为空", trigger: "change" },
  ],
  replaceRule: [
    { required: true, message: "转换规则不能为空", trigger: "change" },
  ],
  replaceContent: [
    { required: true, message: "替换内容不能为空", trigger: "blur" },
  ],
  intervalList: [
    { required: true, message: "脱敏区间不能为空", trigger: "change" },
    {
      validator: (_, value, cb) => {
        const arr = Array.isArray(value) ? value : [];
        if (!arr.length) return cb(new Error("脱敏区间不能为空"));
        for (const it of arr) {
          const s = it?.startNum;
          const e = it?.endNum;
          if (s == null || e == null) {
            return cb(new Error("请填写完整的起始位置与截止位置"));
          }
          if (s < 1 || e < 1)
            return cb(new Error("起始位置与截止位置需大于等于 1"));
          if (e < s) return cb(new Error("截止位置需大于等于起始位置"));
        }
        return cb();
      },
      trigger: "change",
    },
  ],
};

const defaultForm = {
  id: null,
  name: null,
  dataCategoryId: null,
  // 复选框绑定数组
  applicationScene: [],
  maskType: "2",
  replaceRule: null,
  replaceContent: null,
  intervalList: [{ startNum: null, endNum: null, intervalNo: 1 }],
  validFlag: false,
  description: null,
  remark: null,
};

const form = ref({ ...defaultForm });

async function getDataCategoryList() {
  dataCategoryLoading.value = true;
  try {
    const res = await selectTreeDataCategory();
    const rawData = res?.data || [];
    const processTree = (nodes) => {
      return nodes
          .filter((node) => String(node.desensitizationRulesFlag) !== "1")
          .map((node) => {
            const newNode = { ...node };
            newNode.disabled = String(node.type) === "1";
            if (node.children && node.children.length > 0) {
              newNode.children = processTree(node.children);
            }
            return newNode;
          });
    };
    dataCategoryList.value = processTree(rawData);
  } catch {
    dataCategoryList.value = [];
  } finally {
    dataCategoryLoading.value = false;
  }
}

function normalizeIntervalList(list) {
  if (Array.isArray(list) && list.length) return list;
  return [{ startNum: null, endNum: null, intervalNo: 1 }];
}

function reset() {
  form.value = JSON.parse(JSON.stringify(defaultForm));
  categoryFixed.value = false;
  isReadonly.value = false;
  nextTick(() => formRef.value?.clearValidate());
}

function addRange() {
  const nextNo = form.value.intervalList.length + 1;
  form.value.intervalList.push({
    startNum: null,
    endNum: null,
    intervalNo: nextNo,
  });
}

function removeRange(idx) {
  form.value.intervalList.splice(idx, 1);
  if (!form.value.intervalList.length) {
    form.value.intervalList.push({
      startNum: null,
      endNum: null,
      intervalNo: 1,
    });
  } else {
    form.value.intervalList.forEach((it, i) => {
      it.intervalNo = i + 1;
    });
  }
}

async function open(options = {}) {
  reset();
  loading.value = false;
  isReadonly.value = !!options.readonly;
  title.value =
      options.title || (options.id != null ? "编辑脱敏规则" : "新增脱敏规则");
  visible.value = true;
  await getDataCategoryList();

  if (options.id == null) {
    if (dg_mask_type.value && dg_mask_type.value.length) {
      const hit = dg_mask_type.value.find((o) => o.label == "展示脱敏");
      if (hit) form.value.maskType = hit.value;
    }
    if (props.maskRuleOptions && props.maskRuleOptions.length) {
      const hit = props.maskRuleOptions.find((o) => o.label == "自定义掩码");
      if (hit) form.value.replaceRule = hit.value;
    }
    if (options.form && typeof options.form == "object") {
      const preset = { ...defaultForm, ...options.form };
      if (options.form.dataCategoryId != null) {
        categoryFixed.value = true;
      }
      form.value = {
        ...preset,
        intervalList: normalizeIntervalList(preset.intervalList),
        applicationScene: Array.isArray(preset.applicationScene)
            ? preset.applicationScene
            : preset.applicationScene
                ? preset.applicationScene.split(",")
                : [],
      };
      await nextTick();
      //formRef.value?.clearValidate();
    }
    return;
  }

  loading.value = true;
  try {
    const res = await props.get(options.id);
    const d = res?.data || {};
    form.value = {
      ...defaultForm,
      ...d,
      intervalList: normalizeIntervalList(d.intervalList),
      applicationScene: Array.isArray(d.applicationScene)
          ? d.applicationScene
          : d.applicationScene
              ? d.applicationScene.split(",")
              : [],
    };
    await nextTick();
    formRef.value?.clearValidate();
  } finally {
    loading.value = false;
  }
}

function close() {
  visible.value = false;
  loading.value = false;
  reset();
}

function submit() {
  formRef.value?.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;

    const payload = {
      ...form.value,
      // 提交时转逗号分隔字符串
      applicationScene: form.value.applicationScene.join(","),
      intervalList: form.value.intervalList.map((it, idx) => {
        const { id, ...rest } = it;
        return {
          ...rest,
          intervalNo: idx + 1,
        };
      }),
    };

    try {
      if (payload.id != null) {
        await props.update(payload);
        proxy.$modal.msgSuccess("修改成功");
      } else {
        await props.add(payload);
        proxy.$modal.msgSuccess("新增成功");
      }
      close();
      emit("success");
    } finally {
      loading.value = false;
    }
  });
}

defineExpose({
  open,
  close,
});
</script>

<style scoped lang="scss">
.range-panel {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  padding: 10px 12px;
  background: #f5f8ff;
  border: 1px solid #d9e2ff;
  border-radius: 4px;
}
.range-row {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}
.range-label {
  width: 70px;
  color: #606266;
}
.range-input {
  width: 240px;
}
</style>
