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
   <el-form ref="pwdRef" :model="user" :rules="rules" label-width="80px">
      <el-form-item :label="td('sys.system.resetPwd.oldPassword')" prop="oldPassword">
         <el-input v-model="user.oldPassword" :placeholder="td('sys.system.resetPwd.oldPasswordPlaceholder')" type="password" show-password />
      </el-form-item>
      <el-form-item :label="td('sys.system.resetPwd.newPassword')" prop="newPassword">
         <el-input v-model="user.newPassword" :placeholder="td('sys.system.resetPwd.newPasswordPlaceholder')" type="password" show-password />
         <div v-if="passwordStrengthMessage" class="password-strength-message">{{ passwordStrengthMessage }}</div>
      </el-form-item>
      <el-form-item :label="td('sys.system.resetPwd.confirmPassword')" prop="confirmPassword">
         <el-input v-model="user.confirmPassword" :placeholder="td('sys.system.resetPwd.confirmPasswordPlaceholder')" type="password" show-password />
      </el-form-item>
      <el-form-item>
         <el-button type="primary" @click="submit">{{ td('common.button.save') }}</el-button>
         <el-button type="danger" @click="close">{{ td('common.button.close') }}</el-button>
      </el-form-item>
   </el-form>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { updateUserPwd } from "@/api/system/system/user.js";

   const { td } = useDefaultLang();
   const { proxy } = getCurrentInstance();

   const user = reactive({
      oldPassword: undefined,
      newPassword: undefined,
      confirmPassword: undefined
   });

   // 密码强度检测的正则表达式
   const passwordStrengthRegex = {
      minLength: /^.{8,}$/, // 最小 8 位
      upperCase: /[A-Z]/,    // 至少一个大写字母
      lowerCase: /[a-z]/,    // 至少一个小写字母
      number: /\d/,          // 至少一个数字
      specialChar: /[!@#$%^&*(),.?":{}|<>]/, // 至少一个特殊字符
   };

   // 密码强度检测逻辑
   const checkPasswordStrength = (password) => {
      if (!password) return null;

      let message = "";
      let strengthValid = true;

      if (!passwordStrengthRegex.minLength.test(password)) {
         message = td('sys.system.resetPwd.passwordMinLength');
         strengthValid = false;
      } else if (!passwordStrengthRegex.upperCase.test(password)) {
         message = td('sys.system.resetPwd.passwordUppercase');
         strengthValid = false;
      } else if (!passwordStrengthRegex.lowerCase.test(password)) {
         message = td('sys.system.resetPwd.passwordLowercase');
         strengthValid = false;
      } else if (!passwordStrengthRegex.number.test(password)) {
         message = td('sys.system.resetPwd.passwordDigit');
         strengthValid = false;
      } else if (!passwordStrengthRegex.specialChar.test(password)) {
         message = td('sys.system.resetPwd.passwordSpecial');
         strengthValid = false;
      }

      return strengthValid ? null : message;
   };

   // 用于显示密码强度提示
   let passwordStrengthMessage = "";

   const equalToPassword = (rule, value, callback) => {
      if (user.newPassword !== value) {
         callback(new Error(td('sys.system.resetPwd.passwordMismatch')));
      } else {
         callback();
      }
   };

   const rules = ref({
      oldPassword: [{ required: true, message: td('sys.system.resetPwd.oldPasswordRequired'), trigger: "blur" }],
      newPassword: [
         { required: true, message: td('sys.system.resetPwd.newPasswordRequired'), trigger: "blur" },
         { min: 6, max: 20, message: td('sys.system.resetPwd.passwordLengthRange'), trigger: "blur" },
         { pattern: /^[^<>"'|\\]+$/, message: td('sys.system.resetPwd.invalidChars'), trigger: "blur" },
         {
            validator: (rule, value, callback) => {
               const strengthMessage = checkPasswordStrength(value);
               if (strengthMessage) {
                  passwordStrengthMessage = strengthMessage;
                  callback(new Error(strengthMessage));  // 报告错误
               } else {
                  passwordStrengthMessage = "";  // 清除密码强度提示
                  callback();  // 密码强度符合要求
               }
            },
            trigger: "blur"
         }
      ],
      confirmPassword: [
         { required: true, message: td('sys.system.resetPwd.confirmPasswordRequired'), trigger: "blur" },
         { required: true, validator: equalToPassword, trigger: "blur" }
      ]
   });

   /** 提交按钮 */
   function submit() {
      proxy.$refs.pwdRef.validate(valid => {
         if (valid) {
            updateUserPwd(user.oldPassword, user.newPassword).then(response => {
               proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            });
         }
      });
   };

   /** 关闭按钮 */
   function close() {
      proxy.$tab.closePage();
   };
</script>

<style scoped>
   .password-strength-message {
      color: red;
      font-size: 12px;
      margin-top: 5px;
   }
</style>
