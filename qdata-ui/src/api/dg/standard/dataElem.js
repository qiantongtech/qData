import request from '@/utils/request';

// Query data element list
export function listDpDataElem(query) {
    return request({
        url: '/dg/dataElem/list',
        method: 'get',
        params: query
    });
}

// Query data element list
export function getDgDataElemList(query) {
    return request({
        url: '/dg/dataElem/getDgDataElemList',
        method: 'get',
        params: query
    });
}

// Query data element details
export function getDpDataElem(id) {
    return request({
        url: '/dg/dataElem/' + id,
        method: 'get'
    });
}

// Add new data element
export function addDpDataElem(data) {
    return request({
        url: '/dg/dataElem',
        method: 'post',
        data: data
    });
}

// Modify data element
export function updateDpDataElem(data) {
    return request({
        url: '/dg/dataElem',
        method: 'put',
        data: data
    });
}
// Modify data element
export function updateStatusDpDataElem(id, status) {
    return request({
        url: `/dg/dataElem/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// delete data element
export function delDpDataElem(id) {
    return request({
        url: '/dg/dataElem/' + id,
        method: 'delete'
    });
}
// Query standard registration (document) list (by type)
export function listDpDocument(query) {
    return request({
        url: '/dg/document/list',
        method: 'get',
        params: query
    });
}
// Query the data element code mapping list
export function listDpCodeMap(query) {
    return request({
        url: '/dg/codeMap/list',
        method: 'get',
        params: query
    });
}

// Query data element code mapping details
export function getDpCodeMap(id) {
    return request({
        url: '/dg/codeMap/' + id,
        method: 'get'
    });
}

// Added data element code mapping
export function addDpCodeMap(data) {
    return request({
        url: '/dg/codeMap',
        method: 'post',
        data: data
    });
}

// Modify data element code mapping
export function updateDpCodeMap(data) {
    return request({
        url: '/dg/codeMap',
        method: 'put',
        data: data
    });
}

// Delete data metacode mapping
export function delDpCodeMap(id) {
    return request({
        url: '/dg/codeMap/' + id,
        method: 'delete'
    });
}
// Query the data metadata asset related information list
export function listDpDataElemAssetRel(query) {
    return request({
        url: '/dg/dataElemAssetRel/list',
        method: 'get',
        params: query
    });
}

// Query data metadata asset related information details
export function getDpDataElemAssetRel(id) {
    return request({
        url: '/dg/dataElemAssetRel/' + id,
        method: 'get'
    });
}

// Added data metadata asset related information
export function addDpDataElemAssetRel(data) {
    return request({
        url: '/dg/dataElemAssetRel',
        method: 'post',
        data: data
    });
}

// Modify data metadata asset related information
export function updateDpDataElemAssetRel(data) {
    return request({
        url: '/dg/dataElemAssetRel',
        method: 'put',
        data: data
    });
}

// Delete data metadata asset association information
export function delDpDataElemAssetRel(id) {
    return request({
        url: '/dg/dataElemAssetRel/' + id,
        method: 'delete'
    });
}
// Query the data element code list
export function listDpDataElemCode(query) {
    return request({
        url: '/dg/dataElemCode/list',
        method: 'get',
        params: query
    });
}

// Query data element code details
export function getDpDataElemCode(id) {
    return request({
        url: '/dg/dataElemCode/' + id,
        method: 'get'
    });
}

// Add data element code
export function addDpDataElemCode(data) {
    return request({
        url: '/dg/dataElemCode',
        method: 'post',
        data: data
    });
}

// Modify data element code
export function updateDpDataElemCode(data) {
    return request({
        url: '/dg/dataElemCode',
        method: 'put',
        data: data
    });
}

// Delete data element code
export function delDpDataElemCode(id) {
    return request({
        url: '/dg/dataElemCode/' + id,
        method: 'delete'
    });
}

//Verify source code values
export function validateCodeValue(params) {
    return request({
        url: '/dg/dataElemCode/validateCodeValue',
        method: 'get',
        params
    });
}
// Query the list of data metadata rule association information
export function listDpDataElemRuleRel(query) {
    return request({
        url: '/dg/dataElemRuleRel/list',
        method: 'get',
        params: query
    })
}

// Query detailed data metadata rule association information
export function getDpDataElemRuleRel(id) {
    return request({
        url: '/dg/dataElemRuleRel/' + id,
        method: 'get'
    })
}

// Added data metadata rule association information
export function addDpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'post',
        data: data
    })
}

// Modify data metadata rule association information
export function updateDpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'put',
        data: data
    })
}

// Delete data metadata rule association information
export function delDpDataElemRuleRel(id) {
    return request({
        url: '/dg/dataElemRuleRel/' + id,
        method: 'delete'
    })
}

// Save associated information
export function save(dataElemId, ruleType, data) {
    return request({
        url: `/dg/dataElemRuleRel/save/${dataElemId}/${ruleType}`,
        method: 'post',
        data
    })
}

// Data source cleaning audit rules
export function dpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'post',
        data: data
    });
}
// Data source cleaning audit rules modification
export function putDpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'put',
        data: data
    });
}
// Data source cleaning audit rules deletion
export function DlEPutDpDataElemRuleRel(id) {
    return request({
        url: '/dg/dataElemRuleRel/' + id,
        method: 'DELETE',
    });
}

// Data Integration Query
export function listDpDataElemRuleRelV2(query) {
    return request({
        url: '/da/asset/listRelRule/v2',
        method: 'get',
        params: query
    })
}

