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
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket client user collection
 *
 * @author qdata
 */
public class WebSocketUsers
{
    /**
     * WebSocketUsers logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketUsers.class);

    /**
     * User collection
     */
    private static Map<String, Session> USERS = new ConcurrentHashMap<String, Session>();

    /**
     * Store user
     *
     * @param key unique key
     * @param session user session
     */
    public static void put(String key, Session session)
    {
        USERS.put(key, session);
    }

    /**
     * Remove user
     *
     * @param session user session
     *
     * @return removal result
     */
    public static boolean remove(Session session)
    {
        String key = null;
        boolean flag = USERS.containsValue(session);
        if (flag)
        {
            Set<Map.Entry<String, Session>> entries = USERS.entrySet();
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
     * Remove user by key
     *
     * @param key key
     */
    public static boolean remove(String key)
    {
        LOGGER.info("\n Removing user - {}", key);
        Session remove = USERS.remove(key);
        if (remove != null)
        {
            boolean containsValue = USERS.containsValue(remove);
            LOGGER.info("\n Removal result - {}", containsValue ? "failed" : "succeeded");
            return containsValue;
        }
        else
        {
            return true;
        }
    }

    /**
     * Get online user list
     *
     * @return user collection
     */
    public static Map<String, Session> getUsers()
    {
        return USERS;
    }

    /**
     * Broadcast text message to all users
     *
     * @param message message content
     */
    public static void sendMessageToUsersByText(String message)
    {
        Collection<Session> values = USERS.values();
        for (Session value : values)
        {
            sendMessageToUserByText(value, message);
        }
    }

    /**
     * Send text message to a user
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
}
