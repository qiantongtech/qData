<template>
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['etl:dppEtlTaskLog:add']"
          @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['etl:dppEtlTaskLog:export']"
          @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="310px" v-loading="loading" :data="dppEtlTaskLogList"
    @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
    <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
    <el-table-column v-if="columns[1].visible" label="任务类型" align="center" prop="type">
      <template #default="scope">
        {{ scope.row.type || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[2].visible" label="任务名称" align="center" prop="name">
      <template #default="scope">
        {{ scope.row.name || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[3].visible" label="任务编码" align="center" prop="code">
      <template #default="scope">
        {{ scope.row.code || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[4].visible" label="任务版本" align="center" prop="version">
      <template #default="scope">
        {{ scope.row.version || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[5].visible" label="项目id" align="center" prop="projectId">
      <template #default="scope">
        {{ scope.row.projectId || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[6].visible" label="项目编码" align="center" prop="projectCode">
      <template #default="scope">
        {{ scope.row.projectCode || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[7].visible" label="责任人" align="center" prop="personCharge">
      <template #default="scope">
        {{ scope.row.personCharge || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[8].visible" label="节点坐标信息" align="center" prop="locations">
      <template #default="scope">
        {{ scope.row.locations || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[9].visible" label="描述" align="center" prop="description">
      <template #default="scope">
        {{ scope.row.description || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[10].visible" label="超时时间" align="center" prop="timeout">
      <template #default="scope">
        {{ scope.row.timeout || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[11].visible" label="抽取量" align="center" prop="extractionCount">
      <template #default="scope">
        {{ scope.row.extractionCount || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[12].visible" label="写入量" align="center" prop="writeCount">
      <template #default="scope">
        {{ scope.row.writeCount || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[13].visible" label="任务状态" align="center" prop="status">
      <template #default="scope">
        {{ scope.row.status || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[14].visible" label="DolphinScheduler的id" align="center" prop="dsId">
      <template #default="scope">
        {{ scope.row.dsId || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[17].visible" label="创建人" align="center" prop="createBy">
      <template #default="scope">
        {{ scope.row.createBy || "-" }}
      </template>
    </el-table-column>
    <el-table-column v-if="columns[19].visible" label="创建时间" align="center" prop="createTime" width="180">
      <template #default="scope">
        <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
      </template>
    </el-table-column>
    <el-table-column v-if="columns[23].visible" label="备注" align="center" prop="remark">
      <template #default="scope">
        {{ scope.row.remark || "-" }}
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
          v-hasPermi="['etl:dppEtlTaskLog:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
          v-hasPermi="['etl:dppEtlTaskLog:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
          v-hasPermi="['etl:dppEtlTaskLog:edit']">详情</el-button>
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/system/images/no_data/noData.png" alt="" />
        <p>暂无记录</p>
      </div>
    </template>
  </el-table>

  <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize"
    @pagination="getList" />

  <!-- 添加或修改数据集成任务-日志对话框 -->
  <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="dppEtlTaskLogRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入任务名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务编码" prop="code">
            <el-input v-model="form.code" placeholder="请输入任务编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务版本" prop="version">
            <el-input v-model="form.version" placeholder="请输入任务版本" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目id" prop="projectId">
            <el-input v-model="form.projectId" placeholder="请输入项目id" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目编码" prop="projectCode">
            <el-input v-model="form.projectCode" placeholder="请输入项目编码" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="personCharge">
            <el-input v-model="form.personCharge" placeholder="请输入责任人" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="节点坐标信息" prop="locations">
            <el-input v-model="form.locations" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" placeholder="请输入描述" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="超时时间" prop="timeout">
            <el-input v-model="form.timeout" placeholder="请输入超时时间" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="抽取量" prop="extractionCount">
            <el-input v-model="form.extractionCount" placeholder="请输入抽取量" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="写入量" prop="writeCount">
            <el-input v-model="form.writeCount" placeholder="请输入写入量" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="DolphinScheduler的id" prop="dsId">
            <el-input v-model="form.dsId" placeholder="请输入DolphinScheduler的id" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button type="primary" size="small" @click="submitForm">确 定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 数据集成任务-日志详情对话框 -->
  <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="dppEtlTaskLogRef" :model="form" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务类型" prop="type">
            <div>
              {{ form.type }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name">
            <div>
              {{ form.name }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务编码" prop="code">
            <div>
              {{ form.code }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务版本" prop="version">
            <div>
              {{ form.version }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目id" prop="projectId">
            <div>
              {{ form.projectId }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目编码" prop="projectCode">
            <div>
              {{ form.projectCode }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="personCharge">
            <div>
              {{ form.personCharge }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="节点坐标信息" prop="locations">
            <div>
              {{ form.locations }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="描述" prop="description">
            <div>
              {{ form.description }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="超时时间" prop="timeout">
            <div>
              {{ form.timeout }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="抽取量" prop="extractionCount">
            <div>
              {{ form.extractionCount }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="写入量" prop="writeCount">
            <div>
              {{ form.writeCount }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务状态" prop="status">
            <div>
              {{ form.status }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="DolphinScheduler的id" prop="dsId">
            <div>
              {{ form.dsId }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <div>
              {{ form.remark }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="cancel">关 闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
import {
  listDppEtlTaskLog,
  getDppEtlTaskLog,
  delDppEtlTaskLog,
  addDppEtlTaskLog,
  updateDppEtlTaskLog,
} from "@/api/dpp/etl/dppEtlTaskLog";

const { proxy } = getCurrentInstance();

const dppEtlTaskLogList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "ID", visible: true },
  { key: 1, label: "任务类型", visible: true },
  { key: 2, label: "任务名称", visible: true },
  { key: 3, label: "任务编码", visible: true },
  { key: 4, label: "任务版本", visible: true },
  { key: 5, label: "项目id", visible: true },
  { key: 6, label: "项目编码", visible: true },
  { key: 7, label: "责任人", visible: true },
  { key: 8, label: "节点坐标信息", visible: true },
  { key: 9, label: "描述", visible: true },
  { key: 10, label: "超时时间", visible: true },
  { key: 11, label: "抽取量", visible: true },
  { key: 12, label: "写入量", visible: true },
  { key: 13, label: "任务状态", visible: true },
  { key: 14, label: "DolphinScheduler的id", visible: true },
  { key: 15, label: "是否有效", visible: true },
  { key: 16, label: "删除标志", visible: true },
  { key: 17, label: "创建人", visible: true },
  { key: 18, label: "创建人id", visible: true },
  { key: 19, label: "创建时间", visible: true },
  { key: 20, label: "更新人", visible: true },
  { key: 21, label: "更新人id", visible: true },
  { key: 22, label: "更新时间", visible: true },
  { key: 23, label: "备注", visible: true },
]);

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

const data = reactive({
  dppEtlTaskLogDetail: {},
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
  },
  rules: {},
});

const { queryParams, form, dppEtlTaskLogDetail, rules } = toRefs(data);

/** 查询数据集成任务-日志列表 */
function getList() {
  loading.value = true;
  listDppEtlTaskLog(queryParams.value).then((response) => {
    dppEtlTaskLogList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
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
  proxy.resetForm("dppEtlTaskLogRef");
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

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增数据集成任务-日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDppEtlTaskLog(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改数据集成任务-日志";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDppEtlTaskLog(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "数据集成任务-日志详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dppEtlTaskLogRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDppEtlTaskLog(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addDppEtlTaskLog(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除数据集成任务-日志编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDppEtlTaskLog(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "dpp/dppEtlTaskLog/export",
    {
      ...queryParams.value,
    },
    `dppEtlTaskLog_${new Date().getTime()}.xlsx`
  );
}

getList();
</script>
