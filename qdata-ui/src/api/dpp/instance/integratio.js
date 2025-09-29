import request from '@/utils/request'


/**
 * 查询数据集成节点实例列表
 */
export function listDppEtlNodeInstance(query) {
        return request({
                url: '/dpp/etlNodeInstance/list',
                method: 'get',
                params: query
        });
}

/**
 * 查询数据集成节点实例详细
 */
export function getDppEtlNodeInstance(id) {
        return request({
                url: `/dpp/etlNodeInstance/${id}`,
                method: 'get'
        });
}

/**
 * 新增数据集成节点实例
 */
export function addDppEtlNodeInstance(data) {
        return request({
                url: '/dpp/etlNodeInstance',
                method: 'post',
                data: data
        });
}

/**
 * 修改数据集成节点实例
 */
export function updateDppEtlNodeInstance(data) {
        return request({
                url: '/dpp/etlNodeInstance',
                method: 'put',
                data: data
        });
}

/**
 * 删除数据集成节点实例
 */
export function delDppEtlNodeInstance(id) {
        return request({
                url: `/dpp/etlNodeInstance/${id}`,
                method: 'delete'
        });
}

/**
 * 查看日志详情（修复了“超看”错别字）
 */
export function logDetailCat(id) {
        return request({
                url: `/dpp/etlNodeInstance/log/${id}`,
                method: 'get'
        });
}