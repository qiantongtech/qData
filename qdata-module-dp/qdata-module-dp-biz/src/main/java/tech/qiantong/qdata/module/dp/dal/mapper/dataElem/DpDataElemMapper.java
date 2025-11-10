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

package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;

import java.util.Arrays;

import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数据元Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemMapper extends BaseMapperX<DpDataElemDO> {

    default PageResult<DpDataElemDO> selectPage(DpDataElemPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DpDataElemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDataElemDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_DATA_ELEM_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), DpDataElemDO::getName, reqVO.getName())
                .like(StringUtils.isNotBlank(reqVO.getEngName()), DpDataElemDO::getEngName, reqVO.getEngName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DpDataElemDO::getCatCode, reqVO.getCatCode())
                .eq(StringUtils.isNotBlank(reqVO.getType()), DpDataElemDO::getType, reqVO.getType())
                .eq(StringUtils.isNotBlank(reqVO.getColumnType()), DpDataElemDO::getColumnType, reqVO.getColumnType())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DpDataElemDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDocumentId()!= null, DpDataElemDO::getDocumentId, reqVO.getDocumentId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DpDataElemDO.class, lambdaWrapper);
    }
}
