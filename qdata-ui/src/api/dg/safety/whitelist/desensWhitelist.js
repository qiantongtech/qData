import request from "@/utils/request";

export function listDesensWhitelist(query) {
  return request({
    url: "/dg/desensWhitelist/list",
    method: "get",
    params: query,
  });
}

export function getDesensWhitelist(id) {
  return request({
    url: "/dg/desensWhitelist/" + id,
    method: "get",
  });
}

export function addDesensWhitelist(data) {
  return request({
    url: "/dg/desensWhitelist",
    method: "post",
    data,
  });
}

export function updateDesensWhitelist(data) {
  return request({
    url: "/dg/desensWhitelist",
    method: "put",
    data,
  });
}

export function delDesensWhitelist(id) {
  return request({
    url: "/dg/desensWhitelist/" + id,
    method: "delete",
  });
}
