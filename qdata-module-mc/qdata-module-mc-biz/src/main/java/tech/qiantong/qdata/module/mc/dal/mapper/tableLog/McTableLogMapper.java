package tech.qiantong.qdata.module.mc.dal.mapper.tableLog;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Metadata information - Log Mapper interface
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface McTableLogMapper extends BaseMapperX<McTableLogDO> {

    default PageResult<McTableLogDO> selectPage(McTableLogPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<McTableLogDO>()
                .eqIfPresent(McTableLogDO::getDataType, reqVO.getDataType())
                .eqIfPresent(McTableLogDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McTableLogDO::getTableId, reqVO.getTableId())
                .eqIfPresent(McTableLogDO::getVersion, reqVO.getVersion())
                .eqIfPresent(McTableLogDO::getDbId, reqVO.getDbId())
                .eqIfPresent(McTableLogDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(McTableLogDO::getTableName, reqVO.getTableName())
                .eqIfPresent(McTableLogDO::getTableComment, reqVO.getTableComment())
                .eqIfPresent(McTableLogDO::getSafetyLevelId, reqVO.getSafetyLevelId())
                .likeIfPresent(McTableLogDO::getDbName, reqVO.getDbName())
                .likeIfPresent(McTableLogDO::getSchemaName, reqVO.getSchemaName())
                .eqIfPresent(McTableLogDO::getStorageType, reqVO.getStorageType())
                .eqIfPresent(McTableLogDO::getStorageSize, reqVO.getStorageSize())
                .eqIfPresent(McTableLogDO::getBusinessLeader, reqVO.getBusinessLeader())
                .eqIfPresent(McTableLogDO::getBusinessLeaderPhone, reqVO.getBusinessLeaderPhone())
                .eqIfPresent(McTableLogDO::getTechLeader, reqVO.getTechLeader())
                .eqIfPresent(McTableLogDO::getTechLeaderPhone, reqVO.getTechLeaderPhone())
                .eqIfPresent(McTableLogDO::getMasterFlag, reqVO.getMasterFlag())
                .eqIfPresent(McTableLogDO::getTempFlag, reqVO.getTempFlag())
                .eqIfPresent(McTableLogDO::getDataQuality, reqVO.getDataQuality())
                .eqIfPresent(McTableLogDO::getUpdateType, reqVO.getUpdateType())
                .eqIfPresent(McTableLogDO::getUpdateMsg, reqVO.getUpdateMsg())
                .eqIfPresent(McTableLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McTableLogDO::getDescription, reqVO.getDescription())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(McTableLogDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
