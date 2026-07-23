/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
            return;  // If the connection has been initialized and not closed, there is no need to initialize it again.
        }

        // Create a WebSocket connection
        const wsUri = import.meta.env.VITE_APP_WEBSOCKET_API + `/websocket/message/${this.userId}`
        // Establish socket connection
        this.socket = new WebSocket(wsUri);

        // Send authentication information when the connection is opened
        this.socket.onopen = () => {
            console.log('WebSocket connection opened');
            this.socket.send(JSON.stringify({ type: 'authenticate', token: this.token }));
        };

        // Listen for messages
        this.socket.onmessage = (event) => {
            console.log('---------------Received message:', event.data);
        };

        // Handling when connection errors occur
        this.socket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        // Handling when connection is closed
        this.socket.onclose = () => {
            console.log('WebSocket connection closed');
        };
    }

    sendMessage(message) {
        console.log("-----------WebSocket message sent----------", message);
        // Make sure the connection is established
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify({ type: 'message', content: message }));
        } else {
            console.warn('WebSocket is not open. ReadyState:', this.socket ? this.socket.readyState : 'null');
            this.reconnect();
        }
    }

    reconnect() {
        // Try reconnecting WebSocket
        console.log('Attempting to reconnect WebSocket...');
        if (this.socket && this.socket.readyState === WebSocket.CLOSED) {
            this.init(); // Reinitialize WebSocket connection
        }
    }

    close() {
        if (this.socket) {
            this.socket.close();
        }
    }
}

export default WebSocketService;
