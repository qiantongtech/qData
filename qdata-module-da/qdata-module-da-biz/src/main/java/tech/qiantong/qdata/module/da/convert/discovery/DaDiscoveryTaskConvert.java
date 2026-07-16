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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskDO;

import java.util.List;

/**
 * Data Discovery Task Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface DaDiscoveryTaskConvert {
    DaDiscoveryTaskConvert INSTANCE = Mappers.getMapper(DaDiscoveryTaskConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daDiscoveryTaskPageReqVO request parameters
     * @return DaDiscoveryTaskDO
     */
     DaDiscoveryTaskDO convertToDO(DaDiscoveryTaskPageReqVO daDiscoveryTaskPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daDiscoveryTaskSaveReqVO save request parameters
     * @return DaDiscoveryTaskDO
     */
     DaDiscoveryTaskDO convertToDO(DaDiscoveryTaskSaveReqVO daDiscoveryTaskSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daDiscoveryTaskDO entity object
     * @return DaDiscoveryTaskRespVO
     */
     DaDiscoveryTaskRespVO convertToRespVO(DaDiscoveryTaskDO daDiscoveryTaskDO);

    /**
     * Convert DOList to RespVOList
     * @param daDiscoveryTaskDOList entity object list
     * @return List<DaDiscoveryTaskRespVO>
     */
     List<DaDiscoveryTaskRespVO> convertToRespVOList(List<DaDiscoveryTaskDO> daDiscoveryTaskDOList);
}
