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

package tech.qiantong.qdata.module.dg.convert.dataLevel;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;

/**
 * 数据分级 Convert
 *
 * @author qdata
 * @date 2026-04-03
 */
@Mapper
public interface DgDataLevelConvert {
    DgDataLevelConvert INSTANCE = Mappers.getMapper(DgDataLevelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDataLevelPageReqVO 请求参数
     * @return DgDataLevelDO
     */
     DgDataLevelDO convertToDO(DgDataLevelPageReqVO dgDataLevelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDataLevelSaveReqVO 保存请求参数
     * @return DgDataLevelDO
     */
     DgDataLevelDO convertToDO(DgDataLevelSaveReqVO dgDataLevelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDataLevelDO 实体对象
     * @return DgDataLevelRespVO
     */
     DgDataLevelRespVO convertToRespVO(DgDataLevelDO dgDataLevelDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDataLevelDOList 实体对象列表
     * @return List<DgDataLevelRespVO>
     */
     List<DgDataLevelRespVO> convertToRespVOList(List<DgDataLevelDO> dgDataLevelDOList);
}
