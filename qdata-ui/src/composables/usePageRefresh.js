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
