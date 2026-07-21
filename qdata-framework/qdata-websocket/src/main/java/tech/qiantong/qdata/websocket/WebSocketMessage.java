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

package tech.qiantong.qdata.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket client message collection
 *
 * @author qdata
 */
public class WebSocketMessage
{
    /**
     * WebSocketMessage logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessage.class);

    /**
     * Message collection
     */
    private static Map<String, Session> MESSAGES = new ConcurrentHashMap<String, Session>();

    public static Session get(String key) {
        return MESSAGES.get(key);
    }

    // Users may open multiple windows with different sessions, key format: userId_sessionId
    // Get all window sessions for this user
    public static List<Session> getUserSessionList(String userId) {
        List<Session> sessions = new ArrayList<>();
        // Iterate and split key by "_"
        for (Map.Entry<String, Session> entry : MESSAGES.entrySet()) {
            String key = entry.getKey();
            // Split key by "_"
            String[] parts = key.split("_");
            if(userId.equals(parts[0])){
                sessions.add(entry.getValue());
            }
        }
        return sessions;
    }

    /**
     * Store message count
     *
     * @param key unique key
     * @param session message session
     */
    public static void put(String key, Session session)
    {
        MESSAGES.put(key, session);
    }

    /**
     * Remove user message
     *
     * @param session message session
     *
     * @return removal result
     */
    public static boolean remove(Session session)
    {
        String key = null;
        boolean flag = MESSAGES.containsValue(session);
        if (flag)
        {
            Set<Map.Entry<String, Session>> entries = MESSAGES.entrySet();
            for (Map.Entry<String, Session> entry : entries)
            {
                Session value = entry.getValue();
                if (value.equals(session))
                {
                    key = entry.getKey();
                    break;
                }
            }
        }
        else
        {
            return true;
        }
        return remove(key);
    }

    /**
     * Remove user message by key
     *
     * @param key key
     */
    public static boolean remove(String key)
    {
        LOGGER.info("\n Removing user message - {}", key);
        Session remove = MESSAGES.remove(key);
        if (remove != null)
        {
            boolean containsValue = MESSAGES.containsValue(remove);
            LOGGER.info("\n Removal result - {}", containsValue ? "failed" : "succeeded");
            return containsValue;
        }
        else
        {
            return true;
        }
    }

    /**
     * Get online user message list
     *
     * @return user collection
     */
    public static Map<String, Session> getMessages()
    {
        return MESSAGES;
    }

    /**
     * Broadcast text message to all users
     *
     * @param message message content
     */
    public static void sendMessageToUsersByText(String message)
    {
        Collection<Session> values = MESSAGES.values();
        for (Session value : values)
        {
            sendMessageToUserByText(value, message);
        }
    }

    /**
     * Send text message
     *
     * @param session user session
     * @param message message content
     */
    public static void sendMessageToUserByText(Session session, String message)
    {
        if (session != null)
        {
            try
            {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e)
            {
                LOGGER.error("\n[Failed to send message]", e);
            }
        }
        else
        {
            LOGGER.info("\n[You are offline]");
        }
    }

    // Broadcast message to all connected users
    public static void broadcast(String message) {
        for (Session session : MESSAGES.values()) {
            sendMessageToUserByText(session, message);
        }
    }
}
