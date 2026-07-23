package tech.qiantong.qdata.module.mc.dal.mapper.task;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Collection task instance-Log Mapper interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskInstanceLogMapper extends BaseMapperX<McTaskInstanceLogDO> {

    default PageResult<McTaskInstanceLogDO> selectPage(McTaskInstanceLogPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<McTaskInstanceLogDO>()
                .eqIfPresent(McTaskInstanceLogDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McTaskInstanceLogDO::getLogContent, reqVO.getLogContent())
                .eqIfPresent(McTaskInstanceLogDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(McTaskInstanceLogDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
