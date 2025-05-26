import request from '@/utils/request';

// 查询逻辑模型列表
export function listDpModel(query) {
    return request({
        url: '/dp/dpModel/list',
        method: 'get',
        params: query
    });
}

// 查询逻辑模型详细
export function getDpModel(ID) {
    return request({
        url: '/dp/dpModel/' + ID,
        method: 'get'
    });
}

// 新增逻辑模型
export function addDpModel(data) {
    return request({
        url: '/dp/dpModel',
        method: 'post',
        data: data
    });
}
// 新增逻辑模型
export function dpModelColumn(data) {
    return request({
        url: '/dp/dpModelColumn/addList',
        method: 'post',
        data: data
    });
}
// 新增逻辑模型
export function updateDpModel(data) {
    return request({
        url: '/dp/dpModel',
        method: 'put',
        data: data
    });
}
// 修改逻辑模型
// export function updateDpModel(data) {
//   return request({
//     url: '/dp/dpModelColumn/editList',
//     method: 'put',
//     data: data
//   })
// }

// 删除逻辑模型
export function delDpModel(ID) {
    return request({
        url: '/dp/dpModel/' + ID,
        method: 'delete'
    });
}

// 删除逻辑模型字段
export function delDpModelColumn(ID) {
    return request({
        url: '/dp/dpModel/columnAll/' + ID,
        method: 'delete'
    });
}
// 查询逻辑模型类目管理列表
export function listAttModelCat(query) {
    return request({
        url: '/att/attModelCat/list',
        method: 'get',
        params: query
    });
}
// 获取 表信息
export function getDpModelColumnList(query) {
    return request({
        url: '/dp/dpModelColumn/getDpModelColumnList',
        method: 'get',
        params: query
    });
}
// 修改逻辑模型
export function updateDpModelColumn(data) {
    return request({
        url: '/dp/dpModelColumn/editList',
        method: 'put',
        data: data
    });
}
// 物化
export function createMaterializedTable(data) {
    return request({
        url: '/dp/dpModelMaterialized/createMaterializedTable',
        method: 'post',
        data: data
    });
}
// 数据库连接
export function getDaDatasourceList(query) {
    return request({
        url: '/da/daDatasource/getDaDatasourceList',
        method: 'get',
        params: query
    });
}
// 修改状态
export function updateStatusDpDataModel(id, status) {
    return request({
        url: `/dp/dpModel/updateStatus/${id}/${status}`,
        method: 'post'
    });
}
//表 
export function tableList(ID) {
    console.log("🚀 ~ tableList ~ ID:", ID)
    return request({
        url: '/da/daDatasource/tableList/' + ID,
        method: 'get'
    });
}
// 表字段
export function columnsList(data) {
    return request({
        url: `/da/daDatasource/columnsList`,
        method: 'post',
        data: data
    });
}