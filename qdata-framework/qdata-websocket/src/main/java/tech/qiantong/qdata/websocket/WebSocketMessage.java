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

package tech.qiantong.qdata.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 客户端消息集
 *
 * @author qdata
 */
public class WebSocketMessage
{
    /**
     * WebSocketUsers 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessage.class);

    /**
     * 消息集
     */
    private static Map<String, Session> MESSAGES = new ConcurrentHashMap<String, Session>();

    public static Session get(String key) {
        return MESSAGES.get(key);
    }

    //用户可能会开启多个窗口,不同窗口session不一样, 存储key格式: userId_sessionId
    //获取此用户的所有窗口session
    public static List<Session> getUserSessionList(String userId) {
        List<Session> sessions = new ArrayList<>();
        // 遍历并使用 "_" 分割 key
        for (Map.Entry<String, Session> entry : MESSAGES.entrySet()) {
            String key = entry.getKey();
            // 使用 "_" 分割 key
            String[] parts = key.split("_");
            if(userId.equals(parts[0])){
                sessions.add(entry.getValue());
            }
        }
        return sessions;
    }

    /**
     * 存储消息数量
     *
     * @param key 唯一键
     * @param session 消息信息
     */
    public static void put(String key, Session session)
    {
        MESSAGES.put(key, session);
    }

    /**
     * 移除用户消息
     *
     * @param session 消息信息
     *
     * @return 移除结果
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
     * 移出用户消息
     *
     * @param key 键
     */
    public static boolean remove(String key)
    {
        LOGGER.info("\n 正在移出用户消息 - {}", key);
        Session remove = MESSAGES.remove(key);
        if (remove != null)
        {
            boolean containsValue = MESSAGES.containsValue(remove);
            LOGGER.info("\n 移出结果 - {}", containsValue ? "失败" : "成功");
            return containsValue;
        }
        else
        {
            return true;
        }
    }

    /**
     * 获取在线用户消息列表
     *
     * @return 返回用户集合
     */
    public static Map<String, Session> getMessages()
    {
        return MESSAGES;
    }

    /**
     * 群发消息文本消息
     *
     * @param message 消息内容
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
     * 发送文本消息
     *
     * @param message 消息内容
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
                LOGGER.error("\n[发送消息异常]", e);
            }
        }
        else
        {
            LOGGER.info("\n[你已离线]");
        }
    }

    // 向所有连接的用户广播消息
    public static void broadcast(String message) {
        for (Session session : MESSAGES.values()) {
            sendMessageToUserByText(session, message);
        }
    }
}
