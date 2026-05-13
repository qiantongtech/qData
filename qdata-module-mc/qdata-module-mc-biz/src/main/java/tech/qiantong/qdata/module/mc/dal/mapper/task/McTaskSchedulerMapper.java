package tech.qiantong.qdata.module.mc.dal.mapper.task;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成调度信息Mapper接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskSchedulerMapper extends BaseMapperX<McTaskSchedulerDO> {

    default PageResult<McTaskSchedulerDO> selectPage(McTaskSchedulerPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<McTaskSchedulerDO>()
                .eqIfPresent(McTaskSchedulerDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McTaskSchedulerDO::getJobId, reqVO.getJobId())
                .eqIfPresent(McTaskSchedulerDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(McTaskSchedulerDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(McTaskSchedulerDO::getTimezoneId, reqVO.getTimezoneId())
                .eqIfPresent(McTaskSchedulerDO::getCronExpression, reqVO.getCronExpression())
                .eqIfPresent(McTaskSchedulerDO::getFailureStrategy, reqVO.getFailureStrategy())
                .eqIfPresent(McTaskSchedulerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(McTaskSchedulerDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(McTaskSchedulerDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
