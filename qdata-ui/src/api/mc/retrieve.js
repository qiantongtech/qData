import request from '@/utils/request.js';

// Query metadata list
export function listMeta(query) {
    return request({
        url: '/mc/unreleased/db/selectMetaSearchPage',
        method: 'get',
        params: query
    });
}
