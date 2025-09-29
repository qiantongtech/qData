<template>
    <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :ellipsis="false"
        class="custom-topmenu">
        <template v-for="(item, index) in topMenus" :key="item.path">
            <el-menu-item :style="{ '--theme': theme }" :index="item.path" v-if="index < visibleNumber">
                <svg-icon v-if="item.meta && item.meta.icon && item.meta.icon !== '#'" :icon-class="item.meta.icon"
                    :style="iconStyle(item)" />
                {{ item.meta.title }}
            </el-menu-item>
        </template>
        <el-sub-menu index="more" v-if="topMenus.length > visibleNumber" popper-class="more-menu-popper"
            :popper-options="{ modifiers: [{ name: 'offset', options: { offset: [0, -10] } }] }" class="more-menu"
            popper-append-to-body="false">
            <template #title>
                <svg-icon style="margin-right: 10px; width: 17px;height: 17px;" icon-class="gdcd" />
                更多菜单
            </template>
            <template v-for="(item, index) in topMenus" :key="item.path">
                <el-menu-item v-if="index >= visibleNumber" :index="item.path">
                    <svg-icon v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
                        :icon-class="item.meta.icon" />
                    {{ item.meta.title }}
                </el-menu-item>
            </template>
        </el-sub-menu>

    </el-menu>
</template>

<script setup>
import { computed, ref, onMounted, onBeforeUnmount, getCurrentInstance } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import useAppStore from '@/store/system/app';
import useSettingsStore from '@/store/system/settings';
import usePermissionStore from '@/store/system/permission';
import useTagsViewStore from '@/store/system/tagsView';
import { isHttp } from '@/utils/validate';

const { proxy } = getCurrentInstance();

const visibleNumber = ref(null);
const currentIndex = ref('/system');
const hideList = ['/index', '/user/profile'];

const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();
const route = useRoute();
const router = useRouter();
const emit = defineEmits(['getRouter']);

const theme = computed(() => settingsStore.theme);

const routers = computed(() => permissionStore.topbarRouters);

const topMenus = computed(() => {
    let arr = [];
    routers.value.forEach(menu => {
        if (menu.hidden !== true) {
            if (menu.path === '/') {
                arr.push(menu.children[0]);
            } else {
                arr.push(menu);
            }
        }
    });
    return arr;
});
const iconStyle = (item) => {
    let size = '26px' // 默认大小
    if (item.meta.title === '数据服务') {
        size = '28px'
    } else if (item.meta.title === '系统管理' || item.meta.title === '资源门户') {
        size = '16px'
    } else if (item.meta.title === '系统工具') {
        size = '22px'
    }
    return {
        width: size,
        height: size,
        fontSize: size
    }
}
const childrenMenus = computed(() => {
    let arr = [];
    routers.value.forEach(router => {
        router.children.forEach(child => {
            if (child.parentPath === undefined) {
                if (router.path === '/') {
                    child.path = '/' + child.path;
                } else {
                    if (!isHttp(child.path)) {
                        child.path = router.path + '/' + child.path;
                    }
                }
                child.parentPath = router.path;
            }
            arr.push(child);
        });
    });
    return arr;
});

const activeMenu = computed(() => {
    const path = route.path;
    let activePath = path;
    emit('getRouter', path);

    if (path === '/index') {
        const firstMenu = topMenus.value[0];
        if (firstMenu) activePath = firstMenu.path;
    } else if (path !== undefined && path.lastIndexOf('/') > 0 && hideList.indexOf(path) === -1) {
        const tmpPath = path.substring(1, path.length);
        activePath = '/' + tmpPath.substring(0, tmpPath.indexOf('/'));
        if (!route.meta.link) appStore.toggleSideBarHide(false);
    } else if (!route.children) {
        activePath = path;
        appStore.toggleSideBarHide(true);
    }
    activeRoutes(activePath);
    return activePath;
});

// function setVisibleNumber() {
//     const isDpp = route.path.startsWith('/dpp');
//     const width = document.body.getBoundingClientRect().width / 3;

//     if (isDpp) {
//         visibleNumber.value = parseInt((width - 220) / 90);
//     } else {
//         visibleNumber.value = parseInt(width / 90);
//     }
// }

// 计算可用宽度下的顶部导航栏可显示菜单数量
function calculateVisibleMenus() {
    const bodyWidth = document.body.getBoundingClientRect().width;
    const leftWidth = 210 + 50; // Logo + 小图标，左边最大宽度
    const rightWidth = 606;     // 右侧功能区，右边最大宽度
    const menuWidth = 124;      // 每个菜单项宽度

    const availableWidth = bodyWidth - leftWidth - rightWidth;

    if (availableWidth < 0) {
        visibleNumber.value = 0;
        return;
    }

    const rawCount = Math.floor(availableWidth / menuWidth);
    const finalCount = Math.max(0, rawCount - 1); // 减1留给“更多菜单”

    visibleNumber.value = finalCount;
}

watch(route, (newRoute) => {
    calculateVisibleMenus(newRoute);
}, { deep: true, immediate: true });
function closePageExclusion(key) {
    const visitedViews = useTagsViewStore().visitedViews;
    for (let i = visitedViews.length - 1; i >= 0; i--) {
        const view = visitedViews[i];
        if (view.path.includes('/index')) continue;
        if (!view.path.includes(key)) {
            proxy.$tab.closePage(view);
        }
    }
}

async function handleSelect(key, keyPath, type) {
    emit('getRouter', key);
    currentIndex.value = key;

    const routeItem = routers.value.find(item => item.path === key);

    if (isHttp(key)) {
        // 外部链接直接打开新窗口
        window.open(key, '_blank');
    } else if (!routeItem || !routeItem.children) {
        // 叶子路由，带 query 的带上 query 跳转
        const routeMenu = childrenMenus.value.find(item => item.path === key);
        if (routeMenu && routeMenu.query) {
            const query = JSON.parse(routeMenu.query);
            router.push({ path: key, query });
        } else {
            router.push({ path: key });
        }
        appStore.toggleSideBarHide(true);
    } else {
        // 父级路由处理
        const routes = activeRoutes(key);
        if (type) {
            closePageExclusion(key);
            const visibleRoute = routes.find(r => r.hidden === false) || routes[0];
            if (visibleRoute) {
                if (visibleRoute.children && visibleRoute.children.length > 0) {
                    const firstVisibleChild = visibleRoute.children.find(c => c.hidden === false) || visibleRoute.children[0];
                    const lastChild = JSON.parse(JSON.stringify(firstVisibleChild));
                    lastChild.path = `${visibleRoute.path}/${firstVisibleChild.path}`;
                    proxy.$tab.refreshPage(lastChild);
                } else if (visibleRoute.query) {
                    const lastChild = JSON.parse(JSON.stringify(visibleRoute));
                    lastChild.query = JSON.parse(visibleRoute.query);
                    proxy.$tab.refreshPage(lastChild);
                } else {
                    proxy.$tab.refreshPage(visibleRoute);
                }
            }
        }
        appStore.toggleSideBarHide(false);
    }
}


function activeRoutes(key) {
    let routes = [];
    if (childrenMenus.value.length) {
        childrenMenus.value.forEach(item => {
            if (key === item.parentPath || (key === 'index' && item.path === '')) {
                routes.push(item);
            }
        });
    }
    if (routes.length) {
        permissionStore.setSidebarRouters(routes);
    } else {
        appStore.toggleSideBarHide(true);
    }
    return routes;
}

onMounted(() => {
    window.addEventListener('resize', calculateVisibleMenus);
    calculateVisibleMenus();
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', calculateVisibleMenus);
});

defineExpose({ handleSelect });
</script>

<style lang="scss">
.custom-topmenu.el-menu--horizontal {
    display: flex;
    align-items: center;
    height: 66px;
    box-sizing: border-box;
    background-color: #001529;
    border-bottom: none;

    /* 普通菜单项 */
    >.el-menu-item,
    >.el-submenu {
        margin: 0 5px;
        padding: 0 12px;
        height: 40px !important;
        line-height: 40px !important;
        display: flex;
        align-items: center;
        box-sizing: border-box;
        cursor: pointer;
        color: #fff;
        font-size: 16px;
        position: relative;
        overflow: visible;
    }

    /* 子菜单标题 */
    >.el-submenu>.el-submenu__title {
        display: flex;
        align-items: center;
        padding: 0 10px;
        margin: 0 5px;
        height: 40px !important;
        line-height: 40px !important;
        color: #fff;
        cursor: pointer;
        font-size: 16px;
        position: relative;
        border-radius: 4px;
    }

    /* 图标与文字间距 */
    .el-menu-item .svg-icon,
    .el-submenu .svg-icon {
        margin-right: 10px;
        font-size: 20px;
        vertical-align: middle;
    }

    /* 选中菜单项 */
    >.el-menu-item.is-active,
    >.el-sub-menu.is-active>.el-submenu__title {
        height: 66px !important;
        font-weight: 600;
        position: relative;
        background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
        transition: none !important;
        color: #fff !important;

        &::before {
            content: "";
            position: absolute;
            bottom: 4px;
            left: 0;
            width: 100%;
            height: 6px;
            background-color: #1F92FF;
            border-radius: 0 0 4px 4px;
        }
    }

    /* hover状态 */
    >.el-menu-item:not(.is-disabled):focus,
    >.el-menu-item:not(.is-disabled):hover,
    >.el-submenu>.el-submenu__title:hover {
        height: 66px !important;
        font-weight: 600;
        position: relative;
        background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
        transition: none !important;
        color: #fff !important;
    }

    /* 选中菜单项 */
    >.el-menu-item.is-active,
    >.el-sub-menu.is-active>.el-submenu__title {
        height: 66px !important;
        font-weight: 600;
        position: relative;
        background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
        transition: none !important;
        color: #fff !important;

        &::before {
            content: "";
            position: absolute;
            bottom: 4px;
            left: 0;
            width: 100%;
            height: 6px;
            background-color: #1F92FF;
            border-radius: 0 0 4px 4px;
        }
    }


}

/* 更多菜单 */
.more-menu .el-sub-menu__title {
    padding: 0px 12px !important;
}

.more-menu>.el-sub-menu__title {
    height: 66px !important;
    position: relative;
    transition: none !important;
    color: #fff !important;
    font-size: 16px;
    position: relative;
    overflow: visible;

    .el-sub-menu__icon-arrow {
        display: none;
    }

}

.more-menu>.el-sub-menu__title:hover {
    height: 66px !important;
    position: relative;
    background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
    transition: none !important;
    color: #fff !important;
    font-size: 16px;
    position: relative;
    overflow: visible;


}

/* 更多菜单选中下方蓝色条 */
.more-menu.is-active>.el-submenu__title::before {
    content: "";
    position: absolute;
    bottom: 4px;
    left: 0;
    width: 80%;
    height: 6px;
    background-color: #1F92FF;
    border-radius: 0 0 4px 4px;
}

.more-menu .el-menu-item:hover {
    height: 66px !important;
    font-weight: 600;
    position: relative;
    background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
    transition: none !important;
    color: #fff !important;
}

.custom-topmenu.el-menu--horizontal {

    :deep(.more-menu .el-menu-item:hover) {
        height: 66px !important;
        font-weight: 600;
        position: relative;
        background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
        transition: none !important;
        color: #fff !important;
        padding: 0 !important;

    }

    /* 更多菜单里的子菜单项选中样式 */
    :deep(.more-menu .el-menu .el-sub-menu .el-menu-item.is-active) {
        height: 663px !important;
        font-weight: 600;
        position: relative;
        background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
        transition: none !important;
        color: #fff !important;

        &::before {
            content: "";
            position: absolute;
            bottom: 4px;
            left: 0;
            width: 100%;
            height: 6px;
            background-color: #1F92FF;
            border-radius: 0 0 4px 4px;
        }
    }
}

.more-menu.is-active>.el-submenu__title {
    background: linear-gradient(180deg, #588BFE 0%, #3368FC 100%);
    color: #fff !important;
    font-weight: 600;
    position: relative;
}

.el-menu--horizontal>.el-sub-menu.is-active .el-sub-menu__title {
    background: linear-gradient(270deg, #1E60FB 0%, #5D8EFE 100%) !important;

    .svg-icon {
        margin-right: 10px;
        fill: #1069ed !important
    }
}

.el-menu--horizontal .el-menu {
    padding: 8px 10px;
    font-size: 16px;

    .svg-icon {
        font-size: 18px;
        margin-right: 10px;
        filter: invert(53%) sepia(33%) saturate(2466%) hue-rotate(189deg) brightness(96%) contrast(92%);
    }

}

.el-menu--horizontal .el-menu .el-menu-item.is-active,
.el-menu--horizontal .el-menu .el-sub-menu.is-active>.el-sub-menu__title {
    background-color: #1069ed;
    color: #ffff;
    border-radius: 2px;
    height: 40px !important;
    line-height: 40px !important;
    font-size: 16px;

    /* 圆角 */
    .svg-icon {
        margin-right: 10px;
        filter: brightness(0) invert(1);
    }

    &:hover {
        background-color: #1069ed !important;
        color: #ffff !important;

        .svg-icon {
            margin-right: 10px;
            filter: brightness(0) invert(1);
        }
    }
}

.el-menu--horizontal .el-menu .el-menu-item,
.el-menu--horizontal .el-menu .el-sub-menu__title {
    font-size: 16px;
}

.el-menu--horizontal .el-menu .el-menu-item:hover,
.el-menu--horizontal .el-menu .el-sub-menu__title:hover {
    background-color: transparent !important;
    color: #1069ed !important;

    .svg-icon {
        margin-right: 10px;
        filter: invert(53%) sepia(33%) saturate(2466%) hue-rotate(189deg) brightness(96%) contrast(92%);
    }

}

.more-menu-popper {
    margin-top: -6px !important;
    margin-left: -4px;

    .el-menu--popup {
        min-width: 160px;
        border-radius: 4px;
        box-shadow: 0 0 0 1px #dcdfe6;
    }
}
</style>
