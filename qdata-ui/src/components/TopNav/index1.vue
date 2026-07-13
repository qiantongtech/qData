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
    <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :ellipsis="false">
        <template v-for="(item, index) in topMenus">
            <el-menu-item :style="{ '--theme': theme }" :index="item.path" :key="index" v-if="index < visibleNumber">
                <svg-icon v-if="item.meta && item.meta.icon && item.meta.icon !== '#'" :icon-class="item.meta.icon" />
                {{ item.meta.title }}
            </el-menu-item>
        </template>

        <!-- Top menu collapsed beyond quantity -->
        <el-sub-menu :style="{ '--theme': theme }" index="more" v-if="topMenus.length > visibleNumber">
            <template #title>{{ t('components.topNav.moreMenus') }}</template>
            <template v-for="(item, index) in topMenus">
                <el-menu-item :index="item.path" :key="index" v-if="index >= visibleNumber">
                    <svg-icon v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
                        :icon-class="item.meta.icon" />
                    {{ item.meta.title }}
                </el-menu-item>
            </template>
        </el-sub-menu>
    </el-menu>
</template>

<script setup>
import { useI18n } from 'vue-i18n';
import { constantRoutes } from '@/router';
import { isHttp } from '@/utils/validate';
import useAppStore from '@/store/system/app';
import useSettingsStore from '@/store/system/settings';
import usePermissionStore from '@/store/system/permission';
const { t } = useI18n();

// Top bar initial number
const visibleNumber = ref(null);
// The index of the currently active menu
const currentIndex = ref('/system');
// Hide sidebar routing
const hideList = ['/index', '/user/profile'];

const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();
const route = useRoute();
const router = useRouter();
const emit = defineEmits(['getRouter']);
// theme color
const theme = computed(() => settingsStore.theme);
// All routing information
const routers = computed(() => permissionStore.topbarRouters);

// Show menu at top
const topMenus = computed(() => {
    let topMenus = [];
    routers.value.map((menu) => {
        if (menu.hidden !== true) {
            // Compatible with top bar first-level menu internal jump
            if (menu.path === '/') {
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
                if (router.path === '/') {
                    router.children[item].path = '/' + router.children[item].path;
                } else {
                    if (!isHttp(router.children[item].path)) {
                        router.children[item].path =
                            router.path + '/' + router.children[item].path;
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
    emit('getRouter', path);

    // If it is the root path, selects the first visible menu item
    if (path === '/index') {
        const firstMenu = topMenus.value[0];
        if (firstMenu) {
            activePath = firstMenu.path;
        }
    } else if (
        path !== undefined &&
        path.lastIndexOf('/') > 0 &&
        hideList.indexOf(path) === -1
    ) {
        const tmpPath = path.substring(1, path.length);
        activePath = '/' + tmpPath.substring(0, tmpPath.indexOf('/'));
        if (!route.meta.link) {
            appStore.toggleSideBarHide(false);
        }
    } else if (!route.children) {
        activePath = path;
        appStore.toggleSideBarHide(true);
    }
    activeRoutes(activePath);
    return activePath;
});

function setVisibleNumber() {
    const width = document.body.getBoundingClientRect().width / 3;
    visibleNumber.value = parseInt(width / 85);
}

// Handle the selection event of the top navigation menu
function handleSelect(key, keyPath) {
    window.open(key, '_blank', 'noreferrer');
    // console.log(currentIndex.value,"value");

    //Child component calls parent component
    emit('getRouter', key);

    // Set the currently selected menu index
    currentIndex.value = key;
    // Find selected routing configuration
    const route = routers.value.find((item) => item.path === key);

    if (isHttp(key)) {
        // If it is an http(s) link, it will open in a new window
        window.open(key, '_blank');
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
        activeRoutes(key);
        // Show left menu
        appStore.toggleSideBarHide(false);
    }
}

function activeRoutes(key) {
    let routes = [];
    if (childrenMenus.value && childrenMenus.value.length > 0) {
        childrenMenus.value.map((item) => {
            if (key == item.parentPath || (key == 'index' && '' == item.path)) {
                routes.push(item);
            }
        });
    }
    if (routes.length > 0) {
        permissionStore.setSidebarRouters(routes);
    } else {
        appStore.toggleSideBarHide(true);
    }
    return routes;
}

onMounted(() => {
    window.addEventListener('resize', setVisibleNumber);
});
onBeforeUnmount(() => {
    window.removeEventListener('resize', setVisibleNumber);
});

onMounted(() => {
    setVisibleNumber();
});
</script>

<style lang="scss">
.el-menu--horizontal>.el-menu-item,
.el-menu--horizontal>.el-sub-menu.is-active .el-sub-menu__title {
    border-bottom: none !important;
    border-top: 2px solid transparent;
}

.topmenu-container.el-menu--horizontal>.el-menu-item {
    float: left;
    // height: 50px !important;
    // line-height: 50px !important;
    color: #999093 !important;
    padding: 0 15px !important;
    margin: 0 10px !important;
}

/* sub-menu item */
.topmenu-container.el-menu--horizontal>.el-sub-menu .el-sub-menu__title {
    float: left;
    // height: 50px !important;
    // line-height: 50px !important;
    color: #999093 !important;
    padding: 0 15px !important;
    margin: 0 10px !important;
}


.topmenu-container.el-menu--horizontal>.el-menu-item.is-active,
.el-menu--horizontal>.el-sub-menu.is-active .el-submenu__title,
.el-menu--horizontal>.el-sub-menu.is-active .el-sub-menu__title {
    border-top: 2px solid #{'var(--theme)'} !important;
    color: #303133;
    background-color: var(--el-menu-hover-bg-color);
}


/* Background color hidden */
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):focus,
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):hover,
.topmenu-container.el-menu--horizontal>.el-submenu .el-submenu__title:hover {
    border-top: 2px solid #{'var(--theme)'} !important;
    color: #303133;
    background-color: var(--el-menu-hover-bg-color);
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
</style>
