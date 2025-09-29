import request from '@/utils/rpRequest';

// 查询服务资源门户部门列表
export function applyCount(userId) {
    return request({
        url: '/rp/dsApi/applyCount/' + userId,
        method: 'get'
    });
}

export function flyfowApiApply(param) {
    return request({
        url: '/da/apply/flyfowApiApply',
        method: 'post',
        data: param
    });
}
