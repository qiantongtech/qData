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

import axios from 'axios'
import { ElNotification , ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/anivia.js'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'
import useUserStore from '@/store/system/user'
import { i18n } from '@/plugins/vueI18n'
import { getStoredLang } from '@/store/system/locale'

let downloadLoadingInstance;
// Whether to show re-login
export let isRelogin = { show: false };

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// Create axios instance
const service = axios.create({
  // The request configuration in axios has the baseURL option, which indicates the public part of the request URL.
  baseURL: '',
  // timeout
  timeout: 10000
})

// request interceptor
service.interceptors.request.use(config => {
  // Do you need to set token?
  const isToken = (config.headers || {}).isToken === false
  // Is it necessary to prevent repeated submission of data?
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken() // Let each request carry a custom token. Please modify it according to the actual situation.
  }

  // Set the request language parameters uniformly to ensure that the backend can recognize the current language
  config.headers['accept-language'] = getStoredLang()

  // get request mapping params parameters
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params);
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }
  if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
    const requestObj = {
      url: config.url,
      data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
      time: new Date().getTime()
    }
    const requestSize = Object.keys(JSON.stringify(requestObj)).length; // Request data size
    const limitSize = 5 * 1024 * 1024; // Limit storage data to 5M
    if (requestSize >= limitSize) {
      console.warn(`[${config.url}]: ` + "The request data exceeds the permitted 5 MB limit; duplicate submission validation cannot be performed.")
      return config;
    }
    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      const s_url = sessionObj.url;                // Request address
      const s_data = sessionObj.data;              // Request data
      const s_time = sessionObj.time;              // Request time
      const interval = 1000;                       // Interval time (ms), less than this time is considered a duplicate submission
      if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
        const message = i18n.global.t('common.request.repeatSubmit');
        console.warn(`[${s_url}]: ` + message)
        return Promise.reject(new Error(message))
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }
  return config
}, error => {
    console.log(error)
    Promise.reject(error)
})

// response interceptor
service.interceptors.response.use(res => {
    // If the status code is not set, the default success status is
    const code = res.data.code || 200;
    // Get error message
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    // Binary data is returned directly
    if (res.request.responseType ===  'blob' || res.request.responseType ===  'arraybuffer') {
      return res.data
    }
    if (code === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true;
        ElMessageBox.confirm(i18n.global.t('common.request.loginExpired'), i18n.global.t('common.message.systemPrompt'), { confirmButtonText: i18n.global.t('common.request.reLogin'), cancelButtonText: i18n.global.t('common.button.cancel'), type: 'warning' }).then(() => {
          isRelogin.show = false;
          useUserStore().logOut().then(() => {
            location.href = '/index';
          })
      }).catch(() => {
        isRelogin.show = false;
      });
    }
      return Promise.reject(i18n.global.t('common.request.expiredSession'))
    } else if (code === 500) {
      ElMessage({ message: msg, type: 'error' })
      return Promise.reject(new Error(msg))
    } else if (code === 601) {
      ElMessage({ message: msg, type: 'warning' })
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      ElNotification.error({ title: msg })
      return Promise.reject('error')
    } else {
      return  Promise.resolve(res.data)
    }
  },
  error => {
    console.log('err' + error)
    let { message } = error;
    if (message == "Network Error") {
      message = i18n.global.t('common.request.networkError')
    } else if (message.includes("timeout")) {
      message = i18n.global.t('common.request.timeout')
    } else if (message.includes("Request failed with status code")) {
      message = i18n.global.t('common.request.interfaceError').replace('{code}', message.substr(message.length - 3));
    }
    ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
    return Promise.reject(error)
  }
)

// Universal download method
export function download(url, params, filename, config) {
  downloadLoadingInstance = ElLoading.service({ text: i18n.global.t('common.request.downloading'), background: "rgba(0, 0, 0, 0.7)", })
  return service.post(url, params, {
    transformRequest: [(params) => { return tansParams(params) }],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }).then(async (data) => {
    const isBlob = blobValidate(data);
    if (isBlob) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      ElMessage.error(errMsg);
    }
    downloadLoadingInstance.close();
  }).catch((r) => {
      console.error(r)
      ElMessage.error(i18n.global.t('common.request.downloadError'))
      downloadLoadingInstance.close();
    })
}

export default service
