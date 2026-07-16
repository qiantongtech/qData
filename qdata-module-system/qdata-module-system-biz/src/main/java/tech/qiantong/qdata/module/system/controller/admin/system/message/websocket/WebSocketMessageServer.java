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

package tech.qiantong.qdata.module.system.controller.admin.system.message.websocket;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qdata.websocket.WebSocketMessage;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;

/**
 * WebSocket message handler
 *
 * @author qdata
 */
@Component
@ServerEndpoint("/websocket/message/{userId}")
public class WebSocketMessageServer {
    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageServer.class);

    /**
     * Method triggered when connection is established
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws Exception {
        String key = userId + '_' + session.getId();
        LOGGER.info("Connected successfully - User ID: {}", key);
        // Save the connected session object
        // A client may open multiple windows with different sessions. Storage key format: userId_sessionId
        WebSocketMessage.put(key, session);
    }

    /**
     * Method triggered when connection is closed
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        String key = userId + '_' + session.getId();
        LOGGER.info("Connection closed - User ID: {}", key);
        // Remove user connection
        // A client may open multiple windows with different sessions. Storage key format: userId_sessionId
        WebSocketMessage.remove(key);
    }

    /**
     * Method triggered on error
     */
    @OnError
    public void onError(Session session, Throwable exception, @PathParam("userId") String userId) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        LOGGER.error("Connection error - User ID: {} - Error: {}", userId, exception.getMessage());
        // Remove user connection
        WebSocketMessage.remove(userId);
    }

    /**
     * Send message to client
     * A client may open multiple windows with different sessions. Storage key format: userId_sessionId
     * @param userId
     * @param message
     */
    public static void sendMessageToUser(String userId, String message) {
        // Get user sessions
        List<Session> sessionList = WebSocketMessage.getUserSessionList(userId);
        if (sessionList.size() > 0) {
            for (Session session : sessionList) {
                // Send message via WebSocketMessage
                WebSocketMessage.sendMessageToUserByText(session, message);
            }
        }
    }

    /**
     * Send message to client
     * @param userId User ID
     * @param message Message content
     */
    public static void sendMessage(String userId, String message) {
        // Get user session
        Session session = WebSocketMessage.get(userId);
        if (session != null) {
            // Send message via WebSocketMessage
            WebSocketMessage.sendMessageToUserByText(session, message);
        }
    }

    /**
     * Broadcast message to all connected clients
     * @param message Message content
     */
    public static void broadcastMessage(MessagePageReqVO message) {
        String string = JSONObject.toJSONString(message);
        WebSocketMessage.broadcast(string);
    }

}
