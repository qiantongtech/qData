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
 * 元数据数据库与信息及字段信息关系-日志Mapper接口
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface McTableColumnRelLogMapper extends BaseMapperX<McTableColumnRelLogDO> {

    default PageResult<McTableColumnRelLogDO> selectPage(McTableColumnRelLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
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
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(McTableColumnRelLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
