<template>
  <el-dialog v-model="visibleDialog" draggable class="dialog" :title="title" destroy-on-close width="60%" :append-to="$refs['app-container']">
    <el-form ref="daDiscoveryTaskRef" :model="form" label-width="120px" @submit.prevent>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name" :rules="[{ required: true, message: '请输入任务名称', trigger: 'blur' }]">
            <el-input v-model="form.name" placeholder="请输入任务名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="数据开发类目"
            prop="catCode"
            :rules="[
              {
                required: true,
                message: '请选择数据开发类目',
                trigger: 'change',
              },
            ]"
          >
            <el-tree-select filterable v-model="form.catCode" :data="deptOptions" :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id" placeholder="请选择数据开发类目" check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="personCharge" :rules="[{ required: true, message: '请选择责任人', trigger: 'change' }]">
            <el-tree-select
              filterable
              v-model="form.personCharge"
              :data="userList"
              :props="{
                value: 'userId',
                label: 'nickName',
                children: 'children',
              }"
              value-key="ID"
              placeholder="请选择责任人"
              check-strictly
              @change="handleContactChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-model="form.contactNumber" placeholder="请输入联系电话" disabled />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="调度周期" prop="crontab" :rules="[{ required: true, message: '请输入调度周期', trigger: 'change' }]">
            <el-input v-model="form.crontab" placeholder="请输入调度周期" readonly>
              <template #append>
                <el-button type="primary" @click="handleShowCron" style="background-color: #2666fb; color: #fff">
                  配置
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务状态" prop="releaseState" :rules="[{ required: true, message: '请选择任务状态', trigger: 'change' }]">
            <el-radio-group style="width: 100%" v-model="form.releaseState" class="el-form-input-width">
              <el-radio v-for="dict in dpp_etl_task_status" :key="dict.value" :value="dict.value" :disabled="dict.value == 1">
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="执行引擎" prop="typaCode" :rules="[{ required: true, message: '请选择执行引擎', trigger: 'change' }]">
            <el-tree-select
              filterable
              :disabled="info"
              v-model="form.typaCode"
              :data="treeData"
              :props="{ value: 'value', label: 'label', children: 'children' }"
              value-key="label"
              check-strictly
              @change="getDaDatasource(true)"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="任务描述" prop="description">
            <el-input v-model="form.description" type="textarea" placeholder="请输入任务描述" />
          </el-form-item>
        </el-col>
      </el-row>
      <div class="h2" @click="templateShow = !templateShow">> 使用模板</div>
      <template v-if="templateAct.id || templateShow">
        <div class="h2-template" v-loading="tempLoading">
          <div class="h2-item" :class="{ act: templateAct.id == item.id }" v-for="item in templateList" :key="item.id" @click="handleTemplate(item)">
            <div class="h2-item-title">{{ item.name }}</div>
            <div class="h2-item-editor">
              <CodeShow
                v-model="item.content"
                :config="{
                  renderSideBySide: false,
                  fontSize: 9,
                  scrollbar: {
                    vertical: 'hidden',
                    horizontal: 'hidden',
                  },
                }"
              />
            </div>
          </div>
          <el-empty style="width: 100%" v-if="total == 0" description="暂无数据" />
        </div>
        <pagination layout="prev, pager, next" v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
      </template>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <template v-if="info">
          <el-button @click="closeDialog">关闭</el-button>
          <el-button type="primary" @click="saveClose">保存</el-button>
        </template>
        <template v-else>
          <el-button @click="saveClose">仅保存</el-button>
          <el-button type="primary" @click="saveData">保存并配置流程</el-button>
        </template>
      </div>
    </template>
  </el-dialog>
  <el-dialog title="Cron表达式生成器" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close>
    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression"> </crontab>
    <!--    <crontab-->
    <!--      ref="crontabRef"-->
    <!--      @hide="openCron = false"-->
    <!--      @fill="crontabFill"-->
    <!--      :expression="expression"-->
    <!--      :Crontab="false"-->
    <!--    >-->
    <!--    </crontab>-->
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import CodeShow from "@/components/SqlEditor/editorShow/index.vue";
import Crontab from "@/components/Crontab/index.vue";
const { proxy } = getCurrentInstance();
import { dppEtlSqlTemp, getNodeUniqueKey } from "@/api/dpp/etl/dppEtlTask";
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/datasource/daDatasource";
const { dpp_etl_task_status } = proxy.useDict("dpp_etl_task_status");
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import { treeData } from "./data";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});

const emit = defineEmits(["update:visible", "confirm"]);

const form = ref({
  // 表单数据
  name: "",
  catCode: "",
  personCharge: "",
  contactNumber: "",
  crontab: "",
  releaseState: "0",
  description: "",
  // json值
  typaCode: "DM",
  // 固定值
  executionType: "PARALLEL", // 初始化为空或默认值
  status: "0",
  datasources: { datasourceId: "" },
});
const total = ref(0);
const queryParams = ref({
  pageNum: 1,
  pageSize: 6,
});
const tempLoading = ref(false);
const getList = async () => {
  tempLoading.value = true;
  try {
    let type = treeData.filter((item) => item.value == form.value.typaCode)[0].id;
    let params = {
      ...queryParams.value,
      type: type,
    };
    dppEtlSqlTemp(params).then((response) => {
      templateList.value = response.data.rows;
      total.value = response.data.total;
    });
  } finally {
    tempLoading.value = false;
  }
};

const templateShow = ref(false);
const templateAct = ref({
  id: "",
  sqlData: { content: "" },
  queryParams: queryParams.value,
  typaCode: "DM",
});
const templateList = ref([]);
const handleTemplate = (item) => {
  templateAct.value = {
    id: item.id,
    sqlData: item,
    queryParams: queryParams.value,
    typaCode: form.value.typaCode,
  };
};

let loading = ref(false);
let createTypeList = ref([]);

/** 查询数据开发任务列表 */
function getDaDatasource(flag) {
  templateAct.value.typaCode = form.value.typaCode;
  // 刷新模板列表
  getList();
  loading.value = true;
  listDaDatasourceNoKafkaByProjectCode({
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    datasourceType: form.value.typaCode,
  }).then((response) => {
    createTypeList.value = response.data;
    if (flag) {
      form.value.datasources.datasourceId = "";
    }
    // console.log("🚀 ~ getDaDatasourceList ~ response:", response);
    loading.value = false;
  });
}
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      form.value = { ...form.value, ...props.data };
      // 模版
      templateAct.value = form.value.draftJson ? JSON.parse(form.value.draftJson) : { ...templateAct.value };
      // 获取模版列表
      queryParams.value = templateAct.value.queryParams || queryParams.value;
      // 执行引擎
      form.value.typaCode = templateAct.value.typaCode;
      getDaDatasource();
      getList();
      form.value.personCharge = Number(form.value.personCharge) || "";
      // 任务状态
      if (form.value.status != null && form.value.status != undefined) {
        form.value.releaseState = form.value.status == "-1" ? "0" : form.value.status;
      }
    } else {
      proxy.resetForm("daDiscoveryTaskRef");
    }
  }
);

// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

let daDiscoveryTaskRef = ref();
// 关闭对话框的方法
const closeDialog = () => {
  emit("update:visible", false);
};
const saveClose = async () => {
  try {
    const valid = await daDiscoveryTaskRef.value.validate();
    if (valid) {
      if (!form.value.code) {
        const response = await getNodeUniqueKey({
          projectCode: userStore.projectCode || "133545087166112",
          projectId: userStore.projectId,
        });
        if (response && response.data) {
          form.value.code = response.data; // Set unique code
        }
      }
      const formData = JSON.parse(JSON.stringify(form.value));
      formData.draftJson = JSON.stringify(templateAct.value);
      console.log("🚀 ~ saveData ~ formData:", formData);
      emit("save", formData);
      emit("update:visible", false);
    } else {
      console.log("表单校验未通过");
    }
  } catch (error) {
    console.error("保存数据时出错:", error);
  }
};
// 保存数据的方法
const saveData = async () => {
  try {
    const valid = await daDiscoveryTaskRef.value.validate();
    if (valid) {
      if (!form.value.code) {
        const response = await getNodeUniqueKey({
          projectCode: userStore.projectCode || "133545087166112",
          projectId: userStore.projectId,
        });
        if (response && response.data) {
          form.value.code = response.data; // Set unique code
        }
      }
      const formData = JSON.parse(JSON.stringify(form.value));
      formData.draftJson = JSON.stringify(templateAct.value);
      console.log("🚀 ~ saveData ~ formData:", formData);
      emit("confirm", formData);
      emit("update:visible", false);
    } else {
      console.log("表单校验未通过");
    }
  } catch (error) {
    console.error("保存数据时出错:", error);
  }
};

let openCron = ref(false);
const expression = ref("");
/** 调度周期按钮操作 */
function handleShowCron() {
  expression.value = form.value.crontab;
  openCron.value = true;
}
/** 确定后回传值 */
function crontabFill(value) {
  form.value.crontab = value;
}

const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find((user) => user.userId == selectedValue);
  console.log("🚀 ~ handleContactChange ~ selectedUser:", selectedUser);
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
// 定义表单验证规则额
</script>
<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
.h2 {
  user-select: none;
  cursor: pointer;
  font-size: 14px;
  color: var(--el-color-primary);
  &:hover {
    color: var(--el-color-primary-light-3);
  }
}
.h2-template {
  display: flex;
  flex-flow: row wrap;
  margin-top: 10px;
  background: #f8f9fa;
  padding: 10px;
  gap: 10px;
  border-radius: 6px;
  .h2-item {
    position: relative;
    width: 32.6%;
    border: 1px solid rgba(5, 5, 5, 0.06);
    border-radius: 6px;
    transition: box-shadow 0.3s, border-color 0.3s;
    &:hover {
      border-color: transparent;
      box-shadow: 0 1px 2px -2px #00000029, 0 3px 6px #0000001f, 0 5px 12px 4px #00000017;
    }
    &.act {
      .h2-item-title,
      .h2-item-editor {
        background: #e6f7ff;
      }
      &::after {
        visibility: visible;
        position: absolute;
        inset-block-start: 2px;
        inset-inline-end: 2px;
        opacity: 1;
        width: 0;
        height: 0;
        border: 6px solid #1890ff;
        border-block-end: 6px solid transparent;
        border-inline-start: 6px solid transparent;
        border-start-end-radius: 2px;
        content: "";
      }
    }
    .h2-item-title {
      background: #fff;
      padding: 8px 12px 0;
      font-size: 14px;
      color: #000000e0;
    }
    .h2-item-editor {
      background: #fff;
      padding: 8px;
      height: 150px;
    }
  }
}
</style>
