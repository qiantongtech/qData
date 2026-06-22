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
  <div
    class="sidebar-logo-container"
    :class="{ collapse: collapse }"
    :style="{
      backgroundColor:
        sideTheme === 'theme-dark'
          ? variables.menuBackground
          : variables.menuLightBackground,
    }"
  >
    <transition name="sidebarLogoFade">
      <router-link
        v-if="collapse"
        key="collapse"
        class="sidebar-logo-link"
        to="/"
      >
        <!--        <img v-if="logo" :src="simpLogo" class="sidebar-logo" />-->
        <img v-if="logo" :src="displaySimpLogo" ref="simpLogoRef" class="sidebar-logo"
             :class="{ 'logo-animate': simpAnimate, 'logo-hover': simpHover }" @mouseenter="replaySimpHover"/>
        <h1
          v-else
          class="sidebar-title"
          :style="{
            color:
              sideTheme === 'theme-dark'
                ? variables.logoTitleColor
                : variables.logoLightTitleColor,
          }"
        >
          {{ title }}
        </h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <!--        <img v-if="logo" :src="logo" class="sidebar-logo" /> -->
        <img v-if="logo" :src="displayLogo" ref="logoRef" class="sidebar-logo"
             :class="{ 'logo-animate': logoAnimate, 'logo-hover': logoHover }" @mouseenter="replayHover"/>
      </router-link>
    </transition>
  </div>
</template>

<script setup>
import variables from "@/assets/styles/system/variables.module.scss";
import logo from "@/assets/images/system/logo/qData-logo.png";
import logo1 from "@/assets/images/system/logo/qData-logo1.png";
import simpLogo from "@/assets/images/system/logo/qData-simlogo.png"; //千数

import useSettingsStore from "@/store/system/settings";
import defaultSettings from "@/settings";
import { getContent } from "@/api/system/system/content";

import {computed, ref as vueRef, nextTick} from "vue";

// 使用 ref 来创建响应式的 logo
const refLogo = ref(null); // 初始化 logo 为 simpLogo.png
const refSimpLogo = ref(null); // 初始化 logo 为 simpLogo.png

// 动画控制
const logoRef = vueRef(null);
const simpLogoRef = vueRef(null);
const logoAnimate = vueRef(true);
const simpAnimate = vueRef(true);
const logoHover = vueRef(false);
const simpHover = vueRef(false);

// 鼠标悬浮触发抖动动画（展开状态）
const replayHover = () => {
  logoHover.value = false;
  nextTick(() => {
    logoRef.value?.offsetWidth;
    logoHover.value = true;
  });
};

// 鼠标悬浮触发抖动动画（收缩状态）
const replaySimpHover = () => {
  simpHover.value = false;
  nextTick(() => {
    simpLogoRef.value?.offsetWidth;
    simpHover.value = true;
  });
};

const props = defineProps({
  collapse: {
    type: Boolean,
    required: true,
  },
  currentRoute: {
    type: String,
    default: "/",
  },
});

const displayLogo = computed(() => {
  const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
  const isSpecialRoute = navbarLogoRoutes.some((logoPath) =>
    props.currentRoute.startsWith(logoPath)
  );
  return isSpecialRoute ? logo1 : refLogo.value;
});

const displaySimpLogo = computed(() => {
  const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
  const isSpecialRoute = navbarLogoRoutes.some((logoPath) =>
    props.currentRoute.startsWith(logoPath)
  );
  return isSpecialRoute ? logo1 : refSimpLogo.value;
});
onMounted(() => {
  fetchContent();
});
// 使用 getContent 来获取数据，而不是重新定义一个 getContent 函数
const fetchContent = async () => {
  try {
    const res = await getContent(1);
    if (res.code == 200) {
      const data = res.data;
      const sysLogo = data.logo;
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

      &.logo-animate {
        will-change: transform;
        animation: logoEntrance 1.2s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
      }

      &.logo-hover {
        animation: logoWobble 1s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
      }
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

/* 入场动画：阶段1 淡入滑入+缩放 → 阶段2 缩放到位 → 阶段3 皮球回弹 */
@keyframes logoEntrance {
  /* 阶段1：从左淡入滑入，同时从小到大缩放 */
  0% {
    transform: translateX(-200px) scale(0.3);
    opacity: 0;
  }
  35% {
    transform: translateX(0) scale(0.4);
    opacity: 1;
  }
  100% {
    transform: translateY(0) scale(0.7);
    opacity: 1;
  }
}

/* 悬浮抖动动画：右往上左往下抖动三下 */
@keyframes logoWobble {
  /* 阶段1：从左淡入滑入，同时从小到大缩放 */
  0% {
    transform: translateX(-200px) scale(0.3);
    opacity: 0;
  }
  35% {
    transform: translateX(0) scale(0.4);
    opacity: 1;
  }
  100% {
    transform: translateY(0) scale(0.7);
    opacity: 1;
  }
}
</style>
