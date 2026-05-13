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
            详情
          </el-button>
          <template v-if="detail.status == '1' && route.query.table_status">
            <el-button
              link
              type="primary"
              :disabled="row.status == 1"
              icon="Edit"
              @click="handleEditClick(row)"
            >
              修改
            </el-button>
            <el-button
              link
              type="danger"
              icon="Delete"
              :disabled="row.status == 1"
              @click="handleDeleteClick(row)"
            >
              删除
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
        <el-form-item label="字段名称" prop="columnName">
          <el-input
            clearable
            v-model="dialog.form.columnName"
            placeholder="请输入字段注释"
          />
        </el-form-item>
        <el-form-item label="字段注释" prop="columnComment">
          <el-input
            clearable
            v-model="dialog.form.columnComment"
            placeholder="请输入字段注释"
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
        <el-form-item label="标准数据元" prop="dataElemId">
          <el-select
            clearable
            v-model="dialog.form.dataElemId"
            placeholder="请选择标准数据元"
          >
            <el-option
              v-for="item in store.dataElemList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="字段类型" prop="columnType">
          <el-select
            clearable
            v-model="dialog.form.columnType"
            placeholder="请选择字段类型"
          >
            <el-option
              v-for="dict in toValue(dicts.column_type)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="字段长度" prop="columnLength">
          <el-input-number
            :min="0"
            v-model="dialog.form.columnLength"
            placeholder="请输入字段长度"
            :controls="true"
            class="number-input"
            controls-position="right"
          />
        </el-form-item>

        <el-form-item label="字段精度" prop="columnPrecision">
          <el-input-number
            :min="0"
            v-model="dialog.form.columnPrecision"
            placeholder="请输入字段精度"
            :controls="true"
            class="number-input"
            controls-position="right"
          />
        </el-form-item>

        <el-form-item label="字段小数位" prop="columnScale">
          <el-input-number
            :min="0"
            v-model="dialog.form.columnScale"
            placeholder="请输入字段小数位"
            :controls="true"
            class="number-input"
            controls-position="right"
          />
        </el-form-item>

        <el-form-item label="业务定义" prop="businessDefinition">
          <el-input
            clearable
            v-model="dialog.form.businessDefinition"
            placeholder="请输入业务定义"
          />
        </el-form-item>

        <el-form-item label="度量单位" prop="measuringUnit">
          <el-input
            clearable
            v-model="dialog.form.measuringUnit"
            placeholder="请输入度量单位"
          />
        </el-form-item>

        <el-form-item label="是否必填" prop="nullableFlag">
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

        <el-form-item label="默认值" prop="defaultValue">
          <el-input
            clearable
            v-model="dialog.form.defaultValue"
            placeholder="请输入默认值"
          />
        </el-form-item>

        <el-form-item label="是否主键" prop="pkFlag">
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

        <el-form-item label="是否外键" prop="fkFlag">
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

        <el-form-item label="状态" prop="status">
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

        <el-form-item label="备注" class="row-full">
          <el-input
            v-model="dialog.form.remark"
            type="textarea"
            placeholder="请输入备注"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>

        <el-form-item label="描述" class="row-full">
          <el-input
            v-model="dialog.form.description"
            type="textarea"
            placeholder="请输入描述"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>

        <el-form-item
          label="变更说明"
          class="row-full"
          prop="updateMsg"
          v-if="dialog.type != 'Detail'"
        >
          <el-input
            v-model="dialog.form.updateMsg"
            type="textarea"
            placeholder="请输入变更说明"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancelClick" v-if="dialog.type != 'Detail'">
            取消
          </el-button>
          <el-button @click="handleCancelClick" v-if="dialog.type == 'Detail'">
            关闭
          </el-button>
          <el-button
            type="primary"
            v-if="dialog.type != 'Detail'"
            @click="handleConfirmClick"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ColumnList">
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
// import { getDgDataElemList } from "@/api/dg/standard/dataElem.js";

const BASE_URL = "/meta/unreleased/structured/column";

const rules = {
  columnName: [
    { required: true, message: "请输入字段名称", trigger: "change" },
    {
      pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/,
      message: "字段名称必须以字母开头，可包含字母、数字和下划线",
      trigger: "blur",
    },
  ],
  columnType: [
    { required: true, message: "请选择字段类型", trigger: "change" },
  ],
  columnLength: [
    { required: true, message: "请输入字段长度", trigger: ["change", "blur"] },
  ],
  columnScale: [
    {
      required: true,
      message: "请输入字段小数位",
      trigger: ["change", "blur"],
    },
  ],
  updateMsg: [
    { required: true, message: "请输入变更说明", trigger: ["change", "blur"] },
  ],
  businessDefinition: [
    { required: true, message: "请输入业务定义", trigger: "blur" },
  ],
  columnPrecision: [
    { required: true, message: "请输入字段精度", trigger: ["change", "blur"] },
  ],
  pkFlag: [{ required: true, message: "请选择是否主键", trigger: "change" }],
  fkFlag: [{ required: true, message: "请选择是否外键", trigger: "change" }],
  nullableFlag: [
    { required: true, message: "请选择是否可空", trigger: "change" },
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
      label: "编号",
      prop: "id",
      sortable: true,
      width: 70,
    },
    {
      label: "字段名称",
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
      label: "字段注释",
      prop: "columnComment",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 140,
    },

    {
      label: "标准数据元",
      prop: "dataElemName",
      width: 110,
    },
    {
      label: "字段类型",
      prop: "columnType",
      width: 110,
      dict: "column_type",
    },

    {
      label: "字段长度",
      prop: "columnLength",
      width: 100,
      sortable: true,
    },
    {
      label: "字段精度",
      prop: "columnPrecision",
      width: 100,
      sortable: true,
    },
    {
      label: "字段小数",
      prop: "columnScale",
      width: 100,
      sortable: true,
    },

    {
      label: "是否必填",
      prop: "nullableFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: "是否主键",
      prop: "pkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: "是否外键",
      prop: "fkFlag",
      width: 90,
      dict: "table_yes_no",
    },
    {
      label: "状态",
      prop: "status",
      width: 90,
      slot: "status",
      invisible: route.query.released,
    },

    {
      label: "更新人",
      prop: "updateBy",
      width: 120,
    },
    {
      label: "更新时间",
      prop: "updateTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: "创建人",
      prop: "createBy",
      width: 120,
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: "操作",
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
  dialog.title = "修改字段元数据";
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
    `${dialog.form.id ? "修改" : "新增"}字段元数据成功！`
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
  dialog.title = "字段元数据详情";
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(`是否确认删除编号为${row.id}的数据项？`, "系统提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      return delColumn(row.id);
    })
    .then(() => {
      ElMessage.success("删除成功");
      tableRef.value.getList();
    });
}

// 切换状态
function handleStatusChange(row, status) {
  ElMessageBox.confirm(
    `是否确认${status == 1 ? "发布" : "取消发布"}数据编号为${
      row.id
    }的字段元数据吗？`,
    "系统提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
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
        `编号为${row.id}的字段元数据${status == 1 ? "发布" : "取消发布"}成功!`
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}
// getSensitiveLevel();
</script>
