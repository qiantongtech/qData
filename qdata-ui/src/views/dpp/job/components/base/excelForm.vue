<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-form
        v-loading="loading"
        ref="daDiscoveryTaskRef"
        :model="form"
        label-width="120px"
        @submit.prevent
      >
        <el-form-item
          label="任务优先级"
          prop="priority"
          :rules="[
            {
              required: true,
              message: '请选择任务优先级',
              trigger: 'change',
            },
          ]"
        >
          <el-select
            style="width: 100%"
            v-model="form.priority"
            placeholder="请选择任务优先级"
            clearable
            class="el-form-input-width"
            :disabled="false"
          >
            <el-option
              v-for="dict in dpp_etl_node_priority"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="12">
      <el-form-item
        label="上传附件"
        prop="taskParams.excelFile"
        :rules="[{ required: true, message: '请上传附件', trigger: 'change' }]"
      >
        <FileUploadbtn
          :limit="1"
          :file-type="['xls', 'xlsx']"
          v-model="form.taskParams.excelFile"
          :isShowTip="false"
          :dragFlag="false"
          :showDelete="false"
        />
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item
        label="起始行"
        prop="taskParams.startData"
        :rules="[
          { required: true, message: '请输入起始行', trigger: 'change' },
        ]"
      >
        <el-input-number
          :step="1"
          step-strictly
          v-model="form.taskParams.startData"
          style="width: 100%"
          controls-position="right"
          :min="1"
          value-on-clear="min"
        />
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item
        label="起始列"
        prop="taskParams.startColumn"
        :rules="[
          { required: true, message: '请输入起始列', trigger: 'change' },
        ]"
      >
        <el-input-number
          :step="1"
          step-strictly
          v-model="form.taskParams.startColumn"
          style="width: 100%"
          controls-position="right"
          :min="1"
          value-on-clear="min"
        />
      </el-form-item>
    </el-col>
    <!-- <el-col :span="12">
      <el-button type="primary" plain style="margin-left: 60px">
        解析Excel
      </el-button>
    </el-col> -->
  </el-row>
</template>
  
  <script setup>
defineProps({ form: Object, dpp_etl_node_priority: Array });
const requiredRule = (label) => [
  { required: true, message: `请输入${label}`, trigger: "change" },
];
</script>
  