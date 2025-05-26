import request from '@/utils/request';

// 查询数据元代码映射列表
export function listDpCodeMap(query) {
    return request({
        url: '/dp/dpCodeMap/list',
        method: 'get',
        params: query
    });
}

// 查询数据元代码映射详细
export function getDpCodeMap(id) {
    return request({
        url: '/dp/dpCodeMap/' + id,
        method: 'get'
    });
}

// 新增数据元代码映射
export function addDpCodeMap(data) {
    return request({
        url: '/dp/dpCodeMap',
        method: 'post',
        data: data
    });
}

// 修改数据元代码映射
export function updateDpCodeMap(data) {
    return request({
        url: '/dp/dpCodeMap',
        method: 'put',
        data: data
    });
}

// 删除数据元代码映射
export function delDpCodeMap(id) {
    return request({
        url: '/dp/dpCodeMap/' + id,
        method: 'delete'
    });
}
