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

package tech.qiantong.qdata.ai.core.service;

import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Flux;
import tech.qiantong.qdata.ai.core.vo.ChatMessageExportDetailDataReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendRespVO;


/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-07 14:18
 **/
public interface IChatMessageService {
    Flux<ChatMessageSendRespVO> sendChatMessageStream(ChatMessageSendReqVO sendReqVO, Long userId);

    /**
     * Export detailed data
     *
     * @param response
     * @param exportDetailDataReqVO
     */
    void exportDetailData(HttpServletResponse response, ChatMessageExportDetailDataReqVO exportDetailDataReqVO);
}
