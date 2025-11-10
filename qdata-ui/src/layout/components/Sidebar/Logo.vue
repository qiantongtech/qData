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
  <div class="sidebar-logo-container" :class="{ 'collapse': collapse }"
    :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <!--        <img v-if="logo" :src="simpLogo" class="sidebar-logo" />-->
        <img v-if="logo" :src="refSimpLogo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title"
          :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{
            title }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <!--        <img v-if="logo" :src="logo" class="sidebar-logo" /> -->
        <img v-if="logo" :src="refLogo" class="sidebar-logo" />
      </router-link>
    </transition>
  </div>
</template>

<script setup>
import variables from '@/assets/system/styles/variables.module.scss'
import logo from '@/assets/system/logo/qData-logo.png'
import simpLogo from '@/assets/system/logo/qData-simlogo.png'  //千数

import useSettingsStore from '@/store/system/settings'
import { getContent } from "@/api/system/system/content";

// 使用 ref 来创建响应式的 logo
const refLogo = ref(null); // 初始化 logo 为 simpLogo.png
const refSimpLogo = ref(null); // 初始化 logo 为 simpLogo.png

defineProps({
  collapse: {
    type: Boolean,
    required: true
  }
})
onMounted(() => {
  fetchContent();
});
// 使用 getContent 来获取数据，而不是重新定义一个 getContent 函数
const fetchContent = async () => {
  try {
    // 调用你从 API 导入的 getContent 方法
    const res = await getContent(1);  // 假设请求的是 id 为 1 的数据
    if (res.code == 200) {
      const data = res.data
      const sysLogo = data.logo
      refLogo.value = sysLogo ? sysLogo : logo;
      refSimpLogo.value = sysLogo ? sysLogo : simpLogo;
    }

    // this.$message.success('内容加载成功');
  } catch (error) {
    refLogo.value = logo;
    refSimpLogo.value = simpLogo;
  }
};

const title = import.meta.env.VITE_APP_TITLE;
const settingsStore = useSettingsStore();
const sideTheme = computed(() => settingsStore.sideTheme);
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 60px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      height: 48px;
      margin-top: 8px;
      vertical-align: middle;
      // margin-right: 12px;
      transform: scale(0.7);
      margin-left: -30px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      height: 60px;
      margin-top: 0px;
      margin-right: 0px;
      margin-left: 0px;
    }
  }
}
</style>
