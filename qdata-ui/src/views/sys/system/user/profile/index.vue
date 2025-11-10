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
   <div class="app-container">
      <el-row :gutter="15">
         <el-col :span="6" :xs="24">
            <el-card class="box-card">
               <template v-slot:header>
                  <div class="head-container">
                     <span class="head-title"></span>
                     <span>个人信息</span>
                  </div>
               </template>
               <div>
                  <div class="text-center">
                     <userAvatar />
                  </div>
                  <ul class="list-group list-group-striped">
                     <li class="list-group-item vertical-center">
                        <i class="iconfont icon-a-yonghuzhanghaoxianxing mr5"></i>
                        用户名称
                        <div class="pull-right label-text">{{ state.user.userName }}</div>
                     </li>
                     <li class="list-group-item vertical-center">
                        <i class="iconfont icon-a-shoujixianxing mr5"></i>
                        手机号码
                        <div class="pull-right label-text">{{ state.user.phonenumber }}</div>
                     </li>
                     <li class="list-group-item vertical-center">
                        <i class="iconfont icon-a-yonghuyouxiangxianxing mr5"></i>
                        用户邮箱
                        <div class="pull-right label-text">{{ state.user.email }}</div>
                     </li>
                     <li class="list-group-item vertical-center">
                        <i class="iconfont icon-bumen margin-right-5 mr5"></i>
                        所属部门
                        <div class="pull-right label-text" v-if="state.user.dept">{{ state.user.dept.deptName }} /
                           {{ state.postGroup }}
                        </div>
                     </li>
<!--                     <li class="list-group-item vertical-center">-->
<!--                        <i class="iconfont icon-a-suoshujiaosexianxing mr5"></i>-->
<!--                        所属角色-->
<!--                        <div class="pull-right label-text">{{ state.roleGroup }}</div>-->
<!--                     </li>-->
                     <li class="list-group-item vertical-center">
                        <i class="iconfont icon-a-riqixianxing mr5"></i>
                        创建日期
                        <div class="pull-right label-text">{{ state.user.createTime }}</div>
                     </li>
                  </ul>
               </div>
            </el-card>
         </el-col>
         <el-col :span="18" :xs="24">
            <el-card>
               <template v-slot:header>
                  <div class="head-container">
                     <span class="head-title"></span>
                     <span>基本资料</span>
                  </div>
               </template>
               <el-tabs v-model="activeTab">
                  <el-tab-pane label="基本资料" name="userinfo">
                     <userInfo :user="state.user" />
                  </el-tab-pane>
                  <el-tab-pane label="修改密码" name="resetPwd">
                     <resetPwd />
                  </el-tab-pane>
               </el-tabs>
            </el-card>
         </el-col>
      </el-row>
   </div>
</template>

<script setup name="Profile">
import userAvatar from "./userAvatar.vue";
import userInfo from "./userInfo.vue";
import resetPwd from "./resetPwd.vue";
import { getUserProfile } from "@/api/system/system/user.js";

const activeTab = ref("userinfo");
const state = reactive({
   user: {},
   roleGroup: {},
   postGroup: {}
});

function getUser() {
   getUserProfile().then(response => {
      state.user = response.data;
      state.roleGroup = response.roleGroup;
      state.postGroup = response.postGroup;
   });
};

getUser();
</script>
<style scoped lang="scss">
.label-text {
   color: #888888;
   position: absolute;
   right: 10px;
}

:deep {
   .el-tabs__item.is-active {
      color: var(--el-color-primary);
   }

   .el-tabs__item:hover {
      background-color: transparent !important;
      /* 去掉背景色变化 */
      color: var(--el-color-primary);
      /* 字体颜色不变 */
   }

   .el-tabs__active-bar {
      background-color: var(--el-color-primary);
   }

   .el-card {
     height: 100%;
     border-radius: 2px !important;
      .el-card__header {
         padding: 14px !important;
      }
   }
}

.box-card {
   min-width: 260px;
}

.vertical-center {
   display: flex;
   align-items: center;
   position: relative;
}

.head-container {
   display: flex;
   flex-direction: row;
   align-items: center;
}

.head-title {
   display: inline-block;
   content: "";
   width: 6px;
   height: 16px;
   border-radius: 2px;
   background: var(--el-color-primary);
   margin-right: 10px;
}
</style>
