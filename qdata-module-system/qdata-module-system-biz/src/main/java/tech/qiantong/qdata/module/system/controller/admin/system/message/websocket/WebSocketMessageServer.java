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
 * websocket 消息处理
 *
 * @author qdata
 */
@Component
@ServerEndpoint("/websocket/message/{userId}")
public class WebSocketMessageServer {
    // 日志记录
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageServer.class);

    /**
     * 连接建立成功时触发的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws Exception {
        String key = userId + '_' + session.getId();
        LOGGER.info("连接成功 - 用户ID: {}", key);
        // 保存连接的 session 对象
        // 一个客户可能会开启多个窗口,不同窗口session不一样, 存储key格式: userId_sessionId
        WebSocketMessage.put(key, session);
    }

    /**
     * 连接关闭时触发的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        String key = userId + '_' + session.getId();
        LOGGER.info("连接关闭 - 用户ID: {}", key);
        // 移除用户连接
        //一个客户可能会开启多个窗口,不同窗口session不一样, 存储key格式: userId_sessionId
        WebSocketMessage.remove(key);
    }

    /**
     * 异常时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable exception, @PathParam("userId") String userId) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        LOGGER.error("连接异常 - 用户ID: {} - 异常信息: {}", userId, exception.getMessage());
        // 移除用户连接
        WebSocketMessage.remove(userId);
    }

    /**
     * 向客户端发送消息
     * 一个客户可能会开启多个窗口,不同窗口session不一样, 存储key格式: userId_sessionId
     * @param userId
     * @param message
     */
    public static void sendMessageToUser(String userId, String message) {
        // 获取用户的 session
        List<Session> sessionList = WebSocketMessage.getUserSessionList(userId);
        if (sessionList.size() > 0) {
            for (Session session : sessionList) {
                // 通过 WebSocketMessage 发送消息
                WebSocketMessage.sendMessageToUserByText(session, message);
            }
        }
    }

    /**
     * 向客户端发送消息
     * @param userId 用户ID
     * @param message 消息内容
     */
    public static void sendMessage(String userId, String message) {
        // 获取用户的 session
        Session session = WebSocketMessage.get(userId);
        if (session != null) {
            // 通过 WebSocketMessage 发送消息
            WebSocketMessage.sendMessageToUserByText(session, message);
        }
    }

    /**
     * 向所有连接的客户端广播消息
     * @param message 消息内容
     */
    public static void broadcastMessage(MessagePageReqVO message) {
        String string = JSONObject.toJSONString(message);
        WebSocketMessage.broadcast(string);
    }

}
