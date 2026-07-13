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

import { defineConfig, loadEnv } from "vite";
import path from "path";
import createVitePlugins from "./vite/plugins";

// https://vitejs.dev/config/
export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, process.cwd());
  const { VITE_APP_ENV, VITE_APP_FLOW_API } = env;
  return {
    // Deploy URLs in production and development environments.
    // By default, Vite will assume that your application is deployed on the root path of a domain name
    // For example https://www.qdata.vip/. If the application is deployed on a subpath, you need to specify the subpath with this option. For example, if your application is deployed at https://www.qdata.vip/admin/, set the baseUrl to /admin/.
    base: VITE_APP_ENV === "production" ? "/" : "/",
    plugins: createVitePlugins(env, command === "build"),
    build: {
      rollupOptions: {
        input: {
          main: path.resolve(__dirname, "index.html"),
          // nested: path.resolve(__dirname, "login/index.html"),
        },
        output: {
          manualChunks(id) {
            if (id.includes("node_modules")) {
              return id
                .toString()
                .split("node_modules/")[1]
                .split("/")[0]
                .toString();
            }
          },
        },
      },
    },
    resolve: {
      // https://cn.vitejs.dev/config/#resolve-alias
      alias: {
        // Set path
        "~": path.resolve(__dirname, "./"),
        // Set alias
        "@": path.resolve(__dirname, "./src"),
      },
      // https://cn.vitejs.dev/config/#resolve-extensions
      extensions: [".mjs", ".js", ".ts", ".jsx", ".tsx", ".json", ".vue"],
    },
    // Vite related configuration
    server: {
      port: 81,
      host: true,
      open: true,
      proxy: {
        // https://cn.vitejs.dev/config/#server-proxy
        "/dev-api": {
          target: "http://localhost:8080",
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-api/, ""),
        },
        "/dev-ai": {
          target: "http://localhost:8087",
          // target: "http://192.168.20.115:8080",
          // target: "https://qdata-pro.qiantong.tech/prod-api/",
          // target: "http://110.42.38.62:30001/prod-api/",
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-ai/, ""),
        },
        "/jmreport": {
          target: "http://localhost:8080",
          changeOrigin: true,
        },
        "/v3/api-docs": {
          target: "http://localhost:8080",
          changeOrigin: true,
          rewrite: (p) => p.replace("", ""),
        },
      },
    },
    //fix:error:stdin>:7356:1: warning: "@charset" must be the first rule in the file
    css: {
      postcss: {
        plugins: [
          {
            postcssPlugin: "internal:charset-removal",
            AtRule: {
              charset: (atRule) => {
                if (atRule.name === "charset") {
                  atRule.remove();
                }
              },
            },
          },
        ],
      },
    },
  };
});
