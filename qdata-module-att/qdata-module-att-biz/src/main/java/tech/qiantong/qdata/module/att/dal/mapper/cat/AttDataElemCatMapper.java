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

package tech.qiantong.qdata.module.att.dal.mapper.cat;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;

import tech.qiantong.qdata.common.core.page.PageResult;

import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数据元类目管理Mapper接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttDataElemCatMapper extends BaseMapperX<AttDataElemCatDO> {

    default PageResult<AttDataElemCatDO> selectPage(AttDataElemCatPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttDataElemCatDO>()
                .likeIfPresent(AttDataElemCatDO::getName, reqVO.getName())
                .eqIfPresent(AttDataElemCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttDataElemCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttDataElemCatDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(AttDataElemCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttDataElemCatDO::getCreateTime, reqVO.getCreateTime())

                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttDataElemCatDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
                .orderByAsc(AttDataElemCatDO::getSortOrder));
    }


    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
