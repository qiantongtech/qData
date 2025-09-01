package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceLogPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成节点实例-日志Mapper接口
 *
 * @author qdata
 * @date 2025-08-05
 */
public interface DppEtlNodeInstanceLogMapper extends BaseMapperX<DppEtlNodeInstanceLogDO> {

    default PageResult<DppEtlNodeInstanceLogDO> selectPage(DppEtlNodeInstanceLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlNodeInstanceLogDO>()
                .eqIfPresent(DppEtlNodeInstanceLogDO::getTaskType, reqVO.getTaskType())
                .eqIfPresent(DppEtlNodeInstanceLogDO::getNodeId, reqVO.getNodeId())
                .eqIfPresent(DppEtlNodeInstanceLogDO::getNodeCode, reqVO.getNodeCode())
                .eqIfPresent(DppEtlNodeInstanceLogDO::getTaskInstanceId, reqVO.getTaskInstanceId())
                .eqIfPresent(DppEtlNodeInstanceLogDO::getLogContent, reqVO.getLogContent())
                .eqIfPresent(DppEtlNodeInstanceLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlNodeInstanceLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
