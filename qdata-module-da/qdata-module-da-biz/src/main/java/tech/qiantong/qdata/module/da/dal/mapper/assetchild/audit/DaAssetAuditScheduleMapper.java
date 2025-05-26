package tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 资产稽查调度Mapper接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetAuditScheduleMapper extends BaseMapperX<DaAssetAuditScheduleDO> {

    default PageResult<DaAssetAuditScheduleDO> selectPage(DaAssetAuditSchedulePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetAuditScheduleDO>()
                .eqIfPresent(DaAssetAuditScheduleDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetAuditScheduleDO::getScheduleFlag, reqVO.getScheduleFlag())
                .eqIfPresent(DaAssetAuditScheduleDO::getCronExpression, reqVO.getCronExpression())
                .eqIfPresent(DaAssetAuditScheduleDO::getNodeId, reqVO.getNodeId())
                .eqIfPresent(DaAssetAuditScheduleDO::getNodeCode, reqVO.getNodeCode())
                .eqIfPresent(DaAssetAuditScheduleDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DaAssetAuditScheduleDO::getTaskCode, reqVO.getTaskCode())
                .eqIfPresent(DaAssetAuditScheduleDO::getSystemJobId, reqVO.getSystemJobId())
                .eqIfPresent(DaAssetAuditScheduleDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetAuditScheduleDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
