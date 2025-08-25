package tech.qiantong.qdata.quality.dal.mapper.qa;


import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据质量任务-评测规则Mapper接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface DppQualityTaskEvaluateMapper extends BaseMapperX<DppQualityTaskEvaluateDO> {

    default PageResult<DppQualityTaskEvaluateDO> selectPage(DppQualityTaskEvaluatePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityTaskEvaluateDO>()
                .eqIfPresent(DppQualityTaskEvaluateDO::getTaskId, reqVO.getTaskId())
                .likeIfPresent(DppQualityTaskEvaluateDO::getName, reqVO.getName())
                .eqIfPresent(DppQualityTaskEvaluateDO::getRuleCode, reqVO.getRuleCode())
                .likeIfPresent(DppQualityTaskEvaluateDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(DppQualityTaskEvaluateDO::getWarningLevel, reqVO.getWarningLevel())
                .eqIfPresent(DppQualityTaskEvaluateDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppQualityTaskEvaluateDO::getRuleDescription, reqVO.getRuleDescription())
                .eqIfPresent(DppQualityTaskEvaluateDO::getErrDescription, reqVO.getErrDescription())
                .eqIfPresent(DppQualityTaskEvaluateDO::getSuggestion, reqVO.getSuggestion())
                .eqIfPresent(DppQualityTaskEvaluateDO::getWhereClause, reqVO.getWhereClause())
                .eqIfPresent(DppQualityTaskEvaluateDO::getObjId, reqVO.getObjId())
                .likeIfPresent(DppQualityTaskEvaluateDO::getObjName, reqVO.getObjName())
                .likeIfPresent(DppQualityTaskEvaluateDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DppQualityTaskEvaluateDO::getEvaColumn, reqVO.getEvaColumn())
                .eqIfPresent(DppQualityTaskEvaluateDO::getRule, reqVO.getRule())
                .eqIfPresent(DppQualityTaskEvaluateDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppQualityTaskEvaluateDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
