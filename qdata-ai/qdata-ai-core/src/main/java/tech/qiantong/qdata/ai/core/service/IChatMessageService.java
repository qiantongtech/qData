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
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.ai.core.service;

import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Flux;
import tech.qiantong.qdata.ai.core.vo.ChatMessageExportDetailDataReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendRespVO;


/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-07 14:18
 **/
public interface IChatMessageService {
    Flux<ChatMessageSendRespVO> sendChatMessageStream(ChatMessageSendReqVO sendReqVO, Long userId);

    /**
     * 导出明细数据
     *
     * @param response
     * @param exportDetailDataReqVO
     */
    void exportDetailData(HttpServletResponse response, ChatMessageExportDetailDataReqVO exportDetailDataReqVO);
}
