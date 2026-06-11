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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;

import java.util.List;

/**
 * 数据发现库信息 Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface DaDiscoveryTableConvert {
    DaDiscoveryTableConvert INSTANCE = Mappers.getMapper(DaDiscoveryTableConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daDiscoveryTablePageReqVO 请求参数
     * @return DaDiscoveryTableDO
     */
     DaDiscoveryTableDO convertToDO(DaDiscoveryTablePageReqVO daDiscoveryTablePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daDiscoveryTableSaveReqVO 保存请求参数
     * @return DaDiscoveryTableDO
     */
     DaDiscoveryTableDO convertToDO(DaDiscoveryTableSaveReqVO daDiscoveryTableSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daDiscoveryTableDO 实体对象
     * @return DaDiscoveryTableRespVO
     */
     DaDiscoveryTableRespVO convertToRespVO(DaDiscoveryTableDO daDiscoveryTableDO);

    /**
     * DOList 转换为 RespVOList
     * @param daDiscoveryTableDOList 实体对象列表
     * @return List<DaDiscoveryTableRespVO>
     */
     List<DaDiscoveryTableRespVO> convertToRespVOList(List<DaDiscoveryTableDO> daDiscoveryTableDOList);
}
