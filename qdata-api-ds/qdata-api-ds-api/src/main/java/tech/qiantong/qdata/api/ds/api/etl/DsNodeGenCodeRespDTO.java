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
