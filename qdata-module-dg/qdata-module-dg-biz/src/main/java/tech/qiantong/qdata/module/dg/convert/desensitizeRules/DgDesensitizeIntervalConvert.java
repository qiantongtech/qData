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

package tech.qiantong.qdata.module.dg.convert.desensitizeRules;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;

/**
 * Desensitization Interval Convert
 *
 * @author qdata
 * @date 2026-04-10
 */
@Mapper
public interface DgDesensitizeIntervalConvert {
    DgDesensitizeIntervalConvert INSTANCE = Mappers.getMapper(DgDesensitizeIntervalConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dgDesensitizeIntervalPageReqVO request parameters
     * @return DgDesensitizeIntervalDO
     */
     DgDesensitizeIntervalDO convertToDO(DgDesensitizeIntervalPageReqVO dgDesensitizeIntervalPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dgDesensitizeIntervalSaveReqVO save request parameters
     * @return DgDesensitizeIntervalDO
     */
     DgDesensitizeIntervalDO convertToDO(DgDesensitizeIntervalSaveReqVO dgDesensitizeIntervalSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dgDesensitizeIntervalDO entity object
     * @return DgDesensitizeIntervalRespVO
     */
     DgDesensitizeIntervalRespVO convertToRespVO(DgDesensitizeIntervalDO dgDesensitizeIntervalDO);

    /**
     * Convert DO List to RespVO List
     * @param dgDesensitizeIntervalDOList entity object list
     * @return List<DgDesensitizeIntervalRespVO>
     */
     List<DgDesensitizeIntervalRespVO> convertToRespVOList(List<DgDesensitizeIntervalDO> dgDesensitizeIntervalDOList);
}
