package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据质量日志Mapper接口
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface DppQualityLogMapper extends BaseMapperX<DppQualityLogDO> {

//    default PageResult<DppQualityLogDO> selectPage(DppQualityLogPageReqVO reqVO) {
//        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
//
//        // 构造动态查询条件
//        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityLogDO>()
//                .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
//                .eqIfPresent(DppQualityLogDO::getSuccessFlag, reqVO.getSuccessFlag())
//                .eqIfPresent(DppQualityLogDO::getStartTime, reqVO.getStartTime())
//                .eqIfPresent(DppQualityLogDO::getEndTime, reqVO.getEndTime())
//                .eqIfPresent(DppQualityLogDO::getQualityId, reqVO.getQualityId())
//                .eqIfPresent(DppQualityLogDO::getScore, reqVO.getScore())
//                .eqIfPresent(DppQualityLogDO::getProblemData, reqVO.getProblemData())
//                .eqIfPresent(DppQualityLogDO::getCreateTime, reqVO.getCreateTime())
//                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
//                // .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
//                // 按照 createTime 字段降序排序
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
        // 动态排序处理
        String orderByColumn = reqVO.getOrderByColumn();
        Boolean isAsc = StringUtils.equals("asc", reqVO.getIsAsc());
        if (StringUtils.isNotBlank(orderByColumn) && allowedColumns.contains(orderByColumn)) {
            wrapper.orderBy(true, Boolean.TRUE.equals(isAsc), orderByColumn);
        }
        return selectPage(reqVO, wrapper);
    }

}
