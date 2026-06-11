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

const useDictStore = defineStore(
  'dict',
  {
    state: () => ({
      dict: new Array()
    }),
    actions: {
      // 获取字典
      getDict(_key) {
        if (_key == null && _key == "") {
          return null;
        }
        try {
          for (let i = 0; i < this.dict.length; i++) {
            if (this.dict[i].key == _key) {
              return this.dict[i].value;
            }
          }
        } catch (e) {
          return null;
        }
      },
      // 设置字典
      setDict(_key, value) {
        if (_key !== null && _key !== "") {
          this.dict.push({
            key: _key,
            value: value
          });
        }
      },
      // 删除字典
      removeDict(_key) {
        var bln = false;
        try {
          for (let i = 0; i < this.dict.length; i++) {
            if (this.dict[i].key == _key) {
              this.dict.splice(i, 1);
              return true;
            }
          }
        } catch (e) {
          bln = false;
        }
        return bln;
      },
      // 清空字典
      cleanDict() {
        this.dict = new Array();
      },
      // 初始字典
      initDict() {
      }
    }
  })

export default useDictStore
