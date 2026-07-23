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
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
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
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // timeout
  timeout: 600000
})

let cancelTokens = [];

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

  // The government affairs intranet in the production environment cannot use put and delete requests.
  const env = import.meta.env.VITE_APP_ENV;
  const openProxyPutDeleteRequest = import.meta.env.VITE_APP_OPEN_PROXY_PUT_DELETE_REQUEST;
  if (openProxyPutDeleteRequest === 'true') {
    // Check the request method and change to POST request for PUT and DELETE requests
    if (config.method === 'put' || config.method === 'delete') {
      config.headers['X-HTTP-Method-Override'] = config.method.toUpperCase(); // Keep original request method information
      config.method = 'post'; // Change the request method to POST
    }
  }
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
        const err = new Error(message);
        err.isRepeatSubmit = true; // Mark as duplicate submission
        return Promise.reject(err);
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }

  // Create a cancellation token and add it to the request configuration
  const source = axios.CancelToken.source();
  config.cancelToken = source.token;
  cancelTokens.push(source);
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// response interceptor
service.interceptors.response.use(res => {
  const hideErrorMessage = (res.config.headers || {}).hideErrorMessage === true || (res.config.headers || {}).hideErrorMessage === 'true'
  // If the status code is not set, the default success status is
  const code = res.data.code || 200;
  // Get error message
  const msg = errorCode[code] || res.data.msg || errorCode['default']
  // Binary data is returned directly
  if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
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
    if (!hideErrorMessage) {
      ElMessage({ message: msg, type: 'error' })
    }
    return Promise.reject(new Error(msg))
  } else if (code === 601) {
    if (!hideErrorMessage) {
      ElMessage({ message: msg, type: 'warning' })
    }
    return Promise.reject(new Error(msg))
  } else if (code !== 200) {
    if (!hideErrorMessage) {
      ElNotification.warning({ title: msg })
    }
    return Promise.reject('error')
  } else {
    return Promise.resolve(res.data)
  }
},
  error => {
    const hideErrorMessage = (error.config?.headers || {}).hideErrorMessage === true || (error.config?.headers || {}).hideErrorMessage === 'true'
    console.log('err' + error)
    let { message } = error;

    if (message == "Network Error") {
      message = i18n.global.t('common.request.networkError');
    } else if (message.includes("timeout")) {
      message = i18n.global.t('common.request.timeout');
    } else if (message.includes("Request failed with status code")) {
      message = i18n.global.t('common.request.interfaceError').replace('{code}', message.substr(message.length - 3));
    } else if ((message.includes(i18n.global.t('common.request.routeChangeCancel')))) {
      return null
    } else if (error.isRepeatSubmit) {
      ElMessage({ message: error.message, type: 'warning' });
      return Promise.reject(error);
    }
    if (!hideErrorMessage) {
      ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
    }
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
      ElMessage.warning(errMsg);
    }
    downloadLoadingInstance.close();
  }).catch((r) => {
    console.error(r)
    ElMessage.warning(i18n.global.t('common.request.downloadError'))
    downloadLoadingInstance.close();
  })
}
export function download2(url, params, filename, config) {
  downloadLoadingInstance = ElLoading.service({ text: i18n.global.t('common.request.downloading'), background: "rgba(0, 0, 0, 0.7)" });

  return service.get(url, {
    params: params, // When using a GET request, parameters are passed as query parameters
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }).then(async (data) => {
    const isBlob = blobValidate(data);
    if (isBlob) {
      const blob = new Blob([data]);
      saveAs(blob, filename);  // save file
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default'];
      ElMessage.warning(errMsg);  // Show error message
    }
    downloadLoadingInstance.close();  // Turn off loading animation
  }).catch((r) => {
    console.error(r);
    ElMessage.warning(i18n.global.t('common.request.downloadError'));
    downloadLoadingInstance.close();
  });
}


export default service

// Export function to cancel request
export function clearCancelTokens() {
  cancelTokens.forEach(source => source.cancel('Route change: Request canceled'));
  cancelTokens = [];
}
