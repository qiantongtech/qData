import request from '@/utils/request.js';

// 查询敏感等级列表
export function listDgSensitiveLevel(query) {
    return request({
        url: '/dg/sensitiveLevel/list',
        method: 'get',
        params: query
    });
}

// 查询敏感等级详细
export function getDgSensitiveLevel(id) {
    return request({
        url: '/dg/sensitiveLevel/' + id,
        method: 'get'
    });
}

// 新增敏感等级
export function addDgSensitiveLevel(data) {
    return request({
        url: '/dg/sensitiveLevel',
        method: 'post',
        data: data
    });
}

// 修改状态 上线/下线
export function updateStatus(id, status) {
    return request({
        url: `/dg/sensitiveLevel/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// 修改敏感等级
export function updateDgSensitiveLevel(data) {
    return request({
        url: '/dg/sensitiveLevel',
        method: 'put',
        data: data
    });
}

// 删除敏感等级
export function delDgSensitiveLevel(id) {
    return request({
        url: '/dg/sensitiveLevel/' + id,
        method: 'delete'
    });
}
