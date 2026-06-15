<template>
  <div class="container">
    <qt-wrap
      :columns="tableStroe.columns"
      :tableRef="tableRef"
      :config="{ fullContent: false, actions: { table: { search: false } } }"
    >
      <qt-table v-bind="tableStroe" ref="tableRef">
        <template #status="scope">
          <el-switch
            v-if="scope.row.status != undefined"
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row, $event)"
          />
        </template>
        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetailClick(row)"
          >
            {{ td("common.button.details") }}
          </el-button>
          <template v-if="detail.status == '1' && route.query.table_status">
            <el-button
              link
              type="primary"
              :disabled="row.status == 1"
              icon="Edit"
              @click="handleEditClick(row)"
            >
              {{ td("common.button.update") }}
            </el-button>
            <el-button
              link
              type="danger"
              icon="Delete"
              :disabled="row.status == 1"
              @click="handleDeleteClick(row)"
            >
              {{ td("common.button.delete") }}
            </el-button>
          </template>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog
      v-model="dialog.open"
      :title="dialog.title"
      width="1200"
      draggable
    >
      <el-form
        :model="dialog.form"
        :rules="rules"
        ref="formRef"
        class="column-form"
        label-width="110px"
        :disabled="dialog.type == 'Detail'"
      >
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.columnName')"
          prop="columnName"
        >
          <el-input
            clearable
            v-model="dialog.form.columnName"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td('meta.unreleased.structured.table.detail.columnName'),
              })
            "
          />
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.columnComment')"
          prop="columnComment"
        >
          <el-input
            clearable
            v-model="dialog.form.columnComment"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td(
                  'meta.unreleased.structured.table.detail.columnComment'
                ),
              })
            "
          />
        </el-form-item>
        <!-- <el-form-item label="安全等级" prop="safetyLevelId">
          <el-select
            clearable
            v-model="dialog.form.safetyLevelId"
            placeholder="请选择安全等级"
          >
            <el-option
              v-for="item in store.sensitiveLevels"
              :key="item.id"
              :label="item.sensitiveLevel"
              :value="item.id"
            />
          </el-select>
        </el-form-item> -->
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.dataElem')"
          prop="dataElemId"
        >
          <el-select
            clearable
            v-model="dialog.form.dataElemId"
            :placeholder="
              td('common.form.selectPlaceholder', {
                field: td('meta.unreleased.structured.table.detail.dataElem'),
              })
            "
          >
            <el-option
              v-for="item in store.dataElemList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.columnType')"
          prop="columnType"
        >
          <el-select
            clearable
            v-model="dialog.form.columnType"
            :placeholder="
              td('common.form.selectPlaceholder', {
                field: td('meta.unreleased.structured.table.detail.columnType'),
              })
            "
          >
            <el-option
              v-for="dict in toValue(dicts.column_type)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.columnLength')"
          prop="columnLength"
        >
          <el-input-number
            :min="0"
            v-model="dialog.form.columnLength"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td(
                  'meta.unreleased.structured.table.detail.columnLength'
                ),
              })
            "
            :controls="true"
            class="number-input"
            controls-position="right"
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.columnPrecision')"
          prop="columnPrecision"
        >
          <el-input-number
            :min="0"
            v-model="dialog.form.columnPrecision"
            :placeholder="td('meta.unreleased.structured.table.detail.columnPrecisionPlaceholder')"
            :controls="true"
            class="number-input"
            controls-position="right"
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.columnScale')"
          prop="columnScale"
        >
          <el-input-number
            :min="0"
            v-model="dialog.form.columnScale"
            :placeholder="
              td('meta.unreleased.structured.table.detail.columnScalePlaceholder')
            "
            :controls="true"
            class="number-input"
            controls-position="right"
          />
        </el-form-item>

        <el-form-item
          :label="
            td('meta.unreleased.structured.table.detail.businessDefinition')
          "
          prop="businessDefinition"
        >
          <el-input
            clearable
            v-model="dialog.form.businessDefinition"
            :placeholder="
              td('meta.unreleased.structured.table.detail.businessDefinitionPlaceholder')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.measuringUnit')"
          prop="measuringUnit"
        >
          <el-input
            clearable
            v-model="dialog.form.measuringUnit"
            :placeholder="
              td('meta.unreleased.structured.table.detail.measuringUnitPlaceholder')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.nullableFlag')"
          prop="nullableFlag"
        >
          <el-radio-group v-model="dialog.form.nullableFlag">
            <el-radio
              v-for="dict in toValue(dicts.table_yes_no)"
              :key="dict.value"
              :value="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.defaultValue')"
          prop="defaultValue"
        >
          <el-input
            clearable
            v-model="dialog.form.defaultValue"
            :placeholder="
              td('meta.unreleased.structured.table.detail.defaultValuePlaceholder')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.pkFlag')"
          prop="pkFlag"
        >
          <el-radio-group v-model="dialog.form.pkFlag">
            <el-radio
              v-for="dict in toValue(dicts.table_yes_no)"
              :key="dict.value"
              :value="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.fkFlag')"
          prop="fkFlag"
        >
          <el-radio-group v-model="dialog.form.fkFlag">
            <el-radio
              v-for="dict in toValue(dicts.table_yes_no)"
              :key="dict.value"
              :value="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="td('common.texts.status')" prop="status">
          <el-radio-group v-model="dialog.form.status">
            <el-radio
              v-for="dict in toValue(dicts.meta_task_status)"
              :key="dict.value"
              :value="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="td('common.texts.remark')" class="row-full">
          <el-input
            v-model="dialog.form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>

        <el-form-item :label="td('common.texts.description')" class="row-full">
          <el-input
            v-model="dialog.form.description"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.updateMsg')"
          class="row-full"
          prop="updateMsg"
          v-if="dialog.type != 'Detail'"
        >
          <el-input
            v-model="dialog.form.updateMsg"
            type="textarea"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td('meta.unreleased.structured.table.detail.updateMsg'),
              })
            "
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancelClick" v-if="dialog.type != 'Detail'">
            {{ td("common.button.cancel") }}
          </el-button>
          <el-button @click="handleCancelClick" v-if="dialog.type == 'Detail'">
            {{ td("common.button.close") }}
          </el-button>
          <el-button
            type="primary"
            v-if="dialog.type != 'Detail'"
            @click="handleConfirmClick"
          >
            {{ td("common.button.confirm") }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ColumnList">
import useDefaultLang from "@/composables/useDefaultLang";
import { getCurrentInstance, reactive, ref, toValue } from "vue";
import {
  listColumn,
  delColumn,
  getColumn,
  updateColumn,
  updateColumnStatus,
} from "@/api/mc/unreleased/column.js";
import { useRoute, useRouter } from "vue-router";
import { listDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";

const { td } = useDefaultLang();
// import { getDgDataElemList } from "@/api/dg/standard/dataElem.js";

const BASE_URL = "/meta/unreleased/structured/column";

const rules = {
  columnName: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.columnName") +
        td("common.message.required"),
      trigger: "change",
    },
    {
      pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/,
      message:
        td("meta.unreleased.structured.table.detail.columnName") +
        "必须以字母开头，可包含字母、数字和下划线",
      trigger: "blur",
    },
  ],
  columnType: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.columnType") +
        td("common.message.required"),
      trigger: "change",
    },
  ],
  columnLength: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.columnLength") +
        td("common.message.required"),
      trigger: ["change", "blur"],
    },
  ],
  columnScale: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.columnScale") +
        td("common.message.required"),
      trigger: ["change", "blur"],
    },
  ],
  updateMsg: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.updateMsg") +
        td("common.message.required"),
      trigger: ["change", "blur"],
    },
  ],
  businessDefinition: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.businessDefinition") +
        td("common.message.required"),
      trigger: "blur",
    },
  ],
  columnPrecision: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.columnPrecision") +
        td("common.message.required"),
      trigger: ["change", "blur"],
    },
  ],
  pkFlag: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.pkFlag") +
        td("common.message.required"),
      trigger: "change",
    },
  ],
  fkFlag: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.fkFlag") +
        td("common.message.required"),
      trigger: "change",
    },
  ],
  nullableFlag: [
    {
      required: true,
      message:
        td("meta.unreleased.structured.table.detail.nullableFlag") +
        td("common.message.required"),
      trigger: "change",
    },
  ],
};

const props = defineProps({
  detail: {
    type: Object,
    required: true,
  },
});

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "meta_task_status",
  "meta_dw_layers",
  "table_yes_no",
  "column_type"
);

const router = useRouter();
const route = useRoute();

const store = reactive({});

const formRef = ref();
const tableRef = ref();
const tableStroe = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "createTime", order: "descending" },
      onRowDblclick: handleDetailClick,
    },
  },
  columns: [
    {
      label: td("common.texts.number"),
      prop: "id",
      sortable: true,
      width: 70,
    },
    {
      label: td("meta.unreleased.structured.table.detail.columnName"),
      prop: "columnName",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 140,
      link: {
        external: handleDetailClick,
      },
    },
    {
      label: td("meta.unreleased.structured.table.detail.columnComment"),
      prop: "columnComment",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 140,
    },

    {
      label: td("meta.unreleased.structured.table.detail.dataElem"),
      prop: "dataElemName",
      width: 110,
    },
    {
      label: td("meta.unreleased.structured.table.detail.columnType"),
      prop: "columnType",
      width: 110,
      dict: "column_type",
    },

    {
      label: td("meta.unreleased.structured.table.detail.columnLength"),
      prop: "columnLength",
      width: 100,
      sortable: true,
    },
    {
      label: td("meta.unreleased.structured.table.detail.columnPrecision"),
      prop: "columnPrecision",
      width: 100,
      sortable: true,
    },
    {
      label: td("meta.unreleased.structured.table.detail.columnScale"),
      prop: "columnScale",
      width: 100,
      sortable: true,
    },

    {
      label: td("meta.unreleased.structured.table.detail.nullableFlag"),
      prop: "nullableFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: td("meta.unreleased.structured.table.detail.pkFlag"),
      prop: "pkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: td("meta.unreleased.structured.table.detail.fkFlag"),
      prop: "fkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: td("common.texts.status"),
      prop: "status",
      width: 90,
      slot: "status",
      invisible: route.query.released,
    },

    {
      label: td("common.texts.updatedBy"),
      prop: "updateBy",
      width: 120,
    },
    {
      label: td("common.texts.updatedTime"),
      prop: "updateTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: td("common.texts.createdBy"),
      prop: "createBy",
      width: 120,
    },
    {
      label: td("common.texts.createdTime"),
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: td("common.texts.operation"),
      width: 220,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listColumn,
  params: {
    tableId: props.detail.id,
    status: route.query.table_status ? "" : props.detail.status,
    dataType: 1,
  },
});

const dialog = reactive({
  open: false,
  form: {},
});

// 获取安全等级
function getSensitiveLevel() {
  listDgSensitiveLevel({ pageSize: 1000 }).then((res) => {
    store.sensitiveLevels = res.data.rows;
  });
}

// // 获取标准数据元
// function getDataElem() {
//   getDgDataElemList().then((res) => {
//     store.dataElemList = res.data;
//   });
// }

// 打开修改弹窗
function handleEditClick(row) {
  dialog.type = "Edit";
  dialog.title =
    td("common.button.update") +
    td("meta.unreleased.structured.table.detail.columnName") +
    td("common.texts.metadata");
  dialog.open = true;
  getColumn(row.id).then((res) => {
    const {
      createBy,
      createTime,
      delFlag,
      updateBy,
      updateTime,
      updaterId,
      auditTime,
      ...form
    } = res.data;
    dialog.form = form;
  });
}

// 关闭修改弹窗
function handleCancelClick() {
  formRef.value.resetFields();
  dialog.form = {};
  dialog.open = false;
}

// 确认新增/修改
async function handleConfirmClick() {
  dialog.loading = true;
  const valid = await formRef.value.validate();
  dialog.loading = false;
  if (!valid) return;
  dialog.loading = true;
  if (dialog.form.safetyLevelId == undefined) {
    dialog.form.safetyLevelId = null;
    dialog.form.safetyLevelName = null;
  }
  await updateColumn(dialog.form);
  dialog.loading = false;
  proxy.$modal.msgSuccess(
    `${
      dialog.form.id ? td("common.button.update") : td("common.button.add")
    }${td("meta.unreleased.structured.table.detail.columnName")}${td(
      "common.texts.metadata"
    )}${td("common.message.success")}`
  );
  handleCancelClick();
  tableRef.value.getList();
}

// 详情页面
function handleDetailPageClick(row) {
  router.push({
    path: BASE_URL + "/detail",
    query: {
      id: row.id,
    },
  });
}

// 详情
function handleDetailClick(row) {
  handleEditClick(row);
  dialog.type = "Detail";
  dialog.title =
    td("meta.unreleased.structured.table.detail.columnName") +
    td("common.texts.metadata") +
    td("common.texts.detail");
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(
    `${td("common.message.confirmDelete")}${row.id}${td(
      "common.texts.dataItem"
    )}？`,
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return delColumn(row.id);
    })
    .then(() => {
      ElMessage.success(td("common.message.deleteSuccess"));
      tableRef.value.getList();
    });
}

// 切换状态
function handleStatusChange(row, status) {
  ElMessageBox.confirm(
    `${td("common.message.confirm")}${
      status == 1
        ? td("common.button.release")
        : td("common.button.cancelRelease")
    }${td("common.texts.data")}${td("common.texts.number")}${row.id}${td(
      "meta.unreleased.structured.table.detail.columnName"
    )}${td("common.texts.metadata")}${td("common.message.question")}`,
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return updateColumnStatus({
        id: row.id,
        status,
      });
    })
    .then(() => {
      ElMessage.success(
        `${td("common.texts.number")}${row.id}${td(
          "meta.unreleased.structured.table.detail.columnName"
        )}${td("common.texts.metadata")}${
          status == 1
            ? td("common.button.release")
            : td("common.button.cancelRelease")
        }${td("common.message.success")}`
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}
// getSensitiveLevel();
</script>
