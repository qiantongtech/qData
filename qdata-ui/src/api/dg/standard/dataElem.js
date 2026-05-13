import request from '@/utils/request';

// 查询数据元列表
export function listDpDataElem(query) {
    return request({
        url: '/dg/dataElem/list',
        method: 'get',
        params: query
    });
}

// 查询数据元列表
export function getDgDataElemList(query) {
    return request({
        url: '/dg/dataElem/getDgDataElemList',
        method: 'get',
        params: query
    });
}

// 查询数据元详细
export function getDpDataElem(id) {
    return request({
        url: '/dg/dataElem/' + id,
        method: 'get'
    });
}

// 新增数据元
export function addDpDataElem(data) {
    return request({
        url: '/dg/dataElem',
        method: 'post',
        data: data
    });
}

// 修改数据元
export function updateDpDataElem(data) {
    return request({
        url: '/dg/dataElem',
        method: 'put',
        data: data
    });
}
// 修改数据元
export function updateStatusDpDataElem(id, status) {
    return request({
        url: `/dg/dataElem/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// 删除数据元
export function delDpDataElem(id) {
    return request({
        url: '/dg/dataElem/' + id,
        method: 'delete'
    });
}
// 查询标准登记（文档）列表（按类型）
export function listDpDocument(query) {
    return request({
        url: '/dg/document/list',
        method: 'get',
        params: query
    });
}
// 查询数据元代码映射列表
export function listDpCodeMap(query) {
    return request({
        url: '/dg/codeMap/list',
        method: 'get',
        params: query
    });
}

// 查询数据元代码映射详细
export function getDpCodeMap(id) {
    return request({
        url: '/dg/codeMap/' + id,
        method: 'get'
    });
}

// 新增数据元代码映射
export function addDpCodeMap(data) {
    return request({
        url: '/dg/codeMap',
        method: 'post',
        data: data
    });
}

// 修改数据元代码映射
export function updateDpCodeMap(data) {
    return request({
        url: '/dg/codeMap',
        method: 'put',
        data: data
    });
}

// 删除数据元代码映射
export function delDpCodeMap(id) {
    return request({
        url: '/dg/codeMap/' + id,
        method: 'delete'
    });
}
// 查询数据元数据资产关联信息列表
export function listDpDataElemAssetRel(query) {
    return request({
        url: '/dg/dataElemAssetRel/list',
        method: 'get',
        params: query
    });
}

// 查询数据元数据资产关联信息详细
export function getDpDataElemAssetRel(id) {
    return request({
        url: '/dg/dataElemAssetRel/' + id,
        method: 'get'
    });
}

// 新增数据元数据资产关联信息
export function addDpDataElemAssetRel(data) {
    return request({
        url: '/dg/dataElemAssetRel',
        method: 'post',
        data: data
    });
}

// 修改数据元数据资产关联信息
export function updateDpDataElemAssetRel(data) {
    return request({
        url: '/dg/dataElemAssetRel',
        method: 'put',
        data: data
    });
}

// 删除数据元数据资产关联信息
export function delDpDataElemAssetRel(id) {
    return request({
        url: '/dg/dataElemAssetRel/' + id,
        method: 'delete'
    });
}
// 查询数据元代码列表
export function listDpDataElemCode(query) {
    return request({
        url: '/dg/dataElemCode/list',
        method: 'get',
        params: query
    });
}

// 查询数据元代码详细
export function getDpDataElemCode(id) {
    return request({
        url: '/dg/dataElemCode/' + id,
        method: 'get'
    });
}

// 新增数据元代码
export function addDpDataElemCode(data) {
    return request({
        url: '/dg/dataElemCode',
        method: 'post',
        data: data
    });
}

// 修改数据元代码
export function updateDpDataElemCode(data) {
    return request({
        url: '/dg/dataElemCode',
        method: 'put',
        data: data
    });
}

// 删除数据元代码
export function delDpDataElemCode(id) {
    return request({
        url: '/dg/dataElemCode/' + id,
        method: 'delete'
    });
}

//校验源代码值
export function validateCodeValue(params) {
    return request({
        url: '/dg/dataElemCode/validateCodeValue',
        method: 'get',
        params
    });
}
// 查询数据元数据规则关联信息列表
export function listDpDataElemRuleRel(query) {
    return request({
        url: '/dg/dataElemRuleRel/list',
        method: 'get',
        params: query
    })
}

// 查询数据元数据规则关联信息详细
export function getDpDataElemRuleRel(id) {
    return request({
        url: '/dg/dataElemRuleRel/' + id,
        method: 'get'
    })
}

// 新增数据元数据规则关联信息
export function addDpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'post',
        data: data
    })
}

// 修改数据元数据规则关联信息
export function updateDpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'put',
        data: data
    })
}

// 删除数据元数据规则关联信息
export function delDpDataElemRuleRel(id) {
    return request({
        url: '/dg/dataElemRuleRel/' + id,
        method: 'delete'
    })
}

// 保存关联信息
export function save(dataElemId, ruleType, data) {
    return request({
        url: `/dg/dataElemRuleRel/save/${dataElemId}/${ruleType}`,
        method: 'post',
        data
    })
}

// 数据源清洗 稽查规则
export function dpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'post',
        data: data
    });
}
// 数据源清洗 稽查规则 修改
export function putDpDataElemRuleRel(data) {
    return request({
        url: '/dg/dataElemRuleRel',
        method: 'put',
        data: data
    });
}
// 数据源清洗 稽查规则 删除
export function DlEPutDpDataElemRuleRel(id) {
    return request({
        url: '/dg/dataElemRuleRel/' + id,
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

