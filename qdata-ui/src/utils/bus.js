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

// bus.js
import { reactive } from 'vue';

const bus = reactive({
  events: {},

  on(event, callback) {
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event].push(callback);
  },

  off(event, callback) {
    const index = this.events[event]?.indexOf(callback);
    if (index !== -1) {
      this.events[event].splice(index, 1);
    }
  },

  emit(event, data) {
    this.events[event]?.forEach(callback => callback(data));
  }
});

export default bus;
