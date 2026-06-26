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

package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;

import java.util.List;

/**
 * <P>
 * 用途:ds生成节点编码响应DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 16:57
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsNodeGenCodeRespDTO extends DsResultDTO {
    /**
     * 节点编码列表
     */
    private List<Long> data;
}
