import { createWebHistory, createRouter } from 'vue-router';
import { clearCancelTokens } from '@/utils/request'; // 确保导入路径正确

/* Layout */
import Layout from '@/layout';
/* 系统模块公共路由 */
import systemPublicRouter from './system/public/index.js';
/* 系统模块动态路由 */
import systemDynamicRouter from './system/dynamic/index.js';

/* 示例模块公共路由 */
import examplePublicRouter from './example/public/index.js';
/* 示例模块动态路由 */
import exampleDynamicRouter from './example/dynamic/index.js';

/* 示例模块公共路由 */
import daPublicRouter from './da/public/index.js';
/* 示例模块动态路由 */
import daDynamicRouter from './da/dynamic/index.js';
/* 数据资产资产地图 */
import daAssetRouter from './da/asset/index.js';
/* 数据质量 */
import daQualityRouter from './da/quality/index.js';


import dsPublicRouter from './ds/public/index.js';
/* api服务 */
import dsDynamicRouter from './ds/dynamic/index.js';

/* 数据规划公共路由 */
import dpPublicRouter from './dp/model/index.js';
/* 标签管理公共路由 */
import dsCatRouter from './ds/client/index.js';
/* 数据规划动态路由 */
import dpDataElemRouter from './dp/dataElem/index.js';

// // 研发路由
import dppRouter from './dpp/index.js';


import dPdocument from './dp/document/index.js';

/* 数据规划动态路由 */
// 用来存储当前请求的取消源
// let currentCancelSource = null;

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [...systemPublicRouter, ...examplePublicRouter, ...daPublicRouter, ...dsCatRouter, ...dsPublicRouter, ...dPdocument, ...dpPublicRouter, ...dpDataElemRouter, ...daAssetRouter, ...daQualityRouter, ...dppRouter];

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [...systemDynamicRouter, ...exampleDynamicRouter, ...daDynamicRouter, ...dsDynamicRouter,];

const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition;
        } else {
            return { top: 0 };
        }
    }
});

// 在路由守卫中添加取消请求逻辑
router.beforeEach((to, from, next) => {
    clearCancelTokens(); // 在路由切换前取消所有未完成的请求
    next();
});

/**
 * 重置路由
 */
export function resetRouter() {
    window.location.href = `${window.location.protocol}//${window.location.host}/login/`;
}

export default router;
