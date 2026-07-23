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

package tech.qiantong.qdata.module.da.convert.discovery;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;

import java.util.List;

/**
 * Data Discovery Database Info Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface DaDiscoveryTableConvert {
    DaDiscoveryTableConvert INSTANCE = Mappers.getMapper(DaDiscoveryTableConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daDiscoveryTablePageReqVO request parameters
     * @return DaDiscoveryTableDO
     */
     DaDiscoveryTableDO convertToDO(DaDiscoveryTablePageReqVO daDiscoveryTablePageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daDiscoveryTableSaveReqVO save request parameters
     * @return DaDiscoveryTableDO
     */
     DaDiscoveryTableDO convertToDO(DaDiscoveryTableSaveReqVO daDiscoveryTableSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daDiscoveryTableDO entity object
     * @return DaDiscoveryTableRespVO
     */
     DaDiscoveryTableRespVO convertToRespVO(DaDiscoveryTableDO daDiscoveryTableDO);

    /**
     * Convert DOList to RespVOList
     * @param daDiscoveryTableDOList entity object list
     * @return List<DaDiscoveryTableRespVO>
     */
     List<DaDiscoveryTableRespVO> convertToRespVOList(List<DaDiscoveryTableDO> daDiscoveryTableDOList);
}
