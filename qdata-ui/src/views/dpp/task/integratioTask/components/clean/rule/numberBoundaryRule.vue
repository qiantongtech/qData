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
  <!--  Numerical boundary adjustment  -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.minValue', '最小值')"
          prop="min"
          :rules="
            !falg
              ? [{ required: true, message: td('dpp.cleanRule.inputMinValue', '请输入最小值'), trigger: 'blur' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.min"
            :placeholder="td('dpp.cleanRule.noMinLimit', '不填写表示不限制最小值')"
            type="number"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.min ?? "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.maxValue', '最大值')"
          prop="max"
          :rules="
            !falg
              ? [{ required: true, message: td('dpp.cleanRule.inputMaxValue', '请输入最大值'), trigger: 'blur' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.max"
            :placeholder="td('dpp.cleanRule.noMaxLimit', '不填写表示不限制最大值')"
            type="number"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.max ?? "-" }}</div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24" class="hasMsg">
        <el-form-item
          :label="td('dpp.cleanRule.handleMethod', '处理方式')"
          prop="handleType"
          :rules="
            !falg
              ? [{ required: true, message: td('dpp.cleanRule.selectHandleMethod', '请选择处理方式'), trigger: 'blur' }]
              : []
          "
        >
          <el-radio-group v-model="form.handleType" :disabled="falg">
            <el-radio :value="'3'">{{ td('dpp.cleanRule.adjustToMax', '超出最大值时调整为最大值') }}</el-radio>
            <el-radio :value="'2'">{{ td('dpp.cleanRule.adjustToMin', '超出最小值时调整为最小值') }}</el-radio>
            <el-radio :value="'1'">{{ td('dpp.cleanRule.adjustToBoundary', '两种情况都调整到对应的边界值') }}</el-radio>
          </el-radio-group>
          <div class="msg">
            <div v-for="(msg, index) in boundaryExamples" :key="index">
              <el-icon>
                <InfoFilled />
              </el-icon>
              <span>{{ msg }}</span>
            </div>
          </div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { reactive, ref, watch } from "vue";
const { td } = useDefaultLang();
const props = defineProps({
  form: Object,
  inputFields: Array,
  falg: Boolean,
});

const emit = defineEmits(["update:form"]);

const formRef = ref(null);

const form = reactive({ ...props.form });
const boundaryExamples = computed(() => {
  switch (form.handleType) {
    case "3":
      return [td('dpp.cleanRule.exampleAgeOver150', '示例: 如果年龄 > 150，则设置为 150。')];
    case "2":
      return [td('dpp.cleanRule.exampleIncomeUnder1000', '示例: 如果收入 < 1000，则设置为 1000。')];
    case "1":
      return [
        td('dpp.cleanRule.exampleBoth', '示例1: 如果年龄 > 150，则设置为 150。如果收入 < 1000，则设置为 1000。'),
      ];
    default:
      return [];
  }
});
const handleTypeText = computed(() => {
  if (form.handleType === "3") return td('dpp.cleanRule.adjustToMax', '超出最大值时调整为最大值');
  if (form.handleType === "2") return td('dpp.cleanRule.adjustToMin', '超出最小值时调整为最小值');
  if (form.handleType === "1") return td('dpp.cleanRule.adjustToBoundary', '两种情况都调整到对应的边界值');
  return "-";
});
const loading = ref(false);
const exposedFields = ["min", "max", "handleType"];
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
        if (Number(form.min) > Number(form.max)) {
          ElMessage.warning('最小值不能大于最大值。');
          resolve({ valid: false });
          return;
        }
        const data = Object.fromEntries(
          exposedFields.map((key) => [key, form[key]])
        );
        resolve({
          valid: true,
          data,
        });
      } else {
        resolve({ valid: false });
      }
    });
  });
}

defineExpose({ validate });
</script>

