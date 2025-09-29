<template>
  <!-- 新增或修改数据发现任务对话框 -->
  <el-dialog v-model="visibleDialog" draggable class="dialog" destroy-on-close>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="daDiscoveryTaskRef" :model="form" :rules="rules" label-width="120px" @submit.prevent>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入任务名称" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="数据发现类目" prop="catCode">
            <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID" placeholder="请选择所属类目"
              check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据库连接" prop="datasourceId" :rules="[
            {
              required: true,
              message: '请选择数据库连接',
              trigger: 'change',
            },
          ]">
            <el-select v-model="form.datasourceId" placeholder="请选择数据连接" @change="handleDatasourceChange" filterable>
              <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
          <el-col :span="12">
              <el-form-item label="数据库地址" prop="ip">
                  <el-input v-model="form.ip" placeholder="请输入数据库类型" disabled />
              </el-form-item>
          </el-col>
        <el-col :span="12" v-show="false" >
          <el-form-item label="数据库类型" prop="datasourceType" >
              <el-input v-model="form.datasourceType" placeholder="请输入数据库类型" disabled />
          </el-form-item>
        </el-col>

      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="contactId">
            <el-tree-select filterable v-model="form.contactId" :data="userList" :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }" value-key="ID" placeholder="请选择责任人" check-strictly @change="handleContactChange" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="调度周期" prop="cronExpression">
                <el-input v-model="form.cronExpression" placeholder="请输入调度周期" readonly>
                    <template #append>
                        <el-button type="primary" @click="handleShowCron" style="background-color: #2666fb; color: #fff">
                            配置
                            <i class="el-icon-time el-icon--right"></i>
                        </el-button>
                    </template>
                </el-input>
            </el-form-item>
        </el-col>
<!--        <el-col :span="12">-->
<!--            <el-form-item label="联系电话" prop="contactNumber">-->
<!--                <el-input disabled v-model="form.contactNumber" placeholder="请输入联系电话" />-->
<!--            </el-form-item>-->
<!--        </el-col>-->
      </el-row>
      <el-row :gutter="20">
          <el-col :span="24">
              <el-form-item label="描述" prop="description">
                  <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
              </el-form-item>
          </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务状态" prop="status">
            <el-radio-group v-model="form.status" :disabled="true">
              <el-radio v-for="dict in da_discovery_task_status" :key="dict.value" :value="dict.value">{{ dict.label
              }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报警邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入报警邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
          <el-col :span="24">
              <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
              </el-form-item>
          </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <!-- 关闭按钮 -->
        <el-button @click="closeDialog">取消</el-button>
        <!-- 保存按钮 -->
        <el-button type="primary" @click="saveData">确定</el-button>
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
import Crontab from "@/components/Crontab/index.vue";
import { getDaDiscoveryTask } from "@/api/da/discovery/discoveryTask";

const { proxy } = getCurrentInstance();
const { sys_job_status, sys_job_group, da_discovery_task_status } =
  proxy.useDict("sys_job_status", "sys_job_group", "da_discovery_task_status");
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  createTypeList: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
});
let rules = ref({
  name: [{ required: true, message: "请输入任务名称", trigger: "blur" }],
  catCode: [
    { required: true, message: "请选择数据发现类目", trigger: "change" },
  ],
  datasourceId: [
    { required: true, message: "请选择数据库连接", trigger: "change" },
  ],
  datasourceType: [
    { required: true, message: "请选择数据库类型", trigger: "change" },
  ],
  // ip: [{ required: true, message: "请选择数据库地址", trigger: "change" }],
  contactId: [{ required: true, message: "请选择责任人", trigger: "change" }],
  misfirePolicy: [
    { required: true, message: "请选择执行策略", trigger: "change" },
  ],
  cronExpression: [
    { required: true, message: "请输入调度周期", trigger: "change" },
  ],
  concurrent: [
    { required: true, message: "请选择是否并发", trigger: "change" },
  ],
  // status: [{ required: true, message: "请选择状态", trigger: "change" }],
  jobGroup: [{ required: true, message: "请选择任务分组", trigger: "change" }],
});
const emit = defineEmits(["update:visible", "confirm"]);
let openCron = ref(false);
const expression = ref("");
/** 调度周期按钮操作 */
function handleShowCron() {
  expression.value = form.value.cronExpression;
  openCron.value = true;
}
/** 确定后回传值 */
function crontabFill(value) {
  form.value.cronExpression = value;
}
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  form.value.contact = selectedUser?.nickName;
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
// 创建一个本地响应式数据，用来修改表单内容
const form = ref({
  name: "",
  catCode: "",
  datasourceId: "",
  datasourceType: "",
  ip: "",
  contact: "",
  contactId: "",
  contactNumber: "",
  cronExpression: "",
  status: "1",
  jobGroup: "DEFAULT",
  misfirePolicy: "1",
  concurrent: "1",
  email: "",
});
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      // if (data.id) {
      //   getDaDiscoveryTaskDetailById();
      // }
      if (props.data.id) {
        form.value = JSON.parse(JSON.stringify(props.data));
        form.value.datasourceId = Number(form.value.datasourceId) || "";
        form.value.misfirePolicy = Number(form.value.misfirePolicy) || "";
        form.value.concurrent = Number(form.value.concurrent) || "";
      } else {
        form.value.catCode = props.data.catCode || "";
        console.log("🚀 ~ props.data:", props.data.catCode);
      }
    } else {
      clearForm();
    }
  }
);
// function getDaDiscoveryTaskDetailById() {
//   getDaDiscoveryTask(_id).then(response => {
//     form.value = response.data;
//   });
// }
const handleDatasourceChange = (value) => {
  const selectedDatasource = props.createTypeList.find(
    (item) => item.id === value
  );
  if (selectedDatasource) {
    form.value.ip = selectedDatasource.ip;
    form.value.datasourceConfig = selectedDatasource.datasourceConfig;
    form.value.datasourceType = selectedDatasource.datasourceType;
    form.value.datasourceName = selectedDatasource.datasourceName;
    form.value.port = selectedDatasource.port;
  }
};
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
let daDiscoveryTaskRef = ref(); // 保存数据的方法
const saveData = () => {
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      // 如果表单验证通过，发出数据
      emit("confirm", form.value);
      emit("update:visible", false);
    } else {
      // 如果表单验证失败，进行处理（比如提示用户）
      console.log("表单验证失败");
    }
  });
};

// 清空表单数据
const clearForm = () => {
  form.value = {
    name: "",
    catCode: "",
    datasourceId: "",
    datasourceType: "",
    ip: "",
    contactId: "",
    contactNumber: "",
    cronExpression: "",
    status: "1",
    jobGroup: "DEFAULT",
    misfirePolicy: "1",
    concurrent: "1",
    email: "",
    contact: "",
  };
};
</script>
