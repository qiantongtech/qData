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

    <el-container style="90%">
      <DeptTree
        :deptOptions="processedData"
        :leftWidth="leftWidth"
        :placeholder="td('att.common.ruleCategoryPlaceholder')"
        @node-click="handleNodeClick"
        ref="DeptTreeRef"
        :default-expand="true"
      />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
            @submit.prevent
          >
            <el-form-item :label="td('att.common.ruleName')" prop="name" :label-position="labelPosition">
              <el-input
                class="el-form-input-width"
                v-model="queryParams.name"
                :placeholder="td('att.common.ruleNamePlaceholder')"
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                plain
                type="primary"
                @click="handleQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
              </el-button>
              <el-button
                @click="resetQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div class="pagecont-bottom">
          <div class="justify-between mb15">
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <!-- <el-button type="primary" plain @click="handleAdd"
                                    v-hasPermi="['att:rule:auditrule:add']" @mousedown="(e) => e.preventDefault()">
                                    <i class="iconfont-mini icon-xincheng mr5"></i>New
                                </el-button> -->
              </el-col>
              <!--                            <el-col :span="1.5">-->
              <!--                                <el-button type="primary" plain :disabled="single" @click="handleUpdate"-->
              <!--                                    v-hasPermi="['att:rule:auditrule:edit']" @mousedown="(e) => e.preventDefault()">-->
              <!--                                    <i class="iconfont-mini icon-xiugai--copy mr5"></i>Modify-->
              <!--                                </el-button>-->
              <!--                            </el-col>-->
              <!--                            <el-col :span="1.5">-->
              <!--                                <el-button type="danger" plain :disabled="multiple" @click="handleDelete"-->
              <!--                                    v-hasPermi="['att:rule:auditrule:remove']" @mousedown="(e) => e.preventDefault()">-->
              <!--                                    <i class="iconfont-mini icon-shanchu-huise mr5"></i>Delete-->
              <!--                                </el-button>-->
              <!--                            </el-col>-->
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar
                v-model:showSearch="showSearch"
                @queryTable="getList"
                :columns="columns"
              ></right-toolbar>
            </div>
          </div>
          <el-table
            stripe
            v-loading="loading"
            :data="attAuditRuleList"
            @selection-change="handleSelectionChange"
            :default-sort="defaultSort"
            @sort-change="handleSortChange"
          >
            <el-table-column
              v-if="getColumnVisibility(6)"
              :label="td('common.texts.number')"
              align="left"
              prop="code"
              width="80"
            >
              <template #default="scope">
                {{ scope.row.code || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(1)"
              :label="td('att.auditRule.table.name')"
              align="left"
              prop="name"
              :show-overflow-tooltip="{ effect: 'light' }"
              width="200"
            >
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(2)"
              :label="td('att.auditRule.table.qualityDim')"
              align="left"
              prop="qualityDim"
              :show-overflow-tooltip="{ effect: 'light' }"
              width="160"
            >
              <template #default="scope">
                <dict-tag
                  :options="att_rule_audit_q_dimension"
                  :value="scope.row.qualityDim"
                />
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="{ effect: 'light' }"
              v-if="getColumnVisibility(5)"
              :label="td('common.texts.description')"
              width="400"
              align="left"
              prop="description"
            >
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(3)"
              :label="td('att.auditRule.table.useCase')"
              width="500"
              align="left"
              prop="level"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                {{ scope.row.useCase || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(4)"
              :label="td('att.auditRule.table.example')"
              width="700"
              align="left"
              prop="type"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                {{ scope.row.example || "-" }}
              </template>
            </el-table-column>

            <!-- <el-table-column
                            :show-overflow-tooltip="{effect: 'light'}"
                            v-if="getColumnVisibility(14)"
                            :label="td('common.texts.remark')"
                            align="left"
                            prop="remark"
                        >
                            <template #default="scope">
                                {{ scope.row.remark || '-' }}
                            </template>
                        </el-table-column> -->
            <!-- <el-table-column label="Operation" align="left" class-name="small-padding fixed-width" fixed="right"
                            width="120">
                            <template #default="scope">
                                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                                    v-hasPermi="['att:rule:auditrule:edit']">Edit</el-button>
                                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                                    v-hasPermi="['att:rule:auditrule:remove']">Delete</el-button>
                            </template>
                        </el-table-column> -->

            <template #empty>
              <div class="emptyBg">
                <img
                  src="../../../../assets/images/system/no_data/empty-nodata.png"
                  alt=""
                />
                <p>{{td('common.noData')}}</p>
              </div>
            </template>
          </el-table>

          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
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
          <el-button type="primary" size="mini" @click="submitForm"
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
import { getToken } from "@/utils/auth.js";
import { computed } from "vue";
import DeptTree from "@/components/DeptTree";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { att_rule_audit_type, att_rule_level, att_rule_audit_q_dimension } =
  proxy.useDict(
    "att_rule_audit_type",
    "att_rule_level",
    "att_rule_audit_q_dimension"
  );
const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed // Initial left width
let Materialization = ref(false);
const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // Calculate mouse movement distance
    leftWidth.value += delta; // Modify left width
    startX = event.clientX; // Update starting position
    // Use requestAnimationFrame to reduce page redraw frequency
    requestAnimationFrame(() => {});
  }
};
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
const attAuditRuleList = ref([]);
function handleNodeClick(data) {
  queryParams.value.qualityDim = data.id;
  queryParams.value.pageNum = 1;
  handleQuery();
}
// Show hidden information
const columns = ref([
  { key: 6, label: td('common.texts.number'), visible: true },
  { key: 1, label: td('att.auditRule.texts.name'), visible: true },
  { key: 2, label: td('att.auditRule.texts.qualityDim'), visible: true },
  { key: 5, label: td('common.texts.description'), visible: true },
  { key: 3, label: td('att.auditRule.texts.scenario'), visible: true },
  { key: 4, label: td('att.auditRule.texts.example'), visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
  return column.visible;
};

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

/*** User import parameters */
const upload = reactive({
  // Whether to display the pop-up layer (user import)
  open: false,
  // Popup layer title (user imported)
  title: "",
  // Whether to disable uploading
  isUploading: false,
  // Whether to update existing user data
  updateSupport: 0,
  // Set upload request headers
  headers: { Authorization: "Bearer " + getToken() },
  // Upload address
  url: import.meta.env.VITE_APP_BASE_API + "/att/auditRule/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    qualityDim: "",
    type: "",
    code: "",
  },
  rules: {
    name: [{ required: true, message: td('att.common.ruleNameRequired'), trigger: "blur" }],
    qualityDim: [
      { required: true, message: td('att.auditRule.qualityDimRequired'), trigger: "change" },
    ],
    // type: [{ required: true, message: td('att.common.ruleTypeRequired'), trigger: 'change' }],
    code: [{ required: true, message: td('att.common.codeRequired'), trigger: "change" }],
    // level: [{ required: true, message: 'Rule level cannot be empty', trigger: 'change' }]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** Query the list of audit rules */
function getList() {
  loading.value = true;
  listAttAuditRule({ ...queryParams.value, validFlag: 1 }).then((response) => {
    attAuditRuleList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

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
  queryParams.value.pageNum = 1;
  getList();
}
const DeptTreeRef = ref(null);
/** reset button action */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.qualityDim = "";
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  handleQuery();
}

// Multiple selection box selected data
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Sorting trigger events */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** Add button operation */
function handleAdd() {
  reset();
  form.value.qualityDim = queryParams.value.qualityDim;
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
  proxy.$refs["attAuditRuleRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateAttAuditRule(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addAttAuditRule(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('att.auditRule.deleteConfirm').replace('<ids>', _ids))
    .then(function () {
      return delAttAuditRule(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** Export button action */
function handleExport() {
  proxy.download(
    "att/auditRule/export",
    {
      ...queryParams.value,
    },
    `attAuditRule_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
  upload.title = td('att.auditRule.importTitle');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `attAuditRule_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**File upload is being processed */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** File upload successfully processed */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td('att.common.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id,
        },
      });
    }
  }
}

getList();
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
