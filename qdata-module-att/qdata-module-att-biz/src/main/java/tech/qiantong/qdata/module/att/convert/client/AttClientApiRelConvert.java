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

package tech.qiantong.qdata.module.att.convert.client;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;

import java.util.List;

/**
 * 应用API服务关联 Convert
 *
 * @author FXB
 * @date 2025-08-21
 */
@Mapper
public interface AttClientApiRelConvert {
    AttClientApiRelConvert INSTANCE = Mappers.getMapper(AttClientApiRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attClientApiRelPageReqVO 请求参数
     * @return AttClientApiRelDO
     */
     AttClientApiRelDO convertToDO(AttClientApiRelPageReqVO attClientApiRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attClientApiRelSaveReqVO 保存请求参数
     * @return AttClientApiRelDO
     */
     AttClientApiRelDO convertToDO(AttClientApiRelSaveReqVO attClientApiRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attClientApiRelDO 实体对象
     * @return AttClientApiRelRespVO
     */
     AttClientApiRelRespVO convertToRespVO(AttClientApiRelDO attClientApiRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param attClientApiRelDOList 实体对象列表
     * @return List<AttClientApiRelRespVO>
     */
     List<AttClientApiRelRespVO> convertToRespVOList(List<AttClientApiRelDO> attClientApiRelDOList);
}
