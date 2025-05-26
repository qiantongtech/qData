import request from '@/utils/request'

// 执行命令
export function execute(taskInstanceId,executeType) {
    return request({
        url: `/dpp/dppEtlExecutors/execute/${taskInstanceId}/${executeType}`,
        method: 'post'
    })
}
