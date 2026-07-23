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
