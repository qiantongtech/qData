package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成任务-日志Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlTaskLogMapper extends BaseMapperX<DppEtlTaskLogDO> {

    default PageResult<DppEtlTaskLogDO> selectPage(DppEtlTaskLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlTaskLogDO>()
                .eqIfPresent(DppEtlTaskLogDO::getType, reqVO.getType())
                .likeIfPresent(DppEtlTaskLogDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlTaskLogDO::getCode, reqVO.getCode())
                .eqIfPresent(DppEtlTaskLogDO::getVersion, reqVO.getVersion())
                .eqIfPresent(DppEtlTaskLogDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlTaskLogDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlTaskLogDO::getPersonCharge, reqVO.getPersonCharge())
                .eqIfPresent(DppEtlTaskLogDO::getLocations, reqVO.getLocations())
                .eqIfPresent(DppEtlTaskLogDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppEtlTaskLogDO::getTimeout, reqVO.getTimeout())
                .eqIfPresent(DppEtlTaskLogDO::getExtractionCount, reqVO.getExtractionCount())
                .eqIfPresent(DppEtlTaskLogDO::getWriteCount, reqVO.getWriteCount())
                .eqIfPresent(DppEtlTaskLogDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppEtlTaskLogDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlTaskLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlTaskLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    /**
     * 根据任务编码获取最大版本号
     *
     * @param taskCode
     * @return
     */
    Integer queryMaxVersionByCode(@Param("taskCode") String taskCode);
}
