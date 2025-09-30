package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogStatisticsVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEvaluateLogDO;
import java.util.Arrays;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.*;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 评测规则结果Mapper接口
 *
 * @author qdata
 * @date 2025-07-21
 */
public interface DppEvaluateLogMapper extends BaseMapperX<DppEvaluateLogDO> {

    default PageResult<DppEvaluateLogDO> selectPage(DppEvaluateLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEvaluateLogDO>()
                .likeIfPresent(DppEvaluateLogDO::getTableName, reqVO.getTableName())
                .likeIfPresent(DppEvaluateLogDO::getColumnName, reqVO.getColumnName())
                .eqIfPresent(DppEvaluateLogDO::getRuleCode, reqVO.getRuleCode())
                .likeIfPresent(DppEvaluateLogDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(DppEvaluateLogDO::getDimensionType, reqVO.getDimensionType())
                .eqIfPresent(DppEvaluateLogDO::getRuleDescription, reqVO.getRuleDescription())
                .eqIfPresent(DppEvaluateLogDO::getTaskLogId, reqVO.getTaskLogId())
                .eqIfPresent(DppEvaluateLogDO::getEvaluateId, reqVO.getEvaluateId())
                .eqIfPresent(DppEvaluateLogDO::getTotal, reqVO.getTotal())
                .eqIfPresent(DppEvaluateLogDO::getProblemTotal, reqVO.getProblemTotal())
                .eqIfPresent(DppEvaluateLogDO::getCheckDate, reqVO.getCheckDate())
                .eqIfPresent(DppEvaluateLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEvaluateLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }


    @Select(
            "SELECT " +
                    "    t.DIMENSION_TYPE            AS dimensionType, " +
                    "    count(1)           AS succesTotal, " +
                    "    SUM(t.TOTAL)                AS total, " +
                    "    SUM(t.PROBLEM_TOTAL)        AS problemTotal, " +
                    "    CASE " +
                    "        WHEN SUM(t.TOTAL) > 0 " +
                    "        THEN ROUND(CAST(SUM(t.PROBLEM_TOTAL) AS DECIMAL(18,6)) * 100 " +
                    "                   / CAST(SUM(t.TOTAL) AS DECIMAL(18,6)), 2) " +
                    "        ELSE 0 " +
                    "    END                         AS proportion, " +
                    "    NULL                        AS trendType " +
                    "FROM DPP_EVALUATE_LOG t " +
                    "WHERE t.TASK_LOG_ID = #{taskLogId} " +
                    "  AND t.DEL_FLAG = '0' " +
                    "  AND t.VALID_FLAG = '1' " +
                    "GROUP BY t.DIMENSION_TYPE " +
                    "ORDER BY t.DIMENSION_TYPE"
    )
    List<DppEvaluateLogStatisticsVO> selectDimStatsByTaskLogId(@Param("taskLogId") Long taskLogId);

    List<Map<String, Object>> getEvaluateTrend7d();

}
