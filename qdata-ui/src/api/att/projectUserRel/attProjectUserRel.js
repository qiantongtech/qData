import request from '@/utils/request';

// 查询项目与用户关联关系列表
export function listAttProjectUserRel(query) {
    return request({
        url: '/att/projectUserRel/list',
        method: 'get',
        params: query
    });
}

// 查询项目与用户关联关系详细
export function getAttProjectUserRel(id) {
    return request({
        url: '/att/projectUserRel/' + id,
        method: 'get'
    });
}

// 查询项目与用户关联关系详细
export function getRoleUser(id) {
    return request({
        url: '/att/projectUserRel/roleUser/' + id,
        method: 'get'
    });
}

// 新增项目与用户关联关系
export function addAttProjectUserRel(data) {
    return request({
        url: '/att/projectUserRel',
        method: 'post',
        data: data
    });
}

// 新增项目与用户关联关系
export function addUserListAndRoleList(data) {
    return request({
        url: '/att/projectUserRel/addUserListAndRoleList',
        method: 'post',
        data: data
    });
}

// 修改项目与用户关联关系
export function updateAttProjectUserRel(data) {
    return request({
        url: '/att/projectUserRel',
        method: 'put',
        data: data
    });
}

// 修改项目与用户关联关系
export function editUserListAndRoleList(data) {
    return request({
        url: '/att/projectUserRel/editUserListAndRoleList',
        method: 'put',
        data: data
    });
}

// 删除项目与用户关联关系
export function delAttProjectUserRel(id) {
    return request({
        url: '/att/projectUserRel/' + id,
        method: 'delete'
    });
}

// 查询角色列表
export function listRole(query) {
    return request({
        url: '/att/projectUserRel/role/list',
        method: 'get',
        params: query
    });
}

// 查询角色详细
export function getRole(roleId) {
    return request({
        url: '/att/projectUserRel/role/' + roleId,
        method: 'get'
    });
}

// 新增角色
export function addRole(data) {
    return request({
        url: '/att/projectUserRel/role',
        method: 'post',
        data: data
    });
}

// 修改角色
export function updateRole(data) {
    return request({
        url: '/att/projectUserRel/role',
        method: 'put',
        data: data
    });
}

// 角色数据权限
export function dataScope(data) {
    return request({
        url: '/att/projectUserRel/role/dataScope',
        method: 'put',
        data: data
    });
}

// 角色状态修改
export function changeRoleStatus(roleId, status) {
    const data = {
        roleId,
        status
    };
    return request({
        url: '/att/projectUserRel/role/changeStatus',
        method: 'put',
        data: data
    });
}

// 删除角色
export function delRole(roleId) {
    return request({
        url: '/att/projectUserRel/role/' + roleId,
        method: 'delete'
    });
}

// 查询角色已授权用户列表
export function allocatedUserList(query) {
    return request({
        url: '/att/projectUserRel/role/authUser/allocatedList',
        method: 'get',
        params: query
    });
}

// 查询角色未授权用户列表
export function unallocatedUserList(query) {
    return request({
        url: '/att/projectUserRel/role/authUser/unallocatedList',
        method: 'get',
        params: query
    });
}

// 取消用户授权角色
export function authUserCancel(data) {
    return request({
        url: '/att/projectUserRel/role/authUser/cancel',
        method: 'put',
        data: data
    });
}

// 批量取消用户授权角色
export function authUserCancelAll(data) {
    return request({
        url: '/att/projectUserRel/role/authUser/cancelAll',
        method: 'put',
        params: data
    });
}

// 授权用户选择
export function authUserSelectAll(data) {
    return request({
        url: '/att/projectUserRel/role/authUser/selectAll',
        method: 'put',
        params: data
    });
}

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId) {
    return request({
        url: '/att/projectUserRel/role/deptTree/' + roleId,
        method: 'get'
    });
}
