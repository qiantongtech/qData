package tech.qiantong.qdata.module.mc.dal.mapper.task;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.BizDataScopeQueryHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Collection task Mapper interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskMapper extends BaseMapperX<McTaskDO> {

    default PageResult<McTaskDO> selectPage(McTaskPageReqVO reqVO) {
        boolean selfScopeWithUnassigned = BizDataScopeQueryHelper.useSelfScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getLeader());
        boolean deptScopeWithUnassigned = BizDataScopeQueryHelper.useDeptScopeWithUnassigned(
                reqVO.getBizScopeMode(), reqVO.getBizScopeIncludeUnassigned(), reqVO.getResponsibleDept());
        MPJLambdaWrapper<McTaskDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(McTaskDO.class)
                .select("t2.NAME AS sourceSystemName"
                        , "t3.STATUS AS schedulerStatus"
                        ,"t3.CRON_EXPRESSION AS cronExpression"
                        ,"t4.DATASOURCE_NAME AS datasourceName"
                        ,"t4.DATASOURCE_TYPE AS datasourceType"
                        ,"t5.NICK_NAME AS personChargeName"
                )
                .leftJoin("ATT_SOURCE_SYSTEM t2 on t.SOURCE_SYSTEM_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .leftJoin("MC_TASK_SCHEDULER t3 ON t.id = t3.task_id AND t3.DEL_FLAG = '0'")
                .leftJoin("DA_DATASOURCE t4 ON t.datasource_id = t4.id AND t4.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t5 ON t.LEADER = t5.USER_ID AND t5.DEL_FLAG = '0'")
                .eq(reqVO.getId() != null,McTaskDO::getId, reqVO.getId())
                .eq(reqVO.getSourceSystemId() != null,McTaskDO::getSourceSystemId, reqVO.getSourceSystemId())
                .likeRight(StringUtils.isNotBlank(reqVO.getSourceSystemName()), McTaskDO::getSourceSystemName, reqVO.getSourceSystemName())
                .like(StringUtils.isNotEmpty( reqVO.getName()), McTaskDO::getName, reqVO.getName())
                .eq( reqVO.getDatasourceId() != null, McTaskDO::getDatasourceId, reqVO.getDatasourceId())
                .eq(StringUtils.isNotEmpty( reqVO.getDbType()), McTaskDO::getDbType, reqVO.getDbType())
                .eq(StringUtils.isNotEmpty( reqVO.getLeaderPhone()), McTaskDO::getLeaderPhone, reqVO.getLeaderPhone())
                .eq(StringUtils.isNotEmpty( reqVO.getCollectionMode()),McTaskDO::getCollectionMode, reqVO.getCollectionMode())
                .eq(StringUtils.isNotEmpty( reqVO.getCollectionScope()),McTaskDO::getCollectionScope, reqVO.getCollectionScope())
                .eq(StringUtils.isNotEmpty( reqVO.getStatus()),McTaskDO::getStatus, reqVO.getStatus())
                .apply(selfScopeWithUnassigned, "(t.LEADER = {0} OR (t.LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getLeader())
                .eq(!selfScopeWithUnassigned && reqVO.getLeader() != null, McTaskDO::getLeader, reqVO.getLeader())
                .apply(deptScopeWithUnassigned, "(t.RESPONSIBLE_DEPT = {0} OR (t.LEADER IS NULL AND t.RESPONSIBLE_DEPT IS NULL))", reqVO.getResponsibleDept())
                .eq(!deptScopeWithUnassigned && reqVO.getResponsibleDept() != null, McTaskDO::getResponsibleDept, reqVO.getResponsibleDept())
                .ge(reqVO.getCreateTimeStart() != null,
                        McTaskDO::getCreateTime, reqVO.getCreateTimeStart())
                .le(reqVO.getCreateTimeEnd() != null,
                        McTaskDO::getCreateTime, reqVO.getCreateTimeEnd())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, McTaskDO.class, lambdaWrapper);
    }


    default boolean existsBySourceSystemName(String sourceSystemName) {
        return exists(Wrappers.lambdaQuery(McTaskDO.class)
                .eq(McTaskDO::getDelFlag, "0")
                .likeRight(McTaskDO::getSourceSystemName, sourceSystemName));
    }

    default boolean existsBydataSourceID(Long datasourceId) {
        return exists(Wrappers.lambdaQuery(McTaskDO.class)
                .eq(McTaskDO::getDelFlag, "0")
                .eq(McTaskDO::getDatasourceId, datasourceId));
    }

    /**
     * Check whether there is a task for the specified data source (exclude the specified task ID)
     *
     * @param datasourceId data source ID
     * @param excludeTaskId Excluded task ID (used to exclude itself when updating)
     * @return does it exist
     */
    default boolean existsByDatasourceId(Long datasourceId, Long excludeTaskId) {
        return exists(Wrappers.lambdaQuery(McTaskDO.class)
                .eq(McTaskDO::getDelFlag, "0")
                .eq(McTaskDO::getDatasourceId, datasourceId)
                .ne(excludeTaskId != null, McTaskDO::getId, excludeTaskId));
    }

    /**
     * Check whether there is a task with the specified data source and collection range (exclude the specified task ID)
     *
     * @param datasourceId data source ID
     * @param collectionScope collection range
     * @param excludeTaskId Excluded task ID (used to exclude itself when updating)
     * @return does it exist
     */
    default boolean existsByDatasourceAndScope(Long datasourceId, String collectionScope, Long excludeTaskId) {
        return exists(Wrappers.lambdaQuery(McTaskDO.class)
                .eq(McTaskDO::getDelFlag, "0")
                .eq(McTaskDO::getDatasourceId, datasourceId)
                .eq(McTaskDO::getCollectionScope, collectionScope)
                .ne(excludeTaskId != null, McTaskDO::getId, excludeTaskId));
    }

    /**
     * Query the task list of the specified data source and collection range (exclude the specified task ID)
     *
     * @param datasourceId data source ID
     * @param collectionScope collection range
     * @param excludeTaskId Excluded task ID (used to exclude itself when updating)
     * @return task list
     */
    default List<McTaskDO> selectByDatasourceAndScope(Long datasourceId, String collectionScope, Long excludeTaskId) {
        return selectList(Wrappers.lambdaQuery(McTaskDO.class)
                .eq(McTaskDO::getDelFlag, "0")
                .eq(McTaskDO::getDatasourceId, datasourceId)
                .eq(McTaskDO::getCollectionScope, collectionScope)
                .ne(excludeTaskId != null, McTaskDO::getId, excludeTaskId));
    }

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from MC_TASK where del_flag = '0' and id = #{taskId}")
    McTaskDO getByTaskId(Long taskId);
}
