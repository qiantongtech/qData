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
