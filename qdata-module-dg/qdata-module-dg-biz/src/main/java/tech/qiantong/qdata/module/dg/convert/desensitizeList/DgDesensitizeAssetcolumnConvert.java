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

package tech.qiantong.qdata.module.dg.convert.desensitizeList;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;

/**
 * Desensitization List Association Convert
 *
 * @author qdata
 * @date 2026-04-12
 */
@Mapper
public interface DgDesensitizeAssetcolumnConvert {
    DgDesensitizeAssetcolumnConvert INSTANCE = Mappers.getMapper(DgDesensitizeAssetcolumnConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dgDesensitizeAssetcolumnPageReqVO request parameters
     * @return DgDesensitizeAssetcolumnDO
     */
     DgDesensitizeAssetcolumnDO convertToDO(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumnPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dgDesensitizeAssetcolumnSaveReqVO save request parameters
     * @return DgDesensitizeAssetcolumnDO
     */
     DgDesensitizeAssetcolumnDO convertToDO(DgDesensitizeAssetcolumnSaveReqVO dgDesensitizeAssetcolumnSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dgDesensitizeAssetcolumnDO entity object
     * @return DgDesensitizeAssetcolumnRespVO
     */
     DgDesensitizeAssetcolumnRespVO convertToRespVO(DgDesensitizeAssetcolumnDO dgDesensitizeAssetcolumnDO);

    /**
     * Convert DO List to RespVO List
     * @param dgDesensitizeAssetcolumnDOList entity object list
     * @return List<DgDesensitizeAssetcolumnRespVO>
     */
     List<DgDesensitizeAssetcolumnRespVO> convertToRespVOList(List<DgDesensitizeAssetcolumnDO> dgDesensitizeAssetcolumnDOList);
}
