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
 * 脱敏清单关联关系 Convert
 *
 * @author qdata
 * @date 2026-04-12
 */
@Mapper
public interface DgDesensitizeAssetcolumnConvert {
    DgDesensitizeAssetcolumnConvert INSTANCE = Mappers.getMapper(DgDesensitizeAssetcolumnConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDesensitizeAssetcolumnPageReqVO 请求参数
     * @return DgDesensitizeAssetcolumnDO
     */
     DgDesensitizeAssetcolumnDO convertToDO(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumnPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDesensitizeAssetcolumnSaveReqVO 保存请求参数
     * @return DgDesensitizeAssetcolumnDO
     */
     DgDesensitizeAssetcolumnDO convertToDO(DgDesensitizeAssetcolumnSaveReqVO dgDesensitizeAssetcolumnSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDesensitizeAssetcolumnDO 实体对象
     * @return DgDesensitizeAssetcolumnRespVO
     */
     DgDesensitizeAssetcolumnRespVO convertToRespVO(DgDesensitizeAssetcolumnDO dgDesensitizeAssetcolumnDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDesensitizeAssetcolumnDOList 实体对象列表
     * @return List<DgDesensitizeAssetcolumnRespVO>
     */
     List<DgDesensitizeAssetcolumnRespVO> convertToRespVOList(List<DgDesensitizeAssetcolumnDO> dgDesensitizeAssetcolumnDOList);
}
