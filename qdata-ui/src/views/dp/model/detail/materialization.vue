<template>
   <!--  逻辑物化的弹窗  -->
  <el-dialog v-model="localVisible" :title="title" draggable class="warn-dialog" destroy-on-close>
    <!-- <div class="centered-text">
      您将对选择的{{
        ids?.length
      }}个逻辑模型进行逻辑物化，请选择数据资产的数据连接
    </div> -->
    <el-form ref="dpModelRefs" :model="form" :rules="rules" label-width="100px" @submit.prevent>
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
          <el-form-item label="数据库类型" prop="datasourceType">
            <el-input v-model="form.datasourceType" placeholder="请输入数据库类型" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据库地址" prop="ip">
            <el-input v-model="form.ip" placeholder="请输入数据库类型" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述">
            <el-input type="textarea" placeholder="请输入描述" v-model="form.description" :min-height="192" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input type="textarea" placeholder="请输入备注" v-model="form.remark" :min-height="192" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="confirmDialog"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import {
  createMaterializedTable,
  getDaDatasourceList,
} from "@/api/dp/model/model";
import { defineProps, defineEmits, ref, computed, watch } from "vue";
const { proxy } = getCurrentInstance();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  ids: { type: Array, default: () => [] },
});
let createTypeList = ref();
// 监听 `visible` 的变化
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      getDaDatasourceListList();
    }
  }
);

const getDaDatasourceListList = async () => {
  try {
    const response = await getDaDatasourceList();
    createTypeList.value = response.data;
  } catch (error) {
    console.error("请求失败:", error);
  }
};

const emit = defineEmits(["update:dialogFormVisible", "confirm"]);

// 处理弹窗显示状态
const localVisible = computed({
  get() {
    return props.visible;
  },
  set(value) {
    emit("update:dialogFormVisible", value);
  },
});

const handleDatasourceChange = (value) => {
  const selectedDatasource = createTypeList.value.find(
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

const form = ref({
  datasourceId: "",
  datasourceType: "",
  ip: "",
  datasourceConfig: "",
  datasourceType: "",
  port: "",
  datasourceName: "",
});

const rules = ref({
  datasourceId: [
    { required: true, message: "请选择数据连接", trigger: "blur" },
  ],
});

const closeDialog = () => {
  form.value = {
    datasourceId: "",
    datasourceType: "",
    ip: "",
    datasourceConfig: "",
    datasourceType: "",
    port: "",
    datasourceName: "",
  };
  localVisible.value = false;
  proxy.resetForm("dpModelRefs");
};

const confirmDialog = async () => {
  try {
    // 使用 Promise 进行表单验证
    const isValid = await new Promise((resolve, reject) => {
      proxy.$refs["dpModelRefs"].validate((valid) => {
        if (valid) {
          resolve(true); // 表单验证通过
        } else {
          reject("表单验证失败"); // 验证失败时拒绝
        }
      });
    });

    if (isValid) {
      // 创建物化表格
      const response = await createMaterializedTable({
        modelId: props.ids,
        ...form.value,
      });
      console.log(response);

      // 提交数据
      emit("confirm", form.value);

      // 关闭对话框
      closeDialog();
      // 提示成功
      proxy.$modal.msgSuccess(response.msg);
    }
  } catch (error) {
    // 捕获并提示错误信息
    proxy.$message.warning(response.msg);
    console.log(error);
  }
};
</script>

<style scoped lang="less">
.warn-dialog .el-dialog__body {
  max-height: 500px;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}

.dialog-footer .el-button {
  margin-left: 10px;
}

.centered-text {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 10%;
  text-align: center;
  font-size: 14px;
  color: #333;
}
</style>
