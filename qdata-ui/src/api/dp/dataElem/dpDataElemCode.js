import request from '@/utils/request';

// 查询数据元代码列表
export function listDpDataElemCode(query) {
    return request({
        url: '/dp/dpDataElemCode/list',
        method: 'get',
        params: query
    });
}

// 查询数据元代码详细
export function getDpDataElemCode(id) {
    return request({
        url: '/dp/dpDataElemCode/' + id,
        method: 'get'
    });
}

// 新增数据元代码
export function addDpDataElemCode(data) {
    return request({
        url: '/dp/dpDataElemCode',
        method: 'post',
        data: data
    });
}

// 修改数据元代码
export function updateDpDataElemCode(data) {
    return request({
        url: '/dp/dpDataElemCode',
        method: 'put',
        data: data
    });
}

// 删除数据元代码
export function delDpDataElemCode(id) {
    return request({
        url: '/dp/dpDataElemCode/' + id,
        method: 'delete'
    });
}

//校验源代码值
export function validateCodeValue(params) {
    return request({
        url: '/dp/dpDataElemCode/validateCodeValue',
        method: 'get',
        params
    });
}

