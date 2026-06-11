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

package tech.qiantong.qdata.module.att.convert.Tag;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;

import java.util.List;

/**
 * 标签管理 Convert
 *
 * @author qdata
 * @date 2025-07-11
 */
@Mapper
public interface AttTagConvert {
    AttTagConvert INSTANCE = Mappers.getMapper(AttTagConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attTagPageReqVO 请求参数
     * @return AttTagDO
     */
     AttTagDO convertToDO(AttTagPageReqVO attTagPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attTagSaveReqVO 保存请求参数
     * @return AttTagDO
     */
     AttTagDO convertToDO(AttTagSaveReqVO attTagSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attTagDO 实体对象
     * @return AttTagRespVO
     */
     AttTagRespVO convertToRespVO(AttTagDO attTagDO);

    /**
     * DOList 转换为 RespVOList
     * @param attTagDOList 实体对象列表
     * @return List<AttTagRespVO>
     */
     List<AttTagRespVO> convertToRespVOList(List<AttTagDO> attTagDOList);
}
