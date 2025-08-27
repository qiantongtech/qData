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
export function pageListByIds(query) {
    return request({
        url: '/da/daAsset/listAssetTag',
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

// 新增数据资产
export function bindResources(data) {
    return request({
        url: '/da/daAsset/bindResources',
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

// 更新数据接口
export function startDaDiscoveryTask(data) {
    return request({
        url: `/da/daAsset/startDaDiscoveryTask`,
        method: 'put',
        data: data
    });
}

// 文件列表
export function getFileList(query) {
    return request({
        url: '/da/daDatasource/fileList',
        method: 'get',
        params: query
    });
}

// 文件列表
export function uploadFile(query) {
    return request({
        url: '/da/daDatasource/file',
        method: 'post',
        params: query
    });
}


