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
