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

export default {
  /**
   * Web page title
   */
  title: import.meta.env.VITE_APP_TITLE,
  /**
   * Sidebar theme dark theme theme-dark, light theme theme-light
   */
  sideTheme: 'theme-dark',
  /**
   * Whether the system layout configuration
   */
  showSettings: true,

  /**
   * Whether to display top navigation
   */
  topNav: false,

  /**
   * Whether to display tagsView
   */
  tagsView: true,

  /**
   * Whether to fix the head
   */
  fixedHeader: true,

  /**
   * Whether to display logo
   */
  sidebarLogo: true,

  /**
   * Whether to display dynamic titles
   */
  dynamicTitle: true,

  /**
   * The routing path of the logo needs to be displayed in the navigation bar
   * When accessing these paths, the logo will be dynamically inserted on the left side of the navigation bar (occupying 200px width)
   */
  navbarLogoRoutes: ['/ai',],

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production'
}
