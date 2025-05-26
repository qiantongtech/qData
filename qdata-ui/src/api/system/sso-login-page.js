import request from '@/utils/sso-request.js'

// 统一身份认证登录方法
export function ssoLoginPage(name, pwd, rememberMe) {
  const data = {
    "name": name,
    "pwd": pwd,
    "rememberMe": rememberMe
  }
  return request({
    url: '/oauth2/doLogin',
    method: 'post',
    params: data
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
