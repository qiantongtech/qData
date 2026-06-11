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

package tech.qiantong.qdata.module.da.convert.discovery;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;

import java.util.List;

/**
 * 数据发现字段 Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface DaDiscoveryColumnConvert {
    DaDiscoveryColumnConvert INSTANCE = Mappers.getMapper(DaDiscoveryColumnConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daDiscoveryColumnPageReqVO 请求参数
     * @return DaDiscoveryColumnDO
     */
     DaDiscoveryColumnDO convertToDO(DaDiscoveryColumnPageReqVO daDiscoveryColumnPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daDiscoveryColumnSaveReqVO 保存请求参数
     * @return DaDiscoveryColumnDO
     */
     DaDiscoveryColumnDO convertToDO(DaDiscoveryColumnSaveReqVO daDiscoveryColumnSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daDiscoveryColumnDO 实体对象
     * @return DaDiscoveryColumnRespVO
     */
     DaDiscoveryColumnRespVO convertToRespVO(DaDiscoveryColumnDO daDiscoveryColumnDO);

    /**
     * DOList 转换为 RespVOList
     * @param daDiscoveryColumnDOList 实体对象列表
     * @return List<DaDiscoveryColumnRespVO>
     */
     List<DaDiscoveryColumnRespVO> convertToRespVOList(List<DaDiscoveryColumnDO> daDiscoveryColumnDOList);
}
