import request from '@/utils/request';

// 查询数据发现任务列表
export function listDaDiscoveryTask(query) {
    return request({
        url: '/da/discoveryTask/getDaDiscoveryTaskListPage',
        method: 'get',
        params: query
    });
}

// 查询数据发现任务详细
export function getDaDiscoveryTask(id) {
    return request({
        url: '/da/discoveryTask/' + id,
        method: 'get'
    });
}

// 新增数据发现任务
export function addDaDiscoveryTask(data) {
    return request({
        url: '/da/discoveryTask',
        method: 'post',
        data: data
    });
}

// 修改数据发现任务
export function updateDaDiscoveryTask(data) {
    return request({
        url: '/da/discoveryTask',
        method: 'put',
        data: data
    });
}
// 修改数据发现任务
export function updateDaDiscoveryTaskStatus(data) {
    return request({
        url: '/da/discoveryTask/updateDaDiscoveryTaskStatus',
        method: 'post',
        data: data
    });
}
// 修改数据发现任务
export function updateDaDiscoveryTaskCronExpression(data) {
    return request({
        url: '/da/discoveryTask/updateDaDiscoveryTaskCronExpression',
        method: 'post',
        data: data
    });
}

// 删除数据发现任务
export function delDaDiscoveryTask(id) {
    return request({
        url: '/da/discoveryTask/' + id,
        method: 'delete'
    });
}
// 查询调度日志列表
export function listJobLog(query) {
    return request({
        url: '/da/discoveryTask/jobLog/list',
        method: 'get',
        params: query
    });
}

// 数据发现 执行一次
export function startDppEtlTask(data) {
    return request({
        url: '/da/discoveryTask/startDaDiscoveryTask/' + data,
        method: 'put',
    })
}
