<template>
  <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
    :show-close="false" destroy-on-close :close-on-click-modal="false">
    <el-form ref="dpModelRefs" :rules="rules" :model="form" label-width="165px" @submit.prevent v-loading="loading">
      <div class="steps">
        <ul class="steps-inner">
          <li :class="currentStep >= index ? 'statusEnd' : 'cur'" :id="'li' + index" v-for="(item, index) in stepsList"
            :key="index" :style="{ width: `calc(100% / ${stepsList.length})` }" @click="currentStep = index">
            <span>{{ item.title }}</span>
            <div :id="'div' + item.id" v-if="index < stepsList.length - 1"
              :class="currentStep >= index ? 'titleItem' : 'jiaoActive'"></div>
            <div class="interval"></div>
          </li>
        </ul>
      </div>
      <div class="basicAttr hasMsg" v-if="currentStep == 0">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="节点名称" prop="name" :validate-state="'success'">
              <el-input v-model="form.name" placeholder="请输入节点名称" />
              <span class="msg"><el-icon>
                  <InfoFilled />
                </el-icon>任何有意义且能描述本节点的说明(选择API后自动填充为API的名称)</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="节点类型" prop="typeName">
              <el-select v-model="form.typeName" placeholder="请选择类型" filterable disabled>
                <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
              <span class="msg"><el-icon>
                  <InfoFilled />
                </el-icon>节点输入类型</span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="API URL" prop="apiUrl">
              <el-input v-model="form.apiUrl" placeholder="请输入API URL">
                <template #prepend>
                  <el-select :rules="[{ required: false }]" v-model="form.apiMethod" placeholder="请选择请求方式"
                    @change="handleApiMethodChange" style="width: 90px">
                    <el-option label="GET" value="GET" />
                    <el-option label="POST" value="POST" />
                  </el-select>
                </template>
              </el-input>
              <span class="msg"><el-icon>
                  <InfoFilled />
                </el-icon>请指定API地址可用${变量id}获取变量值作为URL的一部分如:http://ip:80/api/${id}</span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回数据格式" prop="returnFormat">
              <el-select v-model="form.returnFormat" placeholder="请选择返回数据格式">
                <el-option label="JSON对象(需要指定JSON中包含数据体所在字段)" :value="1" />
                <el-option label="JSON数组(API返回的是一个JSON数组[])" :value="2" />
                <!--                <el-option disabled label="XML字符串(返回XML字符串系统将自动转为JSON对像)" :value="3" />-->
              </el-select>
              <span class="msg"><el-icon>
                  <InfoFilled />
                </el-icon>指定返回数据的格式,普通字符串默认设置在data中第一行的responseBody字段中</span>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.returnFormat == 1">
            <el-form-item label="返回数据行所在字段" prop="returnDataLine">
              <template #label>
                <div class="returnDataLineLabel">
                  <span>返回数据行所在字段</span>
                  <el-tooltip class="box-item" effect="dark" placement="top"
                    content="如API返回格式为:{status:1,rows:[{}...]}则此处填写$.rows,支持添加多个如果rows为json字符串就需要再配置一个例如 {status:1,data:rows:[{}...]}  [$.data,$.rows] ">
                    <el-icon>
                      <QuestionFilled />
                    </el-icon>
                  </el-tooltip>
                </div>
              </template>
              <div class="returnDataLine" v-for="(domain, index) in form.returnDataLine" :key="index">
                <el-input v-model="form.returnDataLine[index]" placeholder="请输入返回数据行所在字段"
                  style="width: 85%; margin-right: 10px" />
                <el-button @click.prevent="addDomain(domain)" icon="Plus"
                  v-if="index == form.returnDataLine.length - 1">
                </el-button>
                <el-button @click.prevent="removeDomain(domain)" icon="Minus" v-if="index > 0"> </el-button>
              </div>
              <span class="msg"><el-icon>
                  <InfoFilled />
                </el-icon>API返回的JSON对象如果数据行不在data字段中时请指定字段Id,详细语法请参考：<a target="_blank"
                  href="https://docs.apifox.com/jsonpath">https://docs.apifox.com/jsonpath</a></span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分页读取" prop="pageFlag">
              <el-radio-group v-model="form.pageFlag">
                <el-radio :value="0"> 否 </el-radio>
                <el-radio :value="1"> 是 </el-radio>
              </el-radio-group>
              <span class="msg"><el-icon>
                  <InfoFilled />
                </el-icon>是表示分页循环读取,同时必须在API输入参数指定${pageNo}变量作为API的输入参数,否则不能自动分页</span>
            </el-form-item>
          </el-col>
          <template v-if="form.pageFlag == 1">
            <el-col :span="24">
              <el-form-item label="分页参数" prop="page.pageNoKey">
                <el-input v-model="form.page.pageNoKey" placeholder="请输入分页参数" />
                <span class="msg"><el-icon>
                    <InfoFilled />
                  </el-icon>需在参数中用${pageNo}进行占位，pageNo为当前参数的值</span>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="最大读取页数" prop="page.maxPage">
                <el-input v-model="form.page.maxPage" placeholder="请输入最大读取页数" />
                <span class="msg"><el-icon>
                    <InfoFilled />
                  </el-icon>指定最大读取页数支持${变量id}</span>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="休眠时间" prop="interval">
                <el-input-number v-model="form.interval" :min="0" controls-position="right" />
                <span class="msg"><el-icon>
                    <InfoFilled />
                  </el-icon>合并传输表示会把多次分页查询到的数据合并到一起传输给后续节点，数据量大的时候请使用分页传输</span>
              </el-form-item>
            </el-col>
          </template>
          <el-col :span="24">
            <el-form-item label="备注" prop="description">
              <el-input v-model="form.description" type="textarea" :rows="1" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <div class="inParams" v-if="currentStep == 1">
        <div v-show="form.inParams.type === 1">
          <div class="justify-between mb15">
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button type="primary" icon="Plus" @click="handleInParamsAdd">添加参数</el-button>
              </el-col>
            </el-row>
          </div>
          <el-table stripe height="400px" :data="form.inParams.urlParams" v-loading="loadingList">
            <el-table-column label="参数字段" fixed="left" align="left" prop="name"
              :show-overflow-tooltip="{ effect: 'light' }">
              <template #default="{ row, $index }">
                <el-select v-model="row.name" placeholder="请选择或参数字段" filterable allow-create default-first-option
                  :style="{ width: '100%' }">
                  <el-option v-for="item in defaultHeaderNames" :key="item" :label="item" :value="item"
                    :disabled="form.inParams.urlParams.some((p) => p !== row && p.name == item)" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="参数值" fixed="left" align="left" prop="value"
              :show-overflow-tooltip="{ effect: 'light' }" :style="{ width: '100%' }">
              <template #default="{ row, $index }">
                <el-input type="textarea" v-model="row.value" placeholder="分页读取必须传S{pageNo},使用JSONPath取变量S.fieldld"
                  :autosize="{ minRows: 1 }" resize="vertical" />
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" align="left" width="120">
              <template #default="{ $index }">
                <el-button link type="danger" icon="Delete" @click="handleInParamsParam($index)"> 删除 </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="bodyParams mb15" v-show="form.inParams.type === 2">
          <div class="editor-title">自定义Body字符串：</div>
          <VAceEditor v-model:value="form.inParams.bodyParams" ref="aceRef" class="editor-content" placeholder=""
            :options="bodyParamsOptions" lang="json" theme="chrome" />
        </div>
        <!-- 说明区域 -->
        <div class="inparams-desc">
          <div v-show="form.inParams.type === 1" style="margin-top: 10px">说明: 分页读取参数传基本属性中设置的值例如${pageNo}</div>
          <div v-show="false">
            <span>该文本支持Velocity语法取值</span>
            <template v-for="(item, index) in bodyParamsExample" :key="index">
              <a style="margin-left: 10px" @click="handleBodyParamsExample(item)">{{ item.name }}</a>
              <el-divider direction="vertical" v-if="index != bodyParamsExample.length - 1" />
            </template>
          </div>
          <div style="margin-top: 10px">
            <el-switch v-model="form.inParams.type" :active-value="2" :inactive-value="1" active-text="Body请求参数"
              inactive-text="表单请求参数" inline-prompt style="line-height: 88px" />
          </div>
        </div>
      </div>
      <div class="Header" v-if="currentStep == 2">
        <div class="justify-between mb15">
          <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
              <el-button type="primary" icon="Plus" @click="handleHeaderAdd">添加Header头</el-button>
            </el-col>
          </el-row>
        </div>
        <el-table stripe height="500px" :data="form.apiHeaders" v-loading="loadingList">
          <el-table-column label="header" fixed="left" align="left" prop="name"
            :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="{ row, $index }">
              <el-select v-model="row.name" placeholder="请选择或输入header" filterable allow-create default-first-option
                :style="{ width: '100%' }">
                <el-option v-for="item in headerNameOptions" :key="item" :label="item" :value="item"
                  :disabled="form.apiHeaders.some((p) => p !== row && p.name === item)" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="参数值" fixed="left" align="left" prop="value"
            :show-overflow-tooltip="{ effect: 'light' }" :style="{ width: '100%' }">
            <template #default="{ row, $index }">
              <el-input type="textarea" v-model="row.value" placeholder="使用JsonPath取indoc中或全局变量${变量id},${s.data[0].id}"
                :autosize="{ minRows: 1 }" resize="vertical" />
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" align="left" width="120">
            <template #default="{ $index }">
              <el-button link type="danger" icon="Delete" @click="handleRemoveHeader($index)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="output" v-loading="loadingList" v-if="currentStep == 3">
        <div class="justify-between mb15">
          <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
              <el-button type="primary" icon="Plus" @click="handleAddField">添加字段</el-button>
              <el-button type="danger" icon="Delete" @click="handleDeleteField" :disabled="!multiple.length"> 删除字段
              </el-button>
              <el-button icon="Connection" @click="handleFetchFields"> 从结果JSON中自动分析 </el-button>
            </el-col>
          </el-row>
        </div>

        <el-table stripe height="500px" class="outParams" :data="form.outParams"
          @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column label="序号" type="index" width="80" align="left" />

          <el-table-column label="字段名称" align="left" prop="name" width="150">
            <template #default="scope">
              <el-form-item label-width="0px" :prop="'outParams.' + scope.$index + '.name'"
                :rules="[{ required: true, message: '请输入字段', trigger: 'input' }]">
                <el-input v-model="scope.row.name" placeholder="请输入字段" style="width: 100%" />
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="字段类型" align="left" prop="type" width="130">
            <template #default="scope">
              <el-form-item label-width="0px" :prop="'outParams.' + scope.$index + '.type'"
                :rules="[{ required: true, message: '请选择字段类型', trigger: 'change' }]">
                <el-select v-model="scope.row.type" placeholder="请选择字段类型" style="width: 100%">
                  <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="字段格式" align="left" prop="format" width="150">
            <template #default="scope">
              <el-form-item label-width="0px" :prop="'outParams.' + scope.$index + '.format'">
                <el-input :disabled="scope.row.type != 'DATE'" v-model="scope.row.format" placeholder="请输入字段格式"
                  style="width: 100%" />
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="缺省值" align="left" prop="defaultValue">
            <template #default="scope">
              <el-form-item label-width="0px" :prop="'outParams.' + scope.$index + '.defaultValue'">
                <el-input v-model="scope.row.defaultValue" placeholder="请输入值" style="width: 100%" />
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="120">
            <template #default="scope">
              <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-dialog class="fieldsDialog" v-model="fieldsVisible" draggable title="字段冲突处理" :show-close="false"
          destroy-on-close>
          <div style="padding: 10px 0">已有 {{ resultJSON.length }} 个字段，检测到 {{ newFields }} 个新字段，如何处理？</div>
          <template #footer>
            <el-button type="warning" @click="saveFields('addNewOnly')">增加新的</el-button>
            <!-- <el-button type="primary" @click="saveFields('addAll')">增加所有</el-button> -->
            <el-button type="danger" @click="saveFields('clearAndAddAll')">清除并增加所有</el-button>
            <el-button @click="closeFields">取消</el-button>
          </template>
        </el-dialog>
      </div>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button type="primary" v-if="currentStep > 0" @click="handleMinus">上一步</el-button>
        <el-button type="primary" v-if="currentStep < stepsList.length - 1" @click="handlePlus">下一步</el-button>
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup name="ApiForm">
import { VAceEditor } from "vue3-ace-editor";
import "@/views/dpp/utils/aceConfig.js";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey, getResponseColumnReqVO } from "@/api/dpp/task/index.js";
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user.js";
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});
const emit = defineEmits(["update", "confirm"]);
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update", newValue);
  },
});
// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);

//#region 步进器及下一步
const currentStep = ref(0);
const stepsList = ref([{ title: "基本属性" }, { title: "输入参数" }, { title: "Header" }, { title: "输出字段" }]);
const handleMinus = () => {
  currentStep.value--;
};
const handlePlus = async () => {
  const valid = await dpModelRefs.value.validate();
  if (!valid) return;
  let returnDataLine = form.value.returnDataLine.filter((item) => item == "");
  if (returnDataLine.length > 0) return proxy.$message.warning("添加失败，返回数据行所在字段不能为空");
  currentStep.value++;
};
// #endregion
//#region 基本属性
let dpModelRefs = ref();
let form = ref({
  name: "API输入组件",
  typeName: "API输入组件",
  apiUrl: window.location.origin + import.meta.env.VITE_APP_BASE_API,
  apiMethod: "GET",
  returnFormat: 1,
  returnDataLine: ["$.data.rows"],
  pageFlag: 0,
  page: {
    pageNoKey: "pageNo", //分页参数key，需在参数中用${pageNo}进行占位，pageNo为当前参数的值
    maxPage: 10, //最大页数
  },
  interval: 0, //间隔时间 单位毫秒(默认0)
  description: "",
  apiHeaders: [], //header
  inParams: {
    urlParams: [],
    type: 1,
    bodyParams: "",
  },
  outParams: [],
});

const rules = ref({
  name: [{ required: true, message: "任何有意义且能描述本节点的说明(选择API后自动填充为API的名称)", trigger: "blur" }],
  typeName: [{ required: true, message: "任何有意义且能描述本节点的说明(选择API后自动填充为API的名称)", trigger: "blur" }],
  apiUrl: [{ required: true, message: "请指定API地址可用${变量id}获取变量值作为URL的一部分如:http://ip:80/api/${id}", trigger: "blur" }],
  returnFormat: [{ required: true, message: "指定返回数据的格式,普通字符串默认设置在data中第一行的responseBody字段中", trigger: "change" }],
  returnDataLine: [{ required: true, message: "API返回的JSON对象如果数据行不在data字段中时请指定字段Id", trigger: "blur" }],
  pageFlag: [{ required: true, message: "是表示分页循环读取,同时必须在API输入参数指定${pageNo}变量作为API的输入参数,否则不能自动分页", trigger: "change" }],
  "page.pageNoKey": [{ required: true, message: "需在参数中用${pageNo}进行占位，pageNo为当前参数的值", trigger: "blur" }],
  "page.maxPage": [{ required: true, message: "指定最大读取页数支持${变量id}", trigger: "blur" }],
  interval: [{ required: true, message: "合并传输表示会把多次分页查询到的数据合并到一起传输给后续节点，数据量大的时候请使用分页传输", trigger: "blur" }],
});
const addDomain = () => {
  form.value.returnDataLine.push("");
};
const removeDomain = (item) => {
  const index = form.value.returnDataLine.indexOf(item);
  if (index !== -1) {
    form.value.returnDataLine.splice(index, 1);
  }
};
// #endregion
// #region 输入参数
const defaultHeaderNames = ["pageNo", "processId", "transactionId"];
const handleInParamsAdd = (type) => {
  const emptyHeader = form.value.inParams.urlParams.find((item) => !item.name || item.name.trim() === "");
  if (emptyHeader) {
    proxy.$message.warning("添加失败，请先填写完整已有的参数字段");
    return;
  }
  const nameList = form.value.inParams.urlParams.map((item) => item.name.trim().toLowerCase());
  const nameSet = new Set(nameList);
  if (nameList.length !== nameSet.size) {
    proxy.$message.warning("添加失败，参数字段不能重复");
    return;
  }
  const newRow = {
    name: "",
    // columnType: "string",
    // requestFlag: "0",
    value: "",
  };
  form.value.inParams.urlParams = [...form.value.inParams.urlParams, newRow];
};
watch(
  () => form.value.pageFlag,
  (newVal) => {
    if (newVal === 1) {
      const fixedParams = form.value.inParams.urlParams.filter((p) => p.isFixed);
      if (fixedParams.length >= 2) return;
      const paramNames = form.value.inParams.urlParams.map((p) => p.name?.trim().toLowerCase());
      const addIfMissing = (name, defaultValue = "") => {
        if (!paramNames.includes(name.toLowerCase())) {
          form.value.inParams.urlParams.push({
            name,
            value: defaultValue,
            isFixed: false,
          });
        }
      };

      addIfMissing("pageNo", "");
      addIfMissing("pageSize", "");
    } else {
      // 分页关闭，移除所有系统自动添加的参数
      form.value.inParams.urlParams = form.value.inParams.urlParams.filter((p) => !p.isFixed);
    }
  }
);
const bodyParamsOptions = ref({
  enableBasicAutocompletion: true,
  enableSnippets: true,
  enableLiveAutocompletion: true,
  printMarginColumn: 30,
  displayIndentGuides: false,
  enableEmmet: true,
  tabsize: 2,
  fontsize: 14,
  useWorker: true,
  showPrintMargin: false,
  useSoftTabs: true, // 使用空格替代 Tab
  highlightActiveLine: true, // 高亮当前行
  enableMultiselect: true,
  readOnly: false,
  showFoldwidgets: true,
  fadeFoldwidgets: true,
  wrap: true,
});
const bodyParamsExample = ref([
  {
    name: "JSON数组示例",
    value:
      '{\n    "processId":"$!{processId}",\n    "transactionId":"$!{transactionId}",\n    "pageNo":"$!{pageNo}",\n    "P_NOWTIME":"$!{P_NOWTIME}",\n    "P_NOWDATE":"$!{P_NOWDATE}",\n    "data":[\n        #foreach($item in $data)\n        {\n            "userId":"$!{item.userId}",\n            "userName":"$!{item.userName}"\n        }#if($velocityCount!=$data.size()),#end\n        #end\n    ]\n}',
  },
  {
    name: "JSON对象示例 ",
    value:
      '{\n    "pageNo":"$!{pageNo}",\n    "P_NOWTIME":"$!{P_NOWTIME}",\n    "P_NOWDATE":"$!{P_NOWDATE}",\n    "processId":"$!{processId}",\n    "transactionId":"$!{transactionId}",\n    "userId":"$!{data[0].userId}",\n    "userName":"$!{data[0].userName}"\n}',
  },
  {
    name: "Data数组",
    value: "${data}",
  },
]);

const handleApiMethodChange = (e) => {
  if (e === "POST") {
    const header = form.value.apiHeaders.find((item) => item.name === "Content-Type")
    if (!header) {
      form.value.apiHeaders.push({
        name: "Content-Type",
        value: "application/json",
      });
    }
  }
  if (e === "GET") {
    const header = form.value.apiHeaders.find((item) => item.name === "Content-Type")
    if (header && header.value === "application/json") {
      form.value.apiHeaders.splice(header)
    }
  }
  const acceptHeader = form.value.apiHeaders.find((item) => item.name === "Accept")
  if (!acceptHeader) {
    form.value.apiHeaders.push({
      name: "Accept",
      value: "*/*",
    });
  }
}

const handleBodyParamsExample = (item) => {
  form.value.inParams.bodyParams = item.value;
};
// #endregion
// #region Header
const handleInParamsParam = (index) => {
  const item = form.value.inParams.urlParams[index];
  if (item?.isFixed) {
    proxy.$message.warning("删除失败，系统参数不可删除");
    return;
  }
  form.value.inParams.urlParams.splice(index, 1);
};
const headerNameOptions = ["Content-Type", "Authorization", "Connection", "Accept", "access_token"];
const handleHeaderAdd = (type) => {
  // 检查已有apiHeaders的name字段是否都有值
  const emptyHeader = form.value.apiHeaders.find((item) => !item.name || item.name.trim() === "");
  if (emptyHeader) {
    proxy.$message.warning("添加失败，请先填写完整已有的 Header");
    return; // 有空字段，阻止新增
  }
  // 2. 检查是否存在重复的 name（忽略大小写和空格）
  const nameList = form.value.apiHeaders.map((item) => item.name.trim().toLowerCase());
  const nameSet = new Set(nameList);
  if (nameList.length !== nameSet.size) {
    proxy.$message.warning("添加失败，Header 名称不能重复");
    return; // 阻止新增
  }
  const newRow = {
    name: "",
    // columnType: "string",
    // requestFlag: "0",
    value: "",
  };
  form.value.apiHeaders = [...form.value.apiHeaders, newRow];
};
const handleRemoveHeader = (index) => {
  form.value.apiHeaders.splice(index, 1);
};
// #endregion
// #region 输出参数
const outParams = ref([]); // 来源表格
const multiple = ref([]);
const handleSelectionChange = (selection) => {
  multiple.value = selection;
};
const columntype = [
  { value: "STRING", label: "STRING" },
  { value: "INTEGER", label: "INTEGER" },
  { value: "LONG", label: "LONG" },
  { value: "DOUBLE", label: "DOUBLE" },
  { value: "DATE", label: "DATE" },
  { value: "TIMESTAMP", label: "TIMESTAMP" },
];
const handleAddField = () => {
  // 如果有任意一个已有字段 columnName 为空，阻止新增
  const lastEmpty = form.value.outParams.find((item) => !item.name);

  if (lastEmpty) {
    proxy.$message.warning("添加失败，请先填写当前字段名称");
    return;
  }

  form.value.outParams.push({
    name: null,
    type: "STRING",
    defaultValue: null,
    source: form.value.name,
    format: "-",
  });
};
const handleDeleteField = () => {
  multiple.value.forEach((item) => {
    handleDelete(item);
  });
};
// 从结果JSON中自动分析
const fieldsVisible = ref(false);
const resultJSON = ref([]);
const newFields = computed(() => {
  // 计算已有字段名称
  const existingNames = form.value.outParams.map((f) => f.name);
  // 找到新字段中不在已有字段中的字段
  const newUniqueFields = resultJSON.value.filter((f) => !existingNames.includes(f.name));
  return newUniqueFields.length;
});
const closeFields = () => {
  fieldsVisible.value = false;
};
const saveFields = (action) => {
  switch (action) {
    case "addNewOnly": {
      // 计算已有字段名称
      const existingNames = form.value.outParams.map((f) => f.name);
      // 找到新字段中不在已有字段中的字段
      const newUniqueFields = resultJSON.value.filter((f) => !existingNames.includes(f.name));
      // 加入到 outParams 中
      form.value.outParams = form.value.outParams.concat(deepCopy(newUniqueFields));
      break;
    }
    // 暂时没用
    case "addAll": {
      // 这里先清空，再加全部字段，避免重复
      form.value.outParams = deepCopy(resultJSON.value);
      break;
    }

    case "clearAndAddAll": {
      form.value.outParams = [];
      // 恢复原始备份字段
      form.value.outParams = deepCopy(resultJSON.value);
      break;
    }
  }
  fieldsVisible.value = false;
};
const handleFetchFields = async () => {
  const params = {
    ...form.value,
  };
  // console.log(params, "params");
  loadingList.value = true;
  getResponseColumnReqVO(params)
    .then((res) => {
      if (res.code == 200) {
        resultJSON.value = res.data;
        resultJSON.value.forEach((item) => {
          item.format = "-";
          item.source = form.value.name;
        });
        fieldsVisible.value = true;
      } else {
        proxy.$message.warning('操作失败，请重试');
      }
    })
    .finally(() => {
      loadingList.value = false;
    });
};
function handleDelete(row) {
  // 从 outParams 中删除对应字段
  const idxTable = form.value.outParams.findIndex((item) => item.name === row.name);
  if (idxTable !== -1) {
    form.value.outParams.splice(idxTable, 1);
  } else {
    proxy.$message.warning("删除失败，字段未找到");
  }
}
// #endregion
const off = () => {
  proxy.resetForm("dpModelRefs");
  // 清空表格字段数据
  TablesByDataSource.value = [];
  form.value.outParams = [];
};
function validateUniqueAndNotEmptyName(list, label) {
  if (!list || list.length === 0) return true; // 空则跳过校验

  const names = list.map((item) => (item.name || "").trim());
  const hasEmpty = names.some((name) => !name);
  if (hasEmpty) {
    proxy.$message.warning(`校验未通过，${label}名称不能为空`);
    return false;
  }

  const uniqueNames = new Set(names);
  if (uniqueNames.size !== names.length) {
    proxy.$message.warning(`校验未通过，${label}名称不能重复`);
    return false;
  }

  return true;
}

const scrollToFirstError = () => {
  // 等 DOM 把错误提示渲染出来
  nextTick(() => {
    const firstError = document.querySelector('.el-form-item__error')
    if (firstError) {
      // 平滑滚动到该元素
      firstError.scrollIntoView({
        behavior: 'smooth',
        block: 'center'   // 垂直居中
      })
    }
  })
}

// 保存数据
const saveData = async () => {
  try {
    // 异步验证表单
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;
    let returnDataLine = form.value.returnDataLine.filter((item) => item == "");
    if (returnDataLine.length > 0) return proxy.$message.warning("校验未通过，返回数据行所在字段不能为空");
    if (!validateUniqueAndNotEmptyName(form.value.apiHeaders, "Header")) return;
    if (!validateUniqueAndNotEmptyName(form.value.inParams.urlParams, "参数字段")) return;
    // 如果没有 code，就调用接口获取唯一的 code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false; // 结束加载状态
      form.value.code = response.data; // 设置唯一的 code
    }
    const taskParams = {
      ...form.value.taskParams,
      ...form.value,
      outParams: form.value.outParams,
      tableFields: form.value.outParams.map((item) => {
        return {
          ...item,
          columnName: item.name,
          columnType: item.type,
        };
      }),
    };
    form.value.taskParams = taskParams;
    // console.log(form.value, "params");
    emit("confirm", form.value);

  } catch (error) {
    scrollToFirstError()
    loading.value = false; // 确保错误发生时也结束加载状态
  }
};
const closeDialog = () => {
  off();
  // 关闭对话框
  emit("update", false);
};

// 监听属性变化
function deepCopy(data) {
  if (data === undefined || data === null) {
    return {}; // 或者返回一个默认值
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    console.error(e);
    return {}; // 或者返回一个默认值
  }
}
// 监听属性变化
watchEffect(() => {
  if (props.visible) {
    console.log(props.currentNode.data, "props.currentNode.data");
    // 数据源
    form.value = { ...form.value, ...props.currentNode.data, ...deepCopy(props.currentNode.data.taskParams) };
    console.log("🚀 ~ form.value:", form.value);
    form.value.outParams = props.currentNode?.data.taskParams.outParams;
  } else {
    off();
  }
});
</script>
<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

:deep(.el-textarea__inner) {
  height: auto;
}

.steps {
  margin-bottom: 20px;
  width: 100%;
  display: flex;
  color: #303133;
  transition: 0.3s;

  &::-webkit-scrollbar {
    height: 5px;
  }

  .steps-inner {
    width: 100%;
    list-style: none;
    font-size: 14px;
    height: 20px;
    display: flex;
    align-items: flex-end;
    margin: 20px auto;
    cursor: pointer;
    padding: 0;

    li {
      height: 40px;
      background: #d7d8da;
      color: #666;
      text-align: center;
      line-height: 40px;
      font-weight: 500;
      /*width: 20%;*/
      /*flex-basis: 100%;*/
      position: relative;
      padding-left: 10px;
    }

    .statusEnd {
      width: 33%;
      color: rgb(255, 255, 255);
      background: var(--el-color-primary) !important;
    }

    .cur {
      color: rgb(255, 255, 255);
      border-left-color: rgba(120, 140, 160, 0.2) !important;
    }

    .reult {
      color: rgb(102, 102, 102);
      background: rgb(215, 216, 218);
    }

    /*三角形绘制*/

    .jiao {
      width: 0;
      height: 0;
      border-top: 15px solid transparent;
      /*高度一半*/
      border-left: 20px solid #e8514a;
      /*调整宽度*/
      border-bottom: 15px solid transparent;
      /*高度一半*/
      color: rgb(102, 102, 102);
      border-left-color: rgb(215, 216, 218);
      position: absolute;
      right: -20px;
      /*跟宽度保持一致*/
      top: 0;
      z-index: 9999;
    }

    .interval {
      width: 0;
      height: 0;
      border-top: 26px solid transparent;
      /*高度一半*/
      border-left: 26px solid #fff;
      /*调整宽度*/
      border-bottom: 26px solid transparent;
      /*高度一半*/
      position: absolute;
      right: -26px;
      /*跟宽度保持一致*/
      top: -6px;
      z-index: 1;
    }

    .titleItem {
      width: 0;
      height: 0;
      border-top: 20px solid transparent;
      /*高度一半*/
      border-left: 20px solid #e8514a;
      /*调整宽度*/
      border-bottom: 20px solid transparent;
      /*高度一半*/
      color: rgb(255, 255, 255);
      border-left-color: var(--el-color-primary) !important;
      position: absolute;
      right: -20px;
      /*跟宽度保持一致*/
      top: 0;
      z-index: 2;
    }

    .jiaoActive {
      width: 0;
      height: 0;
      border-top: 20px solid transparent;
      /*高度一半*/
      border-left: 20px solid #e8514a;
      /*调整宽度*/
      border-bottom: 20px solid transparent;
      /*高度一半*/
      color: rgb(255, 255, 255);
      border-left-color: #d7d8da !important;

      position: absolute;
      right: -20px;
      /*跟宽度保持一致*/
      top: 0;
      z-index: 2;
    }
  }
}

.returnDataLineLabel {
  display: flex;
  align-items: center;
}

.returnDataLine {
  display: flex;
  width: 100%;
  margin-bottom: 10px;

  &:last-of-type {
    margin-bottom: 0;
  }
}

.bodyParams {
  display: flex;
  justify-content: space-between;

  .editor-title {
    margin-right: 10px;
  }

  .editor-content {
    flex: 1;
    height: 400px;
  }
}

:deep(.basicAttr) {
  height: 580px;
  overflow: hidden auto;
}

:deep(.fieldsDialog) {
  .el-dialog__body {
    height: auto !important;
  }
}

.inparams-desc {
  line-height: 2;
  color: #606266;
  font-size: 14px;

  :deep(.el-switch__core) {
    height: 25px;
    line-height: 25px;
    border-radius: 100px;
  }

  .inparams-tip {}
}

.outParams {
  :deep(.el-form-item) {
    margin-top: 16px;
  }
}
</style>
