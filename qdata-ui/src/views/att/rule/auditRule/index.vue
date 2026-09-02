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
    <GuideTip tip-id="att/attAuditRule.list" />

    <el-container>
      <DeptTree
        :deptOptions="processedData"
        :leftWidth="leftWidth"
        :placeholder="td('att.common.ruleCategoryPlaceholder')"
        @node-click="handleNodeClick"
        ref="DeptTreeRef"
        :default-expand="true"
      />

      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
              :tableRef="tableRef"
            />
          </template>
          <template #actions-data>
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  @click="handleAdd"
                  v-hasPermi="['att:rule:auditrule:add']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-xincheng mr5"></i>{{ td('common.button.new') }}
                </el-button>
              </el-col>
            </el-row>
          </template>
          <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                v-hasPermi="['att:rule:auditrule:edit']"
                >{{ td('common.button.edit') }}</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['att:rule:auditrule:remove']"
                >{{ td('common.button.delete') }}</el-button
              >
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
    <!-- Add or modify audit rule dialog box -->
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
      <el-form
        ref="attAuditRuleRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.ruleName')" prop="name">
              <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('att.common.code')" prop="code" :label-position="labelPosition">
              <el-input v-model="form.code" :placeholder="td('att.auditRule.form.codePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('att.auditRule.table.qualityDim')" prop="qualityDim" :label-position="labelPosition">
              <el-select v-model="form.qualityDim" :placeholder="td('att.common.qualityDimPlaceholder')">
                <el-option
                  v-for="dict in att_rule_audit_q_dimension"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.useCase')" prop="useCase" :label-position="labelPosition">
              <el-input
                type="textarea"
                v-model="form.useCase"
                :placeholder="td('att.common.useCasePlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('att.common.example')" prop="example" :label-position="labelPosition">
              <el-input
                type="textarea"
                v-model="form.example"
                :placeholder="td('att.common.examplePlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <el-input
                type="textarea"
                v-model="form.description"
                :placeholder="td('att.auditRule.form.descriptionPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm"
            >{{ td('common.button.confirm') }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AuditRule">
import {
  listAttAuditRule,
  getAttAuditRule,
  delAttAuditRule,
  addAttAuditRule,
  updateAttAuditRule,
} from "@/api/att/rule/auditRule";
import { computed } from "vue";
import DeptTree from "@/components/DeptTree";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { att_rule_audit_type, att_rule_level, att_rule_audit_q_dimension } =
  proxy.useDict(
    "att_rule_audit_type",
    "att_rule_level",
    "att_rule_audit_q_dimension"
  );
const leftWidth = ref(300); // Initial left width

const tableRef = ref(null);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const tableStore = reactive({
  config: {
    sort: true,
    initResquest: false, // 禁用自动初始化请求
    table: {
      stripe: true,
      defaultSort: { prop: "createTime", order: "descending" },
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
      sortable: true,
    },
    {
      label: td('att.auditRule.table.name'),
      prop: "name",
      align: "left",
      width: 260,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('att.auditRule.table.qualityDim'),
      prop: "qualityDim",
      dict: "att_rule_audit_q_dimension",
      align: "left",
      width: 160,
    },
    {
      label: td('common.texts.description'),
      prop: "description",
      align: "left",
      width: 256,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('att.auditRule.table.useCase'),
      prop: "useCase",
      align: "left",
      width: 500,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('att.auditRule.table.example'),
      prop: "example",
      align: "left",
      width: 700,
      showOverflowTooltip: { effect: 'light' },
    },
    {
      label: td('common.texts.action'),
      slot: "action",
      width: 120,
      fixed: "right",
    },
  ],
  func: listAttAuditRule,
  params: {
    name: null,
    qualityDim: "",
    validFlag: 1,
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

const processedData = computed(() => {
  return [
    {
      name: td('att.auditRule.treeRootName'),
      id: "",
      children: Array.isArray(att_rule_audit_q_dimension.value)
        ? att_rule_audit_q_dimension.value.map((item) => ({
            name: item.label,
            id: item.value,
            validFlag: true,
          }))
        : [],
    },
  ];
});

function handleNodeClick(data) {
  tableStore.params.qualityDim = data.id;
  handleQuery();
}

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const router = useRouter();

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('att.common.ruleNameRequired'), trigger: "blur" }],
    qualityDim: [
      { required: true, message: td('att.auditRule.qualityDimRequired'), trigger: "change" },
    ],
    code: [{ required: true, message: td('att.common.codeRequired'), trigger: "change" }],
  },
});

const { form, rules } = toRefs(data);

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    name: null,
    qualityDim: null,
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
    type: "5",
    level: "1",
  };
  proxy.resetForm("attAuditRuleRef");
}

/** Search button action */
function handleQuery() {
  tableStore.params.pageNum = 1;
  // qt-search-bar 会自动调用 tableRef.value.getList()
}
const DeptTreeRef = ref(null);
/** reset button action */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.qualityDim = "";
  // qt-search-bar 会自动调用 tableRef.value.resetQuery()
}

/** Add button operation */
function handleAdd() {
  reset();
  form.value.qualityDim = tableStore.params.qualityDim;
  form.value.type = "5";
  form.value.level = "1";
  open.value = true;
  title.value = td('att.auditRule.title.add');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getAttAuditRule(_id).then((response) => {
    //Filter out createTime
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    open.value = true;
    title.value = td('att.auditRule.title.edit');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttAuditRule(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('att.auditRule.title.detail');
  });
}

/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["attAuditRuleRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateAttAuditRule(form.value)
          .then((response) => {
            submitLoading.value = false;
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            tableRef.value.getList();
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      } else {
        addAttAuditRule(form.value)
          .then((response) => {
            submitLoading.value = false;
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            tableRef.value.getList();
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('att.auditRule.deleteConfirm', '', { ids: _ids }))
    .then(function () {
      return delAttAuditRule(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** Export button action */
function handleExport() {
  proxy.download(
    "att/auditRule/export",
    {
      ...tableStore.params,
    },
    `attAuditRule_${new Date().getTime()}.xlsx`
  );
}
</script>
<style scoped lang="scss">
.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}
</style>
