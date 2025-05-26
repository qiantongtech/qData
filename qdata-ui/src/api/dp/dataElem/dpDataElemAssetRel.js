import request from '@/utils/request';

// 查询数据元数据资产关联信息列表
export function listDpDataElemAssetRel(query) {
    return request({
        url: '/dp/dpDataElemAssetRel/list',
        method: 'get',
        params: query
    });
}

// 查询数据元数据资产关联信息详细
export function getDpDataElemAssetRel(id) {
    return request({
        url: '/dp/dpDataElemAssetRel/' + id,
        method: 'get'
    });
}

// 新增数据元数据资产关联信息
export function addDpDataElemAssetRel(data) {
    return request({
        url: '/dp/dpDataElemAssetRel',
        method: 'post',
        data: data
    });
}

// 修改数据元数据资产关联信息
export function updateDpDataElemAssetRel(data) {
    return request({
        url: '/dp/dpDataElemAssetRel',
        method: 'put',
        data: data
    });
}

// 删除数据元数据资产关联信息
export function delDpDataElemAssetRel(id) {
    return request({
        url: '/dp/dpDataElemAssetRel/' + id,
        method: 'delete'
    });
}
