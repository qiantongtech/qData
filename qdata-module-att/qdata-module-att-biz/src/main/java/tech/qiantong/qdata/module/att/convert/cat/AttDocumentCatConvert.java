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

package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;

import java.util.List;

/**
 * 标准信息分类管理 Convert
 *
 * @author qdata
 * @date 2025-08-21
 */
@Mapper
public interface AttDocumentCatConvert {
    AttDocumentCatConvert INSTANCE = Mappers.getMapper(AttDocumentCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attDocumentCatPageReqVO 请求参数
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatPageReqVO attDocumentCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attDocumentCatSaveReqVO 保存请求参数
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatSaveReqVO attDocumentCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attDocumentCatDO 实体对象
     * @return AttDocumentCatRespVO
     */
     AttDocumentCatRespVO convertToRespVO(AttDocumentCatDO attDocumentCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attDocumentCatDOList 实体对象列表
     * @return List<AttDocumentCatRespVO>
     */
     List<AttDocumentCatRespVO> convertToRespVOList(List<AttDocumentCatDO> attDocumentCatDOList);
}
