import request from '@/utils/request.js';

// 查询库元数据列表
export function listDb(query) {
    return request({
        url: '/mc/db/list',
        method: 'get',
        params: query
    });
}

// 查询库元数据详细
export function getDb(id) {
    return request({
        url: '/mc/db/' + id,
        method: 'get'
    });
}

// 新增库元数据
export function addDb(data) {
    return request({
        url: '/mc/db',
        method: 'post',
        data: data
    });
}

// 修改库元数据
export function updateDb(data) {
    return request({
        url: '/mc/db',
        method: 'put',
        data: data
    });
}

// 删除库元数据
export function delDb(id) {
    return request({
        url: '/mc/unreleased/db/' + id,
        method: 'delete'
    });
}

// 修改库元数据状态
export function updateDbStatus(data) {
    return request({
        url: '/mc/unreleased/db/toggle',
        method: 'post',
        data
    });
}

// 获取库元数据可删除的列
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/unreleased/db/batchDeleteCheck/' + id,
        method: 'get'
    });
}

