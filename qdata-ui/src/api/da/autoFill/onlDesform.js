import request from "@/utils/request";

/**
 * 分页列表
 * @param params
 * @returns {AxiosPromise}
 */
export function pageData(params) {
  return request({
    url: "/data/masterdata/onlDesform/page",
    method: "get",
    params,
  });
}

/**
 * 添加
 * @param data
 * @returns {AxiosPromise}
 */
export function add(data) {
  return request({
    url: "/data/masterdata/onlDesform/add",
    method: "post",
    data,
  });
}

/**
 * 编辑
 * @param data
 * @returns {AxiosPromise}
 */
export function edit(data) {
  return request({
    url: "/data/masterdata/onlDesform/edit",
    method: "post",
    data,
  });
}

/**
 * 删除
 * @param params
 * @returns {AxiosPromise}
 */
export function deleted(params) {
  return request({
    url: "/data/masterdata/onlDesform/deleted",
    method: "post",
    params,
  });
}

/**
 * 通过表单编码查询
 * @param params
 * @returns {AxiosPromise}
 */
export function getByDesformCode(desformCode) {
  return request({
    url: "/data/masterdata/onlDesform/" + desformCode,
    method: "get",
  });
}

export function duplicateCheck(params) {
  return request({
    url: "/data/masterdata/onlDesform/duplicateCheck",
    method: "get",
    params,
  });
}
