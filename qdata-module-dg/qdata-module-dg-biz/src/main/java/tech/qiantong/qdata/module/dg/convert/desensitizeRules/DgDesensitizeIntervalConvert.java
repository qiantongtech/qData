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
 * 脱敏区间 Convert
 *
 * @author qdata
 * @date 2026-04-10
 */
@Mapper
public interface DgDesensitizeIntervalConvert {
    DgDesensitizeIntervalConvert INSTANCE = Mappers.getMapper(DgDesensitizeIntervalConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDesensitizeIntervalPageReqVO 请求参数
     * @return DgDesensitizeIntervalDO
     */
     DgDesensitizeIntervalDO convertToDO(DgDesensitizeIntervalPageReqVO dgDesensitizeIntervalPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDesensitizeIntervalSaveReqVO 保存请求参数
     * @return DgDesensitizeIntervalDO
     */
     DgDesensitizeIntervalDO convertToDO(DgDesensitizeIntervalSaveReqVO dgDesensitizeIntervalSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDesensitizeIntervalDO 实体对象
     * @return DgDesensitizeIntervalRespVO
     */
     DgDesensitizeIntervalRespVO convertToRespVO(DgDesensitizeIntervalDO dgDesensitizeIntervalDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDesensitizeIntervalDOList 实体对象列表
     * @return List<DgDesensitizeIntervalRespVO>
     */
     List<DgDesensitizeIntervalRespVO> convertToRespVOList(List<DgDesensitizeIntervalDO> dgDesensitizeIntervalDOList);
}
