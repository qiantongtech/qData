<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
  <el-form ref="basicInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item :label="td('sys.tool.genBasic.tableName')" prop="tableName">
          <el-input :placeholder="td('sys.tool.genBasic.tableNamePlaceholder')" v-model="info.tableName" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="td('sys.tool.genBasic.tableDesc')" prop="tableComment">
          <el-input :placeholder="td('sys.tool.genBasic.tableDescRequired')" v-model="info.tableComment" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="td('sys.tool.genBasic.entityName')" prop="className">
          <el-input :placeholder="td('sys.tool.genBasic.entityNameRequired')" v-model="info.className" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="td('sys.tool.genBasic.author')" prop="functionAuthor">
          <el-input :placeholder="td('sys.tool.genBasic.authorRequired')" v-model="info.functionAuthor" />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item :label="td('common.texts.remark')" prop="remark">
          <el-input type="textarea" :rows="3" v-model="info.remark"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();defineProps({
  info: {
    type: Object,
    default: null
  }
});

// 表单校验
const rules = ref({
  tableName: [{ required: true, message: td('sys.tool.genBasic.tableNameRequired'), trigger: "blur" }],
  tableComment: [{ required: true, message: td('sys.tool.genBasic.tableDescRequired'), trigger: "blur" }],
  className: [{ required: true, message: td('sys.tool.genBasic.entityNameRequired'), trigger: "blur" }],
  functionAuthor: [{ required: true, message: td('sys.tool.genBasic.authorRequired'), trigger: "blur" }]
});
</script>
