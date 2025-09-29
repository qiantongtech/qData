import request from '@/utils/request'

// 查询标准信息登记列表
export function listDpDocument(query) {
    return request({
        url: '/dp/document/list',
        method: 'get',
        params: query
    })
}

// 查询标准信息登记详细
export function getDpDocument(ID) {
    return request({
        url: '/dp/document/' + ID,
        method: 'get'
    })
}

// 新增标准信息登记
export function addDpDocument(data) {
    return request({
        url: '/dp/document',
        method: 'post',
        data: data
    })
}

// 修改标准信息登记
export function updateDpDocument(data) {
    return request({
        url: '/dp/document',
        method: 'put',
        data: data
    })
}

// 删除标准信息登记
export function delDpDocument(ID) {
    return request({
        url: '/dp/document/' + ID,
        method: 'delete'
    })
}

export function listAttDocumentCat(query) {
    return request({
        url: 'att/documentCat/getAttDocumentCatList',
        method: 'get',
        params: query
    })
}