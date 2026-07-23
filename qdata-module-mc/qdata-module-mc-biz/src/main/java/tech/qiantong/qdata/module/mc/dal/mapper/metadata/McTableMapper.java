package tech.qiantong.qdata.module.mc.dal.mapper.metadata;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTablePageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.BizDataScopeQueryHelper;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Metadata information Mapper interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface McTableMapper extends BaseMapperX<McTableDO> {

    default PageResult<McTableDO> selectPage(McTablePageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        boolean selfScopeWithUnassigned = BizDataScopeQueryHelper.useSelfScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getBusinessLeader());
        boolean deptScopeWithUnassigned = BizDataScopeQueryHelper.useDeptScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getResponsibleDept());
        Set<String> allowedColumns = Sets.newHashSet("id", "create_time", "update_time", "audit_time", "data_quality");
        MPJLambdaWrapperX<McTableDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(McTableDO.class)
                .select("d.source_system_id",
                        "d.source_system_name",
                        "(CASE WHEN da.ID IS NULL THEN '0' ELSE '1' END) AS dssetFlag"
                        , "t4.DATASOURCE_NAME AS datasourceName"
                        , "t4.DATASOURCE_TYPE AS datasourceType"
                        , "u.PHONENUMBER AS createPhoneNumber"
                        , "u2.PHONENUMBER AS updatePhoneNumber"
                )
                .leftJoin("MC_DB d ON t.DB_ID=d.id")
                .leftJoin("SYSTEM_USER u on t.CREATOR_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER u2 on t.UPDATER_ID = u2.USER_ID AND u2.DEL_FLAG = '0'")
                .leftJoin("DA_ASSET da ON da.TABLE_ID = t.ID AND da.DEL_FLAG = '0'")
                .leftJoin("DA_DATASOURCE t4 ON t.datasource_id = t4.id AND t4.DEL_FLAG = '0'");
        lambdaWrapperX.eqIfPresent(McTableDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McTableDO::getDbId, reqVO.getDbId())
                .eqIfPresent(McTableDO::getDatasourceId, reqVO.getDatasourceId())
                .eqIfPresent(McTableDO::getVersion, reqVO.getVersion())
                .likeIfPresent(McTableDO::getTableName, reqVO.getTableName())
                .likeIfPresent(McTableDO::getTableComment, reqVO.getTableComment())
                .eqIfPresent(McTableDO::getSafetyLevelId, reqVO.getSafetyLevelId())
                .likeIfPresent(McTableDO::getDbName, reqVO.getDbName())
                .likeIfPresent(McTableDO::getSchemaName, reqVO.getSchemaName())
                .eqIfPresent(McTableDO::getStorageType, reqVO.getStorageType())
                .eqIfPresent(McTableDO::getStorageSize, reqVO.getStorageSize())
                .eqIfPresent(McTableDO::getBusinessLeaderPhone, reqVO.getBusinessLeaderPhone())
                .eqIfPresent(McTableDO::getTechLeader, reqVO.getTechLeader())
                .eqIfPresent(McTableDO::getTechLeaderPhone, reqVO.getTechLeaderPhone())
                .eqIfPresent(McTableDO::getMasterFlag, reqVO.getMasterFlag())
                .eqIfPresent(McTableDO::getTempFlag, reqVO.getTempFlag())
                .eqIfPresent(McTableDO::getDataQuality, reqVO.getDataQuality())
                .eqIfPresent(McTableDO::getAuditStatus, reqVO.getAuditStatus())
                .eqIfPresent(McTableDO::getAuditTime, reqVO.getAuditTime())
                .eqIfPresent(McTableDO::getStatus, reqVO.getStatus())
                .eqIfPresent(McTableDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McTableDO::getDescription, reqVO.getDescription())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);
        lambdaWrapperX.and(StringUtils.isNotBlank(reqVO.getKeyWord()), wrapper ->
                wrapper.like(McTableDO::getTableName, reqVO.getKeyWord())
                        .or()
                        .like(McTableDO::getTableComment, reqVO.getKeyWord()));
        lambdaWrapperX.notIn(StringUtils.isNotBlank(reqVO.getHideTableIds()),McTableDO::getId, reqVO.getHideTableIds() != null ? Arrays.asList(reqVO.getHideTableIds().split(",")) : null);
        lambdaWrapperX.apply(selfScopeWithUnassigned, "(t.BUSINESS_LEADER = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getBusinessLeader());
        lambdaWrapperX.eq(!selfScopeWithUnassigned && reqVO.getBusinessLeader() != null, McTableDO::getBusinessLeader, reqVO.getBusinessLeader());
        lambdaWrapperX.apply(deptScopeWithUnassigned, "(t.RESPONSIBLE_DEPT = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getResponsibleDept());
        lambdaWrapperX.eq(!deptScopeWithUnassigned && reqVO.getResponsibleDept() != null, McTableDO::getResponsibleDept, reqVO.getResponsibleDept());
        if (!"0".equals(reqVO.getSourceSystemId())) {
            lambdaWrapperX.likeRightIfExists("d", McDbDO::getSourceSystemId, reqVO.getSourceSystemId());
        }
        // Construct dynamic query conditions
        return selectPage(reqVO, lambdaWrapperX);
    }

    default PageResult<McTableDO> getMcTablelist(McTablePageReqVO reqVO){
        boolean selfScopeWithUnassigned = BizDataScopeQueryHelper.useSelfScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getBusinessLeader());
        boolean deptScopeWithUnassigned = BizDataScopeQueryHelper.useDeptScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getResponsibleDept());
        MPJLambdaWrapperX<McTableDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(McTableDO.class)
                .select("t2.CRON_EXPRESSION as cronExpression")
                .leftJoin("MC_TASK_SCHEDULER t2 on t.TASK_ID= t2.TASK_ID and t2.DEL_FLAG = '0'");
        lambdaWrapperX
                .eq(reqVO.getTaskId() != null , McTableDO::getTaskId, reqVO.getTaskId())
                .apply(selfScopeWithUnassigned, "(t.BUSINESS_LEADER = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getBusinessLeader())
                .eq(!selfScopeWithUnassigned && reqVO.getBusinessLeader() != null, McTableDO::getBusinessLeader, reqVO.getBusinessLeader())
                .apply(deptScopeWithUnassigned, "(t.RESPONSIBLE_DEPT = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getResponsibleDept())
                .eq(!deptScopeWithUnassigned && reqVO.getResponsibleDept() != null, McTableDO::getResponsibleDept, reqVO.getResponsibleDept())
                .in(CollectionUtils.isNotEmpty(reqVO.getDbIdList()),McTableDO::getDbId, reqVO.getDbIdList())
                .and(StringUtils.isNotBlank(reqVO.getKeyWord()), wrapper ->
                        wrapper.like(McTableDO::getTableName, reqVO.getKeyWord())
                                .or()
                                .like(McTableDO::getTableComment, reqVO.getKeyWord()))
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // Construct dynamic query conditions
        return selectPage(reqVO, lambdaWrapperX);
    }
    default List<McTableDO> getMcTableListAsset(McTablePageReqVO reqVO){
        boolean selfScopeWithUnassigned = BizDataScopeQueryHelper.useSelfScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getBusinessLeader());
        boolean deptScopeWithUnassigned = BizDataScopeQueryHelper.useDeptScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getResponsibleDept());
        MPJLambdaWrapperX<McTableDO> lambdaWrapperX = new MPJLambdaWrapperX<>();

        lambdaWrapperX.selectAll(McTableDO.class)
                .select("t2.CRON_EXPRESSION as cronExpression")
                .select("COALESCE(c1.columnCount, 0) as columnCount")
                .leftJoin("MC_TASK_SCHEDULER t2 on t.TASK_ID = t2.TASK_ID and t2.DEL_FLAG = '0'")
                .leftJoin("(SELECT TABLE_ID, COUNT(1) AS columnCount FROM MC_COLUMN WHERE DEL_FLAG = '0' GROUP BY TABLE_ID) c1 on c1.TABLE_ID = t.ID");

        lambdaWrapperX
                .eq(reqVO.getTaskId() != null, McTableDO::getTaskId, reqVO.getTaskId())
                .apply(selfScopeWithUnassigned, "(t.BUSINESS_LEADER = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getBusinessLeader())
                .eq(!selfScopeWithUnassigned && reqVO.getBusinessLeader() != null, McTableDO::getBusinessLeader, reqVO.getBusinessLeader())
                .apply(deptScopeWithUnassigned, "(t.RESPONSIBLE_DEPT = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getResponsibleDept())
                .eq(!deptScopeWithUnassigned && reqVO.getResponsibleDept() != null, McTableDO::getResponsibleDept, reqVO.getResponsibleDept())
                .eq(StringUtils.isNotBlank(reqVO.getPortalVisible()), McTableDO::getPortalVisible, reqVO.getPortalVisible())
                .in(CollectionUtils.isNotEmpty(reqVO.getDbIdList()), McTableDO::getDbId, reqVO.getDbIdList())
                .and(StringUtils.isNotBlank(reqVO.getKeyWord()), wrapper ->
                        wrapper.like(McTableDO::getTableName, reqVO.getKeyWord())
                                .or()
                                .like(McTableDO::getTableComment, reqVO.getKeyWord()))
                .orderByStr(
                        StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn())
                                ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                : null
                );

        return selectList(lambdaWrapperX);
    }

    default McTableDO findById(Long id) {
        MPJLambdaWrapperX<McTableDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(McTableDO.class)
                .select("(SELECT u.NICK_NAME FROM SYSTEM_USER u WHERE u.USER_ID = t.BUSINESS_LEADER) businessLeaderName")
                .select("(SELECT n.NICK_NAME FROM SYSTEM_USER n WHERE n.USER_ID = t.TECH_LEADER) techLeaderName")
//                .select("(SELECT s.SENSITIVE_LEVEL FROM DG_SENSITIVE_LEVEL s WHERE s.ID = t.SAFETY_LEVEL_ID) safetyLevelName")
                .eq(McTableDO::getId, id);
        return selectOne(lambdaWrapperX);
    }


    default boolean existsByDbId(Long dbId) {
        return exists(Wrappers.lambdaQuery(McTableDO.class)
                .eq(McTableDO::getDbId, dbId));
    }

    default boolean existsByDbIds(Collection<Long> dbIds) {
        return exists(Wrappers.lambdaQuery(McTableDO.class)
                .in(McTableDO::getDbId, dbIds));
    }
}
