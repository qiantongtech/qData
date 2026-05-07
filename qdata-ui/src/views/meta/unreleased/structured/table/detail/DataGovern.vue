<template>
  <div class="container">
    <qt-wrap
      :columns="tableStroe.columns"
      :tableRef="tableRef"
      :config="{ fullContent: false, actions: { table: { search: false } } }"
    >
      <qt-table v-bind="tableStroe" ref="tableRef">
        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="view"
            :disabled="!row.dataElemId"
            @click="handleDataElemClick(row)"
          >
            查看标准数据元
          </el-button>

          <el-button
            link
            type="primary"
            icon="view"
            @click="handleSensitiveLevelClick(row)"
            :disabled="!row.safetyLevelId"
          >
            查看安全等级
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog
      title="安全等级详情"
      v-model="dialog.open"
      width="800px"
      draggable
    >
      <el-form label-width="100px" class="column-form">
        <el-form-item label="编号" prop="id">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.id) }}
          </div>
        </el-form-item>
        <el-form-item label="级别名称" prop="sensitiveLevel">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.sensitiveLevel) }}
          </div>
        </el-form-item>
        <el-form-item label="替换规则" prop="sensitiveRule">
          <dict-tag
            :options="toValue(dicts.da_sensitive_level_rule)"
            :value="dialog.form.sensitiveRule"
          />
        </el-form-item>
        <el-form-item label="状态" prop="onlineFlag">
          <dict-tag
            :options="toValue(dicts.da_sensitive_status)"
            :value="dialog.form.onlineFlag"
          />
        </el-form-item>
        <el-form-item label="起始字符位置" prop="startCharLoc">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.startCharLoc) }}
          </div>
        </el-form-item>
        <el-form-item label="截止字符位置" prop="endCharLoc">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.endCharLoc) }}
          </div>
        </el-form-item>
        <el-form-item label="替换内容" prop="maskCharacter">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.maskCharacter) }}
          </div>
        </el-form-item>
        <el-form-item label="描述" prop="description" class="row-full">
          <div class="form-readonly textarea">
            {{ getFormatValue(dialog.form.description) }}
          </div>
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="row-full">
          <div class="form-readonly textarea">
            {{ getFormatValue(dialog.form.remark) }}
          </div>
        </el-form-item>
        <el-form-item label="创建人" prop="createBy">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.createBy) }}
          </div>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <div class="form-readonly">
            {{
              getFormatValue(
                parseTime(dialog.form.createTime, "{y}-{m}-{d} {h}:{i}")
              )
            }}
          </div>
        </el-form-item>
        <el-form-item label="更新人" prop="updateBy">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.updateBy) }}
          </div>
        </el-form-item>
        <el-form-item label="更新时间" prop="updateTime">
          <div class="form-readonly">
            {{
              getFormatValue(
                parseTime(dialog.form.updateTime, "{y}-{m}-{d} {h}:{i}")
              )
            }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="dialog.open = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataGovern">
import { reactive, ref, toValue, getCurrentInstance } from "vue";
import { listColumn } from "@/api/mc/unreleased/column.js";
import { useRouter } from "vue-router";
import { listDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";
// import { getDgDataElemList } from "@/api/dg/standard/dataElem.js";
import { getDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";

const BASE_URL = "/meta/unreleased/structured/column";

const props = defineProps({
  detail: {
    type: Object,
    required: true,
  },
});

const router = useRouter();

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict("da_sensitive_level_rule", "da_sensitive_status");

const store = reactive({});

const dialog = reactive({
  open: false,
  form: {},
});

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
      label: "是否必填",
      prop: "nullableFlag",
      width: 90,
      dict: "table_yes_no",
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
      width: 280,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listColumn,
  params: {
    tableId: props.detail.id,
    dataType: 1,
  },
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

// 详情
function handleDetailClick(row) {
  router.push({
    path: BASE_URL + "/detail",
    query: {
      id: row.id,
    },
  });
}

// 查看标准数据元
function handleDataElemClick(row) {
  router.push({
    path: "/dm/dataElem/column",
    query: {
      id: row.dataElemId,
    },
  });
}

// 查看安全等级
function handleSensitiveLevelClick(row) {
  dialog.open = true;
  getDgSensitiveLevel(row.safetyLevelId).then((res) => {
    dialog.form = res.data;
  });
}

getSensitiveLevel();
</script>
