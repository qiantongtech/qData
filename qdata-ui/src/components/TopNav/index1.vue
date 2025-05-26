<template>
    <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :ellipsis="false">
        <template v-for="(item, index) in topMenus">
            <el-menu-item :style="{ '--theme': theme }" :index="item.path" :key="index" v-if="index < visibleNumber">
                <svg-icon v-if="item.meta && item.meta.icon && item.meta.icon !== '#'" :icon-class="item.meta.icon" />
                {{ item.meta.title }}
            </el-menu-item>
        </template>

        <!-- 顶部菜单超出数量折叠 -->
        <el-sub-menu :style="{ '--theme': theme }" index="more" v-if="topMenus.length > visibleNumber">
            <template #title>更多菜单</template>
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
import { constantRoutes } from '@/router';
import { isHttp } from '@/utils/validate';
import useAppStore from '@/store/system/app';
import useSettingsStore from '@/store/system/settings';
import usePermissionStore from '@/store/system/permission';

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

function setVisibleNumber() {
    const width = document.body.getBoundingClientRect().width / 3;
    visibleNumber.value = parseInt(width / 85);
}

// 处理顶部导航菜单的选择事件
function handleSelect(key, keyPath) {
    window.open(key, '_blank', 'noreferrer');
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
        activeRoutes(key);
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


/* 背景色隐藏 */
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):focus,
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):hover,
.topmenu-container.el-menu--horizontal>.el-submenu .el-submenu__title:hover {
    border-top: 2px solid #{'var(--theme)'} !important;
    color: #303133;
    background-color: var(--el-menu-hover-bg-color);
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
</style>
