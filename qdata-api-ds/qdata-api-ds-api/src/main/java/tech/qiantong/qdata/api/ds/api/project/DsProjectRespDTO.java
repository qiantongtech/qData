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

package tech.qiantong.qdata.api.ds.api.project;

import lombok.Data;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;

/**
 * <P>
 * 用途:项目保存修改响应DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:20
 **/
@Data
public class DsProjectRespDTO extends DsResultDTO {

    private Data data;

    @lombok.Data
    public class Data{
        /**
         * id
         */
        private Long id;

        /**
         * 项目编码
         */
        private Long code;

        /**
         * 项目名称
         */
        private String name;

        /**
         * 项目描述
         */
        private String description;
    }
}
