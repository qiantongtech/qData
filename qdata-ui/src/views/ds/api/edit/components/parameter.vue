<template>
  <div>
    <el-form ref="form2" :model="form2" :rules="rules" label-width="120px" v-loading="loading">
      <div class="header-container" style="margin-top: -10px;">
        <div class="header-left">
          <div class="blue-bar"></div>
          数据源配置
        </div>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="配置方式" prop="apiServiceType">
            <el-select v-model="form2.apiServiceType" placeholder="请选择配置方式" @change="configTypeSelectChanged"
              class="select-width">
              <el-option v-for="dict in ds_api_bas_info_api_service_type" :key="dict.id" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="form2.apiServiceType != '3'">
          <el-form-item label="数据源" prop="sourceId">
            <el-select v-model="form2.sourceId" placeholder="请选择数据源" @change="sourceSelectChanged" class="select-width">
              <el-option v-for="source in sourceOptions" :key="source.id" :label="source.datasourceName"
                :value="source.id" :disabled="source.status === '0'" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="form2.apiServiceType == '3'">
          <el-form-item label="转发类型" prop="transmitType">
            <el-select v-model="form2.transmitType" placeholder="请选择转发类型" @change="handleTransmitTypeChange">
              <el-option v-for="dict in ds_api_transmit_type" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="form2.apiServiceType == '3'">
          <el-form-item label="资产列表" prop="categoryAssetList">
            <el-select v-model="form2.categoryAssetList" placeholder="请选择转发类型" @change="getDaAssetApply">
              <el-option v-for="dict in apiList" :key="dict.id" :label="dict.name" :value="dict.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="form2.apiServiceType === '1'" :gutter="20">
          <el-form-item label="数据库表" prop="table">
            <el-select v-model="form2.table" value-key="tableName" placeholder="请选择数据库表" @change="tableSelectChanged"
              filterable :filter-method="filterTable" class="select-width">
              <el-option v-for="item in form2.filteredTableOptions" :key="item.tableName"
                :label="item.tableComment ? item.tableComment : item.tableName" :value="item">
                <el-tooltip effect="light" :disabled="isShowTooltip"
                  :content="item.tableName + (item.tableComment ? `(${item.tableComment})` : '')">
                  <div class="option-item" @mouseover="spanMouseenter($event)">
                    {{
                      item.tableName + (item.tableComment ? `(${item.tableComment})` : '')
                    }}
                  </div>
                </el-tooltip>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- SQL 编辑器 -->
      <el-row v-if="form2.apiServiceType === '2'" class="sql-editor-container">
        <el-col :span="24">
          <sql-editor ref="sqleditor" :value="form2.sqlText" class="sql-editor"
            @changeTextarea="changeTextarea($event)" />
        </el-col>
        <el-form-item v-if="form2.apiServiceType === '2'" class="sql-parse-btn-container">
          <el-button size="mini" type="primary" @click="sqlParseFunction" class="sql-parse-btn">SQL解析</el-button>
        </el-form-item>
      </el-row>
      <!--      <div class="clearfix header-text">-->
      <!--          <div class="header-left">-->
      <!--              <div class="blue-bar"></div>-->
      <!--              参数配置-->
      <!--          </div>-->
      <!--      </div>-->
      <div class="header-container">
        <div class="header-left">
          <div class="blue-bar"></div>
          参数配置
        </div>
      </div>
      <div v-if="form2.apiServiceType == '3'">
        <!--入参字段（type == 1） -->
        <el-form :model="props.form2.reqParams" :rules="rules" ref="inputForm" label-width="0">
          <el-row :gutter="24" class="mb8" style="margin-left: 0px;!important;margin-right: 0px;!important;">
            <div class="header-text">
              请求参数
              <el-link type="primary" class="add-link" icon="el-icon-circle-plus-outline" @click="openDialog('four')">
                新增参数
              </el-link>
            </div>
            <el-table :data="props.form2.reqParams" class="tableStyle" row-key="id" stripe default-expand-all
              height="200px" :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
              <el-table-column label="序号" width="100" align="center" fixed="left">
                <template #default="{ $index }">
                  {{ $index + 1 }}
                </template>
              </el-table-column>

              <el-table-column label="参数名称" fixed="left" align="center" prop="name"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <el-form-item :prop="`props.form2.reqParams[${findPosi(props.form2.reqParams, row.id)}].name`"
                    :rules="rules.name">
                    <!-- <el-input v-model="row.name" placeholder="请输入参数名称" /> -->
                    {{ row.name }}
                  </el-form-item>
                </template>
              </el-table-column>

              <el-table-column label="描述" fixed="left" align="center" prop="remark"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <!-- <el-form-item :prop="`props.form2.reqParams[${findPosi(props.form2.reqParams, row.id)}].remark`"
                    :rules="rules.fieldDefault">
                    <el-input v-model="row.remark" placeholder="请输入描述" />
                  </el-form-item> -->
                  {{ row.reqParams }}
                </template>
              </el-table-column>

              <el-table-column label="是否为空" fixed="left" align="center" prop="requestFlag"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <el-form-item :prop="`props.form2.reqParams[${findPosi(props.form2.reqParams, row.id)}].requestFlag`"
                    :rules="rules.requestFlag">
                    <el-checkbox v-model="row.requestFlag" :true-label="'1'" :false-label="'0'" disabled />
                  </el-form-item>
                </template>
              </el-table-column>

              <el-table-column label="参数类型" fixed="left" align="center" prop="columnType"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <!-- <el-form-item :prop="`props.form2.reqParams[${findPosi(props.form2.reqParams, row.id)}].columnType`"
                    :rules="rules.columnType">
                    <el-select v-model="row.columnType" placeholder="请选择字段类型">
                      <el-option v-for="dict in da_asset_api_column_type" :key="dict.value" :label="dict.label"
                        :value="dict.value" :disabled="hasChildren(row) && !['Object', 'Array'].includes(dict.value)" />
                    </el-select> -->
                  <!-- </el-form-item> -->
                  {{ row.columnType }}
                </template>
              </el-table-column>

              <el-table-column label="示例值" fixed="left" align="center" prop="exampleValue"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <!-- <el-form-item :prop="`props.form2.reqParams[${findPosi(props.form2.reqParams, row.id)}].exampleValue`"
                    :rules="rules.fieldDefault">
                    <el-input v-model="row.fieldDefault" placeholder="请输入示例值" />
                  </el-form-item> -->
                  {{ row.exampleValue }}
                </template>
              </el-table-column>

              <el-table-column label="默认值" fixed="left" align="center" prop="defaultValue"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <el-form-item :prop="`props.form2.reqParams[${findPosi(props.form2.reqParams, row.id)}].defaultValue`"
                    :rules="rules.defaultValue">
                    <el-input v-model="row.defaultValue" placeholder="请输入默认值" />
                  </el-form-item>
                </template>
              </el-table-column>

              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                  <el-button type="danger" link v-if="scope.row.parentId == null"
                    @click="deleteRow(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>

            </el-table>
          </el-row>

        </el-form>
        <!-- 出参字段（type == 2） -->
        <el-form :model="props.form2.resParams" :rules="rules" ref="outputForm" label-width="0">
          <el-row :gutter="24" class="mb8" style="margin-left: 0px;!important;margin-right: 0px;!important;">
            <div class="header-text">
              返回参数
              <!-- <el-link type="primary" class="add-link" icon="el-icon-circle-plus-outline" @click="handleAdd(2)">
                新增参数
              </el-link> -->
            </div>
            <!-- Replace form2.resParams with props.form2.resParams -->
            <el-table :data="props.form2.resParams" row-key="id" border default-expand-all height="200px"
              class="tableStyle" :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">

              <el-table-column label="序号" width="100" align="center" fixed="left">
                <template #default="{ $index }">
                  {{ $index + 1 }}
                </template>
              </el-table-column>

              <el-table-column label="参数名称" fixed="left" align="center" prop="name"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <!-- <el-form-item :prop="`props.form2.resParams[${findPosi(props.form2.resParams, row.id)}].name`"
                    :rules="rules.name">
                    <el-input v-model="row.name" placeholder="请输入参数名称" />
                  </el-form-item> -->
                  {{ row.name }}
                </template>
              </el-table-column>

              <el-table-column label="描述" fixed="left" align="center" prop="remark"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <el-form-item :prop="`props.form2.resParams[${findPosi(props.form2.resParams, row.id)}].remark`"
                    :rules="rules.fieldDefault">
                    <el-input v-model="row.remark" placeholder="请输入描述" />
                  </el-form-item>
                </template>
              </el-table-column>

              <el-table-column label="数据类型" fixed="left" align="center" prop="columnType"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <!-- <el-select v-model="row.columnType" placeholder="请选择数据类型">
                    <el-option v-for="dict in da_asset_api_column_type" :key="dict.value" :label="dict.label"
                      :value="dict.value" :disabled="hasChildren(row) && !['Object', 'Array'].includes(dict.value)" />
                  </el-select> -->
                  {{ row.columnType }}
                </template>
              </el-table-column>

              <el-table-column label="示例值" fixed="left" align="center" prop="exampleValue"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="{ row, $index }">
                  <el-form-item :prop="`props.form2.resParams[${findPosi(props.form2.resParams, row.id)}].exampleValue`"
                    :rules="rules.fieldDefault">
                    <el-input v-model="row.exampleValue" placeholder="请输入示例值" />
                  </el-form-item>
                </template>
              </el-table-column>

            </el-table>
          </el-row>
        </el-form>
      </div>
      <el-form ref="form2" :model="form2" label-width="100px" label="字段列表：" v-if="form2.apiServiceType != '3'">
        <div class="header-text">
          请求参数
          <el-link v-if="form2.apiServiceType !== '2'" type="primary" class="add-link"
            icon="el-icon-circle-plus-outline" @click="openDialog('first')">
            新增参数
          </el-link>
        </div>
        <el-table :data="form2.reqParams" max-height="250" class="tableStyle" stripe>
          <el-table-column label="序号" width="80" align="center">
            <template #default="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="paramName" label="参数名称" align="center" :show-overflow-tooltip="{ effect: 'light' }" />
          <el-table-column prop="nullable" label="是否允许为空" align="center" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
              <el-checkbox v-model="scope.row.nullable" true-label="1" false-label="0" />
            </template>
          </el-table-column>
          <el-table-column prop="paramComment" label="描述" align="center">
            <template #default="scope">
              <el-input v-model="scope.row.paramComment" placeholder="请输入描述" />
            </template>
          </el-table-column>
          <el-table-column prop="paramType" label="参数类型" align="center">
            <template #default="scope">
              <el-select v-model="scope.row.paramType" placeholder="请选择参数类型">
                <el-option v-for="dict in ds_api_param_type" :key="dict.id" :label="dict.label" :value="dict.value" />
              </el-select>
            </template>
          </el-table-column>
          <!--          <el-table-column-->
          <!--            prop="whereType"-->
          <!--            label="操作符"-->
          <!--            align="center"-->
          <!--            v-if="splReult !== true"-->
          <!--          >-->
          <!--            <template #default="scope">-->
          <!--              <el-select-->
          <!--                v-model="scope.row.whereType"-->
          <!--                placeholder="请选择操作符"-->
          <!--              >-->
          <!--                <el-option-->
          <!--                  v-for="dict in da_api_param_operator"-->
          <!--                  :key="dict.id"-->
          <!--                  :label="dict.label"-->
          <!--                  :value="dict.value"-->
          <!--                />-->
          <!--              </el-select>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
          <el-table-column prop="exampleValue" label="示例值" align="center" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
              <el-input v-model="scope.row.exampleValue" placeholder="请输入示例值" />
            </template>
          </el-table-column>
          <el-table-column prop="defaultValue" label="默认值" align="center" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
              <el-input v-model="scope.row.defaultValue" placeholder="请输入默认值" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="form2.apiServiceType !== '2'">
            <template #default="scope">
              <el-button type="text" size="mini" icon="el-icon-edit" @click="handleDelete(scope.$index)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="header-text">
          返回字段
          <el-link type="primary" v-if="form2.apiServiceType !== '2'" class="add-link"
            icon="el-icon-circle-plus-outline" @click="openDialog('second')">
            新增参数
          </el-link>
        </div>
        <el-table class="tableStyle" max-height="250" :data="form2.resParams" stripe>
          <el-table-column label="序号" width="80" align="center">
            <template #default="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="fieldName" label="中文名称" align="center" :show-overflow-tooltip="{ effect: 'light' }" />
          <el-table-column prop="fieldComment" label="描述" align="center" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
              <el-input v-model="scope.row.fieldComment" placeholder="请输入描述" />
            </template>
          </el-table-column>
          <el-table-column prop="dataType" label="数据类型" align="center" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
              <el-select v-model="scope.row.dataType" clearable placeholder="请选择数据类型">
                <el-option v-for="dict in ds_api_param_type" :key="dict.id" :label="dict.label" :value="dict.value" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="dataType" label="时间格式" align="center" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
              <el-input v-if="scope.row.dataType == '4'" v-model="scope.row.dateFormat" placeholder="请输入时间格式" />
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="exampleValue" label="示例值" align="center" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
              <el-input v-model="scope.row.exampleValue" placeholder="请输入示例值" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="form2.apiServiceType !== '2'">
            <template #default="scope">
              <el-button type="text" size="mini" icon="el-icon-edit" @click="handleDelete(scope.$index, true)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-table v-if="form2.apiServiceType === '1' && false" row-key="id" max-height="250" ref="dragTable"
          class="tableStyle" :data="form2.sortParams" stripe border>
          <el-table-column label="序号" width="80" align="center">
            <template #default="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="fieldName" label="中文名称" align="center" :show-overflow-tooltip="{ effect: 'light' }" />
          <el-table-column label="操作" align="center" :show-overflow-tooltip="{ effect: 'light' }"
            v-if="form2.apiServiceType !== '2'">
            <template #default="scope">
              <el-button type="text" size="mini" icon="el-icon-edit" @click="handlesortDelete(scope.$index, true)">
                删除
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="拖动" width="75" align="center" class-name="allowDrag">
            <template #default="scope">
              <el-icon class="el-icon-s-operation" name="d-caret" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-form>
    <TableDialogapi v-if="fourVisible" :visible="fourVisible" dialog-title="请求参数" :tableData="inputList"
      @confirm="handleFirstConfirm" @close="fourVisible = false" :list="form2.reqParams"
      :apiServiceType="form2.apiServiceType" :inputList="inputList" />
    <tableDialog v-if="firstDialogVisible" :visible="firstDialogVisible" dialog-title="请求参数"
      :tableData="form2.fieldParams" @confirm="handleFirstConfirm" @close="firstDialogVisible = false"
      :list="form2.reqParams" :apiServiceType="form2.apiServiceType" :inputList="inputList" />
    <tableDialog v-if="secondDialogVisible" :visible="secondDialogVisible" dialog-title="返回字段"
      :tableData="form2.fieldParams" @confirm="handleSecondConfirm" @close="secondDialogVisible = false"
      :list="form2.resParams" />
    <tableDialog :visible="sortDialogVisible" dialog-title="返回字段" :tableData="form2.fieldParams"
      @confirm="handlesortConfirm" @close="sortDialogVisible = false" :list="form2.sortParams" />
  </div>
</template>

<script setup name="parameter">
import Sortable from "sortablejs";
import SqlEditor from "@/components/SqlEditor";
import tableDialog from "./tableDialog.vue";
import TableDialogapi from "./tableDialogApi.vue";
import {
  getDaAsset,
} from "@/api/da/asset/asset";
import {
  tableList,
  columnsList,
  getDaAssetRespList
} from "@/api/da/dataSource/dataSource.js";
import { sqlParse } from "@/api/ds/api/api.js";

const { proxy } = getCurrentInstance();
const {
  ds_api_bas_info_api_service_type,
  ds_api_param_type,
  da_api_param_operator,
  ds_api_transmit_type
} = proxy.useDict(
  "ds_api_bas_info_api_service_type",
  "ds_api_param_type",
  "da_api_param_operator",
  "ds_api_transmit_type"
);

const props = defineProps({
  form2: {
    type: Array,
    default: () => [],
    required: true,
  },
  rules: {
    type: Object,
    required: true,
  },
  configTypeOptions: {
    type: Array,
    required: true,
  },
  sourceOptions: {
    type: Array,
    required: true,
  },

  paramTypeOptions: {
    type: Array,
    required: true,
  },
  whereTypeOptions: {
    type: Array,
    required: true,
  },
  splReult: {
    type: Boolean,
    default: false,
  },
  activeReult: {
    type: Number,
    default: 0,
  },
});
let loading = ref(false);
const data = reactive({
  lastSqlText: "", // 存储上次的 SQL 文本，用于检测是否发生变化
  firstDialogVisible: false,
  secondDialogVisible: false,
  sortDialogVisible: false,
  isShowTooltip: false,
  fourVisible: false,
});

const {
  lastSqlText,
  firstDialogVisible,
  secondDialogVisible,
  sortDialogVisible,
  isShowTooltip, fourVisible

} = toRefs(data);
let apiList = ref([]);
function handleTransmitTypeChange(id, falg) {
  if (!falg) {
    props.form2.reqParams = [];
    props.form2.resParams = [];
    props.form2.headerJson = [];
    props.form2.categoryAssetList = '';

  }

  if (id == '1') {
    getDaAssetRespList({ type: 2, status: 2 }).then((response) => {
      console.log("🚀 ~ getDaAssetRespList ~ response:", response)
      apiList.value = response.data;
    });
  } else {
    getDaAssetRespList({ type: 3, status: 2 }).then((response) => {
      apiList.value = response.data;
    });
  }
}
function getTableInfo(sourceId) {
  tableList(sourceId).then((response) => {
    props.form2.filteredTableOptions = response.data;
  });


}
const hasChildren = (row) => {
  const hasChild =
    Array.isArray(row.daAssetApiParamList) &&
    row.daAssetApiParamList.length > 0;
  if (hasChild) {
    if (["Object", "Array"].includes(row.columnType)) {
    } else {
      row.columnType = "Object";
    }
    return true;
  }
  // 如果没有子节点，且 columnType 是 Object 或 Array，则重置为 string
  if (["Object", "Array"].includes(row.columnType)) {
    row.columnType = "string";
  }

  return false;
};
const rules = {
  name: [{ required: true, message: "请输入参数名称", trigger: "blur" }],
  columnType: [
    { required: true, message: "请选择参数类型", trigger: "change" },
  ],
}; const findPosi = (array, targetId, path = "") => {
  for (let i = 0; i < array.length; i++) {
    const item = array[i];
    if (item.id === targetId) {
      return path + i; // 返回当前节点的索引作为路径
    }
    if (item.daAssetApiParamList && item.daAssetApiParamList.length > 0) {
      // 递归查找子节点
      const childPath = `${path}${i}.daAssetApiParamList.`;
      const result = findPosi(item.daAssetApiParamList, targetId, childPath);
      if (result !== null) {
        return result; // 找到则返回路径
      }
    }
  }
  return null; // 没找到返回 null
};
//监听form2.sourceId发生变化查询表格信息
watch(
  () => props.form2?.sourceId,
  (newValue) => {
    if (newValue) {
      console.log("🚀 ~ newValue:", newValue)
      getTableInfo(newValue);
    }
  }
);

watch(
  () => props.form2?.table,
  (newValue, oldValue) => {
    if (
      newValue?.id &&
      oldValue?.id &&
      newValue.id !== oldValue.id
    ) {
      props.form2.reqParams = [];
      props.form2.resParams = [];
    }
  },
  { flush: 'post', deep: false }
);


function sqlParseFunction() {
  if (!props.form2.sourceId) {
    props.$message.warning("数据源不能为空");
    return;
  }
  if (!props.form2.sqlText) {
    props.$message.warning("解析SQL不能为空");
    return;
  }
  const data = {};
  data.sourceId = props.form2.sourceId;
  data.sqlText = props.form2.sqlText;
  sqlParse(data).then((response) => {
    if (response.code === 200) {
      const { data } = response;
      props.form2.reqParams = data.reqParams;
      props.form2.resParams = data.resParams;
      props.form2.lastSqlText = props.form2.sqlText;
      props.splReult = true;
      proxy.$modal.msgSuccess("解析成功，请进行下一步");
    } else {
      proxy.$modal.msgWarning(response.msg || "解析失败，请重试");
    }
  });
}

function setSort() {
  const el = this.$refs.dragTable.$el.querySelectorAll(
    ".el-table__body-wrapper > table > tbody"
  )[0];
  const sortable = Sortable.create(el, {
    handle: ".allowDrag",
    onEnd: (evt) => {
      const targetRow = this.form2.sortParams.splice(evt.oldIndex, 1)[0];
      this.form2.sortParams.splice(evt.newIndex, 0, targetRow);
      for (let index in this.form2.sortParams) {
        this.form2.sortParams[index].sort = parseInt(index) + 1;
      }
    },
  });
}

function validateFormParameter(formName, callback) {
  proxy.$refs[formName].validate((valid) => {
    if (valid) {
      if (props.form2.resParams.length <= 0 && props.form2.apiServiceType != 3) {
        proxy.$message.warning("验证失败，返回字段不能为空");
      } else if (
        props.form2.apiServiceType !== 1 &&
        props.form2.lastSqlText &&
        props.form2.lastSqlText !== props.form2.sqlText
      ) {
        proxy.$message.info("SOL变化请重新解析");
      } else {
        callback(props.form2);
      }
    } else {
      proxy.$message.warning("验证失败，请检查后重试");
      return false;
    }
  });
}
defineExpose({
  validateFormParameter,
  getTableInfo,
});
function closeDialog() {
  firstDialogVisible.value = false;
  secondDialogVisible.value = false;
  sortDialogVisible.value = false;
  fourVisible.value = false;
}
function handleFirstConfirm(val) {
  if (props.form2.apiServiceType != '3') {
    let params = [];
    val.forEach((item) => {
      const exists = props.form2.reqParams.some(param => param.columnName === item.engName);
      if (!exists) {
        params.push({
          paramName: item.engName,
          paramComment: item.cnName || undefined,
          nullable: "0",
          ...item,
        });
      }
    });

    if (params.length > 0) {
      props.form2.reqParams = [...props.form2.reqParams, ...params];
    }
  } else {
    val.forEach((item) => {
      const exists = props.form2.reqParams.some(param => param.columnName === item.columnName);

      if (!exists) {
        props.form2.reqParams.push(item);
      }
    });
  }

  closeDialog();
}
function handleSecondConfirm(val) {
  props.form2.resParams = [];
  props.form2.resParams = val.map((row) => {
    return {
      fieldName: row.engName,
      fieldComment: row.cnName || undefined,
      dataType: row.dataType || undefined,
      ...row,
    };
  });
  closeDialog();
}

function handlesortConfirm(val) {
  props.form2.sortParams = [];
  props.form2.sortParams = val.map((row) => {
    return {
      fieldName: row.columnName,
      fieldComment: row.columnComment || undefined,
      dataType: row.dataType || undefined,
      ...row,
    };
  });
  closeDialog();
}

function configTypeSelectChanged() {
  if (props.form2.apiServiceType != "1") {
    props.form2.reqParams = [];
    props.form2.resParams = [];
    props.form2.headerJson = [];
    props.form2.table = {};
    props.form2.transmitType = ''
    props.form2.apiId = ''
    props.form2.categoryAssetList = ''
  }
}

function sourceSelectChanged() {
  props.form2.reqParams = [];
  props.form2.resParams = [];
  props.form2.table = {};
}
function getDaAssetApply(id) {
  loading.value = true;
  getDaAsset(id)
    .then((response) => {
      if (props.form2.transmitType == 2) {
        props.form2.fieldParams = [];
        props.form2.resParams = [];
        props.form2.headerJson = [];
        props.form2.apiId = response?.data.daAssetGis.id || null;
      } else {
        props.form2.fieldParams = response.data.daAssetApiParamList;
        props.form2.resParams = props.form2.fieldParams.filter((item) => Number(item.type) == 2);
        props.form2.headerJson = props.form2.fieldParams.filter((item) => Number(item.type) == 3);
        props.form2.apiId = response.data.daAssetApi.id;
      }
      console.log("🚀 ~ .then ~  props.form2.apiId:", props.form2.apiId)

    })
    .finally(() => {
      loading.value = false;
    });
}

const inputList = computed(() =>
  props.form2.fieldParams.filter((item) => Number(item.type) == 1)
);

function tableSelectChanged(item) {
  loading.value = true;
  let type = props.sourceOptions.filter(i => i.id == props.form2.sourceId)[0]?.datasourceType
  const data = {
    id: props.form2.sourceId,
    tableName: item.tableName,
    type
  };
  props.form2.tableId = item.id;
  props.form2.tableName = item.tableName;
  columnsList(data)
    .then((response) => {
      props.form2.fieldParams = response.data;
    })
    .finally(() => {
      loading.value = false;
    });
  props.form2.reqParams = [];
  props.form2.resParams = [];

  // listDataColumn(data).then(response => {
  //     if (response.success) {
  //         this.form2.fieldParams = response.data;
  //         this.form2.reqParams = [];
  //         this.form2.resParams = [];
  //         this.form2.sortParams = []
  //         this.form2.sortBy = []
  //         // this.setSort()
  //     } else {
  //         this.$notify({
  //             title: "提示",
  //             dangerouslyUseHTMLString: true, // 启用 HTML 字符串解析
  //             message: response.msg,
  //             type: "error",
  //             duration: 2000
  //         });
  //     }
  // });
}

function openDialog(type) {
  if (type === "first") firstDialogVisible.value = true;

  if (type === "second") secondDialogVisible.value = true;
  if (type === "3") {
    sortDialogVisible.value = true;
  }
  if (type === "four") {
    fourVisible.value = true;
  }

}

function handleDelete(index, falg) {
  if (falg) {
    proxy.form2.resParams.splice(index, 1);
  } else {
    proxy.form2.reqParams.splice(index, 1);
  }
  proxy.$message({
    type: "success",
    message: "删除成功!",
  });
}

function handlesortDelete(index) {
  this.form2.sortParams.splice(index, 1);
  this.$message({
    type: "success",
    message: "删除成功!",
  });
}

function spanMouseenter(e) {
  let target = e.target;
  if (target.clientWidth < target.scrollWidth) {
    this.isShowTooltip = false;
  } else {
    this.isShowTooltip = true;
  }
}

function filterTable(query) { }

function changeTextarea(val) {
  props.form2.sqlText = val;
}
function deleteRow(index, row) {
  const rowIndex = props.form2.reqParams.findIndex(item => item.id == row.id);
  if (rowIndex !== -1) {
    deleteChildren(row);
    props.form2.reqParams.splice(rowIndex, 1);
  }
}
function deleteRows(index, row) {
  const rowIndex = props.form2.resParams.findIndex(item => item.id == row.id);
  if (rowIndex !== -1) {
    deleteChildren(row);
    props.form2.resParams.splice(rowIndex, 1);
  }
}
function deleteChildren(row) {
  if (row.daAssetApiParamList && Array.isArray(row.daAssetApiParamList)) {
    row.daAssetApiParamList.forEach(child => {
      deleteChildren(child);
    });
  }
  row.daAssetApiParamList = [];
}
if (props?.form2?.transmitType && props?.form2?.apiServiceType == '3') {
  handleTransmitTypeChange(props.form2?.transmitType, true);
}

</script>


<style scoped lang="less">
.tableStyle {
  font-size: 14px;
  margin: 0px !important;

  ::v-deep {
    th.el-table__cell>.cell {
      padding: 0 5px !important;
      font-style: normal;
      text-transform: none;
    }

    .el-table__row {
      .el-table__cell {
        padding: 4px 0 !important;
      }
    }

    .el-table__header-wrapper th {
      padding: 4px 0;
    }
  }
}

.home {
  display: flex;
  flex-direction: column;
  height: 88vh;

}

.header-container {
  height: 36px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 0;
  margin: 10px 0;
  border-radius: 4px;
}

.blue-bar {
  background-color: #2666fb;
  width: 5px;
  height: 20px;
  margin-right: 10px;
  border-radius: 2px;
}

.header-text {
  margin: 12px 0
}

.header-left {
  display: flex;
  align-items: center;
  font-size: 16px;
  line-height: 24px;
  font-style: normal;
}

.option-item {
  cursor: pointer;
}

::v-deep.el-select-dropdown__item {
  max-width: 569px !important;
}

.el-input,
.el-select {
  width: 100%;
}

.select-width {
  width: 98%;
}

.header-text {
  font-size: 14px;
  margin-bottom: 3px;
  margin: 10px 0;
}

.add-link {
  margin-left: 10px;
  margin: 10px 0;
}

.sort-section {
  font-size: 14px;
  height: 40px;
  margin: 5px 0;
  display: flex;
  justify-content: space-between;
}

.sql-editor {
  height: 300px;
  margin: 10px 10px;
}

.allowDrag {
  cursor: pointer;
  /* 鼠标悬停时显示小手光标 */
}

.sql-editor-container {
  position: relative;
}

.sql-parse-btn-container {
  position: absolute;
  top: -30px;
  right: 10px;
  z-index: 10;
}
</style>
