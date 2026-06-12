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
  <div class="app-container" ref="app-container">
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane
        :label="td('dpp.setting.projectUserRel.projectMember')"
        name="first"
      >
        <User ref="userRef"></User>
      </el-tab-pane>
      <el-tab-pane
        :label="td('dpp.setting.projectUserRel.projectRole')"
        name="second"
      >
        <Role ref="roleRef"></Role>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup name="index">
import { ref } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";
import Role from "@/views/dpp/setting/projectUserRel/role.vue";
import User from "@/views/dpp/setting/projectUserRel/user.vue";

const { td } = useDefaultLang();
const activeName = ref("first");
</script>

<style scoped lang="less">
::v-deep(.el-tabs__header) {
  margin: 0 0 0px;
  padding: 0px 15px 0px 15px;
  position: relative;
  background-color: #fff;
}
</style>
