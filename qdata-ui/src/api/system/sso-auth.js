import request from '@/utils/request.js'

// 使用 code 登录
export function codeLogin(code) {
    return request({
        url: '/oauth2/codeLogin?code=' + code,
        headers: {
            isToken: false
        },
        method: 'post'
    })
}

// 退出登录
export function loginOut(userId) {
    return request({
        url: '/oauth2/sso/logout?userId=' + userId,
        method: 'post'
    })
}
