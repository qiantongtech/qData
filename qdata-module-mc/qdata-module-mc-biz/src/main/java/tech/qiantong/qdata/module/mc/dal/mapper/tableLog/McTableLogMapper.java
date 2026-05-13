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
 * 元数据信息 - 日志Mapper接口
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface McTableLogMapper extends BaseMapperX<McTableLogDO> {

    default PageResult<McTableLogDO> selectPage(McTableLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
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
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(McTableLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
