import request from '@/utils/request';

// 查询数据元列表
export function listDpDataElem(query) {
    return request({
        url: '/dp/dataElem/list',
        method: 'get',
        params: query
    });
}

// 查询数据元列表
export function getDpDataElemList(query) {
    return request({
        url: '/dp/dataElem/getDpDataElemList',
        method: 'get',
        params: query
    });
}

// 查询数据元详细
export function getDpDataElem(id) {
    return request({
        url: '/dp/dataElem/' + id,
        method: 'get'
    });
}

// 新增数据元
export function addDpDataElem(data) {
    return request({
        url: '/dp/dataElem',
        method: 'post',
        data: data
    });
}

// 修改数据元
export function updateDpDataElem(data) {
    return request({
        url: '/dp/dataElem',
        method: 'put',
        data: data
    });
}
// 修改数据元
export function updateStatusDpDataElem(id, status) {
    return request({
        url: `/dp/dataElem/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// 删除数据元
export function delDpDataElem(id) {
    return request({
        url: '/dp/dataElem/' + id,
        method: 'delete'
    });
}
// 查询数据元代码映射列表
export function listDpCodeMap(query) {
    return request({
        url: '/dp/codeMap/list',
        method: 'get',
        params: query
    });
}

// 查询数据元代码映射详细
export function getDpCodeMap(id) {
    return request({
        url: '/dp/codeMap/' + id,
        method: 'get'
    });
}

// 新增数据元代码映射
export function addDpCodeMap(data) {
    return request({
        url: '/dp/codeMap',
        method: 'post',
        data: data
    });
}

// 修改数据元代码映射
export function updateDpCodeMap(data) {
    return request({
        url: '/dp/codeMap',
        method: 'put',
        data: data
    });
}

// 删除数据元代码映射
export function delDpCodeMap(id) {
    return request({
        url: '/dp/codeMap/' + id,
        method: 'delete'
    });
}
// 查询数据元数据资产关联信息列表
export function listDpDataElemAssetRel(query) {
    return request({
        url: '/dp/dataElemAssetRel/list',
        method: 'get',
        params: query
    });
}

// 查询数据元数据资产关联信息详细
export function getDpDataElemAssetRel(id) {
    return request({
        url: '/dp/dataElemAssetRel/' + id,
        method: 'get'
    });
}

// 新增数据元数据资产关联信息
export function addDpDataElemAssetRel(data) {
    return request({
        url: '/dp/dataElemAssetRel',
        method: 'post',
        data: data
    });
}

// 修改数据元数据资产关联信息
export function updateDpDataElemAssetRel(data) {
    return request({
        url: '/dp/dataElemAssetRel',
        method: 'put',
        data: data
    });
}

// 删除数据元数据资产关联信息
export function delDpDataElemAssetRel(id) {
    return request({
        url: '/dp/dataElemAssetRel/' + id,
        method: 'delete'
    });
}
// 查询数据元代码列表
export function listDpDataElemCode(query) {
    return request({
        url: '/dp/dataElemCode/list',
        method: 'get',
        params: query
    });
}

// 查询数据元代码详细
export function getDpDataElemCode(id) {
    return request({
        url: '/dp/dataElemCode/' + id,
        method: 'get'
    });
}

// 新增数据元代码
export function addDpDataElemCode(data) {
    return request({
        url: '/dp/dataElemCode',
        method: 'post',
        data: data
    });
}

// 修改数据元代码
export function updateDpDataElemCode(data) {
    return request({
        url: '/dp/dataElemCode',
        method: 'put',
        data: data
    });
}

// 删除数据元代码
export function delDpDataElemCode(id) {
    return request({
        url: '/dp/dataElemCode/' + id,
        method: 'delete'
    });
}

//校验源代码值
export function validateCodeValue(params) {
    return request({
        url: '/dp/dataElemCode/validateCodeValue',
        method: 'get',
        params
    });
}
// 查询数据元数据规则关联信息列表
export function listDpDataElemRuleRel(query) {
    return request({
        url: '/dp/dataElemRuleRel/list',
        method: 'get',
        params: query
    })
}

// 查询数据元数据规则关联信息详细
export function getDpDataElemRuleRel(id) {
    return request({
        url: '/dp/dataElemRuleRel/' + id,
        method: 'get'
    })
}

// 新增数据元数据规则关联信息
export function addDpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'post',
        data: data
    })
}

// 修改数据元数据规则关联信息
export function updateDpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'put',
        data: data
    })
}

// 删除数据元数据规则关联信息
export function delDpDataElemRuleRel(id) {
    return request({
        url: '/dp/dataElemRuleRel/' + id,
        method: 'delete'
    })
}

// 保存关联信息
export function save(dataElemId, ruleType, data) {
    return request({
        url: `/dp/dataElemRuleRel/save/${dataElemId}/${ruleType}`,
        method: 'post',
        data
    })
}

// 数据源清洗 稽查规则
export function dpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'post',
        data: data
    });
}
// 数据源清洗 稽查规则 修改
export function putDpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'put',
        data: data
    });
}
// 数据源清洗 稽查规则 删除
export function DlEPutDpDataElemRuleRel(id) {
    return request({
        url: '/dp/dataElemRuleRel/' + id,
        method: 'DELETE',
    });
}

// 数据集成 查询
export function listDpDataElemRuleRelV2(query) {
    return request({
        url: '/da/asset/listRelRule/v2',
        method: 'get',
        params: query
    })
}

