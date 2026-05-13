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
 * 采集任务实例-日志Mapper接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskInstanceLogMapper extends BaseMapperX<McTaskInstanceLogDO> {

    default PageResult<McTaskInstanceLogDO> selectPage(McTaskInstanceLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<McTaskInstanceLogDO>()
                .eqIfPresent(McTaskInstanceLogDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McTaskInstanceLogDO::getLogContent, reqVO.getLogContent())
                .eqIfPresent(McTaskInstanceLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(McTaskInstanceLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
