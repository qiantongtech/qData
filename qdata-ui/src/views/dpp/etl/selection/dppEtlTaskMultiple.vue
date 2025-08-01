<template>
  <el-dialog title="数据集成任务-多选" v-model="visible" width="1200px" :append-to="$refs['app-container']" draggable
    destroy-on-close @close="cancel">
    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
      label-width="68px">
      <el-form-item label="任务名称" prop="name">
        <el-input style="width:240px" v-model="queryParams.name" placeholder="请输入任务名称" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="任务编码" prop="code">
        <el-input style="width:240px" v-model="queryParams.code" placeholder="请输入任务编码" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="任务版本" prop="version">
        <el-input style="width:240px" v-model="queryParams.version" placeholder="请输入任务版本" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="项目id" prop="projectId">
        <el-input style="width:240px" v-model="queryParams.projectId" placeholder="请输入项目id" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="项目编码" prop="projectCode">
        <el-input style="width:240px" v-model="queryParams.projectCode" placeholder="请输入项目编码" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="责任人" prop="personCharge">
        <el-input style="width:240px" v-model="queryParams.personCharge" placeholder="请输入责任人" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input style="width:240px" v-model="queryParams.description" placeholder="请输入描述" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="超时时间" prop="timeout">
        <el-input style="width:240px" v-model="queryParams.timeout" placeholder="请输入超时时间" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="抽取量" prop="extractionCount">
        <el-input style="width:240px" v-model="queryParams.extractionCount" placeholder="请输入抽取量" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="写入量" prop="writeCount">
        <el-input style="width:240px" v-model="queryParams.writeCount" placeholder="请输入写入量" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="DolphinScheduler的id" prop="dsId">
        <el-input style="width:240px" v-model="queryParams.dsId" placeholder="请输入DolphinScheduler的id" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker style="width:240px" clearable v-model="queryParams.createTime" type="date"
          value-format="YYYY-MM-DD" placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table ref="multipletableRef" stripe height="300px" v-loading="loading" :data="dataList" reserve-selection
      row-key="id" @selection-change="handleSelectionChange" @row-click="handleRowClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="任务类型" align="center" prop="type">
        <template #default="scope">
          {{ scope.row.type || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="name">
        <template #default="scope">
          {{ scope.row.name || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="任务编码" align="center" prop="code">
        <template #default="scope">
          {{ scope.row.code || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="任务版本" align="center" prop="version">
        <template #default="scope">
          {{ scope.row.version || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="项目id" align="center" prop="projectId">
        <template #default="scope">
          {{ scope.row.projectId || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="项目编码" align="center" prop="projectCode">
        <template #default="scope">
          {{ scope.row.projectCode || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="责任人" align="center" prop="personCharge">
        <template #default="scope">
          {{ scope.row.personCharge || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="节点坐标信息" align="center" prop="locations">
        <template #default="scope">
          {{ scope.row.locations || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description">
        <template #default="scope">
          {{ scope.row.description || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="超时时间" align="center" prop="timeout">
        <template #default="scope">
          {{ scope.row.timeout || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="抽取量" align="center" prop="extractionCount">
        <template #default="scope">
          {{ scope.row.extractionCount || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="写入量" align="center" prop="writeCount">
        <template #default="scope">
          {{ scope.row.writeCount || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          {{ scope.row.status || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="DolphinScheduler的id" align="center" prop="dsId">
        <template #default="scope">
          {{ scope.row.dsId || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark">
        <template #default="scope">
          {{ scope.row.remark || '-' }}
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button type="primary" size="small" @click="confirm">
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="DppEtlTaskMultiple">
import { listDppEtlTask } from "@/api/dpp/etl/dppEtlTask";
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
    type: null,
    name: null,
    code: null,
    version: null,
    projectId: null,
    projectCode: null,
    personCharge: null,
    locations: null,
    description: null,
    timeout: null,
    extractionCount: null,
    writeCount: null,
    status: null,
    dsId: null,
    createTime: null,
  }
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
          multiple.value = multiple.value.filter(
            (ece) => item.id != ece.id
          );
        }
      });
    }
    if (multiple.value.length > 0) {
      selection.forEach((item) => {
        let index = multiple.value.findIndex(
          (ece) => ece.id == item.id
        );
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
          multiple.value = multiple.value.filter(
            (ece) => item.id != ece.id
          );
        }
      });
    }
  }
  oldSelection.value = selection;
}

/** 行单机事件 */
function handleRowClick(row) {
  // 检查当前行是否已经在 multiple 中
  const index = multiple.value.findIndex(item => item.id === row.id);

  // 如果行已经被选中，移除它
  if (index > -1) {
    multiple.value = multiple.value.filter(item => item.id !== row.id);
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
        multipletableRef.value.toggleRowSelection(data[0], undefined, ignoreSelectable);
      }
    });
  }
}

function rest() {
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  oldSelection.value = []
}

/**
 * 打开选择框
 * @param {Array} val 选中的对象数组
 */
function open(val) {
  if (!Array.isArray(val)) {
    val = [val];  // 将非可迭代值转化为数组
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
  listDppEtlTask(proxy.addDateRange(queryParams.value, dateRange.value)).then(
    async (response) => {
      dataList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
      // 初始化及分页切换选中逻辑
      isAuto.value = true;
      await nextTick();
      setSelectionRow(multiple.value);
      isAuto.value = false;
    }
  );
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
