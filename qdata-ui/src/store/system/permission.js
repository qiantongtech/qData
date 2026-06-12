/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

import auth from '@/plugins/auth';
import router, { constantRoutes, dynamicRoutes } from '@/router';
import { getRouters } from '@/api/system/menu.js';
import Layout from '@/layout/index';
import ParentView from '@/components/ParentView';
import InnerLink from '@/layout/components/InnerLink';

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue');

const usePermissionStore = defineStore('permission', {
    state: () => ({
        routes: [],
        addRoutes: [],
        defaultRoutes: [],
        topbarRouters: [],
        sidebarRouters: []
    }),
    actions: {
        setRoutes(routes) {
            this.addRoutes = routes;
            this.routes = constantRoutes.concat(routes);
        },
        setDefaultRoutes(routes) {
            this.defaultRoutes = constantRoutes.concat(routes);
        },
        setTopbarRoutes(routes) {
            this.topbarRouters = routes;
        },
        setSidebarRouters(routes) {
            this.sidebarRouters = routes;
        },
        generateRoutes(roles) {
            return new Promise((resolve) => {
                // 向后端请求路由数据
                getRouters().then((res) => {
                    const sdata = JSON.parse(JSON.stringify(res.data));
                    const rdata = JSON.parse(JSON.stringify(res.data));
                    const defaultData = JSON.parse(JSON.stringify(res.data));
                    const sidebarRoutes = filterAsyncRouter(sdata);
                    const rewriteRoutes = filterAsyncRouter(rdata, false, true);
                    const defaultRoutes = filterAsyncRouter(defaultData);
                    console.log('As------>',sidebarRoutes,rewriteRoutes,defaultRoutes);
                    const asyncRoutes = filterDynamicRoutes(dynamicRoutes);
                    asyncRoutes.forEach((route) => {
                        router.addRoute(route);
                    });
                    this.setRoutes(rewriteRoutes);
                    this.setSidebarRouters(constantRoutes.concat(sidebarRoutes));
                    this.setDefaultRoutes(sidebarRoutes);
                    this.setTopbarRoutes(defaultRoutes);
                    resolve(rewriteRoutes);
                });
            });
        },
        updateTopbarRoutes(routes) {
            const sdata = JSON.parse(JSON.stringify(routes));
            const rdata = JSON.parse(JSON.stringify(routes));
            const defaultData = JSON.parse(JSON.stringify(routes));
            const sidebarRoutes = filterAsyncRouter(sdata);
            const rewriteRoutes = filterAsyncRouter(rdata, false, true);
            const defaultRoutes = filterAsyncRouter(defaultData);
            this.setRoutes(rewriteRoutes);
            this.setSidebarRouters(constantRoutes.concat(sidebarRoutes));
            this.setDefaultRoutes(sidebarRoutes);
            this.setTopbarRoutes(defaultRoutes);
        }
    }
});

function setupRouteLang(route,lastRouter){
    if(!route.meta){
        route.meta = {};
    }
    if(lastRouter?.meta?.lang){
        route.meta.lang = lastRouter.meta.lang + route.name;
    }else{
        if(route.name){
            const name = route.name.charAt(0).toLowerCase() + route.name.slice(1);
            route.meta.lang = 'dynamic.'+name;
        }
    }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
    return asyncRouterMap.filter((route) => {
        if (type && route.children) {
            const setLang = function(route, lastRouter) {
                setupRouteLang(route, lastRouter);
                if (route.children && route.children.length > 0) {
                    route.children.forEach(child => {
                        setLang(child, route);
                    });
                }
            }
            setLang(route, lastRouter);
            route.children = filterChildren(route.children,lastRouter);
        }

        if (route.component) {
            // Layout ParentView 组件特殊处理
            if (route.component === 'Layout') {
                route.component = Layout;
            } else if (route.component === 'ParentView') {
                route.component = ParentView;
            } else if (route.component === 'InnerLink') {
                route.component = InnerLink;
            } else {
                route.component = loadView(route.component);
            }
        }

        if(!type){
            setupRouteLang(route,lastRouter);
        }

        if (route.children != null && route.children && route.children.length) {
            route.children = filterAsyncRouter(route.children, route, type);
        } else {
            delete route['children'];
            delete route['redirect'];
        }

        return true;
    });
}

function filterChildren(childrenMap, lastRouter = false) {
    var children = [];
    childrenMap.forEach((el, index) => {
        if (el.children && el.children.length) {
            if (el.component === 'ParentView' && !lastRouter) {
                el.children.forEach((c) => {
                    c.path = el.path + '/' + c.path;
                    if (c.children && c.children.length) {
                        children = children.concat(filterChildren(c.children, c));
                        return;
                    }
                    children.push(c);
                });
                return;
            }
        }
        if (lastRouter) {
            el.path = lastRouter.path + '/' + el.path;
            if (el.children && el.children.length) {
                children = children.concat(filterChildren(el.children, el));
                return;
            }
        }
        children = children.concat(el);
    });
    return children;
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
    const res = [];
    routes.forEach((route) => {
        if (route.permissions) {
            if (auth.hasPermiOr(route.permissions)) {
                res.push(route);
            }
        } else if (route.roles) {
            if (auth.hasRoleOr(route.roles)) {
                res.push(route);
            }
        }
    });
    return res;
}

export const loadView = (view) => {
    let res;
    for (const path in modules) {
        const dir = path.split('views/')[1].split('.vue')[0];
        if (dir === view) {
            res = () => modules[path]();
        }
    }
    return res;
};

export default usePermissionStore;
