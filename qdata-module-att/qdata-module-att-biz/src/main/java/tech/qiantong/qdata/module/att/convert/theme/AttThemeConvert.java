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
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
