import { onActivated } from "vue";

/**
 * 页面自动刷新 Hook
 * 用于处理从详情页/新增页返回列表页时的自动刷新逻辑
 * @param {string} key - 唯一的标识符，用于区分不同页面的刷新标记
 * @param {Function} [callback] - 列表页的刷新回调函数。传入此参数时，Hook 会自动在 onActivated 中检查并执行刷新。
 * @returns {Object} 返回包含 setRefreshNeeded 的对象
 */
export function usePageRefresh(key, callback) {
  if (!key) {
    console.warn("usePageRefresh: key is required");
  }

  const storageKey = `page_refresh_${key}`;

  // 如果传入了回调函数，说明是在列表页使用，自动注册 onActivated
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
   * 设置刷新标记
   * 通常在详情页/新增页保存成功后调用
   */
  const setRefreshNeeded = () => {
    sessionStorage.setItem(storageKey, "true");
  };

  return {
    setRefreshNeeded,
  };
}
