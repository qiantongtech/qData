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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;

import java.util.List;

/**
 * Data Discovery Column Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface DaDiscoveryColumnConvert {
    DaDiscoveryColumnConvert INSTANCE = Mappers.getMapper(DaDiscoveryColumnConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daDiscoveryColumnPageReqVO request parameters
     * @return DaDiscoveryColumnDO
     */
     DaDiscoveryColumnDO convertToDO(DaDiscoveryColumnPageReqVO daDiscoveryColumnPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daDiscoveryColumnSaveReqVO save request parameters
     * @return DaDiscoveryColumnDO
     */
     DaDiscoveryColumnDO convertToDO(DaDiscoveryColumnSaveReqVO daDiscoveryColumnSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daDiscoveryColumnDO entity object
     * @return DaDiscoveryColumnRespVO
     */
     DaDiscoveryColumnRespVO convertToRespVO(DaDiscoveryColumnDO daDiscoveryColumnDO);

    /**
     * Convert DOList to RespVOList
     * @param daDiscoveryColumnDOList entity object list
     * @return List<DaDiscoveryColumnRespVO>
     */
     List<DaDiscoveryColumnRespVO> convertToRespVOList(List<DaDiscoveryColumnDO> daDiscoveryColumnDOList);
}
