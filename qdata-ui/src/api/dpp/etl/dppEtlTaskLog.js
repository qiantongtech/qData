import request from '@/utils/request';

// 查询数据集成任务-日志列表
export function listDppEtlTaskLog(query) {
    return request({
        url: '/dpp/dppEtlTaskLog/list',
        method: 'get',
        params: query
    });
}

// 查询数据集成任务-日志详细
export function getDppEtlTaskLog(id) {
    return request({
        url: '/dpp/dppEtlTaskLog/' + id,
        method: 'get'
    });
}

// 新增数据集成任务-日志
export function addDppEtlTaskLog(data) {
    return request({
        url: '/dpp/dppEtlTaskLog',
        method: 'post',
        data: data
    });
}

// 修改数据集成任务-日志
export function updateDppEtlTaskLog(data) {
    return request({
        url: '/dpp/dppEtlTaskLog',
        method: 'put',
        data: data
    });
}

// 删除数据集成任务-日志
export function delDppEtlTaskLog(id) {
    return request({
        url: '/dpp/dppEtlTaskLog/' + id,
        method: 'delete'
    });
}
