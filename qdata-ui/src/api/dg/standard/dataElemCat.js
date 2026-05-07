import request from '@/utils/request.js'

// 查询标准数据元类目管理列表
export function listElemCat(query) {
    return request({
        url: '/dg/dataElemCat/list',
        method: 'get',
        params: query
    })
}

// 查询标准数据元类目管理详细
export function getElemCat(id) {
    return request({
        url: '/dg/dataElemCat/' + id,
        method: 'get'
    })
}

// 新增标准数据元类目管理
export function addElemCat(data) {
    return request({
        url: '/dg/dataElemCat',
        method: 'post',
        data: data
    })
}

// 修改标准数据元类目管理
export function updateElemCat(data) {
    return request({
        url: '/dg/dataElemCat',
        method: 'put',
        data: data
    })
}

// 删除标准数据元类目管理
export function delElemCat(id) {
    return request({
        url: '/dg/dataElemCat/' + id,
        method: 'delete'
    })
}
// 数据元分类 批量删除校验
export function batchDeleteCheck(ids) {
    return request({
        url: '/dg/dataElemCat/batchDeleteCheck/' + ids,
        method: 'get'
    })
}