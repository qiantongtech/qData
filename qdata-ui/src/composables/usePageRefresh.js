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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import { onActivated } from "vue";

/**
 * Page automatic refresh Hook
 * Used to handle the automatic refresh logic when returning to the list page from the details page/new page
 * @param {string} key - a unique identifier used to distinguish the refresh tags of different pages
 * @param {Function} [callback] - The refresh callback function of the list page. When this parameter is passed in, Hook will automatically check and perform refresh in onActivated.
 * @returns {Object} returns an object containing setRefreshNeeded
 */
export function usePageRefresh(key, callback) {
  if (!key) {
    console.warn("usePageRefresh: key is required");
  }

  const storageKey = `page_refresh_${key}`;

  // If the callback function is passed in, it means that it is used on the list page and is automatically registered onActivated
  if (typeof callback === "function") {
    onActivated(() => {
      const needRefresh = sessionStorage.getItem(storageKey);
      if (needRefresh === "true") {
        callback();
        sessionStorage.removeItem(storageKey);
      }
    });
  }

  /**
   * Set refresh flag
   * Usually called after the details page/new page is successfully saved.
   */
  const setRefreshNeeded = () => {
    sessionStorage.setItem(storageKey, "true");
  };

  return {
    setRefreshNeeded,
  };
}
