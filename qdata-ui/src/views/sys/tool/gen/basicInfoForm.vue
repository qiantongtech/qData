<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
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

// form validation
const rules = ref({
  tableName: [{ required: true, message: td('sys.tool.genBasic.tableNameRequired'), trigger: "blur" }],
  tableComment: [{ required: true, message: td('sys.tool.genBasic.tableDescRequired'), trigger: "blur" }],
  className: [{ required: true, message: td('sys.tool.genBasic.entityNameRequired'), trigger: "blur" }],
  functionAuthor: [{ required: true, message: td('sys.tool.genBasic.authorRequired'), trigger: "blur" }]
});
</script>
