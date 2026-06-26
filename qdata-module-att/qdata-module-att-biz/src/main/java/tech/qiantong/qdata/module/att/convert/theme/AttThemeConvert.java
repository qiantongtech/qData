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

package tech.qiantong.qdata.module.att.convert.theme;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeRespVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;

import java.util.List;

/**
 * 主题 Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttThemeConvert {
    AttThemeConvert INSTANCE = Mappers.getMapper(AttThemeConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attThemePageReqVO 请求参数
     * @return AttThemeDO
     */
     AttThemeDO convertToDO(AttThemePageReqVO attThemePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attThemeSaveReqVO 保存请求参数
     * @return AttThemeDO
     */
     AttThemeDO convertToDO(AttThemeSaveReqVO attThemeSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attThemeDO 实体对象
     * @return AttThemeRespVO
     */
     AttThemeRespVO convertToRespVO(AttThemeDO attThemeDO);

    /**
     * DOList 转换为 RespVOList
     * @param attThemeDOList 实体对象列表
     * @return List<AttThemeRespVO>
     */
     List<AttThemeRespVO> convertToRespVOList(List<AttThemeDO> attThemeDOList);
}
