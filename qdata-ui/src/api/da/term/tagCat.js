
import request from '@/utils/request.js'

// 查询术语标签类目管理列表
export function listAttTagCat(query) {
    return request({
        url: '/da/termCat/list',
        method: 'get',
        params: query
    })
}

// 查询术语标签类目管理详细
export function getAttTagCat(id) {
    return request({
        url: '/da/termCat/' + id,
        method: 'get'
    })
}

// 新增术语标签类目管理
export function addAttTagCat(data) {
    return request({
        url: '/da/termCat',
        method: 'post',
        data: data
    })
}

// 修改术语标签类目管理
export function updateAttTagCat(data) {
    return request({
        url: '/da/termCat',
        method: 'put',
        data: data
    })
}

// 删除术语标签类目管理
export function delAttTagCat(id) {
    return request({
        url: '/da/termCat/' + id,
        method: 'delete'
    })
}
