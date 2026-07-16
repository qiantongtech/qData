package tech.qiantong.qdata.module.mc.dal.mapper.metadata;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.BizDataScopeQueryHelper;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Metadata field information Mapper interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface McColumnMapper extends BaseMapperX<McColumnDO> {

    default PageResult<McColumnDO> selectPage(McColumnPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        boolean selfScopeWithUnassigned = BizDataScopeQueryHelper.useSelfScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getBusinessLeader());
        boolean deptScopeWithUnassigned = BizDataScopeQueryHelper.useDeptScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getResponsibleDept());
        Set<String> allowedColumns = Sets.newHashSet("id", "create_time", "update_time", "column_length",
                "column_precision", "column_scale", "audit_time", "data_quality");
        MPJLambdaWrapperX<McColumnDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(McColumnDO.class)
                //.select("(SELECT e.NAME FROM DG_DATA_ELEM e WHERE e.ID = t.DATA_ELEM_ID) dataElemName")
                .select("t2.TABLE_NAME tableName")
                .select("t2.DB_NAME dbName")
                .select("d.source_system_id")
                .leftJoin("MC_TABLE t2 on t.TABLE_ID = t2.ID")
                .leftJoin("MC_DB d on t.DB_ID=d.id");

        lambdaWrapperX.eqIfPresent(McColumnDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McColumnDO::getDbId, reqVO.getDbId())
                .eqIfPresent(McColumnDO::getTableId, reqVO.getTableId())
                .eqIfPresent(McColumnDO::getDatasourceId, reqVO.getDatasourceId())
                .eqIfPresent(McColumnDO::getVersion, reqVO.getVersion())
                .eqIfPresent(McColumnDO::getSafetyLevelId, reqVO.getSafetyLevelId())
                .eqIfPresent(McColumnDO::getDataElemId, reqVO.getDataElemId())
                .likeIfPresent(McColumnDO::getColumnName, reqVO.getColumnName())
                .likeIfPresent(McColumnDO::getColumnComment, reqVO.getColumnComment())
                .eqIfPresent(McColumnDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(McColumnDO::getColumnLength, reqVO.getColumnLength())
                .eqIfPresent(McColumnDO::getColumnPrecision, reqVO.getColumnPrecision())
                .eqIfPresent(McColumnDO::getPortalVisible, reqVO.getPortalVisible())
                .eqIfPresent(McColumnDO::getColumnScale, reqVO.getColumnScale())
                .eqIfPresent(McColumnDO::getDefaultValue, reqVO.getDefaultValue())
                .eqIfPresent(McColumnDO::getPkFlag, reqVO.getPkFlag())
                .eqIfPresent(McColumnDO::getFkFlag, reqVO.getFkFlag())
                .eqIfPresent(McColumnDO::getNullableFlag, reqVO.getNullableFlag())
                .eqIfPresent(McColumnDO::getBusinessDefinition, reqVO.getBusinessDefinition())
                .eqIfPresent(McColumnDO::getMeasuringUnit, reqVO.getMeasuringUnit())
                .eqIfPresent(McColumnDO::getDataQuality, reqVO.getDataQuality())
                .eqIfPresent(McColumnDO::getAuditStatus, reqVO.getAuditStatus())
                .eqIfPresent(McColumnDO::getAuditTime, reqVO.getAuditTime())
                .eqIfPresent(McColumnDO::getStatus, reqVO.getStatus())
                .eqIfPresent(McColumnDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McColumnDO::getDescription, reqVO.getDescription())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);
        lambdaWrapperX.apply(selfScopeWithUnassigned, "(t.BUSINESS_LEADER = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getBusinessLeader());
        lambdaWrapperX.eq(!selfScopeWithUnassigned && reqVO.getBusinessLeader() != null, McColumnDO::getBusinessLeader, reqVO.getBusinessLeader());
        lambdaWrapperX.apply(deptScopeWithUnassigned, "(t.RESPONSIBLE_DEPT = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getResponsibleDept());
        lambdaWrapperX.eq(!deptScopeWithUnassigned && reqVO.getResponsibleDept() != null, McColumnDO::getResponsibleDept, reqVO.getResponsibleDept());

        if (!"0".equals(reqVO.getSourceSystemName())) {
            lambdaWrapperX.likeRightIfExists("d", McDbDO::getSourceSystemName, reqVO.getSourceSystemName());
        }
        // Construct dynamic query conditions
        return selectPage(reqVO, lambdaWrapperX);
    }

    default McColumnDO findById(Long id) {
        MPJLambdaWrapperX<McColumnDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(McColumnDO.class)
                //.select("(SELECT e.NAME FROM DG_DATA_ELEM e WHERE e.ID = t.DATA_ELEM_ID) dataElemName")
//                .select("(SELECT s.SENSITIVE_LEVEL FROM DG_SENSITIVE_LEVEL s WHERE s.ID = t.SAFETY_LEVEL_ID) safetyLevelName")
                .eq(McTableDO::getId, id);
        return selectOne(lambdaWrapperX);
    }

    default boolean existsByDataElemIds(Collection<Long> dataElemIds) {
        return exists(Wrappers.lambdaQuery(McColumnDO.class)
                .in(McColumnDO::getDataElemId, dataElemIds));
    }

    default boolean existsByTableIds(Collection<Long> tableIds) {
        return exists(Wrappers.lambdaQuery(McColumnDO.class)
                .in(McColumnDO::getTableId, tableIds));
    }

    default boolean existsByTableId(Long tableId) {
        return exists(Wrappers.lambdaQuery(McColumnDO.class)
                .eq(McColumnDO::getTableId, tableId));
    }

    default List<McColumnDO> findByTableIdAndColumnNameIn(Long tableId, Collection<String> columnNames) {
        return selectList(Wrappers.lambdaQuery(McColumnDO.class)
                .eq(McColumnDO::getTableId, tableId)
                .in(McColumnDO::getColumnName, columnNames));
    }


}
