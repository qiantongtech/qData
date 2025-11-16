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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
    to.meta.title && useSettingsStore().setTitle(to.meta.title);

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
