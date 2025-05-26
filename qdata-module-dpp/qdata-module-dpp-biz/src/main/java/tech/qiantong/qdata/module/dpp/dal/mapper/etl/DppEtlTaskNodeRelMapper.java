package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成任务节点关系Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlTaskNodeRelMapper extends BaseMapperX<DppEtlTaskNodeRelDO> {

    default PageResult<DppEtlTaskNodeRelDO> selectPage(DppEtlTaskNodeRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlTaskNodeRelDO>()
                .eqIfPresent(DppEtlTaskNodeRelDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlTaskNodeRelDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlTaskNodeRelDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DppEtlTaskNodeRelDO::getTaskCode, reqVO.getTaskCode())
                .eqIfPresent(DppEtlTaskNodeRelDO::getTaskVersion, reqVO.getTaskVersion())
                .eqIfPresent(DppEtlTaskNodeRelDO::getPreNodeId, reqVO.getPreNodeId())
                .eqIfPresent(DppEtlTaskNodeRelDO::getPreNodeCode, reqVO.getPreNodeCode())
                .eqIfPresent(DppEtlTaskNodeRelDO::getPreNodeVersion, reqVO.getPreNodeVersion())
                .eqIfPresent(DppEtlTaskNodeRelDO::getPostNodeId, reqVO.getPostNodeId())
                .eqIfPresent(DppEtlTaskNodeRelDO::getPostNodeCode, reqVO.getPostNodeCode())
                .eqIfPresent(DppEtlTaskNodeRelDO::getPostNodeVersion, reqVO.getPostNodeVersion())
                .eqIfPresent(DppEtlTaskNodeRelDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlTaskNodeRelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
