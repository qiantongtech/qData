import request from '@/utils/request.js';

// 查询表元数据列表
export function listTable(query) {
    return request({
        url: '/mc/table/list',
        method: 'get',
        params: query
    });
}

// 查询表元数据详细
export function getTable(id) {
    return request({
        url: '/mc/table/' + id,
        method: 'get'
    });
}

// 新增表元数据
export function addTable(data) {
    return request({
        url: '/mc/table',
        method: 'post',
        data: data
    });
}

// 修改表元数据
export function updateTable(data) {
    return request({
        url: '/mc/table',
        method: 'put',
        data: data
    });
}

// 删除表元数据
export function delTable(id) {
    return request({
        url: '/mc/table/' + id,
        method: 'delete'
    });
}

// 修改表元数据状态
export function updateTableStatus(data) {
    return request({
        url: '/mc/table/toggle',
        method: 'post',
        data
    });
}

// 暂存表元数据
export function draftTable(data) {
    return request({
        url: '/mc/table/draft',
        method: 'post',
        data: data
    });
}

// 获取表元数据可删除的列
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/table/batchDeleteCheck/' + id,
        method: 'get'
    });
}

