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
        prop="taskParams.file"
        :rules="[{ required: true, message: '请上传附件', trigger: 'change' }]"
      >
        <FileUploadbtn
          :limit="1"
          v-model="form.taskParams.file"
          :isShowTip="false"
          :dragFlag="false"
          :file-type="['csv']"
          :fileSize="50"
          :disabled="!info"
        />
      </el-form-item>
    </el-col>
  </el-row>
</template>
  
  <script setup>
const props = defineProps({ form: Object, dpp_etl_node_priority: Array });
// 获取列数据
</script>
  