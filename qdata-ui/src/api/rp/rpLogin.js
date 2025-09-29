import request from "@/utils/rpRequest";

// 查询服务资源门户区域字典列表
export function rpLogin(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid,
  };
  return request({
    url: "/rp/login",
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: "post",
    data: data,
  });
}
// 获取用户详细信息
export function rpGetInfo() {
  return request({
    url: "/rp/getInfo",
    method: "get",
  });
}

// 退出方法
export function rpLogout() {
  return request({
    url: "/rp/logout",
    method: "post",
  });
}
// 获取验证码
export function rpCodeImg() {
  return request({
    url: "/rp/captchaImage",
    headers: {
      isToken: false,
    },
    method: "get",
    timeout: 20000,
  });
}
