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
  <!-- 枚举值校验 -->
  <el-form ref="formRef" :model="form" :disabled="falg">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.codeTable')" prop="useCodeTable">
          <el-radio-group
            v-model="form.useCodeTable"
            @change="handleUseCodeTableChange"
          >
            <el-radio :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.yes') }}</el-radio>
            <el-radio :value="'0'">{{ td('da.qualityTaskRules.ruleCommon.no') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <el-col :span="8">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.ignoreNullValue')" prop="ignoreNullValue">
          <el-radio-group v-model="form.ignoreNullValue">
            <el-radio :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.yes') }}</el-radio>
            <el-radio :value="'0'">{{ td('da.qualityTaskRules.ruleCommon.no') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <el-col :span="8">
        <el-form-item :label="td('da.qualityTaskRules.ruleCommon.caseSensitive')" prop="ignoreCase">
          <el-radio-group v-model="form.ignoreCase">
            <el-radio :value="'1'">{{ td('da.qualityTaskRules.ruleCommon.yes') }}</el-radio>
            <el-radio :value="'0'">{{ td('da.qualityTaskRules.ruleCommon.no') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>

    <!-- 👇 当选择了“关联代码表”为“是”时展示 -->
    <el-row v-if="form.useCodeTable == '1'" :gutter="20">
      <el-col :span="8">
        <el-form-item
          :label="td('da.qualityTaskRules.ruleCommon.selectCodeTable')"
          prop="codeTableId"
          :rules="
            !falg
              ? [{ required: true, message: td('da.qualityTaskRules.ruleCommon.selectCodeTableRequired'), trigger: 'change' }]
              : []
          "
        >
          <template v-if="!falg">
            <el-select
              v-model="form.codeTableId"
              :placeholder="td('da.qualityTaskRules.ruleCommon.codeTablePlaceholder')"
              filterable
              clearable
              @change="handleCodeTableChange"
              class="rule-half"
            >
              <el-option
                v-for="item in dpDataElemList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </template>
          <div v-else class="form-readonly">
            {{
              dpDataElemList.find((i) => i.id === form.codeTableId)?.name || "-"
            }}
          </div>
        </el-form-item>
      </el-col>
    </el-row>

    <div v-loading="loadingList">
      <div class="justify-between mb15">
        <el-row :gutter="20" class="btn-style">
          <template v-if="!falg && form.useCodeTable == 0">
            <el-col :span="1.5">
              <el-button
                type="primary"
                icon="Plus"
                @click="opencodeDialog(undefined)"
                >{{ td('common.button.add') }}</el-button
              >
            </el-col>
          </template>
        </el-row>
      </div>
      <el-table stripe height="200px" :data="form.codeList">
        <el-table-column :label="td('da.qualityTaskRules.ruleCommon.codeValue')" align="center" prop="codeValue">
          <template #default="scope">
            <template v-if="!falg && form.useCodeTable == 0">
              <el-input
                v-model="scope.row.codeValue"
                style="width: 100%"
                :placeholder="td('da.qualityTaskRules.ruleCommon.codeValuePlaceholder')"
              />
            </template>
            <template v-else>
              {{ scope.row.codeValue || "-" }}
            </template>
          </template>
        </el-table-column>

        <el-table-column :label="td('da.qualityTaskRules.ruleCommon.codeName')" align="center" prop="codeName">
          <template #default="scope">
            <template v-if="!falg && form.useCodeTable == 0">
              <el-input
                v-model="scope.row.codeName"
                style="width: 100%"
                :placeholder="td('da.qualityTaskRules.ruleCommon.codeNamePlaceholder')"
              />
            </template>
            <template v-else>
              {{ scope.row.codeName || "-" }}
            </template>
          </template>
        </el-table-column>
        <el-table-column
          v-if="!falg && form.useCodeTable == 0"
          :label="td('common.texts.operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="150"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row, scope.$index + 1)"
              >{{ td('common.button.delete') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-row> </el-row>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { reactive, ref, watch } from "vue";
import { listDpDataElem } from "@/api/dp/dataElem/dataElem";
import { listDpDataElemCode } from "@/api/dp/dataElem/dataElem";

const { td } = useDefaultLang();
const props = defineProps({
  form: Object,
  dppQualityTaskObjSaveReqVO: Array,
  falg: Boolean,
});
let loadingList = ref(false);
const emit = defineEmits(["update:form"]);
let loading = ref(false);
const formRef = ref(null);

const form = reactive({ ...props.form });
let dpDataElemCodeList = ref([]);
let dpDataElemList = ref([]);
function handleCodeTableChange(id) {
  if (!id || id == -1) return;
  loading.value = true;
  form.dataElemId = id;
  loadCodeItemsByTableId(id);
}
function loadCodeItemsByTableId(id) {
  if (!id || id == -1) return;
  loading.value = true;
  listDpDataElemCode({
    pageNum: 1,
    pageSize: 999,
    dataElemId: id,
    ruleType: 2,
  }).then((res) => {
    dpDataElemCodeList.value = res.data.rows;
    form.codeList = res.data.rows;
    loading.value = false;
  });
}
function handleDelete(row, index) {
  form.codeList.splice(Number(index) - 1, 1);
}
function opencodeDialog() {
  // 新增一行空数据
  form.codeList.push({
    codeValue: "",
    codeName: "",
  });
}
function loadCodeTableList() {
  listDpDataElem({
    pageNum: 1,
    pageSize: 999,
    type: "2",
  })
    .then((res) => {
      dpDataElemList.value = res.data.rows;
      loading.value = false;
    })
    .catch(() => {
      loading.value = false;
    });
}
function handleUseCodeTableChange(val) {
  if (val == "1") {
    loadCodeTableList();
  } else {
    form.codeTableId = "";
    form.codeList = [];
    dpDataElemList.value = [];
  }
}

onMounted(() => {
  if (form.useCodeTable === "1" && form.codeTableId) {
    handleUseCodeTableChange("1", true);
  }
});
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (!valid) {
        resolve({ valid: false });
        return;
      }

      if (form.useCodeTable === "0") {
        if (!form.codeList || form.codeList.length === 0) {
          ElMessage.warning(td('da.qualityTaskRules.ruleCommon.codeTableEmpty'));
          resolve({ valid: false });
          return;
        }

        const values = form.codeList
          .map((item) => item.codeValue?.trim())
          .filter((v) => v !== "");
        const hasEmpty = values.length !== form.codeList.length;
        if (hasEmpty) {
          ElMessage.warning(td('da.qualityTaskRules.ruleCommon.codeValueEmpty'));
          resolve({ valid: false });
          return;
        }

        const duplicates = values.filter(
          (val, idx) => values.indexOf(val) !== idx
        );
        if (duplicates.length > 0) {
          ElMessage.warning(td('da.qualityTaskRules.ruleCommon.codeValueDuplicate'));
          resolve({ valid: false });
          return;
        }
      }

      const result = {
        useCodeTable: form.useCodeTable,
        ignoreNullValue: form.ignoreNullValue,
        ignoreCase: form.ignoreCase,
        codeTableId: form.useCodeTable === "1" ? form.codeTableId : "",
        codeList: form.codeList,
        validValues: form.codeList
          .map((item) => item.codeValue)
          .filter((v) => v !== undefined && v !== null && v !== ""),
      };

      resolve({ valid: true, data: result });
    });
  });
}

defineExpose({ validate });
</script>
<style scoped></style>
