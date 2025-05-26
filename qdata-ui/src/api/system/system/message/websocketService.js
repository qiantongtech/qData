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