import request from '@/utils/request';

// 查询主题列表
export function listAttTheme(query) {
    return request({
        url: '/att/attTheme/list',
        method: 'get',
        params: query
    });
}

// 查询主题详细
export function getAttTheme(id) {
    return request({
        url: '/att/attTheme/' + id,
        method: 'get'
    });
}

// 新增主题
export function addAttTheme(data) {
    return request({
        url: '/att/attTheme',
        method: 'post',
        data: data
    });
}

// 修改主题
export function updateAttTheme(data) {
    return request({
        url: '/att/attTheme',
        method: 'put',
        data: data
    });
}

// 删除主题
export function delAttTheme(id) {
    return request({
        url: '/att/attTheme/' + id,
        method: 'delete'
    });
}
// 获取主题的查询接口
export function getThemeList(query) {
    return request({
        url: '/att/attTheme/getAttThemeListByReqVO',
        method: 'get',
        params: query
    });
}
