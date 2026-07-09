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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskLogDO;

import java.util.List;

/**
 * Data Discovery Task Log Convert
 *
 * @author qdata
 * @date 2025-02-17
 */
@Mapper
public interface DaDiscoveryTaskLogConvert {
    DaDiscoveryTaskLogConvert INSTANCE = Mappers.getMapper(DaDiscoveryTaskLogConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daDiscoveryTaskLogPageReqVO request parameters
     * @return DaDiscoveryTaskLogDO
     */
     DaDiscoveryTaskLogDO convertToDO(DaDiscoveryTaskLogPageReqVO daDiscoveryTaskLogPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daDiscoveryTaskLogSaveReqVO save request parameters
     * @return DaDiscoveryTaskLogDO
     */
     DaDiscoveryTaskLogDO convertToDO(DaDiscoveryTaskLogSaveReqVO daDiscoveryTaskLogSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daDiscoveryTaskLogDO entity object
     * @return DaDiscoveryTaskLogRespVO
     */
     DaDiscoveryTaskLogRespVO convertToRespVO(DaDiscoveryTaskLogDO daDiscoveryTaskLogDO);

    /**
     * Convert DOList to RespVOList
     * @param daDiscoveryTaskLogDOList entity object list
     * @return List<DaDiscoveryTaskLogRespVO>
     */
     List<DaDiscoveryTaskLogRespVO> convertToRespVOList(List<DaDiscoveryTaskLogDO> daDiscoveryTaskLogDOList);
}
