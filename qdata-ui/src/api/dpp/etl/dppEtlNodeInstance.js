import request from '@/utils/request';

// 查询数据集成节点实例列表
export function listDppEtlNodeInstance(query) {
    return request({
        url: '/dpp/dppEtlNodeInstance/list',
        method: 'get',
        params: query
    });
}

// 查询数据集成节点实例详细
export function getDppEtlNodeInstance(id) {
    return request({
        url: '/dpp/dppEtlNodeInstance/' + id,
        method: 'get'
    });
}

// 新增数据集成节点实例
export function addDppEtlNodeInstance(data) {
    return request({
        url: '/dpp/dppEtlNodeInstance',
        method: 'post',
        data: data
    });
}

// 修改数据集成节点实例
export function updateDppEtlNodeInstance(data) {
    return request({
        url: '/dpp/dppEtlNodeInstance',
        method: 'put',
        data: data
    });
}

// 删除数据集成节点实例
export function delDppEtlNodeInstance(id) {
    return request({
        url: '/dpp/dppEtlNodeInstance/' + id,
        method: 'delete'
    });
}

// 超看日志详情
export function logDetailCat(id) {
    return request({
        url: '/dpp/dppEtlNodeInstance/log/' + id,
        method: 'get'
    });
}
