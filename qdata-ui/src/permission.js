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

import router, { resetRouter } from "./router";
import { ElMessage } from "element-plus";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import { getToken } from "@/utils/auth";
import { isHttp } from "@/utils/validate";
import { isRelogin } from "@/utils/request";
import useUserStore from "@/store/system/user";
import useSettingsStore from "@/store/system/settings";
import usePermissionStore from "@/store/system/permission";
import { i18n } from '@/plugins/vueI18n'

NProgress.configure({ showSpinner: false });

// 认证模式
const authType = import.meta.env.VITE_APP_AUTH_TYPE;
// 应用ID
const clientId = import.meta.env.VITE_APP_CLIENTID;
// 服务端地址
const serverUrl = import.meta.env.VITE_APP_SERVER_URL;
// 当前APP地址
const appUrl = import.meta.env.VITE_APP_THIS_APP_URL;

const whiteList = ["/login", "/register", "/sso/login", "/sso",];

router.beforeEach((to, from, next) => {
  NProgress.start();

  if (getToken()) {
    const title = i18n.global.te(`router.${to.meta.lang}`)?i18n.global.t(`router.${to.meta.lang}`):to.meta.title;
    to.meta.title && useSettingsStore().setTitle(title);

    if (to.path === "/login") {
      next({ path: "/" });
      NProgress.done();
    } else if (whiteList.indexOf(to.path) !== -1) {
      next();
    } else {
      if (useUserStore().roles.length === 0) {
        isRelogin.show = true;
        useUserStore()
          .getInfo()
          .then(() => {
            isRelogin.show = false;
            usePermissionStore()
              .generateRoutes()
              .then((accessRoutes) => {
                accessRoutes.forEach((route) => {
                  if (!isHttp(route.path)) {
                    router.addRoute(route);
                  }
                });
                next({ ...to, replace: true });
              });
          })
          .catch((err) => {
            useUserStore()
              .logOut()
              .then(() => {
                ElMessage.error(err);
                next({ path: "/" });
              });
          });
      } else {
        next();
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next();
    } else {
      if (authType !== "sso") {
        next(`/login?redirect=${to.fullPath}`);
      } else {
        if (to.path.indexOf("/login") !== -1) {
          next(`/login?redirect=${to.fullPath}`);
        } else {
          window.location.href =
            serverUrl +
            "/oauth2/authorize?" +
            "response_type=code" +
            "&client_id=" + clientId +
            "&redirect_uri=" + appUrl + "/sso" +
            "&state=" + to.fullPath;
        }
      }
      NProgress.done();
    }
  }
});


router.afterEach(() => {
  NProgress.done();
});
