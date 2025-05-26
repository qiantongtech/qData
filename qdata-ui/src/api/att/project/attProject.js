import request from '@/utils/request';

// 查询项目列表
export function listAttProject(query) {
    return request({
        url: '/att/attProject/list',
        method: 'get',
        params: query
    });
}

// 查询当前用户所属的项目列表
export function currentUser() {
    return request({
        url: '/att/attProject/currentUser/list',
        method: 'get'
    });
}

// 查询当前用户所属的项目列表
export function noProjectUser(data) {
    return request({
        url: '/att/attProject/noProjectUser/list',
        method: 'post',
        data: data
    });
}

// 查询项目详细
export function getAttProject(id) {
    return request({
        url: '/att/attProject/' + id,
        method: 'get'
    });
}

// 获取当前用户是非具备用户添加和项目管理员
export function addUserAndProject(id) {
    return request({
        url: '/att/attProject/addUserAndProject/' + id,
        method: 'get'
    });
}

// 修改状态
export function editProjectStatus(id, status) {
    return request({
        url: `/att/attProject/editProjectStatus/${id}/${status}`,
        method: 'get'
    });
}

// 新增项目
export function addAttProject(data) {
    return request({
        url: '/att/attProject',
        method: 'post',
        data: data
    });
}

// 修改项目
export function updateAttProject(data) {
    return request({
        url: '/att/attProject',
        method: 'put',
        data: data
    });
}

// 删除项目
export function delAttProject(id) {
    return request({
        url: '/att/attProject/' + id,
        method: 'delete'
    });
}
