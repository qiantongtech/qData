import request from '@/utils/rpRequest';

// 左侧树
export function catApiList(query) {
    return request({
        url: '/rp/attApi/catApi/list',
        method: 'get',
        params: query
    });
}

// 列表
export function dsApiList(query) {
    return request({
        url: '/rp/dsApi/list',
        method: 'get',
        params: query
    });
}

// 列表详情
export function dsApiView(id) {
    return request({
        url: '/rp/dsApi/' + id,
        method: 'get'
    });
}

// 查询个人中心我的申请统计项
export function applyCount() {
    return request({
        url: '/rp/dsApi/applyCount',
        method: 'get'
    });
}

// 可申请的API列表
export function areaDict(data) {
    return request({
        url: '/rp/rpDict/areaDict',
        method: 'post',
        data: data
    });
}

export function applyAdd(data) {
    return request({
        url: '/da/apply',
        method: 'post',
        data: data
    });
}

export function applyEdit(data) {
    return request({
        url: '/da/apply',
        method: 'put',
        data: data
    });
}

// 查询API申请服务详细
export function getDsApiApply(id) {
    return request({
        url: '/da/apply/' + id,
        method: 'get'
    });
}

// 删除API申请服务
export function applyDel(id) {
    return request({
        url: '/da/apply/' + id,
        method: 'delete'
    });
}
