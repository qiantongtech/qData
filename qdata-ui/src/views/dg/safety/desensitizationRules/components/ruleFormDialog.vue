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
     :label-position="labelPosition">
      <el-form-item :label="td('dg.desensitizationRules.ruleName')" prop="name" :label-position="labelPosition">
        <el-input v-model="form.name" :placeholder="td('dg.desensitizationRules.ruleNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="td('dg.sensitiveList.dataCategory')" prop="dataCategoryId" :label-position="labelPosition">
        <el-tree-select
            v-if="!form.id"
            v-model="form.dataCategoryId"
            :data="dataCategoryList"
            :placeholder="td('dg.sensitiveList.dataCategoryPlaceholder')"
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
            :placeholder="td('dg.sensitiveList.dataCategoryPlaceholder')"
            disabled
        />
      </el-form-item>

      <!-- 👇 应用场景：改为复选框组 -->
      <qt-form-item
          :label="td('dg.desensitizationRules.applicationScene')"
          prop="applicationScene"
          :tip="{ content: td('dg.desensitizationRules.applicationSceneTip') }"
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
          :label="td('dg.desensitizationRules.maskType')"
          prop="maskType"
          :tip="{ content: td('dg.desensitizationRules.maskTypeTip') }"
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
      <el-form-item :label="td('dg.desensitizationRules.replaceRule')" prop="replaceRule" :label-position="labelPosition">
        <el-select v-model="form.replaceRule" :placeholder="td('dg.desensitizationRules.replaceRulePlaceholder')">
          <el-option
              v-for="opt in maskRuleOptions"
              :key="opt.value"
              v-bind="opt"
              :disabled="opt.label !== '自定义掩码'"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="td('dg.desensitizationRules.replaceContent')" prop="replaceContent" :label-position="labelPosition">
        <el-input v-model="form.replaceContent" :placeholder="td('dg.desensitizationRules.replaceContentPlaceholder')" />
      </el-form-item>
      <qt-form-item
          :label="td('dg.desensitizationRules.desensInterval')"
          prop="intervalList"
          :tip="{
          content: td('dg.desensitizationRules.desensIntervalTip'),
        }"
          class="row-full"
      >
        <div class="range-panel">
          <div
              class="range-row"
              v-for="(it, idx) in form.intervalList"
              :key="idx"
          >
            <div class="range-label">{{ td('dg.desensitizationRules.intervalLabel', '区间 {i}：', { i: idx + 1 }) }}</div>
            <el-input-number
                v-model="it.startNum"
                :min="1"
                :controls="false"
                :placeholder="td('dg.desensitizationRules.startPosition')"
                class="range-input"
            />
            <el-input-number
                v-model="it.endNum"
                :min="1"
                :controls="false"
                :placeholder="td('dg.desensitizationRules.endPosition')"
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
              {{ td('dg.desensitizationRules.addInterval') }}
            </el-button>
          </div>
        </div>
      </qt-form-item>
      <el-form-item :label="td('common.texts.status')" prop="validFlag" class="row-full" :label-position="labelPosition">
        <el-radio-group v-model="form.validFlag">
          <el-radio :label="false">{{ td('dg.desensitizationRules.detailStatusDisable') }}</el-radio>
          <el-radio :label="true">{{ td('dg.desensitizationRules.detailStatusEnable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="td('common.texts.description')" prop="description" class="row-full" :label-position="labelPosition">
        <el-input
            v-model="form.description"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder')"
            maxlength="500"
            show-word-limit
        />
      </el-form-item>
      <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
        <el-input
            v-model="form.remark"
            type="textarea"
            maxlength="500"
            show-word-limit
            :placeholder="td('common.form.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="close">{{
            isReadonly ? "{{ td('common.button.close') }}" : "{{ td('common.button.cancel') }}"
          }}</el-button>
        <el-button
            v-if="!isReadonly"
            type="primary"
            size="mini"
            :loading="loading"
            @click="submit"
        >
          {{ td('common.button.confirm') }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="RuleFormDialog">
import useDefaultLang from "@/composables/useDefaultLang"
import { computed, getCurrentInstance, nextTick, onMounted, ref } from "vue";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";

const { td } = useDefaultLang();
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
  name: [{ required: true, message: td('dg.desensitizationRules.ruleNameRequired'), trigger: "blur" }],
  dataCategoryId: [
    { required: true, message: td('dg.sensitiveList.dataCategoryRequired'), trigger: "change" },
  ],
  // 应用场景复选框校验（必须选一个）
  applicationScene: [
    { required: true, message: td('dg.desensitizationRules.applicationSceneRequired'), trigger: "blur" },
    { type: "array", message: td('dg.desensitizationRules.applicationSceneFormatError'), trigger: "change" },
  ],
  maskType: [
    { required: true, message: td('dg.desensitizationRules.maskTypeRequired'), trigger: "change" },
  ],
  replaceRule: [
    { required: true, message: td('dg.desensitizationRules.replaceRuleRequired'), trigger: "change" },
  ],
  replaceContent: [
    { required: true, message: td('dg.desensitizationRules.replaceContentRequired'), trigger: "blur" },
  ],
  intervalList: [
    { required: true, message: td('dg.desensitizationRules.desensIntervalRequired'), trigger: "change" },
    {
      validator: (_, value, cb) => {
        const arr = Array.isArray(value) ? value : [];
        if (!arr.length) return cb(new Error(td('dg.desensitizationRules.intervalRequired')));
        for (const it of arr) {
          const s = it?.startNum;
          const e = it?.endNum;
          if (s == null || e == null) {
            return cb(new Error(td('dg.desensitizationRules.intervalIncomplete')));
          }
          if (s < 1 || e < 1)
            return cb(new Error(td('dg.desensitizationRules.intervalMin')));
          if (e < s) return cb(new Error(td('dg.desensitizationRules.intervalOrder')));
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
      options.title || (options.id != null ? td('dg.desensitizationRules.editTitle') : td('dg.desensitizationRules.addTitle'));
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
        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
      } else {
        await props.add(payload);
        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
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
