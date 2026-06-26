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
  <el-form ref="userRef" :model="form" :rules="rules" :label-position="labelPosition">
    <el-form-item :label="td('sys.system.userInfo.userNickName')" prop="nickName">
      <el-input v-model="form.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item :label="td('sys.system.userInfo.phone')" prop="phonenumber">
      <el-input v-model="form.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item :label="td('sys.system.userInfo.email')" prop="email">
      <el-input v-model="form.email" maxlength="50" />
    </el-form-item>
    <el-form-item :label="td('sys.system.userInfo.gender')">
      <el-radio-group v-model="form.sex">
        <el-radio value="0">{{ td('sys.system.userInfo.male') }}</el-radio>
        <el-radio value="1">{{ td('sys.system.userInfo.female') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">{{ td('common.button.save') }}</el-button>
      <el-button type="danger" @click="close">{{ td('common.button.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { updateUserProfile } from "@/api/system/system/user.js";

const { td } = useDefaultLang();
const props = defineProps({
  user: {
    type: Object
  }
});

const { proxy } = getCurrentInstance();

const form = ref({});
const rules = ref({
  nickName: [{ required: true, message: td('sys.system.userInfo.userNameRequired'), trigger: "blur" }],
  email: [{ required: true, message: td('sys.system.userInfo.emailRequired'), trigger: "blur" }, { type: "email", message: td('sys.system.userInfo.emailFormat'), trigger: ["blur", "change"] }],
  phonenumber: [{ required: true, message: td('sys.system.userInfo.phoneRequired'), trigger: "blur" }, { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: td('sys.system.userInfo.phoneFormat'), trigger: "blur" }],
});

/** 提交按钮 */
function submit() {
  proxy.$refs.userRef.validate(valid => {
    if (valid) {
      updateUserProfile(form.value).then(response => {
        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
        props.user.phonenumber = form.value.phonenumber;
        props.user.email = form.value.email;
      });
    }
  });
};

/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
};

// 回显当前登录用户信息
watch(() => props.user, user => {
  if (user) {
    form.value = { nickName: user.nickName, phonenumber: user.phonenumber, email: user.email, sex: user.sex };
  }
}, { immediate: true });
</script>
