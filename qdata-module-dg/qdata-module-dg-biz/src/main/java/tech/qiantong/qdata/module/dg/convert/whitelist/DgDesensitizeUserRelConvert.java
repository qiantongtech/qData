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

package tech.qiantong.qdata.module.dg.convert.whitelist;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;

/**
 * 脱敏白名单与用户关联关系 Convert
 *
 * @author qdata
 * @date 2026-04-09
 */
@Mapper
public interface DgDesensitizeUserRelConvert {
    DgDesensitizeUserRelConvert INSTANCE = Mappers.getMapper(DgDesensitizeUserRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDesensitizeUserRelPageReqVO 请求参数
     * @return DgDesensitizeUserRelDO
     */
     DgDesensitizeUserRelDO convertToDO(DgDesensitizeUserRelPageReqVO dgDesensitizeUserRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDesensitizeUserRelSaveReqVO 保存请求参数
     * @return DgDesensitizeUserRelDO
     */
     DgDesensitizeUserRelDO convertToDO(DgDesensitizeUserRelSaveReqVO dgDesensitizeUserRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDesensitizeUserRelDO 实体对象
     * @return DgDesensitizeUserRelRespVO
     */
     DgDesensitizeUserRelRespVO convertToRespVO(DgDesensitizeUserRelDO dgDesensitizeUserRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDesensitizeUserRelDOList 实体对象列表
     * @return List<DgDesensitizeUserRelRespVO>
     */
     List<DgDesensitizeUserRelRespVO> convertToRespVOList(List<DgDesensitizeUserRelDO> dgDesensitizeUserRelDOList);
}
