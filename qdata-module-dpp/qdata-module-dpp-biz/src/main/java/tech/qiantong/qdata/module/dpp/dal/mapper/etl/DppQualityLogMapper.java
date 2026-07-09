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

package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * Data Quality Log Mapper
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface DppQualityLogMapper extends BaseMapperX<DppQualityLogDO> {

//    default PageResult<DppQualityLogDO> selectPage(DppQualityLogPageReqVO reqVO) {
//        // Define sortable fields (prevent SQL injection, must match database column names)
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
//
//        // Build dynamic query conditions
//        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityLogDO>()
//                .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
//                .eqIfPresent(DppQualityLogDO::getSuccessFlag, reqVO.getSuccessFlag())
//                .eqIfPresent(DppQualityLogDO::getStartTime, reqVO.getStartTime())
//                .eqIfPresent(DppQualityLogDO::getEndTime, reqVO.getEndTime())
//                .eqIfPresent(DppQualityLogDO::getQualityId, reqVO.getQualityId())
//                .eqIfPresent(DppQualityLogDO::getScore, reqVO.getScore())
//                .eqIfPresent(DppQualityLogDO::getProblemData, reqVO.getProblemData())
//                .eqIfPresent(DppQualityLogDO::getCreateTime, reqVO.getCreateTime())
//                // If reqVO.getName() is not empty, add exact name match condition (name = '<name>')
//                // .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
//                // Order by createTime descending
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
//    }

    default PageResult<DppQualityLogDO> selectPage(DppQualityLogPageReqVO reqVO) {
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapperX<DppQualityLogDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.selectAll(DppQualityLogDO.class)
                .innerJoin("DPP_QUALITY_TASK t2 ON t.QUALITY_ID = t2.ID AND t2.DEL_FLAG = '0' AND t2.ASSET_FLAG = '0'")
                .likeIfExists(DppQualityLogDO::getName, reqVO.getName())
                .eqIfExists(DppQualityLogDO::getSuccessFlag, reqVO.getSuccessFlag())
                .eqIfExists(DppQualityLogDO::getStartTime, reqVO.getStartTime())
                .eqIfExists(DppQualityLogDO::getEndTime, reqVO.getEndTime())
                .eqIfExists(DppQualityLogDO::getQualityId, reqVO.getQualityId())
                .eqIfExists(DppQualityLogDO::getScore, reqVO.getScore())
                .eqIfExists(DppQualityLogDO::getProblemData, reqVO.getProblemData())
                .eqIfExists(DppQualityLogDO::getCreateTime, reqVO.getCreateTime());
        // Dynamic sort handling
        String orderByColumn = reqVO.getOrderByColumn();
        Boolean isAsc = StringUtils.equals("asc", reqVO.getIsAsc());
        if (StringUtils.isNotBlank(orderByColumn) && allowedColumns.contains(orderByColumn)) {
            wrapper.orderBy(true, Boolean.TRUE.equals(isAsc), orderByColumn);
        }
        return selectPage(reqVO, wrapper);
    }

    default DppQualityLogDO selectPrevLogByIdWithWrapper(String id) {
        // 1) Get key fields of the current record first
        DppQualityLogDO cur = this.selectById(id);
        if (cur == null || cur.getQualityId() == null || cur.getStartTime() == null) {
            return null;
        }

        // 2) Build wrapper: same QUALITY_ID, get the next earlier one
        MPJLambdaWrapperX<DppQualityLogDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.selectAll(DppQualityLogDO.class)
                .eq(DppQualityLogDO::getQualityId, cur.getQualityId())
                .eq(DppQualityLogDO::getDelFlag, "0")
                .eq(DppQualityLogDO::getValidFlag, "1")
                // (start_time < current) OR (start_time = current AND id <> current)
                .and(w -> w.lt(DppQualityLogDO::getStartTime, cur.getStartTime())
                        .or(x -> x.eq(DppQualityLogDO::getStartTime, cur.getStartTime())
                                .ne(DppQualityLogDO::getId, id)))
                // Order by time descending to get the latest one before current
                .orderByDesc(DppQualityLogDO::getStartTime,
                        DppQualityLogDO::getEndTime,
                        DppQualityLogDO::getUpdateTime);

        // 3) Use pagination to get only one record (compatible with existing selectPage(reqVO, wrapper))
        DppQualityLogPageReqVO req = new DppQualityLogPageReqVO();
        req.setPageNum(1);
        req.setPageSize(1);

        PageResult<DppQualityLogDO> page = selectPage(req, wrapper);
        return (page == null || page.getRows() == null || page.getRows().isEmpty())
                ? null
                : (DppQualityLogDO)page.getRows().get(0);
    }
}
