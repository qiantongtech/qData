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
  <!--  Unify field prefix/suffix  -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">
    <el-row>
      <el-col :span="12">
        <el-form-item
          :label="td('dpp.cleanRule.markValue', '标记值')"
          prop="stringValue"
          :rules="
            !falg
              ? [{ required: true, message: td('dpp.cleanRule.inputMarkValue', '请输入标记值'), trigger: 'blur' }]
              : []
          "
        >
          <el-input
            v-if="!falg"
            v-model="form.stringValue"
            :placeholder="td('dpp.cleanRule.inputValue', '请输入添加值')"
            class="rule-half"
          />
          <div v-else class="form-readonly">{{ form.stringValue || "-" }}</div>
        </el-form-item>
      </el-col>
      <el-col :span="12" class="hasMsg">
        <el-form-item
          :label="td('dpp.cleanRule.handleMethod', '处理方式')"
          prop="handleType"
          :rules="
            !falg
              ? [{ required: true, message: td('dpp.cleanRule.selectHandleMethod', '请选择处理方式'), trigger: 'blur' }]
              : []
          "
        >
          <div class="affix-handle-content">
            <el-radio-group v-model="form.handleType" :disabled="falg">
              <el-radio :value="'1'">{{ td('dpp.cleanRule.addPrefix', '加前綴') }}</el-radio>
              <el-radio :value="'2'">{{ td('dpp.cleanRule.addSuffix', '加后綴') }}</el-radio>
              <el-radio :value="'3'">{{ td('dpp.cleanRule.removePrefix', '去除前缀') }}</el-radio>
              <el-radio :value="'4'">{{ td('dpp.cleanRule.removeSuffix', '去除后缀') }}</el-radio>
            </el-radio-group>
            <div v-if="affixExample" class="affix-example">
              {{ affixExample }}
            </div>
          </div>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row> </el-row>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { computed, reactive, ref } from "vue";
const { td } = useDefaultLang();
const props = defineProps({
  form: Object,
  inputFields: Array,
  falg: Boolean,
});

const formRef = ref(null);

const form = reactive({ ...props.form });
const affixExample = computed(() => {
  const mark = form.stringValue || "qq";
  switch (form.handleType) {
    case "1":
      return td(
        'dpp.cleanRule.addPrefixExample',
        '加前缀示例：原值123，标记值{mark} → {mark}123',
        { mark }
      );
    case "2":
      return td(
        'dpp.cleanRule.addSuffixExample',
        '加后缀示例：原值123，标记值{mark} → 123{mark}',
        { mark }
      );
    case "3":
      return td(
        'dpp.cleanRule.removePrefixExample',
        '去除前缀示例：原值{mark}123，标记值{mark} → 123',
        { mark }
      );
    case "4":
      return td(
        'dpp.cleanRule.removeSuffixExample',
        '去除后缀示例：原值123{mark}，标记值{mark} → 123',
        { mark }
      );
    default:
      return "";
  }
});
const exposedFields = ["stringValue", "handleType"];
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (valid) {
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
<style scoped>
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
.rule-half {
  width: 100%;
}
.affix-handle-content {
  width: 100%;
}
.affix-example {
  margin-top: 12px;
  color: #606266;
  line-height: 24px;
}
</style>
