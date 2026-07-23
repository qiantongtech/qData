package tech.qiantong.qdata.module.mc.dal.mapper.tableColumnRelLog;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Metadata database and information and field information relationship - Log Mapper interface
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface McTableColumnRelLogMapper extends BaseMapperX<McTableColumnRelLogDO> {

    default PageResult<McTableColumnRelLogDO> selectPage(McTableColumnRelLogPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<McTableColumnRelLogDO>()
                .eqIfPresent(McTableColumnRelLogDO::getDataType, reqVO.getDataType())
                .eqIfPresent(McTableColumnRelLogDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McTableColumnRelLogDO::getDbId, reqVO.getDbId())
                .eqIfPresent(McTableColumnRelLogDO::getDbVersion, reqVO.getDbVersion())
                .eqIfPresent(McTableColumnRelLogDO::getTableId, reqVO.getTableId())
                .eqIfPresent(McTableColumnRelLogDO::getTableVersion, reqVO.getTableVersion())
                .eqIfPresent(McTableColumnRelLogDO::getColumnId, reqVO.getColumnId())
                .eqIfPresent(McTableColumnRelLogDO::getColumnVersion, reqVO.getColumnVersion())
                .eqIfPresent(McTableColumnRelLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McTableColumnRelLogDO::getDescription, reqVO.getDescription())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(McTableColumnRelLogDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
