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

package tech.qiantong.qdata.module.att.convert.Rel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;

import java.util.List;

/**
 * 标签与资产关联关系 Convert
 *
 * @author qdata
 * @date 2025-07-11
 */
@Mapper
public interface AttTagAssetRelConvert {
    AttTagAssetRelConvert INSTANCE = Mappers.getMapper(AttTagAssetRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attTagAssetRelPageReqVO 请求参数
     * @return AttTagAssetRelDO
     */
     AttTagAssetRelDO convertToDO(AttTagAssetRelPageReqVO attTagAssetRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attTagAssetRelSaveReqVO 保存请求参数
     * @return AttTagAssetRelDO
     */
     AttTagAssetRelDO convertToDO(AttTagAssetRelSaveReqVO attTagAssetRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attTagAssetRelDO 实体对象
     * @return AttTagAssetRelRespVO
     */
     AttTagAssetRelRespVO convertToRespVO(AttTagAssetRelDO attTagAssetRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param attTagAssetRelDOList 实体对象列表
     * @return List<AttTagAssetRelRespVO>
     */
     List<AttTagAssetRelRespVO> convertToRespVOList(List<AttTagAssetRelDO> attTagAssetRelDOList);
}
