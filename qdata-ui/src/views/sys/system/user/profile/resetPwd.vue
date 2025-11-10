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
  For brand customization, please contact the official team for authorization.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
   <el-form ref="pwdRef" :model="user" :rules="rules" label-width="80px">
      <el-form-item label="旧密码" prop="oldPassword">
         <el-input v-model="user.oldPassword" placeholder="请输入旧密码" type="password" show-password />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
         <el-input v-model="user.newPassword" placeholder="请输入新密码" type="password" show-password />
         <div v-if="passwordStrengthMessage" class="password-strength-message">{{ passwordStrengthMessage }}</div>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
         <el-input v-model="user.confirmPassword" placeholder="请确认新密码" type="password" show-password />
      </el-form-item>
      <el-form-item>
         <el-button type="primary" @click="submit">保存</el-button>
         <el-button type="danger" @click="close">关闭</el-button>
      </el-form-item>
   </el-form>
</template>

<script setup>
   import { updateUserPwd } from "@/api/system/system/user.js";

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
         message = "密码长度必须至少 8 位";
         strengthValid = false;
      } else if (!passwordStrengthRegex.upperCase.test(password)) {
         message = "密码必须包含至少一个大写字母";
         strengthValid = false;
      } else if (!passwordStrengthRegex.lowerCase.test(password)) {
         message = "密码必须包含至少一个小写字母";
         strengthValid = false;
      } else if (!passwordStrengthRegex.number.test(password)) {
         message = "密码必须包含至少一个数字";
         strengthValid = false;
      } else if (!passwordStrengthRegex.specialChar.test(password)) {
         message = "密码必须包含至少一个特殊字符";
         strengthValid = false;
      }

      return strengthValid ? null : message;
   };

   // 用于显示密码强度提示
   let passwordStrengthMessage = "";

   const equalToPassword = (rule, value, callback) => {
      if (user.newPassword !== value) {
         callback(new Error("两次输入的密码不一致"));
      } else {
         callback();
      }
   };

   const rules = ref({
      oldPassword: [{ required: true, message: "旧密码不能为空", trigger: "blur" }],
      newPassword: [
         { required: true, message: "新密码不能为空", trigger: "blur" },
         { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
         { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" },
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
         { required: true, message: "确认密码不能为空", trigger: "blur" },
         { required: true, validator: equalToPassword, trigger: "blur" }
      ]
   });

   /** 提交按钮 */
   function submit() {
      proxy.$refs.pwdRef.validate(valid => {
         if (valid) {
            updateUserPwd(user.oldPassword, user.newPassword).then(response => {
               proxy.$modal.msgSuccess("修改成功");
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
