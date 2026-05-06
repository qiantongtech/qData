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
 * 采集范围Mapper接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskScopeMapper extends BaseMapperX<McTaskScopeDO> {

    default PageResult<McTaskScopeDO> selectPage(McTaskScopePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<McTaskScopeDO>()
                .eqIfPresent(McTaskScopeDO::getTaskId, reqVO.getTaskId())
                .likeIfPresent(McTaskScopeDO::getDbName, reqVO.getDbName())
                .likeIfPresent(McTaskScopeDO::getSchemaName, reqVO.getSchemaName())
                .eqIfPresent(McTaskScopeDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McTaskScopeDO::getDescription, reqVO.getDescription())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(McTaskScopeDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
