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
            {{ td("meta.unreleased.structured.table.detail.viewDataElem") }}
          </el-button>

          <el-button
            link
            type="primary"
            icon="view"
            @click="handleSensitiveLevelClick(row)"
            :disabled="!row.safetyLevelId"
          >
            {{
              td("meta.unreleased.structured.table.detail.viewSensitiveLevel")
            }}
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog
      :title="
        td('meta.unreleased.structured.table.detail.sensitiveLevelDetail')
      "
      v-model="dialog.open"
      width="800px"
      draggable
    >
      <el-form label-width="100px" class="column-form">
        <el-form-item :label="td('common.texts.number')" prop="id">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.id) }}
          </div>
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.levelName')"
          prop="sensitiveLevel"
        >
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.sensitiveLevel) }}
          </div>
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.replaceRule')"
          prop="sensitiveRule"
        >
          <dict-tag
            :options="toValue(dicts.da_sensitive_level_rule)"
            :value="dialog.form.sensitiveRule"
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.status')" prop="onlineFlag">
          <dict-tag
            :options="toValue(dicts.da_sensitive_status)"
            :value="dialog.form.onlineFlag"
          />
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.startCharLoc')"
          prop="startCharLoc"
        >
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.startCharLoc) }}
          </div>
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.endCharLoc')"
          prop="endCharLoc"
        >
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.endCharLoc) }}
          </div>
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.replaceContent')"
          prop="maskCharacter"
        >
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.maskCharacter) }}
          </div>
        </el-form-item>
        <el-form-item
          :label="td('common.texts.description')"
          prop="description"
          class="row-full"
        >
          <div class="form-readonly textarea">
            {{ getFormatValue(dialog.form.description) }}
          </div>
        </el-form-item>
        <el-form-item
          :label="td('common.texts.remark')"
          prop="remark"
          class="row-full"
        >
          <div class="form-readonly textarea">
            {{ getFormatValue(dialog.form.remark) }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdBy')" prop="createBy">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.createBy) }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdTime')" prop="createTime">
          <div class="form-readonly">
            {{
              getFormatValue(
                parseTime(dialog.form.createTime, "{y}-{m}-{d} {h}:{i}")
              )
            }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy">
          <div class="form-readonly">
            {{ getFormatValue(dialog.form.updateBy) }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime">
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
          <el-button size="mini" @click="dialog.open = false">{{
            td("common.button.close")
          }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataGovern">
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, ref, toValue, getCurrentInstance } from "vue";
import { listColumn } from "@/api/mc/unreleased/column.js";
import { useRouter } from "vue-router";
import { listDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";
// import { getDgDataElemList } from "@/api/dg/standard/dataElem.js";
import { getDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";

const { td } = useDefaultLang();
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
      label: td("meta.unreleased.structured.table.detail.nullableFlag"),
      prop: "nullableFlag",
      width: 90,
      dict: "table_yes_no",
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
