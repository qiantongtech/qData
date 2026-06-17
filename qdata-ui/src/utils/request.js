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
// 是否显示重新登录
export let isRelogin = { show: false };

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // 超时
  timeout: 600000
})

let cancelTokens = [];

// request拦截器
service.interceptors.request.use(config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  // 是否需要防止数据重复提交
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  }

  // 统一设置请求语言参数，保证后端能识别当前语言
  config.headers['X-Lang'] = getStoredLang()

  // 处理生产环境政务内网无法使用put、delete请求
  const env = import.meta.env.VITE_APP_ENV;
  const openProxyPutDeleteRequest = import.meta.env.VITE_APP_OPEN_PROXY_PUT_DELETE_REQUEST;
  if (openProxyPutDeleteRequest === 'true') {
    // 检查请求方法，并为PUT和DELETE请求改为POST请求
    if (config.method === 'put' || config.method === 'delete') {
      config.headers['X-HTTP-Method-Override'] = config.method.toUpperCase(); // 保留原请求方法信息
      config.method = 'post'; // 改变请求方法为POST
    }
  }
  // get请求映射params参数
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
    const requestSize = Object.keys(JSON.stringify(requestObj)).length; // 请求数据大小
    const limitSize = 5 * 1024 * 1024; // 限制存放数据5M
    if (requestSize >= limitSize) {
      console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
      return config;
    }
    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      const s_url = sessionObj.url;                // 请求地址
      const s_data = sessionObj.data;              // 请求数据
      const s_time = sessionObj.time;              // 请求时间
      const interval = 1000;                       // 间隔时间(ms)，小于此时间视为重复提交
      if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
        const message = i18n.global.t('common.request.repeatSubmit');
        const err = new Error(message);
        err.isRepeatSubmit = true; // 标记为重复提交
        return Promise.reject(err);
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }

  // 创建取消令牌并添加到请求配置中
  const source = axios.CancelToken.source();
  config.cancelToken = source.token;
  cancelTokens.push(source);
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
  // 未设置状态码则默认成功状态
  const code = res.data.code || 200;
  // 获取错误信息
  const msg = errorCode[code] || res.data.msg || errorCode['default']
  // 二进制数据则直接返回
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
    ElMessage({ message: msg, type: 'error' })
    return Promise.reject(new Error(msg))
  } else if (code === 601) {
    ElMessage({ message: msg, type: 'warning' })
    return Promise.reject(new Error(msg))
  } else if (code !== 200) {
    ElNotification.warning({ title: msg })
    return Promise.reject('error')
  } else {
    return Promise.resolve(res.data)
  }
},
  error => {
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
    ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
    return Promise.reject(error)
  }
)

// 通用下载方法
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
    params: params, // 使用 GET 请求时，参数作为查询参数传递
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }).then(async (data) => {
    const isBlob = blobValidate(data);
    if (isBlob) {
      const blob = new Blob([data]);
      saveAs(blob, filename);  // 保存文件
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default'];
      ElMessage.warning(errMsg);  // 显示错误消息
    }
    downloadLoadingInstance.close();  // 关闭加载动画
  }).catch((r) => {
    console.error(r);
    ElMessage.warning(i18n.global.t('common.request.downloadError'));
    downloadLoadingInstance.close();
  });
}


export default service

// 导出取消请求的函数
export function clearCancelTokens() {
  cancelTokens.forEach(source => source.cancel('Route change: Request canceled'));
  cancelTokens = [];
}
