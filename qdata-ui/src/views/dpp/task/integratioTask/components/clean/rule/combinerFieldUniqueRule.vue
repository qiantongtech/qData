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
  <!--  组合字段去重  -->
  <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
    <div class="deduplication-config">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="addtypecolumns">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dpp.cleanRule.addSortField', '新增排序字段') }}
            </el-button>
          </el-col>
        </el-row>
      </div>
      <el-table
        :data="form.stringValue"
        btype
        stripe
        style="width: 100%"
        row-key="sort"
        ref="dragTable"
      >
        <el-table-column :label="td('dpp.cleanRule.index', '序号')" width="80" align="left">
          <template #default="{ $index }">
            <div
              class="allowDrag"
              style="
                cursor: move;
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <el-icon>
                <Operation />
              </el-icon>
              <span style="margin-left: 4px">{{ $index + 1 }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="columns" :label="td('dpp.cleanRule.fieldName', '字段名称')" align="left">
          <template #default="{ row }">
            <template v-if="!falg">
              <el-select
                v-model="row.columns"
                :placeholder="td('dpp.cleanRule.selectCleanField', '请选择清洗字段')"
                clearable
              >
                <el-option
                  v-for="dict in inputFields"
                  :key="dict.columnName"
                  :label="dict.label"
                  :value="dict.columnName"
                  :disabled="iscolumnsDisabled(dict.columnName, row.id)"
                />
              </el-select>
            </template>
            <div v-else class="form-readonly">
              {{
                inputFields.find((d) => d.columnName === row.columns)?.label ||
                row.columns ||
                "-"
              }}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="type" :label="td('dpp.cleanRule.selectSortOrder', '排序顺序')" align="left">
          <template #default="{ row }">
            <template v-if="!falg">
              <el-select v-model="row.type" placeholder="请选择" size="default">
                <el-option :label="td('dpp.cleanRule.ascending', '升序')" value="1"></el-option>
                <el-option :label="td('dpp.cleanRule.descending', '降序')" value="0"></el-option>
              </el-select>
            </template>
            <div v-else class="form-readonly">
              {{ row.type === "1" ? td('dpp.cleanRule.ascending', '升序') : row.type === "0" ? td('dpp.cleanRule.descending', '降序') : "-" }}
            </div>
          </template>
        </el-table-column>
        <template #empty>
          <div class="emptyBg">
            <p>{{ td('dpp.cleanRule.noData', '无数据') }}</p>
          </div>
        </template>
        <el-table-column v-if="!falg" :label="td('common.texts.operation', '操作')" align="center" width="100">
          <template #default="scope">
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDeletetypecolumns(scope.$index)"
              >{{ td('common.button.delete', '删除') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-form-item :label="td('dpp.cleanRule.dedupStrategy', '去重策略')" prop="handleType" style="margin-top: 20px">
      <el-radio-group
        v-model="form.handleType"
        class="strategy-radio-group"
        :disabled="falg"
      >
        <el-radio label="1" class="radio-item">
          <span class="radio-label">{{ td('dpp.cleanRule.keepFirst', '保留首条记录') }}</span>
        </el-radio>
        <p class="strategy-0ription">
          {{ td('dpp.cleanRule.keepFirstDesc', '系统将根据去重条件保留满足去重规则记录中的第一条记录。') }}
        </p>
        <el-radio label="2" class="radio-item">
          <span class="radio-label">{{ td('dpp.cleanRule.keepLatest', '保留最新记录') }}</span>
        </el-radio>
        <p class="strategy-0ription">
          {{ td('dpp.cleanRule.keepLatestDesc', '系统将根据去重条件保留满足去重规则记录中的最新记录。') }}
        </p>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script setup name="columnsCombiner">
import useDefaultLang from "@/composables/useDefaultLang"
import Sortable from "sortablejs";

const { td } = useDefaultLang();
const props = defineProps({
  form: Object,
  inputFields: Array,
  falg: Boolean,
});
const form = reactive({ ...props.form });
const exposedcolumnss = ["stringValue", "handleType"];
const data = Object.fromEntries(exposedcolumnss.map((key) => [key, form[key]])); // 添加排序字段，默认排序顺序为降序

let dragTable = ref(null);
let sortableInstance = null;
function setSort() {
  nextTick(() => {
    const tbody = dragTable.value?.$el.querySelector(
      ".el-table__body-wrapper tbody"
    );
    if (!tbody) {
      console.warn("tbody 找不到，拖拽初始化失败");
      return;
    }

    if (sortableInstance) {
      sortableInstance.destroy();
    }

    sortableInstance = Sortable.create(tbody, {
      handle: ".allowDrag",
      animation: 150,
      onEnd: (evt) => {
        const movedItem = form.stringValue.splice(evt.oldIndex, 1)[0];
        form.stringValue.splice(evt.newIndex, 0, movedItem);
        console.log(
          "拖拽后顺序:",
          form.stringValue.map((f) => f.sort)
        );
      },
    });
  });
}
const addtypecolumns = () => {
  form.stringValue.push({
    sort: form.stringValue.length,
    columns: "", // 字段名称
    type: "0", // 默认降序
  });
  setSort();
};
// 删除排序字段
const handleDeletetypecolumns = (index) => {
  form.stringValue.splice(index, 1);
  setSort();
};
// 判断字段是否已被其他行选择，禁用重复选项
const iscolumnsDisabled = (columnsName, currentRowId) => {
  return form.stringValue.some(
    (item) => item.columns === columnsName && item.id !== currentRowId
  );
};
const formRef = ref(null);
function validate() {
  return new Promise((resolve) => {
    formRef.value.validate((valid) => {
      if (!valid) {
        resolve({ valid: false });
        return;
      }

      // 如果没有添加排序字段，直接通过
      if (!form.stringValue || form.stringValue.length === 0) {
        resolve({
          valid: true,
          data,
        });
        return;
      }

      // 校验每个字段名称非空
      for (const item of form.stringValue) {
        if (!item.columns) {
          ElMessage.error(td('dpp.cleanRule.sortFieldNameRequired', '排序字段名称不能为空'));
          resolve({ valid: false });
          return;
        }
      }

      // 校验字段名称不重复
      const columnss = form.stringValue.map((item) => item.columns);
      const hasDuplicate = new Set(columnss).size !== columnss.length;
      if (hasDuplicate) {
        ElMessage.error(td('dpp.cleanRule.sortFieldNameDuplicate', '排序字段名称不能重复'));
        resolve({ valid: false });
        return;
      }

      // 只有数组有值才更新 sort
      if (form.stringValue && form.stringValue.length > 0) {
        form.stringValue.forEach((item, index) => {
          item.sort = index + 1;
        });
      }

      resolve({
        valid: true,
        data,
      });
    });
  });
}

setSort();
defineExpose({ validate });
</script>

<style scoped lang="scss">
.deduplication-config {
  padding-left: 57px;
}
.form-readonly {
  min-height: 32px;
  line-height: 32px;
}
</style>
