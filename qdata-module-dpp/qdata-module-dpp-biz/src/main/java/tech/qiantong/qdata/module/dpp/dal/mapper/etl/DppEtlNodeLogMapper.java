package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成节点-日志Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlNodeLogMapper extends BaseMapperX<DppEtlNodeLogDO> {

    default PageResult<DppEtlNodeLogDO> selectPage(DppEtlNodeLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlNodeLogDO>()
                .eqIfPresent(DppEtlNodeLogDO::getType, reqVO.getType())
                .likeIfPresent(DppEtlNodeLogDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlNodeLogDO::getCode, reqVO.getCode())
                .eqIfPresent(DppEtlNodeLogDO::getVersion, reqVO.getVersion())
                .eqIfPresent(DppEtlNodeLogDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlNodeLogDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlNodeLogDO::getParameters, reqVO.getParameters())
                .eqIfPresent(DppEtlNodeLogDO::getPriority, reqVO.getPriority())
                .eqIfPresent(DppEtlNodeLogDO::getFailRetryTimes, reqVO.getFailRetryTimes())
                .eqIfPresent(DppEtlNodeLogDO::getFailRetryInterval, reqVO.getFailRetryInterval())
                .eqIfPresent(DppEtlNodeLogDO::getTimeout, reqVO.getTimeout())
                .eqIfPresent(DppEtlNodeLogDO::getDelayTime, reqVO.getDelayTime())
                .eqIfPresent(DppEtlNodeLogDO::getCpuQuota, reqVO.getCpuQuota())
                .eqIfPresent(DppEtlNodeLogDO::getMemoryMax, reqVO.getMemoryMax())
                .eqIfPresent(DppEtlNodeLogDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppEtlNodeLogDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlNodeLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlNodeLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    Integer getMaxVersionByNodeCode(@Param("nodeCode") String nodeCode);
}
