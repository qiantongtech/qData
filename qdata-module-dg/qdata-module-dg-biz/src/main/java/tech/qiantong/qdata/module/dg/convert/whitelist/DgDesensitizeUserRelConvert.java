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
