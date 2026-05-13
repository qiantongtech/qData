import request from '@/utils/request.js';

// 查询元数据列表
export function listMeta(query) {
    return request({
        url: '/mc/unreleased/db/selectMetaSearchPage',
        method: 'get',
        params: query
    });
}
