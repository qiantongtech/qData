/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

class WebSocketService {
    constructor(userId, token) {
        this.userId = userId;
        this.token = token;
        this.socket = null;
    }

    init() {
        if (this.socket && this.socket.readyState !== WebSocket.CLOSED) {
            console.warn('WebSocket already initialized.');
            return;  // 如果连接已经初始化并且没有关闭，就不需要再初始化
        }

        // 创建 WebSocket 连接
        const wsUri = import.meta.env.VITE_APP_WEBSOCKET_API + `/websocket/message/${this.userId}`
        // 建立socket连接
        this.socket = new WebSocket(wsUri);

        // 连接打开时发送认证信息
        this.socket.onopen = () => {
            console.log('WebSocket connection opened');
            this.socket.send(JSON.stringify({ type: 'authenticate', token: this.token }));
        };

        // 监听消息
        this.socket.onmessage = (event) => {
            console.log('---------------Received message:', event.data);
        };

        // 连接出错时的处理
        this.socket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        // 连接关闭时的处理
        this.socket.onclose = () => {
            console.log('WebSocket connection closed');
        };
    }

    sendMessage(message) {
        console.log('-----------WebSocket 发送消息----------', message);
        // 确保连接已建立
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify({ type: 'message', content: message }));
        } else {
            console.warn('WebSocket is not open. ReadyState:', this.socket ? this.socket.readyState : 'null');
            this.reconnect();
        }
    }

    reconnect() {
        // 尝试重新连接 WebSocket
        console.log('Attempting to reconnect WebSocket...');
        if (this.socket && this.socket.readyState === WebSocket.CLOSED) {
            this.init(); // 重新初始化 WebSocket 连接
        }
    }

    close() {
        if (this.socket) {
            this.socket.close();
        }
    }
}

export default WebSocketService;