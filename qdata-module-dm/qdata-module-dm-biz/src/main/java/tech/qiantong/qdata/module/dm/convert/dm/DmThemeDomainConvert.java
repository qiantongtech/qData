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

package tech.qiantong.qdata.module.dm.convert.dm;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;

/**
 * Theme Domain Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmThemeDomainConvert {
    DmThemeDomainConvert INSTANCE = Mappers.getMapper(DmThemeDomainConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dmThemeDomainPageReqVO Request params
     * @return DmThemeDomainDO
     */
     DmThemeDomainDO convertToDO(DmThemeDomainPageReqVO dmThemeDomainPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dmThemeDomainSaveReqVO Save request params
     * @return DmThemeDomainDO
     */
     DmThemeDomainDO convertToDO(DmThemeDomainSaveReqVO dmThemeDomainSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dmThemeDomainDO Entity object
     * @return DmThemeDomainRespVO
     */
     DmThemeDomainRespVO convertToRespVO(DmThemeDomainDO dmThemeDomainDO);

    /**
     * Convert DO List to RespVO List
     * @param dmThemeDomainDOList Entity object list
     * @return List<DmThemeDomainRespVO>
     */
     List<DmThemeDomainRespVO> convertToRespVOList(List<DmThemeDomainDO> dmThemeDomainDOList);
}
