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
