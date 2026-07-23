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

package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;

import java.util.*;

import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;

import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * Data Element Mapper Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemMapper extends BaseMapperX<DpDataElemDO> {

    default PageResult<DpDataElemDO> selectPage(DpDataElemPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
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

    /**
     * Check if the current data element is used by models and assets
     *
     * @param idList
     * @return
     */
    Long checkHasRel(@Param("idList") List<Long> idList);
}
