<template>
   <div class="app-container" ref="app-container">
      <div class="pagecont-top" v-show="showSearch">
         <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
            <el-form-item label="字典名称" prop="dictName">
               <el-input
                  v-model="queryParams.dictName"
                  placeholder="请输入字典名称"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item label="字典类型" prop="dictType">
               <el-input
                  v-model="queryParams.dictType"
                  placeholder="请输入字典类型"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-select
                  v-model="queryParams.status"
                  placeholder="字典状态"
                  clearable
                  class="el-form-input-width"
               >
                  <el-option
                     v-for="dict in sys_normal_disable"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
               <el-date-picker
                  class="el-form-input-width"
                  v-model="dateRange"
                  value-format="YYYY-MM-DD"
                  type="daterange"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
               ></el-date-picker>
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
      <div  class="pagecont-bottom">

         <div class="justify-between mb15">
            <el-row :gutter="10" class="btn-style">
               <el-col :span="1.5">
                  <el-button
                     type="primary"
                     plain
                     icon="Plus"
                     @click="handleAdd"
                     v-hasPermi="['system:dict:add']"
                  >新增</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button
                     type="primary"
                     plain
                     icon="Edit"
                     :disabled="single"
                     @click="handleUpdate"
                     v-hasPermi="['system:dict:edit']"
                  >修改</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button
                     type="danger"
                     plain
                     icon="Delete"
                     :disabled="multiple"
                     @click="handleDelete"
                     v-hasPermi="['system:dict:remove']"
                  >删除</el-button>
               </el-col>
               <el-col :span="1.5">
                  <el-button
                     type="warning"
                     plain
                     icon="Download"
                     @click="handleExport"
                     v-hasPermi="['system:dict:export']"
                  >导出</el-button>
               </el-col>
              <el-col :span="1.5">
              <el-button
                  type="primary"
                  plain
                  icon="Download"
                  :disabled="multiple"
                  @click="handleEnum"
              >下载</el-button>
              </el-col>
              <el-col :span="1.5">
                  <el-button
                     type="danger"
                     plain
                     icon="Refresh"
                     @click="handleRefreshCache"
                     v-hasPermi="['system:dict:remove']"
                  >刷新缓存</el-button>
               </el-col>
            </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table stripe height="60vh" v-loading="loading" :data="typeList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="字典编号" align="center" prop="dictId" />
            <el-table-column label="字典名称" align="center" prop="dictName" :show-overflow-tooltip="true"/>
            <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true">
               <template #default="scope">
                  <router-link :to="'/system/dict-data/index/' + scope.row.dictId" class="link-type">
                     <span>{{ scope.row.dictType }}</span>
                  </router-link>
               </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="status">
               <template #default="scope">
                  <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:dict:edit']">修改</el-button>
                  <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:dict:remove']">删除</el-button>
                  <el-button link type="primary" icon="Download" @click="handleEnum(scope.row)" >下载</el-button>
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
      </div>

      <!-- 添加或修改参数配置对话框 -->
      <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']"  draggable destroy-on-close>
         <el-form ref="dictRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item label="字典名称" prop="dictName">
                     <el-input v-model="form.dictName" placeholder="请输入字典名称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="字典类型" prop="dictType">
                     <el-input v-model="form.dictType" placeholder="请输入字典类型" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="状态" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio
                           v-for="dict in sys_normal_disable"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="备注" prop="remark">
                     <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="cancel">取 消</el-button>
               <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Dict">
import useDictStore from '@/store/system/dict.js'
import { listType, getType, delType, addType, updateType, refreshCache } from "@/api/system/system/dict/type.js";
import {genCode} from "@/api/system/tool/gen.js";

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const typeList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const dictTypes = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dictName: undefined,
    dictType: undefined,
    status: undefined
  },
  rules: {
    dictName: [{ required: true, message: "字典名称不能为空", trigger: "blur" }],
    dictType: [{ required: true, message: "字典类型不能为空", trigger: "blur" }]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询字典类型列表 */
function getList() {
  loading.value = true;
  listType(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    typeList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    dictId: undefined,
    dictName: undefined,
    dictType: undefined,
    status: "0",
    remark: undefined
  };
  proxy.resetForm("dictRef");
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
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增字典类型";
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.dictId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
  dictTypes.value = selection.map(item => item.dictType);

}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const dictId = row.dictId || ids.value;
  getType(dictId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改字典类型";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dictRef"].validate(valid => {
    if (valid) {
      if (form.value.dictId != undefined) {
        updateType(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addType(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const dictIds = row.dictId || ids.value;
  proxy.$modal.confirm('是否确认删除字典编号为"' + dictIds + '"的数据项？').then(function() {
    return delType(dictIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("system/dict/type/export", {
    ...queryParams.value
  }, `dict_${new Date().getTime()}.xlsx`);
}

/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  refreshCache().then(() => {
    proxy.$modal.msgSuccess("刷新成功");
    useDictStore().cleanDict();
  });
}

/** 生成枚举类操作 */
function handleEnum(row) {
  const dtNames = row.dictType || dictTypes.value;
  if (dtNames == "") {
    proxy.$modal.msgError("请选择要生成的字典");
    return;
  }

    proxy.$download.zip("/system/dict/type/batchDictData?dictTypes=" + dtNames, "anivia.zip");
}

getList();
</script>
