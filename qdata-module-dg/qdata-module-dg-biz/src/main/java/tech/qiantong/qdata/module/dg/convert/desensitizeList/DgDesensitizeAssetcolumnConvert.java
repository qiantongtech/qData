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
