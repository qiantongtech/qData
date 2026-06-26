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
 * 数据发现任务日志 Convert
 *
 * @author qdata
 * @date 2025-02-17
 */
@Mapper
public interface DaDiscoveryTaskLogConvert {
    DaDiscoveryTaskLogConvert INSTANCE = Mappers.getMapper(DaDiscoveryTaskLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daDiscoveryTaskLogPageReqVO 请求参数
     * @return DaDiscoveryTaskLogDO
     */
     DaDiscoveryTaskLogDO convertToDO(DaDiscoveryTaskLogPageReqVO daDiscoveryTaskLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daDiscoveryTaskLogSaveReqVO 保存请求参数
     * @return DaDiscoveryTaskLogDO
     */
     DaDiscoveryTaskLogDO convertToDO(DaDiscoveryTaskLogSaveReqVO daDiscoveryTaskLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daDiscoveryTaskLogDO 实体对象
     * @return DaDiscoveryTaskLogRespVO
     */
     DaDiscoveryTaskLogRespVO convertToRespVO(DaDiscoveryTaskLogDO daDiscoveryTaskLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param daDiscoveryTaskLogDOList 实体对象列表
     * @return List<DaDiscoveryTaskLogRespVO>
     */
     List<DaDiscoveryTaskLogRespVO> convertToRespVOList(List<DaDiscoveryTaskLogDO> daDiscoveryTaskLogDOList);
}
