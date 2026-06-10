<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <!-- 资产字段 tab -->

  <qt-wrap
    :columns="tableStore.columns"
    :tableRef="tableRef"
    :config="{ fullContent: false, actions: { table: { search: false } } }"
  >
    <template #actions-data> </template>
    <qt-table v-bind="tableStore" ref="tableRef">
      <template #elementName="{ row }">
        <TagClamp
            :tags="
            Array.isArray(row?.terms)
              ? row.terms
              : row?.terms
              ? String(row.terms)
                  .split(',')
                  .map((v) => v.trim())
                  .filter(Boolean)
              : []
          "
            :max-lines="2"
        />
      </template>

      <template #nullableFlag="scope">
        <el-switch
          v-model="scope.row.nullableFlag"
          active-value="1"
          inactive-value="0"
          disabled
        />
      </template>
      <template #pkFlag="scope">
        <el-switch
          v-model="scope.row.pkFlag"
          active-value="1"
          inactive-value="0"
          disabled
        />
      </template>

      <template #handle="{ row }">
        <el-button
          link
          type="primary"
          icon="Edit"
          @click="handleUpdate(row)"
          v-hasPermi="['da:assetColumn:edit']"
          >{{ t('common.button.update') }}</el-button
        >
        <el-button link type="primary" icon="Pointer" @click="openBindTerm(row)">
          绑定术语
        </el-button>
      </template>
    </qt-table>
  </qt-wrap>

  <!-- 新增或修改数据资产对话框 -->
  <el-dialog
    :title="title"
    v-model="open"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="daAssetRef" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="中文名称" prop="columnComment">
            <el-input
              v-model="form.columnComment"
              placeholder="请输入中文名称"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="英文名称" prop="columnName">
            <el-input
              v-model="form.columnName"
              placeholder="请输入英文名称"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="字段类型" prop="columnType">
            <el-input
              v-model="form.columnType"
              placeholder="请输入字段类型"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="字段长度" prop="columnLength">
            <el-input
              v-model="form.columnLength"
              placeholder="请输入字段长度"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="小数位" prop="columnScale">
            <el-input-number
              :step="1"
              step-strictly
              v-model="form.columnScale"
              style="width: 100%"
              controls-position="right"
              :min="0"
              :max="100"
              placeholder="请输入小数长度"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否可空" prop="nullableFlag">
            <el-radio-group v-model="form.nullableFlag">
              <el-radio
                v-for="dict in dp_model_column_pk_flag"
                :key="dict.value"
                :value="dict.value"
                disabled
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.description')" prop="description">
            <el-input
              v-model="form.description"
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="t('common.form.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否主键" prop="pkFlag">
            <el-radio-group v-model="form.pkFlag">
              <el-radio
                v-for="dict in dp_model_column_pk_flag"
                :key="dict.value"
                :value="dict.value"
                disabled
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据分类" prop="dataCategoryId">
            <el-select
              v-model="form.dataCategoryId"
              filterable
              disabled
              clearable
              placeholder="请选择分类"
              @change="(val) => (form.dataCategoryId = val ?? null)"
            >
              <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                :disabled="!item.validFlag"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="关联代码表" prop="dataElemCodeFlag">
            <el-radio-group v-model="form.dataElemCodeFlag">
              <el-radio
                v-for="dict in dp_model_column_pk_flag"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择代码表" prop="dataElemCodeId">
            <el-select
              v-model="form.dataElemCodeId"
              :disabled="form.dataElemCodeFlag == '0'"
              clearable
              filterable
              placeholder="选择代码表"
            >
              <el-option
                v-for="item in codeTableList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="关联数据元" prop="relDataElmeFlag">
            <el-radio-group v-model="form.relDataElmeFlag">
              <el-radio
                v-for="dict in dp_model_column_pk_flag"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择数据元" prop="elementId">
            <el-select
              v-model="form.elementId"
              multiple
              :disabled="form.relDataElmeFlag == '0'"
              clearable
              filterable
              placeholder="请选择数据元"
            >
              <el-option
                v-for="item in elementList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.remark')" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              maxlength="500个字符"
              show-word-limit
              :placeholder="t('common.form.remarkPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">{{ t('common.button.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ t('common.button.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog
      title="绑定术语"
      v-model="termDialogVisible"
      width="600px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
  >
    <el-form ref="termFormRef" :model="termForm">
      <el-col :span="24">
        <el-form-item
            label="术语"
            prop="termIds"
            :rules="[
            {
              required: true,
              message: '请选择术语',
              trigger: ['blur', 'change'],
            },
          ]"
            class="basicAttr hasMsg"
        >
          <el-select
              v-model="termForm.termIds"
              placeholder="请选择术语"
              filterable
              style="width: calc(100% - 117px)"
          >
            <el-option
                v-for="item in termOptions"
                :key="item.id + ''"
                :label="item.name"
                :value="item.id + ''"
            ></el-option>
          </el-select>
          <el-button
              type="primary"
              style="margin-left: 14px"
              plain
              @click="openAddTerm"
              @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng mr5"></i>新增术语
          </el-button>
          <span class="msg">
          为当前数据字段指定统一的业务定义，显示字段的真实业务含义
          </span>
        </el-form-item>
      </el-col>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="termDialogVisible = false">{{ t('common.button.cancel') }}</el-button>
        <el-button
            type="primary"
            :loading="termSubmitting"
            @click="submitBindTerm"
        >{{ t('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
  <el-dialog
      :title="termAddTitle"
      v-model="termAddOpen"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
  >
    <template #header>
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ termAddTitle }}
      </span>
    </template>
    <el-form
        ref="termAddRef"
        :model="termAddForm"
        :rules="termAddRules"
        label-width="110px"
        @submit.prevent
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="业务术语类目" prop="catCode">
            <el-tree-select
                filterable
                v-model="termAddForm.catCode"
                :data="termCatOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }"
                value-key="ID"
                placeholder="请选择业务术语类目"
                check-strictly
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="术语名称" prop="name">
            <el-input v-model="termAddForm.name" placeholder="请输入术语名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              label="近义词"
              prop="nearSynonyms"
              tip="如果有多个请用英文逗号分隔"
          >
            <el-input
                v-model="termAddForm.nearSynonyms"
                placeholder="请输入近义词"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
              label="同义词"
              prop="synonyms"
              tip="如果有多个请用英文逗号分隔"
          >
            <el-input
                v-model="termAddForm.synonyms"
                placeholder="请输入同义词"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <qt-form-item
              :label="t('common.texts.status')"
              prop="status"
              :tip="{
              content:
                '启用状态表示该标签可用于打标数据术语；禁用后无法再被使用，但已有标签仍保留。',
            }"
          >
            <el-radio-group v-model="termAddForm.status">
              <el-radio
                  v-for="dict in dp_model_status"
                  :key="dict.value"
                  :label="dict.value"
              >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </qt-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.description')" prop="description">
            <el-input
                v-model="termAddForm.description"
                type="textarea"
                :placeholder="t('common.form.descriptionPlaceholder')"
                maxlength="500个字符"
                show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.remark')" prop="remark">
            <el-input
                v-model="termAddForm.remark"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                :placeholder="t('common.form.remarkPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancelAddTerm">{{ t('common.button.cancel') }}</el-button>
        <el-button
            type="primary"
            size="mini"
            :loading="termAddSubmitting"
            @click="submitAddTerm"
        >{{ t('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>

  <!-- 数据资产详情对话框 -->
  <el-dialog
    :title="title"
    v-model="openDetail"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="daAssetRef" :model="form" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="中文名称" prop="columnComment">
            <div>
              {{ form.columnComment }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="英文名称" prop="columnName">
            <div>
              {{ form.columnName }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="字段类型" prop="columnType">
            <div>
              {{ form.columnType }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="小数位" prop="columnScale">
            <div>
              {{ form.columnScale }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否可空" prop="nullableFlag">
            <div>
              {{ form.nullableFlag }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否主键" prop="pkFlag">
            <div>
              {{ form.pkFlag }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="敏感等级" prop="sensitiveLevelName">
            <div>
              {{ form.sensitiveLevelName }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联代码表" prop="dataElemCodeFlag">
            <div>
              {{ form.dataElemCodeFlag }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="关联数据元" prop="relDataElmeFlag">
            <div>
              {{ form.relDataElmeFlag }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.texts.description')" prop="description">
            <div>
              {{ form.description }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="字段描述" prop="remark">
            <div>
              {{ form.remark }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="cancel">{{ t('common.button.close') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
import { useI18n } from 'vue-i18n'
import {
  listDaAsset,
  getDaAsset,
  delDaAsset,
  addDaAsset,
  updateDaAsset,
} from "@/api/da/asset/asset";
import {
  listDaAssetColumn,
  getDaAssetColumn,
  updateDaAssetColumn,
} from "@/api/da/asset/assetColumn.js";
import { listDaSensitiveLevel } from "@/api/da/security/sensitiveLevel/sensitiveLevel";
import { listDpDataElem } from "@/api/dp/dataElem/dataElem";
import { listDataCategoryAll } from "@/api/dg/safety/dataCategory/dataCategory.js";
import { listAttTag, addAttTag } from "@/api/da/term/tag.js";
import { listAttTagCat } from "@/api/da/term/tagCat.js";
import { addTermColumnRel } from "@/api/da/term/termColumnRel.js";
import { useRoute } from "vue-router";
import { ref } from "vue";
import TagClamp from "@/components/TagClamp";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { column_type, dp_model_column_pk_flag, dp_model_column_nullable_flag } =
  proxy.useDict(
    "column_type",
    "dp_model_column_pk_flag",
    "dp_model_column_nullable_flag"
  );
const { dp_model_status } = proxy.useDict("dp_model_status");
const tableRef = ref(null);
const daAssetColumnList = ref([]);
const categoryList = ref([]);
const codeTableList = ref([]); //代码表
const elementList = ref([]); //数据元
const defaultSort = ref({ prop: "createTime", order: "descending" });
const termDialogVisible = ref(false);
const termFormRef = ref(null);
const termForm = reactive({ termIds: "" });
const termOptions = ref([]);
const currentColumnId = ref(null);
const termSubmitting = ref(false);
const termAddOpen = ref(false);
const termAddTitle = ref("新增术语");
const termAddRef = ref(null);
const termCatOptions = ref([]);
const termAddSubmitting = ref(false);
const termAddForm = reactive({
  id: null,
  name: null,
  description: null,
  catCode: null,
  aeestCount: null,
  status: "1",
  nearSynonyms: null,
  synonyms: null,
  remark: null,
});
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const data = reactive({
  daAssetDetail: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    catCode: null,
    themeId: null,
    datasourceId: null,
    tableName: null,
    tableComment: null,
    dataCount: null,
    fieldCount: null,
    STATUS: null,
    description: null,
    createTime: null,
  },
  rules: {
    // columnComment: [{ required: true, message: '请输入中文名称', trigger: 'blur' }],
    // columnName: [{ required: true, message: '请输入英文名称', trigger: 'blur' }],
    dataElemCodeId: [
      { required: false, message: "请选择代码表", trigger: "blur" },
    ],
    elementId: [{ required: false, message: "请选择数据元", trigger: "blur" }],
    // pkFlag: [{ required: true, message: '请选择是否主键', trigger: 'blur' }],
    // nullableFlag: [{ required: true, message: '请选择是是否必填', trigger: 'blur' }],
    // dataElemCodeFlag: [{ required: true, message: '请选择是是否必填', trigger: 'blur' }],
    // relDataElmeFlag: [
    //     { required: true, message: '请选择是是否关联数据元', trigger: 'blur' }
    // ],
    // relCleanFlag: [
    //     { required: true, message: '请选择是是否关联清洗规则', trigger: 'blur' }
    // ],
    relAuditFlag: [
      { required: true, message: "请选择是是否关联稽查规则", trigger: "blur" },
    ],
  },
});

const { queryParams, form, daAssetDetail, rules } = toRefs(data);
const route = useRoute();
let assetId = route.query.id || 1;
watch(
  () => route.query.id,
  (newId) => {
    assetId = newId || 1;
    getList();
  },
  { immediate: true }
);

/** 查询数据资产列表 */
function getList() {
  queryParams.value.assetId = assetId;
  if (tableRef.value) {
    tableRef.value.getList();
  }
}

/** 获取敏感等级列表 */
function getDaSensitiveLevelList() {
  listDataCategoryAll().then((response) => {
    categoryList.value = response.data;
    //将id转换为String类型
    categoryList.value.forEach((item) => {
      item.id = item.id.toString();
    });
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    catCode: null,
    themeId: null,
    datasourceId: null,
    tableName: null,
    tableComment: null,
    dataCount: null,
    fieldCount: null,
    STATUS: null,
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("daAssetRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件（由 qt-table 内部处理，此处保留占位） */
function handleSortChange() {}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增数据资产";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAssetColumn(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改数据资产";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAsset(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "数据资产详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["daAssetRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        delete form.value.updateTime;

        updateDaAssetColumn(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(t('common.message.editSuccess'));
            open.value = false;
            if (tableRef.value) tableRef.value.getList();
          })
          .catch((error) => {});
      } else {
        addDaAsset(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(t('common.message.addSuccess'));
            open.value = false;
            if (tableRef.value) tableRef.value.getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除数据资产编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDaAsset(_ids);
    })
    .then(() => {
      if (tableRef.value) tableRef.value.getList();
      proxy.$modal.msgSuccess(t('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "da/asset/export",
    {
      ...queryParams.value,
    },
    `daAsset_${new Date().getTime()}.xlsx`
  );
}

/** 获取数据元列表 */
function getElementList() {
  listDpDataElem({ type: 1 }).then((response) => {
    elementList.value = response.data.rows;
  });
}
/** 获取代码表列表 */
function getCodeTableList() {
  listDpDataElem({ type: 2 }).then((response) => {
    codeTableList.value = response.data.rows;
    codeTableList.value.forEach((item) => {
      item.id = item.id.toString();
    });
  });
}

function openBindTerm(row) {
  proxy.$modal.msgWarning("功能正在开发中");

  // currentColumnId.value = row.id;
  // Promise.all([getDaAssetColumn(row.id), getTermList()]).then(([detailRes]) => {
  //   const detail = detailRes?.data || {};
  //   let termId = ""; // 单选用单个值
  //
  //   if (Array.isArray(detail.termIds)) {
  //     termId = detail.termIds[0] || ""; // 取第一个
  //   } else if (typeof detail.termIds === "string") {
  //     termId = detail.termIds.split(",")[0] || "";
  //   } else if (Array.isArray(row.termIds)) {
  //     termId = row.termIds[0] || "";
  //   } else if (typeof row.termIds === "string") {
  //     termId = row.termIds.split(",")[0] || "";
  //   }
  //
  //   termForm.termIds = termId; // 直接赋值单个值
  //   termDialogVisible.value = true;
  // });
}

function getTermList() {
  return listAttTag({ pageSize: 1000 }).then((response) => {
    const rows = response?.data?.rows || response?.data || [];
    termOptions.value = rows.sort(
        (a, b) => new Date(b.createTime) - new Date(a.createTime)
    );
  });
}

function submitBindTerm() {
  if (!termFormRef.value) return;
  termFormRef.value.validate((valid) => {
    if (!valid) return;

    // 单选判断
    if (!termForm.termIds) {
      proxy?.$message?.warning("请选择术语");
      return;
    }

    const payload = {
      termIds: [termForm.termIds], // 后端如果要数组就包一层，不要就直接传 termForm.termIds
      columnId: currentColumnId.value + "",
    };

    termSubmitting.value = true;
    addTermColumnRel(payload)
        .then(() => {
          termDialogVisible.value = false;
          tableRef?.value?.getList?.();
        })
        .finally(() => {
          termSubmitting.value = false;
        });
  });
}

function openAddTerm() {
  resetAddTerm();
  loadTermCatTree();
  termAddOpen.value = true;
}

function cancelAddTerm() {
  termAddOpen.value = false;
  resetAddTerm();
}

function resetAddTerm() {
  Object.assign(termAddForm, {
    id: null,
    name: null,
    description: null,
    catCode: null,
    aeestCount: null,
    status: "1",
    nearSynonyms: null,
    synonyms: null,
    remark: null,
  });
  if (termAddRef.value) {
    termAddRef.value.resetFields && termAddRef.value.resetFields();
  }
}

function loadTermCatTree() {
  listAttTagCat({ validFlag: true }).then((response) => {
    termCatOptions.value = proxy.handleTree(response.data, "id", "parentId");
    termCatOptions.value = [
      {
        name: "业务术语类目",
        value: "",
        id: 0,
        children: termCatOptions.value,
      },
    ];
  });
}

function submitAddTerm() {
  if (!termAddRef.value) return;
  termAddRef.value.validate((valid) => {
    if (!valid) return;
    termAddSubmitting.value = true;
    addAttTag(termAddForm)
        .then(() => {
          proxy.$modal.msgSuccess(t('common.message.addSuccess'));
          termAddOpen.value = false;
          resetAddTerm();
          // 新增后仅刷新术语列表，不自动选择
          return getTermList();
        })
        .finally(() => {
          termAddSubmitting.value = false;
        });
  });
}

getElementList();
getCodeTableList();
getDaSensitiveLevelList();

// qt-table 配置
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: defaultSort.value,
    },
  },
  columns: [
    {
      label: t('common.texts.number'),
      prop: "id",
      sortable: true,
      width: 60,
    },

    {
      label: "中文名称",
      prop: "columnComment",
      width: 185,
      showOverflowTooltip: true,
    },
    {
      label: "英文名称",
      prop: "columnName",
      width: 180,
      showOverflowTooltip: true,
    },
    {
      label: t('common.texts.description'),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "字段类型",
      prop: "columnType",
      width: 140,
      showOverflowTooltip: true,
    },
    {
      label: "字段长度",
      prop: "columnLength",
      width: 80,
      showOverflowTooltip: true,
    },
    {
      label: "小数位",
      prop: "columnScale",
      width: 80,
      showOverflowTooltip: true,
    },
    {
      label: "是否可空",
      prop: "nullableFlag",
      width: 80,
      slot: "nullableFlag",
    },
    {
      label: "是否主键",
      prop: "pkFlag",
      width: 100,
      slot: "pkFlag",
    },

    {
      label: "数据分类",
      prop: "dataCategoryName",
      width: 100,
    },
    {
      label: "字段术语",
      align: "left",
      width: 200,
      showOverflowTooltip: { effect: "light" },
      slot: "elementName",
    },
    {
      label: "关联代码表",
      prop: "dataElemCodeName",
      width: 200,
      showOverflowTooltip: { effect: "light" },

      slot: "dataElemCodeName",
    },
    {
      label: "关联数据元",
      prop: "relDataElmeName",
      width: 200,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: t('common.texts.createdBy'),
      prop: "createBy",
      width: 120,
    },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },

    {
      label: t('common.texts.operation'),
      width: 180,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listDaAssetColumn,
  params: {
    assetId: null,
  },
  events: {
    formatParams(params) {
      if (params.isAsc === "descending") params.isAsc = "desc";
      if (params.isAsc === "ascending") params.isAsc = "asc";
      // 默认排序键兼容
      if (params.orderByColumn === "createTime")
        params.orderByColumn = "create_time";
      params.assetId = assetId;
      return params;
    },
  },
});
</script>
