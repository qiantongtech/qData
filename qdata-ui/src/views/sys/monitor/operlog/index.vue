<template>
   <div class="app-container" ref="app-container">
      <div class="pagecont-top" v-show="showSearch">
         <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="操作地址" prop="operIp">
               <el-input v-model="queryParams.operIp" placeholder="请输入操作地址" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="系统模块" prop="title">
               <el-input v-model="queryParams.title" placeholder="请输入系统模块" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="操作人员" prop="operName">
               <el-input v-model="queryParams.operName" placeholder="请输入操作人员" clearable class="el-form-input-width"
                  @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类型" prop="businessType">
               <el-select v-model="queryParams.businessType" placeholder="操作类型" clearable class="el-form-input-width">
                  <el-option v-for="dict in sys_oper_type" :key="dict.value" :label="dict.label" :value="dict.value" />
               </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-select v-model="queryParams.status" placeholder="操作状态" clearable class="el-form-input-width">
                  <el-option v-for="dict in sys_common_status" :key="dict.value" :label="dict.label"
                     :value="dict.value" />
               </el-select>
            </el-form-item>
            <el-form-item label="操作时间">
               <el-date-picker class="el-form-input-width" v-model="dateRange" value-format="YYYY-MM-DD HH:mm:ss"
                  type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                  :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"></el-date-picker>
            </el-form-item>
            <el-form-item>
               <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
               </el-button>
               <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
                  <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
               </el-button>
            </el-form-item>
         </el-form>
      </div>
      <div class="pagecont-bottom pagecont-bottoms">
         <div class="justify-between mb15">
            <el-row :gutter="10" class="btn-style">
               <el-col :span="1.5">
                  <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                     v-hasPermi="['monitor:operlog:remove']">删除</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button type="danger" plain icon="Delete" @click="handleClean"
                     v-hasPermi="['monitor:operlog:remove']">清空</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button type="warning" plain icon="Download" @click="handleExport"
                     v-hasPermi="['monitor:operlog:export']">导出</el-button>
               </el-col>
            </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table stripe ref="operlogRef" v-loading="loading" :data="operlogList"
            @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="日志编号" align="center" prop="operId" />
            <el-table-column label="系统模块" align="center" prop="title" :show-overflow-tooltip="{ effect: 'light' }" />
            <el-table-column label="操作类型" align="center" prop="businessType">
               <template #default="scope">
                  <dict-tag :options="sys_oper_type" :value="scope.row.businessType" />
               </template>
            </el-table-column>
            <el-table-column label="操作人员" align="center" width="110" prop="operName"
               :show-overflow-tooltip="{ effect: 'light' }" sortable="custom"
               :sort-orders="['descending', 'ascending']" />
            <el-table-column label="操作地址" align="center" prop="operIp" width="130"
               :show-overflow-tooltip="{ effect: 'light' }" />
            <el-table-column label="操作状态" align="center" prop="status">
               <template #default="scope">
                  <dict-tag :options="sys_common_status" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column label="操作日期" align="center" prop="operTime" width="180" sortable="custom"
               :sort-orders="['descending', 'ascending']">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.operTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column label="消耗时间" align="center" prop="costTime" width="110"
               :show-overflow-tooltip="{ effect: 'light' }" sortable="custom" :sort-orders="['descending', 'ascending']">
               <template #default="scope">
                  <span>{{ scope.row.costTime }}毫秒</span>
               </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="View" @click="handleView(scope.row, scope.index)"
                     v-hasPermi="['monitor:operlog:query']">详细</el-button>
               </template>
            </el-table-column>
         </el-table>

         <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
      </div>

      <!-- 操作日志详细 -->
      <el-dialog title="操作日志详细" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
         destroy-on-close>
         <el-form :model="form" label-width="80px">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item label="操作模块">
                     <div class="form-value-ifon">
                        {{ form.title }} / {{ typeFormat(form) }}
                     </div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="登录信息">
                     <div class="form-value-ifon">
                        {{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}
                     </div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="请求地址">
                     <div class="form-value-ifon">
                        {{ form.operUrl }}
                     </div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="请求方式">
                     <div class="form-value-ifon">
                        {{ form.requestMethod }}
                     </div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="操作方法">
                     <div class="form-value-ifon">
                        {{ form.method }}
                     </div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="请求参数">
                     <div class="form-value-ifon">
                        {{ form.operParam }}
                     </div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="返回参数">
                     <div class="form-value-ifon">
                        {{ form.jsonResult }}
                     </div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="操作状态">
                     <div class="form-value-ifon" v-if="form.status === 0">正常</div>
                     <div class="form-value-ifon" v-else-if="form.status === 1">失败</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="消耗时间">
                     <div class="form-value-ifon">{{ form.costTime }}毫秒</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="操作时间">
                     <div class="form-value-ifon">{{ parseTime(form.operTime) }}</div>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="异常信息" v-if="form.status === 1">
                     <div class="form-value-ifon">{{ form.errorMsg }}</div>
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="open = false">关 闭</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Operlog">
import { list, delOperlog, cleanOperlog } from "@/api/system/monitor/operlog.js";

const { proxy } = getCurrentInstance();
const { sys_oper_type, sys_common_status } = proxy.useDict("sys_oper_type", "sys_common_status");

const operlogList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const defaultSort = ref({ prop: "operTime", order: "descending" });

const data = reactive({
   form: {},
   queryParams: {
      pageNum: 1,
      pageSize: 10,
      operIp: undefined,
      title: undefined,
      operName: undefined,
      businessType: undefined,
      status: undefined
   }
});

const { queryParams, form } = toRefs(data);

/** 查询登录日志 */
function getList() {
   loading.value = true;
   list(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
      operlogList.value = response.rows;
      total.value = response.total;
      loading.value = false;
   });
}

/** 操作日志类型字典翻译 */
function typeFormat(row, column) {
   return proxy.selectDictLabel(sys_oper_type.value, row.businessType);
}

/** 搜索按钮操作 */
function handleQuery() {
   queryParams.value.pageNum = 1;
   getList();
}

/** 重置按钮操作 */
function resetQuery() {
   dateRange.value = [];
   proxy.resetForm("queryRef");
   queryParams.value.pageNum = 1;
   proxy.$refs["operlogRef"].sort(defaultSort.value.prop, defaultSort.value.order);
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
   ids.value = selection.map(item => item.operId);
   multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
   queryParams.value.orderByColumn = column.prop;
   queryParams.value.isAsc = column.order;
   getList();
}

/** 详细按钮操作 */
function handleView(row) {
   open.value = true;
   form.value = row;
}

/** 删除按钮操作 */
function handleDelete(row) {
   const operIds = row.operId || ids.value;
   proxy.$modal.confirm('是否确认删除日志编号为"' + operIds + '"的数据项?').then(function () {
      return delOperlog(operIds);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
   }).catch(() => { });
}

/** 清空按钮操作 */
function handleClean() {
   proxy.$modal.confirm("是否确认清空所有操作日志数据项?").then(function () {
      return cleanOperlog();
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess("清空成功");
   }).catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
   proxy.download("monitor/operlog/export", {
      ...queryParams.value,
   }, `config_${new Date().getTime()}.xlsx`);
}

getList();
</script>
<style scoped lang="scss">
.form-value-ifon {
   width: 100%;
   border: 1px solid #c0c4cc;
   padding: 0px 10px;
   min-height: 34px;
}
</style>
