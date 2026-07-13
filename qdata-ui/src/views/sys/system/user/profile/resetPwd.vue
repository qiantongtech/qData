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
   <el-form ref="pwdRef" :model="user" :rules="rules" :label-position="labelPosition">
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

   // Regular expression for password strength detection
   const passwordStrengthRegex = {
      minLength: /^.{8,}$/, // Minimum 8 bits
      upperCase: /[A-Z]/,    // at least one capital letter
      lowerCase: /[a-z]/,    // at least one lowercase letter
      number: /\d/,          // at least one number
      specialChar: /[!@#$%^&*(),.?":{}|<>]/, // at least one special character
   };

   // Password strength detection logic
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

   // Used to display password strength prompts
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
                  callback(new Error(strengthMessage));  // Report an error
               } else {
                  passwordStrengthMessage = "";  // Clear password strength prompts
                  callback();  // Password strength meets requirements
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

   /** submit button */
   function submit() {
      proxy.$refs.pwdRef.validate(valid => {
         if (valid) {
            updateUserPwd(user.oldPassword, user.newPassword).then(response => {
               proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            });
         }
      });
   };

   /** close button */
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
