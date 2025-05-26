// ֧���ӿ�
import request from "@/utils/request.js";

export function pay(data) {
    return request({
        url: '/payment/pay',
        method: 'post',
        data: data
    })
}
