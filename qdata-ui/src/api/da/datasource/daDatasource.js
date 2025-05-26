import request from '@/utils/request';

// 查询数据源列表
export function listDaDatasource(query) {
    return request({
        url: '/da/daDatasource/list',
        method: 'get',
        params: query
    });
}

export function daList(query) {
    return request({
        url: '/da/list',
        method: 'get',
        params: query
    });
}
// 查询数据源列表
export function sqlParse(data) {
    return request({
        url: '/da/daDatasource/sqlParse',
        method: 'post',
        data: data
    });
}

// 数据集成中排除Kafka并且是当前项目的数据源列表
export function listDaDatasourceNoKafkaByProjectCode(query) {
    return request({
        url: '/da/daDatasource/dppNoKafka/list',
        method: 'get',
        params: query
    });
}

// 查询研发模块的数据源列表
export function listDaDatasourceByProjectCode(query) {
    return request({
        url: '/da/daDatasource/dpp/list',
        method: 'get',
        params: query
    });
}

// 查询研发模块的数据源列表
export function noDppAdd(query) {
    return request({
        url: '/da/daDatasource/noDppAdd/list',
        method: 'get',
        params: query
    });
}

// 查询数据源列表
export function getDaDatasourceList(query) {
    return request({
        url: '/da/daDatasource/getDataSourceByAsset',
        method: 'get',
        params: query
    });
}

// 查询数据源详细
export function getDaDatasource(id) {
    return request({
        url: '/da/daDatasource/' + id,
        method: 'get'
    });
}

// 查询数据源详细
export function clientsTest(id) {
    return request({
        url: '/da/daDatasource/clientsTest/' + id,
        method: 'get'
    });
}

// 新增数据源
export function addDaDatasource(data) {
    return request({
        url: '/da/daDatasource',
        method: 'post',
        data: data
    });
}

// 修改数据源
export function updateDaDatasource(data) {
    return request({
        url: '/da/daDatasource',
        method: 'put',
        data: data
    });
}

// 删除数据源
export function delDaDatasource(id) {
    return request({
        url: '/da/daDatasource/' + id,
        method: 'delete'
    });
}

// 删除数据源
export function removeDppOrDa(id, type) {
    return request({
        url: '/da/daDatasource/' + id + '/' + type,
        method: 'delete'
    });
}

// 根据id获取表信息
export function getTablesByDataSourceId(query) {
    return request({
        url: '/da/daAsset/getTablesByDataSourceId',
        method: 'get',
        params: query
    });
}

// 根据id获取表信息
export function getColumnByAssetId(query) {
    return request({
        url: '/da/daAssetColumn/getColumnByAssetId',
        method: 'get',
        params: query
    });
}

// 查询研发模块的数据源列表
export function executeSqlQuery(query) {
    return request({
        url: '/da/daDatasource/executeSqlQuery',
        method: 'get',
        params: query
    });
}
// 数据资产资产申请列表
export function getDaAssetRespList(query) {
    return request({
        url: '/da/daAsset/getDaAssetRespList',
        method: 'get',
        params: query
    });
}