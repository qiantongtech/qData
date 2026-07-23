package tech.qiantong.qdata.module.mc.dal.mapper.metadata;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McMetaSearchRespDTO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.BizDataScopeQueryHelper;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Database Mapper interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface McDbMapper extends BaseMapperX<McDbDO> {

    default PageResult<McDbDO> selectPage(McDbPageReqVO reqVO) {
        if ("0".equals(reqVO.getSourceSystemName())) {
            reqVO.setSourceSystemName(null);
        }
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        boolean selfScopeWithUnassigned = BizDataScopeQueryHelper.useSelfScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getBusinessLeader());
        boolean deptScopeWithUnassigned = BizDataScopeQueryHelper.useDeptScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getResponsibleDept());
        Set<String> allowedColumns = Sets.newHashSet("id", "create_time", "update_time", "audit_time",
                "table_count", "data_quality");

        MPJLambdaWrapperX<McDbDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(McDbDO.class)
                .select("(SELECT COUNT(*) FROM MC_TABLE o WHERE o.DB_ID = t.ID) table_count");
        lambdaWrapperX.eqIfPresent(McDbDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(McDbDO::getSourceSystemId, reqVO.getSourceSystemId())
                .eqIfPresent(McDbDO::getVersion, reqVO.getVersion())
                .eqIfPresent(McDbDO::getDatasourceId, reqVO.getDatasourceId())
                .eqIfPresent(McDbDO::getIp, reqVO.getIp())
                .eqIfPresent(McDbDO::getPort, reqVO.getPort())
                .eqIfPresent(McDbDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eqIfPresent(McDbDO::getDbType, reqVO.getDbType())
                .likeIfPresent(McDbDO::getDbName, reqVO.getDbName())
                .likeIfPresent(McDbDO::getSchemaName, reqVO.getSchemaName())
                .eqIfPresent(McDbDO::getSafetyLevelId, reqVO.getSafetyLevelId())
                .eqIfPresent(McDbDO::getBelongingLayer, reqVO.getBelongingLayer())
                .eqIfPresent(McDbDO::getBelongingSystem, reqVO.getBelongingSystem())
                .eqIfPresent(McDbDO::getBusinessLeaderPhone, reqVO.getBusinessLeaderPhone())
                .eqIfPresent(McDbDO::getTechLeader, reqVO.getTechLeader())
                .eqIfPresent(McDbDO::getTechLeaderPhone, reqVO.getTechLeaderPhone())
                .eqIfPresent(McDbDO::getStorageSize, reqVO.getStorageSize())
                .eqIfPresent(McDbDO::getDataQuality, reqVO.getDataQuality())
                .eqIfPresent(McDbDO::getAuditStatus, reqVO.getAuditStatus())
                .eqIfPresent(McDbDO::getAuditTime, reqVO.getAuditTime())
                .eqIfPresent(McDbDO::getStatus, reqVO.getStatus())
                .eqIfPresent(McDbDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McDbDO::getDescription, reqVO.getDescription())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);
        lambdaWrapperX.apply(selfScopeWithUnassigned, "(t.BUSINESS_LEADER = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getBusinessLeader());
        lambdaWrapperX.eq(!selfScopeWithUnassigned && reqVO.getBusinessLeader() != null, McDbDO::getBusinessLeader, reqVO.getBusinessLeader());
        lambdaWrapperX.apply(deptScopeWithUnassigned, "(t.RESPONSIBLE_DEPT = {0} OR (t.BUSINESS_LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getResponsibleDept());
        lambdaWrapperX.eq(!deptScopeWithUnassigned && reqVO.getResponsibleDept() != null, McDbDO::getResponsibleDept, reqVO.getResponsibleDept());
        lambdaWrapperX.likeRightIfExists(McDbDO::getSourceSystemName, reqVO.getSourceSystemName());
        // Construct dynamic query conditions
        return selectPage(reqVO, lambdaWrapperX);
    }

    default McDbDO findById(Long id) {
        MPJLambdaWrapperX<McDbDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(McDbDO.class)
                .select("(SELECT COUNT(*) FROM MC_TABLE o WHERE o.DB_ID = t.ID ) tableCount")
                .select("(SELECT COUNT(*) FROM MC_COLUMN c WHERE c.DB_ID = t.ID ) columnCount")
                .select("(SELECT u.NICK_NAME FROM SYSTEM_USER u WHERE u.USER_ID = t.BUSINESS_LEADER) businessLeaderName")
                .select("(SELECT n.NICK_NAME FROM SYSTEM_USER n WHERE n.USER_ID = t.TECH_LEADER) techLeaderName")
//                .select("(SELECT s.SENSITIVE_LEVEL FROM DG_SENSITIVE_LEVEL s WHERE s.ID = t.SAFETY_LEVEL_ID) safetyLevelName")
                .select("(SELECT d.DEPT_NAME FROM SYSTEM_DEPT d WHERE d.DEPT_ID = t.RESPONSIBLE_DEPT) responsibleDeptName")
                .select("t2.NAME AS sourceSystemName")
                .leftJoin("ATT_SOURCE_SYSTEM t2 on t.SOURCE_SYSTEM_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(McDbDO::getId, id);
        return selectOne(lambdaWrapperX);
    }


    default boolean existsBySourceSystemName(String sourceSystemName) {
        return exists(Wrappers.lambdaQuery(McDbDO.class)
                .likeRight(McDbDO::getSourceSystemName, sourceSystemName));
    }


    /**
     * Metadata retrieval paging query
     */
    List<McMetaSearchRespDTO> selectMetaSearchPage(
            @Param("keyword") String keyword,
            @Param("types") List<String> types,     // DB-1 / TABLE-2 / COLUMN-3
            @Param("dbTypes") List<String> dbTypes, // mysql / oracle / dm / kingbase8
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * Total number of metadata searches
     */
    Long selectMetaSearchCount(
            @Param("keyword") String keyword,
            @Param("types") List<String> types,
            @Param("dbTypes") List<String> dbTypes,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );


    /**
     * Update database storage size
     * @param dbId database ID
     * @return update result
     */
    @Update("UPDATE MC_DB a SET a.STORAGE_SIZE = (SELECT SUM(storage_size) FROM MC_TABLE b WHERE b.db_id = a.id AND b.del_flag = 0) WHERE a.id = #{dbId} AND a.del_flag = 0")
    int updateStorageSizeById(@Param("dbId") Long dbId);

    /**
     * Update the number of fields in the metadata table
     * @param dbId database ID
     * @return update result
     */
    @Update("UPDATE MC_TABLE a SET a.column_count = (SELECT COUNT(*) FROM MC_COLUMN b WHERE b.table_id = a.id AND a.db_id = b.db_id and b.del_flag=0) WHERE EXISTS (SELECT 1 FROM MC_COLUMN b WHERE b.table_id = a.id AND a.db_id = b.db_id and b.del_flag=0) AND a.db_id = #{dbId} and a.del_flag=0")
    int updateColumnCountByDbId(@Param("dbId") Long dbId);

    /**
     * Update the number of database data rows
     * @param dbId database ID
     * @return update result
     */
    @Update("UPDATE MC_DB a SET a.data_row_count = (SELECT SUM(row_count) FROM MC_TABLE b WHERE b.db_id = a.id AND b.del_flag = 0) WHERE a.id = #{dbId} AND a.del_flag = 0")
    int updateDataRowCountById(@Param("dbId") Long dbId);

}
