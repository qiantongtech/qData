<template>
  <el-dialog v-model="visibleDialog" draggable class="dialog" :title="title" destroy-on-close>
    <el-form ref="daDiscoveryTaskRef" :model="form" :rules="rules" label-width="120px" @submit.prevent
      :disabled="title == '任务详情'">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="作业名称" prop="name">
            <el-input v-if="title != '任务详情'" v-model="form.name" placeholder="请输入作业名称" />
            <div class="form-readonly" v-else>{{ form.name || '-' }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="作业类目" prop="catCode">
            <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID" placeholder="请选择作业类目"
              check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="personCharge">
            <el-tree-select filterable v-model="form.personCharge" :data="userList" :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }" value-key="ID" placeholder="请选择责任人" check-strictly @change="handleContactChange" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-if="title != '任务详情'" v-model="form.contactNumber" placeholder="请输入联系电话" disabled>
            </el-input>
            <div class="form-readonly" v-else>{{ form.contactNumber || '-' }}</div>

          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="调度周期" prop="crontab">
            <el-input v-if="title != '任务详情'" v-model="form.crontab" placeholder="请输入调度周期" readonly>
              <template #append>
                <el-button type="primary" @click="handleShowCron" style="background-color: #2666fb; color: #fff">
                  配置
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.crontab }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-if="title != '任务详情'" v-model="form.description" type="textarea" placeholder="请输入描述" />
            <div class="form-readonly" v-else>{{ form.description || '-' }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务状态" prop="releaseState">
            <el-radio-group v-if="title != '任务详情'" v-model="form.releaseState" class="el-form-input-width">
              <el-radio v-for="dict in dpp_etl_task_status" :key="dict.value" :label="dict.value"
                :disabled="dict.value == 1">
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>{{dpp_etl_task_status.find(item => item.value ==
              form.releaseState)?.label ||
              '-'}}</div>
          </el-form-item>
        </el-col>
      </el-row>
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

  <el-dialog title="Cron表达式生成器" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close>
    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression">
    </crontab>
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
import { listAttJobCat } from "@/api/att/cat/jobCat/jobCat";
import Crontab from "@/components/Crontab/index.vue";
import { deptUserTree } from "@/api/system/system/user.js";
const { proxy } = getCurrentInstance();
const { dpp_etl_task_status } = proxy.useDict("dpp_etl_task_status");

const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});
const deptOptions = ref([]);
/** 下拉树结构 */
function getDeptTree() {
  listAttJobCat({ type: 4 }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "作业类目",
        value: "",
        children: deptOptions.value,
      },
    ];
  });
}
const emit = defineEmits(["update:visible", "confirm"]);

const form = ref({
  name: "",
  catCode: "", // 可以初始化为空，也可以设为默认值
  executionType: "", // 初始化为空或默认值
  crontab: "",
  releaseState: "0",
  description: "",
  contactNumber: "",
  catCode: "",
  personCharge: "",
});

watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      console.log("🚀 ~ props.data.taskConfig :", props.data);

      console.log("🚀 ~ newVal:", newVal);
      form.value = JSON.parse(JSON.stringify(props.data.taskConfig || {}));
      form.value.personCharge = Number(form.value.personCharge) || "";
      if (!props.data.id) {
        form.value.releaseState = form.value.releaseState
          ? form.value.releaseState
          : "0";
      }
      getDeptTree();
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

// 关闭对话框的方法
const closeDialog = () => {
  emit("update:visible", false);
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
let daDiscoveryTaskRef = ref();
// 保存数据的方法
const saveData = () => {
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {

      emit("confirm", form.value);
      emit("update:visible", false);
    } else {

      console.log("表单校验未通过");
    }
  });
};
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  console.log("🚀 ~ handleContactChange ~ selectedUser:", selectedUser);
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
// 定义表单验证规则
const rules = {
  name: [{ required: true, message: "作业名称不能为空", trigger: "blur" }],
  catCode: [{ required: true, message: "作业类目不能为空", trigger: "change" }],
  executionType: [
    { required: true, message: "执行策略不能为空", trigger: "change" },
  ],
  crontab: [{ required: true, message: "调度周期不能为空", trigger: "change" }],
  // releaseState: [
  //   { required: true, message: "任务状态不能为空", trigger: "change" },
  // ],
  personCharge: [
    { required: true, message: "责任人不能为空", trigger: "blur" },
  ],
};
</script>
<style lang="scss" scoped>
:deep(.el-select) {
  .el-select__wrapper.is-disabled {
    cursor: default;
    background-color: #fcfcfc;
    --el-select-disabled-color: #333;

    .el-select__suffix {
      display: none;
    }
  }
}
</style>
