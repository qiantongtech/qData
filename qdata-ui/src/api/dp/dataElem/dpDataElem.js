import request from '@/utils/request';

// 查询数据元列表
export function listDpDataElem(query) {
    return request({
        url: '/dp/dpDataElem/list',
        method: 'get',
        params: query
    });
}

// 查询数据元列表
export function getDpDataElemList(query) {
    return request({
        url: '/dp/dpDataElem/getDpDataElemList',
        method: 'get',
        params: query
    });
}

// 查询数据元详细
export function getDpDataElem(id) {
    return request({
        url: '/dp/dpDataElem/' + id,
        method: 'get'
    });
}

// 新增数据元
export function addDpDataElem(data) {
    return request({
        url: '/dp/dpDataElem',
        method: 'post',
        data: data
    });
}

// 修改数据元
export function updateDpDataElem(data) {
    return request({
        url: '/dp/dpDataElem',
        method: 'put',
        data: data
    });
}
// 修改数据元
export function updateStatusDpDataElem(id, status) {
    return request({
        url: `/dp/dpDataElem/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// 删除数据元
export function delDpDataElem(id) {
    return request({
        url: '/dp/dpDataElem/' + id,
        method: 'delete'
    });
}
