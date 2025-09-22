<template>
    <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :ellipsis="false">
        <template v-for="(item, index) in topMenus">
            <el-menu-item :style="{ '--theme': theme }" :index="item.path" :key="index" v-if="index < visibleNumber">
                <svg-icon 
                    v-if="item.meta && item.meta.icon && item.meta.icon !== '#'" :icon-class="item.meta.icon" />
                {{ item.meta.title }}
            </el-menu-item>
        </template>

        <!-- 顶部菜单超出数量折叠 -->
        <el-sub-menu :style="{ '--theme': theme }" index="more" v-if="topMenus.length > visibleNumber">
            <template #title>更多菜单</template>
            <template v-for="(item, index) in topMenus">
                <el-menu-item :index="item.path" :key="index" v-if="index >= visibleNumber">
                    <svg-icon 
                        v-if="item.meta && item.meta.icon && item.meta.icon !== '#'" :icon-class="item.meta.icon" />
                    {{ item.meta.title }}
                </el-menu-item>
            </template>
        </el-sub-menu>
    </el-menu>
</template>

<script setup>
import { constantRoutes } from '@/router';
import { isHttp } from '@/utils/validate';
import useAppStore from '@/store/system/app';
import useSettingsStore from '@/store/system/settings';
import usePermissionStore from '@/store/system/permission';
import { el } from 'element-plus/es/locale/index.mjs';
import useTagsViewStore from '@/store/system/tagsView';
const { proxy } = getCurrentInstance();
// 顶部栏初始数
const visibleNumber = ref(null);
// 当前激活菜单的 index
const currentIndex = ref('/system');
// 隐藏侧边栏路由
const hideList = ['/index', '/user/profile'];

const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();
const route = useRoute();
const router = useRouter();
const emit = defineEmits(['getRouter']);
// 主题颜色
const theme = computed(() => settingsStore.theme);
// 所有的路由信息
const routers = computed(() => permissionStore.topbarRouters);
// 顶部显示菜单
const topMenus = computed(() => {
    let topMenus = [];
    routers.value.map((menu) => {
        if (menu.hidden !== true) {
            // 兼容顶部栏一级菜单内部跳转
            if (menu.path === '/') {
                topMenus.push(menu.children[0]);
            } else {
                topMenus.push(menu);
            }
        }
    });
    return topMenus;
});

// 设置子路由
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

// 默认激活的菜单
const activeMenu = computed(() => {
    const path = route.path;
    let activePath = path;
    console.log(route, '菜单');
    emit('getRouter', path);

    // 如果是根路径，选择第一个可见的菜单项
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

// function setVisibleNumber() {
//     const width = document.body.getBoundingClientRect().width / 3;
//     visibleNumber.value = parseInt(width / 85);
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

function closePageExclusion(key) {
    const visitedViews = useTagsViewStore().visitedViews;

    for (let i = visitedViews.length - 1; i >= 0; i--) {
        const view = visitedViews[i];
        if (view.path.includes('/index')) {
            continue;
        }
        if (!view.path.includes(key)) {
            proxy.$tab.closePage(view);
        }
    }
}

// 处理顶部导航菜单的选择事件
async function handleSelect(key, keyPath, type) {
    console.log(key, 'key');
    // console.log(currentIndex.value,"value");

    //子组件调用父组件
    emit('getRouter', key);

    // 设置当前选中的菜单索引
    currentIndex.value = key;
    // 查找选中的路由配置
    const route = routers.value.find((item) => item.path === key);

    if (isHttp(key)) {
        // 如果是http(s)链接,在新窗口打开
        window.open(key, '_blank');
    } else if (!route || !route.children) {
        // 如果没有子路由,在当前窗口打开
        const routeMenu = childrenMenus.value.find((item) => item.path === key);
        if (routeMenu && routeMenu.query) {
            // 如果有query参数,解析后带上
            let query = JSON.parse(routeMenu.query);
            router.push({ path: key, query: query });
        } else {
            // 没有query参数直接跳转
            router.push({ path: key });
        }
        // 隐藏左侧菜单
        appStore.toggleSideBarHide(true);
    } else {
        // 有子路由,显示左侧联动菜单
        let routes = activeRoutes(key);
        if (type) {
            closePageExclusion(key);
            if (routes.length > 0) {
                // 获取所有标签页

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
        // 显示左侧菜单
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
    window.addEventListener('resize', calculateVisibleMenus);
});
onBeforeUnmount(() => {
    window.removeEventListener('resize', calculateVisibleMenus);
});

onMounted(() => {
    calculateVisibleMenus();
});
// 如果需要暴露给父组件使用，可以使用 defineExpose
defineExpose({
    handleSelect
});
</script>

<style lang="scss">
.el-menu--horizontal.el-menu {
    padding-top: 10px;
}

.topmenu-container.el-menu--horizontal>.el-menu-item {
    font-size: 16px;
    float: left;
    height: 40px !important;
    line-height: 40px !important;
    color: #333 !important;
    padding: 0 15px !important;
    margin: 0 10px !important;
    border-radius: 5px;
}

/* sub-menu item */
.topmenu-container.el-menu--horizontal>.el-sub-menu .el-sub-menu__title {
    font-size: 16px;
    float: left;
    height: 40px !important;
    line-height: 40px !important;
    color: #333 !important;
    padding: 0 15px !important;
    margin: 0 10px !important;
    border-radius: 5px;
}

.topmenu-container.el-menu--horizontal>.el-menu-item.is-active,
.el-menu--horizontal>.el-sub-menu.is-active .el-submenu__title,
.el-menu--horizontal>.el-sub-menu.is-active .el-sub-menu__title {
    background: #{'var(--theme)'} !important;
    color: #fff !important;
}

/* 背景色隐藏 */
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):focus,
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):hover,
.topmenu-container.el-menu--horizontal>.el-submenu .el-submenu__title:hover {
    background: #{'var(--theme)'} !important;
    color: #fff !important;
}

/* 图标右间距 */
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

    .svg-icon {
        margin-right: 10px;
    }
}
</style>
