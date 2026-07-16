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
 * Data integration scheduling information Mapper interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskSchedulerMapper extends BaseMapperX<McTaskSchedulerDO> {

    default PageResult<McTaskSchedulerDO> selectPage(McTaskSchedulerPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
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
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(McTaskSchedulerDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
