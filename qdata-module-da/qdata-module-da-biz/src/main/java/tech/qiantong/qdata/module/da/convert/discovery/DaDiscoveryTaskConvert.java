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
 * 数据发现任务 Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface DaDiscoveryTaskConvert {
    DaDiscoveryTaskConvert INSTANCE = Mappers.getMapper(DaDiscoveryTaskConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daDiscoveryTaskPageReqVO 请求参数
     * @return DaDiscoveryTaskDO
     */
     DaDiscoveryTaskDO convertToDO(DaDiscoveryTaskPageReqVO daDiscoveryTaskPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daDiscoveryTaskSaveReqVO 保存请求参数
     * @return DaDiscoveryTaskDO
     */
     DaDiscoveryTaskDO convertToDO(DaDiscoveryTaskSaveReqVO daDiscoveryTaskSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daDiscoveryTaskDO 实体对象
     * @return DaDiscoveryTaskRespVO
     */
     DaDiscoveryTaskRespVO convertToRespVO(DaDiscoveryTaskDO daDiscoveryTaskDO);

    /**
     * DOList 转换为 RespVOList
     * @param daDiscoveryTaskDOList 实体对象列表
     * @return List<DaDiscoveryTaskRespVO>
     */
     List<DaDiscoveryTaskRespVO> convertToRespVOList(List<DaDiscoveryTaskDO> daDiscoveryTaskDOList);
}
