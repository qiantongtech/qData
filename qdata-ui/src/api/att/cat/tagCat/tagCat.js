import request from '@/utils/request'

// 查询标签类目管理列表
export function listAttTagCat(query) {
    return request({
        url: '/att/tagCat/list',
        method: 'get',
        params: query
    })
}

// 查询标签类目管理详细
export function getAttTagCat(id) {
    return request({
        url: '/att/tagCat/' + id,
        method: 'get'
    })
}

// 新增标签类目管理
export function addAttTagCat(data) {
    return request({
        url: '/att/tagCat',
        method: 'post',
        data: data
    })
}

// 修改标签类目管理
export function updateAttTagCat(data) {
    return request({
        url: '/att/tagCat',
        method: 'put',
        data: data
    })
}

// 删除标签类目管理
export function delAttTagCat(id) {
    return request({
        url: '/att/tagCat/' + id,
        method: 'delete'
    })
}
