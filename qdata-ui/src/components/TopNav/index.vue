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
  <el-menu
    :default-active="activeMenu"
    mode="horizontal"
    @select="handleSelect"
    :ellipsis="false"
  >
    <template v-for="(item, index) in topMenus">
      <el-menu-item
        :style="{ '--theme': theme }"
        :index="item.path"
        :key="index"
        v-if="index < visibleNumber"
      >
        <svg-icon
          v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
          :icon-class="item.meta.icon"
        />
        {{ td(`router.${item.meta.lang}`, item.meta.title) }}
      </el-menu-item>
    </template>

    <!-- Top menu collapsed beyond quantity -->
    <el-sub-menu
      :style="{ '--theme': theme }"
      index="more"
      v-if="topMenus.length > visibleNumber"
    >
      <template #title>{{td("router.dynamic.more", "More Menus")}}</template>
      <template v-for="(item, index) in topMenus">
        <el-menu-item
          :index="item.path"
          :key="index"
          v-if="index >= visibleNumber"
        >
          <svg-icon
            v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
            :icon-class="item.meta.icon"
          />
          {{ td(`router.${item.meta.lang}`, item.meta.title) }}
        </el-menu-item>
      </template>
    </el-sub-menu>
  </el-menu>
</template>

<script setup>
import { constantRoutes } from "@/router";
import { isHttp } from "@/utils/validate";
import useAppStore from "@/store/system/app";
import useSettingsStore from "@/store/system/settings";
import usePermissionStore from "@/store/system/permission";
import { el } from "element-plus/es/locale/index.mjs";
import useTagsViewStore from "@/store/system/tagsView";
import defaultSettings from "@/settings";
import useDefaultLang from "@/composables/useDefaultLang";
import { useLocale } from '@/composables/useLocale';
const { changeLocale } = useLocale();
const { td, locale } = useDefaultLang();
window.demo = function(name){
    changeLocale(name)
}
const NAVBAR_LOGO_WIDTH = 200;
const { proxy } = getCurrentInstance();
// Top bar initial number
const visibleNumber = ref(null);
// The index of the currently active menu
const currentIndex = ref("/system");
// Hide sidebar routing
const hideList = ["/index", "/user/profile"];

const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();
const route = useRoute();
const router = useRouter();
const emit = defineEmits(["getRouter"]);
// theme color
const theme = computed(() => settingsStore.theme);
// All routing information
const routers = computed(() => permissionStore.topbarRouters);
let resizeObserver = null;
let mutationObserver = null;

// Show menu at top
const topMenus = computed(() => {
  let topMenus = [];
  routers.value.map((menu) => {
    if (menu.hidden !== true) {
      // Compatible with top bar first-level menu internal jump
      if (menu.path === "/") {
        topMenus.push(menu.children[0]);
      } else {
        topMenus.push(menu);
      }
    }
  });
  return topMenus;
});

// Set up subroutes
const childrenMenus = computed(() => {
  let childrenMenus = [];
  routers.value.map((router) => {
    for (let item in router.children) {
      if (router.children[item].parentPath === undefined) {
        if (router.path === "/") {
          router.children[item].path = "/" + router.children[item].path;
        } else {
          if (!isHttp(router.children[item].path)) {
            router.children[item].path =
              router.path + "/" + router.children[item].path;
          }
        }
        router.children[item].parentPath = router.path;
      }
      childrenMenus.push(router.children[item]);
    }
  });
  return constantRoutes.concat(childrenMenus);
});

// Menu activated by default
const activeMenu = computed(() => {
  const path = route.path;
  let activePath = path;
  console.log(route, "Menu");
  emit("getRouter", path);

  // If it is the root path, selects the first visible menu item
  if (path === "/index") {
    const firstMenu = topMenus.value[0];
    if (firstMenu) {
      activePath = firstMenu.path;
    }
  } else if (
    path !== undefined &&
    path.lastIndexOf("/") > 0 &&
    hideList.indexOf(path) === -1
  ) {
    const tmpPath = path.substring(1, path.length);
    activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"));
  } else if (!route.children) {
    activePath = path;
  }

  const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
  const isLogoRoute = navbarLogoRoutes.some((logoPath) =>
    path.startsWith(logoPath)
  );
  if (isLogoRoute) {
    appStore.toggleSideBarHide(true);
  } else if (path === "/index") {
    // handled by activeRoutes
  } else if (
    path !== undefined &&
    path.lastIndexOf("/") > 0 &&
    hideList.indexOf(path) === -1
  ) {
    if (!route.meta.link) appStore.toggleSideBarHide(false);
  } else if (!route.children) {
    appStore.toggleSideBarHide(true);
  }

  activeRoutes(activePath);
  return activePath;
});

// function setVisibleNumber() {
//     const width = document.body.getBoundingClientRect().width / 3;
//     visibleNumber.value = parseInt(width / 85);
// }

// Calculate the number of menus that can be displayed in the top navigation bar based on the available width
function calculateVisibleMenus() {
  const el = proxy?.$el;
  if (!el) return;

  const navbar = el.closest(".navbar");
  if (!navbar) return;

  const rightMenu = navbar.querySelector(".right-menu");
  const hamburger = navbar.querySelector(".hamburger-container");

  const navbarRect = navbar.getBoundingClientRect();
  const hamburgerWidth = hamburger
    ? hamburger.getBoundingClientRect().width
    : 50;

  // Dynamically calculate the left width: Navbar distance from the left side of the window (usually the sidebar width) + Hamburger width
  let leftWidth = navbarRect.left + hamburgerWidth;

  // When on a route where the logo needs to be displayed, increase the logo width
  const currentPath = route.path;
  const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
  const isLogoRoute = navbarLogoRoutes.some((logoPath) =>
    currentPath.startsWith(logoPath)
  );
  if (isLogoRoute && appStore.sidebar.hide) {
    leftWidth += NAVBAR_LOGO_WIDTH;
  }

  // Dynamically calculate the width of the right side: directly get the width of RightMenu, if not, use the default value of 606
  const rightWidth = rightMenu
    ? rightMenu.getBoundingClientRect().width + 110
    : 606;

  const bodyWidth = document.body.getBoundingClientRect().width;
  let menuWidth =  124; // Each menu item width
  if(locale.value === "en-US"){
    menuWidth = 170;
  }else if(locale.value === "ja-JP"){
    menuWidth = 166;
  }

  const availableWidth = bodyWidth - leftWidth - rightWidth;

  if (availableWidth < 0) {
    visibleNumber.value = 0;
    return;
  }

  const rawCount = Math.floor(availableWidth / menuWidth);
  const totalCount = topMenus.value.length;

  if (totalCount <= rawCount) {
    visibleNumber.value = totalCount;
  } else {
    visibleNumber.value = Math.max(0, rawCount - 1); // Minus 1 for "More Menus"
  }
}

watch(
  [() => route.path, () => topMenus.value.length, () => locale.value],
  () => {
    nextTick(() => {
      calculateVisibleMenus();
    });
  },
  { immediate: true }
);

function closePageExclusion(key) {
  const visitedViews = useTagsViewStore().visitedViews;

  for (let i = visitedViews.length - 1; i >= 0; i--) {
    const view = visitedViews[i];
    if (view.path.includes("/index")) {
      continue;
    }
    if (!view.path.includes(key)) {
      proxy.$tab.closePage(view);
    }
  }
}

// Handle the selection event of the top navigation menu
async function handleSelect(key, keyPath, type) {
  console.log(key, "key");
  // console.log(currentIndex.value,"value");

  //Child component calls parent component
  emit("getRouter", key);

  // Set the currently selected menu index
  currentIndex.value = key;
  // Find selected routing configuration
  const route = routers.value.find((item) => item.path === key);

  if (isHttp(key)) {
    // If it is an http(s) link, it will open in a new window
    window.open(key, "_blank");
  } else if (!route || !route.children) {
    // If there is no sub-route, open it in the current window
    const routeMenu = childrenMenus.value.find((item) => item.path === key);
    if (routeMenu && routeMenu.query) {
      // If there is a query parameter, bring it after parsing
      let query = JSON.parse(routeMenu.query);
      router.push({ path: key, query: query });
    } else {
      // Jump directly without query parameters
      router.push({ path: key });
    }
    // Hide left menu
    appStore.toggleSideBarHide(true);
  } else {
    // If there are sub-routes, the linkage menu on the left will be displayed.
    let routes = activeRoutes(key);
    if (type) {
      closePageExclusion(key);
      if (routes.length > 0) {
        // Get all tabs

        if (
          routes[0].children != null &&
          routes[0].children != undefined &&
          routes[0].children.length > 0
        ) {
          const lastChild = JSON.parse(JSON.stringify(routes[0].children[0]));
          const fullPath = `${routes[0].path}/${routes[0].children[0].path}`;
          lastChild.path = fullPath;
          proxy.$tab.refreshPage(lastChild);
        } else if (routes[0].query != null) {
          const lastChild = JSON.parse(JSON.stringify(routes[0]));
          const query = JSON.parse(routes[0].query);
          lastChild.query = query;
          proxy.$tab.refreshPage(lastChild);
        } else {
          proxy.$tab.refreshPage(routes[0]);
        }
      }
    }
    // Show left menu
    appStore.toggleSideBarHide(false);
  }
}

function activeRoutes(key) {
  let routes = [];
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    childrenMenus.value.map((item) => {
      if (key == item.parentPath || (key == "index" && "" == item.path)) {
        routes.push(item);
      }
    });
  }
  if (routes.length > 0) {
    console.log('1--->');
    permissionStore.setSidebarRouters(routes);
  } else {
    appStore.toggleSideBarHide(true);
  }
  return routes;
}

onMounted(() => {
  window.addEventListener("resize", calculateVisibleMenus);
  nextTick(() => {
    calculateVisibleMenus();

    const el = proxy?.$el;
    if (el) {
      const navbar = el.closest(".navbar");
      if (navbar) {
        const rightMenu = navbar.querySelector(".right-menu");

        resizeObserver = new ResizeObserver(() => {
          calculateVisibleMenus();
        });

        // Listen to navbar itself
        resizeObserver.observe(navbar);

        if (rightMenu) {
          // Listen to the entire right menu container
          resizeObserver.observe(rightMenu);

          // Listen to all child elements of the right menu in case width changes occur within them (such as the search box expanding)
          const observeChildren = (parent) => {
            Array.from(parent.children).forEach((child) => {
              resizeObserver.observe(child);
            });
          };
          observeChildren(rightMenu);

          // Use MutationObserver to monitor the dynamic increase and decrease of sub-nodes in the right menu (such as v-if switching)
          mutationObserver = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
              if (mutation.type === "childList") {
                mutation.addedNodes.forEach((node) => {
                  if (node.nodeType === 1) {
                    // Element node
                    resizeObserver.observe(node);
                  }
                });
                calculateVisibleMenus();
              }
            });
          });

          mutationObserver.observe(rightMenu, { childList: true });
        }
      }
    }
  });
});
onBeforeUnmount(() => {
  window.removeEventListener("resize", calculateVisibleMenus);
  if (resizeObserver) {
    resizeObserver.disconnect();
    resizeObserver = null;
  }
  if (mutationObserver) {
    mutationObserver.disconnect();
    mutationObserver = null;
  }
});
// If you need to expose it to the parent component, you can use defineExpose
defineExpose({
  handleSelect,
});
</script>

<style lang="scss">
.el-menu--horizontal.el-menu {
  padding-top: 10px;
  border-bottom: 0;
}

.topmenu-container.el-menu--horizontal > .el-menu-item {
  font-size: 16px;
  float: left;
  height: 40px !important;
  line-height: 40px !important;
  color: #333 !important;
  padding: 0 15px !important;
  margin: 0 10px !important;
  border-radius: 5px;
  font-weight: bold !important;
}

/* sub-menu item */
.topmenu-container.el-menu--horizontal > .el-sub-menu .el-sub-menu__title {
  font-size: 16px;
  float: left;
  height: 40px !important;
  line-height: 40px !important;
  color: #333 !important;
  padding: 0 15px !important;
  margin: 0 10px !important;
  border-radius: 5px;
  font-weight: bold !important;
}

.topmenu-container.el-menu--horizontal > .el-menu-item.is-active,
.el-menu--horizontal > .el-sub-menu.is-active .el-submenu__title,
.el-menu--horizontal > .el-sub-menu.is-active .el-sub-menu__title {
  background: #{"var(--theme)"} !important;
  color: #fff !important;
}

/* Background color hidden */
.topmenu-container.el-menu--horizontal > .el-menu-item:not(.is-disabled):focus,
.topmenu-container.el-menu--horizontal > .el-menu-item:not(.is-disabled):hover,
.topmenu-container.el-menu--horizontal > .el-submenu .el-submenu__title:hover {
  background: #{"var(--theme)"} !important;
  color: #fff !important;
}

/* Icon right spacing */
.topmenu-container .svg-icon {
  margin-right: 4px;
}

/* topmenu more arrow */
.topmenu-container .el-sub-menu .el-sub-menu__icon-arrow {
  position: static;
  vertical-align: middle;
  margin-left: 8px;
  margin-top: 0px;
}

.el-menu--horizontal .el-menu .el-menu-item {
  height: 40px !important;
  line-height: 40px !important;
  font-weight: bold !important;

  .svg-icon {
    margin-right: 10px;
  }
}
</style>
