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

package tech.qiantong.qdata.module.att.convert.sourceSystem;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;

import java.util.List;

/**
 * 来源系统 Convert
 *
 * @author qdata
 * @date 2026-04-03
 */
@Mapper
public interface AttSourceSystemConvert {
    AttSourceSystemConvert INSTANCE = Mappers.getMapper(AttSourceSystemConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attSourceSystemPageReqVO 请求参数
     * @return AttSourceSystemDO
     */
     AttSourceSystemDO convertToDO(AttSourceSystemPageReqVO attSourceSystemPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attSourceSystemSaveReqVO 保存请求参数
     * @return AttSourceSystemDO
     */
     AttSourceSystemDO convertToDO(AttSourceSystemSaveReqVO attSourceSystemSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attSourceSystemDO 实体对象
     * @return AttSourceSystemRespVO
     */
     AttSourceSystemRespVO convertToRespVO(AttSourceSystemDO attSourceSystemDO);

    /**
     * DOList 转换为 RespVOList
     * @param attSourceSystemDOList 实体对象列表
     * @return List<AttSourceSystemRespVO>
     */
     List<AttSourceSystemRespVO> convertToRespVOList(List<AttSourceSystemDO> attSourceSystemDOList);
}
