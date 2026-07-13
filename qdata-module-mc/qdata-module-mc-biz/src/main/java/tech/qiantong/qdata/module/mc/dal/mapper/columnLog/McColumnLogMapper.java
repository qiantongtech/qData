package tech.qiantong.qdata.module.mc.dal.mapper.columnLog;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.columnLog.McColumnLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Metadata field information - Log Mapper interface
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface McColumnLogMapper extends BaseMapperX<McColumnLogDO> {

    default PageResult<McColumnLogDO> selectPage(McColumnLogPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<McColumnLogDO>()
                .eqIfPresent(McColumnLogDO::getDataType, reqVO.getDataType())
                .eqIfPresent(McColumnLogDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McColumnLogDO::getColumnId, reqVO.getColumnId())
                .eqIfPresent(McColumnLogDO::getVersion, reqVO.getVersion())
                .eqIfPresent(McColumnLogDO::getDbId, reqVO.getDbId())
                .eqIfPresent(McColumnLogDO::getTableId, reqVO.getTableId())
                .eqIfPresent(McColumnLogDO::getDatasourceId, reqVO.getDatasourceId())
                .eqIfPresent(McColumnLogDO::getSafetyLevelId, reqVO.getSafetyLevelId())
                .eqIfPresent(McColumnLogDO::getDataElemId, reqVO.getDataElemId())
                .likeIfPresent(McColumnLogDO::getColumnName, reqVO.getColumnName())
                .eqIfPresent(McColumnLogDO::getColumnComment, reqVO.getColumnComment())
                .eqIfPresent(McColumnLogDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(McColumnLogDO::getColumnLength, reqVO.getColumnLength())
                .eqIfPresent(McColumnLogDO::getColumnPrecision, reqVO.getColumnPrecision())
                .eqIfPresent(McColumnLogDO::getColumnScale, reqVO.getColumnScale())
                .eqIfPresent(McColumnLogDO::getDefaultValue, reqVO.getDefaultValue())
                .eqIfPresent(McColumnLogDO::getPkFlag, reqVO.getPkFlag())
                .eqIfPresent(McColumnLogDO::getFkFlag, reqVO.getFkFlag())
                .eqIfPresent(McColumnLogDO::getNullableFlag, reqVO.getNullableFlag())
                .eqIfPresent(McColumnLogDO::getBusDefinition, reqVO.getBusDefinition())
                .eqIfPresent(McColumnLogDO::getMeasuringUnit, reqVO.getMeasuringUnit())
                .eqIfPresent(McColumnLogDO::getDataQuality, reqVO.getDataQuality())
                .eqIfPresent(McColumnLogDO::getUpdateType, reqVO.getUpdateType())
                .eqIfPresent(McColumnLogDO::getUpdateMsg, reqVO.getUpdateMsg())
                .eqIfPresent(McColumnLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McColumnLogDO::getDescription, reqVO.getDescription())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(McColumnLogDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
