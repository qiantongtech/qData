import request from '@/utils/request';

// Query data source list
export function listDaDatasource(query) {
    return request({
        url: '/da/dataSource/list',
        method: 'get',
        params: query
    });
}

export function daList(query) {
    return request({
        url: '/mc/list',
        method: 'get',
        params: query
    });
}
// Query data source list
export function sqlParse(data) {
    return request({
        url: '/mc/dataSource/sqlParse',
        method: 'post',
        data: data
    });
}

// Kafka is excluded from data integration and is the data source list of the current project
export function listDaDatasourceNoKafkaByProjectCode(query) {
    return request({
        url: '/mc/dataSource/dppNoKafka/list',
        method: 'get',
        params: query
    });
}

// Query the data source list of the R&D module
export function listDaDatasourceByProjectCode(query) {
    return request({
        url: '/mc/dataSource/dpp/list',
        method: 'get',
        params: query
    });
}

// Query the data source list of the R&D module
export function noDppAdd(query) {
    return request({
        url: '/mc/dataSource/noDppAdd/list',
        method: 'get',
        params: query
    });
}

// Query data source list
export function getDaDatasourceList(query) {
    return request({
        url: '/mc/dataSource/getDataSourceByAsset',
        method: 'get',
        params: query
    });
}

// Query data source details
export function getDaDatasource(id) {
    return request({
        url: '/mc/dataSource/' + id,
        method: 'get'
    });
}



// Query data source details
export function clientsTest(id) {
    return request({
        url: '/mc/dataSource/clientsTest/' + id,
        method: 'get'
    });
}
// Add new data source
export function addDaDatasource(data) {
    return request({
        url: '/mc/dataSource',
        method: 'post',
        data: data
    });
}

// Modify data source
export function updateDaDatasource(data) {
    return request({
        url: '/mc/dataSource',
        method: 'put',
        data: data
    });
}

// Delete data source
export function delDaDatasource(id) {
    return request({
        url: '/mc/dataSource/' + id,
        method: 'delete'
    });
}

// Delete data source
export function removeDppOrDa(id, type) {
    return request({
        url: '/mc/dataSource/' + id + '/' + type,
        method: 'delete'
    });
}

// Modify status
export function editDatasourceStatus(id, status) {
    return request({
        url: `/mc/dataSource/editDatasourceStatus/${id}/${status}`,
        method: 'get'
    });
}

export function tableList(id) {
    return request({
        url: `/mc/dataSource/tableList/${id}`,
        method: 'get',
    });
}

// table fields
export function columnsList(data) {
    return request({
        url: `/mc/dataSource/columnsList`,
        method: 'post',
        data: data
    });
}

// Get table information based on id
export function getTablesByDataSourceId(query) {
    return request({
        url: '/mc/asset/getTablesByDataSourceId',
        method: 'get',
        params: query
    });
}

// Get table information based on id
export function getColumnByAssetId(query) {
    return request({
        url: '/mc/assetColumn/getColumnByAssetId',
        method: 'get',
        params: query
    });
}

// Query the data source list of the R&D module
export function executeSqlQuery(query) {
    return request({
        url: '/mc/dataSource/executeSqlQuery',
        method: 'get',
        params: query
    });
}
// Data asset asset application list
export function getDaAssetRespList(query) {
    return request({
        url: '/mc/asset/getDaAssetRespList',
        method: 'get',
        params: query
    });
}
// Query data source details
export function clientsTestStatus(data) {
    return request({
        url: '/mc/dataSource/clientsTestStatus',
        method: 'post',
        data: data
    });
}
