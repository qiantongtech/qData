<template>
  <el-dialog
    title="数据资产字段-多选"
    v-model="visible"
    width="1200px"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
    @close="cancel"
  >
    <el-form
      class="btn-style"
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="资产id" prop="assetId">
        <el-input
          style="width: 240px"
          v-model="queryParams.assetId"
          placeholder="请输入资产id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="中文名称/英文名称" prop="columnName">
        <el-input
          style="width: 240px"
          v-model="queryParams.columnName"
          placeholder="请输入中文名称/英文名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字段注释/中文名称" prop="columnComment">
        <el-input
          style="width: 240px"
          v-model="queryParams.columnComment"
          placeholder="请输入字段注释/中文名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="长度" prop="columnLength">
        <el-input
          style="width: 240px"
          v-model="queryParams.columnLength"
          placeholder="请输入长度"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="小数位" prop="columnScale">
        <el-input
          style="width: 240px"
          v-model="queryParams.columnScale"
          placeholder="请输入小数位"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否必填" prop="nullableFlag">
        <el-input
          style="width: 240px"
          v-model="queryParams.nullableFlag"
          placeholder="请输入是否必填"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否主键" prop="pkFlag">
        <el-input
          style="width: 240px"
          v-model="queryParams.pkFlag"
          placeholder="请输入是否主键"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="默认值" prop="defaultValue">
        <el-input
          style="width: 240px"
          v-model="queryParams.defaultValue"
          placeholder="请输入默认值"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否代码" prop="dataElemCodeFlag">
        <el-input
          style="width: 240px"
          v-model="queryParams.dataElemCodeFlag"
          placeholder="请输入是否代码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代码id" prop="dataElemCodeId">
        <el-input
          style="width: 240px"
          v-model="queryParams.dataElemCodeId"
          placeholder="请输入代码id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="敏感等级id" prop="sensitiveLevelId">
        <el-input
          style="width: 240px"
          v-model="queryParams.sensitiveLevelId"
          placeholder="请输入敏感等级id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联数据元" prop="relDataElmeFlag">
        <el-input
          style="width: 240px"
          v-model="queryParams.relDataElmeFlag"
          placeholder="请输入关联数据元"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联清洗规则" prop="relCleanFlag">
        <el-input
          style="width: 240px"
          v-model="queryParams.relCleanFlag"
          placeholder="请输入关联清洗规则"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联稽查规则" prop="relAuditFlag">
        <el-input
          style="width: 240px"
          v-model="queryParams.relAuditFlag"
          placeholder="请输入关联稽查规则"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          style="width: 240px"
          v-model="queryParams.description"
          placeholder="请输入描述"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          style="width: 240px"
          clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择创建时间"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          plain
          type="primary"
          @click="handleQuery"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      ref="multipletableRef"
      stripe
      height="300px"
      v-loading="loading"
      :data="dataList"
      reserve-selection
      row-key="id"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="资产id" align="center" prop="assetId">
        <template #default="scope">
          {{ scope.row.assetId || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        label="中文名称/英文名称"
        align="center"
        prop="columnName"
      >
        <template #default="scope">
          {{ scope.row.columnName || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        label="字段注释/中文名称"
        align="center"
        prop="columnComment"
      >
        <template #default="scope">
          {{ scope.row.columnComment || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="数据类型" align="center" prop="columnType">
        <template #default="scope">
          {{ scope.row.columnType || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="长度" align="center" prop="columnLength">
        <template #default="scope">
          {{ scope.row.columnLength || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="小数位" align="center" prop="columnScale">
        <template #default="scope">
          {{ scope.row.columnScale || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="是否必填" align="center" prop="nullableFlag">
        <template #default="scope">
          {{ scope.row.nullableFlag || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="是否主键" align="center" prop="pkFlag">
        <template #default="scope">
          {{ scope.row.pkFlag || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="默认值" align="center" prop="defaultValue">
        <template #default="scope">
          {{ scope.row.defaultValue || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="是否代码" align="center" prop="dataElemCodeFlag">
        <template #default="scope">
          {{ scope.row.dataElemCodeFlag || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="代码id" align="center" prop="dataElemCodeId">
        <template #default="scope">
          {{ scope.row.dataElemCodeId || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        label="敏感等级id"
        align="center"
        prop="sensitiveLevelId"
      >
        <template #default="scope">
          {{ scope.row.sensitiveLevelId || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="关联数据元" align="center" prop="relDataElmeFlag">
        <template #default="scope">
          {{ scope.row.relDataElmeFlag || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="关联清洗规则" align="center" prop="relCleanFlag">
        <template #default="scope">
          {{ scope.row.relCleanFlag || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="关联稽查规则" align="center" prop="relAuditFlag">
        <template #default="scope">
          {{ scope.row.relAuditFlag || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description">
        <template #default="scope">
          {{ scope.row.description || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark">
        <template #default="scope">
          {{ scope.row.remark || "-" }}
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="confirm">
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="DaAssetColumnMultiple">
import { listDaAssetColumn } from "@/api/da/assetColumn/daAssetColumn";
import { ref } from "vue";
const { proxy } = getCurrentInstance();

const dataList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const dateRange = ref([]);
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    assetId: null,
    columnName: null,
    columnComment: null,
    columnType: null,
    columnLength: null,
    columnScale: null,
    nullableFlag: null,
    pkFlag: null,
    defaultValue: null,
    dataElemCodeFlag: null,
    dataElemCodeId: null,
    sensitiveLevelId: null,
    relDataElmeFlag: null,
    relCleanFlag: null,
    relAuditFlag: null,
    description: null,
    createTime: null,
  },
});
const { queryParams, form } = toRefs(data);

// -------------------------------------------
const visible = ref(false);
// 定义多选数据
const multiple = ref([]);
// 定义上次勾选数据==用于对比删除
const oldSelection = ref([]);
// 是否分页切换
const isAuto = ref(false);
// 当前界面table
const multipletableRef = ref();

const emit = defineEmits(["open", "confirm", "cancel"]);

/** 多选框选中事件 */
function handleSelectionChange(selection) {
  // console.log(selection, "===handleSelectionChange");
  if (selection.length > 0) {
    // 如果选中值不是空值且少选了一个值
    if (oldSelection.value.length > selection.length) {
      oldSelection.value.forEach((item) => {
        let index = selection.findIndex((ece) => ece.id == item.id);
        if (index == -1) {
          multiple.value = multiple.value.filter((ece) => item.id != ece.id);
        }
      });
    }
    if (multiple.value.length > 0) {
      selection.forEach((item) => {
        let index = multiple.value.findIndex((ece) => ece.id == item.id);
        if (index == -1) {
          multiple.value.push(item);
        }
      });
    } else {
      multiple.value.push(...selection);
    }
  } else {
    // 如果不是分页导致的
    if (!isAuto.value) {
      // 如果选中值，取消到没有选择任何值
      oldSelection.value.forEach((item) => {
        let index = selection.findIndex((ece) => ece.id == item.id);
        if (index == -1) {
          multiple.value = multiple.value.filter((ece) => item.id != ece.id);
        }
      });
    }
  }
  oldSelection.value = selection;
}

/** 行单机事件 */
function handleRowClick(row) {
  // 检查当前行是否已经在 multiple 中
  const index = multiple.value.findIndex((item) => item.id === row.id);

  // 如果行已经被选中，移除它
  if (index > -1) {
    multiple.value = multiple.value.filter((item) => item.id !== row.id);
  } else {
    // 如果行未被选中，添加到 multiple 中
    multiple.value.push(row);
  }

  // 同步更新表格的选中状态
  multipletableRef.value.toggleRowSelection(row, index === -1);
}

/**
 * 选中table的复选框
 * @param {Array} rows 选中的对象数组
 * @param {Boolean} ignoreSelectable 是否忽略可选
 */
function setSelectionRow(rows, ignoreSelectable) {
  // 选中数据
  if (rows.length > 0) {
    rows.forEach((row) => {
      let data = dataList.value.filter((item) => item.id == row.id);
      if (data.length > 0) {
        multipletableRef.value.toggleRowSelection(
          data[0],
          undefined,
          ignoreSelectable
        );
      }
    });
  }
}

function rest() {
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  oldSelection.value = [];
}

/**
 * 打开选择框
 * @param {Array} val 选中的对象数组
 */
function open(val) {
  if (!Array.isArray(val)) {
    val = [val]; // 将非可迭代值转化为数组
  }
  visible.value = true;
  multiple.value = [...val];
  getList();
}

/**
 * 取消按钮
 * @description 取消按钮时，重置所有状态
 */
function cancel() {
  rest();
  visible.value = false;
}

/**
 * 确定按钮
 * @description 确定按钮时，emit confirm 事件，以便父组件接收到选中的数据
 */
function confirm() {
  if (multiple.value.length == 0) {
    proxy.$modal.msgWarning("未选择数据！");
    return;
  }
  emit("confirm", [...multiple.value]);
  rest();
  visible.value = false;
}

/** 查询字典类型列表 */
function getList() {
  loading.value = true;
  listDaAssetColumn(
    proxy.addDateRange(queryParams.value, dateRange.value)
  ).then(async (response) => {
    dataList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
    // 初始化及分页切换选中逻辑
    isAuto.value = true;
    await nextTick();
    setSelectionRow(multiple.value);
    isAuto.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.value.pageNum = 1;
  handleQuery();
}

defineExpose({ open });
</script>
