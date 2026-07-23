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

package tech.qiantong.qdata.module.dg.convert.dataLevel;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;

/**
 * Data Level Convert
 *
 * @author qdata
 * @date 2026-04-03
 */
@Mapper
public interface DgDataLevelConvert {
    DgDataLevelConvert INSTANCE = Mappers.getMapper(DgDataLevelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dgDataLevelPageReqVO request params
     * @return DgDataLevelDO
     */
     DgDataLevelDO convertToDO(DgDataLevelPageReqVO dgDataLevelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dgDataLevelSaveReqVO save request params
     * @return DgDataLevelDO
     */
     DgDataLevelDO convertToDO(DgDataLevelSaveReqVO dgDataLevelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dgDataLevelDO entity object
     * @return DgDataLevelRespVO
     */
     DgDataLevelRespVO convertToRespVO(DgDataLevelDO dgDataLevelDO);

    /**
     * Convert DO List to RespVO List
     * @param dgDataLevelDOList entity object list
     * @return List<DgDataLevelRespVO>
     */
     List<DgDataLevelRespVO> convertToRespVOList(List<DgDataLevelDO> dgDataLevelDOList);
}
