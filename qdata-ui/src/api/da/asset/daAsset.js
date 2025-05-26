import request from '@/utils/request';

// 查询数据资产列表
export function listDaAsset(query) {
    return request({
        url: '/da/daAsset/list',
        method: 'get',
        params: query
    });
}

// 查询数据资产列表
export function listDppAsset(query) {
    return request({
        url: '/da/daAsset/dpp/list',
        method: 'get',
        params: query
    });
}

// 查询数据资产列表
export function dppNoPageList(query) {
    return request({
        url: '/da/daAsset/dpp/noPage/list',
        method: 'get',
        params: query
    });
}

// 查询数据资产详细
export function getDaAsset(id) {
    return request({
        url: '/da/daAsset/' + id,
        method: 'get'
    });
}

// 新增数据资产
export function addDaAsset(data) {
    return request({
        url: '/da/daAsset',
        method: 'post',
        data: data
    });
}

// 修改数据资产
export function updateDaAsset(data) {
    return request({
        url: '/da/daAsset',
        method: 'put',
        data: data
    });
}

// 删除数据资产
export function delDaAsset(id) {
    return request({
        url: '/da/daAsset/' + id,
        method: 'delete'
    });
}
