import request from "@/utils/request";

export function listDesensitizationRules(query) {
  return request({
    url: "/dg/safety/desensitizationRules/list",
    method: "get",
    params: query,
  });
}

export function getDesensitizationRule(id) {
  return request({
    url: "/dg/safety/desensitizationRules/" + id,
    method: "get",
  });
}

export function addDesensitizationRule(data) {
  return request({
    url: "/dg/safety/desensitizationRules",
    method: "post",
    data,
  });
}

export function updateDesensitizationRule(data) {
  return request({
    url: "/dg/safety/desensitizationRules",
    method: "put",
    data,
  });
}

export function delDesensitizationRule(id) {
  return request({
    url: "/dg/safety/desensitizationRules/" + id,
    method: "delete",
  });
}

