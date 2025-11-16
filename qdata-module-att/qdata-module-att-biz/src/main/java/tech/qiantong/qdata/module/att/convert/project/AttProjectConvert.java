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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.att.convert.project;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;

import java.util.List;

/**
 * 项目 Convert
 *
 * @author shu
 * @date 2025-01-20
 */
@Mapper
public interface AttProjectConvert {
    AttProjectConvert INSTANCE = Mappers.getMapper(AttProjectConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attProjectPageReqVO 请求参数
     * @return AttProjectDO
     */
     AttProjectDO convertToDO(AttProjectPageReqVO attProjectPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attProjectSaveReqVO 保存请求参数
     * @return AttProjectDO
     */
     AttProjectDO convertToDO(AttProjectSaveReqVO attProjectSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attProjectDO 实体对象
     * @return AttProjectRespVO
     */
     AttProjectRespVO convertToRespVO(AttProjectDO attProjectDO);

    /**
     * DOList 转换为 RespVOList
     * @param attProjectDOList 实体对象列表
     * @return List<AttProjectRespVO>
     */
     List<AttProjectRespVO> convertToRespVOList(List<AttProjectDO> attProjectDOList);
}
