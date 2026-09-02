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
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="att/attCleanRule.list" />

    <el-container>
      <DeptTree
        :deptOptions="processedData"
        ref="DeptTreeRef"
        :leftWidth="leftWidth"
        :placeholder="td('att.common.ruleCategoryPlaceholder')"
        @node-click="handleNodeClick"
        :default-expand="true"
      />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <!-- 搜索栏插槽 -->
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
              :tableRef="tableRef"
            />
          </template>

          <!-- 数据操作按钮插槽 -->
          <template #actions-data>
            <el-row :gutter="15" class="btn-style">
              <!-- <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  @click="handleAdd"
                  v-hasPermi="['att:rule:attcleanrule:add']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-xincheng mr5"></i>{{ td('common.button.new') }}
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="info"
                  plain
                  icon="Upload"
                  @click="handleImport"
                  v-hasPermi="['att:rule:attcleanrule:import']"
                  @mousedown="(e) => e.preventDefault()"
                >{{ td('common.button.import') }}</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="Download"
                  @click="handleExport"
                  v-hasPermi="['att:rule:attcleanrule:export']"
                  @mousedown="(e) => e.preventDefault()"
                >{{ td('common.button.export') }}</el-button>
              </el-col> -->
            </el-row>
          </template>

          <!-- 表格组件 -->
          <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
            <!-- 自定义列展示 -->
            <template #catName="{ row }">
              {{ row.catName || '-' }}
            </template>
            <!-- 操作列插槽 -->
            <!-- <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                v-hasPermi="['att:rule:attcleanrule:edit']"
              >{{ td('common.button.edit') }}</el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['att:rule:attcleanrule:remove']"
              >{{ td('common.button.delete') }}</el-button>
            </template> -->
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- Add or modify cleaning rule dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="attCleanRuleRef" :model="form" :rules="rules" label-width="80px" @submit.prevent :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.ruleName')" prop="name">
              <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('att.common.code')" prop="code">
              <el-input v-model="form.code" :placeholder="td('att.common.codePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.ruleType')" prop="type">
              <el-tree-select
                v-model="form.type"
                :data="processedData"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('att.common.ruleTypePlaceholder')"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="validFlag">
              <el-radio-group v-model="form.validFlag">
                <el-radio :label="true">{{ td('att.common.enable') }}</el-radio>
                <el-radio :label="false">{{ td('att.common.disable') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.useCase')" prop="useCase">
              <el-input type="textarea" v-model="form.useCase" :placeholder="td('att.common.useCasePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('att.common.example')" prop="example">
              <el-input type="textarea" v-model="form.example" :placeholder="td('att.common.examplePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description">
              <el-input type="textarea" v-model="form.description" :placeholder="td('common.form.descriptionPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">{{ td('common.upload.dragOrClick') }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />{{ td('common.upload.updateExistingData') }}
            </div>
            <span>{{ td('common.upload.fileFormat') }}</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">{{ td('common.upload.downloadTemplate') }}</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CleanRule">
import { onMounted } from 'vue';
import {
  listAttCleanRule,
  getAttCleanRule,
  delAttCleanRule,
  addAttCleanRule,
  updateAttCleanRule
} from '@/api/att/rule/cleanRule';
import { getToken } from '@/utils/auth.js';
import DeptTree from '@/components/DeptTree';
import { listAttCleanCat } from "@/api/att/cat/cleanCat/cleanCat.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const leftWidth = ref(300);
const tableRef = ref(null);
const DeptTreeRef = ref(null);

const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const processedData = ref([]);
const dataMapCat = new Map();

const tableStore = reactive({
  config: {
    sort: true, // 启用排序功能
    initResquest: false, // 禁用自动初始化请求，在分类树加载后再手动触发
    table: {
      stripe: true,
      defaultSort: { prop: 'code', order: 'descending' }, // 使用 code 字段作为默认排序，避免 createTime 报错
      onSelectionChange: function (selection) {
        ids.value = selection.map((item) => item.id);
        single.value = selection.length != 1;
        multiple.value = !selection.length;
      },
    },
  },
  columns: [
    {
      label: td('common.texts.number'),
      prop: "code",
      width: 60,
      align: "left",
      sortable: true, // 启用编号排序
    },
    {
      label: td('att.cleanRule.table.name'),
      prop: "name",
      align: "left",
      width: 260,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('att.cleanRule.table.type'),
      prop: "catName",
      slot: "catName",
      align: "left",
      width: 180,
    },
    {
      label: td('common.texts.description'),
      prop: "description",
      align: "left",
      width: 256,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('att.cleanRule.table.useCase'),
      prop: "useCase",
      align: "left",
      width: 500,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('att.cleanRule.table.example'),
      prop: "example",
      align: "left",
      width: 600,
      showOverflowTooltip: { effect: 'light' },
    },
    // {
    //   label: td('common.texts.action'),
    //   slot: "action",
    //   width: 120,
    //   fixed: "right",
    // },
  ],
  func: async (params) => {
    const response = await listAttCleanRule(params);
    if (response.data && response.data.rows) {
      response.data.rows.forEach(obj => {
        obj.catName = dataMapCat.get(obj.type + "");
      });
    }
    return response;
  },
  params: {
    name: null,
    catCode: null,
    validFlag: true,
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('att.common.ruleName'),
      prop: "name",
      component: {
        is: "input",
        placeholder: td('att.common.ruleNamePlaceholder'),
      },
    },
  ],
});

function handleNodeClick(data) {
  tableStore.params.catCode = data.id === 0 ? null : data.code;
  handleQuery();
}

const open = ref(false);
const title = ref('');

/*** User import parameters */
const upload = reactive({
  open: false,
  title: '',
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: 'Bearer ' + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + '/att/attCleanRule/importData'
});

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('att.common.ruleNameRequired'), trigger: 'blur' }],
    type: [{ required: true, message: td('att.common.ruleTypeRequired'), trigger: 'change' }],
    code: [{ required: true, message: td('att.common.codeRequired'), trigger: 'change' }],
  }
});

const { form, rules } = toRefs(data);

// Cancel button
function cancel() {
  open.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    name: null,
    type: null,
    level: 1,
    description: null,
    validFlag: true,
    remark: null
  };
  proxy.resetForm('attCleanRuleRef');
}

/** Search button action */
function handleQuery() {
  tableStore.params.pageNum = 1;
}

/** reset button action */
function resetQuery() {
  DeptTreeRef.value?.resetTree?.();
  tableStore.params.catCode = null;
}

/** Add button operation */
function handleAdd() {
  reset();
  form.value.type = tableStore.params.catCode;
  open.value = true;
  title.value = td('att.cleanRule.title.add');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getAttCleanRule(_id).then((response) => {
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    open.value = true;
    title.value = td('att.cleanRule.title.edit');
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs['attCleanRuleRef'].validate((valid) => {
    if (valid) {
      const api = form.value.id != null ? updateAttCleanRule : addAttCleanRule;
      api(form.value)
        .then(() => {
          submitLoading.value = false;
          proxy.$modal.msgSuccess(td(form.value.id != null ? 'common.message.editSuccess' : 'common.message.addSuccess'));
          open.value = false;
          tableRef.value.getList();
        })
        .catch(() => {
          submitLoading.value = false;
        });
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('att.cleanRule.deleteConfirm', '', { ids: _ids }))
    .then(function () {
      return delAttCleanRule(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** Export button action */
function handleExport() {
  proxy.download(
    'att/attCleanRule/export',
    {
      ...tableStore.params
    },
    `attCleanRule_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
function handleImport() {
  upload.title = td('att.cleanRule.importTitle');
  upload.open = true;
}

function importTemplate() {
  proxy.download(
    'system/user/importTemplate',
    {},
    `attCleanRule_template_${new Date().getTime()}.xlsx`
  );
}

function submitFileForm() {
  proxy.$refs['uploadRef'].submit();
}

const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

const handleFileSuccess = (response, file) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs['uploadRef'].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    '</div>',
    td('att.common.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  tableRef.value.getList();
};

function getDeptTree() {
  listAttCleanCat({ validFlag: true }).then((response) => {
    response.data.forEach(obj => {
      dataMapCat.set(obj.id + "", obj.name);
    });
    processedData.value = proxy.handleTree(response.data, "id", "parentId");
    processedData.value = [
      {
        name: td('att.cleanRule.cleanRuleCategory'),
        value: "",
        id: 0,
        children: processedData.value,
      },
    ];
    tableRef.value.getList();
  });
}

onMounted(() => {
  getDeptTree();
});
</script>

<style scoped lang="scss">
.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
}
</style>
