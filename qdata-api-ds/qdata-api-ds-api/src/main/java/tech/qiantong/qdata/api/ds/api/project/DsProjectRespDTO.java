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
