package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据集成节点Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlNodeMapper extends BaseMapperX<DppEtlNodeDO> {

    default PageResult<DppEtlNodeDO> selectPage(DppEtlNodePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlNodeDO>()
                .eqIfPresent(DppEtlNodeDO::getType, reqVO.getType())
                .likeIfPresent(DppEtlNodeDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlNodeDO::getCode, reqVO.getCode())
                .eqIfPresent(DppEtlNodeDO::getVersion, reqVO.getVersion())
                .eqIfPresent(DppEtlNodeDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlNodeDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlNodeDO::getParameters, reqVO.getParameters())
                .eqIfPresent(DppEtlNodeDO::getPriority, reqVO.getPriority())
                .eqIfPresent(DppEtlNodeDO::getFailRetryTimes, reqVO.getFailRetryTimes())
                .eqIfPresent(DppEtlNodeDO::getFailRetryInterval, reqVO.getFailRetryInterval())
                .eqIfPresent(DppEtlNodeDO::getTimeout, reqVO.getTimeout())
                .eqIfPresent(DppEtlNodeDO::getDelayTime, reqVO.getDelayTime())
                .eqIfPresent(DppEtlNodeDO::getCpuQuota, reqVO.getCpuQuota())
                .eqIfPresent(DppEtlNodeDO::getMemoryMax, reqVO.getMemoryMax())
                .eqIfPresent(DppEtlNodeDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppEtlNodeDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlNodeDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlNodeDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    void removeOldDppEtlNode(List<String> code);
}
