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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;

/**
 * Data Domain Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmDataDomainConvert {
    DmDataDomainConvert INSTANCE = Mappers.getMapper(DmDataDomainConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dmDataDomainPageReqVO Request params
     * @return DmDataDomainDO
     */
     DmDataDomainDO convertToDO(DmDataDomainPageReqVO dmDataDomainPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dmDataDomainSaveReqVO Save request params
     * @return DmDataDomainDO
     */
     DmDataDomainDO convertToDO(DmDataDomainSaveReqVO dmDataDomainSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dmDataDomainDO Entity object
     * @return DmDataDomainRespVO
     */
     DmDataDomainRespVO convertToRespVO(DmDataDomainDO dmDataDomainDO);

    /**
     * Convert DO List to RespVO List
     * @param dmDataDomainDOList Entity object list
     * @return List<DmDataDomainRespVO>
     */
     List<DmDataDomainRespVO> convertToRespVOList(List<DmDataDomainDO> dmDataDomainDOList);
}
