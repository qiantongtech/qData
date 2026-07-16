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

package tech.qiantong.qdata.module.system.controller.admin.example.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.websocket.SemaphoreUtils;
import tech.qiantong.qdata.websocket.WebSocketUsers;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.Semaphore;

/**
 * WebSocket Message Handler
 *
 * @author qdata
 */
@Component
@ServerEndpoint("/websocket/message")
public class WebSocketServer
{
    /**
     * WebSocketServer log controller
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * Maximum concurrent online users allowed by default: 100
     */
    public static int socketMaxOnlineCount = 100;

    private static Semaphore socketSemaphore = new Semaphore(socketMaxOnlineCount);

    /**
     * Method called when connection is established
     */
    @OnOpen
    public void onOpen(Session session) throws Exception
    {
        boolean semaphoreFlag = false;
        // Try to acquire semaphore
        semaphoreFlag = SemaphoreUtils.tryAcquire(socketSemaphore);
        if (!semaphoreFlag)
        {
            // Semaphore not acquired
            LOGGER.error("\n Current online users exceed limit - {}", socketMaxOnlineCount);
            WebSocketUsers.sendMessageToUserByText(session, "Current online users exceed limit: " + socketMaxOnlineCount);
            session.close();
        }
        else
        {
            // Add user
            WebSocketUsers.put(session.getId(), session);
            LOGGER.info("\n Connection established - {}", session);
            LOGGER.info("\n Current users - {}", WebSocketUsers.getUsers().size());
            WebSocketUsers.sendMessageToUserByText(session, "Connection successful");
        }
    }

    /**
     * Method called when connection is closed
     */
    @OnClose
    public void onClose(Session session)
    {
        LOGGER.info("\n Connection closed - {}", session);
        // Remove user
        boolean removeFlag = WebSocketUsers.remove(session.getId());
        if (!removeFlag)
        {
            // Semaphore acquired, need to release
            SemaphoreUtils.release(socketSemaphore);
        }
    }

    /**
     * Method called when an exception is thrown
     */
    @OnError
    public void onError(Session session, Throwable exception) throws Exception
    {
        if (session.isOpen())
        {
            // Close connection
            session.close();
        }
        String sessionId = session.getId();
        LOGGER.info("\n Connection error - {}", sessionId);
        LOGGER.info("\n Error details - {}", exception);
        // Remove user
        WebSocketUsers.remove(sessionId);
        // Semaphore acquired, need to release
        SemaphoreUtils.release(socketSemaphore);
    }

    /**
     * Method called when server receives a client message
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        String msg = message.replace("你", "我").replace("吗", "");
        WebSocketUsers.sendMessageToUserByText(session, msg);
    }
}
