import request from '@/utils/request';

// 查询数据资产列表
export function listDaAsset(query) {
    return request({
        url: '/da/asset/list',
        method: 'get',
        params: query
    });
}

// 查询数据资产列表
export function listDppAsset(query) {
    return request({
        url: '/da/asset/dpp/list',
        method: 'get',
        params: query
    });
}

// 查询数据资产列表
export function dppNoPageList(query) {
    return request({
        url: '/da/asset/dpp/noPage/list',
        method: 'get',
        params: query
    });
}
export function pageListByIds(query) {
    return request({
        url: '/da/asset/listAssetTag',
        method: 'get',
        params: query
    });
}

// 查询数据资产详细
export function getDaAsset(id) {
    return request({
        url: '/da/asset/' + id,
        method: 'get'
    });
}

// 新增数据资产
export function addDaAsset(data) {
    return request({
        url: '/da/asset',
        method: 'post',
        data: data
    });
}

// 新增数据资产
export function bindResources(data) {
    return request({
        url: '/da/asset/bindResources',
        method: 'post',
        data: data
    });
}
// 修改数据资产
export function updateDaAsset(data) {
    return request({
        url: '/da/asset',
        method: 'put',
        data: data
    });
}

// 删除数据资产
export function delDaAsset(id) {
    return request({
        url: '/da/asset/' + id,
        method: 'delete'
    });
}

// 更新数据接口
export function startDaDiscoveryTask(data) {
    return request({
        url: `/da/asset/startDaDiscoveryTask`,
        method: 'put',
        data: data
    });
}

// 文件列表
export function getFileList(query) {
    return request({
        url: '/da/dataSource/fileList',
        method: 'get',
        params: query
    });
}

// 文件列表
export function uploadFile(query) {
    return request({
        url: '/da/dataSource/file',
        method: 'post',
        params: query
    });
}

// 数据血缘
export function dataLineage(id) {
    return request({
        url: '/da/asset/dataLineage/' + id,
        method: 'get',
    });
}

