package tech.qiantong.qdata.module.mc.dal.mapper.task;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Collection range Mapper interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskScopeMapper extends BaseMapperX<McTaskScopeDO> {

    default PageResult<McTaskScopeDO> selectPage(McTaskScopePageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<McTaskScopeDO>()
                .eqIfPresent(McTaskScopeDO::getTaskId, reqVO.getTaskId())
                .likeIfPresent(McTaskScopeDO::getDbName, reqVO.getDbName())
                .likeIfPresent(McTaskScopeDO::getSchemaName, reqVO.getSchemaName())
                .eqIfPresent(McTaskScopeDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McTaskScopeDO::getDescription, reqVO.getDescription())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(McTaskScopeDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
