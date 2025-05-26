import request from '@/utils/request.js';

// 获取路由
export const getRouters = () => {
    return request({
        url: '/getRouters',
        method: 'get'
    });
};

// 获取路由
export const getRoutersDpp = (id) => {
    return request({
        url: '/getRoutersDpp/' + id,
        method: 'get'
    });
};
