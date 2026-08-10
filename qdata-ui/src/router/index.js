/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import { createWebHistory, createRouter } from 'vue-router';

/* System module public routing */
import systemPublicRouter from './system/public/index.js';
/* System module dynamic routing */
import systemDynamicRouter from './system/dynamic/index.js';

/* Example module public routing */
import examplePublicRouter from './example/public/index.js';
/* Example module dynamic routing */
import exampleDynamicRouter from './example/dynamic/index.js';

/* Example module public routing */
import daPublicRouter from './da/public/index.js';
/* Example module dynamic routing */
import daDynamicRouter from './da/dynamic/index.js';
/* Data asset asset map */
import daAssetRouter from './da/asset/index.js';
/* Data quality */
import daQualityRouter from './da/quality/index.js';
import dsPublicRouter from './ds/public/index.js';
/* api service */
import dsDynamicRouter from './ds/dynamic/index.js';

/* Data planning public routing */
import dpPublicRouter from './dp/model/index.js';
/* Tag management public routing */
import dsCatRouter from './ds/client/index.js';
/* Data planning dynamic routing */
import dpDataElemRouter from './dp/dataElem/index.js';

// // R&D routing
import dppRouter from './dpp/index.js';

import dmRouter from './dm/businessLayer/index.js'

import dPdocument from './dp/document/index.js';

/* Metadata collection*/
import mcPublicRouter from './mc/public/index.js';
/* Metadata collection */
import mcDynamicRouter from './mc/dynamic/index.js';
/* table metadata */
import metaPublicRouter from './meta/public/index.js';
/* table metadata */
import metaDynamicRouter from './meta/dynamic/index.js';
import attTag from './att/tag/index.js';

/* Data planning dynamic routing */
// Used to store the cancellation source of the current request
// let currentCancelSource = null;

/**
 * Note: Routing configuration items
 *
 * hidden: true // When true is set, the route will not appear in the sidebar, such as 401, login and other pages, or some editing pages /edit/1
 * alwaysShow: true // When you declare more than 1 route as children under a route, it will automatically become a nested mode - such as a component page
 * // When there is only one, that sub-route will be displayed in the sidebar as the root route - such as the guide page
 * // If you want to display your root route regardless of the number of children declared under the route
 * // You can set alwaysShow: true so that it will ignore the previously defined rules and always show the root route
 * redirect: noRedirect // When noRedirect is set, the route cannot be clicked in the breadcrumb navigation.
 * name:'router-name' // Set the name of the route, be sure to fill it in otherwise various problems will occur when using <keep-alive>
 * query: '{"id": 1, "name": "ry"}' // Default passing parameters for access routes
 * roles: ['admin', 'common'] // Role permissions to access routing
 * permissions: ['a:a:a', 'b:b:b'] // Access route menu permissions
 * meta : {
    noCache: true // If set to true, it will not be cached by <keep-alive> (default false)
    title: 'title' // Set the name of the route displayed in the sidebar and breadcrumbs
    icon: 'svg-name' // Set the icon of this route, corresponding to the path src/assets/icons/svg
    breadcrumb: false // If set to false, it will not be displayed in breadcrumbs
    activeMenu: '/system/user' // When the route sets this attribute, the corresponding sidebar will be highlighted.
  }
 */

// public route
export const constantRoutes = [...systemPublicRouter, ...examplePublicRouter, ...daPublicRouter, ...dsCatRouter, ...dsPublicRouter, ...dPdocument, ...dpPublicRouter, ...dpDataElemRouter, ...daAssetRouter, ...daQualityRouter, ...dppRouter, ...mcPublicRouter, ...metaPublicRouter, ...mcDynamicRouter, ...metaDynamicRouter, ...dmRouter, ...attTag];

// Dynamic routing, dynamic loading based on user permissions
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

/**
 * Reset routing
 */
export function resetRouter() {
    window.location.href = `${window.location.protocol}//${window.location.host}/login/`;
}

export default router;
