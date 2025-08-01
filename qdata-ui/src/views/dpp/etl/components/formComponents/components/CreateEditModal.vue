<template>
  <el-dialog v-model="openvisibleDialog" draggable class="medium-dialog" :title="title">
    <el-form :model="data" label-width="100">
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}{{ row }}
        </span>
      </template>
      <el-form label-width="80px">
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="head-container">
              <el-input v-model="sourceName" placeholder="请输入关键词搜索" clearable :prefix-icon="Search"
                style="margin-bottom: 20px"></el-input>
            </div>
            <div class="head-div">
              <div class="head-container">
                <el-tree ref="tree" :data="filteredTreeOptionslist" node-key="id" :props="defaultProps"
                  highlight-current style="font-size: 14px" default-expand-all :default-checked-keys="[...checkedKeys]"
                  :current-node-key="currentNodeKey" :expand-on-click-node="false" :check-strictly="true"
                  :filter-node-method="filterNode" @node-click="handleNodeClick" @check="handleNodeClick"
                  show-checkbox />
              </div>
            </div>
          </el-col>
          <el-col :span="15" :offset="1">
            <div>
              <div class="base-info">
                <div class="type-name">
                  <span>{{ typeName }}</span>
                </div>
                <div class="base-content">
                  <el-form label-position="left">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="规则编号：" prop="id" label-width="70px">
                          <div>{{ form.id }}</div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="规则名称：" prop="name" label-width="70px">
                          <div>{{ form.name }}</div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="规则级别：" prop="level" label-width="70px">
                          <div>
                            {{ formatValue(form.level, att_rule_level) || "-" }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="规则类型：" prop="type" label-width="70px">
                          <div>
                            {{
                              formatValue(form.type, att_rule_clean_type) || "-"
                            }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="23">
                        <el-form-item label="描述：" prop="description" label-width="45px">
                          <div>{{ form.description }}</div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-form>
                </div>
              </div>
              <div class="hint-div">
                <el-icon color="#2A7BFD" size="16px">
                  <InfoFilled />
                </el-icon>
                <span>请根据数据元的属性信息，给选择的规则设置参数信息</span>
              </div>
              <div>
                <el-form ref="dpDataElemRef" label-position="top" label-width="auto" :model="form" :rules="rules"
                  @submit.prevent>
                  <el-row :gutter="20" v-if="form.id == 8">
                    <el-col :span="24">
                      <el-form-item label="转换类型" prop="ruleConfig.toggleCase.type">
                        <el-select v-model="form.ruleConfig.toggleCase.type" placeholder="请选择转换类型" clearable
                          class="el-form-input-width">
                          <el-option label="大写转换" value="1" />
                          <el-option label="小写转换" value="2" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row :gutter="20" v-if="form.id == 9">
                    <el-col :span="24">
                      <el-form-item label="替换内容" prop="ruleConfig.nullReplace.value">
                        <el-input v-model="form.ruleConfig.nullReplace.value" placeholder="请输入替换内容" clearable />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row :gutter="20" v-if="form.id == 10">
                    <el-col :span="12">
                      <el-col :span="24"> 无需设置参数 </el-col>
                    </el-col>
                  </el-row>
                  <!-- 正则表达式 -->
                  <el-row :gutter="20" v-if="form.id == 14">
                    <el-col :span="12">
                      <el-form-item label="正则表达式替换" prop="ruleConfig.regexValidate.pattern">
                        <el-input v-model="form.ruleConfig.regexValidate.pattern" placeholder="请输入正则表达式" clearable />
                      </el-form-item>
                      <el-form-item label="替换内容" prop="ruleConfig.regexValidate.replacement">
                        <el-input v-model="form.ruleConfig.regexValidate.replacement" placeholder="请输入替换内容" clearable />
                      </el-form-item>
<!--                      <el-form-item label="替换状态" prop="ruleConfig.regexValidate.resultType">-->
<!--                        <el-radio-group v-model="form.ruleConfig.regexValidate.resultType">-->
<!--                          <el-radio label="1">替换成功的</el-radio>-->
<!--                          <el-radio label="2">替换失败的</el-radio>-->
<!--                        </el-radio-group>-->

<!--                      </el-form-item>-->
                    </el-col>
                    <!-- <el-col :span="12">
                      <el-form-item label="替换内容" prop="ruleConfig.regexValidate.pattern">
                        <el-input v-model="form.ruleConfig.regexValidate.pattern" placeholder="请输入正替换内容" clearable />
                      </el-form-item>
                    </el-col> -->
                  </el-row>
                  <!-- 填充默认值输入框 -->
                  <el-row :gutter="20" v-if="form.id == 11">
                    <el-col :span="12">
                      <el-form-item label="填充默认值" prop="ruleConfig.defaultValueFill.value">
                        <el-input v-model="form.ruleConfig.defaultValueFill.value" placeholder="请输入默认值" clearable />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row :gutter="20" v-if="form.id == 13">
                    <el-col :span="24">
                      <el-form-item label="字典值" prop="ruleConfig.dictValueList">
                        <dpCodeMap :row="form.ruleConfig.dictValueList" @dpCodeMapList="dpCodeMapList" />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row :gutter="20" v-if="form.id == 12">
                    <el-col :span="24">
                      <el-form-item label="数值计算" prop="ruleConfig.tokens">
                        <dynamic-form :tableFields="tableFields" v-model:parameters="form.ruleConfig.tokens"
                          ref="paramFormRef" />
                      </el-form-item>
                    </el-col>

                  </el-row>
                </el-form>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <!-- 关闭按钮 -->
        <el-button @click="closeDialog">关闭</el-button>
        <!-- 保存按钮 -->
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import { treeAttCleanRule } from "@/api/att/rule/attCleanRule";
import { Search } from "@element-plus/icons-vue";
import { getCleaningRuleTree } from "@/api/dpp/etl/dppEtlTask";
import dpCodeMap from './dpCodeMap.vue';
import DynamicForm from './DynamicForm.vue';
const { proxy } = getCurrentInstance();
const { att_rule_clean_type, att_rule_level } = proxy.useDict(
  "att_rule_clean_type",
  "att_rule_level"
);
const props = defineProps({
  visibleDialogs: { type: Boolean, default: true },
  title: { type: String, default: "关联清洗规则" },
  row: { type: Object, default: () => ({}) },
  tableFields: { type: Array, default: () => ([]) },
});
// 创建一个本地响应式数据，用来修改表单内容
const data = reactive({
  form: {
    name: null,
    level: null,
    type: null,
    ruleConfig: {
      //大小写转换
      toggleCase: {
        type: "1", //1:大写转换，2:小写转换
      },
      //空值替换
      nullReplace: {
        value: null, //替换字符
      },
      //首尾移除空格
      trimSpace: {},
      // 正则校验
      regexValidate: {
        pattern: "",
        replacement: "",
        resultType: "1"

      },
      // 填充默认值
      defaultValueFill: {
        value: "",
      },
      //存储字典值数组
      dictValueList: [],
      tokens: [
        { type: 'field', value: props?.tableFields[0]?.columnName, valueType: "" },
        { type: 'operator', value: '+', valueType: "" },
        { type: 'field', value: props?.tableFields[0]?.columnName, valueType: "" },
      ],
    },
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dataElemId: null,
    ruleType: 2,
  },
  rules: {
    "ruleConfig.toggleCase.type": [
      { required: true, message: "请选择转换类型", trigger: "change" },
    ],
    "ruleConfig.nullReplace.value": [
      { required: true, message: "请输入空值替换内容", trigger: "blur" },
    ],
    "ruleConfig.regexValidate.pattern": [
      { required: true, message: "正则表达式不能为空", trigger: "blur" },
      {
        validator: (rule, value, callback) => {
          try {
            new RegExp(value);
            callback();
          } catch (e) {
            callback(new Error("请输入合法的正则表达式"));
          }
        },
        trigger: "blur",
      },
    ],
    "ruleConfig.regexValidate.resultType": [
      { required: true, message: "请选择替换状态", trigger: "blur" },

    ],
    "ruleConfig.regexValidate.replacement": [
      { required: true, message: "替换内容不能为空", trigger: "blur" },

    ],
    "ruleConfig.defaultValueFill.value": [
      { required: true, message: "替换内容不能为空", trigger: "blur" },
    ],
    "ruleConfig.dictValueList": [
      { required: true, message: "字典列表不能为空", trigger: "change" },
    ],
    "ruleConfig.tokens": [
      { required: true, message: "数值计算不能为空", trigger: "change" },
    ],
  },
  defaultProps: {
    children: "children",
    label: "name",
    isLeaf: "isLeaf", // 指定是否是叶子节点的字段名
  },
});
const { queryParams, form, rules, defaultProps } = toRefs(data);

const sourceName = ref("");
watch(sourceName, (val) => {
  proxy.$refs.tree.filter(val);
});
const checkedKeys = ref([]);
const currentNodeKey = ref(null);
const typeName = ref();

/** 字典翻译 */
function formatValue(value, dict) {
  return proxy.selectDictLabel(dict, value) || "";
}
function buildTree(list, checkedKeyArr, ruleData) {
  console.log("🚀 ~ list.forEach ~ ruleData:", ruleData);

  list.forEach((item) => {
    if (item.children != null && item.children.length > 0) {
      buildTree(item.children, checkedKeyArr, ruleData); // 递归处理子节点
      return;
    }

    if (item.dataType !== "2") {
      return;
    }

    // 如果没有 ruleRelId，禁用节点
    if (!item.ruleRelId) {
      item.disabled = true;
    }

    let matchingRule = {}; // 用来存储匹配的规则
    if (ruleData) {
      matchingRule = ruleData.find((rule) => rule.ruleId === item.id); // 在 ruleData 中寻找匹配的规则
    }
    if (matchingRule && Object.keys(matchingRule).length > 0) {
      item.disabled = false;
      typeName.value = item.name;
      nextTick(() => {
        form.value = item;
        currentNodeKey.value = item.id;
        updateTreeDisable(filteredTreeOptionslist.value, item.id);
      });
      item.ruleConfig = JSON.parse(matchingRule.ruleConfig); // 设置规则配置
    } else if (item.ruleConfig) {
      item.ruleConfig = JSON.parse(item.ruleConfig);
    } else {
      item.ruleConfig = JSON.parse(JSON.stringify(form.value.ruleConfig));
    }
    // 如果 typeName 为空，设置为当前项的 name
    if (!typeName.value) {
      typeName.value = item.name;
      nextTick(() => {
        form.value = item;
        currentNodeKey.value = item.id;
        updateTreeDisable(filteredTreeOptionslist.value, item.id); // 更新树禁用状态
      });
    }

    // 如果有 ruleRelId，添加到 checkedKeyArr 中
    if (item.ruleRelId) {
      checkedKeyArr.push(item.id);
    }
  });
}
function filterNode(value, data) {
  if (!value) return true;
  return data.name.indexOf(value) !== -1;
}
// 字典值更新
function dpCodeMapList(value) {
  form.value.ruleConfig.dictValueList = value || [];
}

//获取tree数据
function getTree() {
  getCleaningRuleTree().then((response) => {
    const tree = {};
    tree.id = 0;
    tree.dataType = "0";
    tree.name = "规则类型";
    if (response.data == undefined || response.data == null) {
      response.data = [];
    }
    var checkedKeyArr = [];
    buildTree(response.data, checkedKeyArr, props.row.cleanRuleList);
    tree.children = response.data;
    filteredTreeOptionslist.value = [];
    filteredTreeOptionslist.value.push(tree);
    checkedKeys.value = Array.isArray(props.row.cleanRuleList)
      ? props.row.cleanRuleList.map((rule) => rule.ruleId)
      : [];
  });
}
const filteredTreeOptionslist = ref([]);
const emit = defineEmits(["update:visibleDialogs", "confirm"]);

watch(
  () => props.visibleDialogs,
  (newVal) => {
    if (newVal) {
      getTree();
    }
  }
);

// 计算属性处理 v-model
const openvisibleDialog = computed({
  get() {
    return props.visibleDialogs;
  },
  set(newValue) {
    emit("update:visibleDialogs", newValue); Dialogs
  },
});

function updateTreeData(list, id, ruleConfig) {
  list.forEach((item) => {
    if (item.children != null && item.children.length > 0) {
      updateTreeData(item.children, id, ruleConfig);
      return;
    }
    if (item.id == id) {
      item.ruleConfig = ruleConfig;
    }
  });
}
const paramFormRef = ref(null);
//节点选中事件
async function handleNodeClick(data) {
  if (data.dataType != "2") {
    return;
  }
  //判断当前节点是否被选中
  var flag = false;
  var nodes = proxy.$refs.tree.getCheckedNodes();
  if (nodes.filter((node) => node.id === form.value.id).length > 0) {
    //校验表单
    if (form.value.id == '12') {
      try {
        let res = await paramFormRef.value.validate();
        console.log("🚀 ~ handleNodeClick ~ res:", res)
        flag = true;
      } catch (e) {
        console.log("🚀 ~ handleNodeClick ~ e:", e)
        flag = false;
      }
    } else {
      await proxy.$refs.dpDataElemRef.validate((valid) => {
        if (valid) {
          flag = true;
        }
      });
    }
  } else {
    flag = true;
  }

  if (!flag) {
    proxy.$message.error("请完善规则");
    proxy.$refs.tree.setChecked(data, false);  // 取消勾选当前节点
    return;
  }
  //保存上一个规则的数据
  var oldData = form.value;
  if (oldData) {
    updateTreeData(
      filteredTreeOptionslist.value,
      oldData.id,
      JSON.parse(JSON.stringify(oldData)).ruleConfig
    );
  }
  typeName.value = data.name;
  form.value = data;
  updateTreeDisable(filteredTreeOptionslist.value, data.id);
}
function reset() {
  form.value = {
    name: null,
    level: null,
    type: null,
    ruleConfig: {
      //大小写转换
      toggleCase: {
        type: "1", //1:大写转换，2:小写转换
      },
      //空值替换
      nullReplace: {
        value: null, //替换字符
      },
      //首尾移除空格
      trimSpace: {},
      // 正则校验
      regexValidate: {
        pattern: "",
      },
      // 填充默认值
      defaultValueFill: {
        value: "",
      },
      //存储字典值数组
      dictValueList: [],
      tokens: [
        { type: 'field', value: props?.tableFields[0]?.columnName, valueType: "" },
        { type: 'operator', value: '+', valueType: "" },
        { type: 'field', value: props?.tableFields[0]?.columnName, valueType: "" },
      ],


    },
  }
  typeName.value = null;
}
function updateTreeDisable(list, id) {
  list.forEach((item) => {
    if (item.children != null && item.children.length > 0) {
      updateTreeDisable(item.children, id);
      return;
    }
    if (item.id == id) {
      item.disabled = false;
    }
  });
}

// 关闭对话框的方法
const closeDialog = () => {
  emit("update:visibleDialogs", false); // 更新父组件的 visibleDialogs 为 false，关闭对话框
  reset();
  typeName.value = "";
};

//表单提交
function submitForm() {
  //获取选中后的节点
  var nodes = proxy.$refs.tree.getCheckedNodes();

  var saveData = [];
  nodes.forEach((node) => {
    var data = {
      id: node.ruleRelId || null,
      dataElemId: props.dataElemId,
      ruleType: props.ruleType,
      ruleId: node.id,
      ruleName: node.name,
      ruleConfig: JSON.stringify(node.ruleConfig),
    };
    saveData.push(data);
  });
  //保存数据
  reset();
  typeName.value = "";
  emit("confirm", saveData);
}
</script>
<style lang="scss" scoped>
.base-info {
  margin-top: 5px;

  .type-name {
    color: #000;
    font-size: 20px;
    font-weight: bold;
  }

  .base-content {
    margin-top: 20px;
    padding-left: 25px;

    :deep(.el-form-item__label) {
      padding: 0 0 0 0 !important;
    }

    :deep(.el-form-item) {
      margin-bottom: 5px;
    }
  }
}

.hint-div {
  margin: 10px 0px 20px 20px;
  border-top: 1px solid rgba(204, 204, 204, 0.5);
  border-right: 1px solid rgba(204, 204, 204, 0.5);
  border-bottom: 1px solid #e5f1f8;
  border-left: 1px solid #e5f1f8;
  border-radius: 2px;
  padding: 10px;
  box-shadow: -1px 1px 2px #e5f1f8;
  display: flex;
  align-items: center;

  span {
    margin-left: 5px;
  }
}

// 设置只有叶子节点有多选框
:deep(.el-tree-node) {
  .is-leaf+.el-checkbox .el-checkbox__inner {
    display: inline-block !important;
  }

  .el-checkbox__input>.el-checkbox__inner {
    display: none;
  }
}
</style>
