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
        <img v-if="logo" :src="displaySimpLogo" class="sidebar-logo" />
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
             :class="{ 'logo-intro': logoIntroActive }" @mouseenter="replayHover"/>
      </router-link>
    </transition>
  </div>
</template>

<script setup>
import variables from "@/assets/styles/system/variables.module.scss";
import logo from "@/assets/images/system/logo/logo-qdata-white.png";
import logo1 from "@/assets/images/system/logo/logo-qdata-grey.png";
import simpLogo from "@/assets/images/system/logo/logo-qdata-sim.png"; //Thousands

import useSettingsStore from "@/store/system/settings";
import defaultSettings from "@/settings";
import { getContent } from "@/api/system/system/content";

import {computed, ref as vueRef, nextTick} from "vue";

// Use ref to create a responsive logo
const refLogo = ref(null); // Initialize logo to simpLogo.png
const refSimpLogo = ref(null); // Initialize logo to simpLogo.png

// animation control
const logoIntroActive = ref(false);

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
  logoIntroActive.value = true;
  window.setTimeout(() => {
    logoIntroActive.value = false;
  }, 1800);
  fetchContent();
});
// Use getContent to get data instead of redefining a getContent function
const fetchContent = async () => {
  try {
    const res = await getContent(1);
    if (res.code == 200) {
      const data = res.data;
      const sysLogo = data.logo;
      refLogo.value = sysLogo ? sysLogo : logo;
      refSimpLogo.value = sysLogo ? sysLogo : simpLogo;
    }

    // this.$message.success('Content loaded successfully');
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

    & .logo-intro,
    &:hover .sidebar-logo {
      animation: logoEntrance 1.2s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
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

/* Entrance animation: Phase 1: Fade in and slide in + zoom → Phase 2: Zoom in place → Phase 3: Ball rebound */
@keyframes logoEntrance {
  /* Stage 1: Fade in from left while zooming from small to large */
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
