<template>
  <el-dialog v-model="visibleDialog" draggable :title="nodeTitle" destroy-on-close class="medium-dialog"
    :append-to="$refs['app-container']">
    <el-form v-loading="loading" :model="form" label-width="120px" @submit.prevent
      :disabled="currentNode.data.releaseState != '-2'">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskConfig.name" :rules="[
            { required: true, message: '请输入任务名称', trigger: 'blur' },
          ]">
            <el-input v-model="form.taskConfig.name" placeholder="请输入任务名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据开发类目" prop="taskConfig.catCode" :rules="[
            {
              required: true,
              message: '请选择数据开发类目',
              trigger: 'change',
            },
          ]">
            <el-tree-select filterable v-model="form.taskConfig.catCode" :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }" value-key="code" placeholder="请选择数据开发类目"
              check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="taskConfig.personCharge" :rules="[
            { required: true, message: '请选择责任人', trigger: 'change' },
          ]">
            <el-tree-select filterable v-model="form.taskConfig.personCharge" :data="userList" :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }" value-key="ID" placeholder="请选择责任人" check-strictly @change="handleContactChange" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="taskConfig.contactNumber">
            <el-input v-model="form.taskConfig.contactNumber" placeholder="请输入联系电话" disabled />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="taskConfig.description">
            <el-input v-model="form.taskConfig.description" type="textarea" placeholder="请输入描述" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form v-loading="loading" ref="taskRef" :model="form" label-width="120px" @submit.prevent>
            <el-form-item label="任务优先级" prop="priority" :rules="[
              {
                required: true,
                message: '请选择任务优先级',
                trigger: 'change',
              },
            ]">
              <el-select style="width: 100%" v-model="form.priority" placeholder="请选择任务优先级" clearable
                class="el-form-input-width" :disabled="false">
                <el-option v-for="dict in dpp_etl_node_priority" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据连接类型" prop="taskParams.typaCode
              {
                required: true,
                message: '请选择数据连接类型',
                trigger: 'change',
              },
            ]">
            <el-tree-select filterable v-model="form.taskParams.typaCode" :data="treeDatas"
              :props="{ value: 'label', label: 'label', children: 'children' }" value-key="label" check-strictly
              disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="脚本" prop="taskParams.rawScript" :rules="[
            {
              required: true,
              message: '请输入脚本',
              trigger: 'change',
            },
          ]">
            <shell-editor ref="editorRef" :value="form.taskParams.rawScript" class="shell-editor"
              :readonly="currentNode.data.releaseState != '-2'" @changeTextarea="changeTextarea" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="center">
        <span class="blue-text">参数</span>
      </el-divider>
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="openDialog()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
        </el-row>
      </div>

      <el-table stripe height="300px" :data="form.taskParams.localParams">
        <el-table-column label="序号" type="index" width="80" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="参数名称" align="left" prop="prop" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.prop || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="参数类型" align="left" prop="type">
          <template #default="scope">
            {{ scope.row.type || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="参数值" align="left" prop="value">
          <template #default="scope">
            {{ scope.row.value || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit"
              @click="openDialog({ ...scope.row, index: scope.$index + 1 })">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <!-- 关闭按钮 -->
        <el-button @click="closeDialog">关闭</el-button>
        <!-- 保存按钮 -->
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
  <taskConfigUploadDialog :visible="open" @update:visible="open = $event" @confirm="handletaskConfig" :data="row" />
  <el-dialog title="Cron表达式生成器" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close>
    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression">
    </crontab>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import Crontab from "@/components/Crontab/index.vue";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataDevCat } from "@/api/att/cat/dataDevCat/dataDevCat.js";
const { proxy } = getCurrentInstance();
import {
  getDaDatasourceList,
  getNodeUniqueKey,
  updateProcessDefinition,
  createProcessDefinition,
  etlTask,
} from "@/api/dpp/task/index.js";
const { dpp_etl_node_priority } = proxy.useDict("dpp_etl_node_priority");
import { fetchNodeUniqueKey } from "@/utils/opBase.js";
import ShellEditor from "@/components/ShellEditor/index.vue";
import taskConfigUploadDialog from "./tableFormSql.vue";
import useUserStore from "@/store/system/user.js";
const userStore = useUserStore();
import { treeData as treeDatas } from "@/views/dpp/task/developTask/data.js";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});

const emit = defineEmits(["update:visible", "confirm"]);
const nodeTitle = computed(() => {
  return "数据开发";
});
let deptOptions = ref([]);
async function getDeptTree() {
  try {
    const taskCatRes = await listAttDataDevCat();

    // 处理部门类别数据
    deptOptions.value = [
      {
        id: "",
        name: "数据开发类目",
        value: "",
        id: 0,
        children: proxy.handleTree(taskCatRes.data, "id", "parentId"),
      },
    ];
  } finally {
    // 所有请求完成后，隐藏 loading
  }
}

function changeTextarea(val) {
  form.value.taskParams.rawScript = val;
  console.log("rawScript", form.value.taskParams.rawScript);
}

const form = ref({
  taskConfig: {
    name: "",
    catCode: "",
    personCharge: "",
    contactNumber: "",
    releaseState: "-2",
    description: "",
    crontab: "0 0 0 1 1 ? *",
  },
  taskParams: {
    rawScript: "",
    localParams: []
  },
});
let loading = ref(false);
let createTypeList = ref([]);
// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
const handletaskConfig = (obj) => {
  if (row.value?.index) {
    // 如果找到匹配项，则更新
    form.value.taskParams.localParams[row.value.index - 1] = {
      ...form.value.taskParams.localParams[row.value.index - 1],
      ...obj,
    };
  } else {
    // 如果找不到，则追加到数组末尾
    form.value.taskParams.localParams.push({ ...obj });
  }
};

const handleDelete = (row) => {
  form.value.taskParams.localParams = form.value.taskParams.localParams.filter(
    (item) => item.prop !== row.prop
  );
};
function handleVisibleChange() {
  if (props.currentNode.data.taskParams.subTaskId) {
    getList();
  }
  getDeptTree();
}
watchEffect(() => {
  if (props.visible) {
    if (!props.currentNode.data.code) {
      form.value.taskParams.typaCode =
        props.currentNode.data.taskParams?.typaCode;
    }
  } else {
    proxy.resetForm("taskRef");
    deptOptions.value = {};
  }
});

handleVisibleChange();
// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// 关闭对话框的方法
const closeDialog = () => {
  emit("update:visible", false);
};

let openCron = ref(false);
const expression = ref("");
/** 调度周期按钮操作 */
function handleShowCron() {
  expression.value = form.value.taskConfig.crontab;
  openCron.value = true;
}
let taskRef = ref();
const dataJson = async () => {
  let obj = {
    componentType: "54",
    taskType: "SHELL",
  };
  if (!form.value.code) {
    const code = await fetchNodeUniqueKey();
    form.value.code = code;
  }
  const { componentType, taskType } = obj || {};
  // 更新表单数据
  const formData = JSON.parse(JSON.stringify(form.value));
  formData.taskParams.type = 3;
  formData.componentType = componentType;
  formData.taskType = taskType;
  const { taskConfig, priority, ...taskDefinition } = formData;
  // 准备需要返回的数据
  const taskRelationData = {
    name: "",
    preTaskCode: 0,
    preTaskVersion: 0,
    postTaskCode: formData?.code || 0,
    postTaskVersion: formData?.version || 0,
    conditionType: "NONE",
    conditionParams: {},
  };

  const locations = [{ taskCode: formData?.code, x: 0, y: 0 }];

  return {
    taskRelationJson: JSON.stringify([taskRelationData]),
    locations,
    taskDefinitionList: JSON.stringify([taskDefinition]),
    projectCode: userStore.projectCode || "133545087166112",
    projectId: userStore.projectId,
    ...taskConfig,
    type: "3",
  };
};
let exportData2 = ref({});
const handleSuccess = () => {
  const message = form.value?.id ? "修改成功" : "新增成功";
  proxy.$modal.msgSuccess(message);
};
// 保存数据的方法
const saveData = async () => {
  try {
    const valid = await taskRef.value.validate();
    if (!valid) return;
    loading.value = true;
    const { id, taskParams, code, releaseState } = props.currentNode.data || {};
    let exportData = null;
    if (releaseState == "-2") {
      exportData = await dataJson();
      const res = taskParams.subTaskId
        ? await updateProcessDefinition({
          ...exportData,
          id: taskParams.subTaskId,
        })
        : await createProcessDefinition(exportData);

      if (res.code == "200") {
        let newCode = code || (await fetchNodeUniqueKey());
        let obj = {
          ...props.currentNode.data,
          type: res.data.type,
          name: res.data.name,
          priority: form.value.priority,
          taskParams: {
            ...props.currentNode.data.taskParams,
            subTaskId: res.data.id,
            processDefinitionCode: res.data.code,
            releaseState: "-2",
            type: "3",
          },
          releaseState: "-2",
          code: newCode,
        };

        emit("confirm", obj);
        console.log("🚀 ~ saveData ~ obj:", obj);
        emit("update:visible", false); // 仅在成功时关闭弹窗
      } else {
        proxy.$modal.msgWarning(res.message || "操作失败，请重试");
      }
    } else {
      form.value.name = form.value.taskConfig.name;
      let newCode = code || (await fetchNodeUniqueKey());
      emit("confirm", {
        ...props.currentNode.data,
        code: newCode,
        priority: form.value.priority,
      });
      emit("update:visible", false); // 仅在成功时关闭弹窗
    }
  } catch (error) {
    proxy.$modal.msgWarning("请求失败，请重试");
  } finally {
    loading.value = false;
  }
};

function getList() {
  loading.value = true;
  etlTask(props.currentNode.data.taskParams.subTaskId).then((response) => {
    form.value = {
      ...response.data.taskDefinitionList[0],
      taskConfig: response.data.taskConfig,
      id: response.data.id,
      priority: props.currentNode.data.priority,
    };
    form.value.taskConfig.personCharge = Number(
      response.data.taskConfig.personCharge
    );
    console.log("🚀 ~ etlTask ~  form.value :", form.value);
    console.log(
      "🚀 ~ etlTask ~ response.data.taskDefinitionList[0]:",
      response.data.taskDefinitionList[0]
    );
    loading.value = false;
    // 部门
  });
}
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  form.value.taskConfig.contactNumber = selectedUser?.phonenumber || "";
};
// 定义表单验证规则额
</script>
<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

:deep(.shell-editor) {
  width: 880px;
  border: 1px solid #c8cbd3;
}
</style>
