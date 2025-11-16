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
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
    <div class="app-container" ref="app-container">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane label="项目成员" name="first">
                <User ref="userRef"></User>
            </el-tab-pane>
            <el-tab-pane label="项目角色" name="second">
                <Role ref="roleRef"></Role>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup name="index">
import { ref } from 'vue';
import Role from '@/views/dpp/setting/projectUserRel/role.vue';
import User from '@/views/dpp/setting/projectUserRel/user.vue';
const activeName = ref('first');
</script>

<style scoped lang="less">
::v-deep(.el-tabs__header) {
    margin: 0 0 0px;
    padding: 0px 15px 0px 15px;
    position: relative;
    background-color: #fff;
}
</style>
