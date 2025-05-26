<template>
  <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
    showCancelButton :show-close="false" destroy-on-close>
    <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="节点名称" prop="name" :rules="[
            { required: true, message: '请输入节点名称', trigger: 'change' },
          ]">
            <el-input v-model="form.name" placeholder="请输入节点名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="typeName">
            <el-select v-model="form.taskParams.typeName" placeholder="请输入类型" filterable disabled>
              <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="上级节点" prop="description">
            <el-select v-model="form.taskParams.parentId" placeholder="请输入类型" filterable @change="handleNodeChange">
              <el-option v-for="dict in nodeOptions" :key="dict.value" :label="dict.label"
                :value="dict.value"></el-option>
            </el-select> </el-form-item></el-col>
      </el-row> -->
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="where条件" prop="where">
            <el-input v-model="form.taskParams.where" type="textarea" placeholder="请输入where条件" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="center">
        <span class="blue-text">规则设置</span>
      </el-divider>
      <el-table stripe height="310px" :data="tableFields" v-loading="loadingList">
        <el-table-column label="序号" type="index" width="80" align="center">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段名称" align="center" prop="columnName" show-overflow-tooltip />
        <el-table-column label="字段注释" align="center" prop="description" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="字段类型" align="center" prop="columnType" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.columnType || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="规则" align="center" prop="elementname">
          <template #default="scope">
            <el-tooltip v-if="scope.row.cleanRuleList?.length" effect="dark" placement="top">
              <template #content>
                <div v-for="item in scope.row.cleanRuleList" :key="item.ruleName">
                  {{ item.ruleName || "-" }}
                </div>
              </template>
              <div>
                {{ scope.row.cleanRuleList[0]?.ruleName || "-"
                }}<span v-if="scope.row.cleanRuleList.length > 1">等</span>
              </div>
            </el-tooltip>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit"
              @click="handleRule({ ...scope.row, index: scope.$index + 1 })">关联规则</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
  <CreateEditModal :visibleDialogs="opens" @update:visibleDialogs="opens = $event" @confirm="submitForm" :row="row"
    :tableFields="tableFields" />
</template>
<script setup>
import CreateEditModal from "./components/CreateEditModal.vue";
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import { typeList } from "@/utils/graph";
import { getNodeUniqueKey } from "@/api/dpp/etl/dppEtlTask";
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user";
import {
  transformColumnsData, createNodeSelect
} from "@/views/dpp/etl/components/opBase.js";
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  graph: { type: Object, default: () => ({}) },
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
let tableFields = ref([]);
// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref([]);
let dpModelRefs = ref();
let form = ref({});

function handleRule(data) {
  row.value = {};
  row.value = data;
  opens.value = true;
}
const submitForm = (value) => {
  if (row.value?.index) {
    tableFields.value[row.value.index - 1] = {
      ...tableFields.value[row.value.index - 1],
      cleanRuleList: value,
      elementId: value.map((item) => item.ruleId),
    };

    opens.value = false;
  }
};

const off = () => {
  proxy.resetForm("dpModelRefs");

  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// 保存数据
const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate();
    if (!valid) return;

    // 如果没有 code，就调用接口获取唯一的 code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      });
      loading.value = false;

      form.value.code = response.data; // 设置唯一的 code
    }
    const taskParams = form.value?.taskParams;
    taskParams.tableFields = tableFields.value;
    taskParams.mainArgs.cleanRuleList = transformColumnsData(taskParams.tableFields)
    console.log("🚀 ~ saveData ~ form.value:", form.value)
    emit("confirm", form.value);
    emit("update", false);
  } catch (error) {
    console.error("保存数据失败:", error);
    loading.value = false;
  }
};
const closeDialog = () => {
  off();

  emit("update", false);
};

// 监听属性变化
function deepCopy(data) {
  if (data === undefined || data === null) {
    return {};
  }
  try {
    return JSON.parse(JSON.stringify(data));
  } catch (e) {
    return {};
  }
}
let nodeOptions = ref([]);
// 监听属性变化
watchEffect(() => {
  if (!props.visible) {
    off();
    return;
  }
  form.value = deepCopy(props.currentNode?.data || {});
  nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
  tableFields.value = props.currentNode?.data.taskParams.tableFields || [];
});
// 选择节点
const handleNodeChange = (selectedNodeId) => {
  // 根据 selectedNodeId 获取节点数据
  const selectedNode = props.graph.getCellById(selectedNodeId); // 获取选中的节点
  if (selectedNode) {
    const nodeData = selectedNode.getProp("data"); // 获取节点的 data 数据
    console.log("选中的节点数据:", nodeData.taskParams.tableFields);
    tableFields.value = nodeData.taskParams.tableFields;

  }
};
</script>


<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
