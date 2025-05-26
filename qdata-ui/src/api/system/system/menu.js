import request from '@/utils/request.js';

// 查询菜单列表
export function listMenu(query) {
    return request({
        url: '/system/menu/list',
        method: 'get',
        params: query
    });
}

// 查询菜单详细
export function getMenu(menuId) {
    return request({
        url: '/system/menu/' + menuId,
        method: 'get'
    });
}

// 查询菜单下拉树结构
export function treeselect() {
    return request({
        url: '/system/menu/treeselect',
        method: 'get'
    });
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselect/' + roleId,
        method: 'get'
    });
}

// 查询菜单下拉树结构(只限于数据研发模块)
export function treeselectDpp() {
    return request({
        url: '/system/menu/treeselectDpp',
        method: 'get'
    });
}

// 根据角色ID查询菜单下拉树结构(只限于数据研发模块)
export function roleMenuTreeselectDpp(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselectDpp/' + roleId,
        method: 'get'
    });
}

// 查询菜单下拉树结构(排除数据研发模块)
export function treeselectNoDpp() {
    return request({
        url: '/system/menu/treeselectNoDpp',
        method: 'get'
    });
}

// 根据角色ID查询菜单下拉树结构(排除数据研发模块)
export function roleMenuTreeselectNoDpp(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselectNoDpp/' + roleId,
        method: 'get'
    });
}

// 新增菜单
export function addMenu(data) {
    return request({
        url: '/system/menu',
        method: 'post',
        data: data
    });
}

// 修改菜单
export function updateMenu(data) {
    return request({
        url: '/system/menu',
        method: 'put',
        data: data
    });
}

// 删除菜单
export function delMenu(menuId) {
    return request({
        url: '/system/menu/' + menuId,
        method: 'delete'
    });
}
