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
