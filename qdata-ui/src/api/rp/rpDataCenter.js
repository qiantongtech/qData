import request from "@/utils/rpRequest";

// 资料类型
export function catApiList(query) {
  return request({
    url: "/rp/attDoc/catApi/list",
    method: "get",
    params: query,
  });
}
// 左侧树
export function rpDictList(query) {
  return request({
    url: `/rp/rpDict/list`,
    method: "get",
    params: query,
  });
}

// 列表
export function rpDocList(query) {
  return request({
    url: "/rp/rpDoc/list",
    method: "get",
    params: query,
  });
}

// 新增
export function rpDocAdd(data) {
  return request({
    url: "/rp/rpDoc",
    method: "post",
    data: data,
  });
}
// 删除
export function rpDocDel(id) {
  return request({
    url: "/rp/rpDoc/" + id,
    method: "delete",
  });
}
// 字典
export function dictDataList(dictType) {
  return request({
    url: `/rp/dictData/type/${dictType}`,
    method: "get",
  });
}
