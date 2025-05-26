import request from '@/utils/request';

// 查询项目与用户关联关系列表
export function listAttProjectUserRel(query) {
    return request({
        url: '/att/AttProjectUserRel/list',
        method: 'get',
        params: query
    });
}

// 查询项目与用户关联关系详细
export function getAttProjectUserRel(id) {
    return request({
        url: '/att/AttProjectUserRel/' + id,
        method: 'get'
    });
}

// 查询项目与用户关联关系详细
export function getRoleUser(id) {
    return request({
        url: '/att/AttProjectUserRel/roleUser/' + id,
        method: 'get'
    });
}

// 新增项目与用户关联关系
export function addAttProjectUserRel(data) {
    return request({
        url: '/att/AttProjectUserRel',
        method: 'post',
        data: data
    });
}

// 新增项目与用户关联关系
export function addUserListAndRoleList(data) {
    return request({
        url: '/att/AttProjectUserRel/addUserListAndRoleList',
        method: 'post',
        data: data
    });
}

// 修改项目与用户关联关系
export function updateAttProjectUserRel(data) {
    return request({
        url: '/att/AttProjectUserRel',
        method: 'put',
        data: data
    });
}

// 修改项目与用户关联关系
export function editUserListAndRoleList(data) {
    return request({
        url: '/att/AttProjectUserRel/editUserListAndRoleList',
        method: 'put',
        data: data
    });
}

// 删除项目与用户关联关系
export function delAttProjectUserRel(id) {
    return request({
        url: '/att/AttProjectUserRel/' + id,
        method: 'delete'
    });
}

// 查询角色列表
export function listRole(query) {
    return request({
        url: '/att/AttProjectUserRel/role/list',
        method: 'get',
        params: query
    });
}

// 查询角色详细
export function getRole(roleId) {
    return request({
        url: '/att/AttProjectUserRel/role/' + roleId,
        method: 'get'
    });
}

// 新增角色
export function addRole(data) {
    return request({
        url: '/att/AttProjectUserRel/role',
        method: 'post',
        data: data
    });
}

// 修改角色
export function updateRole(data) {
    return request({
        url: '/att/AttProjectUserRel/role',
        method: 'put',
        data: data
    });
}

// 角色数据权限
export function dataScope(data) {
    return request({
        url: '/att/AttProjectUserRel/role/dataScope',
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
        url: '/att/AttProjectUserRel/role/changeStatus',
        method: 'put',
        data: data
    });
}

// 删除角色
export function delRole(roleId) {
    return request({
        url: '/att/AttProjectUserRel/role/' + roleId,
        method: 'delete'
    });
}

// 查询角色已授权用户列表
export function allocatedUserList(query) {
    return request({
        url: '/att/AttProjectUserRel/role/authUser/allocatedList',
        method: 'get',
        params: query
    });
}

// 查询角色未授权用户列表
export function unallocatedUserList(query) {
    return request({
        url: '/att/AttProjectUserRel/role/authUser/unallocatedList',
        method: 'get',
        params: query
    });
}

// 取消用户授权角色
export function authUserCancel(data) {
    return request({
        url: '/att/AttProjectUserRel/role/authUser/cancel',
        method: 'put',
        data: data
    });
}

// 批量取消用户授权角色
export function authUserCancelAll(data) {
    return request({
        url: '/att/AttProjectUserRel/role/authUser/cancelAll',
        method: 'put',
        params: data
    });
}

// 授权用户选择
export function authUserSelectAll(data) {
    return request({
        url: '/att/AttProjectUserRel/role/authUser/selectAll',
        method: 'put',
        params: data
    });
}

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId) {
    return request({
        url: '/att/AttProjectUserRel/role/deptTree/' + roleId,
        method: 'get'
    });
}
