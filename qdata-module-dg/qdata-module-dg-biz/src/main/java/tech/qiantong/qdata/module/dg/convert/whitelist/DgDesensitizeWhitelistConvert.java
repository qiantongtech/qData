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
 * Desensitization Whitelist Convert
 *
 * @author qdata
 * @date 2026-04-09
 */
@Mapper
public interface DgDesensitizeWhitelistConvert {
    DgDesensitizeWhitelistConvert INSTANCE = Mappers.getMapper(DgDesensitizeWhitelistConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dgDesensitizeWhitelistPageReqVO request params
     * @return DgDesensitizeWhitelistDO
     */
     DgDesensitizeWhitelistDO convertToDO(DgDesensitizeWhitelistPageReqVO dgDesensitizeWhitelistPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dgDesensitizeWhitelistSaveReqVO save request params
     * @return DgDesensitizeWhitelistDO
     */
     DgDesensitizeWhitelistDO convertToDO(DgDesensitizeWhitelistSaveReqVO dgDesensitizeWhitelistSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dgDesensitizeWhitelistDO entity object
     * @return DgDesensitizeWhitelistRespVO
     */
     DgDesensitizeWhitelistRespVO convertToRespVO(DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO);

    /**
     * Convert DO List to RespVO List
     * @param dgDesensitizeWhitelistDOList entity object list
     * @return List<DgDesensitizeWhitelistRespVO>
     */
     List<DgDesensitizeWhitelistRespVO> convertToRespVOList(List<DgDesensitizeWhitelistDO> dgDesensitizeWhitelistDOList);
}
