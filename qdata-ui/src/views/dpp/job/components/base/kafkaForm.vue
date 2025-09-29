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
        label="数据源连接"
        prop="taskParams.readerDatasource.datasourceId"
        :rules="[
          {
            required: true,
            message: '请选择源数据库连接',
            trigger: 'change',
          },
        ]"
      >
        <el-select
          v-model="form.taskParams.readerDatasource.datasourceId"
          placeholder="请选择源数据库连接"
          @change="handleDatasourceChange"
          filterable
        >
          <el-option
            v-for="dict in createTypeList"
            :key="dict.id"
            :label="dict.datasourceName"
            :value="dict.id"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item
        label="主题名称"
        prop="taskParams.topic"
        :rules="[
          {
            required: true,
            message: '请输入主题名称',
            trigger: 'change',
          },
        ]"
      >
        <el-input
          v-model="form.taskParams.topic"
          placeholder="请输入主题名称"
        />
      </el-form-item>
    </el-col>
    <el-col :span="24">
      <el-form-item label="配置参数" prop="taskParams.config">
        <el-input
          v-model="form.taskParams.config"
          type="textarea"
          placeholder='例如: {"group.id"&colon;"demo_test"}'
        />
      </el-form-item>
    </el-col>
  </el-row>
</template>
<script setup>
const props = defineProps({
  form: Object, // 父组件传入 form
  createTypeList: Array, // 父组件传入 createTypeList
  dpp_etl_node_priority: Array,
});
const requiredRule = (label) => [
  { required: true, message: `请输入${label}`, trigger: "change" },
];
</script>
