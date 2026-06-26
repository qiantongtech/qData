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
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;

/**
 * 脱敏白名单 Convert
 *
 * @author qdata
 * @date 2026-04-09
 */
@Mapper
public interface DgDesensitizeWhitelistConvert {
    DgDesensitizeWhitelistConvert INSTANCE = Mappers.getMapper(DgDesensitizeWhitelistConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDesensitizeWhitelistPageReqVO 请求参数
     * @return DgDesensitizeWhitelistDO
     */
     DgDesensitizeWhitelistDO convertToDO(DgDesensitizeWhitelistPageReqVO dgDesensitizeWhitelistPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDesensitizeWhitelistSaveReqVO 保存请求参数
     * @return DgDesensitizeWhitelistDO
     */
     DgDesensitizeWhitelistDO convertToDO(DgDesensitizeWhitelistSaveReqVO dgDesensitizeWhitelistSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDesensitizeWhitelistDO 实体对象
     * @return DgDesensitizeWhitelistRespVO
     */
     DgDesensitizeWhitelistRespVO convertToRespVO(DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDesensitizeWhitelistDOList 实体对象列表
     * @return List<DgDesensitizeWhitelistRespVO>
     */
     List<DgDesensitizeWhitelistRespVO> convertToRespVOList(List<DgDesensitizeWhitelistDO> dgDesensitizeWhitelistDOList);
}
