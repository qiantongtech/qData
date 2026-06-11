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
  <el-form ref="userRef" :model="form" :rules="rules" label-width="80px">
    <el-form-item :label="t('sys.system.userInfo.userNickName')" prop="nickName">
      <el-input v-model="form.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item :label="t('sys.system.userInfo.phone')" prop="phonenumber">
      <el-input v-model="form.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item :label="t('sys.system.userInfo.email')" prop="email">
      <el-input v-model="form.email" maxlength="50" />
    </el-form-item>
    <el-form-item :label="t('sys.system.userInfo.gender')">
      <el-radio-group v-model="form.sex">
        <el-radio value="0">{{ t('sys.system.userInfo.male') }}</el-radio>
        <el-radio value="1">{{ t('sys.system.userInfo.female') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">{{ t('common.button.save') }}</el-button>
      <el-button type="danger" @click="close">{{ t('common.button.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { updateUserProfile } from "@/api/system/system/user.js";

const { t } = useI18n();
const props = defineProps({
  user: {
    type: Object
  }
});

const { proxy } = getCurrentInstance();

const form = ref({});
const rules = ref({
  nickName: [{ required: true, message: t('sys.system.userInfo.userNameRequired'), trigger: "blur" }],
  email: [{ required: true, message: t('sys.system.userInfo.emailRequired'), trigger: "blur" }, { type: "email", message: t('sys.system.userInfo.emailFormat'), trigger: ["blur", "change"] }],
  phonenumber: [{ required: true, message: t('sys.system.userInfo.phoneRequired'), trigger: "blur" }, { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: t('sys.system.userInfo.phoneFormat'), trigger: "blur" }],
});

/** 提交按钮 */
function submit() {
  proxy.$refs.userRef.validate(valid => {
    if (valid) {
      updateUserProfile(form.value).then(response => {
        proxy.$modal.msgSuccess(t('common.message.editSuccess'));
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
